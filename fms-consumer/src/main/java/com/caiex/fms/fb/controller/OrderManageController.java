package com.caiex.fms.fb.controller;

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

import com.caiex.dbservice.currentdb.entity.OrderTicketDetail;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.fms.bk.service.BkOrderManageService;
import com.caiex.fms.fb.service.AllOrderManageService;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("/orderManage")
public class OrderManageController {

	@Autowired
	private AllOrderManageService service;
	
	@Autowired
	private BkOrderManageService bkService;
	
	
	@RequestMapping("/list")
	public String view(){
		return "user";
	}
	
	
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(OrderTicketModel orderTicket,HttpServletRequest request,HttpServletResponse response) throws Exception {		response.setHeader( "Access-Control-Allow-Origin","*");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
		 if(orderTicket.getBallType()==1){
			 map = service.queryAll(orderTicket);
		 }else{
			 map =bkService.queryAll(orderTicket);
		 }
			
		}catch(Exception e){
			e.printStackTrace();
		}
			return map;
	}
	
	
	
	@RequestMapping(value = "/queryDetail")
	@ResponseBody
	public Map<String, Object> queryDetail(OrderTicketModel orderTicket,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		List<OrderTicketDetail>  list=new ArrayList<OrderTicketDetail>();
		try{
			if(orderTicket.getBallType()==1){
				map=service.queryDetail(orderTicket);
			}else{
				map=bkService.queryDetail(orderTicket);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "/userManagementExcel")
	@ResponseBody
	public Response userManagementExcel(OrderTicketModel orderTicket, HttpServletResponse response) throws Exception {
		
		Response res = new Response();
		try{
			if(orderTicket.getBallType()==1){
				res = 	service.userManagementExcel(response,orderTicket);
			}else{
				res = 	bkService.userManagementExcel(response,orderTicket);
			}
			
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
