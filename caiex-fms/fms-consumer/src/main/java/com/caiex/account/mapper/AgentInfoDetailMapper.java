package com.caiex.account.mapper;

import java.util.List;

import com.caiex.account.entity.AgentInfoDetail;

public interface AgentInfoDetailMapper {
	//添加明细
	 void addPreStoreDetail(AgentInfoDetail agentDetail);
	//查询明细
	List<AgentInfoDetail> queryPreStoreDetail(String agentCode);
	
	
	
}
