package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="绑定监控图表指标对象")
public class MonitorGraphItemUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="指标ID集合", dataType="List")
	private List<MonitorGraphItemBean> refItems;

	public List<MonitorGraphItemBean> getRefItems() {
		return refItems;
	}

	public void setRefItems(List<MonitorGraphItemBean> refItems) {
		this.refItems = refItems;
	}	
}
