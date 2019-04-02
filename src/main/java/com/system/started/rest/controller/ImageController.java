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

import com.system.started.rest.request.ImageUpdateBean;
import com.system.started.rest.request.ImageUpdateDisplayStatusBean;
import com.system.started.service.ImageService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/images")
public class ImageController extends AbstractController {
	private final static Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private ImageService imageService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询系统镜像", httpMethod = "GET", notes = "listImages", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uuid", value = "配置模板UUID，<br/>例子：510d761c-522e-4766-b815-8e75264519ea", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "tagId", value = "标签ID，<br/>例子：50", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "tagValue", value = "标签值，<br/>例子：演示", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "查询关键字，<br/>例子：oss", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "name", value = "名称，<br/>例子：oss-db", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "diskFormat", value = "镜像格式，<br/>例子：qcow2", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "osVersion", value = "操作系统版本，<br/>例子：Red Hat Enterprise Linux Server release 7.3 (Santiago)", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "status", value = "状态，<br/>例子：active", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query"),
		@ApiImplicitParam(name = "osType", value = "操作系统类型，<br/>例子：LINUX", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型，<br/>例子：", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "dataCenterId", value = "所属机房，<br/>例子：1", required = false, dataType = "Integer",  paramType = "query"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=oss", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "draw", value = "draw", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listImages(@RequestParam(required = false, value = "uuid") String uuid,
	                         @RequestParam(required = false, value = "tagId") Integer tagId,
	                         @RequestParam(required = false, value = "tagValue") String tagValue,
	                         @RequestParam(required = false, value = "key") String key,
	                         @RequestParam(required = false, value = "name") String name,
	                         @RequestParam(required = false, value = "diskFormat") String diskFormat,
	                         @RequestParam(required = false, value = "osVersion") String osVersion,
	                         @RequestParam(required = false, value = "status") String status,
	                         @RequestParam(required = false, name = "refresh") Boolean refresh,
	                         @RequestParam(required = false) String osType,
	                         @RequestParam(required = false) String virtualizationType,
	                         @RequestParam(required = false, value = "dataCenterId") String dataCenterId,
	                         @RequestParam(required = false, value = "regionName") String regionName,
	                         @RequestParam(required = false, value = "projectId") String projectId,
	                         @RequestParam(required = false, value = "queryColumn") String queryColumn,
	                         @RequestParam(required = false, value = "rules") String rules,
	                         @RequestParam(required = false, value = "instanceSort") String instanceSort,
	                         @RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
	                         @RequestParam(required = false, value = "draw") String draw,
	                         @RequestParam(required = false, value = "start") String start,
	                         @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (null != refresh) {
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
		if (uuid != null) {
			paramMap.put("uuid", uuid);
		}
		if (name != null) {
			paramMap.put("name", name);
		}
		if (diskFormat != null) {
			paramMap.put("diskFormat", diskFormat);
		}
		if (status != null) {
			paramMap.put("status", status);
		}
		if (osVersion != null) {
			paramMap.put("osVersion", osVersion);
		}
		if (osType != null) {
			paramMap.put("osType", osType);
		}
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		if (dataCenterId != null && !dataCenterId.equals("-1")) {
			paramMap.put("dataCenterId", dataCenterId);
		}
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
		paramMap.put("currentLoginId", getCurrentLoginId());
		return this.imageService.listImages(paramMap);
	}

	@RequestMapping(value = "/details/{imageId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询系统镜像详情", httpMethod = "GET", notes = "listImageDetails", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "imageId", value = "镜像ID，<br/>例子：303311111", required = true, dataType = "String",  paramType = "path"),
		@ApiImplicitParam(name = "osType", value = "操作系统类型，<br/>例子：LINUX", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "virtualizationType", value = "虚拟化类型，<br/>例子：", required = false, dataType = "String",  paramType = "query")
	})
	public String listImageDetails(
			@PathVariable String imageId,
			@RequestParam(required = false) String osType,
			@RequestParam(required = false) String virtualizationType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (osType != null) {
			paramMap.put("osType", osType);
		}
		if (virtualizationType != null) {
			paramMap.put("virtualizationType", virtualizationType);
		}
		paramMap.put("id", imageId);
		parseRelationLoginIds(paramMap);
		return this.imageService.listImageDetails(paramMap);
	}

	@RequestMapping(value = "/ostype", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询系统镜像操作系统类型", httpMethod = "GET", notes = "listImageOsType", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query")
	})
	public String listImageOsType(@RequestParam(required = false) String regionName) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (regionName != null) {
			paramMap.put("regionName", regionName);
		}
		return this.imageService.listImageOsType(paramMap);
	}

	@RequestMapping(value = "/imageFiles", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="查询系统镜像文件", httpMethod = "GET", notes = "listImageFiles", response = String.class)
	public String listImageFiles() {
		return this.imageService.listImageFiles();
	}

