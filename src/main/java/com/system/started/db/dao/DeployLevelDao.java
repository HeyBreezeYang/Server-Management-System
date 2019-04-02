package com.system.started.db.dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;

@Component
public class DeployLevelDao implements IDao {

	@Autowired
	private DBService dbService;

	@Override
	@Transactional
	public void insert(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_DEPLOY_SERVICE_TEMPLATE_PROPERTIES_LEVEL,null);

		dbService.insert(DBServiceConst.INSERT_DEPLOY_SERVICE_TEMPLATE_PROPERTIES_LEVEL,paramMap);
	}

	@Override
	public void update(HashMap<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

}
