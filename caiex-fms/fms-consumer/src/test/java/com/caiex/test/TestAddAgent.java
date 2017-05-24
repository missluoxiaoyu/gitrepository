package com.caiex.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caiex.ConsumerApp;
import com.caiex.account.entity.AgentInfo;
import com.caiex.account.mapper.AgentInfoMapper;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestAddAgent {
	
	@Autowired
	private AgentInfoMapper mapper;
	
	@Test
	public void test (){
		AgentInfo agentIn = new AgentInfo();
		agentIn.setAgentCode("119");
		agentIn.setAgentName("119");
		agentIn.setAgentNum(119);
		agentIn.setPassword("1245678");
		agentIn.setAgentSell(0);
		agentIn.setCreateTime(new Date());
		agentIn.setCreator(1);
		agentIn.setPassword("1111");
		agentIn.setPrestore(10);
		agentIn.setPrestoreAlarm(1);
		agentIn.setUpdateTime(new Date());
		agentIn.setUpdator(11);
		mapper.addAgent(agentIn);
	}
	
	
}
