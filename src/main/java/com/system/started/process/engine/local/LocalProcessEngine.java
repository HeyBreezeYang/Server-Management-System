package com.system.started.process.engine.local;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.process.EProcessEngineType;
import com.system.started.process.EProcessStatus;
import com.system.started.process.EProcessType;
import com.system.started.process.ProcessManager;
import com.system.started.process.engine.IProcessEngine;

@Component
public class LocalProcessEngine implements IProcessEngine {

	@Autowired
	private LocalProcessService localProcessService;

	@Autowired
	private ProcessManager processManager;

	@Override
	public EProcessEngineType getEngineType() {
		return EProcessEngineType.LOCAL;
	}

	@Override
	public String sendProcess(String name, EProcessType type, Object body, HashMap<String, Object> paramMap, String createUser) throws Exception {
		Integer instanceId = localProcessService.createProcess(name, type, body, paramMap, createUser);

		return String.valueOf(instanceId);
	}

	@Override
	public void reSubmitTask(Integer instanceId, Object body, HashMap<String, Object> paramMap, String createUser) throws Exception {
		localProcessService.reSubmitTask(instanceId, body, paramMap, createUser);
	}

	@Override
	public void closeProcess(Integer instanceId, String createUser) throws Exception {
		localProcessService.closeProcess(instanceId, createUser);
	}

	public void dealProcess(HashMap<String, Object> checkTaskMap) {
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

		processManager.dealPorcess(EProcessEngineType.LOCAL, String.valueOf(refInstance), status, statusResult, null);
	}
}
