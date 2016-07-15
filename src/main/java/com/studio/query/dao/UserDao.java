package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.studio.query.entity.User;
import com.studio.query.mapper.UserMapperProvider;

public interface UserDao {

	@Results(value = { @Result(column = "user_id", property = "userId"),
			@Result(column = "user_account", property = "userAccount"),
			@Result(column = "user_password", property = "userPassword"),
			@Result(column = "user_name", property = "userName"),
			@Result(column = "user_role_id", property = "userRoleId"),
			@Result(column = "user_date", property = "userDate") })
	@SelectProvider(type = UserMapperProvider.class, method = "findUser")
	public List<User> findUser(User user);

	@SelectProvider(type = UserMapperProvider.class, method = "countUser")
	public int countUser(User user);

	@Insert("insert into t_user(user_name,user_account,user_password,user_date) values(#{userName},#{userAccount},#{userPassword},now())")
	public int insertUser(User user);

	@Update("update t_user set user_password=#{userPassword} where user_id=#{userId}")
	public int updatePwd(User user);

}
