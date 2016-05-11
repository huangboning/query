package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.studio.query.entity.Variable;
import com.studio.query.mapper.VariableMapperProvider;

public interface VariableDao {

	@Results(value = { @Result(column = "variable_id", property = "variableId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "variable_no", property = "variableNo"),
			@Result(column = "variable_name", property = "variableName"),
			@Result(column = "variable_type", property = "variableType"),
			@Result(column = "variable_obj_type", property = "variableObjType"),
			@Result(column = "variable_scope", property = "variableScope"),
			@Result(column = "variable_desc", property = "variableDesc"),
			@Result(column = "fragment_id", property = "fragmentId"),
			@Result(column = "variable_share", property = "variableShare"),
			@Result(column = "variable_date", property = "variableDate") })
	@SelectProvider(type = VariableMapperProvider.class, method = "findVariable")
	public List<Variable> findVariable(Variable variable);
	
	@Results(value = { @Result(column = "variable_id", property = "variableId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "variable_no", property = "variableNo"),
			@Result(column = "variable_name", property = "variableName"),
			@Result(column = "variable_type", property = "variableType"),
			@Result(column = "variable_obj_type", property = "variableObjType"),
			@Result(column = "variable_scope", property = "variableScope"),
			@Result(column = "variable_desc", property = "variableDesc"),
			@Result(column = "fragment_id", property = "fragmentId"),
			@Result(column = "variable_share", property = "variableShare"),
			@Result(column = "variable_date", property = "variableDate") })
	@Select("select a.*,b.account_name from t_variable a left join t_account b on (a.account_id=b.account_id) where a.variable_share=1")
	public List<Variable> shareVariables(Variable variable);

	@Insert("insert into t_variable(account_id,variable_no,variable_name,variable_type,variable_obj_type,variable_scope,variable_desc,fragment_id,variable_share,variable_date)" +
			" values(#{accountId},#{variableNo},#{variableName},#{variableType},#{variableObjType},#{variableScope},#{variableDesc},#{fragmentId},#{variableShare},now())")
	public int insertVariable(Variable variable);

	@Update("update t_variable set variable_desc=#{variableDesc} where variable_no=#{variableNo} and account_id=#{accountId}")
	public int updateVariable(Variable variable);
	
	@Delete("delete from t_variable where variable_no=#{variableNo} and account_id=#{accountId}")
	public int deleteVariable(Variable variable);
	
	@Update("update t_variable set variable_share=0 where variable_no=#{variableNo} and account_id=#{accountId}")
	public int disableShareVariable(Variable variable);
	
	@Update("update t_variable set variable_share=1 where variable_no=#{variableNo} and account_id=#{accountId}")
	public int enableShareVariable(Variable variable);

}
