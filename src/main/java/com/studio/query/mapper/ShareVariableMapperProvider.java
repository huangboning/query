package com.studio.query.mapper;

import com.studio.query.entity.ShareVariable;
import com.studio.query.util.StringUtil;

public class ShareVariableMapperProvider {
	public static String findShareVariable(ShareVariable shareVariable) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select a.*,b.account_name from t_share_variable a LEFT JOIN t_account b on (b.account_id=a.account_id) where 1=1 ");
		if (shareVariable.getShareVariableId() != 0) {
			buffer.append("and a.share_variable_id = #{shareVariableId} ");
		}
		if (shareVariable.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (!StringUtil.isNullOrEmpty(shareVariable.getShareVariableUUID())) {
			buffer.append("and a.share_variable_uuid =#{shareVariableUUID} ");
		}
		if (!StringUtil.isNullOrEmpty(shareVariable.getShareVariableName())) {
			buffer.append("and a.share_variable_name =#{shareVariableName} ");
		}
		return buffer.toString();
	}
}
