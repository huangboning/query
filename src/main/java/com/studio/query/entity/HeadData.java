package com.studio.zqquery.entity;

import com.studio.zqquery.common.Page;

public class HeadData extends Page {
	private String headDataId;// 数据表头ID
	private String headDataName;// 数据表头名称

	public String getHeadDataId() {
		return headDataId;
	}

	public void setHeadDataId(String headDataId) {
		this.headDataId = headDataId;
	}

	public String getHeadDataName() {
		return headDataName;
	}

	public void setHeadDataName(String headDataName) {
		this.headDataName = headDataName;
	}

}
