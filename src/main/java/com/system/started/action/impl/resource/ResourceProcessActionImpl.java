package com.system.started.action.impl.resource;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.system.started.action.impl.AbstractProcessAction;
import com.system.started.action.impl.IResourceAction;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

@Component("resourceProcessActionImpl")
public class ResourceProcessActionImpl extends AbstractProcessAction implements IResourceAction {

	@Override
	public Result doExcutionAction(String loginId,String projectId, EAction action, HashMap<String, Object> actionParam) {
		HashMap<String, Object> requestParam = new HashMap<>();
		requestParam.put("loginId", loginId);
		requestParam.put("projectId", projectId);
		requestParam.put("action", action);
		requestParam.put("actionParam", actionParam);
		return sendRequest(loginId, requestParam);
	}
}
