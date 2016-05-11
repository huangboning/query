package com.studio.zqquery.mapper;

import com.studio.zqquery.entity.Account;
import com.studio.zqquery.util.StringUtil;

public class AccountMapperProvider {

	public static String findAccount(Account account) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_account a where 1=1 ");

		if (account.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (account.getAccountNumber() != null
				&& !account.getAccountNumber().equals("")) {
			buffer.append("and a.account_number = #{accountNumber} ");
		}
		if (!StringUtil.isNullOrEmpty(account.getAccountName())) {
			buffer.append("and a.account_name = #{accountName} ");
		}
		if (account.getAccountPassword() != null
				&& !account.getAccountPassword().equals("")) {
			buffer.append("and a.account_password = #{accountPassword} ");
		}
		if (account.getBeginDate() != null
				&& !account.getBeginDate().equals("")) {
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

		if (account.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (account.getAccountNumber() != null
				&& !account.getAccountNumber().equals("")) {
			buffer.append("and a.account_number = #{accountNumber} ");
		}
		if (!StringUtil.isNullOrEmpty(account.getAccountName())) {
			buffer.append("and a.account_name = #{accountName} ");
		}
		if (account.getAccountPassword() != null
				&& !account.getAccountPassword().equals("")) {
			buffer.append("and a.account_password = #{accountPassword} ");
		}
		if (account.getBeginDate() != null
				&& !account.getBeginDate().equals("")) {
			buffer.append("and a.account_date >= #{beginDate} ");
		}
		if (account.getEndDate() != null && !account.getEndDate().equals("")) {
			buffer.append("and a.account_date < #{endDate} ");
		}

		return buffer.toString();
	}

}
