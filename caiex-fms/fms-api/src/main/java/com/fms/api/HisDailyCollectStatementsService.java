package com.fms.api;

import java.util.Map;


public interface HisDailyCollectStatementsService {

	public   Map<String, Object> queryAll(String year,String month) throws Exception ;
	
}
