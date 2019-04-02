package com.system.started.db.dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;

@Component
public class DataCenterDao implements IDao {

	@Autowired
	private DBService dbService;

	@Override
	@Transactional
	public void insert(HashMap<String, Object> paramMap) {
		// 插入基础信息
		// 新增成功后，会将新增的dataCenterId放入paramMap中
		dbService.insert(DBServiceConst.INSERT_DATACENTER, paramMap);

//		//插入默认的未分配资源池
//		dbService.insert(DBServiceConst.INSERT_DEFAULT_RESOURCE_POOL, paramMap);

	}

	@Override
	public void update(HashMap<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

}
