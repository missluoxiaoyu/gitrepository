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
import com.caiex.account.entity.MatchInfo;
import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.entity.OrderTicketMatches;
import com.caiex.account.mapper.MatchInfoMapper;
import com.caiex.account.mapper.OrderTicketDetailMapper;
import com.caiex.account.mapper.OrderTicketMapper;
import com.caiex.account.mapper.OrderTicketMatchesMapper;
import com.caiex.account.model.MatchInfoModel;
import com.caiex.account.model.OrderTicketModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestUserManage {

	@Autowired
	private OrderTicketDetailMapper orderTicketDetailMapper;
	
	@Autowired
	private OrderTicketMatchesMapper mapper;
	
	@Autowired
	private MatchInfoMapper matchMapper;
	
	/*@Test
	public void test() {
		OrderTicketModel model =new OrderTicketModel();
		model.setStartDate("2017-6-7");
		model.setEndDate("2017-6-12");
		//model.setAgentId(101);
		model.setAgentNum(101);
		model.setPage(1);
		model.setSize(10);
		model.setInplay(0);
		model.setTradeType(2);
		
	//	PageHelper.startPage(model.getPage(), model.getSize());
		List<OrderTicketModel>  models = orderTicketmapper.queryTicketInfoSuccess(model);
		
	//	new PageInfo<>(models);
	   System.out.println(models.size());
		
	}*/
	
	/*@Test
	public void test() {
		OrderTicketDetail model =new OrderTicketDetail();
		List<OrderTicketDetail> listDetail=orderTicketDetailMapper.queryByListUnCancel(model);
		List<OrderTicketDetail> list=new ArrayList<OrderTicketDetail>();
		
		for (int i = 0; i < listDetail.size(); i++) {
			OrderTicketDetail orderTicketDetail=listDetail.get(i);
			OrderTicketMatches orderTicketMatches=new OrderTicketMatches();
			orderTicketMatches.setSid(orderTicketDetail.getSid());
			List<OrderTicketMatches> listMatch=mapper.queryByListUnCancel(orderTicketMatches);
		}
		
	
	}*/
	
	
	@Test
	public void test() {
		MatchInfo info=new MatchInfo();
		
		
		info.setMatch_code("1001");
		info.setWeek_id("2017061324");
		
		MatchInfo m =matchMapper.queryByCodeWeek(info);
		System.out.println(m.getHome_team());
	}
	

}
