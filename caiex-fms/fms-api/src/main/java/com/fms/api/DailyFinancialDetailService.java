package com.fms.api;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;




public interface DailyFinancialDetailService {
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	//public Response dailyFinancialDetailExcel(String year, String month, String day,HttpServletResponse response);
	
}
