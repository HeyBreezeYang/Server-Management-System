package com.system.started.rest.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.cache.impl.UserCache;
import com.system.started.rest.request.DepartmentResourceGroupUpdateBean;
import com.system.started.rest.request.DepartmentResourceUpdateBean;
import com.system.started.rest.request.FieldTemplateCreateBean;
import com.system.started.rest.request.FieldTemplateRelationCreateBean;
import com.system.started.rest.request.FieldTemplateUpdateBean;
import com.system.started.rest.request.RoleCreateBean;
import com.system.started.rest.request.SystemUserLoginBean;
import com.system.started.rest.request.UserResourceGroupUpdateBean;
import com.system.started.rest.request.UserResourceUpdateBean;
import com.system.started.service.SystemService;
import com.system.started.service.UserService;
import com.system.started.syslog.SysLogObj;
import com.system.started.syslog.WebSysLogTool;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags= {"系统管理"})
@Controller
@RequestMapping(value = "/system")
public class SystemController extends AbstractController {

	private final static Logger logger = LoggerFactory.getLogger(SystemController.class);

	@Value("${oss.apigate.config.openstack-role-default}")
	String OPENSTACK_ROLE_DEFAULT;

	@Autowired
	private UserService userService;

	@Autowired
	private UserCache userCache;

	@Autowired
	private SystemService systemService;


	/**
	 * 登录
	 *
	 * @param loginId    用户名
	 * @param password   密码
	 * @param rememberMe 记住密码
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "登录", httpMethod = "POST", notes = "submitLogin", response = String.class)
	public String submitLogin(@RequestBody SystemUserLoginBean userLoginBean) {
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			resultMap = this.systemService.submitLogin(userLoginBean.getLoginId(), userLoginBean.getPassword(), userLoginBean.getRememberMe());
			if ("success".equals(resultMap.getOrDefault("responseCode", "error").toString())) {
				resultMap.put("indexHtmlUrl", httpRequest.getContextPath() + "/#" + resultMap.get("indexHtmlUrl").toString());
			}
		} catch (Exception e) {
			logger.error("do login error!", e);
			resultMap.put("responseCode", "error");
			resultMap.put("responseMsg", "登录失败，服务器异常，请联系管理员！");
			resultMap.put("errorReason", e.getMessage());
		}
		return JsonHelper.toJson(resultMap);
	}

	/**
	 * 注销
	 *
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "注销", httpMethod = "POST", notes = "submitLogin", response = String.class)
	public String logout() {
		return this.systemService.logout(getCurrentLoginToken());
	}

	/**
	 * 获取用户信息
	 *
	 * @return
	 */
	@RequestMapping(value = "/userinfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "获取用户信息", httpMethod = "GET", notes = "getUserInfo", response = String.class)
	public String getUserInfo() {
		return this.systemService.getUserInfo(getCurrentLoginToken());
	}

	@RequestMapping(value = "/uploadfiles", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询已上传的文件", httpMethod = "GET", notes = "listUploadFiles", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "filePath", value = "文件路径，<br/>例子：20180507/5d1d9736-fed3-4c2b-8a0e-3809946e93a0.yaml", required = true, dataType = "String", paramType = "query")
	})
	public String listUploadFiles(@RequestParam String filePath) {
		return this.systemService.listUploadFiles(filePath);
	}

