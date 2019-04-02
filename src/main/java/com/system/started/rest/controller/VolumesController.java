package com.system.started.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.rest.request.VolumeCreateBean;
import com.system.started.rest.request.VolumeExtendBean;
import com.system.started.rest.request.VolumeResetStatusBean;
import com.system.started.rest.request.VolumeUpdateBean;
import com.system.started.service.VolumesService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/volumes")
public class VolumesController extends AbstractController {
	
	@Autowired
	private VolumesService volumesService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询卷", httpMethod = "GET", notes = "listVolumes", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tagId", value = "标签ID，<br/>例子：50", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "tagValue", value = "标签值，<br/>例子：演示", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：volume", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "volumeIds", value = "卷ID集合字符串，<br/>例子：881515284", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "id", value = "卷ID，<br/>例子：881515284", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "poolType", value = "资源池类型，<br/>例子：COMPUTE", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "name", value = "名称，<br/>例子：volume-swg-test", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "size", value = "容量大小，<br/>例子：10", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=volume", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "status", value = "状态，<br/>例子：available", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query"),
		@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listVolumes(
			@RequestParam(required = false, value = "tagId") Integer tagId,
			@RequestParam(required = false, value = "tagValue") String tagValue,
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, name = "volumeIds") String volumeIds,
			@RequestParam(required = false, name = "id") String id,
			@RequestParam(required = false, value = "poolType") String poolType,
			@RequestParam(required = false, value = "regionName") String regionName,
			@RequestParam(required = false, value = "projectId") String projectId,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "size") Integer size,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length,
			@RequestParam(required = false, name = "status") String status) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (tagId != null) {
			paramMap.put("refresh", refresh);
		}
		if (volumeIds != null) {
			String[] volumeIdArray = volumeIds.split(",");
			String tempIds = "";
			for (int i = 0; i < volumeIdArray.length; i++) {
				tempIds += ",'" + volumeIdArray[i] + "'";
			}
			if (tempIds.length() > 0) {
				tempIds = tempIds.substring(1);
			}
			paramMap.put("volumeIds", tempIds);
		}
		if (id != null) {
			paramMap.put("id", id);
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
		if (poolType != null) {
			paramMap.put("poolType", poolType);
		}
		if (regionName != null && !regionName.equals("-1")) {
			paramMap.put("regionName", regionName);
		}
		if (projectId != null) {
			paramMap.put("projectId", projectId);
		}
		if (status != null) {
			paramMap.put("status", status);
		}
		if (name != null) {
			paramMap.put("name", name);
		}
		if (size != null) {
			paramMap.put("size", size);
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
		return this.volumesService.listVolumes(paramMap);
	}

	@RequestMapping(value = "/{volumeId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询卷详情", httpMethod = "GET", notes = "listVolumeDetails", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "volumeId", value = "卷ID，<br/>例子：881515284", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "poolType", value = "资源池类型，<br/>例子：COMPUTE", required = false, dataType = "String",  paramType = "query")
	})
	public String listVolumeDetails(
			@PathVariable String volumeId,
			@RequestParam(required = false, value = "poolType") String poolType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		paramMap.put("id", volumeId);
		if (poolType != null) {
			paramMap.put("poolType", poolType);
		}
		return this.volumesService.listVolumeDetails(paramMap);
	}


	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="创建卷", httpMethod = "POST", notes = "createVolume", response = String.class)
	public String createVolume(
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ModelAttribute VolumeCreateBean volume) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> volumeForCreate  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(volume));
		volumeForCreate.put("regionName", regionName);
		volumeForCreate.put("projectId", projectId);
		parseCurrentLoginIds(volumeForCreate);
		return this.volumesService.createVolume(volumeForCreate);
	}

	@RequestMapping(value = "/{volumeId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="删除卷", httpMethod = "DELETE", notes = "deleteVolume", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "volumeId", value = "卷ID，<br/>例子：881515284", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = true, dataType = "String",  paramType = "query")
	})
	public String deleteVolume(
			@RequestParam String regionName,
			@RequestParam String projectId,
			@PathVariable Integer volumeId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		paramMap.put("volumeId", volumeId);
		parseCurrentLoginIds(paramMap);
		return this.volumesService.deleteVolume(paramMap);
	}

	@RequestMapping(value = "/{volumeId}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="编辑卷", httpMethod = "POST", notes = "updateVolume", response = String.class)
	public String updateVolume(
			@PathVariable @ApiParam(name="volumeId",value="卷ID，<br/>例子：881515284", required=true) Integer volumeId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName, 
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="volume", value="编辑卷对象", required=true) VolumeUpdateBean volume) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(volume));
		paramMap.put("volumeId", volumeId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		parseCurrentLoginIds(paramMap);
		return this.volumesService.updateVolume(paramMap);
	}

	@RequestMapping(value = "/{volumeId}/action", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="扩展卷", httpMethod = "POST", notes = "extendVolume", response = String.class)
	public String extendVolume(
			@PathVariable @ApiParam(name="volumeId",value="卷ID，<br/>例子：881515284", required=true) Integer volumeId,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName, 
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="volume", value="扩展卷对象", required=true) VolumeExtendBean volume) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(volume));
		paramMap.put("volumeId", volumeId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		parseCurrentLoginIds(paramMap);
		return this.volumesService.extendVolume(paramMap);
	}

	@RequestMapping(value = "/{volumeUuid}/action/{serverUuid}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="挂载/卸载卷", httpMethod = "POST", notes = "resetVolumeStatus", response = String.class)
	public String resetVolumeStatus(
			@RequestParam @ApiParam(name="regionName",value="Region名称，<br/>例子：manageRegion", required=true) String regionName,
			@RequestParam @ApiParam(name="projectId",value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true) String projectId,
			@PathVariable @ApiParam(name="volumeUuid",value="虚机UUID，<br/>例子：91fd7b46-9526-4749-ad63-375937a4877f", required=true) String volumeUuid,
			@PathVariable @ApiParam(name="serverUuid",value="卷UUID，<br/>例子：4fca404f-2c44-453f-9a21-6bbf85cbe281", required=true) String serverUuid,
			@RequestParam @ApiParam(name="attachmentId",value="挂在标识，<br/>例子：0343d2c0-7d53-49d4-82d4-b42ef74c0d2d", required=true) String attachmentId,
			@RequestBody @ApiParam(name="volume", value="挂载/卸载卷对象", required=true) VolumeResetStatusBean volumeResetStatus) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(volumeResetStatus));
		paramMap.put("volumeId", volumeUuid);
		paramMap.put("serverId", serverUuid);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		paramMap.put("attachment_id", attachmentId);
		parseCurrentLoginIds(paramMap);
		return this.volumesService.resetVolumeStatus(paramMap);
	}

	@RequestMapping(value = "/powervc/storage", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询PowerVC存储", httpMethod = "GET", notes = "listPVCStorages", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "String",  paramType = "query")
	})
	public String listPVCStorages(@RequestParam String regionName) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("regionName", regionName);
		parseCurrentLoginIds(paramMap);
		return this.volumesService.listPVCStorages(paramMap);
	}
}
