package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="编辑资源组对象")
public class ResourceGroupUpdateBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源组名称", dataType="String", example="tomcat-group-test")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
