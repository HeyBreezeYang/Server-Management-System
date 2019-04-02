package com.system.started.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.wrapper.VirtualActionWrapper;
import com.system.started.constant.GlobalConst;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.CommonTools;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class NetworkService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(NetworkService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private UserService userService;

	@Autowired
	private VirtualActionWrapper virtualActionWrapper;

	public String listNetworks(HttpSession session, HashMap<String, Object> paramMap) throws Exception {
		parseRelationLoginIds(session, paramMap);
		paramMap.put("currentLoginId", session.getAttribute(GlobalConst.SESSION_ATTRIBUTE_LOGINID));

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_NETWORKS, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listNetworks successful! the result is : " + result);
		return result;
	}

	/**
	 * @param paramMap loginId currentLoginId
	 * @return
	 */
	public String listNetworks(HashMap<String, Object> paramMap) {
		String currentLoginId = "";
		if (paramMap.containsKey("currentLoginId") && null != paramMap.get("currentLoginId")) {
			currentLoginId = paramMap.get("currentLoginId").toString();
		}
		paramMap.put("countSize", userService.getSystemUserAdminRole(currentLoginId));

		String mapperId = DBServiceConst.SELECT_RN_EXT_VIR_NETWORKS;
		if (paramMap.containsKey("type") && paramMap.get("type") != null) {
			String catalog = paramMap.get("type").toString();
			if (catalog.equals("proprietary")) {
				mapperId = DBServiceConst.SELECT_RN_EXT_VIR_PROPRIETARY_NETWORKS;
			} else if (catalog.equals("private")) {
				mapperId = DBServiceConst.SELECT_RN_EXT_VIR_PRIVATE_NETWORKS;
			} else if (catalog.equals("public")) {
				mapperId = DBServiceConst.SELECT_RN_EXT_VIR_PUBLIC_NETWORKS;
			}
		}

		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && null != paramMap.get("start") && paramMap.containsKey("length") && !paramMap.get("length").toString().equals("-1")) {
			String length = paramMap.get("length").toString();
			String draw = paramMap.containsKey("draw") && null != paramMap.get("draw") ? paramMap.get("draw").toString() : "";
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;

			resultMap = dbService.selectByPage(mapperId, paramMap, currentPage, Integer.parseInt(length));

			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(mapperId, paramMap);
		}

		List<HashMap<String, Object>> recordList = (List<HashMap<String, Object>>) resultMap.get("record");
		HashMap<String, Object> tempMap = new HashMap();
		tempMap.putAll(paramMap);
		for (HashMap<String, Object> recordItem : recordList) {
			parseSubnetworkCount(recordItem, tempMap);
			parsePortCount(recordItem, tempMap);
		}

		return JsonHelper.toJson(resultMap);
	}

	private void parseSubnetworkCount(HashMap<String, Object> recordItemMap, HashMap<String, Object> paramMap) {
		int subnetIpCount = 0;

		String networkUuid = (String) recordItemMap.get("uuid");
		String regionName = (String) recordItemMap.get("region");
		paramMap.put("networkId", networkUuid);
		paramMap.put("regionName", regionName);
		String subNetworkResult = listSubnets(paramMap);
		HashMap<String, Object> subnetMap = JsonHelper.fromJson(HashMap.class, subNetworkResult);

		if (Integer.parseInt(subnetMap.get("recordsTotal").toString()) > 0) {
			List<HashMap<String, Object>> subnetList = (List<HashMap<String, Object>>) subnetMap.get("record");
			for (int i = 0; i < subnetList.size(); i++) {
				HashMap<String, Object> subnetItemMap = subnetList.get(i);

				String cidr = (String) subnetItemMap.get("cidr");
				String allocationPool = (String) subnetItemMap.get("allocationPools");

				if (allocationPool != null) {
					List<HashMap<String, Object>> allocationPoolList = JsonHelper.fromJson(List.class, allocationPool);
					if (allocationPoolList != null && allocationPoolList.size() > 0) {
						for (int j = 0; j < allocationPoolList.size(); j++) {
							HashMap<String, Object> allocationPoolItem = allocationPoolList.get(j);

							String startIp = (String) allocationPoolItem.get("start");
							String endIp = (String) allocationPoolItem.get("end");

							Long startIpLong = CommonTools.parseIpToLong(startIp);
							Long endIpLong = CommonTools.parseIpToLong(endIp);
							Long ipCount = endIpLong - startIpLong;
							ipCount += 1;

							subnetIpCount += ipCount;
						}
					}
				} else {
					Integer netmask = Integer.valueOf(cidr.substring(cidr.indexOf("/") + 1));
					long ipCount = 1 << (32 - netmask);
					subnetIpCount += ipCount;
				}
			}


		}
		recordItemMap.put("subnetIpCount", subnetIpCount);
	}

	private void parsePortCount(HashMap<String, Object> recordItemMap, HashMap<String, Object> paramMap) {
		String networkUuid = (String) recordItemMap.get("uuid");
		String regionName = (String) recordItemMap.get("region");
		paramMap.put("networkId", networkUuid);
		paramMap.put("regionName", regionName);
		String portResult = listPorts(paramMap);
		HashMap<String, Object> subnetMap = JsonHelper.fromJson(HashMap.class, portResult);

		recordItemMap.put("portIpCount", subnetMap.get("recordsTotal"));
	}

	public String listNetworkDetails(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_PRIVATE_NETWORKS, paramMap);
		List<HashMap<String, Object>> recordList = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> recordItem : recordList) {
			parseSubnetworkCount(recordItem, paramMap);
			parsePortCount(recordItem, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}

	public String createNetwork(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			paramMap.put("createUser", loginId);
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_CREATE_NETWORK, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateNetwork(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_UPDATE_NETWORK, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteNetwork(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			int networkId = (int) paramMap.get("networkId");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DELETE_NETWORK, new HashMap<String, Object>(), networkId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String createSubnet(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String projectId = paramMap.getOrDefault("projectId", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_CREATE_NETWORK_SUBNETS, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listSubnets(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_NETWORK_SUBNETS, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String deleteSubnet(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String projectId = paramMap.getOrDefault("projectId", "").toString();
		String subnetUuid = paramMap.getOrDefault("subnetUuid", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DELETE_NETWORK_SUBNET, paramMap, subnetUuid);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateSubnet(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String projectId = paramMap.getOrDefault("projectId", "").toString();
		String subnetUuid = paramMap.getOrDefault("subnetUuid", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_UPDATE_NETWORK_SUBNET, paramMap, subnetUuid);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}


	public String listPortDetails(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_NETWORK_PORTS, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String listPorts(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_NETWORK_PORTS, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String listIpAddress(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_NETWORK_IPADDRESS, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String listIpAddressDetail(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_NETWORK_IPADDRESS_DETAIL, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String createPort(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, EAction.VIRTUAL_LIST_NETWORK_PORTS, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deletePort(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String projectId = paramMap.getOrDefault("projectId", "").toString();
		String portUuid = paramMap.getOrDefault("portUuid", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DELETE_NETWORK_PORT, paramMap, portUuid);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updatePort(HashMap<String, Object> paramMap) {
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		String projectId = paramMap.getOrDefault("projectId", "").toString();
		String portUuid = paramMap.getOrDefault("portUuid", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_LIST_NETWORK_PORTS, paramMap, portUuid);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String validateIpInRange(HashMap<String, Object> paramMap) {
		String ipAddress = paramMap.getOrDefault("ipAddress", "").toString();
		HashMap<String, Object> subnetResultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_NETWORK_SUBNETS, paramMap);
		List<HashMap<String, Object>> subnets = (List<HashMap<String, Object>>) subnetResultMap.get("record");
		boolean verifyResult = false;
		for (int i = 0; i < subnets.size(); i++) {
			HashMap<String, Object> subnet = subnets.get(i);
			String cidr = (String) subnet.get("cidr");

			try {
				verifyResult = CommonUtil.validateIpInRange(ipAddress, cidr);
			} catch (Exception e) {

			}
			if (verifyResult) {
				break;
			}
		}
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("messageStatus", verifyResult);
		return JsonHelper.toJson(resultMap);
	}
}
