package com.system.started.rest.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.rest.request.DataCenterAreaCreateBean;
import com.system.started.rest.request.DataCenterAreaSingleCreateBean;
import com.system.started.rest.request.DataCenterCabinetUpdateBean;
import com.system.started.rest.request.DataCenterCreateBean;
import com.system.started.rest.request.DataCenterGroupCreateBean;
import com.system.started.rest.request.DataCenterGroupUpdateBean;
import com.system.started.rest.request.DataCenterUpdateBean;
import com.system.started.rest.request.DataCenterUserUpdateBean;
import com.system.started.rest.request.MonitorHostCreateBean;
import com.system.started.rest.request.MonitorHostFilesystemCreateBean;
import com.system.started.rest.request.MonitorHostUpdateBean;
import com.system.started.rest.request.MonitorProcessCreateBean;
import com.system.started.rest.request.MonitorSiteCreateBean;
import com.system.started.rest.request.ResourceGroupCreateBean;
import com.system.started.rest.request.ResourceGroupResourceNodeUpdateBean;
import com.system.started.rest.request.ResourceGroupUpdateBean;
import com.system.started.rest.request.ResourceNetworkNodePortCreateBean;
import com.system.started.rest.request.ResourceNetworkNodePortUpdateBean;
import com.system.started.rest.request.ResourceNetworkNodeRouteTableCreateBean;
import com.system.started.rest.request.ResourceNetworkNodeRouteTableUpdateBean;
import com.system.started.rest.request.ResourceNodeCreateBean;
import com.system.started.rest.request.ResourceNodeResourceGroupUpdateBean;
import com.system.started.rest.request.ResourceNodeUpdateBean;
import com.system.started.service.DataCenterService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@SuppressWarnings("unchecked")
@RestController
@RequestMapping(value = "/datacenters")
@Api(value="/datacenters", description="资产管理控制器")
public class DataCenterController extends AbstractController {
	private final static Logger logger = LoggerFactory.getLogger(DataCenterController.class);


	@Autowired
	private DBService dbService;

	@Autowired
	private DataCenterService dataCenterService;

	@RequestMapping(value = "/dict/{dictType}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询字典", httpMethod = "GET", notes = "listDict", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dictType", value = "字典类型，<br/>例子：MODULE_WORK_ORDER_CHECK_RESOURCE_TYPE", required = true, dataType = "String", paramType = "path")
	})
	public String listDict(@PathVariable String dictType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("dictType", dictType);
		return this.dataCenterService.listDict(paramMap);
	}

	@RequestMapping(value = "/dict/{dictType}/recursion", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "递归查询字典", httpMethod = "GET", notes = "listRecuDict", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dictType", value = "字典类型，<br/>例子：MODULE_WORK_ORDER_CHECK_RESOURCE_TYPE", required = true, dataType = "String", paramType = "path")
	})
	public String listRecuDict(@PathVariable String dictType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("parentId", dictType);
		return this.dataCenterService.listRecuDict(paramMap);
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询机房", httpMethod = "GET", notes = "listDataCenters", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "groupId", value = "数据中心ID，<br/>例子：1", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listDataCenters(
			@RequestParam(required = false, name = "groupId") Integer groupId,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();

		parseRelationLoginIds(paramMap);
		if (start != null) {
			paramMap.put("start", Integer.parseInt(start));
		}
		if (length != null) {
			paramMap.put("length", Integer.parseInt(length));
		}
		if (groupId != null) {
			paramMap.put("groupId", groupId);
		}
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		return this.dataCenterService.listDataCenters(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询机房详情", httpMethod = "GET", notes = "listDataCenterDetails", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String listDataCenterDetails(@PathVariable Integer dataCenterId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("id", dataCenterId);
		return this.dataCenterService.listDataCenterDetails(paramMap);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建机房", httpMethod = "POST", notes = "createDataCenter", response = String.class)
	public String createDataCenter(@RequestBody DataCenterCreateBean dataCenterCreateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterCreateBean));
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createDataCenter(paramMap);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑机房", httpMethod = "PUT", notes = "updateDataCenter", response = String.class)
	public String updateDataCenter(@PathVariable @ApiParam(name = "id", value = "机房ID，<br/>例子：1", required = true) Integer id,
	                               @RequestBody DataCenterUpdateBean dataCenterUpdateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterUpdateBean));
		parseCurrentLoginIds(paramMap);
		paramMap.put("id", id);
		return this.dataCenterService.updateDataCenter(paramMap);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除机房", httpMethod = "DELETE", notes = "deleteDataCenter", response = String.class)
	public String deleteDataCenter(@PathVariable @ApiParam(name = "id", value = "机房ID，<br/>例子：1", required = true) Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.deleteDataCenter(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/users", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	@ApiOperation(value = "查询机房负责人", httpMethod = "GET", notes = "listDataCenterUsers", response = String.class)
	public String listDataCenterUsers(@PathVariable @ApiParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true) Integer dataCenterId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("dataCenterId", dataCenterId);
		return this.dataCenterService.listDataCenterUsers(paramMap);
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	@ApiOperation(value = "查询机房负责人详情", httpMethod = "GET", notes = "listDataCenterUserDetails", response = String.class)
	public String listDataCenterUserDetails(@PathVariable @ApiParam(name = "userId", value = "用户ID，<br/>例子：1", required = true) Integer userId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("id", userId);
		return this.dataCenterService.listDataCenterUserDetails(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/users/{userId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	@ApiOperation(value = "编辑机房负责人", httpMethod = "PUT", notes = "updateDataCenterUser", response = String.class)
	public String updateDataCenterUser(
			@PathVariable @ApiParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true) Integer dataCenterId,
			@PathVariable @ApiParam(name = "userId", value = "用户ID，<br/>例子：1", required = true) Integer userId,
			@RequestBody DataCenterUserUpdateBean dataCenterUserUpdateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterUserUpdateBean));
		paramMap.put("dataCenterId", dataCenterId);
		paramMap.put("userId", userId);
		return this.dataCenterService.updateDataCenterUser(paramMap);
	}

	@RequestMapping(value = "{id}/users/{loginId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	@ApiOperation(value = "删除机房负责人", httpMethod = "DELETE", notes = "deleteDataCenterUser", response = String.class)
	public String deleteDataCenterUser(
			@PathVariable @ApiParam(name = "id", value = "机房ID，<br/>例子：1", required = true) Integer id,
			@PathVariable @ApiParam(name = "loginId", value = "用户名，<br/>例子：dev", required = true) String loginId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("loginId", loginId);
		return this.dataCenterService.deleteDataCenterUser(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/area", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询机房机柜", httpMethod = "GET", notes = "listDataCenterArea", response = String.class)
	public String listDataCenterArea(
			@PathVariable @ApiParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true) Integer dataCenterId,
			@RequestParam(required = false, value = "type") @ApiParam(name = "type", value = "查询机柜分组，<br/>例子：group", required = false) String type) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("dataCenterId", dataCenterId);
		if(type != null){
			paramMap.put("type", type);
		}
		return this.dataCenterService.listDataCenterArea(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/area/{name}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询机房机柜编号", httpMethod = "GET", notes = "listDataCenterAreaCabinetNum", response = String.class)
	public String listDataCenterAreaCabinetNum(
			@PathVariable @ApiParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true) Integer dataCenterId,
			@PathVariable @ApiParam(name = "name", value = "机柜名称，<br/>例子：A", required = true) String name) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("dataCenterId", dataCenterId);
		paramMap.put("name", name);
		return this.dataCenterService.listDataCenterAreaCabinetNum(paramMap);     
	}

	@RequestMapping(value = "/cabinet/{id}/resourceNode/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询机柜绑定的资源数量", httpMethod = "GET", notes = "listDataCenterCabinetResourceNodeCount", response = String.class)
	public String listDataCenterCabinetResourceNodeCount(@PathVariable @ApiParam(name = "id", value = "机柜ID，<br/>例子：1", required = true) Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		return this.dataCenterService.listDataCenterCabinetResourceNodeCount(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/area", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建机柜", httpMethod = "POST", notes = "createDataCenterArea", response = String.class)
	public String createDataCenterArea(
			@PathVariable @ApiParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true) Integer dataCenterId,
			@RequestBody @ApiParam(name="dataCenterAreaCreateBean", value="创建机房机柜对象<br>例子：column：Z，endNum：1，startNum：50", required=true) DataCenterAreaCreateBean dataCenterAreaCreateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterAreaCreateBean));
		paramMap.put("dataCenterId", dataCenterId);
//		parseRelationLoginIds(paramMap);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createDataCenterArea(paramMap);
	}
	
	@RequestMapping(value = "/{dataCenterId}/area/single", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建单个机柜", httpMethod = "POST", notes = "createSingleDataCenterArea", response = String.class)
	public String createSingleDataCenterArea(
			@PathVariable @ApiParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true) Integer dataCenterId,
			@RequestBody @ApiParam(name="dataCenterAreaCreateBean", value="创建机房机柜对象<br>例子：cabinetNum：88，dataCenterId：18，name：A", required=true) DataCenterAreaSingleCreateBean dataCenterAreaSingleCreateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterAreaSingleCreateBean));
		paramMap.put("dataCenterId", dataCenterId);
