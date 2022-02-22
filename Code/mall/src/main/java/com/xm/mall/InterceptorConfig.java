package com.xm.mall;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mintFM
 * @create 2022-01-25 11:20
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    //定义一个拦截器：因为后面的操作，很多都需要在用户登录的情况下才可以进行操作，如果用上没有登录就必须拦截下来，所以通过写一个拦截器来判断用户的登录状态，统一对用户的登录状态进行处理

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //对登录拦截器类进行拦截：添加需要拦截的路径和排除不要拦截的路径
        //1.添加拦截器；2.添加拦截路径；3.排除拦截路径
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/user/login", "/user/register", "/categories", "/products", "/products/*");
    }
}
