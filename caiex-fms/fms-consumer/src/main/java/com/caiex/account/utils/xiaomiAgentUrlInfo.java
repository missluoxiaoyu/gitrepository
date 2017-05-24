package com.caiex.account.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "xiaomi",locations = "file:/opt/FMS/consumer/fms.properties") 
//@ConfigurationProperties(prefix = "xiaomi",locations = "file:C:/fms/config/fms.properties")  
public class xiaomiAgentUrlInfo {
	private int agentid;
	
	private String onlineGetOrderUrl;
	private String onlineGetOrderListUrl;
	private String testGetOrderUrl;
	private String testGetOrderListUrl;
	
	public int getAgentid() {
		return agentid;
	}
	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}
	public String getOnlineGetOrderUrl() {
		return onlineGetOrderUrl;
	}
	public void setOnlineGetOrderUrl(String onlineGetOrderUrl) {
		this.onlineGetOrderUrl = onlineGetOrderUrl;
	}
	public String getOnlineGetOrderListUrl() {
		return onlineGetOrderListUrl;
	}
	public void setOnlineGetOrderListUrl(String onlineGetOrderListUrl) {
		this.onlineGetOrderListUrl = onlineGetOrderListUrl;
	}
	public String getTestGetOrderUrl() {
		return testGetOrderUrl;
	}
	public void setTestGetOrderUrl(String testGetOrderUrl) {
		this.testGetOrderUrl = testGetOrderUrl;
	}
	public String getTestGetOrderListUrl() {
		return testGetOrderListUrl;
	}
	public void setTestGetOrderListUrl(String testGetOrderListUrl) {
		this.testGetOrderListUrl = testGetOrderListUrl;
	}
	
	public xiaomiAgentUrlInfo(int agentid, String onlineGetOrderUrl,
			String onlineGetOrderListUrl, String testGetOrderUrl,
			String testGetOrderListUrl) {
		super();
		this.agentid = agentid;
		this.onlineGetOrderUrl = onlineGetOrderUrl;
		this.onlineGetOrderListUrl = onlineGetOrderListUrl;
		this.testGetOrderUrl = testGetOrderUrl;
		this.testGetOrderListUrl = testGetOrderListUrl;
	}
	public xiaomiAgentUrlInfo() {
		super();
	}
	
	
	
	

/*
	public Map info(){
		Map<String,AgentUrlInfo> map = new HashMap<>();
		map.put("xiaomi", new AgentUrlInfo(111, "http://xiaomicp.f3322.net:24081/ipub/rfq/getOrderInfo", "http://xiaomicp.f3322.net:24081/ipub/rfq/batchGetOrderInfo"));
		map.put("biying", new AgentUrlInfo(117, "http://api.5biying.com/query/caiex/GetOrder", "http://api.5biying.com/query/caiex/GetOrderList"));
		return map;
	 }*/
}
