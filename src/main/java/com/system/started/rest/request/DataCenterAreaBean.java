package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="机柜对象")
public class DataCenterAreaBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="列名", dataType="string")
	private String column;
	
	@ApiModelProperty(value="开始编号", dataType="integer")
	private Integer startNum;
	
	@ApiModelProperty(value="结束编号", dataType="integer")
	private Integer endNum;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Integer getStartNum() {
		return startNum;
	}

	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}

	public Integer getEndNum() {
		return endNum;
	}

	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}
}
