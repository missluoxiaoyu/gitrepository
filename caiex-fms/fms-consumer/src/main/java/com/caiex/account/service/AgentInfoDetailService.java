package com.caiex.account.service;

import java.util.List;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.AgentInfoDetail;


public interface AgentInfoDetailService {
	    //添加明细
	     void addPreStoreDetail(AgentInfo agentInfo);
		//查询明细
	     public List<AgentInfoDetail> queryPreStoreDetail(String agentCode);
		//更改明细
	     public void insertPreStoreDetail(AgentInfoDetail agentDetail );
	     
	     public void changePreStoreDetail(AgentInfo info,double prestore);
}
