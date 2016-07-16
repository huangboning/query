package com.studio.query.test;

import java.util.ArrayList;
import java.util.List;

public class Dept {
	private String id;
	private String pid;
	private String name;
	private List<Dept> subDept=new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Dept> getSubDept() {
		return subDept;
	}

	public void setSubDept(List<Dept> subDept) {
		this.subDept = subDept;
	}

}
