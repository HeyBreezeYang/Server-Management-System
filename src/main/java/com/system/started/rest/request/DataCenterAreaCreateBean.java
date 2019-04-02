package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description="创建机柜对象")
public class DataCenterAreaCreateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="类型", dataType="string", required=true)
	private String type;

	@ApiModelProperty(value="机柜集合", dataType="array", required=true)
	private List<DataCenterAreaBean> cabinets;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DataCenterAreaBean> getCabinets() {
		return cabinets;
	}

	public void setCabinets(List<DataCenterAreaBean> cabinets) {
		this.cabinets = cabinets;
	}
	
	
}
