package com.system.started.rest.controller;

import java.util.ArrayList;
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

import com.system.started.action.wrapper.OperationActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.service.TaskManageService;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Controller
@RequestMapping(value = "/operationService")
public class OperationController extends AbstractController {
	private static Logger logger = Logger.getLogger(OperationController.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private TaskManageService taskManageService;

	@Autowired
	private OperationActionWrapper operationActionWrapper;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceCatalog() {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		logger.debug("the listOperationServiceCatalog param is :" + JsonHelper.toJson(paramMap));
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_CATALOGS, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceCatalog successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createOperationServiceCatalog(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		logger.debug("the createOperationServiceCatalog param is :" + JsonHelper.toJson(paramMap));
		dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_CATALOG, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createOperationServiceCatalog successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/{catalogId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateOperationServiceCatalog(@PathVariable Integer catalogId, @RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);

		paramMap.put("id", catalogId);
		logger.debug("the updateOperationServiceCatalog param is :" + JsonHelper.toJson(paramMap));
		dbService.insert(DBServiceConst.UPDATE_OPERATION_SERVICE_CATALOG, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert updateOperationServiceCatalog successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/{catalogId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteOperationServiceCatalog(@PathVariable Integer catalogId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("catalogId", catalogId);
		parseCurrentLoginIds(paramMap);
		logger.debug("the deleteOperationServiceCatalog paramMap is :" + JsonHelper.toJson(paramMap));
		dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_CATALOG, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteOperationServiceCatalog successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/templates", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTemplates(@RequestParam(required = false) String name, @RequestParam(required = false) Integer templateId, @RequestParam(required = false) Integer catalogId, @RequestParam(required = false) String type, @RequestParam(required = false) Integer isDefault, @RequestParam(required = false) String createDateStart, @RequestParam(required = false) String createDateEnd, @RequestParam(required = false) String draw,
			@RequestParam(required = false) String start, @RequestParam(required = false) String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (name != null) {
			paramMap.put("name", name);
		}

		if (templateId != null) {
			paramMap.put("templateId", templateId);
		}

		if (catalogId != null) {
			paramMap.put("catalogId", catalogId);
		}

		if (type != null) {
			paramMap.put("type", type);
		}

		if (isDefault != null) {
			paramMap.put("isDefault", isDefault);
		}

		if (createDateStart != null) {
			paramMap.put("createDateStart", createDateStart);
		}

		if (createDateEnd != null) {
			paramMap.put("createDateEnd", createDateEnd);
		}

		parseRelationLoginIds(paramMap);
		logger.debug("the listOperationServiceTemplates paramMap is :" + JsonHelper.toJson(paramMap));

		HashMap<String, Object> resultMap = null;
		if (start != null && !length.equals("-1")) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_OPERATION_SERVICE_TEMPLATES, paramMap, currentPage, Integer.parseInt(length));
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TEMPLATES, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTemplates successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/template/{templateId}/source", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTemplateSources(@PathVariable Integer templateId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("templateId", templateId);
		parseRelationLoginIds(paramMap);
		logger.debug("the listOperationServiceTemplateSources paramMap is :" + JsonHelper.toJson(paramMap));

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TEMPLATE_SOURCES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTemplateSources successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/task/template/{templateId}/source", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskTemplateSources(@PathVariable Integer templateId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("templateId", templateId);
		parseRelationLoginIds(paramMap);
		logger.debug("the listOperationServiceTaskTemplateSources paramMap is :" + JsonHelper.toJson(paramMap));

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_TEMPLATE_SOURCES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTaskTemplateSources successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/templates", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createOperationServiceTemplate(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		logger.debug("the createOperationServiceTemplate paramMap is :" + JsonHelper.toJson(paramMap));

		dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_TEMPLATE, paramMap);

		List<HashMap<String, Object>> scriptDataList = (List<HashMap<String, Object>>) paramMap.get("scriptDatas");
		for (int i = 0; i < scriptDataList.size(); i++) {
			HashMap<String, Object> parameter = scriptDataList.get(i);
			parameter.put("refTemplateId", paramMap.get("refTemplateId"));

			String systemTypes = (String) parameter.get("systemTypes");
			for (String systemType : systemTypes.split(",")) {
				parameter.put("systemType", systemType);
				dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_TEMPLATE_SOURCE, parameter);
			}
		}

		if (taskManageService.catalogId == Integer.parseInt(paramMap.get("catalogId").toString())) {
			logger.debug("add operationServiceTemplate into Quartz !");
			paramMap.put("id", paramMap.get("refTemplateId"));
			taskManageService.addTasks2Quartz(paramMap);
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createOperationServiceTemplate successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/templates/{templateId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateOperationServiceTemplate(@PathVariable Integer templateId, @RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("id", templateId);
		logger.debug("the updateOperationServiceTemplate paramMap is :" + JsonHelper.toJson(paramMap));

		dbService.update(DBServiceConst.UPDATE_OPERATION_SERVICE_TEMPLATE, paramMap);

		List<HashMap<String, Object>> scriptDataList = (List<HashMap<String, Object>>) paramMap.get("scriptDatas");
		if (scriptDataList != null) {
			paramMap.put("refTemplateId", templateId);
			dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_TEMPLATE_SOURCE, paramMap);

			for (int i = 0; i < scriptDataList.size(); i++) {
				HashMap<String, Object> parameter = scriptDataList.get(i);
				parameter.put("refTemplateId", templateId);
				dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_TEMPLATE_SOURCE, parameter);
			}
		}

		if (taskManageService.catalogId == Integer.parseInt(paramMap.get("catalogId").toString())) {
			logger.debug("update operationServiceTemplate into Quartz !");
			taskManageService.updateTask(paramMap);
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert updateOperationServiceTemplate successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/templates/{templateId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteOperationServiceTemplate(@PathVariable Integer templateId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("templateId", templateId);

		List<HashMap<String, Object>> hashMapList = dbService.directSelect(DBServiceConst.SELECT_OPERATION_SERVICE_TEMPLATES, paramMap);
		for (HashMap<String, Object> hashMap : hashMapList) {
			if (taskManageService.catalogId == Integer.parseInt(hashMap.get("catalogId").toString())) {
				logger.debug("update operationServiceTemplate into Quartz !");
				taskManageService.deleteTask(templateId, hashMap.get("expression").toString());
			}
		}
		parseCurrentLoginIds(paramMap);
		logger.debug("the deleteOperationServiceTemplate paramMap is :" + JsonHelper.toJson(paramMap));
		dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_TEMPLATE, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteOperationServiceTemplate successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String startOperationServiceTask(@RequestParam(required = true, value = "actionType") String actionType, @RequestBody HashMap<String, Object> paramMap) {
		try {
			String loginId = getCurrentLoginId();
			HashMap<String, Object> actionParamMap = new HashMap<>();
			actionParamMap.putAll(paramMap);
			actionParamMap.put("accountId", loginId);
			EAction operationActionType = EAction.OPERATE_ACTION_TYPE_SERVICE;
			if (actionType.equals("DIRECT")) {
				operationActionType = EAction.OPERATE_ACTION_TYPE_DIRECT;
			}

			// 创建定时运维任务
			if (actionParamMap.containsKey("expression")) {
				HashMap<String, Object> timingTask = new HashMap<String, Object>();
				timingTask.put("name", actionParamMap.get("name"));
				timingTask.put("serviceArray", JsonHelper.toJson(actionParamMap.get("serviceList")));
				timingTask.put("minionArray", JsonHelper.toJson(actionParamMap.get("minionArray")));
				timingTask.put("status", 1);
				timingTask.put("expression", JsonHelper.toJson(actionParamMap.get("expression")));
				timingTask.put("createDate", new Date());
				timingTask.put("createUser", loginId);
				actionParamMap.put("timingTask", timingTask);

				operationActionType = EAction.OPERATE_CREATE_TIMED_TASK;
			}

			List<HashMap<String, Object>> minionMap = null;
			if (actionParamMap.get("minionArray") instanceof String) {
				minionMap = JsonHelper.fromJson(List.class, (String) actionParamMap.get("minionArray"));
				actionParamMap.put("minionArray", minionMap);
			} else {
				minionMap = (List<HashMap<String, Object>>) actionParamMap.get("minionArray");
			}
			List<String> minionList = new ArrayList<String>();
			for (HashMap<String, Object> minionObj : minionMap) {
				if (minionObj.containsKey("agentId") && !minionObj.get("agentId").toString().equals("")) {
					minionList.add(minionObj.get("agentId").toString());
				}
			}

			if (actionParamMap.get("serviceList") instanceof String) {
				actionParamMap.put("serviceList", JsonHelper.fromJson(List.class, (String) actionParamMap.get("serviceList")));
			}

			Result result = operationActionWrapper.doExcutionAction(loginId, operationActionType, actionParamMap, minionList.toArray(new String[0]));// FIXME EAction改为自动化运维

			// 如果是未经审批直接创建的定时任务，需要将该定时任务添加到任务队列中去
			if (result.getResultCode().equals(EResultCode.SUCCESS) && operationActionType.equals(EAction.OPERATE_CREATE_TIMED_TASK)) {
				HashMap<String, Object> resultObj = result.getResultObj().get(0);
				if (!resultObj.containsKey("id") || resultObj.get("id") == null) {
					logger.error("创建未经审批的定时任务失败！");
					throw new Exception("未创建定时任务");
				}
				actionParamMap.put("id", resultObj.get("id"));
				this.taskManageService.addTasks2Quartz(actionParamMap);
			}

			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	@RequestMapping(value = "/tasks/{taskId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateOperationServiceTimingTask(@PathVariable Integer taskId, @RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		HashMap<String, Object> resultMap = new HashMap<>();
		paramMap.put("id", taskId);
		if (paramMap.containsKey("serviceList")) {
			paramMap.put("serviceArray", paramMap.get("serviceList").toString());
		}

		logger.debug("the updateOperationServiceTimingTask paramMap is :" + JsonHelper.toJson(paramMap));
		dbService.update(DBServiceConst.UPDATE_OPERATION_TIMED_TASK, paramMap);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", taskId);
		List<HashMap<String, Object>> task = dbService.directSelect(DBServiceConst.SELECT_OPERATION_TIMED_TASK, params);
		if (task.size() > 0) {
			if (Integer.parseInt(paramMap.get("status").toString()) == 0) {
				// 关闭
				if (paramMap.containsKey("from")) {
					String from = paramMap.get("from").toString();
					taskManageService.deleteTask(taskId, task.get(0).get("expression").toString());
					if (from.equals("TASKEDIT")) {
						resultMap.put("messageContent", "该运维运维编辑成功！");
					} else {
						resultMap.put("messageContent", "该运维运维已停止！");
					}
				}

			} else {
				// 启动
				if (paramMap.containsKey("from")) {
					String from = paramMap.get("from").toString();
					if (from.equals("TASKSTART")) {
						taskManageService.addTasks2Quartz(task.get(0));
						resultMap.put("messageContent", "该运维运维启动成功！");
					} else {
						taskManageService.updateTask(task.get(0));
						resultMap.put("messageContent", "该运维运维编辑成功！");
					}
				}

			}
		}

		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert updateOperationServiceTemplate successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/minion/{minionName}/tasks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceMinionTasks(@PathVariable String minionName, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("minionName", minionName);
			parseRelationLoginIds(paramMap);

			HashMap<String, Object> resultMap = null;
			if (start != null) {
				int startNum = Integer.parseInt(start);
				int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
				resultMap = dbService.selectByPage(DBServiceConst.SELECT_OPERATION_SERVICE_MINION_TASKS, paramMap, currentPage, Integer.parseInt(length));

				// dataTables需要将接收到的draw直接返回
				resultMap.put("draw", draw);
			} else {
				resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_MINION_TASKS, paramMap);
			}

			List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					HashMap<String, Object> detailsMap = resultList.get(i);
					byte[] byteArray = (byte[]) detailsMap.get("result");
					detailsMap.put("resultString", new String(byteArray));
				}
			}

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listOperationServiceMinionTasks successful! the result is:" + result);
			return result;
		} catch (Exception e) {
			logger.error("query listOperationServiceMinionTasks error!", e);
			return invalidRequest();
		}
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTasks(@RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length, @RequestParam(required = false, value = "timedId") String timedId, @RequestParam(required = false, value = "queryType") String queryType, @RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection) {
		HashMap<String, Object> paramMap = new HashMap<>();

		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = null;
		if (null != timedId) {
			paramMap.put("timedId", Integer.parseInt(timedId));
		} else {
			paramMap.put("timedId", 0);
		}

		if (queryType != null) {
			paramMap.put("queryType", queryType);
		}

		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}

		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}

		HashMap<String, Object> roleParamMap = new HashMap<>();
		parseCurrentLoginIds(roleParamMap);
		Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
		paramMap.put("countSize", countSize);

		if (start != null && !length.equals("-1")) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_INFO, paramMap, currentPage, Integer.parseInt(length));

			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_INFO, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTasks successful! the result is:" + result);
		return result;
	}

	@RequestMapping(value = "/task/nodes", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskNodes(@RequestParam(required = true, value = "parentId") Integer parentId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("parent_id", parentId);

		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_NODES, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTaskNodes successful! the result is:" + result);
		return result;
	}

	@RequestMapping(value = "/task/{taskId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskDetails(@PathVariable Integer taskId) {
		HashMap<String, Object> paramMap = new HashMap<>();

		parseRelationLoginIds(paramMap);
		paramMap.put("taskId", taskId);

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASKS, paramMap);

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTaskDetails successful! the result is:" + result);
		return result;
	}

	@RequestMapping(value = "/task/{taskId}/template/{refTemplateId}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskTemplate(@PathVariable Integer taskId, @PathVariable Integer refTemplateId, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("taskId", taskId);
		paramMap.put("refTemplateId", refTemplateId);
		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = null;
		if (start != null) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_TEMPLATE, paramMap, currentPage, Integer.parseInt(length));

			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_TEMPLATE, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTaskTemplate successful! the result is:" + result);
		return result;
	}

	@RequestMapping(value = "/task/{taskId}/template", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskTemplateList(@PathVariable Integer taskId, @RequestParam(required = false, value = "minionName") String minionName, @RequestParam(required = false, value = "exportStatus") Boolean exportStatus, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("taskId", taskId);

		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = null;
		if (start != null) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_TEMPLATE, paramMap, currentPage, Integer.parseInt(length));

			if (exportStatus != null && exportStatus == true) {
				parseOperationServiceTaskTemplateResources(taskId, resultMap);
			}

			// dataTables需要将接收到的draw直接返回
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_TEMPLATE, paramMap);

			if (exportStatus != null && exportStatus == true) {
				parseOperationServiceTaskTemplateResources(taskId, resultMap);
			}
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTaskTemplate successful! the result is:" + result);
		return result;
	}

	@RequestMapping(value = "/task/{taskId}/template/{templateId}/resources", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskTemplateResources(@PathVariable Integer taskId, @PathVariable Integer templateId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("taskId", taskId);
		paramMap.put("templateId", templateId);

		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_TEMPLATE_RESOURCES, paramMap);

		List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				HashMap<String, Object> detailsMap = resultList.get(i);
				byte[] byteArray = (byte[]) detailsMap.get("result");
				detailsMap.put("resultString", new String(byteArray));
			}
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationServiceTaskTemplateResources successful! the result is:" + result);
		return result;
	}

	private void parseOperationServiceTaskTemplateResources(Integer taskId, HashMap<String, Object> taskTemplatesMap) {
		List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) taskTemplatesMap.get("record");
		for (HashMap<String, Object> item : records) {
			String taskTemplateResourcesString = listOperationServiceTaskTemplateResources(taskId, Integer.parseInt(item.get("relationId").toString()));
			HashMap<String, Object> taskResourceTemplatesResult = JsonHelper.fromJson(HashMap.class, taskTemplateResourcesString);
			item.put("list", taskResourceTemplatesResult.get("record"));
		}
	}

	@RequestMapping(value = "/task/{taskId}/resources", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskResources(@PathVariable Integer taskId, @RequestParam(required = false, value = "templateId") Integer templateId, @RequestParam(required = false, value = "checkStatus") String checkStatus, @RequestParam(required = false, value = "exportStatus") Boolean exportStatus, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length, @RequestParam(required = false, value = "search[value]") String searchValue) {
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("taskId", taskId);

			parseRelationLoginIds(paramMap);

			HashMap<String, Object> resultMap = null;
			if (start != null && !length.equals("-1")) {
				int startNum = Integer.parseInt(start);
				int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
				resultMap = dbService.selectByPage(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_RESOURCES, paramMap, currentPage, Integer.parseInt(length));

				if (exportStatus != null && exportStatus == true) {
					parseOperationServiceTaskResourceTemplates(taskId, resultMap);
				}
				resultMap.put("draw", draw);
			} else {
				resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_RESOURCES, paramMap);

				if (exportStatus != null && exportStatus == true) {
					parseOperationServiceTaskResourceTemplates(taskId, resultMap);
				}
			}

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listOperationServiceTaskResources successful! the result is:" + result);
			return result;

		} catch (Exception e) {
			logger.error("query listOperationServiceTaskResources error!", e);
			return invalidRequest();
		}
	}

