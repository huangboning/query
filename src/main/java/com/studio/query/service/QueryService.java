package com.studio.query.service;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.common.HttpUtil;
import com.studio.query.entity.DataSource;
import com.studio.query.entity.HeadData;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class QueryService {
	/**
	 * 获取数据源列表
	 * 
	 * @return
	 */
	public String getIndexDocTypes(String bodyString) {
		String resultString = null;

		try {
			String str = HttpUtil.sendPost(Configure.esBootstrapServiceUrl, bodyString.getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataSource returnDataSource1 = new DataSource();
		returnDataSource1.setDataSourceId("所有数据源.归一化映射");
		returnDataSource1.setDataSourceName("allindicies.unified");
		returnDataSource1.setDataSourceIsUnified("true");
		DataSource returnDataSource2 = new DataSource();
		returnDataSource2.setDataSourceId("AshleyMadison.实体信息");
		returnDataSource2.setDataSourceName("aminno.entities");
		returnDataSource2.setDataSourceIsUnified("false");

		JSONArray dataJsonArray = new JSONArray();

		JSONObject dataObj1 = new JSONObject();
		dataObj1.put("id", returnDataSource1.getDataSourceId());
		dataObj1.put("name", returnDataSource1.getDataSourceName());
		dataObj1.put("isUnified", returnDataSource1.getDataSourceIsUnified());
		dataJsonArray.add(dataObj1);
		JSONObject dataObj2 = new JSONObject();
		dataObj2.put("id", returnDataSource2.getDataSourceId());
		dataObj2.put("name", returnDataSource2.getDataSourceName());
		dataObj2.put("isUnified", returnDataSource2.getDataSourceIsUnified());
		dataJsonArray.add(dataObj2);

		resultString = StringUtil.packetObject(MethodCode.GET_INDEX_DOC_TYPES, ParameterCode.Result.RESULT_OK, "",
				"获取数据源列表成功", dataJsonArray.toString());

		return resultString;
	}

	/**
	 * 选择数据源
	 * 
	 * @return
	 */
	public String setScope(String bodyString) {
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String scope = parmJb.optString("scope", "");
			if (StringUtil.isNullOrEmpty(scope)) {

				resultString = StringUtil.packetObject(MethodCode.SET_SCOPE, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			// 这里设置数据源逻辑
			try {
				String str = HttpUtil.sendPost(Configure.esTalbleServiceUrl, bodyString.getBytes("utf-8"));
				System.out.println(str);
			} catch (Exception e) {
				e.printStackTrace();
			}

			resultString = StringUtil.packetObject(MethodCode.SET_SCOPE, ParameterCode.Result.RESULT_OK, "", "设置数据源成功",
					"");

		}
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
		try {
			String str = HttpUtil.sendPost(Configure.esTalbleServiceUrl, bodyString.getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		HeadData returnHeadData1 = new HeadData();
		returnHeadData1.setHeadDataId("fullname");
		returnHeadData1.setHeadDataName("姓名");
		HeadData returnHeadData2 = new HeadData();
		returnHeadData2.setHeadDataId("gender");
		returnHeadData2.setHeadDataName("性别");

		JSONArray dataJsonArray = new JSONArray();

		JSONObject dataObj1 = new JSONObject();
		dataObj1.put("id", returnHeadData1.getHeadDataId());
		dataObj1.put("name", returnHeadData1.getHeadDataName());
		dataJsonArray.add(dataObj1);
		JSONObject dataObj2 = new JSONObject();
		dataObj2.put("id", returnHeadData2.getHeadDataId());
		dataObj2.put("name", returnHeadData2.getHeadDataName());
		dataJsonArray.add(dataObj2);

		resultString = StringUtil.packetObject(MethodCode.GET_TABLE_HEAD_DEF, ParameterCode.Result.RESULT_OK, "",
				"获取选择数据源定义的数据表头成功", dataJsonArray.toString());

		return resultString;
	}

	/**
	 * 获取提示字段
	 * 
	 * @return
	 */
	public String getHelpValue(String bodyString) {
		String resultString = null;
		//
		try {
			String str = HttpUtil.sendPost(Configure.esHintServiceUrl, bodyString.getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.GET_HELP_VALUE, ParameterCode.Result.RESULT_OK, "",
				"获取提示字段成功", "");

		return resultString;
	}

	/**
	 * 获取字段值
	 * 
	 * @return
	 */
	public String getFieldValues(String bodyString) {
		String resultString = null;
		//
		try {
			String str = HttpUtil.sendPost(Configure.esTalbleServiceUrl, bodyString.getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.GET_FIELD_VALUES, ParameterCode.Result.RESULT_OK, "",
				"获取字段值成功", "");

		return resultString;
	}

	/**
	 * 获取一个DocTypes下面所有的field列表
	 * 
	 * @return
	 */
	public String getInputTypes(String bodyString) {
		String resultString = null;
		//
		try {
			String str = HttpUtil.sendPost(Configure.esTalbleServiceUrl, bodyString.getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.GET_INPUT_TYPES, ParameterCode.Result.RESULT_OK, "",
				"获取一个DocTypes下面所有的field列表成功", "");

		return resultString;
	}

	/**
	 * 获取field列表
	 * 
	 * @return
	 */
	public String getHintFields(String bodyString) {
		String resultString = null;
		//
		try {
			String str = HttpUtil.sendPost(Configure.esTalbleServiceUrl, bodyString.getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.GET_HINT_FIELDS, ParameterCode.Result.RESULT_OK, "",
				"获取field列表成功", "");

		return resultString;
	}

	/**
	 * 查询位置
	 * 
	 * @return
	 */
	public String getGeocoding(String bodyString) {
		String resultString = null;
		//
		try {
			String str = HttpUtil.sendPost(Configure.esGeocodingServiceUrl, bodyString.getBytes("utf-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.GET_GEOCODING, ParameterCode.Result.RESULT_OK, "", "查询位置成功",
				"");

		return resultString;
	}

	/**
	 * 查询位置
	 * 
	 * @return
	 */
	public String executeScenario(String bodyString) {
		String resultString = null;
		//
		try {
			// String str = HttpUtil.sendPost(Configure.esQueryServiceUrl,
			// bodyString.getBytes("utf-8"));
			// System.out.println(str);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost method = new HttpPost(Configure.esQueryServiceUrl);
			method.addHeader("Content-type", "application/json; charset=utf-8");
			method.setHeader("Accept", "application/json");
			method.setEntity(new StringEntity(bodyString, Charset.forName("UTF-8")));

			HttpResponse response = httpClient.execute(method);
			resultString = EntityUtils.toString(response.getEntity());
			System.out.println(resultString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultString = StringUtil.packetObject(MethodCode.EXECUTE_SCENE, ParameterCode.Result.RESULT_OK, "", "执行场景查询成功",
				"");

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
