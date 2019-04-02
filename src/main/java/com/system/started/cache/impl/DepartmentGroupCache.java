package com.system.started.cache.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.cache.ICache;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.ConcurrentHashSet;

/**
 * 组织架构信息：<br>
 * 1、组织架构（name）
 * 
 * @author Administrator
 *
 */
@Component
public class DepartmentGroupCache implements ICache {


	private static ConcurrentHashSet<String> departmentGroupCacheSet = new ConcurrentHashSet<>();

	@Autowired
	private DBService dbService;

	@Override
	public void initCacheData() {
		initDepartmentGroupSet();
	}

	private void initDepartmentGroupSet() {
		List<HashMap<String, Object>> departmentGroupList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_DEPARTMENT_GROUPS, new HashMap<String,Object>());
		for (int i = 0; i < departmentGroupList.size(); i++) {
			HashMap<String, Object> departmentGroupItem = departmentGroupList.get(i);
			departmentGroupCacheSet.add((String) departmentGroupItem.get("name"));
		}
	}

	public boolean departmentGroupExists(String name) {
		return departmentGroupCacheSet.contains(name);
	}

	public boolean addDepartmentGroup(String name) {
		return departmentGroupCacheSet.add(name);
	}

	public boolean deleteDepartmentGroup(String name) {
		return departmentGroupCacheSet.remove(name);
	}
}
