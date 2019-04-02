package com.system.started.process.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.process.EProcessStatus;
import com.system.started.process.EProcessType;
import com.vlandc.oss.common.CommonTools;

@SuppressWarnings("unchecked")
public class WorkOrderServiceListener implements IProcessListener {

	private static Logger logger = Logger.getLogger(WorkOrderServiceListener.class);
	@Autowired
	private DBService dbService;

	@Override
	public void initLisenter() {

	}

	@Override
	public void dealPorcess(HashMap<String, Object> processItemMap, EProcessStatus status, String statusResult, HashMap<String, Object> paramMap) {
		try {
			String processType = (String) processItemMap.get("type");
			if (!processType.equals(EProcessType.WORK_ORDER_CHECK.toString())) {
				return;
			}
			byte[] requestByteArray = (byte[]) processItemMap.get("body");
			HashMap<String, Object> workOrderItemMap = (HashMap<String, Object>) CommonTools.bytesToObject(requestByteArray);

			Integer workOrderId = (Integer) workOrderItemMap.get("workOrderId");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dealTs = sdf.format(new Date());
			HashMap<String, Object> parameter = new HashMap<>();
			parameter.put("ID", workOrderId);
			parameter.put("DEAL_TS", dealTs);
			if (status.equals(EProcessStatus.SUBMITED)) {// 已提交
				parameter.put("DEAL_STATUS_CODE", 0);
				parameter.remove("DEAL_TS");
			}else if (status.equals(EProcessStatus.ACCEPTED)) {// 已认领
				parameter.put("DEAL_STATUS_CODE", 1);
			}else if (status.equals(EProcessStatus.PROCESSING)) {// 已提交
				parameter.put("DEAL_STATUS_CODE", 2);
			} else if (status.equals(EProcessStatus.SUCCESS_END)) {// 审批通过
				parameter.put("DEAL_STATUS_CODE", 3);
			} else if (status.equals(EProcessStatus.SEND_BACK)) {// 打回
				parameter.put("DEAL_STATUS_CODE", 4);
			}else if (status.equals(EProcessStatus.CLOSE)) {// 关闭
				parameter.put("DEAL_STATUS_CODE", 5);
			}
			dbService.update(DBServiceConst.UPDATE_WORKORDER, parameter);
			logger.debug("deal work order process listener success!");
		} catch (Exception e) {
			logger.error("deal work order process listener error!", e);
		}

	}

}
