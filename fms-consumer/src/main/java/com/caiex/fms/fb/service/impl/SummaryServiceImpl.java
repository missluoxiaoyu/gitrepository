package com.caiex.fms.fb.service.impl;


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
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.caiex.dbservice.basketball.api.BkSummaryProService;
import com.caiex.dbservice.currentdb.api.SummaryProService;
import com.caiex.dbservice.historydb.api.BkSummaryProHisService;
import com.caiex.dbservice.historydb.api.SummaryProHisService;
import com.caiex.dbservice.model.OrderTicketDetailFinancialModel;
import com.caiex.dbservice.utils.DateUtil;
import com.caiex.fms.fb.service.SummaryService;
import com.caiex.fms.redis.RedisUtil;
import com.caiex.fms.utils.MergeUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;
import com.esotericsoftware.minlog.Log;
import com.mysql.fabric.xmlrpc.base.Array;


@Service
public class SummaryServiceImpl implements SummaryService{
	private final static Logger log = Logger.getLogger(SummaryServiceImpl.class);
	
	
	@Autowired
	private SummaryProService summaryProService;
	
	@Autowired
	private SummaryProHisService summaryProHisService;
	
	@Autowired
	private BkSummaryProService bkSummaryProService;
	
	@Autowired
	private BkSummaryProHisService bkSummaryProHisService;
	
	@Autowired
	private RedisUtil redisDao;
	
	
	@Autowired
	MergeUtil mergeUtil;
	
	String [] months= {"1","2","3","4","5","6","7","8","9","10","11","12"};
	//map里面的key
	String [] keys={"modelToday","modelWeek","modelMonth","modelYear","total","modelList"};
	

	
	
