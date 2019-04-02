package com.system.started.db.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.process.engine.local.EProcessTaskStatus;

@Component
public class ProcessInstanceDao {

	@Autowired
	private DBService dbService;

	@Transactional
	public void createProcessModuleInstance(HashMap<String, Object> paramMap) {
		dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_INSTANCE, paramMap);
		dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK_INSTANCE, paramMap);
		dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK_INSTANCE_DETAIL, paramMap);
	}

	@Transactional
	public void createProcessInstance(HashMap<String, Object> paramMap) {
		dbService.insert(DBServiceConst.INSERT_PROCESS_INSTANCE, paramMap);
		createProcessInstanceTask(paramMap);
	}
	
	@Transactional
	public void updateProcessInstance(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_PROCESS_INSTANCE, paramMap);
		List<HashMap<String, Object>> processInstanceList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_INSTANCE, paramMap);
		paramMap.put("moduleInstanceId",processInstanceList.get(0).get("refModuleInstance"));
		createProcessInstanceTask(paramMap);
	}

	@Transactional
	public void closeProcessInstance(HashMap<String, Object> paramMap) {
		//删除当前需要执行的任务
		dbService.delete(DBServiceConst.DELETE_PROCESS_INSTANCE_UNCHECK_TASK, paramMap);
		
		//插入关闭任务条目
		List<HashMap<String, Object>> processInstanceList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_INSTANCE, paramMap);
		paramMap.put("moduleInstanceId",processInstanceList.get(0).get("refModuleInstance"));
		paramMap.put("taskId", 0);
		dbService.insert(DBServiceConst.INSERT_PROCESS_INSTANCE_TASK, paramMap);
		
		paramMap.put("refInstance", paramMap.get("processInstanceId"));
		paramMap.put("id", paramMap.get("currentTaskId"));
		paramMap.put("checkStatus", EProcessTaskStatus.PASS);
		paramMap.put("checkContext", "关闭流程.");
		paramMap.put("nextTask", -1);
		paramMap.put("currentTaskType", "CLOSE"); //设置当前步骤对应的操作类型
		// 更新当前任务状态信息，并更新当前任务的下一步任务
		dbService.update(DBServiceConst.UPDATE_PROCESS_INSTANCE_TASK, paramMap);

		// 更新当前流程的当前步骤为新增的步骤
		HashMap<String, Object> parameter = new HashMap<>(3);
		parameter.put("processInstanceId", paramMap.get("processInstanceId"));
		parameter.put("currentTaskId", -1);
		dbService.update(DBServiceConst.UPDATE_PROCESS_INSTANCE_CURRENT_TASK, parameter);
	}

	@Transactional
	public void createProcessInstanceTask(HashMap<String, Object> paramMap){
		// 查询当前执行步骤
		List<HashMap<String, Object>> moduleInstanceList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_MODULE_INSTANCE, paramMap);
		paramMap.put("taskId", moduleInstanceList.get(0).get("beginTask"));
		dbService.insert(DBServiceConst.INSERT_PROCESS_INSTANCE_TASK, paramMap);

		dbService.update(DBServiceConst.UPDATE_PROCESS_INSTANCE_CURRENT_TASK, paramMap);
	}

	@Transactional
	public void updateInstanceTask(HashMap<String, Object> paramMap) {
		Integer nextTask = -1;
		HashMap<String, Object> parameter = new HashMap<>(3);
		//查询当前流程模板实例中的当前流程节点属性
		List<HashMap<String, Object>> moduleTaskInstanceList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_MODULE_TASK_INSTANCE, paramMap);
		if (moduleTaskInstanceList != null && !moduleTaskInstanceList.isEmpty()) {
			HashMap<String, Object> moduleTaskInstance = moduleTaskInstanceList.get(0);
			parameter.put("moduleInstanceId", moduleTaskInstance.get("refModuleInstance"));
			paramMap.put("currentTaskType", moduleTaskInstance.get("type")); //设置当前步骤对应的操作类型
			Object checkStatus = paramMap.get("checkStatus");
			if (checkStatus.toString().equals(EProcessTaskStatus.PASS.toString())) {
				//审批通过，查询流程模板中的下一节点
				nextTask = (Integer) moduleTaskInstance.get("nextTask");
			} else {
				//审批不通过，查询流程模板中定义的上一节点
				nextTask = (Integer) moduleTaskInstance.get("preTask");
			}
		}
		
		if (nextTask != -1) {
			// 插入下一步的动作并返回相应的序列号
			parameter.put("taskId", nextTask);
			parameter.put("processInstanceId", paramMap.get("refInstance"));
			dbService.insert(DBServiceConst.INSERT_PROCESS_INSTANCE_TASK, parameter);
			paramMap.put("nextTask", parameter.get("currentTaskId"));
		} else {
			paramMap.put("nextTask", nextTask);
			parameter.put("currentTaskId", nextTask);
		}

		// 更新当前任务状态信息，并更新当前任务的下一步任务
		dbService.update(DBServiceConst.UPDATE_PROCESS_INSTANCE_TASK, paramMap);

		// 更新当前流程的当前步骤为新增的步骤
		parameter.put("processInstanceId", paramMap.get("refInstance"));
		dbService.update(DBServiceConst.UPDATE_PROCESS_INSTANCE_CURRENT_TASK, parameter);
	}
}
