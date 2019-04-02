package com.system.started.export;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class ExportServiceManager {

	private HashMap<String, IExportService> exportServiceMap;

	public void setExportServiceMap(HashMap<String, IExportService> exportServiceMap) {
		this.exportServiceMap = exportServiceMap;
	}

	public IExportService getExportService(EExportType exportType) throws Exception {
		if (exportServiceMap.containsKey(exportType.name())) {
			return exportServiceMap.get(exportType.name());
		}
		throw new Exception("no exportService find by the type (" + exportType.name() + ")");
	}
}
