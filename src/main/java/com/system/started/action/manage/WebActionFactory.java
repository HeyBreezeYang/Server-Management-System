package com.system.started.action.manage;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class WebActionFactory<T>{

	private final static Logger logger = LoggerFactory.getLogger(WebActionFactory.class.getName());
	
	@Autowired  
    private ApplicationContext context; 
	
	private Map<String, String> actionDefineMap = null;
	private Map<String, T> actionMap = new HashMap<>();

	public void setActionDefineMap(Map<String, String> actionDefineMap) {
		this.actionDefineMap = actionDefineMap;
	}

	public Map<String, T> getActionMap() {
		return actionMap;
	}

	public void setActionMap(Map<String, T> actionMap) {
		this.actionMap = actionMap;
	}

	public void initFactory(){
		for (String actionDefineKey : actionDefineMap.keySet()) {
			try {
				actionMap.put(actionDefineKey, (T) context.getBean(actionDefineMap.get(actionDefineKey)));
			} catch (Exception e) {
				logger.error("加载配置文件错误！",e);
			}
		}
	}
}
