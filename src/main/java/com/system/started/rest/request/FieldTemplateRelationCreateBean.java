package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="创建数据模型关系对象")
public class FieldTemplateRelationCreateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源类型",required = true, dataType="string", example="VIRTUAL")
	private String resourceTypes;

	@ApiModelProperty(value="数据模版id",required = true,  dataType="string", example="1")
	private String templateIds;

	public String getResourceTypes() {
		return resourceTypes;
	}

	public void setResourceTypes(String resourceTypes) {
		this.resourceTypes = resourceTypes;
	}

	public String getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}
}
