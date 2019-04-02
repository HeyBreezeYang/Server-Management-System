package com.system.started.rest.request;

import io.swagger.annotations.ApiModelProperty;

public class MonitorHostStatusUpdateBean {
	
	@ApiModelProperty(value="hostid", dataType="integer")
	private Integer hostid;
	
	@ApiModelProperty(value="status", dataType="integer")
	private Integer status;
	
	
	public Integer getHostid() {
		return hostid;
	}
	public void setHostid(Integer hostid) {
		this.hostid = hostid;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
