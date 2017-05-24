package com.caiex.account.mapper;

import java.util.List;

import com.caiex.account.entity.OrderTicketDetailHis;



/**
 * OrderTicketDetailHis Mapper
 * @author Administrator
 *
 */
public interface OrderTicketDetailHisMapper{
	


	public List<OrderTicketDetailHis> queryAllup(OrderTicketDetailHis his);
	public OrderTicketDetailHis  detailHisQueryTotalInvest(OrderTicketDetailHis his);
	public OrderTicketDetailHis  detailHisQueryTotalLib(OrderTicketDetailHis his);
	public List<OrderTicketDetailHis> detailHisQueryPay(OrderTicketDetailHis his);
	public OrderTicketDetailHis queryTotalInvests(
			OrderTicketDetailHis orderModelHHAD);
}
