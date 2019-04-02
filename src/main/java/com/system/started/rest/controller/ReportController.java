package com.system.started.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(value = "/report")
@Api(value="/monitor", description="报表管理控制器")
public class ReportController extends AbstractController {
	private static Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/workOrder", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	@ApiOperation(value = "查询工单", httpMethod = "GET", notes = "listWorkOrderReport", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "STATUS", value = "状态，<br/>例子：0,1,2,4", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "BEGIN_CREATE_TS", value = "开始时间，<br/>例子：2017-05-01 00:00:00", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "END_CREATE_TS", value = "结束时间，<br/>例子：2017-08-01 23:59:59", required = false, dataType = "string",  paramType = "query")
	})
	public String listWorkOrderReport(@RequestParam(required = false, value = "STATUS") String status, @RequestParam(required = false, value = "BEGIN_CREATE_TS") String begin_create_ts, @RequestParam(required = false, value = "END_CREATE_TS") String end_create_ts) {
		logger.debug("the query condition is :");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if (status != null) {
			paramMap.put("DEAL_STATUS_CODE", status);
		}
		
		if (begin_create_ts != null) {
			paramMap.put("BEGIN_CREATE_TS", begin_create_ts);
		}
		
		if (end_create_ts != null) {
			paramMap.put("END_CREATE_TS", end_create_ts);
		}

		parseRelationLoginIds(paramMap);

		String result = this.reportService.listWorkOrderReport(paramMap);
		
		logger.debug("query listWorkOrderReport successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/physicalDevice", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String listPhysicalDeviceReport() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		parseRelationLoginIds(paramMap);

		String result = this.reportService.listPhysicalDeviceReport(paramMap);
		
		logger.debug("query listPhysicalDeviceReport successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/instance", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询当前用户的资源数量", httpMethod = "GET", notes = "listInstanceReport", response = String.class)
	public String listInstanceReport() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		
		String result = this.reportService.listInstanceReport(paramMap);
		
		logger.debug("query listInstanceReport successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/businessInstance", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String listBusinessInstanceReport() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		parseRelationLoginIds(paramMap);

		String result = this.reportService.listBusinessInstanceReport(paramMap);
		
		logger.debug("query listBusinessInstanceReport successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/computeResourcePoolUtilization", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询计算资源池的统计信息", httpMethod = "GET", notes = "listReportComputeResourcePoolUtilization", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "regionName", value = "Region名称，<br/>例子：manageRegion", required = true, dataType = "string",  paramType = "query"),
	})
	public String listReportComputeResourcePoolUtilization(@RequestParam(required = true, value = "regionName") String regionName) {		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("regionName", regionName);
		parseCurrentLoginIds(paramMap);
		
		return this.reportService.listReportComputeResourcePoolUtilization(paramMap);
	}

	@RequestMapping(value = "/storageResourcePoolUtilization", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiIgnore
	public String listReportStorageResourcePoolUtilization() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		parseRelationLoginIds(paramMap);

		String result = this.reportService.listReportStorageResourcePoolUtilization(paramMap);
		
		logger.debug("query listReportStorageResourcePoolUtilization successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/{typeInstance}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "统计资源池/机房下的资源数量", httpMethod = "GET", notes = "listReportResource", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "typeInstance", value = "统计方式，<br/>例子：poolInstance，datacenterInstance", required = true, dataType = "string",  paramType = "path"),
		@ApiImplicitParam(name = "type", value = "类型，<br/>例子：virtual，hypervisor，hypervisor", required = true, dataType = "string",  paramType = "query"),
	})
	public String listReportResource(@PathVariable String typeInstance, @RequestParam(required = true, value = "type") String type) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		parseRelationLoginIds(paramMap);
		paramMap.put("typeInstance", typeInstance);
		paramMap.put("type", type);
		paramMap.put("curLoginId", getCurrentLoginId());
		
		String result = this.reportService.listReportResource(paramMap);
		
		logger.debug("query listReportResource successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/task", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看任务报表", httpMethod = "GET", notes = "listReportResourceTask", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "propTags", value = "统计分类，<br/>例子：项目", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527264001", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529942399", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "timeCount", value = "统计方式，<br/>例子：DAY，不填默认按DAY统计", required = false, dataType = "string",  paramType = "query")
	})
	public String listReportResourceTask(
			@RequestParam(required = true, value = "propTags") String propTags,
			@RequestParam(required = true, value = "startDate") String startDate,
			@RequestParam(required = true, value = "endDate") String endDate,
			@RequestParam(required = false, value = "timeCount") String timeCount) {
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if(propTags != null){
			paramMap.put("array", propTags.split(","));
		}
		
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		
		if(timeCount != null){
			paramMap.put("reportType", timeCount);
		}else{
			paramMap.put("reportType", "DAY");
		}
		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String result = this.reportService.listReportResourceTask(paramMap);
		
		logger.debug("query listReportResourceTask successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/task/instance", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看任务报表中的虚机", httpMethod = "GET", notes = "listReportResourceTaskInstance", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "type", value = "操作类型，<br/>例子：DELETED,CREATED", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "countType", value = "统计分类，<br/>例子：项目", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "countValue", value = "统计分类值，<br/>例子：开发测试", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527264001", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529942399", required = true, dataType = "string",  paramType = "query")
	})
	public String listReportResourceTaskInstance(
			@RequestParam(required = true, value = "type") String type,
			@RequestParam(required = true, value = "countType") String countType,
			@RequestParam(required = true, value = "countValue") String countValue,
			@RequestParam(required = true, value = "startDate") String startDate,
			@RequestParam(required = true, value = "endDate") String endDate) {
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if(type != null){
			paramMap.put("type", type);
		}
		if(countType != null){
			paramMap.put("countType", countType);
		}
		if(countValue != null){
			paramMap.put("countValue", countValue);
		}
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		
		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String result = this.reportService.listReportResourceTaskInstance(paramMap);
		
		logger.debug("query listReportResourceTaskInstance successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/resource/charge", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看计量计费统计报表", httpMethod = "GET", notes = "listReportResourceCharge", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "propTags", value = "统计方式，<br/>例子：项目，部门，用户", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527004801", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529683199", required = false, dataType = "string",  paramType = "query"),
	})
	public String listReportResourceCharge(
			@RequestParam(required = false, value = "propTags") String propTags,
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(propTags != null){
			paramMap.put("array", propTags.split(","));
		}
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String result = this.reportService.listReportResourceCharge(paramMap);
		
		logger.debug("query listReportResourceCharge successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/charge/detail", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看指定统计方式下的虚机计量计费明细", httpMethod = "GET", notes = "listReportResourceChargeDetail", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "itemValue", value = "统计方式值，<br/>例子：开发测试", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "propTags", value = "统计方式，<br/>例子：项目", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527004801", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529683199", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "string",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "string",  paramType = "query", defaultValue = "10")
	})
	public String listReportResourceChargeDetail(
			@RequestParam(required = false, value = "itemValue") String itemValue,
			@RequestParam(required = false, value = "propTags") String propTags,
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate,
			@RequestParam(required = false, value = "start") String start, 
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if(itemValue != null){
			paramMap.put("itemValue", itemValue);
		}
		
		if(propTags != null){
			paramMap.put("array", propTags.split(","));
		}
		
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}

		parseRelationLoginIds(paramMap);
		paramMap.put("curLoginId", getCurrentLoginId());
		
		String result = this.reportService.listReportResourceChargeDetail(paramMap);
		
		logger.debug("query listReportResourceCharge successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/charge/detail/list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看指定虚机的计量计费明细", httpMethod = "GET", notes = "listReportResourceChargeDetailList", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nodeId", value = "资源ID，<br/>例子：1714943517", required = true, dataType = "integer",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：2018-5-23 00:00:01", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：2018-6-22 23:59:59", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "10")
	})
	public String listReportResourceChargeDetailList(
			@RequestParam(required = true, value = "nodeId") Integer nodeId,
			@RequestParam(required = true, value = "startDate") String startDate,
			@RequestParam(required = true, value = "endDate") String endDate,
			@RequestParam(required = false, value = "start") String start, 
			@RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if(nodeId != null){
			paramMap.put("nodeId", nodeId);
		}
		
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		String result = this.reportService.listReportResourceChargeDetailList(paramMap);

		logger.debug("query listReportResourceChargeDetailList successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/resourcePool/usage", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看资源池使用量报表", httpMethod = "GET", notes = "listReportResourcePoolUsage", response = String.class)
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "timeCount", value = "统计方式，<br/>例子：DAY、WEEK、MONTH,QUARTER,YEAR", required = true, dataType = "string",  paramType = "query"),
//		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527004801", required = true, dataType = "string",  paramType = "query"),
//		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529683199", required = true, dataType = "string",  paramType = "query"),
//	})
	public String listReportResourcePoolUsage(
			@RequestParam(required = false, value = "propTags") String propTags,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection) {
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("countType", "REPORT");
		
		if(propTags != null){
			paramMap.put("array", propTags.split(","));
		}
		
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String result = this.reportService.listReportResourcePoolUsage(paramMap);
		
		logger.debug("query listReportResourcePoolUsage successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/lifeCycle/count", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看生命周期统计报表", httpMethod = "GET", notes = "listReportResourceLifeCycleList", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "propTags", value = "统计方式，<br/>例子：项目、部门、用户", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSort", value = "排序字段，<br/>例子：greaterThirtyCountValue，overDueCountValue，neverExpireCountValue", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "instanceSortDirection", value = "升序或者降序，<br/>例子：asc，desc", required = true, dataType = "string",  paramType = "query"),
	})
	public String listReportResourceLifeCycleCount(
			@RequestParam(required = false, value = "propTags") String propTags,
			@RequestParam(required = false, value = "instanceSort") String instanceSort,
			@RequestParam(required = false, value = "instanceSortDirection") String instanceSortDirection) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if(propTags != null){
			paramMap.put("array", propTags.split(","));
		}
		
		if (instanceSort != null) {
			paramMap.put("instanceSort", instanceSort);
		}
		
		if (instanceSortDirection != null) {
			paramMap.put("instanceSortDirection", instanceSortDirection);
		}
		
		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String[] condtionArray = {"greaterThirtyCountValue","lessThanEtcThirtyCountValue","lessThanEtcSevenCountValue","overDueCountValue","greaterThirtyOverDueCountValue","neverExpireCountValue"};
		paramMap.put("condtionArray", condtionArray);
		
		String result = this.reportService.listReportResourceLifeCycleCount(paramMap);

		logger.debug("query listReportResourceLifeCycleCount successful! the result is :" + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/lifeCycle/list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查看生命周期项虚机列表", httpMethod = "GET", notes = "listReportResourceLifeCycleList", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "filter", value = "资源所在分组，<br/>例子：项目:开发测试", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "lifeCycle", value = "生命周期类型，<br/>例子：overDueCountValue，neverExpireCountValue", required = true, dataType = "string",  paramType = "query"),
	})
	public String listReportResourceLifeCycleList(
			@RequestParam(required = true, value = "filter") String filter,
			@RequestParam(required = true, value = "lifeCycle") String lifeCycle) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(filter != null){
			List<HashMap<String,String>> filterList = new ArrayList<>();
			String[] filterArray = filter.split(",");
			for (int i = 0; i < filterArray.length; i++) {
				String filterItem = filterArray[i];
				
				HashMap<String, String> filterItemMap = new HashMap<>();
				filterItemMap.put("name", filterItem.split(":")[0]);
				filterItemMap.put("value", filterItem.split(":")[1]);
				filterList.add(filterItemMap);
			}
			
			paramMap.put("filter", filterList);
		}
		
		if(lifeCycle != null){
			paramMap.put("lifeCycle", lifeCycle);
		}

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String result = this.reportService.listReportResourceLifeCycleList(paramMap);

		logger.debug("query listReportResourceLifeCycleList successful! the result is :" + result);
		return result;
	}

	@RequestMapping(value = "/resource/moinitor/servers/count", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看虚机利用率统计报表", httpMethod = "GET", notes = "listResourceMonitorServerCount", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "propTags", value = "统计方式，<br/>例子：项目，部门，用户", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "length", value = "排名数量，<br/>例子：10", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortGraphId", value = "排序字段，<br/>例子：-1：CPU利用率，-2：内存利用率", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortDirection", value = "升序或降序，<br/>例子：asc，desc", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortType", value = "排序类型，<br/>例子：value_max，value_avg，value_min", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527004801", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529683199", required = false, dataType = "string",  paramType = "query")
	})
	public String listResourceMonitorServerCount(
			@RequestParam(required = true, value = "propTags") String propTags,
			@RequestParam(required = false, value = "length") String length, //top n，如10，100等
			@RequestParam(required = false, value = "sortGraphId") String sortGraphId,//取值范围：-1/-2
			@RequestParam(required = false, value = "sortDirection") String sortDirection, //取值范围：asc/desc
			@RequestParam(required = false, value = "sortType") String sortType, //取值范围： value_max/value_avg
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if(propTags != null){
			paramMap.put("array", propTags.split(","));
		}
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
		}
		if (sortType != null) {
			paramMap.put("sortType", sortType);
		}
		if (length != null) {
//			paramMap.put("beginIndex", 0);
//			paramMap.put("perPage", Integer.parseInt(length));
		}
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		paramMap.put("showMonitor", "TOP");

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);

		String result = this.reportService.listResourceMonitorServerCount(paramMap);
		
		logger.debug("query listResourceMonitorServerTop successful! the result is : " + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/moinitor/servers/count/item/detail", method = RequestMethod.GET)
	@ResponseBody
	@ApiIgnore
	public String listResourceMonitorServerCountItemDetail(
			@RequestParam(required = false, value = "hostOrders") String hostOrders, 
			@RequestParam(required = false, value = "sortGraphId") String sortGraphId,//取值范围：-1/-2
			@RequestParam(required = false, value = "sortDirection") String sortDirection, //取值范围：asc/desc
			@RequestParam(required = false, value = "sortType") String sortType, //取值范围： value_max/value_avg
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate,
			@RequestParam(required = false, value = "start") String start, 
			@RequestParam(required = false, value = "length") String length) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if(hostOrders != null){
			paramMap.put("hostIds", hostOrders);
		}
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
		}
		if (sortType != null) {
			paramMap.put("sortType", sortType);
		}
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		if (length != null) {
			paramMap.put("beginIndex", 0);
			paramMap.put("perPage", Integer.parseInt(length));
		}
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		paramMap.put("showMonitor", "TOP");

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String result = this.reportService.listResourceMonitorServerCountItemDetail(paramMap);
		
		logger.debug("query listResourceMonitorServerCountItemDetail successful! the result is : " + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/moinitor/servers/count/detail", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看虚机利用率统计明细报表", httpMethod = "GET", notes = "listResourceMonitorServerCountDetail", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "propTags", value = "统计方式，<br/>例子：项目，部门，用户", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "length", value = "排名数量，<br/>例子：10", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortGraphId", value = "排序字段，<br/>例子：-1：CPU利用率，-2：内存利用率", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortDirection", value = "升序或降序，<br/>例子：asc，desc", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortType", value = "排序类型，<br/>例子：value_max，value_avg，value_min", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527004801", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529683199", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "propValue", value = "统计方式值，<br/>例子：开发测试", required = false, dataType = "string",  paramType = "query")
	})
	public String listResourceMonitorServerCountDetail(
			@RequestParam(required = true, value = "propTags") String propTags,
			@RequestParam(required = false, value = "length") String length, //top n，如10，100等
			@RequestParam(required = false, value = "sortGraphId") String sortGraphId,//取值范围：-1/-2
			@RequestParam(required = false, value = "sortDirection") String sortDirection, //取值范围：asc/desc
			@RequestParam(required = false, value = "sortType") String sortType, //取值范围： value_max/value_avg
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate,
			@RequestParam(required = true, value = "propValue") String propValue) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if(propTags != null){
			paramMap.put("array", propTags.split(","));
		}
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
		}
		if (sortType != null) {
			paramMap.put("sortType", sortType);
		}
		if (length != null) {
			paramMap.put("beginIndex", 0);
			paramMap.put("perPage", Integer.parseInt(length));
		}
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		if(propValue != null){
			paramMap.put("propValue", propValue);
		}
		paramMap.put("showMonitor", "TOP");

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);
		
		String result = this.reportService.listResourceMonitorServerCountDetail(paramMap);
		
		logger.debug("query listResourceMonitorServerCountDetail successful! the result is : " + result);
		return result;
	}
	
	@RequestMapping(value = "/resource/moinitor/servers/top", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看虚机利用率排名报表", httpMethod = "GET", notes = "listResourceMonitorServerTop", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "length", value = "排名数量，<br/>例子：10", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortGraphId", value = "排序字段，<br/>例子：-1：CPU利用率，-2：内存利用率", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortDirection", value = "升序或降序，<br/>例子：asc，desc", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortType", value = "排序类型，<br/>例子：value_max，value_avg，value_min", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527004801", required = false, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529683199", required = false, dataType = "string",  paramType = "query")
	})
	public String listResourceMonitorServerTop(
			@RequestParam(required = false, value = "length") String length, //top n，如10，100等
			@RequestParam(required = false, value = "sortGraphId") String sortGraphId,//取值范围：-1/-2
			@RequestParam(required = false, value = "sortDirection") String sortDirection, //取值范围：asc/desc
			@RequestParam(required = false, value = "sortType") String sortType, //取值范围： value_max/value_avg
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
		}
		if (sortType != null) {
			paramMap.put("sortType", sortType);
		}else{
			paramMap.put("sortType", "value_max");
		}
		
		if (length != null) {
			paramMap.put("beginIndex", 0);
			paramMap.put("perPage", Integer.parseInt(length));
		}
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		
		paramMap.put("showMonitor", "TOP");

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);

		String result = this.reportService.listResourceMonitorServerTop(paramMap);
		
		logger.debug("query listResourceMonitorServerTop successful! the result is : " + result);
		return result;
	}

	@RequestMapping(value = "/resource/moinitor/servers/top/{hostId}/detail", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看指定虚机利用率排名明细", httpMethod = "GET", notes = "listResourceMonitorServerTopDetail", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hostId", value = "hostId，<br/>例子：1377264512", required = true, dataType = "string",  paramType = "path"),
		@ApiImplicitParam(name = "sortGraphId", value = "排序字段，<br/>例子：-1：CPU利用率，-2：内存利用率", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortDirection", value = "升序或降序，<br/>例子：asc，desc", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "sortType", value = "排序类型，<br/>例子：value_max，value_avg，value_min", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "startDate", value = "开始时间，<br/>例子：1527004801", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "endDate", value = "结束时间，<br/>例子：1529683199", required = true, dataType = "string",  paramType = "query"),
		@ApiImplicitParam(name = "start", value = "开始索引", required = false, dataType = "String",  paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "length", value = "条数", required = false, dataType = "String",  paramType = "query", defaultValue = "20")
	})
	public String listResourceMonitorServerTopDetail(
			@PathVariable Integer hostId,
			@RequestParam(required = true, value = "sortGraphId") String sortGraphId,//取值范围：-1/-2
			@RequestParam(required = true, value = "sortDirection") String sortDirection, //取值范围：asc/desc
			@RequestParam(required = true, value = "sortType") String sortType, //取值范围： value_max/value_avg
			@RequestParam(required = true, value = "startDate") String startDate,
			@RequestParam(required = true, value = "endDate") String endDate,
			@RequestParam(required = false, value = "start") String start, 
			@RequestParam(required = false, value = "length") String length) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("hostId", hostId);

		if (sortGraphId != null) {
			paramMap.put("sortGraphId", sortGraphId);
		}
		if (sortDirection != null) {
			paramMap.put("sortDirection", sortDirection);
		}
		if (sortType != null) {
			paramMap.put("sortType", sortType);
		}
		if(startDate != null){
			paramMap.put("beginTimeStamp", startDate);
		}
		if(endDate != null){
			paramMap.put("endTimeStamp", endDate);
		}
		
		if (start != null) {
			paramMap.put("start", start);
		}
		
		if (length != null) {
			paramMap.put("length", length);
		}
		
		paramMap.put("showMonitor", "TOP");

		paramMap.put("curLoginId", getCurrentLoginId());
		parseRelationLoginIds(paramMap);

		String result = this.reportService.listResourceMonitorServerTopDetail(paramMap);
		
		logger.debug("query listResourceMonitorServerTopDetail successful! the result is : " + result);
		return result;
	}
}
