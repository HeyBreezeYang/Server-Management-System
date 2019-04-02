package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑监控图表对象")
public class MonitorGraphUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="String")
	private String name;
	
	@ApiModelProperty(value="统计图，<br>0：区域图，1：折线图，2：饼图", dataType="String")
	private Integer type;
	
	@ApiModelProperty(value="是否实时监控，<br>1：是，0：否", dataType="String")
	private Integer monitor;
	
	@ApiModelProperty(value="描述", dataType="String")
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getMonitor() {
		return monitor;
	}
	public void setMonitor(Integer monitor) {
		this.monitor = monitor;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
