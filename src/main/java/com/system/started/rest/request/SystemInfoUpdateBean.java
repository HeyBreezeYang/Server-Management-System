package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑系统信息对象")
public class SystemInfoUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="ID", dataType="String", example="1450836291")
	private String id;
	
	@ApiModelProperty(value="配置管理组件", dataType="String", example="192.168.1.199")
	private String agentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
}
