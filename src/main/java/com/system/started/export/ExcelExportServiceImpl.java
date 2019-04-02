package com.system.started.export;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import com.system.started.util.ImageBase64Util;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@SuppressWarnings("unchecked")
public class ExcelExportServiceImpl implements IExportService {

	public int IMAGE_HEIGHT = 20;
	public int IMAGE_WIDTH = 8;

	@Override
	public String doExport(HashMap<String, Object> paramMap) throws Exception {
		
			
		String context = (String) paramMap.get("context");

		String fileName = (String) paramMap.get("name");

		String CurrentClassFilePath = this.getClass().getResource("").getPath();
		int lastpath = CurrentClassFilePath.lastIndexOf("WEB-INF/");
		String webPath = CurrentClassFilePath.substring(0, lastpath);
		String targetExcelFilePath = IExportService.EXPORT_EXCEL_DIR + File.separator + fileName + ".xls";
		File targetExcelFile = new File(webPath + targetExcelFilePath);
		WritableWorkbook book = Workbook.createWorkbook(targetExcelFile);
		List<HashMap<String, Object>> reportList = (List<HashMap<String, Object>>) paramMap.get("report");
		List<File> imgFileList = new ArrayList<>();
		
		for (int i = 0; i < reportList.size(); i++) {
			HashMap<String, Object> reportItem = reportList.get(i);
			String sheetName = (String) reportItem.get("name");
			WritableSheet sheet = book.createSheet(sheetName, i);

			int beginLineNum = 0;

			List<HashMap<String, Object>> imageList = (List<HashMap<String, Object>>) reportItem.get("images");
			for (int j = 0; j < imageList.size(); j++) {
				HashMap<String, Object> imageItem = imageList.get(j);
				if (context.equals("IMAGE") || context.equals("ALL")) {
					if (imageItem.containsKey("imageData")) {
						File imgFile = writeImage(fileName+i+j, (String) imageItem.get("imageData"), sheet, beginLineNum);
						imgFileList.add(imgFile);
						beginLineNum += IMAGE_HEIGHT + 1;
					}
				}

				if (context.equals("DATA") || context.equals("ALL")) {
					HashMap<String, String> titleHeadMap = (HashMap<String, String>) imageItem.get("titleHead");
					HashMap<String, String> dataHeadMap = (HashMap<String, String>) imageItem.get("dataHead");
					List<HashMap<String, Object>> dataBodyList = (List<HashMap<String, Object>>) imageItem.get("dataBody");

					List<String> headList = writeDataHead(sheet, beginLineNum, titleHeadMap, dataHeadMap);
					beginLineNum++;

					writeDataBody(sheet, beginLineNum, headList, dataBodyList);
					beginLineNum += dataBodyList.size();
					beginLineNum ++;
				}

			}
		}

		book.write();
		book.close();
		
		for (int i = 0; i < imgFileList.size(); i++) {
			try{
				imgFileList.get(i).delete();
			}catch(Exception ex){
				
			}
		}
		return targetExcelFilePath;
	}

	private void writeDataBody(WritableSheet sheet, int beginLineNum, List<String> headList, List<HashMap<String, Object>> dataBodyList) throws Exception {
		int minLength = 12;
		WritableCellFormat dataFormat = new WritableCellFormat();
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		for (int i = 0; i < dataBodyList.size(); i++) {
			HashMap<String, Object> dataBodyItemMap = dataBodyList.get(i);

			for (int j = 0; j < headList.size(); j++) {
				String headKey = headList.get(j);
				String dataValue = String.valueOf(dataBodyItemMap.get(headKey));
				Label label = new Label(j, beginLineNum, dataValue, dataFormat);
				int valueLength = (int) (dataValue.length() * 1.5);
				sheet.setColumnView(j, 18 ); // valueLength < minLength?minLength:valueLength
				sheet.addCell(label);
			}
			beginLineNum++;
		}
	}

	public List<String> writeDataHead(WritableSheet sheet, int beginLineNum, HashMap<String, String> titleHeadMap, HashMap<String, String> dataHeadMap) throws Exception {
		List<String> headList = new ArrayList<>();

		WritableCellFormat headFormat = new WritableCellFormat();
		headFormat.setAlignment(jxl.format.Alignment.CENTRE);
		headFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		headFormat.setBackground(Colour.GRAY_50);
		headFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		int beginColumnNum = 0;
		for (String titleHeadKey : titleHeadMap.keySet()) {
			String titleHeadValue = titleHeadMap.get(titleHeadKey);
			headList.add(titleHeadKey);
			Label label = new Label(beginColumnNum++, beginLineNum, titleHeadValue, headFormat);
			sheet.addCell(label);
		}
		for (String dataHeadKey : dataHeadMap.keySet()) {
			String dataHeadValue = dataHeadMap.get(dataHeadKey);
			headList.add(dataHeadKey);
			Label label = new Label(beginColumnNum++, beginLineNum, dataHeadValue, headFormat);
			sheet.addCell(label);
		}
		return headList;
	}

	public File writeImage(String fileName, String imageData, WritableSheet sheet, int beginLineNum) throws Exception {
		String tempImageFileName = null;
		imageData = imageData.substring(imageData.indexOf("base64,") + 7);
		tempImageFileName = generateTempImage(fileName, imageData);

		File imgFile = new File(tempImageFileName);
		BufferedImage bufferImage = ImageIO.read(new FileInputStream(imgFile));
		IMAGE_WIDTH = bufferImage.getWidth() / 127;
		IMAGE_HEIGHT = bufferImage.getHeight() / 20;
		WritableImage image = new WritableImage(0, beginLineNum, IMAGE_WIDTH, IMAGE_HEIGHT, imgFile);
		
		WritableCellFormat imageCellFormat = new WritableCellFormat();
		imageCellFormat.setBackground(Colour.WHITE);
		for (int i = 0; i < IMAGE_WIDTH; i++) {
			for (int j = 0; j < IMAGE_HEIGHT; j++) {
				sheet.addCell(new Blank(i, j+beginLineNum,imageCellFormat));
			}
		}
		
		sheet.addImage(image);
		return imgFile;
	}

	public String generateTempImage(String fileName, String imageData) throws Exception {
		String CurrentClassFilePath = this.getClass().getResource("").getPath();
		int lastpath = CurrentClassFilePath.lastIndexOf("WEB-INF/");
		String webPath = CurrentClassFilePath.substring(0, lastpath);
		String tempImageFileName = webPath + IExportService.EXPORT_TEMP_IMAGE_DIR + File.separator + fileName + ".png";
		boolean imageStatus = ImageBase64Util.GenerateImage(tempImageFileName, imageData);
		if (imageStatus) {
			return tempImageFileName;
		}
		throw new Exception("generate temp image file error!");
	}

}
