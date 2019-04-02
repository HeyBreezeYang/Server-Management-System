package com.system.started.license.aspect;

import java.util.HashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.license.ILicenseAspectFilter;
import com.system.started.license.LicenseManager;
import com.system.started.rest.controller.SystemController;
import com.vlandc.oss.common.JsonHelper;

@Component
@Aspect
public class ControllerAuthAspectFilter implements ILicenseAspectFilter {


	@Autowired
	private LicenseManager licenseManager;

	@Around("execution(* com.vlandc.oss.apigate.rest.controller.*Controller.*(..)) && !execution(* com.vlandc.oss.apigate.rest.controller.AbstractController.*(..))")
	public Object doFilter(ProceedingJoinPoint joinPoint) throws Throwable {
		Object targetObj = joinPoint.getTarget().getClass();
		if (targetObj.equals(SystemController.class)) {
			String methodName = joinPoint.getSignature().getName();
			if (methodName.equals("loadCurrentLicenseFile") || methodName.equals("getCurrentLicenseDetail")) {
				Object result = joinPoint.proceed();
				return result;
			}else{
				boolean licenseStatus = licenseManager.validateLicenseExist();
				if (licenseStatus) {
					Object result = joinPoint.proceed();
					return result;
				}else{
					HashMap<String, Object> resultMap = new HashMap<>();
					resultMap.put("licenseStatus", "ERROR");
					resultMap.put("messageStatus", "ERROR");
					resultMap.put("responseMsg", "授权文件存在问题，请重新上传授权文件进行验证！");
					return JsonHelper.toJson(resultMap);
				}
			}
		}else{
			boolean licenseStatus = licenseManager.validateLicenseExist();
			if (licenseStatus) {
				Object result = joinPoint.proceed();
				return result;
			}else{
				HashMap<String, Object> resultMap = new HashMap<>();
				resultMap.put("licenseStatus", "ERROR");
				resultMap.put("messageStatus", "ERROR");
				resultMap.put("responseMsg", "授权文件存在问题，请重新上传授权文件进行验证！");
				return JsonHelper.toJson(resultMap);
			}
		}
	}

}
