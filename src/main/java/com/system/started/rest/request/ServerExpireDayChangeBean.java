package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="变更生命周期对象")
public class ServerExpireDayChangeBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="虚机名称", dataType="String", example="vm-swagger-test")
	private String name;
	
	@ApiModelProperty(value="变更类型", dataType="String", example="DATE")
	private String changeType;
	
//	@ApiModelProperty(value="创建时间", dataType="Long", example="1528904052000")
//	private Long createTime;
	
//	@ApiModelProperty(value="到期日期", dataType="String", example="2018-06-15")
//	private String expireDate;
//	
//	@ApiModelProperty(value="到期天数", dataType="String", example="2")
//	private String expireDay;
//	
//	@ApiModelProperty(value="生命周期类型", dataType="String", example="-2")
//	private String expireTime;
//	
//	@ApiModelProperty(value="上一次生命周期类型", dataType="String", example="-1")
//	private String oldExpireDay;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

//	public Long getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Long createTime) {
//		this.createTime = createTime;
//	}
//
//	public String getExpireDate() {
//		return expireDate;
//	}
//
//	public void setExpireDate(String expireDate) {
//		this.expireDate = expireDate;
//	}
//
//	public String getExpireDay() {
//		return expireDay;
//	}
//
//	public void setExpireDay(String expireDay) {
//		this.expireDay = expireDay;
//	}
//
//	public String getExpireTime() {
//		return expireTime;
//	}
//
//	public void setExpireTime(String expireTime) {
//		this.expireTime = expireTime;
//	}
//
//	public String getOldExpireDay() {
//		return oldExpireDay;
//	}
//
//	public void setOldExpireDay(String oldExpireDay) {
//		this.oldExpireDay = oldExpireDay;
//	}
}
