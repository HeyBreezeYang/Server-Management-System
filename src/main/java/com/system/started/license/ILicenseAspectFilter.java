package com.system.started.license;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ILicenseAspectFilter {

	public Object doFilter(ProceedingJoinPoint  joinPoint) throws Throwable ;
}
