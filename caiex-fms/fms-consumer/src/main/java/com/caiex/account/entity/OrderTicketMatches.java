package com.caiex.account.entity;

import java.io.Serializable;
import java.util.List;

public class OrderTicketMatches implements Serializable{

	private Double priceallup;
	private int localm;
	private int sid;
	private Double invmatch;
	private String lprod;
	private String lcode;
	private String lteam;
	private String lopt;
	private Double lodds;
	private Double localodds;
	private int id;
	private int weekno;
	private String statistics;
	private OrderTicketDetail orderTicketDetail;
	public Double getPriceallup() {
		return priceallup;
	}
	public void setPriceallup(Double priceallup) {
		this.priceallup = priceallup;
	}
	public int getLocalm() {
		return localm;
	}
	public void setLocalm(int localm) {
		this.localm = localm;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public Double getInvmatch() {
		return invmatch;
	}
	public void setInvmatch(Double invmatch) {
		this.invmatch = invmatch;
	}
	public String getLprod() {
		return lprod;
	}
	public void setLprod(String lprod) {
		this.lprod = lprod;
	}
	public String getLcode() {
		return lcode;
	}
	public void setLcode(String lcode) {
		this.lcode = lcode;
	}
	public String getLteam() {
		return lteam;
	}
	public void setLteam(String lteam) {
		this.lteam = lteam;
	}
	public String getLopt() {
		return lopt;
	}
	public void setLopt(String lopt) {
		this.lopt = lopt;
	}
	public Double getLodds() {
		return lodds;
	}
	public void setLodds(Double lodds) {
		this.lodds = lodds;
	}
	public Double getLocalodds() {
		return localodds;
	}
	public void setLocalodds(Double localodds) {
		this.localodds = localodds;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWeekno() {
		return weekno;
	}
	public void setWeekno(int weekno) {
		this.weekno = weekno;
	}
	public String getStatistics() {
		return statistics;
	}
	public void setStatistics(String statistics) {
		this.statistics = statistics;
	}

	public OrderTicketDetail getOrderTicketDetail() {
		return orderTicketDetail;
	}
	public void setOrderTicketDetail(OrderTicketDetail orderTicketDetail) {
		this.orderTicketDetail = orderTicketDetail;
	}
	
	
	
}
