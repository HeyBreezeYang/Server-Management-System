package com.system.started.notification;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vlandc.oss.model.notification.NotificationObject;

@Component
public class NotificationManager {

	private static ConcurrentLinkedQueue<NotificationObject> notificationSendQueue = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<NotificationObject> notificationReceiveQueue = new ConcurrentLinkedQueue<>();

	@Autowired
	private List<INotificationSendListener> notificationSendListenerList ;
	
	@Autowired
	private List<INotificationReceiveListener> notificationReceiveListenerList;


	public void addNotificationSendQueue(NotificationObject notification) {
		notificationSendQueue.add(notification);
		for (int i = 0; i < notificationSendListenerList.size(); i++) {
			notificationSendListenerList.get(i).dealNotification(notification);
		}
	}

	public void addNotificationReceiveQueue(NotificationObject notification) {
		notificationReceiveQueue.add(notification);
		for (int i = 0; i < notificationReceiveListenerList.size(); i++) {
			notificationReceiveListenerList.get(i).dealNotification(notification);
		}
	}
}
