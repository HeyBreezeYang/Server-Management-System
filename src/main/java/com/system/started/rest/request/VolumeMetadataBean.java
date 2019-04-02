package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建卷对象")
public class VolumeMetadataBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="资源池类型", dataType="String", example="COMPUTE")
	private String poolType;

	public String getPoolType() {
		return poolType;
	}

	public void setPoolType(String poolType) {
		this.poolType = poolType;
	}
}
