package com.studio.query.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.studio.query.common.Pager;

public class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ActionContext context = ActionContext.getContext();
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, Object> session;
	protected String message;
	protected Pager pager=new Pager();
	protected int count;

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;

	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;

	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
