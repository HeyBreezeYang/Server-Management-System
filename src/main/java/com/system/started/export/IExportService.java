package com.system.started.export;

import java.util.HashMap;

public interface IExportService {
	public static final String EXPORT_TEMP_IMAGE_DIR = "export/image";
	public static final String EXPORT_EXCEL_DIR = "export/excel";
	public static final String EXPORT_PDF_DIR = "export/pdf";
	public static final String EXPORT_WORD_DIR = "export/word";
	public static final String EXPORT_XML_DIR = "export/xml";


	public String doExport(HashMap<String, Object> paramMap) throws Exception;
}
