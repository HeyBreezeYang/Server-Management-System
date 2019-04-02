package com.system.started.rest.controller;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.export.EExportType;
import com.system.started.export.ExportServiceManager;
import com.system.started.export.IExportService;
import com.vlandc.oss.common.JsonHelper;

@Controller
@RequestMapping(value = "/export")
public class ExportController extends AbstractController {
	private static Logger logger = Logger.getLogger(ExportController.class);

	@Autowired
	ExportServiceManager exportServiceManager;

	@RequestMapping(value = "", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportReport(@RequestBody HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			String exportType = (String) paramMap.get("type");
			IExportService exportServiceImpl = exportServiceManager.getExportService(EExportType.valueOf(exportType));
			String targetFilePath = exportServiceImpl.doExport(paramMap);
			resultMap.put("messageStatus", "END");
			resultMap.put("filePath", targetFilePath);
			logger.debug("create exportReport successful! ");
			String result = JsonHelper.toJson(resultMap);
			return result;
		} catch (Exception e) {
			return invalidRequest(e.getMessage());
			
		}
		
	}
}
