package com.system.started.db.dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;

@Component
public class RoleMenuDao implements IDao {

	@Autowired
	private DBService dbService;

	@Override
	@Transactional
	public void insert(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_SYSTEM_ROLE_MENU, paramMap);
		
		String menuIds = (String)paramMap.get("menuIds");
		if (menuIds.length() > 0) {
			String[] menuIdArray = menuIds.split(",");
			for (int i = 0; i < menuIdArray.length; i++) {
				HashMap<String, Object> parameter = new HashMap<>(2);
				parameter.put("roleId", paramMap.get("roleId"));
				parameter.put("menuId", menuIdArray[i]);
				dbService.insert(DBServiceConst.INSERT_SYSTEM_ROLE_MENU, parameter);
			}
		}
	}

	@Override
	public void update(HashMap<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

}
