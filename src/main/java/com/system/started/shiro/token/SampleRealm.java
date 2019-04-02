package com.system.started.shiro.token;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.license.LicenseManager;
import com.system.started.shiro.token.manager.TokenManager;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.AESHelper;
import com.vlandc.oss.common.ConcurrentHashSet;


/**
 * shiro 认证 + 授权   重写
 */
@Component
public class SampleRealm extends AuthorizingRealm {

	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private DBService dbService;

	@Autowired
	private LicenseManager licenseManager;
	
	public SampleRealm() {
		super();
	}


	/**
	 * 认证信息，主要针对用户登录，
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		
//		//验证是否已经加载license
//		boolean licenseStatus = licenseManager.validateLicenseExist();
//		boolean licenseExpireStatus = licenseManager.validateLicenseExpire();
		
//		if (!licenseStatus) {
//			throw new UnknownAccountException("授权文件存在问题，请重新上传授权文件进行验证！");
//		}else if(!licenseExpireStatus){
//			throw new UnknownAccountException("授权文件已过期，请重新上传授权文件进行验证！");
//		}else{
			ShiroToken token = (ShiroToken) authcToken;
			HashMap<String, Object> parameter = new HashMap<>(4);
			parameter.put("loginId", token.getUsername());
			parameter.put("op", "local");
			parameter.put("status", 1); //1 用户启用 ； 0 用户禁用
			List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USERS, parameter);
			if (resultList.size() == 0) {
				throw new UnknownAccountException("该用户不存在！");
			}
			UserInfo userInfo = null;
			try {
				HashMap<String, Object> userMap = resultList.get(0);
				userInfo = (UserInfo) CommonUtil.mapToObject(userMap, UserInfo.class);
			} catch (Exception e) {
				throw new UnknownAccountException("该用户异常！");
			}
//			if (!AESHelper.decryptString((String)token.getPswd()).equals(AESHelper.decryptString((String) userInfo.getPassword()))) {
//				throw new AccountException("用户名或密码不正确！");
//			} else 
			if (userInfo.getStatus().equals(0)) {
				throw new DisabledAccountException("帐号已经禁止登录！");
			} else {
				//登录成功
				HashMap<String, Object> paramMap = new HashMap<>();
				paramMap.put("userId", userInfo.getId());
				HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_ROLES, paramMap);
				if (null != resultMap) {
					userInfo.setUserRoles(resultMap);

				}
				 paramMap = new HashMap<>();
//				paramMap.put("loginId", "'" + userInfo.getLoginId() + "'");
				paramMap.put("loginId", userInfo.getLoginId());
				List<HashMap<String, Object>> menuList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USER_MENUS, paramMap);
				Set<String> permissions = new ConcurrentHashSet<String>();
				for (HashMap<String, Object> menu : menuList) {
					if (null != menu && menu.containsKey("url") && null != menu.get("url")) {
						permissions.add(menu.get("url").toString());
					}
				}
				userInfo.setPermissions(permissions);
			}
			return new SimpleAuthenticationInfo(userInfo, token.getPswd(), getName());
//		}
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {


		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		UserInfo userInfo = tokenManager.getToken();
		//info.setRoles(userInfo.getRoles().);
		//info.setStringPermissions(userInfo.getPermissions());
		return info;
	}

	/**
	 * 清空当前用户权限信息
	 */
	public void clearCachedAuthorizationInfo() {
		PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 指定principalCollection 清除
	 */
	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}
}
