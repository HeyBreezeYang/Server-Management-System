package com.system.started.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.system.started.action.wrapper.SystemActionWrapper;
import com.system.started.cache.impl.DepartmentGroupCache;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.license.LicenseManager;
import com.system.started.shiro.token.UserInfo;
import com.system.started.shiro.token.manager.TokenManager;
import com.vlandc.oss.common.AESHelper;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.kernel.sync.ISyncService;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class SystemService {

	private final static Logger logger = LoggerFactory.getLogger(SystemService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private UserService userService;

	@Autowired
	private LicenseManager licenseManager;
	
	@Autowired
	private ISyncService serverSyncService;

	@Autowired
	private SystemActionWrapper systemActionWrapper;

	@Autowired
	private DepartmentGroupCache departmentGroupCache;
	
	@Autowired
	private TokenManager tokenManager;

	public HashMap<String, Object> submitLogin(String loginId, String password, Boolean rememberMe) {
		if (rememberMe == null) {
			rememberMe = false;
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginId(loginId);
		userInfo.setPassword(password);
		HashMap<String, Object> resultMap = new HashMap<>();
		userInfo = tokenManager.login(userInfo, rememberMe);

		if (null != userInfo.getToken()) {
			resultMap.put("responseCode", "success");
			resultMap.put("responseMsg", "登录成功");
			resultMap.put("token", userInfo.getToken());
			HashMap<String, Object> parameter = new HashMap<>(4);
			parameter.put("loginId", userInfo.getLoginId());
			parameter.put("op", "local");
			parameter.put("status", 1); //1 用户启用 ； 0 用户禁用
			String indexHtmlUrl = (String) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_INDEX_MENU, parameter);
			resultMap.put("indexHtmlUrl", indexHtmlUrl);
		} else {
			logger.error("token is null !");
			resultMap.put("responseCode", "error");
			resultMap.put("responseMsg", "登录时未获取到 Token ！");
		}
		return resultMap;
	}

	public String logout(String token) {
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			if (tokenManager.isLogin(token)) {
				tokenManager.logout(token);
			}
			resultMap.put("responseCode", "success");
		} catch (Exception e) {
			logger.error("logout error !", e);
			resultMap.put("responseCode", "error");
			resultMap.put("responseMsg", "注销失败！");
		}
		return JsonHelper.toJson(resultMap);
	}

	public String getUserInfo(String token) {
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			UserInfo userInfo = tokenManager.getUserInfo(token);
			if (null == userInfo) {
				resultMap.put("responseCode", "error");
				resultMap.put("responseMsg", "未找到用户基本信息 ！");
			} else {
				resultMap.put("responseCode", "success");
				resultMap.put("responseMsg", userInfo);
			}
		} catch (Exception e) {
			logger.error("find user info error !", e);
			resultMap.put("responseCode", "error");
			resultMap.put("responseMsg", "未找到用户基本信息 ！");
		}
		return JsonHelper.toJson(resultMap);
	}

	public String listUploadFiles(String filePath) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("filePath", filePath.replace("\\", "-"));
		logger.debug("the listUploadFiles paramMap is :" + JsonHelper.toJson(paramMap));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_UPLOAD_FILES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listUploadFiles successful! the result is :" + result);
		return result;
	}

	public String listTimerTask(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_TIMER_TASK_DELAY, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_TIMER_TASK_DELAY, paramMap);
		}

		// dataTables需要将接收到的draw直接返回
		resultMap.put("draw", paramMap.get("draw"));

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listTimerTask successful! the result is :" + result);
		return result;
	}

	public String getLicenseProItem(String proItemKey) {
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		resultMap.put("responseMsg", "获取license系统类型成功！");
		resultMap.put(proItemKey, licenseManager.getLicenseProItem(proItemKey));
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query getLicenseSystemType successful! the result is :" + result);
		return result;
	}

	public String listDepartments(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		HashMap<String, Object> resultMap = null;
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_DEPARTMENTS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_DEPARTMENTS, paramMap);
		}

		// dataTables需要将接收到的draw直接返回
		resultMap.put("draw", paramMap.get("draw"));

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDepartments successful! the result is :" + result);
		return result;
	}

	public String listDepartmentTree(HashMap<String, Object> paramMap) {
		boolean hasUser = (boolean) paramMap.get("hasUser");
		String loginId = (String) paramMap.get("loginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));
		List<HashMap<String, Object>> departments = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_DEPARTMENTS, paramMap);
		List<HashMap<String, Object>> resultMap = buildDepartmentTree(departments, 0, hasUser);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDepartmentTree successful! the result is :" + result);
		return result;
	}

	private List<HashMap<String, Object>> buildDepartmentTree(List<HashMap<String, Object>> departments, int parentId, boolean hasUser) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> item : departments) {
	          int id = Integer.parseInt(item.get("id").toString());
	          int refGroupId = Integer.parseInt(item.get("refGroupId").toString());
	          if (parentId == refGroupId) {
	              List<HashMap<String, Object>> menuLists = buildDepartmentTree(departments, id, hasUser);
	              item.put("title", item.get("name"));
	              item.put("expanded", true);
	              
	              if(hasUser){
	            	  HashMap<String, Object> paramMap = new HashMap<String, Object>();
			          paramMap.put("departmentId", id);
			          List<HashMap<String, Object>> userList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_USERS, paramMap);
			          for (HashMap<String, Object> userItem : userList) {
			        	  String userName = (userItem.get("name") == null || ((String)userItem.get("name")).equals("")) ? (String)userItem.get("loginId") : (String)userItem.get("name");
			        	  userItem.put("title", userName);
			        	  userItem.put("expanded", true);
			        	  userItem.put("objType", "user");
			          }
			          menuLists.addAll(userList);
	              }
		          
	              item.put("children", menuLists);
	              list.add(item);
	          }
	      }
		return list;
	}

	public String createDepartment(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CREATE_DEPARTMENT, paramMap);
			logger.debug("create department result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	@SuppressWarnings("unchecked")
	public String updateDepartmentResourceRelation(HashMap<String, Object> paramMap) {
		try {
			List<HashMap<String,Object>> dataList = (List<HashMap<String, Object>>) paramMap.get("dataList");
			for (HashMap<String, Object> item : dataList) {
				item.put("departmentId", paramMap.get("departmentId"));
				String[] resourceIds = item.get("resourceIds").toString().split(",");
				item.put("resourceIds", resourceIds);

				dbService.delete(DBServiceConst.DELETE_SYSTEM_DEPARTMENT_RESOURCE_RELATION, item);
				dbService.insert(DBServiceConst.INSERT_SYSTEM_DEPARTMENT_RESOURCE_RELATION, item);
			}
			logger.debug("config department's resource success ");

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "SUCCESS");
			result.put("resultMsg", "组织机构的数据权限设置成功");
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("config department's resource error", e);

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "DATABASE_FAIL");
			result.put("resultMsg", "组织机构的数据权限设置失败");
			return JsonHelper.toJson(result);
		}
	}

	public String listDepartmentResourceRelation(HashMap<String, Object> paramMap) {
		HashMap<String, Object> result = dbService.select(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_RESOURCE_RELATION, paramMap);
		logger.debug("query department's resource success ");

		return JsonHelper.toJson(result);
	}

	public String updateDepartmentResourceGroupRelation(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_SYSTEM_DEPARTMENT_RESOURCE_GROUP_RELATION, paramMap);
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> groups = (List<HashMap<String, Object>>)paramMap.get("groups");
			if(groups != null && groups.size()>0){
				dbService.insert(DBServiceConst.INSERT_SYSTEM_DEPARTMENT_RESOURCE_GROUP_RELATION, paramMap);
			}
			logger.debug("config department's resource group success ");

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "SUCCESS");
			result.put("resultMsg", "组织机构的资源组权限设置成功");
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("config department's resource error", e);

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("resultCode", "DATABASE_FAIL");
			result.put("resultMsg", "组织机构的资源组权限设置失败");
			return JsonHelper.toJson(result);
		}
	}

	public String listDepartmentResourceGroupRelation(HashMap<String, Object> paramMap) {
		HashMap<String, Object> result = dbService.select(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_RESOURCE_GROUP_RELATION, paramMap);
		logger.debug("query department's resource group success ");

		return JsonHelper.toJson(result);
	}

	public String updateDepartment(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_DEPARTMENT, paramMap);
			logger.debug("create department result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteDepartment(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_DEPARTMENT, paramMap);
			logger.debug("create department result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String createDepartmentGroup(HashMap<String, Object> paramMap) {
		String name = (String) paramMap.get("name");
		if (departmentGroupCache.departmentGroupExists(name)) {
			logger.error("department group [" + name + "] is exsit !");
			Result result = new Result();
			result.setResultCode(EResultCode.DATABASE_FAIL);
			result.setResultMsg(name + " 已存在，请重新创建！");
			return JsonHelper.toJson(result);
		} else {
			try {
				String loginId = paramMap.getOrDefault("loginId", "").toString();
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CREATE_DEPARTMENT_GROUP, paramMap);
				// 组织架构创建成功，将新创建的组织架构添加到缓存中
				if (result.getResultCode().equals(EResultCode.SUCCESS)) {
					departmentGroupCache.addDepartmentGroup(name);
				}
				logger.debug("create department group result :" + JsonHelper.toJson(result));
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

	public String listDepartmentGroups(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_GROUPS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_GROUPS, paramMap);
		}

		resultMap.put("draw", paramMap.get("draw"));

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDepartmentGroups successful! the result is :" + result);
		return result;
	}

	public String createUserDepartmentGroups(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CONFIG_USER_PROJECT, paramMap);
			logger.debug("config user department group result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listUserDepartmentGroups(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_DEPARTMENT_GROUPS, paramMap);
		String result = JsonHelper.toJson(resultMap);

		logger.debug("query listUserDepartmentGroups successful! the result is :" + result);
		return result;
	}

	public String listCurrentUserDepartmentGroups(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_DEPARTMENT_GROUPS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listCurrentUserDepartmentGroups successful! the result is :" + result);
		return result;
	}

	public String updateDepartmentGroup(HashMap<String, Object> paramMap) {
		String name = (String) paramMap.get("name");
		String oldName = (String) paramMap.get("oldName");
		if (!name.equals(oldName) && departmentGroupCache.departmentGroupExists(name)) {
			logger.error("department group [" + name + "] is exsit !");

			Result result = new Result();
			result.setResultCode(EResultCode.DATABASE_FAIL);
			result.setResultMsg(name + " 已存在，请重新编辑！");
			return JsonHelper.toJson(result);
		} else {
			try {
				String loginId = paramMap.getOrDefault("loginId", "").toString();
				Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_DEPARTMENT_GROUP, paramMap);
				// 组织架构修改成功，将旧组织架构从到缓存中移除，新组织架构添加到缓存中
				if (result.getResultCode().equals(EResultCode.SUCCESS)) {
					departmentGroupCache.deleteDepartmentGroup(oldName);
					departmentGroupCache.addDepartmentGroup(name);
				}
				logger.debug("update department group result :" + JsonHelper.toJson(result));
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

	public String deleteDepartmentGroup(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_DEPARTMENT_GROUP, paramMap);
			// 组织架构删除成功，将该组织架构从到缓存中移除
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				String name = paramMap.getOrDefault("name", "").toString();
				departmentGroupCache.deleteDepartmentGroup(name);
			}
			logger.debug("delete department group result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listEngines(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_ENGINES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_ENGINES, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listEngines successful! the result is :" + result);
		return result;
	}

	public String createEngine(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		try {
			if (paramMap.containsKey("password") && !((String) paramMap.get("password")).equals("")) {
				String newPassword = AESHelper.encryptString((String) paramMap.get("password"));
				paramMap.put("password", newPassword);
			}
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CREATE_ENGINE, paramMap);
			logger.debug("create engine result :" + JsonHelper.toJson(result));

			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				String engineType = (String) paramMap.get("type");
				if (engineType.equals("SALTSTACK") || engineType.equals("ZABBIX") || engineType.equals("TPMFOSD")) {
					String host = (String) paramMap.get("host");
					String uuid = UUID.randomUUID().toString();

					StringBuffer resultBuffer = new StringBuffer();
					resultBuffer.append(host);
					resultBuffer.append("_");
					resultBuffer.append(uuid.substring(0, 8));

					paramMap.put("region_name", resultBuffer.toString());

					List<HashMap<String, Object>> resultListMap = result.getResultObj();
					if (resultListMap.size() > 0) {
						paramMap.putAll(resultListMap.get(0));
					}

					result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CREATE_ENGINE_REGION, paramMap);
					logger.debug("create engine region result :" + JsonHelper.toJson(result));
				}
			}
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateEngine(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			if (paramMap.containsKey("password")) {
				String newPassword = AESHelper.encryptString((String) paramMap.get("password"));
				paramMap.put("password", newPassword);
			}

			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_ENGINE, paramMap);
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				try {
					serverSyncService.reInitManager();
				} catch (Exception e) {
					logger.error("reInitManager error :" + e.getMessage());
				}
			}
			logger.debug("update engine result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteEngine(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_ENGINE, paramMap);
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				try {
					serverSyncService.reInitManager();
				} catch (Exception e) {
					logger.error("reInitManager error :" + e.getMessage());
				}
			}
			logger.debug("delete engine result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listEngineRegions(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_ENGINE_REGIONS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_ENGINE_REGIONS, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listEngines successful! the result is :" + result);
		return result;
	}

	public String createEngineRegion(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			if (paramMap.containsKey("password") && paramMap.get("password") != null && !((String) paramMap.get("password")).equals("")) {
				String newPassword = AESHelper.encryptString((String) paramMap.get("password"));
				paramMap.put("password", newPassword);
			}
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CREATE_ENGINE_REGION, paramMap);
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				try {
					serverSyncService.reInitManager();
				} catch (Exception e) {
					logger.error("reInitManager error :" + e.getMessage());
				}
			}
			logger.debug("create engine region result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateEngineRegion(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			if (paramMap.containsKey("password") && paramMap.get("password") != null && !((String) paramMap.get("password")).equals("")) {
				String newPassword = AESHelper.encryptString((String) paramMap.get("password"));
				paramMap.put("password", newPassword);
			}
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_ENGINE_REGION, paramMap);
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				try {
					serverSyncService.reInitManager();
				} catch (Exception e) {
					logger.error("reInitManager error :" + e.getMessage());
				}
			}
			logger.debug("update engine region result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteEngineRegion(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_ENGINE_REGION, paramMap);
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				try {
					serverSyncService.reInitManager();
				} catch (Exception e) {
					logger.error("reInitManager error :" + e.getMessage());
				}
			}
			logger.debug("delete engine region result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listLifecycleAction(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_LIFECYCLES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_LIFECYCLES, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listLifecycles successful! the result is :" + result);
		return result;
	}

	public String createLifecycleAction(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> lifecycleActionList = dbService.directSelect(DBServiceConst.SELECT_RESOURCE_LIFECYCLES, paramMap);
		if (lifecycleActionList.size() > 0) {
			Result result = new Result();
			result.setResultCode(EResultCode.DATABASE_FAIL);

			String actionValue = (String) paramMap.get("action");
			if (actionValue.equals("ALERT")) {
				actionValue = "提醒";
			} else if (actionValue.equals("SHUTDOWN")) {
				actionValue = "关机";
			} else if (actionValue.equals("DELETE")) {
				actionValue = "删除";
			}
			result.setResultMsg("生命周期 " + actionValue + " 流程已经存在，不能继续创建");
			return JsonHelper.toJson(result);
		}

		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_CREATE_LIFECYCLE_ACTION, paramMap);

			logger.debug("create lifecycle action result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateLifecycleAction(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_UPDATE_LIFECYCLE_ACTION, paramMap);

			logger.debug("update lifecycle action result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteLifecycleAction(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = systemActionWrapper.doExcutionAction(loginId, null, EAction.SYSTEM_DELETE_LIFECYCLE_ACTION, paramMap);

			logger.debug("delete lifecycle action result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}

	}

	public String listAppScriptFiles(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_APP_SCRIPT_FILES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAppScriptFiles successful! the result is :" + result);
		return result;
	}

	public String getCurrentLicenseDetail() {
		HashMap<String, Object> licenProperties = licenseManager.getLicenseProperties();
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		resultMap.put("responseMsg", "查询当前license成功！");
		resultMap.put("record", licenProperties);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("select getCurrentLicenseDetail successful! the result is :" + result);
		return result;
	}

	public String loadCurrentLicenseFile() {
		licenseManager.loadLicenseFiles();
		HashMap<String, Object> licenProperties = licenseManager.getLicenseProperties();
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		resultMap.put("responseMsg", "重新加载license成功！");
		resultMap.put("record", licenProperties);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update loadCurrentLicenseFile successful! the result is :" + result);
		return result;
	}

	public String createOssSystemFieldTemplate(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		dbService.insert(DBServiceConst.INSERT_OSS_SYSTEM_FIELD_TEMPLATE, paramMap);
		logger.debug("createOssSystemFieldTemplate successful! ");
		resultMap.put("messageStatus", "END");
		if(paramMap.containsKey("id")){
			resultMap.put("id",paramMap.get("id"));
		}
		return JsonHelper.toJson(resultMap);
	}

	public String deleteOssSystemFieldTemplate(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		dbService.delete(DBServiceConst.DELETE_OSS_SYSTEM_FIELD_TEMPLATE, paramMap);
		logger.debug("deleteOssSystemFieldTemplate successful! ");
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}

	public String updateOssSystemFieldTemplate(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		dbService.update(DBServiceConst.UPDATE_OSS_SYSTEM_FIELD_TEMPLATE, paramMap);
		logger.debug("updateOssSystemFieldTemplate successful! ");
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}

	public String listOssSystemFieldTemplate(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_OSS_SYSTEM_FIELD_TEMPLATE, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_OSS_SYSTEM_FIELD_TEMPLATE, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		return result;
	}
	
	public String listResourceTypeOssSystemFieldTemplate(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_TYPE_OSS_SYSTEM_FIELD_TEMPLATE, paramMap);
		String result = JsonHelper.toJson(resultMap);
		return result;
	}
	
	public String listOssSystemFieldTemplateResourceType(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OSS_SYSTEM_FIELD_TEMPLATE_RESOURCE_TYPE, paramMap);
		String result = JsonHelper.toJson(resultMap);
		return result;
	}

	public String createRnFieldTemplateRelation(HashMap<String, Object> paramMap){
		String resourceTypes = (String) paramMap.get("resourceTypes");
		String templateIds = (String) paramMap.get("templateIds");
		
		if(resourceTypes != null){
			List<String> tempResourceTypes = new ArrayList<String>();
			for (String resourceType : resourceTypes.split(",")) {
				StringBuffer resultBuffer = new StringBuffer();
				resultBuffer.append("'");
				resultBuffer.append(resourceType);
				resultBuffer.append("'");
				
				tempResourceTypes.add(resultBuffer.toString());
			}
			paramMap.put("resourceTypes", StringUtils.join(tempResourceTypes, ","));
		}
		dbService.delete(DBServiceConst.DELETE_RN_FIELD_TEMPLATE_RELATION, paramMap);
		dbService.delete(DBServiceConst.DELETE_RN_FIELD_TEMPLATE_RELATION_INSTANCE, paramMap);
		
		if(templateIds != null && resourceTypes != null){
			for (String templateId : templateIds.split(",")) {
				for (String resourceType : resourceTypes.split(",")) {
					paramMap.put("templateId", templateId);
					paramMap.put("resourceType", resourceType);
					dbService.insert(DBServiceConst.INSERT_RN_FIELD_TEMPLATE_RELATION, paramMap);
				}
			}
		}
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		logger.debug("createRnFieldTemplateRelation successful! ");
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
}
