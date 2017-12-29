package com.caiex.fms.bookie.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.oltp.api.Response;

public interface BookieSGLService {

	Map<String, Object> queryAll(String year, String month, String day) throws Exception;


	Response detailSGLExcel(HttpServletResponse response, String year,String month, String day);


}
