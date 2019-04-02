package com.system.started.process;

public enum EProcessStatus {
	SUBMITED, //已发起
	ACCEPTED, //已认领
	PROCESSING, //处理中
	SUCCESS_END, //审批通过
	SEND_BACK, //被退回
	CLOSE; //关闭
}
