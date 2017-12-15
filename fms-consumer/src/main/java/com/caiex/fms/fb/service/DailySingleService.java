package com.caiex.fms.fb.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.oltp.api.Response;



public interface DailySingleService {
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	public Response detailSGLExcel( HttpServletResponse res,String year, String month, String day)throws Exception;
}
