package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Pager;

public class SceneDesc extends Pager {
	private int sceneDescId;
	private int sceneId;
	private String sceneUUID;
	private String sceneDesc;
	private String sceneVersion;
	private Date sceneDescDate;

	public int getSceneDescId() {
		return sceneDescId;
	}

	public void setSceneDescId(int sceneDescId) {
		this.sceneDescId = sceneDescId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getSceneUUID() {
		return sceneUUID;
	}

	public void setSceneUUID(String sceneUUID) {
		this.sceneUUID = sceneUUID;
	}

	public String getSceneDesc() {
		return sceneDesc;
	}

	public void setSceneDesc(String sceneDesc) {
		this.sceneDesc = sceneDesc;
	}

	public String getSceneVersion() {
		return sceneVersion;
	}

	public void setSceneVersion(String sceneVersion) {
		this.sceneVersion = sceneVersion;
	}

	public Date getSceneDescDate() {
		return sceneDescDate;
	}

	public void setSceneDescDate(Date sceneDescDate) {
		this.sceneDescDate = sceneDescDate;
	}

}
