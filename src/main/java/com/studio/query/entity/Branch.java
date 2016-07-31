package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Pager;

public class Branch extends Pager {
	private int branchId;
	private String branchName;
	private String branchNameCn;
	private int sceneId;
	private Date branchDate;

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchNameCn() {
		return branchNameCn;
	}

	public void setBranchNameCn(String branchNameCn) {
		this.branchNameCn = branchNameCn;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public Date getBranchDate() {
		return branchDate;
	}

	public void setBranchDate(Date branchDate) {
		this.branchDate = branchDate;
	}

}
