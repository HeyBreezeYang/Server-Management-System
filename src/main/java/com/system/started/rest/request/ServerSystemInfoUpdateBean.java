package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="编辑虚机系统信息对象")
public class ServerSystemInfoUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="管理IP地址", dataType="String", example="192.168.1.168")
	private String osIpAddress;
	
	@ApiModelProperty(value="管理用户名", dataType="String", example="root")
	private String osUserName;
	
	@ApiModelProperty(value="管理密码", dataType="String", example="vlandc")
	private String osPassword;
	
	@ApiModelProperty(value="资源与字段关系集合", dataType="List<FieldTemplateRelationInstanceBean>", required=false)
	private List<FieldTemplateRelationInstanceBean> fieldValues;

	public String getOsIpAddress() {
		return osIpAddress;
	}

	public void setOsIpAddress(String osIpAddress) {
		this.osIpAddress = osIpAddress;
	}

	public String getOsUserName() {
		return osUserName;
	}

	public void setOsUserName(String osUserName) {
		this.osUserName = osUserName;
	}

	public String getOsPassword() {
		return osPassword;
	}

	public void setOsPassword(String osPassword) {
		this.osPassword = osPassword;
	}

	public List<FieldTemplateRelationInstanceBean> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(List<FieldTemplateRelationInstanceBean> fieldValues) {
		this.fieldValues = fieldValues;
	}
}
