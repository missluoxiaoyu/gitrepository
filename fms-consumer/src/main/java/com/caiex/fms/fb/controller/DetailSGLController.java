package com.caiex.fms.fb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.bk.service.BkSingleService;
import com.caiex.fms.fb.service.DailySingleService;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("/detailSGL")
public class DetailSGLController {
	private final static Logger log= Logger.getLogger(DetailSGLController.class);
	@Autowired 
	private DailySingleService service;
	
	
	@Autowired
	private BkSingleService basketBallService;
	
	
	@RequestMapping(value = "/list")
	public String view() {
		return "single";
	}
	
	

	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,HttpServletResponse response) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		
	
			  map=service.queryAll(year,month,day);			
	
			return map;
	}
	
	@RequestMapping(value = "/detailSGLExcel")
	@ResponseBody
	public Response detailSGLExcel(String year,String month,String day,HttpServletResponse response) throws Exception {
		 Response res = new Response();
		
		
			 res = service.detailSGLExcel(response,year,month,day); 
		 
		 
		return res;
	}
}
