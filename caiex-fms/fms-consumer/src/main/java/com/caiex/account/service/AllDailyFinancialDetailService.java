package com.caiex.account.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.account.utils.Response;

public interface AllDailyFinancialDetailService {
	
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	public Response dailyFinancialDetailExcel(String year, String month, String day,HttpServletResponse response);
}