//	@RequestMapping(value = "", method = RequestMethod.POST)
//	@ResponseBody
//	public String createImage(HttpServletRequest servletRequest, HttpSession session, @RequestBody HashMap<String, Object> paramMap) {
//		CreateImageRequest request = new CreateImageRequest();
//		ImageForCreate imageForCreate = new ImageForCreate();
//		imageForCreate.setName((String) paramMap.get("name"));
//		imageForCreate.setMinDisk(Integer.parseInt((String) paramMap.get("minDisk")));
//		imageForCreate.setMinRam((Integer) paramMap.get("minRam"));
//		imageForCreate.setDiskFormat((String) paramMap.get("diskFormat"));
//		imageForCreate.setContainerFormat((String) paramMap.get("containerFormat"));
//		imageForCreate.setOs((String) paramMap.get("os"));
//		imageForCreate.setOsType((String) paramMap.get("osfamily"));
//		imageForCreate.setOsVersion((String) paramMap.get("osversion"));
//		imageForCreate.setPublic(Boolean.parseBoolean((String) paramMap.get("isPublic")));
//		imageForCreate.setArchitecture((String) paramMap.get("architecture"));
//		imageForCreate.setDescription((String) paramMap.get("description"));
//		imageForCreate.setProtected(Boolean.parseBoolean((String) paramMap.get("protected")));
//		imageForCreate.setVirtualizationType((String) paramMap.get("virtualizationType"));
//		request.setImageForCreate(imageForCreate);
//
//		request.setDataCenter((String) paramMap.get("dataCenter"));
//		request.setRegionName((String) paramMap.get("regionName"));
//		paramMap.remove("regionName");
//
//		request.setImageLocation(CommonUtil.getWebBaseUrl(servletRequest) + "/images/" + (String) paramMap.get("imageLocation"));
//
//		String result = super.sendForResult(session, request);
//		logger.debug("create createImage successful! ");
//		return result;
//	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="添加镜像属性", httpMethod = "POST", notes = "addImageProperties", response = String.class)
	public String addImageProperties(
			@PathVariable @ApiParam(name="id",value="镜像ID，<br/>例子：1981917387", required=true) Integer id,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="image", value="添加镜像属性对象", required=true) ImageUpdateBean image) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(image));
		HashMap<String, Object> baseParamMap = new HashMap<String, Object>();
		baseParamMap.put("imageId", id);
		baseParamMap.put("regionName", regionName);
		baseParamMap.put("projectId", projectId);
		parseCurrentLoginIds(baseParamMap);
		return this.imageService.addImageProperties(baseParamMap, paramMap);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value="编辑镜像属性", httpMethod = "PUT", notes = "updateImageProperties", response = String.class)
	public String updateImageProperties(
			@PathVariable @ApiParam(name="id",value="镜像ID，<br/>例子：1981917387", required=true) Integer id,
			@RequestParam @ApiParam(name="regionName", value="Region名称，<br/>例子：manageRegion", required=true)String regionName,
			@RequestParam @ApiParam(name="projectId", value="平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required=true)String projectId,
			@RequestBody @ApiParam(name="image", value="编辑镜像属性对象", required=true) ImageUpdateBean image) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(image));
		HashMap<String, Object> baseParamMap = new HashMap<String, Object>();
		baseParamMap.put("imageId", id);
		baseParamMap.put("regionName", regionName);
		baseParamMap.put("projectId", projectId);
		parseCurrentLoginIds(baseParamMap);
		return this.imageService.updateImageProperties(baseParamMap, paramMap);
	}


	@RequestMapping(value = "/display/{status}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value="设置系统镜像是否显示", httpMethod = "PUT", notes = "updateImageDisplay", response = String.class)
	public String updateImageDisplay(
			@PathVariable @ApiParam(name="status", value="状态，<br/>例子：显示：1 / 隐藏：0", required=true)Integer status,
			@RequestBody @ApiParam(name="imageUpdateDisplayStatus", value="创建配置模板对象", required=true)ImageUpdateDisplayStatusBean imageUpdateDisplayStatus) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(imageUpdateDisplayStatus));
		@SuppressWarnings("unchecked")
		List<String> imageIds = JsonHelper.fromJson(List.class, JsonHelper.toJson(paramMap.get("imageIds")));
		return this.imageService.updateImageDisplay(status, imageIds);
	}

	@RequestMapping(value = "/{imageId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="删除系统镜像", httpMethod = "DELETE", notes = "deleteImage", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "imageId", value = "镜像ID，<br/>例子：303311111", required = true, dataType = "Integer",  paramType = "path"),
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = false, dataType = "String",  paramType = "query"),
		@ApiImplicitParam(name = "projectId", value = "平台项目，<br/>例子：44931aec982e46739cb3336b8511db0d", required = false, dataType = "String",  paramType = "query")
	})
	public String deleteImage(
			@RequestParam(required = false, name = "regionName") String regionName,
			@RequestParam String projectId,
			@PathVariable Integer imageId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("imageId", imageId);
		paramMap.put("regionName", regionName);
		paramMap.put("projectId", projectId);
		parseCurrentLoginIds(paramMap);
		return this.imageService.deleteImage(paramMap);
	}
}
