package com.system.started.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.system.started.action.wrapper.MonitorActionWrapper;
import com.system.started.action.wrapper.ResourceActionWrapper;
import com.system.started.action.wrapper.VirtualActionWrapper;
import com.system.started.constant.GlobalConst;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.AESHelper;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.monitor.EMonitorPanelType;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class ServerService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(ServerService.class);

	@Autowired
	private DBService dbService;
	@Autowired
	private DBService zabbixDbService;

	@Autowired
	private UserService userService;

	@Autowired
	private NetworkService networkService;

	@Autowired
	private VirtualActionWrapper virtualActionWrapper;
	
	@Autowired
	private MonitorActionWrapper monitorActionWrapper;
	
	@Autowired
	private ResourceActionWrapper resourceActionWrapper;

	/**
	 * 通过参数map获取虚机
	 *
	 * @param paramMap
	 * @return server列表
	 * @throws Exception
	 */
	public String listServers(HashMap<String, Object> paramMap) throws Exception {
		HashMap<String, Object> resultMap = null;
		List<String> sortHostIdList = queryTopMonitorHost(paramMap);
		if (sortHostIdList != null && sortHostIdList.size() > 0) {
			String sortHostIdsStr = StringUtils.join(sortHostIdList, ",");
			paramMap.put("hostids", sortHostIdsStr);
		}
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES, paramMap);
		}
		if (paramMap.containsKey("isComputePrice") && (Boolean) paramMap.get("isComputePrice")) {
			queryServerUnitPrice(resultMap);
		}
		resultMap.put("loginId", paramMap.get("loginId"));
