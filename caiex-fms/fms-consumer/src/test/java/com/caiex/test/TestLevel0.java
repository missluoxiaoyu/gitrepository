package com.caiex.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.ConsumerApp;
import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.mapper.OrderTicketDetailMapper;
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestLevel0 {

	@Autowired
	private OrderTicketDetailMapper mapper;
	
	
	
	@Test
	public void test() {
		Map<String, Object> date = new HashMap<String, Object>();
		date.put("startDate", "2017-05-16 17:30:09");
		date.put("endDate", "2017-05-26 17:29:35");
		date.put("product", "HAD");
		
		
	
	
		
	}

}
