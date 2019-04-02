package com.system.started.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.system.started.action.impl.operation.OperationActionImpl;
import com.system.started.action.impl.resource.ResourceActionImpl;
import com.system.started.action.impl.virtual.VirtualActionImpl;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.process.EProcessStatus;
import com.system.started.process.EProcessType;
import com.system.started.process.engine.local.EProcessTaskStatus;
import com.system.started.process.engine.local.LocalProcessService;
import com.vlandc.oss.common.CommonTools;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

public class ServerServiceListener implements IProcessListener {

	private static Logger logger = Logger.getLogger(ServerServiceListener.class);

	@Autowired
	private DBService dbService;
	
	@Autowired  
    ApplicationContext context; 
	
	@Autowired
	private LocalProcessService localProcessService;
	
	@Override
	public void initLisenter() {

	}

	@Override
	public void dealPorcess(HashMap<String, Object> processItemMap, EProcessStatus status, String statusResult, HashMap<String, Object> paramMap) {
		try {
			String processType = (String) processItemMap.get("type");
			// 是请求审批流程，审批通过需要进行实际openstack请求下发
			if (processType.equals(EProcessType.REQUEST_APPROVE.toString())
					|| processType.equals(EProcessType.OPERATE_APPROVE.toString())
					|| processType.equals(EProcessType.LIFECYCLE_APPROVE.toString())) {
				byte[] requestByteArray = (byte[]) processItemMap.get("body");
				HashMap<String, Object> request = (HashMap<String, Object>) CommonTools.bytesToObject(requestByteArray);
				EAction action = EAction.valueOf(request.get("action").toString());

				Integer resourceId = null;
				if (request.containsKey("resourceIdArray")) {
					Integer[] resourceIdArray = (Integer[])request.get("resourceIdArray");
					resourceId = resourceIdArray[0];
				}else if (request.containsKey("resourceUuidArray")) {
					String regionName = (String) request.get("regionName");
					String[] resourceUuidArray = (String[])request.get("resourceUuidArray");
					resourceId = CommonTools.genterateRnId(resourceUuidArray[0], regionName);
				}

				HashMap<String, Object> parameter = new HashMap<>();
				parameter.put("processId", processItemMap.get("id"));
				if (status.equals(EProcessStatus.SUBMITED)) { //流程发起
					if (resourceId != null) {
						parameter.put("resourceType", action);
						parameter.put("resourceId", resourceId);
						dbService.insert(DBServiceConst.INSERT_PROCESSE_RESOURCE, parameter );
					}
				}else if (status.equals(EProcessStatus.SUCCESS_END)) {// 流程审批通过
					if (resourceId != null) {
						dbService.delete(DBServiceConst.DELETE_PROCESSE_RESOURCE, parameter );
					}
					if (action.name().startsWith("VIRTUAL_")) {
						VirtualActionImpl virtualActionImpl = (VirtualActionImpl) context.getBean("virtualAction");

						String regionName = (String) request.get("regionName");
						String loginId = (String) request.get("loginId");
						String projectId = (String) request.get("projectId");
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

						parameter.put("logId", result.getLogId());
						dbService.insert(DBServiceConst.INSERT_PROCESS_REQEUST_MAP, parameter);
					}else if(action.name().startsWith("OPERATE_")){
						OperationActionImpl operationActionImpl = (OperationActionImpl) context.getBean("operationAction");

						String loginId = (String) request.get("loginId");
						HashMap<String, Object> requestParamMap = (HashMap<String, Object>) request.get("actionParam");
						Result result = null;

						HashMap<String, Object> processItemParamMap = JsonHelper.fromJson(HashMap.class, (String)processItemMap.get("param"));
						String subType = (String)processItemParamMap.get("TASK_TYPE");
						String serviceListIndex = subType.substring(0,subType.indexOf(":"));

						List<Object> serviceList = (List<Object>)requestParamMap.get("serviceList");
						List<Object> realServiceList = new ArrayList<>();
						realServiceList.add(serviceList.get(Integer.parseInt(serviceListIndex)));
						requestParamMap.put("serviceList", realServiceList);
						if(action.equals(EAction.OPERATE_CREATE_TIMED_TASK)){// 如果是创建定时运维任务，则直接在web端执行，不下发到Server端执行
							try {
								HashMap<String,Object> timingTask = (HashMap<String, Object>) requestParamMap.get("timingTask");
								dbService.insert(DBServiceConst.INSERT_OPERATION_TIMED_TASK,timingTask);
//								this.taskManageService.addTasks2Quartz(timingTask);
							} catch (Exception e) {
								logger.error("create operate timed task error !", e);
							}
						}else{
							if (request.containsKey("resourceIdArray")) {
								Integer[] resourceIdArray = (Integer[])request.get("resourceIdArray");
								result = operationActionImpl.doExcutionAction(loginId, action, requestParamMap,resourceIdArray);
							}else if (request.containsKey("resourceUuidArray")) {
								String[] resourceUuidArray = (String[])request.get("resourceUuidArray");
								result = operationActionImpl.doExcutionAction(loginId, action, requestParamMap,resourceUuidArray);
							}else{
								String regionName = (String) request.get("regionName");
								result = operationActionImpl.doExcutionAction(loginId, action, regionName,requestParamMap);
							}
							logger.debug("operate action exec result : "+ JsonHelper.toJson(result));
						}
					}else if(action.name().startsWith("RESOURCE_")){
						ResourceActionImpl resourceActionImpl = (ResourceActionImpl) context.getBean("resourceAction");

						String loginId = (String) request.get("loginId");
						String projectId = (String) request.get("projectId");
						HashMap<String, Object> requestParamMap = (HashMap<String, Object>) request.get("actionParam");
						Result result = resourceActionImpl.doExcutionAction(loginId, projectId, action,requestParamMap);

						logger.debug("resource action exec result :"+ JsonHelper.toJson(result));
					}

					logger.debug("deal service approve process listener success!");
				}else if (status.equals(EProcessStatus.CLOSE)) {
					if (resourceId != null) {
						dbService.delete(DBServiceConst.DELETE_PROCESSE_RESOURCE, parameter );
					}
				}
				//自动审批功能  若下一个审批人员为当前创建的用户 则自动审批

				String nextTask = "";
				HashMap params = new HashMap();
				params.put("refInstance",processItemMap.get("identity").toString());
				List<HashMap<String,Object>> nextTaskMap = dbService.directSelect(DBServiceConst.SELECT_PROCESS_NEXT_TASKS, params);
				if(null != nextTaskMap && nextTaskMap.size() == 1){
					nextTask = nextTaskMap.get(0).get("id").toString();
				}
				if(StringUtils.isNotBlank(nextTask) && !nextTask.equals("-1")){
					String createUser = (String) processItemMap.get("createUser");
					parameter.put("identity",processItemMap.get("identity").toString());
					List<HashMap<String, Object>> refUserList = dbService.directSelect(DBServiceConst.SELECT_CURRENT_GTASKS_REFUSER, parameter);
					for (int i = 0; i < refUserList.size(); i++) {
						HashMap<String, Object> refUserItem = refUserList.get(i);
						String checkUser = (String) refUserItem.get("checkUser");
						if(checkUser.equals(createUser)){
							//获取下一个流程
							HashMap<String,Object> checkTaskMap = new HashMap<String,Object>();
							checkTaskMap.put("checkContext","系统默认");
							checkTaskMap.put("checkStatus", EProcessTaskStatus.PASS);
							checkTaskMap.put("checkUser", checkUser);
							checkTaskMap.put("id",nextTask); //下一个流程的id nextTask
							checkTaskMap.put("refInstance",processItemMap.get("identity").toString());
							localProcessService.doCheckTask(checkTaskMap);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("deal service approve process listener error!", e);
		}

	}

}
