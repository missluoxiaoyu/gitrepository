package com.caiex.fms.model;


public class MatchInfoModel {
	
	
	private Integer auto_stop;//   
	private Integer hold;//  
	private String state_name;
	//非数据库字段   
	private String rq_type;
	
	private String code;
	
	private String cid;
	
	private String league_name_eng;
	
	private String autoStop;
	
	private String weekid;// 周号 
	
	private String uniCode;// uniCode
	
	public MatchInfoModel() {
		
	}
	
	public MatchInfoModel(Integer mid) {
		this.mid = mid;
	}
	
	public String getRq_type() {
		return rq_type;
	}
	
	public void setRq_type(String rq_type) {
		this.rq_type = rq_type;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCid() {
		return cid;
	}
	
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public String getLeague_name_eng() {
		return league_name_eng;
	}
	
	public void setLeague_name_eng(String league_name_eng) {
		this.league_name_eng = league_name_eng;
	}
	
	public String getAutoStop() {
		return autoStop;
	}
	
	public void setAutoStop(String autoStop) {
		this.autoStop = autoStop;
	}
	
	public String getWeekid() {
		return weekid;
	}
	
	public void setWeekid(String weekid) {
		this.weekid = weekid;
	}
	
	public String getUniCode() {
		return uniCode;
	}
	
	public void setUniCode(String uniCode) {
		this.uniCode = uniCode;
	}
	
	public String getState_name() {
		return state_name;
	}
	
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	
	public Integer getMid() {
	
	
	
	
		return "";
	}
	
	public void setWeek_id(String week_id) {
		this.week_id = week_id;
	}
	
	public String getMatch_code() {
	
		return st_id;
	}
	
	public void setSt_id(String st_id) {
		this.st_id = st_id;
	}
	
	public String getMatch_league() {
	
	
	
	
	
		return home_score_half;
	}
	
	public void setHome_score_half(String home_score_half) {
		this.home_score_half = home_score_half;
	}
	
	public String getAway_score_half() {
		return away_score_half;
	}
	
	public void setAway_score_half(String away_score_half) {
		this.away_score_half = away_score_half;
	}
	
	public String getHome_score() {
		return home_score;
	}
	
	public void setHome_score(String home_score) {
		this.home_score = home_score;
	}
	
	public String getAway_score() {
		return away_score;
	}

	public void setAway_score(String away_score) {
		this.away_score = away_score;
	}
	
	public Integer getState() {
	
	
	
	
	public Integer getAuto_stop() {
		return auto_stop;
	}
	
	public void setAuto_stop(Integer auto_stop) {
		this.auto_stop = auto_stop;
	}
	
	public Integer getHold() {
		return hold;
	}
	
	public void setHold(Integer hold) {
		this.hold = hold;
	}

}