package com.caiex.account.mapper;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderTicket;

import com.caiex.account.model.OrderTicketModel;


@MapperScan
public interface OrderTicketMapper {
	
	
	//对账
	List<OrderTicketModel> getOrderTicketListWithTkidByTime(Map<String,Object> map);
	
	OrderTicketModel getOrderTicketByTkid(String tkId);
	//
	int getTicketCount(Map<String, Object> dateParams);
	//根据起止时间 查询agentid的集合
	List<Integer> queryAgentid(Map<String, Object> date);
	 
	
	 //渠道 
	List<OrderTicket> queryTicketInfo_id(Map<String, Object> paramsMap);
	//渠道 
	OrderTicket  queryBatchPrice(Map<String, Object> paramsMap);
	
	OrderTicket queryRecycleState(List<OrderTicket> orderTickets);
	//渠道
	public int getOrderAmount(Map<String, Object> paramsMap);
	
}
