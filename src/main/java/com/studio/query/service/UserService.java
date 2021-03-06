package com.studio.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.dao.AccountDao;
import com.studio.query.dao.UserDao;
import com.studio.query.entity.User;

@Service
public class UserService {
	@Autowired
	public UserDao userDao;
	@Autowired
	public AccountDao accountDao;

	public List<User> findUser(User user) {
		return userDao.findUser(user);
	}

	public int countUser(User user) {
		return userDao.countUser(user);
	}

	public int insertUser(User user) {
		return userDao.insertUser(user);
	}

	public int updatePwd(User user) {
		return userDao.updatePwd(user);
	}
}