	@RequestMapping(value = "/timertasks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询定时任务", httpMethod = "GET", notes = "listTimerTask", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listTimerTask(
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.systemService.listTimerTask(paramMap);
	}

	@RequestMapping(value = "/license/{proItemKey}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询license系统类型", httpMethod = "GET", notes = "getLicenseProItem", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "proItemKey", value = "license系统类型，<br/>例子：computeNodeNum / virtualNodeNum / operationNodeNum", required = true, dataType = "String", paramType = "path")
	})
	public String getLicenseProItem(@PathVariable String proItemKey) {
		return this.systemService.getLicenseProItem(proItemKey);
	}

	@RequestMapping(value = "/department/tree", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询组织机构树", httpMethod = "GET", notes = "listDepartmentTree", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hasUser", value = "是否包含用户，<br/>例子：true/false", required = false, dataType = "boolean", paramType = "query")
	})
	public String listDepartmentTree(@RequestParam(required = false, name = "hasUser") Boolean hasUser) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseCurrentLoginIds(paramMap);
		if(hasUser != null){
			paramMap.put("hasUser", hasUser);
		}else{
			paramMap.put("hasUser", true);
		}
		return this.systemService.listDepartmentTree(paramMap);
	}

	@RequestMapping(value = "/department/resource/relation/{id}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置组织机构与资源的关系", httpMethod = "POST", notes = "updateDepartmentResourceRelation", response = String.class)
	public String updateDepartmentResourceRelation(
			@PathVariable @ApiParam(name="id", value="部门ID，<br/>例子：77", required=true) Integer id,
			@RequestBody @ApiParam(name="departmentResource", value="设置组织机构与资源关系对象", required=true) DepartmentResourceUpdateBean departmentResource) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(departmentResource));
		paramMap.put("departmentId", id);
		return this.systemService.updateDepartmentResourceRelation(paramMap);
	}

	@RequestMapping(value = "/department/resource/relation/{id}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询组织机构与资源的关系", httpMethod = "GET", notes = "listDepartmentResourceGroupRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "组织机构ID，<br/>例子：77", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "resourceType", value = "资源类型，<br/>例子：VIR_VOLUME/VIR_NETWORK/VIR_INSTANCE/VIR_IMAGE/<br>VIR_FLAVOR/VIRTUAL/OPERATION/DEPLOY", required = false, dataType = "string", paramType = "query")
	})
	public String listDepartmentResourceRelation(@PathVariable Integer id, @RequestParam(required = false, value = "resourceType") String resourceType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("departmentId", id);
		paramMap.put("resourceType", resourceType);

		return this.systemService.listDepartmentResourceRelation(paramMap);
	}

	@RequestMapping(value = "/department/resourceGroup/relation/{id}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置组织机构与资源组的关系", httpMethod = "POST", notes = "updateDepartmentResourceGroupRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "组织机构ID，<br/>例子：77", required = true, dataType = "Integer", paramType = "path")
	})
	public String updateDepartmentResourceGroupRelation(@PathVariable Integer id, @RequestBody DepartmentResourceGroupUpdateBean departmentResourceGroup) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(departmentResourceGroup));
		paramMap.put("departmentId", id);
		return this.systemService.updateDepartmentResourceGroupRelation(paramMap);
	}

	@RequestMapping(value = "/department/resourceGroup/relation/{id}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询组织机构与资源组的关系", httpMethod = "GET", notes = "listDepartmentResourceGroupRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "组织机构ID，<br/>例子：77", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "groupType", value = "资源组类型，<br/>例子：resourcePool/group/datacenter", required = true, dataType = "string", paramType = "query")
	})
	public String listDepartmentResourceGroupRelation(@PathVariable Integer id, @RequestParam(required = true, value = "groupType") String groupType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("departmentId", id);
		paramMap.put("groupType", groupType);

		return this.systemService.listDepartmentResourceGroupRelation(paramMap);
	}

	@RequestMapping(value = "/departmentGroup", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建组织架构", httpMethod = "POST", notes = "createDepartmentGroup", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"name\":\"信息科技部\",<br>"
					+ "\"remark\":\"信息科技部\"<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createDepartmentGroup(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.systemService.createDepartmentGroup(paramMap);
	}

	@RequestMapping(value = "/departmentGroups", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询组织架构", httpMethod = "GET", notes = "listDepartmentGroups", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "组织架构ID，<br/>例子：1", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "poolId", value = "资源池ID，<br/>例子：3", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "dataCenterId", value = "机房ID，<br/>例子：6", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listDepartmentGroups(
			@RequestParam(required = false, value = "id") Integer id,
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "dataCenterId") Integer dataCenterId,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (id != null) {
			paramMap.put("id", id);
		}
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if (dataCenterId != null) {
			paramMap.put("dataCenterId", dataCenterId);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		parseCurrentLoginIds(paramMap);
		return this.systemService.listDepartmentGroups(paramMap);
	}

	@RequestMapping(value = "/users/{userId}/departmentGroups", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建用户与组织架构关联", httpMethod = "POST", notes = "createUserDepartmentGroups", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：27", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "userUuid", value = "用户UUID，<br/>例子：c04e6929f1484f1c884650f4746f77f7", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br>"
					+ "\"projectAddList\":[<br>"
					+ "{<br>"
					+ "\"id\":15,<br>"
					+ "\"name\":\"系统管理中心\",<br>"
					+ "\"projectId\":\"44931aec982e46739cb3336b8511db0d\"<br>"
					+ "},<br>"
					+ "{<br>"
					+ "\"id\":17,<br>"
					+ "\"name\":\"运维部门\",<br>"
					+ "\"projectId\":\"422b6da1a6064ab8af939ee1fb66bc55\"<br>"
					+ "}<br>"
					+ "],<br>"
					+ "\"projectRemoveList\":[]<br>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createUserDepartmentGroups(
			@PathVariable Integer userId,
			@RequestParam String userUuid,
			@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("userId", userId);
		paramMap.put("userUuid", userUuid);
		paramMap.put("roleUuid", OPENSTACK_ROLE_DEFAULT);
		parseCurrentLoginIds(paramMap);
		return this.systemService.createUserDepartmentGroups(paramMap);
	}

	@RequestMapping(value = "/users/{userId}/departmentGroups", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户关联的组织架构", httpMethod = "GET", notes = "listUserDepartmentGroups", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path")
	})
	public String listUserDepartmentGroups(@PathVariable Integer userId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (userId != null) {
			paramMap.put("userId", userId);
		}
		return this.systemService.listUserDepartmentGroups(paramMap);
	}

	@RequestMapping(value = "/users/departmentGroups/current", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询当前用户关联的组织架构", httpMethod = "GET", notes = "listCurrentUserDepartmentGroups", response = String.class)
	public String listCurrentUserDepartmentGroups() {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseCurrentLoginIds(paramMap);
		return this.systemService.listCurrentUserDepartmentGroups(paramMap);
	}

	@RequestMapping(value = "/departmentGroup/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑组织架构", httpMethod = "PUT", notes = "updateDepartmentGroup", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "组织架构ID，<br/>例子：18", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"id\":\"18\",<br>"
					+ "\"name\":\"信息科技部1\",<br>"
					+ "\"oldName\":\"信息科技部\",<br>"
					+ "\"remark\":\"信息科技部\"<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body"),
	})
	public String updateDepartmentGroup(@PathVariable Integer id, @RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.systemService.updateDepartmentGroup(paramMap);
	}

	@RequestMapping(value = "/departmentGroup/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除组织架构", httpMethod = "DELETE", notes = "deleteDepartmentGroup", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "组织架构ID，<br/>例子：18", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "name", value = "组织架构名称，<br/>例子：信息科技部", required = true, dataType = "String", paramType = "query"),
	})
	public String deleteDepartmentGroup(
			@RequestParam String name,
			@PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", name);
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.systemService.deleteDepartmentGroup(paramMap);
	}

	@RequestMapping(value = "/user/resourcePools", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户关联的资源池", httpMethod = "GET", notes = "", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：admin", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "groupId", value = "组织架构ID，<br/>例子：15", required = false, dataType = "String", paramType = "query")
	})
	public String listUserResourcePools(
			@RequestParam(required = false, value = "loginId") String loginId,
			@RequestParam(required = false, value = "groupId") String groupId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (loginId != null) {
			paramMap.put("loginId", loginId);
		}
		if (groupId != null) {
			paramMap.put("groupId", groupId);
			paramMap.put("poolTypeList", true);
		}
		return this.userService.listUserResourcePools(paramMap);
	}

	@RequestMapping(value = "/user/dataCenters", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户关联的机房", httpMethod = "GET", notes = "listUserDataCenters", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：admin", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "groupId", value = "组织架构ID，<br/>例子：15", required = false, dataType = "String", paramType = "query")
	})
	public String listUserDataCenters(
			@RequestParam(required = false, value = "loginId") String loginId,
			@RequestParam(required = false, value = "groupId") String groupId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (loginId != null) {
			paramMap.put("loginId", loginId);
		}
		if (groupId != null) {
			paramMap.put("groupId", groupId);
		}
		return this.userService.listUserDataCenters(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/{type}/{identity}/dataCenters", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除用户关联的机房", httpMethod = "DELETE", notes = "deleteUserDataCenters", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "type", value = "类型，<br/>例子：user / group", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "identity", value = "ID标识，<br/>例子：如果 type 是 user，则 identity 为 1 ；<br>如果 type 是 group，则 identity 为 15", required = true, dataType = "String", paramType = "path")
	})
	public String deleteUserDataCenters(
			@PathVariable Integer dataCenterId,
			@PathVariable String type,
			@PathVariable String identity) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("dataCenterId", dataCenterId);
		paramMap.put("type", type);
		paramMap.put("identity", identity);
		parseCurrentLoginIds(paramMap);
		return this.userService.deleteUserDataCenters(paramMap);
	}

	@RequestMapping(value = "/{poolId}/{type}/{identity}/resourcePools", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除用户关联的资源池", httpMethod = "DELETE", notes = "deleteUserResourcePools", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "poolId", value = "资源池ID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "type", value = "类型，<br/>例子：user / group", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "identity", value = "ID标识，<br/>例子：如果 type 是 user，则 identity 为 27 ；<br>如果 type 是 group，则 identity 为 15", required = true, dataType = "String", paramType = "path")
	})
	public String deleteUserResourcePools(
			@PathVariable Integer poolId,
			@PathVariable String type,
			@PathVariable String identity) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolId", poolId);
		paramMap.put("type", type);
		paramMap.put("identity", identity);
		parseCurrentLoginIds(paramMap);
		return this.userService.deleteUserResourcePools(paramMap);
	}

	@RequestMapping(value = "/user/resourcePools", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建用户关联的资源池", httpMethod = "POST", notes = "createUserResourcePools", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "如果 type 是 user [为用户绑定资源池]，则 identity 为 27 ；<br>如果 type 是 group [为组织架构绑定资源池]，则 identity 为 15<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"poolIds\":[1],<br>"
					+ "\"identity\":27,<br>"
					+ "\"type\":\"user\"<br/>"
					+ "}", required = true, dataType = "String", paramType = "body")
	})
	public String createUserResourcePools(@RequestBody HashMap<String, Object> paramMap) {
		SysLogObj logObj = WebSysLogTool.generateLog(httpRequest);
		parseCurrentLoginIds(paramMap);
		return this.userService.createUserResourcePools(paramMap, logObj);
	}

	@RequestMapping(value = "/user/dataCenters", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建用户关联的机房", httpMethod = "POST", notes = "createUserDataCenters", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "如果 type 是 user [为用户绑定机房]，则 identity 为 27 ；<br>如果 type 是 group [为组织架构绑定机房]，则 identity 为 15<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"dataCenterIds\":[1],<br>"
					+ "\"identity\":15,<br>"
					+ "\"type\":\"group\"<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createUserDataCenters(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.userService.createUserDataCenters(paramMap);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户", httpMethod = "GET", notes = "listUsers", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：admin", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "departmentId", value = "部门ID，<br/>例子：13", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "groupId", value = "组织架构ID，<br/>例子：15", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "isDisplay", value = "是否显示，<br/>例子：1 / 0", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listUsers(
			@RequestParam(required = false, value = "loginId") String loginId,
			@RequestParam(required = false, value = "departmentId") String departmentId,
			@RequestParam(required = false, value = "groupId") String groupId,
			@RequestParam(required = false, value = "isDisplay") String isDisplay,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();

		if (isDisplay != null) {
			paramMap.put("isDisplay", isDisplay);
		} else {
			paramMap.put("isDisplay", 1);
		}
		if (loginId != null) {
			paramMap.put("loginId", loginId);
		}
		if (departmentId != null) {
			paramMap.put("department", departmentId);
		}
		if (groupId != null) {
			paramMap.put("groupId", groupId);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		paramMap.put("sessionLoginId",getCurrentLoginId());
		return this.userService.listUsers(paramMap);
	}

	@RequestMapping(value = "/users/{userId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户详情", httpMethod = "GET", notes = "listUserDetails", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：27", required = true, dataType = "Integer", paramType = "path")
	})
	public String listUserDetails(@PathVariable Integer userId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		return this.userService.listUserDetails(paramMap);
	}

	@RequestMapping(value = "/users/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "根据登录名查询用户详情", httpMethod = "GET", notes = "listUserDetailsByLoginId", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：admin", required = true, dataType = "String", paramType = "query")
	})
	public String listUserDetailsByLoginId(@RequestParam String loginId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);
		return this.userService.listUserDetailsByLoginId(paramMap);
	}

	@RequestMapping(value = "/users/currentuser", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询当前用户详情", httpMethod = "GET", notes = "listCurrentUserDetails", response = String.class)
	public String listCurrentUserDetails() {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseCurrentLoginIds(paramMap);
		return this.userService.listCurrentUserDetails(paramMap);
	}

	@RequestMapping(value = "/users/{loginId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
//	@ApiOperation(value="查询过滤指定用户后的用户", httpMethod = "GET", notes = "listFilterUser", response = String.class)
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "loginId", value = "登录名", required = true, dataType = "String",  paramType = "path")
//	})
	public String listFilterUser(@PathVariable String loginId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		String filterLoginIds = userCache.getParentRelationLoginIds(loginId);
		paramMap.put("filterLoginIds", filterLoginIds);
		return this.userService.listFilterUser(paramMap);
	}

	@RequestMapping(value = "/users/{loginId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除用户", httpMethod = "DELETE", notes = "deleteUser", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：ccc", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：25", required = true, dataType = "String", paramType = "query")
	})
	public String deleteUser(
			@PathVariable String loginId,
			@RequestParam Integer userId) {
		return this.userService.deleteUser(null, getCurrentLoginId(), loginId, userId, null);
	}

	/**
	 * 创建用户，步骤：<br>
	 * 1、在openstack中创建peojectid 2、数据库中创建相关的用户数据
	 *
	 * @param userMap
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建用户", httpMethod = "POST", notes = "createUser", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "userMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br>"
					+ "\"departmentGroup\":\"15\",<br>"
					+ "\"email\":null,<br>"
					+ "\"loginId\":\"apiuser\",<br>"
					+ "\"moreinfo\":null,<br>"
					+ "\"name\":\"apiuser\",<br>"
					+ "\"password\":\"123123\",<br>"
					+ "\"phone\":\"1500288511\",<br>"
					+ "\"position\":null<br>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createUser(
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestBody HashMap<String, Object> userMap) {
		userMap.put("op", "local");
		if (regionName != null) {
			userMap.put("regionName", regionName);
		}
		userMap.put("sysLoginId", getCurrentLoginId());
		return userService.addUser(userMap);
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑用户", httpMethod = "POST", notes = "updateUser", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：27", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"id\":27,<br>"
					+ "\"loginId\":\"swtest\",<br>"
					+ "\"name\":\"Swagger测试\",<br>"
					+ "\"phone\":\"110\",<br>"
					+ "\"email\":\"110@110.com\",<br>"
					+ "\"departmentGroup\":15,<br>"
					+ "\"department\":0,<br>"
					+ "\"position\":\"dev\",<br>"
					+ "\"moreinfo\":\"不要删除\"<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String updateUser(
			@PathVariable Integer userId,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("userId", userId);
		return this.userService.updateUser(paramMap);
	}

	@RequestMapping(value = "/users/{userId}/roles", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户角色", httpMethod = "GET", notes = "listUserRoles", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：27", required = true, dataType = "String", paramType = "path")
	})
	public String listUserRoles(@PathVariable String userId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		return this.userService.listUserRoles(paramMap);
	}

	@RequestMapping(value = "/user/resource/relation/{loginId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户与资源的关系", httpMethod = "GET", notes = "listUserResourceRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginId", value = "用户名，<br/>例子：dev", required = true, dataType = "string", paramType = "path"),
		@ApiImplicitParam(name = "resourceType", value = "资源类型，<br/>例子：VIR_VOLUME/VIR_NETWORK/VIR_INSTANCE/VIR_IMAGE/<br>VIR_FLAVOR/VIRTUAL/OPERATION/DEPLOY", required = false, dataType = "string", paramType = "query")
	})
	public String listUserResourceRelation(@PathVariable String loginId, @RequestParam(required = false, value = "resourceType") String resourceType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);
		paramMap.put("resourceType", resourceType);

		return this.userService.listUserResourceRelation(paramMap);
	}

	@RequestMapping(value = "/user/resource/relation/{loginId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置用户与资源的关系", httpMethod = "POST", notes = "updateUserResourceRelation", response = String.class)
	public String updateUserResourceRelation(
			@PathVariable @ApiParam(name="loginId", value="用户名，<br>例子：dev", required=true) String loginId,
			@RequestBody @ApiParam(name="userResource", value="设置用户与资源关系对象", required=true) UserResourceUpdateBean userResource) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(userResource));
		paramMap.put("loginId", loginId);
		return this.userService.updateUserResourceRelation(paramMap);
	}

	@RequestMapping(value = "/user/resourceGroup/relation/{loginId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户与资源组的关系", httpMethod = "GET", notes = "listUserResourceGroupRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginId", value = "用户名，<br/>例子：dev", required = true, dataType = "string", paramType = "path"),
		@ApiImplicitParam(name = "groupType", value = "资源组类型，<br/>例子：resourcePool/group/datacenter", required = true, dataType = "string", paramType = "query")
	})
	public String listUserResourceGroupRelation(@PathVariable String loginId, @RequestParam(required = true, value = "groupType") String groupType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);
		paramMap.put("groupType", groupType);

		return this.userService.listUserResourceGroupRelation(paramMap);
	}

	@RequestMapping(value = "/user/resourceGroup/relation/{loginId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置用户与资源组的关系", httpMethod = "POST", notes = "updateUserResourceGroupRelation", response = String.class)
	public String updateUserResourceGroupRelation(
			@PathVariable @ApiParam(name="loginId", value="用户名，<br/>例子：dev", required=true) String loginId,
			@RequestBody @ApiParam(name="userResourceGroup", value="设置组织机构与资源关系对象", required=true) UserResourceGroupUpdateBean userResourceGroup){
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(userResourceGroup));
		paramMap.put("loginId", loginId);
		return this.userService.updateUserResourceGroupRelation(paramMap);
	}

	@RequestMapping(value = "/user/department/relation/{loginId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户与组织机构的关系", httpMethod = "GET", notes = "listUserDepartmentRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginId", value = "用户名，<br/>例子：dev", required = true, dataType = "string", paramType = "path")
	})
	public String listUserDepartmentRelation(@PathVariable String loginId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);

		return this.userService.listUserDepartmentRelation(paramMap);
	}

	@RequestMapping(value = "/user/department/relation/{loginId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置用户与组织机构的关系", httpMethod = "POST", notes = "updateUserDepartmentRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginId", value = "用户名，<br/>例子：dev", required = true, dataType = "string", paramType = "path"),
		@ApiImplicitParam(name = "departmentIds", value = "组织机构ID集合字符串，<br/>例子：77,104", required = true, dataType = "string", paramType = "query")
	})
	public String updateUserDepartmentRelation(@PathVariable String loginId, @RequestParam(required = true, value = "departmentIds") String departmentIds) {
		HashMap<String, Object> paramMap  = new HashMap<String, Object>();
		paramMap.put("loginId", loginId);
		paramMap.put("departmentIds", departmentIds);
		return this.userService.updateUserDepartmentRelation(paramMap);
	}
	
	@RequestMapping(value = "/user/department/relation/{loginId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除用户与组织机构的关系", httpMethod = "DELETE", notes = "deleteUserDepartmentRelation", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginId", value = "用户名，<br/>例子：dev", required = true, dataType = "string", paramType = "path"),
		@ApiImplicitParam(name = "departmentId", value = "组织机构ID，<br/>例子：55", required = true, dataType = "string", paramType = "query")
	})
	public String deleteUserDepartmentRelation(@PathVariable String loginId, @RequestParam(required = true, value = "departmentId") String departmentId) {
		HashMap<String, Object> paramMap  = new HashMap<String, Object>();
		paramMap.put("loginId", loginId);
		paramMap.put("departmentId", departmentId);
		return this.userService.deleteUserDepartmentRelation(paramMap);
	}


	@RequestMapping(value = "/users/{userId}/roles", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建用户关联角色", httpMethod = "POST", notes = "createUserRoles", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：27", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userRoleMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"roleIds\":[1,2],<br>"
					+ "\"userId\":27<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createUserRoles(
			@PathVariable String userId,
			@RequestBody HashMap<String, Object> userRoleMap) {
		userRoleMap.put("userId", userId);
		parseCurrentLoginIds(userRoleMap);
		return this.userService.createUserRoles(userRoleMap);
	}

	@RequestMapping(value = "/treeMenus", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询菜单树", httpMethod = "GET", notes = "listTreeMenus", response = String.class)
	public String listTreeMenus() {
		return this.userService.listTreeMenus();
	}

	@RequestMapping(value = "/menus", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询菜单", httpMethod = "GET", notes = "listMenus", response = String.class)
	public String listMenus() {
		return this.userService.listMenus();
	}

	@RequestMapping(value = "/roles/{roleId}/treeMenus", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询角色关联的菜单树", httpMethod = "GET", notes = "listRolesTreeMenus", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色ID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path")
	})
	public String listRolesTreeMenus(@PathVariable Integer roleId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("roleId", roleId);
		return this.userService.listRolesTreeMenus(paramMap);
	}

	@RequestMapping(value = "/roles/{roleId}/menus", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询角色关联的菜单", httpMethod = "GET", notes = "listRolesTreeMenus", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色ID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path")
	})
	public String listRolesMenus(@PathVariable Integer roleId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("roleId", roleId);
		return this.userService.listRolesMenus(paramMap);
	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询角色", httpMethod = "GET", notes = "listRoles", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "角色ID，<br/>例子：1", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listRoles(
			@RequestParam(required = false, value = "id") Integer id,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (id != null) {
			paramMap.put("id", id);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.userService.listRoles(paramMap);
	}


	@RequestMapping(value = "/roles", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建角色", httpMethod = "POST", notes = "createRole", response = String.class)
	public String createRole(@RequestBody RoleCreateBean role) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> roleMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(role));
		parseCurrentLoginIds(roleMap);
		return this.userService.createRole(roleMap);
	}

	@RequestMapping(value = "/roles/{roleId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "修改角色", httpMethod = "PUT", notes = "updateRole", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色ID，<br/>例子：6", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "roleMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"id\":6,<br/>"
					+ "\"name\":\"平台管理员\",<br/>"
					+ "\"oldName\":\"中心管理员\",<br/>"
					+ "\"description\":\"中心管理员\",<br/>"
					+ "\"type\":\"100\",<br/>"
					+ "\"isDefault\":1<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String updateRole(
			@PathVariable Integer roleId,
			@RequestBody HashMap<String, Object> roleMap) {
		roleMap.put("roleId", roleId);
		parseCurrentLoginIds(roleMap);
		return this.userService.updateRole(roleMap);
	}

	@RequestMapping(value = "/roles/{roleId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除角色", httpMethod = "DELETE", notes = "deleteRole", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色ID，<br/>例子：6", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "roleId", value = "角色名称，<br/>例子：平台管理员", required = true, dataType = "String", paramType = "query")
	})
	public String deleteRole(
			@PathVariable Integer roleId,
			@RequestParam(required = true, value = "roleName") String roleName) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", roleId);
		paramMap.put("roleName", roleName);
		parseCurrentLoginIds(paramMap);
		return this.userService.deleteRole(paramMap);
	}

	@RequestMapping(value = "/roles/menus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建角色关联的菜单", httpMethod = "POST", notes = "createRoleMenus", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleMenuMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"roleId\":7,<br>"
					+ "\"menuIds\":[1,195,57,9,196,197]<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createRoleMenus(
			@RequestBody HashMap<String, Object> roleMenuMap) {
		parseCurrentLoginIds(roleMenuMap);
		return this.userService.createRoleMenus(roleMenuMap);
	}

	@RequestMapping(value = "/users/relations/{loginId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
//	@ApiOperation(value="查询用户间关系", httpMethod = "GET", notes = "listUserRelations", response = String.class)
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "loginId", value = "登录名", required = true, dataType = "String",  paramType = "path")
//	})
	public String listUserRelations(@PathVariable String loginId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);
		return this.userService.listUserRelations(paramMap);
	}

	@RequestMapping(value = "/users/relations", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
//	@ApiOperation(value="创建用户间关系", httpMethod = "POST", notes = "createUserRelations", response = String.class)
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "userRelationsMap", value = "参数体", required = true, dataType = "String",  paramType = "body")
//	})
	public String createUserRelations(@RequestBody HashMap<String, Object> userRelationsMap) {
		return this.userService.createUserRelations(userRelationsMap);
	}

	@RequestMapping(value = "/users/treeMenus", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询当前用户关联的菜单树", httpMethod = "GET", notes = "listUserTreeMenus", response = String.class)
	public String listUserTreeMenus() {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		return this.userService.listUserTreeMenus(paramMap);
	}

	/**
	 * 根据session中的登录ID获取相应的目录权限
	 *
	 * @return
	 */
	@RequestMapping(value = "/users/menus", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询当前用户关联的菜单", httpMethod = "GET", notes = "listUserMenus", response = String.class)
	public String listUserMenus() {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		return this.userService.listUserMenus(paramMap);
	}


	@RequestMapping(value = "/user/{loginId}/data", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户数据权限", httpMethod = "GET", notes = "listUserData", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：admin", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "type", value = "类型，<br/>例子：VIR_VOLUME,VIR_NETWORK,VIR_INSTANCE", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "groupBy", value = "分组字段，<br/>例子：type", required = false, dataType = "String", paramType = "query")
	})
	public String listUserData(
			@PathVariable String loginId,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "groupBy") String groupBy) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginId", loginId);

		if (type != null) {
			paramMap.put("type", type);
		}

		if (groupBy != null) {
			paramMap.put("groupBy", groupBy);
		}
		return this.userService.listUserData(paramMap);
	}

	@RequestMapping(value = "/user/{loginId}/data", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑用户数据权限", httpMethod = "POST", notes = "updateUserData", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：dev", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "type", value = "类型，<br/>例子：VIRTUAL", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"dataList\":[<br/>"
					+ "{<br/>"
					+ "\"VIR_VOLUME\":[\"0\"],<br>"
					+ "\"VIR_NETWORK\":[\"0\"],<br>"
					+ "\"VIR_INSTANCE\":[\"0\"],<br>"
					+ "\"VIR_IMAGE\":[\"0\"],<br>"
					+ "\"VIR_FLAVOR\":[\"0\"],<br>"
					+ "\"VIRTUAL\":[\"1377264512\"],<br>"
					+ "\"OPERATION\":[\"0\"],<br>"
					+ "\"DEPLOY\":[\"0\"]<br/>"
					+ "}<br/>"
					+ "]<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String updateUserData(
			@PathVariable String loginId,
			@RequestParam(required = false, value = "type") String type,
			@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("loginId", loginId);
		paramMap.put("currentLoginId", getCurrentLoginId());
		if (type != null) {
			paramMap.put("type", type);
		}
		return this.userService.updateUserData(paramMap);
	}

	@RequestMapping(value = "/user/logs", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户日志", httpMethod = "GET", notes = "listUserLogs", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "登录名，<br/>例子：admin", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "beginTs", value = "开始时间，<br/>例子：2018-01-01 00:00:00", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "endTs", value = "结束时间，<br/>例子：2018-05-22 23:59:59", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "typeArray", value = "资源类型，<br/>例子：createUserResourcePools,createUserDataCenters,deleteDepartmentGroup", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10"),
	})
	public String listUserLogs(
			@RequestParam(required = false, value = "loginId") String loginId,
			@RequestParam(required = false, value = "beginTs") String beginTs,
			@RequestParam(required = false, value = "endTs") String endTs,
			@RequestParam(required = false, value = "typeArray") String typeArray,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (loginId != null) {
			paramMap.put("loginId", loginId);
		}
		if (beginTs != null) {
			paramMap.put("beginTs", beginTs);
		}
		if (endTs != null) {
			paramMap.put("endTs", endTs);
		}
		if (typeArray != null) {
			StringBuffer subTypeBuffer = new StringBuffer();
			subTypeBuffer.append("''");
			for (String item : typeArray.split(",")) {
				subTypeBuffer.append(",'");
				subTypeBuffer.append(item);
				subTypeBuffer.append("'");
			}
			paramMap.put("subType", subTypeBuffer.toString());
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}

		return this.userService.listUserLogs(paramMap);
	}

	@RequestMapping(value = "/user/{userId}/availabilityZones", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询用户关联的可用性区域", httpMethod = "GET", notes = "listUserAvailabilityZones", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：1", required = true, dataType = "String", paramType = "path")
	})
	public String listUserAvailabilityZones(@PathVariable String userId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		return this.userService.listUserAvailabilityZones(paramMap);
	}

	@RequestMapping(value = "/user/{userId}/availabilityZone", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建用户与可用性区域关联关系", httpMethod = "POST", notes = "createUserAvailabilityZone", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID，<br/>例子：1", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"availabilityZones\":[<br/>"
					+ "{<br/>"
					+ "\"hostAggregateId\":\"1\",<br>"
					+ "\"region\":\"manageRegion\"<br/>"
					+ "}<br/>"
					+ "]<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createUserAvailabilityZone(
			@PathVariable String userId,
			@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("userId", userId);
		return this.userService.createUserAvailabilityZone(paramMap);
	}

	@RequestMapping(value = "/engines", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询功能引擎", httpMethod = "GET", notes = "listEngines", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "引擎ID，<br/>例子：3", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "引擎类型，<br/>例子：OPENSTACK / ZABBIX / SALTSTACK", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listEngines(
			@RequestParam(required = false, value = "id") Integer id,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (id != null) {
			paramMap.put("id", id);
		}
		if (type != null) {
			paramMap.put("type", type);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.systemService.listEngines(paramMap);
	}

	@RequestMapping(value = "/engine", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建功能引擎", httpMethod = "POST", notes = "createEngine", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"description\":\"openstack-test测试引擎\",<br/>"
					+ "\"enabled\":1,<br/>"
					+ "\"group\":\"DIRECT\",<br/>"
					+ "\"host\":\"192.168.1.179\",<br/>"
					+ "\"name\":\"openstack-test\",<br/>"
					+ "\"password\":\"123456\",<br/>"
					+ "\"port\":\"80\",<br/>"
					+ "\"type\":\"OPENSTACK\",<br/>"
					+ "\"url\":\"http://192.168.1.179:5000\",<br/>"
					+ "\"user\":\"admin\"<br/>"
					+ "}", required = true, dataType = "String", paramType = "body")
	})
	public String createEngine(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.systemService.createEngine(paramMap);
	}

	@RequestMapping(value = "/engine/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑功能引擎", httpMethod = "PUT", notes = "updateEngine", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "引擎ID，<br/>例子：3", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"id\":3,<br/>"
					+ "\"name\":\"saltstack\",<br/>"
					+ "\"host\":\"192.168.1.172\",<br/>"
					+ "\"password\":\"065EF054C7FE64FCF8A0744ECE075096\",<br/>"
					+ "\"user\":\"saltapi\",<br/>"
					+ "\"url\":\"http://192.168.1.172:9087\",<br/>"
					+ "\"type\":\"SALTSTACK\",<br/>"
					+ "\"enabled\":1,<br/>"
					+ "\"port\":9087,<br/>"
					+ "\"group\":\"DIRECT\",<br/>"
					+ "\"typeName\":\"运维引擎\",<br/>"
					+ "\"regionCount\":1<br/>"
					+ "}", required = true, dataType = "String", paramType = "body")
	})
	public String updateEngine(
			@PathVariable Integer id,
			@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.systemService.updateEngine(paramMap);
	}

	@RequestMapping(value = "/engine/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除功能引擎", httpMethod = "DELETE", notes = "deleteEngine", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "引擎ID，<br/>例子：3", required = true, dataType = "Integer", paramType = "path")
	})
	public String deleteEngine(@PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.systemService.deleteEngine(paramMap);
	}

	@RequestMapping(value = "/engine/regions", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询Region", httpMethod = "GET", notes = "listEngineRegions", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "RegionID，<br/>例子：1", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "engineId", value = "引擎ID，<br/>例子：1", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "engineType", value = "引擎类型，<br/>例子：OPENSTACK", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "availableStatus", value = "可用状态，<br/>例子：1", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "poolRegionName", value = "资源池Region名称，<br/>例子：manageRegion", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listEngineRegions(
			@RequestParam(required = false, value = "id") Integer id,
			@RequestParam(required = false, value = "engineId") Integer engineId,
			@RequestParam(required = false, value = "engineType") String engineType,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = false, value = "availableStatus") String availableStatus,
			@RequestParam(required = false, value = "poolRegionName") String poolRegionName,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (id != null) {
			paramMap.put("id", id);
		}

		if (regionName != null) {
			paramMap.put("regionName", regionName);
		}

		if (engineId != null) {
			paramMap.put("engineId", engineId);
		}

		if (engineType != null) {
			paramMap.put("engineType", engineType);
		}

		if (availableStatus != null) {
			paramMap.put("availableStatus", availableStatus);
		}

		if (poolRegionName != null) {
			paramMap.put("poolRegionName", poolRegionName);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}

		if (start != null) {
			paramMap.put("start", start);
		}

		if (length != null) {
			paramMap.put("length", length);
		}
		return this.systemService.listEngineRegions(paramMap);
	}

	@RequestMapping(value = "/engine/region", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建Region", httpMethod = "POST", notes = "createEngineRegion", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br>"
					+ "例子：<br>"
					+ "{<br>"
					+ "\"id\":\"\",<br>"
					+ "\"name\":\"openstack\",<br>"
					+ "\"host\":\"192.168.1.170\",<br>"
					+ "\"password\":null,<br>"
					+ "\"user\":null,<br>"
					+ "\"url\":null,<br>"
					+ "\"type\":\"KVM\",<br>"
					+ "\"enabled\":1,<br>"
					+ "\"port\":5000,<br>"
					+ "\"group\":\"DIRECT\",<br>"
					+ "\"typeName\":\"虚拟化引擎\",<br>"
					+ "\"regionCount\":1,<br>"
					+ "\"regionModalType\":\"Create\",<br>"
					+ "\"oss_engine_id\":1,<br>"
					+ "\"region_name\":\"regionOne\",<br>"
					+ "\"project\":null<br>"
					+ "}", required = true, dataType = "String", paramType = "body")
	})
	public String createEngineRegion(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.systemService.createEngineRegion(paramMap);
	}

	@RequestMapping(value = "/engine/region/{regionId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑Region", httpMethod = "PUT", notes = "updateEngineRegion", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "regionId", value = "RegionID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"id\":1,<br>"
					+ "\"region_name\":\"manageRegion\",<br>"
					+ "\"type\":\"KVM\",<br>"
					+ "\"oss_engine_id\":1,<br>"
					+ "\"typeName\":\"KVM\",<br>"
					+ "\"regionModalType\":\"Edit\",<br>"
					+ "\"user\":null,<br>"
					+ "\"password\":null,<br>"
					+ "\"url\":null,<br>"
					+ "\"project\":null<br/>"
					+ "}", required = true, dataType = "String", paramType = "body")
	})
	public String updateEngineRegion(@PathVariable Integer regionId, @RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("regionId", regionId);
		return this.systemService.updateEngineRegion(paramMap);
	}

	@RequestMapping(value = "/engine/region/{regionId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除Region", httpMethod = "DELETE", notes = "deleteEngineRegion", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "regionId", value = "RegionID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path"),
	})
	public String deleteEngineRegion(@PathVariable Integer regionId) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", regionId);
		parseCurrentLoginIds(paramMap);
		return this.systemService.deleteEngineRegion(paramMap);
	}

	@RequestMapping(value = "/lifecycle/action", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询生命周期", httpMethod = "GET", notes = "listLifecycleAction", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "action", value = "动作，<br/>例子：ALERT / SHUTDOWN / DELETE", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listLifecycleAction(
			@RequestParam(required = false, value = "action") String action,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (action != null) {
			paramMap.put("action", action);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.systemService.listLifecycleAction(paramMap);
	}

	@RequestMapping(value = "/lifecycle/action", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建生命周期", httpMethod = "POST", notes = "createLifecycleAction", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"num\":-1,<br>"
					+ "\"action\":\"ALERT\",<br>"
					+ "\"type\":\"BEFORE\"<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String createLifecycleAction(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.systemService.createLifecycleAction(paramMap);
	}

	@RequestMapping(value = "/lifecycle/action/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑生命周期", httpMethod = "PUT", notes = "updateLifecycleAction", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "生命周期ID，<br/>例子：4", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"id\":4,<br>"
					+ "\"type\":\"BEFORE\",<br>"
					+ "\"num\":-1,<br>"
					+ "\"action\":\"ALERT\"<br/>"
					+ "}",
					required = true, dataType = "String", paramType = "body")
	})
	public String updateLifecycleAction(@PathVariable Integer id, @RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("id", id);
		return this.systemService.updateLifecycleAction(paramMap);
	}

	@RequestMapping(value = "/lifecycle/action/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除生命周期", httpMethod = "DELETE", notes = "deleteLifecycleAction", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "生命周期ID，<br/>例子：4", required = true, dataType = "Integer", paramType = "path")
	})
	public String deleteLifecycleAction(@PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.systemService.deleteLifecycleAction(paramMap);
	}

	@RequestMapping(value = "/files/app/script", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询APP脚本文件", httpMethod = "GET", notes = "listAppScriptFiles", response = String.class)
	public String listAppScriptFiles() {
		HashMap<String, Object> paramMap = new HashMap<>();
		return this.systemService.listAppScriptFiles(paramMap);
	}

	@RequestMapping(value = "/license/detail", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询当前License详情", httpMethod = "GET", notes = "getCurrentLicenseDetail", response = String.class)
	public String getCurrentLicenseDetail() {
		return this.systemService.getCurrentLicenseDetail();
	}

	@RequestMapping(value = "/license/load", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "加载当前License文件", httpMethod = "POST", notes = "loadCurrentLicenseFile", response = String.class)
	public String loadCurrentLicenseFile() {
		return this.systemService.loadCurrentLicenseFile();
	}

	@RequestMapping(value = "/field/template", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建数据模型", httpMethod = "POST", notes = "createOssSystemFieldTemplate", response = String.class)
	public String createOssSystemFieldTemplate(@RequestBody @ApiParam(name="template", value="创建配置模板对象", required=true)FieldTemplateCreateBean bean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(bean));
		parseCurrentLoginIds(paramMap);
		return this.systemService.createOssSystemFieldTemplate(paramMap);
	}

	@RequestMapping(value = "/field/template/{templateId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除数据模型", httpMethod = "DELETE", notes = "deleteOssSystemFieldTemplate", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "templateId", value = "数据模型ID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path")
	})
	public String deleteOssSystemFieldTemplate(@PathVariable Integer templateId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", templateId);
		parseCurrentLoginIds(paramMap);
		return this.systemService.deleteOssSystemFieldTemplate(paramMap);
	}

	@RequestMapping(value = "/field/template/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑数据模型", httpMethod = "PUT", notes = "updateOssSystemFieldTemplate", response = String.class)
	public String updateOssSystemFieldTemplate(
			@PathVariable @ApiParam(name="id", value="数据模型ID，<br/>例子：1", required=true) Integer id,
			@RequestBody @ApiParam(name="template", value="编辑配置模板对象", required=true)FieldTemplateUpdateBean bean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(bean));
		paramMap.put("id", id);
		if(bean.getDefaultValue().equals("")){
			paramMap.put("defaultValue", "");
		}
		return this.systemService.updateOssSystemFieldTemplate(paramMap);
	}

	@RequestMapping(value = "/field/template", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询数据模型", httpMethod = "GET", notes = "listOssSystemFieldTemplate", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "key", value = "关键字", required = false, dataType = "string",  paramType = "query"),
			@ApiImplicitParam(name = "type", value = "控件类型", required = false, dataType = "string",  paramType = "query"),
			@ApiImplicitParam(name = "resourceType", value = "资源类型", required = false, dataType = "string",  paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listOssSystemFieldTemplate(
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "resourceType") String resourceType,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap  = new HashMap<>();
		if(null != key){
			paramMap.put("key",key);
		}
		if(null != type && !type.equals("")){
			paramMap.put("type",type);
		}
		if(null != resourceType && !resourceType.equals("")){
			paramMap.put("resourceType",resourceType);
		}
		if(null != start){
			paramMap.put("start",start);
		}
		if(null != length){
			paramMap.put("length",length);
		}
		parseCurrentLoginIds(paramMap);
		return this.systemService.listOssSystemFieldTemplate(paramMap);
	}

	@RequestMapping(value = "/field/template/{resourceType}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源类型的数据模型", httpMethod = "GET", notes = "listResourceTypeOssSystemFieldTemplate", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "resourceType", value = "资源类型", required = true, dataType = "string",  paramType = "path")
	})
	public String listResourceTypeOssSystemFieldTemplate(@PathVariable String resourceType) {
		HashMap<String, Object> paramMap  = new HashMap<>();
		paramMap.put("resourceType",resourceType);
		
		return this.systemService.listResourceTypeOssSystemFieldTemplate(paramMap);
	}
	
	@RequestMapping(value = "/field/template/{templateId}/resourceType", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询数据模型的资源类型", httpMethod = "GET", notes = "listOssSystemFieldTemplateResourceType", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "templateId", value = "字段模板ID", required = true, dataType = "integer",  paramType = "path")
	})
	public String listOssSystemFieldTemplateResourceType(@PathVariable Integer templateId) {
		HashMap<String, Object> paramMap  = new HashMap<>();
		paramMap.put("templateId",templateId);
		
		return this.systemService.listOssSystemFieldTemplateResourceType(paramMap);
	}
	
	@RequestMapping(value = "/field/template/relation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建资源与数据模型关系", httpMethod = "POST", notes = "createRnFieldTemplateRelation", response = String.class)
	public String createRnFieldTemplateRelation(@RequestBody @ApiParam(name="relation", value="创建数据模型关系对象", required=true)FieldTemplateRelationCreateBean bean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(bean));
		parseCurrentLoginIds(paramMap);
		return this.systemService.createRnFieldTemplateRelation(paramMap);
	}
	
	@RequestMapping(value = "/departments", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建部门", httpMethod = "POST", notes = "createDepartment", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paramMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"name\":\"业务管理部\",<br>"
					+ "\"deptCode\":\"YWGLB\",<br>"
					+ "\"description\":\"业务管理部\",<br>"
					+ "\"projectId\":\"44931aec982e46739cb3336b8511db0d\",<br>"
					+ "\"refGroupId\":\"15\""
					+ "<br/>}",
					required = true, dataType = "String", paramType = "body"),
	})
	public String createDepartment(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.systemService.createDepartment(paramMap);
	}
	
	@RequestMapping(value = "/departments/{departmentId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑部门", httpMethod = "POST", notes = "updateDepartment", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "departmentId", value = "部门ID，<br/>例子：77", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "departmentMap", value = "参数体，<br/>"
					+ "例子：<br/>"
					+ "{<br/>"
					+ "\"deptCode\":\"YWGLB\",<br>"
					+ "\"description\":\"业务管理部\",<br>"
					+ "\"id\":\"77\",<br>"
					+ "\"name\":\"业务管理部\",<br>"
					+ "\"projectId\":\"44931aec982e46739cb3336b8511db0d\",<br>"
					+ "\"refGroupId\":\"15\"<br/>"
					+ "}",
					required = true, dataType = "HashMap", paramType = "body"),
	})
	public String updateDepartment(@PathVariable Integer departmentId, @RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("id", departmentId);
		parseCurrentLoginIds(paramMap);
		return this.systemService.updateDepartment(paramMap);
	}

	@RequestMapping(value = "/departments/{departmentId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除部门", httpMethod = "DELETE", notes = "deleteDepartment", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "departmentId", value = "部门ID，<br/>例子：1", required = true, dataType = "Integer", paramType = "path")
	})
	public String deleteDepartment(@PathVariable Integer departmentId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", departmentId);
		parseCurrentLoginIds(paramMap);
		return this.systemService.deleteDepartment(paramMap);
	}
}
