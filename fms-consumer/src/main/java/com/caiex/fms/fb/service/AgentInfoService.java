package com.caiex.fms.fb.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.currentdb.entity.AgentInfoDetail;

import com.caiex.oltp.api.Response;



public interface AgentInfoService {


	//查询渠道信息
	public Map queryChannel(String year, String month, String day) throws Exception;
	//导出表
	Response exportchannelStatisticsExcel(HttpServletRequest request, HttpServletResponse response,String year,String month,String day) throws Exception;
	
	public List<AgentInfoDetail> queryPreStoreDetail(String agentCode);
	
	//查询回收明细
	public  Map<String, Object>  queryRecycleDetail(String agentCode,String startDate,String endDate);
	public void recycleDetailExcel(String agentCode,String startDate,String endDate,HttpServletResponse response) throws Exception;
	
}
