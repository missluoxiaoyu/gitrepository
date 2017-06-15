package com.caiex.account.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.caiex.filter.LoginInterceptor;



@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {
  @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	InterceptorRegistration ir = registry.addInterceptor(new LoginInterceptor());
    	 // 配置拦截的路径
        ir.addPathPatterns("/**");
        // 配置不拦截的路径
       // ir.excludePathPatterns("/**.html");
       // super.addInterceptors(registry);
    }
    
  /* public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(new HandlerInterceptorAdapter() {

	          @Override
	          public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
	                                   Object handler) throws Exception {
	              System.out.println("interceptor====222");
	              return true;
	          }
	      }).addPathPatterns("/*");
	  }*/
   
    
    @Override 
    public void addViewControllers(ViewControllerRegistry registry) { 
	    registry.addViewController("/error").setViewName("error.html"); 
	    registry.setOrder(Ordered.HIGHEST_PRECEDENCE); 
    }

    @Override 
    public void configurePathMatch(PathMatchConfigurer configurer) { 
    super.configurePathMatch(configurer); 
    configurer.setUseSuffixPatternMatch(true); 
   
    }
    
 
   
}