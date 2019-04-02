package com.system.started.process.filter;

import java.util.HashMap;

public interface IProcessFilter {
	public String doFilter(String loginId, HashMap<String, Object> request);
}
