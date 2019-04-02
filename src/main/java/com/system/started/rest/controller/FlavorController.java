package com.system.started.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.system.started.rest.request.FlavorCreateBean;
import com.system.started.rest.request.FlavorUpdateBean;
import com.system.started.service.FlavorService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/flavors")
public class FlavorController extends AbstractController {
	private final static Logger logger = LoggerFactory.getLogger(FlavorController.class);

	@Autowired
	private FlavorService flavorService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询配置模板", httpMethod = "GET", notes = "listFlavors", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dataCenterId", value = "所属机房，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "tagId", value = "标签ID，<br/>例子：50", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "tagValue", value = "标签值，<br/>例子：演示", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "name", value = "名称，<br/>例子：linux_1C2G70G", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "memory", value = "内存，<br/>例子：2048", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "root_gb", value = "硬盘，<br/>例子：50 / 0", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "ephemeral_gb", value = "临时磁盘，<br/>例子：0", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "vcpus", value = "cpu，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：linux", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "id", value = "配置模板ID，<br/>例子：703919319", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "uuid", value = "配置模板UUID，<br/>例子：4a5287eb-9d8a-42f9-9ee4-c66458de6755", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "orderBy", value = "排序字段，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=linux", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query"),
		@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listFlavors(@RequestParam(required = false, value = "dataCenterId") String dataCenterId,
	                          @RequestParam(required = false, value = "regionName") String regionName,
	                          @RequestParam(required = false, value = "tagId") Integer tagId,
	                          @RequestParam(required = false, value = "tagValue") String tagValue,
	                          @RequestParam(required = false, value = "name") String name,
	                          @RequestParam(required = false, value = "memory") Integer memory,
	                          @RequestParam(required = false, value = "root_gb") Integer root_gb,
	                          @RequestParam(required = false, value = "ephemeral_gb") Integer ephemeral_gb,
	                          @RequestParam(required = false, value = "vcpus") Integer vcpus,
	                          @RequestParam(required = false, value = "key") String key,
	                          @RequestParam(required = false, value = "id") Integer id,
	                          @RequestParam(required = false, value = "uuid") String uuid,
	                          @RequestParam(required = false, value = "orderBy") String orderBy,
	                          @RequestParam(required = false, value = "queryColumn") String queryColumn,
	                          @RequestParam(required = false, value = "rules") String rules,
	                          @RequestParam(required = false, value = "instanceSort") String instanceSort,
	                          @RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
	                          @RequestParam(required = false, name = "refresh") Boolean refresh,
	                          @RequestParam(required = false, value = "draw") String draw,
	                          @RequestParam(required = false, value = "start") String start,
	                          @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("refresh", refresh);
		if (dataCenterId != null && !dataCenterId.equals("-1")) {
			paramMap.put("dataCenterId", dataCenterId);
		}
		if (regionName != null && !regionName.equals("-1")) {
			paramMap.put("regionName", regionName);
		}
		if (name != null) {
			paramMap.put("name", name);
		}
		if (memory != null) {
			paramMap.put("memory", memory);
		}
		if (root_gb != null) {
			paramMap.put("root_gb", root_gb);
		}
		if (ephemeral_gb != null) {
			paramMap.put("ephemeral_gb", ephemeral_gb);
		}
		if (vcpus != null) {
			paramMap.put("vcpus", vcpus);
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
		if (id != null) {
			paramMap.put("id", id);
		}
		if (uuid != null) {
			paramMap.put("uuid", uuid);
		}
		if (orderBy != null) {
			paramMap.put("orderBy", orderBy);
		} else {
//			paramMap.put("orderBy", "id");
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
		if (start != null) {
			paramMap.put("start", Integer.parseInt(start));
		}
		if (length != null) {
			paramMap.put("length", Integer.parseInt(length));
		}
		if (draw != null) {
			paramMap.put("draw", Integer.parseInt(draw));
		}
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.flavorService.listFlavors(paramMap);
	}

	@RequestMapping(value = "/{flavorId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询配置模板详情", httpMethod = "GET", notes = "listFlavor", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "flavorId", value = "配置模板ID，<br/>例子：703919319", required = true, dataType = "String",  paramType = "path")
	})
	public String listFlavor(@PathVariable String flavorId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", flavorId);
		return this.flavorService.listFlavor(paramMap);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="创建配置模板", httpMethod = "POST", notes = "createFlavor", response = String.class)
	public String createFlavor(
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName, 
			@RequestBody @ApiParam(name="flavor", value="创建配置模板对象", required=true)FlavorCreateBean flavor) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(flavor));
		paramMap.put("regionName", regionName);
		parseCurrentLoginIds(paramMap);
		return this.flavorService.createFlavor(paramMap);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value="编辑配置模板", httpMethod = "PUT", notes = "updateFlavor", response = String.class)
	public String updateFlavor(
			@PathVariable @ApiParam(name="id",value="配置模板ID", required=true) Integer id,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestBody @ApiParam(name="flavor", value="编辑配置模板对象", required=true)FlavorUpdateBean flavor) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(flavor));
		paramMap.put("flavorId", id);
		paramMap.put("regionName", regionName);
		parseCurrentLoginIds(paramMap);
		return this.flavorService.updateFlavor(paramMap);
	}

	@RequestMapping(value = "/{flavorId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="删除配置模板", httpMethod = "DELETE", notes = "deleteFlavor", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "flavorId", value = "配置模板ID，<br/>例子：1730504949", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query")
	})
	public String deleteFlavor(
			@PathVariable Integer flavorId,
			@RequestParam String regionName) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("flavorId", flavorId);
		paramMap.put("regionName", regionName);
		parseCurrentLoginIds(paramMap);
		return this.flavorService.deleteFlavor(paramMap);
	}
}
