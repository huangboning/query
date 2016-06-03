package com.studio.query.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.common.Constants;
import com.studio.query.common.HttpUtil;
import com.studio.query.entity.Fragment;
import com.studio.query.entity.HeadData;
import com.studio.query.entity.Scene;
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
public class QueryService {

	Logger loger = Logger.getLogger(QueryService.class);

	/**
	 * 获取数据源列表
	 * 
	 * @return
	 */
	public String getIndexDocTypes(String bodyString) {
		String resultString = null;

		List<Map<String, String>> indexList = (List<Map<String, String>>) CacheUtil.getCacheObject("mapIndex");
		if (indexList == null) {
			indexList = new ArrayList<Map<String, String>>();
		}
		resultString = StringUtil.packetObjectObj(MethodCode.GET_INDEX_DOC_TYPES, ParameterCode.Result.RESULT_OK,
				"获取数据源列表成功", indexList);

		return resultString;
	}

	/**
	 * 获取选择数据源定义的数据表头
	 * 
	 * @return
	 */
	public String getTableHeadDef(Map<String, Object> session) {
		String resultString = null;
		// 这里获取选择数据源定义的数据表头逻辑
		String tableHeadDefString = "";
		JSONArray tableHeadDefArray = new JSONArray();
		if (Configure.isDevelopment) {

			tableHeadDefString = FileUtil.readFile(Configure.rootPath + "/WEB-INF/classes/demo_table_head_def.txt");
			tableHeadDefArray = JSONArray.fromObject(tableHeadDefString);
		} else {
			try {
				JSONObject getTableHeadDefJson = new JSONObject();
				getTableHeadDefJson.put("method", "getTableHeadDef");
				JSONObject getTableHeadDefObj = new JSONObject();
				List<String> scopeArray = (ArrayList<String>) session.get(Constants.KEY_SET_SCOPE);
				if (scopeArray == null) {
					scopeArray = new ArrayList<String>();
				}
				getTableHeadDefObj.put("scopes", scopeArray);
				getTableHeadDefJson.put("params", getTableHeadDefObj);
				tableHeadDefString = HttpUtil.sendPost(Configure.esTalbleServiceUrl,
						getTableHeadDefJson.toString().getBytes("utf-8"));
				JSONObject resultObj = JSONObject.fromObject(tableHeadDefString);
				tableHeadDefArray = resultObj.getJSONArray("result");
			} catch (Exception e) {
				e.printStackTrace();
				loger.info(e.toString());
				loger.info("请求失败：" + Configure.esTalbleServiceUrl);
				resultString = StringUtil.packetObject(MethodCode.GET_TABLE_HEAD_DEF,
						ParameterCode.Error.SERVICE_INVALID, "获取选择数据源定义的数据表头失败", "");
				return resultString;
			}
		}

		resultString = StringUtil.packetObjectObj(MethodCode.GET_TABLE_HEAD_DEF, ParameterCode.Result.RESULT_OK,
				"获取选择数据源定义的数据表头成功", tableHeadDefArray);

		return resultString;
	}

