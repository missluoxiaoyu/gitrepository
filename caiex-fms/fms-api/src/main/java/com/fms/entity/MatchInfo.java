package com.fms.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.xml.crypto.Data;


public class MatchInfo {
	
	
	
	
	//非数据库字段
	private List<OrderTicketMatches> sids;
	//玩法
	private String product;
	
	
	
	
	
	public List<OrderTicketMatches> getSids() {
		return sids;
	}
	public void setSids(List<OrderTicketMatches> sids) {
		this.sids = sids;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	
	
	public Integer getMid() {
		return match_code;
	}
	public void setMatch_code(String match_code) {
		this.match_code = match_code;
	}
	public Integer getSt_id() {
	
}