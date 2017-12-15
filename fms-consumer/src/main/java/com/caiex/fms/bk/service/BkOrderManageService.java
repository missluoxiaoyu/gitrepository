package com.caiex.fms.bk.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.dbservice.basketball.entity.BasketBallAgentInfo;
import com.caiex.dbservice.basketball.entity.BasketBallOrderTicketDetail;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.oltp.api.Response;


public interface BkOrderManageService {
	
	public List<BasketBallAgentInfo> queryAllAgentInfo();
	 public Map<String, Object>  queryAll(OrderTicketModel orderTicket) throws Exception;
	 public Map<String, Object> queryDetail(OrderTicketModel orderTicket);
	 public Response userManagementExcel( HttpServletResponse response, OrderTicketModel orderTicket) throws Exception;
	
}
