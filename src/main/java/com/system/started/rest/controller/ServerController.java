package com.system.started.rest.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.system.started.rest.request.HostAggregateActionHostBean;
import com.system.started.rest.request.HostAggregateCreateBean;
import com.system.started.rest.request.HostAggregateOverSubUpdateBean;
import com.system.started.rest.request.HostAggregateUpdateBean;
import com.system.started.rest.request.ServerCreateBean;
import com.system.started.rest.request.ServerManagerChangeBean;
import com.system.started.rest.request.ServerSystemInfoUpdateBean;
import com.system.started.rest.request.SystemInfoUpdateBean;
import com.system.started.service.ServerService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(value = "/servers")
public class ServerController extends AbstractController {
	private static Logger logger = Logger.getLogger(ServerController.class);

	@Autowired
	private ServerService serverService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取虚机列表", httpMethod = "GET", notes = "listServers", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型，<br/>例子：", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "poolType", value = "资源池类型，<br/>例子：COMPUTE", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "name", value = "名称，<br/>例子：db-server", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "host", value = "计算节点，<br/>例子：dell.test.com", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "businessAddress", value = "业务地址，<br/>例子：192.168.1.173", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "businessAddressList", value = "业务地址集合字符串，<br/>例子：192.168.1.173", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "sysMacAddress", value = "Mac地址，<br/>例子：fa:16:3e:91:f4:6e", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "id", value = "虚机ID，<br/>例子：1666160976", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "tagId", value = "标签ID，<br/>例子：50", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "tagValue", value = "标签值，<br/>例子：演示", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：server", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refStack", value = "关联堆栈，<br/>例子：", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "vmState", value = "虚机状态，<br/>例子：ACTIVE", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "imageRef", value = "镜像，<br/>例子：510d761c-522e-4766-b815-8e75264519ea", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "flavorRef", value = "配置模板，<br/>例子：e9256252-2ce2-40d1-9386-a0c573b9f509", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "flavorName", value = "配置模板名称，<br/>例子：linux_1C2G70G", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query"),
		@ApiImplicitParam(name = "saltAgentStatus", value = "运维状态，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "zabbixAgentStatus", value = "监控状态，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "connectStatus", value = "连接状态，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "startExpireTime", value = "生命周期开始时间，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "endExpireTime", value = "生命周期结束时间，<br/>例子：100", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "expireTime", value = "生命周期时间，<br/>例子：", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=server", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "manageUser", value = "所属用户，<br/>例子：admin", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "isComputePrice", value = "是否计算消费数据，<br/>例子：true", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "showMonitor", value = "是否查询监控数据，<br/>例子：current", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "expireType", value = "生命周期类型，<br/>例子：DATE", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "sortGraphId", value = "根据监控报表排序，<br/>例子：CPU利用率：-1，内存利用率：-2", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "agentIds", value = "管理组件集合字符串，<br/>例子：192.168.1.171", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "sortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listServers(
			@RequestParam(required = false, name = "virtualizationType") String virtualizationType,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = false, value = "projectId") String projectId,
			@RequestParam(required = false, value = "poolType") String poolType,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "host") String host,
			@RequestParam(required = false, value = "businessAddress") String businessAddress,
			@RequestParam(required = false, value = "businessAddressList") String businessAddressList,
			@RequestParam(required = false, value = "sysMacAddress") String sysMacAddress,
			@RequestParam(required = false, value = "id") String id,
			@RequestParam(required = false, value = "tagId") Integer tagId,
			@RequestParam(required = false, value = "tagValue") String tagValue,
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "refStack") String refStack,
			@RequestParam(required = false, value = "vmState") String vmState,
			@RequestParam(required = false, value = "imageRef") String imageRef,
			@RequestParam(required = false, value = "flavorRef") String flavorRef,
			@RequestParam(required = false, value = "flavorName") String flavorName,
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@RequestParam(required = false, name = "saltAgentStatus") Integer saltAgentStatus,
			@RequestParam(required = false, name = "zabbixAgentStatus") Integer zabbixAgentStatus,
			@RequestParam(required = false, name = "connectStatus") Integer connectStatus,
			@RequestParam(required = false, name = "startExpireTime") Integer startExpireTime,
			@RequestParam(required = false, name = "endExpireTime") Integer endExpireTime,
			@RequestParam(required = false, name = "expireTime") Integer expireTime,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam(required = false, value = "manageUser") String manageUser,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, value = "isComputePrice") Boolean isComputePrice,
			@RequestParam(required = false, value = "showMonitor") String showMonitor,
			@RequestParam(required = false, value = "expireType") String expireType,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "sortGraphId") String sortGraphId,
			@RequestParam(required = false, value = "agentIds") String agentIds,
			@RequestParam(required = false, value = "sortDirection") String sortDirection) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (refresh != null) {
			paramMap.put("refresh", refresh);
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
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (regionName != null && !regionName.equals("-1")) {
			paramMap.put("regionName", regionName);
		}
		if (projectId != null) {
			paramMap.put("projectId", projectId);
		}
		if (poolType != null && !poolType.equals("-1")) {
			paramMap.put("poolType", poolType);
		}
		if (name != null) {
			paramMap.put("name", name);
		}
		if (host != null) {
			paramMap.put("host", host);
		}
		if (expireType != null) {
			paramMap.put("expireType", expireType);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (refStack != null) {
			paramMap.put("refStack", refStack);
		}
		if (vmState != null) {
			paramMap.put("vmState", vmState);
		}
		if (imageRef != null) {
			paramMap.put("imageRef", imageRef);
		}
		if (flavorRef != null) {
			paramMap.put("flavorRef", flavorRef);
		}
		if (flavorName != null) {
			paramMap.put("flavorName", flavorName);
		}
		if (saltAgentStatus != null) {
			paramMap.put("saltAgentStatus", saltAgentStatus);
		}
		if (zabbixAgentStatus != null) {
			paramMap.put("zabbixAgentStatus", zabbixAgentStatus);
		}
		if (connectStatus != null) {
			paramMap.put("connectStatus", connectStatus);
		}
		if (startExpireTime != null) {
			paramMap.put("startExpireTime", startExpireTime);
		}
		if (endExpireTime != null) {
			paramMap.put("endExpireTime", endExpireTime);
		}
		if (expireTime != null) {
			paramMap.put("expireTime", expireTime);
		}
		if (businessAddress != null) {
			paramMap.put("businessAddress", businessAddress);
		}
		if (manageUser != null) {
			paramMap.put("manageUser", manageUser);
		}
		if (queryColumn != null) {
			paramMap.put("queryColumn", queryColumn);
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

			String agentIdListString = StringUtils.join(agentIdList, ",");

			paramMap.put("agentIds", agentIdListString);
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
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
		}
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		if (start != null) {
			paramMap.put("start", Integer.parseInt(start));
		}
		if (length != null) {
			paramMap.put("length", Integer.parseInt(length));
		}
		if (draw != null) {
			paramMap.put("draw", Integer.parseInt(draw));
		}
		if (isComputePrice != null) {
			paramMap.put("isComputePrice", isComputePrice);
		}
		if (showMonitor != null) {
			paramMap.put("showMonitor", showMonitor);
		}
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.serverService.listServers(paramMap);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询虚机信息", httpMethod = "GET", notes = "listServer", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "虚机ID，<br/>例子：1666160976", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "poolType", value = "资源池类型，<br/>例子：COMPUTE", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型，<br/>例子：", required = false, dataType = "String",  paramType = "query")
	})
	public String listServer(
			@PathVariable String id,
			@RequestParam(required = false, name = "poolType") String poolType,
			@RequestParam(required = false, name = "virtualizationType") String virtualizationType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		parseRelationLoginIds(paramMap);
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (poolType != null) {
			paramMap.put("poolType", poolType);
		}
		return  this.serverService.getServer(paramMap);
	}

	@RequestMapping(value = "/{id}/detail", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询虚机详情", httpMethod = "GET", notes = "listServerDetail", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "虚机ID，<br/>例子：1666160976", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "poolType", value = "资源池类型，<br/>例子：COMPUTE", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型，<br/>例子：", required = false, dataType = "String",  paramType = "query")
	})
	public String listServerDetail(
			@PathVariable String id,
			@RequestParam(required = false, name = "poolType") String poolType,
			@RequestParam(required = false, name = "virtualizationType") String virtualizationType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		parseRelationLoginIds(paramMap);
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (poolType != null) {
			paramMap.put("poolType", poolType);
		}
		return serverService.getServer(paramMap);
	}

	@RequestMapping(value = "/{nodeId}/interface", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询虚机业务地址", httpMethod = "GET", notes = "listServerInterface", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "虚机ID，<br/>例子：1666160976", required = true, dataType = "String",  paramType = "path")
	})
	public String listServerInterface(@PathVariable String nodeId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		return this.serverService.listServerInterface(paramMap);
	}

	@RequestMapping(value = "/{nodeId}/interface/{interfaceId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "更新虚机业务地址", httpMethod = "POST", notes = "updateServerInterface", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "虚机ID，<br/>例子：1666160976", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "interfaceId", value = "业务地址，<br/>例子：", required = true, dataType = "Integer",  paramType = "path")
	})
	public String updateServerInterface(
			@PathVariable Integer nodeId,
			@PathVariable Integer interfaceId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("interfaceId", interfaceId);
		this.serverService.updateServerInterface(paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateServerInterface successful! ");
		return result;
	}

	@RequestMapping(value = "/manageAuth", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "更新虚机权限", httpMethod = "POST", notes = "updateServerManageAuth", response = String.class)
	public String updateServerManageAuth(@RequestBody @ApiParam(name="flavor", value="编辑配置模板对象", required=true) ServerManagerChangeBean serverManagerChange) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(serverManagerChange));
		this.serverService.updateServerManageAuth(paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateServerManageAuth successful! ");
		return result;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建虚机", httpMethod = "POST", notes = "createServer", response = String.class)
	public String createServer(
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestBody @ApiParam(name="server", value="创建虚机对象", required=true)ServerCreateBean server) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(server));
		return this.serverService.createServer(getCurrentLoginId(), regionName, paramMap);
	}

	@RequestMapping(value = "/{serverId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "删除虚机", httpMethod = "DELETE", notes = "deleteServer", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "serverId", value = "虚机ID，<br/>例子：1666160976", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = true, dataType = "String",  paramType = "query")
	})
	public String deleteServer(
			@PathVariable Integer serverId,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = true, value = "projectId") String projectId) {
		return this.serverService.deleteServers(getCurrentLoginId(), regionName, serverId, projectId);
	}

	@RequestMapping(value = "/{serverId}/action", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "对虚机进行操作", httpMethod = "POST", notes = "actionServer", response = String.class)
	public String actionServer(
			@PathVariable Integer serverId,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = true, value = "projectId") String projectId,
			@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("serverId", serverId);
		if (null != regionName) {
			paramMap.put("regionName", regionName);
		}
		paramMap.put("projectId", projectId);
		return this.serverService.actionServer(paramMap);
	}

	@RequestMapping(value = "/{serverId}/resize", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "调整虚机配置", httpMethod = "POST", notes = "resizeServer", response = String.class)
	public String resizeServer(
			@PathVariable Integer serverId,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = true, value = "projectId") String projectId,
			@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("serverId", serverId);
		if (null != regionName) {
			paramMap.put("regionName", regionName);
		}
		paramMap.put("projectId", projectId);
		return this.serverService.resizeServer(paramMap);
	}
	
	@RequestMapping(value = "/{serverId}/confirmResize", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "确认调整虚机配置", httpMethod = "POST", notes = "confirmResizeServer", response = String.class)
	public String confirmResizeServer(
			@PathVariable Integer serverId, 
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = true, value = "projectId") String projectId, 
			@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("serverId", serverId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.serverService.confirmResizeServer(paramMap);
	}
	
	@RequestMapping(value = "/{serverId}/revertResize", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "取消调整虚机配置", httpMethod = "POST", notes = "revertResizeServer", response = String.class)
	public String revertResizeServer(
			@PathVariable Integer serverId, 
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = true, value = "projectId") String projectId, 
			@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("serverId", serverId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.serverService.revertResizeServer(paramMap);
	}

	@RequestMapping(value = "/{serverId}", method = RequestMethod.POST)
	@ResponseBody
	@ApiIgnore
	public String updateServer(
			@PathVariable Integer serverId,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = true, value = "projectId") String projectId,
			@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("serverId", serverId);
		if (null != regionName) {
			paramMap.put("regionName", regionName);
		}
		paramMap.put("projectId", projectId);
		return this.serverService.updateServer(paramMap);
	}

	@RequestMapping(value = "/{nodeId}/systemInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "编辑虚机管理信息", httpMethod = "POST", notes = "updateServerSystemInfo", response = String.class)
	public String updateServerSystemInfo(
			@PathVariable @ApiParam(name="nodeId",value="虚机ID", required=true) Integer nodeId,
			@RequestBody @ApiParam(name="serverSystemInfoUpdate", value="编辑虚机系统信息对象", required=true) ServerSystemInfoUpdateBean serverSystemInfoUpdate) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(serverSystemInfoUpdate));
		parseCurrentLoginIds(paramMap);
		paramMap.put("nodeId", nodeId);
		return this.serverService.updateServerSystemInfo(paramMap);
	}

	@RequestMapping(value = "/{nodeId}/expireday", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "变更虚机生命周期", httpMethod = "POST", notes = "updateServerExpireDay", response = String.class)
	public String updateServerExpireDay(
			@PathVariable Integer nodeId, 
			@RequestBody HashMap<String, Object> paramMap
//			@PathVariable @ApiParam(name="id",value="虚机ID", required=true) Integer nodeId,
//			@RequestBody @ApiParam(name="serverExpireDayChange", value="变更虚机生命周期对象", required=true) ServerExpireDayChangeBean serverExpireDayChange
			) {
//		@SuppressWarnings("unchecked")
//		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(serverExpireDayChange));
		parseCurrentLoginIds(paramMap);
		paramMap.put("nodeId", nodeId);
		return this.serverService.updateServerExpireDay(paramMap);
	}

	@RequestMapping(value = "/availabilityZones", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询可用性区域", httpMethod = "GET", notes = "listAvailabilityZones", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query")
	})
	public String listAvailabilityZones(@RequestParam(required = false, value = "regionName") String regionName) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("regionName", regionName);
		return this.serverService.listAvailabilityZones(paramMap);
	}

	@RequestMapping(value = "/keypairs", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询keypairs", httpMethod = "GET", notes = "listKeypairs", response = String.class)
	@ApiIgnore
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query")
	})
	public String listKeypairs(@RequestParam(required = false, value = "regionName") String regionName) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("curLoginId", getCurrentLoginId());
		paramMap.put("regionName", regionName);
		return this.serverService.listKeypairs(paramMap);
	}

