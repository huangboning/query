package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class Variable extends Page {
	private int variableId;
	private int sceneId;
	private String variableUUID;
	private int variableScope;
	private String fragmentUUID;
	private String variableName;
	private String variableType;
	private String variableObjType;
	private String variableDesc;
	private Date variableDate;

	public int getVariableId() {
		return variableId;
	}

	public void setVariableId(int variableId) {
		this.variableId = variableId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
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

	public String getFragmentUUID() {
		return fragmentUUID;
	}

	public void setFragmentUUID(String fragmentUUID) {
		this.fragmentUUID = fragmentUUID;
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

	public Date getVariableDate() {
		return variableDate;
	}

	public void setVariableDate(Date variableDate) {
		this.variableDate = variableDate;
	}

}
