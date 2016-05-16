package com.studio.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.dao.VariableDao;
import com.studio.query.entity.Account;
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

	public String createVariable(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			int variableScope = parmJb.optInt("variableScope", 0);// 0=scenario表示全局变量，1=fragment表示fragment内的局部变量
			String variableName = parmJb.optString("variableName", "");
			String variableDesc = parmJb.optString("variableDesc", "");
			int variableShare = parmJb.optInt("variableShare", 0);// 是否共享变量
			String variableExpression = parmJb.optString("variableExpression", "");// git保存expression
			if (StringUtil.isNullOrEmpty(variableName)) {

				resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Variable findVariable = new Variable();
			findVariable.setVariableName(variableName);
			List<Variable> variableList = variableDao.findVariable(findVariable);
			if (variableList.size() >= 1) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.CREATE_VARIABLE_EXIST, "变量已经存在", "");
				return resultString;
			}

			Variable insertVariable = new Variable();
			insertVariable.setAccountId(currentAccount.getAccountId());
			insertVariable.setVariableUUID(StringUtil.createVariableUUID());
			insertVariable.setVariableName(variableName);
			insertVariable.setVariableScope(variableScope);
			insertVariable.setVariableShare(variableShare);
			insertVariable.setVariableDesc(variableDesc);
			int insertResult = variableDao.insertVariable(insertVariable);
			if (insertResult == 1) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
						"创建变量成功", "");
			} else {

				resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.CREATE_VARIABLE_FAIL, "创建变量失败", "");
			}
		}
		return resultString;
	}

	public String updateVariable(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String variableUUID = parmJb.optString("variableUUID", "");
			String variableScope = parmJb.optString("variableScope", "");
			int variableShare = parmJb.optInt("variableShare", 0);
			String variableDesc = parmJb.optString("variableDesc", "");
			String variableExpression = parmJb.optString("variableExpression", "");// git保存expression
			if (StringUtil.isNullOrEmpty(variableUUID)) {

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

			Variable updateVariable = new Variable();
			updateVariable.setAccountId(currentAccount.getAccountId());
			updateVariable.setVariableUUID(variableUUID);
			updateVariable.setVariableDesc(variableDesc);
			int insertResult = variableDao.updateVariable(updateVariable);
			if (insertResult == 1) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
						"更新变量成功", "");
			} else {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_VARIABLE_FAIL, "更新变量失败", "");
			}
		}
		return resultString;
	}

	public String getVariable(String bodyString, Account currentAccount) {
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

	public String deleteVariable(String bodyString, Account currentAccount) {

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
			findVariable.setAccountId(currentAccount.getAccountId());
			findVariable.setVariableUUID(variableUUID);
			int result = variableDao.deleteVariable(findVariable);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "变量不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
						"删除变量成功", "");
			}
		}
		return resultString;
	}

	public String disableShareVariable(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String variableUUID = parmJb.optString("variableUUID", "");
			if (StringUtil.isNullOrEmpty(variableUUID)) {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_VARIABLE,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Variable updateVariable = new Variable();
			updateVariable.setAccountId(currentAccount.getAccountId());
			updateVariable.setVariableUUID(variableUUID);
			int result = variableDao.disableShareVariable(updateVariable);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_VARIABLE,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_VARIABLE,
						ParameterCode.Result.RESULT_OK, "", "禁用共享变量成功", "");
			}
		}
		return resultString;
	}

	public String enableShareVariable(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String variableUUID = parmJb.optString("variableUUID", "");
			if (StringUtil.isNullOrEmpty(variableUUID)) {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_VARIABLE,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Variable updateVariable = new Variable();
			updateVariable.setAccountId(currentAccount.getAccountId());
			updateVariable.setVariableUUID(variableUUID);
			int result = variableDao.enableShareVariable(updateVariable);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_VARIABLE,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的变量不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_VARIABLE, ParameterCode.Result.RESULT_OK,
						"", "启用共享变量成功", "");
			}
		}
		return resultString;
	}

	public String getVariables(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONArray variableJsonArray = new JSONArray();
		Variable findVariable = new Variable();
		findVariable.setAccountId(currentAccount.getAccountId());
		List<Variable> variableList = variableDao.findVariable(findVariable);
		for (int i = 0; i < variableList.size(); i++) {

			Variable variable = variableList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("variableUUID", variable.getVariableUUID());
			dataObj.put("variableName", variable.getVariableName());
			dataObj.put("variableDesc", variable.getVariableDesc());
			dataObj.put("variableScope", variable.getVariableScope());
			dataObj.put("variableShare", variable.getVariableShare());
			dataObj.put("variableCreatedBy", currentAccount.getAccountName());
			dataObj.put("variableCreateTime", DateUtil.dateTimeFormat(variable.getVariableDate()));
			dataObj.put("variableexpression", "");

			variableJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_VARIABLE, ParameterCode.Result.RESULT_OK, "", "获取变量列表成功",
				variableJsonArray.toString());

		return resultString;
	}

	public String getShareVariables(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONArray variableJsonArray = new JSONArray();
		Variable findVariable = new Variable();
		findVariable.setVariableShare(1);// 0不共享，1共享
		List<Variable> variableList = variableDao.shareVariables(findVariable);
		for (int i = 0; i < variableList.size(); i++) {

			Variable variable = variableList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("id", variable.getVariableUUID());
			dataObj.put("name", variable.getVariableName());
			dataObj.put("desc", variable.getVariableDesc());
			dataObj.put("scope", variable.getVariableScope());
			dataObj.put("share", variable.getVariableShare());
			dataObj.put("createdBy", variable.getAccountName());
			dataObj.put("createTime", DateUtil.dateTimeFormat(variable.getVariableDate()));
			dataObj.put("expression", "");

			variableJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_SHARE_VARIABLE, ParameterCode.Result.RESULT_OK, "",
				"获取分享变量列表成功", variableJsonArray.toString());

		return resultString;
	}
}
