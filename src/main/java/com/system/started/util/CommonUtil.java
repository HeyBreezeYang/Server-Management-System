package com.system.started.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CommonUtil {

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.length() > 0) {
			String[] ips = ip.split(",");
			for (int i = 0; i < ips.length; i++) {
				if (ips[i].trim().length() > 0 && !"unknown".equalsIgnoreCase(ips[i].trim())) {
					ip = ips[i].trim();
					break;
				}
			}
		}
		return ip;
	}

	public static String getWebBaseUrl(HttpServletRequest servletRequest){
		StringBuffer sb = new StringBuffer();
		sb.append(servletRequest.getScheme());
		sb.append("://");
		sb.append(servletRequest.getServerName());
		sb.append(":");
		sb.append(servletRequest.getServerPort());
		sb.append(servletRequest.getContextPath());
		return sb.toString();
	}

	public static long parseIpToLong(String ip){
		int ipNum = 0;
		String[] ipArray = ip.split("\\.");
		for (int i = 0; i < ipArray.length; i++) {
			ipNum += Integer.parseInt(ipArray[i])<<(24-8*i);
		}

		return Integer.toUnsignedLong(ipNum);
	}

	public static String parseLongToIp(long ipLong){
		String ip = new String();
		ip += ((ipLong>>24) & 0xFF) + ".";
		ip += ((ipLong>>16) & 0xFF) + ".";
		ip += ((ipLong>>8) & 0xFF) + ".";
		ip += (ipLong & 0xFF) ;
		return ip;
	}

	public static synchronized String createRecordNo() {
		String str = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		long orderNum = (long) (Math.random() * 100000);// + 1000000000000l;

		str = str + orderNum;
		return str;
	}
	
	public static synchronized String createFieldName() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, uuid.indexOf("-"));
	}

	public static String createAgentNo(){
		String str = new SimpleDateFormat("HHmmss").format(new Date());

		long agentNo = (long) (Math.random() * 100000);// + 1000000000000l;
		return str + agentNo;
	}

	public static String convert(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		int maxPos = 0;
		while((i=utfString.indexOf("\\u", pos)) != -1){
			sb.append(utfString.substring(pos, i-1));
			if(i+5 < utfString.length()){
				pos = i+6;
				maxPos = pos;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}
		sb.append(utfString.substring(maxPos));

		return sb.toString();
	}

	public static HashMap<String, HashMap<String, Object>> parseListToHashMap(List<HashMap<String, Object>> tempList, String keyword) {
		HashMap<String, HashMap<String, Object>> resultMap = new HashMap<>();

		for (int i = 0; i < tempList.size(); i++) {
			HashMap<String, Object> tempItem = tempList.get(i);
			resultMap.put(String.valueOf(tempItem.get(keyword)), tempItem);
		}
		return resultMap;
	}

	public static HashMap<String, HashMap<String, Object>> parseListToHashMap(List<HashMap<String, Object>> tempList, String... keywordArray) {
		HashMap<String, HashMap<String, Object>> resultMap = new HashMap<>();

		for (int i = 0; i < tempList.size(); i++) {
			HashMap<String, Object> tempItem = tempList.get(i);

			StringBuffer mapKeyBuffer = new StringBuffer();
			for (int j = 0; j < keywordArray.length; j++) {
				mapKeyBuffer.append("_");
				mapKeyBuffer.append(String.valueOf(tempItem.get(keywordArray[j])));
			}
			resultMap.put(mapKeyBuffer.toString(), tempItem);
		}
		return resultMap;
	}

	public static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String MD5(String sourceWord) {
		try {
			byte[] btInput = sourceWord.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	// 将对象转换成字节数组
	public static byte[] Object2Bytes(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.flush();
		return baos.toByteArray();
	}

	// 将字节数组转换成为对象
	public static Object Bytes2Object(byte[] b) throws IOException, ClassNotFoundException {

		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(bais);

		Object obj = ois.readObject();
		return obj;
	}

	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null) {
			return null;
		}
		Object obj = beanClass.newInstance();
		org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		return obj;
	}


	public static byte[] parseMapToXML(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        sb.append("<result>");
        mapToXML(map, sb);
        sb.append("</result>");
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (Exception e) {
        	return null;
        }
    }

	private static String mapToXML(Map map,StringBuffer sb){
		Set set = map.keySet();
		for (Iterator it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = map.get(key);
			if (null == value)
				value = "";
			if (value.getClass().getName().equals("java.util.ArrayList")) {
				ArrayList list = (ArrayList) map.get(key);
				sb.append("<" + key + ">");
				for (int i = 0; i < list.size(); i++) {
					HashMap hm = (HashMap) list.get(i);
					sb.append("<recordObj>");
					mapToXML(hm, sb);
					sb.append("</recordObj>");
				}
				sb.append("</" + key + ">");

			} else {
				if (value instanceof HashMap) {
					sb.append("<" + key + ">");
					mapToXML((HashMap) value, sb);
					sb.append("</" + key + ">");
				} else {
					sb.append("<" + key + ">" + value + "</" + key + ">");
				}
			}
		}
		return sb.toString();
	}

	public static Map<String,Object> parseXMLToMap(String xml) throws DocumentException{
		Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        Map<String, Object> map = (Map<String, Object>) xmlToMap(root);
        if(root.elements().size()==0 && root.attributes().size()==0){
            return map;
        }
        return map;
	}

    private static Map<String,Object> xmlToMap(Element e) {
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        @SuppressWarnings("unchecked")
		List<Object> list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List<Object> mapList = new ArrayList<Object>();

                if (iter.elements().size() > 0) {
                    Map<String,Object> m = xmlToMap(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList<Object>();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj instanceof List) {
                            mapList = (List<Object>) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), m);
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList<Object>();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj instanceof List) {
                            mapList = (List<Object>) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), iter.getText());
                }
            }
        } else
            map.put(e.getName(), e.getText());
        return map;
    }

	public static String writeExcel(String filePath,HashMap<String, Object> paramMap) throws Exception{
		String fileName = (String) paramMap.get("name");

//		String CurrentClassFilePath = CommonUtil.class.getResource("").getPath();
//		int lastpath = CurrentClassFilePath.lastIndexOf("WEB-INF/");
//		String webPath = CurrentClassFilePath.substring(0, lastpath);
//		String targetExcelFilePath = IExportService.EXPORT_EXCEL_DIR + File.separator + fileName + ".xls";
//		File targetExcelFile = new File(webPath + targetExcelFilePath);
		File targetExcelFile = new File(filePath);
		WritableWorkbook book = Workbook.createWorkbook(targetExcelFile);
		List<HashMap<String, Object>> reportList = (List<HashMap<String, Object>>) paramMap.get("report");
		List<File> imgFileList = new ArrayList<>();

		String sheetName=fileName;
		WritableSheet sheet = book.createSheet(sheetName, 0);

		int beginLineNum = 0;

		HashMap<String,Object> dataHeadMap = reportList.get(0);
		List<String> headList = writeDataHead(sheet, beginLineNum, dataHeadMap);
		beginLineNum++;

		writeDataBody(sheet, beginLineNum, headList, reportList);
		beginLineNum += reportList.size();
		beginLineNum ++;

		book.write();
		book.close();

		for (int i = 0; i < imgFileList.size(); i++) {
			try{
				imgFileList.get(i).delete();
			}catch(Exception ex){

			}
		}
		return filePath;
	}

	private static void writeDataBody(WritableSheet sheet, int beginLineNum, List<String> headList, List<HashMap<String, Object>> dataBodyList) throws Exception {
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
				sheet.setColumnView(j,  valueLength < minLength?minLength:valueLength);
				sheet.addCell(label);
			}
			beginLineNum++;
		}
	}

	private static List<String> writeDataHead(WritableSheet sheet, int beginLineNum, HashMap<String, Object> dataHeadMap) throws Exception {
		List<String> headList = new ArrayList<>();

		WritableCellFormat headFormat = new WritableCellFormat();
		headFormat.setAlignment(jxl.format.Alignment.CENTRE);
		headFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		headFormat.setBackground(Colour.GRAY_50);
		headFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		int beginColumnNum = 0;
		for (String dataHeadKey : dataHeadMap.keySet()) {
			headList.add(dataHeadKey);
			Label label = new Label(beginColumnNum++, beginLineNum, dataHeadKey, headFormat);
			sheet.addCell(label);
		}
		return headList;
	}

