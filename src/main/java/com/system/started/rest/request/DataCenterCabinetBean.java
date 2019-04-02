package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="创建机柜对象")
public class DataCenterCabinetBean implements Serializable{

	/**
	 * [{column: "A", startNum: 1, endNum: 50}]
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="列", dataType="String", example="A")
	private String column;

	@ApiModelProperty(value="开始位置", dataType="Integer", example="1")
	private Integer startNum;

	@ApiModelProperty(value="结束位置", dataType="Integer", example="50")
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
