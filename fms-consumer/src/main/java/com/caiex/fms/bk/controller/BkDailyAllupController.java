package com.caiex.fms.bk.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.bk.service.BkAllupService;
import com.caiex.oltp.api.Response;

@Controller
@RequestMapping("/basketballDailyAllup")
public class BkDailyAllupController {
	
	@Autowired
	private BkAllupService service;
	
	
	
	@RequestMapping(value = "/list")
	public String view(HttpServletResponse response) {
		
		return "basketballAllup";
	}
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		response.setHeader( "Access-Control-Allow-Origin","*");
		Map<String, Object> map=new HashMap<String, Object>();
		try{
		    map=service.queryAll(year,month,day);
		}catch(Exception e){
			//log.error(e);
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/dailyAllupExcel")
	@ResponseBody
	public Response  dailyAllupExcel(String year,String month,String day,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Response res = new Response();
		response.setHeader( "Access-Control-Allow-Origin","*");
		try{
			service.dailyAllupExcel(year,month,day,response);
		}catch(Exception e){
			//log.error(e);
			e.printStackTrace();
		}
		return res;
	}
	
	
	
}