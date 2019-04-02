package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建配置模板对象")
public class FlavorCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="String", example="volume-swg-test")
	private String name;
	
//	@ApiModelProperty(value="平台项目", dataType="String", example="44931aec982e46739cb3336b8511db0d")
//	private String projectId;
	
	@ApiModelProperty(value="所属资源池", dataType="[Ljava.lang.String;")
	private List<String> regionNameArray;
	
	@ApiModelProperty(value="虚拟内核", dataType="Integer", example="1")
	private Integer vcpus;
	
	@ApiModelProperty(value="内存", dataType="Integer", example="1024")
	private Integer ram;
	
	@ApiModelProperty(value="根磁盘", dataType="Integer", example="1")
	private Integer disk;
	
	@ApiModelProperty(value="临时磁盘", dataType="Integer", example="0")
	private Integer ephemeral;
	
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
	public List<String> getRegionNameArray() {
		return regionNameArray;
	}
	public void setRegionNameArray(List<String> regionNameArray) {
		this.regionNameArray = regionNameArray;
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
}
