package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑触发器对象")
public class MonitorTriggerUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="描述", dataType="string")
	private String description;
		
	@ApiModelProperty(value="阈值", dataType="string")
	private Integer threshold;
	
	@ApiModelProperty(value="级别，<br>例子：可选值：0：通知,1：普通,2：告警,3：严重,4：致命", dataType="string")
	private String priority;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
}
