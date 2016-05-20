package com.studio.query.action;

import com.studio.query.common.ApacheHttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestAction {
	public static void main(String[] args) {
		mainApi();

	}

	public static void mainApi() {
		try {
			// // 注册测试
			// JSONObject registerJson = new JSONObject();
			// registerJson.put("method", "register");
			// JSONObject registerObj = new JSONObject();
			// registerObj.put("accountName", "huangboning");
			// registerObj.put("accountPassword","123456");
			// registerObj.put("accountNumber", "15557181347");
			// registerObj.put("accountEmail", "huangboning@test.com");
			// registerJson.put("params", registerObj);

			// 登录测试
			JSONObject loginJson = new JSONObject();
			loginJson.put("method", "login");
			JSONObject loginObj = new JSONObject();
			loginObj.put("accountName", "huangboning");
			loginObj.put("accountPassword", "123456");
			loginJson.put("params", loginObj);

			// // 账号查询测试
			// JSONObject queryJson = new JSONObject();
			// queryJson.put("method", "accountQuery");
			// JSONObject queryObj = new JSONObject();
			// queryObj.put("accountName", "huangboning");
			// queryJson.put("params", queryObj);

			// // 账号注销测试
			// JSONObject logoutJson = new JSONObject();
			// logoutJson.put("method", "logout");
			// logoutJson.put("params", "");
			//
			// String result = ApacheHttpUtil.sendPost(
			// "http://192.168.1.32:8080/query/service/v1/main.do", queryJson
			// .toString().getBytes("utf-8"));
			// System.out.println(queryJson.toString());
			// System.out.println(result);

			// // 获取数据源列表
			// JSONObject typesJson = new JSONObject();
			// typesJson.put("method", "getIndexDocTypes");
			// JSONObject jb = new JSONObject();
			// typesJson.put("params", jb);

			// // 设置数据源
			// JSONObject setScopeJson = new JSONObject();
			// setScopeJson.put("method", "setScope");
			// setScopeJson.put("params", "");
			// JSONObject jb = new JSONObject();
			// jb.put("scope", "aminno.entities");
			// setScopeJson.put("params", jb);

			// // 获取选择数据源定义的数据表头
			// JSONObject headJson = new JSONObject();
			// headJson.put("method", "getTableHeadDef");
			// headJson.put("params", "");

			// // 创建场景
			// JSONObject createSceneJson = new JSONObject();
			// createSceneJson.put("method", "createScene");
			// JSONObject createSceneObj = new JSONObject();
			// createSceneObj.put("sceneName", "foxquery2");
			// createSceneObj.put("sceneDesc", "我的第二个场景foxquery");
			// createSceneJson.put("params", createSceneObj);

			// // 获取场景列表
			// JSONObject listSceneJson = new JSONObject();
			// listSceneJson.put("method", "getScenarioes");
			// JSONObject listSceneObj = new JSONObject();
			// listSceneJson.put("params", listSceneObj);

			// // 获取场景历史版本
			// JSONObject historySceneJson = new JSONObject();
			// historySceneJson.put("method", "getScenarioHistory");
			// JSONObject historySceneObj = new JSONObject();
			// historySceneObj.put("sceneNo",
			// "SCNO6d527981f75343a8b52cd54e7e4b3b9d");
			// historySceneJson.put("params", historySceneObj);

			// 切换场景
			JSONObject switchSceneJson = new JSONObject();
			switchSceneJson.put("method", "switchScenario");
			JSONObject switchSceneObj = new JSONObject();
			switchSceneObj.put("sceneUUID", "SCNObd1d9a66bca44137a70fd766558f56a8");
			switchSceneJson.put("params", switchSceneObj);

			// // 获取某版本场景
			// JSONObject sceneVersionJson = new JSONObject();
			// sceneVersionJson.put("method", "getScenarioVersion");
			// JSONObject sceneVersionObj = new JSONObject();
			// sceneVersionObj.put("sceneUUID",
			// "SCNObd1d9a66bca44137a70fd766558f56a8");
			// sceneVersionObj.put("sceneVersion", "sd");
			// sceneVersionJson.put("params", sceneVersionObj);

			// // 关闭场景版本
			// JSONObject closeSceneJson = new JSONObject();
			// closeSceneJson.put("method", "closeScenario");
			// JSONObject closeSceneObj = new JSONObject();
			// closeSceneObj.put("sceneVersion",
			// "235c0729afe31e95b0f44d18f9b998f15f7c9b90");
			// closeSceneJson.put("params", closeSceneObj);

			// // 创建fragment
			// JSONObject createFragmentJson = new JSONObject();
			// createFragmentJson.put("method", "createFragment");
			// JSONObject createFragmentObj = new JSONObject();
			// createFragmentObj.put("fragmentName", "cangFragment");
			// createFragmentObj.put("fragmentType", "filter");
			// createFragmentObj.put("fragmentDesc", "我的第二个fragment");
			// createFragmentJson.put("params", createFragmentObj);

			// // 更新fragment
			// JSONObject updateFragmentJson = new JSONObject();
			// updateFragmentJson.put("method", "updateFragment");
			// JSONObject updateFragmentObj = new JSONObject();
			// updateFragmentObj.put("fragmentUUID",
			// "FRGM7fd6af722f064610bf8dbb66676bd0ee");
			// updateFragmentObj.put("fragmentName", "foxFragment2");
			// updateFragmentObj.put("fragmentType", "filter");
			// updateFragmentObj.put("fragmentDesc", "update foxFragment");
			// updateFragmentJson.put("params", updateFragmentObj);

			// // 禁用fragment
			// JSONObject disableFragmentJson = new JSONObject();
			// disableFragmentJson.put("method", "disableFragment");
			// JSONObject disableFragmentObj = new JSONObject();
			// disableFragmentObj.put("fragmentUUID",
			// "FRGM7fd6af722f064610bf8dbb66676bd0ee");
			// disableFragmentJson.put("params", disableFragmentObj);

			// // 启用fragment
			// JSONObject enableFragmentJson = new JSONObject();
			// enableFragmentJson.put("method", "enableFragment");
			// JSONObject enableFragmentObj = new JSONObject();
			// enableFragmentObj.put("fragmentUUID",
			// "FRGM7fd6af722f064610bf8dbb66676bd0ee");
			// enableFragmentJson.put("params", enableFragmentObj);

			// // 删除fragment
			// JSONObject deleteFragmentJson = new JSONObject();
			// deleteFragmentJson.put("method", "deleteFragment");
			// JSONObject deleteFragmentObj = new JSONObject();
			// deleteFragmentObj.put("fragmentUUID",
			// "FRGM7fd6af722f064610bf8dbb66676bd0ee");
			// deleteFragmentJson.put("params", deleteFragmentObj);

			// 获取fragment列表
			// JSONObject getFragmentsJson = new JSONObject();
			// getFragmentsJson.put("method", "getFragments");
			// getFragmentsJson.put("params", "");

			// // 发布共享fragment
			// JSONObject releaseShareFragmentJson = new JSONObject();
			// releaseShareFragmentJson.put("method", "releaseShareFragment");
			// JSONObject releaseShareFragmentObj = new JSONObject();
			// releaseShareFragmentObj.put("shareFragmentUUID",
			// "FRGMaa3a42fc184f4943b0f6035baedec0fd");
			// releaseShareFragmentObj.put("shareFragmentName", "foxQuery");
			// releaseShareFragmentObj.put("shareFragmentType", "query");
			// releaseShareFragmentObj.put("shareFragmentDesc",
			// "我的第一个分享fragment");
			// releaseShareFragmentObj.put("shareFragmentComment",
			// "huangboning分享fragment");
			// releaseShareFragmentObj.put("shareFragmentExpression", "select *
			// from t_test where name='test' update");
			// releaseShareFragmentJson.put("params", releaseShareFragmentObj);

			// // 更新场景
			// JSONObject updateSceneJson = new JSONObject();
			// updateSceneJson.put("method", "updateScenario");
			// JSONObject updateSceneObj = new JSONObject();
			// updateSceneObj.put("sceneUUID",
			// "SCNObd1d9a66bca44137a70fd766558f56a8");
			// updateSceneObj.put("sceneComment",
			// "huangboning向场景中更新第一个fragment");
			// updateSceneObj.put("sceneExpression",
			// "{\"fragmentList\":\"[{\"fragmentUUID\":\"FRGM001UPDATE\"}]\"}");
			// updateSceneJson.put("params", updateSceneObj);

			 // 获取共享fragment列表
			 JSONObject getShareFragmentsJson = new JSONObject();
			 getShareFragmentsJson.put("method", "getShareFragments");
			 getShareFragmentsJson.put("params", "");
			
			String result = ApacheHttpUtil.testSceneBySession("http://localhost:8080/query/service/v1/main.do",
					loginJson.toString().getBytes("utf-8"), switchSceneJson.toString().getBytes("utf-8"), null,
					getShareFragmentsJson.toString().getBytes("utf-8"));
			System.out.println(getShareFragmentsJson.toString());
			System.out.println(result);

			// // 获取fragment
			// JSONObject getFragmentJson = new JSONObject();
			// getFragmentJson.put("method", "getFragment");
			// JSONObject getFragmentObj = new JSONObject();
			// getFragmentObj.put("version",
			// "af975235a63239f9c1b396fe32203bfae6b6de12");
			// getFragmentJson.put("params", getFragmentObj);

			// String result = ApacheHttpUtil.sendPostBySession(
			// "http://localhost:8080/zqQuery/service/v1/main.do",
			// loginJson.toString().getBytes("utf-8"), deleteFragmentJson
			// .toString().getBytes("utf-8"));
			// System.out.println(deleteFragmentJson.toString());
			// System.out.println(result);

			// // 禁用共享fragment
			// JSONObject disableShareFragmentJson = new JSONObject();
			// disableShareFragmentJson.put("method", "disableShareFragment");
			// JSONObject disableShareFragmentObj = new JSONObject();
			// disableShareFragmentObj.put("id",
			// "FRGM3b617aa7be24475699f73b7ce47b3bec");
			// disableShareFragmentJson.put("params", disableShareFragmentObj);

			// // 启用共享fragment
			// JSONObject enableShareFragmentJson = new JSONObject();
			// enableShareFragmentJson.put("method", "enableShareFragment");
			// JSONObject enableShareFragmentObj = new JSONObject();
			// enableShareFragmentObj.put("id",
			// "FRGM3b617aa7be24475699f73b7ce47b3bec");
			// enableShareFragmentJson.put("params", enableShareFragmentObj);

			// // 获取共享fragment列表
			// JSONObject getFragmentsJson = new JSONObject();
			// getFragmentsJson.put("method", "getShareFragments");
			// getFragmentsJson.put("params", "");

			// // 创建变量
			// JSONObject createVariableJson = new JSONObject();
			// createVariableJson.put("method", "createVariable");
			// JSONObject createVariableObj = new JSONObject();
			// createVariableObj.put("variableScope", 0);
			// createVariableObj.put("variableName", "sencond Variable");
			// createVariableObj.put("variableDesc", "我的第二个变量");
			// createVariableObj.put("variableShare", 0);
			// createVariableJson.put("params", createVariableObj);

			// // 更新变量
			// JSONObject updateVariableJson = new JSONObject();
			// updateVariableJson.put("method", "updateVariable");
			// JSONObject updateVariableObj = new JSONObject();
			// updateVariableObj.put("variableId",
			// "VAR3055aa1577e24bbc8835c441669976c6");
			// updateVariableObj.put("variableScope", 0);
			// updateVariableObj.put("variableDesc", "update 1 变量");
			// updateVariableObj.put("variableShare", 0);
			// updateVariableJson.put("params", updateVariableObj);

			// // 获取变量
			// JSONObject getVariableJson = new JSONObject();
			// getVariableJson.put("method", "getVariable");
			// JSONObject getVariableObj = new JSONObject();
			// getVariableObj.put("version",
			// "af975235a63239f9c1b396fe32203bfae6b6de12");
			// getVariableJson.put("params", getVariableObj);

			// // 删除变量
			// JSONObject deleteVariableJson = new JSONObject();
			// deleteVariableJson.put("method", "deleteVariable");
			// JSONObject deleteVariableObj = new JSONObject();
			// deleteVariableObj.put("id",
			// "VAR3055aa1577e24bbc8835c441669976c6");
			// deleteVariableJson.put("params", deleteVariableObj);

			// // 禁用共享变量
			// JSONObject disableShareVariableJson = new JSONObject();
			// disableShareVariableJson.put("method", "disableShareVariable");
			// JSONObject disableShareVariableObj = new JSONObject();
			// disableShareVariableObj.put("id",
			// "VAR4b237122c1b241e69176329d698e1eaa");
			// disableShareVariableJson.put("params", disableShareVariableObj);

			// // 启用共享变量
			// JSONObject enableShareVariableJson = new JSONObject();
			// enableShareVariableJson.put("method", "enableShareVariable");
			// JSONObject enableShareVariableObj = new JSONObject();
			// enableShareVariableObj.put("id",
			// "VAR4b237122c1b241e69176329d698e1eaa");
			// enableShareVariableJson.put("params", enableShareVariableObj);

			// // 获取变量列表
			// JSONObject getVariablesJson = new JSONObject();
			// getVariablesJson.put("method", "getVariables");
			// getVariablesJson.put("params", "");

			// // 获取共享变量列表
			// JSONObject getVariablesJson = new JSONObject();
			// getVariablesJson.put("method", "getShareVariables");
			// getVariablesJson.put("params", "");
			
			// String result =
			// ApacheHttpUtil.sendPostBySession("http://192.168.1.32:8080/query/service/v1/main.do",
			// loginJson.toString().getBytes("utf-8"),
			// listSceneJson.toString().getBytes("utf-8"));
			// System.out.println(listSceneJson.toString());
			// System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getSceneJson() {
		JSONObject sceneJson = new JSONObject();
		sceneJson.put("id", "SCNO8a45aae29e3d452c8cb6c4ee1857cd59");
		sceneJson.put("name", "test Scenario");
		sceneJson.put("desc", "my first scenario");
		sceneJson.put("version", "");
		sceneJson.put("commonet", "add scenario");
		JSONArray scopeObjs = new JSONArray();
		scopeObjs.add("allindicies.unified");
		scopeObjs.add("aminno.entities");
		scopeObjs.add("voter_fl.entities");
		sceneJson.put("scope", scopeObjs.toString());
		sceneJson.put("createTime", "2016-04-25 11:45:23");
		JSONArray fragmentObjs = new JSONArray();
		JSONObject fragmentJson1 = new JSONObject();
		fragmentJson1.put("id", "FRGMc1803bf6c3c347d3a46ae86c3be4e14a");
		fragmentJson1.put("name", "test fragment");
		fragmentJson1.put("desc", "my first fragment");
		fragmentJson1.put("type", "filter");
		fragmentJson1.put("isShare", 0);
		fragmentJson1.put("enable", true);
		JSONObject fragmentJson2 = new JSONObject();
		fragmentJson2.put("id", "FRGMc5dd8fac58054db0b294d03535cc2433");
		fragmentJson2.put("name", "hbnfragment");
		fragmentJson2.put("desc", "haha");
		fragmentJson2.put("type", "query");
		fragmentJson2.put("isShare", 0);
		fragmentJson2.put("enable", true);
		fragmentObjs.add(fragmentJson1);
		fragmentObjs.add(fragmentJson2);
		sceneJson.put("fragmentList", fragmentObjs.toArray());
		JSONArray variableObjs = new JSONArray();
		JSONObject variableJson1 = new JSONObject();
		variableJson1.put("id", "VARC5444bb041b8441878536c9b3a6840e8b");
		variableJson1.put("name", "my first variable");
		variableJson1.put("scope", 0);// 0是scenario表示全局变量，1fragment表示fragment内的局部变量
		variableJson1.put("isShare", 0);
		variableJson1.put("enable", true);
		variableJson1.put("type", "string");
		variableJson1.put("value", "tom");
		JSONObject variableJson2 = new JSONObject();
		variableJson2.put("id", "VARI68ec103269d547b6906c329e3a583364");
		variableJson2.put("name", "my first variable");
		variableJson2.put("scope", 1);// 0是scenario表示全局变量，1fragment表示fragment内的局部变量
		variableJson2.put("fragmentId", "FRGMc5dd8fac58054db0b294d03535cc2433");
		variableJson2.put("isShare", 0);
		variableJson2.put("enable", true);
		variableJson2.put("type", "string");
		variableJson2.put("value", "tom");
		variableObjs.add(variableJson1);
		variableObjs.add(variableJson2);
		sceneJson.put("variableList", variableObjs.toString());
		return sceneJson.toString();
	}
}
