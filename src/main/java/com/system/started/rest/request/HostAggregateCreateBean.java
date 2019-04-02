package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建主机聚集对象")
public class HostAggregateCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="String", example="manage-group-swagger-test")
	private String name;
	
	@ApiModelProperty(value="平台项目", dataType="String", example="44931aec982e46739cb3336b8511db0d")
	private String projectId;
	
	@ApiModelProperty(value="所属资源池", dataType="String", example="manageRegion")
	private String regionName;
	
	@ApiModelProperty(value="可用性区域", dataType="String", example="manage-az-swagger-test")
	private String availability_zone;
	
	@ApiModelProperty(value="描述", dataType="String", example="通过swagger2创建卷")
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	public String getAvailability_zone() {
		return availability_zone;
	}
	public void setAvailability_zone(String availability_zone) {
		this.availability_zone = availability_zone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
