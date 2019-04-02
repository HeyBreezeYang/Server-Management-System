package com.system.started.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;

@Component
public class ThirdAPIService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(ThirdAPIService.class);

	@Autowired
	private DBService dbService;

	public String listDhRooms() {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DH_ROOMS, new HashMap<String, Object>());

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDhRooms successful! the result is :" + result);
		return result;
	}
	
	public String listDhRoomDevices(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DH_ROOM_DEVICES, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDhRoomDevices successful! the result is :" + result);
		return result;
	}
	
	public String listDhEvents() {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_DH_EVENTS, new HashMap<String, Object>());

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listDhEvents successful! the result is :" + result);
		return result;
	}
	
	public String updateDhEventStatus(HashMap<String, Object> paramMap) {
		try {
			Integer dealStatus = Integer.parseInt(paramMap.get("dealStatus").toString());
			if(dealStatus == 1) { //已处理，将告警记录移动到 history 表中
				dbService.insert(DBServiceConst.INSERT_DH_EVENT_HISTORY, paramMap);
				dbService.delete(DBServiceConst.DELETE_DH_EVENT, paramMap);
			}else {
				dbService.update(DBServiceConst.UPDATE_DH_EVENT, paramMap);
			}
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("messageStatus", "END");
			resultMap.put("messageContent", "告警处理成功！");
			return JsonHelper.toJson(resultMap);
		} catch (Exception e) {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("messageStatus", "ERROR");
			resultMap.put("messageContent", "告警处理失败！");
			return JsonHelper.toJson(resultMap);
		}
	}
}
