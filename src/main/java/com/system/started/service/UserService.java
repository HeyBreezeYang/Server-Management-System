package com.system.started.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.system.started.action.wrapper.SystemActionWrapper;
import com.system.started.cache.impl.RoleCache;
import com.system.started.cache.impl.UserCache;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;
import com.system.started.license.LicenseManager;
import com.system.started.syslog.ESysLogOperType;
import com.system.started.syslog.SysLogObj;
import com.system.started.syslog.WebSysLogTool;
import com.vlandc.oss.common.AESHelper;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserService.class);

	private static final String SUB_MENU_TAG = "subMenus";

	@Value("${oss.apigate.config.admin-login-id}")
	String ADMIN_LOGIN_ID;

	@Value("${oss.apigate.config.user-data-auth-default}")
	String USER_DATA_AUTH_DEFAULT;

	@Value("${oss.apigate.config.openstack-role-default}")
	String OPENSTACK_ROLE_DEFAULT;
	
	@Autowired
	private UserCache userCache;

	@Autowired
	private RoleCache roleCache;


	@Autowired
	private IDao userRelationDao;

	@Autowired
	private LicenseManager licenseManager;

	@Autowired
	private DBService dbService;


	@Autowired
	private SystemActionWrapper systemActionWrapper;

	public String addUser(HashMap<String, Object> userMap) {
		String loginId = (String) userMap.get("loginId");
		String sysLoginId = (String) userMap.get("sysLoginId");

		if (userCache.userExists(loginId)) {
			logger.error("user[" + loginId + "] is exsit !");

			Result result = new Result();
			result.setResultCode(EResultCode.DATABASE_FAIL);
			result.setResultMsg(loginId + " 已存在，请重新创建！");
			return JsonHelper.toJson(result);
		} else {
//			if (licenseManager.getOpenstackLicenseStatus()) {
				try {
					userMap.put("roleId", OPENSTACK_ROLE_DEFAULT);
					userMap.put("dataAuthDefault", USER_DATA_AUTH_DEFAULT);
					userMap.put("password", AESHelper.encryptString((String) userMap.get("password")));
					Result result = systemActionWrapper.doExcutionAction(sysLoginId, null, EAction.SYSTEM_CREATE_USER, userMap);
					logger.debug("create user result :" + JsonHelper.toJson(result));

					if (result.getResultCode().equals(EResultCode.SUCCESS)) {
						userCache.addUser(loginId);
					}
					return JsonHelper.toJson(result);
				} catch (Exception e) {
					logger.error("find action error !", e);
					Result result = new Result();
					result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
					result.setResultMsg("不能找到对应的实现类！");
					return JsonHelper.toJson(result);
				}
//			} else {
//				logger.error("delete user,don't have openstack auth !");
//				Result result = new Result();
//				result.setResultCode(EResultCode.VIRTUAL_FAIL);
//				result.setResultMsg("不具备Openstack权限！");
//				return JsonHelper.toJson(result);
//			}
		}
	}

	public String deleteUser(String op, String sysLoginId, String loginId, Integer userId, String regionName) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);
		paramMap.put("userId", userId);
		if (op != null) {
			paramMap.put("op", op);
		}

//		if (!userCache.userExists(loginId)) {
//			logger.error("user[" + loginId + "] dose't exsit !");
//
//			Result result = new Result();
//			result.setResultCode(EResultCode.DATABASE_FAIL);
//			result.setResultMsg("当前用户不存在，请联系管理员！");
//			return JsonHelper.toJson(result);
//		} else {
//			if (licenseManager.getOpenstackLicenseStatus()) {
				try {
					Result result = systemActionWrapper.doExcutionAction(sysLoginId, null, EAction.SYSTEM_DELETE_USER, paramMap);
					logger.debug("delete user result :" + JsonHelper.toJson(result));

					if (result.getResultCode().equals(EResultCode.SUCCESS)) {
						userCache.deleteUser(loginId);
					}
					return JsonHelper.toJson(result);
				} catch (Exception e) {
					logger.error("find action error !", e);
					Result result = new Result();
					result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
					result.setResultMsg("不能找到对应的实现类！");
					return JsonHelper.toJson(result);
				}
