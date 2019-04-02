package com.system.started.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.action.wrapper.VirtualActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class FlavorService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(FlavorService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private UserService userService;

	@Autowired
	private VirtualActionWrapper virtualActionWrapper;

	public String listFlavors(HashMap<String, Object> paramMap) {
		String curLoginId = paramMap.getOrDefault("curLoginId", "").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_VIR_FLAVORS, paramMap, currentPage, length);
			resultMap.put("draw", paramMap.get("draw"));
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_FLAVORS, paramMap);
		}
		return JsonHelper.toJson(resultMap);
	}

	public String listFlavor(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_FLAVORS, paramMap);
		return JsonHelper.toJson(resultMap);
	}

	public String createFlavor(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			@SuppressWarnings("unchecked")
			List<String> regionNameArray = (List<String>) paramMap.get("regionNameArray");
			int regionCount = regionNameArray.size();
			List<HashMap<String, Object>> existFlavorList = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> errorFlavorList = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < regionNameArray.size(); i++) {
				String flavorRegionName = regionNameArray.get(i);

				HashMap<String, Object> flavorParamMap = new HashMap<String, Object>();
				flavorParamMap.put("regionName", flavorRegionName);
				flavorParamMap.put("vcpus", paramMap.get("vcpus"));
				flavorParamMap.put("memory", paramMap.get("ram"));
				flavorParamMap.put("root_gb", paramMap.get("disk"));
				List<HashMap<String, Object>> flavorList = dbService.directSelect(DBServiceConst.SELECT_RN_EXT_VIR_FLAVORS, flavorParamMap);
				if (flavorList.size() == 0) {
					paramMap.put("createUser", loginId);
					Result result = virtualActionWrapper.doExcutionAction(flavorRegionName, loginId, null, EAction.VIRTUAL_CREATE_FALVOR, paramMap);
					if (!result.getResultCode().equals(EResultCode.SUCCESS)) {
						HashMap<String, Object> errorFlavor = new HashMap<String, Object>();
						errorFlavor.put(flavorRegionName, paramMap.get("name"));

						errorFlavorList.add(errorFlavor);
					}
				} else {
					HashMap<String, Object> existFlavor = new HashMap<String, Object>();
					existFlavor.put(flavorRegionName, paramMap.get("name"));
					logger.debug(paramMap.get("name") + "[" + flavorRegionName + "] is already exist !");

					existFlavorList.add(existFlavor);
				}
			}

			HashMap<String, Object> resultObj = new HashMap<String, Object>();
			resultObj.put("exist", existFlavorList);
			resultObj.put("error", errorFlavorList);

			Result result = new Result();
			if (regionCount == errorFlavorList.size()) {
				result.setResultCode(EResultCode.VIRTUAL_FAIL);
			} else {
				result.setResultCode(EResultCode.SUCCESS);
			}

			List<HashMap<String, Object>> resultObjList = new ArrayList<HashMap<String, Object>>();
			resultObjList.add(resultObj);
			result.setResultObj(resultObjList);

			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateFlavor(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		int flavorId = (int) paramMap.get("flavorId");
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_UPDATE_FALVOR, paramMap, flavorId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteFlavor(HashMap<String, Object> paramMap) {
		String loginId = paramMap.getOrDefault("loginId", "").toString();
		int flavorId = (int) paramMap.getOrDefault("flavorId", "");
		String regionName = paramMap.getOrDefault("regionName", "").toString();
		try {
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_DELETE_FALVOR, new HashMap<String, Object>(), flavorId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
}
