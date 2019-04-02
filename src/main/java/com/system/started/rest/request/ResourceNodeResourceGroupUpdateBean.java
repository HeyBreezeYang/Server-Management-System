package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description="设置资源与资源组关系对象")
public class ResourceNodeResourceGroupUpdateBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="分组集合", dataType="[Ljava.lang.String;")
	private List<String> groupIds;
	
	@ApiModelProperty(value="资源集合", dataType="[Ljava.lang.String;")
	private List<String> resourceIds;
	
	@ApiModelProperty(value="资源类型", dataType="string")
	private String resourceType;

	public List<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}
	
	public List<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
}
