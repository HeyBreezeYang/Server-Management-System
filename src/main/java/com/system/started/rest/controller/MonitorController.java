package com.system.started.rest.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.rest.request.MonitorEventAckBean;
import com.system.started.rest.request.MonitorGraphCreateBean;
import com.system.started.rest.request.MonitorGraphItemUpdateBean;
import com.system.started.rest.request.MonitorHostStatusUpdateBean;
import com.system.started.rest.request.MonitorItemCreateBean;
import com.system.started.rest.request.MonitorItemStatusUpdateBean;
import com.system.started.rest.request.MonitorItemUpdateBean;
import com.system.started.rest.request.MonitorMultyNodeGraphAddBean;
import com.system.started.rest.request.MonitorNetworkCreateBean;
import com.system.started.rest.request.MonitorNetworkUpdateBean;
import com.system.started.rest.request.MonitorNodeGraphAddBean;
import com.system.started.rest.request.MonitorTemplateCreateBean;
import com.system.started.rest.request.MonitorTemplateUpdateBean;
import com.system.started.rest.request.MonitorTriggerUpdateBean;
import com.system.started.rest.request.MonitorWebSceneCreateBean;
import com.system.started.rest.request.MonitorWebSceneStatusUpdateBean;
import com.system.started.rest.request.MonitorWebSceneUpdateBean;
import com.system.started.rest.request.ZabbixTriggerCreateBean;
import com.system.started.service.MonitorService;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.monitor.EMonitorPanelType;
import com.vlandc.oss.model.monitor.EMonitorReportType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(value = "/monitor")
@Api(value="/monitor", description="监控管理控制器")
public class MonitorController extends AbstractController {

	private static Logger logger = Logger.getLogger(MonitorController.class);
	
	@Autowired
	private MonitorService monitorService;

