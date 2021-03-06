package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.studio.query.entity.Account;
import com.studio.query.mapper.AccountMapperProvider;

public interface AccountDao {
	@Results(value = { @Result(column = "account_id", property = "accountId"),
			@Result(column = "account_status", property = "accountStatus"),
			@Result(column = "account_pwd_status", property = "accountPwdStatus"),
			@Result(column = "account_template_status", property = "accountTemplateStatus"),
			@Result(column = "account_number", property = "accountNumber"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "account_email", property = "accountEmail"),
			@Result(column = "account_real_name", property = "accountRealName"),
			@Result(column = "account_password", property = "accountPassword"),
			@Result(column = "account_repository", property = "accountRepository"),
			@Result(column = "account_date", property = "accountDate") })
	@SelectProvider(type = AccountMapperProvider.class, method = "loginAccount")
	public List<Account> loginAccount(Account account);

	@Results(value = { @Result(column = "account_id", property = "accountId"),
			@Result(column = "account_name", property = "accountName") })
	@Select("select * from t_account where account_name=#{accountName}")
	public List<Account> verifyAccount(Account account);

	@Results(value = { @Result(column = "account_id", property = "accountId"),
			@Result(column = "account_status", property = "accountStatus"),
			@Result(column = "account_pwd_status", property = "accountPwdStatus"),
			@Result(column = "account_template_status", property = "accountTemplateStatus"),
			@Result(column = "account_number", property = "accountNumber"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "account_email", property = "accountEmail"),
			@Result(column = "account_real_name", property = "accountRealName"),
			@Result(column = "account_password", property = "accountPassword"),
			@Result(column = "account_repository", property = "accountRepository"),
			@Result(column = "account_date", property = "accountDate") })
	@SelectProvider(type = AccountMapperProvider.class, method = "findAccount")
	public List<Account> findAccount(Account account);

	@SelectProvider(type = AccountMapperProvider.class, method = "countAccount")
	public int countAccount(Account account);

	@Insert("insert into t_account(account_name,account_password,account_number,account_email,account_real_name,account_repository,account_pwd_status,account_date)values(#{accountName},#{accountPassword},#{accountNumber},#{accountEmail},#{accountRealName},#{accountRepository},#{accountPwdStatus},now())")
	public int insertAccount(Account account);

	@Update("update t_account set account_password=#{accountPassword},account_pwd_status=-1 where account_id=#{accountId}")
	public int initPwd(Account account);
	
	@Update("update t_account set account_password=#{accountPassword},account_pwd_status=0 where account_id=#{accountId}")
	public int updatePwd(Account account);

	@Update("update t_account set account_status=0 where account_id=#{accountId}")
	public int enable(Account account);

	@Update("update t_account set account_status=-1 where account_id=#{accountId}")
	public int disable(Account account);
	
	@Update("update t_account set account_template_status=1 where account_id=#{accountId}")
	public int enableTemplate(Account account);

	@Update("update t_account set account_template_status=0 where account_id=#{accountId}")
	public int disableTemplate(Account account);
}
