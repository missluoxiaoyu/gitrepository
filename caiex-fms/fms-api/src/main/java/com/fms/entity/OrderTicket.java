package com.fms.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderTicket {
	
	private Long orderId;
	private String ticketInfoId;
	private int agentId;
	private Timestamp tradeTime;
	private Double tradePrice;
	private int tradeType;
	private int state;
	private int recycleState;
	private  double recyclePrice;
	private double winMoney;
	
	public String getTicketInfoId() {
		return ticketInfoId;
	}
	public void setTicketInfoId(String ticketInfoId) {
		this.ticketInfoId = ticketInfoId;
	}
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	public Timestamp getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(Timestamp tradeTime) {
		this.tradeTime = tradeTime;
	}
	public Double getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(Double tradePrice) {
		this.tradePrice = tradePrice;
	}
	public int getTradeType() {
		return tradeType;
	}
	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getRecycleState() {
		return recycleState;
	}
	public void setRecycleState(int recycleState) {
		this.recycleState = recycleState;
	}
	
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
	public OrderTicket() {
		super();
	}
	
}
