package com.caiex.account.model;

import java.sql.Timestamp;
/*
 * daily single 页面json
 * */

public class OrderTicketDetailSGLModel {

//单关赛事明细
    
    public String match_code;
    private Timestamp match_date;
	private String match_league;//   match league: 联赛代码
	private String home_team;//    home team: 主队名称
	private String away_team;//    away team: 客队名称
	private String home_score_half;//    home team score at half: 主队上半场分数
	private String away_score_half;//    away team score at half: 客队上半场分数
	private String home_score;//   home score: 主队全场最终分数
	private String away_score;//    away score: 客队全场最终分数
    
	 private double totalInvestmentHAD ;
	 private double investHAD;
	 private double totalPriceHAD;
	 private double payoutRateHAD;
	 private double winpriceHAD;
	 private double averageHAD;
	

	 private double totalInvestmentHHAD ;
	 private double investHHAD;
	 private double totalPriceHHAD;
	 private double payoutRateHHAD;
	 private double winpriceHHAD;
	 private double averageHHAD;

	 private double totalInvestmentHAFU ;
	 private double investHAFU;
	 private double totalPriceHAFU;
	 private double payoutRateHAFU;
	 private double winpriceHAFU;
	 private double averageHAFU;
	 
	 private double totalInvestmentTTG ;
	 private double investTTG;
	 private double totalPriceTTG;
	 private double payoutRateTTG;
	 private double winpriceTTG;
	 private double averageTTG;

