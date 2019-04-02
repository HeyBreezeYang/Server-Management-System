package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建卷对象")
public class VolumeCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="String", example="volume-swg-test")
	private String name;
	
	@ApiModelProperty(value="平台项目", dataType="String", example="44931aec982e46739cb3336b8511db0d")
	private String projectId;
	
	@ApiModelProperty(value="所属资源池", dataType="String", example="manageRegion")
	private String region;
	
	@ApiModelProperty(value="容量", dataType="integer", example="10")
	private Integer size;
	
	@ApiModelProperty(value="元数据", dataType="VolumeMetadataBean")
	private VolumeMetadataBean metadata;
	
	@ApiModelProperty(value="描述", dataType="String", example="通过swagger2创建卷")
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public VolumeMetadataBean getMetadata() {
		return metadata;
	}
	public void setMetadata(VolumeMetadataBean metadata) {
		this.metadata = metadata;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