//		parseRelationLoginIds(paramMap);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createDataCenterArea(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/area/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑机柜", httpMethod = "PUT", notes = "updateDataCenterArea", response = String.class)
	public String updateDataCenterArea(
			@PathVariable @ApiParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = true) Integer dataCenterId,
			@PathVariable @ApiParam(name = "id", value = "机柜ID，<br/>例子：14", required = true) Integer id,
			@RequestBody @ApiParam(name="dataCenterCabinetUpdateBean", value="编辑机柜对象，<br/>例子：cabinetNum：99，name：A", required=true) DataCenterCabinetUpdateBean dataCenterCabinetUpdateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterCabinetUpdateBean));
		paramMap.put("dataCenterId", dataCenterId);
		paramMap.put("id", id);
		return this.dataCenterService.updateDataCenterArea(paramMap);
	}

	@RequestMapping(value = "/area", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除机柜", httpMethod = "DELETE", notes = "deleteDataCenterArea", response = String.class)
	public String deleteDataCenterArea(@RequestParam(required = true) @ApiParam(name = "cabinetIds", value = "机柜ID，<br/>例子：1,2,3", required = true) String cabinetIds) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("ids", cabinetIds);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.deleteDataCenterArea(paramMap);
	}

	@RequestMapping(value = "/groups", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询数据中心", httpMethod = "GET", notes = "listDataCenterGroups", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string", paramType = "query", defaultValue = "10")
	})
	public String listDataCenterGroups(
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);

		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.dataCenterService.listDataCenterGroups(paramMap);
	}

	@RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询数据中心详情", httpMethod = "GET", notes = "listDataCenterGroupDetails", response = String.class)
	public String listDataCenterGroupDetails(@PathVariable @ApiParam(name = "groupId", value = "数据中心ID，<br/>例子：1", required = true) Integer groupId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("id", groupId);
		return this.dataCenterService.listDataCenterGroupDetails(paramMap);
	}

	@RequestMapping(value = "/group", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建数据中心", httpMethod = "POST", notes = "createDataCenterGroup", response = String.class)
	public String createDataCenterGroup(@RequestBody DataCenterGroupCreateBean dataCenterGroup) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterGroup));
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createDataCenterGroup(paramMap);
	}

	@RequestMapping(value = "/group/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑数据中心", httpMethod = "PUT", notes = "updateDataCenterGroup", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据中心ID，<br/>例子：18", required = true, dataType = "integer", paramType = "path"),
	})
	public String updateDataCenterGroup(@PathVariable Integer id, @RequestBody DataCenterGroupUpdateBean dataCenterGroup) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(dataCenterGroup));
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.updateDataCenterGroup(paramMap);
	}

	@RequestMapping(value = "/group/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "删除数据中心", httpMethod = "DELETE", notes = "deleteDataCenterGroup", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据中心ID，<br/>例子：18", required = true, dataType = "integer", paramType = "path"),
	})
	public String deleteDataCenterGroup(@PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.deleteDataCenterGroup(paramMap);
	}

	@RequestMapping(value = "/resourcePools", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源池", httpMethod = "GET", notes = "listResourcePools", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "poolId", value = "资源池ID，<br/>例子：1", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "key", value = "搜索关键字，<br/>例子：manage", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "poolType", value = "资源，<br/>例子：COMPUTE/BARE", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "资源池类型，<br/>例子：VIRTUAL/PHYSICAL", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=manage", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string", paramType = "query", defaultValue = "10")
	})
	public String listResourcePools(
			@RequestParam(required = false, name = "poolId") Integer poolId,
			@RequestParam(required = false, name = "key") String key,
			@RequestParam(required = false, name = "poolType") String poolType,
			@RequestParam(required = false, name = "regionName") String regionName,
			@RequestParam(required = false, name = "type") String type,
			@RequestParam(required = false, name = "queryColumn") String queryColumn,
			@RequestParam(required = false, name = "rules") String rules,
			@RequestParam(required = false, name = "start") String start,
			@RequestParam(required = false, name = "length") String length,
			@RequestParam(required = false, name = "instanceSort") String instanceSort,
			@RequestParam(required = false, name = "instanceSortDirection") String instanceSortDirection) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (poolType != null) {
			paramMap.put("poolType", poolType);
		}
		if (regionName != null) {
			paramMap.put("regionName", regionName);
		}
		if (type != null) {
			paramMap.put("type", type);
		}
		if (queryColumn != null) {
			paramMap.put("queryColumn", queryColumn);
		}
		if (rules != null) {
			List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
			String[] ruleList = rules.split(",");
			for (String rule : ruleList) {
				HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
				String[] ri = rule.split("&");
				for (String r : ri) {
					String[] i = r.split("=");
					String value = null;
					try {
						value = URLDecoder.decode(i[1], "UTF-8");
					} catch (UnsupportedEncodingException e) {
						logger.error("url param decode error !", e);
					}
					ruleItemMap.put(i[0], value);
				}
				ruleItems.add(ruleItemMap);
			}
			paramMap.put("list", ruleItems);
		}

		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}

		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}

		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.dataCenterService.listResourcePools(paramMap);
	}

	@RequestMapping(value = "/dataAuth/resourcePools", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询已授权的资源池", httpMethod = "GET", notes = "listDataAuthResourcePools", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "用户loginId，<br/>例子：admin", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "poolType", value = "类型，<br/>例子：COMPUTE/BARE", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "资源池类型，<br/>例子：VIRTUAL/PHYSICAL", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string", paramType = "query", defaultValue = "10")
	})
	public String listDataAuthResourcePools(
			@RequestParam(required = true, value = "loginId") String loginId,
			@RequestParam(required = false, value = "poolType") String poolType,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (poolType != null) {
			paramMap.put("poolType", poolType);
		}
		if (type != null) {
			paramMap.put("type", type);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		paramMap.put("loginId", loginId);
		return this.dataCenterService.listDataAuthResourcePools(paramMap);
	}

	@RequestMapping(value = "/resourcePools/{resourcePoolId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源池详情", httpMethod = "GET", notes = "listResourcePoolDetails", response = String.class)
	public String listResourcePoolDetails(@PathVariable @ApiParam(name = "resourcePoolId", value = "资源池ID，<br/>例子：1", required = true) Integer resourcePoolId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolId", resourcePoolId);
		parseRelationLoginIds(paramMap);
		return this.dataCenterService.listResourcePoolDetails(paramMap);
	}

	@RequestMapping(value = "/resourcePool/history", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源池历史", httpMethod = "GET", notes = "listResourcePoolHistory", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start_create_ts", value = "开始时间，<br/>例子：2018-5-30 00:00:01", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "end_create_ts", value = "截至时间，<br/>例子：2018-6-29 23:59:59", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "reportType", value = "报表类型，<br/>例子：DAY/WEEK/MONTH/QUARTER/YEAR", required = true, dataType = "string", paramType = "query")
	})
	public String listResourcePoolHistory(
			@RequestParam(required = true, value = "start_create_ts") String start_create_ts,
			@RequestParam(required = true, value = "end_create_ts") String end_create_ts,
			@RequestParam(required = true, value = "reportType") String reportType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolType", "COMPUTE");
		paramMap.put("type", "VIRTUAL");
		paramMap.put("reportType", reportType);
		paramMap.put("startCreateTs", start_create_ts);
		paramMap.put("endCreateTs", end_create_ts);
		return this.dataCenterService.listResourcePoolHistory(paramMap);
	}

	@RequestMapping(value = "/resourcePools", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建资源池", httpMethod = "POST", notes = "createResourcePool", response = String.class)
	public String createResourcePool(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createResourcePool(paramMap);
	}

	@RequestMapping(value = "/resourcePools/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除资源池", httpMethod = "DELETE", notes = "deleteResourcePool", response = String.class)
	public String deleteResourcePool(@PathVariable @ApiParam(name = "id", value = "资源池id，<br/>例子：1", required = true) Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.deleteResourcePool(paramMap);
	}

	@RequestMapping(value = "/resourcePools/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public String updateResourcePool(@PathVariable Integer id, @RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("id", id);
		return this.dataCenterService.updateResourcePool(paramMap);
	}

	@RequestMapping(value = "/resourcePools/{poolId}/users", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查资源池的用户", httpMethod = "GET", notes = "listResourcePoolUsers", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "poolId", value = "资源池id，<br/>例子：1", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listResourcePoolUsers(
			@PathVariable Integer poolId,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolId", poolId);
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.dataCenterService.listResourcePoolUsers(paramMap);
	}

	@RequestMapping(value = "/resourcePools/{poolId}/deptGroup", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "listResourcePoolDeptGroup", httpMethod = "GET", notes = "listResourcePoolDeptGroup", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "poolId", value = "资源池id，<br/>例子：1", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listResourcePoolDeptGroup(
			@PathVariable Integer poolId,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolId", poolId);
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.dataCenterService.listResourcePoolDeptGroup(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/deptGroup", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "listDataCenterDeptGroup", httpMethod = "GET", notes = "listDataCenterDeptGroup", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dataCenterId", value = "数据中心id，<br/>例子：1", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listDataCenterDeptGroup(
			@PathVariable Integer dataCenterId,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("dataCenterId", dataCenterId);
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.dataCenterService.listDataCenterDeptGroup(paramMap);
	}

	@RequestMapping(value = "/{dataCenterId}/deptGroup/users", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "listDataCenterDeptGroupUsers", httpMethod = "GET", notes = "listDataCenterDeptGroupUsers", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dataCenterId", value = "数据中心id，<br/>例子：1", required = true, dataType = "Integer", paramType = "query"),
	})
	public String listDataCenterDeptGroupUsers(@PathVariable  Integer dataCenterId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("dataCenterId", dataCenterId);
		return this.dataCenterService.listDataCenterDeptGroupUsers(paramMap);
	}

	@RequestMapping(value = "/resourcePools/{poolId}/deptGroup/users", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "listDataCenterDeptGroupUsers", httpMethod = "GET", notes = "listDataCenterDeptGroupUsers", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "poolId", value = "资源池id，<br/>例子：1", required = true, dataType = "Integer", paramType = "query"),
	})
	public String listResourcePoolDeptGroupUsers(@PathVariable Integer poolId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolId", poolId);
		return this.dataCenterService.listResourcePoolDeptGroupUsers(paramMap);
	}

	@RequestMapping(value = "/ipPools", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "listResourceIpPools", httpMethod = "GET", notes = "listResourceIpPools", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "poolId", value = "资源池id，<br/>例子：1", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "datacenterId", value = "数据中心id", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "resourceType", value = "资源类型，<br/>例子：1", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "machineType", value = "机器类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "返回", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "searchValue", value = "查询值，<br/>例子：1", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listResourceIpPools(
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "resourceType") String resourceType,
			@RequestParam(required = false, value = "machineType") String machineType,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, value = "search[value]") String searchValue) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (null != searchValue && !searchValue.equals("")) {
			paramMap = JsonHelper.fromJson(HashMap.class, searchValue);
		}

		if (datacenterId != null) {
			paramMap.put("datacenterId", datacenterId);
		}

		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}

		if (type != null) {
			paramMap.put("type", type);
		}
		if (machineType != null) {
			paramMap.put("machineType", machineType);
		}
		if (resourceType != null) {
			paramMap.put("resourceType", resourceType);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.listResourceIpPools(paramMap);
	}

	@RequestMapping(value = "/ipPools", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createResourceIpPool(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createResourceIpPool(paramMap);
	}


	@RequestMapping(value = "/ipPools/{poolId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteResourceIpPool(@PathVariable Integer poolId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolId", poolId);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.deleteResourceIpPool(paramMap);

	}


	@RequestMapping(value = "/ipPools", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateResourceIpPool(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.updateResourceIpPool(paramMap);
	}

	@RequestMapping(value = "/ipPools/{poolId}/relation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createResourceIpPoolRelation(
			@PathVariable Integer poolId,
			@RequestParam(required = false, value = "refPoolId") String refPoolId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolId", poolId);
		if (null != refPoolId) {
			paramMap.put("refPoolId", refPoolId);
		}
		return this.dataCenterService.createResourceIpPoolRelation(paramMap);
	}

	@RequestMapping(value = "/ipPools/{poolId}/relation", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceIpPoolRelation(
			@PathVariable Integer poolId,
			@RequestParam(required = true, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = true, value = "type") String type,
			@RequestParam(required = true, value = "resourceType") String resourceType,
			@RequestParam(required = true, value = "machineType") String machineType) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("poolId", poolId);

		if (datacenterId != null) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (type != null) {
			paramMap.put("type", type);
		}
		if (machineType != null) {
			paramMap.put("machineType", machineType);
		}
		if (resourceType != null) {
			paramMap.put("resourceType", resourceType);
		}

		return this.dataCenterService.listResourceIpPoolRelation(paramMap);
	}

	@RequestMapping(value = "/ipPools/{poolId}/items", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceIpItems(
			@PathVariable Integer poolId,
			@RequestParam(required = false, value = "status") String status) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (status != null) {
			paramMap.put("status", status);
		}

		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}

		return this.dataCenterService.listResourceIpItems(paramMap);
	}

	@RequestMapping(value = "/ipPools/{poolId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String lockIpItem(
			@PathVariable Integer poolId,
			@RequestParam Integer lockNum) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}

		if (lockNum != null) {
			paramMap.put("lockNum", lockNum);
		}
		return this.dataCenterService.lockIpItem(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/networks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询网络资源", httpMethod = "GET", notes = "listResourceNetworkNodes", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tagId", value = "标签id", required = false, dataType = "integer", paramType = "query"),
		@ApiImplicitParam(name = "tagValue", value = "标签值", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "manufacturerCode", value = "厂家", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "datacenterId", value = "数据中心id", required = false, dataType = "integer", paramType = "query"),
		@ApiImplicitParam(name = "nodeId", value = "资源id", required = false, dataType = "integer", paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查找列", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "规则", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "type", value = "资源类型", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "subType", value = "资源子类型", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string", paramType = "query", defaultValue = "10")
	})
	public String listResourceNetworkNodes(
			@RequestParam(required = false, value = "tagId") Integer tagId,
			@RequestParam(required = false, value = "tagValue") String tagValue,
			@RequestParam(required = false, value = "manufacturerCode") String manufacturerCode,
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "nodeId") String nodeId,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam String type,
			@RequestParam String subType,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (type != null) {
			paramMap.put("type", type);
		}
		
		if (subType != null) {
			paramMap.put("subType", subType);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		if (tagId != null) {
			paramMap.put("tagId", tagId);
		}
		
		if (tagValue != null) {
			List<String> tagList = new ArrayList<String>();
			for (String value : tagValue.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(value.trim());
				stringBuffred.append("'");

				tagList.add(stringBuffred.toString());
			}

			String tagListString = StringUtils.join(tagList, ",");

			paramMap.put("tagValue", tagListString);
		}
		if (manufacturerCode != null) {
			paramMap.put("manufacturerCode", manufacturerCode);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (datacenterId != null) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (nodeId != null) {
			paramMap.put("nodeId", nodeId);
		}
		if (queryColumn != null) {
			paramMap.put("queryColumn", queryColumn);
		}
		if (rules != null) {
			List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
			String[] ruleList = rules.split(",");
			for (String rule : ruleList) {
				HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
				String[] ri = rule.split("&");
				for (String r : ri) {
					String[] i = r.split("=");
					ruleItemMap.put(i[0], i[1]);
				}
				ruleItems.add(ruleItemMap);
			}
			paramMap.put("list", ruleItems);
		}
		parseRelationLoginIds(paramMap);

		return this.dataCenterService.listResourceNetworkNodes(paramMap);
	}
	
	@RequestMapping(value = "/network/{nodeId}/ports", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询交换机端口", httpMethod = "GET", notes = "listResourceNetworkPorts", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String listResourceNetworkPorts(@PathVariable Integer nodeId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		return this.dataCenterService.listResourceNetworkPorts(paramMap);
	}
	
	@RequestMapping(value = "/network/{nodeId}/port", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "添加交换机端口", httpMethod = "POST", notes = "createResourceNetworkPort", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String createResourceNetworkPort(@PathVariable Integer nodeId, @RequestBody ResourceNetworkNodePortCreateBean resourceNetworkNodePortBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNetworkNodePortBean));
		paramMap.put("nodeId", nodeId);
		return this.dataCenterService.createResourceNetworkPort(paramMap);
	}
	
	@RequestMapping(value = "/network/{nodeId}/port/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑交换机端口", httpMethod = "PUT", notes = "updateResourceNetworkPort", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "id", value = "端口id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String updateResourceNetworkPort(@PathVariable Integer nodeId, @PathVariable Integer id, @RequestBody ResourceNetworkNodePortUpdateBean resourceNetworkNodePortBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNetworkNodePortBean));
		paramMap.put("nodeId", nodeId);
		paramMap.put("id", id);
		return this.dataCenterService.updateResourceNetworkPort(paramMap);
	}
	
	@RequestMapping(value = "/network/{nodeId}/ports/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除交换机端口", httpMethod = "DELETE", notes = "deleteResourceNetworkPort", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "id", value = "端口id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String deleteResourceNetworkPort(@PathVariable Integer nodeId, @PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("id", id);
		return this.dataCenterService.deleteResourceNetworkPort(paramMap);
	}

	@RequestMapping(value = "/network/{nodeId}/routeTables", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询路由表", httpMethod = "GET", notes = "listResourceNetworkRouteTables", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String listResourceNetworkRouteTables(@PathVariable Integer nodeId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		return this.dataCenterService.listResourceNetworkRouteTables(paramMap);
	}
	
	@RequestMapping(value = "/network/{nodeId}/routeTable", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "添加路由表", httpMethod = "POST", notes = "createResourceNetworkRouteTable", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String createResourceNetworkRouteTable(@PathVariable Integer nodeId, @RequestBody ResourceNetworkNodeRouteTableCreateBean resourceNetworkNodeRouteTableBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNetworkNodeRouteTableBean));
		paramMap.put("nodeId", nodeId);
		return this.dataCenterService.createResourceNetworkRouteTable(paramMap);
	}
	
	@RequestMapping(value = "/network/{nodeId}/routeTable/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑路由表", httpMethod = "PUT", notes = "updateResourceNetworkRouteTable", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "id", value = "路由id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String updateResourceNetworkRouteTable(@PathVariable Integer nodeId, @PathVariable Integer id, @RequestBody ResourceNetworkNodeRouteTableUpdateBean resourceNetworkNodeRouteTableBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNetworkNodeRouteTableBean));
		paramMap.put("nodeId", nodeId);
		paramMap.put("id", id);
		return this.dataCenterService.updateResourceNetworkRouteTable(paramMap);
	}
	
	@RequestMapping(value = "/network/{nodeId}/routeTables/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除路由表", httpMethod = "DELETE", notes = "deleteResourceNetworkRouteTable", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "网络资源id，<br/>例子：1", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "id", value = "端口id，<br/>例子：1", required = true, dataType = "integer", paramType = "path")
	})
	public String deleteResourceNetworkRouteTable(@PathVariable Integer nodeId, @PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("id", id);
		return this.dataCenterService.deleteResourceNetworkRouteTable(paramMap);
	}
	
	
	@RequestMapping(value = "/ipMap", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createResourceIpMap(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createResourceIpMap(paramMap);
	}

	@RequestMapping(value = "/ipMap", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceIpMap(
			@RequestParam(required = false, value = "sourceIp") String sourceIp,
			@RequestParam(required = false, value = "mapIp") String mapIp,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (sourceIp != null) {
			paramMap.put("sourceIp", sourceIp);
		}
		if (mapIp != null) {
			paramMap.put("mapIp", mapIp);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.dataCenterService.listResourceIpMap(paramMap);
	}

	@RequestMapping(value = "/ipMap/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteResourceIpMap(@PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.deleteResourceIpMap(paramMap);
	}

	@RequestMapping(value = "/ipMap", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateResourceIpMap(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.updateResourceIpMap(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源池节点", httpMethod = "GET", notes = "listResourceNodes", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "key", value = "查询关键字", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "vcpus", value = "cpu大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "memory", value = "内存大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "localDisk", value = "本地磁盘大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "datacenterId", value = "数据中心id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "poolId", value = "资源池id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "deployType", value = "自动化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddress", value = "业务地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddressList", value = "业务地址列表", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysMacAddress", value = "系统mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "macAddress", value = "mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "hostName", value = "主机名称", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "queryColumn", value = "查找列", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rules", value = "规则", required = false, dataType = "String", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "返回", required = false, dataType = "String", paramType = "query" ),
			@ApiImplicitParam(name = "searchValue", value = "查询值，<br/>例子：1", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listResourceNodes(
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "vcpus") Integer vcpus,
			@RequestParam(required = false, value = "memory") Integer memory,
			@RequestParam(required = false, value = "localDisk") Integer localDisk,
			@RequestParam(required = false, value = "virtualizationType") String virtualizationType,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "deployType") String deployType,
			@RequestParam String type,
			@RequestParam(required = false, value = "businessAddress") String businessAddress,
			@RequestParam(required = false, value = "businessAddressList") String businessAddressList,
			@RequestParam(required = false, value = "sysMacAddress") String sysMacAddress,
			@RequestParam(required = false, value = "macAddress") String macAddress,
			@RequestParam(required = false, value = "hostName") String hostName,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, value = "search[value]") String searchValue) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("type", type);

		if (null != searchValue && !searchValue.equals("")) {
			paramMap = JsonHelper.fromJson(HashMap.class, searchValue);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (deployType != null) {
			paramMap.put("deployType", deployType);
		}
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (vcpus != null) {
			paramMap.put("vcpus", vcpus);
		}
		if (memory != null) {
			paramMap.put("memory", memory);
		}
		if (localDisk != null) {
			paramMap.put("localDisk", localDisk);
		}
		if (datacenterId != null && datacenterId != -1) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (poolId != null && poolId != -1) {
			paramMap.put("poolId", poolId);
		}
		if (businessAddress != null) {
			paramMap.put("businessAddress", businessAddress);
		}
		if (queryColumn != null) {
			paramMap.put("queryColumn", queryColumn);
		}
		if (rules != null) {
			List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
			String[] ruleList = rules.split(",");
			for (String rule : ruleList) {
				HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
				String[] ri = rule.split("&");
				for (String r : ri) {
					String[] i = r.split("=");
					ruleItemMap.put(i[0], i[1]);
				}
				ruleItems.add(ruleItemMap);
			}
			paramMap.put("list", ruleItems);
		}
		if (businessAddressList != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : businessAddressList.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String businessAddressListString = StringUtils.join(addrList, ",");

			paramMap.put("businessAddressList", businessAddressListString);
		}
		if (sysMacAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : sysMacAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}
			String sysMacAddressString = StringUtils.join(addrList, ",");
			paramMap.put("sysMacAddress", sysMacAddressString);
		}
		if (macAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : macAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String macAddressString = StringUtils.join(addrList, ",");

			paramMap.put("macAddress", macAddressString);

		}
		if (hostName != null) {
			paramMap.put("hostName", hostName);
		}

		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}

		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}

		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.dataCenterService.listResourceNodes(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodesBase", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "listResourceNodesBase", httpMethod = "GET", notes = "listResourceNodesBase", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "用户名，例子：admin", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "subType", value = "subType", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "poolSubType", value = "poolSubType", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "datacenterId", value = "数据中心id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "poolId", value = "资源池id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "region", value = "region", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddress", value = "业务地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "name", required = false, dataType = "String", paramType = "query")
	})
	public String listResourceNodesBase(
			@RequestParam String loginId,
			@RequestParam String subType,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "poolSubType") String poolSubType,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "region") String region,
			@RequestParam(required = false, value = "businessAddress") String businessAddress,
			@RequestParam(required = false, value = "name") String name) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);
		paramMap.put("subType", subType);

		if (type != null) {
			paramMap.put("type", type);
		}
		if (datacenterId != null && datacenterId != -1) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (poolId != null && poolId != -1) {
			paramMap.put("poolId", poolId);
		}
		if (region != null) {
			paramMap.put("region", region);
		}
		if (businessAddress != null) {
			paramMap.put("businessAddress", businessAddress);
		}
		if (name != null) {
			paramMap.put("name", name);
		}
		if (poolSubType != null) {
			StringBuffer resultBuffer = new StringBuffer();
			resultBuffer.append("''");
			String[] array = poolSubType.split(",");
			for (String item : array) {
				resultBuffer.append(",'");
				resultBuffer.append(item);
				resultBuffer.append("'");
			}
			paramMap.put("poolSubType", resultBuffer.toString());
		}
		return this.dataCenterService.listResourceNodesBase(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodesBase/{id}/detail", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "listResourceNodesBaseDetail", httpMethod = "GET", notes = "listResourceNodesBaseDetail", response = String.class)
	public String listResourceNodesBaseDetail(@PathVariable Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);

		return this.dataCenterService.listResourceNodesBaseDetail(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源节点详情", httpMethod = "GET", notes = "listResourceNodeDetails", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "资源节点id", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "nodeType", value = "资源节点类型", required = false, dataType = "String", paramType = "query")
	})
	public String listResourceNodeDetails(@PathVariable String nodeId, @RequestParam String nodeType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("nodeType", nodeType);
		return this.dataCenterService.listResourceNodeDetails(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/physical", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询物理机资源节点", httpMethod = "GET", notes = "listResourcePhysicalNodes", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tagId", value = "标签id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "tagValue", value = "标签值", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "manufacturerCode", value = "厂家", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "systemType", value = "操作系统类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "key", value = "查询关键字", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "vcpus", value = "cpu大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "memory", value = "内存大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "localDisk", value = "本地磁盘大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "datacenterId", value = "数据中心id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "poolId", value = "资源池id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "nodeId", value = "资源id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "saltAgentStatus", value = "saltAgentStatus", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "deployType", value = "自动化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "hostName", value = "主机名称", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddress", value = "业务地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddressList", value = "业务地址列表", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysMacAddress", value = "系统mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "macAddress", value = "mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "queryColumn", value = "查找列", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rules", value = "规则", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "showMonitor", value = "显示监控", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sortDirection", value = "sortDirection", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sortGraphId", value = "sortGraphId", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "返回", required = false, dataType = "String", paramType = "query" ),
			@ApiImplicitParam(name = "searchValue", value = "查询值，<br/>例子：1", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10")
	})
	public String listResourcePhysicalNodes(
			@RequestParam(required = false, value = "tagId") Integer tagId,
			@RequestParam(required = false, value = "tagValue") String tagValue,
			@RequestParam(required = false, value = "manufacturerCode") String manufacturerCode,
			@RequestParam(required = false, value = "systemType") String systemType,
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "vcpus") Integer vcpus,
			@RequestParam(required = false, value = "memory") Integer memory,
			@RequestParam(required = false, value = "localDisk") Integer localDisk,
			@RequestParam(required = false, value = "virtualizationType") String virtualizationType,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "nodeId") String nodeId,
			@RequestParam(required = false, value = "saltAgentStatus") Integer saltAgentStatus,
			@RequestParam(required = false, value = "deployType") String deployType,
			@RequestParam(required = false, value = "hostName") String hostName,
			@RequestParam(required = false, value = "businessAddress") String businessAddress,
			@RequestParam(required = false, value = "businessAddressList") String businessAddressList,
			@RequestParam(required = false, value = "sysMacAddress") String sysMacAddress,
			@RequestParam(required = false, value = "macAddress") String macAddress,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam String type,
			@RequestParam(required = false, value = "showMonitor") String showMonitor,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "sortGraphId") String sortGraphId,
			@RequestParam(required = false, value = "sortDirection") String sortDirection,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, value = "search[value]") String searchValue) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (null != searchValue && !searchValue.equals("")) {
			paramMap = JsonHelper.fromJson(HashMap.class, searchValue);
		}
		if (type != null) {
			paramMap.put("type", type);
		}
		if (showMonitor != null) {
			paramMap.put("showMonitor", showMonitor);
		}
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
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
		if (tagId != null) {
			paramMap.put("tagId", tagId);
		}
		if (tagValue != null) {
			List<String> tagList = new ArrayList<String>();
			for (String value : tagValue.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(value.trim());
				stringBuffred.append("'");

				tagList.add(stringBuffred.toString());
			}

			String tagListString = StringUtils.join(tagList, ",");

			paramMap.put("tagValue", tagListString);
		}
		if (manufacturerCode != null) {
			paramMap.put("manufacturerCode", manufacturerCode);
		}
		if (systemType != null) {
			paramMap.put("systemType", systemType);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (deployType != null) {
			paramMap.put("deployType", deployType);
		}
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (vcpus != null) {
			paramMap.put("vcpus", vcpus);
		}
		if (memory != null) {
			paramMap.put("memory", memory);
		}
		if (localDisk != null) {
			paramMap.put("localDisk", localDisk);
		}
		if (datacenterId != null) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if (nodeId != null) {
			paramMap.put("nodeId", nodeId);
		}
		if (saltAgentStatus != null) {
			paramMap.put("saltAgentStatus", saltAgentStatus);
		}
		if (hostName != null) {
			paramMap.put("hostName", hostName);
		}
		if (businessAddress != null) {
			paramMap.put("businessAddress", businessAddress);
		}
		if (queryColumn != null) {
			paramMap.put("queryColumn", queryColumn);
		}
		if (rules != null) {
			List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
			String[] ruleList = rules.split(",");
			for (String rule : ruleList) {
				HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
				String[] ri = rule.split("&");
				for (String r : ri) {
					String[] i = r.split("=");
					ruleItemMap.put(i[0], i[1]);
				}
				ruleItems.add(ruleItemMap);
			}
			paramMap.put("list", ruleItems);
		}
		if (businessAddressList != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : businessAddressList.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String businessAddressListString = StringUtils.join(addrList, ",");

			paramMap.put("businessAddressList", businessAddressListString);
		}
		if (sysMacAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : sysMacAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}
			String sysMacAddressString = StringUtils.join(addrList, ",");
			paramMap.put("sysMacAddress", sysMacAddressString);
		}
		if (macAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : macAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String macAddressString = StringUtils.join(addrList, ",");

			paramMap.put("macAddress", macAddressString);

		}
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.dataCenterService.listResourcePhysicalNodes(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/physical/total", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "物理机资源节点统计", httpMethod = "GET", notes = "listResourcePhysicalNodesTotal", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tagId", value = "标签id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "tagValue", value = "标签值", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "key", value = "查询关键字", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "vcpus", value = "cpu大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "memory", value = "内存大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "localDisk", value = "本地磁盘大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "datacenterId", value = "数据中心id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "poolId", value = "资源池id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "nodeId", value = "资源id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "saltAgentStatus", value = "saltAgentStatus", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "deployType", value = "自动化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "hostName", value = "主机名称", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddress", value = "业务地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddressList", value = "业务地址列表", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysMacAddress", value = "系统mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "macAddress", value = "mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "queryColumn", value = "查找列", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rules", value = "规则", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "searchValue", value = "查询值，<br/>例子：1", required = false, dataType = "String", paramType = "query")
	})
	public String listResourcePhysicalNodesTotal(
			@RequestParam(required = false, value = "tagId") Integer tagId,
			@RequestParam(required = false, value = "tagValue") String tagValue,
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "vcpus") Integer vcpus,
			@RequestParam(required = false, value = "memory") Integer memory,
			@RequestParam(required = false, value = "localDisk") Integer localDisk,
			@RequestParam(required = false, value = "virtualizationType") String virtualizationType,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "nodeId") String nodeId,
			@RequestParam(required = false, value = "saltAgentStatus") Integer saltAgentStatus,
			@RequestParam(required = false, value = "deployType") String deployType,
			@RequestParam(required = false, value = "hostName") String hostName,
			@RequestParam(required = false, value = "businessAddress") String businessAddress,
			@RequestParam(required = false, value = "businessAddressList") String businessAddressList,
			@RequestParam(required = false, value = "sysMacAddress") String sysMacAddress,
			@RequestParam(required = false, value = "macAddress") String macAddress,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam String type,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "search[value]") String searchValue) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (null != searchValue && !searchValue.equals("")) {
			paramMap = JsonHelper.fromJson(HashMap.class, searchValue);
		}

		if (tagId != null) {
			paramMap.put("tagId", tagId);
		}
		if (tagValue != null) {
			List<String> tagList = new ArrayList<String>();
			for (String value : tagValue.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(value.trim());
				stringBuffred.append("'");

				tagList.add(stringBuffred.toString());
			}

			String tagListString = StringUtils.join(tagList, ",");

			paramMap.put("tagValue", tagListString);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (deployType != null) {
			paramMap.put("deployType", deployType);
		}
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (vcpus != null) {
			paramMap.put("vcpus", vcpus);
		}
		if (memory != null) {
			paramMap.put("memory", memory);
		}
		if (localDisk != null) {
			paramMap.put("localDisk", localDisk);
		}
		if (datacenterId != null) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if (nodeId != null) {
			paramMap.put("nodeId", nodeId);
		}
		if (saltAgentStatus != null) {
			paramMap.put("saltAgentStatus", saltAgentStatus);
		}
		if (hostName != null) {
			paramMap.put("hostName", hostName);
		}
		if (businessAddress != null) {
			paramMap.put("businessAddress", businessAddress);
		}
		if (queryColumn != null) {
			paramMap.put("queryColumn", queryColumn);
		}
		if (rules != null) {
			List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
			String[] ruleList = rules.split(",");
			for (String rule : ruleList) {
				HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
				String[] ri = rule.split("&");
				for (String r : ri) {
					String[] i = r.split("=");
					ruleItemMap.put(i[0], i[1]);
				}
				ruleItems.add(ruleItemMap);
			}
			paramMap.put("list", ruleItems);
		}
		if (businessAddressList != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : businessAddressList.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String businessAddressListString = StringUtils.join(addrList, ",");

			paramMap.put("businessAddressList", businessAddressListString);
		}
		if (sysMacAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : sysMacAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}
			String sysMacAddressString = StringUtils.join(addrList, ",");
			paramMap.put("sysMacAddress", sysMacAddressString);
		}
		if (macAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : macAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String macAddressString = StringUtils.join(addrList, ",");

			paramMap.put("macAddress", macAddressString);

		}
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		// parseRelationLoginIds(session, paramMap);

		paramMap.put("type", type);
		if (type.equals("MANAGE")) {//查询云平台资源管理中的 物理机
			paramMap.put("poolId", 0);
		}
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.dataCenterService.listResourcePhysicalNodesTotal(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/hypervisor", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询计算节点", httpMethod = "GET", notes = "listResourceHypervisorNodes", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "计算节点id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "tagId", value = "标签id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "tagValue", value = "标签值", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "manufacturerCode", value = "厂家", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "systemType", value = "操作系统类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "key", value = "查询关键字", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "vcpus", value = "cpu大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "memory", value = "内存大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "localDisk", value = "本地磁盘大小", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "datacenterId", value = "数据中心id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "poolId", value = "资源池id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "nodeId", value = "资源id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "regionType", value = "regionType", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "saltAgentStatus", value = "saltAgentStatus", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "deployType", value = "自动化类型", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "hostName", value = "主机名称", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddress", value = "业务地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "businessAddressList", value = "业务地址列表", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysMacAddress", value = "系统mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "macAddress", value = "mac地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "aggregateId", value = "主机聚集id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "systemSubTypeName", value = "systemSubTypeName", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "virtualCount", value = "virtualCount", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "osUserName", value = "系统用户名", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "osIpAddress", value = "系统ip地址", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "agentIds", value = "agentIds", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "zabbixAgentStatus", value = "zabbix状态", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "connectStatus", value = "连接状态", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "queryColumn", value = "查找列", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rules", value = "规则", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "showMonitor", value = "显示监控", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sortGraphId", value = "sortGraphId", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "draw", value = "返回", required = false, dataType = "String", paramType = "query" ),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "searchValue", value = "查询值，<br/>例子：1", required = false, dataType = "String", paramType = "query")
	})
	public String listResourceHypervisorNodes(
			@RequestParam(required = false, value = "id") Integer id,
			@RequestParam(required = false, value = "tagId") Integer tagId,
			@RequestParam(required = false, value = "tagValue") String tagValue,
			@RequestParam(required = false, value = "manufacturerCode") String manufacturerCode,
			@RequestParam(required = false, value = "systemType") String systemType,
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "vcpus") Integer vcpus,
			@RequestParam(required = false, value = "memory") Integer memory,
			@RequestParam(required = false, value = "localDisk") Integer localDisk,
			@RequestParam(required = false, value = "virtualizationType") String virtualizationType,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "nodeId") String nodeId,
			@RequestParam(required = false, value = "regionType") String regionType,
			@RequestParam(required = false, value = "saltAgentStatus") Integer saltAgentStatus,
			@RequestParam(required = false, value = "deployType") String deployType,
			@RequestParam(required = false, value = "hostName") String hostName,
			@RequestParam(required = false, value = "businessAddress") String businessAddress,
			@RequestParam(required = false, value = "businessAddressList") String businessAddressList,
			@RequestParam(required = false, value = "sysMacAddress") String sysMacAddress,
			@RequestParam(required = false, value = "macAddress") String macAddress,
			@RequestParam(required = false, value = "aggregateId") Integer aggregateId,
			@RequestParam(required = false, value = "systemSubTypeName") String systemSubTypeName,
			@RequestParam(required = false, value = "virtualCount") Integer virtualCount,
			@RequestParam(required = false, value = "osUserName") String osUserName,
			@RequestParam(required = false, value = "osIpAddress") String osIpAddress,
			@RequestParam(required = false, value = "agentIds") String agentIds,
			@RequestParam(required = false, value = "zabbixAgentStatus") Integer zabbixAgentStatus,
			@RequestParam(required = false, value = "connectStatus") Integer connectStatus,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam String type,
			@RequestParam(required = false, value = "showMonitor") String showMonitor,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "sortGraphId") String sortGraphId,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, value = "search[value]") String searchValue) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (null != searchValue && !searchValue.equals("")) {
			paramMap = JsonHelper.fromJson(HashMap.class, searchValue);
		}
		if (type != null) {
			paramMap.put("type", type);
		}
		if (showMonitor != null) {
			paramMap.put("showMonitor", showMonitor);
		}
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
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
		if (id != null) {
			paramMap.put("id", id);
		}

		if (tagId != null) {
			paramMap.put("tagId", tagId);
		}
		if (tagValue != null) {
			List<String> tagList = new ArrayList<String>();
			for (String value : tagValue.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(value.trim());
				stringBuffred.append("'");

				tagList.add(stringBuffred.toString());
			}

			String tagListString = StringUtils.join(tagList, ",");

			paramMap.put("tagValue", tagListString);
		}
		if (manufacturerCode != null) {
			paramMap.put("manufacturerCode", manufacturerCode);
		}
		if (systemType != null) {
			paramMap.put("systemType", systemType);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (deployType != null) {
			paramMap.put("deployType", deployType);
		}
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (vcpus != null) {
			paramMap.put("vcpus", vcpus);
		}
		if (memory != null) {
			paramMap.put("memory", memory);
		}
		if (localDisk != null) {
			paramMap.put("localDisk", localDisk);
		}
		if (datacenterId != null) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if (nodeId != null) {
			paramMap.put("nodeId", nodeId);
		}
		if (saltAgentStatus != null) {
			paramMap.put("saltAgentStatus", saltAgentStatus);
		}
		if (hostName != null) {
			paramMap.put("hostName", hostName);
		}
		if (businessAddress != null) {
			paramMap.put("businessAddress", businessAddress);
		}
		if (systemSubTypeName != null) {
			paramMap.put("systemSubTypeName", systemSubTypeName);
		}
		if (virtualCount != null) {
			paramMap.put("virtualCount", virtualCount);
		}
		if (aggregateId != null) {
			if (regionType == null || !regionType.equals("VMWARE")) {
				paramMap.put("aggregateId", aggregateId);
			}
		}
		if (osUserName != null) {
			paramMap.put("osUserName", osUserName);
		}
		if (osIpAddress != null) {
			paramMap.put("osIpAddress", osIpAddress);
		}
		if (agentIds != null) {
			List<String> agentIdList = new ArrayList<String>();
			for (String agentId : agentIds.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(agentId.trim());
				stringBuffred.append("'");

				agentIdList.add(stringBuffred.toString());
			}

			String agentIdListString = com.alibaba.dubbo.common.utils.StringUtils.join(agentIdList, ",");

			paramMap.put("agentIds", agentIdListString);
		}
		if (zabbixAgentStatus != null) {
			paramMap.put("zabbixAgentStatus", zabbixAgentStatus);
		}
		if (connectStatus != null) {
			paramMap.put("connectStatus", connectStatus);
		}
		if (queryColumn != null) {
			paramMap.put("queryColumn", queryColumn);
		}
		if (rules != null) {
			List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
			String[] ruleList = rules.split(",");
			for (String rule : ruleList) {
				HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
				String[] ri = rule.split("&");
				for (String r : ri) {
					String[] i = r.split("=");
					ruleItemMap.put(i[0], i[1]);
				}
				ruleItems.add(ruleItemMap);
			}
			paramMap.put("list", ruleItems);
		}

		if (businessAddressList != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : businessAddressList.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String businessAddressListString = StringUtils.join(addrList, ",");

			paramMap.put("businessAddressList", businessAddressListString);
		}
		if (sysMacAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : sysMacAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String sysMacAddressString = StringUtils.join(addrList, ",");

			paramMap.put("sysMacAddress", sysMacAddressString);
		}
		if (macAddress != null) {
			List<String> addrList = new ArrayList<String>();
			for (String addr : macAddress.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(addr.trim());
				stringBuffred.append("'");

				addrList.add(stringBuffred.toString());
			}

			String macAddressString = StringUtils.join(addrList, ",");

			paramMap.put("macAddress", macAddressString);
		}

		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		return this.dataCenterService.listResourceHypervisorNodes(paramMap);
	}


	@RequestMapping(value = "/hypervisor", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOpenstackHypervisors(
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, value = "search[value]") String searchValue) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (null != searchValue && !searchValue.equals("")) {
			paramMap = JsonHelper.fromJson(HashMap.class, searchValue);
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
		parseRelationLoginIds(paramMap);
		return this.dataCenterService.listOpenstackHypervisors(paramMap);
	}

	@RequestMapping(value = "/hypervisor/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOpenstackHypervisorsCount() {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);

		return this.dataCenterService.listOpenstackHypervisorsCount(paramMap);
	}

	@RequestMapping(value = "/hypervisor/delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteOpenstackHypervisor(@RequestBody HashMap<String, Object> paramMap) {

		return this.dataCenterService.deleteOpenstackHypervisor(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/power", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourcePowerNodes(
			@RequestParam(required = false, value = "vcpus") Integer vcpus,
			@RequestParam(required = false, value = "memory") Integer memory,
			@RequestParam(required = false, value = "virtualizationType") String virtualizationType,
			@RequestParam(required = false, value = "datacenterId") Integer datacenterId,
			@RequestParam(required = false, value = "poolId") Integer poolId,
			@RequestParam(required = false, value = "nodeId") String nodeId,
			@RequestParam(required = false, value = "hostName") String hostName,
			@RequestParam String type,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, value = "search[value]") String searchValue) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (null != searchValue && !searchValue.equals("")) {
			paramMap = JsonHelper.fromJson(HashMap.class, searchValue);
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
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (vcpus != null) {
			paramMap.put("vcpus", vcpus);
		}
		if (memory != null) {
			paramMap.put("memory", memory);
		}
		if (datacenterId != null) {
			paramMap.put("datacenterId", datacenterId);
		}
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if (nodeId != null) {
			paramMap.put("nodeId", nodeId);
		}
		if (hostName != null) {
			paramMap.put("hostName", hostName);
		}
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.dataCenterService.listResourcePowerNodes(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/physical/{nodeId}/interface", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourcePhysicalNodesInterface(@PathVariable String nodeId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		return this.dataCenterService.listResourcePhysicalNodesInterface(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/physical/{nodeId}/interface/{interfaceId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateResourcePhysicalNodesInterface(@PathVariable String nodeId, @PathVariable Integer interfaceId) {
		return this.dataCenterService.updateResourcePhysicalNodesInterface(nodeId, interfaceId);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/disk", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceNodeDisk(@PathVariable String nodeId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		return this.dataCenterService.listResourceNodeDisk(paramMap);
	}

	@RequestMapping(value = "/resourceNodes/group/filter", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceNodeGroupByFilter(@RequestParam(required = true, value = "key") String key) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseCurrentLoginIds(paramMap);
		paramMap.put("key", key);
		return this.dataCenterService.listResourceNodeGroupByFilter(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/validate", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String validateResourceNode(@RequestBody HashMap<String, Object> paramMap) {
		return this.dataCenterService.validateResourceNode(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value="添加资源", httpMethod = "POST", notes = "createResourceNode", response = String.class)
	public String createResourceNode(@RequestBody ResourceNodeCreateBean resourceNode) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNode));
		paramMap.put("nodeType", paramMap.get("sourceNodeType"));
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createResourceNode(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNode/unitPrice", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceNodeUnitPrice() {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("poolType", "COMPUTE");
		paramMap.put("type", "VIRTUAL");
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.dataCenterService.listResourceNodeUnitPrice(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNode/unitPrice", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createResourceNodeUnitPrice(@RequestBody HashMap<String, Object> paramMap) {
		return this.dataCenterService.createResourceNodeUnitPrice(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNode/unitPrice", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateResourceNodeUnitPrice(@RequestBody HashMap<String, Object> paramMap) {
		return this.dataCenterService.updateResourceNodeUnitPrice(paramMap);
	}

//	@RequestMapping(value = "/resourcePool/resourceNode/power", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String upResourceNodePower(@RequestBody HashMap<String, Object> paramMap) {
//		String ipmiIpAddress = (String) paramMap.get("ipAddress");
//		String ipmiUser = (String) paramMap.get("userName");
//		String ipmiPassword = (String) paramMap.get("password");
//		int ipmiPort = (int) paramMap.get("port");
//		String nodeName=(String)paramMap.get("nodeName");
//		String id = (String)paramMap.get("id");
//
//		String action = (String) paramMap.get("action");
//		if (action.equals("up")) {
//			UpResourceNodePowerRequest request = new UpResourceNodePowerRequest();
//			request.setIpAddress(ipmiIpAddress);
//			request.setUser(ipmiUser);
//			request.setPassword(ipmiPassword);
//			request.setPort(ipmiPort);
//			request.setNodeName(nodeName);
//
//			SysLogObj logObj = WebSysLogTool.generateLog(httpRequest);
//			logObj.setOPER_TYPE(ESysLogOperType.RESOURCE_OPERATION.name());
//			logObj.setOPER_SUB_TYPE(request.getEventType().name());
//			logObj.setOPER("启动资源节点");
//			logObj.setOPER_OBJ(id);
//			request.setLogObj(logObj);
//
//			String result = this.sendForResult(session,request);
//			logger.debug("insert upResourceNodePower successful! the result is : " + result);
//			return result;
//		} else if (action.equals("down")) {
//			DownResourceNodePowerRequest request = new DownResourceNodePowerRequest();
//			request.setIpAddress(ipmiIpAddress);
//			request.setUser(ipmiUser);
//			request.setPassword(ipmiPassword);
//			request.setPort(ipmiPort);
//			request.setNodeName(nodeName);
//
//			SysLogObj logObj = WebSysLogTool.generateLog(httpRequest);
//			logObj.setOPER_TYPE(ESysLogOperType.RESOURCE_OPERATION.name());
//			logObj.setOPER_SUB_TYPE(request.getEventType().name());
//			logObj.setOPER("关闭资源节点");
//			logObj.setOPER_OBJ(id);
//			request.setLogObj(logObj);
//
//			String result = this.sendForResult(session,request);
//			logger.debug("insert downResourceNodePowerRequest successful! the result is : " + result);
//			return result;
//		} else if (action.equals("reset")) {
//			ResetResourceNodePowerRequest request = new ResetResourceNodePowerRequest();
//			request.setIpAddress(ipmiIpAddress);
//			request.setUser(ipmiUser);
//			request.setPassword(ipmiPassword);
//			request.setPort(ipmiPort);
//			request.setNodeName(nodeName);
//
//			SysLogObj logObj = WebSysLogTool.generateLog(httpRequest);
//			logObj.setOPER_TYPE(ESysLogOperType.RESOURCE_OPERATION.name());
//			logObj.setOPER_SUB_TYPE(request.getEventType().name());
//			logObj.setOPER("重启资源节点");
//			logObj.setOPER_OBJ(id);
//			request.setLogObj(logObj);
//
//			String result = this.sendForResult(session,request);
//			logger.debug("insert resetResourceNodePowerRequest successful! the result is : " + result);
//			return result;
//		} else {
//			return invalidRequest("no action is confit");
//		}
//	}

	@RequestMapping(value = "/resourcePools/resourceNodes/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除资源", httpMethod = "DELETE", notes = "deleteResourceNode", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "资源ID", required = true, dataType = "string",  paramType = "path")
	})
	public String deleteResourceNode(@PathVariable String id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.deleteResourceNode(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑资源", httpMethod = "PUT", notes = "updateResourceNode", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID", required = true, dataType = "string",  paramType = "path")
	})
	public String updateResourceNode(@PathVariable String nodeId, @RequestBody ResourceNodeUpdateBean resourceNode) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNode));
		parseCurrentLoginIds(paramMap);
		paramMap.put("nodeType", paramMap.get("sourceNodeType"));
		paramMap.put("nodeId", nodeId);
		paramMap.put("id", nodeId);
		
		if(null != resourceNode.getName() && resourceNode.getName().equals("")){
			paramMap.put("name", "");
		}
		
		if(null != resourceNode.getHostName() && resourceNode.getHostName().equals("")){
			paramMap.put("hostName", "");
		}
		
		if(null != resourceNode.getUnitType() && resourceNode.getUnitType().equals("")){
			paramMap.put("unitType", "");
		}
		
		if(null != resourceNode.getSerialNumber() && resourceNode.getSerialNumber().equals("")){
			paramMap.put("serialNumber", "");
		}
		
		if(null != resourceNode.getOsUserName() && resourceNode.getOsUserName().equals("")){
			paramMap.put("osUserName", "");
		}
		
		if(null != resourceNode.getOsPassword() && resourceNode.getOsPassword().equals("")){
			paramMap.put("osPassword", "");
		}
		
		if(null != resourceNode.getMacAddress() && resourceNode.getMacAddress().equals("")){
			paramMap.put("macAddress", "");
		}
		
		if(null != resourceNode.getSystemType() && resourceNode.getSystemType().equals("")){
			paramMap.put("systemType", "");
		}
		
		if(null != resourceNode.getSystemSubType() && resourceNode.getSystemSubType().equals("")){
			paramMap.put("systemSubType", "");
		}
		
		if(null != resourceNode.getIpAddress() && resourceNode.getIpAddress().equals("")){
			paramMap.put("ipAddress", "");
		}
		
		if(null != resourceNode.getPort() && resourceNode.getPort().equals("")){
			paramMap.put("port", "");
		}
		
		if(null != resourceNode.getUserName() && resourceNode.getUserName().equals("")){
			paramMap.put("userName", "");
		}
		
		if(null != resourceNode.getPassword() && resourceNode.getPassword().equals("")){
			paramMap.put("password", "");
		}
		
		return this.dataCenterService.updateResourceNode(paramMap);
	}

	@RequestMapping(value = "/resourcePools/resourceNodes/systemInfo", method = RequestMethod.PUT)
	@ResponseBody
	public String updateResourceNodeSystemInfo(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.updateResourceNodeSystemInfo(paramMap);
	}

	@RequestMapping(value = "/resourcePools/services/{type}/{serviceType}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceServices(@PathVariable String type, @PathVariable String serviceType) {
		logger.debug("the query param :type = " + type);
		logger.debug("the query param :serviceType = " + serviceType);
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("type", type);
		paramMap.put("serviceType", serviceType);
		return this.dataCenterService.listResourceServices(paramMap);
	}

