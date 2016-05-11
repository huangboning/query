package com.studio.zqquery.common;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthorityInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1358600090729208361L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext()
				.getSession();

		if (null != session.get(Configure.systemSessionUser)) {

			return invocation.invoke();

		} else {

			return "login";

		}
	}
}
