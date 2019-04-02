package com.system.started.syslog;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.system.started.shiro.token.UserInfo;
import com.system.started.shiro.token.manager.TokenManager;
import com.system.started.util.CommonUtil;

public class WebSysLogTool {
	private final static Logger logger = LoggerFactory.getLogger(WebSysLogTool.class);

	@Value("#{client.syslog.server.master.host}")
	private static String syslogHost;
	@Value("#{client.syslog.server.master.port}")
	private static String syslogPort ;
	@Value("#{WEB_SERVER_IP}")
	private static String webServerIp;
	@Value("#{WEB_SERVER_PORT}")
	private static String webServerPort;
	
	@Autowired
	private static TokenManager tokenManager;
	
	public static SysLogObj generateLog(HttpServletRequest request) {
		try {
			SysLogObj sysLog = new SysLogObj();
			String token = request.getHeader(TokenManager.TOKEN_KEY);
			UserInfo userInfo = tokenManager.getUserInfo(token);
			if(null == userInfo){
				throw new Exception("无法获取用户信息，发送日志信息失败");
			}
			String loginId = userInfo.getLoginId();
			String mainId = userInfo.getName();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sysLog.setOPER_TIME(sdf.format(new Date()));
			sysLog.setSESSION_ID(token);
			sysLog.setOPER_ACCT(loginId);
			sysLog.setUSER_NAME(mainId);
			sysLog.setFROM_SYS("CLOUDM");

			sysLog.setSRC_IP(CommonUtil.getIpAddr(request));
			sysLog.setDEST_IP(webServerIp);
			sysLog.setDEST_PORT(webServerPort);

			return sysLog;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void sendLog(SysLogObj sysLog) {
		DatagramSocket ds = null;
		try {
			logger.debug("send syslog to 4a("+syslogHost+"-"+syslogPort+") :" + sysLog.toString());

			ds = new DatagramSocket();
			byte[] bytes = sysLog.toString().getBytes("UTF-8");
			InetAddress ia = InetAddress.getByName(syslogHost);

			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ia, Integer.parseInt(syslogPort));
			ds.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
	}
}
