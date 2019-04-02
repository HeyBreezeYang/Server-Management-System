package com.system.started.javaconf.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class VirtualApiSwaggerConfig{
	@Bean
    public Docket virtualRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();  
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build();  
        pars.add(tokenPar.build());  
        
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3-虚拟化管理")
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.vlandc.oss.apigate.rest.controller"))
                .paths(PathSelectors.regex("^(/servers/).*|(/flavors/).*|(/images/).*|(/volumes/).*|(/networks/).*$"))
                .build()
                .pathMapping("/")
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("私有云管理平台API文档")
                //版本号
                .version("1.0")
                //描述
                .description("虚拟化管理接口定义")
                .build();
    }
}
