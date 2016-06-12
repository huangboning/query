package com.studio.query.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studio.query.entity.Account;
import com.studio.query.service.AccountService;

/**
 * 
 * @author hbn
 * 
 */
@Component
public class AccountAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String accountAccount;
	private String accountPassword;
	private Account searchAccount = new Account();
	private List<Account> accountList = new ArrayList<Account>();

	@Autowired
	public AccountService accountService;

	public String execute() {
		return null;
	}

	public String listAccount() {
		searchAccount.setOffset(pager.getOffset());
		searchAccount.setLimit(pager.getLimit());
		accountList = accountService.findAccount(searchAccount);
		count = accountService.countAccount(searchAccount);
		return SUCCESS;
	}

	public String initPwd() {

		accountService.initPwd(searchAccount);
		return SUCCESS;
	}

	public String enable() {

		accountService.enable(searchAccount);
		return SUCCESS;
	}

	public String disable() {

		accountService.disable(searchAccount);
		return SUCCESS;
	}

	public String getAccountAccount() {
		return accountAccount;
	}

	public void setAccountAccount(String accountAccount) {
		this.accountAccount = accountAccount;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public Account getSearchAccount() {
		return searchAccount;
	}

	public void setSearchAccount(Account searchAccount) {
		this.searchAccount = searchAccount;
	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}

}
