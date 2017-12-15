package com.caiex.fms.fb.service;

import java.util.List;

import com.caiex.dbservice.currentdb.entity.OrderTicketError;




public interface OrderTicketErrorService {
	//分页查询
	public List<OrderTicketError> getTicketErrors(int page,int size,String startDate,String endDate);
	
	//存贮
	void saveTicketError(OrderTicketError ticketError);
	//删除
	public void deleteTicketError(OrderTicketError ticketError);
	//处理后的票状态的改变
	public void changeTicketErrorState(OrderTicketError ticketError);
	//根据id查询
	public OrderTicketError getTicketErrorByid(int id);
	//根据起止时间查询error表
	public List<OrderTicketError> getTicketErrorByTime(String startTime,String endTime);
}
