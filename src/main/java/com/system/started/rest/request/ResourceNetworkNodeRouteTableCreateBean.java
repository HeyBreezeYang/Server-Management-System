package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="创建路由表对象")
public class ResourceNetworkNodeRouteTableCreateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源ID", dataType="string", required=false)
	private String nodeId;
	
	@ApiModelProperty(value="目的地址", dataType="string", required=false)
	private String destination;
	
	@ApiModelProperty(value="下一跳", dataType="string", required=false)
	private String nextHop;
	
	@ApiModelProperty(value="网关", dataType="string", required=false)
	private String gateway;
	
	@ApiModelProperty(value="子网掩码", dataType="string", required=false)
	private String netmask;
	
	@ApiModelProperty(value="接口名称", dataType="string", required=false)
	private String iface;
	
	@ApiModelProperty(value="路由类型", dataType="string", required=false)
	private String type;
	
	@ApiModelProperty(value="路由状态", dataType="string", required=false)
	private String status;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getNextHop() {
		return nextHop;
	}

	public void setNextHop(String nextHop) {
		this.nextHop = nextHop;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getIface() {
		return iface;
	}

	public void setIface(String iface) {
		this.iface = iface;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
