package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="创建机柜对象")
public class DataCenterAreaSingleCreateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="类型", dataType="string", required=true)
	private String type;

	@ApiModelProperty(value="机柜集合", dataType="DataCenterAreaBean", required=true)
	private DataCenterAreaSingleBean cabinets;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DataCenterAreaSingleBean getCabinets() {
		return cabinets;
	}

	public void setCabinets(DataCenterAreaSingleBean cabinets) {
		this.cabinets = cabinets;
	}
	
	
}