//	public static String getLastFileName(){
//		String CurrentClassFilePath = CommonUtil.class.getResource("").getPath();
//		int lastpath = CurrentClassFilePath.lastIndexOf("WEB-INF/");
//		String webPath = CurrentClassFilePath.substring(0, lastpath);
//		String targetExcelFilePath = IExportService.EXPORT_EXCEL_DIR;
//		File targetExcelFile = new File(webPath + targetExcelFilePath);
////		targetExcelFile.listFiles(filter)
//
//		return null;
//	}

	public static boolean uploadFileToFTP(String url,int port,String username,String password,String path,String filename,InputStream input){
		boolean result = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("GBK");
        try {
            int reply;
            ftp.connect(url, port);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.makeDirectory(path);
            ftp.changeWorkingDirectory(path);
            ftp.storeFile(filename, input);
            input.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {

                }
            }
        }
        return result;
	}

	public static boolean validateIpInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

	public static void main(String args[]) {
//		System.out.println(CommonUtil.MD5("admin"));
//		long ipNum = CommonUtil.parseIpToLong("172.29.0.100");
//		System.out.println(ipNum);
//		System.out.println(CommonUtil.parseLongToIp(new Long("2887581796")));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		System.out.println(sdf.format(new Date()));

		boolean result= CommonUtil.validateIpInRange("192.168.5.112", "192.168.5.0/24");
		System.out.println(result);
	}
}
