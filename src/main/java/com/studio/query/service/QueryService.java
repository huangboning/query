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
import com.studio.query.common.HttpUtil;
import com.studio.query.entity.HeadData;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.CacheUtil;
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
		resultString = StringUtil.packetObjectObj(MethodCode.GET_INDEX_DOC_TYPES, ParameterCode.Result.RESULT_OK, "",
				"获取数据源列表成功", indexList);

		return resultString;
	}

	/**
	 * 获取选择数据源定义的数据表头
	 * 
	 * @return
	 */
	public String getTableHeadDef(String bodyString) {
		String resultString = null;
		// 这里获取选择数据源定义的数据表头逻辑
		String tableHeadDefString = "";
		try {
			tableHeadDefString = HttpUtil.sendPost(Configure.esTalbleServiceUrl, bodyString.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.GET_TABLE_HEAD_DEF, ParameterCode.Result.RESULT_OK, "",
				"获取选择数据源定义的数据表头成功", tableHeadDefString);

		return resultString;
	}

	/**
	 * 获取提示字段
	 * 
	 * @return
	 */
	public String getHelpValue(String bodyString) {
		// 注意这里要加工字段
		// Map<String, String> mapValues = new HashMap<String, String>();
		// mapValues.put("indexId", indexId);
		// mapValues.put("docTypeId", docTypeId);
		// mapValues.put("fieldId", fieldId);
		// rb.setParams(mapValues);
		String resultString = null;
		// 这里获取提示字段
		String helpValueString = "";
		try {
			helpValueString = HttpUtil.sendPost(Configure.esHintServiceUrl, bodyString.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
			resultString = StringUtil.packetObject(MethodCode.GET_HELP_VALUE, ParameterCode.Result.RESULT_FAIL,
					ParameterCode.Error.SERVICE_INVALID, "获取提示字段失败", "");
			return resultString;
		}

		resultString = StringUtil.packetObject(MethodCode.GET_HELP_VALUE, ParameterCode.Result.RESULT_OK, "",
				"获取提示字段成功", helpValueString);

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

				resultString = StringUtil.packetObject(MethodCode.GET_INPUT_TYPES, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			List<JSONObject> map = CacheUtil.getInputTypes(indexDocType, fieldId, fragmentType);
			resultString = StringUtil.packetObjectObj(MethodCode.GET_INPUT_TYPES, ParameterCode.Result.RESULT_OK, "",
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

				resultString = StringUtil.packetObject(MethodCode.GET_HINT_FIELDS, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			List<JSONObject> map = CacheUtil.getHintFields(indexDocType, fieldEffective);

			resultString = StringUtil.packetObjectObj(MethodCode.GET_HINT_FIELDS, ParameterCode.Result.RESULT_OK, "",
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
		try {
			geocodingString = HttpUtil.sendPost(Configure.esGeocodingServiceUrl, bodyString.getBytes("utf-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.GET_GEOCODING, ParameterCode.Result.RESULT_OK, "", "查询位置成功",
				geocodingString);

		return resultString;
	}

	/**
	 * 查询位置
	 * 
	 * @return
	 */
	public String executeScenario(String bodyString) {
		String resultString = null;
		//这里执行场景查询结果返回
		String executeScenarioString="";
		try {
			// String str = HttpUtil.sendPost(Configure.esQueryServiceUrl,
			// bodyString.getBytes("utf-8"));
			// System.out.println(str);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost method = new HttpPost(Configure.esQueryServiceUrl);
			method.addHeader("Content-type", "application/json; charset=utf-8");
			method.setHeader("Accept", "application/json");
			String bodyStringaa = FileUtil.readFile("D:/hbn/workspaces/query/src/main/resources/valid.txt");
			method.setEntity(new StringEntity(bodyStringaa, Charset.forName("UTF-8")));

			HttpResponse response = httpClient.execute(method);
			executeScenarioString = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.EXECUTE_SCENE, ParameterCode.Result.RESULT_OK, "", "执行场景查询成功",
				executeScenarioString);

		return resultString;
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
