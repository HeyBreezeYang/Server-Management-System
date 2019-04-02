package com.system.started.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;
import com.system.started.util.CommonUtil;

@Component
public class LparInstanceDao implements IDao {

	@Autowired
	private DBService dbService;
	
	@Override
	public void insert(HashMap<String, Object> paramMap) {
		paramMap.put("type", "LPAR");
		paramMap.put("subType", "LPAR");
		paramMap.put("name", paramMap.get("lparName"));
		int id=UUID.randomUUID().hashCode();
		paramMap.put("id", id);
		paramMap.put("nodeId", id);
		paramMap.put("resourceId", id);
		
		dbService.insert(DBServiceConst.INSERT_RN_BASE, paramMap);
		
		paramMap.remove("id");
		dbService.insert(DBServiceConst.INSERT_LPAR_INSTANCE, paramMap);
		dbService.insert(DBServiceConst.INSERT_RN_EXT_SYSTEMINFO, paramMap);
		dbService.insert(DBServiceConst.INSERT_RN_EXT_OSINFO, paramMap);
		
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>>interfaceList= (List<HashMap<String, Object>>) paramMap.get("list");
		for (HashMap<String, Object> map : interfaceList) {
			map.put("ipAddressLong", CommonUtil.parseIpToLong(map.get("ipAddress").toString()));
		}
		dbService.insert(DBServiceConst.INSERT_RN_EXT_INTERFACE, paramMap);
		
		//将新增的资源添加到用户的数据权限表中
		List<HashMap<String,Object>> userDataList=dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USER_DATA_MIN, paramMap);
		int resourceId = (int) userDataList.get(0).get("resourceId");
		if(resourceId == -1){
			dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_DATA, paramMap);
			dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_DATA, paramMap);
		}else if(resourceId > 0){
			dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_DATA, paramMap);
		}
	}

	@Override
	public void update(HashMap<String, Object> paramMap) {
		String nodeId=paramMap.get("id").toString();
		paramMap.put("id", nodeId);
		paramMap.put("nodeId", nodeId);
		
		paramMap.put("name", paramMap.get("lparName"));
		
		dbService.update(DBServiceConst.UPDATE_RN_BASE, paramMap);
		dbService.update(DBServiceConst.UPDATE_LPAR_INSTANCE, paramMap);
		
		dbService.update(DBServiceConst.UPDATE_RN_EXT_SYSTEMINFO, paramMap);
		dbService.update(DBServiceConst.UPDATE_RN_EXT_OSINFO, paramMap);
		
		dbService.delete(DBServiceConst.DELETE_RN_EXT_INTERFACE, paramMap);
		
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>>interfaceList= (List<HashMap<String, Object>>) paramMap.get("list");
		for (HashMap<String, Object> map : interfaceList) {
			map.put("ipAddressLong", CommonUtil.parseIpToLong(map.get("ipAddress").toString()));
		}
		dbService.insert(DBServiceConst.INSERT_RN_EXT_INTERFACE, paramMap);
	}

}
