package com.system.started.action.wrapper;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.impl.IVirtualAction;
import com.system.started.action.manage.IWebActionSelector;
import com.system.started.action.manage.IWebActionSelectorJudge;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class VirtualActionWrapper implements IVirtualAction {
	private final static Logger logger = LoggerFactory.getLogger(VirtualActionWrapper.class);

	@Autowired
	private DBService dbService;
	@Autowired
	private IWebActionSelector<IVirtualAction> virtualWebActionSelector;

	@Override
	public Result doExcutionAction(String loginId,String projectId, EAction action, HashMap<String, Object> actionParam) {
		try {
			List<IVirtualAction> serviceList = virtualWebActionSelector.getTargetAction(new virtualActionJudge(loginId, action));
			if (serviceList != null && serviceList.size() > 0) {
				IVirtualAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(loginId,projectId, action, actionParam);
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
	public Result doExcutionAction(String regionName, String loginId,String projectId, EAction action, HashMap<String, Object> actionParam) {
		try {
			List<IVirtualAction> serviceList = virtualWebActionSelector.getTargetAction(new virtualActionJudge(loginId, action));
			if (serviceList != null && serviceList.size() > 0) {
				IVirtualAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(regionName, loginId,projectId, action, actionParam);
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
	public Result doExcutionAction(String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray) {
		try {
			List<IVirtualAction> serviceList = virtualWebActionSelector.getTargetAction(new virtualActionJudge(loginId, action));
			if (serviceList != null && serviceList.size() > 0) {
				IVirtualAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(loginId,projectId, action, actionParam, resourceIdArray);
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
	public Result doExcutionAction(String regionName, String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray) {
		try {
			List<IVirtualAction> serviceList = virtualWebActionSelector.getTargetAction(new virtualActionJudge(loginId, action));
			if (serviceList != null && serviceList.size() > 0) {
				IVirtualAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(regionName, loginId,projectId, action, actionParam, resourceIdArray);
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
	public Result doExcutionAction(String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, String... resourceUuidArray) {
		try {
			List<IVirtualAction> serviceList = virtualWebActionSelector.getTargetAction(new virtualActionJudge(loginId, action));
			if (serviceList != null && serviceList.size() > 0) {
				IVirtualAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(loginId,projectId, action, actionParam, resourceUuidArray);
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
	public Result doExcutionAction(String regionName, String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, String... resourceUuidArray) {
		try {
			List<IVirtualAction> serviceList = virtualWebActionSelector.getTargetAction(new virtualActionJudge(loginId, action));
			if (serviceList != null && serviceList.size() > 0) {
				IVirtualAction serviceItem = serviceList.get(0);
				return serviceItem.doExcutionAction(regionName, loginId,projectId, action, actionParam, resourceUuidArray);
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

	class virtualActionJudge implements IWebActionSelectorJudge<IVirtualAction> {

		private String loginId;
		private EAction action;

		public virtualActionJudge(String loginId, EAction action) {
			this.loginId = loginId;
			this.action = action;
		}

		@Override
		public boolean compare(String actionName, IVirtualAction actionInstance) {
			HashMap<String, Object> roleParamMap = new HashMap<>();
			roleParamMap.put("loginId", loginId);
			Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
			if (countSize >= 1000 || !filterAction(action) || filterAvailabilityZone(loginId)) { // 如果是管理员角色或者不需要审批的功能或者当前用户已分配可用区域，则直接下发openstack命令
				if (actionName.equals("action")) {//  || filterAvailabilityZone(loginId)
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
		
		private boolean filterAction(EAction action){
			if (action.equals(EAction.VIRTUAL_LIST_STACK_ITEM) 
					|| action.equals(EAction.VIRTUAL_LIST_STACKS)
					|| action.equals(EAction.VIRTUAL_LIST_STACK_ITEM_TEMPLATE)
					|| action.equals(EAction.VIRTUAL_LIST_STACK_ITEM_RESOURCES)
//					||  request instanceof UpdateNeutronNetworkRequest 
					|| action.equals(EAction.VIRTUAL_UPDATE_NETWORK_SUBNET)
//					|| request instanceof UpdateNeutronPortRequest 
//					|| request instanceof UpdateImagePropertiesRequest 
					|| action.equals(EAction.VIRTUAL_UPDATE_VOLUME) 
					|| action.equals(EAction.VIRTUAL_ATTACH_SERVER_VOLUME)
					|| action.equals(EAction.VIRTUAL_DETACH_SERVER_VOLUME)
					|| action.equals(EAction.VIRTUAL_UPDATE_SERVER) 
					|| action.equals(EAction.VIRTUAL_CREATE_AGGREGATE)
					|| action.equals(EAction.VIRTUAL_LIST_ZONES) 
					|| action.equals(EAction.OPERATE_LIST_HOSTS) 
					|| action.equals(EAction.OPERATE_LIST_MINIONS) 
					|| action.equals(EAction.OPERATE_LIST_SERVICE) 
//					|| action.equals(EAction.VIRTUAL_CREATE_STACK) 
					|| action.equals(EAction.VIRTUAL_DELETE_STACK) 
					|| action.equals(EAction.VIRTUAL_CREATE_NETWORK) 
					|| action.equals(EAction.VIRTUAL_UPDATE_FALVOR) 
					|| action.equals(EAction.VIRTUAL_CREATE_NETWORK_SUBNETS) 
					|| action.equals(EAction.VIRTUAL_DELETE_NETWORK_PORT) 
					|| action.equals(EAction.VIRTUAL_EXTENSION_LIST_STORAGE_CONTROLLER)
//					|| action.equals(EAction.VIRTUAL_UPDATE_IMAGE_PROPERTIES)
//					|| request instanceof UpdateVolumeRequest 
//					|| request instanceof CreateKeypairRequest 
//					|| request instanceof DeleteKeypairRequest 
//					|| request instanceof ListKeypairsRequest 
//					|| request instanceof ListKeypairDetailRequest 
//					|| request instanceof GetServerConsoleOutputRequest 
//					|| request instanceof GetServerConsoleUrlRequest
					) {
				return false;
			}
			return true;
		}

		private boolean filterAvailabilityZone(String loginId){
			HashMap<String,Object>paramMap = new HashMap<String,Object>();
			StringBuffer loginBuffer = new StringBuffer();
			loginBuffer.append("'");
			loginBuffer.append(loginId);
			loginBuffer.append("'");
			paramMap.put("loginId", loginBuffer);
			
			HashMap<String,Object>resultMap = dbService.select(DBServiceConst.SELECT_SYSTEM_USER_AVAILABILITY_ZONES, paramMap);
			
			@SuppressWarnings("unchecked")
			List<HashMap<String,Object>>records= (List<HashMap<String, Object>>) resultMap.get("record");
			if(records.size()>0){
				return true;
			}
			return false;
		}
	}
}
