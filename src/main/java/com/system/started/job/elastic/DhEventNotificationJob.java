package com.system.started.job.elastic;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.notification.NotificationManager;
import com.vlandc.oss.model.notification.ENotificationType;
import com.vlandc.oss.model.notification.NotificationObject;

@Component("dhEventNotificationJobImpl")
public class DhEventNotificationJob implements SimpleJob {

	@Autowired
	private DBService dbService;

	@Autowired
	private NotificationManager notificationManager;
	
	@Override
	public void execute(ShardingContext shardingContext) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("checkTime", true);
		paramMap.put("dealStatusList", "2,3");
		List<HashMap<String, Object>> list = dbService.directSelect(DBServiceConst.SELECT_DH_EVENTS, paramMap);
		for (HashMap<String, Object> item : list) {
			NotificationObject eventNotification = new NotificationObject();
			//eventNotification.setNotificationType(ENotificationType.DH_EVENT);
//			eventNotification.setRefUser(getCurrentLoginId());
			HashMap<String, Object> addParamMap = new HashMap<>();
			addParamMap.putAll(item);
			eventNotification.setParameter(addParamMap);
			notificationManager.addNotificationSendQueue(eventNotification);
			
			// 将推送后的告警时间设置为空
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("id", item.get("id"));
			parameters.put("dealValue", "");
			dbService.update(DBServiceConst.UPDATE_DH_EVENT, parameters);
		}
	}
}
