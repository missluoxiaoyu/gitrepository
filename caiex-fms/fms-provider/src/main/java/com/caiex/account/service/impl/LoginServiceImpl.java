package com.caiex.account.service.impl;

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

import com.caiex.account.mapper.SysUserMapper;
import com.fms.api.LoginService;
import com.fms.entity.SysUser;



@Service("loginService")
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
	public String createKey(String email,String pwd){
			
		Key key = MacProvider.generateKey();
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
			String token = Jwts.builder()
			        .setIssuer(email)
			        .setSubject(pwd)
			        .signWith(signatureAlgorithm,key)
			        .compact();
			return token;	
	}
	

	@Override
	public void update(Object sysUser) {
		sysUserMapper.update((SysUser)sysUser);
	}

}