	 private double totalInvestmentCRS ;
	 private double investCRS;
	 private double totalPriceCRS;
	 private double payoutRateCRS;
	 private double winpriceCRS;
	 private double averageCRS;
	 
	 
	public String getMatch_code() {
		return match_code;
	}
	public void setMatch_code(String match_code) {
		this.match_code = match_code;
	}
	public Timestamp getMatch_date() {
		return match_date;
	}
	public void setMatch_date(Timestamp match_date) {
		this.match_date = match_date;
	}
	public String getMatch_league() {
		return match_league;
	}
	public void setMatch_league(String match_league) {
		this.match_league = match_league;
	}
	public String getHome_team() {
		return home_team;
	}
	public void setHome_team(String home_team) {
		this.home_team = home_team;
	}
	public String getAway_team() {
		return away_team;
	}
	public void setAway_team(String away_team) {
		this.away_team = away_team;
	}
	public String getHome_score_half() {
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
	public double getTotalInvestmentHAD() {
		return totalInvestmentHAD;
	}
	public void setTotalInvestmentHAD(double totalInvestmentHAD) {
		this.totalInvestmentHAD = totalInvestmentHAD;
	}
	public double getInvestHAD() {
		return investHAD;
	}
	public void setInvestHAD(double investHAD) {
		this.investHAD = investHAD;
	}
	public double getTotalPriceHAD() {
		return totalPriceHAD;
	}
	public void setTotalPriceHAD(double totalPriceHAD) {
		this.totalPriceHAD = totalPriceHAD;
	}
	public double getPayoutRateHAD() {
		return payoutRateHAD;
	}
	public void setPayoutRateHAD(double payoutRateHAD) {
		this.payoutRateHAD = payoutRateHAD;
	}
	public double getWinpriceHAD() {
		return winpriceHAD;
	}
	public void setWinpriceHAD(double winpriceHAD) {
		this.winpriceHAD = winpriceHAD;
	}
	public double getAverageHAD() {
		return averageHAD;
	}
	public void setAverageHAD(double averageHAD) {
		this.averageHAD = averageHAD;
	}
	public double getTotalInvestmentHHAD() {
		return totalInvestmentHHAD;
	}
	public void setTotalInvestmentHHAD(double totalInvestmentHHAD) {
		this.totalInvestmentHHAD = totalInvestmentHHAD;
	}
	public double getInvestHHAD() {
		return investHHAD;
	}
	public void setInvestHHAD(double investHHAD) {
		this.investHHAD = investHHAD;
	}
	public double getTotalPriceHHAD() {
		return totalPriceHHAD;
	}
	public void setTotalPriceHHAD(double totalPriceHHAD) {
		this.totalPriceHHAD = totalPriceHHAD;
	}
	public double getPayoutRateHHAD() {
		return payoutRateHHAD;
	}
	public void setPayoutRateHHAD(double payoutRateHHAD) {
		this.payoutRateHHAD = payoutRateHHAD;
	}
	public double getWinpriceHHAD() {
		return winpriceHHAD;
	}
	public void setWinpriceHHAD(double winpriceHHAD) {
		this.winpriceHHAD = winpriceHHAD;
	}
	public double getAverageHHAD() {
		return averageHHAD;
	}
	public void setAverageHHAD(double averageHHAD) {
		this.averageHHAD = averageHHAD;
	}
	public double getTotalInvestmentHAFU() {
		return totalInvestmentHAFU;
	}
	public void setTotalInvestmentHAFU(double totalInvestmentHAFU) {
		this.totalInvestmentHAFU = totalInvestmentHAFU;
	}
	public double getInvestHAFU() {
		return investHAFU;
	}
	public void setInvestHAFU(double investHAFU) {
		this.investHAFU = investHAFU;
	}
	public double getTotalPriceHAFU() {
		return totalPriceHAFU;
	}
	public void setTotalPriceHAFU(double totalPriceHAFU) {
		this.totalPriceHAFU = totalPriceHAFU;
	}
	public double getPayoutRateHAFU() {
		return payoutRateHAFU;
	}
	public void setPayoutRateHAFU(double payoutRateHAFU) {
		this.payoutRateHAFU = payoutRateHAFU;
	}
	public double getWinpriceHAFU() {
		return winpriceHAFU;
	}
	public void setWinpriceHAFU(double winpriceHAFU) {
		this.winpriceHAFU = winpriceHAFU;
	}
	public double getAverageHAFU() {
		return averageHAFU;
	}
	public void setAverageHAFU(double averageHAFU) {
		this.averageHAFU = averageHAFU;
	}
	public double getTotalInvestmentTTG() {
		return totalInvestmentTTG;
	}
	public void setTotalInvestmentTTG(double totalInvestmentTTG) {
		this.totalInvestmentTTG = totalInvestmentTTG;
	}
	public double getInvestTTG() {
		return investTTG;
	}
	public void setInvestTTG(double investTTG) {
		this.investTTG = investTTG;
	}
	public double getTotalPriceTTG() {
		return totalPriceTTG;
	}
	public void setTotalPriceTTG(double totalPriceTTG) {
		this.totalPriceTTG = totalPriceTTG;
	}
	public double getPayoutRateTTG() {
		return payoutRateTTG;
	}
	public void setPayoutRateTTG(double payoutRateTTG) {
		this.payoutRateTTG = payoutRateTTG;
	}
	public double getWinpriceTTG() {
		return winpriceTTG;
	}
	public void setWinpriceTTG(double winpriceTTG) {
		this.winpriceTTG = winpriceTTG;
	}
	public double getAverageTTG() {
		return averageTTG;
	}
	public void setAverageTTG(double averageTTG) {
		this.averageTTG = averageTTG;
	}
	public double getTotalInvestmentCRS() {
		return totalInvestmentCRS;
	}
	public void setTotalInvestmentCRS(double totalInvestmentCRS) {
		this.totalInvestmentCRS = totalInvestmentCRS;
	}
	public double getInvestCRS() {
		return investCRS;
	}
	public void setInvestCRS(double investCRS) {
		this.investCRS = investCRS;
	}
	public double getTotalPriceCRS() {
		return totalPriceCRS;
	}
	public void setTotalPriceCRS(double totalPriceCRS) {
		this.totalPriceCRS = totalPriceCRS;
	}
	public double getPayoutRateCRS() {
		return payoutRateCRS;
	}
	public void setPayoutRateCRS(double payoutRateCRS) {
		this.payoutRateCRS = payoutRateCRS;
	}
	public double getWinpriceCRS() {
		return winpriceCRS;
	}
	public void setWinpriceCRS(double winpriceCRS) {
		this.winpriceCRS = winpriceCRS;
	}
	public double getAverageCRS() {
		return averageCRS;
	}
	public void setAverageCRS(double averageCRS) {
		this.averageCRS = averageCRS;
	}
	public OrderTicketDetailSGLModel(String match_code, Timestamp match_date,
			String match_league, String home_team, String away_team,
			String home_score_half, String away_score_half, String home_score,
			String away_score, double totalInvestmentHAD, double investHAD,
			double totalPriceHAD, double payoutRateHAD, double winpriceHAD,
			double averageHAD, double totalInvestmentHHAD, double investHHAD,
			double totalPriceHHAD, double payoutRateHHAD, double winpriceHHAD,
			double averageHHAD, double totalInvestmentHAFU, double investHAFU,
			double totalPriceHAFU, double payoutRateHAFU, double winpriceHAFU,
			double averageHAFU, double totalInvestmentTTG, double investTTG,
			double totalPriceTTG, double payoutRateTTG, double winpriceTTG,
			double averageTTG, double totalInvestmentCRS, double investCRS,
			double totalPriceCRS, double payoutRateCRS, double winpriceCRS,
			double averageCRS) {
		super();
		this.match_code = match_code;
		this.match_date = match_date;
		this.match_league = match_league;
		this.home_team = home_team;
		this.away_team = away_team;
		this.home_score_half = home_score_half;
		this.away_score_half = away_score_half;
		this.home_score = home_score;
		this.away_score = away_score;
		this.totalInvestmentHAD = totalInvestmentHAD;
		this.investHAD = investHAD;
		this.totalPriceHAD = totalPriceHAD;
		this.payoutRateHAD = payoutRateHAD;
		this.winpriceHAD = winpriceHAD;
		this.averageHAD = averageHAD;
		this.totalInvestmentHHAD = totalInvestmentHHAD;
		this.investHHAD = investHHAD;
		this.totalPriceHHAD = totalPriceHHAD;
		this.payoutRateHHAD = payoutRateHHAD;
		this.winpriceHHAD = winpriceHHAD;
		this.averageHHAD = averageHHAD;
		this.totalInvestmentHAFU = totalInvestmentHAFU;
		this.investHAFU = investHAFU;
		this.totalPriceHAFU = totalPriceHAFU;
		this.payoutRateHAFU = payoutRateHAFU;
		this.winpriceHAFU = winpriceHAFU;
		this.averageHAFU = averageHAFU;
		this.totalInvestmentTTG = totalInvestmentTTG;
		this.investTTG = investTTG;
		this.totalPriceTTG = totalPriceTTG;
		this.payoutRateTTG = payoutRateTTG;
		this.winpriceTTG = winpriceTTG;
		this.averageTTG = averageTTG;
		this.totalInvestmentCRS = totalInvestmentCRS;
		this.investCRS = investCRS;
		this.totalPriceCRS = totalPriceCRS;
		this.payoutRateCRS = payoutRateCRS;
		this.winpriceCRS = winpriceCRS;
		this.averageCRS = averageCRS;
	}
	public OrderTicketDetailSGLModel() {
		super();
	}
	
	 
	 
}
