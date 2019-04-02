package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="设置用户与资源关系对象")
public class UserResourceUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源", required=true, dataType="List<UserResourceBean>")
	private List<UserResourceBean> dataList;

	public List<UserResourceBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<UserResourceBean> dataList) {
		this.dataList = dataList;
	}
}
