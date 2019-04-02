package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="创建交换机端口对象")
public class ResourceNetworkNodePortCreateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源ID", dataType="string", required=false)
	private String nodeId;
	
	@ApiModelProperty(value="端口", dataType="string", required=false)
	private String portId;
	
	@ApiModelProperty(value="vlan", dataType="string", required=false)
	private String vlan;
	
	@ApiModelProperty(value="vlan配置", dataType="string", required=false)
	private String vlanConfig;
	
	@ApiModelProperty(value="状态", dataType="string", required=false)
	private String status;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	public String getVlan() {
		return vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public String getVlanConfig() {
		return vlanConfig;
	}

	public void setVlanConfig(String vlanConfig) {
		this.vlanConfig = vlanConfig;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