	@RequestMapping(value = "/task/{taskId}/resource/{minion}/templates", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskResourceTemplates(@PathVariable Integer taskId, @PathVariable String minion) {
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("taskId", taskId);
			paramMap.put("minionName", minion);

			parseRelationLoginIds(paramMap);

			HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_RESOURCE_TEMPLATES, paramMap);

			List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get("record");
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					HashMap<String, Object> detailsMap = resultList.get(i);
					byte[] byteArray = (byte[]) detailsMap.get("result");
					detailsMap.put("resultString", new String(byteArray));
				}
			}

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listOperationServiceTaskResourceTemplates successful! the result is:" + result);
			return result;

		} catch (Exception e) {
			logger.error("query listOperationServiceTaskResourceTemplates error!", e);
			return invalidRequest();
		}
	}

	private void parseOperationServiceTaskResourceTemplates(Integer taskId, HashMap<String, Object> taskResourcesMap) {
		List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) taskResourcesMap.get("record");
		for (HashMap<String, Object> item : records) {
			String taskResourceTemplatesString = listOperationServiceTaskResourceTemplates(taskId, (String) item.get("minionName"));
			HashMap<String, Object> taskResourceTemplatesResult = JsonHelper.fromJson(HashMap.class, taskResourceTemplatesString);
			item.put("list", taskResourceTemplatesResult.get("record"));
		}
	}

	@RequestMapping(value = "/task/{taskId}/result", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationServiceTaskResult(@PathVariable Integer taskId) {
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("taskId", taskId);
			parseRelationLoginIds(paramMap);

			HashMap<String, Object> resultMap = null;
			resultMap = dbService.select(DBServiceConst.SELECT_OPERATION_SERVICE_TASK_RESULT, paramMap);

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listOperationServiceTaskResult successful! the result is:" + result);
			return result;

		} catch (Exception e) {
			logger.error("query listOperationServiceTaskResult error!", e);
			return invalidRequest();
		}
	}

	@RequestMapping(value = "/tasks/{taskId}/{refTemplateId}/{taskType}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteOperationServiceProject(@PathVariable Integer taskId, @PathVariable Integer refTemplateId, @PathVariable String taskType) {
		HashMap<String, Object> parameter = new HashMap<>();
		if (taskType.equals("TIMED") && refTemplateId == 0) {
			parameter.put("id", taskId);
			List<HashMap<String, Object>> task = dbService.directSelect(DBServiceConst.SELECT_OPERATION_TIMED_TASK, parameter);
			if (task.size() > 0) {
				taskManageService.deleteTask(taskId, task.get(0).get("expression").toString());
				dbService.delete(DBServiceConst.DELETE_OPERATION_TIMED_TASK, parameter);
			}
		} else {
			parameter.put("taskId", taskId);
			parameter.put("refTemplateId", refTemplateId);
			dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_TASK, parameter);
		}
		// dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_TASK_TEMPLATE,
		// parameter);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteDeployServiceProject successful! ");
		return result;
	}

	@RequestMapping(value = "/positions", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String changeOperationServicePositions(@RequestBody HashMap<String, Object> paramMap) {
		HashMap<String, Object> positionChangeTemplateMap = (HashMap<String, Object>) paramMap.get("positionChangeTemplate");
		for (String templateId : positionChangeTemplateMap.keySet()) {
			HashMap<String, Object> parameter = new HashMap<>();
			parameter.put("id", templateId);
			parameter.put("catalogId", positionChangeTemplateMap.get(templateId));
			dbService.update(DBServiceConst.UPDATE_OPERATION_SERVICE_TEMPLATE, parameter);
		}

		dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_POSITIONS, new HashMap<String, Object>()); // 删除现有排序记录

		List<String> catalogPositionList = (List<String>) paramMap.get("catalogPositions"); // 增加目录排序记录
		StringBuffer selectStringBuffer = new StringBuffer();
		HashMap<String, Object> positionParameter = new HashMap<>();
		for (int i = 0; i < catalogPositionList.size(); i++) {
			String catalogIdStr = catalogPositionList.get(i);
			selectStringBuffer.append("select ");
			selectStringBuffer.append(catalogIdStr.substring(catalogIdStr.lastIndexOf("_") + 1));
			selectStringBuffer.append(",");
			selectStringBuffer.append(-1);
			selectStringBuffer.append(",");
			selectStringBuffer.append(i);
			if (i < catalogPositionList.size() - 1) {
				selectStringBuffer.append(" union all ");
			}
		}
		positionParameter.put("valueSqlString", selectStringBuffer.toString());
		dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_POSITION, positionParameter);

		HashMap<String, Object> templatePositionsMap = (HashMap<String, Object>) paramMap.get("templatePositions"); // 增加每个目录下的模板排序记录
		for (String catalogId : templatePositionsMap.keySet()) {
			List<String> templatePositionList = (List<String>) templatePositionsMap.get(catalogId); // 增加目录排序记录
			StringBuffer selectTemplateStringBuffer = new StringBuffer();
			for (int i = 0; i < templatePositionList.size(); i++) {
				String templateIdStr = templatePositionList.get(i);
				if (templateIdStr.length() == 0) {
					continue;
				}
				selectTemplateStringBuffer.append(" union all ");
				selectTemplateStringBuffer.append("select ");
				selectTemplateStringBuffer.append(templateIdStr.substring(templateIdStr.lastIndexOf("_") + 1));
				selectTemplateStringBuffer.append(",");
				selectTemplateStringBuffer.append(catalogId);
				selectTemplateStringBuffer.append(",");
				selectTemplateStringBuffer.append(i);
			}
			if (selectTemplateStringBuffer.toString().length() > 0) {
				String valueSqlString = selectTemplateStringBuffer.toString();
				valueSqlString = valueSqlString.substring(11);
				positionParameter.put("valueSqlString", valueSqlString);
				dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_POSITION, positionParameter);
			}
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert changeOperationServicePositions successful! the result is :" + result);
		return result;
	}

	// @RequestMapping(value = "/nodes/ipaddress", method = RequestMethod.POST,
	// produces = "text/html;charset=UTF-8")
	// @ResponseBody
	// public String listOperationNodeIPAddress(HttpSession
	// session,@RequestParam(required = false, name = "systemSubType") String
	// systemSubType, @RequestBody HashMap<String, Object> paramMap) {
	// paramMap.put("sessionId", session.getId());
	// if (systemSubType != null) {
	// paramMap.put("systemSubType", systemSubType);
	// }
	//
	// HashMap<String, Object> resultMap = null;
	// try {
	// dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
	// if (paramMap.containsKey("ipAddress")) {
	// List<String> ipaddressList = (List<String>) paramMap.get("ipAddress");
	// if (ipaddressList.size() > 0) {
	// dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_QUERY_IPADDRESS,
	// paramMap);
	// parseRelationLoginIds(paramMap);
	//
	// HashMap<String, Object> roleParamMap = new HashMap<>();
	// parseCurrentLoginIds(roleParamMap);
	// Integer countSize = (Integer)
	// dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE,
	// roleParamMap);
	// paramMap.put("countSize", countSize);
	//
	// resultMap =
	// dbService.select(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES_BY_IPADDRESS,
	// paramMap);
	// }
	// }
	// } catch (Exception e) {
	// logger.error("query listOperationNodes by ipaddress error!", e);
	// } finally {
	// dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
	// }
	//
	// if (resultMap == null) {
	// return super.invalidRequest();
	// }
	//
	// String result = JsonHelper.toJson(resultMap);
	// logger.debug("query listOperationNodes by the ipaddress successful! the
	// result is :" + result);
	// return result;
	// }

	// @RequestMapping(value = "/nodes/hostname", method = RequestMethod.POST,
	// produces = "text/html;charset=UTF-8")
	// @ResponseBody
	// public String listOperationNodesByHostName(HttpSession
	// session,@RequestParam(required = false, name = "systemSubType") String
	// systemSubType,@RequestBody HashMap<String, Object> paramMap) {
	// paramMap.put("sessionId", session.getId());
	// if (systemSubType != null) {
	// paramMap.put("systemSubType", systemSubType);
	// }
	//
	// HashMap<String, Object> resultMap = null;
	// try {
	// dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
	// if (paramMap.containsKey("hostName")) {
	// List<String> ipaddressList = (List<String>) paramMap.get("hostName");
	// if (ipaddressList.size() > 0) {
	// dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_QUERY_HOSTNAME,
	// paramMap);
	// parseRelationLoginIds(paramMap);
	//
	// HashMap<String, Object> roleParamMap = new HashMap<>();
	// parseCurrentLoginIds(roleParamMap);
	// Integer countSize = (Integer)
	// dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE,
	// roleParamMap);
	// paramMap.put("countSize", countSize);
	//
	// resultMap =
	// dbService.select(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES_BY_HOSTNAME,
	// paramMap);
	// }
	// }
	// } catch (Exception e) {
	// logger.error("query listOperationNodes by ipaddress error!", e);
	// } finally {
	// dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, paramMap);
	// }
	//
	// if (resultMap == null) {
	// return super.invalidRequest();
	// }
	//
	// String result = JsonHelper.toJson(resultMap);
	// logger.debug("query listOperationNodes by the ipaddress successful! the
	// result is :" + result);
	// return result;
	// }

	@RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationNodes(@RequestParam(required = false, name = "beginIp") String beginIp, @RequestParam(required = false, name = "endIp") String endIp, @RequestParam(required = false, name = "tagValue") String tagValue, @RequestParam(required = false, name = "key") String key, @RequestParam(required = false, name = "hostName") String hostName, @RequestParam(required = false, name = "ipAddress") String ipAddress,
			@RequestParam(required = false, name = "macAddress") String macAddress, @RequestParam(required = false, name = "systemSubType") String systemSubType, @RequestParam(required = false, name = "poolId") Integer poolId, @RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "currentPage") Integer currentPage,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();

		HashMap<String, Object> queryParamMap = new HashMap<>();
