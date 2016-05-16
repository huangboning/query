package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class Variable extends Page {
	private int variableId;
	private int accountId;
	private String accountName;
	private String variableUUID;
	private int variableScope;
	private int fragmentId;
	private String variableName;
	private String variableType;
	private String variableObjType;
	private String variableDesc;
	private int variableShare;
	private Date variableDate;

	public int getVariableId() {
		return variableId;
	}

	public void setVariableId(int variableId) {
		this.variableId = variableId;
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

	public String getVariableUUID() {
		return variableUUID;
	}

	public void setVariableUUID(String variableUUID) {
		this.variableUUID = variableUUID;
	}

	public int getVariableScope() {
		return variableScope;
	}

	public void setVariableScope(int variableScope) {
		this.variableScope = variableScope;
	}

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getVariableObjType() {
		return variableObjType;
	}

	public void setVariableObjType(String variableObjType) {
		this.variableObjType = variableObjType;
	}

	public String getVariableDesc() {
		return variableDesc;
	}

	public void setVariableDesc(String variableDesc) {
		this.variableDesc = variableDesc;
	}

	public int getVariableShare() {
		return variableShare;
	}

	public void setVariableShare(int variableShare) {
		this.variableShare = variableShare;
	}

	public Date getVariableDate() {
		return variableDate;
	}

	public void setVariableDate(Date variableDate) {
		this.variableDate = variableDate;
	}

}
