package com.system.started.javaconf.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class CommonSwaggerConfig implements WebMvcConfigurer{

	@Bean
    public Docket loginDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1-登陆")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vlandc.oss.apigate.rest.controller"))
                .paths(PathSelectors.regex("(/system/login)"))
                .build();
    }
	
    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    protected ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("私有云管理平台API文档")
                //版本号
                .version("1.0")
                //描述
                .description("系统登陆操作，需要先进行登陆，获取token进行其他API接口操作。")
                .build();
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/swagger-ui.html","/docs.html").addResourceLocations("/", "classpath:/META-INF/resources/");
    	registry.addResourceHandler("/webjars/**").addResourceLocations("/", "classpath:/META-INF/resources/webjars/");
    }
}
