package com.studio.query.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.common.Constants;
import com.studio.query.dao.SceneDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Committer;
import com.studio.query.entity.Scene;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.FileUtil;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SceneService {
	@Autowired
	public SceneDao sceneDao;

	public List<Scene> findScene(Scene scene) {
		return sceneDao.findScene(scene);
	}

	public int insertScene(Scene scene) {
		return sceneDao.insertScene(scene);
	}

	public int updateScene(Scene scene) {
		return sceneDao.updateScene(scene);
	}

	public String createScene(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneName = parmJb.optString("sceneName", "");
			String sceneDesc = parmJb.optString("sceneDesc", "");
			if (StringUtil.isNullOrEmpty(sceneName)) {

				resultString = StringUtil.packetObject(MethodCode.CREATE_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
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
				String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
						+ insertScene.getSceneGit();

				jGitService.initAccountGit(gitPath, currentAccount);
				resultString = StringUtil.packetObject(MethodCode.CREATE_SCENE, ParameterCode.Result.RESULT_OK, "",
						"创建场景成功", "");
			} else {

				resultString = StringUtil.packetObject(MethodCode.CREATE_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.CREATE_SCENE_FAIL, "创建场景失败", "");
			}
		}
		return resultString;
	}

	public String getSceneList(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONArray sceneJsonArray = new JSONArray();
		Scene findScene = new Scene();
		findScene.setAccountId(currentAccount.getAccountId());
		List<Scene> sceneList = sceneDao.findScene(findScene);
		for (int i = 0; i < sceneList.size(); i++) {

			Scene scene = sceneList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("sceneUUID", scene.getSceneUUID());
			dataObj.put("sceneName", scene.getSceneName());
			dataObj.put("sceneDesc", scene.getSceneDesc());
			dataObj.put("sceneActive", scene.getSceneActive() == 1 ? "true" : "false");
			dataObj.put("sceneEnable", scene.getSceneEnable() == 1 ? "true" : "false");
			sceneJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_SCENE, ParameterCode.Result.RESULT_OK, "", "获取场景列表成功",
				sceneJsonArray.toString());

		return resultString;
	}

	public String getSceneHistory(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneUUID = parmJb.optString("sceneUUID", "");
			if (StringUtil.isNullOrEmpty(sceneUUID)) {

				resultString = StringUtil.packetObject(MethodCode.HISTORY_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene findScene = new Scene();
			findScene.setSceneUUID(sceneUUID);
			List<Scene> sceneList = sceneDao.findScene(findScene);
			if (sceneList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.HISTORY_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_SCENE_NO_EXIST, "场景不存在", "");
				return resultString;
			}

			JSONObject sceneJson = new JSONObject();
			JSONObject currentJson = new JSONObject();
			JSONObject rootJson = new JSONObject();
			JSONArray commitJsons = new JSONArray();

			currentJson.put("committer", "test2");
			currentJson.put("commiteEmail", "test2@zq.com");
			currentJson.put("comment", "");
			currentJson.put("version", "235c0729afe31e95b0f44d18f9b998f15f7c9b90");
			currentJson.put("branch", "master");
			currentJson.put("commitDate", "2016-04-25 12:55:51");

			JSONArray childrenObjs = new JSONArray();
			childrenObjs.add("a2928f08a4d68905409ca1dae4dedc5b87b7634c");
			rootJson.put("children", childrenObjs.toString());
			rootJson.put("version", "b1a3d9419283efa738ae1aa5d16d84310d862419");
			JSONArray branchObjs = new JSONArray();
			branchObjs.add("master");
			branchObjs.add("dev");
			rootJson.put("branch", branchObjs.toString());
			rootJson.put("commitDate", "2016-04-22 11:50:01");

			JSONObject cJson1 = new JSONObject();
			cJson1.put("version", "b14744c957c1e57f395106f8590ecc1de30bd53f");
			cJson1.put("committer", "test2");
			cJson1.put("commiteEmail", "test2@zq.com");
			cJson1.put("comment", "");
			cJson1.put("parent", "e7f44c4f35571c16698644a0401dc3a716223110");
			JSONArray chiObjs1 = new JSONArray();
			chiObjs1.add("d87ab50598cf705a56a50e7bd7ec0c60056ea23a");
			chiObjs1.add("d87ab50598cf705a56a50e7bd7ec0c60056ea23b");
			cJson1.put("children", chiObjs1.toString());
			cJson1.put("branch", branchObjs.toString());
			cJson1.put("commitDate", "2016-04-22 11:50:01");
			JSONObject cJson2 = new JSONObject();
			cJson2.put("version", "b14744c957c1e57f395106f8590ecc1de30bd522");
			cJson2.put("committer", "test2");
			cJson2.put("commiteEmail", "test2@zq.com");
			cJson2.put("comment", "");
			cJson2.put("parent", "e7f44c4f35571c16698644a0401dc3a716223122");
			JSONArray chiObjs2 = new JSONArray();
			chiObjs2.add("d87ab50598cf705a56a50e7bd7ec0c60056ea23c");
			chiObjs2.add("d87ab50598cf705a56a50e7bd7ec0c60056ea23d");
			cJson2.put("children", chiObjs2.toString());
			cJson2.put("branch", branchObjs.toString());
			cJson2.put("commitDate", "2016-04-22 11:50:01");
			commitJsons.add(cJson1);
			commitJsons.add(cJson2);

			sceneJson.put("current", currentJson.toString());
			sceneJson.put("root", rootJson.toString());
			sceneJson.put("commit", commitJsons.toString());

			resultString = StringUtil.packetObject(MethodCode.HISTORY_SCENE, ParameterCode.Result.RESULT_OK, "",
					"查询场景历史版本成功", sceneJson.toString());

		}
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

				resultString = StringUtil.packetObject(MethodCode.SWITCH_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene findScene = new Scene();
			findScene.setSceneUUID(sceneUUID);
			List<Scene> sceneList = this.findScene(findScene);
			if (sceneList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.SWITCH_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_SCENE_NO_EXIST, "查询的场景不存在", "");
				return resultString;
			}
			String scenePath = Configure.gitRepositoryPath + currentAccount.getAccountRepository() + "/"
					+ sceneList.get(0).getSceneGit();
			session.put(Constants.KEY_SCENE_PATH, scenePath);

			resultString = StringUtil.packetObject(MethodCode.SWITCH_SCENE, ParameterCode.Result.RESULT_OK, "",
					"切换场景成功，当前场景是" + sceneList.get(0).getSceneName(), "");

		}
		return resultString;
	}

	public String switchSceneVersion(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneVersion = parmJb.optString("sceneVersion", "");
			if (StringUtil.isNullOrEmpty(sceneVersion)) {

				resultString = StringUtil.packetObject(MethodCode.SWITCH_VERSION, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}

			resultString = StringUtil.packetObject(MethodCode.SWITCH_VERSION, ParameterCode.Result.RESULT_OK, "",
					"切换版本成功", "");

		}
		return resultString;
	}

	public String getSceneVersion(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneUUID = parmJb.optString("sceneUUID", "");
			String sceneVersion = parmJb.optString("sceneVersion", "");
			if (StringUtil.isNullOrEmpty(sceneUUID)) {

				resultString = StringUtil.packetObject(MethodCode.GET_SCENE_VERSION, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene findScene = new Scene();
			findScene.setSceneUUID(sceneUUID);
			List<Scene> sceneList = this.findScene(findScene);
			if (sceneList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_SCENE_VERSION, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_SCENE_NO_EXIST, "查询的场景不存在", "");
				return resultString;
			}
			Scene currentScene = sceneList.get(0);
			String scenePath = Configure.gitRepositoryPath + currentAccount.getAccountRepository() + "/"
					+ currentScene.getSceneGit();

			String sessionScenePath = session.get(Constants.KEY_SCENE_PATH) == null ? null
					: session.get(Constants.KEY_SCENE_PATH).toString();
			// 如果session中没有记录当前场景，或记录的当前场景跟提交的场景不匹配
			if (StringUtil.isNullOrEmpty(sessionScenePath) || !scenePath.equals(sessionScenePath)) {
				resultString = StringUtil.packetObject(MethodCode.GET_SCENE_VERSION, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "提交的场景跟会话中设置当前的场景不匹配，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}
			// 根据版本获取场景内容
			JGitService jGitService = new JGitService();
			jGitService.getContentByVersion(sessionScenePath, sceneVersion);
			
			JSONObject sceneObject = new JSONObject();
			// sceneObject.put("id", "SCNO8a45aae29e3d452c8cb6c4ee1857cd59");
			// sceneObject.put("name", "test scene");
			// sceneObject.put("desc", "");
			// sceneObject.put("comment", "");
			// sceneObject.put("version",
			// "af975235a63239f9c1b396fe32203bfae6b6de12");
			// JSONArray scopeObjs = new JSONArray();
			// scopeObjs.add("allindicies.unified");
			// scopeObjs.add("aminno.entities");
			// scopeObjs.add("voter_fl.entities");
			// sceneObject.put("scope", scopeObjs.toString());
			// sceneObject.put("createTime", "2016-04-25 11:45:23");
			// JSONArray fragmentObjs = new JSONArray();
			// JSONObject fragmentJson1 = new JSONObject();
			// fragmentJson1.put("id", "FRGMc1803bf6c3c347d3a46ae86c3be4e14a");
			// fragmentJson1.put("name", "test fragment");
			// fragmentJson1.put("desc", "my first fragment");
			// fragmentJson1.put("type", "filter");
			// fragmentJson1.put("isShare", 0);
			// fragmentJson1.put("enable", true);
			// fragmentJson1.put("expression", "");
			// JSONObject fragmentJson2 = new JSONObject();
			// fragmentJson2.put("id", "FRGMc5dd8fac58054db0b294d03535cc2433");
			// fragmentJson2.put("name", "hbnfragment");
			// fragmentJson2.put("desc", "haha");
			// fragmentJson2.put("type", "query");
			// fragmentJson2.put("isShare", 0);
			// fragmentJson2.put("enable", true);
			// fragmentJson2.put("expression", "");
			// fragmentObjs.add(fragmentJson1);
			// fragmentObjs.add(fragmentJson2);
			// sceneObject.put("fragmentList", fragmentObjs.toArray());
			// JSONArray variableObjs = new JSONArray();
			// JSONObject variableJson1 = new JSONObject();
			// variableJson1.put("id", "VARC5444bb041b8441878536c9b3a6840e8b");
			// variableJson1.put("name", "my first variable");
			// variableJson1.put("scope", 0);//
			// 0是scenario表示全局变量，1fragment表示fragment内的局部变量
			// variableJson1.put("isShare", 0);
			// variableJson1.put("enable", true);
			// variableJson1.put("type", "string");
			// variableJson1.put("value", "tom");
			// JSONObject variableJson2 = new JSONObject();
			// variableJson2.put("id", "VARI68ec103269d547b6906c329e3a583364");
			// variableJson2.put("name", "my first variable");
			// variableJson2.put("scope", 1);//
			// 0是scenario表示全局变量，1fragment表示fragment内的局部变量
			// variableJson2.put("fragmentId",
			// "FRGMc5dd8fac58054db0b294d03535cc2433");
			// variableJson2.put("isShare", 0);
			// variableJson2.put("enable", true);
			// variableJson2.put("type", "string");
			// variableJson2.put("value", "tom");
			// variableObjs.add(variableJson1);
			// variableObjs.add(variableJson2);
			// sceneObject.put("variableList", variableObjs.toString());

			resultString = StringUtil.packetObject(MethodCode.GET_SCENE_VERSION, ParameterCode.Result.RESULT_OK, "",
					"获取某版本场景成功", sceneObject.toString());
		}
		return resultString;
	}

	public String closeSceneVersion(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneVersion = parmJb.optString("sceneVersion", "");
			if (StringUtil.isNullOrEmpty(sceneVersion)) {

				resultString = StringUtil.packetObject(MethodCode.CLOSE_VERSION, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			resultString = StringUtil.packetObject(MethodCode.CLOSE_VERSION, ParameterCode.Result.RESULT_OK, "",
					"关闭场景成功", "");

		}
		return resultString;
	}

	public String updateScene(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String sceneUUID = parmJb.optString("sceneUUID", "");
			String sceneComment = parmJb.optString("sceneComment", "");
			String sceneExpression = parmJb.optString("sceneExpression", "");
			if (StringUtil.isNullOrEmpty(sceneUUID)) {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene findScene = new Scene();
			findScene.setSceneUUID(sceneUUID);
			List<Scene> sceneList = this.findScene(findScene);
			if (sceneList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_SCENE_NO_EXIST, "查询的场景不存在", "");
				return resultString;
			}
			Scene currentScene = sceneList.get(0);
			String scenePath = Configure.gitRepositoryPath + currentAccount.getAccountRepository() + "/"
					+ currentScene.getSceneGit();

			String sessionScenePath = session.get(Constants.KEY_SCENE_PATH) == null ? null
					: session.get(Constants.KEY_SCENE_PATH).toString();
			// 如果session中没有记录当前场景，或记录的当前场景跟提交的场景不匹配
			if (StringUtil.isNullOrEmpty(sessionScenePath) || !scenePath.equals(sessionScenePath)) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_SCENE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "提交的场景跟会话中设置当前的场景不匹配，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}

			// 保存parmJb string to info.txt
			FileUtil.writeFile(sessionScenePath + "/info.txt", sceneExpression);
			JGitService jGitService = new JGitService();
			jGitService.jGitCommit(sessionScenePath, currentAccount, sceneComment);

			JSONObject sceneObject = new JSONObject();
			sceneObject.put("sceneUUID", currentScene.getSceneUUID());
			sceneObject.put("sceneName", currentScene.getSceneName());
			sceneObject.put("sceneDesc", currentScene.getSceneDesc());
			// 从git查询最新版本的comment和version
			Committer committer = jGitService.getLastCommitter(sessionScenePath);

			sceneObject.put("sceneComment", committer.getCommitMssage());
			sceneObject.put("sceneVersion", committer.getCommitVersion());

			sceneObject.put("sceneActive", currentScene.getSceneActive());
			sceneObject.put("sceneEnable", currentScene.getSceneEnable());
			resultString = StringUtil.packetObject(MethodCode.UPDATE_SCENE, ParameterCode.Result.RESULT_OK, "",
					"更新场景成功", sceneObject.toString());

		}
		return resultString;
	}
}
