package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑资产生命周期对象")
public class AssetLifecycleUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="string")
	private String name;
	
	@ApiModelProperty(value="类型", dataType="integer")
	private Integer type;
	
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
}
