package com.studio.query.mapper;

import com.studio.query.entity.Branch;
import com.studio.query.util.StringUtil;

public class BranchMapperProvider {
	public static String findBranch(Branch branch) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_branch a where 1=1 ");
		if (branch.getBranchId() != 0) {
			buffer.append("and a.branch_id = #{branchId} ");
		}
		if (branch.getSceneId() != 0) {
			buffer.append("and a.scene_id = #{sceneId} ");
		}
		if (!StringUtil.isNullOrEmpty(branch.getBranchName())) {
			buffer.append("and a.branch_name =#{branchName} ");
		}
		if (!StringUtil.isNullOrEmpty(branch.getBranchNameCn())) {
			buffer.append("and a.branch_name_cn =#{branchNameCn} ");
		}
		buffer.append(" order by a.branch_date asc ");
		return buffer.toString();
	}
}
