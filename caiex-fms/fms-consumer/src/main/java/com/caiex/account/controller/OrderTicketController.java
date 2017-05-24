package com.caiex.account.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caiex.account.entity.OrderTicket;
import com.caiex.account.service.OrderTicketService;

@Controller
public class OrderTicketController {
	
	@Autowired
	private OrderTicketService orderTicketService;
	
	
	
	@RequestMapping(value="/time")
	public void verifymore() throws ParseException{
		Map map=new HashMap<>();
		String str="2017-01-12 18:12:05";
		String end="2017-03-24 09:46:48";
		SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str1=sdf.parse(str);
		Date end1=sdf.parse(end);
		orderTicketService.verifyMore(str1,end1,111,1);
		
	}
	@RequestMapping(value="/one")
	public void verifyone(String tid){
		orderTicketService.verifyOne(tid);
	}
	
}
