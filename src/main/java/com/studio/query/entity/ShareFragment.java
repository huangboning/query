package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class ShareFragment extends Page {
	private int shareFragmentId;
	private int accountId;
	private String accountName;
	private String shareFragmentUUID;
	private String shareFragmentName;
	private String shareFragmentType;
	private String shareFragmentObjType;
	private String shareFragmentDesc;
	private int shareFragmentEnable;
	private int shareFragmentActive;
	private Date shareFragmentDate;
	private String shareFragmentVersion;

	public int getShareFragmentId() {
		return shareFragmentId;
	}

	public void setShareFragmentId(int shareFragmentId) {
		this.shareFragmentId = shareFragmentId;
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

	public String getShareFragmentUUID() {
		return shareFragmentUUID;
	}

	public void setShareFragmentUUID(String shareFragmentUUID) {
		this.shareFragmentUUID = shareFragmentUUID;
	}

	public String getShareFragmentName() {
		return shareFragmentName;
	}

	public void setShareFragmentName(String shareFragmentName) {
		this.shareFragmentName = shareFragmentName;
	}

	public String getShareFragmentType() {
		return shareFragmentType;
	}

	public void setShareFragmentType(String shareFragmentType) {
		this.shareFragmentType = shareFragmentType;
	}

	public String getShareFragmentObjType() {
		return shareFragmentObjType;
	}

	public void setShareFragmentObjType(String shareFragmentObjType) {
		this.shareFragmentObjType = shareFragmentObjType;
	}

	public String getShareFragmentDesc() {
		return shareFragmentDesc;
	}

	public void setShareFragmentDesc(String shareFragmentDesc) {
		this.shareFragmentDesc = shareFragmentDesc;
	}

	public int getShareFragmentEnable() {
		return shareFragmentEnable;
	}

	public void setShareFragmentEnable(int shareFragmentEnable) {
		this.shareFragmentEnable = shareFragmentEnable;
	}

	public int getShareFragmentActive() {
		return shareFragmentActive;
	}

	public void setShareFragmentActive(int shareFragmentActive) {
		this.shareFragmentActive = shareFragmentActive;
	}

	public Date getShareFragmentDate() {
		return shareFragmentDate;
	}

	public void setShareFragmentDate(Date shareFragmentDate) {
		this.shareFragmentDate = shareFragmentDate;
	}

	public String getShareFragmentVersion() {
		return shareFragmentVersion;
	}

	public void setShareFragmentVersion(String shareFragmentVersion) {
		this.shareFragmentVersion = shareFragmentVersion;
	}

}
