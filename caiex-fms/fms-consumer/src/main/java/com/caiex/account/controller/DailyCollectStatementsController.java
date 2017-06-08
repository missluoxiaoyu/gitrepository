package com.caiex.account.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.account.service.DailyCollectStatementsService;
import com.caiex.account.utils.Response;

@Controller
@RequestMapping("/dailyCollectStatements")
public class DailyCollectStatementsController {
	private final static Logger log= Logger.getLogger(DailyCollectStatementsController.class);
	
	
	@Autowired
	private DailyCollectStatementsService service;
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,HttpServletResponse response) throws Exception {
		response.setHeader( "Access-Control-Allow-Origin","*");
		Map<String, Object> map=new HashMap<String, Object>();
		
		try{
			 map=service.queryAll(year, month);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "/dailySummaryExcel")
	@ResponseBody
	public Response dailySummaryExcel(String year ,String month,HttpServletResponse response) throws Exception {
		response.setHeader( "Access-Control-Allow-Origin","*");
		Response res = new Response();
		try{
			res = service.dailySummaryExcel(year,month,response);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}
}
