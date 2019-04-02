package com.system.started.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.wrapper.MonitorActionWrapper;
import com.system.started.action.wrapper.ResourceActionWrapper;
import com.system.started.action.wrapper.VirtualActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.ZabbixDBService;
import com.system.started.elasticsearch.ElasticsearchService;
import com.system.started.elasticsearch.EsPage;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.monitor.EMonitorPanelType;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class DataCenterService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(DataCenterService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private ZabbixDBService zabbixDbService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ElasticsearchService elasticsearchService;

	@Autowired
	ResourceActionWrapper resourceActionWrapper;

	@Autowired
	VirtualActionWrapper virtualActionWrapper;

	@Autowired
	MonitorActionWrapper monitorActionWrapper;

	public String listDict(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DICT, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenters dict successful! ");
		return result;
	}

	public String listRecuDict(HashMap<String, Object> paramMap){
		List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_RECU_DICT, paramMap);
		parseDictList(resultList);
		String result = JsonHelper.toJson(resultList);
		logger.debug("query listRecuDict dict successful! ");
		return result;
	}

	private List<HashMap<String, Object>> parseDictList(List<HashMap<String, Object>> resultList) {
		for (int i = 0; i < resultList.size(); i++) {
			HashMap<String,Object> resultMap = resultList.get(i);

			HashMap<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("parentId", resultMap.get("id"));
			List<HashMap<String, Object>> subResultList = dbService.directSelect(DBServiceConst.SELECT_RECU_DICT, paramMap);
			if(subResultList.size()>0){
				resultList.addAll(subResultList);
				parseDictList(subResultList);
			}else{
				continue;
			}
		}
		return resultList;
	}

	public String listDataCenters(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap=dbService.selectByPage(DBServiceConst.SELECT_DATACENTERS, paramMap,currentPage, length);
		}else{
			resultMap=dbService.select(DBServiceConst.SELECT_DATACENTERS, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenters successful! ");
		return result;
	}

	public String listDataCenterDetails(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DATACENTERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenters successful! ");
		return result;
	}

	public String createDataCenter(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId,null, EAction.RESOURCE_CREATE_DATACENTER, paramMap);
			logger.debug("create datacenter result :"+JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateDataCenter(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId,null, EAction.RESOURCE_UPDATE_DATACENTER, paramMap);
			logger.debug("update datacenter result :"+JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteDataCenter(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId,null, EAction.RESOURCE_DELETE_DATACENTER, paramMap);
			logger.debug("update datacenter result :"+JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listDataCenterUsers(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_USERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterUsers successful! ");
		return result;
	}

	public String listDataCenterUserDetails(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_USERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterUserDetails successful! ");
		return result;
	}

	public String updateDataCenterUser(HashMap<String, Object> paramMap){
		dbService.update(DBServiceConst.UPDATE_DATACENTER_USER, paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateDataCenterUser successful! ");
		return result;
	}

	public String deleteDataCenterUser(HashMap<String, Object> paramMap){
		dbService.delete(DBServiceConst.DELETE_DATACENTER_USER, paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteDataCenterUser successful! ");
		return result;
	}

	public String listDataCenterArea(HashMap<String, Object> paramMap){
		String selectName=DBServiceConst.SELECT_DATACENTER_AREA;
		if(paramMap.get("type") != null && paramMap.get("type").toString().equals("group")){
			selectName=DBServiceConst.SELECT_DATACENTER_AREA_GROUP;
		}
		HashMap<String, Object> resultMap = dbService.select(selectName, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterArea successful! ");
		return result;
	}

	public String listDataCenterAreaCabinetNum(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_AREA_CABINETNUM, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterAreaCabinetNum successful! ");
		return result;
	}

	public String listDataCenterCabinetResourceNodeCount(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_CABINET_RESOURCENODE_COUNT, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterCabinetResourceNodeCount successful! ");
		return result;
	}

	public String createDataCenterArea(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_CREATE_DATACENTER_AREA, paramMap);
			logger.debug("create datacenter area result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateDataCenterArea(HashMap<String, Object> paramMap){
		dbService.update(DBServiceConst.UPDATE_DATACENTER_AREA, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateDataCenterArea successful! ");
		return result;
	}

	public String deleteDataCenterArea(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_DELETE_DATACENTER_AREA, paramMap);
			logger.debug("delete datacenter area result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listDataCenterGroups(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_DATACENTER_GROUPS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_GROUPS, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterGroups successful! ");
		return result;
	}

	public String listDataCenterGroupDetails(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_GROUPS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterGroupDetails successful! ");
		return result;
	}

	public String createDataCenterGroup(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_CREATE_DATACENTER_GROUP, paramMap);
			logger.debug("create datacenter group result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateDataCenterGroup(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_UPDATE_DATACENTER_GROUP, paramMap);
			logger.debug("update datacenter group result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteDataCenterGroup(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_DELETE_DATACENTER_GROUP, paramMap);
			logger.debug("delete datacenter group result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listResourcePools(HashMap<String, Object> paramMap){
		String curLoginId = paramMap.getOrDefault("curLoginId", "").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_POOLS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_POOLS, paramMap);
		}

		List<HashMap<String, Object>> recordList = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> recordItemMap : recordList) {
			String regionType = (String) recordItemMap.get("regionType");
			if (regionType != null && regionType.equals("VMWARE")) {
				listAvailabilityZoneHypervisorHosts(curLoginId , recordItemMap);
			}
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourcePools successful! ");
		return result;
	}

	private void listPVCStorages(String loginId, HashMap<String, Object> record) {
		try {
			String regionName = (String) record.get("region");
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			if (record.containsKey("hostName")) {
				paramMap.put("host_name", record.get("hostName"));
			}
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_EXTENSION_LIST_STORAGE_CONTROLLER, paramMap);
			logger.debug("powervc storage data :" + JsonHelper.toJson(result));
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				List<HashMap<String, Object>> resultObjList = result.getResultObj();
				if (resultObjList.size() > 0) {
					List<HashMap<String, Object>> hosts = (List<HashMap<String, Object>>) resultObjList.get(0).get("hosts");
					List<HashMap<String, Object>> detail = (List<HashMap<String, Object>>) hosts.get(0).get("detail");
					HashMap<String, Object> resource = (HashMap<String, Object>) detail.get(0).get("resource");
					double total_capacity_gb = Double.parseDouble(resource.get("total_capacity_gb").toString());
					double free_capacity_gb = Double.parseDouble(resource.get("free_capacity_gb").toString());
					record.put("diskUsed", total_capacity_gb - free_capacity_gb);
					record.put("diskTotal", total_capacity_gb);
				}
			}
		} catch (Exception e) {
			logger.error("get powervc storage error !", e);
		}
	}

	private void listAvailabilityZoneHypervisorHosts(String loginId, HashMap<String, Object> record) {
		try {
			String regionName = (String) record.get("regionName");
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("regionName", regionName);
			List<HashMap<String, Object>> aggregateList = dbService.directSelect(DBServiceConst.SELECT_HOST_AGGREGATES, paramMap);
			HashMap<String, Object> aggregateMap = new HashMap<String, Object>();
			if (aggregateList.size() > 0) {
				aggregateMap = aggregateList.get(0);
			}

			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_LIST_HYPERVISORS, new HashMap<String, Object>());
			logger.debug("hypervisor hosts data :" + JsonHelper.toJson(result));
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				List<HashMap<String, Object>> resultObjList = result.getResultObj();
				if (resultObjList.size() > 0) {
					double cpuOversub = 1;
					double ramOversub = 1;
					double diskOversub = 1;
					if (aggregateMap.get("cpu_oversub") != null) {
						cpuOversub = ((BigDecimal) aggregateMap.get("cpu_oversub")).doubleValue();
					}
					if (aggregateMap.get("ram_oversub") != null) {
						ramOversub = ((BigDecimal) aggregateMap.get("ram_oversub")).doubleValue();
					}
					if (aggregateMap.get("disk_oversub") != null) {
						diskOversub = ((BigDecimal) aggregateMap.get("disk_oversub")).doubleValue();
					}

					List<HashMap<String, Object>> hypervisors = (List<HashMap<String, Object>>) resultObjList.get(0).get("hypervisors");
					HashMap<String, Object> hypervisor = (HashMap<String, Object>) hypervisors.get(0);
					record.put("vcpusUsed", hypervisor.get("vcpus_used"));
					record.put("vcpusUsedOver", Double.parseDouble(hypervisor.get("vcpus_used").toString()) * cpuOversub);
					record.put("vcpusTotal", hypervisor.get("vcpus"));
					record.put("vcpusTotalOver", Double.parseDouble(hypervisor.get("vcpus").toString()) * cpuOversub);
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

	public String listDataAuthResourcePools(HashMap<String, Object> paramMap){
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));
		StringBuffer resultBuffer = new StringBuffer();
		resultBuffer.append("'");
		resultBuffer.append(loginId);
		resultBuffer.append("'");
		paramMap.put("loginId", resultBuffer.toString());
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_POOLS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_POOLS, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourcePools successful! ");
		return result;
	}

	public String listResourcePoolDetails(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_POOL_DETAILS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourcePools successful! ");
		return result;
	}

	public String listResourcePoolHistory(HashMap<String, Object> paramMap){
		List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_RESOURCE_POOLS, paramMap);
		for (HashMap<String, Object> resultItem : resultList) {
			paramMap.put("region", resultItem.get("regionName"));
			List<HashMap<String, Object>> historyList = dbService.directSelect(DBServiceConst.SELECT_HOST_AGGREGATE_HISTORY, paramMap);
			resultItem.put("history", historyList);
		}
		String result = JsonHelper.toJson(resultList);
		logger.debug("query listAggregateHistory successful! ");
		return result;
	}

	public String createResourcePool(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_CREATE_RESOURCE_POOL, paramMap);
			logger.debug("create resource pool result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteResourcePool(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_DELETE_RESOURCE_POOL, paramMap);
			logger.debug("delete resource pool result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateResourcePool(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_UPDATE_RESOURCE_POOL, paramMap);
			logger.debug("delete resource pool result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listResourcePoolUsers(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_POOL_USERS, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_POOL_USERS, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourcePoolUsers successful! ");
		return result;
	}

	public String listResourcePoolDeptGroup(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_POOL_DEPTGROUP, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_POOL_DEPTGROUP, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourcePoolDeptGroup successful! ");
		return result;
	}

	public String listDataCenterDeptGroup(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_DATACENTER_DEPTGROUP, paramMap, currentPage,length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_DEPTGROUP, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterDeptGroup successful! ");
		return result;
	}

	public String listDataCenterDeptGroupUsers(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DATACENTER_DEPTGROUP_USERS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDataCenterDeptGroupUsers successful! ");
		return result;
	}

	public String listResourcePoolDeptGroupUsers(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_POOL_DEPTGROUP_USERS, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourcePoolDeptGroupUsers successful! ");
		return result;
	}

	public String listResourceIpPools(HashMap<String, Object> paramMap){
		String loginId = (String) paramMap.get("loginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));

		String result = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_IP_POOLS, paramMap, currentPage, length);
			resultMap.put("draw", paramMap.get("draw"));
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_IP_POOLS, paramMap);
		}

		result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceIpPools successful! ");
		return result;
	}

	public String createResourceIpPool(HashMap<String, Object> paramMap){
		try {
			String cidr = (String) paramMap.get("cidr");
			String baseIp = cidr.split("/")[0];
			int mask = Integer.parseInt(cidr.split("/")[1]);//默认mask均为24进行处理

			paramMap.put("baseIp", baseIp);
			paramMap.put("mask", mask);
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_CREATE_IP_POOL, paramMap);
			logger.debug("create resource ip pool result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteResourceIpPool(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_DELETE_IP_POOL, paramMap);
			logger.debug("delete resource ip pool result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateResourceIpPool(HashMap<String, Object> paramMap){
		try {
			String cidr = (String) paramMap.get("cidr");
			String baseIp = cidr.split("/")[0];
			int mask = Integer.parseInt(cidr.split("/")[1]);//默认mask均为24进行处理

			paramMap.put("baseIp", baseIp);
			paramMap.put("mask", mask);

			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_UPDATE_IP_POOL, paramMap);
			logger.debug("update resource ip pool result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String createResourceIpPoolRelation(HashMap<String, Object> paramMap){
		dbService.delete(DBServiceConst.DELETE_RESOURCE_IP_POOL_RELATION, paramMap);

		if (paramMap.containsKey("refPoolId") && paramMap.get("refPoolId") != null) {
			String refPoolId = (String) paramMap.get("refPoolId");
			String[] refPoolIds = refPoolId.split(",");
			for (int i = 0; i < refPoolIds.length; i++) {
				paramMap.put("refPoolId", refPoolIds[i]);
				dbService.insert(DBServiceConst.INSERT_RESOURCE_IP_POOL_RELATION, paramMap);
			}
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createResourceIpPoolRelation successful! ");
		return result;
	}

	public String listResourceIpPoolRelation(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_IP_POOL_RELATION, paramMap);

		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert listResourceIpPoolRelation successful! ");
		return result;
	}

	public String listResourceIpItems(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_IP_ITEMS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceIpItems successful! ");
		return result;
	}

	public synchronized String lockIpItem(HashMap<String, Object> paramMap ){
		Integer poolId = (Integer) paramMap.get("poolId");
		Integer lockNum = (Integer) paramMap.get("lockNum");
		synchronized (poolId) {
			paramMap = new HashMap<>();
			paramMap.put("poolId", poolId);
			List<HashMap<String, Object>> ipPoolList = dbService.directSelect(DBServiceConst.SELECT_RESOURCE_IP_POOLS, paramMap);
			if (ipPoolList != null && ipPoolList.size() > 0) {
				HashMap<String, Object> ipPoolMap = ipPoolList.get(0);
				int freeCount = (int) ipPoolMap.get("freeCount");

				if (lockNum > freeCount) {
					return this.invalidRequest("not enough ip to use error!");
				} else {
					paramMap.put("lockNum", lockNum);
					paramMap.put("status", 0);
					HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_IP_ITEMS, paramMap);

					//update the ipaddress item status
					List<HashMap<String, Object>> lockIpItemList = (List<HashMap<String, Object>>) resultMap.get("record");
					for (int i = 0; i < lockIpItemList.size(); i++) {
						HashMap<String, Object> lockIpItem = lockIpItemList.get(i);
						paramMap.put("status", 1);
						paramMap.put("ipItemId", lockIpItem.get("id"));
						dbService.update(DBServiceConst.UPDATE_RESOURCE_IP_ITEM, paramMap);
					}

					String result = JsonHelper.toJson(resultMap);
					logger.debug("query lockIpItem successful! ");
					return result;
				}
			} else {
				return this.invalidRequest("lockIpItem error!");
			}
		}
	}

	public String listResourceNetworkPorts(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_NETWORK_PORTS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNetworkPorts successful! ");
		return result;
	}
	
	public String createResourceNetworkPort(HashMap<String, Object> paramMap){
		dbService.insert(DBServiceConst.INSERT_RESOURCE_NETWORK_PORT, paramMap);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createResourceNetworkPort successful! ");
		return result;
	}
	
	public String updateResourceNetworkPort(HashMap<String, Object> paramMap){
		dbService.update(DBServiceConst.UPDATE_RESOURCE_NETWORK_PORT, paramMap);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert updateResourceNetworkPort successful! ");
		return result;
	}
	
	public String deleteResourceNetworkPort(HashMap<String, Object> paramMap){
		dbService.delete(DBServiceConst.DELETE_RESOURCE_NETWORK_PORT, paramMap);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert deleteResourceNetworkPort successful! ");
		return result;
	}
	
	public String listResourceNetworkRouteTables(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_NETWORK_ROUTE_TABLES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNetworkRouteTables successful! ");
		return result;
	}
	
	public String createResourceNetworkRouteTable(HashMap<String, Object> paramMap){
		dbService.insert(DBServiceConst.INSERT_RESOURCE_NETWORK_ROUTE_TABLE, paramMap);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createResourceNetworkRouteTable successful! ");
		return result;
	}
	
	public String updateResourceNetworkRouteTable(HashMap<String, Object> paramMap){
		dbService.update(DBServiceConst.UPDATE_RESOURCE_NETWORK_ROUTE_TABLE, paramMap);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert updateResourceNetworkRouteTable successful! ");
		return result;
	}
	
	public String deleteResourceNetworkRouteTable(HashMap<String, Object> paramMap){
		dbService.delete(DBServiceConst.DELETE_RESOURCE_NETWORK_ROUTE_TABLE, paramMap);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert deleteResourceNetworkRouteTable successful! ");
		return result;
	}
	
	public String listResourceNetworkNodes(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		int length = 0, currentPage = 0;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			currentPage = startNum == 0 ? 1 : startNum / length + 1;
			
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_NETWORK_NODES, paramMap, currentPage, length);
		}else{
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_NETWORK_NODES, paramMap);
		}
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNetworkNodes successful! ");
		return result;
	}

	public String createResourceIpMap(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_CREATE_IP_MAP, paramMap);
			logger.debug("create resource ip map result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listResourceIpMap(HashMap<String, Object> paramMap){
		int length = 0, currentPage = 0;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			currentPage = startNum == 0 ? 1 : startNum / length + 1;
		}
		HashMap<String, Object> resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_IP_MAP, paramMap, currentPage, length);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceIpMap successful! ");
		return result;
	}

	public String deleteResourceIpMap(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_DELETE_IP_MAP, paramMap);
			logger.debug("delete resource ip map result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateResourceIpMap(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_UPDATE_IP_MAP, paramMap);
			logger.debug("update resource ip map result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listResourceNodes(HashMap<String, Object> paramMap){

		String result = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String curLoginId = paramMap.getOrDefault("curLoginId","").toString();
		String type = paramMap.getOrDefault("type","").toString();
		String start = paramMap.getOrDefault("start","").toString();
		String length = paramMap.getOrDefault("length","").toString();
		int startNum = 0, currentPage = 0;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			startNum = Integer.parseInt(paramMap.get("start").toString());
			currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(paramMap.get("length").toString()) + 1;
		}
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));

		if (type.equals("COMPUTE") || type.equals("OPERATION") || type.equals("MANAGE")) {
			if (start != null && !length.equals("-1")) {
				resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES, paramMap, currentPage, Integer.parseInt(length));
				resultMap.put("draw", paramMap.get("draw"));
			} else {
				resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES, paramMap);
			}
		} else if (type.equals("STORAGE")) {
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_STORAGE_NODES, paramMap, currentPage, Integer.parseInt(length));
		} else if (type.equals("NETWORK")) {
			paramMap.put("type", "NETWORK");
			if (start != null && !length.equals("-1")) {
				resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_NETWORK_NODES, paramMap, currentPage, Integer.parseInt(length));
				resultMap.put("draw", paramMap.get("draw"));
			} else {
				resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_NETWORK_NODES, paramMap);
			}
