package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="设置组织机构与资源组关系对象")
public class DepartmentResourceGroupUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源组", required=true, dataType="List<DepartmentResourceGroupBean>")
	private List<DepartmentResourceGroupBean> groups;

	public List<DepartmentResourceGroupBean> getGroups() {
		return groups;
	}

	public void setGroups(List<DepartmentResourceGroupBean> groups) {
		this.groups = groups;
	}
}
