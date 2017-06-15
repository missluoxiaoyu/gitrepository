package com.caiex.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.HisProviderApp;
import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.fms.entity.OrderTicketDetail;
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=HisProviderApp.class)// 指定spring-boot的启动类 
public class TestHisFinancial {

	@Autowired
	private OrderTicketDetailHisMapper mapper;
	
	@Test
	public void test() {
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", "2017-6-2");
		params.put("endDate", "2017-6-4");
		 OrderTicketDetail ticketTotalInvest = mapper.queryTotalInvestment(params);
		 
		 System.out.println(ticketTotalInvest.getInv_allup());
	}

}
