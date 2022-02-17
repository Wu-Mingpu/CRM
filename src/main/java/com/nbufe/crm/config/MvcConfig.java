package com.nbufe.crm.config;

import com.nbufe.crm.exceptions.NoLoginException;
import com.nbufe.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置类
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    //实例化拦截器
    @Bean
    NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }
    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor())
                // 设置需要被拦截的资源
                .addPathPatterns("/**") // 默认拦截所有的资源
                // 设置不需要被拦截的资源
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**", "/index","/user/login");
    }
}
