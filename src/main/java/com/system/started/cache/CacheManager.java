package com.system.started.cache;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CacheManager {

	private List<ICache> cacheList = null;

	public void setCacheList(List<ICache> cacheList) {
		this.cacheList = cacheList;
	}

	public void initCache() {
		if (cacheList != null) {
			for (int i = 0; i < this.cacheList.size(); i++) {
				cacheList.get(i).initCacheData();
			}
		}
	}
}
