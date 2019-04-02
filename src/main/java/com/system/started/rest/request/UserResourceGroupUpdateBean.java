package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="设置用户与资源组关系对象")
public class UserResourceGroupUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源组",required=true, dataType="List<UserResourceGroupBean>")
	private List<UserResourceGroupBean> groups;

	public List<UserResourceGroupBean> getGroups() {
		return groups;
	}

	public void setGroups(List<UserResourceGroupBean> groups) {
		this.groups = groups;
	}
}
