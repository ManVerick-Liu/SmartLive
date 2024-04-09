package com.MacrohardStudio.utilities.webConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 允许所有域名跨域访问，也可以指定特定域名
                .allowedMethods("*")  // 允许所有请求方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true);  // 是否允许发送Cookie信息
    }
}
