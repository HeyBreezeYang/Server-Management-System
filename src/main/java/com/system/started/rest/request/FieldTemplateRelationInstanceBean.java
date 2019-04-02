package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="数据模型关系实例对象")
public class FieldTemplateRelationInstanceBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源类型",required = true, dataType="string", example="VIRTUAL")
	private String resourceType;
	
	@ApiModelProperty(value="模板ID",required = true, dataType="integer", example="20")
	private Integer templateId;
	
	@ApiModelProperty(value="字段值",required = true, dataType="string", example="开发测试")
	private String value;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
