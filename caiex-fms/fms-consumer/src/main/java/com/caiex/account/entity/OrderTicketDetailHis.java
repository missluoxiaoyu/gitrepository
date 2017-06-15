package com.caiex.account.entity;


public class OrderTicketDetailHis  {
	
		private Integer local_m;//   local m: 本地实际关注，即针对降关情况	private Double total_price;//   total price: 最大中奖额	private Integer sid;//   、	private String tid;//   ticket id: 一个完整betline对应的唯一id	private Double inv_allup;//   allup investment: 总投注量	private Double price_allup;//   allup price: 此票总投注额	private Integer status;//   1：中了，2： 没中， 3： Alive	private Integer alive_m;//   	private String product;//   	private String payouttime;//   
	
	
	//非数据库字段
	private String startDate;
	private String endDate;

	private int matchNum;
	
	private Double pl;
	private Double plt;
	private Integer totalInvestment;
	public Integer getLocal_m() {
		return local_m;
	}
	public void setLocal_m(Integer local_m) {
		this.local_m = local_m;
	}
	public Double getTotal_price() {
		return total_price;
	}
	public void setTotal_price(Double total_price) {
		this.total_price = total_price;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Double getInv_allup() {
		return inv_allup;
	}
	public void setInv_allup(Double inv_allup) {
		this.inv_allup = inv_allup;
	}
	public Double getPrice_allup() {
		return price_allup;
	}
	public void setPrice_allup(Double price_allup) {
		this.price_allup = price_allup;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getAlive_m() {
		return alive_m;
	}
	public void setAlive_m(Integer alive_m) {
		this.alive_m = alive_m;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getPayouttime() {
		return payouttime;
	}
	public void setPayouttime(String payouttime) {
		this.payouttime = payouttime;
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
	public int getMatchNum() {
		return matchNum;
	}
	public void setMatchNum(int matchNum) {
		this.matchNum = matchNum;
	}
	public Double getPl() {
		return pl;
	}
	public void setPl(Double pl) {
		this.pl = pl;
	}
	public Double getPlt() {
		return plt;
	}
	public void setPlt(Double plt) {
		this.plt = plt;
	}
	public Integer getTotalInvestment() {
		return totalInvestment;
	}
	public void setTotalInvestment(Integer totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	
	
	
}
