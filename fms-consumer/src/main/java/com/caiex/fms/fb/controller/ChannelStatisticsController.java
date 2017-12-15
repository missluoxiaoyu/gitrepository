package com.caiex.fms.fb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.dbservice.currentdb.entity.AgentInfoDetail;

import com.caiex.fms.fb.service.AgentInfoService;

import com.caiex.oltp.api.Response;


/** 
 * 
 */
@Controller
@RequestMapping("/channelStatistics")
public class ChannelStatisticsController {
	private final static Logger log= Logger.getLogger(ChannelStatisticsController.class);
	
	@Autowired
	private AgentInfoService agentInfoService;
	
	
	

	@RequestMapping(value = "/list")
	public String view() {
		return "channel";
	}
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,HttpServletRequest request,HttpServletResponse response) throws Exception {

		Map<String, Object> map=agentInfoService.queryChannel(year, month, day);
		return map;
	}
	
	
	
	

	
	
	

	@RequestMapping("/detailExcel")
	@ResponseBody
	public void detailExcel(String agentId,String startDate,String endDate,HttpServletResponse response) throws Exception{
		
		agentInfoService.recycleDetailExcel(agentId, startDate, endDate, response);
	
	}
	
	
	


	@RequestMapping(value = "/queryPreStoreDetail")
	@ResponseBody
	public Map<String, Object> queryPreStoreDetail(String   agentCode,HttpServletResponse response) throws Exception {
		
		Map<String, Object> preStoreMap = new HashMap<String, Object>();
		 List <AgentInfoDetail> agentInfoDetails =agentInfoService.queryPreStoreDetail(agentCode);
		 preStoreMap.put("agentInfoDetails", agentInfoDetails);
		 return preStoreMap;
	}
	
	
	
	
	@RequestMapping(value = "/channelStatisticsExcel")
	@ResponseBody
	public Response channelStatisticsExcel(String year,String month,String day,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Response rs=new Response();
		try{
				agentInfoService.exportchannelStatisticsExcel(request,response,year,month,day);
		
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
}
