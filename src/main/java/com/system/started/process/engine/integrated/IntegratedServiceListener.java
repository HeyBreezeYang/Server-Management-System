package com.system.started.process.engine.integrated;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.system.started.action.impl.virtual.VirtualActionImpl;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.process.EProcessStatus;
import com.system.started.process.EProcessType;
import com.system.started.process.impl.IProcessListener;
import com.vlandc.oss.common.CommonTools;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@SuppressWarnings("unchecked")
public class IntegratedServiceListener implements IProcessListener {

	private static Logger logger = Logger.getLogger(IntegratedServiceListener.class);
	@Autowired  
    ApplicationContext context; 
	
	@Autowired
	private DBService dbService;

	@Override
	public void initLisenter() {

	}

	@Override
	public void dealPorcess(HashMap<String, Object> processItemMap, EProcessStatus status, String statusResult, HashMap<String, Object> paramMap) {
		try {
			String processType = (String) processItemMap.get("type");
			if (processType.equals(EProcessType.DEPLOY_APPROVE.toString())) {
				byte[] requestByteArray = (byte[]) processItemMap.get("body");
				HashMap<String, Object> request = (HashMap<String, Object>) CommonTools.bytesToObject(requestByteArray);
				HashMap<String, Object> actionParam = (HashMap<String, Object>)request.get("actionParam");
				String integratedProcessKey = String.valueOf(actionParam.get("approveId"));
				EAction action = EAction.valueOf(request.get("action").toString());
				
				HashMap<String, Object> parameter = new HashMap<>();
				parameter.put("processId", processItemMap.get("id"));
				if (status.equals(EProcessStatus.SUBMITED)) { //流程发起
					if (integratedProcessKey != null) {
						parameter.put("relationType", "INTEGRATED");
						parameter.put("relationKey", integratedProcessKey);
						dbService.insert(DBServiceConst.INSERT_PROCESSE_RELATION, parameter );
						
						//向openstack 下发请求
						VirtualActionImpl virtualActionImpl = (VirtualActionImpl) context.getBean("virtualAction");
						
						String regionName = (String) request.get("regionName");
						String projectId = (String) request.get("projectId");
						String loginId = (String) request.get("loginId");
						HashMap<String, Object> requestParamMap = (HashMap<String, Object>) request.get("actionParam");
						Result result = null;
						if (request.containsKey("resourceIdArray")) {
							Integer[] resourceIdArray = (Integer[])request.get("resourceIdArray");
							result = virtualActionImpl.doExcutionAction(regionName, loginId, projectId, action, requestParamMap,resourceIdArray);
						}else if (request.containsKey("resourceUuidArray")) {
							String[] resourceUuidArray = (String[])request.get("resourceUuidArray");
							result = virtualActionImpl.doExcutionAction(regionName, loginId, projectId, action, requestParamMap,resourceUuidArray);
						}else{
							result = virtualActionImpl.doExcutionAction(regionName, loginId, projectId, action, requestParamMap);
						}
						
						if(result.getResultCode().equals(EResultCode.SUCCESS)){
							List<HashMap<String,Object>> resultObj= result.getResultObj();
							HashMap<String,Object> stack= (HashMap<String, Object>) resultObj.get(0).get("stack");							
							String stackUuid = (String) stack.get("id");
							String links = JsonHelper.toJson(stack.get("links"));
							HashMap<String,Object> parameterMap = new HashMap<String,Object>();
							parameterMap.put("region", regionName);
							parameterMap.put("project_id", projectId);
							parameterMap.put("id", stackUuid);
							parameterMap.put("links", links);
							
//							dbService.insert(DBServiceConst.INSERT_OPENSTACK_STACK, parameterMap);
							
							parameterMap.put("processId", processItemMap.get("id"));
							parameterMap.put("resourceType", action);
							parameterMap.put("resourceId", stackUuid);
							dbService.insert(DBServiceConst.INSERT_PROCESSE_RESOURCE, parameterMap);
						}
//						parameter.put("logId", result.getLogId());
//						dbService.insert(DBServiceConst.INSERT_PROCESS_REQEUST_MAP, parameter);
					}
				}
			}
		} catch (Exception e) { 
			logger.error("deal service approve process listener error!", e);
		}

	}

}
