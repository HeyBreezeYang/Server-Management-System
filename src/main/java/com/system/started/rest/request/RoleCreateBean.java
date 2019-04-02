package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建角色对象")
public class RoleCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", name="name", dataType="String", example="swagger测试")
	private String name;
	
	@ApiModelProperty(value="权重", name="type", dataType="integer", example="10/100/1000")
	private Integer type;
	
	@ApiModelProperty(value="描述", name="description", dataType="string")
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
