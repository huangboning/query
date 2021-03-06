package com.studio.query.entity;

import com.studio.query.common.Pager;

public class DataSource extends Pager {
	private String dataSourceId;// 数据源ID
	private String dataSourceName;// 数据源名称
	private boolean dataSourceIsUnified;// 是否为归一化数据源

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public boolean isDataSourceIsUnified() {
		return dataSourceIsUnified;
	}

	public void setDataSourceIsUnified(boolean dataSourceIsUnified) {
		this.dataSourceIsUnified = dataSourceIsUnified;
	}

}
