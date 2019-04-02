package com.system.started.socketio;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.vlandc.oss.common.JsonHelper;

public class SocketEventListener implements ISocketIOListener, DataListener<String> {

	@Autowired
	private SocketIOClientManager socketIOClientManager;
	
	private SocketIOServer server;

	public void setServer(SocketIOServer server) {
		this.server = server;
	}

	public void initListener() {
		server.addEventListener("socketioEvent", String.class, this);
	}

	public void onData(SocketIOClient client, String messageJson, AckRequest ackSender) throws Exception {
		HashMap<String, Object> messageMap = JsonHelper.fromJson(HashMap.class, messageJson);
		String loginId = (String) messageMap.get("loginId");
		socketIOClientManager.addConnection(loginId, client);
		String eventType = (String) messageMap.get("type");
		if (eventType.equals("REGISTER_NOTIFICATION")) {
			String notificationType = (String) messageMap.get("notificationType");
			socketIOClientManager.registerNotification(notificationType, client);
		} else if (eventType.equals("UNREGISTER_NOTIFICATION")) {
			String notificationType = (String) messageMap.get("notificationType");
			socketIOClientManager.unRegisterNotification(notificationType, client);
		}
	}
}