package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="编辑机柜对象")
public class DataCenterCabinetUpdateBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称，<br>例子：A", dataType="string")
	private String name;

	@ApiModelProperty(value="机柜编号，<br>例子：1", dataType="integer")
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
