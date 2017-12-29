package com.caiex.fms.bookie.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caiex.dbservice.caiexbooker.entity.VirBetAgentInfoDetail;
import com.caiex.dbservice.model.BookieOrderModel;
import com.caiex.oltp.api.Response;

public interface BookieAgentService {

	Map<String, Object> queryChannel(String year, String month, String day) throws Exception ;

	List<VirBetAgentInfoDetail> queryPreStoreDetail(String agentCode);

	void exportchannelStatisticsExcel(HttpServletRequest request,HttpServletResponse response, String year, String month, String day)throws Exception ;

	

}
