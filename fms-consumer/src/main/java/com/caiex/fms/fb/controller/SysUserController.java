package com.caiex.fms.fb.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.dbservice.currentdb.api.LoginService;
import com.caiex.dbservice.currentdb.entity.SysUser;
import com.caiex.fms.fb.service.CookieService;
import com.caiex.fms.utils.CookieUtils;
import com.caiex.fms.utils.Md5Util;
import com.caiex.fms.utils.SessionUtils;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("user/")
public class SysUserController {
	 private final static Logger log = Logger.getLogger(SysUserController.class);
	
	@Autowired
	private LoginService loginService;
	//@Autowired
	//private CookieService cookieService;
	

	
	
	@RequestMapping(value="tologin",method=RequestMethod.POST)
	@ResponseBody
	public Response  login(String email, String pwd,HttpServletRequest request, HttpServletResponse response) throws Exception{
		Response res = new Response();
		
		if(StringUtils.isBlank(email)){
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("邮箱不能为空");
			return res;
			
		}if(StringUtils.isBlank(pwd)){
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("密码不能为空");
			return res;
		}
		SysUser sysUser =  loginService.querySysUser(email,Md5Util.md5(pwd));
		String msg="用户登录日志";
		if(sysUser == null){
			log.info(msg + "[" + email + "]" + "账号或者密码输入错误.");
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("账号或密码错误");
			return res;
		}
		String token = loginService.createKey(email, pwd);
		//cookieService.createCookie(token, request, response);//将存贮信息放在cookie中
		try {
		
			int loginCount = sysUser.getLoginCount() != null ? sysUser.getLoginCount() : 0;
			sysUser.setLoginCount(loginCount + 1);//增加登陆次数
			sysUser.setLoginTime(new Timestamp(System.currentTimeMillis()));//更新登录时间
			loginService.update(sysUser);//保存更新状态
			SessionUtils.setUser(request, sysUser);
			
			Cookie c = new Cookie("priKey", token);
			c.setMaxAge(8*60*60*1000);  
			c.setPath(request.getContextPath());  
			response.addCookie(c);   
			
			log.info("id为"+sysUser.getId()+"的"+sysUser.getNickName()+"登陆成功");
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("登录成功");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("登录失败");
		}
		
		return res;
	}
	
	
	
	
	@RequestMapping(value="loginout")
	public void loginout(HttpServletRequest request, HttpServletResponse response){
		//cookieService.cleanCookie(response, request);//清除cookie
		Cookie cookie = new Cookie("priKey", "");  
		cookie.setMaxAge(0);  
		cookie.setPath(request.getContextPath());  
		response.addCookie(cookie);   
		request.getSession().invalidate();
		try {
			response.sendRedirect("http://"+request.getLocalAddr()+":"+request.getLocalPort()+"/account/index.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
