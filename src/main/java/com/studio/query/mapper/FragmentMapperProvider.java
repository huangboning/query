package com.studio.query.mapper;

import com.studio.query.entity.Fragment;
import com.studio.query.util.StringUtil;

public class FragmentMapperProvider {
	public static String findFragment(Fragment fragment) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_fragment a where 1=1 ");
		if (fragment.getFragmentId() != 0) {
			buffer.append("and a.fragment_id = #{fragmentId} ");
		}
		if (fragment.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (!StringUtil.isNullOrEmpty(fragment.getFragmentUUID())) {
			buffer.append("and a.fragment_uuid =#{fragmentUUID} ");
		}
		if (!StringUtil.isNullOrEmpty(fragment.getFragmentName())) {
			buffer.append("and a.fragment_name =#{fragmentName} ");
		}
		return buffer.toString();
	}
}
