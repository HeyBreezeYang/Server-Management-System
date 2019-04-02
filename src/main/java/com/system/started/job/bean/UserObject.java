package com.system.started.job.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserObject {

	@Value("${oss.apigate.dh.login-name}")
	private String LoginName;
	
	@Value("${oss.apigate.dh.login-pass}")
	private String LoginPass;
	
	public String getLoginName() {
		return LoginName;
	}
	public void setLoginName(String loginName) {
		LoginName = loginName;
	}
	public String getLoginPass() {
		return LoginPass;
	}
	public void setLoginPass(String loginPass) {
		LoginPass = loginPass;
	}
}
