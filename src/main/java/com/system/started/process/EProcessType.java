package com.system.started.process;

public enum EProcessType {
	WORK_ORDER_CHECK, //工单处理流程
	OPERATE_APPROVE, //运维审批流程
	DEPLOY_APPROVE, //自动化部署流程
	REQUEST_APPROVE, //请求审批流程
	LIFECYCLE_APPROVE; //生命周期审批流程
	
	public static String getSubmitTips(EProcessType type){
		if (type.equals(WORK_ORDER_CHECK)) {
			return "工单已提交，等待处理！";
		}else if (type.equals(OPERATE_APPROVE)) {
			return "运维申请已提交，等待审批！";
		}else{
			return "操作申请已提交，等待审批！";
		}
	}
}
