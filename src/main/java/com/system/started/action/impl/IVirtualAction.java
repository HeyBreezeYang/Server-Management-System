package com.system.started.action.impl;

import java.util.HashMap;

import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

public interface IVirtualAction{
	
	public Result doExcutionAction(String loginId,String projectId, EAction action, HashMap<String, Object> actionParam);

	public Result doExcutionAction(String regionName, String loginId,String projectId, EAction action, HashMap<String, Object> actionParam);

	public Result doExcutionAction(String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray);
	
	public Result doExcutionAction(String regionName, String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray);

	public Result doExcutionAction(String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, String... resourceUuidArray);
	
	public Result doExcutionAction(String regionName, String loginId,String projectId, EAction action, HashMap<String, Object> actionParam, String... resourceUuidArray);

}
