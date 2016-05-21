package com.studio.query.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.common.Constants;
import com.studio.query.dao.VariableDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Fragment;
import com.studio.query.entity.Scene;
import com.studio.query.entity.ShareVariable;
import com.studio.query.entity.Variable;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.DateUtil;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class VariableService {
	@Autowired
	public VariableDao variableDao;

	public List<Variable> findVariable(Variable variable) {
		return variableDao.findVariable(variable);
	}

	public int insertVariable(Variable variable) {
		return variableDao.insertVariable(variable);
	}

	public int updateVariable(Variable variable) {
		return variableDao.updateVariable(variable);
	}

	public String createVariable(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			int variableScope = parmJb.optInt("variableScope", 0);// 0=scenario表示全局变量，1=fragment表示fragment内的局部变量
			String variableName = parmJb.optString("variableName", "");
			String variableType = parmJb.optString("variableType", "");
			String variableObjType = parmJb.optString("variableObjType", "");
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			String variableDesc = parmJb.optString("variableDesc", "");
			if (StringUtil.isNullOrEmpty(variableName)
					|| (variableScope == 1 && StringUtil.isNullOrEmpty(fragmentUUID))) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			// 变量重名验证暂时不启用
			// Variable findVariable = new Variable();
			// findVariable.setVariableName(variableName);
			// List<Variable> variableList =
			// variableDao.findVariable(findVariable);
			// if (variableList.size() >= 1) {
			// resultString =
			// StringUtil.packetObject(MethodCode.CREATE_VARIABLE,
			// ParameterCode.Result.RESULT_FAIL,
			// ParameterCode.Error.CREATE_VARIABLE_EXIST, "变量已经存在", "");
			// return resultString;
			// }

			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}
			Variable insertVariable = new Variable();
			insertVariable.setSceneId(sceneActive.getSceneId());
			insertVariable.setVariableUUID(StringUtil.createVariableUUID());
			insertVariable.setVariableName(variableName);
			insertVariable.setVariableScope(variableScope);
			insertVariable.setFragmentUUID(fragmentUUID);
			insertVariable.setVariableType(variableType);
			insertVariable.setVariableObjType(variableObjType);
			insertVariable.setVariableDesc(variableDesc);

			List<Variable> sessionVariableList = (List<Variable>) session.get(Constants.KEY_VARIABLE_ADD);
			if (sessionVariableList == null || sessionVariableList.size() == 0) {
				sessionVariableList = new ArrayList<Variable>();
			}
			sessionVariableList.add(insertVariable);
			session.put(Constants.KEY_VARIABLE_ADD, sessionVariableList);

			JSONObject variableJsonObject = new JSONObject();
			variableJsonObject.put("variableUUID", insertVariable.getVariableUUID());
			resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
					"创建变量到缓存成功，请注意在切换场景前保存场景数据。", variableJsonObject.toString());

		}
		return resultString;
	}

	public String updateVariable(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String variableUUID = parmJb.optString("variableUUID", "");
			String variableName = parmJb.optString("variableName", "");
			int variableScope = parmJb.optInt("variableScope", -1);
			String variableType = parmJb.optString("variableType", "");
			String variableObjType = parmJb.optString("variableObjType", "");
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			String variableDesc = parmJb.optString("variableDesc", "");

			if (StringUtil.isNullOrEmpty(variableUUID)
					|| (variableScope == 1 && StringUtil.isNullOrEmpty(fragmentUUID))) {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Variable findvVariable = new Variable();
			findvVariable.setVariableUUID(variableUUID);
			List<Variable> variableList = variableDao.findVariable(findvVariable);
			if (variableList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
				return resultString;
			}

			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}
			Variable updateVariable = variableList.get(0);
			if (!StringUtil.isNullOrEmpty(variableName)) {
				updateVariable.setVariableName(variableName);
			}
			if (variableScope != -1) {
				updateVariable.setVariableScope(variableScope);
			}
			if (!StringUtil.isNullOrEmpty(variableType)) {
				updateVariable.setVariableType(variableType);
			}
			if (!StringUtil.isNullOrEmpty(variableObjType)) {
				updateVariable.setVariableObjType(variableObjType);
			}
			if (!StringUtil.isNullOrEmpty(fragmentUUID)) {
				updateVariable.setFragmentUUID(fragmentUUID);
			}
			if (!StringUtil.isNullOrEmpty(variableDesc)) {
				updateVariable.setVariableDesc(variableDesc);
			}
			List<Variable> sessionVariableList = (List<Variable>) session.get(Constants.KEY_VARIABLE_UPDATE);
			if (sessionVariableList == null || sessionVariableList.size() == 0) {
				sessionVariableList = new ArrayList<Variable>();
			}
			sessionVariableList.add(updateVariable);
			session.put(Constants.KEY_VARIABLE_UPDATE, sessionVariableList);

			resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
					"更新变量到缓存成功，请注意在切换场景前保存场景数据。", "");

		}
		return resultString;
	}

	public String getVariable(String bodyString, Account currentAccount, Map<String, Object> session) {
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentVersion = parmJb.optString("version", "");
			if (StringUtil.isNullOrEmpty(fragmentVersion)) {

				resultString = StringUtil.packetObject(MethodCode.GET_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			// 这里根据version解析出相对应的variable

			JSONObject variableObj = new JSONObject();
			variableObj.put("id", "VAR3055aa1577e24bbc8835c441669976c6");
			variableObj.put("name", "testVariable");
			variableObj.put("desc", "");
			variableObj.put("scope", 0);
			variableObj.put("isShare", 0);
			variableObj.put("version", "");
			variableObj.put("createBy", "test2");
			variableObj.put("createTime", "2016-04-25 00:12:45");
			variableObj.put("expression", "");// expression直接从当前version解析提取

			resultString = StringUtil.packetObject(MethodCode.GET_VARIABLE, ParameterCode.Result.RESULT_OK, "",
					"获取某个版本variable成功", variableObj.toString());

		}
		return resultString;
	}

	public String deleteVariable(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String variableUUID = parmJb.optString("variableUUID", "");
			if (StringUtil.isNullOrEmpty(variableUUID)) {

				resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Variable findVariable = new Variable();
			findVariable.setVariableUUID(variableUUID);
			List<Variable> variableList = variableDao.findVariable(findVariable);
			if (variableList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}
			Variable deleteVariable = variableList.get(0);
			List<Variable> sessionVariableList = (List<Variable>) session.get(Constants.KEY_VARIABLE_DELETE);
			if (sessionVariableList == null || sessionVariableList.size() == 0) {
				sessionVariableList = new ArrayList<Variable>();
			}
			sessionVariableList.add(deleteVariable);
			session.put(Constants.KEY_VARIABLE_DELETE, sessionVariableList);

			resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
					"删除变量到缓存成功，请注意在切换场景前保存场景数据。", "");
		}
		return resultString;
	}

	// public String disableShareVariable(String bodyString, Account
	// currentAccount) {
	//
	// String resultString = null;
	// JSONObject jb = JSONObject.fromObject(bodyString);
	// JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
	// if (parmJb != null) {
	// String variableUUID = parmJb.optString("variableUUID", "");
	// if (StringUtil.isNullOrEmpty(variableUUID)) {
	//
	// resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_VARIABLE,
	// ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER,
	// "必要参数不足", "");
	// return resultString;
	// }
	// Variable updateVariable = new Variable();
	// updateVariable.setAccountId(currentAccount.getAccountId());
	// updateVariable.setVariableUUID(variableUUID);
	// int result = variableDao.disableShareVariable(updateVariable);
	// if (result < 1) {
	// resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_VARIABLE,
	// ParameterCode.Result.RESULT_FAIL,
	// ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
	// return resultString;
	// } else {
	//
	// resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_VARIABLE,
	// ParameterCode.Result.RESULT_OK, "", "禁用共享变量成功", "");
	// }
	// }
	// return resultString;
	// }
	//
	// public String enableShareVariable(String bodyString, Account
	// currentAccount) {
	//
	// String resultString = null;
	// JSONObject jb = JSONObject.fromObject(bodyString);
	// JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
	// if (parmJb != null) {
	// String variableUUID = parmJb.optString("variableUUID", "");
	// if (StringUtil.isNullOrEmpty(variableUUID)) {
	//
	// resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_VARIABLE,
	// ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER,
	// "必要参数不足", "");
	// return resultString;
	// }
	// Variable updateVariable = new Variable();
	// updateVariable.setAccountId(currentAccount.getAccountId());
	// updateVariable.setVariableUUID(variableUUID);
	// int result = variableDao.enableShareVariable(updateVariable);
	// if (result < 1) {
	// resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_VARIABLE,
	// ParameterCode.Result.RESULT_FAIL,
	// ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的变量不存在", "");
	// return resultString;
	// } else {
	//
	// resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_VARIABLE,
	// ParameterCode.Result.RESULT_OK,
	// "", "启用共享变量成功", "");
	// }
	// }
	// return resultString;
	// }

	public String getVariables(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
		// 如果session中没有记录当前场景
		if (sceneActive == null) {
			resultString = StringUtil.packetObject(MethodCode.LIST_VARIABLE, ParameterCode.Result.RESULT_FAIL,
					ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
			return resultString;
		}
		JSONArray variableJsonArray = new JSONArray();
		Variable findVariable = new Variable();
		findVariable.setSceneId(sceneActive.getSceneId());
		List<Variable> variableList = variableDao.findVariable(findVariable);
		for (int i = 0; i < variableList.size(); i++) {

			Variable variable = variableList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("variableUUID", variable.getVariableUUID());
			dataObj.put("variableName", variable.getVariableName());
			dataObj.put("variableType", variable.getVariableType());
			dataObj.put("variableObjType", variable.getVariableObjType());
			dataObj.put("variableScope", variable.getVariableScope());
			dataObj.put("fragmentUUID", variable.getFragmentUUID());
			dataObj.put("variableDesc", variable.getVariableDesc());
			dataObj.put("variableCreateTime", DateUtil.dateTimeFormat(variable.getVariableDate()));

			variableJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_VARIABLE, ParameterCode.Result.RESULT_OK, "", "获取变量列表成功",
				variableJsonArray.toString());

		return resultString;
	}

	public String getShareVariables(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONArray variableJsonArray = new JSONArray();
		ShareVariable findShareVariable = new ShareVariable();
		List<ShareVariable> shareVariableList = variableDao.findShareVariable(findShareVariable);
		for (int i = 0; i < shareVariableList.size(); i++) {

			ShareVariable shareVariable = shareVariableList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("shareVariableUUID", shareVariable.getShareVariableUUID());
			dataObj.put("shareVariableName", shareVariable.getShareVariableName());
			dataObj.put("shareVariableType", shareVariable.getShareVariableType());
			dataObj.put("shareVariableObjType", shareVariable.getShareVariableObjType());
			dataObj.put("shareVariableScope", shareVariable.getShareVariableScope());
			dataObj.put("fragmentUUID", shareVariable.getFragmentUUID());
			dataObj.put("shareVariableDesc", shareVariable.getShareVariableDesc());
			dataObj.put("shareVariableCreatedBy", shareVariable.getAccountName());
			dataObj.put("shareVariableCreateTime", DateUtil.dateTimeFormat(shareVariable.getShareVariableDate()));

			variableJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_SHARE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
				"获取分享变量列表成功", variableJsonArray.toString());

		return resultString;
	}
}
