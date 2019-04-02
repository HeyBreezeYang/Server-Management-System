package com.system.started.notification.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.core.config.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.notification.INotificationReceiveListener;
import com.system.started.notification.impl.action.IEventNotificationAction;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.notification.ENotificationType;
import com.vlandc.oss.model.notification.NotificationObject;

@Order(1)
@Component
public class NotificationSendToWebListener implements INotificationReceiveListener {
	private final static Logger logger = LoggerFactory.getLogger(NotificationSendToWebListener.class);
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private INotificationSendToWebTool socketSendTool;

	@Resource
	private HashMap<String, Object> eventNotificationActionMap;
	
	@Override
	public void dealNotification(NotificationObject notification) {
		logger.debug("receive notification:" + JsonHelper.toJson(notification));
		try {
			socketSendTool.sendMessage(notification);
		} catch (Exception e) {
			logger.error("send message error !", e);
		}

		if(notification.getNotificationType() != null && notification.getNotificationType().equals(ENotificationType.ZABBIX_EVENT_ADD)){
			HashMap<String, Object> notificationParameters = notification.getParameter();
			
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put("hostId", notificationParameters.get("hostId"));
//			paramMap.put("triggerId", notificationParameters.get("triggerId"));
			List<HashMap<String, Object>> notificationResult = dbService.directSelect(DBServiceConst.SELECT_MONITOR_EVENT_NOTIFICATION, paramMap);
			
			for (HashMap<String, Object> itemMap : notificationResult) {
				String type = (String) itemMap.get("notificationType");
				IEventNotificationAction dealAction =  (IEventNotificationAction) eventNotificationActionMap.get(type);
				dealAction.dealEventNotification(notificationParameters);
			}
		}
	}
}