//		queryParamMap.put("sessionId", session.getId());
		dbService.delete(DBServiceConst.DELETE_OPERATION_SERVICE_QUERY, queryParamMap);

		if (hostName != null) {
			String[] hostNameArray = hostName.split(",");
			if (hostNameArray.length > 0) {
				queryParamMap.put("queryData", hostNameArray);
				queryParamMap.put("queryType", "hostName");
				dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_QUERY_ITEM, queryParamMap);
				paramMap.put("hostName", 1);
			}
		}

		if (ipAddress != null) {
			String[] ipAddressArray = ipAddress.split(",");
			if (ipAddressArray.length > 0) {
				queryParamMap.put("queryData", ipAddressArray);
				queryParamMap.put("queryType", "ipAddress");
				dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_QUERY_ITEM, queryParamMap);
				paramMap.put("ipAddress", 1);
			}
		}

		if (macAddress != null) {
			String[] macAddressArray = macAddress.split(",");
			if (macAddressArray.length > 0) {
				queryParamMap.put("queryData", macAddressArray);
				queryParamMap.put("queryType", "macAddress");
				dbService.insert(DBServiceConst.INSERT_OPERATION_SERVICE_QUERY_ITEM, queryParamMap);
				paramMap.put("macAddress", 1);
			}
		}

