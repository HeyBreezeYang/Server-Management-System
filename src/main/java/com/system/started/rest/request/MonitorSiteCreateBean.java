package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建站点监控对象")
public class MonitorSiteCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="string")
	private String name;
	
	@ApiModelProperty(value="站点地址", dataType="string")
	private String siteUrl;
	
	@ApiModelProperty(value="类型", dataType="string")
	private String type;
	
	@ApiModelProperty(value="频率", dataType="integer")
	private Integer frequency;
	
	@ApiModelProperty(value="通道", dataType="string")
	private String passageway;
	
	@ApiModelProperty(value="超时时间", dataType="integer")
	private Integer timeout;
	
	@ApiModelProperty(value="启用/停用", dataType="String")
	private Integer disabled;

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getPassageway() {
		return passageway;
	}

	public void setPassageway(String passageway) {
		this.passageway = passageway;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
}
