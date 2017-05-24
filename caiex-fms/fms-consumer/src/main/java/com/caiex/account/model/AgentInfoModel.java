package com.caiex.account.model;
/**
 * AgentInfo
 * */
public class AgentInfoModel {
	private String agentName;//渠道名称
	private String agentCode;
	private Integer agentId;//渠道编号
	private int userAmount;//注册用户数
	private int orderAmount;//订单数
	private int updateflag; // 页面修改的标志
	private double tradePrice;//交易额
	private double winMoney;//中奖额
	private double profit;//盈亏
	private double profitability;//盈利率
	private double prestore;//预存额
	private String url;//渠道url
	
	private String password; //	渠道密码
	
	private double prestoreAlarm;//预存报警
	private int agentSell;//渠道开停售
	private String agentSellMessage;
	
	
	
	
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
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public int getUserAmount() {
		return userAmount;
	}
	public void setUserAmount(int userAmount) {
		this.userAmount = userAmount;
	}
	public int getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}
	public int getUpdateflag() {
		return updateflag;
	}
	public void setUpdateflag(int updateflag) {
		this.updateflag = updateflag;
	}
	public double getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(double tradePrice) {
		this.tradePrice = tradePrice;
	}
	public double getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(double winMoney) {
		this.winMoney = winMoney;
	}
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public double getProfitability() {
		return profitability;
	}
	public void setProfitability(double profitability) {
		this.profitability = profitability;
	}
	public double getPrestore() {
		return prestore;
	}
	public void setPrestore(double prestore) {
		this.prestore = prestore;
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
	public String getAgentSellMessage() {
		return agentSellMessage;
	}
	public void setAgentSellMessage(String agentSellMessage) {
		this.agentSellMessage = agentSellMessage;
	}
	

	
}
