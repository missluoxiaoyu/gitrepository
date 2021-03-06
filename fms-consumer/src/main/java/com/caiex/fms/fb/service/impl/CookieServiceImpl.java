package com.caiex.fms.fb.service.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.caiex.fms.fb.service.CookieService;


@Service
public class CookieServiceImpl  implements CookieService{

	@Override
	public void createCookie(String token, HttpServletRequest request,
			HttpServletResponse response) {
		Cookie cookie = new Cookie("priKey",token);// 新建Cookie
		cookie.setDomain("localhost");
		cookie.setPath("/");
		cookie.setMaxAge(0);                          
		response.addCookie(cookie);   
	}

	@Override
	public void cleanCookie(HttpServletResponse response,HttpServletRequest request) {
		Cookie [] cookies = request.getCookies();
		 for (Cookie cookie : cookies) {
			 if(cookie.getName().equals("prikey")){
			
				 Cookie cookie1 = new Cookie(cookie.getName(), null);
			      cookie1.setMaxAge(0);
			      response.addCookie(cookie1); 
			 }
		}
	}

}
