package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="主机聚集添加/移除主机对象")
public class HostAggregateActionHostBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="移除主机", dataType="HostAggregateHostBean")
	private HostAggregateHostBean remove_host;
	
	@ApiModelProperty(value="添加主机", dataType="HostAggregateHostBean")
	private HostAggregateHostBean add_host;

	public HostAggregateHostBean getRemove_host() {
		return remove_host;
	}

	public void setRemove_host(HostAggregateHostBean remove_host) {
		this.remove_host = remove_host;
	}

	public HostAggregateHostBean getAdd_host() {
		return add_host;
	}

	public void setAdd_host(HostAggregateHostBean add_host) {
		this.add_host = add_host;
	}
}
