package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑卷对象")
public class VolumeUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="ID", dataType="Integer", example="881515284")
	private String id;
	
	@ApiModelProperty(value="名称", dataType="String", example="volume-swg-test")
	private String name;
	
	@ApiModelProperty(value="平台项目", dataType="String", example="44931aec982e46739cb3336b8511db0d")
	private String projectId;
	
	@ApiModelProperty(value="所属资源池", dataType="String", example="manageRegion")
	private String region;
	
	@ApiModelProperty(value="描述", dataType="String", example="通过swagger2创建卷")
	private String description;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
