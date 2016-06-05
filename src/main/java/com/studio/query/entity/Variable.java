package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class Variable extends Page {
	private int variableId;
	private int sceneId;
	private String variableUUID;
	private String variableScope;
	private String fragmentUUID;
	private String sceneUUID;
	private String variableName;
	private String variableType;
	private String variableFieldType;
	private String variableValueType;
	private String variableValue;
	private String variableDesc;
	private Date variableDate;
	private String variableDateStr;

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

	public String getVariableScope() {
		return variableScope;
	}

	public void setVariableScope(String variableScope) {
		this.variableScope = variableScope;
	}

	public String getFragmentUUID() {
		return fragmentUUID;
	}

	public void setFragmentUUID(String fragmentUUID) {
		this.fragmentUUID = fragmentUUID;
	}

	public String getSceneUUID() {
		return sceneUUID;
	}

	public void setSceneUUID(String sceneUUID) {
		this.sceneUUID = sceneUUID;
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

	public String getVariableFieldType() {
		return variableFieldType;
	}

	public void setVariableFieldType(String variableFieldType) {
		this.variableFieldType = variableFieldType;
	}

	public String getVariableValueType() {
		return variableValueType;
	}

	public void setVariableValueType(String variableValueType) {
		this.variableValueType = variableValueType;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
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

	public String getVariableDateStr() {
		return variableDateStr;
	}

	public void setVariableDateStr(String variableDateStr) {
		this.variableDateStr = variableDateStr;
	}

}
