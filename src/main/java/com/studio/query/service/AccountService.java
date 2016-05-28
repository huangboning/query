package com.studio.query.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.dao.AccountDao;
import com.studio.query.entity.Account;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.HistoryUtil;
import com.studio.query.util.Md5Util;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONObject;

@Service
public class AccountService {
	@Autowired
	public AccountDao accountDao;

	public List<Account> findAccount(Account account) {
		return accountDao.findAccount(account);
	}

	public int countAccount(Account account) {
		return accountDao.countAccount(account);
	}

	public int insertAccount(Account account) {
		return accountDao.insertAccount(account);
	}

	public String doLoginLogic(String bodyString, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String accountName = parmJb.optString("accountName", "");
			String accountPassword = parmJb.optString("accountPassword", "");
			if (StringUtil.isNullOrEmpty(accountName) || StringUtil.isNullOrEmpty(accountPassword)) {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGIN, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			String md5AccountPassword = Md5Util.md5Encode(accountPassword);
			Account loginAccount = new Account();
			loginAccount.setAccountName(accountName);
			loginAccount.setAccountPassword(md5AccountPassword);
			List<Account> accountList = accountDao.findAccount(loginAccount);
			if (accountList.size() == 1) {
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGIN, ParameterCode.Result.RESULT_OK, "",
						"登录成功", "");
				session.put(Configure.systemSessionAccount, accountList.get(0));
				HistoryUtil.createUserHistory(loginAccount.getAccountName());
			} else {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGIN, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.ACCOUNT_LOGIN_VALIDATE, "登录失败，帐号或密码错误。", "");
			}
		}
		return resultString;
	}

	public String doRegisterLogic(String bodyString, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String accountName = parmJb.optString("accountName", "");
			String accountPassword = parmJb.optString("accountPassword", "");
			String accountNumber = parmJb.optString("accountNumber", "");
			String accountEmail = parmJb.optString("accountEmail", "");
			if (StringUtil.isNullOrEmpty(accountName) || StringUtil.isNullOrEmpty(accountPassword)) {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Account findAccount = new Account();
			findAccount.setAccountName(accountName);
			List<Account> accountList = accountDao.findAccount(findAccount);
			if (accountList.size() >= 1) {
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.ACCOUNT_REGISTER_EXIST, "账号已存在", "");
				return resultString;
			}
			String md5AccountPassword = Md5Util.md5Encode(accountPassword);
			Account insertAccount = new Account();
			insertAccount.setAccountName(accountName);
			insertAccount.setAccountPassword(md5AccountPassword);
			insertAccount.setAccountNumber(accountNumber);
			insertAccount.setAccountEmail(accountEmail);
			Calendar a = Calendar.getInstance();
			String accountRepository = "/" + a.get(Calendar.YEAR) + "/" + (a.get(Calendar.MONTH) + 1) + "/"
					+ accountName;
			insertAccount.setAccountRepository(accountRepository);
			int insertResult = accountDao.insertAccount(insertAccount);
			if (insertResult == 1) {
				JGitService jGitService = new JGitService();
				jGitService.createAccountRepository(Configure.gitRepositoryPath, String.valueOf(a.get(Calendar.YEAR)),
						String.valueOf((a.get(Calendar.MONTH) + 1)), accountName);
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER, ParameterCode.Result.RESULT_OK, "",
						"注册成功", "");
				session.put(Configure.systemSessionAccount, insertAccount);
			} else {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.ACCOUNT_REGISTER_FAIL, "注册失败", "");
			}
		}
		return resultString;
	}

	public String doQueryLogic(String bodyString) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String accountName = parmJb.optString("accountName", "");
			if (StringUtil.isNullOrEmpty(accountName)) {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_QUERY, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Account findAccount = new Account();
			findAccount.setAccountName(accountName);
			List<Account> accountList = accountDao.findAccount(findAccount);
			if (accountList.size() >= 1) {
				JSONObject dataObj1 = new JSONObject();
				dataObj1.put("accountExist", "1");
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_QUERY, ParameterCode.Result.RESULT_OK, "",
						"查询的账号存在", dataObj1.toString());
				return resultString;

			} else {

				JSONObject dataObj1 = new JSONObject();
				dataObj1.put("accountExist", "0");
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_QUERY, ParameterCode.Result.RESULT_OK, "",
						"查询的账号不存在", dataObj1.toString());
				return resultString;
			}
		}
		return resultString;
	}

	public String doLogoutLogic(String bodyString, Map<String, Object> session) {

		String resultString = null;
		session.remove(Configure.systemSessionAccount);
		resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGOUT, ParameterCode.Result.RESULT_OK, "", "注销成功",
				"");

		return resultString;
	}
}
