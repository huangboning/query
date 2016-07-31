package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.studio.query.entity.Branch;
import com.studio.query.mapper.BranchMapperProvider;

public interface BranchDao {

	@Results(value = { @Result(column = "branch_id", property = "branchId"),
			@Result(column = "scene_id", property = "sceneId"),
			@Result(column = "branch_name", property = "branchName"),
			@Result(column = "branch_name_cn", property = "branchNameCn"),
			@Result(column = "branch_date", property = "branchDate") })
	@SelectProvider(type = BranchMapperProvider.class, method = "findBranch")
	public List<Branch> findBranch(Branch branch);

	@Insert("insert into t_branch(scene_id,branch_name,branch_name_cn,branch_date) values(#{sceneId},#{branchName},#{branchNameCn},now())")
	public int insertBranch(Branch branch);
}