//	@RequestMapping(value = "/keypairs/{name}", method = RequestMethod.GET)
//	@ResponseBody
//	public String listKeypairDetail(HttpSession session, @RequestParam(required = false, value = "regionName") String regionName, @PathVariable String name) {
//		ListKeypairDetailRequest request = new ListKeypairDetailRequest();
//		request.setName(name);
//		request.setRegionName(regionName);
//		String result = super.sendForResult(session, request);
//		logger.debug("query listKeypairDetail (" + name + ") success");
//		return result;
//	}

//	@RequestMapping(value = "/keypairs", method = RequestMethod.POST)
//	@ResponseBody
//	public String createKeypair(HttpSession session, @RequestParam(required = false, value = "regionName") String regionName, @RequestBody Keypair keypair) {
//		logger.debug("createKeypair param:" + JsonHelper.toJson(keypair));
//		CreateKeypairRequest request = new CreateKeypairRequest();
//		request.setKeypair(keypair);
//		request.setRegionName(regionName);
//		String result = super.sendForResult(session, request);
//		logger.debug("create createKeypair successful");
//		return result;
//	}

//	@RequestMapping(value = "/keypairs/{name}", method = RequestMethod.DELETE)
//	@ResponseBody
//	public String deleteKeypair(HttpSession session, @RequestParam(required = false, value = "regionName") String regionName, @PathVariable String name) {
//		logger.debug("deleteKeypair param:" + name);
//		DeleteKeypairRequest request = new DeleteKeypairRequest();
//		request.setName(name);
//		request.setRegionName(regionName);
//		String result = super.sendForResult(session, request);
//		logger.debug("delete deleteKeypair successful");
//		return result;
//	}

