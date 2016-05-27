package com.studio.query.util;

import java.util.ArrayList;
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
					fragment.setFragmentActiveStr(obj.optString("fragmentActive", ""));
					fragment.setFragmentEnableStr(obj.optString("fragmentEnable", ""));
					fragment.setFragmentDateStr(obj.optString("fragmentCreateTime", ""));
					fragment.setFragmentExpression(obj.optString("fragmentExpression", ""));
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
					variable.setVariableName(obj.optString("variableName", ""));
					variable.setVariableType(obj.optString("variableType", ""));
					variable.setVariableObjType(obj.optString("variableObjType", ""));
					variable.setVariableScopeStr(obj.optString("variableScope", ""));
					variable.setFragmentUUID(obj.optString("fragmentUUID", ""));
					variable.setVariableDesc(obj.optString("variableDesc", ""));
					variable.setVariableDateStr(obj.optString("variableCreateTime", ""));
					variable.setVariableExpression(obj.optString("variableExpression", ""));
					variableList.add(variable);

				}
			}
		}
		return variableList;
	}
}
