package com.studio.query.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studio.query.common.Configure;
import com.studio.query.entity.Account;
import com.studio.query.service.AccountService;
import com.studio.query.service.JGitService;
import com.studio.query.util.Md5Util;
import com.studio.query.util.StringUtil;

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
	private String outPwd;// 显示的随机密码
	private Account accountInfo = new Account();
	private Account searchAccount = new Account();
	private Account insertAccount = new Account();
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

	public String accountInfo() {
		accountInfo = (Account) session.get(Configure.systemSessionAccount);
		if (accountInfo != null) {
			Account a = new Account();
			a.setAccountId(accountInfo.getAccountId());
			List<Account> list = accountService.findAccount(a);
			if (list.size() == 1) {
				accountInfo = list.get(0);
				return SUCCESS;
			} else {
				return ERROR;
			}

		} else {
			return ERROR;
		}

	}

	public String initPwd() {
		String randomPassword = StringUtil.createPassword();
		searchAccount.setAccountPassword(Md5Util.md5Encode(randomPassword));
		accountService.initPwd(searchAccount);
		this.setMessage("密码初始化成功！随机密码为：");
		this.setOutPwd(randomPassword);
		return SUCCESS;
	}

	public String addAccount() {
		if (StringUtil.isNullOrEmpty(insertAccount.getAccountName().trim())
				|| StringUtil.isNullOrEmpty(insertAccount.getAccountPassword().trim())
				|| StringUtil.isNullOrEmpty(insertAccount.getConfirmPassword().trim())) {
			this.setMessage("请将信息填写完整！");
			return ERROR;
		} else {

			if (!insertAccount.getAccountPassword().trim().equals(insertAccount.getConfirmPassword().trim())) {
				this.setMessage("两次密码输入不一致！");
				return ERROR;
			} else {
				Account findAccount = new Account();
				findAccount.setAccountName(insertAccount.getAccountName().trim());
				if (!findAccount.getAccountName().trim().matches("[a-zA-Z\\d_]+")) {
					this.setMessage("用户名无效！");
					return ERROR;
				}
				if (!StringUtil.isNullOrEmpty(insertAccount.getAccountEmail().trim())) {
					if (!insertAccount.getAccountEmail().trim().matches(
							"^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
						this.setMessage("邮箱无效！");
						return ERROR;
					}
				}

				List<Account> accountList = accountService.verifyAccount(findAccount);
				if (accountList.size() >= 1) {
					this.setMessage("该用户已经存在！");
					return ERROR;
				}
				insertAccount.setAccountPassword(Md5Util.md5Encode(insertAccount.getAccountPassword().trim()));
				insertAccount.setAccountName(insertAccount.getAccountName().trim());
				insertAccount.setAccountEmail(insertAccount.getAccountEmail());
				Calendar a = Calendar.getInstance();
				String accountRepository = "/" + a.get(Calendar.YEAR) + "/" + (a.get(Calendar.MONTH) + 1) + "/"
						+ insertAccount.getAccountName();
				insertAccount.setAccountRepository(accountRepository);
				insertAccount.setAccountPwdStatus(-1);
				int result = accountService.insertAccount(insertAccount);
				if (result == 1) {
					JGitService jGitService = new JGitService();
					jGitService.createAccountRepository(Configure.gitRepositoryPath,
							String.valueOf(a.get(Calendar.YEAR)), String.valueOf((a.get(Calendar.MONTH) + 1)),
							insertAccount.getAccountName());
					this.setMessage("新增用户成功！");
					return SUCCESS;
				} else {
					this.setMessage("操作失败！");
					return ERROR;
				}
			}
		}

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

	public Account getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(Account accountInfo) {
		this.accountInfo = accountInfo;
	}

	public Account getSearchAccount() {
		return searchAccount;
	}

	public void setSearchAccount(Account searchAccount) {
		this.searchAccount = searchAccount;
	}

	public Account getInsertAccount() {
		return insertAccount;
	}

	public void setInsertAccount(Account insertAccount) {
		this.insertAccount = insertAccount;
	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}

	public String getOutPwd() {
		return outPwd;
	}

	public void setOutPwd(String outPwd) {
		this.outPwd = outPwd;
	}

}
