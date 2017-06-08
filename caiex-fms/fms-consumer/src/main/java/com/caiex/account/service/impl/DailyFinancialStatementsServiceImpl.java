package com.caiex.account.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;

import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.caiex.account.mapper.OrderTicketDetailMapper;
import com.caiex.account.model.OrderTicketDetailFinancialModel;
import com.caiex.account.model.OrderTicketDetailSGLModel;
import com.caiex.account.service.DailyFinancialStatementsService;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;
import com.esotericsoftware.minlog.Log;

@Service
public class DailyFinancialStatementsServiceImpl implements DailyFinancialStatementsService{

	
	@Autowired 
	private OrderTicketDetailHisMapper orderTicketDetailHisMapper;
	
	@Autowired
	private OrderTicketDetailMapper orderTicketDetailMapper;
	
	String [] months= {"1","2","3","4","5","6","7","8","9","10","11","12"};
	
	@Override
	public Map<String, Object> queryAll(String year,String month,String day) throws Exception {
		Map<String, Object> models = new HashMap<>();
		
		List<OrderTicketDetailFinancialModel> modelList =new ArrayList<OrderTicketDetailFinancialModel>();
		
		OrderTicketDetailFinancialModel modelToday = queryAllModel(getToday(year, month, day));
		modelToday.setColumn1(year+"-"+month+"-"+day);
		OrderTicketDetailFinancialModel modelWeek = queryAllModel(getTimesWeek(year, month, day));
		modelWeek.setColumn1("本周截止到今日");
		OrderTicketDetailFinancialModel modelMonth = queryAllModel(getTimesMonth(year, month, day));
		modelMonth.setColumn1("本月截止到今日");
		OrderTicketDetailFinancialModel modelYear = queryAllModel(getCurrentYear(year, month, day));
		modelYear.setColumn1("本年截止到今日");
	
		for (String monthParam : months) {
			OrderTicketDetailFinancialModel model = queryAllModel(getMonth(year, monthParam));
			model.setColumn1(monthParam+"月");
			modelList.add(model);
		}
		OrderTicketDetailFinancialModel total = queryAllModel(getYear(year, month, day));
		total.setColumn1("合计Total");
		
		models.put("modelToday", modelToday);
		models.put("modelWeek", modelWeek);
		models.put("modelMonth", modelMonth);
		models.put("modelYear", modelYear);
		models.put("modelList", modelList);
		models.put("total", total);
		
		return models;
	}	
	
	
	
	 public OrderTicketDetailFinancialModel queryAllModel(Map<String, Object> params){		
		
		 Integer num=orderTicketDetailHisMapper .queryMatchNum(params);// 根据时间统计场次数量
		 num= num.equals("")? 0: num;
						
		//统计每日
		    OrderTicketDetail ticketTotalInvest = orderTicketDetailMapper.queryTotalInvestment(params);
		    Double totalInvestment = ticketTotalInvest == null ? 0.0 :ticketTotalInvest.getInv_allup();
		    OrderTicketDetail  ticketInvest  = orderTicketDetailMapper.queryInvest(params);
			Double invest= ticketInvest == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvest.getPrice_allup(),3);
			
			OrderTicketDetail  ticketTotalPrice =  orderTicketDetailMapper.queryTotalprice(params);
			Double totalPrice = ticketTotalPrice == null ? 0.0 :ticketTotalPrice.getTotal_price();
			Double payoutRate = invest.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
			
		//串关	
			OrderTicketDetail ticketTotalInvestAllup= orderTicketDetailMapper.queryTotalInvestmentALLUP(params);
			Double totalInvestmentAllup = ticketTotalInvestAllup == null ? 0.0 :ticketTotalInvestAllup.getInv_allup();  
			
