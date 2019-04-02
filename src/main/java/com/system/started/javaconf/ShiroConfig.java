package com.system.started.javaconf;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.system.started.shiro.session.dao.ShiroSessionRedisDAO;

@Configuration
public class ShiroConfig {

	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
		shiroFilterFactoryBean.setLoginUrl("/notLogin");
		// 设置无权限时跳转的 url;
		shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");

		// 设置拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/#/**", "anon");
		filterChainDefinitionMap.put("/system/login", "anon");
		filterChainDefinitionMap.put("/system/logout","anon");
		filterChainDefinitionMap.put("/system/secretkey", "anon");
		filterChainDefinitionMap.put("/api/system/login", "anon");
		filterChainDefinitionMap.put("/api/system/logout", "anon");
		filterChainDefinitionMap.put("/api/system/secretkey", "anon");
		filterChainDefinitionMap.put("/page/uploadify.uploadify", "anon");
		filterChainDefinitionMap.put("/favicon.ico", "anon");
		filterChainDefinitionMap.put("/doc.html", "anon");
		filterChainDefinitionMap.put("/docs.html", "anon");
		filterChainDefinitionMap.put("/uploadify.uploadify", "anon");
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/swagger/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/upload/**", "anon");
		filterChainDefinitionMap.put("/export/**", "anon");
		filterChainDefinitionMap.put("/script/**", "anon");
		filterChainDefinitionMap.put("/service/**", "anon");
		filterChainDefinitionMap.put("/swagger-resources", "anon");
//		filterChainDefinitionMap.put("/** ", "authc");
		
//		// 游客，开发权限
//		filterChainDefinitionMap.put("/guest/**", "anon");
//		// 用户，需要角色权限 “user”
//		filterChainDefinitionMap.put("/user/**", "roles[user]");
//		// 管理员，需要角色权限 “admin”
//		filterChainDefinitionMap.put("/admin/**", "roles[admin]");
//		// 开放登陆接口
//		filterChainDefinitionMap.put("/login", "anon");
//		// 其余接口一律拦截
//		// 主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
//		filterChainDefinitionMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		System.out.println("Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}

	@Bean
	@ConfigurationProperties(prefix = "oss.shiro.session.scheduler")
	public SessionValidationScheduler sessionValidationScheduler() {
		SessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
		return sessionValidationScheduler;
	}

	@Bean
	@ConfigurationProperties(prefix = "oss.shiro.session")
	protected SessionManager sessionManager(List<SessionListener> listeners, ShiroSessionRedisDAO shiroSessionDao, SessionValidationScheduler sessionValidationScheduler) {
		DefaultSessionManager sessionManager = new DefaultSessionManager();
		shiroSessionDao.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
		sessionManager.setSessionDAO(shiroSessionDao);
		sessionManager.setSessionListeners(listeners);
		sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
		return sessionManager;
	}

	@Bean
	public SessionsSecurityManager securityManager(List<Realm> realms, SessionManager sessionManager) {
		SessionsSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealms(realms);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}
}
