package com.system.started.rest.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.notification.NotificationManager;
import com.system.started.process.EProcessType;
import com.system.started.process.ProcessManager;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.notification.ENotificationType;
import com.vlandc.oss.model.notification.NotificationObject;

@Controller
@RequestMapping(value = "/workorders")
public class WorkOrderController extends AbstractController {
	private static Logger logger = Logger.getLogger(WorkOrderController.class);

	@Autowired
	ProcessManager processManager;

	@Autowired
	private DBService dbService;

	@Autowired
	private NotificationManager notificationManager;

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listWorkOrdersCount(@RequestParam(required = false, value = "STATUS") String status) {

		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("DEAL_STATUS_CODE", "0,1,2,4");
		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_WORKORDERS_COUNT, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listWorkOrdersCount successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listWorkOrders(@RequestParam(required = false, value = "STATUS") String status, @RequestParam(required = false, value = "KEY_WORD") String key_word, @RequestParam(required = false, value = "BEGIN_CREATE_TS") String begin_create_ts, @RequestParam(required = false, value = "END_CREATE_TS") String end_create_ts, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {

		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		if (status != null) {
			paramMap.put("DEAL_STATUS_CODE", status);
		}
		if (key_word != null) {
			paramMap.put("KEY_WORD", key_word);
		}
		if (begin_create_ts != null) {
			paramMap.put("BEGIN_CREATE_TS", begin_create_ts);
		}
		if (end_create_ts != null) {
			paramMap.put("END_CREATE_TS", end_create_ts);
		}

		parseRelationLoginIds(paramMap);

		int startNum = Integer.parseInt(start);
		int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
		HashMap<String, Object> resultMap = dbService.selectByPage(DBServiceConst.SELECT_WORKORDERS, paramMap, currentPage, Integer.parseInt(length));

		// dataTables需要将接收到的draw直接返回
		resultMap.put("draw", draw);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listWorkOrders successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/{workOrderId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listWorkOrderDetails(@PathVariable Integer workOrderId) {

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", workOrderId);

		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_WORKORDERS, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listWorkOrderDetails successful! ");
		return result;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createWorkOrder(@RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("WORKORDER_UUID", CommonUtil.createRecordNo());
		paramMap.put("CREATE_USER", getCurrentLoginId());
		paramMap.put("DEAL_STATUS_CODE", 0);

		paramMap.put("subType", paramMap.get("RESOURCE_TYPE"));

		HashMap<String, Object> resultMap = new HashMap<>();

		HashMap<String, Object> moduleTypeMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TYPE_BY_DEPARTMENT, paramMap);
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> recordListMap = (List<HashMap<String, Object>>) moduleTypeMap.get("record");
		long totalSize = (long) recordListMap.get(0).get("totalSize");
		if (totalSize == 0) {
			resultMap.put("messageStatus", "ERROR");
			resultMap.put("messageContent", "用户所在部门尚未定义工单流程！");

			String result = JsonHelper.toJson(resultMap);
			return result;
		}

		dbService.insert(DBServiceConst.INSERT_WORKORDER, paramMap);

		sendWorkOrderProcess(paramMap);

		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);

		// 发送我的工单增加通知
		NotificationObject notification = new NotificationObject();
		notification.setNotificationType(ENotificationType.WORKORDER_COUNT_ADD);
		notification.setRefUser(getCurrentLoginId());
		notificationManager.addNotificationSendQueue(notification);

		NotificationObject listAddNotification = new NotificationObject();
		listAddNotification.setNotificationType(ENotificationType.WORKORDER_LIST_ADD);
		listAddNotification.setRefUser(getCurrentLoginId());
		HashMap<String, Object> addParamMap = new HashMap<>();
		addParamMap.put("id", paramMap.get("workOrderId"));
		listAddNotification.setParameter(addParamMap);
		notificationManager.addNotificationSendQueue(listAddNotification);

		return result;
	}

	private void sendWorkOrderProcess(HashMap<String, Object> paramMap) {
		String loginId = getCurrentLoginId();
		try {
			byte[] byteArray = CommonUtil.Object2Bytes(paramMap);
			Integer processId = processManager.createProcess("工单处理", EProcessType.WORK_ORDER_CHECK, byteArray, paramMap, loginId);

			paramMap.put("processId", processId);
			dbService.insert(DBServiceConst.INSERT_WORKORDER_PROCESS_MAP, paramMap);

			logger.debug("create work order process success!");
		} catch (IOException e) {
			logger.error("create work order process error!", e);
		}
	}

	@RequestMapping(value = "/{workOrderId}", method = RequestMethod.DELETE)
	@ResponseBody
	public String closeWorkOrder(@PathVariable Integer workOrderId, @RequestParam Integer processId) {
		String loginId = getCurrentLoginId();
		processManager.closeProcess(processId, EProcessType.WORK_ORDER_CHECK, loginId);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete closeWorkOrder successful! ");
		return result;
	}

	@RequestMapping(value = "/{workOrderId}", method = RequestMethod.PUT)
	@ResponseBody
	public String updateWorkOrder(@PathVariable Integer workOrderId, @RequestParam(required = false) Integer processId, @RequestBody HashMap<String, Object> paramMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dealTs = sdf.format(new Date());
		paramMap.put("DEAL_TS", dealTs);
		paramMap.put("ID", workOrderId);
		paramMap.put("workOrderId", workOrderId);

		HashMap<String, Object> resultMap = new HashMap<>();

		paramMap.put("CREATE_USER", getCurrentLoginId());
		paramMap.put("subType", paramMap.get("RESOURCE_TYPE"));
		HashMap<String, Object> moduleTypeMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TYPE_BY_DEPARTMENT, paramMap);
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> recordListMap = (List<HashMap<String, Object>>) moduleTypeMap.get("record");
		long totalSize = (long) recordListMap.get(0).get("totalSize");
		if (totalSize == 0) {
			resultMap.put("messageStatus", "ERROR");
			resultMap.put("messageContent", "用户所在部门尚未定义工单流程！");

			String result = JsonHelper.toJson(resultMap);
			return result;
		}

		dbService.update(DBServiceConst.UPDATE_WORKORDER, paramMap);

		reSubmitWorkOrderProcess(processId, paramMap);

		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateWorkOrder successful! ");
		return result;
	}

	private void reSubmitWorkOrderProcess(Integer processId, HashMap<String, Object> paramMap) {
		String loginId = getCurrentLoginId();
		try {
			byte[] byteArray = CommonUtil.Object2Bytes(paramMap);
			HashMap<String, Object> processParamMap = new HashMap<>();
			processParamMap.put("checkContext", "工单已重新提交，等待审批！");
			processManager.reSubmitTask(processId, EProcessType.WORK_ORDER_CHECK, byteArray, processParamMap, loginId);

			logger.debug("resubmit work order process success!");
		} catch (IOException e) {
			logger.error("resubmit work order process error!", e);
		}
	}
}
