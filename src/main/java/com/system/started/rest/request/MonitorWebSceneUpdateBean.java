package com.system.started.rest.request;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MonitorWebSceneUpdateBean {
	
	@ApiModelProperty(value="name", dataType="string")
	private String name;
	
	@ApiModelProperty(value="httptestid", dataType="string")
	private String httptestid;
	
	@ApiModelProperty(value="hostid", dataType="integer")
	private Integer hostid;
	
	@ApiModelProperty(value="host", dataType="integer")
	private Integer host;
	
	@ApiModelProperty(value="agent", dataType="string")
	private String agent;
	
	@ApiModelProperty(value="delay", dataType="string")
	private String delay;
	
	@ApiModelProperty(value="monitorType", dataType="string")
	private String monitorType;
	
	@ApiModelProperty(value="status", dataType="string")
	private String status;
	
	@ApiModelProperty(value="参数", dataType="string")
	private String params;
	 
	@ApiModelProperty(value="包", dataType="integer")
	private String packages;
	
	@ApiModelProperty(value="passageway", dataType="string")
	private String passageway;
	
	@ApiModelProperty(value="告警频率", dataType="Integer")
	private Integer warningFeq;
	 
	@ApiModelProperty(value="告警通知类型", dataType="String")
	private String mediaType;
	
	@ApiModelProperty(value="URL回调", dataType="String")
	private String URLCallback;
	
	@ApiModelProperty(value="类型值", dataType="String")
	private String typeValue;
	
	@ApiModelProperty(value="key_值", dataType="string")
	private String key_;
	
	@ApiModelProperty(value="类型值", dataType="integer")
	private Integer type;
	
	@ApiModelProperty(value="数据类型", dataType="integer")
	private Integer valueType;
	
	@ApiModelProperty(value="接口ID", dataType="integer")
	private Integer interfaceid;
	
	public Integer getWarningFeq() {
		return warningFeq;
	}

	public void setWarningFeq(Integer warningFeq) {
		this.warningFeq = warningFeq;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String getURLCallback() {
		return URLCallback;
	}

	public void setURLCallback(String uRLCallback) {
		URLCallback = uRLCallback;
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

	@ApiModelProperty(value="steps", dataType="array")
	private List<MonitorWebSceneStepBean> steps;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getHttptestid() {
		return httptestid;
	}

	public void setHttptestid(String httptestid) {
		this.httptestid = httptestid;
	}

	public Integer getHostid() {
		return hostid;
	}

	public void setHostid(Integer hostid) {
		this.hostid = hostid;
	}

	public Integer getHost() {
		return host;
	}

	public void setHost(Integer host) {
		this.host = host;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}
	
	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public String getKey_() {
		return key_;
	}

	public void setKey_(String key_) {
		this.key_ = key_;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public Integer getInterfaceid() {
		return interfaceid;
	}

	public void setInterfaceid(Integer interfaceid) {
		this.interfaceid = interfaceid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassageway() {
		return passageway;
	}

	public void setPassageway(String passageway) {
		this.passageway = passageway;
	}

	public List<MonitorWebSceneStepBean> getSteps() {
		return steps;
	}

	public void setSteps(List<MonitorWebSceneStepBean> steps) {
		this.steps = steps;
	}

	
}
