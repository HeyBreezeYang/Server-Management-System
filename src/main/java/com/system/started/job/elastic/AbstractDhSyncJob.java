package com.system.started.job.elastic;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.system.started.job.bean.UserObject;
import com.vlandc.oss.common.JsonHelper;

public abstract class AbstractDhSyncJob {

	private final static Logger logger = LoggerFactory.getLogger(AbstractDhSyncJob.class);
	
	// 登录临时url，对接中通服时需要修改
	private final static String LOGIN_URL = "http://116.52.158.233:9979/Api/PortalUser/Login";
	
	@Autowired
	private RestTemplate restTemplate;
	
	protected HashMap<String, Object> login(UserObject userObject) {
		HashMap<String, Object> bodyParamMap = new HashMap<String, Object>();
		bodyParamMap.put("LoginName", userObject.getLoginName());
		bodyParamMap.put("LoginPass", userObject.getLoginPass());
		
		HashMap<String, Object> resultMap = this.doRequest(LOGIN_URL, HttpMethod.POST, bodyParamMap);
		return resultMap;
	}
	
	protected HashMap<String, Object> doRequest(String url, HttpMethod method) {
		return this.doRequest(url, method, null, null);
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