//	@RequestMapping(value = "/resourcePools/resourceNodes/virtualNodes/service", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String operateResourceNodeService(@RequestBody HashMap<String, Object> paramMap) {
//		String nodeId = (String) paramMap.get("nodeId");
//		String nodeType = (String) paramMap.get("nodeType");
//		String serviceKey = (String) paramMap.get("serviceKey");
//
//		if (nodeId == null || nodeType == null || serviceKey == null) {
//			return invalidRequest("the request body is not right,please check the request body!");
//		}
//		String action = (String) paramMap.get("action");
//
//		OSSMessageRequest request = null;
//		ServiceItem serviceItem = hostServiceCache.getServiceItem(nodeId, nodeType, serviceKey, action);
//
//		if (serviceItem != null) {
//			if (action.equals("start")) {
//				request = new StartHostServiceRequest();
//				((StartHostServiceRequest) request).setServiceItem(serviceItem);
//			} else if (action.equals("stop")) {
//				request = new StopHostServiceRequest();
//				((StopHostServiceRequest) request).setServiceItem(serviceItem);
//			} else if (action.equals("query")) {
//				request = new GetHostServiceStatusRequest();
//				((GetHostServiceStatusRequest) request).addServiceList(serviceItem);
//			}
//
//			if (request == null) {
//				return invalidRequest("the action is not in (start or stop or query).please check the input param!");
//			}
//			String result = super.sendForResult(session, request);
//			if (action.equals("query")) {
//				hostServiceCache.checkServiceItem(result);
//			}
//			return result;
//		} else {
//			return invalidRequest("the request body is not right,please check the request body!");
//		}
//	}

	@RequestMapping(value = "/availabilityZone/hypervisor", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listAvailabilityZoneHypervisorHosts(@RequestParam String regionName) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("regionName", regionName);
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.listAvailabilityZoneHypervisorHosts(paramMap);
	}

