package com.system.started.notification.impl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;

@Component("sendMessageAction")
public class SendMessageAction implements IEventNotificationAction {

	private final static Logger logger = LoggerFactory.getLogger(UrlCallbackAction.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private RestTemplate restTemplate;
    
	@Override
	public void dealEventNotification(HashMap<String, Object> paramMap) {
		HashMap<String, Object> userParamMap = new HashMap<String, Object>();
		userParamMap.put("receiveSms", 1);
		List<HashMap<String, Object>> userList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USERS,userParamMap);
		List<String> userNames = new ArrayList<String>();
		for (HashMap<String, Object> userItem : userList) {
			String userName = userItem.get("name") != null ? (String) userItem.get("name"):(String) userItem.get("loginId");
			userNames.add(userName);
		}
		
		String description = (String) paramMap.get("description");
		String userNameString = StringUtils.join(userNames,"|");
		
		//获取各种 参数集合
		HashMap<String, Object> dictParamMap = new HashMap<String, Object>();
		dictParamMap.put("dictType", "WEIXIN_RESEND_URL");
		String url = getUrl(dictParamMap);
		
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("toUser", userNameString);
		parametersMap.put("agented",1000017);
		parametersMap.put("content", description);
		HashMap<String, Object> resultMap = doRequest(url, HttpMethod.POST, parametersMap);
		logger.debug("send weixin result : " + JsonHelper.toJson(resultMap));
		
		// 发送的正文内容
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", paramMap.get("eventId"));
		parameters.put("notificationType", "SEND_MESSAGE");
		parameters.put("notificationContent", "告警内容：" + description + "，已通过微信发送到：" + userNameString);
		dbService.insert(DBServiceConst.INSERT_ZABBIX_EVENT_NOTIFICATION, parameters);
	}

	public String getUrl(HashMap<String, Object> paramMap) {
		HashMap<String, Object> urlMap = dbService.select(DBServiceConst.SELECT_DICT, paramMap);
		return (String) urlMap.get("dictId");
	}
	
	
	protected HashMap<String, Object> doRequest(String url, HttpMethod method, Object bodyParam) {
		return this.doRequest(url, method, bodyParam, null);
	}

	protected HashMap<String, Object> doRequest(String url, HttpMethod method, Object bodyParam,
			HashMap<String, String> headerParam) {
		logger.debug("the dh sync( " + url + " )request:");
		logger.debug("	the headerParam is :" + headerParam);
		logger.debug("	the bodyParam is :" + bodyParam);

		bodyParam = bodyParam == null ? new HashMap<>() : bodyParam;

		HttpHeaders headers = new HttpHeaders();
		if (headerParam != null) {
			for (String headerKey : headerParam.keySet()) {
				headers.add(headerKey, headerParam.get(headerKey));
			}
		}

		HttpEntity<Object> request = null;
		if (method.equals(HttpMethod.DELETE)) {
			request = new HttpEntity<>(headers);
		} else {
			request = new HttpEntity<Object>(bodyParam, headers);
		}

		ResponseEntity<String> response = restTemplate.exchange(url, method, request, String.class);

		HashMap<String, Object> resultMap = new HashMap<>();
		// resultMap.put("headers", response.getHeaders());
		// resultMap.put("body", response.getBody());

		@SuppressWarnings("unchecked")
		HashMap<String, Object> responseBodyMap = JsonHelper.fromJson(HashMap.class, response.getBody());
		resultMap.putAll(responseBodyMap);

		logger.debug("the dh sync( " + url + " ) response :");
		logger.debug("	the status code is :" + response.getStatusCode());
		logger.debug("	the response map is : " + JsonHelper.toJson(response));
		return resultMap;
	}
}
