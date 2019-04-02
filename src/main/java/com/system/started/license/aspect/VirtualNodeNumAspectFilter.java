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
import com.vlandc.oss.common.JsonHelper;

@Component
@Aspect
@SuppressWarnings("unchecked")
public class VirtualNodeNumAspectFilter implements ILicenseAspectFilter {

	@Autowired
	private DBService dbService;

	@Autowired
	private LicenseManager licenseManager;

	@Around("execution(* com.vlandc.oss.apigate.rest.controller.ServerController.createServer(..))")
	public Object doFilter(ProceedingJoinPoint joinPoint) throws Throwable {
		Integer virtualNodeNum = (Integer) licenseManager.getLicenseProItem(LicenseConst.VIRTUAL_NODE_NUM);
		if (virtualNodeNum == null) {
			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put("licenseStatus", "ERROR");
			resultMap.put("messageStatus", "ERROR");
			resultMap.put("responseMsg", "没有授权");
			return JsonHelper.toJson(resultMap);
		}else{

			HashMap<String, Object> dbResultMap = (HashMap<String, Object>) dbService.selectOne(DBServiceConst.SELECT_RN_EXT_VIR_INSTANCES_COUNT, new HashMap<String,Object>());
			long serversCount = (long) (dbResultMap.get("totalSize"));
			if (serversCount >= virtualNodeNum) {
				HashMap<String, Object> resultMap = new HashMap<>();
				resultMap.put("licenseStatus", "ERROR");
				resultMap.put("messageStatus", "ERROR");
				resultMap.put("responseMsg", "最多只能添加（"+virtualNodeNum +"）台虚拟机！");
				return JsonHelper.toJson(resultMap);
			}else{
				Object result = joinPoint.proceed();
				return result;
			}
		}
	}

}
