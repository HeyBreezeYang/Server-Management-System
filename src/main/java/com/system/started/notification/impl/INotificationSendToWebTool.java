package com.system.started.notification.impl;

import com.vlandc.oss.model.notification.NotificationObject;

public interface INotificationSendToWebTool {
	public void sendMessage(NotificationObject notification) throws Exception;
}
