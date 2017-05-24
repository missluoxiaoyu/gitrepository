package com.caiex.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.ConsumerApp;
import com.caiex.account.entity.AgentInfoDetail;
import com.caiex.account.mapper.AgentInfoDetailMapper;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestAgentDetail {
	
	@Autowired
	private AgentInfoDetailMapper agentInfoDetailMapper;
	
	@Test
	public void test(){
		List <AgentInfoDetail>  details=agentInfoDetailMapper.queryPreStoreDetail("101");
		AgentInfoDetail agent=details.get(details.size()-1);
		System.out.println("ddddddddddddddddddddddddddddd");
	    System.out.println(agent.getOperateTime());
		
	}
	
	
	
}
