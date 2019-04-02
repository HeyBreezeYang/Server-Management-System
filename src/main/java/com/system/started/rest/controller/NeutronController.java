package com.system.started.rest.controller;

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

import com.system.started.rest.request.NetworkCreateBean;
import com.system.started.rest.request.NetworkUpdateBean;
import com.system.started.rest.request.SubNetworkCreateBean;
import com.system.started.rest.request.SubNetworkUpdateBean;
import com.system.started.service.NetworkService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(value = "/networks")
public class NeutronController extends AbstractController {
	private static Logger logger = Logger.getLogger(NeutronController.class);

	@Autowired
	private NetworkService networkService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询私有网络", httpMethod = "GET", notes = "listNetworks", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tagId", value = "标签ID，<br/>例子：50", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "tagValue", value = "标签值，<br/>例子：演示", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：oss", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "name", value = "名称，<br/>例子：oss-flat1", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "adminStateUp", value = "管理状态，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "status", value = "状态，<br/>例子：ACTIVE", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "subnets", value = "子网数，<br/>例子：4", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "catalog", value = "分类，<br/>例子：", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "self", value = "自身，<br/>例子：", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "networkType", value = "网络类型，<br/>例子：flat", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=oss", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query"),
		@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listNetworks(
			@RequestParam(required = false) Integer tagId,
			@RequestParam(required = false) String tagValue,
			@RequestParam(required = false) String key,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer adminStateUp,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Integer subnets,
			@RequestParam(required = false) String catalog,
			@RequestParam(required = false) String self,
			@RequestParam(required = false) String networkType,
			@RequestParam(required = false) String regionName,
			@RequestParam(required = false) String projectId,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {

		HashMap<String, Object> paramMap = new HashMap<>();
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		if (tagId != null) {
			paramMap.put("tagId", tagId);
		}
		if (tagValue != null) {
			StringBuffer stringBuffred = new StringBuffer();
			stringBuffred.append("''");
			for (String value : tagValue.split(",")) {
				stringBuffred.append(",'");
				stringBuffred.append(value.trim());
				stringBuffred.append("'");
			}
			paramMap.put("tagValue", stringBuffred.toString());
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (name != null) {
			paramMap.put("name", name);
		}
		if (adminStateUp != null) {
			paramMap.put("adminStateUp", adminStateUp);
		}
		if (status != null) {
			paramMap.put("status", status);
		}
		if (subnets != null) {
			paramMap.put("subnets", subnets);
		}
		if (self != null) {
			paramMap.put("self", "true");
		}
		if (catalog != null) {
			paramMap.put("type", catalog);
		}
		parseRelationLoginIds(paramMap);
		paramMap.put("currentLoginId", getCurrentLoginId());

		if (regionName != null && !regionName.equals("-1")) {
			paramMap.put("regionName", regionName);
		}
		if (projectId != null) {
			paramMap.put("projectId", projectId);
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

		if (networkType != null) {
			String[] networkTypeArray = networkType.split(",");
			StringBuffer networkTypeBuffer = new StringBuffer();
			for (int i = 0; i < networkTypeArray.length; i++) {
				networkTypeBuffer.append(",");
				networkTypeBuffer.append("'");
				networkTypeBuffer.append(networkTypeArray[i]);
				networkTypeBuffer.append("'");
			}
			if (networkTypeBuffer.toString().length() > 0) {
				paramMap.put("networkType", networkTypeBuffer.toString().substring(1));
			}
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
		return this.networkService.listNetworks(paramMap);
	}

	@RequestMapping(value = "/{networkId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询私有网络详情", httpMethod = "GET", notes = "listNetworkDetails", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkId", value = "私有网络ID，<br/>例子：1268772994", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "catalog", value = "操作系统类型，<br/>例子：", required = false, dataType = "String",  paramType = "query")
	})
	public String listNetworkDetails(
			@RequestParam(required = false) String catalog,
			@PathVariable Integer networkId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("id", networkId);
		paramMap.put("type", catalog);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.networkService.listNetworkDetails(paramMap);
	}


	@RequestMapping(value = "/details/{networkUuid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询私有网络详情", httpMethod = "GET", notes = "listNetworkDetails", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络UUID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "catalog", value = "操作系统类型，<br/>例子：", required = false, dataType = "String",  paramType = "query")
	})
	public String listNetworkDetails(
			@RequestParam(required = false) String catalog,
			@PathVariable String networkUuid) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("uuid", networkUuid);
		paramMap.put("type", catalog);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.networkService.listNetworkDetails(paramMap);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="创建私有网络", httpMethod = "POST", notes = "createNetwork", response = String.class)
	public String createNetwork(
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="network", value="创建私有网络对象", required=true)NetworkCreateBean network) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(network));
		parseCurrentLoginIds(paramMap);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.networkService.createNetwork(paramMap);
	}

	@RequestMapping(value = "/{networkId}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="编辑私有网络", httpMethod = "POST", notes = "updateNetwork", response = String.class)
	public String updateNetwork(
			@PathVariable @ApiParam(name="networkId", value="网络ID，<br/>例子：1268772994", required=true)Integer networkId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="network", value="编辑私有网络对象", required=true)NetworkUpdateBean network) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(network));
		parseCurrentLoginIds(paramMap);
		paramMap.put("networkId", networkId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.networkService.updateNetwork(paramMap);
	}

	@RequestMapping(value = "/{networkId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="删除私有网络", httpMethod = "DELETE", notes = "deleteImage", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkId", value = "私有网络ID，<br/>例子：1268772994", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = true, dataType = "String",  paramType = "query")
	})
	public String deleteNetwork(
			@PathVariable Integer networkId,
			@RequestParam String regionName,
			@RequestParam String projectId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("networkId", networkId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		parseCurrentLoginIds(paramMap);
		return this.networkService.deleteNetwork(paramMap);
	}

	@RequestMapping(value = "/{networkId}/subnets", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="创建子网", httpMethod = "POST", notes = "createSubnet", response = String.class)
	public String createSubnet(
			@PathVariable @ApiParam(name="networkId", value="网络ID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required=true)String networkId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="subNetwork", value="创建子网对象", required=true) SubNetworkCreateBean subNetwork) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(subNetwork));
		parseCurrentLoginIds(paramMap);
		paramMap.put("networkId", networkId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.networkService.createSubnet(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/subnets", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询子网", httpMethod = "GET", notes = "listSubnets", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络UUID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query")
	})
	public String listSubnets(
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@PathVariable String networkUuid) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("networkId", networkUuid);
		paramMap.put("refresh", refresh);
		return this.networkService.listSubnets(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/subnets/{subnetUuid}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="删除子网", httpMethod = "DELETE", notes = "deleteSubnet", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络ID，<br/>例子：1268772994", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "subnetUuid", value = "子网UUID，<br/>例子：058494c3-437f-4479-9bcc-c6ee3d2e4925", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = true, dataType = "String",  paramType = "query")
	})
	public String deleteSubnet(
			@RequestParam String regionName,
			@RequestParam String projectId,
			@PathVariable String networkUuid,
			@PathVariable String subnetUuid) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseCurrentLoginIds(paramMap);
		paramMap.put("networkUuid", networkUuid);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		paramMap.put("subnetUuid", subnetUuid);
		return this.networkService.deleteSubnet(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/subnets/{subnetUuid}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value="编辑子网", httpMethod = "POST", notes = "updateSubnet", response = String.class)
	public String updateSubnet(
			@PathVariable @ApiParam(name="networkUuid", value="网络Uuid，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required=true)String networkUuid,
			@PathVariable @ApiParam(name="subnetUuid", value="子网Uuid，<br/>例子：058494c3-437f-4479-9bcc-c6ee3d2e4925", required=true)String subnetUuid,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="subNetwork", value="编辑子网对象", required=true) SubNetworkUpdateBean subNetwork) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(subNetwork));
		parseCurrentLoginIds(paramMap);
		paramMap.put("networkUuid", networkUuid);
		paramMap.put("subnetUuid", subnetUuid);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		return this.networkService.updateSubnet(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/ports/{portId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询端口详情", httpMethod = "GET", notes = "listPortDetails", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络UUID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "portId", value = "端口ID，<br/>例子：1", required = true, dataType = "Integer",  paramType = "path")
	})
	public String listPortDetails(
			@PathVariable String networkUuid,
			@PathVariable Integer portId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", portId);
		paramMap.put("networkUuid", networkUuid);
		return this.networkService.listPortDetails(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/ports", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询端口", httpMethod = "GET", notes = "listPorts", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络UUID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceName", value = "虚机名称，<br/>例子：customer-db-server", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "fixedIps", value = "IP地址，<br/>例子：192.168.1.177", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：177", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "String",  paramType = "query")
	})
	public String listPorts(
			@RequestParam(required = false, name = "regionName") String regionName,
			@RequestParam(required = false, name = "instanceName") String instanceName,
			@RequestParam(required = false, name = "fixedIps") String fixedIps,
			@RequestParam(required = false, name = "key") String key,
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@RequestParam(required = false, name = "uuid") String uuid,
			@PathVariable String networkUuid) {

		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("networkId", networkUuid);
		if (regionName != null) {
			paramMap.put("regionName", regionName);
		}
		if (instanceName != null) {
			paramMap.put("refresh", refresh);
		}
		if (instanceName != null) {
			paramMap.put("instanceName", instanceName);
		}
		if (fixedIps != null) {
			paramMap.put("fixedIps", fixedIps);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (uuid != null) {
			paramMap.put("uuid", uuid);
		}
		return this.networkService.listPorts(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/ipAddress", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询IP地址", httpMethod = "GET", notes = "listIpAddress", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络UUID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "fixedIps", value = "IP地址，<br/>例子：192.168.1.177", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：177", required = false, dataType = "String",  paramType = "query")
	})
	public String listIpAddress(
			@RequestParam(required = false, name = "fixedIps") String fixedIps,
			@RequestParam(required = false, name = "key") String key,
			@RequestParam(required = false, name = "cidr") String cidr,
			@PathVariable String networkUuid) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("networkId", networkUuid);
		if (fixedIps != null) {
			paramMap.put("fixedIps", fixedIps);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (cidr != null) {
			paramMap.put("cidr", cidr);
		}
		return this.networkService.listIpAddress(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/ports", method = RequestMethod.POST)
	@ResponseBody
	@ApiIgnore
	public String createPort(
			@PathVariable String networkUuid,
			@RequestParam String regionName,
			@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("networkUuid", networkUuid);
		paramMap.put("regionName", regionName);
		return this.networkService.createPort(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/ports/{portUuid}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiIgnore
	public String deletePort(
			@RequestParam String regionName,
			@RequestParam String projectId,
			@PathVariable String networkUuid,
			@PathVariable String portUuid) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("uuid", portUuid);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		paramMap.put("networkUuid", networkUuid);
		paramMap.put("portUuid", portUuid);
		parseCurrentLoginIds(paramMap);
		return this.networkService.deletePort(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/ports/{portUuid}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiIgnore
	public String updatePort(
			@PathVariable String networkUuid,
			@PathVariable String portUuid,
			@RequestParam String regionName,
			@RequestParam String projectId,
			@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("networkUuid", networkUuid);
		paramMap.put("portUuid", portUuid);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		parseCurrentLoginIds(paramMap);
		return this.networkService.updatePort(paramMap);
	}

	@RequestMapping(value = "/{networkUuid}/validate", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="验证IP地址范围", httpMethod = "POST", notes = "validateIpInRange", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络UUID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "ipAddress", value = "IP地址，<br/>例子：192.168.1.177", required = true, dataType = "String",  paramType = "query")
	})
	public String validateIpInRange(@PathVariable String networkUuid, @RequestParam String ipAddress) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("networkId", networkUuid);
		paramMap.put("ipAddress", ipAddress);
		return this.networkService.validateIpInRange(paramMap);
	}


	@RequestMapping(value = "/{networkUuid}/ipAddressDetail", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询IP地址详情", httpMethod = "GET", notes = "listIpAddressDetail", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "networkUuid", value = "私有网络UUID，<br/>例子：801510c5-a226-413a-a4ab-e630d8aec304", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "fixedIps", value = "IP地址，<br/>例子：192.168.1.177", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：oss-flat", required = false, dataType = "String",  paramType = "query")
	})
	public String listIpAddressDetail(
			@PathVariable String networkUuid,
			@RequestParam(required = false, name = "fixedIps") String fixedIps,
			@RequestParam(required = false, name = "key") String key) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("networkId", networkUuid);
		if (fixedIps != null) {
			paramMap.put("fixedIps", fixedIps);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		return this.networkService.listIpAddressDetail(paramMap);
	}
}
