package com.system.started.process.filter;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.CommonTools;
import com.vlandc.oss.model.action.EAction;

public class ProcessReSubmitFilter implements IProcessFilter {
	
	@Autowired
	private DBService dbService;

	@Override
	public String doFilter(String loginId, HashMap<String, Object> request) {
		EAction action = (EAction) request.get("action");
		
		Integer resourceId = null;
		if (request.containsKey("resourceIdArray")) {
			Integer[] resourceIdArray = (Integer[])request.get("resourceIdArray");
			resourceId = resourceIdArray[0];
		}else if (request.containsKey("resourceUuidArray")) {
			String regionName = (String) request.get("regionName");
			String[] resourceUuidArray = (String[])request.get("resourceUuidArray");
			resourceId = CommonTools.genterateRnId(resourceUuidArray[0], regionName);
		}
		
		if (resourceId != null && !action.name().startsWith("OPERATE_")) {
			return validateRequest(action.name(),resourceId);
		}
		return null;
	}
	
	private String validateRequest(String resourceType,Integer resourceId){
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("resourceType", resourceType);
		parameter.put("resourceId", resourceId);
		Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_PROCESSE_RESOURCE_EXIST, parameter );
		if (countSize > 0) { //表示该申请已经发起，不能重复发起
			return "该申请已经发起，请等待审批！";
		}
		return null;
	}


}
