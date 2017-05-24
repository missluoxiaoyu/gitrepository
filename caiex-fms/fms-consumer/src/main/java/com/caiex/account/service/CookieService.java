package com.caiex.account.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieService {
	
	public void createCookie(String token,HttpServletRequest request,HttpServletResponse response);
	public void cleanCookie(HttpServletResponse response,HttpServletRequest request);
}
