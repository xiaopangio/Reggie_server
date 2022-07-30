package org.xiaogang.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.xiaogang.server.common.JacksonObjectMapper;
import org.xiaogang.server.interceptor.LoginCheckInterceptor;

import java.util.List;

/**
 * className: webConfig
 * description: mvc配置类
 * author: xiaopangio
 * date: 2022/7/24 11:42
 * version: 1.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    private String [] whiteList = {
            "/employee/login", "/employee/logout", "/backend/**", "/front/**","/common/**","/user/sendMsg","/user/login"};
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
    //    设置跨域访问
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns(whiteList);
    }
//    扩展消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,messageConverter);
    }
}
