package com.caiex.account.mapper;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.AgentInfo;

@MapperScan
public interface AgentInfoMapper {
	//查询所有
	List<AgentInfo> queryAll();
	AgentInfo getUrlByAgentNum(AgentInfo agentInfo);
	
	AgentInfo queryAgentInfoByAgentid(int agentNum);

	void updateAgentinfo(AgentInfo oldAgent);
	
	void addAgent(AgentInfo agentInfo);
	
	AgentInfo queryAgentInfoByAgentName(String AgentName);
	
}
