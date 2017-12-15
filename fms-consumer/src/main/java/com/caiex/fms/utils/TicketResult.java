package com.caiex.fms.utils;

import java.math.BigDecimal;
import java.util.Date;

public class TicketResult {
	private String tkId;
	private int schemeState;
	private Date tradeTime;
	private double tradePrice;
	private double winMoney;
	private String stateMessage;
	private Integer agentid;
	
	
	
	public String getStateMessage() {
		return stateMessage;
	}
	public void setStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}
	public Integer getAgentid() {
		return agentid;
	}
	public void setAgentid(Integer agentid) {
		this.agentid = agentid;
	}
	
	
	public int getSchemeState() {
		return schemeState;
	}
	public void setSchemeState(int schemeState) {
		this.schemeState = schemeState;
	}
	public Date getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	public double getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(double tradePrice) {
		this.tradePrice = tradePrice;
	}
	
	
	public String getTkId() {
		return tkId;
	}
	public void setTkId(String tkId) {
		this.tkId = tkId;
	}
	public TicketResult() {
		super();
	}
	
	public double getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(double winMoney) {
		this.winMoney = winMoney;
	}
	
	
	public TicketResult(String tkId, int schemeState, Date tradeTime,
			double tradePrice, double winMoney) {
		super();
		this.tkId = tkId;
		this.schemeState = schemeState;
		this.tradeTime = tradeTime;
		this.tradePrice = tradePrice;
		this.winMoney = winMoney;
	}
	
	@Override
	public String toString() {
		return "TicketResult [schemeState=" + schemeState + ", tradeTime="
				+ tradeTime + ", tradePrice=" + tradePrice + ", winMoney="
				+ winMoney + "]";
	}
	
}
