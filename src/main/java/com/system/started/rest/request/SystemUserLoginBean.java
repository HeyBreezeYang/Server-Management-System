package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="SystemUserLoginBean",description="用户登陆对象")
public class SystemUserLoginBean implements Serializable{

	private static final long serialVersionUID = 9203861339231266880L;

	@ApiModelProperty(value="用户名", dataType="string",required=true)
	private String loginId;
	
	@ApiModelProperty(value="密码", dataType="string",required=true)
	private String password;
	
	@ApiModelProperty(value="记住密码", dataType="boolean")
	private Boolean rememberMe;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}
