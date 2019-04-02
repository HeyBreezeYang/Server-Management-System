package com.system.started.action.wrapper;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.impl.IOperationAction;
import com.system.started.action.manage.IWebActionSelector;
import com.system.started.action.manage.IWebActionSelectorJudge;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class OperationActionWrapper implements IOperationAction {
	private final static Logger logger = LoggerFactory.getLogger(OperationActionWrapper.class);

	@Autowired
	private DBService dbService;
	@Autowired
	private IWebActionSelector<IOperationAction> operationWebActionSelector;

	@Override
	public Result doExcutionAction(String loginId, EAction action, String regionName ,HashMap<String, Object> actionParam) {
		try {
			String serviceAction = null;
			String taskType = (String)actionParam.get("taskType");
			Object serviceParamObject = actionParam.get("serviceParam");
			if(serviceParamObject != null){
				serviceAction= (String)((HashMap<String,Object>)serviceParamObject).get("action");
			}
			List<IOperationAction> serviceList = operationWebActionSelector.getTargetAction(new operationServiceActionJudge(loginId, action,serviceAction,taskType));
			if (serviceList != null && serviceList.size() > 0) {
				IOperationAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(loginId, action, regionName, actionParam);
			} else {
				logger.error("can not find the target action from the action factory!");
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到action实现类！");
				return result;
			}
		} catch (Exception e) {
			logger.error("can not find the target action from the action factory!", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("执行action错误 (" + e.getMessage() + ")！");
			return result;
		}
	}

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray) {
		try {
			String serviceAction = null;
			String taskType = (String)actionParam.get("taskType");
			Object serviceParamObject = actionParam.get("serviceParam");
			if(serviceParamObject != null){
				serviceAction= (String)((HashMap<String,Object>)serviceParamObject).get("action");
			}
			List<IOperationAction> serviceList = operationWebActionSelector.getTargetAction(new operationServiceActionJudge(loginId, action,serviceAction,taskType));
			if (serviceList != null && serviceList.size() > 0) {
				IOperationAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(loginId, action, actionParam,resourceIdArray);
			} else {
				logger.error("can not find the target action from the action factory!");
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到action实现类！");
				return result;
			}
		} catch (Exception e) {
			logger.error("can not find the target action from the action factory!", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("执行action错误 (" + e.getMessage() + ")！");
			return result;
		}
	}

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, String... minionArray) {
		try {
			String serviceAction = null;
			String taskType = (String)actionParam.get("taskType");
			Object serviceParamObject = actionParam.get("serviceParam");
			if(serviceParamObject != null){
				serviceAction= (String)((HashMap<String,Object>)serviceParamObject).get("action");
			}
			List<IOperationAction> serviceList = operationWebActionSelector.getTargetAction(new operationServiceActionJudge(loginId, action,serviceAction,taskType));
			if (serviceList != null && serviceList.size() > 0) {
				IOperationAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(loginId, action, actionParam,minionArray);
			} else {
				logger.error("can not find the target action from the action factory!");
				Result result = new Result();
				result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
				result.setResultMsg("不能找到action实现类！");
				return result;
			}
		} catch (Exception e) {
			logger.error("can not find the target action from the action factory!", e);
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("执行action错误 (" + e.getMessage() + ")！");
			return result;
		}
	}

	class operationServiceActionJudge implements IWebActionSelectorJudge<IOperationAction> {

		private String loginId;
		private EAction action;
		private String serviceAction;
		private String taskType;

		public operationServiceActionJudge(String loginId, EAction action,String serviceAction, String taskType) {
			this.loginId = loginId;
			this.action = action;
			this.serviceAction = serviceAction;
			this.taskType = taskType;
		}

		@Override
		public boolean compare(String actionName, IOperationAction actionInstance) {
			HashMap<String, Object> roleParamMap = new HashMap<>();
			roleParamMap.put("loginId", loginId);
			Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
			if (countSize >= 1000 || !filterAction(action,serviceAction) || (taskType != null && taskType.equals("TIMED_TASK"))) { // 如果是管理员角色或者不需要审批的功能，则直接下发openstack命令
				if (actionName.equals("action")) {
					return true;
				}else{
					return false;
				}
			} else {
				if (actionName.equals("process")) {
					return true;
				}else
					return false;
			}
		}
		
		private boolean filterAction(EAction action,String serviceAction){
			if (action.equals(EAction.VIRTUAL_LIST_STACK_ITEM) 
					|| action.equals(EAction.VIRTUAL_LIST_STACKS)
					|| action.equals(EAction.VIRTUAL_LIST_STACK_ITEM_TEMPLATE)
					|| action.equals(EAction.VIRTUAL_LIST_STACK_ITEM_RESOURCES)
//					||  request instanceof UpdateNeutronNetworkRequest 
					|| action.equals(EAction.VIRTUAL_UPDATE_NETWORK_SUBNET)
//					|| request instanceof UpdateNeutronPortRequest 
//					|| request instanceof UpdateImagePropertiesRequest 
					|| action.equals(EAction.VIRTUAL_ATTACH_SERVER_VOLUME)
					|| action.equals(EAction.VIRTUAL_DETACH_SERVER_VOLUME)
					|| action.equals(EAction.VIRTUAL_UPDATE_SERVER) 
					|| action.equals(EAction.VIRTUAL_LIST_ZONES) 
					|| action.equals(EAction.OPERATE_LIST_HOSTS) 
					|| action.equals(EAction.OPERATE_LIST_MINIONS) 
					|| action.equals(EAction.OPERATE_LIST_SERVICE) 
//					|| request instanceof UpdateVolumeRequest 
//					|| request instanceof CreateKeypairRequest 
//					|| request instanceof DeleteKeypairRequest 
//					|| request instanceof ListKeypairsRequest 
//					|| request instanceof ListKeypairDetailRequest 
//					|| request instanceof GetServerConsoleOutputRequest 
//					|| request instanceof GetServerConsoleUrlRequest
					|| (serviceAction != null && (serviceAction.equals("status") || serviceAction.equals("list")))
					) {
				return false;
			}
			return true;
		}
	}
}
