package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class Fragment extends Page {
	private int fragmentId;
	private int sceneId;
	private String fragmentUUID;
	private String fragmentName;
	private String fragmentType;
	private String fragmentObjType;
	private String fragmentDesc;
	private int fragmentEnable;
	private int fragmentActive;
	private String fragmentEnableStr;
	private String fragmentActiveStr;
	private String fragmentExpression;

	private Date fragmentDate;
	private String fragmentDateStr;

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getFragmentUUID() {
		return fragmentUUID;
	}

	public void setFragmentUUID(String fragmentUUID) {
		this.fragmentUUID = fragmentUUID;
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

	public Date getFragmentDate() {
		return fragmentDate;
	}

	public void setFragmentDate(Date fragmentDate) {
		this.fragmentDate = fragmentDate;
	}

	public String getFragmentExpression() {
		return fragmentExpression;
	}

	public void setFragmentExpression(String fragmentExpression) {
		this.fragmentExpression = fragmentExpression;
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

	public String getFragmentEnableStr() {
		return fragmentEnableStr;
	}

	public void setFragmentEnableStr(String fragmentEnableStr) {
		this.fragmentEnableStr = fragmentEnableStr;
	}

	public String getFragmentActiveStr() {
		return fragmentActiveStr;
	}

	public void setFragmentActiveStr(String fragmentActiveStr) {
		this.fragmentActiveStr = fragmentActiveStr;
	}

	public String getFragmentDateStr() {
		return fragmentDateStr;
	}

	public void setFragmentDateStr(String fragmentDateStr) {
		this.fragmentDateStr = fragmentDateStr;
	}

}
