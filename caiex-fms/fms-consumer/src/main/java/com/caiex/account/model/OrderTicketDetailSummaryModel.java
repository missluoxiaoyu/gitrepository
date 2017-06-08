package com.caiex.account.model;
/**
 * daily summary 返回json
 * 
 * */


public class OrderTicketDetailSummaryModel {

	 private String week;
	 private Integer totalNum;
	
	
	 private Double totalInvestment ;
	 private Double invest;
	 private Double totalPrice;
	 private Double payoutRate;
	 
	 private Integer fbNum;
	 
	
	 
	 private Double totalInvestmentFb ;
	 private Double investFb;
	 private Double totalPriceFb;
	 private Double payoutRateFb;
	 
	 private Integer bkNum;
	
	 
	 private Double totalInvestmentBk ;
	 private Double investBk;
	 private Double totalPriceBk;
	 private Double payoutRateBk;
	 
	 
	 
	 
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Double getTotalInvestment() {
		return totalInvestment;
	}
	public void setTotalInvestment(Double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	public Double getInvest() {
		return invest;
	}
	public void setInvest(Double invest) {
		this.invest = invest;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Double getPayoutRate() {
		return payoutRate;
	}
	public void setPayoutRate(Double payoutRate) {
		this.payoutRate = payoutRate;
	}
	public Integer getFbNum() {
		return fbNum;
	}
	public void setFbNum(Integer fbNum) {
		this.fbNum = fbNum;
	}
	public Double getTotalInvestmentFb() {
		return totalInvestmentFb;
	}
	public void setTotalInvestmentFb(Double totalInvestmentFb) {
		this.totalInvestmentFb = totalInvestmentFb;
	}
	public Double getInvestFb() {
		return investFb;
	}
	public void setInvestFb(Double investFb) {
		this.investFb = investFb;
	}
	public Double getTotalPriceFb() {
		return totalPriceFb;
	}
	public void setTotalPriceFb(Double totalPriceFb) {
		this.totalPriceFb = totalPriceFb;
	}
	public Double getPayoutRateFb() {
		return payoutRateFb;
	}
	public void setPayoutRateFb(Double payoutRateFb) {
		this.payoutRateFb = payoutRateFb;
	}
	public Integer getBkNum() {
		return bkNum;
	}
	public void setBkNum(Integer bkNum) {
		this.bkNum = bkNum;
	}
	public Double getTotalInvestmentBk() {
		return totalInvestmentBk;
	}
	public void setTotalInvestmentBk(Double totalInvestmentBk) {
		this.totalInvestmentBk = totalInvestmentBk;
	}
	public Double getInvestBk() {
		return investBk;
	}
	public void setInvestBk(Double investBk) {
		this.investBk = investBk;
	}
	public Double getTotalPriceBk() {
		return totalPriceBk;
	}
	public void setTotalPriceBk(Double totalPriceBk) {
		this.totalPriceBk = totalPriceBk;
	}
	public Double getPayoutRateBk() {
		return payoutRateBk;
	}
	public void setPayoutRateBk(Double payoutRateBk) {
		this.payoutRateBk = payoutRateBk;
	}
	public OrderTicketDetailSummaryModel(String week, Integer totalNum,
			Double totalInvestment, Double invest, Double totalPrice,
			Double payoutRate, Integer fbNum, Double totalInvestmentFb,
			Double investFb, Double totalPriceFb, Double payoutRateFb,
			Integer bkNum, Double totalInvestmentBk, Double investBk,
			Double totalPriceBk, Double payoutRateBk) {
		super();
		this.week = week;
		this.totalNum = totalNum;
		this.totalInvestment = totalInvestment;
		this.invest = invest;
		this.totalPrice = totalPrice;
		this.payoutRate = payoutRate;
		this.fbNum = fbNum;
		this.totalInvestmentFb = totalInvestmentFb;
		this.investFb = investFb;
		this.totalPriceFb = totalPriceFb;
		this.payoutRateFb = payoutRateFb;
		this.bkNum = bkNum;
		this.totalInvestmentBk = totalInvestmentBk;
		this.investBk = investBk;
		this.totalPriceBk = totalPriceBk;
		this.payoutRateBk = payoutRateBk;
	}
	public OrderTicketDetailSummaryModel() {
		super();
	}
	

	 
}
