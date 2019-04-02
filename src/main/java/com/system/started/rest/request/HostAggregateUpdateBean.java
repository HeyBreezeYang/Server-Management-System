package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑主机聚集对象")
public class HostAggregateUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="主机聚集", dataType="HostAggregateBean")
	private HostAggregateBean aggregate;

	public HostAggregateBean getAggregate() {
		return aggregate;
	}

	public void setAggregate(HostAggregateBean aggregate) {
		this.aggregate = aggregate;
	}
	
	
}
