package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑网络对象")
public class NetworkUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="UUID", dataType="String", example="801510c5-a226-413a-a4ab-e630d8aec304")
	private String uuid;
	
	@ApiModelProperty(value="名称", dataType="String", example="oss-flat1")
	private String name;
	
	@ApiModelProperty(value="Region名称", dataType="String", example="manageRegion")
	private String region;
	
	@ApiModelProperty(value="平台项目", dataType="String", example="44931aec982e46739cb3336b8511db0d")
	private String projectId;
	
	@ApiModelProperty(value="管理状态", dataType="Boolean", example="true")
	private String admin_state_up;

	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getAdmin_state_up() {
		return admin_state_up;
	}

	public void setAdmin_state_up(String admin_state_up) {
		this.admin_state_up = admin_state_up;
	}

}
