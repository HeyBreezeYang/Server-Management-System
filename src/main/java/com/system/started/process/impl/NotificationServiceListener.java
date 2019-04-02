package com.system.started.process.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.system.started.cache.impl.UserCache;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.notification.NotificationManager;
import com.system.started.process.EProcessStatus;
import com.system.started.process.EProcessType;
import com.vlandc.oss.common.CommonTools;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.notification.ENotificationType;
import com.vlandc.oss.model.notification.NotificationObject;


public class NotificationServiceListener implements IProcessListener {

	private static Logger logger = Logger.getLogger(NotificationServiceListener.class);

	@Autowired
	private NotificationManager notificationManager;

	@Autowired
	private DBService dbService;

	@Autowired
	private UserCache userCache;

	@Override
	public void initLisenter() {

	}

	@Override
	public void dealPorcess(HashMap<String, Object> processItemMap, EProcessStatus status, String statusResult, HashMap<String, Object> paramMap) {
		HashSet<String> processAddUserSet = new HashSet<>();
		HashSet<String> processReduceUserSet = new HashSet<>();
		HashSet<String> processChangeUserSet = new HashSet<>();

		HashSet<String> workOrderReduceUserSet = new HashSet<>();
		HashSet<String> workOrderChangeUserSet = new HashSet<>();

		HashSet<String> myApplyReduceUserSet = new HashSet<>();
		HashSet<String> myApplyChangeUserSet = new HashSet<>();

		HashMap<String, Object> parameter = new HashMap<>();
		Integer processId = (Integer) processItemMap.get("id");
		String createUser = (String) processItemMap.get("createUser");
		String processType = (String) processItemMap.get("type");
		String identity = (String) processItemMap.get("identity");
		parameter.put("identity", identity);

		if (status.equals(EProcessStatus.SEND_BACK) 
				|| status.equals(EProcessStatus.ACCEPTED) 
				|| status.equals(EProcessStatus.PROCESSING) 
				|| status.equals(EProcessStatus.SUCCESS_END) 
				|| status.equals(EProcessStatus.CLOSE)) {
			// 获取上一步涉及的相关人员，对应的待办任务减1
			// 流程处理完毕，最后一步审批人对应的待办任务减1
			List<HashMap<String, Object>> refUserList = dbService.directSelect(DBServiceConst.SELECT_PRE_GTASKS_REFUSER, parameter);
			for (int i = 0; i < refUserList.size(); i++) {
				HashMap<String, Object> refUserItem = refUserList.get(i);
				String checkUser = (String) refUserItem.get("checkUser");

				String relationLoginIdS = userCache.getSubRelationLoginIds(checkUser);
				parameter.put("loginId", relationLoginIdS);

				processReduceUserSet.add(checkUser);
			}
		}

		if (!status.equals(EProcessStatus.SUCCESS_END) && !status.equals(EProcessStatus.SEND_BACK)) {
			// 获取当前步骤涉及的相关人员，对应的待办任务加1
			List<HashMap<String, Object>> refUserList = dbService.directSelect(DBServiceConst.SELECT_CURRENT_GTASKS_REFUSER, parameter);
			for (int i = 0; i < refUserList.size(); i++) {
				HashMap<String, Object> refUserItem = refUserList.get(i);
				String checkUser = (String) refUserItem.get("checkUser");
				if (processReduceUserSet.contains(checkUser)) { // 又增加又减少，不发送增加/减少通知，但是发送状态变化通知
					processReduceUserSet.remove(checkUser);
					processChangeUserSet.add(checkUser);
				} else {
					processAddUserSet.add(checkUser);
				}

				String relationLoginIdS = userCache.getSubRelationLoginIds(checkUser);
				parameter.put("loginId", relationLoginIdS);
			}
		}

		// 流程审批完成/关闭，根据流程类型，判断放入具体的统计信息中
		if (status.equals(EProcessStatus.SUCCESS_END) || status.equals(EProcessStatus.CLOSE)) {
			if (processType.equals("WORK_ORDER_CHECK")) {
				workOrderReduceUserSet.add(createUser);
			} else if (processType.equals("REQUEST_APPROVE")) {
				myApplyReduceUserSet.add(createUser);
			}
		} else if(status.equals(EProcessStatus.ACCEPTED) || status.equals(EProcessStatus.PROCESSING)|| status.equals(EProcessStatus.SEND_BACK)){
			if (processType.equals("WORK_ORDER_CHECK")) {
				workOrderChangeUserSet.add(createUser);
			} else if (processType.equals("REQUEST_APPROVE")) {
				myApplyChangeUserSet.add(createUser);
			}
		}

		logger.debug("the process reduce user hashSet is :" + processReduceUserSet);
		logger.debug("the process add user hashSet is :" + processAddUserSet);
		logger.debug("the process change user hashSet is :" + processChangeUserSet);

		for (String userKey : processReduceUserSet) {
			NotificationObject notification = new NotificationObject();
			notification.setNotificationType(ENotificationType.PROCESS_COUNT_REDUCE);
			notification.setRefUser(userKey);
			notificationManager.addNotificationSendQueue(notification);

			// 发送流程列表删除通知
			NotificationObject listReduceNotification = new NotificationObject();
			listReduceNotification.setNotificationType(ENotificationType.PROCESS_LIST_REDUCE);
			listReduceNotification.setRefUser(userKey);
			HashMap<String, Object> reduceParamMap = new HashMap<>();
			reduceParamMap.put("id", processId);
			listReduceNotification.setParameter(reduceParamMap);
			notificationManager.addNotificationSendQueue(listReduceNotification);
		}

		for (String userKey : processAddUserSet) {
			NotificationObject notification = new NotificationObject();
			notification.setNotificationType(ENotificationType.PROCESS_COUNT_ADD);
			notification.setRefUser(userKey);
			notificationManager.addNotificationSendQueue(notification);

			NotificationObject listAddNotification = new NotificationObject();
			listAddNotification.setNotificationType(ENotificationType.PROCESS_LIST_ADD);
			listAddNotification.setRefUser(userKey);
			HashMap<String, Object> addParamMap = new HashMap<>();
			addParamMap.put("id", processId);
			listAddNotification.setParameter(addParamMap);
			notificationManager.addNotificationSendQueue(listAddNotification);
		}

		for (String userKey : processChangeUserSet) {
			HashMap<String, Object> currentProcessItemMap = null;
			String relationLoginIdS = userCache.getSubRelationLoginIds(userKey);
			parameter.put("loginId", relationLoginIdS);
			parameter.put("processId", processId);
			List<HashMap<String, Object>> processItemList = dbService.directSelect(DBServiceConst.SELECT_GTASKS, parameter);
			if (processItemList != null && processItemList.size() > 0) {
				currentProcessItemMap = processItemList.get(0);
			}

			NotificationObject listChangeNotification = new NotificationObject();
			listChangeNotification.setNotificationType(ENotificationType.PROCESS_LIST_CHANGE);
			listChangeNotification.setRefUser(userKey);
			listChangeNotification.setParameter(currentProcessItemMap);
			notificationManager.addNotificationSendQueue(listChangeNotification);
		}

		for (String userKey : workOrderReduceUserSet) {
			NotificationObject notification = new NotificationObject();
			notification.setNotificationType(ENotificationType.WORKORDER_COUNT_REDUCE);
			notification.setRefUser(userKey);
			notificationManager.addNotificationSendQueue(notification);

			// 发送我的工单列表删除通知
			List<HashMap<String, Object>> workOrderItemList = dbService.directSelect(DBServiceConst.SELECT_WORKORDERS, parameter);
			if (workOrderItemList != null && workOrderItemList.size() > 0) {
				HashMap<String, Object> currentWorkOrderItemMap = workOrderItemList.get(0);
				Integer workOrderId = (Integer) currentWorkOrderItemMap.get("ID");
				NotificationObject listReduceNotification = new NotificationObject();
				listReduceNotification.setNotificationType(ENotificationType.WORKORDER_LIST_REDUCE);
				listReduceNotification.setRefUser(userKey);
				HashMap<String, Object> reduceParamMap = new HashMap<>();
				reduceParamMap.put("id", workOrderId);
				listReduceNotification.setParameter(reduceParamMap);
				notificationManager.addNotificationSendQueue(listReduceNotification);
			}
		}

		for (String userKey : workOrderChangeUserSet) {
				HashMap<String, Object> currentWorkOrderItemMap = null;
				String relationLoginIdS = userCache.getSubRelationLoginIds(userKey);
				parameter.put("loginId", relationLoginIdS);
				List<HashMap<String, Object>> workOrderItemList = dbService.directSelect(DBServiceConst.SELECT_WORKORDERS, parameter);
				if (workOrderItemList != null && workOrderItemList.size() > 0) {
					currentWorkOrderItemMap = workOrderItemList.get(0);
				}
				NotificationObject listChangeNotification = new NotificationObject();
				listChangeNotification.setNotificationType(ENotificationType.WORKORDER_LIST_CHANGE);
				listChangeNotification.setRefUser(userKey);
				listChangeNotification.setParameter(currentWorkOrderItemMap);
				notificationManager.addNotificationSendQueue(listChangeNotification);
		}

		for (String userKey : myApplyReduceUserSet) {
			NotificationObject notification = new NotificationObject();
			notification.setNotificationType(ENotificationType.MYAPPLY_COUNT_REDUCE);
			notification.setRefUser(userKey);
			notificationManager.addNotificationSendQueue(notification);

			// 发送我的申请列表删除通知
			NotificationObject listReduceNotification = new NotificationObject();
			listReduceNotification.setNotificationType(ENotificationType.MYAPPLY_LIST_REDUCE);
			listReduceNotification.setRefUser(userKey);
			HashMap<String, Object> reduceParamMap = new HashMap<>();
			reduceParamMap.put("id", processId);
			listReduceNotification.setParameter(reduceParamMap);
			notificationManager.addNotificationSendQueue(listReduceNotification);
		}

		for (String userKey : myApplyChangeUserSet) {
			try {
				HashMap<String, Object> currentProcessItemMap = null;
				String relationLoginIdS = userCache.getSubRelationLoginIds(userKey);
				parameter.put("loginId", relationLoginIdS);
				List<HashMap<String, Object>> processItemList = dbService.directSelect(DBServiceConst.SELECT_MY_APPLY_PROCESSES, parameter);
				if (processItemList != null && processItemList.size() > 0) {
					currentProcessItemMap = processItemList.get(0);

					String type = (String) currentProcessItemMap.get("type");
					byte[] byteArray = (byte[]) currentProcessItemMap.get("body");
					Object body = CommonTools.bytesToObject(byteArray);
					currentProcessItemMap.put("bodyJson", JsonHelper.toJson(body));

					if (type.equals(EProcessType.WORK_ORDER_CHECK.toString())) {
						currentProcessItemMap.put("bodyType", EProcessType.WORK_ORDER_CHECK);
					} else {
						currentProcessItemMap.put("bodyType", body.getClass().getSimpleName());
					}
				}
				NotificationObject listChangeNotification = new NotificationObject();
				listChangeNotification.setNotificationType(ENotificationType.MYAPPLY_LIST_CHANGE);
				listChangeNotification.setRefUser(userKey);
				listChangeNotification.setParameter(currentProcessItemMap);
				notificationManager.addNotificationSendQueue(listChangeNotification);
			} catch (Exception e) {
				logger.error("create myapply change(userKey:" + userKey + ")(identity:" + identity + ") notification error!", e);
			}
		}
	}

}
