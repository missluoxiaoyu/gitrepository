package com.caiex.account.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;


public class OrderTicketInfo implements Serializable{
	
	
	private int inplay;
	private String tkId;
	private Timestamp sellingTime;
	private int unitInvestment;
	private int totalInvestment;
	private int agentId;
	private BigInteger uid;
	private Double bonus;
	private String bettingProducts;
	private int localm;
	private Double tradePrice;
	private Double totalPrice;
	private String tid;
	private int ballType;
	private BigInteger id;
	public int getInplay() {
		return inplay;
	}
	public void setInplay(int inplay) {
		this.inplay = inplay;
	}
	public String getTkId() {
		return tkId;
	}
	public void setTkId(String tkId) {
		this.tkId = tkId;
	}
	public Timestamp getSellingTime() {
		return sellingTime;
	}
	public void setSellingTime(Timestamp sellingTime) {
		this.sellingTime = sellingTime;
	}
	public int getUnitInvestment() {
		return unitInvestment;
	}
	public void setUnitInvestment(int unitInvestment) {
		this.unitInvestment = unitInvestment;
	}
	public int getTotalInvestment() {
		return totalInvestment;
	}
	public void setTotalInvestment(int totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	public BigInteger getUid() {
		return uid;
	}
	public void setUid(BigInteger uid) {
		this.uid = uid;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public String getBettingProducts() {
		return bettingProducts;
	}
	public void setBettingProducts(String bettingProducts) {
		this.bettingProducts = bettingProducts;
	}
	public int getLocalm() {
		return localm;
	}
	public void setLocalm(int localm) {
		this.localm = localm;
	}
	public Double getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(Double tradePrice) {
		this.tradePrice = tradePrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public int getBallType() {
		return ballType;
	}
	public void setBallType(int ballType) {
		this.ballType = ballType;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	
}
