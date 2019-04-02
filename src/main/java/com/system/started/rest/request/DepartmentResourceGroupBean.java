package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="组织机构与资源组关系对象")
public class DepartmentResourceGroupBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源组类型，<br>例子：resourcePool/group/datacenter",required=true, dataType="string")
	private String groupType;
	
	@ApiModelProperty(value="资源组ID，<br>例子：1", required=true, dataType="string")
	private String groupId;

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
