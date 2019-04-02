package com.system.started.process.impl;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.process.EProcessStatus;

public class DbServiceListener implements IProcessListener {

	private static Logger logger = Logger.getLogger(DbServiceListener.class);

	@Autowired
	private DBService dbService;

	@Override
	public void initLisenter() {

	}

	@Override
	public void dealPorcess(HashMap<String, Object> processItemMap, EProcessStatus status, String statusResult, HashMap<String, Object> paramMap) {

		HashMap<String, Object> parameter = new HashMap<>();
		Integer processId = (Integer) processItemMap.get("id");

		parameter.put("id", processId);
		parameter.put("status", status);
		parameter.put("statusResult", statusResult);

		dbService.update(DBServiceConst.UPDATE_PROCESS, parameter);
		logger.debug("deal db process listener success!");
	}

}
