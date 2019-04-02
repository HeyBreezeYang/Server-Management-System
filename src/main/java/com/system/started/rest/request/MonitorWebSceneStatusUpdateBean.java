package com.system.started.rest.request;

import io.swagger.annotations.ApiModelProperty;

public class MonitorWebSceneStatusUpdateBean {
	
	@ApiModelProperty(value="httptestid", dataType="string")
	private String httptestid;
	
	@ApiModelProperty(value="itemId", dataType="string")
	private String itemId;
	
	@ApiModelProperty(value="monitorType", dataType="string")
	private String monitorType;
	
	@ApiModelProperty(value="status", dataType="integer")
	private Integer status;
	
	public String getHttptestid() {
		return httptestid;
	}
	public void setHttptestid(String httptestid) {
		this.httptestid = httptestid;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