	@Override
	public Map<String, Object> queryAll(String year,String month,String day,String pid) throws Exception {
		Map<String, Object> models = new HashMap<>();
		
		List<OrderTicketDetailFinancialModel> modelList =new ArrayList<OrderTicketDetailFinancialModel>();
		
		OrderTicketDetailFinancialModel modelToday = getTotalModel(getToday(year, month, day),pid);
		modelToday.setDate(year+"-"+month+"-"+day);
		
		OrderTicketDetailFinancialModel modelWeek = getTotalModel(getTimesWeek(year, month, day),pid);
		modelWeek.setDate("本周截止到今日"+year+month+day);
		OrderTicketDetailFinancialModel modelMonth = getTotalModel(getTimesMonth(year, month, day),pid);
		modelMonth.setDate("本月截止到今日"+year+month+day);
		OrderTicketDetailFinancialModel modelYear = getTotalModel(getCurrentYear(year, month, day),pid);
		modelYear.setDate("本年截止到今日"+year+month+day);
	
		for (String monthParam : months) {
			 Calendar cal = Calendar.getInstance();
			 int nowMonth=(cal.get(Calendar.MONTH)+1);
			 if(Integer.valueOf(monthParam)> nowMonth){
				break;
			 }
			 
			OrderTicketDetailFinancialModel model = getTotalModel(getMonth(year, monthParam),pid);
			model.setDate(monthParam+"月");
			modelList.add(model);
		}
		
		
		OrderTicketDetailFinancialModel total = getTotalModel(getYear(year, month, day),pid);
		total.setDate("合计Total");
		
		models.put("modelToday", modelToday);
		models.put("modelWeek", modelWeek);
		models.put("modelMonth", modelMonth);
		models.put("modelYear", modelYear);
		models.put("modelList", modelList);
		models.put("total", total);
		
		return models;
	}	
	
	
	public OrderTicketDetailFinancialModel   getTotalModel (Map<String,Object> params,String pid) throws Exception{
			OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel();
		
		if(pid.equals("1")){//查足球
			
			OrderTicketDetailFinancialModel modelCur= summaryProService.queryAllModel(params);
			OrderTicketDetailFinancialModel modelHis = summaryProHisService.queryAllModel(params);
			model = (OrderTicketDetailFinancialModel) mergeUtil.merge(modelCur, modelHis, model);
		}else if(pid.equals("2")){//查篮球
			OrderTicketDetailFinancialModel basmodel= bkSummaryProService.queryAllModel(params);
			OrderTicketDetailFinancialModel basmodelhis= bkSummaryProHisService.queryAllModel(params);
			model = (OrderTicketDetailFinancialModel) mergeUtil.merge(basmodel, basmodelhis, model);
		
		}else{//查所有
			OrderTicketDetailFinancialModel fmodel = new OrderTicketDetailFinancialModel();
			OrderTicketDetailFinancialModel modelCur= summaryProService.queryAllModel(params);
			OrderTicketDetailFinancialModel modelHis = summaryProHisService.queryAllModel(params);
			fmodel = (OrderTicketDetailFinancialModel) mergeUtil.merge(modelCur, modelHis, fmodel);
			
			OrderTicketDetailFinancialModel bmodel = new OrderTicketDetailFinancialModel();
			OrderTicketDetailFinancialModel basmodel= bkSummaryProService.queryAllModel(params);
			OrderTicketDetailFinancialModel basmodelhis= bkSummaryProHisService.queryAllModel(params);
			bmodel = (OrderTicketDetailFinancialModel) mergeUtil.merge(basmodel, basmodelhis, bmodel);
			
			model= (OrderTicketDetailFinancialModel) mergeUtil.merge(fmodel, bmodel, model);
		}
		
		
		calculatePayoutRate(model);
		return model;
	}
	
	
	//计算payoutrate
			public OrderTicketDetailFinancialModel calculatePayoutRate(OrderTicketDetailFinancialModel model){
				Double totalPrice = model.getTotalPrice();
				Double invest = model.getInvest();
				Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
				model.setTotalPrice(NumberUtil.getNumberAccordingToPercision(totalPrice, 3));
				model.setInvest(NumberUtil.getNumberAccordingToPercision(invest, 3));
				model.setPayoutRate(payoutRate);
				model.setBonusInvest(NumberUtil.getNumberAccordingToPercision(model.getBonusInvest(), 3));
				
				Double totalPriceAllup = model.getTotalPriceAllup();
				Double investAllup = model.getInvestAllup();
				Double payoutRateAllup = investAllup==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceAllup - investAllup) / investAllup * 100, 3);
				model.setTotalPriceAllup(NumberUtil.getNumberAccordingToPercision(totalPriceAllup, 3));
				model.setInvestAllup(NumberUtil.getNumberAccordingToPercision(investAllup, 3));
				model.setPayoutRateAllup(payoutRateAllup);
				model.setBonusInvestAllup(NumberUtil.getNumberAccordingToPercision(model.getBonusInvestAllup(), 3));
				
				Double totalPriceFsgl = model.getTotalPriceFsgl();
				Double investFsgl = model.getInvestFsgl();
				Double payoutRateFsgl = investFsgl==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceFsgl - investFsgl) / investFsgl * 100, 3);
				model.setTotalPriceFsgl(NumberUtil.getNumberAccordingToPercision(totalPriceFsgl, 3));
				model.setInvestFsgl(NumberUtil.getNumberAccordingToPercision(investFsgl, 3));
				model.setPayoutRateFsgl(payoutRateFsgl);
				model.setBonusInvestFsgl(NumberUtil.getNumberAccordingToPercision(model.getBonusInvestFsgl(), 3));
				
				Double totalPriceLevel0 = model.getTotalPriceLevel0();
				Double investLevel0 = model.getInvestLevel0();
				Double payoutRateLevel0 = investLevel0==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceLevel0- investLevel0) / investLevel0 * 100, 3);
				model.setTotalPriceLevel0(NumberUtil.getNumberAccordingToPercision(totalPriceLevel0, 3));
				model.setInvestLevel0(NumberUtil.getNumberAccordingToPercision(investLevel0, 3));
				model.setPayoutRateLevel0(payoutRateLevel0);
				model.setBonusInvestLevel0(NumberUtil.getNumberAccordingToPercision(model.getBonusInvestLevel0(), 3));
				
				return model;
			}
			


	@Override
	public Response summaryExcel(String year, String month, String day,HttpServletResponse response,String pid) {
		Response res =new Response();
		try {
			Map<String, Object> map = query(year, month, day,pid);
			
			List<OrderTicketDetailFinancialModel> modelList = (List<OrderTicketDetailFinancialModel>) map.get("modelList");
			OrderTicketDetailFinancialModel modelToday = (OrderTicketDetailFinancialModel) map.get("modelToday");
			OrderTicketDetailFinancialModel modelWeek = (OrderTicketDetailFinancialModel) map.get("modelWeek");
			OrderTicketDetailFinancialModel modelMonth =(OrderTicketDetailFinancialModel) map.get("modelMonth");
			OrderTicketDetailFinancialModel modelYear = (OrderTicketDetailFinancialModel) map.get("modelYear");
			OrderTicketDetailFinancialModel total = (OrderTicketDetailFinancialModel) map.get("total");
			
			List<OrderTicketDetailFinancialModel> list = new ArrayList<>();
			Resource resource = new ClassPathResource("/excel/summary-demo.xls");
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
			String[] strKeyArray = new String[] {"date","column2","totalInvestment", "invest", "bonusInvest","totalPrice", "payoutRate","totalInvestmentAllup", "investAllup", "bonusInvestAllup","totalPriceAllup", "payoutRateAllup","totalInvestmentFsgl", "investFsgl","bonusInvestFsgl", "totalPriceFsgl","payoutRateFsgl","totalInvestmentLevel0", "investLevel0","bonusInvestLevel0","totalPriceLevel0","payoutRateLevel0"};
			
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
  
   log.info("本周"+startDate+"截止到"+endDate);
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
     
       log.info("本月"+startDate+"截止到"+endDate);
       
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
       log.info("本年"+DateUtil.formatDate(cal.getTime())+" 14:00:00"+"截止到"+endDate);
       return params;  
   }


	@Override
	public void addAllInfo(String year,String month,String day,String pid) throws Exception{
		
		Map<String, Object> map = queryAll(year, month, day, pid);
		OrderTicketDetailFinancialModel modelToday = (OrderTicketDetailFinancialModel) map.get("modelToday");
		OrderTicketDetailFinancialModel modelWeek = (OrderTicketDetailFinancialModel) map.get("modelWeek");
		OrderTicketDetailFinancialModel modelMonth = (OrderTicketDetailFinancialModel) map.get("modelMonth");
		OrderTicketDetailFinancialModel modelYear = (OrderTicketDetailFinancialModel) map.get("modelYear");
		OrderTicketDetailFinancialModel total = (OrderTicketDetailFinancialModel) map.get("total");
		List<OrderTicketDetailFinancialModel> modelList =  (List<OrderTicketDetailFinancialModel>) map.get("modelList");
		
		modelToday.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(modelToday);
		modelWeek.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(modelWeek);
		modelMonth.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(modelMonth);
		modelYear.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(modelYear);
		total.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(total);
		
		for (OrderTicketDetailFinancialModel orderTicketDetailFinancialModel : modelList) {
			orderTicketDetailFinancialModel.setBallType(Integer.valueOf(pid));
			summaryProHisService.addSummaryStatistics(orderTicketDetailFinancialModel);
		}
		
	}


@Override
	public void  addInfo(String year,String month,String day,String pid) throws Exception{
	
		List<String> monthParams = new ArrayList<>();
		
		for (String param : months) {
			if(Integer.valueOf(param) >=Integer.valueOf(month)-2){
				monthParams.add(param);
			}
		}
				
		
		//近两个月的数据 会不断插入表中
		for (String param : monthParams) {
			OrderTicketDetailFinancialModel model = getTotalModel(getMonth(year,param ),pid);
			model.setBallType(Integer.valueOf(pid));
			model.setDate(param+"月");
			summaryProHisService.addSummaryStatistics(model);//存入数据库
		}
		
		
		OrderTicketDetailFinancialModel modelWeek = getTotalModel(getTimesWeek(year, month, day),pid);
		modelWeek.setDate("本周截止到今日"+year+month+day);
		modelWeek.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(modelWeek);
		
		OrderTicketDetailFinancialModel modelMonth = getTotalModel(getTimesMonth(year, month, day),pid);
		modelMonth.setDate("本月截止到今日"+year+month+day);
		modelMonth.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(modelMonth);
		
		OrderTicketDetailFinancialModel modelYear = getTotalModel(getCurrentYear(year, month, day),pid);
		modelYear.setDate("本年截止到今日"+year+month+day);
		modelYear.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(modelYear);
		
		OrderTicketDetailFinancialModel total = getTotalModel(getYear(year, month, day),pid);
		total.setDate("合计Total");
		total.setBallType(Integer.valueOf(pid));
		summaryProHisService.addSummaryStatistics(total);//存入数据库
		
	}
	
	
	
	
	




	@Override
	public Map<String, Object> query(String year,String month,String day,String pid) throws Exception {
		
		OrderTicketDetailFinancialModel modelToday = getTotalModel(getToday(year, month, day),pid);
		modelToday.setDate(year+"-"+month+"-"+day);
	
		 OrderTicketDetailFinancialModel modelParam = new OrderTicketDetailFinancialModel();
		 modelParam.setBallType(Integer.valueOf(pid));
		 modelParam.setDate("本周截止到今日"+year+month+day);
		 OrderTicketDetailFinancialModel modelWeek =summaryProHisService.queryAll(modelParam);
		modelParam.setDate("本月截止到今日"+year+month+day);
		OrderTicketDetailFinancialModel modelMonth =summaryProHisService.queryAll(modelParam);
		modelParam.setDate("本年截止到今日"+year+month+day);
		OrderTicketDetailFinancialModel modelYear = summaryProHisService.queryAll(modelParam);
		 
		List<OrderTicketDetailFinancialModel> modelList = new ArrayList<>();
		for (String monthParam : months) {
			 Calendar cal = Calendar.getInstance();
			 int nowMonth=(cal.get(Calendar.MONTH)+1);
			 if(Integer.valueOf(monthParam)> nowMonth){
				break;
			 }
			 OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel();
			 model.setBallType(Integer.valueOf(pid));
			 model.setDate(monthParam+"月");
			 OrderTicketDetailFinancialModel modell =summaryProHisService.queryAll(model);
			 modelList.add(modell);
		}

		
		 modelParam.setDate("合计Total");
		 OrderTicketDetailFinancialModel total =summaryProHisService.queryAll(modelParam);
		
	    modelWeek.setDate("本周截止到今日");
	    modelMonth.setDate("本月截止到今日"); 
	    modelYear.setDate("本年截止到今日");
		 
		Map<String, Object> map = new HashMap<>();
		map.put("modelToday", modelToday);
		map.put("modelWeek", modelWeek);
		map.put("modelMonth", modelMonth);
		map.put("modelYear", modelYear);
		map.put("modelList", modelList);
		map.put("total", total);
		
		return map;
	}









}
