package com.studio.query.entity;

import com.studio.query.common.Pager;

public class Committer extends Pager {
	private String commitVersion;
	private String commitName;
	private String commitEmail;
	private String commitDate;
	private String commitMssage;

	public String getCommitVersion() {
		return commitVersion;
	}

	public void setCommitVersion(String commitVersion) {
		this.commitVersion = commitVersion;
	}

	public String getCommitName() {
		return commitName;
	}

	public void setCommitName(String commitName) {
		this.commitName = commitName;
	}

	public String getCommitEmail() {
		return commitEmail;
	}

	public void setCommitEmail(String commitEmail) {
		this.commitEmail = commitEmail;
	}

	public String getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}

	public String getCommitMssage() {
		return commitMssage;
	}

	public void setCommitMssage(String commitMssage) {
		this.commitMssage = commitMssage;
	}

}
