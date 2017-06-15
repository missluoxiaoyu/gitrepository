package com.caiex.account.model;

import java.sql.Timestamp;
/**
 * OrderTicket 的页面json 返回结果
 * */
public class OrderTicketModel {
	
	private Integer page;
	private Integer size;
	
	private Long orderId;
	private String ticketInfoId;//与order_ticket_info 中的id相关联
	private Integer agentId;
	private Timestamp tradeTime;
	private Double tradePrice;
	private Integer tradeType;
	private Integer state;
	private Integer recycleState;
	private  Double recyclePrice;
	private Double winMoney;
	private String tkId;
	
	//新加字段
	private String requestTimestamp;

	private String startDate;//渠道报表统计的开始时间
	private String endDate;//渠道报表统计的结束时间
	private String uid;//用户id
	private String agentName;//渠道名称
	private String agentCode;//渠道编码
	private Integer inplay;//订单类型
	private Double totalPrice;//   total price: 最大中奖额
	private String bettingproducts;//   betting product: 玩法 [HAD, HHAD, FCA]
	private Integer investmentNum;//投注倍数 TotalInvestment/UnitInvestment
	private Integer totalInvestment;//   total investment: 此票总投注额
	private Integer addAwardAmount;//   M: 串关数
	private Integer agentNum;//   渠道编号
	private Double payoutrate;
	private Integer ballType;
	
	private String inplayMessage;
	private String ballTypeMessage;
	private String stateMessage;
	private String recycleMessage;
	private String tradeMessage;
	
	
	
	
	public String getTradeMessage() {
		return tradeMessage;
	}

	public void setTradeMessage(String tradeMessage) {
		this.tradeMessage = tradeMessage;
	}

	public String getInplayMessage() {
		return inplayMessage;
	}

	public void setInplayMessage(String inplayMessage) {
		this.inplayMessage = inplayMessage;
	}

	public String getBallTypeMessage() {
		return ballTypeMessage;
	}

	public void setBallTypeMessage(String ballTypeMessage) {
		this.ballTypeMessage = ballTypeMessage;
	}

	public String getStateMessage() {
		return stateMessage;
	}

	public void setStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}

	public String getRecycleMessage() {
		return recycleMessage;
	}

	public void setRecycleMessage(String recycleMessage) {
		this.recycleMessage = recycleMessage;
	}

	public Integer getBallType() {
		return ballType;
	}

	public void setBallType(Integer ballType) {
		this.ballType = ballType;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
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

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
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

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getRecycleState() {
		return recycleState;
	}

	public void setRecycleState(Integer recycleState) {
		this.recycleState = recycleState;
	}

	public Double getRecyclePrice() {
		return recyclePrice;
	}

	public void setRecyclePrice(Double recyclePrice) {
		this.recyclePrice = recyclePrice;
	}

	public Double getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(Double winMoney) {
		this.winMoney = winMoney;
	}

	public String getTkId() {
		return tkId;
	}

	public void setTkId(String tkId) {
		this.tkId = tkId;
	}

	public String getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
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

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
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


	public Integer getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(Integer totalInvestment) {
		this.totalInvestment = totalInvestment;
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

	public Double getPayoutrate() {
		return payoutrate;
	}

	public void setPayoutrate(Double payoutrate) {
		this.payoutrate = payoutrate;
	}

		
}
