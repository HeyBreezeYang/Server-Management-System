package com.system.started.javaconf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.system.started.javaconf.properties.ElasticJobConfigProperties;

@Configuration
@ImportResource(locations={"classpath:config/spring/apigate-scheduleJob-config.xml"})
public class ElasticJobConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "oss.apigate.elaticjob.zookeeper")
	public ElasticJobConfigProperties elasticJobProperties() {
		ElasticJobConfigProperties elasticJobProperties = new ElasticJobConfigProperties();
		return elasticJobProperties;
	}
	
	@Bean
	@ConfigurationProperties(prefix = "oss.apigate.elaticjob.zookeeper")
	public ZookeeperConfiguration zookeeperConfiguration(ElasticJobConfigProperties elasticJobProperties) {
		ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(elasticJobProperties.getServerList(),elasticJobProperties.getNameSpace());
		return zookeeperConfiguration;
	}
	
	@Bean(initMethod="init")
	public ZookeeperRegistryCenter zookeeperRegistryCenter(ZookeeperConfiguration zookeeperConfiguration) {
		ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
		return zookeeperRegistryCenter;
	}
}
