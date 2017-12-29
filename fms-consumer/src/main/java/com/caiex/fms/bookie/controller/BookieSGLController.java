package com.caiex.fms.bookie.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.bk.service.BkSingleService;
import com.caiex.fms.bookie.service.BookieSGLService;
import com.caiex.fms.fb.service.DailySingleService;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("/bookieSingle")
public class BookieSGLController {
	private final static Logger log= Logger.getLogger(BookieSGLController.class);

	
	@Autowired
	private BookieSGLService service;
	
	
	@RequestMapping("/list")
	public String view(){
		return "bookieSingle";
	}
	

	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,HttpServletResponse response) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		try{
		    map=service.queryAll(year,month,day);			
		}catch(Exception e){
			//log.error(e);
			e.printStackTrace();
		}
				return map;
	}
	
	@RequestMapping(value = "/detailSGLExcel")
	@ResponseBody
	public Response detailSGLExcel(String year,String month,String day,HttpServletResponse response) throws Exception {
		 Response res = new Response();
		try{
		 res = service.detailSGLExcel(response,year,month,day);
		}catch(Exception e){
			//log.error(e);
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	
	
	
}
