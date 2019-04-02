package com.system.started.action.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.system.started.notification.NotificationManager;
import com.system.started.process.EProcessType;
import com.system.started.process.ProcessManager;
import com.system.started.process.filter.ProcessFilterChain;
import com.vlandc.oss.common.CommonTools;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.notification.ENotificationType;
import com.vlandc.oss.model.notification.NotificationObject;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.ProcessResult;
import com.vlandc.oss.model.result.Result;

public abstract class AbstractProcessAction {
	private final static Logger logger = LoggerFactory.getLogger(AbstractProcessAction.class);

	private ProcessFilterChain processFilterChain;
	private NotificationManager notificationManager;
	private ProcessManager processManager;

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void setProcessFilterChain(ProcessFilterChain processFilterChain) {
		this.processFilterChain = processFilterChain;
	}

	protected Result sendRequest(String loginId, HashMap<String, Object> requestParam) {
		String errorResponse = processFilterChain.doFilter(loginId, requestParam);
		 if (errorResponse != null) {
			 Result tempResult = new Result();
			 tempResult.setResultCode(EResultCode.CREATE_PROCESS_FAIL);
			 tempResult.setResultMsg(errorResponse);
			 return tempResult;
		 }
		 
		return doSendProcess(loginId, requestParam);
	}

	protected Result doSendProcess(String loginId, HashMap<String, Object> requestParam) {
		ProcessResult result = new ProcessResult(); 
		try {
			byte[] byteArray = CommonTools.objectToBytes(requestParam);
			HashMap<String, Object> paramMap = new HashMap<>();
			StringBuffer loginIdBuffer = new StringBuffer();
			loginIdBuffer.append("'");
			loginIdBuffer.append(requestParam.get("loginId"));
			loginIdBuffer.append("'");
			paramMap.put("loginId", loginIdBuffer.toString());
			
			EAction action = (EAction) requestParam.get("action");
			Integer processId = -1;
			if (action.name().startsWith("OPERATE_")) {
				HashMap<String, Object> actionParam = (HashMap<String, Object>) requestParam.get("actionParam");
				List<HashMap<String, Object>> serviceList = (List<HashMap<String, Object>>) actionParam.get("serviceList");
				for (int i = 0; i < serviceList.size(); i++) {
					HashMap<String, Object> serviceItem = serviceList.get(i);
					int templateId = (int) serviceItem.get("templateId");
					paramMap.put("TASK_TYPE", i + ":" + templateId); //加入index ,避免同一个任务不同参数执行时不能正确判断模板位置
					processId = processManager.createProcess("运维审批", EProcessType.OPERATE_APPROVE, byteArray, paramMap, loginId);
				}
			}
			// 为对接第三方一体化平台流程
//			else if(action.name().equals("VIRTUAL_CREATE_STACK")){
//				paramMap.put("TASK_TYPE", requestParam.get("action"));
//				
//				HashMap<String, Object> actionParam = (HashMap<String, Object>) requestParam.get("actionParam");
//				paramMap.put("approveId", actionParam.get("approveId"));
//				
//				processId = processManager.createProcess("自动化部署审批", EProcessType.DEPLOY_APPROVE, byteArray, paramMap, loginId); 
//			}
			else if(action.name().endsWith("_LIFECYCLE_SERVER")){
				paramMap.put("TASK_TYPE", requestParam.get("action"));
				processId = processManager.createProcess("生命周期审批", EProcessType.LIFECYCLE_APPROVE, byteArray, paramMap, loginId);
			}
			else{
				paramMap.put("TASK_TYPE", requestParam.get("action"));
				processId = processManager.createProcess("操作审批", EProcessType.REQUEST_APPROVE, byteArray, paramMap, loginId);
			}
			

			// 发送我的申请增加通知
			NotificationObject notification = new NotificationObject();
			notification.setNotificationType(ENotificationType.MYAPPLY_COUNT_ADD);
			notification.setRefUser(loginId);
			notificationManager.addNotificationSendQueue(notification);

			NotificationObject listAddNotification = new NotificationObject();
			listAddNotification.setNotificationType(ENotificationType.MYAPPLY_LIST_ADD);
			listAddNotification.setRefUser(loginId);
			HashMap<String, Object> addParamMap = new HashMap<>();
			addParamMap.put("id", processId);
			listAddNotification.setParameter(addParamMap);
			notificationManager.addNotificationSendQueue(listAddNotification);
		} catch (IOException e) {
			logger.error("create openstack request process error!", e);
			result.setResultCode(EResultCode.CREATE_PROCESS_FAIL);
			result.setResultMsg("发起流程失败！");
		}

		result.setProcessStatus(1);
		result.setResultCode(EResultCode.PROCESS_SUCCESS);
		result.setResultMsg("发起流程成功！");

		return result;
	}
}
