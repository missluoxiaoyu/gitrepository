package com.caiex.account.service;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DetailAllupService {
	public Map<String, Object> queryAll(String date) throws ParseException;

	public void dailyAllupExcel(HttpServletRequest request,
			HttpServletResponse response, String date) throws ParseException;
}
