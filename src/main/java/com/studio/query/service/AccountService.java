package com.studio.query.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.common.Constants;
import com.studio.query.dao.AccountDao;
import com.studio.query.dao.SceneDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Scene;
import com.studio.query.entity.User;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.CacheUtil;
import com.studio.query.util.DateUtil;
import com.studio.query.util.HistoryUtil;
import com.studio.query.util.Md5Util;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONObject;

@Service
public class AccountService {
	@Autowired
	public AccountDao accountDao;
	@Autowired
	public SceneDao sceneDao;
	@Autowired
	public SceneService sceneService;

	public List<Account> loginAccount(Account account) {
		return accountDao.loginAccount(account);
	}

	public List<Account> findAccount(Account account) {
		return accountDao.findAccount(account);
	}

	public List<Account> verifyAccount(Account account) {
		return accountDao.verifyAccount(account);
	}

	public int countAccount(Account account) {
		return accountDao.countAccount(account);
	}

	public int initPwd(Account account) {
		return accountDao.initPwd(account);
	}

	public int updatePwd(Account account) {
		return accountDao.updatePwd(account);
	}

	public int enable(Account account) {
		return accountDao.enable(account);
	}

	public int disable(Account account) {
		return accountDao.disable(account);
	}

	public int enableTemplate(Account account) {
		return accountDao.enableTemplate(account);
	}