	/**
	 * 获取提示字段
	 * 
	 * @return
	 */
	public String getHelpValue(String bodyString) {

		String resultString = null;
		// 这里获取提示字段
		String helpValueString = "";
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			if (Configure.isDevelopment) {

				helpValueString = FileUtil.readFile(Configure.rootPath + "/WEB-INF/classes/demo_help_value.txt");
			} else {
				String indexDocTypeId = parmJb.optString("indexDocTypeId", "");
				String fieldId = parmJb.optString("fieldId", "");
				if (StringUtil.isNullOrEmpty(indexDocTypeId) || StringUtil.isNullOrEmpty(fieldId)) {

					resultString = StringUtil.packetObject(MethodCode.GET_HELP_VALUE,
							ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
					return resultString;
				}

				try {
					// 注意这里要加工字段 （变成indexId和docTypeId和fieldId）

					String method = jb.optString("method", "");

					JSONObject getHelpValueJson = new JSONObject();
					getHelpValueJson.put("method", method);
					JSONObject getHelpValueObj = new JSONObject();
					if (!StringUtil.isNullOrEmpty(indexDocTypeId)) {
						String str[] = indexDocTypeId.split("\\.");
						if (str.length >= 2) {
							getHelpValueObj.put("indexId", str[0]);
							getHelpValueObj.put("docTypeId", str[1]);
						}
					}
					getHelpValueObj.put("fieldId", fieldId);
					getHelpValueJson.put("params", getHelpValueObj);

					helpValueString = HttpUtil.sendPost(Configure.esHintServiceUrl,
							getHelpValueJson.toString().getBytes("utf-8"));
				} catch (Exception e) {
					e.printStackTrace();
					loger.info(e.toString());
					loger.info("请求失败：" + Configure.esHintServiceUrl);
					resultString = StringUtil.packetObject(MethodCode.GET_HELP_VALUE,
							ParameterCode.Error.SERVICE_INVALID, "获取提示字段失败", "");
					return resultString;
				}
			}
			resultString = StringUtil.packetObject(MethodCode.GET_HELP_VALUE, ParameterCode.Result.RESULT_OK,
					"获取提示字段成功", helpValueString);
		}
		return resultString;
	}

