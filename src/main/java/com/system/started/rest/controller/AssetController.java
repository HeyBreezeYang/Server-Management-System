package com.system.started.rest.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.rest.request.AssetFieldTemplateCreateBean;
import com.system.started.rest.request.AssetFieldTemplateUpdateBean;
import com.system.started.rest.request.AssetInfoCreateBean;
import com.system.started.rest.request.AssetInfoUpdateBean;
import com.system.started.rest.request.AssetLifecycleCreateBean;
import com.system.started.rest.request.AssetLifecycleUpdateBean;
import com.system.started.rest.request.AssetTypeCreateBean;
import com.system.started.rest.request.AssetTypeUpdateBean;
import com.system.started.service.AssetService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/asset")
public class AssetController extends AbstractController {
	
	@Autowired
	private AssetService assetService;
	
	@RequestMapping(value = "/type", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资产类型", httpMethod = "GET", notes = "listAssetType", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listAssetType(
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "start") String start,
            @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(name != null) {
			paramMap.put("name", name);
		}
		if(start != null) {
			paramMap.put("start", start);
		}
		if(length != null) {
			paramMap.put("length", length);
		}
		return this.assetService.listAssetType(paramMap);
	}
	
	@RequestMapping(value = "/type", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建资产类型", httpMethod = "POST", notes = "createAssetType", response = String.class)
	public String createAssetType(@RequestBody AssetTypeCreateBean assetTypeCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetTypeCreateBean));
		return this.assetService.createAssetType(paramMap);
	}
	
	@RequestMapping(value = "/type/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑资产类型", httpMethod = "PUT", notes = "updateAssetType", response = String.class)
	public String updateAssetType(@PathVariable @ApiParam(name = "id", value = "资产类型ID", required = true)Integer id, @RequestBody AssetTypeUpdateBean assetTypeUpdateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetTypeUpdateBean));
		paramMap.put("id", id);
		return this.assetService.updateAssetType(paramMap);
	}
	
	@RequestMapping(value = "/type/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除资产类型", httpMethod = "DELETE", notes = "deleteAssetType", response = String.class)
	public String deleteAssetType(@PathVariable @ApiParam(name = "id", value = "资产类型ID", required = true)Integer id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		return this.assetService.deleteAssetType(paramMap);
	}
	
	@RequestMapping(value = "/lifecycle", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资产生命周期", httpMethod = "GET", notes = "listAssetLifecycle", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listAssetLifecycle(
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "type") Integer type,
			@RequestParam(required = false, value = "start") String start,
            @RequestParam(required = false, value = "length") String length
			) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(name != null) {
			paramMap.put("name", name);
		}
		if(type != null) {
			paramMap.put("type", type);
		}
		if(start != null) {
			paramMap.put("start", start);
		}
		if(length != null) {
			paramMap.put("length", length);
		}
		return this.assetService.listAssetLifecycles(paramMap);
	}
	
	@RequestMapping(value = "/lifecycle", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建资产生命周期", httpMethod = "POST", notes = "createAssetLifecycle", response = String.class)
	public String createAssetLifecycle(@RequestBody AssetLifecycleCreateBean assetLifecycleCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetLifecycleCreateBean));
		return this.assetService.createAssetLifecycle(paramMap);
	}
	
	@RequestMapping(value = "/lifecycle/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑资产生命周期", httpMethod = "PUT", notes = "updateAssetLifecycle", response = String.class)
	public String updateAssetLifecycle(@PathVariable @ApiParam(name = "id", value = "资产生命周期ID", required = true)Integer id, @RequestBody AssetLifecycleUpdateBean assetLifecycleUpdateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetLifecycleUpdateBean));
		paramMap.put("id", id);
		return this.assetService.updateAssetLifecycle(paramMap);
	}
	
	@RequestMapping(value = "/lifecycle/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除资产生命周期", httpMethod = "DELETE", notes = "deleteAssetLifecycle", response = String.class)
	public String deleteAssetLifecycle(@PathVariable @ApiParam(name = "id", value = "资产生命周期ID", required = true)Integer id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		return this.assetService.deleteAssetLifecycle(paramMap);
	}
	
	@RequestMapping(value = "/assetOverall", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资产总览", httpMethod = "GET", notes = "listAssetOverall", response = String.class)
	public String listAssetOverall() {
		HashMap<String, Object> paramMap = new HashMap<>();
		return this.assetService.listAssetOverall(paramMap);
	}
	
	@RequestMapping(value = "/assetNote", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资产记录生命周期", httpMethod = "GET", notes = "listAssetNoteLifecycle", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "typeId", value = "属性ID", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "propType", value = "属性类型：LIFECYCLE", required = true, dataType = "string", paramType = "query")
	})
	public String listAssetNoteLifecycle(
			@RequestParam(required = true, value = "typeId") String propId,
			@RequestParam(required = true, value = "propType") String propType
            ) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("typeId", propId);
		paramMap.put("propType", propType);
		
		return this.assetService.listAssetNoteLifecycle(paramMap);
	}
	
	@RequestMapping(value = "/{id}/assetNote", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "添加记录生命周期", httpMethod = "POST", notes = "createAssetNoteLifecycle", response = String.class)
	public String createAssetNoteLifecycle(@PathVariable @ApiParam(name = "id", value = "字段ID", required = true)Integer id, @RequestBody AssetInfoCreateBean assetInfoCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetInfoCreateBean));
		paramMap.put("assetId", id);
		return this.assetService.createAssetNoteLifecycle(paramMap);
	}
	
	
	@RequestMapping(value = "/field/templates", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询自定义字段", httpMethod = "GET", notes = "listAssetFieldTemplates", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "key", value = "关键字", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "typeId", value = "类型ID", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "propId", value = "属性ID", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "propType", value = "属性类型", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listAssetFieldTemplates(
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "typeId") String typeId,
			@RequestParam(required = false, value = "propId") String propId,
			@RequestParam(required = true, value = "propType") String propType,
			@RequestParam(required = false, value = "start") String start,
            @RequestParam(required = false, value = "length") String length
			) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("propType", propType);
		
		if(key != null) {
			paramMap.put("key", key);
		}
		if(name != null) {
			paramMap.put("name", name);
		}
		if(propId != null) {
			paramMap.put("propId", propId);
		}
		if(type != null && !type.equals("")) {
			paramMap.put("type", type);
		}
		if(typeId != null) {
			paramMap.put("typeId", typeId);
		}
		if(start != null) {
			paramMap.put("start", start);
		}
		if(length != null) {
			paramMap.put("length", length);
		}
		return this.assetService.listAssetFieldTemplates(paramMap);
	}
	
	@RequestMapping(value = "/field/template", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建自定义字段", httpMethod = "POST", notes = "createAssetFieldTemplate", response = String.class)
	public String createAssetFieldTemplate(@RequestBody AssetFieldTemplateCreateBean assetFieldTemplateCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetFieldTemplateCreateBean));
		return this.assetService.createAssetFieldTemplate(paramMap);
	}
	
	@RequestMapping(value = "/field/template/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑自定义字段", httpMethod = "PUT", notes = "updateAssetLifecycle", response = String.class)
	public String updateAssetFieldTemplate(@PathVariable @ApiParam(name = "id", value = "字段ID", required = true)Integer id, @RequestBody AssetFieldTemplateUpdateBean assetFieldTemplateUpdateeBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetFieldTemplateUpdateeBean));
		paramMap.put("id", id);
		return this.assetService.updateAssetFieldTemplate(paramMap);
	}
	
	@RequestMapping(value = "/field/template/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除自定义字段", httpMethod = "DELETE", notes = "deleteAssetFieldTemplate", response = String.class)
	public String deleteAssetFieldTemplate(@PathVariable @ApiParam(name = "id", value = "字段ID", required = true)Integer id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		return this.assetService.deleteAssetFieldTemplate(paramMap);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资产", httpMethod = "GET", notes = "listAssetInfo", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ipAddress", value = "IP地址", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "typeId", value = "类型ID", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listAssetInfo(
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "ipAddress") String ipAddress,
			@RequestParam(required = false, value = "typeId") Integer typeId,
			@RequestParam(required = false, value = "start") String start,
            @RequestParam(required = false, value = "length") String length
			) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(name != null) {
			paramMap.put("name", name);
		}
		if(ipAddress != null) {
			paramMap.put("ipAddress", ipAddress);
		}
		if(typeId != null) {
			paramMap.put("typeId", typeId);
		}
		if(start != null) {
			paramMap.put("start", start);
		}
		if(length != null) {
			paramMap.put("length", length);
		}
		return this.assetService.listAssetInfo(paramMap);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "添加资产", httpMethod = "POST", notes = "createAssetInfo", response = String.class)
	public String createAssetInfo(@RequestBody AssetInfoCreateBean assetInfoCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetInfoCreateBean));
		return this.assetService.createAssetInfo(paramMap);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑资产", httpMethod = "PUT", notes = "updateAssetLifecycle", response = String.class)
	public String updateAssetInfo(@PathVariable @ApiParam(name = "id", value = "资产ID", required = true)Integer id, @RequestBody AssetInfoUpdateBean assetInfoUpdateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(assetInfoUpdateBean));
		paramMap.put("id", id);
		parseCurrentLoginIds(paramMap);
		return this.assetService.updateAssetInfo(paramMap);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除资产", httpMethod = "DELETE", notes = "deleteAssetInfo", response = String.class)
	public String deleteAssetInfo(@PathVariable @ApiParam(name = "id", value = "资产ID", required = true)Integer id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		return this.assetService.deleteAssetInfo(paramMap);
	}
	
	@RequestMapping(value = "/logs", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资产日志", httpMethod = "GET", notes = "listAssetInfoLogs", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "key", value = "关键字", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "typeId", value = "类型ID", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listAssetInfoLogs(
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "typeId") Integer typeId,
			@RequestParam(required = false, value = "start") String start,
            @RequestParam(required = false, value = "length") String length
			) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(key != null) {
			paramMap.put("key", key);
		}
		if(typeId != null) {
			paramMap.put("typeId", typeId);
		}
		if(start != null) {
			paramMap.put("start", start);
		}
		if(length != null) {
			paramMap.put("length", length);
		}
		return this.assetService.listAssetInfoLogs(paramMap);
	}
}
