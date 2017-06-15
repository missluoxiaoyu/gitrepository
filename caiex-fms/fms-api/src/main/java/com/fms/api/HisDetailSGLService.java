package com.fms.api;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;



public interface HisDetailSGLService {
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	//public Response detailSGLExcel( HttpServletResponse res,String year, String month, String day)throws Exception;
}
