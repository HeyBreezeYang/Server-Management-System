package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建主机监控对象")
public class MonitorHostCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="ID", dataType="integer")
	private String id;
	
	@ApiModelProperty(value="名称", dataType="string")
	private String name;
	
	@ApiModelProperty(value="分组", dataType="integer")
	private Integer groupId;
	
	@ApiModelProperty(value="IP地址", dataType="string")
	private String ipAddress;
	
	@ApiModelProperty(value="端口", dataType="string")
	private String port;
	
	@ApiModelProperty(value="CPU", dataType="integer")
	private Integer cpu;
	
	@ApiModelProperty(value="内存", dataType="integer")
	private Integer memory;
	
	@ApiModelProperty(value="磁盘", dataType="integer")
	private Integer disk;
	
	@ApiModelProperty(value="操作系统类型", dataType="string")
	private String systemType;
	
	@ApiModelProperty(value="操作系统子类型", dataType="string")
	private String systemSubType;
	
	@ApiModelProperty(value="管理组件标识", dataType="string")
	private String agentId;
	
	@ApiModelProperty(value="监控状态", dataType="integer")
	private Integer zabbixAgentStatus;
	
	@ApiModelProperty(value="连接状态", dataType="integer")
	private Integer connectStatus;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public Integer getCpu() {
		return cpu;
	}
	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public Integer getDisk() {
		return disk;
	}
	public void setDisk(Integer disk) {
		this.disk = disk;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getSystemSubType() {
		return systemSubType;
	}
	public void setSystemSubType(String systemSubType) {
		this.systemSubType = systemSubType;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public Integer getZabbixAgentStatus() {
		return zabbixAgentStatus;
	}
	public void setZabbixAgentStatus(Integer zabbixAgentStatus) {
		this.zabbixAgentStatus = zabbixAgentStatus;
	}
	public Integer getConnectStatus() {
		return connectStatus;
	}
	public void setConnectStatus(Integer connectStatus) {
		this.connectStatus = connectStatus;
	}
}
