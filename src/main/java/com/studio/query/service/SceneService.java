package com.studio.query.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.common.Constants;
import com.studio.query.dao.FragmentDao;
import com.studio.query.dao.SceneDao;
import com.studio.query.dao.VariableDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Committer;
import com.studio.query.entity.Fragment;
import com.studio.query.entity.Scene;
import com.studio.query.entity.Variable;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.CacheUtil;
import com.studio.query.util.DateUtil;
import com.studio.query.util.FileUtil;
import com.studio.query.util.HistoryUtil;
import com.studio.query.util.JsonUtil;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SceneService {
	@Autowired
	public SceneDao sceneDao;
	@Autowired
	public FragmentDao fragmentDao;
	@Autowired
	public VariableDao variableDao;

	public List<Scene> findScene(Scene scene) {
		return sceneDao.findScene(scene);
	}

	public int insertScene(Scene scene) {
		return sceneDao.insertScene(scene);
	}

	public int updateScene(Scene scene) {
		return sceneDao.updateScene(scene);
	}

	public String createScene(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneName = parmJb.optString("sceneName", "");
			String sceneDesc = parmJb.optString("sceneDesc", "");
			if (StringUtil.isNullOrEmpty(sceneName)) {

				resultString = StringUtil.packetObject(MethodCode.CREATE_SCENE, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			// 暂时不启用场景名称重复的验证
			// Scene findScene = new Scene();
			// findScene.setSceneName(sceneName);
			// List<Scene> sceneList = sceneDao.findScene(findScene);
			// if (sceneList.size() >= 1) {
			// resultString = StringUtil
			// .packetObject(
			// MethodCode.CREATE_SCENE,
			// com.studio.zqquery.protocol.ParameterCode.Result.RESULT_FAIL,
			// ParameterCode.Error.CREATE_SCENE_EXIST,
			// "场景已经存在", "");
			// return resultString;
			// }
			Scene insertScene = new Scene();
			insertScene.setAccountId(currentAccount.getAccountId());
			insertScene.setSceneUUID(StringUtil.createSceneUUID());
			insertScene.setSceneName(sceneName);
			insertScene.setSceneDesc(sceneDesc);
			insertScene.setSceneGit(StringUtil.createSceneGit());
			int insertResult = sceneDao.insertScene(insertScene);
			if (insertResult == 1) {
				JGitService jGitService = new JGitService();
				String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/" + "/"
						+ Constants.SCENE_REPOSITORY_NAME + "/" + insertScene.getSceneGit();

				jGitService.initAccountGit(gitPath, currentAccount);
				resultString = StringUtil.packetObject(MethodCode.CREATE_SCENE, ParameterCode.Result.RESULT_OK,
						"创建场景成功", "");
				this.resetScope(null, session);
			} else {

				resultString = StringUtil.packetObject(MethodCode.CREATE_SCENE, ParameterCode.Error.CREATE_SCENE_FAIL,
						"创建场景失败", "");
			}
		}
		return resultString;
	}

	public String getSceneList(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONArray sceneJsonArray = new JSONArray();
		Scene findScene = new Scene();
		findScene.setAccountId(currentAccount.getAccountId());
		List<Scene> sceneList = sceneDao.findScene(findScene);
		String sceneActiveUUID = HistoryUtil.getUserSceneHistory(currentAccount.getAccountName());
		boolean isFrist = false;
		if (StringUtil.isNullOrEmpty(sceneActiveUUID)) {
			isFrist = true;
		}
		for (int i = 0; i < sceneList.size(); i++) {

			Scene scene = sceneList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("sceneUUID", scene.getSceneUUID());
			dataObj.put("sceneName", scene.getSceneName());
			dataObj.put("sceneDesc", scene.getSceneDesc());
			if (isFrist) {
				if (i == 0) {
					dataObj.put("sceneActive", "true");
					// 会话中设置当前活动场景
					this.setActiveScene(scene.getSceneUUID(), currentAccount, session);
				} else {
					dataObj.put("sceneActive", "false");
				}
			} else {
				if (sceneActiveUUID.equals(scene.getSceneUUID())) {
					dataObj.put("sceneActive", "true");
					// 会话中设置当前活动场景
					this.setActiveScene(scene.getSceneUUID(), currentAccount, session);
				} else {
					dataObj.put("sceneActive", "false");
				}
			}
			dataObj.put("sceneEnable", scene.getSceneEnable() == 0 ? "true" : "false");
			sceneJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_SCENE, ParameterCode.Result.RESULT_OK, "获取场景列表成功",
				sceneJsonArray.toString());

		return resultString;
	}

	// 设置当前活动场景
	public void setActiveScene(String sceneUUID, Account currentAccount, Map<String, Object> session) {

		Scene findScene = new Scene();
		findScene.setSceneUUID(sceneUUID);
		List<Scene> sceneList = this.findScene(findScene);
		if (sceneList.size() == 1) {
			String scenePath = Configure.gitRepositoryPath + currentAccount.getAccountRepository() + "/"
					+ Constants.SCENE_REPOSITORY_NAME + "/" + sceneList.get(0).getSceneGit();
			session.put(Constants.KEY_SCENE_PATH, scenePath);
			session.put(Constants.SCENE_ACTIVE, sceneList.get(0));
		}

	}

	// 重新设置scope
	public void resetScope(List<String> resetScopes, Map<String, Object> session) {

		session.remove(Constants.KEY_SET_SCOPE);
		List<String> scopeList = new ArrayList<String>();
		List<Map<String, String>> indexList = (List<Map<String, String>>) CacheUtil.getCacheObject("mapIndex");
		if (indexList != null) {
			for (Map<String, String> map : indexList) {
				String scopeId = map.get("id");
				String isUnified = map.get("isUnified");
				if (isUnified.equals("true")) {
					scopeList.add(scopeId);
				}
			}
		}
		if (resetScopes != null) {
			for (String scope : resetScopes) {
				boolean isExist = false;
				for (int i = 0; i < scopeList.size(); i++) {
					String oldScope = scopeList.get(i);
					if (oldScope.equals(scope)) {
						isExist = true;
						break;
					}
				}
				if (!isExist) {
					scopeList.add(scope);
				}
			}
		}
		session.put(Constants.KEY_SET_SCOPE, scopeList);
	}

	public String getSceneHistory(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;

		Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
		// 如果session中没有记录当前场景
		if (sceneActive == null) {
			resultString = StringUtil.packetObject(MethodCode.HISTORY_SCENE, ParameterCode.Error.SERVICE_SESSION,
					"当前没设置活动场景，或会话已经过期！", "");
			return resultString;
		}

		String scenePath = Configure.gitRepositoryPath + currentAccount.getAccountRepository() + "/"
				+ Constants.SCENE_REPOSITORY_NAME + "/" + sceneActive.getSceneGit();

		Map map = JGitService.readLogTree(scenePath, new HashMap<>());
		resultString = StringUtil.packetObjectObj(MethodCode.HISTORY_SCENE, ParameterCode.Result.RESULT_OK,
				"查询场景历史版本成功", map);

		return resultString;
	}

	/**
	 * 切换场景，session记录当前用户操作的场景
	 * 
	 * @param bodyString
	 * @param currentAccount
	 * @return
	 */
	public String switchScene(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneUUID = parmJb.optString("sceneUUID", "");
			if (StringUtil.isNullOrEmpty(sceneUUID)) {

				resultString = StringUtil.packetObject(MethodCode.SWITCH_SCENE, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			Scene findScene = new Scene();
			findScene.setSceneUUID(sceneUUID);
			List<Scene> sceneList = this.findScene(findScene);
			if (sceneList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.SWITCH_SCENE,
						ParameterCode.Error.QUERY_SCENE_NO_EXIST, "查询的场景不存在", "");
				return resultString;
			}
			String scenePath = Configure.gitRepositoryPath + currentAccount.getAccountRepository() + "/"
					+ Constants.SCENE_REPOSITORY_NAME + "/" + sceneList.get(0).getSceneGit();
			session.put(Constants.KEY_SCENE_PATH, scenePath);
			session.put(Constants.SCENE_ACTIVE, sceneList.get(0));

			// 记录当前活动的场景
			JSONObject activeObj = new JSONObject();
			activeObj.put("sceneUUID", sceneList.get(0).getSceneUUID());
			HistoryUtil.setUserSceneHistory(currentAccount.getAccountName(), activeObj.toString());

			resultString = StringUtil.packetObject(MethodCode.SWITCH_SCENE, ParameterCode.Result.RESULT_OK,
					"切换场景成功，当前场景是" + sceneList.get(0).getSceneName(), "");

		}
		return resultString;
	}

	public String switchVersion(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneVersion = parmJb.optString("sceneVersion", "");
			if (StringUtil.isNullOrEmpty(sceneVersion)) {
				resultString = StringUtil.packetObject(MethodCode.SWITCH_VERSION, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.SWITCH_VERSION, ParameterCode.Error.SERVICE_SESSION,
						"当前没设置活动场景，或会话已经过期！", "");
				return resultString;
			}
			String sessionScenePath = (String) session.get(Constants.KEY_SCENE_PATH);

			// 根据版本获取场景内容
			JGitService jGitService = new JGitService();
			String contentString = jGitService.getContentByVersion(sessionScenePath, sceneVersion, "info.txt");
			// 解析场景json数据
			List<Fragment> fragmentList = JsonUtil.getFragmentFromSceneString(contentString);
			List<Variable> variableList = JsonUtil.getVariableFromSceneString(contentString);
			List<String> scopeList = JsonUtil.getScopeFromSceneString(contentString);

			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM, fragmentList);
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR, variableList);
			// 重设scope
			this.resetScope(scopeList, session);

			session.put(Constants.SCENE_VERSION, sceneVersion);
			resultString = StringUtil.packetObject(MethodCode.SWITCH_VERSION, ParameterCode.Result.RESULT_OK, "切换版本成功",
					"");
		}
		return resultString;
	}

	public String getCurrentVersion(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
		if (sceneActive == null) {
			resultString = StringUtil.packetObject(MethodCode.GET_CURRENT_VERSION, ParameterCode.Error.SERVICE_SESSION,
					"当前没设置活动场景，或会话已经过期！", "");
			return resultString;
		}
		String sessionScenePath = (String) session.get(Constants.KEY_SCENE_PATH);
		// 获取最新提交记录
		JGitService jGitService = new JGitService();
		Committer committer = jGitService.getLastCommitter(sessionScenePath);

		JSONObject sceneObject = new JSONObject();
		sceneObject.put("sceneUUID", sceneActive.getSceneUUID());
		sceneObject.put("sceneName", sceneActive.getSceneName());
		sceneObject.put("sceneDesc", sceneActive.getSceneDesc());
		sceneObject.put("sceneCreateTime", sceneActive.getSceneDesc());

		sceneObject.put("sceneVersion", committer.getCommitVersion());
		sceneObject.put("sceneComment", committer.getCommitMssage());
		JSONArray scopeObjs = new JSONArray();
		List<String> scopeArray = (ArrayList<String>) session.get(Constants.KEY_SET_SCOPE);
		if (scopeArray == null) {
			scopeArray = new ArrayList<String>();
		}
		for (String scopeStr : scopeArray) {
			scopeObjs.add(scopeStr);
		}
		sceneObject.put("scope", scopeObjs.toString());
		JSONArray fragmentJsonArray = new JSONArray();
		JSONArray variableJsonArray = new JSONArray();

		// 读取缓存中的fragment数据
		List<Fragment> fragmentList = (List<Fragment>) CacheUtil
				.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
		if (fragmentList == null) {
			fragmentList = new ArrayList<Fragment>();
		}
		for (int i = 0; i < fragmentList.size(); i++) {

			Fragment fragment = fragmentList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("fragmentUUID", fragment.getFragmentUUID());
			dataObj.put("fragmentName", fragment.getFragmentName());
			dataObj.put("fragmentDesc", fragment.getFragmentDesc());
			dataObj.put("fragmentType", fragment.getFragmentType());
			dataObj.put("fragmentObjType", fragment.getFragmentObjType());
			dataObj.put("fragmentEnable", fragment.getFragmentEnableStr());
			dataObj.put("fragmentActive", fragment.getFragmentActiveStr());
			dataObj.put("fragmentCreateTime", fragment.getFragmentDateStr());

			fragmentJsonArray.add(dataObj);
		}
		sceneObject.put("fragmentList", fragmentJsonArray);

		List<Variable> variableList = (List<Variable>) CacheUtil
				.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
		if (variableList == null) {
			variableList = new ArrayList<Variable>();
		}
		for (int i = 0; i < variableList.size(); i++) {

			Variable variable = variableList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("variableUUID", variable.getVariableUUID());
			dataObj.put("variableName", variable.getVariableName());
			dataObj.put("variableType", variable.getVariableType());
			dataObj.put("variableObjType", variable.getVariableObjType());
			dataObj.put("variableScope", variable.getVariableScopeStr());
			dataObj.put("fragmentUUID", variable.getFragmentUUID());
			dataObj.put("variableDesc", variable.getVariableDesc());
			dataObj.put("variableCreateTime", variable.getVariableDateStr());

			variableJsonArray.add(dataObj);
		}
		sceneObject.put("variableList", variableJsonArray);

		resultString = StringUtil.packetObject(MethodCode.GET_CURRENT_VERSION, ParameterCode.Result.RESULT_OK,
				"获取当前本场景成功", sceneObject.toString());
		return resultString;

	}

	public String closeSceneVersion(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneVersion = parmJb.optString("sceneVersion", "");
			if (StringUtil.isNullOrEmpty(sceneVersion)) {

				resultString = StringUtil.packetObject(MethodCode.CLOSE_VERSION, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			resultString = StringUtil.packetObject(MethodCode.CLOSE_VERSION, ParameterCode.Result.RESULT_OK, "关闭场景成功",
					"");

		}
		return resultString;
	}

	public String updateScene(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneComment = parmJb.optString("sceneComment", "");
			if (StringUtil.isNullOrEmpty(sceneComment)) {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_SCENE, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}

			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_SCENE, ParameterCode.Error.SERVICE_SESSION,
						"当前没设置活动场景，或会话已经过期！", "");
				return resultString;
			}
			String scenePath = (String) session.get(Constants.KEY_SCENE_PATH);

			// 执行缓存
			this.executeCache(session);
			JSONObject sceneJson = new JSONObject();
			// 逻辑处理，保存scene json数据
			// 获取缓存中fragment数组
			List<Fragment> sessionFragmentArray = (ArrayList<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (sessionFragmentArray == null) {
				sessionFragmentArray = new ArrayList<Fragment>();
			}
			JSONArray fragmentJsonArray = new JSONArray();
			for (Fragment fragment : sessionFragmentArray) {
				JSONObject dataObj = new JSONObject();
				dataObj.put("fragmentUUID", fragment.getFragmentUUID());
				dataObj.put("fragmentName", fragment.getFragmentName());
				dataObj.put("fragmentDesc", fragment.getFragmentDesc());
				dataObj.put("fragmentType", fragment.getFragmentType());
				dataObj.put("fragmentObjType", fragment.getFragmentObjType());
				dataObj.put("fragmentEnable", fragment.getFragmentEnableStr());
				dataObj.put("fragmentActive", fragment.getFragmentActiveStr());
				dataObj.put("fragmentCreateTime", fragment.getFragmentDateStr());
				dataObj.put("fragmentExpression", fragment.getFragmentExpression());
				fragmentJsonArray.add(dataObj);
			}
			sceneJson.put("fragmentList", fragmentJsonArray);
			// 获取缓存中变量数组
			List<Variable> sessionVariableArray = (ArrayList<Variable>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
			if (sessionVariableArray == null) {
				sessionVariableArray = new ArrayList<Variable>();
			}
			JSONArray variableJsonArray = new JSONArray();

			for (Variable variable : sessionVariableArray) {
				JSONObject dataObj = new JSONObject();
				dataObj.put("variableUUID", variable.getVariableUUID());
				dataObj.put("variableName", variable.getVariableName());
				dataObj.put("variableType", variable.getVariableType());
				dataObj.put("variableObjType", variable.getVariableObjType());
				dataObj.put("variableScope", variable.getVariableScopeStr());
				dataObj.put("fragmentUUID", variable.getFragmentUUID());
				dataObj.put("variableDesc", variable.getVariableDesc());
				dataObj.put("variableCreateTime", variable.getVariableDateStr());
				dataObj.put("variableExpression", variable.getVariableExpression());
				variableJsonArray.add(dataObj);
			}
			sceneJson.put("variableList", variableJsonArray);

			JSONArray scopeObjs = new JSONArray();
			List<String> scopeArray = (ArrayList<String>) session.get(Constants.KEY_SET_SCOPE);
			if (scopeArray == null) {
				scopeArray = new ArrayList<String>();
			}
			for (String scopeStr : scopeArray) {
				scopeObjs.add(scopeStr);
			}
			sceneJson.put("scopeList", scopeObjs.toString());

			FileUtil.writeFile(scenePath + "/info.txt", sceneJson.toString());
			JGitService jGitService = new JGitService();
			jGitService.jGitCommit(scenePath, currentAccount, sceneComment);

			JSONObject sceneObject = new JSONObject();
			sceneObject.put("sceneUUID", sceneActive.getSceneUUID());
			sceneObject.put("sceneName", sceneActive.getSceneName());
			sceneObject.put("sceneDesc", sceneActive.getSceneDesc());
			// 从git查询最新版本的comment和version
			Committer committer = jGitService.getLastCommitter(scenePath);

			sceneObject.put("sceneComment", committer.getCommitMssage());
			sceneObject.put("sceneVersion", committer.getCommitVersion());

			sceneObject.put("scope", scopeObjs.toString());
			sceneObject.put("sceneActive", "true");
			sceneObject.put("sceneEnable", "true");
			resultString = StringUtil.packetObject(MethodCode.UPDATE_SCENE, ParameterCode.Result.RESULT_OK, "更新场景成功",
					sceneObject.toString());

		}
		return resultString;
	}

	// 验证缓存是否有未保存数据
	public boolean verifyCache(String bodyString, Account currentAccount, Map<String, Object> session) {
		boolean result = false;
		List<Fragment> fragmentAddList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_ADD);
		if (fragmentAddList != null && fragmentAddList.size() >= 1) {
			result = true;
		}
		List<Fragment> fragmentUpdateList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_UPDATE);
		if (fragmentUpdateList != null && fragmentUpdateList.size() >= 1) {
			result = true;
		}
		List<Fragment> fragmentDeleteList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_DELETE);
		if (fragmentDeleteList != null && fragmentDeleteList.size() >= 1) {
			result = true;
		}
		List<Variable> variableAddList = (List<Variable>) session.get(Constants.KEY_VARIABLE_ADD);
		if (variableAddList != null && variableAddList.size() >= 1) {
			result = true;
		}
		List<Variable> variableUpdateList = (List<Variable>) session.get(Constants.KEY_VARIABLE_UPDATE);
		if (variableUpdateList != null && variableUpdateList.size() >= 1) {
			result = true;
		}
		List<Variable> variableDeleteList = (List<Variable>) session.get(Constants.KEY_VARIABLE_DELETE);
		if (variableDeleteList != null && variableDeleteList.size() >= 1) {
			result = true;
		}
		return result;
	}

	// 执行缓存将相关数据保存后清空缓存。
	public void executeCache(Map<String, Object> session) {
		List<Fragment> fragmentAddList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_ADD);
		if (fragmentAddList != null) {
			for (Fragment fragment : fragmentAddList) {
				fragmentDao.insertFragment(fragment);
			}
			session.remove(Constants.KEY_FRAGMENT_ADD);
		}

		List<Fragment> fragmentUpdateList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_UPDATE);
		if (fragmentUpdateList != null) {
			for (Fragment fragment : fragmentUpdateList) {
				fragmentDao.updateFragment(fragment);
			}
			session.remove(Constants.KEY_FRAGMENT_UPDATE);
		}

		List<Fragment> fragmentDeleteList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_DELETE);
		if (fragmentDeleteList != null) {
			for (Fragment fragment : fragmentDeleteList) {
				fragmentDao.deleteFragment(fragment);
			}
			session.remove(Constants.KEY_FRAGMENT_DELETE);
		}

		List<Variable> variableAddList = (List<Variable>) session.get(Constants.KEY_VARIABLE_ADD);
		if (variableAddList != null) {
			for (Variable variable : variableAddList) {
				variableDao.insertVariable(variable);
			}
			session.remove(Constants.KEY_VARIABLE_ADD);
		}

		List<Variable> variableUpdateList = (List<Variable>) session.get(Constants.KEY_VARIABLE_UPDATE);
		if (variableUpdateList != null) {
			for (Variable variable : variableUpdateList) {
				variableDao.updateVariable(variable);
			}
			session.remove(Constants.KEY_VARIABLE_UPDATE);
		}

		List<Variable> variableDeleteList = (List<Variable>) session.get(Constants.KEY_VARIABLE_DELETE);
		if (variableDeleteList != null) {
			for (Variable variable : variableDeleteList) {
				variableDao.deleteVariable(variable);
			}
			session.remove(Constants.KEY_VARIABLE_DELETE);
		}
	}
}
