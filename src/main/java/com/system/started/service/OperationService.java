package com.system.started.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;

@Component
public class OperationService extends AbstractService{

	private final static Logger logger = LoggerFactory.getLogger(OperationService.class);
	
	@Autowired
	private DBService dbService;
	
	public String listOperationNodes(HttpSession session,HashMap<String,Object> paramMap) throws Exception{
		Integer start=(Integer)paramMap.get("start");
		Integer length=(Integer)paramMap.get("length");
		Integer currentPage=(Integer)paramMap.get("currentPage");
		Integer draw=(Integer)paramMap.get("draw");
		
		parseRelationLoginIds(session, paramMap);

		HashMap<String, Object> roleParamMap = new HashMap<>();
		parseCurrentLoginIds(session, roleParamMap);
		
		Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
		paramMap.put("countSize", countSize);
		
		HashMap<String, Object> resultMap = null;
		if (start != null && length != -1) {
			int startNum = start;
			if(currentPage == null){
				currentPage = startNum == 0 ? 1 : startNum / length + 1;
			}

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES, paramMap, currentPage, length);
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String listOperationNodesByIPAddress(HttpSession session,HashMap<String,Object> paramMap){
		HashMap<String, Object> resultMap = null;
		try {
			dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
			if (paramMap.containsKey("ipAddress")) {
				List<String> ipaddressList = (List<String>) paramMap.get("ipAddress");
				if (ipaddressList.size() > 0) {
					dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_QUERY_IPADDRESS, paramMap);
					parseRelationLoginIds(session, paramMap);

					HashMap<String, Object> roleParamMap = new HashMap<>();
					parseCurrentLoginIds(session, roleParamMap);
					Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
					paramMap.put("countSize", countSize);

					resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES_BY_IPADDRESS, paramMap);
				}
			}
		} catch (Exception e) {
			logger.error("query listOperationNodes by ipaddress error!", e);
		} finally {
			dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
		}

		if (resultMap == null) {
			resultMap=JsonHelper.fromJson(HashMap.class, super.invalidRequest());
		}
		
		String result = JsonHelper.toJson(resultMap);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String listOperationNodesByHostName(HttpSession session,HashMap<String,Object> paramMap){
		HashMap<String, Object> resultMap = null;
		try {
			dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
			if (paramMap.containsKey("hostName")) {
				List<String> hostNameList = (List<String>)paramMap.get("hostName");
				if (hostNameList.size() > 0) {
					dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_QUERY_HOSTNAME, paramMap);
					parseRelationLoginIds(session, paramMap);

					HashMap<String, Object> roleParamMap = new HashMap<>();
					parseCurrentLoginIds(session, roleParamMap);
					Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
					paramMap.put("countSize", countSize);

					resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES_BY_HOSTNAME, paramMap);
				}
			}
		} catch (Exception e) {
			logger.error("query listOperationNodes by ipaddress error!", e);
		} finally {
			dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
		}

		if (resultMap == null) {
			resultMap= JsonHelper.fromJson(HashMap.class, super.invalidRequest());
		}
		
		String result = JsonHelper.toJson(resultMap);
		return result;
	}
}
