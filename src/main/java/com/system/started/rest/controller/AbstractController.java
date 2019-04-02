package com.system.started.rest.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.system.started.cache.impl.UserCache;
import com.system.started.shiro.token.UserInfo;
import com.system.started.shiro.token.manager.TokenManager;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

public abstract class AbstractController {

	@Value("${oss.apigate.config.admin-login-id}")
	private static String ADMIN_LOGIN_ID;

	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private UserCache userCache;

	public HttpServletRequest httpRequest ;

	@ModelAttribute
	public void beforeControllerFunc(HttpServletRequest request){
		this.httpRequest = request;
	}

	protected void parseCurrentLoginIds(HashMap<String, Object> paramMap) {
		paramMap.put("loginId", getCurrentLoginId());
	}

	protected void parseRelationLoginIds(HashMap<String, Object> paramMap) {
		String relationLoginIdS = userCache.getSubRelationLoginIds(getCurrentLoginId());
		paramMap.put("loginId", relationLoginIdS);
	}

	protected String getCurrentLoginId(){
		UserInfo userInfo = tokenManager.getUserInfo(getCurrentLoginToken());
		if(null != userInfo){
			return userInfo.getLoginId();
		}else{
			return null;
		}
	}

	protected String getCurrentLoginToken(){
		String token = httpRequest.getHeader(TokenManager.TOKEN_KEY);
		return token;
	}

	protected String invalidRequest(String errorReson){
		Result result = new Result();
		result.setResultCode(EResultCode.PARAM_VALIDATE_FAIL);
		result.setResultMsg(errorReson);
		return JsonHelper.toJson(result);
	}

	protected String invalidRequest(){
		Result result = new Result();
		result.setResultCode(EResultCode.PARAM_VALIDATE_FAIL);
		result.setResultMsg("操作失败！");
		return JsonHelper.toJson(result);
	}
}
