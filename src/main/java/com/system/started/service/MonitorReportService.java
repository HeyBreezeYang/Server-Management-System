package com.system.started.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MonitorReportService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(MonitorReportService.class);
	
	SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	private String generateXML(List<HashMap<String,Object>> recordList){
		String result="";
		if(recordList.size()>0){
			Element bomcElem = DocumentHelper.createElement("bomc");
			Document document = DocumentHelper.createDocument(bomcElem);
			bomcElem.addElement("type").addText("HOST");
			String createTime = formater.format(new Date()).replace(" ", "T");
			bomcElem.addElement("createtime").addText(createTime);
			
			String currentDate=formater.format(new Date()).replace(" ", "T");
			String begintime=currentDate,endtime=currentDate;
			
			if(recordList.size()>0){
				HashMap<String,Object>firstRecord=recordList.get(0);
				HashMap<String,Object>lastRecord=recordList.get(recordList.size()-1);
				
				begintime=formater.format(new Date((long)firstRecord.get("collectTime")));
				endtime=formater.format(new Date((long)lastRecord.get("collectTime")));
				
				begintime=begintime.replace(" ", "T");
				endtime=endtime.replace(" ", "T");
			}
			bomcElem.addElement("begintime").addText(begintime);
			bomcElem.addElement("endtime").addText(endtime);
			
			Element dataElem= bomcElem.addElement("data");
			
			int count=1;
			for (HashMap<String, Object> map : recordList) {
				Element rcdElem = DocumentHelper.createElement("rcd");
				rcdElem.addElement("seq").addText(count+"");
				rcdElem.addElement("kpiid").addText((String) map.get("itemId"));
				rcdElem.addElement("ciname").addText((String) map.get("hostName"));
				
				String value="0";
				if(map.get("value") instanceof Float){
					value=String.format("%.4f", map.get("value"));
				}else{
					value=map.get("value").toString();
				}
				rcdElem.addElement("value").addText(value);
				rcdElem.addElement("unit").addText((String) map.get("unit"));
				
				String collectTime =formater.format(new Date((long)map.get("collectTime")));
				rcdElem.addElement("collectTime").addText(collectTime);
				
				dataElem.add(rcdElem);
				count++;
			}
			bomcElem.addElement("sum").addText(count-1+"");
			result=document.asXML();
		}else{
			Element resultElem = DocumentHelper.createElement("result");
			Document document = DocumentHelper.createDocument(resultElem);
			
			resultElem.addElement("messageStatus").addText("ERROR");
			resultElem.addElement("generateAt").addText(new Date().getTime()+"");
			resultElem.addElement("responseMessage").addText("统计性能数据出现异常！");
			
			result=document.asXML();
		}
		
		return result;
	}
}
