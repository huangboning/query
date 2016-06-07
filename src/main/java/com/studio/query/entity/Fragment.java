package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class Fragment extends Page {
	private int fragmentId;
	private int sceneId;
	private String fragmentUUID;
	private String fragmentTemplateId;
	private String fragmentTemplateVersion;
	private String fragmentName;
	private String fragmentType;
	private String fragmentObjType;
	private String fragmentDesc;
	private boolean fragmentEnable;
	private boolean fragmentActive;
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

	public String getFragmentTemplateId() {
		return fragmentTemplateId;
	}

	public void setFragmentTemplateId(String fragmentTemplateId) {
		this.fragmentTemplateId = fragmentTemplateId;
	}

	public String getFragmentTemplateVersion() {
		return fragmentTemplateVersion;
	}

	public void setFragmentTemplateVersion(String fragmentTemplateVersion) {
		this.fragmentTemplateVersion = fragmentTemplateVersion;
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

	public String getFragmentDateStr() {
		return fragmentDateStr;
	}

	public void setFragmentDateStr(String fragmentDateStr) {
		this.fragmentDateStr = fragmentDateStr;
	}

	public boolean isFragmentEnable() {
		return fragmentEnable;
	}

	public void setFragmentEnable(boolean fragmentEnable) {
		this.fragmentEnable = fragmentEnable;
	}

	public boolean isFragmentActive() {
		return fragmentActive;
	}

	public void setFragmentActive(boolean fragmentActive) {
		this.fragmentActive = fragmentActive;
	}

}
