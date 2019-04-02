package com.system.started.notification.impl;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.notification.INotificationSendListener;
import com.system.started.notification.OSSNotificationRabbitMQClient;
import com.vlandc.oss.model.notification.NotificationObject;

@Order(1)
@Component
public class NotificationSendToMqListener implements INotificationSendListener {

	@Autowired
	OSSNotificationRabbitMQClient ossNotificationMQClient;
	
	@Override
	public void dealNotification(NotificationObject notification) {
		ossNotificationMQClient.send(notification);
	}

}
	