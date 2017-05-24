package com.caiex.account.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.AgentInfoDetail;
import com.caiex.account.model.AgentInfoModel;
import com.caiex.account.utils.Response;

public interface AgentInfoService {
	AgentInfo getUrlByAgentNum(AgentInfo agentInfo);

	//查询渠道信息
	public Map queryChannel(String year, String month, String day) throws Exception;
	//更新渠道信息
	public Response updateAgent(AgentInfoModel result,HttpSession session);
	//添加渠道信息
	public Response addAgent(AgentInfo agentInfo,HttpSession session);
	
	//导出表
	Response exportchannelStatisticsExcel(HttpServletRequest request, HttpServletResponse response,String year,String month,String day) throws Exception;
	//渠道全部停售 全部开售
	public Response changeAllAgentSellStatus(AgentInfoModel info,HttpServletRequest request);
	//开停售
	public Response changeAgentSellStatus(AgentInfoModel agentInfo,HttpServletRequest request);
	//查询preStore的操作明细
	public List<AgentInfoDetail> queryPreStoreDetail(int agentId);
}
