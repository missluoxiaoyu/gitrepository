package com.caiex.account.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.account.service.DetailAllupService;



@Controller
@RequestMapping("/dailyAllup")
public class DetaiAllupController {
	@Autowired(required=false) 
	private DetailAllupService service;
	private final static Logger log= Logger.getLogger(DetaiAllupController.class);

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String batchList() {
		return "statement/dailyAllup";
	}
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(String date,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//==============================================
		response.setHeader( "Access-Control-Allow-Origin","*");
		//测试allup，跨域
		//==============================================
		Map<String, Object> map=new HashMap<String, Object>();
		try{
		    map=service.queryAll(date);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/dailyAllupExcel")
	@ResponseBody
	public void dailyAllupExcel(String date,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			service.dailyAllupExcel(request,response,date);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
	}
	
}
