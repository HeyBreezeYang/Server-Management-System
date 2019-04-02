package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="子网分配地址池对象")
public class SubNetworkAllocationPoolBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@ApiModelProperty(value="开始地址", dataType="String", example="192.168.21.2")
	private String start;
	
	@ApiModelProperty(value="结束地址", dataType="String", example="192.168.21.25")
	private String end;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}
