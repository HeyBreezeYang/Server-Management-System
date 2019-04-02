package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="创建数据模板对象")
public class FieldTemplateCreateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="显示名称",required = true, dataType="string")
	private String label;
	
	@ApiModelProperty(value="字段名称",required = true, dataType="string")
	private String fieldName;
	
	@ApiModelProperty(value="控件类型",required = true, dataType="string")
	private String type;
	
	@ApiModelProperty(value="最大值",required = false, dataType="string")
	private String max;
	
	@ApiModelProperty(value="默认值",required = true, dataType="string")
	private String defaultValue;
	
	@ApiModelProperty(value="是否必须",required = true, dataType="integer")
	private Integer isRequire;
	
	@ApiModelProperty(value="是否只读",required = true, dataType="integer")
	private Integer isReadonly;
	
	@ApiModelProperty(value="状态",required = true, dataType="string")
	private String status;
	
	@ApiModelProperty(value="排序",required = true, dataType="integer")
	private Integer order;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getIsRequire() {
		return isRequire;
	}
	public void setIsRequire(Integer isRequire) {
		this.isRequire = isRequire;
	}
	public Integer getIsReadonly() {
		return isReadonly;
	}
	public void setIsReadonly(Integer isReadonly) {
		this.isReadonly = isReadonly;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
}
