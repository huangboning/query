package com.studio.query.mapper;

import com.studio.query.entity.User;

public class UserMapperProvider {
	public static String findUser(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_user a where 1=1 ");
		if (user.getUserId() != 0) {
			buffer.append("and a.user_id = #{userId} ");
		}
		if (user.getUserName() != null && !user.getUserName().equals("")) {
			buffer.append("and a.user_name like concat('%',#{userName},'%') ");
		}

		if (user.getUserAccount() != null && !user.getUserAccount().equals("")) {
			buffer.append("and a.user_account = #{userAccount} ");
		}
		if (user.getUserPassword() != null
				&& !user.getUserPassword().equals("")) {
			buffer.append("and a.user_password = #{userPassword} ");
		}
		buffer.append(" order by a.user_date desc ");
		if (user.getOffset() == 0 && user.getLimit() == 0) {
		} else {
			buffer.append(" limit #{offset}, #{limit} ");
		}
		return buffer.toString();
	}

	public static String countUser(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from t_user a where 1=1 ");
		if (user.getUserId() != 0) {
			buffer.append("and a.user_id = #{userId} ");
		}
		if (user.getUserName() != null && !user.getUserName().equals("")) {
			buffer.append("and a.user_name like concat('%',#{userName},'%') ");
		}
		if (user.getUserAccount() != null && !user.getUserAccount().equals("")) {
			buffer.append("and a.user_account = #{userAccount} ");
		}

		if (user.getUserPassword() != null
				&& !user.getUserPassword().equals("")) {
			buffer.append("and a.user_password = #{userPassword} ");
		}
		return buffer.toString();
	}
}
