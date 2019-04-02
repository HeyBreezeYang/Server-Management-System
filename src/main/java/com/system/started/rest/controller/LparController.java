package com.system.started.rest.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.db.dao.LparInstanceDao;
import com.vlandc.oss.common.JsonHelper;

@Controller
@RequestMapping(value = "/lpars")
public class LparController extends AbstractController {

	private final static Logger logger = LoggerFactory.getLogger(LparController.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private LparInstanceDao lparInstanceDao;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public String createLpar(@RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		try {
			lparInstanceDao.insert(paramMap);
		} catch (Exception e) {
			return this.invalidRequest("createLpar error!");
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createLpar successful! ");
		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public String updateLpar(@PathVariable String id, @RequestBody HashMap<String, Object> paramMap) {
		parseCurrentLoginIds(paramMap);
		try {
			lparInstanceDao.update(paramMap);
		} catch (Exception e) {
			return this.invalidRequest("updateLpar error!");
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert updateLpar successful! ");
		return result;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listLpars(HttpSession session, @RequestParam(required = false, value = "id") String id, @RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "tagId") Integer tagId, @RequestParam(required = false, value = "tagValue") String tagValue, @RequestParam(required = false, value = "key") String key, @RequestParam(required = false, value = "parentNodeId") Integer parentNodeId, @RequestParam(required = false, value = "draw") String draw,
			@RequestParam(required = false, value = "start") String start, @RequestParam(required = false, value = "length") String length) {
		HashMap<String, Object> paramMap = new HashMap<>();
		if (id != null) {
			paramMap.put("id", id);
		}
		if (name != null) {
			paramMap.put("name", name);
		}
		if (tagId != null) {
			paramMap.put("tagId", tagId);
		}
		if (tagValue != null) {
			paramMap.put("tagValue", tagValue);
		}
		if (key != null) {
			paramMap.put("key", key);
		}
		if (parentNodeId != null) {
			paramMap.put("parentNodeId", parentNodeId);
		}

		int startNum = 0, currentPage = 0;
		if (start != null && !length.equals("-1")) {
			startNum = Integer.parseInt(start);
			currentPage = startNum == 0 ? 1 : startNum / Integer.parseInt(length) + 1;
		}
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		parseRelationLoginIds(paramMap);

		HashMap<String, Object> roleParamMap = new HashMap<>();
		parseCurrentLoginIds(roleParamMap);
		Integer countSize = (Integer) dbService.selectOne(DBServiceConst.SELECT_SYSTEM_USER_ADMIN_ROLE, roleParamMap);
		paramMap.put("countSize", countSize);

		if (start != null && !length.equals("-1")) {
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_LPAR_INSTANCES, paramMap, currentPage, Integer.parseInt(length));
			resultMap.put("draw", draw);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_LPAR_INSTANCES, paramMap);
		}

		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listLpars successful! ");
		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteLpar(HttpSession session, @PathVariable String id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);

		dbService.delete(DBServiceConst.DELETE_RN_BASE, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("delete deleteLpar successful! ");

		return result;
	}

	@RequestMapping(value = "/{id}/interface", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listLparInterface(HttpSession session, @PathVariable String id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", id);

		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_INTERFACE, paramMap);

		String result = JsonHelper.toJson(resultMap);
		return result;
	}

	@RequestMapping(value = "/{id}/interface/{interfaceId}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateLparInterface(HttpSession session, @PathVariable String id, @PathVariable Integer interfaceId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeId", id);
		dbService.update(DBServiceConst.UPDATE_RN_EXT_DEFAULT_INTERFACE, paramMap);
		paramMap.put("interfaceId", interfaceId);
		dbService.update(DBServiceConst.UPDATE_RN_EXT_DEFAULT_INTERFACE, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateLparInterface successful! ");
		return result;
	}

	@RequestMapping(value = "/systemInfo", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateLparSystemInfo(HttpSession session, @RequestBody HashMap<String, Object> paramMap) {

		dbService.update(DBServiceConst.UPDATE_RN_EXT_SYSTEMINFO, paramMap);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("update updateLparSystemInfo successful! ");
		return result;
	}
}
