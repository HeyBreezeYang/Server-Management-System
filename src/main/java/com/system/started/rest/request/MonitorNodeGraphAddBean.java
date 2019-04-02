package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="添加监控图表对象")
public class MonitorNodeGraphAddBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="图表ID集合", dataType="[Ljava.lang.String;")
	private List<String> graphIds;

	public List<String> getGraphIds() {
		return graphIds;
	}

	public void setGraphIds(List<String> graphIds) {
		this.graphIds = graphIds;
	}
	
}
