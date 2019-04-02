package com.system.started.service;

import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.*;
import com.system.started.action.wrapper.MonitorActionWrapper;
import com.system.started.action.wrapper.ResourceActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.monitor.EMonitorPanelType;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class MonitorService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(MonitorService.class);

	@Autowired
	private DBService dbService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DBService zabbixDbService;

	@Autowired
	private MonitorActionWrapper monitorActionWrapper;
	
	@Autowired
	private ResourceActionWrapper resourceActionWrapper;
	
	public String getHostPanel(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_GET_HOST_PANEL, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String getHostReport(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_GET_HOST_REPORT, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listHostEvents(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_EVENTS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_EVENTS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listMonitorTemplates(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_TEMPLATES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_TEMPLATES, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateZabbixTemplate(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_UPDATE_TEMPLATE,regionName,paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String createZabbixTemplate(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_CREATE_TEMPLATE,regionName,paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	//创建监控项
	public String createZabbixItem(HashMap<String, Object> paramMap) {
		try {
			String regionName=getMonitorDefaultRegion();
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_CREATE_ITEM, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	//删除监控项
	public String deleteZabbixItem(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_DELETE_ITEM, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	//编辑监控项
	public String updateZabbixItem(HashMap<String, Object> paramMap) {
		try {
			String regionName=getMonitorDefaultRegion();
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_UPDATE_ITEM, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String deleteZabbixTemplate(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_DELETE_TEMPLATE,regionName,paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listMonitorTemplateItems(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_TEMPLATE_ITEMS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_TEMPLATE_ITEMS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listMonitorTemplateTriggers(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_TEMPLATE_TRIGGERS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_TEMPLATE_TRIGGERS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateMonitorTemplateItem(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_RESOURCE_MONITOR_TEMPLATE_ITEM, paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateMonitorTemplateItemStatus(HashMap<String, Object> paramMap) {
		try {
			Integer itemId = (Integer) paramMap.get("itemId");
			String regionName = getMonitorDefaultRegion();

			String loginId = (String) paramMap.get("loginid");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_UPDATE_ITEM, regionName, paramMap);

			if(result.getResultCode().equals(EResultCode.SUCCESS)){
				// OSS下发成功后，更新数据库状态
				// 更新关联的host item
				paramMap.put("parentItemId", itemId);
				dbService.update(DBServiceConst.UPDATE_RESOURCE_MONITOR_HOST_ITEM, paramMap);
				// 更新template item
//				paramMap.put("itemId", itemId);
				dbService.update(DBServiceConst.UPDATE_RESOURCE_MONITOR_TEMPLATE_ITEM, paramMap);
				logger.debug("update updateMonitorTemplateItemStatus successful! ");
			}
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listMonitorHostItems(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_ITEMS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_ITEMS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listMonitorHostTriggers(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_TRIGGERS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_TRIGGERS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateMonitorHostItemStatus(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_RESOURCE_MONITOR_HOST_ITEM, paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String listMonitorHostTemplates(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_TEMPLATES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_TEMPLATES, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String createZabbixTrigger(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();

			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_CREATE_TRIGGER,regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String updateZabbixTrigger(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();

			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_UPDATE_TRIGGER, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String deleteZabbixTrigger(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();

			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_DELETE_TRIGGER, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String createLinkHostToTemplate(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();
			
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_ATTACH_TEMPLATE_TO_HOST, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String deleteLinkHostToTemplate(HashMap<String, Object> paramMap) {
		try {
			String regionName = getMonitorDefaultRegion();
			
			String loginId = (String)paramMap.get("loginId");
			Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_DETACH_TEMPLATE_TO_HOST, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listMonitorNodes(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		String sortGraphId = (String)paramMap.get("sortGraphId");
		String showMonitor = (String)paramMap.get("showMonitor");
		
		List<String> sortHostIdList = queryTopMonitorHost(sortGraphId,paramMap);

		if (sortHostIdList != null && sortHostIdList.size() > 0) {
			String sortHostIdsStr = StringUtils.join(sortHostIdList, ",");
			paramMap.put("hostids", sortHostIdsStr);
		}
		
		HashMap<String, Object> resultMap = null;
		
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_NODES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_NODES, paramMap);
		}
		
		try {
			if(showMonitor != null){
				queryMonitorData(curLoginId, resultMap);
			}
		} catch (Exception e) {
			logger.error("query monitor data error !", e);
		}
		
		return JsonHelper.toJson(resultMap);
	}
	
	private List<String> queryTopMonitorHost(String sortGraphId,HashMap<String, Object> paramMap){
		List<String> realList = new ArrayList<>();
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
			List<HashMap<String, Object>> targetItemsResult = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_ITEMS, paramMap);
			if (targetItemsResult != null && targetItemsResult.get(0) != null) {
				String targetItems = (String)targetItemsResult.get(0).get("items");

				paramMap.put("itemIds",targetItems);
				List<HashMap<String, Object>> resultList = zabbixDbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_HOSTORDER, paramMap);
				for (int i = 0; i < resultList.size(); i++) {
					realList.add(String.valueOf(resultList.get(i).get("hostId")));
				}
			}
		}
		return realList;
	}
	
	private void queryMonitorData(String loginId, HashMap<String, Object> serverResultMap){
		List<HashMap<String,Object>>serverRecord= (List<HashMap<String, Object>>) serverResultMap.get("record");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, new HashMap<String,Object>());
		for (int n = 0; n < serverRecord.size(); n++) {
			HashMap<String,Object>serverMap= serverRecord.get(n);

			Integer monitorHostId= Integer.parseInt(serverMap.get("hostid").toString());
			if(monitorHostId !=null && monitorHostId != -1){

				parseMonitorGraphs(monitorHostId, graphResultMap);
				List<HashMap<String,Object>>graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
				for (int i = 0; i < graphRecord.size(); i++) {
					HashMap<String,Object> graph = graphRecord.get(i);
					List<HashMap<String,Object>>refItems= (List<HashMap<String, Object>>) graph.get("refItems");

					List<Integer> itemList = new ArrayList<>();
					for (int j = 0; j < refItems.size(); j++) {
						HashMap<String,Object>refItem= refItems.get(j);
						itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
					}

					HashMap<String, Object> actionParamMap = new HashMap<>();
					actionParamMap.put("endTime", new Date().getTime()/1000);
					actionParamMap.put("itemList", itemList);
					actionParamMap.put("panelType", EMonitorPanelType.MULITY);
					actionParamMap.put("length", 1);

					try {
						Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
						if(result.getResultCode().equals(EResultCode.SUCCESS)){
							List<HashMap<String,Object>>resultList= result.getResultObj();
							HashMap<String,Object>itemDataMap= resultList.get(0);
							List<String> itemKeys= new ArrayList<String>(itemDataMap.keySet());

							for (int j = 0; j < itemKeys.size(); j++) {
								List<HashMap> dataList= (List<HashMap>) itemDataMap.get(itemKeys.get(j));
								HashMap<String,Object>dataMap= dataList.get(0);
								serverMap.put((String)graph.get("name"), dataMap.get("value"));
							}
						}
					} catch (Exception e) {
						logger.error("query vm monitor data error", e);
					}
				}
			}
		}
	}
	
	public String createMonitorNodesGraphs(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId,null, EAction.RESOURCE_CONFIG_MONITOR_REPORT, paramMap);
			logger.debug("config server agent result :"+JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String createMonitorMultyNodesGraphs(HashMap<String, Object> paramMap) {
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> nodeObjList = (List<HashMap<String, Object>>) paramMap.get("nodeObjs");
		for (int i = 0; i < nodeObjList.size(); i++) {
			HashMap<String, Object> nodeObj = nodeObjList.get(i);
			paramMap.put("nodeId", nodeObj.get("nodeId"));
			createMonitorNodesGraphs(paramMap);
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String listAllMonitorGraphs(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_ALL_MONITOR_GRAPHS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_ALL_MONITOR_GRAPHS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
		
	public String listMonitorGraphTemplateItems(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_MONITOR_GRAPH_TEMPLATE_ITEMS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_MONITOR_GRAPH_TEMPLATE_ITEMS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String createMonitorGraphs(HashMap<String, Object> paramMap) {
		dbService.insert(DBServiceConst.INSERT_MONITOR_GRAPH, paramMap);

		Integer graphId = (Integer) paramMap.get("graphId");
		List<HashMap<String, Object>> refItemsList = (List<HashMap<String, Object>>) paramMap.get("refItems");
		if (refItemsList != null) {
			paramMap.put("valueSqlString", parseRefItems(graphId, refItemsList));
			dbService.insert(DBServiceConst.INSERT_MONITOR_GRAPH_ITEMS, paramMap);
		}
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateMonitorGraphInfo(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_MONITOR_GRAPH, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateMonitorGraphItems(HashMap<String, Object> paramMap) {
		int graphId = (int) paramMap.get("graphId");
		dbService.delete(DBServiceConst.DELETE_MONITOR_GRAPH_ITEMS, paramMap);

		List<HashMap<String, Object>> refItemsList = (List<HashMap<String, Object>>) paramMap.get("refItems");
		if (refItemsList.size() > 0) {
			paramMap.put("valueSqlString", parseRefItems(graphId, refItemsList));
			dbService.insert(DBServiceConst.INSERT_MONITOR_GRAPH_ITEMS, paramMap);
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String deleteMonitorGraph(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_MONITOR_GRAPH, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String listMonitorNodesGraphs(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_GRAPHS, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listMonitorNodesGraphsItems(HashMap<String, Object> paramMap) {
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_GRAPHS, paramMap);
		
		int hostId = (int) paramMap.get("hostId");
		parseMonitorGraphs(hostId, graphResultMap);
		return JsonHelper.toJson(graphResultMap);
	}
	
	public String listMonitorVirtuals(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_VIRTUALS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_VIRTUALS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listMonitorEvents(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_EVENTS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_HOST_EVENTS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String ackMonitorEvent(HashMap<String, Object> paramMap) {
		dbService.insert(DBServiceConst.ACK_RESOURCE_MONITOR_EVENTS, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String listAllEventReports(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_ALL_EVENT_REPORTS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_ALL_EVENT_REPORTS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String createEventReport(HashMap<String, Object> paramMap) {
		dbService.insert(DBServiceConst.INSERT_EVENT_REPORT, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateEventReport(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_EVENT_REPORT, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String deleteEventReport(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_EVENT_REPORT, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateEventReportTriggers(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_EVENT_REPORT_TRIGGERS, paramMap);
		int reportId = (int) paramMap.get("reportId");
		List<HashMap<String, Object>> refTriggersList = (List<HashMap<String, Object>>) paramMap.get("refTriggers");
		if (refTriggersList.size() > 0) {
			paramMap.put("valueSqlString", parseRefTriggers(reportId, refTriggersList));
			dbService.insert(DBServiceConst.INSERT_EVENT_REPORT_TRIGGERS, paramMap);
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String listEventReportTriggers(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_EVENT_REPORT_TRIGGERS, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateMonitorHostStatus(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			String regionName = getMonitorDefaultRegion();
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_CHANGE_HOST_STATUS, regionName, paramMap);
			if(result.getResultCode().equals(EResultCode.SUCCESS)) {
				paramMap.put("zabbixAgentStatus", paramMap.get("status"));
				dbService.update(DBServiceConst.UPDATE_MONITOR_HOST, paramMap);
			}
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("update monitor host status error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String createMonitorWebScene(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			String regionName = getMonitorDefaultRegion();
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_CREATE_WEB_SCENE, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("create monitor web scene error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String deleteMonitorWebScene(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			String regionName = getMonitorDefaultRegion();
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_DELETE_WEB_SCENE, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("delete monitor web scene error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String updateMonitorWebScene(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			String regionName = getMonitorDefaultRegion();
			Result result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_UPDATE_WEB_SCENE, regionName, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("update monitor web scene error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String updateMonitorWebSceneStatus(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			String regionName = getMonitorDefaultRegion();
			String monitorType = (String) paramMap.get("monitorType");
			Result result = null;
			if(monitorType.equals("PING")) {
				result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_UPDATE_ITEM, regionName, paramMap);
			}else {
				result = monitorActionWrapper.doExcutionAction(loginId,EAction.MONITOR_CHANGE_WWBSCENE_STATUS, regionName, paramMap);
			}
			
			if(result.getResultCode().equals(EResultCode.SUCCESS)) {
				paramMap.put("disabled", paramMap.get("status"));
				dbService.update(DBServiceConst.UPDATE_MONITOR_WEB_SCENE, paramMap);
			}
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("update monitor host status error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	private String parseRefItems(Integer graphId, List<HashMap<String, Object>> refItemsList) {
		StringBuffer selectStringBuffer = new StringBuffer();
		for (int i = 0; i < refItemsList.size(); i++) {
			HashMap<String, Object> itemObj = refItemsList.get(i);
			String itemIdStr = (String) itemObj.get("itemId");
			if (itemObj.containsKey("param")) {
				String paramsStr = String.valueOf(itemObj.get("param"));
				String[] paramsArray = paramsStr.split(",");
				for (int j = 0; j < paramsArray.length; j++) {
					selectStringBuffer.append("select ");
					selectStringBuffer.append(graphId);
					selectStringBuffer.append(",");
					selectStringBuffer.append(itemIdStr);
					selectStringBuffer.append(",'");
					selectStringBuffer.append(paramsArray[j]);
					selectStringBuffer.append("'");
					if (i == refItemsList.size() - 1 && j == paramsArray.length - 1) {
						continue;
					} else {
						selectStringBuffer.append(" union all ");
					}
				}
			} else {
				selectStringBuffer.append("select ");
				selectStringBuffer.append(graphId);
				selectStringBuffer.append(",");
				selectStringBuffer.append(itemIdStr);
				selectStringBuffer.append(",null");
				if (i < refItemsList.size() - 1) {
					selectStringBuffer.append(" union all ");
				}
			}
		}
		return selectStringBuffer.toString();
	}

	private String parseRefTriggers(Integer reportId, List<HashMap<String, Object>> refTriggersList) {
		StringBuffer selectStringBuffer = new StringBuffer();
		for (int i = 0; i < refTriggersList.size(); i++) {
			HashMap<String, Object> triggerObj = refTriggersList.get(i);
			String triggerIdStr = (String) triggerObj.get("triggerId");
			if (triggerObj.containsKey("param")) {
				String paramsStr = String.valueOf(triggerObj.get("param"));
				String[] paramsArray = paramsStr.split(",");
				for (int j = 0; j < paramsArray.length; j++) {
					selectStringBuffer.append("select ");
					selectStringBuffer.append(reportId);
					selectStringBuffer.append(",");
					selectStringBuffer.append(triggerIdStr);
					selectStringBuffer.append(",'");
					selectStringBuffer.append(paramsArray[j]);
					selectStringBuffer.append("'");
					if (i == refTriggersList.size() - 1 && j == paramsArray.length - 1) {
						continue;
					} else {
						selectStringBuffer.append(" union all ");
					}
				}
			} else {
				selectStringBuffer.append("select ");
				selectStringBuffer.append(reportId);
				selectStringBuffer.append(",");
				selectStringBuffer.append(triggerIdStr);
//				selectStringBuffer.append(",null");
				if (i < refTriggersList.size() - 1) {
					selectStringBuffer.append(" union all ");
				}
			}
		}
		return selectStringBuffer.toString();
	}
	
	private void parseMonitorGraphs(Integer hostId, HashMap<String, Object> graphResultMap) {
		List<HashMap<String, Object>> graphsList = (List<HashMap<String, Object>>) graphResultMap.get("record");
		for (int i = 0; i < graphsList.size(); i++) {
			HashMap<String, Object> graphItem = graphsList.get(i);

			Integer graphId = (Integer) graphItem.get("id");

			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("graphId", graphId);
			paramMap.put("hostId", hostId);
			
			List<HashMap<String, Object>> graphRefItemsList = null;
			if(graphItem.containsKey("discovery") && Integer.parseInt(graphItem.get("discovery").toString()) == 1) {
				graphRefItemsList = dbService.directSelect(DBServiceConst.SELECT_MONITOR_DISCOVERY_GRAPH_ITEMS, paramMap);
			}else {
				graphRefItemsList = dbService.directSelect(DBServiceConst.SELECT_MONITOR_GRAPH_ITEMS, paramMap);
			}
			graphItem.put("refItems", graphRefItemsList);
		}
	}
	
	private String getMonitorDefaultRegion(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("engineType", "ZABBIX");

		String regionName = null;

		List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_ENGINE_REGIONS, paramMap);
		if(resultList.size()>0){
			regionName = (String) resultList.get(0).get("region_name");
		}

		logger.debug("list monitor default region : "+regionName);
		return regionName;
	}

	public String listMonitorNetwork(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NETWORK, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String createMonitorNetwork(HashMap<String, Object> paramMap) {
		dbService.insert(DBServiceConst.INSERT_MONITOR_NETWORK, paramMap);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createMonitorNetwork successful! ");
		return result;
	}

	public String updateMonitorNetwork(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_MONITOR_NETWORK, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateMonitorNetwork successful! ");
		return result;
	}

	
	public String deleteMonitorNetwork(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_MONITOR_NETWORK, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteMonitorNetwork successful! ");
		return result;
	}

	public String listdhHistoryRoom(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap =dbService.select(DBServiceConst.SELECT_DH_ROOM_HISTORY, paramMap);
		return JsonHelper.toJson(resultMap);
	}


	
	
}
