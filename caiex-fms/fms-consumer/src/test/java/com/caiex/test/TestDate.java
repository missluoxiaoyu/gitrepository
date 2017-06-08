package com.caiex.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class TestDate {

	public static void main(String[] args) throws Exception {
		
		getStartAndEndDate("2017", "5", "22");
	}
	
	private static void getStartAndEndDate(String year, String month, String day)  throws Exception{
		String startDate = null;//开始时间
     	String endDate = null;//结束时间
		
		
		if(StringUtils.isEmpty(month)){//如果传入的时间格式为 年，传入的日期格式为2017 查询的是2017-01-01 到 2018-01-01的数据
			startDate = year + "-01-01 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(year,new String[] { "yyyy" }));
			calendar.add(Calendar.YEAR, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd") + " 00:00:00";
		}
		else if (StringUtils.isEmpty(day)) {//如果传入的时间格式为 年月，日期格式为 2017-01 查询的是一个月的数据
			startDate = year + "-" + month + "-01 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate, new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.MONTH, 1); 
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd") + " 00:00:00";
		} else {//如果传入的时间格式为 年月日 例 2017-2017-01-01 
			startDate=year + "-" + month + "-" + day+" 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate, new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.DATE, 1); 
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd") + " 00:00:00"; 
			
		}
		
		System.out.println(startDate);
		System.out.println(endDate);
	}
	
}
