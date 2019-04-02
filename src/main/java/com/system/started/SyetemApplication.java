package com.system.started;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,MybatisAutoConfiguration.class})
@ComponentScan(basePackages= {"com.vlandc.oss.apigate.javaconf","com.vlandc.oss.apigate.javaconf.*"})
public class SyetemApplication {

	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		SpringApplication.run(SyetemApplication.class, args);
	}
}
