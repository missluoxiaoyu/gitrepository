package com.caiex.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.caiex.account.entity.AgentInfo;

public class TestList {

	public static void main(String[] args) {
		List<AgentInfo> all = new ArrayList<>();
		List<AgentInfo> some = new ArrayList<>();
		
		
	  AgentInfo agent1 = new AgentInfo();
	  agent1.setAgentName("101");
	  AgentInfo agent2 = new AgentInfo();
	  agent2.setAgentName("102");
	  AgentInfo agent3 = new AgentInfo();
	  agent3.setAgentName("103");
	  AgentInfo agent4 = new AgentInfo();
	  agent4.setAgentName("104");
	  
	  all.add(agent1);
	  all.add(agent2);
	  all.add(agent3);
	  all.add(agent4);
	  
	  System.out.println(all.contains(agent1));
	  System.out.println(all.get(0).getAgentName());
	  System.out.println(all.get(all.size()-1).getAgentName());
	 /* some.add(agent1);
	//  some.add(agent2);
	//  some.add(agent3);
	  
	boolean flag = all.removeAll(some);
	  
	    for (AgentInfo agentInfo : all) {
	    	System.out.println(agentInfo.getAgentName());
	     }*/
	}
	
	
	public static  List diff(List ls1, List ls2) {
		List list = new ArrayList(Arrays.asList(new Object[ls1.size()]));
        Collections.copy(list, ls1);
        list.removeAll(ls2);
        return list;
  }
	
}
