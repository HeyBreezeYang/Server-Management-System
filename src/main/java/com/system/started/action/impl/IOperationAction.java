package com.system.started.action.impl;

import java.util.HashMap;

import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

public interface IOperationAction{

	public Result doExcutionAction(String loginId, EAction action,String regionName, HashMap<String, Object> actionParam);

	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray);
	
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, String... minionArray);

}
