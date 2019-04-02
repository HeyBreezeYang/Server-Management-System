package com.system.started.export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.system.started.util.ImageBase64Util;

@SuppressWarnings("unchecked")
public class PdfExportServiceImpl implements IExportService {

	private BaseFont baseFont = null;

	@Override
	public String doExport(HashMap<String, Object> paramMap) throws Exception {
		String context = (String) paramMap.get("context");
		String fileName = (String) paramMap.get("name");

		String CurrentClassFilePath = this.getClass().getResource("").getPath();
		int lastpath = CurrentClassFilePath.lastIndexOf("WEB-INF/");
		String webPath = CurrentClassFilePath.substring(0, lastpath);
		String targetPdfFilePath = IExportService.EXPORT_PDF_DIR + File.separator + fileName + ".pdf";

		if (baseFont == null) {
			baseFont = BaseFont.createFont(this.getClass().getResource("").getPath() + "SIMFANG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		}

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, new FileOutputStream(webPath + targetPdfFilePath));
		document.open();

		List<HashMap<String, Object>> reportList = (List<HashMap<String, Object>>) paramMap.get("report");
		for (int i = 0; i < reportList.size(); i++) {
			HashMap<String, Object> reportItem = reportList.get(i);
			String imageName = (String) reportItem.get("name");

			document.add(Chunk.NEWLINE);
			Paragraph titleParagraph = new Paragraph(imageName, new Font(baseFont, 18, Font.BOLD));
			titleParagraph.setAlignment(Element.ALIGN_CENTER);
			document.add(titleParagraph);
			document.add(Chunk.NEWLINE);

			List<HashMap<String, Object>> imageList = (List<HashMap<String, Object>>) reportItem.get("images");
			if (imageList.size() == 0) {
				document.add(Chunk.NEWLINE);
				Font font = new Font(baseFont, 12, Font.NORMAL);
				font.setColor(BaseColor.RED);
				Paragraph noDataParagraph = new Paragraph("相关数据不存在", font);
				noDataParagraph.setAlignment(Element.ALIGN_CENTER);
				document.add(noDataParagraph);
			} else {
				for (int j = 0; j < imageList.size(); j++) {
					HashMap<String, Object> imageItem = imageList.get(j);

					if (context.equals("IMAGE") || context.equals("ALL")) {
						if (imageItem.containsKey("imageData")) {
							writeImage(fileName + i + j, (String) imageItem.get("imageData"), document);
						}
					}

					if (context.equals("DATA") || context.equals("ALL")) {
						if (context.equals("DATA")) {
							String imageItemName = (String) imageItem.get("name");
							Font font = new Font(baseFont, 12, Font.NORMAL);
							Paragraph dataTableNameParagraph = new Paragraph(imageItemName, font);
							dataTableNameParagraph.setAlignment(Element.ALIGN_CENTER);
							document.add(dataTableNameParagraph);
							document.add(Chunk.NEWLINE);
						}
						HashMap<String, String> titleHeadMap = (HashMap<String, String>) imageItem.get("titleHead");
						HashMap<String, String> dataHeadMap = (HashMap<String, String>) imageItem.get("dataHead");
						List<HashMap<String, Object>> dataBodyList = (List<HashMap<String, Object>>) imageItem.get("dataBody");

						writeDataTable(document, titleHeadMap, dataHeadMap, dataBodyList);
						document.newPage();
					}
				}
			}
		}

		document.close();
		return targetPdfFilePath;
	}

	private void writeDataTable(Document document, HashMap<String, String> titleHeadMap, HashMap<String, String> dataHeadMap, List<HashMap<String, Object>> dataBodyList) throws Exception {
		PdfPTable table = new PdfPTable(titleHeadMap.size() + dataHeadMap.size());
		List<String> headList = new ArrayList<>();
		for (String titleHeadKey : titleHeadMap.keySet()) {
			String titleHeadValue = titleHeadMap.get(titleHeadKey);
			headList.add(titleHeadKey);

			PdfPCell cell = new PdfPCell(new Paragraph(titleHeadValue, new Font(baseFont, 12, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setBorderColor(BaseColor.BLACK);
			table.addCell(cell);
		}
		for (String dataHeadKey : dataHeadMap.keySet()) {
			String dataHeadValue = dataHeadMap.get(dataHeadKey);
			headList.add(dataHeadKey);

			PdfPCell cell = new PdfPCell(new Paragraph(dataHeadValue, new Font(baseFont, 12, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setBorderColor(BaseColor.BLACK);
			table.addCell(cell);
		}

		for (int i = 0; i < dataBodyList.size(); i++) {
			HashMap<String, Object> dataBodyItemMap = dataBodyList.get(i);

			for (int j = 0; j < headList.size(); j++) {
				String headKey = headList.get(j);
				String dataValue = String.valueOf(dataBodyItemMap.get(headKey));

				PdfPCell cell = new PdfPCell(new Paragraph(dataValue, new Font(baseFont, 12, Font.NORMAL)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(BaseColor.BLACK);
				table.addCell(cell);
			}
		}

		document.add(table);
	}

	private void writeImage(String fileName, String imageData, Document document) throws Exception {
		String tempImageFileName = null;
		imageData = imageData.substring(imageData.indexOf("base64,") + 7);
		tempImageFileName = generateTempImage(fileName, imageData);

		Image png = Image.getInstance(tempImageFileName);
		document.add(png);
		document.add(Chunk.NEWLINE);
		try {
			new File(tempImageFileName).delete();
		} catch (Exception e) {
		}
		
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
