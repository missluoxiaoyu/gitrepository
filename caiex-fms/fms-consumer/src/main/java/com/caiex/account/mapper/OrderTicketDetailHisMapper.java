package com.caiex.account.mapper;

import java.util.List;
import java.util.Map;

import com.caiex.account.entity.MatchInfoHis;
import com.caiex.account.entity.OrderTicketDetailHis;




/**
 
 *
 */
public interface OrderTicketDetailHisMapper{
	//daily all up
	public List<OrderTicketDetailHis> queryAllup(OrderTicketDetailHis his);
	public OrderTicketDetailHis  detailHisQueryTotalInvest(OrderTicketDetailHis his);
	public OrderTicketDetailHis  detailHisQueryTotalLib(OrderTicketDetailHis his);
	public List<OrderTicketDetailHis> detailHisQueryPay(OrderTicketDetailHis his);
	public OrderTicketDetailHis queryTotalInvests(OrderTicketDetailHis orderModelHHAD);
	
	//daily single
	public List<OrderTicketDetailHis> queryDetailHisByPayouttime(Map<String, Object> date);
	public OrderTicketDetailHis detailHisQueryInvest(MatchInfoHis infoModel);
	public OrderTicketDetailHis detailHisQueryTotalPrice(MatchInfoHis infoModel);
	public OrderTicketDetailHis detailHisQueryWinInvest(MatchInfoHis infoModel);
	public OrderTicketDetailHis detailHisQueryTotalInvestment(MatchInfoHis infoModel);
	
	//summary
	public Integer queryMatchNum(Map<String, Object> params);
	
}
