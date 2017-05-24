package com.caiex.account.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderTicketDetail implements Serializable{
	
	private int localm;
	private Double totalPrice;
	private int sid;
	private int tid;
	private Double invallup;
	private Double priceallup;
	private int status;
	private int alivem;
	private String product;
	private Date payouttime;
	private OrderTicketInfo orderTicketInfo;
	private List<OrderTicketMatches> matches;
	public int getLocalm() {
		return localm;
	}
	public void setLocalm(int localm) {
		this.localm = localm;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public Double getInvallup() {
		return invallup;
	}
	public void setInvallup(Double invallup) {
		this.invallup = invallup;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAlivem() {
		return alivem;
	}
	public void setAlivem(int alivem) {
		this.alivem = alivem;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public Date getPayouttime() {
		return payouttime;
	}
	public void setPayouttime(Date payouttime) {
		this.payouttime = payouttime;
	}
	
	public Double getPriceallup() {
		return priceallup;
	}
	public void setPriceallup(Double priceallup) {
		this.priceallup = priceallup;
	}
	public OrderTicketInfo getOrderTicketInfo() {
		return orderTicketInfo;
	}
	public void setOrderTicketInfo(OrderTicketInfo orderTicketInfo) {
		this.orderTicketInfo = orderTicketInfo;
	}
	public List<OrderTicketMatches> getMatches() {
		return matches;
	}
	public void setMatches(List<OrderTicketMatches> matches) {
		this.matches = matches;
	}
	
	public OrderTicketDetail() {
		super();
	}
	@Override
	public String toString() {
		return "OrderTicketDetail [localm=" + localm + ", totalPrice="
				+ totalPrice + ", sid=" + sid + ", tid=" + tid + ", invallup="
				+ invallup + ", priceallup=" + priceallup + ", status="
				+ status + ", alivem=" + alivem + ", product=" + product
				+ ", payouttime=" + payouttime + "]";
	}
	
	
	
}
