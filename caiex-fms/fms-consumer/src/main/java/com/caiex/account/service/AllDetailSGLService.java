package com.caiex.account.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.account.utils.Response;

public interface AllDetailSGLService {
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	public Response detailSGLExcel( HttpServletResponse res,String year, String month, String day)throws Exception;
}
