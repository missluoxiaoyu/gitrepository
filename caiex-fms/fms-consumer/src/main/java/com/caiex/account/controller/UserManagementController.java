/*package com.caiex.account.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.model.OrderTicketDetailUMModel;
import com.caiex.account.model.OrderTicketModel;
import com.caiex.account.service.UserManagementService;
import com.caiex.account.utils.Response;


@Controller
@RequestMapping("/userManagement")
public class UserManagementController {

	@Autowired
	private UserManagementService service;
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(OrderTicketModel orderTicket,HttpServletRequest request) throws Exception {
		 Map<String, Object> map = new HashMap<String, Object>();
		try{
			map = service.queryAll(orderTicket);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
			return map;
	}
	
	
	
	@RequestMapping(value = "/queryDetail")
	@ResponseBody
	public List<OrderTicketDetailUMModel> queryDetail(OrderTicketModel orderTicket,HttpServletRequest request) throws Exception {
		List<OrderTicketDetailUMModel>  list=new ArrayList<OrderTicketDetailUMModel>();
		try{
			list=service.queryDetail(orderTicket);
		}catch(Exception e){
			//log.error(e.getMessage());
			e.printStackTrace();
		}
				return list;
	}
	
	@RequestMapping(value = "/userManagementExcel")
	@ResponseBody
	public Response userManagementExcel(OrderTicketModel orderTicket, HttpServletResponse response) throws Exception {
		Response res = new Response();
		try{
		res = 	service.userManagementExcel(response,orderTicket);
		}catch(Exception e){
			//log.error(e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	@RequestMapping(value = "/updateNotWinning")
	@ResponseBody
	public Response updateNotWinning(String order_id,String terminalNo,String agentName,Integer inplay,
			String startDate,String endDate,Integer state,Integer recycleState,Integer trade_type,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		OrderTicketModel orderTicket=new OrderTicketModel();
		orderTicket.setTkId(order_id);
		orderTicket.setUid(terminalNo);
		orderTicket.setAgentName(agentName);
		orderTicket.setInplay(inplay);
		orderTicket.setTrade_type(trade_type);
		orderTicket.setStartDate(startDate);
		orderTicket.setEndDate(endDate);
		orderTicket.setState(state);
		orderTicket.setRecycleState(recycleState);
		Response rs=new Response();
		try{
			rs=userManagementService.updateNotWinning(request,response,orderTicket);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	
	@RequestMapping(value = "/updateRecycleMoney")
	@ResponseBody
	public Response updateRecycleMoney(OrderTicketModel orderTicket,HttpServletRequest request){
		Response rs=new Response();
		try{
			 rs  = userManagementService.updateRecycleMoney(orderTicket);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	
	@RequestMapping(value = "/deleteAlive")
	@ResponseBody
	public Response deleteAlive(OrderTicketModel orderTicket,HttpServletRequest request){
		Response rs=new Response();
		try{
			 rs  = userManagementService.deleteAlive(orderTicket);			
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	@RequestMapping(value = "/updateCancel")
	@ResponseBody
	public Response updateCancel(OrderTicketModel orderTicket,HttpServletRequest request){
		Response rs=new Response();
		try{
			 rs  = userManagementService.updateCancel(orderTicket);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	
	@RequestMapping(value = "/queryAllAgent")
	@ResponseBody
	public List<AgentInfo> queryAllAgent() throws Exception {
		return service.queryAllAgentInfo();
		
	}
}
*/