/*package com.caiex.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.caiex.account.entity.*;
import com.caiex.account.utils.*;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		response.setHeader( "Access-Control-Allow-Origin","*");
		//response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");  
		 String url = request.getRequestURI();
		 if(url.indexOf("tologin.do")>=0){  
	            return true;  
	        }  
		//=========================================
		 if(url.indexOf("/dailyAllup/queryAll.do")>=0){  
	            return true;  
	        }//测试/dailyAllup/queryAll.do放行
	    //==========================================
		
		Cookie cookies[] = request.getCookies();
		if(cookies == null){
			System.out.println("cookie为null");
		}else{
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("priKey")){
					System.out.println(cookie.getValue());
					return true;
				} 
			}
		}
		response.sendRedirect("http://192.168.1.209:8083/account/login.html");
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
*/