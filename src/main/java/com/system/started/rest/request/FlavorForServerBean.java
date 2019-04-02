package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建虚机时指定配置模板对象")
public class FlavorForServerBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="内存", dataType="Integer", example="2048")
	private int ram;
	
	@ApiModelProperty(value="CPU", dataType="Integer", example="1")
	private int vcpus;
	
	@ApiModelProperty(value="硬盘", dataType="Integer", example="50")
	private int disk;
	
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}
	public int getVcpus() {
		return vcpus;
	}
	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}
	public int getDisk() {
		return disk;
	}
	public void setDisk(int disk) {
		this.disk = disk;
	}
	
}
