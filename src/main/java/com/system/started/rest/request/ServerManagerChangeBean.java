package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="变更虚机管理用户对象")
public class ServerManagerChangeBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="虚机ID集合字符串", dataType="String", example="1607498642,1666160976")
	private String ids;
	
	@ApiModelProperty(value="云堆栈ID集合字符串", dataType="String", example="")
	private String stackIds;
	
	@ApiModelProperty(value="管理用户", dataType="String", example="admin")
	private String manageUser;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getStackIds() {
		return stackIds;
	}

	public void setStackIds(String stackIds) {
		this.stackIds = stackIds;
	}

	public String getManageUser() {
		return manageUser;
	}

	public void setManageUser(String manageUser) {
		this.manageUser = manageUser;
	}
}
