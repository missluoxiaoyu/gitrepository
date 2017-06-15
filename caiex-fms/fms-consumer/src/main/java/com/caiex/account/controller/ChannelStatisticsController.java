package com.caiex.account.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.AgentInfoDetail;
import com.caiex.account.model.AgentInfoModel;
import com.caiex.account.service.AgentInfoService;
import com.caiex.account.utils.Response;


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
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public Response update(AgentInfoModel result,HttpSession session,HttpServletResponse response) throws Exception {
		
		Response rs=new Response();
		try{
		   rs=agentInfoService.updateAgent(result,session);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	
	
	
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Response add(AgentInfo agentInfo,HttpServletResponse response,HttpSession session) throws Exception {
		
		Response rs=new Response();
		try{
			rs=agentInfoService.addAgent(agentInfo, session);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	@RequestMapping(value = "/queryPreStoreDetail")
	@ResponseBody
	public Map<String, Object> queryPreStoreDetail(int  agentId,HttpServletResponse response) throws Exception {
		
		Map<String, Object> preStoreMap = new HashMap<String, Object>();
		 List <AgentInfoDetail> agentInfoDetails =agentInfoService.queryPreStoreDetail(agentId);
		 preStoreMap.put("agentInfoDetails", agentInfoDetails);
		 return preStoreMap;
	}
	
	
	
	
	@RequestMapping(value = "/updateSell")
	@ResponseBody
	public Response updateSell(AgentInfoModel agentInfo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Response rs=new Response();
		try{
		    rs=agentInfoService.changeAgentSellStatus(agentInfo,request);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	
	@RequestMapping(value = "/updateSellAll")
	@ResponseBody
	public Response updateSellAll(AgentInfoModel agentInfo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Response rs=new Response();
		try{
			rs=agentInfoService.changeAllAgentSellStatus(agentInfo,request);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	@RequestMapping(value = "/channelStatisticsExcel")
	@ResponseBody
	public Response channelStatisticsExcel(String year,String month,String day,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
