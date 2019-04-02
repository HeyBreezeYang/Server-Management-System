package com.system.started.action.wrapper;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.impl.IResourceAction;
import com.system.started.action.manage.IWebActionSelector;
import com.system.started.action.manage.IWebActionSelectorJudge;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class ResourceActionWrapper implements IResourceAction {
	private final static Logger logger = LoggerFactory.getLogger(ResourceActionWrapper.class);
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private IWebActionSelector<IResourceAction> resourceWebActionSelector;

	@Override
	public Result doExcutionAction(String loginId, String projectId, EAction action, HashMap<String, Object> actionParam) {
		try {
			List<IResourceAction> driverList= resourceWebActionSelector.getTargetAction(new resourceActionJudge(loginId,action));
			if(driverList !=null && driverList.size()>0){
				IResourceAction driverItem = driverList.get(0);
				return driverItem.doExcutionAction(loginId, projectId, action, actionParam);
			}else{
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
	
	class resourceActionJudge implements IWebActionSelectorJudge<IResourceAction>{

		private String loginId;
		private EAction action;

		public resourceActionJudge(String loginId, EAction action) {
			this.loginId = loginId;
			this.action = action;
		}
		
		@Override
		public boolean compare(String actionName, IResourceAction actionInstance) {
			HashMap<String, Object> roleParamMap = new HashMap<>();
			roleParamMap.put("loginId", loginId);
			Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
			if (countSize >= 1000 || !filterAction(action) ) { // 如果是管理员角色或者不需要审批的功能或者当前用户已分配可用区域，则直接下发openstack命令
				if (actionName.equals("action")) {//|| filterAvailabilityZone(loginId)
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
			if (!action.equals(EAction.RESOURCE_UPDATE_SERVER_EXPIREDAY)) {
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
