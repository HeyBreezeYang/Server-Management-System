package com.system.started.db.dao;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;
import com.vlandc.oss.common.JsonHelper;

@Component
public class ProcessModuleDao implements IDao {

	private static final Logger logger = LoggerFactory.getLogger(ProcessModuleDao.class);
	
	@Autowired
	private DBService dbService;
	
	@Transactional
	@Override
	public void insert(HashMap<String, Object> paramMap) {
		paramMap.put("beginTask", 0);
		logger.debug("the create process module param is :" + JsonHelper.toJson(paramMap));
		dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE, paramMap);
		int moduleId= (int) paramMap.get("moduleId");
		
		@SuppressWarnings("unchecked")
		List<String> moduleDepartments=(List<String>) paramMap.get("department");
		@SuppressWarnings("unchecked")
		List<String> moduleTaskTypes= (List<String>) paramMap.get("taskType");
		for(String depart : moduleDepartments){
			for (String type : moduleTaskTypes) {
				HashMap<String,Object> moduleTypeParamMap= new HashMap<String,Object>();
				moduleTypeParamMap.put("id", moduleId);
				moduleTypeParamMap.put("subType", type);
				moduleTypeParamMap.put("status", 1);
				moduleTypeParamMap.put("department", depart);
				moduleTypeParamMap.put("weight", paramMap.get("weight"));
				
				dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TYPE, moduleTypeParamMap);
			}
		}
		
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> tasks=(List<HashMap<String, Object>>) paramMap.get("tasks");
		for(int i=0;i<tasks.size();i++){
			HashMap<String,Object>taskMap=tasks.get(i);
			taskMap.put("refModule", moduleId);
			dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK, taskMap);
			int id=(int) taskMap.get("id");
			
			String assignType=(String) taskMap.get("objType");
			List<Object> taskAssignedValues=null;
			if(assignType.equals("user")){
				taskAssignedValues=(List<Object>)taskMap.get("refUser");
			}else if(assignType.equals("role") || assignType.equals("all")){
				taskAssignedValues=(List<Object>)taskMap.get("refRole");
			}
			@SuppressWarnings("unchecked")
			List<String> taskType=(List<String>) taskMap.get("taskType");
			
			if(taskMap.containsKey("refDepartment")){
				List<String> taskDepartments=(List<String>) taskMap.get("refDepartment");
				for(String depart : taskDepartments){
					for(Object assigne : taskAssignedValues){
						for (String type : taskType) {
							HashMap<String,Object> taskDetailsParamMap= new HashMap<String,Object>();
							taskDetailsParamMap.put("id", id);
							taskDetailsParamMap.put("taskType", taskMap.get("type"));
							taskDetailsParamMap.put("subType", type);
							taskDetailsParamMap.put("refDepartment", depart);
							if(assignType.equals("role") || assignType.equals("all")){
								taskDetailsParamMap.put("refRole", assigne);
								taskDetailsParamMap.put("refUser", -1);
							}else if(assignType.equals("user")){
								taskDetailsParamMap.put("refUser", assigne);
								taskDetailsParamMap.put("refRole", -1);
							}
							dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK_DETAIL, taskDetailsParamMap);
						}
					}
				}
			}else{
				for(Object assigne : taskAssignedValues){
					for (String type : taskType) {
						HashMap<String,Object> taskDetailsParamMap= new HashMap<String,Object>();
						taskDetailsParamMap.put("id", id);
						taskDetailsParamMap.put("taskType", taskMap.get("type"));
						taskDetailsParamMap.put("subType", type);
						taskDetailsParamMap.put("refDepartment", -1);
						if(assignType.equals("role") || assignType.equals("all")){
							taskDetailsParamMap.put("refRole", assigne);
							taskDetailsParamMap.put("refUser", -1);
						}else if(assignType.equals("user")){
							taskDetailsParamMap.put("refUser", assigne);
							taskDetailsParamMap.put("refRole", -1);
						}
						dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK_DETAIL, taskDetailsParamMap);
					}
				}
			}
		}
	}

	@Transactional
	@Override
	public void update(HashMap<String, Object> paramMap) {
		int moduleId=Integer.parseInt(paramMap.get("id").toString());
		HashMap<String,Object> delParamMap= new HashMap<String,Object>();
		delParamMap.put("moduleId", moduleId);
		
		dbService.update(DBServiceConst.UPDATE_PROCESS_MODULE, paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TASK_DETAIL, delParamMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TASK, delParamMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TYPE, delParamMap);
		
		////////////////////////////////////////////////////////////////////////////////////////
		@SuppressWarnings("unchecked")
		List<String> moduleDepartments=(List<String>) paramMap.get("department");
		@SuppressWarnings("unchecked")
		List<String> moduleTaskTypes= (List<String>) paramMap.get("taskType");
		for(String depart : moduleDepartments){
			for (String type : moduleTaskTypes) {
				HashMap<String,Object> moduleTypeParamMap= new HashMap<String,Object>();
				moduleTypeParamMap.put("id", moduleId);
				moduleTypeParamMap.put("subType", type);
				moduleTypeParamMap.put("status", 1);
				moduleTypeParamMap.put("department", depart);
				moduleTypeParamMap.put("weight", paramMap.get("weight"));
				
				dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TYPE, moduleTypeParamMap);
			}
		}
		
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> tasks=(List<HashMap<String, Object>>) paramMap.get("tasks");
		for(int i=0;i<tasks.size();i++){
			HashMap<String,Object>taskMap=tasks.get(i);
			taskMap.put("refModule", moduleId);
			dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK, taskMap);
			int id=(int) taskMap.get("id");
			
			String assignType=(String) taskMap.get("objType");
			List<Object> taskAssignedValues=null;
			if(assignType.equals("user")){
				taskAssignedValues=(List<Object>)taskMap.get("refUser");
			}else if(assignType.equals("role") || assignType.equals("all")){
				taskAssignedValues=(List<Object>)taskMap.get("refRole");
			}
			@SuppressWarnings("unchecked")
			List<String> taskType=(List<String>) taskMap.get("taskType");
			
			if(taskMap.containsKey("refDepartment")){
				List<String> taskDepartments=(List<String>) taskMap.get("refDepartment");
				for(String depart : taskDepartments){
					for(Object assigne : taskAssignedValues){
						for (String type : taskType) {
							HashMap<String,Object> taskDetailsParamMap= new HashMap<String,Object>();
							taskDetailsParamMap.put("id", id);
							taskDetailsParamMap.put("taskType", taskMap.get("type"));
							taskDetailsParamMap.put("subType", type);
							taskDetailsParamMap.put("refDepartment", depart);
							if(assignType.equals("role") || assignType.equals("all")){
								taskDetailsParamMap.put("refRole", assigne);
								taskDetailsParamMap.put("refUser", -1);
							}else if(assignType.equals("user")){
								taskDetailsParamMap.put("refUser", assigne);
								taskDetailsParamMap.put("refRole", -1);
							}
							dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK_DETAIL, taskDetailsParamMap);
						}
					}
				}
			}else{
				for(Object assigne : taskAssignedValues){
					for (String type : taskType) {
						HashMap<String,Object> taskDetailsParamMap= new HashMap<String,Object>();
						taskDetailsParamMap.put("id", id);
						taskDetailsParamMap.put("taskType", taskMap.get("type"));
						taskDetailsParamMap.put("subType", type);
						taskDetailsParamMap.put("refDepartment", -1);
						if(assignType.equals("role") || assignType.equals("all")){
							taskDetailsParamMap.put("refRole", assigne);
							taskDetailsParamMap.put("refUser", -1);
						}else if(assignType.equals("user")){
							taskDetailsParamMap.put("refUser", assigne);
							taskDetailsParamMap.put("refRole", -1);
						}
						dbService.insert(DBServiceConst.INSERT_PROCESS_MODULE_TASK_DETAIL, taskDetailsParamMap);
					}
				}
			}
		}
	}
	
	@Transactional
	public void delete(HashMap<String, Object> paramMap){
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TASK_DETAIL, paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TASK, paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TYPE, paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE, paramMap);
	}

}
