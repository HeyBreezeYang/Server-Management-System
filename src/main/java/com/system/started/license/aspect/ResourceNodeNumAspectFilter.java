package com.system.started.license.aspect;

import java.util.HashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.license.ILicenseAspectFilter;
import com.system.started.license.LicenseConst;
import com.system.started.license.LicenseManager;
import com.system.started.rest.request.ResourceNodeCreateBean;
import com.vlandc.oss.common.JsonHelper;

@Component
@Aspect
@SuppressWarnings("unchecked")
public class ResourceNodeNumAspectFilter implements ILicenseAspectFilter {

	@Autowired
	private DBService dbService;

	@Autowired
	private LicenseManager licenseManager;
	// 2018-08-24 无法添加宿主机
//	@Around("execution(* com.vlandc.oss.apigate.rest.controller.DataCenterController.createResourceNode(..))")
	public Object doFilter(ProceedingJoinPoint joinPoint) throws Throwable {
//		HashMap<String, Object> requestParamMap = (HashMap<String, Object>) joinPoint.getArgs()[1];
		ResourceNodeCreateBean resourceNode = (ResourceNodeCreateBean) joinPoint.getArgs()[0];
		HashMap<String, Object> requestParamMap  = JsonHelper.fromJson(HashMap.class, JsonHelper.toJson(resourceNode));
		String nodeType = (String) requestParamMap.get("sourceNodeType");
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("nodeType", nodeType);
		
		if (nodeType.equals("COMPUTE")) {
			Integer computeNodeNum = (Integer) licenseManager.getLicenseProItem(LicenseConst.COMPUTE_NODE_NUM);
			if (computeNodeNum == null) {
				HashMap<String, Object> resultMap = new HashMap<>();
				resultMap.put("licenseStatus", "ERROR");
				resultMap.put("messageStatus", "ERROR");
				resultMap.put("responseMsg", "没有授权");
				return JsonHelper.toJson(resultMap);
			}

			HashMap<String, Object> dbResultMap = (HashMap<String, Object>) dbService.selectOne(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES_COUNT, paramMap);
			long computeNodesCount = (long) (dbResultMap.get("totalSize"));
			if (computeNodesCount >= computeNodeNum) {
				HashMap<String, Object> resultMap = new HashMap<>();
				resultMap.put("licenseStatus", "ERROR");
				resultMap.put("messageStatus", "ERROR");
				resultMap.put("responseMsg", "最多只能添加（" + computeNodeNum + "）台计算资源节点！");
				return JsonHelper.toJson(resultMap);
			} else {
				Object result = joinPoint.proceed();
				return result;
			}
		} else if (nodeType.equals("OPERATION")) {
			Integer operationNodeNum = (Integer) licenseManager.getLicenseProItem(LicenseConst.OPERATION_NODE_NUM);
			if (operationNodeNum == null) {
				HashMap<String, Object> resultMap = new HashMap<>();
				resultMap.put("licenseStatus", "ERROR");
				resultMap.put("messageStatus", "ERROR");
				resultMap.put("responseMsg", "没有授权");
				return JsonHelper.toJson(resultMap);
			}

			HashMap<String, Object> dbResultMap = (HashMap<String, Object>) dbService.selectOne(DBServiceConst.SELECT_RN_EXT_COMPUTE_NODES_COUNT, paramMap);
			long computeNodesCount = (long) (dbResultMap.get("totalSize"));
			if (computeNodesCount >= operationNodeNum) {
				HashMap<String, Object> resultMap = new HashMap<>();
				resultMap.put("licenseStatus", "ERROR");
				resultMap.put("messageStatus", "ERROR");
				resultMap.put("responseMsg", "最多只能添加（" + operationNodeNum + "）台运维资源节点！");
				return JsonHelper.toJson(resultMap);
			} else {
				Object result = joinPoint.proceed();
				return result;
			}
		} else {
			Object result = joinPoint.proceed();
			return result;
		}

	}

}
