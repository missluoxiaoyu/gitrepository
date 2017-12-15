package com.caiex.fms.fb.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.currentdb.api.OrderTicketErrorApi;
import com.caiex.dbservice.currentdb.entity.OrderTicketError;
import com.caiex.fms.fb.service.OrderTicketErrorService;


@Service
public class OrderTicketErrorServiceImpl implements OrderTicketErrorService{
	
	@Autowired
	private OrderTicketErrorApi orderTicketErrorApi;
	
	//分页查询
	@Override
	public List<OrderTicketError> getTicketErrors(int page,int size,String startDate,String endDate){
		
		return orderTicketErrorApi.getTicketErrors(page, size, startDate, endDate);
	}
	
	//存贮
	@Override
	public void saveTicketError(OrderTicketError newTicket) {
		orderTicketErrorApi.saveTicketError(newTicket);
	}

	//当数据是正常的时候，将error表中此数据删除
	@Override
	public void deleteTicketError(OrderTicketError ticketError) {
		orderTicketErrorApi.deleteTicketError(ticketError);
	}

	//当页面处理后数据库将更新state的状态
	@Override
	public void changeTicketErrorState(OrderTicketError ticketError) {
		orderTicketErrorApi.changeTicketErrorState(ticketError);	
	}

	//根据id查ticket
	@Override
	public OrderTicketError getTicketErrorByid(int id) {
		return orderTicketErrorApi.getTicketErrorByid(id);
	}
	
	//根据起止时间查询error表
	@Override
	public List<OrderTicketError> getTicketErrorByTime(String startTime,String endTime){
		
		return orderTicketErrorApi.getTicketErrorByTime(startTime,endTime);
	}
		
}
