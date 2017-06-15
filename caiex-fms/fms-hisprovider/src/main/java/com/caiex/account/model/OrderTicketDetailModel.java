package com.caiex.account.model;

import java.io.Serializable;

/**
 * daily all up
 * */

public class OrderTicketDetailModel implements Serializable{

	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer local_m;
	private String product;
	 private Double totalInvestment ;
	 private Double invest;
	 private Double totalPrice;
	 private Double payoutRate;
	 private Double payout;
	 private Double winprice;
	 private Double average;
	 
	 
	 
	 
	public Integer getLocal_m() {
		return local_m;
	}
	public void setLocal_m(Integer local_m) {
		this.local_m = local_m;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
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
	public Double getPayout() {
		return payout;
	}
	public void setPayout(Double payout) {
		this.payout = payout;
	}
	public Double getWinprice() {
		return winprice;
	}
	public void setWinprice(Double winprice) {
		this.winprice = winprice;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public OrderTicketDetailModel(String product, Double totalInvestment,
			Double invest, Double totalPrice, Double payoutRate,
			Double winprice, Double average) {
		super();
		this.product = product;
		this.totalInvestment = totalInvestment;
		this.invest = invest;
		this.totalPrice = totalPrice;
		this.payoutRate = payoutRate;
		this.winprice = winprice;
		this.average = average;
	}
	public OrderTicketDetailModel() {
		super();
	}
	public OrderTicketDetailModel(Integer local_m, String product,
			Double totalInvestment, Double invest, Double totalPrice,
			Double payoutRate, Double payout) {
		super();
		this.local_m = local_m;
		this.product = product;
		this.totalInvestment = totalInvestment;
		this.invest = invest;
		this.totalPrice = totalPrice;
		this.payoutRate = payoutRate;
		this.payout = payout;
	}
	
	
	 
	 
}
