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
 * 缓存角色信息：<br>
 * 1、角色（name）
 * 
 * @author Administrator
 *
 */
@Component
public class RoleCache implements ICache {


	private static ConcurrentHashSet<String> roleCacheSet = new ConcurrentHashSet<>();

	@Autowired
	private DBService dbService;

	@Override
	public void initCacheData() {
		initRoleSet();
	}

	private void initRoleSet() {
		List<HashMap<String, Object>> roleList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_ROLES, new HashMap<String,Object>());
		for (int i = 0; i < roleList.size(); i++) {
			HashMap<String, Object> roleItem = roleList.get(i);
			roleCacheSet.add((String) roleItem.get("name"));
		}
	}

	public boolean roleExists(String name) {
		return roleCacheSet.contains(name);
	}

	public boolean addRole(String name) {
		return roleCacheSet.add(name);
	}

	public boolean deleteRole(String name) {
		return roleCacheSet.remove(name);
	}
}
