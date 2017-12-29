package com.caiex.fms.bookie.controller;

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

import com.caiex.dbservice.caiexbooker.entity.VirBetOrderTicketDetail;
import com.caiex.dbservice.model.BookieOrderModel;
import com.caiex.fms.bookie.service.BookieOrderService;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("bookieOrder")
public class BookieOrderController {

	
	
	@Autowired
	private BookieOrderService service;
	
	@RequestMapping("/list")
	public String view(){
		return "bookieOrder";
	}
	
	
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(BookieOrderModel orderTicket,HttpServletRequest request,HttpServletResponse response) throws Exception {		response.setHeader( "Access-Control-Allow-Origin","*");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			 map = service.queryAll(orderTicket);

		}catch(Exception e){
			e.printStackTrace();
		}
			return map;
	}
	
	
	
	@RequestMapping(value = "/queryDetail")
	@ResponseBody
	public Map<String, Object> queryDetail(BookieOrderModel orderTicket,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		List<VirBetOrderTicketDetail>  list=new ArrayList<VirBetOrderTicketDetail>();
		try{
				map=service.queryDetail(orderTicket);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "/userManagementExcel")
	@ResponseBody
	public Response userManagementExcel(BookieOrderModel orderTicket, HttpServletResponse response) throws Exception {
		
		Response res = new Response();
		try{
		res = 	service.userManagementExcel(response,orderTicket);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	@RequestMapping(value = "/queryAllAgent")
	@ResponseBody
	public Map<String, Object>  queryAllAgent(HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("agentList", service.queryAllAgentInfo());
		return map;
		
	}
	
}
