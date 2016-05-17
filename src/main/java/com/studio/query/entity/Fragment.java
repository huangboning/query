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
	private String fragmentExpression;

	private Date fragmentDate;

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

	public String getFragmentExpression() {
		return fragmentExpression;
	}

	public void setFragmentExpression(String fragmentExpression) {
		this.fragmentExpression = fragmentExpression;
	}

}
