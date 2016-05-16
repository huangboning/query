package com.studio.query.entity;

import java.util.Date;

import com.studio.query.common.Page;

public class Scene extends Page {
	private int sceneId;
	private int accountId;
	private String sceneUUID;
	private String sceneName;
	private String sceneDesc;
	private String sceneComment;
	private String sceneGit;
	private String sceneVersion;
	private int sceneActive;
	private int sceneEnable;

	private Date sceneDate;

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getSceneUUID() {
		return sceneUUID;
	}

	public void setSceneUUID(String sceneUUID) {
		this.sceneUUID = sceneUUID;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getSceneDesc() {
		return sceneDesc;
	}

	public void setSceneDesc(String sceneDesc) {
		this.sceneDesc = sceneDesc;
	}

	public String getSceneComment() {
		return sceneComment;
	}

	public void setSceneComment(String sceneComment) {
		this.sceneComment = sceneComment;
	}

	public String getSceneGit() {
		return sceneGit;
	}

	public void setSceneGit(String sceneGit) {
		this.sceneGit = sceneGit;
	}

	public String getSceneVersion() {
		return sceneVersion;
	}

	public void setSceneVersion(String sceneVersion) {
		this.sceneVersion = sceneVersion;
	}

	public int getSceneActive() {
		return sceneActive;
	}

	public void setSceneActive(int sceneActive) {
		this.sceneActive = sceneActive;
	}

	public int getSceneEnable() {
		return sceneEnable;
	}

	public void setSceneEnable(int sceneEnable) {
		this.sceneEnable = sceneEnable;
	}

	public Date getSceneDate() {
		return sceneDate;
	}

	public void setSceneDate(Date sceneDate) {
		this.sceneDate = sceneDate;
	}

}
