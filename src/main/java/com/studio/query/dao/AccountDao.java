package com.studio.zqquery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.studio.zqquery.entity.Account;
import com.studio.zqquery.mapper.AccountMapperProvider;

public interface AccountDao {

	@Results(value = { @Result(column = "account_id", property = "accountId"),
			@Result(column = "account_number", property = "accountNumber"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "account_email", property = "accountEmail"),
			@Result(column = "account_password", property = "accountPassword"),
			@Result(column = "account_repository", property = "accountRepository"),
			@Result(column = "account_date", property = "accountDate") })
	@SelectProvider(type = AccountMapperProvider.class, method = "findAccount")
	public List<Account> findAccount(Account account);

	@SelectProvider(type = AccountMapperProvider.class, method = "countAccount")
	public int countAccount(Account account);

	@Insert("insert into t_account(account_name,account_password,account_number,account_email,account_repository,account_date)values(#{accountName},#{accountPassword},#{accountNumber},#{accountEmail},#{accountRepository},now())")
	public int insertAccount(Account account);
}
