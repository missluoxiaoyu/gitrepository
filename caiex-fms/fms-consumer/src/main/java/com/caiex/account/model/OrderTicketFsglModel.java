package com.caiex.account.model;
/**
 * 
 * */

public class OrderTicketFsglModel {

	
	 private String product;
	 private double totalInvestment ;
	 private double invest;
	 private double totalPrice;
	 private double payoutRate;
	 private double winprice;
	 private double average;
	 
	 
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public double getTotalInvestment() {
		return totalInvestment;
	}
	public void setTotalInvestment(double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	public double getInvest() {
		return invest;
	}
	public void setInvest(double invest) {
		this.invest = invest;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getPayoutRate() {
		return payoutRate;
	}
	public void setPayoutRate(double payoutRate) {
		this.payoutRate = payoutRate;
	}
	public double getWinprice() {
		return winprice;
	}
	public void setWinprice(double winprice) {
		this.winprice = winprice;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	
	public OrderTicketFsglModel(String product, double totalInvestment,
			double invest, double totalPrice, double payoutRate,
			double winprice, double average) {
		super();
		this.product = product;
		this.totalInvestment = totalInvestment;
		this.invest = invest;
		this.totalPrice = totalPrice;
		this.payoutRate = payoutRate;
		this.winprice = winprice;
		this.average = average;
	}
	public OrderTicketFsglModel() {
		super();
	}
	
	 
	 
}
