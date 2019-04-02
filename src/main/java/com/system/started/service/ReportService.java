package com.system.started.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.system.started.action.wrapper.VirtualActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class ReportService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private DBService zabbixDbService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VirtualActionWrapper virtualActionWrapper;
	
	public String listWorkOrderReport(HashMap<String, Object> paramMap) {
		logger.debug("the query condition is :" + JsonHelper.toJson(paramMap));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_WORKORDER, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String listPhysicalDeviceReport(HashMap<String, Object> paramMap) {
		logger.debug("the query condition is :" + JsonHelper.toJson(paramMap));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_PHYSICAL_DEVICE, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listInstanceReport(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		logger.debug("the query condition is :" + JsonHelper.toJson(paramMap));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_INSTANCE, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listBusinessInstanceReport(HashMap<String, Object> paramMap) {
		logger.debug("the query condition is :" + JsonHelper.toJson(paramMap));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_BUSINESS_INSTANCE, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportComputeResourcePoolUtilization(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String)paramMap.get("loginId");
			String regionName = (String)paramMap.get("regionName");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_LIST_HYPERVISORS_STATICS, new HashMap<>());
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String listReportStorageResourcePoolUtilization(HashMap<String, Object> paramMap) {
		logger.debug("the query condition is :" + JsonHelper.toJson(paramMap));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_STORAGE_RESOURCE_POOL_UTILIZATION, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportResource(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		logger.debug("the query condition is :" + JsonHelper.toJson(paramMap));
		String typeInstance = (String) paramMap.get("typeInstance");
		HashMap<String, Object> resultMap = null;
		if (typeInstance.equals("datacenterInstance")) {
			resultMap = dbService.select(DBServiceConst.SELECT_REPORT_DATACENTER_RESOURCE, paramMap);
		} else if (typeInstance.equals("poolInstance")) {
			resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCEPOOL_RESOURCE, paramMap);
		}

		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportResourceTask(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_TASK, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportResourceTaskInstance(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_TASK_INSTANCE, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportResourceCharge(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_RESOURCE_CHARGE, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportResourceChargeDetail(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_REPORT_RESOURCE_RESOURCE_CHARGE_DETAIL, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_RESOURCE_CHARGE_DETAIL, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportResourceChargeDetailList(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_REPORT_RESOURCE_RESOURCE_CHARGE_DETAIL_LIST, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_RESOURCE_CHARGE_DETAIL_LIST, paramMap);
		}
		queryServerUnitPrice(resultMap);
		
		return JsonHelper.toJson(resultMap);
	}
	
	private void queryServerUnitPrice(HashMap<String, Object> serverResultMap){
		List<HashMap<String,Object>>serverRecord= (List<HashMap<String, Object>>) serverResultMap.get("record");
		for (HashMap<String, Object> serverItemMap : serverRecord) {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", serverItemMap.get("nodeId"));
			List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_INSTANCE_UNIT_PRICE, paramMap);
			serverItemMap.put("unitPrice", resultList);
		}
	}
	
	public String listReportResourcePoolUsage(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_POOL_USAGE, paramMap);
		List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> record : records) {
			String regionType = (String) record.get("regionType");
			if(regionType != null && regionType.equals("POWERVC")){
//				listPVCStorages(session,record);
			}else if(regionType != null && regionType.equals("VMWARE")){
				listAvailabilityZoneHypervisorHosts(curLoginId, record);
			}
		}
		return JsonHelper.toJson(resultMap);
	}
	
	private void listAvailabilityZoneHypervisorHosts(String loginId,HashMap<String,Object> record) {
		try {
			String regionName = (String) record.get("regionName");
			HashMap<String, Object> paramMap = new HashMap<>();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId,null, EAction.VIRTUAL_LIST_HYPERVISORS, paramMap);
			logger.debug("hypervisor hosts data :"+JsonHelper.toJson(result));
			if(result.getResultCode().equals(EResultCode.SUCCESS)){
				List<HashMap<String,Object>> resultObjList = result.getResultObj();
				if(resultObjList.size()>0){
					double cpuOversub = 1;
					double ramOversub = 1;
					double diskOversub = 1;
					if(record.get("cpu_oversub") != null){
						cpuOversub = ((BigDecimal)record.get("cpu_oversub")).doubleValue();
					} 
					if(record.get("ram_oversub") != null){
						ramOversub = ((BigDecimal)record.get("ram_oversub")).doubleValue();
					} 
					if(record.get("disk_oversub") != null){
						diskOversub = ((BigDecimal)record.get("disk_oversub")).doubleValue();
					}
					
					List<HashMap<String,Object>> hypervisors = (List<HashMap<String, Object>>) resultObjList.get(0).get("hypervisors");
					HashMap<String, Object> hypervisor = (HashMap<String, Object>) hypervisors.get(0);
					record.put("vcpusUsed", hypervisor.get("vcpus_used"));
					record.put("vcpusUsedOver", Double.parseDouble(hypervisor.get("vcpus_used").toString()) * cpuOversub);
					record.put("vcpusTotal", hypervisor.get("vcpus"));
					record.put("vcpusTotalOver",Double.parseDouble(hypervisor.get("vcpus").toString()) * cpuOversub);
					record.put("vcpusUsageRateOver", Double.parseDouble(record.get("vcpusUsed").toString()) / Double.parseDouble(record.get("vcpusTotalOver").toString()) * 100);
					
					record.put("memoryUsed", hypervisor.get("memory_mb_used"));
					record.put("memoryUsedOver", Double.parseDouble(hypervisor.get("memory_mb_used").toString()) * ramOversub);
					record.put("memoryTotal", hypervisor.get("memory_mb"));
					record.put("memoryTotalOver", Double.parseDouble(hypervisor.get("memory_mb").toString()) * ramOversub);
					record.put("memoryUsageRateOver", Double.parseDouble(record.get("memoryUsed").toString()) / Double.parseDouble(record.get("memoryTotalOver").toString()) * 100);
					
					record.put("diskUsed", hypervisor.get("local_gb_used"));
					record.put("diskUsedOver", Double.parseDouble(hypervisor.get("local_gb_used").toString()) * diskOversub);
					record.put("diskTotal", hypervisor.get("local_gb"));
					record.put("diskTotalOver", Double.parseDouble(hypervisor.get("local_gb").toString()) * diskOversub);
					record.put("diskUsageRateOver", Double.parseDouble(record.get("diskUsed").toString()) / Double.parseDouble(record.get("diskTotalOver").toString()) * 100);
				}
			}
		} catch (Exception e) {
			logger.error("get vmware resource error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
		}
	}
	
	public String listReportResourceLifeCycleCount(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_LIFECYCLE_COUNT, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listReportResourceLifeCycleList(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_LIFECYCLE_LIST, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listResourceMonitorServerCount(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_MONITOR_SERVERS_COUNT, paramMap);
		
		String sortType = (String) paramMap.get("sortType");
		String sortGraphId = (String) paramMap.get("sortGraphId");
		String sortDirection = (String) paramMap.get("sortDirection");
		queryMonitorUtilizationData(sortGraphId,sortType,sortDirection,paramMap,resultMap);
		
		return JsonHelper.toJson(resultMap);
	}
	
	private void queryMonitorUtilizationData(String sortGraphId,String sortType, String sortDirection,HashMap<String, Object> paramMap, HashMap<String, Object> serverResultMap) {
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, new HashMap<String,Object>());
		List<HashMap<String, Object>> graphsList = (List<HashMap<String, Object>>) graphResultMap.get("record");
		
		List<HashMap<String,Object>>serverRecord= (List<HashMap<String, Object>>) serverResultMap.get("record");
		for (int n = 0; n < serverRecord.size(); n++) {
			HashMap<String,Object> serverMap = serverRecord.get(n);
			String monitorHostOrders= serverMap.get("hostOrders").toString();
			
			HashMap<String,HashMap<String, HashMap<String, Object>>> tempMonitorResultListMap = new HashMap<>();
			
			for (int i = 0; i < graphsList.size(); i++) {
				HashMap<String, Object> graphItem = graphsList.get(i);
				Integer graphId = (Integer) graphItem.get("id");
				
				List<HashMap<String, Object>> tempList = null;
				
				if (sortGraphId != null && paramMap.containsKey("showMonitor")) {
					paramMap.put("sortGraphId", graphId);
					List<HashMap<String, Object>> targetItemsResult = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_ITEMS, paramMap);
					if (targetItemsResult != null && targetItemsResult.get(0) != null) {
						String targetItems = (String)targetItemsResult.get(0).get("items");
						
						paramMap.put("itemIds",targetItems);
						paramMap.put("hostIds", monitorHostOrders);
						tempList = zabbixDbService.directSelect(DBServiceConst.SELECT_RESOURCE_MONITOR_UTILIZATION, paramMap);
					}
				}
				
				double maxValue = 0;
				double avgValue = 0;
				double minValue = 10000;
				for (HashMap<String, Object> item : tempList) {
					double tempMaxValue = (double) item.get("value_max");
					if(tempMaxValue > maxValue){
						maxValue = tempMaxValue;
					}
					
					double tempMinValue = (double) item.get("value_min");
					if(tempMinValue < minValue){
						minValue = tempMinValue;
					}
					
					double tempAvgValue = (double) item.get("value_avg");
					avgValue = avgValue + tempAvgValue;
				}
				
				HashMap<String, Object> monitorMap = new HashMap<String, Object>();
				monitorMap.put("value_max", maxValue);
				monitorMap.put("value_min", minValue);
				monitorMap.put("value_avg", avgValue/tempList.size());
				
				serverMap.put(String.valueOf(graphId), monitorMap);
			}
		}
		
		serverRecord.sort(new Comparator<HashMap<String, Object>>() {
			@Override
			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
				
				HashMap<String, Object> sort1Map = (HashMap<String, Object>) o1.get(sortGraphId);
				double sort1Value = (double) sort1Map.get(sortType);

				HashMap<String, Object> sort2Map = (HashMap<String, Object>) o2.get(sortGraphId);
				double sort2Value = (double) sort2Map.get(sortType);
				
				if (sortDirection.equals("desc")) {
					double tempValue = sort2Value - sort1Value;
					if(tempValue > 0) return 1 ;
					else return -1;
				}else{
					double tempValue = sort1Value - sort2Value;
					if(tempValue > 0) return 1 ;
					else return -1;
				}
			}
		});
	}
	
	public String listResourceMonitorServerCountItemDetail(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> targetItemsResult = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_ITEMS, paramMap);
		if (targetItemsResult != null && targetItemsResult.get(0) != null) {
			String targetItems = (String)targetItemsResult.get(0).get("items");
			paramMap.put("itemIds",targetItems);
			
			if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
				int length = Integer.parseInt(paramMap.get("length").toString());
				int startNum = Integer.parseInt(paramMap.get("start").toString());
				int currentPage = startNum == 0 ? 1 : startNum / length + 1;
				
				resultMap = zabbixDbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_UTILIZATION, paramMap, currentPage, length);
			} else {
				resultMap = zabbixDbService.select(DBServiceConst.SELECT_RESOURCE_MONITOR_UTILIZATION, paramMap);
			}
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listResourceMonitorServerCountDetail(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_REPORT_RESOURCE_MONITOR_SERVERS_COUNT_DETAIL, paramMap);
		
		String sortGraphId = (String) paramMap.get("sortGraphId");
		String sortType = (String) paramMap.get("sortType");
		String sortDirection = (String) paramMap.get("sortGraphId");
		querySingleMonitorData(sortGraphId,sortType,sortDirection,paramMap,resultMap);
		return JsonHelper.toJson(resultMap);
	}
	
	private void querySingleMonitorData(String sortGraphId,String sortType, String sortDirection,HashMap<String, Object> paramMap, HashMap<String, Object> serverResultMap) {
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, new HashMap<String,Object>());
		List<HashMap<String, Object>> graphsList = (List<HashMap<String, Object>>) graphResultMap.get("record");
		HashMap<String,HashMap<String, HashMap<String, Object>>> tempMonitorResultListMap = new HashMap<>();
		
		for (int i = 0; i < graphsList.size(); i++) {
			HashMap<String, Object> graphItem = graphsList.get(i);
			Integer graphId = (Integer) graphItem.get("id");
			List<HashMap<String,Object>> tempList = queryTopMonitorHost(String.valueOf(graphId),paramMap);
			HashMap<String, HashMap<String, Object>> tempMonitorResultMap = parseListToHashMap(tempList, "hostId");
			tempMonitorResultListMap.put(String.valueOf(graphId), tempMonitorResultMap);
		}
		
		List<HashMap<String,Object>>serverRecord= (List<HashMap<String, Object>>) serverResultMap.get("record");
		
		for (int n = 0; n < serverRecord.size(); n++) {
			HashMap<String,Object> serverMap = serverRecord.get(n);

			String monitorHostOrders= serverMap.get("hostOrders").toString();

			String[] monitorHostOrdersArray = monitorHostOrders.split(",");
			for (String key : tempMonitorResultListMap.keySet()) {

				double maxValue = 0;
				double totalValue = 0;
				double minValue = 10000;
				HashMap<String, Object> tempMap = new HashMap<>();
				for (int i = 0; i < monitorHostOrdersArray.length; i++) {
					String monitorHostId = monitorHostOrdersArray[i];
					
					HashMap<String, HashMap<String, Object>> tempMonitorResultMap = tempMonitorResultListMap.get(key);
					if (tempMonitorResultMap.containsKey(monitorHostId) && tempMonitorResultMap.get(monitorHostId) != null) {
						double tempMaxValue = (double) tempMonitorResultMap.get(monitorHostId).get("value_max");
						double tempAvgValue = (double) tempMonitorResultMap.get(monitorHostId).get("value_avg");
						double tempMinValue = (double) tempMonitorResultMap.get(monitorHostId).get("value_min");
						
						if (maxValue < tempMaxValue) {
							maxValue = tempMaxValue;
						}
						if (minValue > tempMinValue) {
							minValue = tempMinValue;
						}
						totalValue += tempAvgValue;
					}
				}
				tempMap.put("value_max", maxValue);
				tempMap.put("value_min", minValue);
				tempMap.put("value_avg", totalValue/monitorHostOrdersArray.length);
				serverMap.put(key, tempMap);
			}
		}
		
		serverRecord.sort(new Comparator<HashMap<String, Object>>() {
			@Override
			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
				
				HashMap<String, Object> sort1Map = (HashMap<String, Object>) o1.get(sortGraphId);
				double sort1Value = (double) sort1Map.get(sortType);

				HashMap<String, Object> sort2Map = (HashMap<String, Object>) o2.get(sortGraphId);
				double sort2Value = (double) sort2Map.get(sortType);
				
				if (sortDirection.equals("desc")) {
					double tempValue = sort2Value - sort1Value;
					if(tempValue > 0) return 1 ;
					else return -1;
				}else{
					double tempValue = sort1Value - sort2Value;
					if(tempValue > 0) return 1 ;
					else return -1;
				}
			}
		});
	}
	
	public String listResourceMonitorServerTop(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		String sortGraphId = (String) paramMap.get("sortGraphId");
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<HashMap<String,Object>> monitorResultList = queryTopMonitorHost(sortGraphId,paramMap);
		if (monitorResultList != null && monitorResultList.size() > 0) {
			List<String> sortHostIdList = new ArrayList<>();
			for (int i = 0; i < monitorResultList.size(); i++) {
				sortHostIdList.add(String.valueOf(monitorResultList.get(i).get("hostId")));
			}
			String sortHostIdsStr = StringUtils.join(sortHostIdList, ",");
			paramMap.put("hostids", sortHostIdsStr);
		}
		
		resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES, paramMap);
		parseMonitorData(sortGraphId,paramMap,resultMap,monitorResultList);
		return JsonHelper.toJson(resultMap);
	}
		
	private List<HashMap<String, Object>> queryTopMonitorHost(String sortGraphId,HashMap<String, Object> paramMap){
		if (sortGraphId != null && paramMap.containsKey("showMonitor")) {
			paramMap.put("sortGraphId", sortGraphId);
			List<HashMap<String, Object>> targetItemsResult = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_ITEMS, paramMap);
			if (targetItemsResult != null && targetItemsResult.get(0) != null) {
				String targetItems = (String)targetItemsResult.get(0).get("items");
				
				paramMap.put("itemIds",targetItems);
				List<HashMap<String, Object>> resultList = zabbixDbService.directSelect(DBServiceConst.SELECT_RESOURCE_MONITOR_HOSTORDER, paramMap);
				return resultList;
			}
		}
		return null;
	}
	
	private void parseMonitorData(String sortGraphId,HashMap<String, Object> paramMap, HashMap<String, Object> serverResultMap, List<HashMap<String, Object>> monitorResultList) {
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, new HashMap<String,Object>());
		List<HashMap<String, Object>> graphsList = (List<HashMap<String, Object>>) graphResultMap.get("record");
		HashMap<String,HashMap<String, HashMap<String, Object>>> tempMonitorResultListMap = new HashMap<>();
		
		for (int i = 0; i < graphsList.size(); i++) {
			HashMap<String, Object> graphItem = graphsList.get(i);

			Integer graphId = (Integer) graphItem.get("id");
			if (Integer.parseInt(sortGraphId) == graphId.intValue()) { //不重复查询同样的请求
				continue;
			}
			List<HashMap<String,Object>> tempList = queryTopMonitorHost(String.valueOf(graphId),paramMap);
			HashMap<String, HashMap<String, Object>> tempMonitorResultMap = parseListToHashMap(tempList, "hostId");
			tempMonitorResultListMap.put(String.valueOf(graphId), tempMonitorResultMap);
		}
		
		List<HashMap<String,Object>>serverRecord= (List<HashMap<String, Object>>) serverResultMap.get("record");
		
		HashMap<String, HashMap<String, Object>> monitorResultMap = parseListToHashMap(monitorResultList, "hostId");
		
		for (int n = 0; n < serverRecord.size(); n++) {
			HashMap<String,Object> serverMap = serverRecord.get(n);

			String monitorHostId= serverMap.get("monitorHostId").toString();
			if(monitorHostId !=null && monitorHostId != "-1"){
				if (monitorResultMap.containsKey(monitorHostId) && monitorResultMap.get(monitorHostId) != null) {
					serverMap.put(sortGraphId, monitorResultMap.get(monitorHostId));
				}
				
				for (String key : tempMonitorResultListMap.keySet()) {
					HashMap<String, HashMap<String, Object>> tempMonitorResultMap = tempMonitorResultListMap.get(key);
					if (tempMonitorResultMap.containsKey(monitorHostId) && tempMonitorResultMap.get(monitorHostId) != null) {
						serverMap.put(key, tempMonitorResultMap.get(monitorHostId));
					}
				}
			}
		}
	}
	
	public String listResourceMonitorServerTopDetail(HashMap<String, Object> paramMap) {
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		
		String sortGraphId = (String) paramMap.get("sortGraphId");
		int hostId = Integer.parseInt(paramMap.get("hostId").toString());
		
		int startNum = Integer.parseInt(paramMap.get("start").toString());
		int length = Integer.parseInt(paramMap.get("length").toString());
		
		int currentPage = startNum == 0 ? 1 : startNum / length + 1;
		HashMap<String, Object> resultMap = queryTopMonitorHostList(sortGraphId,paramMap,hostId,currentPage, length);
		return JsonHelper.toJson(resultMap);
	}
	
	private HashMap<String, Object> queryTopMonitorHostList(String sortGraphId,HashMap<String, Object> paramMap, Integer hostId, Integer currentPage, Integer perPage){
		if (sortGraphId != null && paramMap.containsKey("showMonitor")) {
			paramMap.put("sortGraphId", sortGraphId);
			List<HashMap<String, Object>> targetItemsResult = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_ITEMS, paramMap);
			if (targetItemsResult != null && targetItemsResult.get(0) != null) {
				String targetItems = (String)targetItemsResult.get(0).get("items");
				
				paramMap.put("itemIds",targetItems);
				paramMap.put("hostId", hostId);
				HashMap<String, Object> resultMap = zabbixDbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MONITOR_HOSTORDER_LIST, paramMap, currentPage, perPage);
				return resultMap;
			}
		}
		return null;
	}
	
	protected HashMap<String, HashMap<String, Object>> parseListToHashMap(List<HashMap<String, Object>> tempList, String keyword) {
		HashMap<String, HashMap<String, Object>> resultMap = new HashMap<>();

		for (int i = 0; i < tempList.size(); i++) {
			HashMap<String, Object> tempItem = tempList.get(i);
			resultMap.put(String.valueOf(tempItem.get(keyword)), tempItem);
		}
		return resultMap;
	}
}
