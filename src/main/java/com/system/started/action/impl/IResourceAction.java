package com.system.started.action.impl;

import java.util.HashMap;

import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

public interface IResourceAction {

	public Result doExcutionAction(String loginId, String projectId, EAction action, HashMap<String,Object> actionParam);
}
