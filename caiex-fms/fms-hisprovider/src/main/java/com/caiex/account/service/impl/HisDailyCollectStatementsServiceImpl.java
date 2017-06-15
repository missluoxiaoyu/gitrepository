package com.caiex.account.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.stereotype.Service;

import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.caiex.account.model.OrderTicketDetailSummaryModel;
import com.caiex.account.model.SummaryTitle;
import com.caiex.account.util.NumberUtil;
import com.fms.api.DailyCollectStatementsService;
import com.fms.api.HisDailyCollectStatementsService;
import com.fms.api.OrderTicketDetailService;
import com.fms.entity.OrderTicketDetail;


@Service("hisDailyCollectStatementsService")
public class HisDailyCollectStatementsServiceImpl implements HisDailyCollectStatementsService{

	@Autowired
	private OrderTicketDetailHisMapper orderTicketDetailMapper;
	
	@Override
	public   Map<String, Object> queryAll(String year,String month) throws Exception {
		Map<String, Object> models = new HashMap<String, Object>();
		models = getModel(year, month, getDays(year, month));
		
		OrderTicketDetailSummaryModel monthTotal = queryAllModel(getTotalMonth(year, month));
		monthTotal.setWeek("本月合计");
		models.put("monthTotal", monthTotal);
		
		return models;
	}
	
	//每月合计的时间参数
	public Map<String ,Object> getTotalMonth(String year,String month) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String s1 = year+"-"+month+"-"+"1";
	    String s2 = year+"-"+month+"-"+getDays(year, month);
	    Date date1=sdf.parse(s1);
	    Date date2=sdf.parse(s2);
		String startDate = sdf.format(date1);
		String endDate	= sdf.format(date2);
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return params;
	}

	
	//每周合计的时间参数
	public Map<String ,Object> getWeek(String startDate,String endDate){
		Map<String, Object> params = new HashMap<String, Object>();
		String startdate = startDate + " 00:00:00";
		String enddate = endDate + " 24:00:00";
		params.put("startDate", startdate);
		params.put("endDate", enddate);
		
		return params;
		
	}
	//每天的时间参数
	public Map<String, Object>  getOneDay(String someDay) throws Exception{
		 String startDate=null;
		 String endDate=null;
		Map<String, Object> params = new HashMap<String, Object>();
	
	   	startDate = someDay+" 12:00:00";
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
		calendar.add(Calendar.DATE, 1);
		endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 12:00:00";
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		
		return params;
	}
	

	 public OrderTicketDetailSummaryModel queryAllModel(Map<String, Object> params){
		 Integer num =0;
		 /*=orderTicketDetailMapper .queryMatchNum(params);// 根据时间统计场次数量
		 num= num.equals("")? 0: num;*/
		 
		//统计每日
		    OrderTicketDetail ticketTotalInvest = orderTicketDetailMapper.queryTotalInvestment(params);
		    Double totalInvestment = ticketTotalInvest == null ? 0.0 :ticketTotalInvest.getInv_allup();
		    OrderTicketDetail  ticketInvest  = orderTicketDetailMapper.queryInvest(params);
			Double invest= ticketInvest == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvest.getPrice_allup(),3);
			
			OrderTicketDetail  ticketTotalPrice =  orderTicketDetailMapper.queryTotalprice(params);
			Double totalPrice = ticketTotalPrice == null ? 0.0 :ticketTotalPrice.getTotal_price();
			//Double payoutRate = invest.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
			
			OrderTicketDetailSummaryModel model = new OrderTicketDetailSummaryModel(null, num, totalInvestment, invest, totalPrice, null, num, totalInvestment, invest, totalPrice, null, null, null, null, null, null);
			return model;
	 }
    
	 //获取每月天数
	 public Integer  getDays(String year,String month){
		 Map<String, Object> params = new HashMap<>();
		 int yer = Integer.parseInt(year);
		 int num = Integer.parseInt(month);
		 Integer day=0;
		 if ((yer %4==0)&&(yer%100!=0)||(yer%400==0) && num==2){//闰年二月判断
			 day=29;
		 }
		 if( num == 1 || num ==3 || num ==5 || num == 7|| num == 8 || num == 10 || num ==12){
		       day=31;
		 }else if(num == 2){
			   day=28;
		 }else{
			 	day=30;
		 }	 
		 return day;
		
	}
	
	
	
	 public   Map<String, Object> getModel(String year ,String month,Integer day) throws Exception{
	
		 Map<String, Object> allModel = new HashMap<String, Object>();
		 
		 Map<String, Object> weekModel = new HashMap<String, Object>();//用于每周的数据封装
		 Map<String, Object> models = new HashMap<String, Object>();
		
		 List<String> time = new ArrayList<>();
		 List<String> timeList = new ArrayList<String>(); 
		 int count = 0;
		
		String s = year+"-"+month;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        SimpleDateFormat sdfWeek = new SimpleDateFormat("EEE");
        for(int i = 1; i <= day; i++){
                Date date = sdf.parse(s + "-" + i);
                timeList.add(sdf.format(date) + " : " + sdfWeek.format(date));
        }

		for (int i = 0; i < timeList.size(); i++) {//将日期按照周末分割
			time.add(timeList.get(i));
			if(timeList.get(i).endsWith("日") ){
				count++;
				models.put(count+"", time);
				time = new ArrayList<>();
			}
			if(i==timeList.size()-1 && !timeList.get(i).endsWith("Sun")){//最后一周 不是周末
				models.put(count+1+"", time);
			}
		}
		 
		
		List <OrderTicketDetailSummaryModel> weekList = new ArrayList<>();
	
		for (String key : models.keySet()) {
			List <String> datas = (List<String>) models.get(key);
			
			SummaryTitle title = new SummaryTitle(null, "场次数量NO. Match", "原始投注额TotalInvestment人民币RMB", "交易额investment人民币RMB", "中间额Payout人民币RMB", "payoutRatio%",  "场次数量NO. Match", "原始投注额TotalInvestment人民币RMB", "交易额investment人民币RMB", "中间额Payout人民币RMB", "payoutRatio%", "场次数量NO. Match", "原始投注额TotalInvestment人民币RMB", "交易额investment人民币RMB", "中间额Payout人民币RMB", "payoutRatio%");
			title.setWeek("第"+key+"周");//每周的title
			weekModel.put("title", title);
			
				for (String data : datas) {
					String date = data.split(":")[0];
					Map<String, Object> params = getOneDay(date);
					OrderTicketDetailSummaryModel model = queryAllModel(params);
					model.setWeek(data);
					weekList.add(model);
				}
				
				String start = datas.get(0).split(":")[0];//周一
				String end = datas.get(datas.size()-1).split(":")[0];//周末
				Map<String, Object> params = getWeek(start, end);
				OrderTicketDetailSummaryModel total=  queryAllModel(params);
				total.setWeek("本周合计");
				weekModel.put("weekList", weekList);
				weekList = new ArrayList<>();
			
				weekModel.put("Weektotal", total);
				
				allModel.put(key, weekModel);
				weekModel=new HashMap<>();
			}
				
		
		return allModel;
       }

	
	
}
