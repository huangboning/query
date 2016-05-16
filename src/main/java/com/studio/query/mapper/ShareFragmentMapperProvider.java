package com.studio.query.mapper;

import com.studio.query.entity.ShareFragment;
import com.studio.query.util.StringUtil;

public class ShareFragmentMapperProvider {
	public static String findShareFragment(ShareFragment shareFragment) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_share_fragment a where 1=1 ");
		if (shareFragment.getShareFragmentId() != 0) {
			buffer.append("and a.share_fragment_id = #{shareFragmentId} ");
		}
		if (shareFragment.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (!StringUtil.isNullOrEmpty(shareFragment.getShareFragmentUUID())) {
			buffer.append("and a.share_fragment_uuid =#{shareFragmentUUID} ");
		}
		if (!StringUtil.isNullOrEmpty(shareFragment.getShareFragmentName())) {
			buffer.append("and a.share_fragment_name =#{shareFragmentName} ");
		}
		return buffer.toString();
	}
}
