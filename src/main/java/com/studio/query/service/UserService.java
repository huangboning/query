package com.studio.zqquery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.zqquery.dao.UserDao;
import com.studio.zqquery.entity.User;

@Service
public class UserService {
	@Autowired
	public UserDao userDao;

	public List<User> findUser(User user) {
		return userDao.findUser(user);
	}

	public int countUser(User user) {
		return userDao.countUser(user);
	}

	public int insertUser(User user) {
		return userDao.insertUser(user);
	}

	public int updateUser(User user) {
		return userDao.updateUser(user);
	}
}
