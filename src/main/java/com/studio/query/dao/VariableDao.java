package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import com.studio.query.entity.ShareVariable;
import com.studio.query.entity.Variable;
import com.studio.query.mapper.ShareVariableMapperProvider;
import com.studio.query.mapper.VariableMapperProvider;

public interface VariableDao {

	@Results(value = { @Result(column = "variable_id", property = "variableId"),
			@Result(column = "scene_id", property = "sceneId"),
			@Result(column = "variable_uuid", property = "variableUUID"),
			@Result(column = "variable_name", property = "variableName"),
			@Result(column = "variable_type", property = "variableType"),
			@Result(column = "variable_obj_type", property = "variableObjType"),
			@Result(column = "variable_scope", property = "variableScope"),
			@Result(column = "variable_desc", property = "variableDesc"),
			@Result(column = "fragment_uuid", property = "fragmentUUID"),
			@Result(column = "variable_date", property = "variableDate") })
	@SelectProvider(type = VariableMapperProvider.class, method = "findVariable")
	public List<Variable> findVariable(Variable variable);

	@Results(value = { @Result(column = "share_variable_id", property = "shareVariableId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "share_variable_uuid", property = "shareVariableUUID"),
			@Result(column = "share_variable_name", property = "shareVariableName"),
			@Result(column = "share_variable_type", property = "shareVariableType"),
			@Result(column = "share_variable_obj_type", property = "shareVariableObjType"),
			@Result(column = "share_variable_scope", property = "shareVariableScope"),
			@Result(column = "share_variable_desc", property = "shareVariableDesc"),
			@Result(column = "fragment_uuid", property = "fragmentUUID"),
			@Result(column = "share_variable_version", property = "shareVariableVersion"),
			@Result(column = "share_variable_git", property = "shareVariableGit"),
			@Result(column = "share_variable_date", property = "shareVariableDate") })
	@SelectProvider(type = ShareVariableMapperProvider.class, method = "findShareVariable")
	public List<ShareVariable> findShareVariable(ShareVariable shareVariable);

	@Insert("insert into t_variable(scene_id,variable_uuid,variable_name,variable_type,variable_obj_type,variable_scope,variable_desc,fragment_uuid,variable_date)"
			+ " values(#{sceneId},#{variableUUID},#{variableName},#{variableType},#{variableObjType},#{variableScope},#{variableDesc},#{fragmentUUID},now())")
	public int insertVariable(Variable variable);

	@Update("update t_variable set variable_name=#{variableName},variable_type=#{variableType},variable_obj_type=#{variableObjType},fragment_uuid=#{fragmentUUID},variable_scope=#{variableScope},variable_desc=#{variableDesc} where variable_uuid=#{variableUUID}")
	public int updateVariable(Variable variable);

	@Delete("delete from t_variable where variable_uuid=#{variableUUID}")
	public int deleteVariable(Variable variable);

	// 发布模板变量
	@Insert("insert into t_share_variable(account_id,share_variable_uuid,share_variable_name,share_variable_type,share_variable_obj_type,share_variable_scope,fragment_uuid,share_variable_desc,share_variable_version,share_variable_git,share_variable_date)"
			+ " values(#{accountId},#{shareVariableUUID},#{shareVariableName},#{shareVariableType},#{shareVariableObjType},#{shareVariableScope},#{fragmentUUID},#{shareVariableDesc},#{shareVariableVersion},#{shareVariableGit},now())")
	public int releaseVariable(ShareVariable shareVariable);

	// 更新模板变量
	@Update("update t_share_variable set share_variable_name=#{shareVariableName},share_variable_type=#{shareVariableType},share_variable_obj_type=#{shareVariableObjType},share_variable_scope=#{shareVariableScope},fragment_uuid=#{fragmentUUID},share_variable_desc=#{shareVariableDesc},share_variable_version=#{shareVariableVersion} where share_variable_uuid=#{shareVariableUUID} and account_id=#{accountId}")
	public int updateShareVariable(ShareVariable shareVariable);

}