//		queryServerPropTags(resultMap);

		queryMonitorData(resultMap);
		
		
		
		try {
			Result result = virtualActionWrapper.doExcutionAction("VmwareRegion", "admin", null, EAction.VIRTUAL_EXTENSION_LIST_VM_INFORMATION, new HashMap<>());
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		
		return JsonHelper.toJson(resultMap);
	}

	/**
	 * 获取虚机价格信息
	 *
	 * @param serverResultMap
	 */

	private void queryServerUnitPrice(HashMap<String, Object> serverResultMap) {
		List<HashMap<String, Object>> serverRecord = (List<HashMap<String, Object>>) serverResultMap.get("record");
		for (HashMap<String, Object> serverItemMap : serverRecord) {
			List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_INSTANCE_UNIT_PRICE, serverItemMap);
			serverItemMap.put("unitPrice", resultList);
		}
	}

	/**
	 * 获取虚机标签
	 *
	 * @param serverResultMap
	 */
	private void queryServerPropTags(HashMap<String, Object> serverResultMap) {
		List<HashMap<String, Object>> serverRecord = (List<HashMap<String, Object>>) serverResultMap.get("record");
		for (HashMap<String, Object> serverItemMap : serverRecord) {
			HashMap<String, Object> parameter = new HashMap<>();
			parameter.put("resourceId", serverItemMap.get("id"));
			parameter.put("loginId", serverResultMap.get("loginId"));

			List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_RESOURCE_PROPERTY_TAG_VALUES, parameter);
			serverItemMap.put("propTags", resultList);
		}
	}

	/**
	 * 获取监控信息
	 *
	 * @param paramMap
	 * @return
	 */
	private List<String> queryTopMonitorHost(HashMap<String, Object> paramMap) {
		List<String> realList = new ArrayList<>();
		if (paramMap.containsKey("sortGraphId") && null != paramMap.get("sortGraphId")) {
			List<HashMap<String, Object>> targetItemsResult = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_ITEMS, paramMap);
			if (targetItemsResult != null && targetItemsResult.get(0) != null) {
				String targetItems = (String) targetItemsResult.get(0).get("items");
				paramMap.put("itemIds", targetItems);
				List<HashMap<String, Object>> resultList = zabbixDbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_HOSTORDER, paramMap);
				for (int i = 0; i < resultList.size(); i++) {
					realList.add(String.valueOf(resultList.get(i).get("hostId")));
				}
			}
		}
		return realList;
	}

	/**
	 * 获取监控图表数据
	 *
	 * @param hostId
	 * @param graphResultMap
	 */
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

	/**
	 * 获取监控数据
	 *
	 * @param serverResultMap
	 */
	private void queryMonitorData(HashMap<String, Object> serverResultMap) {
		List<HashMap<String, Object>> serverRecord = (List<HashMap<String, Object>>) serverResultMap.get("record");
		HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, new HashMap<String, Object>());
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

	/**
	 * 根据条件获取虚机信息
	 *
	 * @param paramMap: id 、 virtualizationType 、 poolType
	 * @return
	 */
	public String getServer(HashMap<String, Object> paramMap) {
		return JsonHelper.toJson(dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES, paramMap));
	}

	public String listServerInterface(HashMap<String, Object> paramMap) {
		return JsonHelper.toJson(dbService.select(DBServiceConst.SELECT_RN_EXT_INTERFACE, paramMap));
	}

	public void updateServerInterface(HashMap<String, Object> paramMap) {
		HashMap<String, Object> curParamMap = new HashMap<>();
		paramMap.put("nodeId", curParamMap.get("nodeId"));
		dbService.update(DBServiceConst.UPDATE_RN_EXT_DEFAULT_INTERFACE, paramMap);
		paramMap.put("interfaceId", curParamMap.get("interfaceId"));
		dbService.update(DBServiceConst.UPDATE_RN_EXT_DEFAULT_INTERFACE, paramMap);
	}

	public void updateServerManageAuth(HashMap<String, Object> paramMap) {
		if (paramMap.containsKey("stackIds") && ((String) paramMap.get("stackIds")).length() > 0) {
			String stackIdString = (String) paramMap.get("stackIds");
			List<String> stackIds = new ArrayList<String>();
			for (String stackId : stackIdString.split(",")) {
				StringBuffer stringBuffred = new StringBuffer();
				stringBuffred.append("'");
				stringBuffred.append(stackId);
				stringBuffred.append("'");

				stackIds.add(stringBuffred.toString());
			}
			paramMap.put("stackIds", StringUtils.join(stackIds, ","));
			dbService.update(DBServiceConst.UPDATE_OPENSTACK_STACK_MANAGE_USER, paramMap);
		}
		dbService.update(DBServiceConst.UPDATE_RN_BASE_MANAGE_USER, paramMap);

	}

	public String createServer(String loginId, String regionName, HashMap<String, Object> paramMap) {
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_CREATE_SERVER, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteServers(String loginId, String regionName, Integer serverId, String projectId) {
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DELETE_SERVER, paramMap, serverId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String actionServer(HashMap<String, Object> paramMap) {
		try {
			Result result = virtualActionWrapper.doExcutionAction(paramMap.get("regionName").toString(), paramMap.get("loginId").toString(), paramMap.get("projectId").toString(), EAction.VIRTUAL_ACTION_SERVER, paramMap, (int)paramMap.get("serverId"));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String resizeServer(HashMap<String, Object> paramMap) {
		try {
			Result result = virtualActionWrapper.doExcutionAction(paramMap.get("regionName").toString(), paramMap.get("loginId").toString(), paramMap.get("projectId").toString(), EAction.VIRTUAL_RESIZE_SERVER, paramMap, (int)paramMap.get("serverId"));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String confirmResizeServer(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String) paramMap.get("loginId");
			String regionName = (String) paramMap.get("regionName");
			String projectId = (String) paramMap.get("projectId");
			int serverId = (int) paramMap.get("serverId");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId,EAction.VIRTUAL_CONFIRM_RESIZE_SERVER, paramMap,serverId);
			try {
				if(result.getResultCode().equals(EResultCode.SUCCESS)){
					HashMap<String, Object> opResultMap = result.getResultObj().get(0);
					Result opResult = (Result)opResultMap.get(String.valueOf(serverId));
					if(opResult.getResultCode().equals(EResultCode.SUCCESS)){
						// 更新状态为 RESIZE_MIGRATING
						paramMap.put("vmState", "RESIZE_MIGRATING");
						paramMap.put("nodeId", serverId);
						dbService.update(DBServiceConst.UPDATE_RN_EXT_VIR_INSTANCE_INFO, paramMap);
					}
				}
			} catch (Exception e) {
				logger.error("update instance vmState error !", e);
			}
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
	
	public String revertResizeServer(HashMap<String, Object> paramMap) {
		try {
			String loginId = (String) paramMap.get("loginId");
			String regionName = (String) paramMap.get("regionName");
			String projectId = (String) paramMap.get("projectId");
			int serverId = (int) paramMap.get("serverId");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId,EAction.VIRTUAL_REVERT_RESIZE_SERVER, paramMap,serverId);
			try {
				if(result.getResultCode().equals(EResultCode.SUCCESS)){
					HashMap<String, Object> opResultMap = result.getResultObj().get(0);
					Result opResult = (Result)opResultMap.get(String.valueOf(serverId));
					if(opResult.getResultCode().equals(EResultCode.SUCCESS)){
						// 更新状态为 RESIZE_MIGRATING
						paramMap.put("vmState", "RESIZE_MIGRATING");
						paramMap.put("nodeId", serverId);
						dbService.update(DBServiceConst.UPDATE_RN_EXT_VIR_INSTANCE_INFO, paramMap);
					}
				}
			} catch (Exception e) {
				logger.error("update instance vmState error !", e);
			}
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateServer(HashMap<String, Object> paramMap) {
		try {
			Result result = virtualActionWrapper.doExcutionAction(paramMap.get("regionName").toString(), paramMap.get("loginId").toString(), paramMap.get("projectId").toString(), EAction.VIRTUAL_UPDATE_SERVER, paramMap, paramMap.get("serverId").toString());
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateServerSystemInfo(HashMap<String, Object> paramMap) {
		try {
			if (paramMap.containsKey("osPassword") && !((String) paramMap.get("osPassword")).equals("")) {
				String newPassword = AESHelper.encryptString((String) paramMap.get("osPassword"));
				paramMap.put("osPassword", newPassword);
			}
			Result result = resourceActionWrapper.doExcutionAction(paramMap.get("loginId").toString(), null, EAction.RESOURCE_UPDATE_SERVER_OS_INFO, paramMap);
			logger.debug("update server os info result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateServerExpireDay(HashMap<String, Object> paramMap) {
		try {
			Result result = resourceActionWrapper.doExcutionAction(paramMap.get("loginId").toString(), null, EAction.RESOURCE_UPDATE_SERVER_EXPIREDAY, paramMap);
			logger.debug("update server expireDay result :" + JsonHelper.toJson(result));
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			logger.error("find action error !", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listAvailabilityZones(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("curLoginId") && userService.getSystemUserAdminRole(paramMap.get("curLoginId").toString()) > 100) {
			resultMap = dbService.select(DBServiceConst.SELECT_AVAILABILITY_ZONES, paramMap);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_AVAILABILITY_ZONES, paramMap);
		}

		return JsonHelper.toJson(resultMap);
	}

	public String listKeypairs(HashMap<String, Object> paramMap) {
		try {
			Result result = virtualActionWrapper.doExcutionAction(paramMap.get("regionName").toString(), paramMap.get("loginId").toString(), EAction.VIRTUAL_LIST_KEYPAIRS, new HashMap<String, Object>());
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateSystemInfo(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.get("loginId").toString();
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

	public String listHosts(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.get("loginId").toString();
			Result result = virtualActionWrapper.doExcutionAction(paramMap.get("regionName").toString(), loginId, paramMap.get("projectId").toString(), EAction.VIRTUAL_LIST_HOSTS, new HashMap<String, Object>());
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listAggregateHosts(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		String regionType = paramMap.containsKey("regionType") && null != paramMap.get("regionType") ? paramMap.get("regionType").toString() : "";
		if (paramMap.containsKey("start") && null != paramMap.get("start") && paramMap.containsKey("length") && !paramMap.get("length").toString().equals("-1")) {
			String start = paramMap.get("start").toString();
			String length = paramMap.get("length").toString();
			String draw = paramMap.containsKey("draw") && null != paramMap.get("draw") ? paramMap.get("draw").toString() : "";
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_HOST_AGGREGATE_HOSTS, paramMap, currentPage, Integer.parseInt(length));
			List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) resultMap.get("record");
			for (HashMap<String, Object> record : records) {
				if (regionType.equals("POWERVC")) {
//					listPVCStorages(session,record);
//					record.put("localDisk", record.get("diskTotal"));
//					record.put("localDiskUsed", record.get("diskUsed"));
				} else if (regionType.equals("VMWARE")) {
//					listAvailabilityZoneHypervisorHosts(session,record);
//					record.put("vcpus", record.get("vcpusTotal"));
//					record.put("memory", record.get("memoryTotal"));
//					record.put("localDisk", record.get("diskTotal"));
//					record.put("localDiskUsed", record.get("diskUsed"));
				}
			}
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_HOST_AGGREGATE_HOSTS, paramMap);

			List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) resultMap.get("record");
			for (HashMap<String, Object> record : records) {
				if (regionType.equals("POWERVC")) {
//					listPVCStorages(session,record);
//					record.put("localDisk", record.get("diskTotal"));
//					record.put("localDiskUsed", record.get("diskUsed"));
				} else if (regionType.equals("VMWARE")) {
//					listAvailabilityZoneHypervisorHosts(session,record);
//					record.put("vcpus", record.get("vcpusTotal"));
//					record.put("memory", record.get("memoryTotal"));
//					record.put("localDisk", record.get("diskTotal"));
//					record.put("localDiskUsed", record.get("diskUsed"));
				}
			}
		}
		return JsonHelper.toJson(resultMap);
	}

	public String listAggregates(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		String loginId = paramMap.get("loginId").toString();
		String curLoginId = paramMap.get("curLoginId").toString();
		if (paramMap.containsKey("start") && null != paramMap.get("start") && paramMap.containsKey("length") && !paramMap.get("length").toString().equals("-1")) {
			String length = paramMap.get("length").toString();
			String draw = paramMap.containsKey("draw") && null != paramMap.get("draw") ? paramMap.get("draw").toString() : "";
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_HOST_AGGREGATES, paramMap, currentPage, Integer.parseInt(paramMap.get("length").toString()));

			List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) resultMap.get("record");
			for (HashMap<String, Object> record : records) {
				String regionType = (String) record.get("regionType");
				if (regionType.equals("POWERVC")) {
//					listPVCStorages(session,record);
				} else if (regionType.equals("VMWARE")) {
					listAvailabilityZoneHypervisorHosts(curLoginId, record);
				}

				listIpCount(loginId, curLoginId, record);
			}
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_HOST_AGGREGATES, paramMap);

			List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) resultMap.get("record");
			for (HashMap<String, Object> record : records) {
				String regionType = (String) record.get("regionType");
				if (regionType.equals("POWERVC")) {
//					listPVCStorages(session,record);
				} else if (regionType.equals("VMWARE")) {
					listAvailabilityZoneHypervisorHosts(curLoginId, record);
				}
				listIpCount(loginId, curLoginId, record);
			}
		}
		return JsonHelper.toJson(resultMap);
	}

	private void listIpCount(String loginId, String currentLoginId, HashMap<String, Object> record) {
		int ipCount = 0;
		try {
			String regionName = (String) record.get("region");
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("catalog", "private");
			paramMap.put("regionName", regionName);
			paramMap.put("length", "-1");
			paramMap.put("loginId", loginId);
			paramMap.put("currentLoginId", currentLoginId);
			String resultString = networkService.listNetworks(paramMap);
			HashMap<String, Object> resultMap = JsonHelper.fromJson(HashMap.class, resultString);
			List<HashMap<String, Object>> recordList = (List<HashMap<String, Object>>) resultMap.get("record");
			for (int i = 0; i < recordList.size(); i++) {
				HashMap<String, Object> recordItemMap = recordList.get(i);
				int networkId = Integer.parseInt((String) recordItemMap.get("id"));
				paramMap.put("networkId", networkId);
				String detailResultString = networkService.listNetworkDetails(paramMap);
				HashMap<String, Object> detailResultMap = JsonHelper.fromJson(HashMap.class, detailResultString);
				HashMap<String, Object> detailRecordMap = ((List<HashMap<String, Object>>) detailResultMap.get("record")).get(0);
				int subnetIpCount = 0;
				if (detailRecordMap.get("subnetIpCount") != null) {
					subnetIpCount = (int) detailRecordMap.get("subnetIpCount");
				}

				ipCount += subnetIpCount;
			}
		} catch (Exception e) {
			logger.error("get ip count error!", e);
		}
		record.put("ipCount", ipCount);
	}

	private void listPVCStorages(HttpSession session, HashMap<String, Object> record) {
		try {
			String loginId = (String) session.getAttribute(GlobalConst.SESSION_ATTRIBUTE_LOGINID);
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

	private void listAvailabilityZoneHypervisorHosts(String currentLoginId, HashMap<String, Object> record) {
		try {
			String regionName = (String) record.get("region");
			HashMap<String, Object> paramMap = new HashMap<>();
			Result result = virtualActionWrapper.doExcutionAction(regionName, currentLoginId, null, EAction.VIRTUAL_LIST_HYPERVISORS, paramMap);
			logger.debug("hypervisor hosts data :" + JsonHelper.toJson(result));
			if (result.getResultCode().equals(EResultCode.SUCCESS)) {
				List<HashMap<String, Object>> resultObjList = result.getResultObj();
				if (resultObjList.size() > 0) {
					double cpuOversub = 1;
					double ramOversub = 1;
					double diskOversub = 1;
					if (record.get("cpu_oversub") != null) {
						cpuOversub = ((BigDecimal) record.get("cpu_oversub")).doubleValue();
					}
					if (record.get("ram_oversub") != null) {
						ramOversub = ((BigDecimal) record.get("ram_oversub")).doubleValue();
					}
					if (record.get("disk_oversub") != null) {
						diskOversub = ((BigDecimal) record.get("disk_oversub")).doubleValue();
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

	public String listAggregateDetails(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_LIST_AGGREGATE_ITEM, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String createAggregate(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_CREATE_AGGREGATE, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateAggregate(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_UPDATE_AGGREGATE, paramMap);
//			if(result.getResultCode().equals(EResultCode.SUCCESS)){
//				HashMap<String,Object>parameterMap = new HashMap<String,Object>();
//				parameterMap.put("id", String.valueOf(aggregateId));
//				parameterMap.put("region", regionName);
//				parameterMap.put("availability_zone_zh_cn", ((HashMap<String,Object>)paramMap.get("aggregate")).get("availability_zone_zh_cn"));
//
//				dbService.update(DBServiceConst.UPDATE_HOST_AGGREGATE, parameterMap);
//			}
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateAilabilityZoneOversub(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_HOST_AGGREGATE, paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		resultMap.put("responseMsg", "设置超分比例成功");
		return JsonHelper.toJson(resultMap);
	}

	public String deleteAggregate(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_DELETE_AGGREGATE, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateAggregateHosts(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		try {
			EAction action = null;
			if(paramMap.containsKey("add_host") && null == paramMap.get("add_host")){
				paramMap.remove("add_host");
			}
			if(paramMap.containsKey("remove_host") && null == paramMap.get("remove_host")){
				paramMap.remove("remove_host");
			}
			
			if (paramMap.containsKey("add_host")) {
				action = EAction.VIRTUAL_CREATE_AGGREGATE_HOSTS;
			} else if (paramMap.containsKey("remove_host")) {
				action = EAction.VIRTUAL_DELETE_AGGREGATE_HOSTS;
			}
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, action, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateAggregateMetadata(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			String aggregateId = paramMap.getOrDefault("aggregateId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DELETE_AGGREGATE, paramMap, aggregateId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listAggregateResourceNodeUnitPrice(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_HOST_AGGREGATES, paramMap);
		List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> resultItem : resultList) {
			paramMap.put("type", "AGGREGATE");
			paramMap.put("id", resultItem.get("id"));

			List<HashMap<String, Object>> unitPriceList = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_UNIT_PRICE, paramMap);
			resultItem.put("unitPriceList", unitPriceList);
		}
		return JsonHelper.toJson(resultMap);
	}

	public String listAggregateHistory(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_HOST_AGGREGATES, paramMap);
		for (HashMap<String, Object> resultItem : resultList) {
			String regionType = (String) resultItem.get("resultItem");
			if (regionType != null && !regionType.equals("VMWARE")) {
				paramMap.put("aggregateId", resultItem.get("id"));
			}
			paramMap.put("region", resultItem.get("region"));
			List<HashMap<String, Object>> historyList = dbService.directSelect(DBServiceConst.SELECT_HOST_AGGREGATE_HISTORY, paramMap);
			resultItem.put("history", historyList);
		}
		return JsonHelper.toJson(resultList);
	}

	public String instanceResetCharge(String nodeId) {
		HashMap<String, Object> parameter = new HashMap<>();
		HashMap<String, Object> resultMap = new HashMap<>();
		if (null != nodeId) {
			parameter.put("nodeId", nodeId);
			dbService.directSelect(DBServiceConst.DELETE_RN_EXT_INSTANCE_CHARGE, parameter);
			logger.debug("DELETE_RN_EXT_INSTANCE_CHARGE successful! ");
			dbService.directSelect(DBServiceConst.DELETE_RN_EXT_INSTANCE_CHARGE_HISTORY, parameter);
			logger.debug("DELETE_RN_EXT_INSTANCE_CHARGE_HISTORY successful! ");
			resultMap.put("messageStatus", "END");
			logger.debug("delete deleteInstanceCharge successful! ");
		} else {
			resultMap.put("messageStatus", "ERROR");
			logger.debug("delete deleteInstanceCharge fail ! ");
		}
		return JsonHelper.toJson(resultMap);
	}

	public String listInstanceStackInstances(HashMap<String, Object> paramMap) {
		String nodeIds = paramMap.getOrDefault("nodeIds", "").toString();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (String nodeId : nodeIds.split(",")) {
			paramMap.clear();
			paramMap.put("id", nodeId);
			List<HashMap<String, Object>> instances = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES, paramMap);
			if (instances.size() > 0) {
				HashMap<String, Object> instanceObj = instances.get(0);
				paramMap.clear();
				String refStack = (String) instanceObj.get("refStack");
				if (StringUtils.isNotEmpty(refStack) && !refStack.equals("SINGLE")) {
					HashMap<String, Object> refStackMap = JsonHelper.fromJson(HashMap.class, refStack);
					paramMap.put("refStack", refStackMap.get("name"));

					List<HashMap<String, Object>> stackInstances = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES, paramMap);
					instanceObj.put("stackInstances", stackInstances);
				}

				list.add(instanceObj);
			}
		}
		logger.debug("query listInstanceStackInstances successful! ");
		return JsonHelper.toJson(list);
	}
	
	
	public String listServerOsEnvironment(HashMap<String, Object> paramMap){
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
	
	public String listServerOsEnvironmentDetails(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_OS_ENVIRONMENT, paramMap);
		return JsonHelper.toJson(resultMap);
	}
}