//		} else if (nodeType.equals("")) {
//			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_MANAGE_NODES, paramMap, currentPage, Integer.parseInt(length));
		} else if (type.equals("BARE")) {
			if (start != null && !length.equals("-1")) {
				resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_BARE_NODES, paramMap, currentPage, Integer.parseInt(length));
				resultMap.put("draw", paramMap.get("draw"));
			} else {
				resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_BARE_NODES, paramMap);
			}
		}

		result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodes " + type + " successful! ");
		return result;
	}

	public String listResourceNodesBase(HashMap<String, Object> paramMap){
		String loginId = paramMap.getOrDefault("loginId","").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_BASE, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodes [" + paramMap.toString() + "] successful!");
		return result;
	}

	public String listResourceNodesBaseDetail(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_BASE_DETAIL, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodesBaseDetail successful!");
		return result;
	}

	public String listResourceNodeDetails(HashMap<String, Object> paramMap){
		String result = null;
		String nodeType = paramMap.getOrDefault("nodeType","").toString();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (nodeType.equals("COMPUTE") || nodeType.equals("OPERATION")) {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES, paramMap);
		} else if (nodeType.equals("STORAGE")) {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_STORAGE_NODES, paramMap);
		} else if (nodeType.equals("NETWORK")) {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_NETWORK_NODES, paramMap);
		} else if (nodeType.equals("MANAGE")) {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_MANAGE_NODES, paramMap);
		}

		result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodeDetails " + nodeType + " successful! ");
		return result;
	}

	public String listResourcePhysicalNodes(HashMap<String, Object> paramMap){
		String curLoginId = paramMap.getOrDefault("curLoginId","").toString();
		String type = paramMap.getOrDefault("type","").toString();
		String start = paramMap.getOrDefault("start","").toString();
		String length = paramMap.getOrDefault("length","").toString();
		String sortGraphId = paramMap.getOrDefault("sortGraphId","").toString();
		String showMonitor = paramMap.getOrDefault("showMonitor","").toString();
		int startNum = 0, currentPage = 0;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			startNum = Integer.parseInt(paramMap.get("start").toString());
			currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(paramMap.get("length").toString()) + 1;
		}

		String result = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		paramMap.put("type", type);
		if (type.equals("MANAGE")) {//查询云平台资源管理中的 物理机
			paramMap.put("poolId", 0);
		}
		if (start != null && !length.equals("-1")) {
			List<String> sortHostIdList = queryTopMonitorHost(sortGraphId, paramMap);

			if (sortHostIdList != null && sortHostIdList.size() > 0) {
				String sortHostIdsStr = StringUtils.join(sortHostIdList, ",");
				paramMap.put("hostids", sortHostIdsStr);
			}

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_COMPUTE_NODES_PHYSICAL, paramMap, currentPage, Integer.parseInt(length));

			if (showMonitor != null) {
				queryMonitorData(curLoginId, resultMap);
			}

			resultMap.put("draw", paramMap.get("draw"));
		} else {
			List<String> sortHostIdList = queryTopMonitorHost(sortGraphId, paramMap);

			if (sortHostIdList != null && sortHostIdList.size() > 0) {
				String sortHostIdsStr = StringUtils.join(sortHostIdList, ",");
				paramMap.put("hostids", sortHostIdsStr);
			}

			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_COMPUTE_NODES_PHYSICAL, paramMap);

			if (showMonitor != null) {
				queryMonitorData(curLoginId, resultMap);
			}
		}

		result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodes " + type + " successful! ");
		return result;
	}

	private List<String> queryTopMonitorHost(String sortGraphId, HashMap<String, Object> paramMap) {
		List<String> realList = new ArrayList<>();
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
			List<HashMap<String, Object>> targetItemsResult = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_ITEMS, paramMap);
			if (targetItemsResult != null && targetItemsResult.get(0) != null) {
				String targetItems = (String) targetItemsResult.get(0).get("items");

				paramMap.put("itemIds", targetItems);
				List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_HOSTORDER, paramMap);
				for (int i = 0; i < resultList.size(); i++) {
					realList.add(String.valueOf(resultList.get(i).get("hostId")));
				}
			}
		}
		return realList;
	}

	private void queryMonitorData(String loginId, HashMap<String, Object> serverResultMap) {
		List<HashMap<String, Object>> serverRecord = (List<HashMap<String, Object>>) serverResultMap.get("record");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, new HashMap<String, Object>());
		for (int n = 0; n < serverRecord.size(); n++) {
			HashMap<String, Object> serverMap = serverRecord.get(n);

			Integer monitorHostId = Integer.parseInt(serverMap.get("hostid").toString());
			if (monitorHostId != null && monitorHostId != -1) {

				parseMonitorGraphs(monitorHostId, graphResultMap);
				List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
				for (int i = 0; i < graphRecord.size(); i++) {
					HashMap<String, Object> graph = graphRecord.get(i);
					List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

					List<Integer> itemList = new ArrayList<>();
					for (int j = 0; j < refItems.size(); j++) {
						HashMap<String, Object> refItem = refItems.get(j);
						itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
					}

					HashMap<String, Object> actionParamMap = new HashMap<>();
					actionParamMap.put("endTime", new Date().getTime() / 1000);
					actionParamMap.put("itemList", itemList);
					actionParamMap.put("panelType", EMonitorPanelType.MULITY);
					actionParamMap.put("length", 1);

					try {
						Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
						if (result.getResultCode().equals(EResultCode.SUCCESS)) {
							List<HashMap<String, Object>> resultList = result.getResultObj();
							HashMap<String, Object> itemDataMap = resultList.get(0);
							List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

							for (int j = 0; j < itemKeys.size(); j++) {
								List<HashMap> dataList = (List<HashMap>) itemDataMap.get(itemKeys.get(j));
								HashMap<String, Object> dataMap = dataList.get(0);
								serverMap.put((String) graph.get("name"), dataMap.get("value"));
							}
						}
					} catch (Exception e) {
						logger.error("query vm monitor data error", e);
					}
				}
			}
		}
	}

	private void parseMonitorGraphs(Integer hostId, HashMap<String, Object> graphResultMap) {
		List<HashMap<String, Object>> graphsList = (List<HashMap<String, Object>>) graphResultMap.get("record");
		for (int i = 0; i < graphsList.size(); i++) {
			HashMap<String, Object> graphItem = graphsList.get(i);

			Integer graphId = (Integer) graphItem.get("id");

			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("graphId", graphId);
			paramMap.put("hostId", hostId);

			List<HashMap<String, Object>> graphRefItemsList = dbService.directSelect(DBServiceConst.SELECT_MONITOR_GRAPH_ITEMS, paramMap);
			graphItem.put("refItems", graphRefItemsList);
		}
	}

	private void queryResourceNodePropTags(String loginId,  HashMap<String, Object> serverResultMap) {
		List<HashMap<String, Object>> serverRecord = (List<HashMap<String, Object>>) serverResultMap.get("record");
		for (HashMap<String, Object> serverItemMap : serverRecord) {
			HashMap<String, Object> parameter = new HashMap<>();
			parameter.put("resourceId", serverItemMap.get("id"));
			parameter.put("loginId",loginId);

			List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_RESOURCE_PROPERTY_TAG_VALUES, parameter);
			serverItemMap.put("propTags", resultList);
		}
	}

	public String listResourcePhysicalNodesTotal(HashMap<String, Object> paramMap){
		String curLoginId = paramMap.getOrDefault("curLoginId","").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_COMPUTE_NODES_PHYSICAL_TOTAL, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodes total successful! ");
		return result;
	}

	public String listResourceHypervisorNodes(HashMap<String, Object> paramMap){
		String curLoginId = paramMap.getOrDefault("curLoginId","").toString();
		String loginId = paramMap.getOrDefault("loginId","").toString();
		String type = paramMap.getOrDefault("type","").toString();
		String start = paramMap.getOrDefault("start","").toString();
		String length = paramMap.getOrDefault("length","").toString();
		String sortGraphId = paramMap.getOrDefault("sortGraphId","").toString();
		String showMonitor = paramMap.getOrDefault("showMonitor","").toString();
		int startNum = 0, currentPage = 0;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			startNum = Integer.parseInt(paramMap.get("start").toString());
			currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(paramMap.get("length").toString()) + 1;
		}

		String result = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		paramMap.put("type", type);


		if (start != null && !length.equals("-1")) {
			List<String> sortHostIdList = queryTopMonitorHost(sortGraphId, paramMap);

			if (sortHostIdList != null && sortHostIdList.size() > 0) {
				String sortHostIdsStr = StringUtils.join(sortHostIdList, ",");
				paramMap.put("hostids", sortHostIdsStr);
			}

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES_HYPERVISOR, paramMap, currentPage, Integer.parseInt(length));

			if (showMonitor != null) {
				queryMonitorData(curLoginId, resultMap);
			}

			resultMap.put("draw", paramMap.get("draw"));
		} else {
			List<String> sortHostIdList = queryTopMonitorHost(sortGraphId, paramMap);

			if (sortHostIdList != null && sortHostIdList.size() > 0) {
				String sortHostIdsStr = StringUtils.join(sortHostIdList, ",");
				paramMap.put("hostids", sortHostIdsStr);
			}

			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES_HYPERVISOR, paramMap);

			if (showMonitor != null) {
				queryMonitorData(curLoginId, resultMap);
			}
		}

