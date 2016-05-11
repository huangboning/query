package com.studio.zqquery.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studio.zqquery.common.Configure;
import com.studio.zqquery.entity.JsonResult;
import com.studio.zqquery.entity.User;
import com.studio.zqquery.service.UserService;

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
				result.result = request.getContextPath() + "/user/info.do";
			} else {
				result.code = 1;
				result.result = "用户名或密码错误！";
			}
			out.write(JSONObject.fromObject(result).toString()
					.getBytes("utf-8"));
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
