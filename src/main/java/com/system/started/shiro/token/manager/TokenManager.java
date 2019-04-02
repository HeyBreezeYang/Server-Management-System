package com.system.started.shiro.token.manager;

import java.util.Optional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.shiro.session.dao.ShiroSessionRedisDAO;
import com.system.started.shiro.token.ShiroToken;
import com.system.started.shiro.token.UserInfo;


/**
 * Shiro管理下的Token工具类
 */
@Component
public class TokenManager {

	private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

	public static final String TOKEN_KEY = "token";

	@Autowired
	private ShiroSessionRedisDAO shiroSessionDao;
	/**
	 * 获取当前登录的用户User对象
	 * 只允许登录的时候进行使用
	 *
	 * @return
	 */
	public UserInfo getToken() {
		UserInfo token = (UserInfo) SecurityUtils.getSubject().getPrincipal();
		return token;
	}


	/**
	 * 获取当前用户的Session
	 *
	 * @param sessionID token
	 * @return
	 */
	public Session getSession(String sessionID) {
		try {
			SessionKey key = new DefaultSessionKey(sessionID);
			return SecurityUtils.getSecurityManager().getSession(key);
		} catch (UnknownSessionException e) {
			logger.error("当前sessionId: [" + sessionID + "]无效");
		} catch (Exception e) {
			logger.error("根据sessionID获取Session出错", e);
		}
		return null;
	}


	/**
	 * 获取用户登录信息
	 */
	public UserInfo getUserInfo(String sessionID) {
		try {
			Session session = getSession(sessionID);
			if (null != session) {
				Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
				UserInfo userInfo = (UserInfo) coll.getPrimaryPrincipal();
				userInfo.setToken(sessionID);
				return userInfo;
			}
		} catch (Exception e) {
			logger.error("获取用户登录信息出错", e);
		}
		return null;
	}


	/**
	 * 登录
	 *
	 * @param user
	 * @param rememberMe
	 * @return
	 */
	public UserInfo login(UserInfo user, Boolean rememberMe) {
		ShiroToken token = new ShiroToken(user.getLoginId(), user.getPassword());
		if (null == rememberMe) {
			token.setRememberMe(false);
		} else {
			token.setRememberMe(rememberMe);
		}
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		return getUserInfo(subject.getSession().getId().toString());
	}


	/**
	 * 判断是否登录
	 *
	 * @return
	 */
	public boolean isLogin(String sessionID) {
		return null != shiroSessionDao.getSession(sessionID);
	}

	/**
	 * 退出登录
	 */
	public void logout(String sessionID) {
//		Subject subject = SecurityUtils.getSubject();
		shiroSessionDao.delete(Optional.ofNullable(sessionID).orElse(new String()));
		//subject.logout();
	}

}
