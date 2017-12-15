package com.caiex.fms.fb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.fb.service.DailyBreakDownService;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("/dailyFinancialDetail")
public class DailyBreakDownController {
	private final static Logger log= Logger.getLogger(DailyBreakDownController.class);
	
	@Autowired
	private DailyBreakDownService service;
	
	
	@RequestMapping(value = "/list")
	public String view(HttpServletResponse response) {
	
		return "breakdown";
	}
	
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year, String month, String day,
			HttpServletResponse response) throws Exception {
		
		
		Map<String, Object> map=new HashMap<String, Object>();
		try{
	     	map=service.queryAll(year,month,day);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "/dailyFinancialDetailExcel")
	@ResponseBody
	public Response dailyFinancialDetailExcel(String year, String month, String day,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Response res = new Response();
		try{
			res=service.dailyFinancialDetailExcel(year,month,day,response);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return res;	
	}
}