//	@RequestMapping(value = "/resourcePools/resourceNodes/virtualNodes/services/{type}/{serviceType}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String listResourceNodeServices(@RequestParam(required = false, name = "refresh") Boolean refresh, @PathVariable String type, @PathVariable String serviceType, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length, @RequestParam(required = false, value = "search[value]") String searchValue) {
//		if (refresh != null && refresh) {
//			DirectSyncRequest request = new DirectSyncRequest();
//			request.setSyncImplName(ESyncImplName.HOST_SERVICE);
//			super.sendForResult(request);
//		}
//
//		logger.debug("the query param : serviceType = " + serviceType);
//		HashMap<String, Object> serviceParamMap = new HashMap<>();
//		serviceParamMap.put("type", type);
//		serviceParamMap.put("serviceType", serviceType);
//		List<HashMap<String, Object>> serviceList = dbService.directSelect(DBServiceConst.SELECT_RESOURCE_SERVICES, serviceParamMap);
//		logger.debug("the service list is : " + JsonHelper.toJson(serviceList));
//
//		HashMap<String, Object> nodeParamMap = new HashMap<>();
//		parseRelationLoginIds(session, nodeParamMap);
//		HashMap<String, Object> nodeResultMap = getResourceVirtualNodeByPage(type, serviceType, null, null, nodeParamMap);
//		List<HashMap<String, Object>> nodeList = (List<HashMap<String, Object>>) nodeResultMap.get("record");
//
//		StringBuffer physicalNodeIdsBuffer = new StringBuffer();
//		StringBuffer virtualNodeIdsBuffer = new StringBuffer();
//		for (int i = 0; i < nodeList.size(); i++) {
//			HashMap<String, Object> nodeItemMap = nodeList.get(i);
//			String nodeType = (String) nodeItemMap.get("nodeType");
//			if (nodeType.equals("PHYSICAL")) {
//				physicalNodeIdsBuffer.append(",");
//				physicalNodeIdsBuffer.append(nodeItemMap.get("id"));
//			} else {
//				virtualNodeIdsBuffer.append(",");
//				virtualNodeIdsBuffer.append(nodeItemMap.get("id"));
//			}
//		}
//
//		logger.debug("physicalNodeIds is :" + physicalNodeIdsBuffer.toString());
//		logger.debug("virtualNodeIds is :" + virtualNodeIdsBuffer.toString());
//
//		HashMap<String, Object> nodeServiceParamMap = new HashMap<>();
//		String physicalNodeIds = physicalNodeIdsBuffer.toString();
//		String virtualNodeIds = virtualNodeIdsBuffer.toString();
//		if (physicalNodeIds.length() > 0) {
//			physicalNodeIds = physicalNodeIds.substring(1);
//			nodeServiceParamMap.put("physicalNodeIds", physicalNodeIds);
//		}
//		if (virtualNodeIds.length() > 0) {
//			virtualNodeIds = virtualNodeIds.substring(1);
//			nodeServiceParamMap.put("virtualNodeIds", virtualNodeIds);
//		}
//		nodeServiceParamMap.put("type", type);
//
//		List<HashMap<String, Object>> nodeServiceList = dbService.directSelect(DBServiceConst.SELECT_RESOURCE_NODE_SERVICES, nodeServiceParamMap);
//		HashMap<String, HashMap<String, Object>> nodeServiceMap = CommonUtil.parseListToHashMap(nodeServiceList, "hostName", "serviceId");
//
//		for (int i = nodeList.size() - 1; i >= 0; i--) {
//			HashMap<String, Object> nodeItemMap = nodeList.get(i);
//			String nodeHost = (String) nodeItemMap.get("hostName");
//			for (int j = 0; j < serviceList.size(); j++) {
//				HashMap<String, Object> serviceItemMap = serviceList.get(j);
//				String serviceKey = (String) serviceItemMap.get("key");
//				Integer serviceId = (Integer) serviceItemMap.get("id");
//				String nodeServiceKey = "_" + nodeHost + "_" + serviceId;
//				if (nodeServiceMap.containsKey(nodeServiceKey)) {
//					HashMap<String, Object> nodeServiceItemMap = nodeServiceMap.get(nodeServiceKey);
//					nodeItemMap.put(serviceKey, nodeServiceItemMap.get("serviceStatus"));
//				} else {
//					nodeItemMap.put(serviceKey, "UNSUPPORT");
//				}
//			}
//			if (!nodeItemMap.containsValue("OFF") && !nodeItemMap.containsValue("ON") && !nodeItemMap.containsValue("UNREACH")) {
//				nodeList.remove(i);
//			}
//		}
//
//		// dataTables需要将接收到的draw直接返回
//		nodeResultMap.put("draw", draw);
//
//		String result = JsonHelper.toJson(nodeResultMap);
//		logger.debug("query listResourceNodeServices successful! the result is : " + result);
//		return result;
//	}

	@RequestMapping(value = "/resourcePools/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String resourcePoolCount(@RequestParam(required = false, value = "poolId") Integer poolId, @RequestParam(required = false, value = "poolType") String poolType) {
		HashMap<String, Object> paramMap = new HashMap<>();

		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		// parseRelationLoginIds(session, paramMap);

		return this.dataCenterService.resourcePoolCount(paramMap);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dataCenterCount(
			@RequestParam(required = false, value = "dataCenterId") Integer dataCenterId,
			@RequestParam(required = false, value = "poolType") String poolType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (dataCenterId != null) {
			paramMap.put("dataCenterId", dataCenterId);
		}
		// parseRelationLoginIds(session, paramMap);

		HashMap<String, Object> resultMap = null;
		if (poolType.equals("COMPUTE")) {
			resultMap = dbService.select(DBServiceConst.DATACENTER_COMPUTE_COUNT, paramMap);
		} else if (poolType.equals("STORAGE")) {
			resultMap = dbService.select(DBServiceConst.DATACENTER_STORAGE_COUNT, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query dataCenterCount successful! the result is :" + result);
		return result;
	}

//	@RequestMapping(value = "/resourcePools/resourceNodes/info", method = RequestMethod.GET)
//	@ResponseBody
//	public String getResourceNodeInfo(@RequestParam String ipAddress, @RequestParam String userName, @RequestParam String password, @RequestParam String systemType) {
//		GetHostNodeInfoRequest request = new GetHostNodeInfoRequest();
//		request.setIpAddress(ipAddress);
//		request.setUserName(userName);
//		request.setPassword(password);
//		request.setSystemType(ESystemType.valueOf(systemType));
//		String result = this.sendForResult(request);
//		logger.debug("deploy getResourceNodeInfo successful! the result is : " + result);
//		return result;
//	}

	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/systemInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getResourceNodeSystemInfo(@PathVariable String nodeId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("type", "PHYSICAL");
		return this.dataCenterService.getResourceNodeSystemInfo(paramMap);


//		logger.debug("deploy getResourceNodeInfo successful! the result is : " + result);
//		return result;
	}

//	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/deploy/manageAgent", method = RequestMethod.POST)
//	@ResponseBody
//	public String deployResouceNodeManageAgent(@PathVariable String nodeId, @RequestParam String ipAddress, @RequestParam String userName, @RequestParam String password, @RequestParam String systemType) {
//		DeployHostManageAgentRequest request = new DeployHostManageAgentRequest();
//		request.setHostId(nodeId);
//		request.setIpAddress(ipAddress);
//		request.setUserName(userName);
//		request.setPassword(password);
//		request.setSystemType(ESystemType.valueOf(systemType));
//		String result = this.sendForResult(request);
//		logger.debug("deploy deployResouceNodeManageAgent successful! the result is : " + result);
//		return result;
//	}

//	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/uninstall/manageAgent", method = RequestMethod.POST)
//	@ResponseBody
//	public String uninstallResouceNodeManageAgent(@PathVariable String nodeId, @RequestParam String ipAddress, @RequestParam String userName, @RequestParam String password, @RequestParam String systemType) {
//		UnDeployHostManageAgentRequest request = new UnDeployHostManageAgentRequest();
//		request.setNodeId(nodeId);
//		request.setIpAddress(ipAddress);
//		request.setUserName(userName);
//		request.setPassword(password);
//		request.setSystemType(ESystemType.valueOf(systemType));
//		String result = this.sendForResult(request);
//		logger.debug("deploy uninstallResouceNodeManageAgent successful! the result is : " + result);
//		return result;
//	}

//	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/recycle", method = RequestMethod.PUT)
//	@ResponseBody
//	public String recycleResouceNode(@PathVariable String nodeId, @RequestParam String dataCenterId) {
//		HashMap<String,Object>paramMap = new HashMap<String,Object>();
//		paramMap.put("dataCenterId", dataCenterId);
//		HashMap<String,Object>poolMap= (HashMap<String, Object>) dbService.selectOne(DBServiceConst.SELECT_BARE_RESOURCE_POOLS, paramMap);
//
//		AbortOsDeployTaskRequest abortRequest = new AbortOsDeployTaskRequest();
//		abortRequest.setNodeId(Integer.parseInt(nodeId));
//		this.sendForResult(abortRequest);
//
//		paramMap.put("id", nodeId);
//		paramMap.put("nodeId", nodeId);
//		paramMap.put("poolId", poolMap.get("id"));
//		paramMap.put("subType", "BARE");
//
//		dbService.update(DBServiceConst.UPDATE_RN_BASE, paramMap);
////		dbService.update(DBServiceConst.insert_, paramMap);
//
//		HashMap<String, Object> resultMap = new HashMap<>();
//		resultMap.put("messageStatus", "END");
//		String result = JsonHelper.toJson(resultMap);
//
//		logger.debug("update recycleResouceNode tag successful! ");
//
//		return result;
//	}

	
	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/os/file", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源操作系统关键配置文件", httpMethod = "GET", notes = "listResourceNodeOsFile", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：160705451", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：PHYSICAL/VIR_INSTANCE/VIRTUAL", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listResourceNodeOsFile(
			@PathVariable Integer nodeId,
			@RequestParam(required = true, name = "sourceNodeType") String sourceNodeType, 
			@RequestParam(required = false, name = "start") String start, 
			@RequestParam(required = false, name = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("type", "OS_PROFILE");
		
		if(sourceNodeType != null){
			paramMap.put("nodeType", sourceNodeType);
		}
		
		if(start != null){
			paramMap.put("start", start);
		}
		
		if(length != null){
			paramMap.put("length", length);
		}
		
		return this.dataCenterService.listResourceNodeOsEnvironment(paramMap);
	}
	
	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/os/file/{itemId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源操作系统关键配置文件详细信息", httpMethod = "GET", notes = "listResourceNodeOsFileDetails", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：160705451", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "itemId", value = "配置文件ID，<br/>例子：30", required = true, dataType = "integer", paramType = "path")})
	public String listResourceNodeOsFileDetails(@PathVariable Integer nodeId, @PathVariable Integer itemId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("itemId", itemId);
		
		return this.dataCenterService.listResourceNodeOsEnvironmentDetails(paramMap);
	}
	
	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/os/params", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源操作系统关键参数", httpMethod = "GET", notes = "listResourceNodeOsParams", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：160705451", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：PHYSICAL/VIRTUAL/VIR_INSTANCE", required = true, dataType = "string",  paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listResourceNodeOsParams(
			@PathVariable Integer nodeId,
			@RequestParam(required = true, name = "sourceNodeType") String sourceNodeType, 
			@RequestParam(required = false, name = "start") String start, 
			@RequestParam(required = false, name = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("type", "OS_PARAMS");
		
		if(sourceNodeType != null){
			paramMap.put("nodeType", sourceNodeType);
		}
		
		if(start != null){
			paramMap.put("start", start);
		}
		
		if(length != null){
			paramMap.put("length", length);
		}
		
		return this.dataCenterService.listResourceNodeOsEnvironment(paramMap);
	}
	
	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/os/params/{itemId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源操作系统关键参数详细信息", httpMethod = "GET", notes = "listResourceNodeOsParamsDetails", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：160705451", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "itemId", value = "参数ID，<br/>例子：4", required = true, dataType = "integer", paramType = "path")})
	public String listResourceNodeOsParamsDetails(@PathVariable Integer nodeId, @PathVariable Integer itemId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("itemId", itemId);
		
		return this.dataCenterService.listResourceNodeOsEnvironmentDetails(paramMap);
	}
	
	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/logicalVolume", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源逻辑卷", httpMethod = "GET", notes = "listResourceNodeLogicalVolume", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：160705451", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：PHYSICAL/VIRTUAL", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listResourceNodeLogicalVolume(
			@PathVariable Integer nodeId,
			@RequestParam(required = true, name = "sourceNodeType") String sourceNodeType, 
			@RequestParam(required = false, name = "start") String start, 
			@RequestParam(required = false, name = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("type", "LOGICAL_VOLUME");
		
		if(sourceNodeType != null){
			paramMap.put("nodeType", sourceNodeType);
		}
		
		if(start != null){
			paramMap.put("start", start);
		}
		
		if(length != null){
			paramMap.put("length", length);
		}
		
		return this.dataCenterService.listResourceNodeOsEnvironment(paramMap);
	}
	
	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/fileSystem", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源文件系统配置", httpMethod = "GET", notes = "listResourceNodeFileSystem", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：160705451", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：PHYSICAL/VIRTUAL", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listResourceNodeFileSystem(
			@PathVariable Integer nodeId,
			@RequestParam(required = true, name = "sourceNodeType") String sourceNodeType, 
			@RequestParam(required = false, name = "start") String start, 
			@RequestParam(required = false, name = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("type", "FILE_SYSTEM");
		
		if(sourceNodeType != null){
			paramMap.put("nodeType", sourceNodeType);
		}
		
		if(start != null){
			paramMap.put("start", start);
		}
		
		if(length != null){
			paramMap.put("length", length);
		}
		
		return this.dataCenterService.listResourceNodeOsEnvironment(paramMap);
	}
	
	@RequestMapping(value = "/resourcePools/resourceNodes/{nodeId}/datastore", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源Datastore配置信息", httpMethod = "GET", notes = "listResourceNodeFileSystem", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：727534081", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：VIRTUAL/VIR_INSTANCE", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listResourceNodeDatastore(
			@PathVariable Integer nodeId,
			@RequestParam(required = true, name = "sourceNodeType") String sourceNodeType, 
			@RequestParam(required = false, name = "start") String start, 
			@RequestParam(required = false, name = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		
		if(sourceNodeType != null){
			paramMap.put("nodeType", sourceNodeType);
		}
		
		if(start != null){
			paramMap.put("start", start);
		}
		
		if(length != null){
			paramMap.put("length", length);
		}
		
		return this.dataCenterService.listResourceNodeDatastore(paramMap);
	}
	
	
	@RequestMapping(value = "/resourceGroup", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建资源组", httpMethod = "POST", notes = "createResourceGroupAction", response = String.class)
	public String createResourceGroupAction(@RequestBody @ApiParam(name="resourceGroup", value="创建资源组对象", required=true)ResourceGroupCreateBean resourceGroup) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceGroup));
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.createResourceGroup(paramMap);
	}
	
	@RequestMapping(value = "/resourceGroup/resourceNode/relation/{id}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置资源组与资源关系", httpMethod = "POST", notes = "updateResourceGroupResourceNodeRelation", response = String.class)
	public String updateResourceGroupResourceNodeRelation(
			@PathVariable @ApiParam(name="id",value="资源组ID", required=true) Integer id,
			@RequestBody @ApiParam(name="resourceGroupResourceNode", value="设置资源组与资源关系", required=true)ResourceGroupResourceNodeUpdateBean resourceGroupResourceNode) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceGroupResourceNode));
		paramMap.put("groupId", id);
		if(resourceGroupResourceNode.getResourceIds().size() == 0){
			paramMap.put("resourceIds", new ArrayList<String>());
		}
		return this.dataCenterService.updateResourceGroupResourceNodeRelation(paramMap);
	}
	
	@RequestMapping(value = "/resourceGroup/resourceNode/relation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置资源与资源组关系", httpMethod = "POST", notes = "updateResourceNodeResourceGroupRelation", response = String.class)
	public String updateResourceNodeResourceGroupRelation(
			@RequestBody @ApiParam(name="resourceNodeResourceGroup", value="设置资源组与资源关系", required=true)ResourceNodeResourceGroupUpdateBean resourceNodeResourceGroup) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNodeResourceGroup));
		if(resourceNodeResourceGroup.getGroupIds().size() == 0){
			paramMap.put("groupIds", new ArrayList<String> ());
		}
		return this.dataCenterService.updateResourceGroupResourceNodeRelation(paramMap);
	}
	
	@RequestMapping(value = "/resourceNode/{id}/resourceGroup/relation", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源与资源组关系", httpMethod = "GET", notes = "listResourceNodeResourceGroupRelation", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "资源ID", required = true, dataType = "string",  paramType = "path")
	})
	public String listResourceNodeResourceGroupRelation(@PathVariable String id){
		HashMap<String, Object> paramMap  = new HashMap<>();
		paramMap.put("resourceId", id);
		return this.dataCenterService.listResourceNodeResourceGroupRelation(paramMap);
	}
	
	@RequestMapping(value = "/resourceGroup/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑资源组", httpMethod = "PUT", notes = "updateResourceGroupAction", response = String.class)
	public String updateResourceGroupAction(
			@PathVariable @ApiParam(name="id",value="资源组ID", required=true) Integer id,
			@RequestBody @ApiParam(name="resourceGroup", value="编辑资源组对象", required=true)ResourceGroupUpdateBean resourceGroup) {
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceGroup));
		parseCurrentLoginIds(paramMap);
		paramMap.put("id", id);
		return this.dataCenterService.updateResourceGroup(paramMap);
	}
	
	@RequestMapping(value = "/resourceGroup/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除资源组", httpMethod = "DELETE", notes = "deleteResourceGroupAction", response = String.class)
	public String deleteResourceGroupAction(@PathVariable @ApiParam(name="id",value="资源组ID", required=true) Integer id){
		HashMap<String, Object> paramMap  = new HashMap<String, Object>();
		paramMap.put("id", id);
		return this.dataCenterService.deleteResourceGroup(paramMap);
	}

	@RequestMapping(value = "/resourceGroup", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源组", httpMethod = "GET", notes = "listResourceGroupAction", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "资源组名称模糊查询", required = false, dataType = "String",  paramType = "query", defaultValue = ""),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listResourceGroupAction(
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap  = new HashMap<>();
		if(null != name){
			paramMap.put("name",name);
		}
		if(null != start){
			paramMap.put("start",start);
		}
		if(null != length){
			paramMap.put("length",length);
		}
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.listResourceGroup(paramMap);
	}

	@RequestMapping(value = "/resourceGroup/{groupId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "获取资源组资源", httpMethod = "GET", notes = "listResourceGroupDetail", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "groupId", value = "资源组id，<br/>例子：27", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
			})
	public String listResourceGroupDetail(
			@PathVariable String groupId,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap  = new HashMap<>();
		if(null != groupId){
			paramMap.put("id",groupId);
		}
		if(null != start){
			paramMap.put("start",start);
		}
		if(null != length){
			paramMap.put("length",length);
		}
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.listResourceGroupDetail(paramMap);
	}

	@RequestMapping(value = "/user/resource/detail", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "获取用户所拥有的资源权限", httpMethod = "GET", notes = "listResourceUserDetail", response = String.class)
	public String listResourceUserDetail() {
		HashMap<String, Object> paramMap  = new HashMap<>();
		parseCurrentLoginIds(paramMap);
		return this.dataCenterService.listResourceUserDetail(paramMap);
	}
	
	@RequestMapping(value = "/hmc", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listHMC(@RequestParam(required = false, name = "id") Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (id != null) {
			paramMap.put("id", id);
		}
		return this.dataCenterService.listHMC(paramMap);
	}
	
	/********************************************************************************************************************************************************************************************/
	/********************************************************************************************************************************************************************************************/
	/****************************************** 性能监控模块 *************************************************************************************************************************************/
	/********************************************************************************************************************************************************************************************/
	/********************************************************************************************************************************************************************************************/
	
	@RequestMapping(value = "/monitor/host", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机监控", httpMethod = "GET", notes = "listMonitorHost", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "key", value = "关键字", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "ipAddress", value = "IP地址", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listMonitorHost(
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "ipAddress") String ipAddress,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("loginId", getCurrentLoginId());
		
		if(key != null) {
			paramMap.put("key", key);
		}
		
		if(ipAddress != null) {
			paramMap.put("ipAddress", ipAddress);
		}
		
		if (start != null) {
			paramMap.put("start", Integer.parseInt(start));
		}
		
		if (length != null) {
			paramMap.put("length", Integer.parseInt(length));
		}
		return this.dataCenterService.listMonitorHost(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host/{id}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机监控详情", httpMethod = "GET", notes = "listMonitorHostDetails", response = String.class)
	public String listMonitorHostDetails(
			@PathVariable @ApiParam(required = true, value = "id") Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		
		return this.dataCenterService.listMonitorHost(paramMap);
	}
	
	@RequestMapping(value = "/monitor/{hostId}/host/info", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机所有信息", httpMethod = "GET", notes = "listMonitorHostDetails", response = String.class)
	public String listMonitorHostInfo(@PathVariable @ApiParam(required = true, value = "hostId") Integer hostId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("hostId", hostId);
		
		return this.dataCenterService.listMonitorHostInfo(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建主机监控", httpMethod = "POST", notes = "createMonitorHost", response = String.class)
	public String createMonitorHost(@RequestBody MonitorHostCreateBean monitorHostCreateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorHostCreateBean));
		return this.dataCenterService.createMonitorHost(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑主机监控", httpMethod = "PUT", notes = "updateMonitorHost", response = String.class)
	public String updateMonitorHost(@PathVariable @ApiParam(name = "id", required = true)Integer id, @RequestBody MonitorHostUpdateBean monitorHostUpdateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorHostUpdateBean));
		paramMap.put("id", id);
		return this.dataCenterService.updateMonitorHost(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除主机监控", httpMethod = "DELETE", notes = "deleteMonitorHostDetails", response = String.class)
	public String deleteMonitorHost(@PathVariable @ApiParam(value = "id", required = true) Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		
		return this.dataCenterService.deleteMonitorHost(paramMap);
	}
	
	@RequestMapping(value = "/log/search", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "日志检索", httpMethod = "GET", notes = "listMonitorHostDetails", response = String.class)
	public String listSysLog(
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "startTime") Long startTime,
			@RequestParam(required = false, value = "endTime") Long endTime,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (key != null) {
			paramMap.put("key", key);
		}else {
			paramMap.put("key", "");
		}
		
		if (startTime != null) {
			paramMap.put("startTime", startTime);
		}else {
			paramMap.put("startTime", 0);
		}
		
		if (endTime != null) {
			paramMap.put("endTime", endTime);
		}else {
			paramMap.put("endTime", 0);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		return this.dataCenterService.listSysLog(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host/{id}/process", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机进程监控", httpMethod = "GET", notes = "listMonitorHost", response = String.class)
	public String listMonitorHostProcess(
			@PathVariable @ApiParam(name = "id", required = true)Integer id, 
			@RequestParam(required = true, value = "monitorHostId") String monitorHostId, 
			@RequestParam(required = false, value = "isGlobal") Boolean isGlobal) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("nodeId", id);
		paramMap.put("monitorHostId", monitorHostId);
		
		if(isGlobal != null) {
			paramMap.put("isGlobal", isGlobal);
		}
		
		return this.dataCenterService.listMonitorHostProcess(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host/{id}/filesystem", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机文件系统监控", httpMethod = "GET", notes = "listMonitorHostFilesystem", response = String.class)
	public String listMonitorHostFilesystem(
			@PathVariable @ApiParam(name = "id", required = true)Integer id, 
			@RequestParam(required = true, value = "monitorHostId") String monitorHostId,
			@RequestParam(required = false, value = "isGlobal") Boolean isGlobal) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("nodeId", id);
		paramMap.put("monitorHostId", monitorHostId);
		
		if(isGlobal != null) {
			paramMap.put("isGlobal", isGlobal);
		}
		
		return this.dataCenterService.listMonitorHostFilesystem(paramMap);
	}
	
	@RequestMapping(value = "/monitor/process", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "添加进程监控", httpMethod = "POST", notes = "createMonitorProcess", response = String.class)
	public String createMonitorProcess(@RequestBody MonitorProcessCreateBean monitorProcessCreateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorProcessCreateBean));
		return this.dataCenterService.createMonitorProcess(paramMap);
	}
	
	@RequestMapping(value = "/monitor/process/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除进程监控", httpMethod = "DELETE", notes = "deleteMonitorProcess", response = String.class)
	public String deleteMonitorProcess(@PathVariable @ApiParam(value = "id", required = true) Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		
		return this.dataCenterService.deleteMonitorProcess(paramMap);
	}
	
	
	@RequestMapping(value = "/monitor/filesystem", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "添加系统文件系统",httpMethod = "POST", notes = "createMonitorfilesystem", response = String.class)
	public String createMonitorfilesystem(@RequestBody MonitorHostFilesystemCreateBean monitorHostFilesystemCreateBean){
		 HashMap<String,Object> paramMap =JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorHostFilesystemCreateBean));
		  
	    return this.dataCenterService.createMonitorfilesystem(paramMap);	
		
	}

	@RequestMapping(value = "/monitor/filesystem/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除系统文件系统",httpMethod = "DELETE", notes = "deleteMonitorfilesystem", response = String.class)
	public String deleteMonitorfilesystem(@PathVariable @ApiParam(value = "id",required = true) Integer id){
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id",id);
		
		return this.dataCenterService.deleteMonitorfilesystem(paramMap);
		
	}
	
	
	@RequestMapping(value = "/monitor/process", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "进程查询", httpMethod = "GET", notes = "listMonitorProcesGroup", response = String.class)
    public String listMonitorProcesGroup() {
		
		 HashMap<String, Object> paramMap = new HashMap<>(); 
		 
		 return this.dataCenterService.listMonitorProcesGroup(paramMap);
	
	}

	@RequestMapping(value = "/monitor/host/{id}/cpu/usage", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机CPU利用率", httpMethod = "GET", notes = "listMonitorHostCpuUsage", response = String.class)

    

	public String listMonitorHostCpuUsage(@PathVariable @ApiParam(name = "id", required = true)Integer id, @RequestParam(required = true, value = "monitorHostId") String monitorHostId) {

		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("nodeId", id);
		paramMap.put("monitorHostId", monitorHostId);
		
		return this.dataCenterService.listMonitorHostCpuUsage(paramMap);
	}

	
	// 新增告警对象内容， 从页面获取选择参数
	@RequestMapping(value = "/monitor/notifaction", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "添加告警信息", httpMethod = "POST", notes = "createMonitornotification", response = String.class)
	public  String  creatMonitornotification(
			@RequestParam(required = true, value = "triggerId") Integer triggerId,
			@RequestParam(required = true, value = "notificationType") String notificationType,
			@RequestParam(required = false, value = "remark") String remark
			) {
		HashMap<String, Object> paramMap = new HashMap<>(); 
		if (triggerId != null) {
			paramMap.put("triggerId", triggerId);
		}
		if (notificationType != null) {
			if( notificationType.equals("URL_CALLBACK")){
			  if(remark != null) {
			paramMap.put("remark", remark);
			}
			else {
				paramMap.put("remark", "www.daidu.com");
			 }
			}
			paramMap.put("notificationType", notificationType);
		}
		return this.dataCenterService.creatMonitornotification(paramMap);
	}
	
	// 修改告警选项
	@RequestMapping(value = "/monitor/notifaction", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "修改告警信息", httpMethod = "PUT", notes = "updateMonitornotification", response = String.class)
	public String updateMonitiorEventnotification(
			@RequestParam(required = true, value = "triggerId") Integer triggerId,
			@RequestParam(required = true, value = "notificationType") String notificationType,
			@RequestParam(required = false, value = "remark") String remark
			){
		HashMap<String, Object> paramMap = new HashMap<>(); 
		if (triggerId != null) {
			paramMap.put("triggerId", triggerId);
		}
		if (notificationType != null) {
			if( notificationType.equals("URL_CALLBACK")){
			  if(remark != null) {
			paramMap.put("remark", remark);
			}
			else {
				paramMap.put("remark", "www.daidu.com");
			 }
			}
			paramMap.put("notificationType", notificationType);
		}
		return this.dataCenterService.updateMonitornotification(paramMap);
		
	}
	
	 //删除告警信息
	@RequestMapping(value = "/monitor/notification", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除告警信息",httpMethod = "DELETE", notes = "deleteMonitorEventNotification", response = String.class)
	public String  deleteMonitorEvenNotification(@RequestParam(required = true, value = "triggerId") Integer triggerId) {
		
		HashMap<String, Object> paramMap = new HashMap<>(); 
		if (triggerId != null) {
			paramMap.put("triggerId", triggerId);
		}
		return this.dataCenterService.deleteMonitornotification(paramMap);
		
	}
	
	//查询告警信息
	@RequestMapping(value = "/monitor/notification", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "告警查询", httpMethod = "GET", notes = "listMonitorEventNotification", response = String.class)
	public String listMonitorEventNotification(@RequestParam(required = true, value = "triggerId") Integer triggerId) {
		
		HashMap<String, Object> paramMap = new HashMap<>(); 
		 paramMap.put("triggerId", triggerId);
		return this.dataCenterService.listMonitorEventNotification(paramMap);
	
	}
	
	//查询告警通知记录
	@RequestMapping(value = "/monitor/alarm", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询告警通知记录", httpMethod = "GET", notes = "listMonitorNotification", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "keyWord", value = "关键字", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "notificationType", value = "通知类型", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorNotification(
			@RequestParam(required = false, name = "keyWord") String keyWord,
			@RequestParam(required = false, name = "notificationType") String notificationType,
			@RequestParam(required = false, name = "start") String start,
			@RequestParam(required = false, name = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (keyWord != null) {
			paramMap.put("keyWord", keyWord);
		}
		if (notificationType != null) {
			paramMap.put("notificationType", keyWord);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());

		String result = this.dataCenterService.listMonitorNotification(paramMap);
		logger.debug("query listMonitorNotification  successful! the result is :" + result);
		return result;
	}
	
	
	@RequestMapping(value = "/monitor/host/{id}/ram/usage", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机内存利用率", httpMethod = "GET", notes = "listMonitorHostRamUsage", response = String.class)
	public String listMonitorHostRamUsage(@PathVariable @ApiParam(name = "id", required = true)Integer id, @RequestParam(required = true, value = "monitorHostId") String monitorHostId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("nodeId", id);
		paramMap.put("monitorHostId", monitorHostId);
		
		return this.dataCenterService.listMonitorHostRamUsage(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host/{id}/network/usage", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机网络流量", httpMethod = "GET", notes = "listMonitorHostNetworkUsage", response = String.class)
	public String listMonitorHostNetworkUsage(@PathVariable @ApiParam(name = "id", required = true)Integer id, @RequestParam(required = true, value = "monitorHostId") String monitorHostId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("nodeId", id);
		paramMap.put("monitorHostId", monitorHostId);
		
		return this.dataCenterService.listMonitorHostNetworkUsage(paramMap);
	}
	
	@RequestMapping(value = "/monitor/host/{id}/disk/usage", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机磁盘读写速率", httpMethod = "GET", notes = "listMonitorHostDiskUsage", response = String.class)
	public String listMonitorHostDiskUsage(@PathVariable @ApiParam(name = "id", required = true)Integer id, @RequestParam(required = true, value = "monitorHostId") String monitorHostId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("nodeId", id);
		paramMap.put("monitorHostId", monitorHostId);
		
		return this.dataCenterService.listMonitorHostDiskUsage(paramMap);
	}
	
	@RequestMapping(value = "/monitor/site", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询站点监控", httpMethod = "GET", notes = "listMonitorSite", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "key", value = "关键字", required = false, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listMonitorSite(
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();

		if(key != null) {
			paramMap.put("key", key);
		}
		
		if (start != null) {
			paramMap.put("start", Integer.parseInt(start));
		}
		
		if (length != null) {
			paramMap.put("length", Integer.parseInt(length));
		}
		return this.dataCenterService.listMonitorSite(paramMap);
	}
	
	@RequestMapping(value = "/monitor/site", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建站点监控", httpMethod = "POST", notes = "createMonitorSite", response = String.class)
	public String createMonitorSite(@RequestBody MonitorSiteCreateBean monitorSiteCreateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorSiteCreateBean));
		return this.dataCenterService.createMonitorSite(paramMap);
	}
	
	@RequestMapping(value = "/monitor/site/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑站点监控", httpMethod = "PUT", notes = "updateMonitorSite", response = String.class)
	public String updateMonitorSite(@PathVariable @ApiParam(name = "id", required = true)Integer id, @RequestBody MonitorSiteCreateBean monitorSiteUpdateBean) {
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorSiteUpdateBean));
		paramMap.put("id", id);
		return this.dataCenterService.updateMonitorSite(paramMap);
	}
	
	@RequestMapping(value = "/monitor/site/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除站点监控", httpMethod = "DELETE", notes = "deleteMonitorSite", response = String.class)
	public String deleteMonitorSite(@PathVariable @ApiParam(value = "id", required = true) Integer id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		
		return this.dataCenterService.deleteMonitorSite(paramMap);
	}

}
