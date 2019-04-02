package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="设置组织机构与资源关系对象")
public class DepartmentResourceUpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="资源", required=true, dataType="List<DepartmentResourceBean>")
	private List<DepartmentResourceBean> dataList;

	public List<DepartmentResourceBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<DepartmentResourceBean> dataList) {
		this.dataList = dataList;
	}
}
