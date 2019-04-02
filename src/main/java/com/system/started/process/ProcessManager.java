package com.system.started.process;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.process.engine.IProcessEngine;
import com.system.started.process.impl.IProcessListener;
import com.vlandc.oss.common.CommonTools;
import com.vlandc.oss.common.JsonHelper;

@Component
public class ProcessManager {
	private final static Logger logger = LoggerFactory.getLogger(ProcessManager.class);

	@Autowired
	private DBService dbService;

	private HashMap<String, IProcessEngine> processEngineMap = null;

	private HashMap<String, List<IProcessListener>> processListenerMap = null;

	public void setProcessListenerMap(HashMap<String, List<IProcessListener>> processListenerMap) {
		this.processListenerMap = processListenerMap;
	}

	public HashMap<String, IProcessEngine> getProcessEngineMap() {
		return processEngineMap;
	}

	public void setProcessEngineMap(HashMap<String, IProcessEngine> processEngineMap) {
		this.processEngineMap = processEngineMap;
	}

	/**
	 * 创建流程步骤：<br>
	 * 1、根据流程类型，发送到对应的流程引擎创建相应的流程；<br>
	 * 2、流程引擎需返回相应的标识符；<br>
	 * 3、保存到OSS数据库中，便于后期进行关联；<br>
	 * 
	 * @param name
	 *            流程名称
	 * @param type
	 *            流程类型
	 * @param body
	 *            流程内容
	 * @param paramMap
	 *            其他自定义参数
	 * @param createUser
	 *            创建人
	 * 
	 * @return 返回创建的流程对应的ID 该ID在工单发起的流程是，需要建立相关表，保存关联关系，可以存在一个工单对应多个审批流程
	 */
	public Integer createProcess(String name, EProcessType type, byte[] body, HashMap<String, Object> paramMap, String createUser) {
		String processType = type.toString(); 
		if (processEngineMap.containsKey(processType)) {
			try {
				HashMap<String, Object> parameter = new HashMap<>();
				parameter.put("name", name);
				parameter.put("type", type);
				parameter.put("body", body);
				
				Object bodyObject = CommonTools.bytesToObject((byte[]) body);
				if (type.equals(EProcessType.WORK_ORDER_CHECK)) {
					parameter.put("bodyType", EProcessType.WORK_ORDER_CHECK);
				} else {
					parameter.put("bodyType", bodyObject.getClass().getSimpleName());
				}
				 
				parameter.put("param", JsonHelper.toJson(paramMap));
				parameter.put("createUser", createUser);
				parameter.put("status", EProcessStatus.SUBMITED);
				dbService.insert(DBServiceConst.INSERT_PROCESS, parameter);
				Integer processId = (Integer) parameter.get("processId");

				IProcessEngine processEngine = processEngineMap.get(processType); 
				String identity = processEngine.sendProcess(name, type, body, paramMap, createUser);

				parameter.put("engineType", processEngine.getEngineType());
				parameter.put("identity", identity);
				dbService.update(DBServiceConst.UPDATE_PROCESS_ENGINE, parameter);

				dealPorcess(processEngine.getEngineType(), identity, EProcessStatus.SUBMITED, EProcessType.getSubmitTips(type), paramMap);

				return processId;
			} catch (Exception e) { 
				logger.debug("create process error.", e); 
				return null;
			}
		} else {
			logger.debug("can't find the engine of process by the process type.the process type is (" + type + ")");
		}
		return null;
	}

	public void reSubmitTask(Integer processId, EProcessType type, Object body, HashMap<String, Object> paramMap, String createUser) {
		String processType = type.toString();
		if (processEngineMap.containsKey(processType)) {
			try {
				HashMap<String, Object> parameter = new HashMap<>();
				parameter.put("processId", processId);
				List<HashMap<String, Object>> processResultList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_DETAILS, parameter);

				if (processResultList != null && processResultList.size() > 0) {
					// 更新流程body信息
					parameter.put("body", body);
					dbService.update(DBServiceConst.UPDATE_PROCESS_PARAM, parameter);

					// 开始处理流程
					Integer instanceId = Integer.parseInt((String) processResultList.get(0).get("identity"));
					IProcessEngine processEngine = processEngineMap.get(processType);
					processEngine.reSubmitTask(instanceId, body, paramMap, createUser);
				} else {
					logger.debug("reSubmit process error(can not find the process by the id [" + processId + "]).");
				}
			} catch (Exception e) {
				logger.debug("reSubmit process error.", e);
			}
		} else {
			logger.debug("can't find the engine of process by the process type.the process type is (" + type + ")");
		}
	}

	public void closeProcess(Integer processId, EProcessType type, String createUser) {
		String processType = type.toString();
		if (processEngineMap.containsKey(processType)) {
			try {
				HashMap<String, Object> parameter = new HashMap<>();
				parameter.put("processId", processId);
				List<HashMap<String, Object>> processResultList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_DETAILS, parameter);
				
				Integer instanceId = Integer.parseInt((String) processResultList.get(0).get("identity"));
				IProcessEngine processEngine = processEngineMap.get(processType);
				processEngine.closeProcess(instanceId, createUser);
			} catch (Exception e) {
				logger.debug("close process error.", e);
			}
		} else {
			logger.debug("can't find the engine of process by the process type.the process type is (" + type + ")");
		}
	}

	/**
	 * 当流程发生变化时，根据注册的监听器，进行相应的处理
	 * 
	 * @param identity
	 *            流程标识符
	 * @param status
	 *            流程状态
	 * @param statusResult
	 *            流程结果
	 * @param paramMap
	 *            其他自定义参数
	 */
	public void dealPorcess(EProcessEngineType engineType, String identity, EProcessStatus status, String statusResult, HashMap<String, Object> paramMap) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("identity", identity);
		parameter.put("engineType", engineType);
		if(paramMap != null && paramMap.get("loginId") != null){
			parameter.put("loginId", paramMap.get("loginId")); 	
		}
		List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_PROCESSES, parameter);
		if (resultList.size() > 0) {
			HashMap<String, Object> processItemMap = resultList.get(0);
			List<IProcessListener> processListenerList = null; 
			if (processListenerMap.containsKey(engineType.name())) {
				processListenerList = processListenerMap.get(engineType.name());
			}else{
				processListenerList = processListenerMap.get("default");
			}
			for (int i = 0; i < processListenerList.size(); i++) {
				try {
					processListenerList.get(i).dealPorcess(processItemMap, status, statusResult, paramMap);
				} catch (Exception e) {
					logger.error("deal process listener error!", e);
				}
			}
		}
	}

}
