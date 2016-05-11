package com.studio.query.common;

import com.studio.query.protocol.ParameterCode;

import net.sf.json.JSONObject;

public class CommonError {
	public static byte[] thorwError(int errorCode) {
		byte[] out = null;
		JSONObject o = new JSONObject();
		o.put(ParameterCode.Result.RESPONSE_RESULT,
				ParameterCode.Result.RESULT_FAIL);
		o.put(ParameterCode.Result.RESPONSE_ERROR_CODE, errorCode);
		out = o.toString().getBytes();
		return out;
	}

}
