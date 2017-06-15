package com.caiex.account.model;
/**
 *daily break down 页面返回结果
 * */
public class OrderTicketDetailFinancialModel {
	
	 private String column1;
	 private Integer column2;
	
	 private String product;
	//今日统计
	 private Double totalInvestment ;
	 private Double invest;
	 private Double totalPrice;
	 private Double payoutRate;
	 
	 //串关
	 private Double totalInvestmentAllup ;
	 private Double investAllup;
	 private Double totalPriceAllup;
	 private Double payoutRateAllup;
	 
	//单关
		 private Double totalInvestmentFsgl ;
		 private Double investFsgl;
		 private Double totalPriceFsgl;
		 private Double payoutRateFsgl;
		 private Double winpriceFsgl;
		 private Double averageFsgl;
	 //level0
	 private Double totalInvestmentLevel0 ;
	 private Double investLevel0;
	 private Double totalPriceLevel0;
	 private Double payoutRateLevel0;
	public String getColumn1() {
		return column1;
	}
	public void setColumn1(String column1) {
		this.column1 = column1;
	}
	public Integer getColumn2() {
		return column2;
	}
	public void setColumn2(Integer column2) {
		this.column2 = column2;
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
	public Double getTotalInvestmentAllup() {
		return totalInvestmentAllup;
	}
	public void setTotalInvestmentAllup(Double totalInvestmentAllup) {
		this.totalInvestmentAllup = totalInvestmentAllup;
	}
	public Double getInvestAllup() {
		return investAllup;
	}
	public void setInvestAllup(Double investAllup) {
		this.investAllup = investAllup;
	}
	public Double getTotalPriceAllup() {
		return totalPriceAllup;
	}
	public void setTotalPriceAllup(Double totalPriceAllup) {
		this.totalPriceAllup = totalPriceAllup;
	}
	public Double getPayoutRateAllup() {
		return payoutRateAllup;
	}
	public void setPayoutRateAllup(Double payoutRateAllup) {
		this.payoutRateAllup = payoutRateAllup;
	}
	public Double getTotalInvestmentFsgl() {
		return totalInvestmentFsgl;
	}
	public void setTotalInvestmentFsgl(Double totalInvestmentFsgl) {
		this.totalInvestmentFsgl = totalInvestmentFsgl;
	}
	public Double getInvestFsgl() {
		return investFsgl;
	}
	public void setInvestFsgl(Double investFsgl) {
		this.investFsgl = investFsgl;
	}
	public Double getTotalPriceFsgl() {
		return totalPriceFsgl;
	}
	public void setTotalPriceFsgl(Double totalPriceFsgl) {
		this.totalPriceFsgl = totalPriceFsgl;
	}
	public Double getPayoutRateFsgl() {
		return payoutRateFsgl;
	}
	public void setPayoutRateFsgl(Double payoutRateFsgl) {
		this.payoutRateFsgl = payoutRateFsgl;
	}
	public Double getWinpriceFsgl() {
		return winpriceFsgl;
	}
	public void setWinpriceFsgl(Double winpriceFsgl) {
		this.winpriceFsgl = winpriceFsgl;
	}
	public Double getAverageFsgl() {
		return averageFsgl;
	}
	public void setAverageFsgl(Double averageFsgl) {
		this.averageFsgl = averageFsgl;
	}
	public Double getTotalInvestmentLevel0() {
		return totalInvestmentLevel0;
	}
	public void setTotalInvestmentLevel0(Double totalInvestmentLevel0) {
		this.totalInvestmentLevel0 = totalInvestmentLevel0;
	}
	public Double getInvestLevel0() {
		return investLevel0;
	}
	public void setInvestLevel0(Double investLevel0) {
		this.investLevel0 = investLevel0;
	}
	public Double getTotalPriceLevel0() {
		return totalPriceLevel0;
	}
	public void setTotalPriceLevel0(Double totalPriceLevel0) {
		this.totalPriceLevel0 = totalPriceLevel0;
	}
	public Double getPayoutRateLevel0() {
		return payoutRateLevel0;
	}
	public void setPayoutRateLevel0(Double payoutRateLevel0) {
		this.payoutRateLevel0 = payoutRateLevel0;
	}
	public OrderTicketDetailFinancialModel(String column1, Integer column2,
			String product, Double totalInvestment, Double invest,
			Double totalPrice, Double payoutRate, Double totalInvestmentAllup,
			Double investAllup, Double totalPriceAllup, Double payoutRateAllup,
			Double totalInvestmentFsgl, Double investFsgl,
			Double totalPriceFsgl, Double payoutRateFsgl, Double winpriceFsgl,
			Double averageFsgl, Double totalInvestmentLevel0,
			Double investLevel0, Double totalPriceLevel0,
			Double payoutRateLevel0) {
		super();
		this.column1 = column1;
		this.column2 = column2;
		this.product = product;
		this.totalInvestment = totalInvestment;
		this.invest = invest;
		this.totalPrice = totalPrice;
		this.payoutRate = payoutRate;
		this.totalInvestmentAllup = totalInvestmentAllup;
		this.investAllup = investAllup;
		this.totalPriceAllup = totalPriceAllup;
		this.payoutRateAllup = payoutRateAllup;
		this.totalInvestmentFsgl = totalInvestmentFsgl;
		this.investFsgl = investFsgl;
		this.totalPriceFsgl = totalPriceFsgl;
		this.payoutRateFsgl = payoutRateFsgl;
		this.winpriceFsgl = winpriceFsgl;
		this.averageFsgl = averageFsgl;
		this.totalInvestmentLevel0 = totalInvestmentLevel0;
		this.investLevel0 = investLevel0;
		this.totalPriceLevel0 = totalPriceLevel0;
		this.payoutRateLevel0 = payoutRateLevel0;
	}
	public OrderTicketDetailFinancialModel() {
		super();
	}
	
	
	 
	 
	 
	 
	
}
