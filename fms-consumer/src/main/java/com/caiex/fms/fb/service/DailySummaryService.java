package com.caiex.fms.fb.service;


import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import com.caiex.oltp.api.Response;


public interface DailySummaryService {

	public   Map<String, Object> queryAll(String year,String month) throws Exception ;

	public Response dailySummaryExcel(String year, String month,HttpServletResponse response);
	
}
