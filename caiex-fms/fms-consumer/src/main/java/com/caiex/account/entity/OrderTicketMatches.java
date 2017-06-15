package com.caiex.account.entity;

import java.io.Serializable;
import java.util.List;

public class OrderTicketMatches implements Serializable{
	private Integer id;
	private Double price_allup;
	private Integer local_m;
	private Integer sid;
	private Double inv_match;
	private String l_prod;
	private String l_code;
	private String l_team;
	private String l_opt;
	private Double l_odds;
	private Double local_odds;
	private Integer weekno;
	private String statistics;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getPrice_allup() {
		return price_allup;
	}
	public void setPrice_allup(Double price_allup) {
		this.price_allup = price_allup;
	}
	public Integer getLocal_m() {
		return local_m;
	}
	public void setLocal_m(Integer local_m) {
		this.local_m = local_m;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Double getInv_match() {
		return inv_match;
	}
	public void setInv_match(Double inv_match) {
		this.inv_match = inv_match;
	}
	public String getL_prod() {
		return l_prod;
	}
	public void setL_prod(String l_prod) {
		this.l_prod = l_prod;
	}
	public String getL_code() {
		return l_code;
	}
	public void setL_code(String l_code) {
		this.l_code = l_code;
	}
	public String getL_team() {
		return l_team;
	}
	public void setL_team(String l_team) {
		this.l_team = l_team;
	}
	public String getL_opt() {
		return l_opt;
	}
	public void setL_opt(String l_opt) {
		this.l_opt = l_opt;
	}
	public Double getL_odds() {
		return l_odds;
	}
	public void setL_odds(Double l_odds) {
		this.l_odds = l_odds;
	}
	public Double getLocal_odds() {
		return local_odds;
	}
	public void setLocal_odds(Double local_odds) {
		this.local_odds = local_odds;
	}
	public Integer getWeekno() {
		return weekno;
	}
	public void setWeekno(Integer weekno) {
		this.weekno = weekno;
	}
	public String getStatistics() {
		return statistics;
	}
	public void setStatistics(String statistics) {
		this.statistics = statistics;
	}
	
	
}
