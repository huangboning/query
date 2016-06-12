package com.studio.query.entity;

import com.studio.query.common.Pager;

public class HeadData extends Pager {
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
