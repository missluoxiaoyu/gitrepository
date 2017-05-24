package com.caiex.account.entity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderTicketError {
	
	private int id;
	private String tkId;
	private int tradeState;
	private int agentId;
	private Date tradeTime;
	private double tradePrice;
	private double recyclePrice;
	private double winMoney;
	private int state;
	private Date queryTime;
	
	//新加字段
	private String requestTimestamp;
	
	
	
	public String getRequestTimestamp() {
		return requestTimestamp;
	}
	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	
	
	
	public Date getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTkId() {
		return tkId;
	}
	public void setTkId(String tkId) {
		this.tkId = tkId;
	}
	
	public int getTradeState() {
		return tradeState;
	}
	public void setTradeState(int tradeState) {
		this.tradeState = tradeState;
	}
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
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
	
	
	public double getRecyclePrice() {
		return recyclePrice;
	}
	public void setRecyclePrice(double recyclePrice) {
		this.recyclePrice = recyclePrice;
	}
	public double getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(double winMoney) {
		this.winMoney = winMoney;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public OrderTicketError() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + agentId;
		result = prime * result + id;
		result = prime * result
				+ ((queryTime == null) ? 0 : queryTime.hashCode());
		long temp;
		temp = Double.doubleToLongBits(recyclePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + state;
		result = prime * result + ((tkId == null) ? 0 : tkId.hashCode());
		temp = Double.doubleToLongBits(tradePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + tradeState;
		result = prime * result
				+ ((tradeTime == null) ? 0 : tradeTime.hashCode());
		temp = Double.doubleToLongBits(winMoney);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderTicketError other = (OrderTicketError) obj;
		if (agentId != other.agentId)
			return false;
		if (id != other.id)
			return false;
		if (queryTime == null) {
			if (other.queryTime != null)
				return false;
		} else if (!queryTime.equals(other.queryTime))
			return false;
		if (Double.doubleToLongBits(recyclePrice) != Double
				.doubleToLongBits(other.recyclePrice))
			return false;
		if (state != other.state)
			return false;
		if (tkId == null) {
			if (other.tkId != null)
				return false;
		} else if (!tkId.equals(other.tkId))
			return false;
		if (Double.doubleToLongBits(tradePrice) != Double
				.doubleToLongBits(other.tradePrice))
			return false;
		if (tradeState != other.tradeState)
			return false;
		if (tradeTime == null) {
			if (other.tradeTime != null)
				return false;
		} else if (!tradeTime.equals(other.tradeTime))
			return false;
		if (Double.doubleToLongBits(winMoney) != Double
				.doubleToLongBits(other.winMoney))
			return false;
		return true;
	}
	
}
