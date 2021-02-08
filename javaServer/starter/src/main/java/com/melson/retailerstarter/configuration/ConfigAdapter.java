package com.melson.retailerstarter.configuration;

import com.melson.base.interceptor.SecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @Author Nelson
 * @Description  服务器登录权限验证
 * @Date 2020/7/24
 */
@Configuration
public class ConfigAdapter extends WebMVCConfig {

    //将拦截器加入容器中，允许在拦截器中注入其他bean
    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor()).excludePathPatterns("/static/*")
                .excludePathPatterns("/error").addPathPatterns("/**");
    }
}
