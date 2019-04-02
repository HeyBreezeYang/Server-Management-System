package com.system.started.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.javaconf.properties.RabbitMQConfigProperties;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.notification.NotificationObject;


@Component("ossNotificationMQClient")
public class OSSNotificationRabbitMQClient implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());  

	@Autowired
	private RabbitTemplate notificationTemplate;
	
	@Autowired
	private TopicExchange notificationExchange;
	
	@Autowired 
	private RabbitMQConfigProperties notificationProperties;

	@Autowired
	private NotificationManager notificationManager;

	@Override
	public void onMessage(Message message) {
		byte[] messageBody = message.getBody();
		Object messageObject = SerializationUtils.deserialize(messageBody);
		logger.debug("received notification from OSS_NOTIFICATION: ");

		if (messageObject instanceof NotificationObject) {
			logger.debug("received notification from OSS_NOTIFICATION: " + JsonHelper.toJson(messageObject));
			notificationManager.addNotificationReceiveQueue((NotificationObject)messageObject);
		}
	}
    
	public void send(NotificationObject notification) {
		logger.debug("send notification to OSS_NOTIFICATION: " + JsonHelper.toJson(notification));
		notificationTemplate.convertAndSend(notificationExchange.getName(),notificationProperties.getRoutekey(), notification);
	}
}
