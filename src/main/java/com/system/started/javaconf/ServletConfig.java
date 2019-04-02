package com.system.started.javaconf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ServletConfig {

	@Bean
	public HttpMessageConverter<?> stringHttpMessageConverter() {
		return new StringHttpMessageConverter();
	}
	@Bean
	public HttpMessageConverter<?> resourceHttpMessageConverter() {
		return new ResourceHttpMessageConverter();
	}
	@Bean
	public HttpMessageConverter<?> mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter();
	}
}
