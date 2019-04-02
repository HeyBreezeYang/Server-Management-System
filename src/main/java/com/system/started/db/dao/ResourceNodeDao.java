package com.system.started.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.JsonHelper;

@Component
public class ResourceNodeDao implements IDao{

	@Autowired
	private DBService dbService;
	
	@Transactional
	public void insert(HashMap<String, Object> paramMap){
		//插入基础信息
		//新增成功后，会将新增的nodeId放入paramMap中
		String port = (String) paramMap.get("port");
		if (port == null || port.length() == 0) {
			paramMap.remove("port");
		}
		
		String ipAddress = (String) paramMap.get("osIpAddress");
		long ipLong = 0;
		if (ipAddress != null) {
			ipLong = CommonUtil.parseIpToLong(ipAddress);
		}else{
			paramMap.put("osIpAddress", "0.0.0.0");
		}
		paramMap.put("osIpAddressLong", ipLong);
		
		if (paramMap.containsKey("sysMacAddr")) {
			String macAddress = (String) paramMap.get("sysMacAddr");
			macAddress = macAddress.toUpperCase().replace(":", "-");
			paramMap.put("sysMacAddr", macAddress);
		}

		if (paramMap.containsKey("macAddress")) {
			String macAddress = (String) paramMap.get("macAddress");
			macAddress = macAddress.toUpperCase().replace(":", "-");
			paramMap.put("macAddress", macAddress);
		}
		
		paramMap.put("agentId", paramMap.get("osIpAddress"));
		
		String nodeType = (String)paramMap.get("nodeType");
		int id=Math.abs(UUID.randomUUID().toString().hashCode());
		paramMap.put("id", id);
		paramMap.put("nodeId", id);
		paramMap.put("type", nodeType);
		paramMap.put("resourceId", id);
		
		if (nodeType.equals("COMPUTE")) {
			if (paramMap.containsKey("nodeInfo")) {
				try {
					byte[] byteArray = CommonUtil.Object2Bytes(JsonHelper.toJson(paramMap.get("nodeInfo")));
					paramMap.put("systemInfo", byteArray);
				} catch (Exception e) {
				}
			}
			
			dbService.insert(DBServiceConst.INSERT_RN_BASE, paramMap);
			
			paramMap.remove("id");
			dbService.insert(DBServiceConst.INSERT_RN_EXT_MGMTINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_RACKINFO, paramMap);
			
			dbService.insert(DBServiceConst.INSERT_RN_EXT_OSINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_SYSTEMINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_PHYSICAL_NODE, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_COMPUTE_NODE, paramMap);
			
			String virtualizationType = (String) paramMap.get("virtualizationType");
			if (virtualizationType != null && virtualizationType.equals("IRONIC")) { // 创建ironic node
				dbService.insert(DBServiceConst.INSERT_RESOURCE_COMPUTE_IRONIC_NODE, paramMap);
			}
			//往rn_ext_vir_hypervisor表中写入nodeId
			dbService.insert(DBServiceConst.INSERT_RN_EXT_VIR_HYPERVISOR, paramMap);
			
		}else if(nodeType.equals("OPERATION")){
			paramMap.put("subType", "OPERATION");
			if (paramMap.containsKey("nodeInfo")) {
				try {
					byte[] byteArray = CommonUtil.Object2Bytes(JsonHelper.toJson(paramMap.get("nodeInfo")));
					paramMap.put("systemInfo", byteArray);
				} catch (Exception e) {
				}
			}
			dbService.insert(DBServiceConst.INSERT_RN_BASE, paramMap);
			
			paramMap.remove("id");
			dbService.insert(DBServiceConst.INSERT_RN_EXT_MGMTINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_RACKINFO, paramMap);
			
			dbService.insert(DBServiceConst.INSERT_RN_EXT_OSINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_SYSTEMINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_PHYSICAL_NODE, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_COMPUTE_NODE, paramMap);
		}else if(nodeType.equals("STORAGE")) {
			//dbService.insert(DBServiceConst.INSERT_RN_EXT_STORAGE_NODE, paramMap);
		}else if (nodeType.equals("NETWORK")) {
			paramMap.put("subType", "NETWORK");
			
			dbService.insert(DBServiceConst.INSERT_RN_BASE, paramMap);
			
			paramMap.remove("id");
			dbService.insert(DBServiceConst.INSERT_RN_EXT_MGMTINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_RACKINFO, paramMap);
			
			dbService.insert(DBServiceConst.INSERT_RN_EXT_PHYSICAL_NODE, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_NETWORK_NODE, paramMap);
			//dbService.insert(DBServiceConst.INSERT_RESOURCE_NETWORK_NODE_PORT, paramMap);
		}else if (nodeType.equals("MANAGE")) {
			paramMap.put("subType", "PHYSICAL");
			
			dbService.insert(DBServiceConst.INSERT_RN_BASE, paramMap);
			
			paramMap.remove("id");
			dbService.insert(DBServiceConst.INSERT_RN_EXT_MGMTINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_RACKINFO, paramMap);
			
			dbService.insert(DBServiceConst.INSERT_RN_EXT_OSINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_SYSTEMINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_PHYSICAL_NODE, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_MANAGE_NODE, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_COMPUTE_NODE, paramMap);
		}else if (nodeType.equals("BARE")) {
			paramMap.put("subType", "BARE");
			
			dbService.insert(DBServiceConst.INSERT_RN_BASE, paramMap);
			
			paramMap.remove("id");
			dbService.insert(DBServiceConst.INSERT_RN_EXT_MGMTINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_RACKINFO, paramMap);
			
			dbService.insert(DBServiceConst.INSERT_RN_EXT_OSINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_SYSTEMINFO, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_PHYSICAL_NODE, paramMap);
			dbService.insert(DBServiceConst.INSERT_RN_EXT_COMPUTE_NODE, paramMap);
		}
		
		//将新增的资源添加到用户的数据权限表中
		List<HashMap<String,Object>> userDataList=dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USER_DATA_MIN, paramMap);
		int resourceId= Integer.parseInt(userDataList.get(0).get("resourceId").toString());
		if(resourceId == -1){
			dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_DATA, paramMap);
			dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_DATA, paramMap);
		}else if(resourceId > 0){
			dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_DATA, paramMap);
		}
	}

	@Override
	public void update(HashMap<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}
}
