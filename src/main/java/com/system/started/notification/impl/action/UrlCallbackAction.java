package com.system.started.notification.impl.action;

import java.util.HashMap;

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

@Component("urlCallbackAction")
public class UrlCallbackAction implements IEventNotificationAction {

	private final static Logger logger = LoggerFactory.getLogger(UrlCallbackAction.class);
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public void dealEventNotification(HashMap<String, Object> paramMap) {
		// 以post的方式将告警数据发送到url端
		String url = (String) paramMap.get("url");
		if(url != null && !url.equals("")) {
			HashMap<String, Object> resultMap = doRequest(url, HttpMethod.POST, paramMap);
			
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			String description = (String) paramMap.get("description");
			parameters.put("eventId", paramMap.get("eventId"));
			parameters.put("notificationType", "URL_CALLBACK");
			parameters.put("notificationContent", "告警内容：" + description + "，已发送到 " + url + "，发送结果：" + JsonHelper.toJson(resultMap));
			dbService.insert(DBServiceConst.INSERT_ZABBIX_EVENT_NOTIFICATION, parameters);
		}else{
			logger.error("url is null or empty !");
		}
	}
	
	protected HashMap<String, Object> doRequest(String url, HttpMethod method, Object bodyParam) {
		return this.doRequest(url, method, bodyParam, null);
	}
	
	protected HashMap<String, Object> doRequest(String url, HttpMethod method, Object bodyParam, HashMap<String, String> headerParam) {
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
		}else{
			request = new HttpEntity<Object>(bodyParam, headers);
		}
		
		ResponseEntity<String> response = restTemplate.exchange(url, method, request, String.class);

		HashMap<String, Object> resultMap = new HashMap<>();
//		resultMap.put("headers", response.getHeaders());
//		resultMap.put("body", response.getBody());
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> responseBodyMap = JsonHelper.fromJson(HashMap.class, response.getBody());
		resultMap.putAll(responseBodyMap);

		logger.debug("the dh sync( " + url + " ) response :");
		logger.debug("	the status code is :" + response.getStatusCode());
		logger.debug("	the response map is : " + JsonHelper.toJson(response));
		return resultMap;
	}
}