//	@RequestMapping(value = "/console/output/{id}/{length}", method = RequestMethod.GET)
//	@ResponseBody
//	public String getConsoleOutput(HttpSession session, @PathVariable String id, @RequestParam(required = false, value = "regionName") String regionName, @PathVariable Integer length) {
//		logger.debug("getConsoleOutput param:" + id);
//		GetServerConsoleOutputRequest request = new GetServerConsoleOutputRequest();
//		request.setServerId(id);
//		request.setRegionName(regionName);
//		request.setLength(length);
//		String result = super.sendForResult(session, request);
//		logger.debug("query getConsoleOutput successful! the result is : " + result);
//		return result;
//	}

//	@RequestMapping(value = "/console/url/{id}/{imageId}", method = RequestMethod.GET)
//	@ResponseBody
//	public String getConsoleUrl(HttpSession session, @PathVariable String id, @RequestParam(required = false, value = "regionName") String regionName, @PathVariable String imageId) {
//		logger.debug("getConsoleUrl param:" + id);
//		GetServerConsoleUrlRequest request = new GetServerConsoleUrlRequest();
//		request.setServerId(id);
//		request.setRegionName(regionName);
//
//		HashMap<String, Object> parameter = new HashMap<>();
//		parameter.put("imageId", imageId);
//		parseRelationLoginIds(session, parameter);
//		List<HashMap<String, Object>> imageList = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_IMAGES, parameter);
//		if (imageList != null && imageList.size() > 0) {
//			String serverType = (String) imageList.get(0).get("osType");
//			request.setServerType(serverType);
//			String result = super.sendForResult(session, request);
//			logger.debug("query getConsoleUrl successful! the result is : " + result);
//			return result;
//		}
//		return invalidRequest("the request is error!please check the request param!");
//	}

	@RequestMapping(value = "/systemInfo", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "编辑系统信息", httpMethod = "PUT", notes = "updateSystemInfo", response = String.class)
	public String updateSystemInfo(@RequestBody @ApiParam(name="flavor", value="编辑系统信息对象", required=true) SystemInfoUpdateBean systemInfoUpdate) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(systemInfoUpdate));
		parseCurrentLoginIds(paramMap);
		return this.serverService.updateSystemInfo(paramMap);
	}

	@RequestMapping(value = "/hosts", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询Hosts", httpMethod = "GET", notes = "listHosts", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = true, dataType = "String",  paramType = "query")
	})
	public String listHosts(
			@RequestParam(required = true, value = "regionName") String regionName,
			@RequestParam(required = true, value = "projectId") String projectId) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		parseCurrentLoginIds(paramMap);
		return this.serverService.listHosts(paramMap);
	}

	@RequestMapping(value = "/aggregate/{id}/hosts", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询主机聚集下的主机", httpMethod = "GET", notes = "listAggregateHosts", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主机聚集ID，<br/>例子：1", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "regionType", value = "Region类型，<br/>例子：COMPUTE", required = true, dataType = "String",  paramType = "query")
	})
	public String listAggregateHosts(
			@PathVariable String id,
			@RequestParam(required = true, value = "regionName") String regionName,
			@RequestParam(required = true, value = "regionType") String regionType,
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if (regionName != null) {
			paramMap.put("regionName", regionName);
		}
		if (regionType != null) {
			paramMap.put("regionType", regionType);
		}

		if (regionType != null && !regionType.equals("VMWARE")) {
			paramMap.put("hostAggregate", id);
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
		return this.serverService.listAggregateHosts(paramMap);
	}

	@RequestMapping(value = "/aggregates", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value="查询主机聚集", httpMethod = "GET", notes = "listAggregates", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主机聚集ID，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "name", value = "名称，<br/>例子：manage", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "availabilityZone", value = "可用性区域，<br/>例子：az", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=manage", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query"),
		@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listAggregates(
			@RequestParam(required = false, value = "id") Integer id,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "availabilityZone") String availabilityZone,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		if (id != null) {
			paramMap.put("id", id);
		}

		if (name != null) {
			paramMap.put("name", name);
		}

		if (availabilityZone != null) {
			paramMap.put("availabilityZone", availabilityZone);
		}

		if (regionName != null && !regionName.equals("ALL")) {
			paramMap.put("regionName", regionName);
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

		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
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
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.serverService.listAggregates(paramMap);
	}


	@RequestMapping(value = "/aggregate/{aggregateId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询主机聚集详情", httpMethod = "GET", notes = "listAggregateDetails", response = String.class)
	public String listAggregateDetails(
			@PathVariable @ApiParam(name="aggregateId",value="主机聚集ID，<br/>例子：2", required=true)Integer aggregateId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uuid", String.valueOf(aggregateId));
		paramMap.put("regionName", regionName);
		parseCurrentLoginIds(paramMap);
		return this.serverService.listAggregateDetails(paramMap);
	}

	@RequestMapping(value = "/aggregate", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="创建主机聚集", httpMethod = "POST", notes = "createAggregate", response = String.class)
	public String createAggregate(
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="hostAggregate", value="创建主机聚集对象", required=true)HostAggregateCreateBean hostAggregate) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(hostAggregate));
		parseCurrentLoginIds(paramMap);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.serverService.createAggregate(paramMap);
	}

	@RequestMapping(value = "/aggregate/{aggregateId}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value="编辑主机聚集", httpMethod = "PUT", notes = "updateAggregate", response = String.class)
	public String updateAggregate(
			@PathVariable @ApiParam(name="aggregateId",value="主机聚集ID，<br/>例子：2", required=true)Integer aggregateId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="hostAggregate", value="编辑主机聚集对象", required=true)HostAggregateUpdateBean hostAggregate) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(hostAggregate));
		parseCurrentLoginIds(paramMap);
		paramMap.put("uuid", String.valueOf(aggregateId));
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.serverService.updateAggregate(paramMap);
	}

	@RequestMapping(value = "/aggregate/availabilityZone/oversub", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value="设置主机聚集超分比例", httpMethod = "PUT", notes = "updateAilabilityZoneOversub", response = String.class)
	public String updateAilabilityZoneOversub(@RequestBody @ApiParam(name="hostAggregateOverSub", value="设置主机聚集超分比例对象", required=true)HostAggregateOverSubUpdateBean hostAggregateOverSub) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(hostAggregateOverSub));
		return this.serverService.updateAilabilityZoneOversub(paramMap);
	}

	@RequestMapping(value = "/aggregate/{aggregateId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="删除主机聚集", httpMethod = "DELETE", notes = "deleteAggregate", response = String.class)
	public String deleteAggregate(
			@PathVariable @ApiParam(name="aggregateId",value="主机聚集ID，<br/>例子：2", required=true)Integer aggregateId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		parseCurrentLoginIds(paramMap);
		paramMap.put("regionName", regionName);
		paramMap.put("uuid", String.valueOf(aggregateId));
		return this.serverService.deleteAggregate(paramMap);
	}

	@RequestMapping(value = "/aggregate/{aggregateId}/hosts/action", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="添加/移除主机聚集下主机", httpMethod = "POST", notes = "updateAggregateHosts", response = String.class)
	public String updateAggregateHosts(
			@PathVariable @ApiParam(name="aggregateId",value="主机聚集ID，<br/>例子：1", required=true) Integer aggregateId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true) String regionName,
			@RequestBody @ApiParam(name="hostAggregateActionHost", value="添加/移除主机对象", required=true) HostAggregateActionHostBean hostAggregateActionHost) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(hostAggregateActionHost));
		parseCurrentLoginIds(paramMap);
		paramMap.put("uuid", String.valueOf(aggregateId));
		paramMap.put("regionName", regionName);
		return this.serverService.updateAggregateHosts(paramMap);
	}

	@RequestMapping(value = "/aggregate/{aggregateId}/metadata", method = RequestMethod.POST)
	@ResponseBody
	@ApiIgnore
	public String updateAggregateMetadata(
			@PathVariable @ApiParam(name="aggregateId",value="主机聚集ID，<br/>例子：1", required=true) Integer aggregateId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true) String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true) String projectId,
			@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("uuid", String.valueOf(aggregateId));
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.serverService.updateAggregateMetadata(paramMap);
	}

	@RequestMapping(value = "/aggregate/resourceNode/unitPrice", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value="查询主机聚集下资源计量计费单价", httpMethod = "GET", notes = "listAggregateResourceNodeUnitPrice", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query")
	})
	public String listAggregateResourceNodeUnitPrice(@RequestParam(required = true, value = "regionName") String regionName) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("regionName", regionName);
		return this.serverService.listAggregateResourceNodeUnitPrice(paramMap);
	}

	@RequestMapping(value = "/aggregate/history", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String listAggregateHistory(
			@RequestParam(required = true, value = "start_create_ts") String start_create_ts,
			@RequestParam(required = true, value = "end_create_ts") String end_create_ts,
			@RequestParam(required = true, value = "reportType") String reportType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("reportType", reportType);
		paramMap.put("startCreateTs", start_create_ts);
		paramMap.put("endCreateTs", end_create_ts);
		return this.serverService.listAggregateHistory(paramMap);
	}


	@RequestMapping(value = "/reset/charge/{nodeId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="虚机计量计费清零", httpMethod = "DELETE", notes = "instanceResetCharge", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "虚机ID，<br/>例子：1450836291", required = true, dataType = "String",  paramType = "path"),
	})
	public String instanceResetCharge(@PathVariable String nodeId) {
		return this.serverService.instanceResetCharge(nodeId);
	}

	@RequestMapping(value = "/stack/instances", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value="查询虚机所属堆栈的其他虚机", httpMethod = "GET", notes = "listInstanceStackInstances", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeIds", value = "虚机ID集合字符串，<br/>例子：1714943517", required = true, dataType = "String",  paramType = "query"),
	})
	public String listInstanceStackInstances(@RequestParam(required = true, value = "nodeIds") String nodeIds) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeIds", nodeIds);
		return this.serverService.listInstanceStackInstances(paramMap);
	}
	

	@RequestMapping(value = "/{nodeId}/os/file", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询虚机操作系统关键配置文件", httpMethod = "GET", notes = "listServerOsFile", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：122279761", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：VIR_INSTANCE", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listServerOsFile(
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
		
		return this.serverService.listServerOsEnvironment(paramMap);
	}
	
	@RequestMapping(value = "/{nodeId}/os/file/{itemId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询虚机操作系统关键配置文件详细信息", httpMethod = "GET", notes = "listServerOsFileDetails", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：122279761", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "itemId", value = "配置文件ID，<br/>例子：86", required = true, dataType = "integer", paramType = "path")})
	public String listServerOsFileDetails(@PathVariable Integer nodeId, @PathVariable Integer itemId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("itemId", itemId);
		
		return this.serverService.listServerOsEnvironmentDetails(paramMap);
	}
	
	@RequestMapping(value = "/{nodeId}/os/params", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询虚机操作系统关键参数", httpMethod = "GET", notes = "listServerOsParams", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：122279761", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：VIR_INSTANCE", required = true, dataType = "string",  paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listServerOsParams(
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
		
		return this.serverService.listServerOsEnvironment(paramMap);
	}
	
	@RequestMapping(value = "/{nodeId}/os/params/{itemId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询虚机操作系统关键参数详细信息", httpMethod = "GET", notes = "listServerOsParamsDetails", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：122279761", required = true, dataType = "integer", paramType = "path"),
			@ApiImplicitParam(name = "itemId", value = "参数ID，<br/>例子：68", required = true, dataType = "integer", paramType = "path")})
	public String listServerOsParamsDetails(@PathVariable Integer nodeId, @PathVariable Integer itemId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("itemId", itemId);
		
		return this.serverService.listServerOsEnvironmentDetails(paramMap);
	}
	
	@RequestMapping(value = "/{nodeId}/logicalVolume", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询虚机逻辑卷", httpMethod = "GET", notes = "listServerLogicalVolume", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：122279761", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：VIR_INSTANCE", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listServerLogicalVolume(
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
		
		return this.serverService.listServerOsEnvironment(paramMap);
	}
	
	@RequestMapping(value = "/{nodeId}/fileSystem", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询虚机文件系统配置", httpMethod = "GET", notes = "listServerFileSystem", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：122279761", required = true, dataType = "integer", paramType = "path"),
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：VIR_INSTANCE", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")})
	public String listServerFileSystem(
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
		
		return this.serverService.listServerOsEnvironment(paramMap);
	}
}
