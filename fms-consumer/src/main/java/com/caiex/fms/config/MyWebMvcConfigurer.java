package com.caiex.fms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.caiex.fms.filter.LoginInterceptor;



@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {
 
	 @Bean
	  LoginInterceptor localInterceptor() {
	        return new LoginInterceptor();
	  }
	
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		  registry.addInterceptor(localInterceptor()).addPathPatterns("/**")
          .excludePathPatterns("/account/user/tologin");
    }
    
 
}