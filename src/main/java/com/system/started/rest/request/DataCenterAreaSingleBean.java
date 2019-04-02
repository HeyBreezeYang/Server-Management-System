package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="机柜对象")
public class DataCenterAreaSingleBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="列名", dataType="string")
	private String name;
	
	@ApiModelProperty(value="机柜编号", dataType="integer")
	private Integer cabinetNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCabinetNum() {
		return cabinetNum;
	}

	public void setCabinetNum(Integer cabinetNum) {
		this.cabinetNum = cabinetNum;
	}
}
