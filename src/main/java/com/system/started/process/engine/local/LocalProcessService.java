package com.system.started.process.engine.local;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.dao.ProcessInstanceDao;
import com.system.started.process.EProcessType;

@Component
public class LocalProcessService {
	private final static Logger logger = LoggerFactory.getLogger(LocalProcessService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private ProcessInstanceDao processInstanceDao;

	@Autowired
	private LocalProcessEngine localProcessEngine;

	public Integer createProcess(String name, EProcessType type, Object body, HashMap<String, Object> paramMap, String createUser) throws Exception {

		HashMap<String, Object> parameter = new HashMap<>(1);

		if (type.equals(EProcessType.WORK_ORDER_CHECK)) {
			parameter.put("subType", paramMap.get("RESOURCE_TYPE"));
		} else if (type.equals(EProcessType.OPERATE_APPROVE)) {
			String taskType = (String)paramMap.get("TASK_TYPE");
			parameter.put("subType", taskType.substring(taskType.indexOf(":") + 1));
		} else {
			parameter.put("subType", paramMap.get("TASK_TYPE"));
		}
		parameter.put("createUser", createUser);
		parameter.put("type", type);
		Integer moduleId = (Integer) dbService.selectOne(DBServiceConst.SELECT_PROCESS_MODULE_ID_FILTER, parameter);

		if (moduleId != null) {
			parameter.put("moduleId", moduleId);
		} else {
			logger.error("can not find one process module by the type (" + type + ") to send!");
			throw new Exception("can not find the process module by the type! the type is (" + type + ")");
		}

//		parameter.put("type", type);		
		List<HashMap<String, Object>> moduleList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_MODULES, parameter);

		if (moduleList != null && !moduleList.isEmpty()) {
//			Integer moduleId = (Integer) moduleList.get(0).get("id");
			logger.debug("begin to create process by the type (" + type + "),ref the module is (" + moduleId + ")");

			Date createTs = (Date) moduleList.get(0).get("createTs");
			parameter.put("baseModule", moduleId);
			parameter.put("createTs", createTs);
			Object moduleInstanceIdObject = (Object) dbService.selectOne(DBServiceConst.SELECT_PROCESS_MODULE_INSTANCE_ID, parameter);

			Integer moduleInstanceId = -1;
			if (moduleInstanceIdObject != null) {
				logger.debug("process module instance is exist,begin to create process instance!");
				moduleInstanceId = (Integer) moduleInstanceIdObject;
			} else {
				logger.debug("process module instance is not exist,the first step is create process module instance!");
				HashMap<String, Object> processModuleInstanceParamMap = new HashMap<>();
				processModuleInstanceParamMap.put("baseModule", moduleId);
				processInstanceDao.createProcessModuleInstance(processModuleInstanceParamMap);
				moduleInstanceId = (Integer) processModuleInstanceParamMap.get("moduleInstanceId");
			}

			HashMap<String, Object> processInstanceParamMap = new HashMap<>();
			processInstanceParamMap.put("name", name);
			processInstanceParamMap.put("moduleInstanceId", moduleInstanceId);
			processInstanceParamMap.put("body", body);
			processInstanceParamMap.put("createUser", createUser);
			if (type.equals(EProcessType.OPERATE_APPROVE)){
				processInstanceParamMap.put("subType", paramMap.get("TASK_TYPE"));
			}else{
				processInstanceParamMap.put("subType", parameter.get("subType"));
			}
			processInstanceDao.createProcessInstance(processInstanceParamMap);

			logger.debug("create process success!");
			Integer instanceId = (Integer) processInstanceParamMap.get("processInstanceId");
			Integer currentTaskId = (Integer) processInstanceParamMap.get("currentTaskId");

			doSubmitTask(currentTaskId, instanceId, createUser, null);

			return instanceId;
		} else {
			throw new Exception("can not find the process module by the type! the type is (" + type + ")");
		}
	}

	public void reSubmitTask(Integer instanceId, Object body, HashMap<String, Object> paramMap, String createUser) {
		HashMap<String, Object> processInstanceParamMap = new HashMap<>();
		processInstanceParamMap.put("processInstanceId", instanceId);
		processInstanceParamMap.put("body", body);
		processInstanceDao.updateProcessInstance(processInstanceParamMap);

		Integer currentTaskId = (Integer) processInstanceParamMap.get("currentTaskId");

		String checkContext = (String) paramMap.get("checkContext");
		doSubmitTask(currentTaskId, instanceId, createUser, checkContext);
	}

	public void closeProcess(Integer instanceId, String createUser) {
		HashMap<String, Object> processInstanceParamMap = new HashMap<>();
		processInstanceParamMap.put("processInstanceId", instanceId);
		processInstanceParamMap.put("checkUser", createUser);

		processInstanceDao.closeProcessInstance(processInstanceParamMap);

		localProcessEngine.dealProcess(processInstanceParamMap);
	}

	public void doSubmitTask(Integer currentTaskId, Integer instanceId, String createUser, String checkContext) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("refInstance", instanceId);
		parameter.put("id", currentTaskId);
		parameter.put("checkStatus", EProcessTaskStatus.PASS);
		parameter.put("checkContext", checkContext);
		parameter.put("checkUser", createUser);
		doCheckTask(parameter);
		logger.debug("do submit task success!");
	}

	public void doCheckTask(HashMap<String, Object> checkTaskMap) {
		processInstanceDao.updateInstanceTask(checkTaskMap);

		localProcessEngine.dealProcess(checkTaskMap);
	}
}
