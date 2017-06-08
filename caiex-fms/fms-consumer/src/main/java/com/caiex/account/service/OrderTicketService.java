package com.caiex.account.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.model.OrderTicketModel;


public interface OrderTicketService {
	//验证的单个票
	public void verifyOne(String tid);
	//验证很多票
	public void verifyMore(Date startTime, Date endTime,int agentid,int page);
	//根据时间获得
	List<OrderTicketModel> getOrderTicketListWithTkidByTime(Map<String,Object> map);
	
	public Map<String, Object> 	queryAgentTicket(String tkId);
	
	public  Map<String,Object> queryAgentTicketByTime(Date startTime, Date endTime,int agentid,int page);
	
	 public List<AgentInfo> queryAllAgentInfo();
}