//		paramMap.put("sessionId", session.getId());

		if (tagValue != null) {
			paramMap.put("tagValue", tagValue);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if (systemSubType != null) {
			paramMap.put("systemSubType", systemSubType);
		}

		if (beginIp != null) {
			paramMap.put("beginIp", CommonUtil.parseIpToLong(beginIp));
		}

		if (endIp != null) {
			paramMap.put("endIp", CommonUtil.parseIpToLong(endIp));
		}
		parseRelationLoginIds(paramMap);

		HashMap<String, Object> roleParamMap = new HashMap<>();
		parseCurrentLoginIds(roleParamMap);
		Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
		paramMap.put("countSize", countSize);

		HashMap<String, Object> resultMap = null;
		if (start != null && !length.equals("-1")) {
			int startNum = Integer.parseInt(start);
			if (currentPage == null) {
				currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
			}

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES, paramMap, currentPage, Integer.parseInt(length));
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_OPERATION_NODES, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listOperationNodes  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/virtuals", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listOperationVirtuals(@RequestParam(required = false, name = "tagId") String tagId, @RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "draw") String draw, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (tagId != null) {
			paramMap.put("tagId", tagId);
		}
		parseRelationLoginIds(paramMap);

		HashMap<String, Object> resultMap = null;
		if (start != null && !length.equals("-1")) {
			int startNum = Integer.parseInt(start);
			int currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;

			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RESOURCE_OPERATION_VIRTUALS, paramMap, currentPage, Integer.parseInt(length));
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RESOURCE_OPERATION_VIRTUALS, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listMonitorVirtuals successful! the result is :" + result);
		return result;
	}
}
