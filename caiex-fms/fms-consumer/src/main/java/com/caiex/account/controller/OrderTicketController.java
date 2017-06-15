package com.caiex.account.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.OrderTicket;
import com.caiex.account.entity.OrderTicketError;
import com.caiex.account.service.OrderTicketService;

@Controller
@RequestMapping("/orderTicket")
public class OrderTicketController {
	
	@Autowired
	private OrderTicketService orderTicketService;
	
	//根据页面上的给定时间查询
		@RequestMapping(value="/queryAll")
		@ResponseBody
		public Map<String, Object> getTicketsByTime(String startDate,String endDate,Integer agentid,Integer page,HttpServletResponse response) throws Exception{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> map =orderTicketService.queryAgentTicketByTime(sdf.parse(startDate),sdf.parse(endDate), agentid, page);
			return map;
		}
		
		//单独查询tkid
		@RequestMapping(value="/query")
		@ResponseBody
		public Map<String, Object> queryTogether(String tkId,String agentid,HttpServletResponse response){
			return orderTicketService.queryAgentTicket(tkId,Integer.parseInt(agentid));
		}
	
		@RequestMapping(value = "/queryAllAgent")
		@ResponseBody
		public  Map<String, Object> queryAllAgent(HttpServletResponse response) throws Exception {
			List<AgentInfo> agentList = orderTicketService.queryAllAgentInfo();
			Map<String, Object> map = new HashMap<>();
			map.put("agentList", agentList);
			return map;
			
		}
		
}
