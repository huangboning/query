package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class Fragment extends Page {
	private int fragmentId;
	private int accountId;
	private String accountName;
	private String fragmentNo;
	private String fragmentName;
	private String fragmentType;
	private String fragmentObjType;
	private String fragmentDesc;
	private int fragmentShare;
	private int fragmentEnable;
	private int fragmentActive;

	private Date fragmentDate;

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
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

	public String getFragmentNo() {
		return fragmentNo;
	}

	public void setFragmentNo(String fragmentNo) {
		this.fragmentNo = fragmentNo;
	}

	public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}

	public String getFragmentType() {
		return fragmentType;
	}

	public void setFragmentType(String fragmentType) {
		this.fragmentType = fragmentType;
	}

	public String getFragmentObjType() {
		return fragmentObjType;
	}

	public void setFragmentObjType(String fragmentObjType) {
		this.fragmentObjType = fragmentObjType;
	}

	public String getFragmentDesc() {
		return fragmentDesc;
	}

	public void setFragmentDesc(String fragmentDesc) {
		this.fragmentDesc = fragmentDesc;
	}

	public int getFragmentShare() {
		return fragmentShare;
	}

	public void setFragmentShare(int fragmentShare) {
		this.fragmentShare = fragmentShare;
	}

	public int getFragmentEnable() {
		return fragmentEnable;
	}

	public void setFragmentEnable(int fragmentEnable) {
		this.fragmentEnable = fragmentEnable;
	}

	public int getFragmentActive() {
		return fragmentActive;
	}

	public void setFragmentActive(int fragmentActive) {
		this.fragmentActive = fragmentActive;
	}

	public Date getFragmentDate() {
		return fragmentDate;
	}

	public void setFragmentDate(Date fragmentDate) {
		this.fragmentDate = fragmentDate;
	}

}
