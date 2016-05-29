package com.studio.query.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.studio.query.common.Configure;
import com.studio.query.common.HttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CacheUtil {
	Logger loger = Logger.getLogger(CacheUtil.class);

	private final static Map<String, Object> objectCacheFactory = new HashMap<String, Object>();
	private static Map<String, String> stringCacheFactory = new HashMap<String, String>();

	public static void putCacheString(String key, String value) {
		stringCacheFactory.put(key, value);
	}

	public static String getCacheString(String key) {
		return stringCacheFactory.get(key);
	}

	public static void putCacheObject(String key, Object value) {
		objectCacheFactory.remove(key);
		objectCacheFactory.put(key, value);
	}

	public static Object getCacheObject(String key) {
		return objectCacheFactory.get(key);
	}

	public void initIndexDocTypes() {

		// 这里实现ES接口调用
		try {
			JSONObject getIndexDocTypesJson = new JSONObject();
			getIndexDocTypesJson.put("method", "getIndexDocTypes");
			JSONObject getIndexDocTypesObj = new JSONObject();
			getIndexDocTypesJson.put("params", getIndexDocTypesObj);
			String str = HttpUtil.sendPost(Configure.esBootstrapServiceUrl,
					getIndexDocTypesJson.toString().getBytes("utf-8"));
			CacheUtil cacheUtil = new CacheUtil();
			cacheUtil.getIndexDocTypesES(str);
			cacheUtil.getIndexDocTypesOriginES(str);
		} catch (Exception e) {
			e.printStackTrace();
			loger.info("获取ES数据源失败:" + Configure.esBootstrapServiceUrl);
			loger.info(e.toString());
		}
		// //测试初始化数据源
		// CacheUtil cacheUtil=new CacheUtil();
		// String
		// testPath=arg0.getServletContext().getRealPath("/")+"WEB-INF/classes/source.txt";
		// cacheUtil.getIndexDocTypes( testPath);
		// cacheUtil.getIndexDocTypesOrigin(testPath);
	}

	public void getIndexDocTypes(String path) {

		List<Map<String, String>> indexList = new ArrayList<Map<String, String>>();

		try {
			String sourceString = FileUtil.readFile(path);
			JSONObject sourceObj = JSONObject.fromObject(sourceString);
			JSONArray indexDefArray = sourceObj.getJSONArray("result");
			for (int i = 0; i < indexDefArray.size(); i++) {
				Map<String, String> indexMap = new HashMap<String, String>();

				JSONObject infoObj = indexDefArray.getJSONObject(i);
				JSONObject indexDefObj = infoObj.getJSONObject("indexDef");
				JSONObject indexDocTypeObj = infoObj.getJSONObject("docTypeDef");

				String indexId = indexDefObj.optString("id", "");
				String indexName = indexDefObj.optString("name", "");
				String docDefId = indexDocTypeObj.optString("id", "");
				String docDefName = indexDocTypeObj.optString("name", "");
				String isUnified = infoObj.optString("isUnified", "");

				indexMap.put("id", indexId.concat(".").concat(docDefId));
				indexMap.put("name", indexName.concat(".").concat(docDefName));
				indexMap.put("isUnified", isUnified);

				indexList.add(indexMap);

				// 保存availableOperators
				JSONArray availableOperatorsJsonArray = infoObj.getJSONArray("availableOperators");
				CacheUtil.putCacheObject(indexId.concat(".").concat(docDefId).concat("availableOperators"),
						availableOperatorsJsonArray);
			}
			CacheUtil.putCacheObject("mapIndex", indexList);
			// JSONObject test = new JSONObject();
			// test.put("baseObject", CacheUtil.getCacheObject("mapIndex"));
			// System.out.println(test.toString());
			loger.info("init IndexDocTypes successful");
		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
		}
	}

	public void getIndexDocTypesES(String sourceString) {

		List<Map<String, String>> indexList = new ArrayList<Map<String, String>>();

		try {
			JSONObject sourceObj = JSONObject.fromObject(sourceString);
			JSONArray indexDefArray = sourceObj.getJSONArray("result");
			for (int i = 0; i < indexDefArray.size(); i++) {
				Map<String, String> indexMap = new HashMap<String, String>();

				JSONObject infoObj = indexDefArray.getJSONObject(i);
				JSONObject indexDefObj = infoObj.getJSONObject("indexDef");
				JSONObject indexDocTypeObj = infoObj.getJSONObject("docTypeDef");

				String indexId = indexDefObj.optString("id", "");
				String indexName = indexDefObj.optString("name", "");
				String docDefId = indexDocTypeObj.optString("id", "");
				String docDefName = indexDocTypeObj.optString("name", "");
				String isUnified = infoObj.optString("isUnified", "");

				indexMap.put("id", indexId.concat(".").concat(docDefId));
				indexMap.put("name", indexName.concat(".").concat(docDefName));
				indexMap.put("isUnified", isUnified);

				indexList.add(indexMap);

				// 保存availableOperators
				JSONArray availableOperatorsJsonArray = infoObj.getJSONArray("availableOperators");
				CacheUtil.putCacheObject(indexId.concat(".").concat(docDefId).concat("availableOperators"),
						availableOperatorsJsonArray);
			}
			CacheUtil.putCacheObject("mapIndex", indexList);
			// JSONObject test = new JSONObject();
			// test.put("baseObject", CacheUtil.getCacheObject("mapIndex"));
			// System.out.println(test.toString());
			loger.info("init IndexDocTypes successful");
		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
		}
	}

	public void getIndexDocTypesOrigin(String path) {

		List<Map<String, String>> indexList = new ArrayList<Map<String, String>>();

		try {
			String sourceString = FileUtil.readFile(path);
			JSONObject sourceObj = JSONObject.fromObject(sourceString);
			JSONArray indexDefArray = sourceObj.getJSONArray("result");
			for (int i = 0; i < indexDefArray.size(); i++) {
				Map<String, String> indexMap = new HashMap<String, String>();

				JSONObject infoObj = indexDefArray.getJSONObject(i);
				JSONObject indexDefObj = infoObj.getJSONObject("indexDef");
				JSONObject indexDocTypeObj = infoObj.getJSONObject("docTypeDef");

				String indexId = indexDefObj.optString("id", "");
				String indexName = indexDefObj.optString("name", "");
				String docDefId = indexDocTypeObj.optString("id", "");
				String docDefName = indexDocTypeObj.optString("name", "");
				String isUnified = infoObj.optString("isUnified", "");

				indexMap.put("id", indexId.concat(".").concat(docDefId));
				indexMap.put("name", indexName.concat(".").concat(docDefName));
				indexMap.put("isUnified", isUnified);

				indexList.add(indexMap);

				// 保存availableOperators
				JSONArray availableOperatorsJsonArray = infoObj.getJSONArray("availableOperators");
				CacheUtil.putCacheObject(indexId.concat(".").concat(docDefId).concat("availableOperatorsOrigin"),
						availableOperatorsJsonArray);
			}
			CacheUtil.putCacheObject("mapIndex", indexList);
			JSONObject test = new JSONObject();
			test.put("baseObject", CacheUtil.getCacheObject("mapIndex"));
			// System.out.println(test.toString());
			loger.info("init IndexDocTypesOrigin successful");
		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
		}
	}

	public void getIndexDocTypesOriginES(String sourceString) {

		List<Map<String, String>> indexList = new ArrayList<Map<String, String>>();

		try {
			JSONObject sourceObj = JSONObject.fromObject(sourceString);
			JSONArray indexDefArray = sourceObj.getJSONArray("result");
			for (int i = 0; i < indexDefArray.size(); i++) {
				Map<String, String> indexMap = new HashMap<String, String>();

				JSONObject infoObj = indexDefArray.getJSONObject(i);
				JSONObject indexDefObj = infoObj.getJSONObject("indexDef");
				JSONObject indexDocTypeObj = infoObj.getJSONObject("docTypeDef");

				String indexId = indexDefObj.optString("id", "");
				String indexName = indexDefObj.optString("name", "");
				String docDefId = indexDocTypeObj.optString("id", "");
				String docDefName = indexDocTypeObj.optString("name", "");
				String isUnified = infoObj.optString("isUnified", "");

				indexMap.put("id", indexId.concat(".").concat(docDefId));
				indexMap.put("name", indexName.concat(".").concat(docDefName));
				indexMap.put("isUnified", isUnified);

				indexList.add(indexMap);

				// 保存availableOperators
				JSONArray availableOperatorsJsonArray = infoObj.getJSONArray("availableOperators");
				CacheUtil.putCacheObject(indexId.concat(".").concat(docDefId).concat("availableOperatorsOrigin"),
						availableOperatorsJsonArray);
			}
			CacheUtil.putCacheObject("mapIndex", indexList);
			JSONObject test = new JSONObject();
			test.put("baseObject", CacheUtil.getCacheObject("mapIndex"));
			// System.out.println(test.toString());
			loger.info("init IndexDocTypesOrigin successful");
		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
		}
	}

	public static List<JSONObject> getHintFields(String scope, String hint) {

		List<JSONObject> matchMap = new ArrayList<JSONObject>();

		try {
			JSONArray availableOperatorsJsonArray = (JSONArray) CacheUtil
					.getCacheObject(scope.concat("availableOperators"));
			if (hint == null || hint.trim().equals("")) {
				for (int i = 0; i < availableOperatorsJsonArray.size(); i++) {
					JSONObject infoObj = availableOperatorsJsonArray.getJSONObject(i);
					String type = infoObj.optString("type", "");
					if (type != null && (type.startsWith("all") || type.equals("hint"))) {
						infoObj.remove("operators");
						matchMap.add(infoObj);
					}
				}
			} else {
				for (int i = 0; i < availableOperatorsJsonArray.size(); i++) {
					JSONObject infoObj = availableOperatorsJsonArray.getJSONObject(i);
					String fieldname = infoObj.optString("fieldname", "");
					String fieldEffective = infoObj.optString("fieldEffective", "");
					if (fieldname.contains(hint) || fieldEffective.contains(hint)) {
						infoObj.remove("operators");
						matchMap.add(infoObj);
					}
				}

			}
			// JSONObject test = new JSONObject();
			// test.put("baseObject", matchMap);
			// System.out.println(test.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchMap;
	}

	public static List<JSONObject> getInputTypes(String scope, String fieldEffective, String inputType) {
		List<JSONObject> matchMap = new ArrayList<JSONObject>();

		try {
			JSONArray availableOperatorsJsonArray = (JSONArray) CacheUtil
					.getCacheObject(scope.concat("availableOperatorsOrigin"));

			for (int i = 0; i < availableOperatorsJsonArray.size(); i++) {
				JSONObject infoObj = availableOperatorsJsonArray.getJSONObject(i);
				String availaOperFieldEffective = infoObj.optString("fieldEffective", "");
				String availaOperType = infoObj.optString("type", "");
				JSONArray operatorsJsonArray = new JSONArray();
				if (availaOperFieldEffective.equals(fieldEffective) && !availaOperType.equals("hint")) {
					operatorsJsonArray = infoObj.getJSONArray("operators");
					for (int j = 0; j < operatorsJsonArray.size(); j++) {
						JSONObject operObj = operatorsJsonArray.getJSONObject(j);
						String operType = operObj.optString("type", "");
						if (operType.equals(inputType)) {
							matchMap.add(operObj);
						}
					}
				}
			}
			// JSONObject test = new JSONObject();
			// test.put("baseObject", matchMap);
			// System.out.println(test.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchMap;
	}

	public static void main(String[] args) {
		CacheUtil c = new CacheUtil();
		c.getIndexDocTypes("E:/query/source.txt");
		c.getIndexDocTypesOrigin("E:/query/source.txt");
		c.getHintFields("voter_fl.entities", "profile");
		c.getInputTypes("voter_fl.entities", "profile.gender_word", "filter");
	}
}
