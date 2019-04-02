package com.system.started.db.dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;

@Component
public class ResourcePoolDao implements IDao {

	@Autowired
	private DBService dbService;

	@Transactional
	public void insert(HashMap<String, Object> paramMap) {
		// 插入基础信息
		dbService.insert(DBServiceConst.INSERT_RESOURCE_POOL, paramMap);

		String poolType = (String) paramMap.get("poolType");
		if (poolType.equals("COMPUTE") || poolType.equals("MANAGE")) {
			dbService.insert(DBServiceConst.INSERT_RESOURCE_COMPUTE_POOL, paramMap);
		} else if (poolType.equals("STORAGE")) {
			dbService.insert(DBServiceConst.INSERT_RESOURCE_STORAGE_POOL, paramMap);
		} else if (poolType.equals("NETWORK")) {
			 dbService.insert(DBServiceConst.INSERT_RESOURCE_NETWORK_POOL,paramMap);
		} else if (poolType.equals("MANAGE")) {
		}
	}

	@Override
	@Transactional
	public void update(HashMap<String, Object> paramMap) {
		dbService.update(DBServiceConst.UPDATE_RESOURCE_POOL, paramMap);
		String poolType = (String) paramMap.get("poolType");
		if (poolType.equals("COMPUTE") || poolType.equals("MANAGE")) {
			dbService.insert(DBServiceConst.UPDATE_RESOURCE_COMPUTE_POOL, paramMap);
		} else if (poolType.equals("STORAGE")) {
			dbService.insert(DBServiceConst.UPDATE_RESOURCE_STORAGE_POOL, paramMap);
		} else if (poolType.equals("NETWORK")) {
			 dbService.insert(DBServiceConst.UPDATE_RESOURCE_NETWORK_POOL,paramMap);
		} else if (poolType.equals("MANAGE")) {
		}
	}
}
