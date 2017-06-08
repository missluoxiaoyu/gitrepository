package com.caiex.account.model;

import java.sql.Timestamp;
/**
 * OrderTicket
 * */
public class OrderTicketModel {
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
	private String tkId;
	
	//新加字段
	private String requestTimestamp;
	
	//
	private String startDate;//渠道报表统计的开始时间
	private String endDate;//渠道报表统计的结束时间
	private String uid;//用户id
	private String agentName;//渠道名称
	private String agentCode;//渠道编码
	private Integer inplay;//订单类型
	private Double total_price;//   total price: 最大中奖额
	private String bettingproducts;//   betting product: 玩法 [HAD, HHAD, FCA]
	private Integer investmentNum;//投注倍数 TotalInvestment/UnitInvestment
	private Integer totalinvestment;//   total investment: 此票总投注额
	private Integer addAwardAmount;//   M: 串关数
	private Integer agentNum;//   渠道编号
	
	
	
	
	
	public String getRequestTimestamp() {
		return requestTimestamp;
	}
	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	
	
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
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
	
	public String getTkId() {
		return tkId;
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
	public void setTkId(String tkId) {
		this.tkId = tkId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public Integer getInplay() {
		return inplay;
	}
	public void setInplay(Integer inplay) {
		this.inplay = inplay;
	}
	public Double getTotal_price() {
		return total_price;
	}
	public void setTotal_price(Double total_price) {
		this.total_price = total_price;
	}
	public String getBettingproducts() {
		return bettingproducts;
	}
	public void setBettingproducts(String bettingproducts) {
		this.bettingproducts = bettingproducts;
	}
	public Integer getInvestmentNum() {
		return investmentNum;
	}
	public void setInvestmentNum(Integer investmentNum) {
		this.investmentNum = investmentNum;
	}
	public Integer getTotalinvestment() {
		return totalinvestment;
	}
	public void setTotalinvestment(Integer totalinvestment) {
		this.totalinvestment = totalinvestment;
	}
	public Integer getAddAwardAmount() {
		return addAwardAmount;
	}
	public void setAddAwardAmount(Integer addAwardAmount) {
		this.addAwardAmount = addAwardAmount;
	}
	public Integer getAgentNum() {
		return agentNum;
	}
	public void setAgentNum(Integer agentNum) {
		this.agentNum = agentNum;
	}
	
	
	
}
