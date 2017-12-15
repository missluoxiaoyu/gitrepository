package com.caiex.fms.bk.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.fms.bk.service.BkSingleService;
import com.caiex.oltp.api.Response;

@Controller
@RequestMapping("/basketBallDetailSGL")
public class BkDailySingleController {
	
	@Autowired
	private BkSingleService service;
	
	@RequestMapping(value = "/list")
	public String view() {
		return "basketballSingle";
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
