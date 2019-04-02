package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="更新数据中心用户对象")
public class DataCenterUserUpdateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="姓名", dataType="string", required=true)
	private String name;

	@ApiModelProperty(value="类型", dataType="string", required=true)
	private String type;

	@ApiModelProperty(value="电话", dataType="string", required=true)
	private String phone;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
