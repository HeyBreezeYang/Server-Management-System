package com.system.started.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.JsonHelper;

@Component
public class AssetService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(AssetService.class);

	@Autowired
	private DBService dbService;

	public String listAssetType(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_ASSET_TYPE, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_ASSET_TYPE, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAssetType successful! the result is :" + result);
		return result;
	}
	
	public String createAssetType(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.INSERT_ASSET_TYPE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产类型创建成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create assetType successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create assetType error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产类型创建失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String updateAssetType(HashMap<String, Object> paramMap) {
		try {
			dbService.update(DBServiceConst.UPDATE_ASSET_TYPE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产类型编辑成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update assetType successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("update assetType error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产类型编辑失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String deleteAssetType(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_ASSET_TYPE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产类型删除成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete assetType successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("delete assetType error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产类型删除失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String listAssetLifecycles(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_ASSET_LIFECYCLES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_ASSET_LIFECYCLES, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAssetLifecycle successful! the result is :" + result);
		return result;
	}
	
	public String createAssetLifecycle(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.INSERT_ASSET_LIFECYCLE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产生命周期创建成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create assetLifecycle successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create assetLifecycle error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产生命周期创建失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String updateAssetLifecycle(HashMap<String, Object> paramMap) {
		try {
			dbService.update(DBServiceConst.UPDATE_ASSET_LIFECYCLE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产生命周期编辑成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update assetLifecycle successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("update assetLifecycle error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产生命周期编辑失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String deleteAssetLifecycle(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_ASSET_LIFECYCLE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产生命周期删除成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete assetLifecycle successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("delete assetLifecycle error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产生命周期删除失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String listAssetOverall(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_ASSET_OVERALL, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAssetOverall successful! the result is :" + result);
		return result;
	}
	
	public String listAssetNoteLifecycle(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_ASSET_NOTE_LIFECYCLE, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAssetNoteLifecycle successful! the result is :" + result);
		return result;
	}
	
	public String createAssetNoteLifecycle(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.INSERT_ASSET_INFO_FIELD_TEMPLATE_INSTANCE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "记录生命周期成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create asset successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create asset error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "记录生命周期失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String listAssetFieldTemplates(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_ASSET_FIELD_TEMPLATES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_ASSET_FIELD_TEMPLATES, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAssetFieldTemplates successful! the result is :" + result);
		return result;
	}
	
	public String createAssetFieldTemplate(HashMap<String, Object> paramMap) {
		try {
			String fieldName = CommonUtil.createFieldName();
			paramMap.put("fieldName", fieldName);
			dbService.insert(DBServiceConst.INSERT_ASSET_FIELD_TEMPLATE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "自定义字段创建成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create assetFieldTemplate successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create assetFieldTemplate error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "自定义字段创建失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String updateAssetFieldTemplate(HashMap<String, Object> paramMap) {
		try {
			dbService.update(DBServiceConst.UPDATE_ASSET_FIELD_TEMPLATE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "自定义字段编辑成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update assetFieldTemplate successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("update assetFieldTemplate error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "自定义字段编辑失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String deleteAssetFieldTemplate(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_ASSET_FIELD_TEMPLATE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "自定义字段删除成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete assetFieldTemplate successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("delete assetFieldTemplate error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "自定义字段删除失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String listAssetInfo(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_ASSET_INFO, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_ASSET_INFO, paramMap);
		}
		
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>>records = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> itemMap : records) {
			List<HashMap<String, Object>> lifeCycleGroups = dbService.directSelect(DBServiceConst.SELECT_ASSET_INFO_LIFECYCLE_GROUPS, itemMap);
			List<HashMap<String, Object>> lifeCycleFieldValues = new ArrayList<HashMap<String, Object>>();
			for (HashMap<String, Object> lifeCycleMap : lifeCycleGroups) {
				HashMap<String, Object> lifeCycleGroup = new HashMap<String, Object>();
				lifeCycleMap.put("assetId", itemMap.get("id"));
				List<HashMap<String, Object>> itemFieldValues = dbService.directSelect(DBServiceConst.SELECT_ASSET_INFO_LIFECYCLE_VALUES, lifeCycleMap);
				for (HashMap<String, Object> itemFieldValueMap : itemFieldValues) {
					lifeCycleGroup.put(lifeCycleMap.get("updateDate").toString(), itemFieldValueMap.get("lifeCycleFieldValues"));
				}
				lifeCycleFieldValues.add(lifeCycleGroup);
			}
			itemMap.put("lifeCycleFieldValues", lifeCycleFieldValues);
		}
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAssetInfo successful! the result is :" + result);
		return result;
	}
	
	public String createAssetInfo(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.INSERT_ASSET_INFO, paramMap);
			if(paramMap.containsKey("fieldValues")) {
				dbService.insert(DBServiceConst.INSERT_ASSET_INFO_FIELD_TEMPLATE_INSTANCE, paramMap);
			}
			
			// 记录操作日志
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			String content = "新增一台名称为\"" + paramMap.get("name") + "\"";
			parameters.put("content", content);
			parameters.put("loginId", paramMap.get("loginId"));
			parameters.put("assetId", paramMap.get("assetId"));
			parameters.put("type", "ASSET_INFO");
			
			dbService.insert(DBServiceConst.INSERT_ASSET_INFO_LOG, parameters);
						
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产添加成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create asset successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create asset error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产添加失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String updateAssetInfo(HashMap<String, Object> paramMap) {
		try {
			List<HashMap<String, Object>> dbFieldValues = dbService.directSelect(DBServiceConst.SELECT_ASSET_INFO_FIELD_TEMPLATE_INSTANCES, paramMap);
			
			List<HashMap<String, Object>> fieldValueResultList = null;
			if(paramMap.containsKey("fieldValues")) {
				fieldValueResultList = compareFieldValue((List<HashMap<String, Object>>)paramMap.get("fieldValues"), dbFieldValues);
			}
			
			HashMap<String, Object> assetInfoParamMap = new HashMap<String, Object>();
			assetInfoParamMap.put("id", paramMap.get("id"));
			List<HashMap<String, Object>> assetInfos = dbService.directSelect(DBServiceConst.SELECT_ASSET_INFO, assetInfoParamMap);
			HashMap<String, Object> dbAssetInfo = assetInfos.get(0);
			List<HashMap<String, Object>> baseFieldValueResultList = compareBaseFieldValue(paramMap, dbAssetInfo);
			
			dbService.update(DBServiceConst.UPDATE_ASSET_INFO, paramMap);
			
			// 记录操作日志
			if(fieldValueResultList != null) {
				dbService.update(DBServiceConst.UPDATE_ASSET_INFO_FIELD_TEMPLATE_INSTANCE, paramMap);
				
				for (HashMap<String, Object> itemMap : fieldValueResultList) {
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					String content = itemMap.get("label") + " 从  " + itemMap.get("oldValue") + " 变更为 " + itemMap.get("value");
					parameters.put("content", content);
					parameters.put("loginId", paramMap.get("loginId"));
					parameters.put("assetId", paramMap.get("id"));
					parameters.put("type", "TYPE");
					
					dbService.insert(DBServiceConst.INSERT_ASSET_INFO_LOG, parameters);
				}
			}
			
			for (HashMap<String, Object> itemMap : baseFieldValueResultList) {
				HashMap<String, Object> parameters = new HashMap<String, Object>();
				String content = itemMap.get("label") + " 从  " + itemMap.get("oldValue") + " 变更为 " + itemMap.get("value");
				parameters.put("content", content);
				parameters.put("loginId", paramMap.get("loginId"));
				parameters.put("assetId", paramMap.get("id"));
				parameters.put("type", "TYPE");
				
				dbService.insert(DBServiceConst.INSERT_ASSET_INFO_LOG, parameters);
			}
			
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产编辑成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update assetInfo successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("update assetInfo error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产编辑失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String deleteAssetInfo(HashMap<String, Object> paramMap) {
		try {
			List<HashMap<String, Object>> assetResultList = dbService.directSelect(DBServiceConst.SELECT_ASSET_INFO, paramMap);			
			dbService.delete(DBServiceConst.DELETE_ASSET_INFO, paramMap);
			
			// 记录操作日志
			String content = "未找到设备（ID="+ paramMap.get("id") +"）";
			if(assetResultList.size()>0) {
				HashMap<String, Object> assetResultMap = assetResultList.get(0);
				content = "删除一台名称为\"" + assetResultMap.get("name") + "\"";
			}
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("content", content);
			parameters.put("loginId", paramMap.get("loginId"));
			parameters.put("assetId", paramMap.get("id"));
			parameters.put("type", "ASSET_INFO");
			
			dbService.insert(DBServiceConst.INSERT_ASSET_INFO_LOG, parameters);
						
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "资产删除成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete asset successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("delete asset error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "资产删除失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String listAssetInfoLogs(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_ASSET_INFO_LOGS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_ASSET_INFO_LOGS, paramMap);
		}
		
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>>records = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> itemMap : records) {
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			String createDate = (String) itemMap.get("createDate");
			parameters.put("startCreateDate", createDate + " 00:00:00");
			parameters.put("endCreateDate", createDate + " 23:59:59");
			
			if(paramMap.get("key") != null) {
				parameters.put("key", paramMap.get("key"));
			}
			
			if(paramMap.get("typeId") != null) {
				parameters.put("typeId", paramMap.get("typeId"));
			}
			List<HashMap<String, Object>> logs = dbService.directSelect(DBServiceConst.SELECT_ASSET_INFO_LOG_DETAILS, parameters);
			itemMap.put("logs", logs);
		}
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listAssetInfoLogs successful! the result is :" + result);
		return result;
	}
}
