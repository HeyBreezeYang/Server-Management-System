package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description="编辑监控模板对象")
public class MonitorTemplateUpdateBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private List<MonitorTemplateHostBean> hosts;
	private String description;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<MonitorTemplateHostBean> getHosts() {
		return hosts;
	}
	public void setHosts(List<MonitorTemplateHostBean> hosts) {
		this.hosts = hosts;
	}
}
