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

import com.caiex.fms.bk.service.BkAllupService;
import com.caiex.fms.fb.service.DailyAllupService;
import com.caiex.oltp.api.Response;



@Controller
@RequestMapping("/dailyAllup")
public class DailyAllupController {
	@Autowired
	private DailyAllupService service;
	
	
	@Autowired
	private BkAllupService  basketballService;
	
	private final static Logger log= Logger.getLogger(DailyAllupController.class);

	@RequestMapping(value = "/list")
	public String view(HttpServletResponse response) {
		response.setHeader( "Access-Control-Allow-Origin","*");
		return "allup";
	}
	
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String year,String month,String day,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
	
		map=service.queryAll(year,month,day);
		return map;
	}

	@RequestMapping(value = "/dailyAllupExcel")
	@ResponseBody
	public Response  dailyAllupExcel(String year,String month,String day,HttpServletRequest request, HttpServletResponse response,String pid) throws Exception {
		Response res = new Response();
	
		service.dailyAllupExcel(year,month,day,response);
		return res;
	}
	
}