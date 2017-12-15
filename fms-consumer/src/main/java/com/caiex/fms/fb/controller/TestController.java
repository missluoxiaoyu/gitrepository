package com.caiex.fms.fb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caiex.dbservice.currentdb.api.AgentInfoProApi;
import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.currentdb.entity.OrderTicket;
import com.caiex.fms.fb.service.SummaryService;

@Controller
public class TestController {

@RequestMapping("/test")
 public String test(){
	return "menu" ;
 }

@RequestMapping("testOrder")
@ResponseBody
public OrderTicket testOrder(){
	OrderTicket o = new OrderTicket();
	o.setAgent_id(1111111);
  return o;
}
	
}
