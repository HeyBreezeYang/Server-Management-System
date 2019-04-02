package com.system.started.javaconf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

	@Bean
	@ConfigurationProperties(prefix = "oss.database.apigate.dbcp2.oss")
	public BasicDataSource ossDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		return dataSource;
	}
	@Bean
	@ConfigurationProperties(prefix = "oss.database.apigate.mybaltis.oss")
	public MybatisProperties ossMybatisProperties() {
		MybatisProperties ossMybatisProperties = new MybatisProperties();
		return ossMybatisProperties;
	}
	@Bean
	public SqlSessionFactory ossSqlSessionFactory(BasicDataSource ossDataSource,ApplicationContext context,MybatisProperties ossMybatisProperties) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(ossDataSource); 
        factoryBean.setConfigLocation(context.getResource(ossMybatisProperties.getConfigLocation()));
        factoryBean.setMapperLocations(ossMybatisProperties.resolveMapperLocations());
        return factoryBean.getObject();
	}
    @Bean("ossSqlSessionTemplate")
    public SqlSessionTemplate ossSqlSessionTemplate(SqlSessionFactory ossSqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(ossSqlSessionFactory); 
        return template;
    }

	@Bean
	@ConfigurationProperties(prefix = "oss.database.apigate.dbcp2.zabbix")
	public BasicDataSource zabbixDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		return dataSource;
	}
	@Bean
	@ConfigurationProperties(prefix = "oss.database.apigate.mybaltis.zabbix")
	public MybatisProperties zabbixMybatisProperties() {
		MybatisProperties ossMybatisProperties = new MybatisProperties();
		return ossMybatisProperties;
	}
	@Bean
	public SqlSessionFactory zabbixSqlSessionFactory(BasicDataSource zabbixDataSource,ApplicationContext context,MybatisProperties zabbixMybatisProperties) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(zabbixDataSource); 
        factoryBean.setConfigLocation(context.getResource(zabbixMybatisProperties.getConfigLocation()));
        factoryBean.setMapperLocations(zabbixMybatisProperties.resolveMapperLocations());
        return factoryBean.getObject();
	}
    @Bean("zabbixSqlSessionTemplate")
    public SqlSessionTemplate zabbixSqlSessionTemplate(SqlSessionFactory zabbixSqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(zabbixSqlSessionFactory); 
        return template;
    }
}
