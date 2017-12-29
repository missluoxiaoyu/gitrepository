package com.caiex.fms.bookie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import com.caiex.dbservice.caiexbooker.entity.VirBetAgentInfoDetail;
import com.caiex.fms.bookie.service.BookieAgentService;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("/bookieAgent")
public class BookieAgentController {
	private final static Logger log= Logger.getLogger(BookieAgentController.class);

	@Autowired
	private BookieAgentService service;
	
	
	@RequestMapping(value = "/list")
	public String view() {
		return "bookieChannel";
	}
	
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,HttpServletRequest request,HttpServletResponse response) throws Exception {

		Map<String, Object> map=service.queryChannel(year, month, day);
		return map;
	}
	

	@RequestMapping(value = "/queryPreStoreDetail")
	@ResponseBody
	public Map<String, Object> queryPreStoreDetail(String   agentCode,HttpServletResponse response) throws Exception {
		
		Map<String, Object> preStoreMap = new HashMap<String, Object>();
		 List <VirBetAgentInfoDetail> agentInfoDetails =service.queryPreStoreDetail(agentCode);
		 preStoreMap.put("agentInfoDetails", agentInfoDetails);
		 return preStoreMap;
	}
	
	
	
	
	@RequestMapping(value = "/channelStatisticsExcel")
	@ResponseBody
	public Response channelStatisticsExcel(String year,String month,String day,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Response rs=new Response();
		try{
			service.exportchannelStatisticsExcel(request,response,year,month,day);
		
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}

}
