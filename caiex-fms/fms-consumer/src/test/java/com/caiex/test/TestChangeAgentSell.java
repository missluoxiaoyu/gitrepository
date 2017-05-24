package com.caiex.test;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.ConsumerApp;
import com.caiex.account.model.AgentInfoModel;
import com.caiex.account.service.AgentInfoService;
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestChangeAgentSell {

	@Autowired
	private AgentInfoService service;
	
	
	@Test
	public void test(){
		
		HttpServletRequest request = null ;
		AgentInfoModel agentInfo = new AgentInfoModel();
		agentInfo.setAgentCode("101");
		agentInfo.setAgentId(101);
		agentInfo.setAgentSell(0);
		service.changeAgentSellStatus(agentInfo, request);
		
	}
}
