package com.system.started.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

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

import com.system.started.action.wrapper.SystemActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "标签管理")
@Controller
@RequestMapping(value = "/tags")
public class TagController extends AbstractController {
	private final static Logger logger = LoggerFactory.getLogger(TagController.class);

	@Value("${oss.apigate.config.tag-scope-default}")
	String TAG_SCOPE_DEFAULT;
	
	@Autowired
	private SystemActionWrapper systemActionWrapper;
	
	@Autowired
	private DBService dbService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listTags( 
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "tagType") String tagType,
			@RequestParam(required = true, value = "resourceType") String resourceType,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "start") String start, 
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> parameter = new HashMap<>();
//		parseRelationLoginIds( parameter);
		
		if (key != null) {
			parameter.put("key", key);
		}
		if (name != null) {
			parameter.put("name", name);
		}
		if (tagType != null) {
			parameter.put("tagType", tagType);
		}
		if (resourceType != null) {
			parameter.put("resourceType", resourceType);
		}
		if (instanceSort != null) {
			parameter.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			parameter.put("instanceSortDirection", instanceSortDirection);
		}
		
		HashMap<String, Object> resultMap =null;
		if(start!=null && !length.equals("-1")){
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_TAGS, parameter, currentPage, Integer.parseInt(length));
		}else{
			resultMap=dbService.select(DBServiceConst.SELECT_SYSTEM_TAGS, parameter);
		}
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listTags successful! the result is: " + result);
		return result;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="创建标签", httpMethod = "POST", notes = "", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "paramMap", value = "参数体", required = true, dataType = "String",  paramType = "body")
	})
	public String createTag(@RequestBody HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		
		if(!paramMap.containsKey("scope")){
			if(TAG_SCOPE_DEFAULT.equals("ALL")){
				paramMap.put("scope", TAG_SCOPE_DEFAULT);
			}
		}
		
		parseCurrentLoginIds(paramMap);
		
		if (paramMap.get("tagType").equals("RESOURCE")) {
			if(paramMap.containsKey("values") && ((List<String>)paramMap.get("values")).size()>0){
				List<String> tagValues = (List<String>) paramMap.get("values");
				List<HashMap<String, Object>> tagValueList = new ArrayList<HashMap<String, Object>>();
				for (String tagItem : tagValues) {
					HashMap<String, Object> tagItemMap = new HashMap<String, Object>();
					tagItemMap.put("value", tagItem);
					tagItemMap.put("shorthand", tagItem);
					
					tagValueList.add(tagItemMap);
				}
				
				paramMap.put("list", tagValueList);
				
				dbService.insert(DBServiceConst.INSERT_SYSTEM_TAG_RESOURCE, paramMap);
			}
			
			if(paramMap.containsKey("propertyTags")){
				List<HashMap<String, Object>> propertyTags = (List<HashMap<String, Object>>) paramMap.get("propertyTags");
				for (HashMap<String, Object> propertyTagMap : propertyTags) {
					HashMap<String, Object> propertyTagParamMap = new HashMap<String, Object>();
					parseRelationLoginIds(propertyTagParamMap);
					propertyTagParamMap.put("resourceIds", paramMap.get("resourceIds"));
					propertyTagParamMap.put("tagId", propertyTagMap.get("id"));
					propertyTagParamMap.put("name", propertyTagMap.get("name"));
					
					dbService.delete(DBServiceConst.DELETE_SYSTEM_TAG_RESOURCE, propertyTagParamMap);
					
					List<String> tagValues = (List<String>) propertyTagMap.get("textArray");
					if(tagValues.size()>0){
						List<HashMap<String, Object>> tagValueList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> propertyTagShorthandObj = (HashMap<String, Object>) paramMap.get("propertyTagShorthandObj");
						for (String tagItem : tagValues) {
							HashMap<String, Object> tagItemMap = new HashMap<String, Object>();
							tagItemMap.put("value", tagItem);
							if(propertyTagShorthandObj.containsKey(tagItem)){
								tagItemMap.put("shorthand", propertyTagShorthandObj.get(tagItem));
							}else{
								tagItemMap.put("shorthand", tagItem);
							}
							
							tagValueList.add(tagItemMap);
						}
						
						propertyTagParamMap.put("list", tagValueList);
						
						parseCurrentLoginIds(propertyTagParamMap);
						dbService.insert(DBServiceConst.INSERT_SYSTEM_TAG_RESOURCE, propertyTagParamMap);
					}
				}
			}
			// 用于单独创建标签名称
			if(!paramMap.containsKey("values") && !paramMap.containsKey("propertyTags")){
				
				dbService.insert(DBServiceConst.INSERT_SYSTEM_TAG_RESOURCE, paramMap);
				// 为组织架构设置部门标签
				if(paramMap.containsKey("refGroupId")){
					dbService.insert(DBServiceConst.INSERT_SYSTEM_DEPARTMENT_GROUP_TAG_RESOURCE, paramMap);
					
					// 向部门表中添加部门标签，以保持部门数据一致
					try {
						String loginId = getCurrentLoginId();
						paramMap.put("name", paramMap.get("value"));
						Result result = systemActionWrapper.doExcutionAction(loginId,null, EAction.SYSTEM_CREATE_DEPARTMENT, paramMap);
						if(!result.getResultCode().equals(EResultCode.SUCCESS)){
							logger.error("error creating department to create tag.");
						}
					} catch (Exception e) {
						logger.error("error creating department to create tag.", e);
					}
				}
			}
		}else if(paramMap.get("tagType").equals("PROPERTY")){
			dbService.insert(DBServiceConst.INSERT_SYSTEM_TAG, paramMap);
		}
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createTag successful! ");
		return result;
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
	public String updateTag(@RequestBody HashMap<String, Object> paramMap) {
		logger.debug("the update tag param is :" + JsonHelper.toJson(paramMap));
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		
//		parseRelationLoginIds(paramMap);
		
		if(paramMap.get("tagType").equals("PROPERTY")){
			dbService.update(DBServiceConst.UPDATE_SYSTEM_TAG, paramMap);
		}else{
			HashMap<String, Object> existParamMap = new HashMap<>();
			existParamMap.putAll(paramMap);
			parseRelationLoginIds(existParamMap);
			
			List<HashMap<String, Object>> existTags = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_PROPERTY_TAG_VALUES, existParamMap);
			if(existTags.size()>0){
				logger.debug(existParamMap.get("value") + " 已经存在了");
				resultMap.put("messageStatus", "ERROR");
				resultMap.put("responseMsg", existParamMap.get("value") + " 已存在，请重新编辑！");
			}else{
				dbService.update(DBServiceConst.UPDATE_SYSTEM_TAG_RESOURCE, paramMap);
				
				if(paramMap.containsKey("refGroupId")){
					try {
						paramMap.put("refTagResourceId", paramMap.get("id"));
						paramMap.remove("id");
						
						String loginId = getCurrentLoginId();
						Result result = systemActionWrapper.doExcutionAction(loginId,null, EAction.SYSTEM_UPDATE_DEPARTMENT, paramMap);
						if(!result.getResultCode().equals(EResultCode.SUCCESS)){
							logger.error("error updating department to update tag.");
						}
					} catch (Exception e) {
						logger.error("error updating department to update tag.");
					}
				}
			}
		}
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateTagModules successful! ");
		return result;
	}

	@RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteTag(
			@PathVariable Integer tagId, 
			@RequestParam(required = true, value = "tagType") String tagType, 
			@RequestParam(required = false, value = "id") Integer id,
			@RequestParam(required = true, value = "name") String name,
			@RequestParam(required = false, value = "tagName") String tagName,
			@RequestParam(required = true, value = "resourceType") String resourceType,
			@RequestParam(required = false, value = "refGroupId") String refGroupId) {
		HashMap<String, Object> paramMap = new HashMap<>();
//		parseRelationLoginIds(paramMap);
		logger.debug("the delete tag param is :" + JsonHelper.toJson(paramMap));
		
		if(tagType.equals("PROPERTY")){
			paramMap.put("id", tagId);
			dbService.delete(DBServiceConst.DELETE_SYSTEM_TAG, paramMap);
		}else{
			paramMap.put("value", name);
			paramMap.put("resourceType", resourceType);
			if(tagName != null){
				paramMap.put("name", tagName);
			}
			dbService.delete(DBServiceConst.DELETE_SYSTEM_TAG_RESOURCE, paramMap);
			
			if(refGroupId != null){
				try {
					HashMap<String, Object> departmentParamMap = new HashMap<>();
					departmentParamMap.put("refGroupId", refGroupId);
					departmentParamMap.put("refTagResourceId", id);
					
					String loginId = getCurrentLoginId();
					Result result = systemActionWrapper.doExcutionAction(loginId,null, EAction.SYSTEM_DELETE_DEPARTMENT, departmentParamMap);
					if(!result.getResultCode().equals(EResultCode.SUCCESS)){
						logger.error("error deleting department to delete tag.");
					}
				} catch (Exception e) {
					logger.error("error deleting department to delete tag.");
				}
			}
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteTag successful! ");
		return result;
	}
	
	@RequestMapping(value = "/property", method = RequestMethod.GET)
	@ResponseBody
	public String listPropertyTags( 
			@RequestParam(required = false, value = "id") Integer id, 
			@RequestParam(required = false, value = "groupId") Integer groupId,
			@RequestParam(required = false, value = "resourceId") Integer resourceId, 
			@RequestParam(required = false, value = "scope") String scope) {
		HashMap<String, Object> parameter = new HashMap<>();
		parseRelationLoginIds(parameter);
		
		if(id != null){
			parameter.put("id", id);
		}
		
		if(resourceId != null){
			parameter.put("resourceId", resourceId);
		}
		
		if(scope != null){
			parameter.put("scope", scope);
		}
		
		List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_PROPERTY_TAGS, parameter);
		for (HashMap<String, Object> tagMap : resultList) {
			HashMap<String, Object> valueParamMap = new HashMap<String, Object>();
			valueParamMap.put("loginId", parameter.get("loginId"));
			valueParamMap.put("tagId", tagMap.get("id"));
			if (groupId != null) {
				valueParamMap.put("groupId", groupId);
			}
			
			List<HashMap<String, Object>> valueList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_PROPERTY_TAG_VALUES, valueParamMap);
			tagMap.put("values", valueList);
		}
		
		String result = JsonHelper.toJson(resultList);
		logger.debug("query listPropertyTags successful! the result is: " + result);
		return result;
	}
	
	@RequestMapping(value = "/property/system", method = RequestMethod.GET)
	@ResponseBody
	public String listPropertyTagSystem( 
			@RequestParam(required = true, value = "tagId") Integer tagId, 
			@RequestParam(required = false, value = "groupId") Integer groupId,
			@RequestParam(required = false, value = "draw") String draw, 
			@RequestParam(required = false, value = "start") String start, 
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		
		if(tagId != null){
			paramMap.put("tagId", tagId);
		}
		
		if (groupId != null) {
			paramMap.put("groupId", groupId);
		}
		
		
		HashMap<String, Object> resultMap = null;
		if (start != null && !length.equals("-1")) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_SYSTEM_PROPERTY_TAG_VALUES, paramMap, currentPage, Integer.parseInt(length));
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_PROPERTY_TAG_VALUES, paramMap);
		}

		resultMap.put("draw", draw);
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listPropertyTags successful! the result is: " + result);
		return result;
	}

	@RequestMapping(value = "/modules", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listTagModules(
			@RequestParam(required = false, name = "tagType") String tagType,
			@RequestParam(required = true, name = "resourceType") String resourceType, 
			@RequestParam(required = false, name = "scope") String scope,
			@RequestParam(required = false, name = "nodeType") String nodeType) {
		HashMap<String, Object> paramMap = new HashMap<>();
//		parseRelationLoginIds(paramMap);
		
		if(!TAG_SCOPE_DEFAULT.equals("ALL")){
			paramMap.put("scope", scope);
		}
		if(resourceType != null){
			paramMap.put("resourceType", resourceType);
		}
		if(tagType != null){
			paramMap.put("tagType", tagType);
		}
		if(nodeType != null){
			paramMap.put("nodeType", nodeType);
		}
		
		List<HashMap<String, Object>> list = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_TAG_MODULES, paramMap);
		String result = JsonHelper.toJson(list);
		logger.debug("query listTagModules successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/{resourceId}/tags", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listResourceTags( @PathVariable String resourceId) {
		HashMap<String, Object> paramMap = new HashMap<>();
//		parseRelationLoginIds(paramMap);
		
		paramMap.put("resourceId", resourceId);
		paramMap.put("tagType", "RESOURCE");
		
		List<HashMap<String, Object>> resultMap=dbService.directSelect(DBServiceConst.SELECT_SYSTEM_RESOURCE_TAGS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceTags successful! the result is: " + result);
		return result;
	}
	
	@RequestMapping(value = "/{tagId}/resources", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listTagResources(@PathVariable String tagId, 
			@RequestParam(required = true, name = "resourceType") String resourceType, 
			@RequestParam(required = false, name = "value") String value) {
		HashMap<String, Object> paramMap = new HashMap<>();
//		parseRelationLoginIds(paramMap);
		
		paramMap.put("tagId", tagId);
		paramMap.put("resourceType", resourceType);
		
		if(value != null) {
			paramMap.put("value", value);
		}
		
		HashMap<String, Object> resultMap=dbService.select(DBServiceConst.SELECT_SYSTEM_TAG_RESOURCES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listTagResources successful! the result is: " + result);
		return result;
	}

	@RequestMapping(value = "/resource/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteTagResource( 
			@PathVariable Integer id, 
			@RequestParam(required = false, name = "value") String value, 
			@RequestParam(required = false, name = "tagId") Integer tagId, 
			@RequestParam(required = false, name = "resourceIds") String resourceIds) {
		HashMap<String, Object> paramMap = new HashMap<>();
//		parseRelationLoginIds(paramMap);
		
		if(id != -1){
			paramMap.put("id", id);
		}
		
		if(value != null){
			paramMap.put("value", value);
		}
		
		if(tagId != null){
			paramMap.put("tagId", tagId);
		}
		
		if(resourceIds != null){
			paramMap.put("resourceIds", resourceIds);
		}
		
		logger.debug("the delete tagResource param is :" + JsonHelper.toJson(paramMap));
		
		dbService.delete(DBServiceConst.DELETE_SYSTEM_TAG_RESOURCE, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteTagResource successful! ");
		return result;
	}
	
	@RequestMapping(value = "/{tagId}/resources/group", method = RequestMethod.GET)
	@ResponseBody
	public String listTagResourceGroup( @PathVariable String tagId, @RequestParam(required = false, name = "value") String value, @RequestParam(required = false, name = "name") String name) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		
		paramMap.put("tagId", tagId);
		
		if(value != null){
			paramMap.put("value", value);
		}
		
		if(name != null){
			paramMap.put("name", name);
		}
		
		List<HashMap<String, Object>> resultMap=dbService.directSelect(DBServiceConst.SELECT_SYSTEM_TAG_RESOURCE_GROUP, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listTagResourceGroup successful! the result is: " + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/type", method = RequestMethod.GET)
	@ResponseBody
	public String listTagResourceType(HttpSession session) {
		List<HashMap<String, Object>> resultMap =dbService.directSelect(DBServiceConst.SELECT_SYSTEM_TAG_RESOURCE_TYPE, new HashMap<String,Object>());
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listTags listTagResourceTypes! the result is: " + result);
		return result;
	}
}
