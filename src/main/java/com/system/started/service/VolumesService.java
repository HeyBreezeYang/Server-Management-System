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

/**
 * @Author wyj
 * @Date 2018/6/8
 * @Description
 */
@Component
public class VolumesService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(VolumesService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private UserService userService;

	@Autowired
	private VirtualActionWrapper virtualActionWrapper;

	public String listVolumes(HashMap<String, Object> paramMap) {
		String curLoginId = paramMap.getOrDefault("curLoginId", "").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(curLoginId));
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_VIR_VOLUMES, paramMap, currentPage, length);
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_VOLUMES, paramMap);
		}

		// 查询卷挂在到的虚机信息
		parseAttachmentsServer(resultMap);
		resultMap.put("draw", paramMap.get("draw"));
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listVolumes successful! ");
		return result;
	}

	private void parseAttachmentsServer(HashMap<String, Object> resultMap) {
		List<HashMap<String, Object>> records = (List<HashMap<String, Object>>) resultMap.get("record");
		for (HashMap<String, Object> recordItem : records) {
			if (recordItem.get("attachments") != null) {
				List<HashMap<String, Object>> attachmentServers = new ArrayList<HashMap<String, Object>>();
				List<HashMap<String, Object>> attachments = JsonHelper.fromJson(List.class, recordItem.get("attachments").toString());
				for (HashMap<String, Object> attachmentItem : attachments) {
					HashMap<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("uuid", attachmentItem.get("server_id"));
					HashMap<String, Object> serverResultMap = (HashMap<String, Object>) dbService.selectOne(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES, paramMap);

					if (serverResultMap != null) {
						HashMap<String, Object> serverMap = new HashMap<String, Object>();
						serverMap.put("id", serverResultMap.get("id"));
						serverMap.put("uuid", serverResultMap.get("uuid"));
						serverMap.put("name", serverResultMap.get("name"));
						attachmentServers.add(serverMap);
					}
				}
				recordItem.put("attachmentServers", JsonHelper.toJson(attachmentServers));
			}
		}
	}

	public String listVolumeDetails(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_VOLUMES, paramMap);
		// 查询卷挂在到的虚机信息
		parseAttachmentsServer(resultMap);
		logger.debug("query listVolumeDetails successful! ");
		return JsonHelper.toJson(resultMap);
	}

	public String createVolume(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			paramMap.put("createUser", loginId);
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_CREATE_VOLUME, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String deleteVolume(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			int volumeId = (int) paramMap.getOrDefault("volumeId", "");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DELETE_VOLUME, new HashMap<String, Object>(), volumeId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}


	public String updateVolume(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			int volumeId = (int) paramMap.getOrDefault("volumeId", "");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_UPDATE_VOLUME, paramMap, volumeId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String extendVolume(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			int volumeId = (int) paramMap.get("volumeId");
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_EXTEND_VOLUME, paramMap, volumeId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String resetVolumeStatus(HashMap<String, Object> paramMap) {
		String actionType = (String) paramMap.get("action");
		if (actionType.equals("attach")) {
			return attachVolume(paramMap);
		} else if (actionType.equals("detach")) {
			paramMap.put("uuid", paramMap.get("serverId"));
			paramMap.put("attachment_id", paramMap.get("volumeId"));
			return detachVolume(paramMap);
		}
		return super.invalidRequest();
	}

	public String detachVolume(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			String serverId = paramMap.getOrDefault("serverId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DETACH_SERVER_VOLUME, paramMap, serverId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	private String attachVolume(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			String serverId = paramMap.getOrDefault("serverId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_ATTACH_SERVER_VOLUME, paramMap, serverId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String listPVCStorages(HashMap<String, Object> paramMap) {
		try {
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, null, EAction.VIRTUAL_EXTENSION_LIST_STORAGE_CONTROLLER, paramMap);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
}
