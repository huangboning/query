package com.studio.query.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studio.query.common.Configure;
import com.studio.query.common.HttpUtil;
import com.studio.query.service.UserService;

/**
 * 
 * @author hbn
 * 
 */
@Component
public class ValidateAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String vstrJson;
	private String vstrResult;

	@Autowired
	public UserService userService;

	public String execute() {
		return null;
	}

	public String sceneValidate() {
		try {
			this.vstrResult = HttpUtil.sendPost(Configure.esQueryServiceUrl, this.vstrJson.getBytes("utf-8"));

		} catch (Exception e) {
			e.printStackTrace();
			this.setVstrResult(e.toString());
		}
		return SUCCESS;
	}

	public String getVstrJson() {
		return vstrJson;
	}

	public void setVstrJson(String vstrJson) {
		this.vstrJson = vstrJson;
	}

	public String getVstrResult() {
		return vstrResult;
	}

	public void setVstrResult(String vstrResult) {
		this.vstrResult = vstrResult;
	}

}
