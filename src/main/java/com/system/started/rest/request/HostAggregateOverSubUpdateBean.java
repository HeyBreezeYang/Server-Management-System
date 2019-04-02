package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="为主机聚集对象设置超分比例")
public class HostAggregateOverSubUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@ApiModelProperty(value="主机聚集ID", dataType="Integer", example="1")
	private Integer id;
	
	@ApiModelProperty(value="名称", dataType="String", example="manage-group-swagger-test")
	private String name;
	
	@ApiModelProperty(value="所属资源池", dataType="String", example="manageRegion")
	private String region;
	
	@ApiModelProperty(value="CPU超分比例", dataType="Integer", example="1")
	private Integer cpu_oversub;
	
	@ApiModelProperty(value="内存超分比例", dataType="Integer", example="1")
	private Integer ram_oversub;
	
	@ApiModelProperty(value="存储超分比例", dataType="Integer", example="1")
	private Integer disk_oversub;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getCpu_oversub() {
		return cpu_oversub;
	}

	public void setCpu_oversub(Integer cpu_oversub) {
		this.cpu_oversub = cpu_oversub;
	}

	public Integer getRam_oversub() {
		return ram_oversub;
	}

	public void setRam_oversub(Integer ram_oversub) {
		this.ram_oversub = ram_oversub;
	}

	public Integer getDisk_oversub() {
		return disk_oversub;
	}

	public void setDisk_oversub(Integer disk_oversub) {
		this.disk_oversub = disk_oversub;
	}
}
