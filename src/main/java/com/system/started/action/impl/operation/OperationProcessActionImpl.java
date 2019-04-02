package com.system.started.action.impl.operation;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.system.started.action.impl.AbstractProcessAction;
import com.system.started.action.impl.IOperationAction;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

@Component("operationProcessActionImpl")
public class OperationProcessActionImpl extends AbstractProcessAction implements IOperationAction {

	@Override
	public Result doExcutionAction(String loginId, EAction action, String regionName, HashMap<String, Object> actionParam) {
		HashMap<String, Object> requestParam = new HashMap<>();
		requestParam.put("regionName", regionName);
		requestParam.put("loginId", loginId);
		requestParam.put("action", action);
		requestParam.put("actionParam", actionParam);
		return sendRequest(loginId, requestParam);
	}

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray) {
		HashMap<String, Object> requestParam = new HashMap<>();
		requestParam.put("loginId", loginId);
		requestParam.put("action", action);
		requestParam.put("actionParam", actionParam);
		requestParam.put("resourceIdArray", resourceIdArray);
		return sendRequest(loginId, requestParam);
	}

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, String... resourceUuidArray) {
		HashMap<String, Object> requestParam = new HashMap<>();
		requestParam.put("loginId", loginId);
		requestParam.put("action", action);
		requestParam.put("actionParam", actionParam);
		requestParam.put("resourceUuidArray", resourceUuidArray);
		return sendRequest(loginId, requestParam);
	}
}
