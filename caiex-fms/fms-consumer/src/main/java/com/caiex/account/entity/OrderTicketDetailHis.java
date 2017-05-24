package com.caiex.account.entity;


public class OrderTicketDetailHis  {
	
		private Integer local_m;//   local m: 本地实际关注，即针对降关情况	private Double total_price;//   total price: 最大中奖额	private Integer sid;//   、	private String tid;//   ticket id: 一个完整betline对应的唯一id	private Double inv_allup;//   allup investment: 总投注量	private Double price_allup;//   allup price: 此票总投注额	private Integer status;//   1：中了，2： 没中， 3： Alive	private Integer alive_m;//   	private String product;//   	private String payouttime;//   
	
	
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
	private Double plt;	private String monweekDate;
	private String tueweekDate;
	private String wedweekDate;
	private String thuweekDate;
	private String firweekDate;
	private String satweekDate;

	private Integer totalInvestment;
	
	
	public Integer getTotalInvestment() {
		return totalInvestment;
	}
	public void setTotalInvestment(Integer totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	public Double getPlt() {
		return plt;
	}
	public void setPlt(Double plt) {
		this.plt = plt;
	}
	public String getMonweekDate() {
		return monweekDate;
	}
	public void setMonweekDate(String monweekDate) {
		this.monweekDate = monweekDate;
	}
	public String getTueweekDate() {
		return tueweekDate;
	}
	public void setTueweekDate(String tueweekDate) {
		this.tueweekDate = tueweekDate;
	}
	public String getWedweekDate() {
		return wedweekDate;
	}
	public void setWedweekDate(String wedweekDate) {
		this.wedweekDate = wedweekDate;
	}
	public String getThuweekDate() {
		return thuweekDate;
	}
	public void setThuweekDate(String thuweekDate) {
		this.thuweekDate = thuweekDate;
	}
	public String getFirweekDate() {
		return firweekDate;
	}
	public void setFirweekDate(String firweekDate) {
		this.firweekDate = firweekDate;
	}
	public String getSatweekDate() {
		return satweekDate;
	}
	public void setSatweekDate(String satweekDate) {
		this.satweekDate = satweekDate;
	}

	public Double getPl() {
		return pl;
	}
	public void setPl(Double pl) {
		this.pl = pl;
	}

	public int getMatchNum() {
		return matchNum;
	}
	public void setMatchNum(int matchNum) {
		this.matchNum = matchNum;
	}
	public String getWeekDate() {
		return weekDate;
	}
	public void setWeekDate(String weekDate) {
		this.weekDate = weekDate;
	}
	public String getMonStartNumDate() {
		return monStartNumDate;
	}
	public void setMonStartNumDate(String monStartNumDate) {
		this.monStartNumDate = monStartNumDate;
	}
	public String getMonEndNumDate() {
		return monEndNumDate;
	}
	public void setMonEndNumDate(String monEndNumDate) {
		this.monEndNumDate = monEndNumDate;
	}
	public String getTueStartNumDate() {
		return tueStartNumDate;
	}
	public void setTueStartNumDate(String tueStartNumDate) {
		this.tueStartNumDate = tueStartNumDate;
	}

	public String getTueEndDate() {
		return tueEndDate;
	}
	public void setTueEndDate(String tueEndDate) {
		this.tueEndDate = tueEndDate;
	}
	public String getTueEndNumDate() {
		return tueEndNumDate;
	}
	public void setTueEndNumDate(String tueEndNumDate) {
		this.tueEndNumDate = tueEndNumDate;
	}
	public String getWedStartNumDate() {
		return wedStartNumDate;
	}
	public void setWedStartNumDate(String wedStartNumDate) {
		this.wedStartNumDate = wedStartNumDate;
	}
	public String getWedEndNumDate() {
		return wedEndNumDate;
	}
	public void setWedEndNumDate(String wedEndNumDate) {
		this.wedEndNumDate = wedEndNumDate;
	}
	public String getThuSatrtNumDate() {
		return thuSatrtNumDate;
	}
	public void setThuSatrtNumDate(String thuSatrtNumDate) {
		this.thuSatrtNumDate = thuSatrtNumDate;
	}
	public String getThuEndNumDate() {
		return thuEndNumDate;
	}
	public void setThuEndNumDate(String thuEndNumDate) {
		this.thuEndNumDate = thuEndNumDate;
	}
	public String getFirStartNumDate() {
		return firStartNumDate;
	}
	public void setFirStartNumDate(String firStartNumDate) {
		this.firStartNumDate = firStartNumDate;
	}
	public String getFirEndNumDate() {
		return firEndNumDate;
	}
	public void setFirEndNumDate(String firEndNumDate) {
		this.firEndNumDate = firEndNumDate;
	}
	public String getSatStartNumDate() {
		return satStartNumDate;
	}
	public void setSatStartNumDate(String satStartNumDate) {
		this.satStartNumDate = satStartNumDate;
	}
	public String getSatEndNumDate() {
		return satEndNumDate;
	}
	public void setSatEndNumDate(String satEndNumDate) {
		this.satEndNumDate = satEndNumDate;
	}
	public String getMonStartDate() {
		return monStartDate;
	}
	public void setMonStartDate(String monStartDate) {
		this.monStartDate = monStartDate;
	}
	public String getMonEndDate() {
		return monEndDate;
	}
	public void setMonEndDate(String monEndDate) {
		this.monEndDate = monEndDate;
	}
	public String getTueStartDate() {
		return tueStartDate;
	}
	public void setTueStartDate(String tueStartDate) {
		this.tueStartDate = tueStartDate;
	}

	public String getWedStartDate() {
		return wedStartDate;
	}
	public void setWedStartDate(String wedStartDate) {
		this.wedStartDate = wedStartDate;
	}
	public String getWedEndDate() {
		return wedEndDate;
	}
	public void setWedEndDate(String wedEndDate) {
		this.wedEndDate = wedEndDate;
	}
	public String getThuSatrtDate() {
		return thuSatrtDate;
	}
	public void setThuSatrtDate(String thuSatrtDate) {
		this.thuSatrtDate = thuSatrtDate;
	}
	public String getThuEndDate() {
		return thuEndDate;
	}
	public void setThuEndDate(String thuEndDate) {
		this.thuEndDate = thuEndDate;
	}
	public String getFirStartDate() {
		return firStartDate;
	}
	public void setFirStartDate(String firStartDate) {
		this.firStartDate = firStartDate;
	}
	public String getFirEndDate() {
		return firEndDate;
	}
	public void setFirEndDate(String firEndDate) {
		this.firEndDate = firEndDate;
	}
	public String getSatStartDate() {
		return satStartDate;
	}
	public void setSatStartDate(String satStartDate) {
		this.satStartDate = satStartDate;
	}
	public String getSatEndDate() {
		return satEndDate;
	}
	public void setSatEndDate(String satEndDate) {
		this.satEndDate = satEndDate;
	}
	public String getStartNumDate() {
		return startNumDate;
	}
	public void setStartNumDate(String startNumDate) {
		this.startNumDate = startNumDate;
	}
	public String getEndNumDate() {
		return endNumDate;
	}
	public void setEndNumDate(String endNumDate) {
		this.endNumDate = endNumDate;
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
	public Integer getLocal_m() {	    return this.local_m;	}	public void setLocal_m(Integer local_m) {	    this.local_m=local_m;	}	public Double getTotal_price() {	    return this.total_price;	}	public void setTotal_price(Double total_price) {	    this.total_price=total_price;	}	public Integer getSid() {	    return this.sid;	}	public void setSid(Integer sid) {	    this.sid=sid;	}	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Double getInv_allup() {	    return this.inv_allup;	}	public void setInv_allup(Double inv_allup) {	    this.inv_allup=inv_allup;	}	public Double getPrice_allup() {	    return this.price_allup;	}	public void setPrice_allup(Double price_allup) {	    this.price_allup=price_allup;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public Integer getAlive_m() {	    return this.alive_m;	}	public void setAlive_m(Integer alive_m) {	    this.alive_m=alive_m;	}	public String getProduct() {	    return this.product;	}	public void setProduct(String product) {	    this.product=product;	}
	public String getPayouttime() {
		return payouttime;
	}
	public void setPayouttime(String payouttime) {
		this.payouttime = payouttime;
	}	
}
