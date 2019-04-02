package com.system.started.job.elastic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.system.started.action.impl.IMonitorAction;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.monitor.EMonitorPanelType;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component("monitorHostInfoSyncJobImpl")
public class MonitorHostInfoSyncJob extends AbstractDhSyncJob implements SimpleJob {

	private final static Logger logger = LoggerFactory.getLogger(MonitorHostInfoSyncJob.class);
	
	@Value("${oss.apigate.config.admin-login-id}")
	private static String ADMIN_LOGIN_ID;
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private IMonitorAction monitorActionImpl;
	
	@Override
	public void execute(ShardingContext shardingContext) {
		monitorHostCpuSync();
		monitorHostMemorySync();
	}
	
	private void monitorHostCpuSync() {
		try {
			HashMap<String, Object> defaultGraphParamMap = new HashMap<String, Object>();
			defaultGraphParamMap.put("ids", "-8");
			HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
			
//			List<HashMap<String, Object>> monitorHostList = dbService.directSelect(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
			HashMap<String, Object> testMap = new HashMap<String, Object>();
			testMap.put("id", 601999570);
			testMap.put("monitorHostId", 17927);
			List<HashMap<String, Object>> monitorHostList = new ArrayList<HashMap<String, Object>>();
			monitorHostList.add(testMap);
			
			for (HashMap<String, Object> monitorHostMap : monitorHostList) {
				int id = Integer.parseInt(monitorHostMap.get("id").toString());
				int monitorHostId = Integer.parseInt(monitorHostMap.get("monitorHostId").toString());
				
				parseMonitorGraphs(monitorHostId, graphResultMap);
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
				if(graphRecord.size()>0) {
					HashMap<String, Object> graph = graphRecord.get(0);
					@SuppressWarnings("unchecked")
					List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

					List<Integer> itemList = new ArrayList<>();
					for (int j = 0; j < refItems.size(); j++) {
						HashMap<String, Object> refItem = refItems.get(j);
						itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
					}

					HashMap<String, Object> actionParamMap = new HashMap<>();
					actionParamMap.put("endTime", new Date().getTime() / 1000);
					actionParamMap.put("itemList", itemList);
					actionParamMap.put("panelType", EMonitorPanelType.MULITY);
					actionParamMap.put("length", 1);

					try {
						Result result = monitorActionImpl.doExcutionAction(ADMIN_LOGIN_ID, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
						if (result.getResultCode().equals(EResultCode.SUCCESS)) {
							List<HashMap<String, Object>> resultList = result.getResultObj();
							HashMap<String, Object> itemDataMap = resultList.get(0);
							List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

							for (int j = 0; j < itemKeys.size(); j++) {
								@SuppressWarnings("unchecked")
								List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) itemDataMap.get(itemKeys.get(j));
								HashMap<String, Object> dataMap = dataList.get(0);
								
								HashMap<String, Object> parameters = new HashMap<String, Object>(); 
								parameters.put("id", id);
								parameters.put("cpu", dataMap.get("value"));
								dbService.update(DBServiceConst.UPDATE_MONITOR_HOST, parameters);
							}
						}
					} catch (Exception e) {
						logger.error("query monitor host cpu data error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("monitor host info sync error !", e);
		}
	}
	
	private void monitorHostMemorySync() {
		try {
			HashMap<String, Object> defaultGraphParamMap = new HashMap<String, Object>();
			defaultGraphParamMap.put("ids", "-9");
			HashMap<String, Object> graphResultMap = dbService.select(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
			
//			List<HashMap<String, Object>> monitorHostList = dbService.directSelect(DBServiceConst.SELECT_MONITOR_NODE_DEFAULT_GRAPHS, defaultGraphParamMap);
			HashMap<String, Object> testMap = new HashMap<String, Object>();
			testMap.put("id", 601999570);
			testMap.put("monitorHostId", 17927);
			List<HashMap<String, Object>> monitorHostList = new ArrayList<HashMap<String, Object>>();
			monitorHostList.add(testMap);
			
			for (HashMap<String, Object> monitorHostMap : monitorHostList) {
				int id = Integer.parseInt(monitorHostMap.get("id").toString());
				int monitorHostId = Integer.parseInt(monitorHostMap.get("monitorHostId").toString());
				
				parseMonitorGraphs(monitorHostId, graphResultMap);
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> graphRecord = (List<HashMap<String, Object>>) graphResultMap.get("record");
				if(graphRecord.size()>0) {
					HashMap<String, Object> graph = graphRecord.get(0);
					@SuppressWarnings("unchecked")
					List<HashMap<String, Object>> refItems = (List<HashMap<String, Object>>) graph.get("refItems");

					List<Integer> itemList = new ArrayList<>();
					for (int j = 0; j < refItems.size(); j++) {
						HashMap<String, Object> refItem = refItems.get(j);
						itemList.add(Integer.parseInt(refItem.get("itemid").toString()));
					}

					HashMap<String, Object> actionParamMap = new HashMap<>();
					actionParamMap.put("endTime", new Date().getTime() / 1000);
					actionParamMap.put("itemList", itemList);
					actionParamMap.put("panelType", EMonitorPanelType.MULITY);
					actionParamMap.put("length", 1);

					try {
						Result result = monitorActionImpl.doExcutionAction(ADMIN_LOGIN_ID, EAction.MONITOR_GET_HOST_PANEL, actionParamMap);
						if (result.getResultCode().equals(EResultCode.SUCCESS)) {
							List<HashMap<String, Object>> resultList = result.getResultObj();
							HashMap<String, Object> itemDataMap = resultList.get(0);
							List<String> itemKeys = new ArrayList<String>(itemDataMap.keySet());

							for (int j = 0; j < itemKeys.size(); j++) {
								@SuppressWarnings("unchecked")
								List<HashMap<String, Object>> dataList = (List<HashMap<String, Object>>) itemDataMap.get(itemKeys.get(j));
								HashMap<String, Object> dataMap = dataList.get(0);
								long value = Long.parseLong(dataMap.get("value").toString());
								value = value / 1024 / 1024 / 1024;
								
								BigDecimal memoryValue = new BigDecimal(value);
						        Double memory = memoryValue.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue();
								
								HashMap<String, Object> parameters = new HashMap<String, Object>(); 
								parameters.put("id", id);
								parameters.put("memory", memory);
								dbService.update(DBServiceConst.UPDATE_MONITOR_HOST, parameters);
							}
						}
					} catch (Exception e) {
						logger.error("query monitor host memory data error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("monitor host info sync error !", e);
		}
	}
	
	private void parseMonitorGraphs(Integer hostId, HashMap<String, Object> graphResultMap) {
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> graphsList = (List<HashMap<String, Object>>) graphResultMap.get("record");
		for (int i = 0; i < graphsList.size(); i++) {
			HashMap<String, Object> graphItem = graphsList.get(i);

			Integer graphId = (Integer) graphItem.get("id");

			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("graphId", graphId);
			paramMap.put("hostId", hostId);

			List<HashMap<String, Object>> graphRefItemsList = dbService.directSelect(DBServiceConst.SELECT_MONITOR_GRAPH_ITEMS, paramMap);
			graphItem.put("refItems", graphRefItemsList);
		}
	}
}
