package com.caiex.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.ConsumerApp;
import com.caiex.account.entity.MatchInfoHis;
import com.caiex.account.entity.OrderTicketDetailHis;
import com.caiex.account.entity.OrderTicketMatchesHis;
import com.caiex.account.mapper.OrderTicketDetailHisMapper;
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestDetailMapper {
	
	@Autowired
	private OrderTicketDetailHisMapper mapper;
	
	
	@Test
	public void test() {
		List<OrderTicketMatchesHis> sids = new ArrayList<OrderTicketMatchesHis>();
		OrderTicketMatchesHis s =new OrderTicketMatchesHis();
		s.setSid(20);
		sids.add(s);
		MatchInfoHis infoModel = new MatchInfoHis();
		infoModel.setMatch_code("7047");
		//infoModel.setProduct("HAD");
		infoModel.setSids(sids);
		OrderTicketDetailHis detailInvest  = mapper.detailHisQueryInvest(infoModel);
		System.out.println(detailInvest.getPrice_allup());
		
		OrderTicketDetailHis detailTotalPrice = mapper.detailHisQueryTotalPrice(infoModel);
		System.out.println(detailTotalPrice.getTotal_price());
	}

}
