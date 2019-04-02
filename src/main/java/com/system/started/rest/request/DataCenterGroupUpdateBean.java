package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="编辑数据中心对象")
public class DataCenterGroupUpdateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="string", required=true)
	private String name;
	
	@ApiModelProperty(value="图片", dataType="string")
	private String cover;
	
	@ApiModelProperty(value="授权认证", dataType="string")
	private String authentication;
	
	@ApiModelProperty(value="别名", dataType="string", required=true)
	private String alias;

	@ApiModelProperty(value="地址", dataType="string", required=true)
	private String address;

	@ApiModelProperty(value="规模", dataType="string")
	private String scale;

	@ApiModelProperty(value="描述", dataType="string")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
