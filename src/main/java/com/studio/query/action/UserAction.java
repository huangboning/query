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
import com.studio.query.util.Md5Util;
import com.studio.query.util.StringUtil;

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
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	private User userInfo = new User();
	private User searchUser = new User();
	private User initUser = new User();
	private List<User> userList = new ArrayList<User>();

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
			List<User> userList = userService.findUser(loginUser);
			JsonResult result = new JsonResult();
			OutputStream out = response.getOutputStream();
			if (userList.size() == 1) {
				session.put(Configure.systemSessionUser, userList.get(0));
				result.code = 0;
				result.result = request.getContextPath() + "/user/info";
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

	public String userInit() {
		
		List<User> userList = userService.findUser(initUser);
		if (userList.size() == 0) {
			initUser.setUserAccount("admin");
			initUser.setUserName("系统管理员");
			String userPassword=StringUtil.createPasswordBase64();
			initUser.setUserDisplyPwd(userPassword);
			initUser.setUserPassword(Md5Util.md5Encode(userPassword));
			userService.insertUser(initUser);
			return "init";
		} else {
			return "login";
		}
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

	public String updatePwd() {
		userInfo = (User) session.get(Configure.systemSessionUser);
		if (StringUtil.isNullOrEmpty(this.getOldPassword().trim())
				|| StringUtil.isNullOrEmpty(this.getNewPassword().trim())
				|| StringUtil.isNullOrEmpty(this.getConfirmPassword().trim())) {
			this.setMessage("请将信息填写完整！");
			return ERROR;
		} else {

			if (!this.getNewPassword().trim().equals(this.getConfirmPassword().trim())) {
				this.setMessage("两次密码输入不一致！");
				return ERROR;
			} else {
				if (!Md5Util.md5Encode(this.getOldPassword().trim()).equals(userInfo.getUserPassword())) {
					this.setMessage("原始密码错误！");
					return ERROR;
				}
				User updateUser = new User();
				updateUser.setUserId(userInfo.getUserId());
				updateUser.setUserPassword(Md5Util.md5Encode(this.getNewPassword().trim()));
				int result = userService.updatePwd(updateUser);
				if (result == 1) {
					this.setMessage("修改密码成功！");
					return SUCCESS;
				} else {
					this.setMessage("操作失败！");
					return ERROR;
				}
			}
		}

	}

	public String listUser() {
		searchUser.setOffset(pager.getOffset());
		searchUser.setLimit(pager.getLimit());
		userList = userService.findUser(searchUser);
		count = userService.countUser(searchUser);
		return SUCCESS;
	}

	public User getSearchUser() {
		return searchUser;
	}

	public void setSearchUser(User searchUser) {
		this.searchUser = searchUser;
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

	public User getInitUser() {
		return initUser;
	}

	public void setInitUser(User initUser) {
		this.initUser = initUser;
	}

	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
