package com.system.started.rest.controller;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.process.EProcessStatus;
import com.system.started.service.ProcessService;
import com.vlandc.oss.common.JsonHelper;

import io.swagger.annotations.Api;

@Api(value = "流程操作")
@Controller
@RequestMapping(value = "/processes")
public class ProcessController extends AbstractController {

	private static Logger logger = Logger.getLogger(ProcessController.class);

	@Autowired
	private ProcessService processService;


	@RequestMapping(value = "/gtasks/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listGtasksCount(@RequestParam(required = false, value = "STATUS") String status) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		if (status != null && !status.equals("")) {
			paramMap.put("status", "'" + status + "'");
		}
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.processService.listGtasksCount(paramMap);
	}

	// 查询待办任务
	@RequestMapping(value = "/gtasks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listGtasks(
			@RequestParam(required = false, value = "user") String user,
			@RequestParam(required = false, value = "STATUS") String status,
			@RequestParam(required = false, value = "BEGIN_CREATE_TS") String begin_create_ts,
			@RequestParam(required = false, value = "END_CREATE_TS") String end_create_ts,
			@RequestParam(required = false, value = "type") String taskType,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("engineType", "LOCAL");

		if (user != null) {
			paramMap.put("CREATE_USER", user);
		}
		if (status != null && !status.equals("")) {
			paramMap.put("status", "'" + status + "'");
		}
		if (begin_create_ts != null) {
			paramMap.put("BEGIN_CREATE_TS", begin_create_ts);
		}
		if (end_create_ts != null) {
			paramMap.put("END_CREATE_TS", end_create_ts);
		}
		if (taskType != null) {
			paramMap.put("type", taskType);
		}
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}

		if (start != null) {
			paramMap.put("start", start);
		}

		if (length != null) {
			paramMap.put("length", length);
		}

		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.processService.listGtasks(paramMap);
	}

	@RequestMapping(value = "/gtasks/refUser", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listGtaskRefUsers(@RequestParam(required = false, value = "identity") String identity) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("identity", identity);
		return this.processService.listGtaskRefUsers(paramMap);
	}

	@RequestMapping(value = "/myapply/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listMyApplyProcessesCount() {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);
		return this.processService.listMyApplyProcessesCount(paramMap);
	}

	// 查询我的申请
	@RequestMapping(value = "/myapply", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listMyApplyProcesses(
			@RequestParam(required = false, value = "STATUS") String status,
			@RequestParam(required = false, value = "BEGIN_CREATE_TS") String begin_create_ts,
			@RequestParam(required = false, value = "END_CREATE_TS") String end_create_ts,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();

		if (status != null) {
			paramMap.put("status", "'" + status + "'");
		} else {
			paramMap.put("status", "'" + EProcessStatus.ACCEPTED + "','" + EProcessStatus.PROCESSING + "','" + EProcessStatus.SUBMITED + "','" + EProcessStatus.SEND_BACK + "'");
		}
		if (begin_create_ts != null) {
			paramMap.put("BEGIN_CREATE_TS", begin_create_ts);
		}
		if (end_create_ts != null) {
			paramMap.put("END_CREATE_TS", end_create_ts);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}

		if (start != null) {
			paramMap.put("start", start);
		}

		if (length != null) {
			paramMap.put("length", length);
		}
		parseRelationLoginIds(paramMap);

		return this.processService.listMyApplyProcesses(paramMap);
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesses(
			@RequestParam(required = false, value = "user") String user,
			@RequestParam(required = false, value = "STATUS") String status,
			@RequestParam(required = false, value = "BEGIN_CREATE_TS") String begin_create_ts,
			@RequestParam(required = false, value = "END_CREATE_TS") String end_create_ts,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("engineType", "LOCAL");

		if (user != null) {
			paramMap.put("CREATE_USER", user);
		}
		if (status != null) {
			paramMap.put("status", "'" + status + "'");
		}
		if (begin_create_ts != null) {
			paramMap.put("BEGIN_CREATE_TS", begin_create_ts);
		}
		if (end_create_ts != null) {
			paramMap.put("END_CREATE_TS", end_create_ts);
		}
		if (draw != null) {
			paramMap.put("draw", draw);
		}

		if (start != null) {
			paramMap.put("start", start);
		}

		if (length != null) {
			paramMap.put("length", length);
		}
		parseRelationLoginIds(paramMap);
		return this.processService.listProcesses(paramMap);
	}

	@RequestMapping(value = "/{processId}/requests/result", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listRequestProcesseResult(@PathVariable Integer processId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("processId", processId);
		return this.processService.listRequestProcesseResult(paramMap);
	}

	@RequestMapping(value = "/{processId}/details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesseDetails(
			@PathVariable Integer processId,
			@RequestParam(required = false, value = "type") String taskType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		parseRelationLoginIds(paramMap);

		paramMap.put("processId", processId);
		if (taskType != null) {
			paramMap.put("type", taskType);
		}
		paramMap.put("curLoginId", getCurrentLoginId());
		return this.processService.listProcesseDetails(paramMap);
	}

	@RequestMapping(value = "/{processInstanceId}/tasks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesseTasks(@PathVariable Integer processInstanceId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("processInstanceId", processInstanceId);
		return this.processService.listProcesseTasks(paramMap);
	}

	@RequestMapping(value = "/{processId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String closeRequestProcess(@PathVariable Integer processId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("processId", processId);
		parseCurrentLoginIds(paramMap);
		return this.processService.closeRequestProcess(paramMap);
	}

	@RequestMapping(value = "/{processId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reSubmitRequestProcess(
			@PathVariable Integer processId,
			@RequestBody HashMap<String, Object> reSubmitParamMap) {
		parseCurrentLoginIds(reSubmitParamMap);
		reSubmitParamMap.put("processId", processId);
		return this.processService.reSubmitRequestProcess(reSubmitParamMap);
	}

	@RequestMapping(value = "/task", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkTask(@RequestBody HashMap<String, Object> checkTaskMap) {
		checkTaskMap.put("checkUser", getCurrentLoginId());
		return this.processService.checkTask(checkTaskMap);
	}

	@RequestMapping(value = "/{resourceId}/requests", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcessResourceRequests(@PathVariable Integer resourceId,
	                                          @RequestParam(required = false, value = "loginId") String loginId,
	                                          @RequestParam(required = false, value = "draw") String draw,
	                                          @RequestParam(required = false, value = "start") String start,
	                                          @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> parameter = new HashMap<>();
		if (resourceId != -1) {
			parameter.put("resourceId", resourceId);
		}

		if (loginId != null) {
			parameter.put("loginId", loginId);
		}
		if (draw != null) {
			parameter.put("draw", draw);
		}
		if (start != null) {
			parameter.put("start", start);
		}
		if (length != null) {
			parameter.put("length", length);
		}
		return this.processService.listProcessResourceRequests(parameter);
	}

	@RequestMapping(value = "/modules", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesseModules(
			@RequestParam(required = false, value = "id") String id,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "department") String department,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "subType") String subType,
			@RequestParam(required = false, value = "begin_create_ts") String begin_create_ts,
			@RequestParam(required = false, value = "end_create_ts") String end_create_ts,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("name", name);
		paramMap.put("department", department);
		paramMap.put("type", type);
		paramMap.put("subType", subType);
		paramMap.put("begin_create_ts", begin_create_ts);
		paramMap.put("end_create_ts", end_create_ts);
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}
		return this.processService.listProcesseModules(paramMap);
	}

	@RequestMapping(value = "/module/{id}/type/{type}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesseModuleTypes(
			@PathVariable Integer id,
			@PathVariable String type,
			@RequestParam(required = false, value = "department") String department,
			@RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (id != 0) {
			paramMap.put("id", id);
		}
		if (department != null) {
			paramMap.put("department", department);
		}
		paramMap.put("type", type);
		if (draw != null) {
			paramMap.put("draw", draw);
		}
		if (start != null) {
			paramMap.put("start", start);
		}
		if (length != null) {
			paramMap.put("length", length);
		}

		return this.processService.listProcesseModuleTypes(paramMap);
	}

	@RequestMapping(value = "/module/type/{type}/departments", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesseModuleTypeDepartments(@PathVariable String type) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("type", type);
		return this.processService.listProcesseModuleTypeDepartments(paramMap);
	}

	@RequestMapping(value = "/module", method = RequestMethod.POST)
	@ResponseBody
//	@ApiOperation(value="创建流程", httpMethod = "POST", notes = "createProcesseModule", response = String.class)
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "roleMap", value = "参数体，<br/>"
//				+ "例子：<br/>"
//				+ "{<br/>"
//				+ "\"name\":\"测试管理员\",<br>"
//				+ "\"description\":\"测试管理员\",<br>"
//				+ "\"type\":10<br/>"
//				+ "}",
//				required = true, dataType = "String",  paramType = "body")
//	})
	public String createProcesseModule( @RequestBody HashMap<String, Object> paramMap) {
		return this.processService.createProcesseModule(paramMap);
	}

	@RequestMapping(value = "/module", method = RequestMethod.PUT)
	@ResponseBody
	public String updateProcesseModule( @RequestBody HashMap<String, Object> paramMap) {
		return this.processService.updateProcesseModule(paramMap);
	}

	@RequestMapping(value = "/module/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteProcesseModule(@PathVariable String id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("moduleId", id);
		return this.processService.deleteProcesseModule(paramMap);
	}


	@RequestMapping(value = "/module/type", method = RequestMethod.POST)
	@ResponseBody
	public String updateProcesseModuleType( @RequestBody HashMap<String, Object> paramMap) {
		return this.processService.updateProcesseModuleType(paramMap);
	}

	@RequestMapping(value = "/module/{refModule}/tasks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesseModuleTasks( @PathVariable String refModule) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("refModule", refModule);
		return this.processService.listProcesseModuleTasks(paramMap);
	}

	@RequestMapping(value = "/module/{refModuleInstance}/task/instances", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcesseModuleTaskInstances(@PathVariable String refModuleInstance) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("refModuleInstance", refModuleInstance);
		return this.processService.listProcesseModuleTaskInstances(paramMap);
	}

	@RequestMapping(value = "/module/type/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcessModuleTypeCount(@RequestParam(required = true, value = "resourceType") String resourceType) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("subType", resourceType);
		paramMap.put("CREATE_USER", getCurrentLoginId());
		return this.processService.listProcessModuleTypeCount(paramMap);
	}

	@RequestMapping(value = "/stack", method = RequestMethod.POST)
	@ResponseBody
	public String createStack(@RequestParam String regionName, @RequestParam String projectId, @RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		paramMap.put("regionName",regionName);
		paramMap.put("projectId",projectId);
		return this.processService.createStack(paramMap);
	}

	@RequestMapping(value = "/relation/stack", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listProcessRelationStacks(
			@RequestParam(required = false, value = "relationType") String relationType,
			@RequestParam(required = false, value = "relationKey") String relationKey) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("relationType", relationType);
		paramMap.put("relationKey", relationKey);
		return this.processService.listProcessRelationStacks(paramMap);
	}

	@RequestMapping(value = "/operate", method = RequestMethod.POST)
	@ResponseBody
	public String createProcessOperate(@RequestBody HashMap<String, Object> paramMap) {
		parseRelationLoginIds(paramMap);
		paramMap.put("param", JsonHelper.toJson(paramMap.get("param")));
		return this.processService.createProcessOperate(paramMap);
	}
}
