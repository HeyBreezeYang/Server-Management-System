package com.system.started.job.elastic;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.job.bean.UserObject;
import com.vlandc.oss.common.JsonHelper;

@Component("dhEventSyncJobImpl")
public class DhEventSyncJob extends AbstractDhSyncJob implements SimpleJob {

	private final static Logger logger = LoggerFactory.getLogger(DhEventSyncJob.class);
	
	@Autowired
	private UserObject userObject;
	
	@Autowired
	private DBService dbService;
	
	// 获取告警数据临时url，对接中通服时需要修改
	private String eventUrl = "http://116.52.158.233:9979/Api/PortalUser/Login";
	
	@Override
	public void execute(ShardingContext shardingContext) {
		try {
			HashMap<String, Object> userMap = login(userObject);
			String token = (String) userMap.get("token");
			
			String url = eventUrl + "?token=" + token;
			HashMap<String, Object> resultMap = doRequest(url, HttpMethod.GET);
			int resultCode = Integer.parseInt(resultMap.get("ResultCode").toString());
			if(resultCode == 0) {
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> resultContent = (List<HashMap<String, Object>>) resultMap.get("ResultConcent");
				for (HashMap<String, Object> itemMap : resultContent) {
					dbService.insert(DBServiceConst.INSERT_DH_EVENT, itemMap);
				}
			}else {
				logger.error("dh event sync error ! the result code is -1，the result is " + JsonHelper.toJson(resultMap));
			}
		} catch (Exception e) {
			logger.error("dh event sync error !", e);
		}
	}
}
