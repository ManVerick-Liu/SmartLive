package com.MacrohardStudio.interceptor.config;

import com.MacrohardStudio.interceptor.JWTinterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//配置类
@Configuration
public class interceptorConfig implements WebMvcConfigurer{
    // 生成拦截器对象
    @Bean
    public JWTinterceptor createJwtInterceptor() {
        return new JWTinterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createJwtInterceptor())
                // 作用于所有的login
                .addPathPatterns("/user/login");
//                .excludePathPatterns("/login");

    }

}

