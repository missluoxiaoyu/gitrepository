package com.caiex.account.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.model.OrderTicketDetailUMModel;
import com.caiex.account.model.OrderTicketModel;
import com.caiex.account.utils.Response;

public interface UserManagementService {
	 public List<AgentInfo> queryAllAgentInfo();
	 public Map<String, Object>  queryAll(OrderTicketModel orderTicket) throws Exception;
	 public List<OrderTicketDetailUMModel> queryDetail(OrderTicketModel orderTicket);
	 public Response userManagementExcel( HttpServletResponse response, OrderTicketModel orderTicket) throws Exception;
}
