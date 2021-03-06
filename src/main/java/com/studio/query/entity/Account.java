package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Pager;

public class Account extends Pager {
	private int accountId;
	private int accountStatus = 1;// 表示查询所有帐户
	private int accountPwdStatus;
	private int accountTemplateStatus;
	private String accountNumber;
	private String accountPassword;
	private String accountName;
	private String accountEmail;
	private String accountRealName;
	private String accountRepository;
	private Date accountDate;

	private String beginDate;
	private String endDate;

	private String confirmPassword;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	public int getAccountPwdStatus() {
		return accountPwdStatus;
	}

	public void setAccountPwdStatus(int accountPwdStatus) {
		this.accountPwdStatus = accountPwdStatus;
	}

	public int getAccountTemplateStatus() {
		return accountTemplateStatus;
	}

	public void setAccountTemplateStatus(int accountTemplateStatus) {
		this.accountTemplateStatus = accountTemplateStatus;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getAccountRealName() {
		return accountRealName;
	}

	public void setAccountRealName(String accountRealName) {
		this.accountRealName = accountRealName;
	}

	public String getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(String accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Date getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(Date accountDate) {
		this.accountDate = accountDate;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
