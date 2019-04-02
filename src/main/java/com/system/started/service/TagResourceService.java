package com.system.started.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;

@Component
public class TagResourceService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(TagResourceService.class);
	
	@Autowired
	private DBService dbService;
	
	public String listTags(HttpSession session,HashMap<String, Object> paramMap) throws Exception{
		parseRelationLoginIds(session, paramMap);
		HashMap<String, Object> resultMap=dbService.select(DBServiceConst.SELECT_SYSTEM_TAGS, paramMap);
		
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listTags successful! the result is : " + result);
		return result;
	}
}
