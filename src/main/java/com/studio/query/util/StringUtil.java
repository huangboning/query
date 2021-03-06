package com.studio.query.util;

import java.util.Base64;
import java.util.Calendar;
import java.util.Random;

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

	public static String packetObject(String optCode, String statusCode, String message, String baseObject) {
		JSONObject o = new JSONObject();
		o.put("optCode", optCode);
		o.put("statusCode", statusCode);
		o.put("message", message);
		if (StringUtil.isNullOrEmpty(baseObject)) {
			o.put("baseObject", "{}");
		} else {
			o.put("baseObject", baseObject);
		}

		return o.toString();
	}

	public static String packetObjectObj(String optCode, String statusCode, String message, Object mapObject) {
		JSONObject o = new JSONObject();
		o.put("optCode", optCode);
		o.put("statusCode", statusCode);
		o.put("message", message);
		if (mapObject == null || mapObject.equals("")) {
			o.put("baseObject", "{}");
		} else {
			o.put("baseObject", mapObject);
		}
		return o.toString();
	}

	public static String createSceneUUID() {
		String sceneUUID = java.util.UUID.randomUUID().toString();
		sceneUUID = sceneUUID.substring(0, 8) + sceneUUID.substring(9, 13) + sceneUUID.substring(14, 18)
				+ sceneUUID.substring(19, 23) + sceneUUID.substring(24);
		sceneUUID = "SCNO" + sceneUUID;
		return sceneUUID;

	}

	public static String createFragmentUUID() {
		String fragmentUUID = java.util.UUID.randomUUID().toString();
		fragmentUUID = fragmentUUID.substring(0, 8) + fragmentUUID.substring(9, 13) + fragmentUUID.substring(14, 18)
				+ fragmentUUID.substring(19, 23) + fragmentUUID.substring(24);
		fragmentUUID = "FRAG" + fragmentUUID;
		return fragmentUUID;

	}

	public static String createTemplateFragmentUUID() {
		String fragmentUUID = java.util.UUID.randomUUID().toString();
		fragmentUUID = fragmentUUID.substring(0, 8) + fragmentUUID.substring(9, 13) + fragmentUUID.substring(14, 18)
				+ fragmentUUID.substring(19, 23) + fragmentUUID.substring(24);
		fragmentUUID = "FRAT" + fragmentUUID;
		return fragmentUUID;

	}

	public static String createVariableUUID() {
		String variableUUID = java.util.UUID.randomUUID().toString();
		variableUUID = variableUUID.substring(0, 8) + variableUUID.substring(9, 13) + variableUUID.substring(14, 18)
				+ variableUUID.substring(19, 23) + variableUUID.substring(24);
		variableUUID = "VAR" + variableUUID;
		return variableUUID;

	}

	public static String createSceneGit() {
		String sceneGit = String.valueOf(Calendar.getInstance().getTimeInMillis());
		return sceneGit;

	}

	public static String createShareFragmentGit() {
		String shareFragmentGit = String.valueOf(Calendar.getInstance().getTimeInMillis());
		return shareFragmentGit;

	}

	public static String createShareVariableGit() {
		String shareVariableGit = String.valueOf(Calendar.getInstance().getTimeInMillis());
		return shareVariableGit;

	}

	public static String createPassword() {
		int randomInt = new Random().nextInt(999999);
		String randomString = String.valueOf(randomInt);
		return randomString;
	}

	public static String createPasswordBase64() {
		int randomInt = new Random().nextInt(999999);
		String randomString = String.valueOf(randomInt);
		randomString = Md5Util.md5Encode(randomString);
		String str = new String(Base64.getEncoder().encode(randomString.getBytes()));
		randomString = str.substring(0, 8);
		return randomString;
	}
	public static String createBranchUUID() {
		String variableUUID = java.util.UUID.randomUUID().toString();
		variableUUID = variableUUID.substring(0, 8) + variableUUID.substring(9, 13) + variableUUID.substring(14, 18)
				+ variableUUID.substring(19, 23) + variableUUID.substring(24);
		variableUUID = "BN" + variableUUID;
		return variableUUID;

	}
}
