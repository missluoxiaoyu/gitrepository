package com.fms.api;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;



public interface DetailAllupService {
	public Map<String, Object> queryAll(String year,String month,String day) throws Exception;

	//public Response dailyAllupExcel(String year,String month,String day,HttpServletResponse response) throws Exception;
}
