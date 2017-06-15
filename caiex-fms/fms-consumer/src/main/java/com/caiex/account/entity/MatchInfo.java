package com.caiex.account.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.xml.crypto.Data;


public class MatchInfo {
	
		private Integer mid;//    match id：比赛id	private java.sql.Timestamp match_date;//   match date: 比赛开始时间	private String match_code;//   match code: 官方赛事编号	private Integer st_id;//    sport type id: 赛事	private String match_league;//   match league: 联赛代码	private String home_team;//    home team: 主队名称	private String away_team;//    away team: 客队名称	private String home_score_half;//    home team score at half: 主队上半场分数	private String away_score_half;//    away team score at half: 客队上半场分数	private String home_score;//   home score: 主队全场最终分数	private String away_score;//    away score: 客队全场最终分数	private Integer state;//   selling state: 赛事销售状态 (需要重命名)	private java.sql.Timestamp create_time;//   create time: 赛事创建时间，即奖池初始化时间	private Integer auto_stop;//   auto stop: 是否自动停售：[0, 1]	private Integer hold;//   hold: 赛事是否是hold状态: [0, 1]	private String week_id;//   week id: 周id
	
	
	//非数据库字段
	private List<OrderTicketMatchesHis> sids;
	//玩法
	private String product;
	
	
	
	
	
	public List<OrderTicketMatchesHis> getSids() {
		return sids;
	}
	public void setSids(List<OrderTicketMatchesHis> sids) {
		this.sids = sids;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	
	
	public Integer getMid() {	    return this.mid;	}	public void setMid(Integer mid) {	    this.mid=mid;	}	public java.sql.Timestamp getMatch_date() {	    return this.match_date;	}	public void setMatch_date(java.sql.Timestamp match_date) {	    this.match_date=match_date;	}	public String getMatch_code() {
		return match_code;
	}
	public void setMatch_code(String match_code) {
		this.match_code = match_code;
	}
	public Integer getSt_id() {	    return this.st_id;	}	public void setSt_id(Integer st_id) {	    this.st_id=st_id;	}	public String getMatch_league() {	    return this.match_league;	}	public void setMatch_league(String match_league) {	    this.match_league=match_league;	}	public String getHome_team() {	    return this.home_team;	}	public void setHome_team(String home_team) {	    this.home_team=home_team;	}	public String getAway_team() {	    return this.away_team;	}	public void setAway_team(String away_team) {	    this.away_team=away_team;	}	public String getHome_score_half() {	    return this.home_score_half;	}	public void setHome_score_half(String home_score_half) {	    this.home_score_half=home_score_half;	}	public String getAway_score_half() {	    return this.away_score_half;	}	public void setAway_score_half(String away_score_half) {	    this.away_score_half=away_score_half;	}	public String getHome_score() {	    return this.home_score;	}	public void setHome_score(String home_score) {	    this.home_score=home_score;	}	public String getAway_score() {	    return this.away_score;	}	public void setAway_score(String away_score) {	    this.away_score=away_score;	}	public Integer getState() {	    return this.state;	}	public void setState(Integer state) {	    this.state=state;	}	public java.sql.Timestamp getCreate_time() {	    return this.create_time;	}	public void setCreate_time(java.sql.Timestamp create_time) {	    this.create_time=create_time;	}	public Integer getAuto_stop() {	    return this.auto_stop;	}	public void setAuto_stop(Integer auto_stop) {	    this.auto_stop=auto_stop;	}	public Integer getHold() {	    return this.hold;	}	public void setHold(Integer hold) {	    this.hold=hold;	}	public String getWeek_id() {	    return this.week_id;	}	public void setWeek_id(String week_id) {	    this.week_id=week_id;	}
	
}
