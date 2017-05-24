package com.caiex.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.ConsumerApp;
import com.caiex.account.entity.OrderTicketError;
import com.caiex.account.mapper.OrderTicketErrorMapper;
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)
public class TestTicketError {

	@Autowired
	private OrderTicketErrorMapper mapper;
	
	@Test
	public void test() {
		OrderTicketError errorTicket = mapper.getTicketErrorByid(21);
		System.out.println(errorTicket.getRequestTimestamp());
		System.out.println(errorTicket.getAgentId());
	}

}
