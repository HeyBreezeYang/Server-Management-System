package com.system.started.javaconf;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.system.started.action.impl.IMonitorAction;
import com.system.started.action.impl.IOperationAction;
import com.system.started.action.impl.IResourceAction;
import com.system.started.action.impl.ISystemAction;
import com.system.started.action.impl.IVirtualAction;
import com.system.started.action.manage.WebActionFactory;
import com.system.started.action.manage.WebActionSelector;
import com.vlandc.oss.javaconfig.dubbo.OssDubboConfiguration;

@Configuration
@ImportResource(locations={"classpath:config/spring/apigate-kernel-consumer.xml","classpath:config/spring/apigate-action-config.xml"})
@Import(value= {OssDubboConfiguration.class})
@ComponentScan(basePackages= {"com.vlandc.oss.apigate.*"})
public class ApiGateActionConfig {

	@Resource(name="monitorWebActionMap")
	private Map<String, IMonitorAction> monitorWebActionMap;
	@Bean
	public WebActionFactory<IMonitorAction> monitorWebActionFactory(){
		WebActionFactory<IMonitorAction> monitorWebActionFactory = new WebActionFactory<>();
		monitorWebActionFactory.setActionMap(Optional.ofNullable(monitorWebActionMap).orElse(new HashMap<>()));
		return monitorWebActionFactory;
	}
	@Bean
	public WebActionSelector<IMonitorAction> monitorWebActionSelector(WebActionFactory<IMonitorAction> monitorWebActionFactory){
		WebActionSelector<IMonitorAction> monitorWebActionSelector = new WebActionSelector<>(monitorWebActionFactory);
		return monitorWebActionSelector;
	}

	@Resource(name="operateWebActionMap")
	private Map<String, IOperationAction> operateWebActionMap;
	@Bean
	public WebActionFactory<IOperationAction> operationWebActionFactory(){
		WebActionFactory<IOperationAction> operationWebActionFactory = new WebActionFactory<>();
		operationWebActionFactory.setActionMap(Optional.ofNullable(operateWebActionMap).orElse(new HashMap<>()));
		return operationWebActionFactory;
	}
	@Bean
	public WebActionSelector<IOperationAction> operationWebActionSelector(WebActionFactory<IOperationAction> operationWebActionFactory){
		WebActionSelector<IOperationAction> operationWebActionSelector = new WebActionSelector<>(operationWebActionFactory);
		return operationWebActionSelector;
	}
	
	@Resource(name="resourceWebActionMap")
	private Map<String, IResourceAction> resourceWebActionMap;
	@Bean
	public WebActionFactory<IResourceAction> resourceWebActionFactory(){
		WebActionFactory<IResourceAction> resourceWebActionFactory = new WebActionFactory<>();
		resourceWebActionFactory.setActionMap(Optional.ofNullable(resourceWebActionMap).orElse(new HashMap<>()));
		return resourceWebActionFactory;
	}
	@Bean
	public WebActionSelector<IResourceAction> resourceWebActionSelector(WebActionFactory<IResourceAction> resourceWebActionFactory){
		WebActionSelector<IResourceAction> resourceWebActionSelector = new WebActionSelector<>(resourceWebActionFactory);
		return resourceWebActionSelector;
	}
	
	@Resource(name="systemWebActionMap")
	private Map<String, ISystemAction> systemWebActionMap;
	@Bean
	public WebActionFactory<ISystemAction> systemWebActionFactory(){
		WebActionFactory<ISystemAction> systemWebActionFactory = new WebActionFactory<>();
		systemWebActionFactory.setActionMap(Optional.ofNullable(systemWebActionMap).orElse(new HashMap<>()));
		return systemWebActionFactory;
	}
	@Bean
	public WebActionSelector<ISystemAction> systemWebActionSelector(WebActionFactory<ISystemAction> systemWebActionFactory){
		WebActionSelector<ISystemAction> systemWebActionSelector = new WebActionSelector<>(systemWebActionFactory);
		return systemWebActionSelector;
	}

	@Resource(name="virtualWebActionMap")
	private Map<String, IVirtualAction> virtualWebActionMap;
	@Bean
	public WebActionFactory<IVirtualAction> virtualWebActionFactory(){
		WebActionFactory<IVirtualAction> virtualWebActionFactory = new WebActionFactory<>();
		virtualWebActionFactory.setActionMap(Optional.ofNullable(virtualWebActionMap).orElse(new HashMap<>()));
		return virtualWebActionFactory;
	}
	@Bean
	public WebActionSelector<IVirtualAction> virtualWebActionSelector(WebActionFactory<IVirtualAction> virtualWebActionFactory){
		WebActionSelector<IVirtualAction> virtualWebActionSelector = new WebActionSelector<>(virtualWebActionFactory);
		return virtualWebActionSelector;
	}
}
