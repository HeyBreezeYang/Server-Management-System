package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="设置镜像显示状态对象")
public class ImageUpdateDisplayStatusBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="镜像ID集合", dataType="[Ljava.lang.String;")
	private List<String> imageIds;

	public List<String> getImageIds() {
		return imageIds;
	}

	public void setImageIds(List<String> imageIds) {
		this.imageIds = imageIds;
	}
	
}
