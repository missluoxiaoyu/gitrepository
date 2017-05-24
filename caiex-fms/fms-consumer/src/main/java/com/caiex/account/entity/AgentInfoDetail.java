package com.caiex.account.entity;

import java.util.Date;

public class AgentInfoDetail {
	
	private int id ;
	private String agentName;//渠道名称
	private String agentCode;//渠道编码
	private double oldPreStore;//原预存额
	private double newPreStore;//新预存额
	private String preStoreDetail;//预存额变化详情
	private Date operateTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public double getOldPreStore() {
		return oldPreStore;
	}
	public void setOldPreStore(double oldPreStore) {
		this.oldPreStore = oldPreStore;
	}
	public double getNewPreStore() {
		return newPreStore;
	}
	public void setNewPreStore(double newPreStore) {
		this.newPreStore = newPreStore;
	}
	public String getPreStoreDetail() {
		return preStoreDetail;
	}
	public void setPreStoreDetail(String preStoreDetail) {
		this.preStoreDetail = preStoreDetail;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	
	
}
