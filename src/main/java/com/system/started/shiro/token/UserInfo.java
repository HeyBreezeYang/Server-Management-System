package com.system.started.shiro.token;

import java.util.HashMap;
import java.util.Set;

/**
 * @Author wyj
 * @Date 2018/6/5
 * @Description
 */
public class UserInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private String token;

	//id
	private Integer id;

	//姓名
	private String name;

	//用户名
	private String loginId;

	//密码
	private String password;

	//电子邮箱
	private String email;

	//用户状态
	private Integer status;

	//部门名称
	private String departmentName;

	//默认页面
	private String defaultPage;

	//角色信息
	private String roles;

	//角色类型
	private String roleTypes;

	//角色详细信息
	private HashMap<String, Object> userRoles;

	private Set<String> permissions;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDefaultPage() {
		return defaultPage;
	}

	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getRoleTypes() {
		return roleTypes;
	}

	public void setRoleTypes(String roleTypes) {
		this.roleTypes = roleTypes;
	}

	public HashMap<String, Object> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(HashMap<String, Object> userRoles) {
		this.userRoles = userRoles;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
