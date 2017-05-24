package com.caiex.account.model;

public class OrderTicketDetailHisModel {
	
	private Integer local_m;//   local m: 本地实际关注，即针对降关情况
	private Double total_price;//   total price: 最大中奖额
	private Integer sid;//   、
	private String tid;//   ticket id: 一个完整betline对应的唯一id
	private Double inv_allup;//   allup investment: 总投注量
	private Double price_allup;//   allup price: 此票总投注额
	private Integer status;//   1：中了，2： 没中， 3： Alive
	private Integer alive_m;//   
	private String product;//   
	private String payouttime;//   
	
	
	//非数据库字段
	private String startDate;
	private String endDate;
	private String startNumDate;
	private String endNumDate;
	private String monStartDate;
	private String monEndDate;
	private String tueStartDate;
	private String tueEndDate;
	private String wedStartDate;
	private String wedEndDate;
	private String thuSatrtDate;
	private String thuEndDate;
	private String firStartDate;
	private String firEndDate;
	private String satStartDate;
	private String satEndDate;
	
	private String monStartNumDate;
	private String monEndNumDate;
	private String tueStartNumDate;
	private String tueEndNumDate;
	private String wedStartNumDate;
	private String wedEndNumDate;
	private String thuSatrtNumDate;
	private String thuEndNumDate;
	private String firStartNumDate;
	private String firEndNumDate;
	private String satStartNumDate;
	private String satEndNumDate;
	
	
	
	
	private int matchNum;
	private String weekDate;
	private Double pl;
	private Double plt;
	private String monweekDate;
	private String tueweekDate;
	private String wedweekDate;
	private String thuweekDate;
	private String firweekDate;
	private String satweekDate;

	private Integer totalInvestment;
	
}
