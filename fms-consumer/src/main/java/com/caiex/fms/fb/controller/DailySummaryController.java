package com.caiex.fms.fb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.fb.service.DailySummaryService;
import com.caiex.oltp.api.Response;

@Controller
@RequestMapping("/dailyCollectStatements")
public class DailySummaryController {
	private final static Logger log= Logger.getLogger(DailySummaryController.class);
	
	
	@Autowired
	private DailySummaryService service;
	
	
	@RequestMapping(value = "/list")
	public String view() {
		return "summary";
	}
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,HttpServletResponse response) throws Exception {
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
