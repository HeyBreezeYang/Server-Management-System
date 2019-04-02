package com.system.started.action.wrapper;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.impl.IMonitorAction;
import com.system.started.action.manage.IWebActionSelector;
import com.system.started.action.manage.IWebActionSelectorJudge;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class MonitorActionWrapper implements IMonitorAction {
	private final static Logger logger = LoggerFactory.getLogger(MonitorActionWrapper.class);
	
	@Autowired
	private IWebActionSelector<IMonitorAction> monitorWebActionSelector;
	
	@Override
	public Result doExcutionAction(String loginId, EAction action,String regionName, HashMap<String, Object> actionParam) {
		try {
			List<IMonitorAction> driverList= monitorWebActionSelector.getTargetAction(new monitorActionJudge());
			if(driverList !=null && driverList.size()>0){
				IMonitorAction driverItem = driverList.get(0);
				return driverItem.doExcutionAction(loginId, action, regionName, actionParam);
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

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam,Integer... resourceIdArray) {
		try {
			List<IMonitorAction>driverList= monitorWebActionSelector.getTargetAction(new monitorActionJudge());
			if(driverList !=null && driverList.size()>0){
				IMonitorAction driverItem = driverList.get(0);
				return driverItem.doExcutionAction(loginId, action, actionParam, resourceIdArray);
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
	
	class monitorActionJudge implements IWebActionSelectorJudge<IMonitorAction>{

		@Override
		public boolean compare(String actionName, IMonitorAction actionInstance) {
			return true;
		}
	}
}
