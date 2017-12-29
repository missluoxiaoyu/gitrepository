package com.caiex.fms.bookie.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.bookie.service.BookieSummaryService;
import com.caiex.fms.fb.service.SummaryService;
import com.caiex.oltp.api.Response;


@Controller
@RequestMapping("/bookieSummary")
public class BookieSummaryController {

	@Autowired
	private BookieSummaryService service;
	
	
	@RequestMapping("/list")
	public String view(){
		return "bookieSummary";
	}
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			map= service.queryAll(year, month, day);
		}catch(Exception e){
			//log.error(e);
			e.printStackTrace();
		}
				return map;
	}
	@RequestMapping(value = "/summaryExcel")
	@ResponseBody
	public 	Response summaryExcel(String year,String month,String day,HttpServletResponse response) throws Exception {
		Response res = new Response();
		try{
			res = service.summaryExcel(year, month, day, response);
		
		}catch(Exception e){
			//log.error(e);
			e.printStackTrace();
		}
		return res;
	}
	
	
}