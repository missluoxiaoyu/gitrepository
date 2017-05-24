package com.caiex.account.mapper;

import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.SysUser;



@MapperScan
public interface SysUserMapper {
	SysUser queryLogin(Map<String, String> paramsMap);
	public void update(SysUser sysUser);
}
