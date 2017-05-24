package com.caiex.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



import com.caiex.ConsumerApp;
import com.caiex.account.entity.AgentInfo;
import com.caiex.account.mapper.AgentInfoMapper;
import com.caiex.account.mapper.OrderTicketMapper;
import com.caiex.account.service.AgentInfoService;
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=ConsumerApp.class)// 指定spring-boot的启动类 
public class TestAgent {

	@Autowired 
	private AgentInfoMapper agentMapper;
	
	@Autowired
	private AgentInfoService agentService;
	
	@Autowired
	private OrderTicketMapper orderTicketMapper;
	
	
	@Test
	public void test() throws Exception {
		/*Map map = agentService.queryChannel("2017","5","12");
		List<ChannelStatisticsResult> channelList =(List<ChannelStatisticsResult>) map.get("channelList");
		System.out.println(map.get("channelList"));*/
		
		Map date = new HashMap<String, Object>();
		date.put("startDate", "2017-04-12 00:00:00");
		date.put("endDate", "2017-04-13 00:00:00");
		
		List<Integer> agentids=orderTicketMapper.queryAgentid(date);
		System.out.println("有交易的渠道"+agentids);
		
		List<AgentInfo> notrade = find(agentids);
		for (AgentInfo agentInfo : notrade) {
			System.out.println("没有交易的渠道"+agentInfo.getAgentName());
		}
		
	}

	
	//查出没有交易的渠道
		public List<AgentInfo> find(List<Integer> agentids){
			List<AgentInfo> agentInfos=new ArrayList<>();
			
			List <Integer> allAgents = queryAll();
			allAgents.removeAll(agentids);
			
			for (Integer agentid : allAgents) {
				AgentInfo agentInfo = agentMapper.queryAgentInfoByAgentid(agentid);
				agentInfos.add(agentInfo);
			}
			
			return agentInfos;
		}
	
	
	
	    public List <Integer> queryAll(){
	    	List <Integer> allIds= new ArrayList<>();
	    	List<AgentInfo>  all = agentMapper.queryAll();
	    	for (AgentInfo agentInfo : all) {
	    		allIds.add(agentInfo.getAgentNum());
			}
		    return allIds;
	    }
			
		//求差集
		public   List<AgentInfo> diff(List agentAll, List tardeAgent) {
			    @SuppressWarnings("rawtypes")
				List list = new ArrayList(Arrays.asList(new Object[agentAll.size()]));
		        Collections.copy(list, agentAll);
		        list.removeAll(tardeAgent);
		        return list;
		  }
		
	 
	
}
