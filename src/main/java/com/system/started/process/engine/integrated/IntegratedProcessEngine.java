package com.system.started.process.engine.integrated;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.system.started.process.EProcessEngineType;
import com.system.started.process.EProcessStatus;
import com.system.started.process.EProcessType;
import com.system.started.process.ProcessManager;
import com.system.started.process.engine.IProcessEngine;
import com.system.started.process.engine.local.EProcessTaskStatus;

public class IntegratedProcessEngine implements IProcessEngine {

	@Autowired
	private ProcessManager processManager;
	
	@Override
	public EProcessEngineType getEngineType() {
		return EProcessEngineType.INTEGRATED;
	}

	@Override
	public String sendProcess(String name, EProcessType type, Object body, HashMap<String, Object> paramMap, String createUser) throws Exception {
		return String.valueOf(paramMap.get("approveId"));
	}

	@Override
	public void reSubmitTask(Integer instanceId, Object body, HashMap<String, Object> paramMap, String createUser) throws Exception {
		// send to integrated process engine to resubmit the process
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("refInstance", instanceId);
		parameter.put("checkStatus", EProcessTaskStatus.PASS);
		parameter.put("checkUser", createUser);
		this.dealProcess(parameter);
	}

	@Override
	public void closeProcess(Integer instanceId, String createUser) throws Exception {
		// send to integrated process engine to close the process
		HashMap<String, Object> checkTaskMap = new HashMap<>();
		checkTaskMap.put("processInstanceId", instanceId);
		checkTaskMap.put("checkUser", createUser);
		this.dealProcess(checkTaskMap);
	}

	public void dealProcess(HashMap<String, Object> checkTaskMap) {
		// send to integrated process engine to deal the process
		Integer refInstance = (Integer) checkTaskMap.get("refInstance");
		Integer nextTask = (Integer) checkTaskMap.get("nextTask");
		Object checkStatus = checkTaskMap.get("checkStatus");

		String checkTaskType = (String) checkTaskMap.get("currentTaskType");

		EProcessStatus status = null;
		if (nextTask == -1) {
			if (checkTaskType.equals("CLOSE")) {
				status = EProcessStatus.CLOSE;
			} else {
				if (checkStatus.toString().equals(EProcessTaskStatus.PASS.toString())) {
					status = EProcessStatus.SUCCESS_END;
				} else {
					status = EProcessStatus.SEND_BACK;
				}
			}
		} else {
			if (checkTaskType.equals("SUBMIT")) {
				status = EProcessStatus.SUBMITED;
			} else if (checkTaskType.equals("ACCEPT")) {
				status = EProcessStatus.PROCESSING;
			}else{
				status = EProcessStatus.PROCESSING;
			}
		}

		String statusResult = (String) checkTaskMap.get("checkContext");

		processManager.dealPorcess(EProcessEngineType.INTEGRATED, String.valueOf(refInstance), status, statusResult, null);
	}
}
