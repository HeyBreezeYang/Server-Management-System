package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑镜像对象")
public class ImageUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@ApiModelProperty(value="名称", dataType="String", example="oss-db")
	private String name;
	
	@ApiModelProperty(value="操作系统名称", dataType="String", example="Red Hat Enterprise Linux Server release 7.3 (Santiago)")
	private String os;

	@ApiModelProperty(value="操作系统类型", dataType="String", example="LINUX")
	private String osfamily;
	
	@ApiModelProperty(value="操作系统版本", dataType="String", example="Red Hat Enterprise Linux Server release 7.3 (Santiago)")
	private String osversion;
	
	@ApiModelProperty(value="最小虚拟内核值", dataType="String", example="1")
	private String min_cpu;
	
	@ApiModelProperty(value="最小内存值", dataType="Integer", example="1024")
	private Integer min_ram;
	
//	@ApiModelProperty(value="最小硬盘", dataType="String", example="130")
//	private String min_disk;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOsfamily() {
		return osfamily;
	}

	public void setOsfamily(String osfamily) {
		this.osfamily = osfamily;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public String getMin_cpu() {
		return min_cpu;
	}

	public void setMin_cpu(String min_cpu) {
		this.min_cpu = min_cpu;
	}

	public Integer getMin_ram() {
		return min_ram;
	}

	public void setMin_ram(Integer min_ram) {
		this.min_ram = min_ram;
	}

	
//
//	public String getMin_disk() {
//		return min_disk;
//	}
//
//	public void setMin_disk(String min_disk) {
//		this.min_disk = min_disk;
//	}
}
