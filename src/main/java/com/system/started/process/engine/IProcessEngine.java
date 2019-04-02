package com.system.started.process.engine;

import java.util.HashMap;

import com.system.started.process.EProcessEngineType;
import com.system.started.process.EProcessType;

public interface IProcessEngine {

	public EProcessEngineType getEngineType();

	/**
	 * 创建流程，并返回标识符
	 * 
	 * @param name
	 * @param body
	 * @param paramMap
	 * @return
	 */
	public String sendProcess(String name, EProcessType type, Object body, HashMap<String, Object> paramMap, String createUser) throws Exception;

	public void reSubmitTask(Integer instanceId, Object body, HashMap<String, Object> paramMap, String createUser) throws Exception;

	public void closeProcess(Integer instanceId, String createUser) throws Exception;
}
