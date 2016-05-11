package com.studio.query.service;

import org.springframework.stereotype.Service;

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
}