			OrderTicketDetail  ticketInvestAllup  = orderTicketDetailMapper.queryinvestALLUP(params);
			Double investAllup= ticketInvestAllup == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvestAllup.getPrice_allup(),3);
			
			OrderTicketDetail  ticketTotalPriceAllup =  orderTicketDetailMapper.queryTotalPriceALLUP(params);
			Double totalPriceAllup=ticketTotalPriceAllup == null ? 0.0 :ticketTotalPriceAllup.getTotal_price();
			Double payoutRateAllup = investAllup.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPriceAllup - investAllup) / investAllup * 100, 3);		
				
	   //单关		
			OrderTicketDetail ticketTotalInvestFsgl = orderTicketDetailMapper.queryTotalInvestmentFSGL(params);
			Double totalInvestmentFsgl= ticketTotalInvestFsgl == null ?0.0 :ticketTotalInvestFsgl.getInv_allup();
			OrderTicketDetail  ticketInvestFsgl  = orderTicketDetailMapper.queryinvestFSGL(params);
			Double investFsgl= ticketInvestFsgl == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvestFsgl.getPrice_allup(), 3);
			OrderTicketDetail  ticketTotalPriceFsgl =  orderTicketDetailMapper.queryTotalPriceFSGL(params);
			Double totalPriceFsgl=ticketTotalPriceFsgl == null ? 0.0 :ticketTotalPriceFsgl.getTotal_price();
			Double payoutRateFsgl = investFsgl.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPriceFsgl - investFsgl) / investFsgl * 100, 3);
			
	//0关		
			OrderTicketDetail ticketTotalInvestLevel0 = orderTicketDetailMapper.queryTotalInvestmentLEVEL0(params);
			Double totalInvestmentLevel0 = ticketTotalInvestLevel0 == null ?0.0 :ticketTotalInvestLevel0.getInv_allup();
			OrderTicketDetail  ticketInvestLevel0 = orderTicketDetailMapper.queryinvestLEVEL0(params);
			Double investLevel0= ticketInvestLevel0 == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvestLevel0.getPrice_allup(), 3);
			Double totalPriceLevel0=ticketInvestLevel0== null ? 0.0 :ticketInvestLevel0.getTotal_price();
			Double payoutRateLevel0 = investLevel0.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPriceLevel0 - investLevel0) / investLevel0 * 100, 3);
			
			OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel(null, num, null, totalInvestment, invest, totalPrice, payoutRate, totalInvestmentAllup, investAllup, totalPriceAllup, payoutRateAllup, totalInvestmentFsgl, investFsgl, totalPriceFsgl, payoutRateFsgl, null, null, totalInvestmentLevel0, investLevel0, totalPriceLevel0, payoutRateLevel0);
		    
			return model;
			
		}
	
	//根据月份
		 public Map<String, Object>  getMonth(String year,String month){
			 Map<String, Object> params = new HashMap<>();
			 int yer = Integer.parseInt(year);
			 int num = Integer.parseInt(month);
			 if ((yer %4==0)&&(yer%100!=0)||(yer%400==0) && num==2){//闰年二月判断
				  params.put("startDate", year + "-" + month + "-" +"01"+" 00:00:00");
			      params.put("endDate",year + "-" + month + "-" +"29"+" 00:00:00");
			 }

			 if( num == 1 || num ==3 || num ==5 || num == 7|| num == 8 || num == 10 || num ==12){
			        params.put("startDate", year + "-" + month + "-" +"01"+" 00:00:00");
			        params.put("endDate",year + "-" + month + "-" +"31"+" 00:00:00");
			 }else if(num == 2){
				   params.put("startDate", year + "-" + month + "-" +"01"+" 00:00:00");
			        params.put("endDate",year + "-" + month + "-" +"28"+" 00:00:00");
			 }else{
				 params.put("startDate", year + "-" + month + "-" +"01"+" 00:00:00");
			     params.put("endDate",year + "-" + month + "-" +"30"+" 00:00:00");
			 }	 
			 return params;
			
		}
		 
		 
	//全年
		 public Map<String, Object> getYear(String year,String month,String day) throws ParseException{
			   Map<String, Object> params = new HashMap<>();
			   String  startDate = year + "-01-01 00:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
				calendar.add(Calendar.YEAR, 1);
				String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
		        params.put("startDate",startDate );
		        params.put("endDate", endDate);
			return params;
		} 
	
	//当日
	public Map<String, Object> getToday(String year,String month,String day) throws ParseException{
		  Map<String, Object> params = new HashMap<>();
		    String startDate = year + "-" + month + "-" + day + " 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.DATE, 1);
			String endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
	        params.put("startDate",startDate );
	        params.put("endDate", endDate);
		return params;
	}
	
	//本周截止到今日
	public  Map<String, Object> getTimesWeek(String year,String month,String day) {  
		Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        params.put("startDate", sdf.format(cal.getTime())+" 00:00:00");
        params.put("endDate", year + "-" + month + "-" + day + " 24:00:00");
        return params;  
    }  
  
	//本月截止到今日
	 public   Map<String, Object> getTimesMonth(String year,String month,String day) {  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	        Map<String, Object> params = new HashMap<>();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        
	        params.put("startDate", sdf.format(cal.getTime())+" 00:00:00");
	        params.put("endDate", year + "-" + month + "-" + day + " 24:00:00");
	        
	        Log.info("startDate"+sdf.format(cal.getTime())+" 00:00:00");
	        Log.info("endDate"+ year + "-" + month + "-" + day + " 24:00:00");
	        return params;  
	    }  
	
	//本年截止到今日
	 public   Map<String, Object> getCurrentYear(String year,String month,String day) {  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.YEAR)); 
	        Map<String, Object> params = new HashMap<>();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        
	        params.put("startDate", sdf.format(cal.getTime())+" 00:00:00");
	        params.put("endDate", year + "-" + month + "-" + day + " 24:00:00");
	       
	        Log.info("startDate"+sdf.format(cal.getTime())+" 00:00:00");
	        Log.info("endDate"+ year + "-" + month + "-" + day + " 24:00:00");
	        return params;  
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
			String strTemplate = "/opt/FMS/consumer/template/summary-demo.xls";// 模板位置
			
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
			String[] strKeyArray = new String[] {"column1","column2","totalInvestment", "invest", "totalPrice", "payoutRate","totalInvestmentAllup", "investAllup", "totalPriceAllup", "payoutRateAllup","totalInvestmentFsgl", "investFsgl", "totalPriceFsgl","payoutRateFsgl","totalInvestmentLevel0", "investLevel0","totalPriceLevel0","payoutRateLevel0"};
			
			HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, OrderTicketDetailFinancialModel.class);
			PoiUtil.returnExcel(response, workbook, "summary");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			return res;
	}  
	
	
}
