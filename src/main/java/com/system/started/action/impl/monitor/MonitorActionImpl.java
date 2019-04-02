package com.system.started.action.impl.monitor;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.impl.IMonitorAction;
import com.vlandc.oss.kernel.monitor.IMonitorService;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.Result;

@Component("monitorActionImpl")
public class MonitorActionImpl implements IMonitorAction {

	@Autowired
	private IMonitorService kernelMonitorService;

	@Override
	public Result doExcutionAction(String loginId, EAction action, String regionName, HashMap<String, Object> actionParam) {
		return kernelMonitorService.doExcutionAction(action, regionName, actionParam);
	}

	@Override
	public Result doExcutionAction(String loginId, EAction action, HashMap<String, Object> actionParam, Integer... resourceIdArray) {
		return kernelMonitorService.doExcutionAction(action, actionParam, resourceIdArray);
	}
}
