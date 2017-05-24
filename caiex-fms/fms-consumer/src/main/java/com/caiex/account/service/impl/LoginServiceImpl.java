/*package com.caiex.account.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.entity.SysUser;
import com.caiex.account.mapper.SysUserMapper;
import com.caiex.account.service.LoginService;
import com.caiex.account.utils.CookieUtils;


@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Override
	public SysUser querySysUser(String email,String pwd) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("email", email);
		paramsMap.put("pwd", pwd);
		return sysUserMapper.queryLogin(paramsMap);
	}
	
	//添加token验证
	@Override
	public void createToken(String email,String pwd,HttpServletResponse response,HttpServletRequest request){
			Key key = MacProvider.generateKey();
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
			String token = Jwts.builder()
			        .setIssuer(email)
			        .setSubject(pwd)
			        .signWith(signatureAlgorithm,key)
			        .compact();
			CookieUtils.setCookie(request, response, "priKey", token, 60*60*1000, true);
	}
	
	@Override
	public boolean getToken(HttpServletResponse response,HttpServletRequest request){
		Key key = MacProvider.generateKey();
		String token = CookieUtils.getCookieValue(request, "prikey", true);
		if(token == null){
			System.out.println("请重新登录");
			return false;
		}
		Jws<Claims> result = Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
		return true;
	}
	
	@Override
	public void cleanCooie(HttpServletResponse response,HttpServletRequest request){
		Cookie [] cookies = request.getCookies();
		 for (Cookie cookie : cookies) {
			if(cookie.getName().equals("priKey")){
				cookie.setMaxAge(0);
			}
		}
	}

	@Override
	public void update(Object sysUser) {
		sysUserMapper.update((SysUser)sysUser);
	}
	
}
*/