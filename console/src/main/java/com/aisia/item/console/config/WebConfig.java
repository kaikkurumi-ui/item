package com.aisia.item.console.config;

import com.aisia.item.console.handle.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequestHandler requestHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHandler)
                .addPathPatterns("/**") //拦截所有请求
                .excludePathPatterns(
                        "/login",
                        "/favicon.ico"
                ); //登录接口放行
    }
}
