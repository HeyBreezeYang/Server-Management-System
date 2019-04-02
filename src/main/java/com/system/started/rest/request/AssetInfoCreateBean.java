package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建资产对象")
public class AssetInfoCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="string")
	private String name;
	
	@ApiModelProperty(value="编号", dataType="string")
	private String serialNumber;
	
	@ApiModelProperty(value="IP地址", dataType="string")
	private String ipAddress;
	
	@ApiModelProperty(value="类型", dataType="integer")
	private Integer typeId;

	@ApiModelProperty(value="字段值", dataType="array")
	private List<AssetInfoFieldTemplateInstanceBean> fieldValues;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public List<AssetInfoFieldTemplateInstanceBean> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(List<AssetInfoFieldTemplateInstanceBean> fieldValues) {
		this.fieldValues = fieldValues;
	} 
}
