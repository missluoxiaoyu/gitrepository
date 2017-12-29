package com.caiex.fms.redis;

import java.util.Arrays;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
	
	private String masterName;
	private String password;
	private String [] sentinels;
	public String getMasterName() {
		return masterName;
	}
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String[] getSentinels() {
		return sentinels;
	}
	public void setSentinels(String[] sentinels) {
		this.sentinels = sentinels;
	}

	
	
	

	

	
}
