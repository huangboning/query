package com.studio.query.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.studio.query.entity.Fragment;
import com.studio.query.entity.Variable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	/**
	 * 从场景json中解析出fragment数据
	 * 
	 * @param str
	 * @return
	 */
	public static List<Fragment> getFragmentFromSceneString(String str) {

		List<Fragment> fragmentList = new ArrayList<Fragment>();
		if (!StringUtil.isNullOrEmpty(str)) {
			JSONObject strObj = JSONObject.fromObject(str);
			JSONArray strFragmentArray = strObj.getJSONArray("fragmentList");
			if (strFragmentArray != null) {
				for (int i = 0; i < strFragmentArray.size(); i++) {

					JSONObject obj = strFragmentArray.getJSONObject(i);
					Fragment fragment = new Fragment();
					fragment.setFragmentUUID(obj.optString("fragmentUUID", ""));
					fragment.setFragmentName(obj.optString("fragmentName", ""));
					fragment.setFragmentDesc(obj.optString("fragmentDesc", ""));
					fragment.setFragmentType(obj.optString("fragmentType", ""));
					fragment.setFragmentObjType(obj.optString("fragmentObjType", ""));
					fragment.setFragmentActive(obj.optBoolean("fragmentActive", true));
					fragment.setFragmentEnable(obj.optBoolean("fragmentEnable", true));
					fragment.setFragmentDateStr(obj.optString("fragmentCreateTime", ""));
					fragment.setFragmentExpression(obj.optString("fragmentExpression", ""));
					fragmentList.add(fragment);
				}
			}
		}
		return fragmentList;
	}

	/**
	 * 从场景json中解析出fragment模板数据
	 * 
	 * @param str
	 * @return
	 */
	public static List<Fragment> getTemplateFromSceneString(String str) {

		List<Fragment> fragmentList = new ArrayList<Fragment>();
		if (!StringUtil.isNullOrEmpty(str)) {
			JSONObject strObj = JSONObject.fromObject(str);
			JSONArray strFragmentArray = strObj.getJSONArray("templateList");
			if (strFragmentArray != null) {
				for (int i = 0; i < strFragmentArray.size(); i++) {

					JSONObject obj = strFragmentArray.getJSONObject(i);
					Fragment fragment = new Fragment();
					fragment.setFragmentTemplateId(obj.optString("templateId", ""));
					fragment.setFragmentTemplateVersion(obj.optString("version", ""));
					fragment.setFragmentUUID(obj.optString("instanceId", ""));
					fragment.setFragmentName(obj.optString("fragmentName", ""));
					fragment.setFragmentDesc(obj.optString("fragmentDesc", ""));
					fragment.setFragmentType(obj.optString("fragmentType", ""));
					fragment.setFragmentObjType(obj.optString("fragmentObjType", ""));
					fragment.setFragmentActive(obj.optBoolean("fragmentActive", true));
					fragment.setFragmentEnable(obj.optBoolean("fragmentEnable", true));
					fragment.setFragmentDateStr(obj.optString("fragmentCreateTime", ""));
					fragment.setFragmentExpression(obj.optString("fragmentExpression", ""));
					fragment.setFragmentTemplateVariable(obj.optString("fragmentTemplateVariable", ""));
					fragmentList.add(fragment);
				}
			}
		}
		return fragmentList;
	}

	/**
	 * 从场景json中解析出变量数据
	 * 
	 * @param str
	 * @return
	 */
	public static List<Variable> getVariableFromSceneString(String str) {

		List<Variable> variableList = new ArrayList<Variable>();
		if (!StringUtil.isNullOrEmpty(str)) {
			JSONObject strObj = JSONObject.fromObject(str);
			JSONArray strVariableArray = strObj.getJSONArray("variableList");
			if (strVariableArray != null) {
				for (int i = 0; i < strVariableArray.size(); i++) {

					JSONObject obj = strVariableArray.getJSONObject(i);
					Variable variable = new Variable();
					variable.setVariableUUID(obj.optString("variableUUID", ""));
					variable.setVariableClassId(obj.optString("variableClassId", ""));
					variable.setFragmentUUID(obj.optString("fragmentUUID", ""));
					variable.setSceneUUID(obj.optString("scenarioUUID", ""));
					variable.setVariableName(obj.optString("variableName", ""));
					variable.setVariableType(obj.optString("variableType", ""));
					variable.setVariableValueType(obj.optString("variableValueType", ""));
					variable.setVariableFieldType(obj.optString("variableFieldType", ""));
					variable.setVariableValue(obj.optString("variableValue", ""));
					variable.setVariableScope(obj.optString("variableScope", ""));
					variable.setVariableDateStr(obj.optString("variableCreateTime", ""));
					variableList.add(variable);

				}
			}
		}
		return variableList;
	}

	/**
	 * 从场景json中解析出scope数据
	 * 
	 * @param str
	 * @return
	 */
	public static List<String> getScopeFromSceneString(String str) {

		List<String> scopeList = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(str)) {
			JSONObject strObj = JSONObject.fromObject(str);
			JSONArray strScopeArray = strObj.getJSONArray("scopeList");
			if (strScopeArray != null) {
				for (int i = 0; i < strScopeArray.size(); i++) {

					String scope = (String) strScopeArray.get(i);

					scopeList.add(scope);

				}
			}
		}
		return scopeList;
	}

	/**
	 * 从场景json中解析出desc数据
	 * 
	 * @param str
	 * @return
	 */
	public static String getDescFromSceneString(String str) {

		String descString = "";
		try {
			JSONObject strObj = JSONObject.fromObject(str);
			JSONObject sceneObj = strObj.getJSONObject("scene");
			if (sceneObj != null) {
				descString = sceneObj.optString("desc", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return descString;
	}

	/**
	 * 从场景json中解析出attr数据
	 * 
	 * @param str
	 * @return
	 */
	public static JSONObject getAttrFromSceneString(String str) {

		JSONObject attrObj = new JSONObject();
		try {
			JSONObject strObj = JSONObject.fromObject(str);
			if (null != strObj) {
				attrObj = strObj.getJSONObject("attr");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return attrObj;
	}

	public static List<Variable> getRefVariableList(JSONArray refVariableArray) {
		List<Variable> refVariableList = new ArrayList<>();
		for (int j = 0; j < refVariableArray.size(); j++) {
			JSONObject reVOj = refVariableArray.getJSONObject(j);
			Variable refVar = new Variable();
			refVar.setFragmentUUID(reVOj.optString("fragmentUUID", ""));
			refVar.setSceneUUID(reVOj.optString("scenarioUUID", ""));
			refVar.setVariableUUID(reVOj.optString("variableUUID", ""));
			refVar.setVariableClassId(reVOj.optString("variableClassId", ""));
			refVar.setVariableName(reVOj.optString("variableName", ""));
			refVar.setVariableType(reVOj.optString("variableType", ""));
			refVar.setVariableScope(reVOj.optString("variableScope", ""));
			refVar.setVariableFieldType(reVOj.optString("variableFieldType", ""));
			refVar.setVariableValueType(reVOj.optString("variableValueType", ""));
			refVar.setVariableValue(reVOj.optString("variableValue", ""));
			refVariableList.add(refVar);
		}
		return refVariableList;
	}
}