	public int disableTemplate(Account account) {
		return accountDao.disableTemplate(account);
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

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGIN, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			String md5AccountPassword = Md5Util.md5Encode(accountPassword);
			Account loginAccount = new Account();
			loginAccount.setAccountName(accountName);
			loginAccount.setAccountPassword(md5AccountPassword);
			List<Account> accountList = accountDao.loginAccount(loginAccount);
			if (accountList.size() == 1) {
				if (accountList.get(0).getAccountStatus() != 0) {
					resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGIN,
							ParameterCode.Error.ACCOUNT_LOGIN_STATUS, "登录失败，帐号已经被禁用。", "");
					return resultString;
				}
				JSONObject accountJson = new JSONObject();
				accountJson.put("accountName", accountList.get(0).getAccountName());
				accountJson.put("accountRealName", accountList.get(0).getAccountRealName());
				accountJson.put("needset", accountList.get(0).getAccountPwdStatus() == -1 ? true : false);
				JSONObject priorityObj=new JSONObject();
				priorityObj.put("couldReleaseTemplate",  accountList.get(0).getAccountTemplateStatus() == 1 ? true : false);
				accountJson.put("priority",priorityObj);
				
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGIN, ParameterCode.Result.RESULT_OK, "登录成功",
						accountJson.toString());
				session.put(Configure.systemSessionAccount, accountList.get(0));
				// 设置当前活动场景
				this.setAccountActiveScene(accountList.get(0), session);
				// 设置默认scope
				List<Map<String, Object>> indexList = (List<Map<String, Object>>) CacheUtil.getCacheObject("mapIndex");
				// 初始化场景未保存
				session.put(Constants.SCENE_ISDIRTY, false);
				if (indexList != null) {
					for (Map<String, Object> map : indexList) {
						boolean isUndified = (boolean) map.get("isUnified");
						if (isUndified) {
							JSONObject setScopeJson = new JSONObject();
							JSONObject scopeObj = new JSONObject();
							scopeObj.put("scope", (String) map.get("id"));
							setScopeJson.put("params", scopeObj);
							this.setScope(setScopeJson.toString(), session);
						}
					}
				}
				HistoryUtil.createUserHistory(loginAccount.getAccountName());
			} else {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGIN,
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

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Account findAccount = new Account();
			findAccount.setAccountName(accountName);
			List<Account> accountList = accountDao.verifyAccount(findAccount);
			if (accountList.size() >= 1) {
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER,
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
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER, ParameterCode.Result.RESULT_OK,
						"注册成功", "");
				session.put(Configure.systemSessionAccount, insertAccount);
			} else {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_REGISTER,
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

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_QUERY, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			Account findAccount = new Account();
			findAccount.setAccountName(accountName);
			List<Account> accountList = accountDao.findAccount(findAccount);
			if (accountList.size() >= 1) {
				JSONObject dataObj1 = new JSONObject();
				dataObj1.put("accountExist", "1");
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_QUERY, ParameterCode.Result.RESULT_OK,
						"查询的账号存在", dataObj1.toString());
				return resultString;

			} else {

				JSONObject dataObj1 = new JSONObject();
				dataObj1.put("accountExist", "0");
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_QUERY, ParameterCode.Result.RESULT_OK,
						"查询的账号不存在", dataObj1.toString());
				return resultString;
			}
		}
		return resultString;
	}

	public String doLogoutLogic(String bodyString, Map<String, Object> session) {

		String resultString = null;
		session.remove(Configure.systemSessionAccount);
		session.remove(Constants.KEY_SET_SCOPE);
		session.remove(Constants.KEY_SCENE_PATH);
		session.remove(Constants.SCENE_ACTIVE);
		session.remove(Constants.SCENE_VERSION);
		resultString = StringUtil.packetObject(MethodCode.ACCOUNT_LOGOUT, ParameterCode.Result.RESULT_OK, "", "");

		return resultString;
	}

	public String doAccountInfoLogic(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;

		JSONObject accountJson = new JSONObject();
		accountJson.put("accountName", currentAccount.getAccountName());
		accountJson.put("accountRealName", currentAccount.getAccountRealName());
		accountJson.put("accountEmail", currentAccount.getAccountEmail());
		accountJson.put("accountStatus", currentAccount.getAccountStatus() == 0 ? "正常" : "禁用");
		accountJson.put("accountDate", DateUtil.dateTimeFormat(currentAccount.getAccountDate()));
		accountJson.put("needset", currentAccount.getAccountPwdStatus() == -1 ? true : false);
		JSONObject priorityObj=new JSONObject();
		priorityObj.put("couldReleaseTemplate",  currentAccount.getAccountTemplateStatus() == 1 ? true : false);
		accountJson.put("priority",priorityObj);
		resultString = StringUtil.packetObject(MethodCode.ACCOUNT_INFO, ParameterCode.Result.RESULT_OK, "获取用户信息成功",
				accountJson.toString());

		return resultString;
	}
	public String doAccountPwdUpdateLogic(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String oldPassword = parmJb.optString("oldPassword", "");
			String newPassword = parmJb.optString("newPassword", "");
			if (StringUtil.isNullOrEmpty(oldPassword) || StringUtil.isNullOrEmpty(newPassword)) {

				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_PWD_UPDATE,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			oldPassword = oldPassword.trim();
			newPassword = newPassword.trim();
			if (!Md5Util.md5Encode(oldPassword).equals(currentAccount.getAccountPassword())) {
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_PWD_UPDATE,
						ParameterCode.Error.OLD_PASSWORD_VALIDATE, "原始密码错误！", "");
				return resultString;
			}
			Account updateAccount = new Account();
			updateAccount.setAccountId(currentAccount.getAccountId());
			updateAccount.setAccountPassword(Md5Util.md5Encode(newPassword));
			int result = this.updatePwd(updateAccount);
			if (result == 1) {
				JSONObject accountJson = new JSONObject();
				accountJson.put("accountName", currentAccount.getAccountName());
				accountJson.put("accountEmail", currentAccount.getAccountEmail());
				accountJson.put("accountStatus", currentAccount.getAccountStatus() == 0 ? "正常" : "禁用");
				accountJson.put("accountDate", DateUtil.dateTimeFormat(currentAccount.getAccountDate()));
				accountJson.put("needset", currentAccount.getAccountPwdStatus() == -1 ? true : false);
				currentAccount.setAccountPassword(updateAccount.getAccountPassword());
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_PWD_UPDATE, ParameterCode.Result.RESULT_OK,
						"更新用户密码成功", accountJson.toString());
			} else {
				resultString = StringUtil.packetObject(MethodCode.ACCOUNT_PWD_UPDATE,
						ParameterCode.Error.SERVICE_RESOLVE, "更新用户密码失败！", "");
				return resultString;
			}

		}
		return resultString;
	}

	/**
	 * 选择数据源
	 * 
	 * @return
	 */
	public String setScope(String bodyString, Map<String, Object> session) {
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String scope = parmJb.optString("scope", "");
			if (StringUtil.isNullOrEmpty(scope)) {

				resultString = StringUtil.packetObject(MethodCode.SET_SCOPE, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			// 这里设置数据源逻辑
			List<String> scopeList = (List<String>) session.get(Constants.KEY_SET_SCOPE);
			if (scopeList == null) {
				scopeList = new ArrayList<String>();
			}
			boolean isExist = false;
			for (int i = 0; i < scopeList.size(); i++) {
				String oldScope = scopeList.get(i);
				if (oldScope.equals(scope)) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				scopeList.add(scope);
				session.put(Constants.KEY_SET_SCOPE, scopeList);
			}

			resultString = StringUtil.packetObject(MethodCode.SET_SCOPE, ParameterCode.Result.RESULT_OK, "设置数据源成功", "");

		}
		return resultString;
	}

	// 设置用户活动场景
	public void setAccountActiveScene(Account currentAccount, Map<String, Object> session) {

		Scene findScene = new Scene();
		findScene.setAccountId(currentAccount.getAccountId());
		findScene.setSceneEnable(0);
		List<Scene> sceneList = sceneDao.findScene(findScene);
		String sceneActiveUUID = HistoryUtil.getUserSceneHistory(currentAccount.getAccountName());
		boolean isFrist = false;
		if (StringUtil.isNullOrEmpty(sceneActiveUUID)) {
			isFrist = true;
		}
		for (int i = 0; i < sceneList.size(); i++) {

			Scene scene = sceneList.get(i);
			if (isFrist) {
				if (i == 0) {
					// 会话中设置当前活动场景
					sceneService.setActiveScene(scene.getSceneUUID(), currentAccount, session);
					sceneService.manualSwitchScene(scene.getSceneUUID(), currentAccount, session);
				} else {

				}
			} else {
				if (sceneActiveUUID.equals(scene.getSceneUUID())) {
					// 会话中设置当前活动场景
					sceneService.setActiveScene(scene.getSceneUUID(), currentAccount, session);
					sceneService.manualSwitchScene(scene.getSceneUUID(), currentAccount, session);
				} else {

				}
			}
		}
	}
}
