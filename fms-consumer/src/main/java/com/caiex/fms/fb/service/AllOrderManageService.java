package com.caiex.fms.fb.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.currentdb.entity.OrderTicketDetail;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.oltp.api.Response;


public interface AllOrderManageService {
	
	public List<AgentInfo> queryAllAgentInfo();
	 public Map<String, Object>  queryAll(OrderTicketModel orderTicket) throws Exception;
	 public Map<String, Object> queryDetail(OrderTicketModel orderTicket);
	 public Response userManagementExcel( HttpServletResponse response, OrderTicketModel orderTicket) throws Exception;
	
}
