package com.caiex.fms.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.caiex.dbservice.currentdb.entity.SysUser;
import com.caiex.fms.utils.CookieUtils;
import com.caiex.fms.utils.SessionUtils;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		 
			SysUser user = SessionUtils.getUser(request);
			
			String url  = request.getRequestURL().toString();
			if(url.indexOf("account/user/tologin") >0){
				return true;
			}
			
			Cookie cookies[] = request.getCookies();
			
			if(cookies != null){
	 	  		for (Cookie cookie : cookies) {
	 	  				if(cookie.getName().equals("priKey")){
	 	  					return true;
	 	  				} 
	 	  		}
			}
			
			
			/*if(cookies != null){
	 	  		for (Cookie cookie : cookies) {
	 	  				if(cookie.getName().equals("JSESSIONID")){
	 	  					//CookieUtils.setCookie(request, response, "JSESSIONID", cookie.getValue(),100, true);
	 	  					return true;
	 	  				} 
	 	  		}
			}*/
	    	 response.sendRedirect("http://"+request.getLocalAddr()+":"+request.getLocalPort()+"/account/index.html");
	    	 return false;
	   
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	

	
}
