package com.caiex.fms.fb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.dbservice.currentdb.api.AgentInfoProApi;
import com.caiex.dbservice.currentdb.api.OrderTicketErrorApi;
import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.currentdb.entity.OrderTicketError;
import com.caiex.fms.fb.service.NotifyResultService;
import com.caiex.fms.fb.service.OrderTicketErrorService;
import com.caiex.fms.fb.service.OrderTicketService;
import com.caiex.fms.utils.MessageResult;
import com.caiex.oltp.api.Response;
import com.caiex.oltp.api.dto.CancelOrder;
import com.caiex.oltp.api.dto.RecycleEntity;
import com.caiex.oltp.api.dto.RefusedTicket;
import com.caiex.oltp.api.ticket.TicketApi;

@Controller
@RequestMapping("/errorTicket")
public class OrderTicketErrorController {
	private final static Logger log = Logger.getLogger(OrderTicketErrorController.class);
	
	@Autowired
	private OrderTicketErrorService orderTicketErrorService;
	
	@Autowired
	private OrderTicketService orderTicketService;
	
	@Autowired
	private NotifyResultService notifyResultService;
	
	@Autowired
	private AgentInfoProApi agentInfoApi;
	
	@Autowired
	private OrderTicketErrorApi orderTicketErrorApi;
	
	@Autowired
	private TicketApi ticketApi;
	
	
	@RequestMapping(value = "/list")
	public String view(HttpServletResponse response) {
		response.setHeader( "Access-Control-Allow-Origin","*");
		return "errorTicket";
	}
	
	
	
	//查询
	@RequestMapping(value="/getErrorTickets")
	@ResponseBody
	public Map<String, Object> getTicketErrors(@RequestParam (defaultValue="1")int page,@RequestParam (defaultValue="10")int size, 
												String startDate,String endDate,HttpServletResponse response) throws Exception{
		
		Map<String, Object> map =new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date str = sdf.parse(startDate);
		Date end = sdf.parse(endDate);
		map.put("ticketResult", orderTicketErrorService.getTicketErrors(page,size,sdf.format(str),sdf.format(end)));
		return map;
	}
	
	//拒票
	@RequestMapping(value="/refuseTicket")
	@ResponseBody
	public MessageResult refuseTicket(Integer id,HttpServletResponse response){
		
		OrderTicketError orderTicketError = orderTicketErrorService.getTicketErrorByid(id);
		
		RefusedTicket refusedTicket=new RefusedTicket();
		refusedTicket.setTradeTime(orderTicketError.getTradeTime());
		refusedTicket.setAgent_id(orderTicketError.getAgentId());
		refusedTicket.setTid(orderTicketError.getTkId());//拒票的tid 实际上是 表中 tkid
		refusedTicket.setTotal(orderTicketError.getTradePrice());
	//	refusedTicket.setRequestTimestamp(orderTicketError.getRequestTimestamp());
		
		AgentInfo agent = agentInfoApi.queryAgentInfoByOrderTicket(orderTicketError.getAgentId());
		Boolean flag = notifyResultService.notifyMsg(null, refusedTicket,agent.getUrl());
		
		if(!flag){
			log.error("拒票失败tid为"+orderTicketError.getAgentId()+orderTicketError.getTkId());
			return MessageResult.rfail();
		}
		//更新数据库里state的状态
		   orderTicketErrorService.changeTicketErrorState(orderTicketError);
		   log.error("拒票成功tid为"+orderTicketError.getAgentId()+orderTicketError.getTkId());
		return MessageResult.rok();
	}
	

	
	//回收票	
	@RequestMapping(value="/recycleTicket")
	@ResponseBody
	public MessageResult recycleTicket(Integer id,HttpServletResponse res){
		
		OrderTicketError orderTicketError = orderTicketErrorService.getTicketErrorByid(id);
		log.info(orderTicketError.getAgentId()+"回收"+orderTicketError.getAgentId()+orderTicketError.getTkId());
		RecycleEntity entity = new RecycleEntity();
		entity.setAgentid(orderTicketError.getAgentId());
		entity.setRecyclePrice(orderTicketError.getRecyclePrice().doubleValue());
		entity.setTkid(orderTicketError.getTkId());
		
		Response response = ticketApi.recycleTicket(entity);
		if(response.getData() == null){
			log.error(response.getResult().getResultCode()+response.getResult().getResultMsg());
			return MessageResult.fail(response.getResult().getResultCode(), response.getResult().getResultMsg());
			
		}
		orderTicketErrorService.changeTicketErrorState(orderTicketError);
		log.info(response.getResult().getResultCode()+response.getResult().getResultMsg());
		return MessageResult.ok(response.getResult().getResultCode(), response.getResult().getResultMsg());
		
	}
	
	//取消票	
	@RequestMapping(value="/cancelTicket")
	@ResponseBody
	public MessageResult cancelTicket(Integer id,HttpServletResponse res){
		OrderTicketError orderTicketError = orderTicketErrorService.getTicketErrorByid(id);
		
		CancelOrder order = new CancelOrder(null, null, null, null, null);
		order.setAgentid(orderTicketError.getAgentId());
		order.setTkid(orderTicketError.getTkId());
		order.setApplyTime(new Date().getTime());
		//order.setDesc(desc);
		order.setType(2);
		
		log.info(orderTicketError.getAgentId()+"取消"+orderTicketError.getAgentId()+orderTicketError.getTkId());
		Response response = ticketApi.cancelOrder(order);
		if(response.getData() == null){
			log.error(response.getResult().getResultCode()+response.getResult().getResultMsg());
			return MessageResult.fail(response.getResult().getResultCode(), response.getResult().getResultMsg());
		}
		orderTicketErrorService.changeTicketErrorState(orderTicketError);
		log.info(response.getResult().getResultCode()+response.getResult().getResultMsg());
		return MessageResult.ok(response.getResult().getResultCode(), response.getResult().getResultMsg());
	}

	//查出错误票的总数
	@RequestMapping(value="/amount")
	@ResponseBody
	public Map queryCount(HttpServletResponse res,String startDate,String endDate){
	  Map<String, Object> map = new HashMap<>();
	  Map<String, Object> paramsMap = new HashMap<>();
	  paramsMap.put("startDate", startDate);
	  paramsMap.put("endDate", endDate);
	  int amount =orderTicketErrorApi.queryOrderTicketErrorAmount(paramsMap);
	  map.put("amount", amount);
	  return map;
	}

	
	
}
