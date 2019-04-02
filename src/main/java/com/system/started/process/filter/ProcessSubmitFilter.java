package com.system.started.process.filter;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.process.EProcessEngineType;
import com.system.started.process.EProcessType;
import com.system.started.process.ProcessManager;
import com.vlandc.oss.model.action.EAction;


public class ProcessSubmitFilter implements IProcessFilter {
	
	@Autowired
	private DBService dbService;

	@Autowired
	private ProcessManager processManager;

	@Override
	public String doFilter(String loginId, HashMap<String, Object> requestParam) {
		EAction action = (EAction) requestParam.get("action");
		String processType = EProcessType.REQUEST_APPROVE.name();
		if (action.name().startsWith("OPERATE_")) {
			processType = EProcessType.OPERATE_APPROVE.name();
		}else if(action.name().equals("VIRTUAL_CREATE_STACK")){
			processType = EProcessType.DEPLOY_APPROVE.name();
		}
		if(!processManager.getProcessEngineMap().get(processType).getEngineType().equals(EProcessEngineType.LOCAL)){
			return null;
		}
		
		// 针对本地流程验证当前用户部门是否已定义流程
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("CREATE_USER", loginId);
		
		if (action.name().startsWith("OPERATE_")) {
			HashMap<String, Object> actionParam = (HashMap<String, Object>) requestParam.get("actionParam");
			
			List<HashMap<String, Object>> serviceList = (List<HashMap<String, Object>>) actionParam.get("serviceList");
			long totalSize = 0;
			for (int i = 0; i < serviceList.size(); i++) {
				HashMap<String, Object> serviceItem = serviceList.get(i);
				int templateId = (int) serviceItem.get("templateId");
				paramMap.put("subType", templateId);
				HashMap<String, Object> moduleTypeMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TYPE_BY_DEPARTMENT, paramMap);
				List<HashMap<String, Object>> recordListMap = (List<HashMap<String, Object>>) moduleTypeMap.get("record");
				totalSize += (long) recordListMap.get(0).get("totalSize");
				if (totalSize > 0) {
					return null;
				}
			}
			if (totalSize == 0) {
				return "用户所在部门尚未定义流程，请先定义流程！";
			}
			return null;
		}else{
			paramMap.put("subType", requestParam.get("action"));
		}

		HashMap<String, Object> moduleTypeMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TYPE_BY_DEPARTMENT, paramMap);
		List<HashMap<String, Object>> recordListMap = (List<HashMap<String, Object>>) moduleTypeMap.get("record");
		long totalSize = (long) recordListMap.get(0).get("totalSize");
		if (totalSize == 0) {
			return "用户所在部门尚未定义流程，请先定义流程！";
		}
		
		return null;
	}
}
