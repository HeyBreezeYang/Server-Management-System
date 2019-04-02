package com.system.started.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.system.started.constant.GlobalConst;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;

/**
 * @Author wyj
 * @Date 2017/12/12
 * @Description 定时任务管理
 */
@Component
public class TaskManageService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(TaskManageService.class);

	public final Integer catalogId = 47; //定时任务的id 查表 operation_service_catalog

	@Value("${oss.apigate.config.admin-login-id}")
	private static String ADMIN_LOGIN_ID;

//	@Autowired
//	private QuartzManager quartzManager;

	@Autowired
	private DBService dbService;


	private String getJobName(Integer templateId) {
		return GlobalConst.QUARTZ_JOB_NAME + templateId;
	}

	private String getTriggerJobName(Integer templateId) {
		return GlobalConst.QUARTZ_JOB_NAME + templateId;
	}

	private String getJobName(Integer templateId, Integer index) {
		return GlobalConst.QUARTZ_JOB_NAME + templateId + "_" + index;
	}

	private String getTriggerJobName(Integer templateId, Integer index) {
		return GlobalConst.TRIGGER_JOB_NAME + templateId + "_" + index;
	}

	public void init() {
		try {
			//获取所有的定时任务
			logger.debug("---------------------------------------------------------------------------------------");
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put("status", 1); //0：停止，1：正在进行
			List<HashMap<String, Object>> resultMap = dbService.directSelect(DBServiceConst.SELECT_OPERATION_TIMED_TASK, paramMap);
			logger.debug("the taskManageService  result  is :" + JsonHelper.toJson(resultMap));

			//添加任务到Quartz
			this.addAllTasks2Quartz(resultMap);
		} catch (Exception e) {
			logger.error("the taskManageService task init fail" + e);
		} finally {
			logger.debug("the taskManageService task init success");
		}


	}


	/**
	 * 添加任务集合到Quartz任务管理
	 *
	 * @param tasks
	 */
	public void addAllTasks2Quartz(List<HashMap<String, Object>> tasks) {
		for (HashMap<String, Object> task : tasks) {
			this.addTasks2Quartz(task);
		}
	}




	public void addTasks2Quartz(HashMap<String, Object> task) {
		try {
			if (task.containsKey("expression") && null != task.get("expression")) {
				Integer id = Integer.parseInt(task.get("id").toString());
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("task", task);
				params.put("loginId", ADMIN_LOGIN_ID);
//				params.put("clazz", OperationServiceTask.class);
				JSONObject jsonObject = (JSONObject) JSON.parse(task.get("expression").toString());
				JSONArray jsonArray = jsonObject.getArray("data");
				boolean isEnd = true;

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getObject(i);
					String cron = obj.get("cron").toString();
					CronExpression exp = new CronExpression(cron);
					Date d = new Date();
					params.put("cron",cron);
					if(exp.getNextValidTimeAfter(d) != null || exp.getNextValidTimeAfter(d).compareTo(d) > 0){
						isEnd = false;//说明该任务还能执行
						this.updateTaskNextRunDate(id,exp.getNextValidTimeAfter(d));
//						this.quartzManager.addJob(
//								getJobName(id, i),
//								GlobalConst.QUARTZ_JOB_GROUP,
//								getTriggerJobName(id, i),
//								GlobalConst.TRIGGER_JOB_GROUP,
//								OperationServiceTask.class,
//								cron, params
//						);
						logger.debug("add task into Quartz success");
					}
				}
				if(isEnd){
					this.endTask(id);
				}
			}
		} catch (Exception e) {
			logger.error("add task into Quartz fail !! " + e);
		}
	}

	/**
	 * 更新任务的定时表达式
	 *
	 * @param task 任务
	 */
	public void updateTask(HashMap<String, Object> task) {
		try {
			Integer id = Integer.parseInt(task.get("id").toString());
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("task", task);
			params.put("loginId", ADMIN_LOGIN_ID);
//			params.put("clazz", OperationServiceTask.class);
			JSONObject jsonObject = (JSONObject) JSON.parse(task.get("expression").toString());
			JSONArray jsonArray = jsonObject.getArray("data");
			boolean isEnd = true;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getObject(i);
				String cron = obj.get("cron").toString();
				CronExpression exp = new CronExpression(cron);
				Date d = new Date();
				params.put("cron",cron);
				if(exp.getNextValidTimeAfter(d) != null || exp.getNextValidTimeAfter(d).compareTo(d) > 0){
					this.updateTaskNextRunDate(id,exp.getNextValidTimeAfter(d));
					isEnd = false;//说明该任务还能执行
//					this.quartzManager.modifyJobTime(
//							getJobName(id, i),
//							GlobalConst.QUARTZ_JOB_GROUP,
//							getTriggerJobName(id, i),
//							GlobalConst.TRIGGER_JOB_GROUP,
//							cron, params
//					);
					logger.debug("add task into Quartz success");
				}
			}
			if(isEnd){
				this.endTask(id);
			}

		} catch (Exception e) {
			logger.error("update task into Quartz fail !! " + e);
		}


	}

	public void endTask(Integer id){
		//结束任务
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("id", id);
		parameter.put("status", 0);
		dbService.update(DBServiceConst.UPDATE_OPERATION_TIMED_TASK, parameter);
		logger.debug("task:"+id+" is end");
	}

	public void updateTaskNextRunDate(Integer id,Date d){
		//结束任务
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("id", id);
		parameter.put("nextRunDate", d);
		dbService.update(DBServiceConst.UPDATE_OPERATION_TIMED_TASK, parameter);
	}

	/**
	 * 移除某一个任务
	 *
	 * @param templateId 任务id
	 */
	public void deleteTask(Integer templateId, String expression) {
		try {
			JSONObject jsonObject = (JSONObject) JSON.parse(expression);
			JSONArray jsonArray = jsonObject.getArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
//				this.quartzManager.removeJob(getJobName(templateId, i),
//						GlobalConst.QUARTZ_JOB_GROUP,
//						getTriggerJobName(templateId, i),
//						GlobalConst.TRIGGER_JOB_GROUP);
			}
		} catch (Exception e) {
			logger.error("delete task into Quartz fail !! " + e);
		}

	}



}
