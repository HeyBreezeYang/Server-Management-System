package com.system.started.socketio;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.system.started.notification.impl.INotificationSendToWebTool;
import com.vlandc.oss.common.ConcurrentHashSet;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.notification.NotificationObject;

@Component
public class SocketIOClientManager implements INotificationSendToWebTool{
	private final static Logger logger = LoggerFactory.getLogger(SocketIOClientManager.class);

	private ConcurrentHashMap<String, ConcurrentHashSet<SocketIOClient>> userClientMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, ConcurrentHashSet<SocketIOClient>> notificationClientMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<SocketIOClient, ConcurrentHashSet<String>> clientNotificationMap = new ConcurrentHashMap<>();

	private ConcurrentHashMap<UUID, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

	/**
	 * 保存一个连接
	 */
	public void addConnection(String loginId, SocketIOClient client) {
		ConcurrentHashSet<SocketIOClient> clientSet;
		if (userClientMap.containsKey(loginId)) {
			clientSet = userClientMap.get(loginId);
		} else {
			clientSet = new ConcurrentHashSet<>();
			userClientMap.put(loginId, clientSet);
		}
		
		if (!socketIOClientMap.containsKey(client.getSessionId())) {
			clientSet.add(client);
			socketIOClientMap.put(client.getSessionId(), client);
		}
	}

	/**
	 * 注册事件对应的session
	 * 
	 * @param notificationType
	 * @param client
	 */
	public void registerNotification(String notificationType, SocketIOClient client) {
		ConcurrentHashSet<SocketIOClient> clientSet;
		if (notificationClientMap.containsKey(notificationType)) {
			clientSet = notificationClientMap.get(notificationType);
		} else {
			clientSet = new ConcurrentHashSet<>();
			notificationClientMap.put(notificationType, clientSet);
		}
		clientSet.add(client);

		ConcurrentHashSet<String> notificationSet;
		if (clientNotificationMap.containsKey(client)) {
			notificationSet = clientNotificationMap.get(client);
		} else {
			notificationSet = new ConcurrentHashSet<>();
			clientNotificationMap.put(client, notificationSet);
		}
		notificationSet.add(notificationType);
	}

	/**
	 * 取消注册事件对应的session
	 * 
	 * @param notificationType
	 * @param client
	 */
	public void unRegisterNotification(String notificationType, SocketIOClient client) {
		if (notificationClientMap.containsKey(notificationType)) {
			if (notificationClientMap.get(notificationType).contains(client)) {
				notificationClientMap.get(notificationType).remove(client);
				if (notificationClientMap.get(notificationType).size() == 0) {
					notificationClientMap.remove(notificationType);
				}
			}
		}

		if (clientNotificationMap.containsKey(client)) {
			if (clientNotificationMap.get(client).contains(notificationType)) {
				clientNotificationMap.get(client).remove(notificationType);
				if (clientNotificationMap.get(client).size() == 0) {
					clientNotificationMap.remove(client);
				}
			}
		}
	}

	/**
	 * 获取一个连接
	 */
	public ConcurrentHashSet<SocketIOClient> getConnectionSet(String loginId, String notificationType) {
		ConcurrentHashSet<SocketIOClient> resultSet = new ConcurrentHashSet<>();
		if (userClientMap.containsKey(loginId) && notificationClientMap.containsKey(notificationType)) {
			ConcurrentHashSet<SocketIOClient> userClientSet = userClientMap.get(loginId);
			ConcurrentHashSet<SocketIOClient> notificationClientSet = notificationClientMap.get(notificationType);

			Iterator<SocketIOClient> userClientIterator = userClientSet.iterator();
			while (userClientIterator.hasNext()) {
				SocketIOClient client = userClientIterator.next();
				if (notificationClientSet.contains(client)) {
					resultSet.add(client);
				}
			}
		}
		return resultSet;
	}

	public ConcurrentHashSet<SocketIOClient> getConnectionSet(String notificationType) {
		ConcurrentHashSet<SocketIOClient> resultSet = new ConcurrentHashSet<>();
		if (notificationClientMap.containsKey(notificationType)) {
			resultSet = notificationClientMap.get(notificationType);
		}
		return resultSet;
	}

	/**
	 * 客户端退出，关闭该连接
	 */
	public void removeConnection(String loginId, SocketIOClient client) throws IOException {
		if (socketIOClientMap.containsKey(client.getSessionId())) {
			socketIOClientMap.remove(client.getSessionId());
		}

		if (userClientMap.containsKey(loginId)) {
			if (userClientMap.get(loginId).contains(client)) {
				userClientMap.get(loginId).remove(client);
				if (userClientMap.get(loginId).size() == 0) {
					userClientMap.remove(loginId);
				}
			}
		}

		if (clientNotificationMap.containsKey(client)) {
			ConcurrentHashSet<String> notificationSet = clientNotificationMap.get(client);
			Iterator<String> notificationIterator = notificationSet.iterator();
			while (notificationIterator.hasNext()) {
				String notification = notificationIterator.next();
				if (notificationClientMap.containsKey(notification)) {
					if (notificationClientMap.get(notification).contains(client)) {
						notificationClientMap.get(notification).remove(client);
						if (notificationClientMap.get(notification).size() == 0) {
							notificationClientMap.remove(notification);
						}
					}
				}
			}
			clientNotificationMap.remove(client);
		}
	}

	/**
	 * 发送消息到客户端
	 */
	public void sendMessage(NotificationObject notification) throws Exception {
		ConcurrentHashSet<SocketIOClient> clientSet;
		if (notification.getRefUser().equals("ALL")) {
			clientSet = getConnectionSet(notification.getNotificationType().name());
		} else {
			clientSet = getConnectionSet(notification.getRefUser(), notification.getNotificationType().name());
		}

		Iterator<SocketIOClient> clientIterator = clientSet.iterator();
		while (clientIterator.hasNext()) {
			SocketIOClient client = clientIterator.next();
			try {
				String notificationMessage = JsonHelper.toJson(notification);
				client.sendEvent("socketioEvent", notificationMessage);
				logger.debug("send message(" + notificationMessage + ") by socketio(" + client.getSessionId() + ") client.");
			} catch (Exception e) {
				logger.error("send message by websocket error!", e);
			}
		}

	}

}