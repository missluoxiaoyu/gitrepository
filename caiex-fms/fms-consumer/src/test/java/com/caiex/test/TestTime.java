package com.caiex.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTime {

	public static void main(String[] args) throws Exception {
		/*
		 Map<String, Object> map = queryAll("2017", "1");
		 
		 for (String key : map.keySet()) {
			   System.out.println("key= "+ key + " and value= " + map.get(key));
			  }
			  
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(getCurrentYearStartTime()));
		 
		 
		/* SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式  
         Calendar cal = Calendar.getInstance();  
         Date time=sdf.parse("2017-6-6");
         cal.setTime(time);  
         System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期  
         
         //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
         int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
         if(1 == dayWeek) {  
            cal.add(Calendar.DAY_OF_MONTH, -1);  
         }  
         
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
        System.out.println("所在周星期一的日期："+sdf.format(cal.getTime()));
       // System.out.println(cal.getFirstDayOfWeek()+"-"+day+"+6="+(cal.getFirstDayOfWeek()-day+6));
        
        cal.add(Calendar.DATE, 6);
        System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));  
         */
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		/*
		System.out.println(getLastDay("2017-6-5"));
		 Calendar now = Calendar.getInstance();
		 now.set(2017, 6, 7);
		System.out.println(now.get(Calendar.DAY_OF_MONTH));
		System.out.println(new Date().getDate());*/
		/*String s = "2017/1";
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
	        sdf1.setLenient(false);
	        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");
	        for(int i = 1; i < 32; i++){
	            try {
	                Date date = sdf1.parse(s + "/" + i);
	                System.out.println(sdf1.format(date) + " : " + sdf2.format(date));
	            } catch (Exception e) {
	                //do nothing
	            }
	        }*/
              
		/*List<String> time =getDate("2017", "6");
		for (String string : time) {
			System.out.println(string);
		}*/
		
	}
	
	
	public static Date getTimesWeekmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
        return cal.getTime();  
    }  
	
	 public static Date getTimesMonthmorning() {  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
	        return cal.getTime();  
	    }  
	 
	 public static Date getCurrentYearStartTime() {  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.YEAR));  
	        return cal.getTime();  
	    }  
	
	 public static int getWeek(String str) throws Exception{  
	     SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");  
	     Date date =sdf.parse(str);  
	     Calendar calendar = Calendar.getInstance();  
	     calendar.setTime(date);  
	     //第几周  
	     int week = calendar.get(Calendar.WEEK_OF_MONTH);  
	     //第几天，从周日开始  
	     int day = calendar.get(Calendar.DAY_OF_WEEK);  
	     return week;  
	 }   
	 
	 public static String getLastDay(String datadate)throws Exception{
	        Date date = null;
	        String lastDay = null;
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        date = format.parse(datadate);
	        
	        //创建日历
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.MONTH, 1);    //加一个月
	        calendar.set(Calendar.DATE, 1);     //设置为该月第一天
	        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
	        lastDay = format.format(calendar.getTime());
	        
	        return lastDay ;
	    }
	 /*
	 public static  List<String>  getDate(String year ,String month) throws Exception{
    	 List<String> timeList = new ArrayList<String>(); 
    	String s = year+"-"+month;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        SimpleDateFormat sdfWeek = new SimpleDateFormat("EEE");
        for(int i = 1; i < 32; i++){
            
                Date date = sdf.parse(s + "-" + i);
                System.out.println(sdf.format(date) + " : " + sdfWeek.format(date));
                timeList.add(sdf.format(date) + " : " + sdfWeek.format(date));
       
        }
       return timeList;  
    }*/
	 
	 
	 public  static Map<String, Object> queryAll(String year,String month) throws Exception {
		 Map<String, Object> model = new HashMap<String, Object>();
		 
		 List<String> timeList = getDate(year, month);
		 
		 List<String> time = new ArrayList<>();
		 int count = 0;
		for (int i = 0; i < timeList.size(); i++) {
			
			time.add(timeList.get(i));
			if(timeList.get(i).endsWith("日") ){
				count++;
				model.put(count+"", time);
				time = new ArrayList<>();
			}
			if(i==timeList.size()-1){
				model.put(count+1+"", time);
			}
		}
		 
		return model;
	}
	
	
	
	 public static  List<String>  getDate(String year ,String month) throws Exception{
    	 List<String> timeList = new ArrayList<String>(); 
    	String s = year+"-"+month;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        SimpleDateFormat sdfWeek = new SimpleDateFormat("EEE");
        for(int i = 1; i < 32; i++){
            try {
				
		
                Date date = sdf.parse(s + "-" + i);
                System.out.println(sdf.format(date) + " : " + sdfWeek.format(date));
                timeList.add(sdf.format(date) + " : " + sdfWeek.format(date));
        	} catch (Exception e) {
				// TODO: handle exception
			}
        }
       return timeList;  
       }
	 
	 
}
