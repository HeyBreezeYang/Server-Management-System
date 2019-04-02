package com.system.started.rest.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.service.ThirdAPIService;

@Controller
@RequestMapping(value = "/thirdApi")
public class ThirdAPIController extends AbstractController {
	
	@Autowired
	private ThirdAPIService thirdAPIService;
	
	@RequestMapping(value = "/dh/rooms", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listDhRooms() {
		return this.thirdAPIService.listDhRooms();
	}
	
	@RequestMapping(value = "/dh/room/{roomId}/type/{typeId}/devices", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listDhRoomDevices(@PathVariable Integer roomId, @PathVariable Integer typeId) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("roomId", roomId);
		paramMap.put("typeId", typeId);
		return this.thirdAPIService.listDhRoomDevices(paramMap);
	}
	
	@RequestMapping(value = "/dh/events", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listDhEvents() {
		return this.thirdAPIService.listDhEvents();
	}
	
	@RequestMapping(value = "/dh/event/{id}", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateDhEventStatus(@PathVariable Integer id, @RequestBody HashMap<String, Object> paramMap) {
		paramMap.put("id", id);
		Integer dealStatus = Integer.parseInt(paramMap.get("dealStatus").toString());
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		
		if(dealStatus == 2){ // 1小时候后处理
			calendar.add(Calendar.HOUR, 1);
			paramMap.put("dealValue", formater.format(calendar));
		}else if(dealStatus == 3) { // 1天后处理
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			paramMap.put("dealValue", formater.format(calendar));
		}else {
			paramMap.put("dealValue", "");
		}
		return this.thirdAPIService.updateDhEventStatus(paramMap);
	}
}
