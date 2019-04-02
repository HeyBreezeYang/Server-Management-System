package com.system.started.process.filter;

import java.util.HashMap;
import java.util.List;

public class ProcessFilterChain {
	private List<IProcessFilter> filters = null;

	public void setFilters(List<IProcessFilter> filters) {
		this.filters = filters;
	}

	public String doFilter(String loginId, HashMap<String, Object> request) {
		for (int i = 0; i < filters.size(); i++) {
			String errorResponse = filters.get(i).doFilter(loginId, request);
			if (errorResponse != null) {
				return errorResponse;
			}
		}
		return null;
	}

}
