package com.system.started.action.impl.operation;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.impl.IOperationAction;
import com.vlandc.oss.kernel.operate.IOperateService;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

@Component("operationActionImpl")
public class OperationActionImpl implements IOperationAction {

	@Autowired
	private IOperateService kernelOperationService;

	@Override
	public Result doExcutionAction(String loginId, EAction action, String regionName, HashMap<String, Object> actionParam) {
		return kernelOperationService.doExcutionAction(action, regionName, actionParam);
	}

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray) {
		return kernelOperationService.doExcutionAction(action, actionParam, resourceIdArray);
	}

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, String... minionArray) {
		return kernelOperationService.doExcutionAction(action, actionParam, minionArray);
	}
}
