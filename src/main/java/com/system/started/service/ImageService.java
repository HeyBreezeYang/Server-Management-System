package com.system.started.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.system.started.action.wrapper.VirtualActionWrapper;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;
import com.vlandc.oss.model.action.EAction;
import com.vlandc.oss.model.result.EResultCode;
import com.vlandc.oss.model.result.Result;

@Component
public class ImageService extends AbstractService {

	private final static Logger logger = LoggerFactory.getLogger(ImageService.class);

	@Autowired
	private DBService dbService;

	@Autowired
	private UserService userService;

	@Value("${oss.apigate.config.image-dir-path}")
	public String imageDirPath;

	@Autowired
	private VirtualActionWrapper virtualActionWrapper;

	public String listImages(HashMap<String, Object> paramMap) {
		String currentLoginId = paramMap.getOrDefault("currentLoginId", "").toString();
		paramMap.put("countSize", userService.getSystemUserAdminRole(currentLoginId));
		HashMap<String, Object> resultMap = null;
		if (paramMap.containsKey("start") && paramMap.containsKey("length")) {
			int length = Integer.parseInt(paramMap.get("length").toString());
			int startNum = Integer.parseInt(paramMap.get("start").toString());
			int currentPage = startNum == 0 ? 1 : startNum / length + 1;
			resultMap = dbService.selectByPage(DBServiceConst.SELECT_RN_EXT_VIR_IMAGES, paramMap, currentPage, length);
			resultMap.put("draw", paramMap.get("draw"));
		} else {
			resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_IMAGES, paramMap);
		}
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listImages successful! ");
		return result;
	}

	public String listImageDetails(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_IMAGES, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listImageDetails successful! ");
		return result;
	}

	public String listImageOsType(HashMap<String, Object> paramMap) {
		HashMap<String, Object> resultMap = dbService.select(DBServiceConst.SELECT_RN_EXT_VIR_IMAGE_OSTYPE, paramMap);
		String result = JsonHelper.toJson(resultMap);
		logger.debug("query listImageOsType successful! ");
		return result;
	}

	public String listImageFiles() {
		try {
			HashMap<String, Object> resultMap = new HashMap<>();
			File imageDir = new File(imageDirPath);
			if (imageDir.isDirectory()) {
				File[] filesArray = imageDir.listFiles();
				String[] fileNameArray = new String[filesArray.length];
				for (int i = 0; i < filesArray.length; i++) {
					fileNameArray[i] = filesArray[i].getName();
				}
				resultMap.put("record", fileNameArray);
			} else {
				return invalidRequest();
			}

			String result = JsonHelper.toJson(resultMap);
			logger.debug("query listImageFiles successful! ");
			return result;
		} catch (Exception e) {
			logger.error("listImageFiles error!", e);
			return invalidRequest();
		}
	}

	public String addImageProperties(HashMap<String, Object> baseParamMap, HashMap<String, Object> paramMap) {
		try {
			String regionName = baseParamMap.getOrDefault("regionName", "").toString();
			String loginId = baseParamMap.getOrDefault("loginId", "").toString();
			String projectId = baseParamMap.getOrDefault("projectId", "").toString();
			int imageId = (int) baseParamMap.get("imageId");
			HashMap<String, Object> requestParamMap = new HashMap<>();
			requestParamMap.put("imageProperties", parseImageProperties("add", paramMap));
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_UPDATE_IMAGE_PROPERTIES, requestParamMap, imageId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	private List<HashMap<String, Object>> parseImageProperties(String operation, HashMap<String, Object> paramMap) {
		List<HashMap<String, Object>> imageProperties = new ArrayList<>();

		for (String key : paramMap.keySet()) {
			Object value = paramMap.get(key);
			HashMap<String, Object> imageProperty = new HashMap<>();
			imageProperty.put("op", operation);
			imageProperty.put("value", value);
			if (key.equals("osversion")) {
				imageProperty.put("path", "/osversion");
			} else if (key.equals("osfamily")) {
				imageProperty.put("path", "/osfamily");
			} else if (key.equals("isPublic")) {
				imageProperty.put("path", "/is_public");
			} else if (key.equals("minCpu")) {
				imageProperty.put("path", "/min_cpu");
			} else if (key.equals("minRam")) {
				imageProperty.put("path", "/min_ram");
			} else if (key.equals("minDisk")) {
				imageProperty.put("path", "/min_disk");
			} else if (key.equals("diskFormat")) {
				imageProperty.put("path", "/disk_format");
			} else if (key.equals("containerFormat")) {
				imageProperty.put("path", "/container_format");
			} else if (key.equals("virtualizationtype")) {
				imageProperty.put("path", "/virtualizationtype");
			} else {
				imageProperty.put("path", "/" + key);
			}
			imageProperties.add(imageProperty);
		}

		return imageProperties;
	}

	public String updateImageProperties(HashMap<String, Object> baseParamMap, HashMap<String, Object> paramMap) {
		try {
			String regionName = baseParamMap.getOrDefault("regionName", "").toString();
			String loginId = baseParamMap.getOrDefault("loginId", "").toString();
			String projectId = baseParamMap.getOrDefault("projectId", "").toString();
			int imageId = (int) baseParamMap.get("imageId");
			HashMap<String, Object> requestParamMap = new HashMap<>();
			requestParamMap.put("imageProperties", parseImageProperties("replace", paramMap));
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_UPDATE_IMAGE_PROPERTIES, requestParamMap, imageId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}

	public String updateImageDisplay(Integer status, List<String> imageIds) {
		HashMap<String, Object> paramMap = new HashMap<>();
		for (int i = 0; i < imageIds.size(); i++) {
			String imageId = imageIds.get(i);
			paramMap.put("nodeId", imageId);
			paramMap.put("isDisplay", status);
			dbService.update(DBServiceConst.UPDATE_RN_EXT_IMAGE_DISPLAY, paramMap);
		}
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("messageStatus", "END");
		String result = JsonHelper.toJson(resultMap);
		logger.debug("insert createDataCenter successful! ");
		return result;
	}

	public String deleteImage(HashMap<String, Object> paramMap) {
		try {
			String regionName = paramMap.getOrDefault("regionName", "").toString();
			String loginId = paramMap.getOrDefault("loginId", "").toString();
			String projectId = paramMap.getOrDefault("projectId", "").toString();
			String imageId = paramMap.getOrDefault("imageId", "").toString();
			Result result = virtualActionWrapper.doExcutionAction(regionName, loginId, projectId, EAction.VIRTUAL_DELETE_IMAGE, new HashMap<String, Object>(), imageId);
			return JsonHelper.toJson(result);
		} catch (Exception e) {
			Result result = new Result();
			result.setResultCode(EResultCode.NO_ACTION_FIND_FAIL);
			result.setResultMsg("不能找到对应的实现类！");
			return JsonHelper.toJson(result);
		}
	}
}
