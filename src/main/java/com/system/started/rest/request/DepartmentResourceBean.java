package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="组织机构与资源关系对象")
public class DepartmentResourceBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源类型，<br>例子：VIR_VOLUME/VIR_NETWORK/VIR_INSTANCE/VIR_IMAGE/VIR_FLAVOR/VIRTUAL/OPERATION/DEPLOY", example="VIR_INSTANCE", required=true, dataType="string")
	private String resourceType;
	
	@ApiModelProperty(value="资源ID，<br>例子：1714943517,139664949", example="1714943517,139664949" , required=true, dataType="string")
	private String resourceIds;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}
	
}
