package com.studio.query.action;

import com.studio.query.common.ApacheHttpUtil;
import com.studio.query.util.FileUtil;

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

			// 设置数据源
			JSONObject setScopeJson = new JSONObject();
			setScopeJson.put("method", "setScope");
			setScopeJson.put("params", "");
			JSONObject setScopeObj = new JSONObject();
			setScopeObj.put("scope", "aminno.entities");
			setScopeJson.put("params", setScopeObj);

			JSONObject setScopeJson2 = new JSONObject();
			setScopeJson2.put("method", "setScope");
			setScopeJson2.put("params", "");
			JSONObject setScopeObj2 = new JSONObject();
			setScopeObj2.put("scope", "aminno.entities22");
			setScopeJson2.put("params", setScopeObj2);

			// // 获取选择数据源定义的数据表头
			// JSONObject headJson = new JSONObject();
			// headJson.put("method", "getTableHeadDef");
			// headJson.put("params", "");

			// 创建场景
			JSONObject createSceneJson = new JSONObject();
			createSceneJson.put("method", "createScene");
			JSONObject createSceneObj = new JSONObject();
			createSceneObj.put("sceneName", "foxquery2");
			createSceneObj.put("sceneDesc", "我的第三个场景foxquery");
			createSceneJson.put("params", createSceneObj);

			// 获取场景列表
			JSONObject listSceneJson = new JSONObject();
			listSceneJson.put("method", "getScenarioes");
			JSONObject listSceneObj = new JSONObject();
			listSceneJson.put("params", listSceneObj);

			// 获取场景历史版本
			JSONObject historySceneJson = new JSONObject();
			historySceneJson.put("method", "getScenarioHistory");
			JSONObject historySceneObj = new JSONObject();
			historySceneObj.put("sceneUUID", "SCNO12c2b35ff3394866ab12cf15ffbecfbb");
			historySceneJson.put("params", historySceneObj);

			// 切换场景
			JSONObject switchSceneJson = new JSONObject();
			switchSceneJson.put("method", "switchScenario");
			JSONObject switchSceneObj = new JSONObject();
			switchSceneObj.put("sceneUUID", "SCNO7ad652da15cd4172b042c901d0dce3c3");
			switchSceneJson.put("params", switchSceneObj);

			// 切换版本
			JSONObject switchVersionJson = new JSONObject();
			switchVersionJson.put("method", "switchVersion");
			JSONObject switchVersionObj = new JSONObject();
			switchVersionObj.put("sceneVersion", "42f4f0bddd44624bad920a785ab95c8bdf7a7a6c");
			switchVersionJson.put("params", switchVersionObj);

			// 获取某版本场景
			JSONObject currentVersionJson = new JSONObject();
			currentVersionJson.put("method", "getCurrentVersion");
			currentVersionJson.put("params", "");

			// 更新场景
			JSONObject updateSceneJson = new JSONObject();
			updateSceneJson.put("method", "updateScenario");
			JSONObject updateSceneObj = new JSONObject();
			updateSceneObj.put("sceneComment", "huangboning update scene 1");

			updateSceneJson.put("params", updateSceneObj);

			// 创建fragment
			JSONObject createFragmentJson = new JSONObject();
			createFragmentJson.put("method", "createFragment");
			JSONObject createFragmentObj = new JSONObject();
			createFragmentObj.put("fragmentName", "FRAM1");
			createFragmentObj.put("fragmentType", "filter");
			createFragmentObj.put("fragmentObjType", "directInstance");
			createFragmentObj.put("fragmentDesc", "my fram");
			createFragmentObj.put("fragmentExpression", "{expression here}");
			createFragmentJson.put("params", createFragmentObj);

			// 获取fragment
			JSONObject getFragmentJson = new JSONObject();
			getFragmentJson.put("method", "getFragment");
			JSONObject getFragmentObj = new JSONObject();
			getFragmentObj.put("fragmentUUID", "FRGMe191a9b9215641308463f1ff46ee5786");
			getFragmentJson.put("params", getFragmentObj);

			// 更新fragment
			JSONObject updateFragmentJson = new JSONObject();
			updateFragmentJson.put("method", "updateFragment");
			JSONObject updateFragmentObj = new JSONObject();
			updateFragmentObj.put("fragmentUUID", "FRGMe191a9b9215641308463f1ff46ee5786");
			updateFragmentObj.put("fragmentName", "foxFragmentupdate");
			updateFragmentObj.put("fragmentType", "filterupdate");
			updateFragmentObj.put("fragmentDesc", "update foxFragment");
			updateFragmentObj.put("fragmentExpression", "{\"key\":\"960\"}");
			updateFragmentJson.put("params", updateFragmentObj);

			// 删除fragment
			JSONObject deleteFragmentJson = new JSONObject();
			deleteFragmentJson.put("method", "deleteFragment");
			JSONObject deleteFragmentObj = new JSONObject();
			deleteFragmentObj.put("fragmentUUID", "FRGMe191a9b9215641308463f1ff46ee5786");
			deleteFragmentJson.put("params", deleteFragmentObj);

			// // 关闭场景版本
			// JSONObject closeSceneJson = new JSONObject();
			// closeSceneJson.put("method", "closeScenario");
			// JSONObject closeSceneObj = new JSONObject();
			// closeSceneObj.put("sceneVersion",
			// "235c0729afe31e95b0f44d18f9b998f15f7c9b90");
			// closeSceneJson.put("params", closeSceneObj);

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
			// "huangboning发布的第四个版本fragment");
			// releaseShareFragmentObj.put("shareFragmentExpression", "select *
			// from t_test where name='test' update 4 new version");
			// releaseShareFragmentJson.put("params", releaseShareFragmentObj);

			// // 获取共享fragment列表
			// JSONObject getShareFragmentsJson = new JSONObject();
			// getShareFragmentsJson.put("method", "getShareFragments");
			// getShareFragmentsJson.put("params", "");

			// // 获取共享fragment历史版本
			// JSONObject shareFragmentHistoryJson = new JSONObject();
			// shareFragmentHistoryJson.put("method",
			// "getShareFragmentHistory");
			// JSONObject shareFragmentHistoryObj = new JSONObject();
			// shareFragmentHistoryObj.put("shareFragmentUUID",
			// "FRGMaa3a42fc184f4943b0f6035baedec0fd");
			// shareFragmentHistoryJson.put("params", shareFragmentHistoryObj);

			// // 获取某个版本共享fragment
			// JSONObject getShareFragmentVersionJson = new JSONObject();
			// getShareFragmentVersionJson.put("method",
			// "getShareFragmentVersion");
			// JSONObject getShareFragmentVersionObj = new JSONObject();
			// getShareFragmentVersionObj.put("shareFragmentVersion",
			// "e8e623820c74a6cd1b89b1a2242cb6e678452300");
			// getShareFragmentVersionObj.put("shareFragmentUUID",
			// "FRGMaa3a42fc184f4943b0f6035baedec0fd");
			// getShareFragmentVersionJson.put("params",
			// getShareFragmentVersionObj);

			// // 创建变量
			// JSONObject createVariableJson = new JSONObject();
			// createVariableJson.put("method", "createVariable");
			// JSONObject createVariableObj = new JSONObject();
			// createVariableObj.put("variableScope", 1);
			// createVariableObj.put("fragmentUUID",
			// "FRGMaa3a42fc184f4943b0f6035baedec0fd");
			// createVariableObj.put("variableName", "third Variable");
			// createVariableObj.put("variableType", "vType");
			// createVariableObj.put("variableObjType", "voType");
			// createVariableObj.put("variableDesc", "我的第二个变量");

			// // 更新变量
			// JSONObject updateVariableJson = new JSONObject();
			// updateVariableJson.put("method", "updateVariable");
			// JSONObject updateVariableObj = new JSONObject();
			// updateVariableObj.put("variableUUID",
			// "VARdf3d803d0a4646949d0a62d81af6c893");
			// updateVariableObj.put("variableScope", 1);
			// updateVariableObj.put("fragmentUUID",
			// "FRGMaa3a42fc184f4943b0f6035baedec0fd");
			// updateVariableObj.put("variableName", "third Variable update");
			// updateVariableObj.put("variableType", "vType update");
			// updateVariableObj.put("variableObjType", "voType update");
			// updateVariableObj.put("variableDesc", "我的第二个变量 update");
			// updateVariableJson.put("params", updateVariableObj);

			// // 删除变量
			// JSONObject deleteVariableJson = new JSONObject();
			// deleteVariableJson.put("method", "deleteVariable");
			// JSONObject deleteVariableObj = new JSONObject();
			// deleteVariableObj.put("variableUUID",
			// "VARdf3d803d0a4646949d0a62d81af6c893");
			// deleteVariableJson.put("params", deleteVariableObj);

			// // 获取变量列表
			// JSONObject getVariablesJson = new JSONObject();
			// getVariablesJson.put("method", "getVariables");
			// getVariablesJson.put("params", "");

			// // // 获取共享变量列表
			// JSONObject getShareVariablesJson = new JSONObject();
			// getShareVariablesJson.put("method", "getShareVariables");
			// getShareVariablesJson.put("params", "");

			// // 发布共享变量
			// JSONObject releaseShareVariableJson = new JSONObject();
			// releaseShareVariableJson.put("method", "releaseShareVariable");
			// JSONObject releaseShareVariableObj = new JSONObject();
			// releaseShareVariableObj.put("shareVariableUUID",
			// "VAR4fbdb8ec8ac04fdd94f059571f6e4e4a");
			// releaseShareVariableObj.put("shareVariableName",
			// "foxVariableUpdate");
			// releaseShareVariableObj.put("shareVariableType", "vTypeUpdate");
			// releaseShareVariableObj.put("shareVariableObjType",
			// "vObjTypeUpdate");
			// releaseShareVariableObj.put("shareVariableScope", 1);
			// releaseShareVariableObj.put("fragmentUUID",
			// "FRGMaa3a42fc184f4943b0f6035baedec0fd");
			// releaseShareVariableObj.put("shareVariableDesc",
			// "我更新第一个分享Variable");
			// releaseShareVariableObj.put("shareVariableComment",
			// "huangboning更新第一个版本变量");
			// releaseShareVariableObj.put("shareVariableExpression",
			// "select * from t_variable where name='test' first update
			// content");
			// releaseShareVariableJson.put("params", releaseShareVariableObj);

			// // 获取共享变量列表
			// JSONObject getShareVariableJson = new JSONObject();
			// getShareVariableJson.put("method", "getShareVariables");
			// getShareVariableJson.put("params", "");

			// // 获取共享变量历史版本
			// JSONObject shareVariableHistoryJson = new JSONObject();
			// shareVariableHistoryJson.put("method",
			// "getShareVariableHistory");
			// JSONObject shareVariableHistoryObj = new JSONObject();
			// shareVariableHistoryObj.put("shareVariableUUID",
			// "VAR4fbdb8ec8ac04fdd94f059571f6e4e4a");
			// shareVariableHistoryJson.put("params", shareVariableHistoryObj);

			// // 获取某个版本共享变量
			// JSONObject getShareVariableVersionJson = new JSONObject();
			// getShareVariableVersionJson.put("method",
			// "getShareVariableVersion");
			// JSONObject getShareVariableVersionObj = new JSONObject();
			// getShareVariableVersionObj.put("shareVariableVersion",
			// "4bf6433cd6a46d82c7aa5ecef70a9e5c6f556008");
			// getShareVariableVersionObj.put("shareVariableUUID",
			// "VAR4fbdb8ec8ac04fdd94f059571f6e4e4a");
			// getShareVariableVersionJson.put("params",
			// getShareVariableVersionObj);

			// ES接口 getIndexDocTypes
			JSONObject getInitDocTypeJson = new JSONObject();
			getInitDocTypeJson.put("method", "getIndexDocTypes");
			getInitDocTypeJson.put("params", "");

			String result = ApacheHttpUtil.testSceneBySession("http://localhost:8080/query/V1/index/getIndexDocTypes",
					loginJson.toString().getBytes("utf-8"), switchSceneJson.toString().getBytes("utf-8"), null, null,
					listSceneJson.toString().getBytes("utf-8"));
			System.out.println(listSceneJson.toString());
			System.out.println(result);

			// // ES接口 getHintFields
			// JSONObject getHintFieldsJson = new JSONObject();
			// getHintFieldsJson.put("method", "getHintFields");
			// JSONObject getHintFieldsObj = new JSONObject();
			// getHintFieldsObj.put("indexDocType", "voter_fl.entities");
			// getHintFieldsObj.put("fieldEffective", "");
			// getHintFieldsJson.put("params", getHintFieldsObj);

			// // ES接口 getInputTypes
			// JSONObject getInputTypesJson = new JSONObject();
			// getInputTypesJson.put("method", "getInputTypes");
			// JSONObject getInputTypesObj = new JSONObject();
			// getInputTypesObj.put("indexDocType", "voter_fl.entities");
			// getInputTypesObj.put("fieldId", "profile.gender_word");
			// getInputTypesObj.put("fragmentType", "filter");
			// getInputTypesJson.put("params", getInputTypesObj);

			// // ES接口 getTableHeadDef
			// JSONObject getTableHeadDefJson = new JSONObject();
			// getTableHeadDefJson.put("method", "getTableHeadDef");
			// // JSONObject getTableHeadDefObj = new JSONObject();
			// // JSONArray scopeArray = new JSONArray();
			// // scopeArray.add("voter_fl.entities");
			// // getTableHeadDefObj.put("scopes", scopeArray);
			// getTableHeadDefJson.put("params", "");

			// // ES接口 getHelpValue
			// JSONObject getHelpValueJson = new JSONObject();
			// getHelpValueJson.put("method", "getHelpValue");
			// JSONObject etHelpValueObj = new JSONObject();
			// etHelpValueObj.put("indexDocTypeId", "voter_fl.entities");
			// etHelpValueObj.put("fieldId", "profile.gender_word");
			// // 测试，这里服务器自动加工转化
			// // etHelpValueObj.put("indexId", "voter_fl");
			// // etHelpValueObj.put("docTypeId", "entities");
			// // etHelpValueObj.put("fieldId", "profile.gender_word");
			// getHelpValueJson.put("params", etHelpValueObj);

			// // ES接口 getGeocoding
			// JSONObject getGeocodingJson = new JSONObject();
			// getGeocodingJson.put("method", "getGeocoding");
			// JSONObject getGeocodingObj = new JSONObject();
			// getGeocodingObj.put("addressName", "beijing");
			// getGeocodingJson.put("params", getGeocodingObj);

			// // ES接口 executeScene
			// JSONObject executeSceneJson = new JSONObject();
			// executeSceneJson.put("method", "executeScenario");
			// String bodyString =
			// FileUtil.readFile("D:/hbn/workspaces/query/src/main/resources/valid.txt");
			// executeSceneJson.put("params", bodyString);

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

			// // 获取变量
			// JSONObject getVariableJson = new JSONObject();
			// getVariableJson.put("method", "getVariable");
			// JSONObject getVariableObj = new JSONObject();
			// getVariableObj.put("version",
			// "af975235a63239f9c1b396fe32203bfae6b6de12");
			// getVariableJson.put("params", getVariableObj);

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
