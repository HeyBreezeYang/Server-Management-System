package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="挂载/卸载卷对象")
public class VolumeResetStatusBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="动作<br />例子：attach / detach", dataType="String", example="attach")
	private String action;
	
	@ApiModelProperty(value="虚机UUID", dataType="String", example="91fd7b46-9526-4749-ad63-375937a4877f")
	private String serverUuid;
	
	@ApiModelProperty(value="卷UUID ", dataType="String", example="4fca404f-2c44-453f-9a21-6bbf85cbe281")
	private String volumeUuid;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getServerUuid() {
		return serverUuid;
	}

	public void setServerUuid(String serverUuid) {
		this.serverUuid = serverUuid;
	}

	public String getVolumeUuid() {
		return volumeUuid;
	}

	public void setVolumeUuid(String volumeUuid) {
		this.volumeUuid = volumeUuid;
	}
}
