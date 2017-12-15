package com.caiex.fms.fb.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.oltp.api.Response;



public interface SummaryService {
	
	public Map<String, Object> queryAll(String year,String month,String day,String pid) throws Exception ;
	
	public Response summaryExcel(String year, String month, String day,HttpServletResponse response,String pid);
	
	public Map<String, Object> query(String year,String month,String day,String pid) throws Exception;
	
	public void addAllInfo(String year,String month,String day,String pid) throws Exception;
	public void  addInfo(String year,String month,String day,String pid) throws Exception;
}
