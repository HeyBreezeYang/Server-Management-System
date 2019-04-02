package com.system.started.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.system.started.cache.impl.UserCache;
import com.system.started.constant.GlobalConst;
import com.system.started.process.ProcessManager;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

public abstract class AbstractService {
	
	@Value("${oss.apigate.config.admin-login-id}")
	String ADMIN_LOGIN_ID;
	
	@Autowired
	private UserCache userCache;
	
	@Autowired
	ProcessManager processManager;

	protected String invalidRequest() {
		Result result = new Result();
		result.setResultCode(EResultCode.PARAM_VALIDATE_FAIL);
		result.setResultMsg("操作失败");
		return JsonHelper.toJson(result);
	}
	
	protected String invalidRequest(String responseMessage) {
		Result result = new Result();
		result.setResultCode(EResultCode.PARAM_VALIDATE_FAIL);
		result.setResultMsg(responseMessage);
		return JsonHelper.toJson(result);
	}
	
	protected void parseCurrentLoginIds(HttpSession session, HashMap<String, Object> paramMap) {
		paramMap.put("loginId", session.getAttribute(GlobalConst.SESSION_ATTRIBUTE_LOGINID));
	}

	protected void parseRelationLoginIds(HttpSession session, HashMap<String, Object> paramMap) throws Exception {
		String loginId = (String) session.getAttribute(GlobalConst.SESSION_ATTRIBUTE_LOGINID);
		String relationLoginIdS = userCache.getSubRelationLoginIds(loginId);
		paramMap.put("loginId", relationLoginIdS);
	}
	
	protected List<HashMap<String, Object>> compareFieldValue(List<HashMap<String, Object>> fieldValues, List<HashMap<String, Object>> dbFieldValues) {
		List<HashMap<String, Object>> resultMapList = new ArrayList<HashMap<String, Object>>();
		
		List<Integer> dbFieldIdList = new ArrayList<Integer>();
		for (HashMap<String, Object> dbItemMap : dbFieldValues) {
			dbFieldIdList.add((Integer) dbItemMap.get("fieldId"));
		}
		for (HashMap<String, Object> itemMap : fieldValues) {
			int fieldId = (int) itemMap.get("fieldId");
			String fieldValue = (String) itemMap.get("value");
			int position = dbFieldIdList.indexOf(fieldId);
			if(position == -1) {
				resultMapList.add(itemMap);
			} else {
				HashMap<String, Object> dbItemMap = dbFieldValues.get(position);
				String dbFieldValue = dbItemMap.get("value").toString();
				if(!dbFieldValue.equals(fieldValue)) {
					itemMap.put("label", dbItemMap.get("label"));
					itemMap.put("oldValue", dbFieldValue);
					resultMapList.add(itemMap);
				}
			}
		}
		return resultMapList;
	}
	
	protected List<HashMap<String, Object>> compareBaseFieldValue(HashMap<String, Object> fieldValue, HashMap<String, Object> dbFieldValue) {
		List<HashMap<String, Object>> resultMapList = new ArrayList<HashMap<String, Object>>();
		if(!fieldValue.containsKey("name") || !dbFieldValue.get("name").equals(fieldValue.get("name"))) {
			HashMap<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("label", "名称");
			if(!fieldValue.containsKey("name")) {
				itemMap.put("value", "无");
			}else {
				itemMap.put("value", fieldValue.get("name"));
			}
			itemMap.put("oldValue", dbFieldValue.get("name"));
			resultMapList.add(itemMap);
		}
		
		if(!(dbFieldValue.get("serialNumber")).equals(fieldValue.get("serialNumber"))) {
			HashMap<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("label", "序列号");
			if(!fieldValue.containsKey("serialNumber")) {
				itemMap.put("value", "无");
			}else {
				itemMap.put("value", fieldValue.get("serialNumber"));
			}
			itemMap.put("oldValue", dbFieldValue.get("serialNumber"));
			resultMapList.add(itemMap);
		}
		
		if(!(dbFieldValue.get("ipAddress")).equals(fieldValue.get("ipAddress"))) {
			HashMap<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("label", "IP地址");
			if(!fieldValue.containsKey("ipAddress")) {
				itemMap.put("value", "无");
			}else {
				itemMap.put("value", fieldValue.get("ipAddress"));
			}
			itemMap.put("oldValue", dbFieldValue.get("ipAddress"));
			resultMapList.add(itemMap);
		}
		return resultMapList;
	}
}
