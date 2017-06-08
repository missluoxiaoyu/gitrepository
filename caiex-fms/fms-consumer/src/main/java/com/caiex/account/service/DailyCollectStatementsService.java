package com.caiex.account.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.account.utils.Response;

public interface DailyCollectStatementsService {

	public   Map<String, Object> queryAll(String year,String month) throws Exception ;

	public Response dailySummaryExcel(String year, String month,HttpServletResponse response);
	
}
