package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="主机聚集下主机对象")
public class HostAggregateHostBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="主机名称", dataType="String", example="dell.test.com")
	private String host;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	
}
