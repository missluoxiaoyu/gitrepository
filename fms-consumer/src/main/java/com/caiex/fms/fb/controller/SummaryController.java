package com.caiex.fms.fb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.fb.service.SummaryService;
import com.caiex.oltp.api.Response;


@Controller
@RequestMapping("/summary")
public class SummaryController {

	private final static Logger log= Logger.getLogger(SummaryController.class);
	
	@Autowired
	private SummaryService service;
	
	@RequestMapping(value = "/list")
	public String view() {
		return "finance";
	}
	
	
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,String pid) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			map= service.query(year, month, day, pid);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
				return map;
	}
	@RequestMapping(value = "/summaryExcel")
	@ResponseBody
	public 	Response summaryExcel(String year,String month,String day,HttpServletResponse response,String pid) throws Exception {
		Response res = new Response();
		try{
			res = service.summaryExcel(year, month, day, response,pid);
		
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	
	
	
}