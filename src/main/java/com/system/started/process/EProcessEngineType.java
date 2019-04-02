package com.system.started.process;

public enum EProcessEngineType {
	EMPTY, //不通过流程
	LOCAL, //本地流程
	INTEGRATED, //一体化流程平台
	REMOTE; //对接第三方流程
}
