package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class ShareVariable extends Page {
	private int shareVariableId;
	private int accountId;
	private String accountName;
	private String shareVariableUUID;
	private int shareVariableScope;
	private String fragmentUUID;
	private String shareVariableName;
	private String shareVariableType;
	private String shareVariableObjType;
	private String shareVariableDesc;
	private Date shareVariableDate;
	private String shareVariableVersion;
	private String shareVariableGit;

	public int getShareVariableId() {
		return shareVariableId;
	}

	public void setShareVariableId(int shareVariableId) {
		this.shareVariableId = shareVariableId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getShareVariableUUID() {
		return shareVariableUUID;
	}

	public void setShareVariableUUID(String shareVariableUUID) {
		this.shareVariableUUID = shareVariableUUID;
	}

	public int getShareVariableScope() {
		return shareVariableScope;
	}

	public void setShareVariableScope(int shareVariableScope) {
		this.shareVariableScope = shareVariableScope;
	}

	public String getFragmentUUID() {
		return fragmentUUID;
	}

	public void setFragmentUUID(String fragmentUUID) {
		this.fragmentUUID = fragmentUUID;
	}

	public String getShareVariableName() {
		return shareVariableName;
	}

	public void setShareVariableName(String shareVariableName) {
		this.shareVariableName = shareVariableName;
	}

	public String getShareVariableType() {
		return shareVariableType;
	}

	public void setShareVariableType(String shareVariableType) {
		this.shareVariableType = shareVariableType;
	}

	public String getShareVariableObjType() {
		return shareVariableObjType;
	}

	public void setShareVariableObjType(String shareVariableObjType) {
		this.shareVariableObjType = shareVariableObjType;
	}

	public String getShareVariableDesc() {
		return shareVariableDesc;
	}

	public void setShareVariableDesc(String shareVariableDesc) {
		this.shareVariableDesc = shareVariableDesc;
	}

	public Date getShareVariableDate() {
		return shareVariableDate;
	}

	public void setShareVariableDate(Date shareVariableDate) {
		this.shareVariableDate = shareVariableDate;
	}

	public String getShareVariableVersion() {
		return shareVariableVersion;
	}

	public void setShareVariableVersion(String shareVariableVersion) {
		this.shareVariableVersion = shareVariableVersion;
	}

	public String getShareVariableGit() {
		return shareVariableGit;
	}

	public void setShareVariableGit(String shareVariableGit) {
		this.shareVariableGit = shareVariableGit;
	}

}
