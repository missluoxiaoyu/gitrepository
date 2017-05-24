package com.caiex.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.ConsumerApp;
import com.caiex.account.entity.AgentInfoDetail;
import com.caiex.account.mapper.AgentInfoDetailMapper;
import com.caiex.account.service.AgentInfoDetailService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestDetail {

	@Autowired
	private AgentInfoDetailMapper  mapper;
	
	
	@Test
	public void test (){
	
		AgentInfoDetail  agentDetail = new AgentInfoDetail();
		
		agentDetail.setAgentCode("101");
		agentDetail.setNewPreStore(1000);
		agentDetail.setAgentName("101cc");
		agentDetail.setOperateTime(new Date());
		agentDetail.setPreStoreDetail("");
		agentDetail.setOldPreStore(0);
		
		mapper.addPreStoreDetail(agentDetail);
		
	}
	
	
}
