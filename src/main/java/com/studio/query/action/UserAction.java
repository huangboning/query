package com.studio.query.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studio.query.common.Configure;
import com.studio.query.entity.Account;
import com.studio.query.entity.JsonResult;
import com.studio.query.entity.User;
import com.studio.query.service.UserService;

import net.sf.json.JSONObject;

/**
 * 
 * @author hbn
 * 
 */
@Component
public class UserAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String userAccount;
	private String userPassword;
	private List<User> userList = new ArrayList<User>();
	private User userInfo = new User();
	private Account searchAccount = new Account();
	private List<Account> accountList = new ArrayList<Account>();

	@Autowired
	public UserService userService;

	public String execute() {
		return null;
	}

	public String userLogin() {
		try {
			User loginUser = new User();
			loginUser.setUserAccount(this.getUserAccount());
			loginUser.setUserPassword(this.getUserPassword());
			userList = userService.findUser(loginUser);
			JsonResult result = new JsonResult();
			OutputStream out = response.getOutputStream();
			if (userList.size() == 1) {
				session.put(Configure.systemSessionUser, userList.get(0));
				result.code = 0;
				result.result = request.getContextPath() + "/user/list";
			} else {
				result.code = 1;
				result.result = "用户名或密码错误！";
			}
			response.setHeader("Content-Type", "text/html;charset=utf-8");
			out.write(JSONObject.fromObject(result).toString().getBytes("utf-8"));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String userLogOut() {
		session.remove(Configure.systemSessionUser);
		return SUCCESS;
	}

	public String userInfo() {
		userInfo = (User) session.get(Configure.systemSessionUser);
		if (userInfo != null) {
			User u = new User();
			u.setUserId(userInfo.getUserId());
			List<User> list = userService.findUser(u);
			if (list.size() == 1) {
				userInfo = list.get(0);
				return SUCCESS;
			} else {
				return ERROR;
			}

		} else {
			return ERROR;
		}

	}

	public String listAccount() {
		searchAccount.setOffset(offset);
		searchAccount.setLimit(limit);
		accountList = userService.findAccount(searchAccount);
		count = userService.countAccount(searchAccount);
		return SUCCESS;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

}
