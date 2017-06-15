package com.fms.api;

import java.util.Map;


public interface HisDailyFinancialDetailService {
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception;
	
	
}
