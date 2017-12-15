package com.caiex.fms.fb.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.oltp.api.Response;



public interface OrderTicketService {
	
	//验证很多票
	public void verifyMore(Date startTime, Date endTime,int agentid,int page);
	//根据时间获得
	List<OrderTicketModel> getOrderTicketListWithTkidByTime(Map<String,Object> map);
	
	public Map<String, Object> 	queryAgentTicket(String tkId,int agentid);
	
	public  Map<String,Object> queryAgentTicketByTime(Date startTime, Date endTime,int agentid,int page);
	
	 public List<AgentInfo> queryAllAgentInfo();
	 //导出excel
	 public Response orderExcel(Date startTime,Date endTime,int agentid,int page,HttpServletResponse response);
}
