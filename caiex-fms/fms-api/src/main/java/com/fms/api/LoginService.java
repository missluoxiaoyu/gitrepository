package com.fms.api;

public interface LoginService {
	public Object querySysUser(String email,String pwd);
	//创建token并存在cookie中
	public String createKey(String email,String pwd);
	
	public void update(Object sysUser);
}
