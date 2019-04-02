package com.system.started.shiro.filter;

import java.util.HashMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.system.started.cache.impl.UserCache;
import com.system.started.shiro.token.UserInfo;
import com.system.started.shiro.token.manager.TokenManager;
import com.vlandc.oss.common.JsonHelper;


/**
 * @Author wyj
 * @Date 2018/6/7
 * @Description
 */
public class AuthFilter extends AccessControlFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private UserCache userCache;

	@Override
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		try {
			String url = httpServletRequest.getRequestURI();// .substring(1);
			if(url.equals("/")){
				return true;
			}
			String sessionID = httpServletRequest.getHeader(TokenManager.TOKEN_KEY);
		 	UserInfo userInfo = tokenManager.getUserInfo(sessionID);
		 	if (userInfo == null) {
		 		logger.error("access the url (" + url + ")  error ,because the loginId is null.");
				return false;
			} else {
				if (!userCache.userExists(userInfo.getLoginId())) {
					logger.error("access the url (" + url + ")  error ,because the loginId is deleted.");
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("validate access control error !", e);
		}
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
		HashMap<String, String> resultMap = new HashMap<>();
		resultMap.put("status", "needLogin");
		httpServletResponse.getWriter().write(JsonHelper.toJson(resultMap));
		logger.debug("access denied, return login page !");
		return false;
	}



}
