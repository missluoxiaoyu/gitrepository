package com.caiex.fms.bk.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.oltp.api.Response;

public interface BkSingleService {

	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	public Response detailSGLExcel( HttpServletResponse res,String year, String month, String day)throws Exception;
	
}
