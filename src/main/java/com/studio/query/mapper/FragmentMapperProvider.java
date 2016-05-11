package com.studio.zqquery.mapper;

import com.studio.zqquery.entity.Fragment;
import com.studio.zqquery.util.StringUtil;

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
		if (!StringUtil.isNullOrEmpty(fragment.getFragmentNo())) {
			buffer.append("and a.fragment_no =#{fragmentNo} ");
		}
		if (!StringUtil.isNullOrEmpty(fragment.getFragmentName())) {
			buffer.append("and a.fragment_name =#{fragmentName} ");
		}
		return buffer.toString();
	}
}
