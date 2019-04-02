package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑子网对象")
public class SubNetworkUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="私有网络ID", dataType="String", example="801510c5-a226-413a-a4ab-e630d8aec304")
	private String networkId;
	
	@ApiModelProperty(value="名称", dataType="String", example="oss-sub-swagger-test")
	private String name;
	
	@ApiModelProperty(value="网络地址", dataType="String", example="192.168.11.0/24")
	private String cidr;
	
	@ApiModelProperty(value="IP版本", dataType="Integer", example="4")
	private String ipVersion;
	
	@ApiModelProperty(value="IP网关", dataType="String", example="192.168.11.1")
	private String gateway_ip;
	
	@ApiModelProperty(value="禁用网关", dataType="Boolean", example="false")
	private String enableDhcp;

	@ApiModelProperty(value="DNS域名解析服务", dataType="[Ljava.lang.String;")
	private String dnsNameServers;
	
	@ApiModelProperty(value="分配地址池", dataType="SubNetworkAllocationPoolBean")
	private List<SubNetworkAllocationPoolBean> allocationPools;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getIpVersion() {
		return ipVersion;
	}

	public void setIpVersion(String ipVersion) {
		this.ipVersion = ipVersion;
	}

	public String getGateway_ip() {
		return gateway_ip;
	}

	public void setGateway_ip(String gateway_ip) {
		this.gateway_ip = gateway_ip;
	}

	public String getEnableDhcp() {
		return enableDhcp;
	}

	public void setEnableDhcp(String enableDhcp) {
		this.enableDhcp = enableDhcp;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getDnsNameServers() {
		return dnsNameServers;
	}

	public void setDnsNameServers(String dnsNameServers) {
		this.dnsNameServers = dnsNameServers;
	}

	public List<SubNetworkAllocationPoolBean> getAllocationPools() {
		return allocationPools;
	}

	public void setAllocationPools(List<SubNetworkAllocationPoolBean> allocationPools) {
		this.allocationPools = allocationPools;
	}
}
