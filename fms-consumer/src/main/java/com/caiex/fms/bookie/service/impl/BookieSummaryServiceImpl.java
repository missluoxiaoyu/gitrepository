package com.caiex.fms.bookie.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.caiexbooker.api.fms.SummaryBookieProService;
import com.caiex.dbservice.historydb.api.bookiefms.SummaryBookieProHisService;
import com.caiex.dbservice.model.OrderTicketDetailFinancialModel;
import com.caiex.dbservice.utils.DateUtil;
import com.caiex.fms.bookie.service.BookieSummaryService;
import com.caiex.fms.utils.MergeUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;

@Service
public class BookieSummaryServiceImpl  implements BookieSummaryService{

	String [] months= {"1","2","3","4","5","6","7","8","9","10","11","12"};
	@Autowired
	private SummaryBookieProService summaryBookieProService;
	
	@Autowired
	private SummaryBookieProHisService summaryBookieProHisService;
	
	@Autowired
	MergeUtil mergeUtil;
	
	@Override
	public Map<String, Object> queryAll(String year,String month,String day) throws Exception {
		Map<String, Object> models = new HashMap<>();
		
		List<OrderTicketDetailFinancialModel> modelList =new ArrayList<OrderTicketDetailFinancialModel>();
		
		OrderTicketDetailFinancialModel modelToday = getTotalModel(getToday(year, month, day));
		modelToday.setDate(year+"-"+month+"-"+day);
		
		OrderTicketDetailFinancialModel modelWeek = getTotalModel(getTimesWeek(year, month, day));
		modelWeek.setDate("本周截止到今日"+year+month+day);
		OrderTicketDetailFinancialModel modelMonth = getTotalModel(getTimesMonth(year, month, day));
		modelMonth.setDate("本月截止到今日"+year+month+day);
		OrderTicketDetailFinancialModel modelYear = getTotalModel(getCurrentYear(year, month, day));
		modelYear.setDate("本年截止到今日"+year+month+day);
	
		for (String monthParam : months) {
			 Calendar cal = Calendar.getInstance();
			 int nowMonth=(cal.get(Calendar.MONTH)+1);
			 if(Integer.valueOf(monthParam)> nowMonth){
				break;
			 }
			 
			OrderTicketDetailFinancialModel model = getTotalModel(getMonth(year, monthParam));
			model.setDate(monthParam+"月");
			modelList.add(model);
		}
		
		
		OrderTicketDetailFinancialModel total = getTotalModel(getYear(year, month, day));
		total.setDate("合计Total");
		
		models.put("modelToday", modelToday);
		models.put("modelWeek", modelWeek);
		models.put("modelMonth", modelMonth);
		models.put("modelYear", modelYear);
		models.put("modelList", modelList);
		models.put("total", total);
		
		return models;
	}	
	
	
	public OrderTicketDetailFinancialModel   getTotalModel (Map<String,Object> params) throws Exception{
		OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel();
	
		OrderTicketDetailFinancialModel modelCur= summaryBookieProService.queryAllModel(params);
		OrderTicketDetailFinancialModel modelHis = summaryBookieProHisService.queryAllModel(params);
		model = (OrderTicketDetailFinancialModel) mergeUtil.merge(modelCur, modelHis, model);
	
	
	calculatePayoutRate(model);
	return model;
}


//计算payoutrate
		public OrderTicketDetailFinancialModel calculatePayoutRate(OrderTicketDetailFinancialModel model){
			Double totalPrice = model.getTotalPrice();
			Double invest = model.getTotalInvestment();
			Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
			model.setTotalPrice(NumberUtil.getNumberAccordingToPercision(totalPrice, 3));
			model.setInvest(NumberUtil.getNumberAccordingToPercision(invest, 3));
			model.setPayoutRate(payoutRate);
	
			return model;
		}
		


@Override
public Response summaryExcel(String year, String month, String day,HttpServletResponse response) {
	Response res =new Response();
	try {
		Map<String, Object> map = queryAll(year, month, day);
		
		List<OrderTicketDetailFinancialModel> modelList = (List<OrderTicketDetailFinancialModel>) map.get("modelList");
		OrderTicketDetailFinancialModel modelToday = (OrderTicketDetailFinancialModel) map.get("modelToday");
		OrderTicketDetailFinancialModel modelWeek = (OrderTicketDetailFinancialModel) map.get("modelWeek");
		OrderTicketDetailFinancialModel modelMonth =(OrderTicketDetailFinancialModel) map.get("modelMonth");
		OrderTicketDetailFinancialModel modelYear = (OrderTicketDetailFinancialModel) map.get("modelYear");
		OrderTicketDetailFinancialModel total = (OrderTicketDetailFinancialModel) map.get("total");
		
		List<OrderTicketDetailFinancialModel> list = new ArrayList<>();
		Resource resource = new ClassPathResource("/excel/bookie-summary.xls");
		InputStream in = resource.getInputStream();
	
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(modelToday);
		list.add(modelWeek);
		list.add(modelMonth);
		list.add(modelYear);
		list.add(null);
		list.addAll(modelList);
		list.add(total);
		String[] strKeyArray = new String[] {"date","num", "invest","totalPrice", "payoutRate"};
		
		HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, OrderTicketDetailFinancialModel.class);
		PoiUtil.returnExcel(response, workbook, "summary");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
}  

//根据月份
 public Map<String, Object>  getMonth(String year,String month) throws Exception{
	 Map<String, Object> params = new HashMap<>();
	 Date date =  DateUtil.parse(year+ "-" + month + "-01");
	 String startDate = DateUtil.formatDate(date);
	
	 Calendar calendar = new GregorianCalendar();
	 calendar.setTime(DateUtils.parseDate(startDate ,new String[] { "yyyy-MM-dd" }));
	 calendar.add(Calendar.MONTH, 1);
	 String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
        
	params.put("startDate",startDate+" 14:00:00" );
    params.put("endDate", endDate);
	 return params;
	
}
 
 
//全年
 public Map<String, Object> getYear(String year,String month,String day) throws Exception{
	   Map<String, Object> params = new HashMap<>();
	   Date date = DateUtil.parse(year + "-01-01");
	   
	   	String  startDate = DateUtil.formatDate(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(DateUtils.parseDate(startDate, new String[] { "yyyy-MM-dd" }));
		calendar.add(Calendar.YEAR, 1);
		
		String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
        params.put("startDate",startDate +" 14:00:00");
        params.put("endDate", endDate);
	return params;
} 

//当日
public Map<String, Object> getToday(String year,String month,String day) throws Exception{
  Map<String, Object> params = new HashMap<>();
    Date date = DateUtil.parse(year + "-" + month + "-" + day);
	   
   	String  startDate = DateUtil.formatDate(date);
    Calendar calendar = new GregorianCalendar();
	calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd" }));
	calendar.add(Calendar.DATE, 1);
	String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
   params.put("startDate",startDate  + " 14:00:00");
   params.put("endDate", endDate);
return params;
}

//本周截止到今日 周一的14点到今日下一天的14点
public  Map<String, Object> getTimesWeek(String year,String month,String day) throws Exception {  
Map<String, Object> params =new HashMap<>();

	Date date = DateUtil.parse(year + "-" + month + "-" + day);

   Calendar cal = Calendar.getInstance();  
   cal.setTime(date);  
   //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
   int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
   if(1 == dayWeek) {  
     cal.add(Calendar.DAY_OF_MONTH, -1);  

   }  
   cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  

   int day1 = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
   cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day1);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
   String startDate = DateUtil.formatDate(cal.getTime())+" 14:00:00"; //周一时间 

Calendar calendar = new GregorianCalendar();
calendar.setTime(DateUtils.parseDate(DateUtil.formatDate(date),new String[] { "yyyy-MM-dd" }));
calendar.add(Calendar.DATE, 1);
String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";

params.put("startDate",startDate );
params.put("endDate",  endDate);

return params;  
}  

//本月截止到今日
public   Map<String, Object> getTimesMonth(String year,String month,String day) throws Exception { 

 Date date1 = DateUtil.parse(year + "-" + month + "-" + "01");
 String startDate =DateUtil.formatDate(date1)+" 14:00:00";
  Map<String, Object> params = new HashMap<>();
  
  Date date = DateUtil.parse(year + "-" + month + "-" + day);
  
 Calendar calendar = new GregorianCalendar();
	calendar.setTime(DateUtils.parseDate(DateUtil.formatDate(date),new String[] { "yyyy-MM-dd" }));
	calendar.add(Calendar.DATE, 1);
	String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
  
  
   params.put("startDate", startDate);
   params.put("endDate", endDate );
 
   
   return params;  
}  

//本年截止到今日
public   Map<String, Object> getCurrentYear(String year,String month,String day) throws Exception {  
   Calendar cal = Calendar.getInstance();  
   cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
   cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.YEAR)); 
   Map<String, Object> params = new HashMap<>();
   Date date = DateUtil.parse(year + "-" + month + "-" + day);
   
   Calendar calendar = new GregorianCalendar();
	calendar.setTime(DateUtils.parseDate(DateUtil.formatDate(date),new String[] { "yyyy-MM-dd" }));
	calendar.add(Calendar.DATE, 1);
	String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
  
   params.put("startDate", DateUtil.formatDate(cal.getTime())+" 14:00:00");
   params.put("endDate", endDate);
   return params;  
}

}
