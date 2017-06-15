package com.caiex.account.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component  
@ConfigurationProperties(prefix = "redis",locations = "file:C:/fms/config/fms.properties") 
//@ConfigurationProperties(prefix = "redis",locations = "file:/opt/FMS/consumer/fms.properties") 
public class RedisProperties {
	
	private String masterName;
	private String password;
	private String [] sentines;
	
	
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
	public String[] getSentines() {
		return sentines;
	}
	public void setSentines(String[] sentines) {
		this.sentines = sentines;
	}
	public RedisProperties(String masterName, String password, String[] sentines) {
		super();
		this.masterName = masterName;
		this.password = password;
		this.sentines = sentines;
	}
	public RedisProperties() {
		super();
	}
	
	
	
	
}
