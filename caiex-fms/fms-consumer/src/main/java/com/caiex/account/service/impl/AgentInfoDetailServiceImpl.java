package com.caiex.account.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.AgentInfoDetail;
import com.caiex.account.mapper.AgentInfoDetailMapper;
import com.caiex.account.redis.RedisUtil;
import com.caiex.account.service.AgentInfoDetailService;

@Service
public class AgentInfoDetailServiceImpl implements AgentInfoDetailService{

	@Autowired 
	private AgentInfoDetailMapper agentInfoDetailMapper;
	

	
	@Override
	public List<AgentInfoDetail> queryPreStoreDetail(String agentCode) {
		return agentInfoDetailMapper.queryPreStoreDetail(agentCode);
	}


	@Override
	public void addPreStoreDetail(AgentInfo agentInfo) {
		AgentInfoDetail newAgentDetail = new AgentInfoDetail();
		newAgentDetail.setAgentCode(agentInfo.getAgentCode());
		newAgentDetail.setAgentName(agentInfo.getAgentName());
		newAgentDetail.setNewPreStore(agentInfo.getPrestore());//新的preStore
		newAgentDetail.setOperateTime(new Date());
		newAgentDetail.setOldPreStore(0);
		double oldPreStore = newAgentDetail.getOldPreStore();
		double newPreStore = newAgentDetail.getNewPreStore();
		if(newPreStore - oldPreStore >0){
			newAgentDetail.setPreStoreDetail("存款"+agentInfo.getAgentCode()+"预存款由"+oldPreStore+"增加了"+(newPreStore - oldPreStore));	
		}else{
			newAgentDetail.setPreStoreDetail("取款"+agentInfo.getAgentCode()+"预存款由"+oldPreStore+"减少了"+(oldPreStore - newPreStore));
		}
		agentInfoDetailMapper.addPreStoreDetail(newAgentDetail);
	}


/*	@Override
	public void insertPreStoreDetail(AgentInfo agentInfo) {
		List<AgentInfoDetail> agentDetails=queryPreStoreDetail(agentInfo.getAgentCode());
		
		if(agentDetails.size() ==0 ){//如果之前没有数据明细  添加新的明细
			addPreStoreDetail(agentInfo);
		}else{//如果有数据，更新明细
			AgentInfoDetail oldAgentDetail = agentDetails.get(agentDetails.size()-1); //取出最新一条更新记录
			
			AgentInfoDetail newAgentDetail = new AgentInfoDetail();
			newAgentDetail.setAgentCode(agentInfo.getAgentCode());
			newAgentDetail.setAgentName(agentInfo.getAgentName());
			newAgentDetail.setNewPreStore(agentInfo.getPrestore());//新的preStore
			newAgentDetail.setOperateTime(new Date());
			newAgentDetail.setOldPreStore(oldAgentDetail.getNewPreStore());
			
			double oldPreStore = newAgentDetail.getOldPreStore();
			double newPreStore = newAgentDetail.getNewPreStore();
			if(newPreStore - oldPreStore >0){
				newAgentDetail.setPreStoreDetail("存款"+agentInfo.getAgentCode()+"预存款由"+oldPreStore+"增加了"+(newPreStore - oldPreStore));	
			}else{
				newAgentDetail.setPreStoreDetail("取款"+agentInfo.getAgentCode()+"预存款由"+oldPreStore+"减少了"+(oldPreStore - newPreStore));		
			}
			agentInfoDetailMapper.addPreStoreDetail(newAgentDetail);
		}
	}
*/
	
	public void insertPreStoreDetail(AgentInfoDetail agentDetail ) {
		double oldPreStore = agentDetail.getOldPreStore();
		double newPreStore = agentDetail.getNewPreStore();
		
		if(newPreStore - oldPreStore >0){
			agentDetail.setPreStoreDetail("存款"+agentDetail.getAgentCode()+"预存款由"+oldPreStore+"增加了"+(newPreStore - oldPreStore));	
		}else{
			agentDetail.setPreStoreDetail("取款"+agentDetail.getAgentCode()+"预存款由"+oldPreStore+"减少了"+(oldPreStore - newPreStore));		
		}
		agentInfoDetailMapper.addPreStoreDetail(agentDetail);
		
	}
	
	@Override
	public void changePreStoreDetail(AgentInfo info,double prestore){
		AgentInfoDetail agentDetail = new AgentInfoDetail();
		
		agentDetail.setAgentCode(info.getAgentCode());
		agentDetail.setAgentName(info.getAgentName());
		agentDetail.setOldPreStore(info.getPrestore());
		agentDetail.setNewPreStore(info.getPrestore()+prestore);
		agentDetail.setOperateTime(new Date());
		
		insertPreStoreDetail(agentDetail);
		
	}
	
	
}
