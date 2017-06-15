package com.caiex.account.entity;

import java.math.BigDecimal;


public class OrderTicketCancel {
	
		private Integer order_id;
	private String ticketInfoId;
	private Integer agent_id;
	private java.sql.Timestamp trade_time;
	private Double trade_price;
	private Integer trade_type;
	private Integer state;
	private Integer ballType; 
	private Integer recycleState;
	private BigDecimal recyclePrice;
	private BigDecimal winMoney;
	
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public String getTicketInfoId() {
		return ticketInfoId;
	}
	public void setTicketInfoId(String ticketInfoId) {
		this.ticketInfoId = ticketInfoId;
	}
	public Integer getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(Integer agent_id) {
		this.agent_id = agent_id;
	}
	public java.sql.Timestamp getTrade_time() {
		return trade_time;
	}
	public void setTrade_time(java.sql.Timestamp trade_time) {
		this.trade_time = trade_time;
	}
	public Double getTrade_price() {
		return trade_price;
	}
	public void setTrade_price(Double trade_price) {
		this.trade_price = trade_price;
	}
	public Integer getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(Integer trade_type) {
		this.trade_type = trade_type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getBallType() {
		return ballType;
	}
	public void setBallType(Integer ballType) {
		this.ballType = ballType;
	}
	public Integer getRecycleState() {
		return recycleState;
	}
	public void setRecycleState(Integer recycleState) {
		this.recycleState = recycleState;
	}
	public BigDecimal getRecyclePrice() {
		return recyclePrice;
	}
	public void setRecyclePrice(BigDecimal recyclePrice) {
		this.recyclePrice = recyclePrice;
	}
	public BigDecimal getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}
	
	
}
