package com.studio.zqquery.util;

import java.util.Calendar;

import net.sf.json.JSONObject;

/**
 * 
 * @author hbn
 * 
 */
public class StringUtil {

	public static boolean isNullOrEmpty(String s) {
		if (null == s)
			return true;
		s = s.trim();
		if ("".equals(s))
			return true;
		return false;
	}

	public static String packetObject(String optCode, String statusCode,
			String errorCode, String message, String baseObject) {
		JSONObject o = new JSONObject();
		o.put("optCode", optCode);
		o.put("statusCode", statusCode);
		o.put("errorCode", errorCode);
		o.put("message", message);
		o.put("baseObject", baseObject);
		return o.toString();
	}

	public static String createSceneNo() {
		String sceneNo = java.util.UUID.randomUUID().toString();
		sceneNo = sceneNo.substring(0, 8) + sceneNo.substring(9, 13)
				+ sceneNo.substring(14, 18) + sceneNo.substring(19, 23)
				+ sceneNo.substring(24);
		sceneNo = "SCNO" + sceneNo;
		return sceneNo;

	}
	public static String createFragmentNo() {
		String fragmentNo = java.util.UUID.randomUUID().toString();
		fragmentNo = fragmentNo.substring(0, 8) + fragmentNo.substring(9, 13)
				+ fragmentNo.substring(14, 18) + fragmentNo.substring(19, 23)
				+ fragmentNo.substring(24);
		fragmentNo = "FRGM" + fragmentNo;
		return fragmentNo;

	}
	public static String createVariableNo() {
		String variableNo = java.util.UUID.randomUUID().toString();
		variableNo = variableNo.substring(0, 8) + variableNo.substring(9, 13)
				+ variableNo.substring(14, 18) + variableNo.substring(19, 23)
				+ variableNo.substring(24);
		variableNo = "VAR" + variableNo;
		return variableNo;

	}
	
	public static String createScenegit() {
		String sceneGit = String.valueOf(Calendar.getInstance()
				.getTimeInMillis());
		return sceneGit;

	}
}
