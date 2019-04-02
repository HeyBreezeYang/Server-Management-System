package com.system.started.process.impl;

import java.util.HashMap;

import com.system.started.process.EProcessStatus;

public interface IProcessListener {

	public void initLisenter();

	public void dealPorcess(HashMap<String, Object> processItemMap, EProcessStatus status, String statusResult, HashMap<String, Object> paramMap);
}
