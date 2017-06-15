package com.fms.api;

import java.util.Map;

public interface OrderTicketDetailService {

	/*public Map<String, Object> queryDailyCollectStatements(String year, String month)throws Exception;
	public Map<String, Object> queryDailyFinancialDetail(String year, String month, String day)throws Exception;
	public Map<String, Object> queryDailyFinancialStatements(String year, String month, String day)throws Exception;
	public Map<String, Object> queryDetailAllup(String year, String month, String day)throws Exception;
	public Map<String, Object> queryDetailSGL(String year, String month, String day)throws Exception;*/
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
}
