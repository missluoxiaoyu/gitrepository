package com.caiex.account.entity;

import java.util.Date;

public class AgentInfo {
	
	private Integer agentNum;//渠道编号
	private String agentCode;
	private String url;//路径
	private String password;//密码
	private String agentName;//渠道名称
	private Date createTime;//创建时间
	private int creator;//创建者
	private Date updateTime;//更新时间
	private int updator;//更新者
	private String salt;
	private int id;
	private double prestore;//预存额
	private double prestoreAlarm;//预存额报警
	private int agentSell;//开停售
	
	
	public Integer getAgentNum() {
		return agentNum;
	}
	public void setAgentNum(Integer agentNum) {
		this.agentNum = agentNum;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getUpdator() {
		return updator;
	}
	public void setUpdator(int updator) {
		this.updator = updator;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrestore() {
		return prestore;
	}
	public void setPrestore(double prestore) {
		this.prestore = prestore;
	}
	public double getPrestoreAlarm() {
		return prestoreAlarm;
	}
	public void setPrestoreAlarm(double prestoreAlarm) {
		this.prestoreAlarm = prestoreAlarm;
	}
	public int getAgentSell() {
		return agentSell;
	}
	public void setAgentSell(int agentSell) {
		this.agentSell = agentSell;
	}
	
}
