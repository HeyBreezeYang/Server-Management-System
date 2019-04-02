package com.system.started.db.dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;

@Component
public class UserRelationDao implements IDao {

	@Autowired
	private DBService dbService;

	@Override
	@Transactional
	public void insert(HashMap<String, Object> paramMap) {
		dbService.delete(DBServiceConst.DELETE_SYSTEM_USER_RELATION, paramMap);
		
		String subLoginIds = (String)paramMap.get("subLoginIds");
		if (subLoginIds.length() > 0) {
			String[] subLoginIdArray = subLoginIds.split(",");
			for (int i = 0; i < subLoginIdArray.length; i++) {
				HashMap<String, Object> parameter = new HashMap<>(2);
				parameter.put("loginId", paramMap.get("loginId"));
				parameter.put("subLoginId", subLoginIdArray[i]);
				if (paramMap.get("loginId").equals(subLoginIdArray[i])) { //如果选中的是自己，则进行下一个循环
					continue;
				}
				dbService.insert(DBServiceConst.INSERT_SYSTEM_USER_RELATION, parameter);
			}
		}
	}

	@Override
	public void update(HashMap<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

}
