package com.caiex.account.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.caiex.account.entity.SysUser;
import com.caiex.account.service.CookieService;
import com.caiex.account.utils.Md5Util;
import com.fms.api.LoginService;


@Controller
public class SysUserController {
	 private final static Logger log = Logger.getLogger(SysUserController.class);
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private CookieService cookieService;
	
	@RequestMapping(value="tologin")
	public void login(String email, String pwd,HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setCharacterEncoding("utf-8");
		response.setHeader( "Access-Control-Allow-Origin","*");
		if(StringUtils.isBlank(email)){
			response.getWriter().print("邮箱不能为空");
		}if(StringUtils.isBlank(pwd)){
			response.getWriter().print("邮箱不能为空");
		}
		SysUser sysUser = (SysUser) loginService.querySysUser(email,Md5Util.md5(pwd));
		String msg="用户登录日志";
		if(sysUser == null){
			log.info(msg + "[" + email + "]" + "账号或者密码输入错误.");
			response.getWriter().print("用户名或密码错误");
			return ;
		}
		String token = loginService.createKey(email, pwd);
		cookieService.createCookie(token, request, response);//将存贮信息放在cookie中
		int loginCount = sysUser.getLoginCount() != null ? sysUser.getLoginCount() : 0;
	    sysUser.setLoginCount(loginCount + 1);//增加登陆次数
	    sysUser.setLoginTime(new Timestamp(System.currentTimeMillis()));//更新登录时间
	    loginService.update(sysUser);//保存更新状态
		request.getSession().setAttribute("user", sysUser);//放入session
		//ModelAndView mv = new ModelAndView("redirect:/errorTicket.html");
		//return mv;
		response.sendRedirect("http://192.168.1.5:8083/account/errorTicket.html");
	}
	
	@RequestMapping(value="loginout")
	public void loginout(HttpServletRequest request, HttpServletResponse response){
		
		cookieService.cleanCookie(response, request);//清除cookie
		request.getSession().invalidate();
		try {
			response.sendRedirect("http://192.168.1.5:8083/account/1.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
