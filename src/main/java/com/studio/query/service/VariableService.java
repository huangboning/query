package com.studio.query.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.common.Constants;
import com.studio.query.dao.VariableDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Committer;
import com.studio.query.entity.Fragment;
import com.studio.query.entity.Scene;
import com.studio.query.entity.ShareFragment;
import com.studio.query.entity.ShareVariable;
import com.studio.query.entity.Variable;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.CacheUtil;
import com.studio.query.util.DateUtil;
import com.studio.query.util.FileUtil;
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
			String fragmentId;
			String variableName;
			String variableType;
			String variableScope;
			String variableFieldType;
			String variableValueType;
			String variableValue;
			if (Configure.serverVersion == 0) {
				fragmentId = parmJb.optString("fragmentId", "");
				variableName = parmJb.optString("name", "");
				variableType = parmJb.optString("variableType", "");
				variableScope = parmJb.optString("variableScope", "");
				variableFieldType = parmJb.optString("fieldType", "");
				variableValueType = parmJb.optString("valueType", "");
				variableValue = parmJb.optString("value", "");
			} else {
				fragmentId = parmJb.optString("fragmentId", "");
				variableName = parmJb.optString("variableName", "");
				variableType = parmJb.optString("variableType", "");
				variableScope = parmJb.optString("variableScope", "");
				variableFieldType = parmJb.optString("variableFieldType", "");
				variableValueType = parmJb.optString("variableValueType", "");
				variableValue = parmJb.optString("variableValue", "");
			}
			if (StringUtil.isNullOrEmpty(variableName) || StringUtil.isNullOrEmpty(variableScope)
					|| StringUtil.isNullOrEmpty(variableValue)) {

				resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}

			Variable insertVariable = new Variable();
			insertVariable.setFragmentUUID(fragmentId);
			insertVariable.setSceneUUID(sceneActive.getSceneUUID());
			insertVariable.setVariableUUID(StringUtil.createVariableUUID());
			insertVariable.setVariableClassId("");
			insertVariable.setVariableName(variableName);
			insertVariable.setVariableType(variableType);
			insertVariable.setVariableScope(variableScope);
			insertVariable.setVariableFieldType(variableFieldType);
			insertVariable.setVariableValueType(variableValueType);
			insertVariable.setVariableValue(variableValue);
			insertVariable.setVariableDateStr(DateUtil.dateTimeFormat(new Date()));

			// 将变量保存到缓存中
			List<Variable> sessionVariableArray = (ArrayList<Variable>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
			if (sessionVariableArray == null) {
				sessionVariableArray = new ArrayList<Variable>();
			}
			sessionVariableArray.add(insertVariable);
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR, sessionVariableArray);

			JSONObject variableJsonObject = new JSONObject();

			variableJsonObject.put("variableClassId", insertVariable.getVariableClassId());
			variableJsonObject.put("variableInstanceId", insertVariable.getVariableUUID());
			variableJsonObject.put("name", insertVariable.getVariableName());
			variableJsonObject.put("variableType", insertVariable.getVariableType());
			JSONObject belongObj = new JSONObject();
			belongObj.put("fragmentId", insertVariable.getFragmentUUID());
			belongObj.put("scenarioId", insertVariable.getSceneUUID());
			variableJsonObject.put("beLongsTo", belongObj);
			variableJsonObject.put("valueType", insertVariable.getVariableValueType());
			variableJsonObject.put("fieldType", insertVariable.getVariableFieldType());
			variableJsonObject.put("value", insertVariable.getVariableValue());
			variableJsonObject.put("variableScope", insertVariable.getVariableScope());

			resultString = StringUtil.packetObject(MethodCode.CREATE_VARIABLE, ParameterCode.Result.RESULT_OK,
					"创建变量到缓存成功，请注意在切换场景前保存场景数据。", variableJsonObject.toString());

		}
		return resultString;
	}

	public String updateVariable(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String variableInstanceId = parmJb.optString("variableInstanceId", "");
			String variableName = parmJb.optString("variableName", "");
			String variableValueType = parmJb.optString("valueType", "");
			String variableValue = parmJb.optString("value", "");

			if (StringUtil.isNullOrEmpty(variableInstanceId)) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			List<Variable> variableList = (List<Variable>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
			if (variableList == null) {
				variableList = new ArrayList<Variable>();
			}
			JSONObject variableObj = new JSONObject();
			for (int i = 0; i < variableList.size(); i++) {

				Variable variable = variableList.get(i);
				// git库存的UUID=classId，instanceId为引用模板的时候生成的id
				if (variable.getVariableUUID().equals(variableInstanceId)) {
					if (!StringUtil.isNullOrEmpty(variableName)) {
						variable.setVariableName(variableName);
					}
					if (!StringUtil.isNullOrEmpty(variableValueType)) {
						variable.setVariableValueType(variableValueType);
					}
					if (!StringUtil.isNullOrEmpty(variableValue)) {
						variable.setVariableValue(variableValue);
					}
					break;
				}
			}

			// 将fragment更新到缓存中
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR, variableList);

			resultString = StringUtil.packetObject(MethodCode.UPDATE_VARIABLE, ParameterCode.Result.RESULT_OK,
					"更新变量到缓存成功，请注意在切换场景前保存场景数据。", "");
		}
		return resultString;
	}

	public String getVariable(String bodyString, Account currentAccount, Map<String, Object> session) {
		String resultString = null;
		// JSONObject jb = JSONObject.fromObject(bodyString);
		// JSONObject parmJb = JSONObject.fromObject(jb.optString("params",
		// ""));
		// if (parmJb != null) {
		// String fragmentVersion = parmJb.optString("version", "");
		// if (StringUtil.isNullOrEmpty(fragmentVersion)) {
		//
		// resultString = StringUtil.packetObject(MethodCode.GET_VARIABLE,
		// ParameterCode.Error.SERVICE_PARAMETER,
		// "必要参数不足", "");
		// return resultString;
		// }
		// // 这里根据version解析出相对应的variable
		//
		// JSONObject variableObj = new JSONObject();
		// variableObj.put("id", "VAR3055aa1577e24bbc8835c441669976c6");
		// variableObj.put("name", "testVariable");
		// variableObj.put("desc", "");
		// variableObj.put("scope", 0);
		// variableObj.put("isShare", 0);
		// variableObj.put("version", "");
		// variableObj.put("createBy", "test2");
		// variableObj.put("createTime", "2016-04-25 00:12:45");
		// variableObj.put("expression", "");// expression直接从当前version解析提取
		//
		// resultString = StringUtil.packetObject(MethodCode.GET_VARIABLE,
		// ParameterCode.Result.RESULT_OK,
		// "获取某个版本variable成功", variableObj.toString());
		//
		// }
		return resultString;
	}

	public String deleteVariable(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		// JSONObject jb = JSONObject.fromObject(bodyString);
		// JSONObject parmJb = JSONObject.fromObject(jb.optString("params",
		// ""));
		// if (parmJb != null) {
		// String variableUUID = parmJb.optString("variableUUID", "");
		// if (StringUtil.isNullOrEmpty(variableUUID)) {
		//
		// resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE,
		// ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
		// return resultString;
		// }
		// Variable findVariable = new Variable();
		// findVariable.setVariableUUID(variableUUID);
		// List<Variable> variableList = variableDao.findVariable(findVariable);
		// if (variableList.size() != 1) {
		// resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE,
		// ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
		// return resultString;
		// }
		// Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
		// // 如果session中没有记录当前场景
		// if (sceneActive == null) {
		// resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE,
		// ParameterCode.Error.UPDATE_SCENE_NO_MATCH,
		// "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
		// return resultString;
		// }
		// Variable deleteVariable = variableList.get(0);
		// List<Variable> sessionVariableList = (List<Variable>)
		// session.get(Constants.KEY_VARIABLE_DELETE);
		// if (sessionVariableList == null || sessionVariableList.size() == 0) {
		// sessionVariableList = new ArrayList<Variable>();
		// }
		// sessionVariableList.add(deleteVariable);
		// session.put(Constants.KEY_VARIABLE_DELETE, sessionVariableList);
		//
		// resultString = StringUtil.packetObject(MethodCode.DELETE_VARIABLE,
		// ParameterCode.Result.RESULT_OK,
		// "删除变量到缓存成功，请注意在切换场景前保存场景数据。", "");
		// }
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

	public String getVariablesList(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
		// 如果session中没有记录当前场景
		if (sceneActive == null) {
			resultString = StringUtil.packetObject(MethodCode.LIST_VARIABLE, ParameterCode.Error.UPDATE_SCENE_NO_MATCH,
					"会话已经过期！", "");
			return resultString;
		}
		JSONArray variableJsonArray = new JSONArray();
		List<Variable> variableList = (List<Variable>) CacheUtil
				.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
		if (variableList == null) {
			variableList = new ArrayList<Variable>();
		}
		for (int i = 0; i < variableList.size(); i++) {

			Variable variable = variableList.get(i);
			JSONObject dataObj = new JSONObject();

			dataObj.put("variableClassId", variable.getVariableClassId());
			dataObj.put("variableInstanceId", variable.getVariableUUID());
			dataObj.put("name", variable.getVariableName());
			dataObj.put("variableType", variable.getVariableType());
			JSONObject belongObj = new JSONObject();
			belongObj.put("fragmentId", variable.getFragmentUUID());
			belongObj.put("scenarioId", variable.getSceneUUID());
			dataObj.put("beLongsTo", belongObj);
			dataObj.put("valueType", variable.getVariableValueType());
			dataObj.put("fieldType", variable.getVariableFieldType());
			dataObj.put("value", variable.getVariableValue());
			
			dataObj.put("variableScope", variable.getVariableScope());

			variableJsonArray.add(dataObj);
		}
		resultString = StringUtil.packetObject(MethodCode.LIST_VARIABLE, ParameterCode.Result.RESULT_OK, "获取变量列表成功",
				variableJsonArray.toString());

		return resultString;
	}

	public String getShareVariableHistory(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareVariableUUID = parmJb.optString("shareVariableUUID", "");
			if (StringUtil.isNullOrEmpty(shareVariableUUID)) {

				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_VARIABLE_HISTORY,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			ShareVariable shareVariable = new ShareVariable();
			shareVariable.setShareVariableUUID(shareVariableUUID);
			List<ShareVariable> shareVariableList = variableDao.findShareVariable(shareVariable);
			if (shareVariableList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_VARIABLE_HISTORY,
						ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
				return resultString;
			}
			shareVariable = shareVariableList.get(0);
			JSONObject variableJsonObj = new JSONObject();
			variableJsonObj.put("shareVariableUUID", shareVariable.getShareVariableUUID());
			variableJsonObj.put("shareVariableName", shareVariable.getShareVariableName());
			variableJsonObj.put("shareVariableType", shareVariable.getShareVariableType());
			variableJsonObj.put("shareVariableObjType", shareVariable.getShareVariableObjType());
			variableJsonObj.put("shareVariableScope", shareVariable.getShareVariableScope());
			variableJsonObj.put("fragmentUUID", shareVariable.getFragmentUUID());
			variableJsonObj.put("shareVariableDesc", shareVariable.getShareVariableDesc());
			variableJsonObj.put("shareVariableCreatedBy", shareVariable.getAccountName());
			variableJsonObj.put("shareVariableCreateTime",
					DateUtil.dateTimeFormat(shareVariable.getShareVariableDate()));

			JGitService jGitService = new JGitService();
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_VARIABLE_REPOSITORY_NAME + "/" + shareVariable.getShareVariableGit();
			List<Committer> committerList = jGitService.getLogs(gitPath);
			JSONArray committerJsonArray = new JSONArray();
			for (Committer c : committerList) {
				JSONObject cJson = new JSONObject();

				cJson.put("commitVersion", c.getCommitVersion());
				cJson.put("commitName", c.getCommitName());
				cJson.put("commitEmail", c.getCommitEmail());
				cJson.put("commitMssage", c.getCommitMssage());
				cJson.put("commitDate", c.getCommitDate());
				committerJsonArray.add(cJson);
			}
			variableJsonObj.put("committers", committerJsonArray.toString());

			resultString = StringUtil.packetObject(MethodCode.GET_SHARE_VARIABLE_HISTORY,
					ParameterCode.Result.RESULT_OK, "获取共享变量共享历史版本成功", variableJsonObj.toString());

		}
		return resultString;
	}

	public String getShareVariableVersion(String bodyString, Account currentAccount, Map<String, Object> session) {
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareVariableUUID = parmJb.optString("shareVariableUUID", "");
			String shareVariableVersion = parmJb.optString("shareVariableVersion", "");
			if (StringUtil.isNullOrEmpty(shareVariableUUID) || StringUtil.isNullOrEmpty(shareVariableVersion)) {

				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_VARIABLE_VERSION,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}

			ShareVariable shareVariable = new ShareVariable();
			shareVariable.setShareVariableUUID(shareVariableUUID);
			List<ShareVariable> shareVariableList = variableDao.findShareVariable(shareVariable);
			if (shareVariableList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_VARIABLE_VERSION,
						ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
				return resultString;
			}
			shareVariable = shareVariableList.get(0);
			JSONObject variableJsonObj = new JSONObject();
			variableJsonObj.put("shareVariableUUID", shareVariable.getShareVariableUUID());
			variableJsonObj.put("shareVariableName", shareVariable.getShareVariableName());
			variableJsonObj.put("shareVariableType", shareVariable.getShareVariableType());
			variableJsonObj.put("shareVariableObjType", shareVariable.getShareVariableObjType());
			variableJsonObj.put("shareVariableScope", shareVariable.getShareVariableScope());
			variableJsonObj.put("fragmentUUID", shareVariable.getFragmentUUID());
			variableJsonObj.put("shareVariableDesc", shareVariable.getShareVariableDesc());
			variableJsonObj.put("shareVariableCreatedBy", shareVariable.getAccountName());
			variableJsonObj.put("shareVariableCreateTime",
					DateUtil.dateTimeFormat(shareVariable.getShareVariableDate()));

			JGitService jGitService = new JGitService();
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_VARIABLE_REPOSITORY_NAME + "/" + shareVariable.getShareVariableGit();
			// 根据版本获取共享变量内容
			String contentString = jGitService.getContentByVersion(gitPath, shareVariableVersion, "template.txt");
			Committer commitInfo = jGitService.getCommitterByVersion(gitPath, shareVariableVersion);
			if (commitInfo == null) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_VARIABLE_VERSION,
						ParameterCode.Error.QUERY_VERSION_NO_EXIST, "查询的版本号不存在", "");
				return resultString;
			}
			JSONObject committerJsonObj = new JSONObject();

			committerJsonObj.put("commitVersion", commitInfo.getCommitVersion());
			committerJsonObj.put("commitName", commitInfo.getCommitName());
			committerJsonObj.put("commitEmail", commitInfo.getCommitEmail());
			committerJsonObj.put("commitMssage", commitInfo.getCommitMssage());
			committerJsonObj.put("commitDate", commitInfo.getCommitDate());
			committerJsonObj.put("expression", contentString);

			variableJsonObj.put("committer", committerJsonObj.toString());

			resultString = StringUtil.packetObject(MethodCode.GET_SHARE_VARIABLE_VERSION,
					ParameterCode.Result.RESULT_OK, "获取某个版本共享变量成功", variableJsonObj.toString());

		}
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

		resultString = StringUtil.packetObject(MethodCode.LIST_SHARE_VARIABLE, ParameterCode.Result.RESULT_OK,
				"获取分享变量列表成功", variableJsonArray.toString());

		return resultString;
	}

	// 发布共享变量版本
	public String releaseShareVariable(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareVariableUUID = parmJb.optString("shareVariableUUID", "");
			String shareVariableComment = parmJb.optString("shareVariableComment", "");
			String shareVariableExpression = parmJb.optString("shareVariableExpression", "");
			String shareVariableName = parmJb.optString("shareVariableName", "");
			String shareVariableType = parmJb.optString("shareVariableType", "");
			String shareVariableObjType = parmJb.optString("shareVariableObjType", "");
			int shareVariableScope = parmJb.optInt("shareVariableScope", -1);
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			String shareVariableDesc = parmJb.optString("shareVariableDesc", "");

			if (StringUtil.isNullOrEmpty(shareVariableUUID) || StringUtil.isNullOrEmpty(shareVariableComment)
					|| StringUtil.isNullOrEmpty(shareVariableExpression)
					|| (shareVariableScope == 1 && StringUtil.isNullOrEmpty(fragmentUUID))) {

				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_VARIABLE,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Variable findVariable = new Variable();
			findVariable.setVariableUUID(shareVariableUUID);
			List<Variable> variableList = variableDao.findVariable(findVariable);
			if (variableList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_VARIABLE,
						ParameterCode.Error.QUERY_VARIABLE_NO_EXIST, "查询的变量不存在", "");
				return resultString;
			}
			Variable fromVariable = variableList.get(0);
			// 查询是否已经存在共享记录
			ShareVariable findShareVariable = new ShareVariable();
			findShareVariable.setShareVariableUUID(shareVariableUUID);
			List<ShareVariable> shareVariableList = variableDao.findShareVariable(findShareVariable);
			ShareVariable shareVariable = null;
			if (shareVariableList.size() == 1) {
				shareVariable = shareVariableList.get(0);
			} else {
				shareVariable = new ShareVariable();
				shareVariable.setShareVariableUUID(shareVariableUUID);
				shareVariable.setShareVariableGit(StringUtil.createShareVariableGit());
			}
			shareVariable.setAccountId(currentAccount.getAccountId());
			if (!StringUtil.isNullOrEmpty(shareVariableName)) {
				shareVariable.setShareVariableName(shareVariableName);
			} else {
				shareVariable.setShareVariableName(fromVariable.getVariableName());
			}
			if (!StringUtil.isNullOrEmpty(shareVariableType)) {
				shareVariable.setShareVariableType(shareVariableType);
			} else {
				// shareVariable.setShareVariableType(fromVariable.getVariableType());
			}
			if (!StringUtil.isNullOrEmpty(shareVariableObjType)) {
				shareVariable.setShareVariableObjType(shareVariableObjType);
			} else {
				// shareVariable.setShareVariableObjType(fromVariable.getVariableObjType());
			}
			if (shareVariableScope != -1) {
				shareVariable.setShareVariableScope(shareVariableScope);
			} else {
				// shareVariable.setShareVariableScope(fromVariable.getVariableScope());
			}
			if (!StringUtil.isNullOrEmpty(fragmentUUID)) {
				if (shareVariableScope == 1) {
					shareVariable.setFragmentUUID(fragmentUUID);
				}
			} else {
				if (shareVariableScope == 1) {
					shareVariable.setFragmentUUID(fromVariable.getFragmentUUID());
				}
			}

			if (!StringUtil.isNullOrEmpty(shareVariableDesc)) {
				shareVariable.setShareVariableDesc(shareVariableDesc);
			} else {
				shareVariable.setShareVariableDesc(fromVariable.getVariableDesc());
			}

			// 提交版本库后获取版本
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_VARIABLE_REPOSITORY_NAME + "/" + shareVariable.getShareVariableGit();
			JGitService jGitService = new JGitService();
			if (shareVariableList.size() == 1) {
			} else {
				jGitService.initShareVariableGit(gitPath, currentAccount);
			}
			FileUtil.writeFile(gitPath + "/template.txt", shareVariableExpression);
			jGitService.shareVariableCommit(gitPath, currentAccount, shareVariableComment);
			// 获取最新版本
			Committer committer = jGitService.getLastCommitter(gitPath);
			shareVariable.setShareVariableVersion(committer.getCommitVersion());
			if (shareVariableList.size() == 1) {
				variableDao.updateShareVariable(shareVariable);
			} else {
				variableDao.releaseVariable(shareVariable);
			}
			resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_VARIABLE, ParameterCode.Result.RESULT_OK,
					"发布共享变量成功", "");

		}
		return resultString;
	}
}
