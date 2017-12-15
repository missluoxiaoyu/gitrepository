package com.caiex.fms.fb.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.oltp.api.Response;



public interface DailyBreakDownService {
	
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	public Response dailyFinancialDetailExcel(String year, String month, String day,HttpServletResponse response);
}