//		queryResourceNodePropTags(loginId, resultMap);

		result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceHypervisorNodes " + type + " successful! ");
		return result;
	}

	public String listOpenstackHypervisors(HashMap<String, Object> paramMap){
		String start = paramMap.getOrDefault("start","").toString();
		String length = paramMap.getOrDefault("length","").toString();
		int startNum = 0, currentPage = 0;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			startNum = Integer.parseInt(paramMap.get("start").toString());
			currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(paramMap.get("length").toString()) + 1;
		}
		String result = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();


		if (start != null && !length.equals("-1")) {
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_OPENSTACK_HYPERVISORS, paramMap, currentPage, Integer.parseInt(length));

			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", paramMap.get("draw"));
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_OPENSTACK_HYPERVISORS, paramMap);
		}

		result = JsonHelper.toJson(resultMap);
		logger.debug("query listOpenstackHypervisors successful! ");
		return result;
	}

	public String listOpenstackHypervisorsCount(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPENSTACK_HYPERVISORS_COUNT, paramMap);

		logger.debug("query listOpenstackHypervisorsCount successful! ");
		return JsonHelper.toJson(resultMap);
	}

	public String deleteOpenstackHypervisor(HashMap<String, Object> paramMap){
		dbService.delete(DBServiceConst.DELETE_OPENSTACK_HYPERVISOR, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteOpenstackHypervisor successful! ");
		return result;
	}

	public String listResourcePowerNodes(HashMap<String, Object> paramMap){
		String curLoginId = paramMap.getOrDefault("curLoginId","").toString();
		String start = paramMap.getOrDefault("start","").toString();
		String length = paramMap.getOrDefault("length","").toString();
		int startNum = 0, currentPage = 0;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			startNum = Integer.parseInt(paramMap.get("start").toString());
			currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(paramMap.get("length").toString()) + 1;
		}
		String result = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));

		if (start != null && !length.equals("-1")) {
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES_POWER, paramMap, currentPage, Integer.parseInt(length));

			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", paramMap.get("draw"));
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES_POWER, paramMap);
		}
		// dataTables需要将接收到的draw直接返回
		resultMap.put("draw", paramMap.get("draw"));

		result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourcePowerNodes  successful! ");
		return result;
	}

	public String listResourcePhysicalNodesInterface(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_COMPUTE_NODES_PHYSICAL_INTERFACE, paramMap);
		// dataTables需要将接收到的draw直接返回
		String result = JsonHelper.toJson(resultMap);
		return result;
	}

	public String updateResourcePhysicalNodesInterface( String nodeId, Integer interfaceId){
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		dbService.update(DBServiceConst.UPDATE_NODE_DEFAULT_INTERFACE, paramMap);
		paramMap.put("interfaceId", interfaceId);
		dbService.update(DBServiceConst.UPDATE_NODE_DEFAULT_INTERFACE, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateResourcePhysicalNodesInterface successful! ");
		return result;
	}

	public String listResourceNodeDisk(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_NODE_DISK, paramMap);

		String result = JsonHelper.toJson(resultMap);
		return result;
	}

	public String listResourceNodeGroupByFilter(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_GROUP_FILTER, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodeGroupByFilter successful! ");
		return result;
	}

	public String validateResourceNode(HashMap<String, Object> paramMap){
		Integer ipAddressCount = (Integer) dbService.selectOne(DBServiceConst.SELECT_RN_EXT_OSINFO_IPADDRESS, paramMap);
		if (ipAddressCount > 0) {
			return this.invalidRequest("the ipAddress is exist!");
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query validateResourceNode successful! ");
		return result;
	}

	public String createResourceNode(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			paramMap.put("createUser", loginId);
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_ADD_NODE, paramMap);
			logger.debug("add resource node result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listResourceNodeUnitPrice(HashMap<String, Object> paramMap){
		String curLoginId = (String) paramMap.get("curLoginId");
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_POOLS, paramMap);
		List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> resultItem : resultList) {
			HashMap<String, Object> unitPriceParamMap = new HashMap<>();
			unitPriceParamMap.put("type", "RESOURCE_POOL");
			unitPriceParamMap.put("regionName", resultItem.get("regionName"));
			unitPriceParamMap.put("id", resultItem.get("id"));

			// 查询资源池下主机聚集及主机聚集下的资源单价信息
			HashMap<String, Object> aggregateParamMap = new HashMap<>();
			aggregateParamMap.put("regionName", resultItem.get("regionName"));
			List<HashMap<String, Object>> aggregateResultList = dbService.directSelect(DBServiceConst.SELECT_HOST_AGGREGATES, aggregateParamMap);
			for (HashMap<String, Object> aggregateResultItem : aggregateResultList) {
				aggregateParamMap.put("type", "AGGREGATE");
				aggregateParamMap.put("id", aggregateResultItem.get("id"));

				List<HashMap<String, Object>> aggregateUnitPriceList = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_UNIT_PRICE, aggregateParamMap);
				aggregateResultItem.put("unitPriceList", aggregateUnitPriceList);
			}
			resultItem.put("aggregates", aggregateResultList);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodeUnitPrice successful! ");
		return result;
	}

	public String createResourceNodeUnitPrice(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			// 通过资源池统一设置主机聚集的资源单价
			if (paramMap.containsKey("configType") && paramMap.get("configType").equals("RESOURCE_POOL")) {
				List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) paramMap.get("list");
				HashMap<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("regionName", list.get(0).get("regionName"));
				dbService.delete(DBServiceConst.DELETE_RN_EXT_UNIT_PRICE, parameters);

				List<HashMap<String, Object>> aggregates = dbService.directSelect(DBServiceConst.SELECT_HOST_AGGREGATES, parameters);
				for (HashMap<String, Object> aggregateItem : aggregates) {
					List<HashMap<String, Object>> unitPriceList = new ArrayList<HashMap<String, Object>>();
					for (HashMap<String, Object> unitPriceItem : list) {
						unitPriceItem.put("id", aggregateItem.get("id"));
						unitPriceList.add(unitPriceItem);
					}
					paramMap.put("list", unitPriceList);
					dbService.insert(DBServiceConst.INSERT_RN_EXT_UNIT_PRICE, paramMap);
				}
			} else {
				// 通过主机聚集设置资源单价
				dbService.insert(DBServiceConst.INSERT_RN_EXT_UNIT_PRICE, paramMap);
			}
			resultMap.put("messageStatus", "END");
		} catch (Exception e) {
			logger.error("createResourceNodeUnitPrice error:" + e.getMessage());
			resultMap.put("messageStatus", "ERROR");
			resultMap.put("messageContent", "设置资源单价出错！");
		}
		return JsonHelper.toJson(resultMap);
	}

	public String updateResourceNodeUnitPrice(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) paramMap.get("list");
			for (HashMap<String, Object> itemMap : list) {
				dbService.update(DBServiceConst.UPDATE_RN_EXT_UNIT_PRICE, itemMap);
			}
			resultMap.put("messageStatus", "END");
		} catch (Exception e) {
			logger.error("updateResourceNodeUnitPrice error:" + e.getMessage());
			resultMap.put("messageStatus", "ERROR");
			resultMap.put("messageContent", "设置资源单价出错！");
		}
		return JsonHelper.toJson(resultMap);
	}

	public String deleteResourceNode(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_DELETE_NODE, paramMap);
			logger.debug("delete resource node result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateResourceNode(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_UPDATE_NODE, paramMap);
			logger.debug("update resource node result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateResourceNodeSystemInfo(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			Result result = resourceActionWrapper.doExcutionAction(loginId, null, EAction.RESOURCE_CONFIG_AGENT, paramMap);
			logger.debug("config server agent result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listResourceServices(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_SERVICES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listResourceNodeServices successful! the result is " + result);
		return result;
	}

	public String listAvailabilityZoneHypervisorHosts(HashMap<String, Object> paramMap){
		try {
			String loginId = (String) paramMap.get("loginId");
			String regionName = (String) paramMap.get("regionName");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_LIST_HYPERVISORS, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String resourcePoolCount(HashMap<String, Object> paramMap){
		String poolType = (String) paramMap.get("poolType");
		HashMap<String, Object> resultMap = null;
		if (poolType.equals("COMPUTE")) {
			resultMap = dbService.select(DBServiceConst.RESOURCE_POOL_COMPUTE_COUNT, paramMap);
		} else if (poolType.equals("STORAGE")) {
			resultMap = dbService.select(DBServiceConst.RESOURCE_POOL_STORAGE_COUNT, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query resourcePoolCount successful! the result is :" + result);
		return result;
	}

	public String getResourceNodeSystemInfo(HashMap<String, Object> paramMap){
		try {
			LinkedHashMap<String, Object> systemInfoMap = (LinkedHashMap<String, Object>) dbService.selectOne(DBServiceConst.SELECT_RN_EXT_SYSTEMINFO, paramMap);
			String systemInfoString = "";
			if (systemInfoMap.containsKey("systemInfo")) {
				byte[] systemInfoByteArray = (byte[]) systemInfoMap.get("systemInfo");
				systemInfoString = new String(systemInfoByteArray);
			}
			systemInfoMap.put("systemInfoString", systemInfoString);

			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put("messageStatus", "END");
			resultMap.put("record", systemInfoMap);
			String result = JsonHelper.toJson(resultMap);
			logger.debug("query getResourceNodeSystemInfo successful! ");
			return result;
		} catch (Exception e) {
			logger.error("query getResourceNodeSystemInfo error!", e);
			return invalidRequest();
		}
	}
	
	public String listResourceNodeOsEnvironment(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_OS_ENVIRONMENT, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_OS_ENVIRONMENT, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listResourceNodeOsEnvironmentDetails(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_OS_ENVIRONMENT, paramMap);
		return JsonHelper.toJson(resultMap);
	}
	
	public String listResourceNodeDatastore(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_DATASTORE, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_DATASTORE, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String listResourceGroup(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		HashMap<String, Object> resultMap = null;
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_GROUP, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_GROUP, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		return result;
	}

	public String createResourceGroup(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<>();
		
		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("fullName", paramMap.get("name"));
		List<HashMap<String,Object>> groupList = dbService.directSelect(DBServiceConst.SELECT_RN_GROUP,parameters);

		if(groupList.size() != 0){
			resultMap.put("messageStatus", EResultCode.DATABASE_FAIL);
			resultMap.put("responseMessage", "分组名称已存在！");
			logger.debug("insert createResourceGroup fail! ");
		}else{
			dbService.insert(DBServiceConst.INSERT_RN_GROUP, paramMap);
			updateResourceGroupResourceNodeRelation(paramMap);
			logger.debug("insert createResourceGroup successful! ");
			resultMap.put("messageStatus", "END");
			resultMap.put("responseMessage", "分组创建成功！");
		}
		return JsonHelper.toJson(resultMap);
	}
	
	public String updateResourceGroupResourceNodeRelation(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = new HashMap<>();
		if(paramMap.containsKey("groupId")){
			@SuppressWarnings("unchecked")
			List<String> resourceIds = (List<String>) paramMap.get("resourceIds");
			if(null != resourceIds){
				HashMap<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("groupId", paramMap.get("groupId"));
				parameters.put("resourceType", paramMap.get("resourceType"));
				dbService.delete(DBServiceConst.DELETE_RN_GROUP_RELATION, parameters);
				for (int i = 0; i < resourceIds.size(); i++) {
					parameters.put("resourceId",resourceIds.get(i));
					dbService.insert(DBServiceConst.INSERT_RN_GROUP_RELATION, parameters);
				}
			}
		}else if(paramMap.containsKey("groupIds")){
			@SuppressWarnings("unchecked")
			List<String> groupIds = (List<String>) paramMap.get("groupIds");
			@SuppressWarnings("unchecked")
			List<String> resourceIds = (List<String>) paramMap.get("resourceIds");
			String resourceType = (String) paramMap.get("resourceType");
			for (int i = 0; i < resourceIds.size(); i++) {
				HashMap<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("resourceId",resourceIds.get(i));
				parameters.put("resourceType", resourceType);
				dbService.delete(DBServiceConst.DELETE_RN_GROUP_RELATION, parameters);
				for (int j = 0; j < groupIds.size(); j++) {
					parameters.put("groupId",groupIds.get(j));
					dbService.insert(DBServiceConst.INSERT_RN_GROUP_RELATION, parameters);
				}
			}
		}
		
		logger.debug("insert updateResourceGroupResourceNodeRelation successful! ");
		resultMap.put("messageStatus", "END");
		return JsonHelper.toJson(resultMap);
	}
	
	public String listResourceNodeResourceGroupRelation(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_RN_GROUP, paramMap);
		String result = JsonHelper.toJson(resultMap);
		return result;
	}
	
	public String updateResourceGroup(HashMap<String, Object> paramMap){
		dbService.update(DBServiceConst.UPDATE_RN_GROUP,paramMap);

		logger.debug("update updateResourceGroup successful! ");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("messageStatus", "END");
		resultMap.put("responseMessage", "分组编辑成功！");
		
		return JsonHelper.toJson(resultMap);
	}
	
	public String deleteResourceGroup(HashMap<String, Object> paramMap){
		dbService.delete(DBServiceConst.DELETE_RN_GROUP,paramMap);

		logger.debug("delete deleteResourceGroup successful! ");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("messageStatus", "END");
		resultMap.put("responseMessage", "分组删除成功！");
		
		return JsonHelper.toJson(resultMap);
	}
	
	public String listResourceGroupDetail(HashMap<String, Object> paramMap){
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		HashMap<String, Object> resultMap = null;
		paramMap.put("countSize", userService.getSystemUserAdminRole(loginId));
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_GROUP_RESOURCE, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_GROUP_RESOURCE, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		return result;
	}

	public String listResourceUserDetail(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_USER_RESOURCE, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String listHMC(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_HMC, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listHMC successful! ");
		return result;
	}
	
	/********************************************************************************************************************************************************************************************/
	/********************************************************************************************************************************************************************************************/
	/****************************************** 性能监控模块 *************************************************************************************************************************************/
	/********************************************************************************************************************************************************************************************/
	/********************************************************************************************************************************************************************************************/
	
	public String listMonitorHost(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap=dbService.selectByPage(DBServiceConst.SELECT_MONITOR_HOST, paramMap,currentPage, length);
		}else{
			resultMap=dbService.select(DBServiceConst.SELECT_MONITOR_HOST, paramMap);
		}
		resultMap.put("loginId", paramMap.get("loginId"));
		queryMonitorHostMonitorData(resultMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listMonitorHost successful! ");
		return result;
	}
	
	public String listMonitorHostInfo(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap=dbService.selectByPage(DBServiceConst.SELECT_MONITOR_HOST, paramMap,currentPage, length);
		}else{
			resultMap=dbService.select(DBServiceConst.SELECT_MONITOR_HOST, paramMap);
		}
		resultMap.put("loginId", paramMap.get("loginId"));
		queryMonitorHostMonitorData(resultMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listMonitorHost successful! ");
		return result;
	}

	/**
	 * 获取监控数据
	 *
	 * @param serverResultMap
	 */
	private void queryMonitorHostMonitorData(HashMap<String, Object> serverResultMap) {
		List<HashMap<String, Object>> serverRecord = (List<HashMap<String, Object>>) serverResultMap.get("record");
		
		HashMap<String, Object> defaultGraphParamMap = new HashMap<String, Object>();
		defaultGraphParamMap.put("ids", "-4,-3,-2");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
		for (int n = 0; n < serverRecord.size(); n++) {
			HashMap<String, Object> serverMap = serverRecord.get(n);

			Integer monitorHostId = Integer.parseInt(serverMap.get("monitorHostId").toString());
			if (monitorHostId != null && monitorHostId != -1) {

				parseMonitorGraphs(monitorHostId, graphResultMap);
				List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
				for (int i = 0; i < graphRecord.size(); i++) {
					HashMap<String, Object> graph = graphRecord.get(i);
					List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

					List<Integer> itemList = new ArrayList<>();
					for (int j = 0; j < refItems.size(); j++) {
						HashMap<String, Object> refItem = refItems.get(j);
						itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
					}

					HashMap<String, Object> actionParamMap = new HashMap<>();
					actionParamMap.put("endTime", new Date().getTime() / 1000);
					actionParamMap.put("itemList", itemList);
					actionParamMap.put("panelType", EMonitorPanelType.MULITY);
					actionParamMap.put("length", 1);

					try {
						String loginId = serverResultMap.get("loginId").toString();
						Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
						if (result.getResultCode().equals(EResultCode.SUCCESS)) {
							List<HashMap<String, Object>> resultList = result.getResultObj();
							HashMap<String, Object> itemDataMap = resultList.get(0);
							List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

							for (int j = 0; j < itemKeys.size(); j++) {
								List<HashMap> dataList = (List<HashMap>) itemDataMap.get(itemKeys.get(j));
								HashMap<String, Object> dataMap = dataList.get(0);
								serverMap.put((String) graph.get("name"), dataMap.get("value"));
							}
						}
					} catch (Exception e) {
						logger.error("query vm monitor data error", e);
					}
				}
			}
		}
	}
	
	public String createMonitorHost(HashMap<String, Object> paramMap) {
		try {
			int id=Math.abs(UUID.randomUUID().toString().hashCode());
			paramMap.put("id", id);
			dbService.insert(DBServiceConst.INSERT_MONITOR_HOST, paramMap);
			dbService.insert(DBServiceConst.INSERT_GROUP, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "主机监控创建成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create monitorHost successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create monitorHost error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "主机监控创建失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String updateMonitorHost(HashMap<String, Object> paramMap) {
		try {
			dbService.update(DBServiceConst.UPDATE_MONITOR_HOST, paramMap);
			dbService.update(DBServiceConst.UPDATE_GROUP, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "主机监控编辑成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update monitorHost successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("update monitorHost error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "主机监控编辑失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	public String deleteMonitorHost(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_MONITOR_HOST, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "主机监控删除成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete monitorHost successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("delete monitorHost error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "主机监控删除失败！");
			return JsonHelper.toJson(resultMap);	
		}
	}
	//日志搜索
	public String listSysLog(HashMap<String, Object> paramMap){
		EsPage esPage = null;
		List<Map<String, Object>> resultList = null;
		
		String matchStr = (String) paramMap.get("key");
		matchStr = "message=" + matchStr;
		
		long startTime = Long.parseLong(paramMap.get("startTime").toString());
		long endTime = Long.parseLong(paramMap.get("endTime").toString());
		
		
			if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
				int length = Integer.parseInt(paramMap.get("length").toString());
				int startNum = Integer.parseInt(paramMap.get("start").toString());
				int currentPage = startNum == 0 ? 1 : startNum / length + 1;
				esPage = elasticsearchService.searchDataPage("filebeat-*", "log",currentPage, length, startTime, endTime, "", "", false, "", matchStr);
				logger.debug("query listSysLog successful! ");
				return JsonHelper.toJson(esPage);
			}else{
				resultList = elasticsearchService.searchListData("filebeat-*", "log", startTime, endTime, 1024, "", "", false, "", matchStr);
				logger.debug("query listSysLog successful! ");
				return JsonHelper.toJson(resultList);
			}

			    
	}
	
	public String listMonitorHostProcess(HashMap<String, Object> paramMap){
		List<HashMap<String, Object>> processResultList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> monitorHostProcesses = dbService.directSelect(DBServiceConst.SELECT_MONITOR_HOST_PROCESSES, paramMap);
		Boolean isGlobal = (Boolean) paramMap.get("isGlobal");
		
		HashMap<String, Object> defaultGraphParamMap = new HashMap<String, Object>();
		defaultGraphParamMap.put("ids", "-6");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
		
		Integer monitorHostId = Integer.parseInt(paramMap.get("monitorHostId").toString());
		if (monitorHostId != null && monitorHostId != -1) {
			parseMonitorGraphs(monitorHostId, graphResultMap);
			List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
			if(graphRecord.size()>0) {
				HashMap<String, Object> graph = graphRecord.get(0);
				List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

				List<Integer> itemList = new ArrayList<>();
				for (int j = 0; j < refItems.size(); j++) {
					HashMap<String, Object> refItem = refItems.get(j);
					itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
				}

				HashMap<String, Object> actionParamMap = new HashMap<>();
				actionParamMap.put("endTime", new Date().getTime() / 1000);
				actionParamMap.put("itemList", itemList);
				actionParamMap.put("panelType", EMonitorPanelType.MULITY);
				actionParamMap.put("length", 1);

				try {
					String loginId = paramMap.get("curLoginId").toString();
					Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
					if (result.getResultCode().equals(EResultCode.SUCCESS)) {
						List<HashMap<String, Object>> resultList = result.getResultObj();
						HashMap<String, Object> itemDataMap = resultList.get(0);
						List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

						for (int j = 0; j < itemKeys.size(); j++) {
							@SuppressWarnings("unchecked")
							List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) itemDataMap.get(itemKeys.get(j));
							HashMap<String, Object> dataMap = dataList.get(0);
							String value  = (String) dataMap.get("value");
							List<HashMap<String, Object>> valueResultList = parseProcessData(value);
							
							if(isGlobal == null || !isGlobal) {
								for (HashMap<String, Object> processItemMap : monitorHostProcesses) {
									for (HashMap<String, Object> valueItemMap : valueResultList) {
										if(processItemMap.get("name").toString().equals(valueItemMap.get("pidName").toString())) {
											valueItemMap.putAll(processItemMap);
											processResultList.add(valueItemMap);
										}
									}
								}
							}else {
								processResultList.addAll(valueResultList);
							}
						}
					}
				} catch (Exception e) {
					logger.error("query monitor host process data error", e);
				}
			}
		}
		return JsonHelper.toJson(processResultList);
	}
	
	public String listMonitorHostFilesystem(HashMap<String, Object> paramMap){
		List<HashMap<String, Object>> filesystemResultList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> monitorHostFilesystem = dbService.directSelect(DBServiceConst.SELECT_MONITOR_HOST_FILESYSTEMES, paramMap);
		Boolean isGlobal = (Boolean) paramMap.get("isGlobal");
		
		HashMap<String, Object> defaultGraphParamMap = new HashMap<String, Object>();
		defaultGraphParamMap.put("ids", "-7");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
		
		Integer monitorHostId = Integer.parseInt(paramMap.get("monitorHostId").toString());
		if (monitorHostId != null && monitorHostId != -1) {
			parseMonitorGraphs(monitorHostId, graphResultMap);
			List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
			if(graphRecord.size()>0) {
				HashMap<String, Object> graph = graphRecord.get(0);
				List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

				List<Integer> itemList = new ArrayList<>();
				for (int j = 0; j < refItems.size(); j++) {
					HashMap<String, Object> refItem = refItems.get(j);
					itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
				}

				HashMap<String, Object> actionParamMap = new HashMap<>();
				actionParamMap.put("endTime", new Date().getTime() / 1000);
				actionParamMap.put("itemList", itemList);
				actionParamMap.put("panelType", EMonitorPanelType.MULITY);
				actionParamMap.put("length", 1);

				try {
					String loginId = paramMap.get("curLoginId").toString();
					Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
					if (result.getResultCode().equals(EResultCode.SUCCESS)) {
						List<HashMap<String, Object>> resultList = result.getResultObj();
						HashMap<String, Object> itemDataMap = resultList.get(0);
						List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

						for (int j = 0; j < itemKeys.size(); j++) {
							@SuppressWarnings("unchecked")
							List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) itemDataMap.get(itemKeys.get(j));
							HashMap<String, Object> dataMap = dataList.get(0);
							String value  = (String) dataMap.get("value");
							List<HashMap<String, Object>> valueResultList = parseFilesystemData(value);
							
							if(isGlobal == null || !isGlobal) {
								for (HashMap<String, Object> filesystemItemMap : monitorHostFilesystem) {
									for (HashMap<String, Object> valueItemMap : valueResultList) {
										if(filesystemItemMap.get("name").toString().equals(valueItemMap.get("filesystem").toString())) {
											valueItemMap.putAll(filesystemItemMap);
											filesystemResultList.add(valueItemMap);
										}
									}
								}
							}else {
								filesystemResultList.addAll(valueResultList);
							}
						}
					}
				} catch (Exception e) {
					logger.error("query monitor host file system data error", e);
				}
			}
		}
		return JsonHelper.toJson(filesystemResultList);
	}
	
	public String createMonitorProcess(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.INSERT_MONITOR_HOST_PROCESS, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "进程添加成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create monitorProcess successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create monitorProcess error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "进程添加失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	
	public String deleteMonitorProcess(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_MONITOR_HOST_PROCESS, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "进程删除成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete monitorProcess successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("delete monitorProcess error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>(); 
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "进程删除失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
	
	private List<HashMap<String, Object>> parseProcessData(String value){
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		try {
			byte[] valueBytes = value.getBytes(Charset.forName("utf-8"));
			InputStreamReader isReader = new InputStreamReader(new ByteArrayInputStream(valueBytes), Charset.forName("utf-8"));
			BufferedReader bufferedReader = new BufferedReader(isReader);  
			String line = bufferedReader.readLine(); // 读取标题行
			while ((line = bufferedReader.readLine()) != null ) {  
				String[] processInfos = line.split(" ");
				int i = 0;
				
				StringBuffer sbCommand = new StringBuffer();
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				for (int j = 0; j < processInfos.length; j++) {
					String item = processInfos[j].toString().trim();
					if(!item.equals("")) {
						if(i<=9) {
							if(i == 0) {
								itemMap.put("user", item);
							}
							if(i == 1) {
								itemMap.put("pid", item);
							}
							if(i == 2) {
								itemMap.put("cpuUsage", item);
							}
							if(i == 3) {
								itemMap.put("ramUsage", item);
							}
						}else {
							sbCommand.append(item);
							sbCommand.append(" ");
						}
						i++;
					}
				}

				itemMap.put("pidName", sbCommand.toString().trim());
				itemMap.put("diskUsage", 0.0);
				itemMap.put("networkUsage", 0.0);
				
				resultList.add(itemMap);
			}  
			bufferedReader.close();
			isReader.close();
		} catch (Exception e) {
			logger.error("parse process data error !", e);
		}
		return resultList;
	}
	
	private List<HashMap<String, Object>> parseFilesystemData(String value){
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		try {
			byte[] valueBytes = value.getBytes(Charset.forName("utf-8"));
			InputStreamReader isReader = new InputStreamReader(new ByteArrayInputStream(valueBytes), Charset.forName("utf-8"));
			BufferedReader bufferedReader = new BufferedReader(isReader);  
			String line = bufferedReader.readLine(); // 读取标题行
			while ((line = bufferedReader.readLine()) != null ) {  
				String[] processInfos = line.split(" ");
				int i = 0;
				
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				for (int j = 0; j < processInfos.length; j++) {
					String item = processInfos[j].toString().trim();
					if(!item.equals("")) {
						if(i == 0) {
							itemMap.put("filesystem", item);
						}
						if(i == 1) {
							itemMap.put("size", item);
						}
						if(i == 2) {
							itemMap.put("used", item);
						}
						if(i == 3) {
							itemMap.put("avail", item);
						}
						if(i == 4) {
							itemMap.put("use", item);
						}
						i++;
					}
				}
				resultList.add(itemMap);
			}  
			bufferedReader.close();
			isReader.close();
		} catch (Exception e) {
			logger.error("parse filesystem data error !", e);
		}
		return resultList;
	}

	public String createMonitorfilesystem(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.INSERT_MONITOR_HOST_FILESYSTEMES, paramMap);
			HashMap<String, Object>resultMap = new HashMap<String,Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "文件系统名称添加成功");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create monitorfilesystem successful ! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create monitorfilesystem error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "文件系统添加失败！");
			return JsonHelper.toJson(resultMap);
		}
	}

	public String deleteMonitorfilesystem(HashMap<String, Object> paramMap) {
		try {
		dbService.delete(DBServiceConst.DELETE_MONITOR_HOST_FILESYSTEM,paramMap);
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("resultCode","SUCCESS");
		resultMap.put("resultMsg","文件系统删除成功"); 
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete monitorfilesystem successful ! the result is :" +result);
		return result;
	  } catch (Exception e){
		logger.error("delete monitorfilesystem error!", e);
		HashMap<String, Object> resultMap = new HashMap<String, Object>(); 
		resultMap.put("resultCode", "DATABASE_FAIL");
		resultMap.put("resultMsg", "文件系统删除失败！");
		return JsonHelper.toJson(resultMap);
	}
  }

	public String listMonitorHostCpuUsage(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> cpuUsageResultList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> defaultGraphParamMap = new HashMap<String, Object>();
		defaultGraphParamMap.put("ids", "-3,-4,-5");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
		
		Integer monitorHostId = Integer.parseInt(paramMap.get("monitorHostId").toString());
		if (monitorHostId != null && monitorHostId != -1) {
			parseMonitorGraphs(monitorHostId, graphResultMap);
			List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
			for (HashMap<String, Object> graph : graphRecord) {
				List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

				List<Integer> itemList = new ArrayList<>();
				for (int j = 0; j < refItems.size(); j++) {
					HashMap<String, Object> refItem = refItems.get(j);
					itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
				}
				
				HashMap<String, Object> actionParamMap = new HashMap<>();
				actionParamMap.put("endTime", new Date().getTime() / 1000);
				actionParamMap.put("itemList", itemList);
				actionParamMap.put("panelType", EMonitorPanelType.MULITY);
				actionParamMap.put("length", 1);

				try {
					String loginId = paramMap.get("curLoginId").toString();
					Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
					if (result.getResultCode().equals(EResultCode.SUCCESS)) {
						List<HashMap<String, Object>> resultList = result.getResultObj();
						HashMap<String, Object> itemDataMap = resultList.get(0);
						List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

						for (int j = 0; j < itemKeys.size(); j++) {
							@SuppressWarnings("unchecked")
							List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) itemDataMap.get(itemKeys.get(j));
							HashMap<String, Object> dataMap = dataList.get(0);
							int itemId = Integer.parseInt(dataMap.get("itemid").toString());
							
							for (HashMap<String, Object> miItem : refItems) {
								int monitorItemId = Integer.parseInt(miItem.get("itemid").toString());
								if(itemId == monitorItemId) {
									HashMap<String, Object> cpuUsageMap = new HashMap<String, Object>();
									cpuUsageMap.put((String)miItem.get("key_"), dataMap.get("value"));
									cpuUsageResultList.add(cpuUsageMap);
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("query monitor host cpu usage data error", e);
				}
			}
		}
		return JsonHelper.toJson(cpuUsageResultList);
	}
	
	public String listMonitorHostRamUsage(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> cpuUsageResultList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> defaultGraphParamMap = new HashMap<String, Object>();
		defaultGraphParamMap.put("ids", "-10,-12,-13");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
		
		Integer monitorHostId = Integer.parseInt(paramMap.get("monitorHostId").toString());
		if (monitorHostId != null && monitorHostId != -1) {
			parseMonitorGraphs(monitorHostId, graphResultMap);
			List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
			for (HashMap<String, Object> graph : graphRecord) {
				List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

				List<Integer> itemList = new ArrayList<>();
				for (int j = 0; j < refItems.size(); j++) {
					HashMap<String, Object> refItem = refItems.get(j);
					itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
				}
				
				HashMap<String, Object> actionParamMap = new HashMap<>();
				actionParamMap.put("endTime", new Date().getTime() / 1000);
				actionParamMap.put("itemList", itemList);
				actionParamMap.put("panelType", EMonitorPanelType.MULITY);
				actionParamMap.put("length", 1);

				try {
					String loginId = paramMap.get("curLoginId").toString();
					Result result = monitorActionWrapper.doExcutionAction(loginId, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
					if (result.getResultCode().equals(EResultCode.SUCCESS)) {
						List<HashMap<String, Object>> resultList = result.getResultObj();
						HashMap<String, Object> itemDataMap = resultList.get(0);
						List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

						for (int j = 0; j < itemKeys.size(); j++) {
							@SuppressWarnings("unchecked")
							List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) itemDataMap.get(itemKeys.get(j));
							HashMap<String, Object> dataMap = dataList.get(0);
							int itemId = Integer.parseInt(dataMap.get("itemid").toString());
							
							for (HashMap<String, Object> miItem : refItems) {
								int monitorItemId = Integer.parseInt(miItem.get("itemid").toString());
								if(itemId == monitorItemId) {
									HashMap<String, Object> cpuUsageMap = new HashMap<String, Object>();
									cpuUsageMap.put((String)miItem.get("key_"), dataMap.get("value"));
									cpuUsageResultList.add(cpuUsageMap);
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("query monitor host cpu usage data error", e);
				}
			}
		}
		return JsonHelper.toJson(cpuUsageResultList);
	}
	
	public String listMonitorHostNetworkUsage(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> networkUsageResultList = new ArrayList<HashMap<String, Object>>();
		Integer monitorHostId = Integer.parseInt(paramMap.get("monitorHostId").toString());
		if (monitorHostId != null && monitorHostId != -1) {
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("graphIds", "-14,-15");
			HashMap<String, Object> graphItemResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_DISCOVERY_GRAPH_ITEMS, parameters);
			List<HashMap<String, Object>> graphItemRecord = (List<HashMap<String, Object>>) graphItemResultMap.get("record");
			
			for (HashMap<String, Object> graphItem : graphItemRecord) {
				HashMap<String, Object> itemParamMap = new HashMap<String, Object>();
				itemParamMap.put("templateId", graphItem.get("itemid"));
				itemParamMap.put("hostId", monitorHostId);
				List<HashMap<String, Object>> discoveryItemList = zabbixDbService.directSelect(DBServiceConst.SELECT_ZABBIX_DISCOVERY_ITEM, itemParamMap);
				for (HashMap<String, Object> discoveryItem : discoveryItemList) {
					HashMap<String, Object> actionParamMap = new HashMap<>();
					actionParamMap.put("itemId", discoveryItem.get("itemid"));
					actionParamMap.put("endTime", new Date().getTime() / 1000);
					actionParamMap.put("historyTableName", "history_uint");
					actionParamMap.put("length", 1);
					
					List<HashMap<String, Object>> discoveryItemValues = zabbixDbService.directSelect(DBServiceConst.SELECT_ZABBIX_DISCOVERY_ITEM_VALUE, actionParamMap);
					if(discoveryItemValues.size()>0) {
						discoveryItem.put("value", discoveryItemValues.get(0).get("value"));
						networkUsageResultList.add(discoveryItem);
					}
				}
			}
		}
		return JsonHelper.toJson(networkUsageResultList);
	}
	
	public String listMonitorHostDiskUsage(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> networkUsageResultList = new ArrayList<HashMap<String, Object>>();
		Integer monitorHostId = Integer.parseInt(paramMap.get("monitorHostId").toString());
		if (monitorHostId != null && monitorHostId != -1) {
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("graphIds", "-16,-17");
			HashMap<String, Object> graphItemResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_DISCOVERY_GRAPH_ITEMS, parameters);
			List<HashMap<String, Object>> graphItemRecord = (List<HashMap<String, Object>>) graphItemResultMap.get("record");
			
			for (HashMap<String, Object> graphItem : graphItemRecord) {
				HashMap<String, Object> itemParamMap = new HashMap<String, Object>();
				itemParamMap.put("templateId", graphItem.get("itemid"));
				itemParamMap.put("hostId", monitorHostId);
				List<HashMap<String, Object>> discoveryItemList = zabbixDbService.directSelect(DBServiceConst.SELECT_ZABBIX_DISCOVERY_ITEM, itemParamMap);
				for (HashMap<String, Object> discoveryItem : discoveryItemList) {
					HashMap<String, Object> actionParamMap = new HashMap<>();
					actionParamMap.put("itemId", discoveryItem.get("itemid"));
					actionParamMap.put("endTime", new Date().getTime() / 1000);
					actionParamMap.put("historyTableName", "history_uint");
					actionParamMap.put("length", 1);
					
					List<HashMap<String, Object>> discoveryItemValues = zabbixDbService.directSelect(DBServiceConst.SELECT_ZABBIX_DISCOVERY_ITEM_VALUE, actionParamMap);
					if(discoveryItemValues.size()>0) {
						discoveryItem.put("value", discoveryItemValues.get(0).get("value"));
						networkUsageResultList.add(discoveryItem);
					}
				}
			}
		}
		return JsonHelper.toJson(networkUsageResultList);
	}
	
	public String listMonitorSite(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if(paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1: startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_MONITOR_WEB_SCENES, paramMap, currentPage, length);
		}else {
			resultMap = dbService.select(DBServiceConst.SELECT_MONITOR_WEB_SCENES, paramMap);
		}
		logger.debug("query listMonitorSite successful! ");
		return JsonHelper.toJson(resultMap);
	}
	
	public String createMonitorSite(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.INSERT_MONITOR_WEB_SCENE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "站点添加成功");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create monitorSite successful! the result is:" + result);
			return result;
		} catch (Exception e){
			logger.error("create monitorSite error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>(); 
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "站点添加失败！");
			return JsonHelper.toJson(resultMap);
		}
	}


	public String updateMonitorSite(HashMap<String, Object> paramMap) {
		try {
			dbService.update(DBServiceConst.UPDATE_MONITOR_WEB_SCENE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "站点编辑成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update monitorSite successful! the result is:" + result);
			return result;
		} catch (Exception e){
			logger.error("update monitorSite error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>(); 
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "站点编辑失败！");
			return JsonHelper.toJson(resultMap);
		}
	}


	public String deleteMonitorSite(HashMap<String, Object> paramMap) {
		try {
			dbService.delete(DBServiceConst.DELETE_MONITOR_WEB_SCENE, paramMap);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMsg", "站点删除成功！");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete monitorSite successful! the result is:" + result);
			return result;
		} catch (Exception e){
			logger.error("delete monitorSite error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>(); 
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "站点删除失败！");
			return JsonHelper.toJson(resultMap);
		}
	}


	  //查询进程
	public String listMonitorProcesGroup(HashMap<String, Object> paramMap) {
		HashMap<String,Object> process = dbService.select(DBServiceConst.SELECT_MONITOR_PROCESS_GROUP, paramMap);
		return JsonHelper.toJson(process);
	}
 
	// 增加告警信息
	public String creatMonitornotification(HashMap<String,Object> paramMap) {
		
		try {
			dbService.insert(DBServiceConst.INSERT_MONITOR_EVENT_NOTIFICATION, paramMap);
			HashMap<String, Object>resultMap = new HashMap<String,Object>();
			resultMap.put("resultCode", "SUCESS");
			resultMap.put("resultMsg", "告警添加成功");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("create monitoreventnotification successful ! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("create monitoreventnotification error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "告警添加失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
   
	 //修改更新告警信息
	public String updateMonitornotification(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.UPDATE_MONITOR_EVENT_NOTIFICATION, paramMap);
			HashMap<String, Object>resultMap = new HashMap<String,Object>();
			resultMap.put("resultCode", "SUCESS");
			resultMap.put("resultMsg", "告警修改成功");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update monitoreventnotification successful ! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("update monitoreventnotification error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "告警修改失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
       
	 
	 //删除告警信息
	public String deleteMonitornotification(HashMap<String, Object> paramMap) {
		try {
			dbService.insert(DBServiceConst.DELETE_MONITOR_EVENT_NOTIFICATION, paramMap);
			HashMap<String, Object>resultMap = new HashMap<String,Object>();
			resultMap.put("resultCode", "SUCESS");
			resultMap.put("resultMsg", "告警删除成功");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("delete monitoreventnotification successful ! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("delete monitoreventnotification error!", e);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", "DATABASE_FAIL");
			resultMap.put("resultMsg", "告警删除失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
   
	
	  //查询告警信息
	public String listMonitorEventNotification(HashMap<String, Object> paramMap) {
		
		HashMap<String,Object> notification =dbService.select(DBServiceConst.SELECT_MONITOR_EVENT_NOTIFICATION, paramMap);
		
		return JsonHelper.toJson(notification);
	}
	
	//查询告警通知记录
	public String listMonitorNotification(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_MONITOR_NOTIFICATION, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NOTIFICATION, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}
	
	
	
	
   
	
	
	
	


	
     
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 
}
