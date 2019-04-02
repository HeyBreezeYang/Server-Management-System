package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑配置模板对象")
public class FlavorUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@ApiModelProperty(value="名称", dataType="String", example="volume-swg-test")
	private String name;
	
	@ApiModelProperty(value="所属资源池", dataType="String", example="manageRegion")
	private String region;
	
	@ApiModelProperty(value="虚拟内核", dataType="Integer", example="1")
	private Integer vcpus;
	
	@ApiModelProperty(value="内存", dataType="Integer", example="1024")
	private Integer ram;
	
	@ApiModelProperty(value="内存", dataType="Integer", example="1024")
	private Integer memory;
	
	@ApiModelProperty(value="根磁盘", dataType="Integer", example="10")
	private Integer disk;
	
	@ApiModelProperty(value="根磁盘", dataType="Integer", example="10")
	private Integer root_gb;
	
	@ApiModelProperty(value="临时磁盘", dataType="Integer", example="0")
	private Integer ephemeral;
	
	@ApiModelProperty(value="临时磁盘", dataType="Integer", example="0")
	private Integer ephemeral_gb;
	
	@ApiModelProperty(value="Swap磁盘", dataType="Integer", example="0")
	private Integer swap;
	
	@ApiModelProperty(value="rxtxFactor", dataType="Integer", example="10")
	private Integer rxtxFactor;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getVcpus() {
		return vcpus;
	}
	public void setVcpus(Integer vcpus) {
		this.vcpus = vcpus;
	}
	public Integer getRam() {
		return ram;
	}
	public void setRam(Integer ram) {
		this.ram = ram;
	}
	public Integer getDisk() {
		return disk;
	}
	public void setDisk(Integer disk) {
		this.disk = disk;
	}
	
	public Integer getRoot_gb() {
		return root_gb;
	}
	public void setRoot_gb(Integer root_gb) {
		this.root_gb = root_gb;
	}
	public Integer getEphemeral() {
		return ephemeral;
	}
	public void setEphemeral(Integer ephemeral) {
		this.ephemeral = ephemeral;
	}
	public Integer getSwap() {
		return swap;
	}
	public void setSwap(Integer swap) {
		this.swap = swap;
	}
	public Integer getRxtxFactor() {
		return rxtxFactor;
	}
	public void setRxtxFactor(Integer rxtxFactor) {
		this.rxtxFactor = rxtxFactor;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public Integer getEphemeral_gb() {
		return ephemeral_gb;
	}
	public void setEphemeral_gb(Integer ephemeral_gb) {
		this.ephemeral_gb = ephemeral_gb;
	}
}
