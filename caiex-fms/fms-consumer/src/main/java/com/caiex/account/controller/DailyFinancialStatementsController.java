package com.caiex.account.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.account.service.DailyFinancialStatementsService;
import com.caiex.account.utils.Response;


@Controller
@RequestMapping("/dailyFinancialStatements")
public class DailyFinancialStatementsController {

	private final static Logger log= Logger.getLogger(DailyFinancialStatementsController.class);
	
	@Autowired
	private DailyFinancialStatementsService service;
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,HttpServletResponse response) throws Exception {
		response.setHeader( "Access-Control-Allow-Origin","*");
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			map=service.queryAll(year,month,day);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
				return map;
	}
	@RequestMapping(value = "/summaryExcel")
	@ResponseBody
	public 	Response summaryExcel(String year,String month,String day,HttpServletResponse response) throws Exception {
		response.setHeader( "Access-Control-Allow-Origin","*");
		Response res = new Response();
		try{
			res = service.summaryExcel(year, month, day, response);
		
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	
	
	
}
