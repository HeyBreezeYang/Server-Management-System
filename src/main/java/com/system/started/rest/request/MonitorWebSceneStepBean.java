package com.system.started.rest.request;

import io.swagger.annotations.ApiModelProperty;

public class MonitorWebSceneStepBean {
	
	@ApiModelProperty(value="name", dataType="string")
	private String name;
	
	@ApiModelProperty(value="url", dataType="string")
	private String url;
	
	@ApiModelProperty(value="status_codes", dataType="string")
	private String status_codes;
	
	@ApiModelProperty(value="no", dataType="integer")
	private Integer no;
	
	@ApiModelProperty(value="timeout", dataType="integer")
	private Integer timeout;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStatus_codes() {
		return status_codes;
	}

	public void setStatus_codes(String status_codes) {
		this.status_codes = status_codes;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
}