	@RequestMapping(value = "/hosts/panel", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询图表数据", httpMethod = "GET", notes = "getHostPanel", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "itemIds", value = "指标ID集合字符串，<br/>例子：26138", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "length", value = "条数，<br/>例子：100", required = true, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "panelType", value = "面板类型，<br/>例子：MULITY", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束日期，<br/>例子：2018-06-22", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "beginTime", value = "开始时间，<br/>例子：1529647489", required = true, dataType = "long",  paramType = "query"),
		@ApiImplicitParam(name = "endTime", value = "结束时间，<br/>例子：1529653489", required = true, dataType = "long",  paramType = "query")
	})
	public String getHostPanel(
			@RequestParam String itemIds, 
			@RequestParam Integer length, 
			@RequestParam String panelType,
			@RequestParam(required=false,name="endDate")String endDate,
			@RequestParam(required=false,name="beginTime")Long beginTime,
			@RequestParam(required=false,name="endTime")Long endTime) {
		HashMap<String, Object> actionParamMap = new HashMap<>();
		String[] itemArray = itemIds.split(",");
		List<Integer> itemList = new ArrayList<>();
		for (int i = 0; i < itemArray.length; i++) {
			itemList.add(Integer.parseInt(itemArray[i]));
		}
		if (endDate != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				actionParamMap.put("endTime", sdf.parse(endDate).getTime()/1000);
			} catch (Exception e) {

			}
		}
		if (endTime != null) {
			actionParamMap.put("endTime", endTime);
		}
		if (beginTime != null) {
			actionParamMap.put("beginTime", beginTime);
		}
		actionParamMap.put("itemList", itemList);
		actionParamMap.put("panelType", EMonitorPanelType.valueOf(panelType));
		actionParamMap.put("length", length);
		
		parseCurrentLoginIds(actionParamMap);
		
		return this.monitorService.getHostPanel(actionParamMap);
	}

	@RequestMapping(value = "/hosts/report", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询主机指标数据", httpMethod = "GET", notes = "getHostPanel", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "itemIds", value = "指标ID集合字符串，<br/>例子：296964", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "reportType", value = "报表类型，<br/>例子：HOUR", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "beginTimeStamp", value = "开始时间，<br/>例子：1529647489", required = true, dataType = "long",  paramType = "query"),
		@ApiImplicitParam(name = "endTimeStamp", value = "结束时间，<br/>例子：1529653489", required = true, dataType = "long",  paramType = "query")
	})
	public String getHostReport(@RequestParam String itemIds, @RequestParam long beginTimeStamp, @RequestParam long endTimeStamp, @RequestParam String reportType) {
		HashMap<String, Object> actionParamMap = new HashMap<>();

		String[] itemArray = itemIds.split(",");
		List<Integer> itemList = new ArrayList<>();
		for (int i = 0; i < itemArray.length; i++) {
			itemList.add(Integer.parseInt(itemArray[i]));
		}
		actionParamMap.put("itemList", itemList);
		actionParamMap.put("reportType", EMonitorReportType.valueOf(reportType));
		actionParamMap.put("beginTimeStamp", beginTimeStamp);
		actionParamMap.put("endTimeStamp", endTimeStamp);
		
		parseCurrentLoginIds(actionParamMap);
		
		return this.monitorService.getHostReport(actionParamMap);
	}

	@RequestMapping(value = "/hosts/{hostId}/events", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询主机的告警事件", httpMethod = "GET", notes = "listHostEvents", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：11161", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "triggerId", value = "触发器ID，<br/>例子：31600", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "itemId", value = "指标ID，<br/>例子：68316", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "ackStatus", value = "确认状态，<br/>例子：0", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "severity", value = "级别，<br/>例子：WARNING", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "keyWord", value = "关键字，<br/>例子：zabbix", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "begin_clock", value = "开始时间，<br/>例子：2018-06-01 00:00:00", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "end_clock", value = "结束时间，<br/>例子：2018-06-30 23:59:59", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "20")
	})
	public String listHostEvents(@PathVariable Integer hostId, 
			@RequestParam(required = false) Integer triggerId, 
			@RequestParam(required = false) Integer itemId, 
			@RequestParam(required = false, value = "ackStatus") String ackStatus, 
			@RequestParam(required = false, value = "severity") String severity, 
			@RequestParam(required = false, value = "keyWord") String keyWord, 
			@RequestParam(required = false, value = "begin_clock") String begin_clock, 
			@RequestParam(required = false, value = "end_clock") String end_clock, 
			@RequestParam(required = false, value = "start") String start, 
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("hostId", hostId);
		if (triggerId != null) {
			paramMap.put("triggerId", triggerId);
		}
		if (itemId != null) {
			paramMap.put("itemId", itemId);
		}

		if (ackStatus != null) {
			paramMap.put("ackStatus", ackStatus);
		}
		if (severity != null) {
			paramMap.put("severity", severity);
		}
		if (keyWord != null) {
			paramMap.put("keyWord", keyWord);
		}
		if (begin_clock != null) {
			paramMap.put("begin_clock", begin_clock);
		}
		if (end_clock != null) {
			paramMap.put("end_clock", end_clock);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		parseRelationLoginIds(paramMap);
		
		String result = this.monitorService.listHostEvents(paramMap);
		logger.debug("query listHostEvents  successful! the result is :" + result);
		return result;

	}

	@RequestMapping(value = "/templates", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询监控模板", httpMethod = "GET", notes = "listMonitorTemplates", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "templateId", value = "监控模板ID，<br/>例子：10001", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "key", value = "关键字，<br/>例子：Linux", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=Template OS Linux", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorTemplates(@RequestParam(required = false) Integer templateId,
			@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "queryColumn") String queryColumn,
			@RequestParam(required = false, value = "rules") String rules,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, name = "refresh") Boolean refresh,
			@RequestParam(required = false, value = "start") String start,
			@RequestParam(required = false, value = "length") String length) {	
			HashMap<String, Object> paramMap = new HashMap<>();
	
			if (templateId != null) {
				paramMap.put("templateId", templateId);
			}
	
			if (key != null) {
				paramMap.put("key", key);
			}
	
			if (refresh != null) {
				paramMap.put("refresh", refresh);
			}
			
			if (start != null) {
				paramMap.put("start", start);
			}
			
			if (length != null) {
				paramMap.put("length", length);
			}
			
			if(queryColumn != null){
				paramMap.put("queryColumn", queryColumn);
			}
			if(rules != null){
				List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
				String[] ruleList = rules.split(",");
				for (String rule : ruleList) {
					HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
					String[] ri = rule.split("&");
					for (String r : ri) {
						String[] i = r.split("=");
						ruleItemMap.put(i[0], i[1]);
					}
					ruleItems.add(ruleItemMap);
				}
				paramMap.put("list", ruleItems);
			}
			if (instanceSort != null) {
				paramMap.put("instanceSort", instanceSort);
			}
			if (instanceSortDirection != null) {
				paramMap.put("instanceSortDirection", instanceSortDirection);
			}
	
			parseRelationLoginIds(paramMap);
	
			String result = this.monitorService.listMonitorTemplates(paramMap);
			logger.debug("query listMonitorTemplates  successful! the result is :" + result);
			return result;
	}

	@RequestMapping(value = "/templates/{templateId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑监控模板", httpMethod = "PUT", notes = "updateZabbixTemplate", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "templateId", value = "监控模板ID，<br/>例子：10001", required = true, dataType = "integer",  paramType = "path")
	})
	public String updateZabbixTemplate(@PathVariable Integer templateId, @RequestBody MonitorTemplateUpdateBean monitorTemplate) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> templateMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorTemplate));
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		templateMap.put("host", templateMap.get("name"));
		templateMap.put("templateid", templateId);
		paramMap.put("template", templateMap);
		parseCurrentLoginIds(paramMap);
		
		String result = this.monitorService.updateZabbixTemplate(paramMap);
		logger.debug("update updateZabbixTemplate successful! ");
		return result;
	}
	
	@RequestMapping(value = "/template", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建监控模板", httpMethod = "POST", notes = "createZabbixTemplate", response = String.class)
	public String createZabbixTemplate(@RequestBody MonitorTemplateCreateBean monitorTemplateCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object>templateMap = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorTemplateCreateBean));
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		templateMap.put("host", templateMap.get("name"));
		paramMap.put("template", templateMap);
		parseCurrentLoginIds(paramMap);
		
		String result = this.monitorService.createZabbixTemplate(paramMap);	
		logger.debug("create createZabbixTemplate successful! ");
		return result;
	}
	
	@RequestMapping(value = "/templates/{templateId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除监控模板", httpMethod = "DELETE", notes = "deleteZabbixTemplate", response = String.class)
	public String deleteZabbixTemplate(@PathVariable Integer templateId) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		List<Integer> templates = new ArrayList<Integer>();
		templates.add(templateId);
		paramMap.put("templates", templates);
		parseCurrentLoginIds(paramMap);
		
		String result = this.monitorService.deleteZabbixTemplate(paramMap);
		logger.debug("delete deleteZabbixTemplate successful! ");
		return result;
	}
	
	
	//创建监控项
	@RequestMapping(value = "/item", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建监控项", httpMethod = "POST", notes = "createZabbixItem", response = String.class)
	public String createdZabbixItem(@RequestBody MonitorItemCreateBean monitorItemCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> itemMap=JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorItemCreateBean));
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("item", itemMap);
		parseCurrentLoginIds(paramMap);
		
		String result = this.monitorService.createZabbixItem(paramMap);	
		logger.debug("create createZabbixItem successful! ");
		return result;
	}
	
	//删除监控项
	@RequestMapping(value="/items/{itemId}",method=RequestMethod.DELETE,produces="text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value="删除监控项", httpMethod = "DELETE",notes = "deleteZabbixItem",response = String.class)
	public String deleteZabbixItem(@PathVariable Integer itemId) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		List<Integer> itemIds = new ArrayList<Integer>();
		itemIds.add(itemId);
		paramMap.put("itemIds", itemIds);
		parseCurrentLoginIds(paramMap);
		
		String result =this.monitorService.deleteZabbixItem(paramMap);
		logger.debug("delete deleteZabbixItem successful! ");
		return result;
	}

	//编辑监控项
	@RequestMapping(value = "/items/{itemId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑监控项", httpMethod = "PUT", notes = "updateZabbixItem", response = String.class)
	public String updateZabbixItem(@PathVariable Integer itemId, @RequestBody MonitorItemUpdateBean monitorItemUpdateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> itemMap=JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorItemUpdateBean));
		itemMap.put("itemId", itemId);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("item", itemMap);
		parseCurrentLoginIds(paramMap);
		
		String result = this.monitorService.updateZabbixItem(paramMap);	
		logger.debug("update updateZabbixItem successful! ");
		return result;
	}

	@RequestMapping(value = "/templates/{templateId}/items", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询监控模板下的指标", httpMethod = "GET", notes = "listMonitorTemplateItems", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "templateId", value = "监控模板ID，<br/>例子：10001", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "itemId", value = "指标ID，<br/>例子：10010", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "keyWord", value = "关键字，<br/>例子：Processor load", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorTemplateItems(@PathVariable Integer templateId, @RequestParam(required = false) Integer itemId, @RequestParam(required = false, name = "keyWord") String keyWord, @RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (itemId != null) {
			paramMap.put("itemId", itemId);
		}
		
		if (keyWord != null) {
			paramMap.put("keyWord", keyWord);
		}
		
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		paramMap.put("templateId", templateId);

		parseRelationLoginIds(paramMap);

		String result = this.monitorService.listMonitorTemplateItems(paramMap);
		logger.debug("query listMonitorTemplateItems  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/templates/{templateId}/triggers", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询监控模板下的触发器", httpMethod = "GET", notes = "listMonitorTemplateTriggers", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "templateId", value = "监控模板ID，<br/>例子：10001", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "triggerId", value = "触发器ID，<br/>例子：10010", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "itemId", value = "指标ID，<br/>例子：10010", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorTemplateTriggers(@PathVariable Integer templateId, @RequestParam(required = false) Integer triggerId, @RequestParam(required = false) Integer itemId, @RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (triggerId != null) {
			paramMap.put("triggerId", triggerId);
		}
		if (itemId != null) {
			paramMap.put("itemId", itemId);
		}
		
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		paramMap.put("templateId", templateId);
		parseRelationLoginIds(paramMap);

		String result = this.monitorService.listMonitorTemplateTriggers(paramMap);
		logger.debug("query listMonitorTemplateTriggers  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/templates/{templateId}/items/{itemId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑监控模板的指标", httpMethod = "PUT", notes = "updateMonitorTemplateItem", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "templateId", value = "模板ID，<br/>例子：10001", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "itemId", value = "指标ID，<br/>例子：10009", required = true, dataType = "integer",  paramType = "path")
	})
	public String updateMonitorTemplateItem(@PathVariable Integer templateId, @PathVariable Integer itemId, @RequestBody @ModelAttribute MonitorItemUpdateBean monitorItem) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorItem));
		paramMap.put("templateId", templateId);
		paramMap.put("itemId", itemId);
		String result = this.monitorService.updateMonitorTemplateItem(paramMap);
		logger.debug("update updateMonitorTemplateItem successful! ");
		return result;
	}

	@RequestMapping(value = "/templates/{templateId}/items/{itemId}/status", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑监控模板的指标的状态", httpMethod = "PUT", notes = "updateMonitorTemplateItemStatus", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "templateId", value = "模板ID，<br/>例子：16533", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "itemId", value = "指标ID，<br/>例子：292738", required = true, dataType = "integer",  paramType = "path")
	})
	public String updateMonitorTemplateItemStatus(@PathVariable Integer templateId, @PathVariable Integer itemId, @RequestBody @ModelAttribute MonitorItemStatusUpdateBean monitorItemStatus) {
		@SuppressWarnings("unchecked")               //先转成实体对象                                                                      转为json串
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorItemStatus));
		paramMap.put("itemId", itemId);
		parseCurrentLoginIds(paramMap);
		String result = this.monitorService.updateMonitorTemplateItemStatus(paramMap);
		logger.debug("update updateMonitorTemplateItemStatus successful! ");
		return result;
	}

	@RequestMapping(value = "/hosts/{hostId}/items", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机绑定的指标", httpMethod = "GET", notes = "listMonitorHostItems", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：10108", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "itemId", value = "指标ID，<br/>例子：23801", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorHostItems(@PathVariable Integer hostId, @RequestParam(required = false) Integer itemId, @RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (itemId != null) {
			paramMap.put("itemId", itemId);
		}
		
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		paramMap.put("hostId", hostId);
		parseRelationLoginIds(paramMap);

		String result = this.monitorService.listMonitorHostItems(paramMap);
		logger.debug("query listMonitorHostItems  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/hosts/{hostId}/triggers", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机绑定的触发器", httpMethod = "GET", notes = "listMonitorHostTriggers", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：10108", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "triggerId", value = "触发器ID，<br/>例子：13614", required = false, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorHostTriggers(@PathVariable Integer hostId, @RequestParam(required = false) Integer triggerId, @RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (triggerId != null) {
			paramMap.put("triggerId", triggerId);
		}
		
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		paramMap.put("hostId", hostId);
		parseRelationLoginIds(paramMap);

		String result = this.monitorService.listMonitorHostTriggers(paramMap);
		logger.debug("query listMonitorHostTriggers  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/hosts/{hostId}/items/{itemId}/status", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑绑定到主机上的监控指标状态", httpMethod = "PUT", notes = "updateMonitorHostItemStatus", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：10120", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "itemId", value = "itemId，<br/>例子：24526", required = true, dataType = "integer",  paramType = "path")
	})
	public String updateMonitorHostItemStatus(@PathVariable Integer hostId, @PathVariable Integer itemId, @RequestBody @ModelAttribute MonitorItemStatusUpdateBean monitorItemStatus) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorItemStatus));
		paramMap.put("hostId", hostId);
		paramMap.put("itemId", itemId);
		String changeResult = this.monitorService.updateMonitorHostItemStatus(paramMap);
		logger.debug("update updateMonitorHostItemStatus successful! ");
		return changeResult;
	}

	@RequestMapping(value = "/hosts/{hostId}/templates", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询主机绑定的监控模板", httpMethod = "GET", notes = "listMonitorHostTriggers", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：10108", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorHostTemplates(@PathVariable Integer hostId, @RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("hostId", hostId);
		if(refresh != null){
			paramMap.put("refresh", refresh);
		}
		
		if(start != null){
			paramMap.put("start", start);
		}
		
		if(length != null){
			paramMap.put("length", length);
		}
		
		parseRelationLoginIds(paramMap);

		String result = this.monitorService.listMonitorHostTemplates(paramMap);
		logger.debug("query listMonitorHostTemplates  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/triggers", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String createZabbixTrigger(@RequestBody ZabbixTriggerCreateBean zabbixTriggerCreateBean) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(zabbixTriggerCreateBean));	
		parseCurrentLoginIds(paramMap);
		return this.monitorService.createZabbixTrigger(paramMap);
	}

	@RequestMapping(value = "/triggers/{triggerId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑触发器", httpMethod = "PUT", notes = "updateZabbixTrigger", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "triggerId", value = "triggerId，<br/>例子：124161", required = true, dataType = "integer",  paramType = "path")
	})
	public String updateZabbixTrigger(@PathVariable Integer triggerId, @RequestBody @ModelAttribute MonitorTriggerUpdateBean monitorTrigger) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorTrigger));
		paramMap.put("triggerId", triggerId);
		parseCurrentLoginIds(paramMap);
		return this.monitorService.updateZabbixTrigger(paramMap);
	}

	@RequestMapping(value = "/triggers", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String deleteZabbixTrigger(@RequestParam String triggerIds) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		String[] triggerIdArray = triggerIds.split(",");
		paramMap.put("triggers", triggerIdArray);
		parseCurrentLoginIds(paramMap);
		return this.monitorService.deleteZabbixTrigger(paramMap);
	}

	@RequestMapping(value = "/hosts/{hostId}/templates", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "为主机绑定监控模板", httpMethod = "POST", notes = "createLinkHostToTemplate", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：10107", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "templateIds", value = "监控模板ID集合字符串，<br/>例子：10073,10001", required = true, dataType = "string",  paramType = "query")
	})
	public String createLinkHostToTemplate(@PathVariable Integer hostId, @RequestParam String templateIds) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("hostid", hostId);

		List<Integer> templates = new ArrayList<Integer>();
		String[] templateIdsArray = templateIds.split(",");
		for (int i = 0; i < templateIdsArray.length; i++) {
			templates.add(Integer.parseInt(templateIdsArray[i]));
		}
		paramMap.put("templates", templates);
		parseCurrentLoginIds(paramMap);
		
		return this.monitorService.createLinkHostToTemplate(paramMap);
	}

	@RequestMapping(value = "/hosts/{hostId}/templates/{templateId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除主机已绑定的监控模板", httpMethod = "DELETE", notes = "deleteLinkHostToTemplate", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：10107", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "templateId", value = "监控模板ID集合字符串，<br/>例子：10001", required = true, dataType = "integer",  paramType = "path")
	})
	public String deleteLinkHostToTemplate(@PathVariable Integer hostId, @PathVariable Integer templateId) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("hostid", hostId);

		List<Integer> templates = new ArrayList<Integer>();
		templates.add(templateId);
		paramMap.put("templates", templates);
		parseCurrentLoginIds(paramMap);
		
		return this.monitorService.deleteLinkHostToTemplate(paramMap);
	}

	@RequestMapping(value = "/monitorNodes", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询云资源监控", httpMethod = "GET", notes = "listMonitorNodes", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：COMPUTE", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "nodeSubType", value = "资源子类型，<br/>例子：VIR_INSTANCE，VIRTUAL", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "dataCenterId", value = "机房ID，<br/>例子：1", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "poolId", value = "资源池ID，<br/>例子：1", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "queryColumn", value = "查询字段，<br/>例子：name", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "rules", value = "查询规则，<br/>例子：key=name&type=contain&value=server", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "showMonitor", value = "是否查询监控数据，<br/>例子：current", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortGraphId", value = "根据监控报表排序，<br/>例子：CPU利用率：-1，内存利用率：-2", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：name", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorNodes(
						@RequestParam(required = false, name = "sourceNodeType") String sourceNodeType,
						@RequestParam(required = false, name = "nodeSubType") String nodeSubType,
						@RequestParam(required = false, name = "refresh") Boolean refresh,
						@RequestParam(required = false, name = "start") String start,
						@RequestParam(required = false, name = "length") String length,
						@RequestParam(required = false, name = "dataCenterId") String dataCenterId,
						@RequestParam(required = false, name = "poolId") String poolId,
						@RequestParam(required = false, name = "queryColumn") String queryColumn,
						@RequestParam(required = false, name = "rules") String rules,
						@RequestParam(required = false, name = "showMonitor") String showMonitor,
						@RequestParam(required = false, name = "sortGraphId") String sortGraphId,
						@RequestParam(required = false, name = "sortDirection") String sortDirection,
						@RequestParam(required = false, name = "instanceSort") String instanceSort,
						@RequestParam(required = false, name = "instanceSortDirection") String instanceSortDirection) {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (nodeSubType != null) {
			paramMap.put("nodeSubType", nodeSubType);
		}
		if (sourceNodeType != null) {
			paramMap.put("nodeType", sourceNodeType);
		}
		if (dataCenterId != null) {
			paramMap.put("dataCenterId", dataCenterId);
		}
		if (poolId != null) {
			paramMap.put("poolId", poolId);
		}
		if(queryColumn != null){
			paramMap.put("queryColumn", queryColumn);
		}
		if(rules != null){
			List<HashMap<String, Object>> ruleItems = new ArrayList<HashMap<String, Object>>();
			String[] ruleList = rules.split(",");
			for (String rule : ruleList) {
				HashMap<String, Object> ruleItemMap = new HashMap<String, Object>();
				String[] ri = rule.split("&");
				for (String r : ri) {
					String[] i = r.split("=");
					ruleItemMap.put(i[0], i[1]);
				}
				ruleItems.add(ruleItemMap);
			}
			paramMap.put("list", ruleItems);
		}
		if (showMonitor != null) {
			paramMap.put("showMonitor", showMonitor);
		}
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
		}
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		
		if (showMonitor != null) {
			paramMap.put("showMonitor", showMonitor);
		}
		
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());

		String result = this.monitorService.listMonitorNodes(paramMap);
		logger.debug("query listMonitorNodes successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/nodes/{nodeId}/graphs", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "为资源绑定监控图表", httpMethod = "POST", notes = "createMonitorNodesGraphs", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：1714943517", required = false, dataType = "integer",  paramType = "path"),
	})
	public String createMonitorNodesGraphs(@PathVariable Integer nodeId, @RequestBody MonitorNodeGraphAddBean monitorNodeGraph) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorNodeGraph));
		paramMap.put("nodeId", nodeId);
		parseCurrentLoginIds(paramMap);
		return this.monitorService.createMonitorNodesGraphs(paramMap);
	}

	@RequestMapping(value = "/nodes/graphs", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "为多台资源绑定监控图表", httpMethod = "POST", notes = "createMonitorMultyNodesGraphs", response = String.class)
	public String createMonitorMultyNodesGraphs(@RequestBody @ApiParam(name="monitorMultyNodeGraph", value="资源与监控图表对象", required=true)MonitorMultyNodeGraphAddBean monitorMultyNodeGraph) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorMultyNodeGraph));
		parseCurrentLoginIds(paramMap);
		logger.debug("insert createMonitorNodesGraphs successful! ");
		return this.monitorService.createMonitorMultyNodesGraphs(paramMap);
	}
	
	@RequestMapping(value = "/graphs", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询监控图表", httpMethod = "GET", notes = "listAllMonitorGraphs", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listAllMonitorGraphs(@RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}

		String result = this.monitorService.listAllMonitorGraphs(paramMap);
		logger.debug("query listAllMonitorGraphs dict successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/graphs/{graphId}/items", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询监控图表绑定的指标", httpMethod = "GET", notes = "listMonitorGraphTemplateItems", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "graphId", value = "图表ID，<br/>例子：1", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorGraphTemplateItems(@PathVariable Integer graphId, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("graphId", graphId);
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}

		String result = this.monitorService.listMonitorGraphTemplateItems(paramMap);
		logger.debug("query listMonitorGraphTemplateItems successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/graphs", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建监控图表", httpMethod = "POST", notes = "createMonitorGraphs", response = String.class)
	public String createMonitorGraphs(@RequestBody MonitorGraphCreateBean graph) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(graph));
		String result = this.monitorService.createMonitorGraphs(paramMap);
		logger.debug("insert createMonitorGraphs successful! ");
		return result;
	}

	@RequestMapping(value = "/graphs/{graphId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑监控图表", httpMethod = "PUT", notes = "updateMonitorGraphInfo", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "graphId", value = "图表ID，<br/>例子：1", required = true, dataType = "integer",  paramType = "path")
	})
	public String updateMonitorGraphInfo(@PathVariable Integer graphId, @RequestBody MonitorGraphCreateBean graph) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(graph));
		paramMap.put("graphId", graphId);
		String result = this.monitorService.updateMonitorGraphInfo(paramMap);
		logger.debug("update updateMonitorGraphInfo successful! ");
		return result;
	}

	@RequestMapping(value = "/graphs/{graphId}/items", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "绑定监控图表指标", httpMethod = "PUT", notes = "updateMonitorGraphItems", response = String.class)
	public String updateMonitorGraphItems(
			@PathVariable @ApiParam(name="graphId", value="图表ID，<br/>例子：5", required=true) Integer graphId, 
			@RequestBody @ApiParam(name="departmentResource", value="设置组织机构与资源关系对象，<br/>例子：itemId可以为：291965,291966,291967", required=true) MonitorGraphItemUpdateBean refItems) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(refItems));
		paramMap.put("graphId", graphId);
		String result = this.monitorService.updateMonitorGraphItems(paramMap);
		logger.debug("update updateMonitorGraphItems successful! ");
		return result;
	}

	@RequestMapping(value = "/graphs/{graphId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除监控图表", httpMethod = "DELETE", notes = "deleteMonitorGraph", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "graphId", value = "图表Id，<br/>例子：1", required = true, dataType = "integer",  paramType = "path")
	})
	public String deleteMonitorGraph(@PathVariable Integer graphId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("graphId", graphId);
		String result = this.monitorService.deleteMonitorGraph(paramMap);
		logger.debug("delete deleteMonitorGraph successful! ");
		return result;
	}

	@RequestMapping(value = "/nodes/{nodeId}/graphs", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源绑定的监控图表", httpMethod = "GET", notes = "listMonitorNodesGraphs", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：1666160976", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "monitor", value = "实时监控，<br/>例子：1：实时监控的图表，0：非实时监控的图表", required = false, dataType = "String",  paramType = "query")
	})
	public String listMonitorNodesGraphs(@PathVariable Integer nodeId, @RequestParam(required = false, name = "monitor") String monitor) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		if (monitor != null) {
			paramMap.put("monitor", monitor);
		}
		String result = this.monitorService.listMonitorNodesGraphs(paramMap);
		logger.debug("query listMonitorNodesGraphs dict successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/nodes/{nodeId}/graphs/items", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询资源绑定的监控图表的指标", httpMethod = "GET", notes = "listMonitorNodesGraphsItems", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：1666160976", required = true, dataType = "integer",  paramType = "path"),
		@ApiImplicitParam(name = "hostId", value = "HostId，<br/>例子：10108", required = true, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "type", value = "图表类型，<br/>例子：0：区域图，1：折线图，2：饼图", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "monitor", value = "实时监控，<br/>例子：1：实时监控的图表，0：非实时监控的图表", required = false, dataType = "String",  paramType = "query")
	})
	public String listMonitorNodesGraphsItems(@PathVariable String nodeId, @RequestParam Integer hostId, @RequestParam String type, @RequestParam(required = false, value = "monitor") String monitor) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", nodeId);
		paramMap.put("hostId", hostId);
		paramMap.put("type", type);
		if(monitor != null){
			paramMap.put("monitor", monitor);
		}

		String result = this.monitorService.listMonitorNodesGraphsItems(paramMap);
		logger.debug("query listMonitorNodesGraphsItems  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/monitorVirtuals", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	@ApiOperation(value = "查询监控的虚机", httpMethod = "GET", notes = "listMonitorVirtuals", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "refresh", value = "是否同步，<br/>例子：false", required = false, dataType = "Boolean",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorVirtuals(@RequestParam(required = false, name = "refresh") Boolean refresh, @RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (refresh != null) {
			paramMap.put("refresh", refresh);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		parseRelationLoginIds(paramMap);
		String result = this.monitorService.listMonitorVirtuals(paramMap);
		logger.debug("query listMonitorVirtuals successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询云事件", httpMethod = "GET", notes = "listMonitorEvents", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sourceNodeType", value = "资源类型，<br/>例子：VIR_INSTANCE，VIRTUAL", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "ackStatus", value = "事件确认状态，<br/>例子：0，4", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "severity", value = "事件级别，<br/>例子：INFORMATION，WARNING，DISASTER", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "keyWord", value = "关键字，<br/>例子：server", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "hostId", value = "HostId，<br/>例子：22121221", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "begin_clock", value = "开始时间，<br/>例子：2018-05-31 00:00:00", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "end_clock", value = "结束时间，<br/>例子：2018-07-31 23:59:59", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：description", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序/降序，<br/>例子：desc", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listMonitorEvents(
			@RequestParam(required = false, name = "sourceNodeType") String sourceNodeType,
			@RequestParam(required = false, name = "ackStatus") String ackStatus,
			@RequestParam(required = false, name = "severity") String severity,
			@RequestParam(required = false, name = "keyWord") String keyWord,
			@RequestParam(required = false, name = "hostId") String hostId,
            @RequestParam(required = false, name = "begin_clock") String begin_clock,
			@RequestParam(required = false, name = "end_clock") String end_clock,
			@RequestParam(required = false, name = "instanceSort") String instanceSort,
			@RequestParam(required = false, name = "instanceSortDirection") String instanceSortDirection,
			@RequestParam(required = false, name = "start") String start,
			@RequestParam(required = false, name = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (ackStatus != null) {
			paramMap.put("ackStatus", ackStatus);
		}
		if (severity != null) {
			paramMap.put("severity", severity);
		}
		if (keyWord != null) {
			paramMap.put("keyWord", keyWord);
		}
		if (hostId != null) {
			paramMap.put("hostId", hostId);
		}
		
		if (sourceNodeType != null) {
			paramMap.put("nodeType", sourceNodeType);
		}
		if (begin_clock != null) {
			paramMap.put("begin_clock", begin_clock);
		}
		if (end_clock != null) {
			paramMap.put("end_clock", end_clock);
		}

		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());

		String result = this.monitorService.listMonitorEvents(paramMap);
		logger.debug("query listMonitorEvents  successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/events/{eventId}/ack", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "确认告警事件", httpMethod = "POST", notes = "ackMonitorEvent", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eventId", value = "事件ID，<br/>例子：22920", required = true, dataType = "integer",  paramType = "path"),
	})
	public String ackMonitorEvent(@PathVariable Integer eventId, @RequestBody @ModelAttribute MonitorEventAckBean monitorEventAck) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paramMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorEventAck));
		paramMap.put("eventId", eventId);
		paramMap.put("ackStatus", 1);

		String result = this.monitorService.ackMonitorEvent(paramMap);
		logger.debug("insert ackMonitorEvent successful! ");
		return result;
	}

	@RequestMapping(value = "/event/report", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	@ApiOperation(value = "查询所有事件报表", httpMethod = "GET", notes = "ackMonitorEvent", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "20")
	})
	public String listAllEventReports(@RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}

		String result = this.monitorService.listAllEventReports(paramMap);
		logger.debug("query listAllEventReports dict successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/event/report", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String createEventReport(@RequestBody HashMap<String, Object> paramMap) {
		String result = this.monitorService.createEventReport(paramMap);
		logger.debug("insert createEventReport successful! ");
		return result;
	}

	@RequestMapping(value = "/event/report/{reportId}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String updateEventReport(@PathVariable Integer reportId, @RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("reportId", reportId);
		
		String result = this.monitorService.updateEventReport(paramMap);
		logger.debug("update updateEventReport successful! ");
		return result;
	}

	@RequestMapping(value = "/event/report/{reportId}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String deleteEventReport(@PathVariable Integer reportId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("reportId", reportId);
		
		String result = this.monitorService.deleteEventReport(paramMap);
		logger.debug("delete deleteEventReport successful! ");
		return result;
	}

	@RequestMapping(value = "/event/report/{reportId}/triggers", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String updateEventReportTriggers(@PathVariable Integer reportId, @RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("reportId", reportId);
		
		String result = this.monitorService.updateEventReportTriggers(paramMap);
		logger.debug("update updateMonitorGraphItems successful! ");
		return result;
	}

	@RequestMapping(value = "/event/report/{reportId}/triggers", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String listEventReportTriggers(@PathVariable Integer reportId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("reportId", reportId);
		String result = this.monitorService.listEventReportTriggers(paramMap);
		logger.debug("query listEventReportTriggers successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/host/{hostId}/change/status", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置监控主机状态",httpMethod = "PUT", notes = "updateMonitorHostStatus", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主机ID", required = true, dataType = "string",  paramType = "query")
	})
	public String updateMonitorHostStatus(@RequestParam(required = true, value = "id") String id, @RequestBody MonitorHostStatusUpdateBean monitorHostStatusUpdateBean){
		@SuppressWarnings("unchecked")
		HashMap<String,Object> paramMap =JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorHostStatusUpdateBean));
		paramMap.put("loginId", getCurrentLoginId());
		paramMap.put("id", id);
		return this.monitorService.updateMonitorHostStatus(paramMap);	
	}
	
	@RequestMapping(value = "/host/webscene", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建Web场景",httpMethod = "POST", notes = "createMonitorWebScene", response = String.class)
	public String createMonitorWebScene(@RequestBody MonitorWebSceneCreateBean monitorWebSceneCreateBean){
		@SuppressWarnings("unchecked")
		HashMap<String,Object> paramMap =JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorWebSceneCreateBean));
		paramMap.put("loginId", getCurrentLoginId());
		return this.monitorService.createMonitorWebScene(paramMap);	
	}
	
	@RequestMapping(value = "/host/{httptestid}/webscene", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除Web场景",httpMethod = "DELETE", notes = "deleteMonitorWebScene", response = String.class)
	public String deleteMonitorWebScene(@PathVariable @ApiParam(value = "httptestid", required = true) String httptestid, @RequestParam(required = true, value = "monitorType") String monitorType){
		List<String> httpTestIds = new ArrayList<String>();
		httpTestIds.add(httptestid);
		
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("monitorType", monitorType);
		paramMap.put("loginId", getCurrentLoginId());
		if(monitorType.equals("PING")) {
			paramMap.put("itemIds", httpTestIds);
		}else {
			paramMap.put("httptestids", httpTestIds);
		}
		return this.monitorService.deleteMonitorWebScene(paramMap);	
	}
	
	@RequestMapping(value = "/host/webscene", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑Web场景",httpMethod = "PUT", notes = "createMonitorWebScene", response = String.class)
	public String updateMonitorWebScene(@RequestBody MonitorWebSceneUpdateBean monitorWebSceneUpdateBean){
		@SuppressWarnings("unchecked")
		HashMap<String,Object> paramMap =JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorWebSceneUpdateBean));
		paramMap.put("loginId", getCurrentLoginId());
		return this.monitorService.updateMonitorWebScene(paramMap);	
	}
	
	@RequestMapping(value = "/host/webscene/{httpTestId}/change/status", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "设置Web场景状态",httpMethod = "PUT", notes = "updateMonitorHostStatus", response = String.class)
	public String updateMonitorWebSceneStatus(@PathVariable @ApiParam(value = "httpTestId", required = true) String httpTestId, @RequestBody MonitorWebSceneStatusUpdateBean monitorWebSceneStatusUpdateBean){
		@SuppressWarnings("unchecked")
		HashMap<String,Object> paramMap =JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorWebSceneStatusUpdateBean));
		paramMap.put("loginId", getCurrentLoginId());
		if(paramMap.containsKey("itemId")) {
			paramMap.put("httptestid", paramMap.get("itemId"));
		}
		return this.monitorService.updateMonitorWebSceneStatus(paramMap);	
	}
	
	
	//网络设备监控接口
	//查询/搜索接口
	@RequestMapping(value = "/monitorNetwork", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询网络设备监控", httpMethod = "GET", notes = "listMonitorNetwork", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "type", value = "类型搜索", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "name", value = "名称关键字搜索", required = false, dataType = "string",  paramType = "query")
	})
	public String listMonitorNetwork(@RequestParam(required = false, name = "type")  String type,
			@RequestParam(required = false, name = "key") String key){
		HashMap<String,Object> paramMap =new HashMap<String,Object>();
		if(type != null) {
			paramMap.put("type", type);
		}
		if(key != null) {
			paramMap.put("key", key);
		}
		
	
		return this.monitorService.listMonitorNetwork(paramMap);	
		
	}
	
	//创建网络设备监控接口
	@RequestMapping(value = "/monitorNetwork", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "创建网络设备监控",httpMethod = "POST", notes = "createMonitorNetwork", response = String.class)
	public String createMonitorNetwork(@RequestBody MonitorNetworkCreateBean monitorNetworkCreateBean){
		@SuppressWarnings("unchecked")
		HashMap<String,Object> paramMap =JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorNetworkCreateBean));
		return this.monitorService.createMonitorNetwork(paramMap);	
	}
	
	//更新修改接口
	@RequestMapping(value = "/monitorNetwork/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "编辑网络设备监控",httpMethod = "PUT", notes = "updateMonitorNetwork", response = String.class)
	public String updateMonitorNetwork(
			@PathVariable @ApiParam(value = "id", required = true) String id,
			@RequestBody MonitorNetworkUpdateBean monitorNetworkUpdateBean)
	{
    	@SuppressWarnings("unchecked")
		HashMap<String,Object> paramMap =JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(monitorNetworkUpdateBean));
    	paramMap.put("id", id);
		return this.monitorService.updateMonitorNetwork(paramMap);	
	}

	//删除监控接口
	
	@RequestMapping(value = "/monitorNetwork/{id}", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除网络设备监控",httpMethod = "DELETE", notes = "deleteMonitorNetwork", response = String.class)
	public String deleteMonitorNetwork(@PathVariable @ApiParam(value = "id", required = true) String id) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		return this.monitorService.deleteMonitorNetwork(paramMap);		
	}

	
	//动环湿度，温度，烟感功率历史数据监测
	@RequestMapping(value = "/DhroomHistory", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "动环历史监测数据",httpMethod = "GET", notes = "listdhHistoryRoom", response = String.class)
	public String listdhHistoryRoom(){		
		HashMap<String,Object> paramMap =new HashMap<String,Object>();
		//获取当前时间
		SimpleDateFormat timesystem = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		String createDate = timesystem.format(date);
		paramMap.put("createDate", createDate);
		
		//获取当前月，确定表名
		SimpleDateFormat getmonth = new SimpleDateFormat("M");
		String month = getmonth.format(date);
		paramMap.put("month", month);
		
		//将当前时间减上一天，做条件查询
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-1);
		date= calendar.getTime();
		String beforetime = timesystem.format(date);
		paramMap.put("beforetime", beforetime);
		return this.monitorService.listdhHistoryRoom(paramMap);
		
	}
  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
