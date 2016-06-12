package com.studio.query.mapper;

import com.studio.query.entity.Account;
import com.studio.query.util.StringUtil;

public class AccountMapperProvider {

	public static String loginAccount(Account account) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_account a where 1=1 ");

		if (!StringUtil.isNullOrEmpty(account.getAccountName())) {
			buffer.append("and a.account_name = #{accountName} ");
		}
		if (account.getAccountPassword() != null && !account.getAccountPassword().equals("")) {
			buffer.append("and a.account_password = #{accountPassword} ");
		}
		return buffer.toString();
	}

	public static String findAccount(Account account) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_account a where 1=1 ");

		if (!StringUtil.isNullOrEmpty(account.getAccountName())) {
			buffer.append("and a.account_name like concat('%',#{accountName},'%') ");
		}
		if (account.getAccountStatus() != 1) {
			buffer.append("and a.account_status = #{accountStatus} ");
		}
		if (account.getBeginDate() != null && !account.getBeginDate().equals("")) {
			buffer.append("and a.account_date >= #{beginDate} ");
		}
		if (account.getEndDate() != null && !account.getEndDate().equals("")) {
			buffer.append("and a.account_date < #{endDate} ");
		}
		buffer.append(" order by a.account_date desc ");
		if (account.getOffset() == 0 && account.getLimit() == 0) {
		} else {
			buffer.append(" limit #{offset}, #{limit} ");
		}
		return buffer.toString();
	}

	public static String countAccount(Account account) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from t_account a where 1=1 ");

		if (!StringUtil.isNullOrEmpty(account.getAccountName())) {
			buffer.append("and a.account_name like concat('%',#{accountName},'%') ");
		}
		if (account.getAccountStatus() != 1) {
			buffer.append("and a.account_status = #{accountStatus} ");
		}
		if (account.getBeginDate() != null && !account.getBeginDate().equals("")) {
			buffer.append("and a.account_date >= #{beginDate} ");
		}
		if (account.getEndDate() != null && !account.getEndDate().equals("")) {
			buffer.append("and a.account_date < #{endDate} ");
		}
		return buffer.toString();
	}

}
