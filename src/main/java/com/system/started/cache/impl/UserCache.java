package com.system.started.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.cache.ICache;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.ConcurrentHashSet;

/**
 * 缓存用户信息：<br>
 * 1、用户登录名（loginId） <========> openstack projectId
 * 
 * @author Administrator
 *
 */
@Component
public class UserCache implements ICache {

	// private static HashMap<String,String> userCacheMap = new HashMap<>();

	private static ConcurrentHashSet<String> userCacheSet = new ConcurrentHashSet<>();
	private static ConcurrentHashMap<String, ConcurrentHashSet<String>> userRelationSubMap = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, String> userRelationParentMap = new ConcurrentHashMap<>();

	@Autowired
	private DBService dbService;

	@Override
	public void initCacheData() {
		initUserSet();

		initRelationMap();
	}

	private void initUserSet() {
		List<HashMap<String, Object>> userList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USERS, new HashMap<String,Object>());
		for (int i = 0; i < userList.size(); i++) {
			HashMap<String, Object> userItem = userList.get(i);
			userCacheSet.add((String) userItem.get("loginId"));
		}
	}

	private void initRelationMap() {
		userRelationSubMap.clear();
		
		List<HashMap<String, Object>> userRelationList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USER_RELATION, new HashMap<String,Object>());
		for (int i = 0; i < userRelationList.size(); i++) {
			HashMap<String, Object> userRelationItem = userRelationList.get(i);
			String loginId = (String) userRelationItem.get("loginId");
			String subLoginId = (String) userRelationItem.get("subLoginId");
			
			//保存父级节点对应关系
			userRelationParentMap.put(subLoginId, loginId); 
			
			//保存下一级节点对应关系
			if (userRelationSubMap.containsKey(loginId)) {
				userRelationSubMap.get(loginId).add(subLoginId);
			} else {
				ConcurrentHashSet<String> subLoginIdSet = new ConcurrentHashSet<>();
				subLoginIdSet.add(subLoginId);
				userRelationSubMap.put(loginId, subLoginIdSet);
			}
		}
	}

	public boolean userExists(String loginId) {
		return userCacheSet.contains(loginId);
	}

	public boolean addUser(String loginId) {
		return userCacheSet.add(loginId);
	}

	public boolean deleteUser(String loginId) {
		return userCacheSet.remove(loginId);
	}
	
	public String getParentRelationLoginIds(String loginId) {
		StringBuffer resultBuffer = new StringBuffer();
		if (userRelationParentMap.containsKey(loginId)) {
			String parentLoginId = userRelationParentMap.get(loginId);
			resultBuffer.append(",");
			resultBuffer.append("'");
			resultBuffer.append(parentLoginId);
			resultBuffer.append("'");
			if (userRelationParentMap.containsKey(parentLoginId)) {
				resultBuffer.append(getParentRelationLoginIds(parentLoginId));
			}
		}
		String resultStr = resultBuffer.toString();
		if (resultStr.length() > 0) {
			return resultStr.substring(1);
		}
		return null;
	}

	public String getSubRelationLoginIds(String loginId) {
		StringBuffer resultBuffer = new StringBuffer();
//		resultBuffer.append("'");
		resultBuffer.append(loginId);
//		resultBuffer.append("'");
		if (userRelationSubMap.containsKey(loginId)) {
			ConcurrentHashSet<String> subLoginIdSet = userRelationSubMap.get(loginId);
			for (String subLoginId : subLoginIdSet) {
				if (userRelationSubMap.containsKey(subLoginId)) {
					resultBuffer.append(getSubRelationLoginIds(subLoginId));
				} else {
					resultBuffer.append(",");
					resultBuffer.append("'");
					resultBuffer.append(subLoginId);
					resultBuffer.append("'");
				}
			}
		}
		return resultBuffer.toString();
	}

	public void reloadUserRelationMap(){
		initRelationMap();
	}

	// /**
	// * 根据用户名查询对应的projectId
	// * @param loginId
	// * @return
	// */
	// public static String getUserProjectId(String loginId){
	// if (userCacheMap.containsKey(loginId)) {
	// return userCacheMap.get(loginId);
	// }
	// return null;
	// }
	//
	//
	// /**
	// * 新增用户时，更新缓存数据
	// * @param loginId
	// * @param projectId
	// */
	// public static void addUserPorjectId(String loginId,String projectId){
	// userCacheMap.put(loginId, projectId);
	// }
}