//			} else {
//				logger.error("delete user,don't have openstack auth !");
//				Result result = new Result();
//				result.setResultCode(EResultCode.VIRTUAL_FAIL);
//				result.setResultMsg("不具备Openstack权限！");
//				return JsonHelper.toJson(result);
//			}
//		}
	}

	@SuppressWarnings("unchecked")
	public String updateUserResourceRelation(HashMap<String, Object> paramMap) {
		try {
			List<HashMap<String,Object>> dataList = (List<HashMap<String, Object>>) paramMap.get("dataList");
			for (HashMap<String, Object> item : dataList) {
				item.put("loginId", paramMap.get("loginId"));
				String[] resourceIds = item.get("resourceIds").toString().split(",");
				item.put("resourceIds", resourceIds);
				
				dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_RESOURCE_RELATION, item);
				dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_RESOURCE_RELATION, item);
			}
			logger.debug("config user's resource success ");
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "SUCCESS");
			result.put("resultMsg", "用户的数据权限设置成功");
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("config user's resource error", e);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "DATABASE_FAIL");
			result.put("resultMsg", "用户的数据权限设置失败");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listUserResourceRelation(HashMap<String, Object> paramMap) {
		HashMap<String, Object> result = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_RESOURCE_RELATION, paramMap);
		logger.debug("query user's resource success ");
		
		return JsonHelper.toJson(result);
	}
	
	public String updateUserResourceGroupRelation(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_RESOURCE_GROUP_RELATION, paramMap);
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> groups = (List<HashMap<String, Object>>) paramMap.get("groups");
			if(groups != null && groups.size() > 0){
				dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_RESOURCE_GROUP_RELATION, paramMap);
			}
			logger.debug("config user's resource group success ");
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "SUCCESS");
			result.put("resultMsg", "用户的资源组权限设置成功");
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("config user's resource error", e);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "DATABASE_FAIL");
			result.put("resultMsg", "用户的资源组权限设置失败");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listUserResourceGroupRelation(HashMap<String, Object> paramMap) {
		HashMap<String, Object> result = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_RESOURCE_GROUP_RELATION, paramMap);
		logger.debug("query user's resource group success ");
		
		return JsonHelper.toJson(result);
	}
	
	public String updateUserDepartmentRelation(HashMap<String, Object> paramMap) {
		try {
			String departmentIdString = (String) paramMap.get("departmentIds");
			String[] departmentIds = departmentIdString.split(",");
			paramMap.put("departmentIds", departmentIds);
			
//			dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_DEPARTMENT_RELATION, paramMap);
			dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_DEPARTMENT_RELATION, paramMap);
			logger.debug("config user's department success ");
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "SUCCESS");
			result.put("resultMsg", "用户的组织机构权限设置成功");
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("config user's department error", e);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "DATABASE_FAIL");
			result.put("resultMsg", "用户的组织机构权限设置失败");
			return JsonHelper.toJson(result);
		}
	}
	
	public String deleteUserDepartmentRelation(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_DEPARTMENT_RELATION, paramMap);
			logger.debug("delete user's department success ");
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "SUCCESS");
			result.put("resultMsg", "用户的组织机构权限删除成功");
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("delete user's department error", e);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "DATABASE_FAIL");
			result.put("resultMsg", "用户的组织机构权限删除失败");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listUserDepartmentRelation(HashMap<String, Object> paramMap) {
		HashMap<String, Object> result = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_DEPARTMENT_RELATION, paramMap);
		logger.debug("query department's resource success ");
		
		return JsonHelper.toJson(result);
	}
	
	public List<HashMap<String, Object>> getMenuListByLoginId(String loginId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", "'" + loginId + "'");
		List<HashMap<String, Object>> menuList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USER_MENUS, paramMap);

		List<HashMap<String, Object>> resultList = new ArrayList<>();

		HashMap<Integer, Object> tempResultMap = new HashMap<>();

		// 遍历当前目录树，得到parentId对应的子节点list
		HashMap<Integer, List<HashMap<String, Object>>> tempSubMenuMap = new HashMap<>();
		for (int i = 0; i < menuList.size(); i++) {
			HashMap<String, Object> menuItem = menuList.get(i);

			Integer parentId = (Integer) menuItem.get("parentId");
			Integer id = (Integer) menuItem.get("id");

			String type = (String) menuItem.get("type");
			if (!type.equals("menu")) {
				continue;
			}

			if (parentId == -1) { // 一级菜单,直接放入结果中进行保存
				resultList.add(menuItem);
			} else {
				if (tempResultMap.containsKey(parentId)) {// 父节点已经存在结果map中，直接获取submenu对象进行处理
					HashMap<String, Object> parentMenuItem = (HashMap<String, Object>) tempResultMap.get(parentId);
					List<HashMap<String, Object>> subMenuList = null;
					if (parentMenuItem.containsKey(SUB_MENU_TAG)) {
						subMenuList = (List<HashMap<String, Object>>) parentMenuItem.get(SUB_MENU_TAG);

					} else {
						subMenuList = new ArrayList<>();
						parentMenuItem.put(SUB_MENU_TAG, subMenuList);
					}
					subMenuList.add(menuItem);
				} else {
					List<HashMap<String, Object>> subMenuList = null;
					if (tempSubMenuMap.containsKey(parentId)) {
						subMenuList = tempSubMenuMap.get(parentId);
					} else {
						subMenuList = new ArrayList<>();
						tempSubMenuMap.put(parentId, subMenuList);
					}
					subMenuList.add(menuItem);
				}
			}

			tempResultMap.put(id, menuItem);
		}

		for (Integer parentId : tempSubMenuMap.keySet()) {
			HashMap<String, Object> parentMenuItem = (HashMap<String, Object>) tempResultMap.get(parentId);

			List<HashMap<String, Object>> subMenuList = null;
			if (parentMenuItem.containsKey(SUB_MENU_TAG)) {
				subMenuList = (List<HashMap<String, Object>>) parentMenuItem.get(SUB_MENU_TAG);

			} else {
				subMenuList = new ArrayList<>();
				parentMenuItem.put(SUB_MENU_TAG, subMenuList);
			}
			subMenuList.addAll(tempSubMenuMap.get(parentId));
		}

		return resultList;
	}

	public Integer getSystemUserAdminRole(String loginId) {
		HashMap<String, Object> roleParamMap = new HashMap<>();
		roleParamMap.put("loginId", loginId);
		return (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
	}

	public String listUsers(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		String sessionLoginId = paramMap.getOrDefault("sessionLoginId", "").toString();
		paramMap.put("countSize", this.getSystemUserAdminRole(sessionLoginId));
		paramMap.put("sessionLoginId", sessionLoginId);

		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_USERS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USERS, paramMap);
		}

		// dataTables需要将接收到的draw直接返回
		resultMap.put("draw", paramMap.get("draw"));

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUsers successful! the result is :" + result);
		return result;
	}

	public String listUserDetails(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserDetails successful! the result is :" + result);
		return result;
	}

	public String listUserDetailsByLoginId(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_DETAILS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserDetailsByLoginId successful! the result is :" + result);
		return result;
	}

	public String listCurrentUserDetails(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listCurrentUserDetails successful! the result is :" + result);
		return result;
	}

	public String listFilterUser(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listFilterUser successful! the result is :" + result);
		return result;
	}

	public String updateUser(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		logger.debug("update user param is : " + JsonHelper.toJson(paramMap));
		try {
			if (paramMap.containsKey("password") && paramMap.get("password") != null) {
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_USER_PASSWORD, paramMap);
				logger.debug("update user's password result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} else {
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_USER, paramMap);
				logger.debug("update user result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			}
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listUserRoles(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_ROLES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserRoles successful! the result is :" + result);
		return result;
	}

	public String createUserRoles(HashMap<String, Object> userRoleMap) {
		try {
			String loginId = userRoleMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CONFIG_USER_ROLE, userRoleMap);
			logger.debug("config user role result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listTreeMenus() {
		HashMap<String, Object> paramMap = new HashMap<>();
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_AUTH_SYSTEM_MENUS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listTreeMenus successful! the result is :" + result);
		return result;
	}

	public String listMenus() {
		HashMap<String, Object> paramMap = new HashMap<>();
		List<HashMap<String, Object>> menuList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_MENUS, paramMap);
		List<HashMap<String, Object>> resultList = parseMenuList(menuList);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("record", resultList);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listMenus successful! the result is :" + result);
		return result;
	}

	private List<HashMap<String, Object>> parseMenuList(List<HashMap<String, Object>> menuList) {
		List<HashMap<String, Object>> resultList = new ArrayList<>();

		HashMap<Integer, Object> tempResultMap = new HashMap<>();
try {
	// 遍历当前目录树，得到parentId对应的子节点list
			HashMap<Integer, List<HashMap<String, Object>>> tempSubMenuMap = new HashMap<>();
			for (int i = 0; i < menuList.size(); i++) {
				HashMap<String, Object> menuItem = menuList.get(i);

				Integer parentId = (Integer) menuItem.get("parentId");
				Integer id = (Integer) menuItem.get("id");

				String type = (String) menuItem.get("type");
				if (!type.equals("menu")) {
					continue;
				}

				if (parentId == -1) { // 一级菜单,直接放入结果中进行保存
					resultList.add(menuItem);
				} else {
					if (tempResultMap.containsKey(parentId)) {// 父节点已经存在结果map中，直接获取submenu对象进行处理
						HashMap<String, Object> parentMenuItem = (HashMap<String, Object>) tempResultMap.get(parentId);
						List<HashMap<String, Object>> subMenuList = null;
						if (parentMenuItem.containsKey(SUB_MENU_TAG)) {
							subMenuList = (List<HashMap<String, Object>>) parentMenuItem.get(SUB_MENU_TAG);

						} else {
							subMenuList = new ArrayList<>();
							parentMenuItem.put(SUB_MENU_TAG, subMenuList);
						}
						subMenuList.add(menuItem);
					} else {
						List<HashMap<String, Object>> subMenuList = null;
						if (tempSubMenuMap.containsKey(parentId)) {
							subMenuList = tempSubMenuMap.get(parentId);
						} else {
							subMenuList = new ArrayList<>();
							tempSubMenuMap.put(parentId, subMenuList);
						}
						subMenuList.add(menuItem);
					}
				}

				tempResultMap.put(id, menuItem);
			}

			for (Integer parentId : tempSubMenuMap.keySet()) {
				HashMap<String, Object> parentMenuItem = (HashMap<String, Object>) tempResultMap.get(parentId);

				List<HashMap<String, Object>> subMenuList = null;
				if (parentMenuItem.containsKey(SUB_MENU_TAG)) {
					subMenuList = (List<HashMap<String, Object>>) parentMenuItem.get(SUB_MENU_TAG);

				} else {
					subMenuList = new ArrayList<>();
					parentMenuItem.put(SUB_MENU_TAG, subMenuList);
				}
				subMenuList.addAll(tempSubMenuMap.get(parentId));
			}

} catch (Exception e) {
	e.printStackTrace();
}
		
		return resultList;
	}

	public String listRolesTreeMenus(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_ROLE_MENUS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listRolesMenus successful! the result is :" + result);
		return result;
	}

	public String listRolesMenus(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> menuList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_ROLE_MENUS, paramMap);

		List<HashMap<String, Object>> resultList = parseMenuList(menuList);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("record", resultList);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listRolesMenus successful! the result is :" + result);
		return result;
	}

	public String listRoles(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_ROLES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_ROLES, paramMap);
		}

		// dataTables需要将接收到的draw直接返回
		resultMap.put("draw", paramMap.get("draw"));

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listRoles successful! the result is :" + result);
		return result;
	}

	public String createRole(HashMap<String, Object> roleMap) {
		String name = roleMap.getOrDefault("name", "").toString();
		String loginId = roleMap.getOrDefault("loginId", "").toString();
		if (roleCache.roleExists(name)) {
			logger.error("role[" + name + "] is exsit !");

			Result result = new Result();
			result.setResultCode(EResultCode.DATABASE_FAIL);
			result.setResultMsg(name + " 已存在，请重新创建！");
			return JsonHelper.toJson(result);
		} else {
			try {
				roleMap.put("isDefault", 0);
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CREATE_ROLE, roleMap);
				// 角色创建成功，将新创建的角色添加到缓存中
				if (result.getResultCode().equals(EResultCode.SUCCESS)) {
					roleCache.addRole(name);
				}
				logger.debug("create role result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		}
	}

	public String updateRole(HashMap<String, Object> roleMap) {
		String name = roleMap.getOrDefault("name", "").toString();
		String oldName = (String) roleMap.get("oldName");
		String loginId = roleMap.getOrDefault("loginId", "").toString();
		if (!name.equals(oldName) && roleCache.roleExists(name)) {
			logger.error("role[" + name + "] is exsit !");

			Result result = new Result();
			result.setResultCode(EResultCode.DATABASE_FAIL);
			result.setResultMsg(name + " 已存在，请重新编辑！");
			return JsonHelper.toJson(result);
		} else {
			try {
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_ROLE, roleMap);
				// 角色修改成功，将旧角色从到缓存中移除，新角色添加到缓存中
				if (result.getResultCode().equals(EResultCode.SUCCESS)) {
					roleCache.deleteRole(oldName);
					roleCache.addRole(name);
				}
				logger.debug("update role result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		}
	}

	public String deleteRole(HashMap<String, Object> roleMap) {
		String roleName = roleMap.getOrDefault("roleName", "").toString();
		String loginId = roleMap.getOrDefault("loginId", "").toString();
		try {
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_ROLE, roleMap);
			// 角色删除成功，将该角色从到缓存中移除
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				roleCache.deleteRole(roleName);
			}
			logger.debug("delete role result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String createRoleMenus(HashMap<String, Object> roleMenuMap) {
		String loginId = roleMenuMap.getOrDefault("loginId", "").toString();
		try {
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CONFIG_ROLE_AUTH, roleMenuMap);
			logger.debug("config role auth result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listUserRelations(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_RELATION, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserRelations successful! the result is :" + result);
		return result;
	}

	public String createUserRelations(HashMap<String, Object> userRelationsMap) {
		userRelationDao.insert(userRelationsMap);
		userCache.reloadUserRelationMap();
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createUserRelations successful! ");
		return result;
	}

	public String listUserTreeMenus(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> menuList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USER_MENUS, paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("record", menuList);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserTreeMenus successful! the result is :" + result);
		return result;
	}

	public String listUserMenus(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> menuList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USER_MENUS, paramMap);
		List<HashMap<String, Object>> resultList = parseMenuList(menuList);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("record", resultList);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserMenus successful! the result is :" + result);
		return result;
	}

	public String listUserData(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_DATA, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserData successful! ");
		return result;
	}

	public String updateUserData(HashMap<String, Object> paramMap) {
		try {
			String currentLoginId = paramMap.getOrDefault("currentLoginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(currentLoginId, null, EAction.SYSTEM_CONFIG_USER_DATA_AUTH, paramMap);
			logger.debug("update role result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listUserLogs(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_USER_LOGS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_LOGS, paramMap);
		}

		List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				HashMap<String, Object> detailsMap = resultList.get(i);
				byte[] responseByteArray = (byte[]) detailsMap.get("response");
				String responsebody = new String(responseByteArray);

				detailsMap.put("responseJson", JsonHelper.toJson(responsebody));
			}
		}

		resultMap.put("draw", paramMap.get("draw"));

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserLogs successful! the result is :" + result);
		return result;
	}

	public String listUserResourcePools(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_GROUP_RESOURCEPOOLS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserResourcePools successful! the result is :" + result);
		return result;
	}

	public String listUserDataCenters(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_GROUP_DATACENTERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserDataCenters successful! the result is :" + result);
		return result;
	}

	public String deleteUserDataCenters(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String type = paramMap.getOrDefault("type", "").toString();
		String identity = paramMap.getOrDefault("identity", "").toString();
		if (type.equals("user")) {
			try {
				paramMap.put("loginId", identity);
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_USER_MACHINE_ROOM, paramMap);
				logger.debug("delete user machine room result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		} else {
			try {
				paramMap.put("groupId", identity);
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_DEPARTMENT_GROUP_MACHINE_ROOM, paramMap);
				logger.debug("delete department group machine room result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		}
	}

	public String deleteUserResourcePools(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String type = paramMap.getOrDefault("type", "").toString();
		String identity = paramMap.getOrDefault("identity", "").toString();
		if (type.equals("user")) {
			try {
				paramMap.put("loginId", identity);
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_USER_RESOURCE_POOL, paramMap);
				logger.debug("delete user resource pool result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		} else {
			try {
				paramMap.put("groupId", identity);
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_DEPARTMENT_GROUP_RESOURCE_POOL, paramMap);
				logger.debug("delete department group resource pool result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		}
	}

	public String createUserResourcePools(HashMap<String, Object> paramMap, SysLogObj logObj) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String type = paramMap.getOrDefault("type", "").toString();
		if (type.equals("user")) {
			try {
				List<String> poolIds = (List<String>) paramMap.get("poolIds");
				paramMap.put("poolIds", poolIds);
				paramMap.put("loginId", paramMap.get("identity"));
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_ADD_USER_RESOURCE_POOL, paramMap);
				logger.debug("add user resource pool result :" + JsonHelper.toJson(result));
				logObj.setOPER_TYPE(ESysLogOperType.SYSTEM_OPERATION.name());
				logObj.setOPER_SUB_TYPE("CREATE_USER_RESOURCE_POOLS");
				logObj.setOPER("修改用户资源权限");
				logObj.setOPER_OBJ(String.valueOf(paramMap.get("identity")));
				logObj.setOPER_RESULT("0");
				WebSysLogTool.sendLog(logObj);

				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		} else {
			try {
				List<String> poolIds = (List<String>) paramMap.get("poolIds");
				paramMap.put("poolIds", poolIds);
				paramMap.put("groupId", paramMap.get("identity"));
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_ADD_DEPARTMENT_GROUP_RESOURCE_POOL, paramMap);
				logger.debug("add department group resource pool result :" + JsonHelper.toJson(result));
				logObj.setOPER_TYPE(ESysLogOperType.SYSTEM_OPERATION.name());
				logObj.setOPER_SUB_TYPE("CREATE_USER_RESOURCE_POOLS");
				logObj.setOPER("修改用户资源权限");
				logObj.setOPER_OBJ(String.valueOf(paramMap.get("identity")));
				logObj.setOPER_RESULT("0");
				WebSysLogTool.sendLog(logObj);

				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		}
	}

	public String createUserDataCenters(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String type = paramMap.getOrDefault("type", "").toString();
		if (type.equals("user")) {
			try {
				List<String> dataCenterIds = (List<String>) paramMap.get("dataCenterIds");
				paramMap.put("dataCenterIds", dataCenterIds);
				paramMap.put("loginId", paramMap.get("identity"));
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_ADD_USER_MACHINE_ROOM, paramMap);
				logger.debug("add user machine room result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		} else {
			try {
				List<String> dataCenterIds = (List<String>) paramMap.get("dataCenterIds");
				paramMap.put("dataCenterIds", dataCenterIds);
				paramMap.put("groupId", paramMap.get("identity"));
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_ADD_DEPARTMENT_GROUP_MACHINE_ROOM, paramMap);
				logger.debug("add department group machine room result :" + JsonHelper.toJson(result));
				return JsonHelper.toJson(result);
			} catch (Exception e) {
				logger.error("find action error !", e);
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到对应的实现类！");
				return JsonHelper.toJson(result);
			}
		}
	}

	public String listUserAvailabilityZones(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_AVAILABILITY_ZONES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUserAvailabilityZones successful! the result is :" + result);
		return result;
	}

	public String createUserAvailabilityZone(HashMap<String, Object> paramMap) {
		try {
			String userId = paramMap.getOrDefault("userId", "").toString();
			dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_AVAILABILITY_ZONE, paramMap);
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> availabilityZonesMap = (List<HashMap<String, Object>>) paramMap.get("availabilityZones");
			for (HashMap<String, Object> availabilityZoneMap : availabilityZonesMap) {
				availabilityZoneMap.put("userId", userId);
				dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_AVAILABILITY_ZONE, availabilityZoneMap);
			}
		} catch (Exception e) {
			logger.error("createUserAvailabilityZone error:" + e.getMessage());
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createUserAvailabilityZone successful! ");
		return result;
	}

}