	/**
	 * 获取一个DocTypes下面所有的field列表
	 * 
	 * @return
	 */
	public String getInputTypes(String bodyString) {
		// //
		// try {
		// String str = HttpUtil.sendPost(Configure.esTalbleServiceUrl,
		// bodyString.getBytes("utf-8"));
		// System.out.println(str);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String indexDocType = parmJb.optString("indexDocType", "");
			String fieldId = parmJb.optString("fieldId", "");
			String fragmentType = parmJb.optString("fragmentType", "");
			if (StringUtil.isNullOrEmpty(indexDocType) || StringUtil.isNullOrEmpty(fieldId)
					|| StringUtil.isNullOrEmpty(fragmentType)) {

				resultString = StringUtil.packetObject(MethodCode.GET_INPUT_TYPES,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			List<JSONObject> map = CacheUtil.getInputTypes(indexDocType, fieldId, fragmentType);
			resultString = StringUtil.packetObjectObj(MethodCode.GET_INPUT_TYPES, ParameterCode.Result.RESULT_OK,
					"获取一个DocTypes下面所有的field列表成功", map);
		}
		return resultString;
	}

	/**
	 * 获取field列表
	 * 
	 * @return
	 */
	public String getHintFields(String bodyString) {
		//
		// try {
		// String str = HttpUtil.sendPost(Configure.esTalbleServiceUrl,
		// bodyString.getBytes("utf-8"));
		// System.out.println(str);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String indexDocType = parmJb.optString("indexDocType", "");
			String fieldEffective = parmJb.optString("fieldEffective", "");
			if (StringUtil.isNullOrEmpty(indexDocType)) {

				resultString = StringUtil.packetObject(MethodCode.GET_HINT_FIELDS,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			List<JSONObject> map = CacheUtil.getHintFields(indexDocType, fieldEffective);

			resultString = StringUtil.packetObjectObj(MethodCode.GET_HINT_FIELDS, ParameterCode.Result.RESULT_OK,
					"获取HintField列表成功", map);
		}
		return resultString;
	}

	/**
	 * 查询位置
	 * 
	 * @return
	 */
	public String getGeocoding(String bodyString) {
		String resultString = null;
		// 这里获取查询地理字段
		String geocodingString = "";
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String addressName = parmJb.optString("addressName", "");
			if (StringUtil.isNullOrEmpty(addressName)) {

				resultString = StringUtil.packetObject(MethodCode.GET_GEOCODING, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			if (Configure.isDevelopment) {

				geocodingString = FileUtil.readFile(Configure.rootPath + "/WEB-INF/classes/demo_geocoding.txt");
			} else {
				try {
					geocodingString = HttpUtil.sendPost(Configure.esGeocodingServiceUrl, bodyString.getBytes("utf-8"));

				} catch (Exception e) {
					e.printStackTrace();
					loger.info(e.toString());
					loger.info("请求失败：" + Configure.esGeocodingServiceUrl);
					resultString = StringUtil.packetObject(MethodCode.GET_GEOCODING,
							ParameterCode.Error.SERVICE_INVALID, "查询位置失败", "");
					return resultString;
				}
			}
			resultString = StringUtil.packetObject(MethodCode.GET_GEOCODING, ParameterCode.Result.RESULT_OK, "查询位置成功",
					geocodingString);
		}
		return resultString;
	}

	/**
	 * 查询场景
	 * 
	 * @param bodyString
	 * @param session
	 * @return
	 */
	public String executeScenario(String bodyString, Map<String, Object> session) {
		String resultString = null;
		// 这里执行场景查询结果返回
		String executeScenarioString = "";
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		int position = 0;
		int recCount = 20;
		if (parmJb != null) {
			position = parmJb.optInt("position", 0);
			recCount = parmJb.optInt("recCount", 20);
		}

		if (Configure.isDevelopment) {

			executeScenarioString = FileUtil
					.readFile(Configure.rootPath + "/WEB-INF/classes/demo_execute_scenario.txt");
		} else {

			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.EXECUTE_SCENE, ParameterCode.Error.SERVICE_SESSION,
						"当前没设置活动场景，或会话已经过期！", "");
				return resultString;
			}
			executeScenarioString = this.executeQuery(position, recCount, session);

		}
		resultString = StringUtil.packetObject(MethodCode.EXECUTE_SCENE, ParameterCode.Result.RESULT_OK, "执行场景查询成功",
				executeScenarioString);
		return resultString;
	}

	/**
	 * 查询场景下一页
	 * 
	 * @param bodyString
	 * @param session
	 * @return
	 */
	public String nextPage(String bodyString, Map<String, Object> session) {
		String resultString = null;
		// 这里执行场景查询结果返回
		String executeScenarioString = "";
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		int position = 0;
		int recCount = 20;
		if (parmJb != null) {
			position = parmJb.optInt("position", 0);
			recCount = parmJb.optInt("recCount", 20);
		}

		if (Configure.isDevelopment) {

			executeScenarioString = FileUtil
					.readFile(Configure.rootPath + "/WEB-INF/classes/demo_execute_scenario.txt");
		} else {
			try {
				Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
				// 如果session中没有记录当前场景
				if (sceneActive == null) {
					resultString = StringUtil.packetObject(MethodCode.NEXT_PAGE, ParameterCode.Error.SERVICE_SESSION,
							"当前没设置活动场景，或会话已经过期！", "");
					return resultString;
				}
				executeScenarioString = this.executeQuery(position, recCount, session);

			} catch (Exception e) {
				e.printStackTrace();
				loger.info(e.toString());
				loger.info("请求失败：" + Configure.esQueryServiceUrl);
				resultString = StringUtil.packetObject(MethodCode.NEXT_PAGE, ParameterCode.Error.SERVICE_INVALID,
						"执行场景查询失败", "");
				return resultString;
			}
		}
		resultString = StringUtil.packetObject(MethodCode.NEXT_PAGE, ParameterCode.Result.RESULT_OK, "执行场景查询成功",
				executeScenarioString);
		return resultString;
	}

	/**
	 * 执行查询逻辑
	 * 
	 * @param position
	 * @param recCount
	 * @param session
	 * @return
	 */
	public String executeQuery(int position, int recCount, Map<String, Object> session) {
		String resultString = "";
		try {
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			JSONArray scopeObjs = new JSONArray();
			List<String> scopeArray = (ArrayList<String>) session.get(Constants.KEY_SET_SCOPE);
			if (scopeArray == null) {
				scopeArray = new ArrayList<String>();
			}
			for (String scopeStr : scopeArray) {
				scopeObjs.add(scopeStr);
			}
			JSONObject queryObj = new JSONObject();
			JSONObject sceneObj = new JSONObject();
			sceneObj.put("createTime", DateUtil.dateTimeFormat(sceneActive.getSceneDate()));
			sceneObj.put("scope", scopeObjs.toString());
			// sceneObj.put("name", sceneActive.getSceneName());
			// sceneObj.put("id", sceneActive.getSceneUUID());
			// sceneObj.put("tages", "[]");

			JSONArray fragmentListArray = new JSONArray();
			Map<String, Object> fragmentsMap = new HashMap<String, Object>();
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
				dataObj.put("id", fragment.getFragmentUUID());
				dataObj.put("name", fragment.getFragmentName());
				dataObj.put("desc", fragment.getFragmentDesc());
				dataObj.put("type", fragment.getFragmentType());
				dataObj.put("objectType", fragment.getFragmentObjType());
				dataObj.put("enable", fragment.isFragmentEnable());
				dataObj.put("version", "");
				if (fragment.isFragmentEnable()) {
					fragmentListArray.add(dataObj);
				}

			}
			sceneObj.put("fragmentList", fragmentListArray);
			queryObj.put("scenario", sceneObj.toString());

			for (int i = 0; i < fragmentList.size(); i++) {
				Fragment fragment = fragmentList.get(i);
				JSONObject dataObj = new JSONObject();
				dataObj.put("id", fragment.getFragmentUUID());
				dataObj.put("name", fragment.getFragmentName());
				dataObj.put("desc", fragment.getFragmentDesc());
				dataObj.put("type", fragment.getFragmentType());
				dataObj.put("objectType", fragment.getFragmentObjType());
				dataObj.put("tags", "[]");
				dataObj.put("version", "");
				dataObj.put("expression", fragment.getFragmentExpression());
				if (fragment.isFragmentEnable()) {
					fragmentsMap.put(fragment.getFragmentUUID(), dataObj);
				}

			}
			queryObj.put("fragments", fragmentsMap);
			JSONObject paginationObj = new JSONObject();
			paginationObj.put("size", recCount);
			paginationObj.put("from", position);
			queryObj.put("pagination", paginationObj);
			queryObj.put("method", "query");

			queryObj.put("variables", "[]");
			loger.info("executeScenario post data=" + queryObj.toString());

			resultString = HttpUtil.sendPost(Configure.esQueryServiceUrl, queryObj.toString().getBytes("utf-8"));

		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
			loger.info("请求失败：" + Configure.esQueryServiceUrl);
		}
		return resultString;
	}

	/**
	 * 验证表达式
	 * 
	 * @return
	 */
	public String validate(String bodyString, Map<String, Object> session) {

		// 这里返回验证表达式结果
		String validateExpressionString = "";

		if (Configure.isDevelopment) {
			validateExpressionString = "{\"isValid\":true,\"fragmentWithVarError\":[],\"results\":[],\"success\":true,\"varReferenced\":{}}";
		} else {

			validateExpressionString = this.executeQuery(0, 20, session);
		}
		loger.info("validate result=" + validateExpressionString);

		// 在更新fragment的时候做验证处理，如果验证成功则保存，如果验证失败则将失败信息返回。
		// JSONObject
		// validateObj=JSONObject.fromObject(validateExpressionString);
		// if (validateObj!=null) {
		//
		// boolean result=validateObj.optBoolean("isValid",false);
		// }else {
		// return false;
		// }
		return validateExpressionString;

	}

	public static void main(String[] args) {

		try {
			String str = HttpUtil.sendPost("http://10.2.118.16:8088/api/bootstrapService",
					"{\"method\":\"\"}".getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
