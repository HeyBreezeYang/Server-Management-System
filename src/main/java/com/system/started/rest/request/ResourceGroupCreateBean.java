package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description="创建资源组对象")
public class ResourceGroupCreateBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源组名称", dataType="string", example="tomcat-group-test")
	private String name;
	
	@ApiModelProperty(value="类型", dataType="string", example="APP_SYSTEM/COMMON")
	private String type;

	@ApiModelProperty(value="资源集合", dataType="[Ljava.lang.String;")
	private List<String> resourceIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}
}
