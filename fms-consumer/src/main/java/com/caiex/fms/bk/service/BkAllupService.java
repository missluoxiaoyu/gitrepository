package com.caiex.fms.bk.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.oltp.api.Response;

public interface BkAllupService {

	Map<String, Object> queryAll(String year, String month, String day)throws Exception;

	Response dailyAllupExcel(String year, String month, String day,
			HttpServletResponse response);

}
