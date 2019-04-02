package com.system.started.db.dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;

@Component
public class UserRoleDao implements IDao {

	@Autowired
	private DBService dbService;
	
	@Override
	@Transactional
	public void insert(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_ROLE, paramMap);

		String roleIds = (String) paramMap.get("roleIds");
		if (roleIds.length() > 0) {
			String[] roleIdArray = roleIds.split(",");
			for (int i = 0; i < roleIdArray.length; i++) {
				HashMap<String, Object> parameter = new HashMap<>(2);
				parameter.put("userId", paramMap.get("userId"));
				parameter.put("roleId", roleIdArray[i]);
				dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_ROLE, parameter);
			}
		}
	}

	@Override
	public void update(HashMap<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

}
