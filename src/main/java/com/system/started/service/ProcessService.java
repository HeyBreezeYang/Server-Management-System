package com.system.started.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.wrapper.VirtualActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.IDao;
import com.system.started.process.EProcessStatus;
import com.system.started.process.EProcessType;
import com.system.started.process.ProcessManager;
import com.system.started.process.engine.local.LocalProcessService;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

/**
 * @Author 没有用户名
 * @Date 2018/6/12
 * @Description
 */
@Component
public class ProcessService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(SystemService.class);

	@Autowired
	private IDao processModuleDao;

	@Autowired
	private ProcessManager processManager;

	@Autowired
	private LocalProcessService localProcessService;

	@Autowired
	private UserService userService;

	@Autowired
	private DBService dbService;

	@Autowired
	private VirtualActionWrapper virtualActionWrapper;

	public String listGtasksCount(HashMap<String, Object> paramMap) {
		try {
			String curLoginId = paramMap.getOrDefault("curLoginId", "").toString();
			paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));

			HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_GTASKS_COUNT, paramMap);
			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listGtasksCount successful! the result is :" + result);
			return result;

		} catch (Exception e) {
			logger.error("query listGtasksCount error!", e);
			return invalidRequest();
		}
	}

	public String listGtasks(HashMap<String, Object> paramMap) {
		try {
			String curLoginId = paramMap.getOrDefault("curLoginId", "").toString();
			paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
			String start = paramMap.getOrDefault("start", "0").toString();
			String length = paramMap.getOrDefault("length", "0").toString();
			String draw = paramMap.getOrDefault("draw", "").toString();
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			HashMap<String, Object> resultMap = dbService.selectByPage(DBServiceConst.SELECT_GTASKS, paramMap, currentPage, Integer.parseInt(length));
			List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					HashMap<String, Object> detailsMap = resultList.get(i);
					String type = (String) detailsMap.get("type");
					if (type.equals("LIFECYCLE_APPROVE") ||
							type.equals("VIRTUAL_ACTION_SERVER") ||
							type.equals("VIRTUAL_DELETE_SERVER")) {
						continue;
					}

					if (detailsMap.get("body") != null) {
						byte[] byteArray = (byte[]) detailsMap.get("body");
						Object body = null;
						try {
							body = CommonUtil.Bytes2Object(byteArray);
							detailsMap.put("bodyJson", JsonHelper.toJson(body));
						} catch (Exception e) {
							logger.error("Bytes2Object Error : ", e);
							detailsMap.put("bodyJson", null);
						}

						if (type.equals(EProcessType.WORK_ORDER_CHECK.toString())) {
							detailsMap.put("bodyType", EProcessType.WORK_ORDER_CHECK);
						} else {
							if (body != null) {
								detailsMap.put("bodyType", body.getClass().getSimpleName());
							} else {
								detailsMap.put("bodyJson", null);
							}
						}
					}

					// 判断当前用户是否具有权限审批该任务
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("identity", detailsMap.get("id"));
					parameters.put("loginId", curLoginId);
					List<HashMap<String, Object>> currentUserlist = dbService.directSelect(DBServiceConst.SELECT_CURRENT_GTASKS_REFUSER, parameters);
					if (currentUserlist.size() > 0) {
						detailsMap.put("approveStatus", true);
					} else {
						detailsMap.put("approveStatus", false);
					}
				}
			}
			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", draw);

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listGtasks successful! the result is : " + result);
			return result;

		} catch (Exception e) {
			logger.error("query listGtasks error!", e);
			return invalidRequest();
		}
	}

	public String listGtaskRefUsers(HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> resultMap = dbService.directSelect(DBServiceConst.SELECT_CURRENT_GTASKS_REFUSER, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcesses successful! ");

		return result;
	}

	public String listMyApplyProcessesCount(HashMap<String, Object> paramMap) {
		try {
			paramMap.put("type", "'REQUEST_APPROVE','OPERATE_APPROVE'");
			paramMap.put("status", "'" + EProcessStatus.ACCEPTED + "','" + EProcessStatus.PROCESSING + "','" + EProcessStatus.SUBMITED + "','" + EProcessStatus.SEND_BACK + "'");
			HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_MY_APPLY_PROCESSES_COUNT, paramMap);
			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listMyApplyProcessesCount successful! the result is :" + result);
			return result;
		} catch (Exception e) {
			logger.error("query listMyApplyProcessesCount error", e);
			return invalidRequest();
		}
	}

	public String listMyApplyProcesses(HashMap<String, Object> paramMap) {
		try {
			paramMap.put("type", "'REQUEST_APPROVE','OPERATE_APPROVE'");
			String start = paramMap.getOrDefault("start", "0").toString();
			String length = paramMap.getOrDefault("length", "0").toString();
			String draw = paramMap.getOrDefault("draw", "").toString();
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			HashMap<String, Object> resultMap = dbService.selectByPage(DBServiceConst.SELECT_MY_APPLY_PROCESSES, paramMap, currentPage, Integer.parseInt(length));

			List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					HashMap<String, Object> detailsMap = resultList.get(i);

					String type = (String) detailsMap.get("type");
					byte[] byteArray = (byte[]) detailsMap.get("body");
					Object body = CommonUtil.Bytes2Object(byteArray);
					detailsMap.put("bodyJson", JsonHelper.toJson(body));

					if (type.equals(EProcessType.WORK_ORDER_CHECK.toString())) {
						detailsMap.put("bodyType", EProcessType.WORK_ORDER_CHECK);
					} else {
						detailsMap.put("bodyType", body.getClass().getSimpleName());
					}
				}
			}
			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", draw);

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listMyApplyProcesses successful! ");
			return result;
		} catch (Exception e) {
			logger.error("query listMyApplyProcesses error", e);
			return invalidRequest();
		}
	}

	public String listProcesses(HashMap<String, Object> paramMap) {
		try {
			String start = paramMap.getOrDefault("start", "0").toString();
			String length = paramMap.getOrDefault("length", "0").toString();
			String draw = paramMap.getOrDefault("draw", "").toString();
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			HashMap<String, Object> resultMap = dbService.selectByPage(DBServiceConst.SELECT_PROCESSES, paramMap, currentPage, Integer.parseInt(length));

			List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					HashMap<String, Object> detailsMap = resultList.get(i);

					String type = (String) detailsMap.get("type");
					if (type.equals("LIFECYCLE_APPROVE") ||
							type.equals("VIRTUAL_ACTION_SERVER") ||
							type.equals("VIRTUAL_DELETE_SERVER")) {
						continue;
					}

					byte[] byteArray = (byte[]) detailsMap.get("body");
					Object body = null;
					try {
						body = CommonUtil.Bytes2Object(byteArray);
						detailsMap.put("bodyJson", JsonHelper.toJson(body));
					} catch (Exception e) {
						logger.error("Bytes2Object Error : ", e);
						detailsMap.put("bodyJson", null);
					}


					if (type.equals(EProcessType.WORK_ORDER_CHECK.toString())) {
						detailsMap.put("bodyType", EProcessType.WORK_ORDER_CHECK);
					} else {
						if (body != null) {
							detailsMap.put("bodyType", body.getClass().getSimpleName());
						} else {
							detailsMap.put("bodyType", null);
						}
					}
				}
			}

			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", draw);

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listProcesses successful! ");
			return result;
		} catch (Exception e) {
			logger.debug("query listProcesses error! ", e);
			return invalidRequest();
		}
	}

	public String listRequestProcesseResult(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_PROCESSE_REQUEST_RESULT, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listRequestProcesseResult successful! ");
		return result;
	}

	public String listProcesseDetails(HashMap<String, Object> paramMap) {
		try {
			String curLoginId = paramMap.getOrDefault("curLoginId", "").toString();
			paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
			List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_GTASKS, paramMap);

			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					HashMap<String, Object> detailsMap = resultList.get(i);
					String type = (String) detailsMap.get("type");
					if (type.equals("LIFECYCLE_APPROVE") ||
							type.equals("VIRTUAL_ACTION_SERVER") ||
							type.equals("VIRTUAL_DELETE_SERVER")) {
						continue;
					}

					if (detailsMap.get("body") != null) {
						byte[] byteArray = (byte[]) detailsMap.get("body");
						Object body = null;
						try {
							body = CommonUtil.Bytes2Object(byteArray);
							detailsMap.put("bodyJson", JsonHelper.toJson(body));
						} catch (Exception e) {
							logger.error("Bytes2Object Error : ", e);
							detailsMap.put("bodyJson", null);
						}

						if (type.equals(EProcessType.WORK_ORDER_CHECK.toString())) {
							detailsMap.put("bodyType", EProcessType.WORK_ORDER_CHECK);
						} else {
							if (body != null) {
								detailsMap.put("bodyType", body.getClass().getSimpleName());
							} else {
								detailsMap.put("bodyJson", null);
							}
						}
					}
				}
			}

			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put("record", resultList);
			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listProcesseDetails successful! ");
			return result;
		} catch (Exception e) {
			logger.error("query listProcesseDetails error", e);
			return invalidRequest();
		}
	}

	public String listProcesseTasks(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_PROCESS_TASKS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcesseTasks successful! ");
		return result;
	}

	public String closeRequestProcess(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		Integer processId = Integer.getInteger(paramMap.getOrDefault("processId", "").toString());
		processManager.closeProcess(processId, EProcessType.REQUEST_APPROVE, loginId);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete closeRequestProcess successful! ");
		return result;
	}

	public String reSubmitRequestProcess(HashMap<String, Object> paramMap) {
		Integer processId = Integer.getInteger(paramMap.getOrDefault("processId", "").toString());
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		try {
			Object bodyObject = parseBody(processId, paramMap.get("bodyJson"));
			if (bodyObject == null) {
				return invalidRequest();
			}
			byte[] byteArray = CommonUtil.Object2Bytes(bodyObject);

			HashMap<String, Object> processParamMap = new HashMap<>();
			processParamMap.put("checkContext", "申请已重新提交，等待审批！");
			processManager.reSubmitTask(processId, EProcessType.REQUEST_APPROVE, byteArray, processParamMap, loginId);

			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put("messageStatus", "END");
			String result = JsonHelper.toJson(resultMap);
			logger.debug("update reSubmitInstance successful! ");
			return result;
		} catch (Exception e) {
			logger.debug("update reSubmitInstance error e! ");
			return invalidRequest();
		}
	}

	private Object parseBody(Integer processId, Object bodyJsonParam) throws Exception {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("processId", processId);
		List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_DETAILS, parameter);
		if (resultList != null && resultList.size() > 0) {
			HashMap<String, Object> detailsMap = resultList.get(0);

			byte[] byteArray = (byte[]) detailsMap.get("body");
			Object body = CommonUtil.Bytes2Object(byteArray);

			Class<?> bodyClass = body.getClass();
			HashMap<String, Object> bodyJsonParamMap = (HashMap<String, Object>) bodyJsonParam;
			return parseParamMap(bodyClass, bodyJsonParamMap);
		}
		return null;
	}

	private Object parseParamMap(Class<?> bodyClass, HashMap<String, Object> paramMap) throws Exception {
		Method[] bodyMethodsArray = bodyClass.getMethods();
		Object bodyObject = bodyClass.newInstance();

		if (bodyClass.equals(HashMap.class) || bodyClass.equals(LinkedHashMap.class)) {
			return paramMap;
		}
		for (int i = 0; i < bodyMethodsArray.length; i++) {
			try {
				Method methodItem = bodyMethodsArray[i];
				String methodName = methodItem.getName();
				if (!methodName.startsWith("set")) {
					continue;
				}
				// 找到 set 方法
				String subMethodName = methodName.substring(3);
				String fieldName = subMethodName.substring(0, 1).toLowerCase() + subMethodName.substring(1);
				if (paramMap.containsKey(fieldName)) {
					Object paramValue = paramMap.get(fieldName);

					Class<?> methodParamClass = methodItem.getParameterTypes()[0];
					if (methodParamClass.isEnum()) {
						// 如果是枚举类型
						Method paramEmnuMethod = methodParamClass.getMethod("valueOf", String.class);
						Object paramObject = paramEmnuMethod.invoke(methodParamClass, paramValue);
						methodItem.invoke(bodyObject, paramObject);
					} else if (methodParamClass.isPrimitive()) { // 判断是否基础类型
						if (methodParamClass.getName().equals("int")) {
							methodItem.invoke(bodyObject, Integer.parseInt(String.valueOf(paramValue)));
						} else {
							methodItem.invoke(bodyObject, paramValue);
						}
					} else if (methodParamClass.equals(Integer.class)) {
						methodItem.invoke(bodyObject, Integer.parseInt(String.valueOf(paramValue)));
					} else if (methodParamClass.equals(Long.class)) {
						methodItem.invoke(bodyObject, Long.valueOf(String.valueOf(paramValue)));
					} else if (methodParamClass.equals(Boolean.class)) {
						methodItem.invoke(bodyObject, Boolean.valueOf(String.valueOf(paramValue)));
					} else if (paramValue instanceof HashMap) {
						Field fieldItem = null;
						try {
							fieldItem = bodyClass.getField(fieldName);
						} catch (Exception e) {
							fieldItem = bodyClass.getDeclaredField(fieldName);
						}

						Class<?> paramClass = fieldItem.getType();
						Object paramObject = null;
						if (paramClass.equals(HashMap.class) || paramClass.equals(Map.class)) {
							paramObject = paramValue;
						} else {
							paramObject = parseParamMap(paramClass, (HashMap<String, Object>) paramValue);
						}
						methodItem.invoke(bodyObject, paramObject);
					} else if (paramValue instanceof List) {
						List<Object> paramObject = new ArrayList<>();
						// 获得泛型对应的类
						Field fieldItem = null;
						try {
							fieldItem = bodyClass.getField(fieldName);
						} catch (Exception e) {
							fieldItem = bodyClass.getDeclaredField(fieldName);
						}
						Type fieldGenericType = fieldItem.getGenericType();
						if (fieldGenericType instanceof ParameterizedType) // 如果是泛型参数的类型
						{
							ParameterizedType pt = (ParameterizedType) fieldGenericType;
							Class<?> fieldGenericClass = (Class<?>) pt.getActualTypeArguments()[0]; // 得到泛型里的class类型对象。

							List<Object> paramValuObject = List.class.cast(paramValue);
							for (int j = 0; j < paramValuObject.size(); j++) {
								Object paramValueItem = paramValuObject.get(j);

								Object paramObjectItem = parseParamMap(fieldGenericClass, (HashMap<String, Object>) paramValueItem);
								paramObject.add(paramObjectItem);
							}
							methodItem.invoke(bodyObject, paramObject);
						} else if (fieldGenericType.getTypeName().equals("java.lang.String[]")) {
							List<Object> paramValuObject = List.class.cast(paramValue);
							Object paramValueArray = paramValuObject.toArray(new String[0]);
							methodItem.invoke(bodyObject, paramValueArray);
						} else {
							methodItem.invoke(bodyObject, paramObject);
						}
					} else {
						methodItem.invoke(bodyObject, paramValue);
					}
				}
			} catch (Exception e) {
				logger.error("parseParamMap error!", e);
			}
		}
		return bodyObject;
	}

	public String checkTask(HashMap<String, Object> paramMap) {
		localProcessService.doCheckTask(paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update checkTask successful! ");
		return result;
	}

	public String listProcessResourceRequests(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = null;
		String start = paramMap.getOrDefault("start", "0").toString();
		String length = paramMap.getOrDefault("length", "0").toString();
		String draw = paramMap.getOrDefault("draw", "").toString();
		if (start != null && !length.equals("-1")) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_PROCESS_RESOURCE_REQUESTS, paramMap, currentPage, Integer.parseInt(length));
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_PROCESS_RESOURCE_REQUESTS, paramMap);
		}

		List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				HashMap<String, Object> detailsMap = resultList.get(i);

				byte[] requestByteArray = (byte[]) detailsMap.get("request");
				String requestbody = new String(requestByteArray);

				byte[] responseByteArray = (byte[]) detailsMap.get("response");
				String responsebody = new String(responseByteArray);

				detailsMap.put("requestJson", JsonHelper.toJson(requestbody));
				detailsMap.put("responseJson", JsonHelper.toJson(responsebody));
			}
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcessResourceRequests successful! the result is :" + result);
		return result;
	}

	public String listProcesseModules(HashMap<String, Object> paramMap) {
		String start = paramMap.getOrDefault("start", "0").toString();
		String length = paramMap.getOrDefault("length", "0").toString();
		String draw = paramMap.getOrDefault("draw", "").toString();
		int startNum = Integer.parseInt(start);
		int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
		HashMap<String, Object> resultMap = dbService.selectByPage(DBServiceConst.SELECT_PROCESS_MODULES, paramMap, currentPage, Integer.parseInt(length));
		resultMap.put("draw", draw);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcesseModules successful! ");

		return result;
	}

	public String listProcesseModuleTypes(HashMap<String, Object> paramMap) {
		String start = paramMap.getOrDefault("start", "0").toString();
		String length = paramMap.getOrDefault("length", "0").toString();
		String draw = paramMap.getOrDefault("draw", "").toString();
		HashMap<String, Object> resultMap = null;

		if (start != null && !length.equals("-1")) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_PROCESS_MODULE_TYPES, paramMap, currentPage, Integer.parseInt(length));
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TYPES, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcesseModuleTypes successful! ");

		return result;
	}

	public String listProcesseModuleTypeDepartments(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TYPE_DEPARTMENTS, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcesseModuleTypeDepartments successful! ");

		return result;
	}

	public String createProcesseModule(HashMap<String, Object> paramMap) {
		processModuleDao.insert(paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createProcesseModule successful! ");

		return result;
	}

	public String updateProcesseModule(HashMap<String, Object> paramMap) {
		processModuleDao.update(paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert updateProcesseModule successful! ");
		return result;
	}

	public String deleteProcesseModule(HashMap<String, Object> paramMap) {
		//		processModuleDao.delete(paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TASK_DETAIL, paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TASK, paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE_TYPE, paramMap);
		dbService.delete(DBServiceConst.DELETE_PROCESS_MODULE, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete ProcesseModule successful! ");

		return result;
	}

	public String updateProcesseModuleType(HashMap<String, Object> paramMap){
		dbService.update(DBServiceConst.UPDATE_PROCESS_MODULE_TYPE, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update ProcesseModule successful! ");

		return result;
	}

	public String listProcesseModuleTasks(HashMap<String, Object> paramMap){
		HashMap<String, Object> moduleTaskMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TASKS, paramMap);
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> moduleTaskList = (List<HashMap<String, Object>>) moduleTaskMap.get("record");
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < moduleTaskList.size(); i++) {
			HashMap<String, Object> taskMap = moduleTaskList.get(i);
			int taskId = (int) taskMap.get("id");

			HashMap<String, Object> taskDetailsParamMap = new HashMap<String, Object>();
			taskDetailsParamMap.put("id", taskId);

			HashMap<String, Object> taskDetailsMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TASK_DETAILS, taskDetailsParamMap);
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> taskDetailsList = (List<HashMap<String, Object>>) taskDetailsMap.get("record");
			taskMap.put("detail", taskDetailsList);

			list.add(taskMap);
		}

		moduleTaskMap.put("record", list);

		String result = JsonHelper.toJson(moduleTaskMap);
		logger.debug("query listProcesseModuleTasks successful! ");

		return result;
	}

	public String listProcesseModuleTaskInstances(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TASK_INSTANCE_LIST, paramMap);
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> item : list) {
			String type = (String) item.get("type");
			if (type.equals("SUBMIT")) {
				continue;
			}

			HashMap<String, Object> parameters = new HashMap<>();
			parameters.put("instanceId", item.get("id"));

			List<HashMap<String, Object>> userList = dbService.directSelect(DBServiceConst.SELECT_PROCESS_MODULE_TASK_INSTANCE_REFUSER, parameters);
			item.put("refUsers", userList);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcesseModuleTaskInstances successful! ");

		return result;
	}

	public String listProcessModuleTypeCount(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_PROCESS_MODULE_TYPE_BY_DEPARTMENT, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listProcessModuleTypeCount successful! ");

		return result;
	}

	public String createStack(HashMap<String, Object> paramMap){
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			List<HashMap<String, Object>> stacks = (List<HashMap<String, Object>>) paramMap.get("stacks");
			for (HashMap<String, Object> stackMap : stacks) {
				String stackProjectId = (String) stackMap.get("projectId");
				HashMap<String, Object> stackParamMap = (HashMap<String, Object>) stackMap.get("stackParameter");
				stackParamMap.put("approveId", stackMap.get("approveId"));
				virtualActionWrapper.doExcutionAction(regionName, loginId, stackProjectId, EAction.VIRTUAL_CREATE_STACK, stackParamMap);
			}
			Result result = new Result();
			result.setResultCode(EResultCode.SUCCESS);
			result.setResultMsg("操作成功");
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listProcessRelationStacks(HashMap<String, Object> paramMap){
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_PROCESSE_RELATION, paramMap);
		try {
			List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					HashMap<String, Object> detailsMap = resultList.get(i);

					String type = (String) detailsMap.get("type");
					byte[] byteArray = (byte[]) detailsMap.get("body");
					Object body = CommonUtil.Bytes2Object(byteArray);
					detailsMap.put("bodyJson", JsonHelper.toJson(body));

					if (type.equals(EProcessType.WORK_ORDER_CHECK.toString())) {
						detailsMap.put("bodyType", EProcessType.WORK_ORDER_CHECK);
					} else {
						detailsMap.put("bodyType", body.getClass().getSimpleName());
					}
				}
			}
			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listProcessRelationStacks successful! ");

			return result;
		} catch (Exception e) {
			logger.error("query listProcessRelationStacks error!", e);
			return invalidRequest();
		}
	}

	public String createProcessOperate(HashMap<String, Object> paramMap){
		dbService.insert(DBServiceConst.INSERT_PROCESS_OPERATE, paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		return result;
	}
}
