package com.caiex.fms.fb.service.impl;


import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.caiex.dbservice.basketball.api.BkDailySummaryProService;
import com.caiex.dbservice.currentdb.api.DailySummaryProService;
import com.caiex.dbservice.historydb.api.BkDailySummaryProHisService;
import com.caiex.dbservice.historydb.api.DailySummaryProHisService;
import com.caiex.dbservice.model.OrderTicketDetailSummaryModel;
import com.caiex.dbservice.model.SummaryTitle;
import com.caiex.dbservice.utils.DateUtil;
import com.caiex.fms.fb.service.DailySummaryService;
import com.caiex.fms.utils.MergeUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;

@Service
public class DailySummaryServiceImpl implements DailySummaryService{
	private final static Logger log = Logger.getLogger(DailySummaryServiceImpl.class);
	
	
	@Autowired
	private DailySummaryProService dailyCollectStatementsService;
	
	@Autowired
	private DailySummaryProHisService dailyCollectStatementsHistoryService;
	
	@Autowired
	private BkDailySummaryProService basketBallDailyCollectStatementsService;
	
	@Autowired
	private BkDailySummaryProHisService basketBallDailyCollectStatementsHistoryService;
	
	
	@Autowired
	MergeUtil mergeUtil;

	Calendar calendar = new GregorianCalendar();
	
	
	
	
	@Override
	public Map<String, Object> queryAll(String year, String month)throws Exception {
		Map<String, Object> all = new HashMap<>();
		all =getModel(year, month, getDays(year, month));
		
		OrderTicketDetailSummaryModel monthTotal = getMonthTotal(year, month);
		mergeUtil.getNumberAccordingToPercision(monthTotal);
		calculatePayoutRate(monthTotal);
		
		all.put("monthTotal", monthTotal);
		
		return all;
	}

	
	


	public   Map<String, Object> getModel(String year ,String month,Integer day) throws Exception{	
		 Map<String, Object> allModel = new HashMap<String, Object>();
		 
		 Map<String, Object> weekModel = new HashMap<String, Object>();//用于每周的数据封装
		 Map<String, Object> models = new HashMap<String, Object>();
		
		 List<String> time = new ArrayList<>();
		 List<String> timeList = new ArrayList<String>(); 
		 int count = 0;
		
		String s = year+"-"+month;
      
        for(int i = 1; i <= day; i++){
                Date date =  DateUtil.parse(s + "-" + i);
                timeList.add(DateUtil.formatDate(date) + " : " + DateUtil.formatWeek(date));
        }

		for (int i = 0; i < timeList.size(); i++) {//将日期按照周末分割
			time.add(timeList.get(i));
			if(timeList.get(i).endsWith("日")|| timeList.get(i).endsWith("Sun")){
				count++;
				models.put(count+"", time);
				time = new ArrayList<>();
			}
			if(i==timeList.size()-1 && !(timeList.get(i).endsWith("日")|| timeList.get(i).endsWith("Sun"))){//最后一周 不是周末
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
					//足球数据合并
					OrderTicketDetailSummaryModel FbCurrent = dailyCollectStatementsService.queryAllModel(params);
					FbCurrent.setDate(date);
					FbCurrent.setWeek(data);
					OrderTicketDetailSummaryModel FbHis = dailyCollectStatementsHistoryService.queryAllModel(params);
					
					OrderTicketDetailSummaryModel Fb = new OrderTicketDetailSummaryModel();
					Fb=(OrderTicketDetailSummaryModel) mergeUtil.merge(FbCurrent, FbHis, Fb);
					mergeUtil.getNumberAccordingToPercision(Fb);//将double类型的属性 进行字符串截取
					
					
					//篮球数据合并
					OrderTicketDetailSummaryModel BkCurrent = basketBallDailyCollectStatementsService.queryAllModel(params);
					OrderTicketDetailSummaryModel BkHis = basketBallDailyCollectStatementsHistoryService.queryAllModel(params);
					OrderTicketDetailSummaryModel Bk = new OrderTicketDetailSummaryModel();
					Bk=(OrderTicketDetailSummaryModel) mergeUtil.merge(BkCurrent, BkHis, Bk);
					mergeUtil.getNumberAccordingToPercision(Bk);//将double类型的属性 进行字符串截取
				
					OrderTicketDetailSummaryModel model = getAll(Fb, Bk);
					calculatePayoutRate(model);//计算盈利率
					weekList.add(model);
					
					
				}
				
				weekModel.put("weekList", weekList);
				OrderTicketDetailSummaryModel total = getTotal(weekList);
				total.setWeek("本周合计");
				mergeUtil.getNumberAccordingToPercision(total);
				calculatePayoutRate(total);
				weekModel.put("Weektotal", total);
				weekList = new ArrayList<>();
			
				allModel.put(key, weekModel);
				weekModel=new HashMap<>();
			}
		return allModel;
      }

	
	public OrderTicketDetailSummaryModel getAll(OrderTicketDetailSummaryModel Fb,OrderTicketDetailSummaryModel Bk){
		
		Integer totalNum = Fb.getFbNum()==null?0:Fb.getFbNum();
		totalNum += Bk.getBkNum()==null?0:Bk.getBkNum();
		
		Double totalInvestment = Fb.getTotalInvestmentFb()==null?0:Fb.getTotalInvestmentFb();
		totalInvestment += Bk.getTotalInvestmentBk()==null?0:Bk.getTotalInvestmentBk();
		
		Double invest = Fb.getInvestFb()==null?0:Fb.getInvestFb();
		invest += Bk.getInvestBk()==null?0:Bk.getInvestBk();
		
		Double totalPrice = Fb.getTotalPriceFb()==null?0:Fb.getTotalPriceFb();
		totalPrice += Bk.getInvestBk()==null?0:Bk.getInvestBk();
		
		Double cxBonus = Fb.getCxBonusFb()==null?0:Fb.getCxBonusFb();
		cxBonus +=Bk.getCxBonusBk()==null?0:Bk.getCxBonusBk();
		
		OrderTicketDetailSummaryModel model = new OrderTicketDetailSummaryModel();
		model.setDate(Fb.getDate());
		model.setWeek(Fb.getWeek());
		model.setTotalNum(totalNum);
		model.setTotalInvestment(totalInvestment);
		model.setInvest(invest);
		model.setTotalPrice(totalPrice);
		model.setCxBonus(cxBonus);
		
		model.setFbNum(Fb.getFbNum()==null?0:Fb.getFbNum());
		model.setTotalInvestmentFb(Fb.getTotalInvestmentFb()==null?0:Fb.getTotalInvestmentFb());
		model.setInvestFb(Fb.getInvestFb()==null?0:Fb.getInvestFb());
		model.setTotalPriceFb(Fb.getTotalPriceFb()==null?0:Fb.getTotalPriceFb());
		model.setCxBonusFb(Fb.getCxBonusFb()==null?0:Fb.getCxBonusFb());
		
		
		model.setBkNum(Bk.getBkNum()==null?0:Bk.getBkNum());
		model.setInvestBk(Bk.getInvestBk()==null?0:Bk.getInvestBk());
		model.setTotalInvestmentBk(Bk.getTotalInvestmentBk()==null?0:Bk.getTotalInvestmentBk());
		model.setTotalPriceBk(Bk.getTotalPriceBk()==null?0:Bk.getTotalPriceBk());
		model.setCxBonusBk(Bk.getCxBonusBk()==null?0:Bk.getCxBonusBk());
		
		
		return model;
	}
	
	
	
	
	
	
	public OrderTicketDetailSummaryModel getTotal(List <OrderTicketDetailSummaryModel> weekList ) throws ParseException{
		
		
		  Integer totalNum = 0;
		  Double totalInvestment =0.0;
		  Double invest = 0.0;
		  Double totalPrice=0.0;
		  Double cxBonus = 0.0;
		  
		  
		  Integer FbNum = 0;
		  Double totalInvestmentFb =0.0;
		  Double investFb = 0.0;
		  Double totalPriceFb=0.0;
		  Double cxBonusFb = 0.0;
		  
		  Integer BkNum = 0;
		  Double totalInvestmentBk =0.0;
		  Double investBk = 0.0;
		  Double totalPriceBk=0.0;
		  Double cxBonusBk = 0.0;
		 
		for (OrderTicketDetailSummaryModel model : weekList) {
			totalNum += model.getTotalNum();
			totalInvestment +=model.getTotalInvestment();
			invest +=model.getInvest();
			totalPrice+=model.getTotalPrice();
			cxBonus +=model.getCxBonus();
			
			FbNum += model.getFbNum();
			totalInvestmentFb += model.getTotalInvestmentFb();
			investFb += model.getInvestFb();
			totalPriceFb += model.getTotalPriceFb();
			cxBonusFb +=model.getCxBonusFb();
			
			BkNum +=model.getBkNum();
			totalInvestmentBk +=model.getTotalInvestmentBk();
			investBk += model.getInvestBk();
			totalPriceBk +=model.getTotalPriceBk();
			cxBonusBk += model.getCxBonusBk();
			
		}
	
		
		
		OrderTicketDetailSummaryModel total = new OrderTicketDetailSummaryModel();
		total.setWeek("本周合计");
		total.setTotalNum(totalNum);
		total.setTotalInvestment(totalInvestment);
		total.setInvest(invest);
		total.setTotalPrice(totalPrice);
		total.setCxBonus(cxBonus);
		
		
		total.setFbNum(FbNum);
		total.setTotalInvestmentFb(totalInvestmentFb);
		total.setInvestFb(investFb);
		total.setTotalPriceFb(totalPriceFb);
		total.setCxBonusFb(cxBonusFb);
		
		total.setBkNum(BkNum);
		total.setInvestBk(investBk);
		total.setTotalInvestmentBk(totalInvestmentBk);
		total.setTotalPriceBk(totalPriceBk);
		total.setCxBonusBk(cxBonusBk);
		
		return total;
	}
	
	
	public OrderTicketDetailSummaryModel getMonthTotal(String year, String month) throws Exception{
		
		
		  Integer totalNum = 0;
		  Double totalInvestment =0.0;
		  Double invest = 0.0;
		  Double totalPrice=0.0;
		  Double cxBonus = 0.0;
		  
		  Integer FbNum = 0;
		  Double totalInvestmentFb =0.0;
		  Double investFb = 0.0;
		  Double totalPriceFb=0.0;
		  Double cxBonusFb = 0.0;
		  
		  Integer BkNum = 0;
		  Double totalInvestmentBk =0.0;
		  Double investBk = 0.0;
		  Double totalPriceBk=0.0;
		  Double cxBonusBk = 0.0;
	
			Map<String, Object> all = getModel(year, month, getDays(year, month));
			
			for(String key : all.keySet()){
				Map<String, Object> weekModel = (Map<String, Object>) all.get(key);
				List <OrderTicketDetailSummaryModel> weekList = (List<OrderTicketDetailSummaryModel>) weekModel.get("weekList");
				
				for (OrderTicketDetailSummaryModel model : weekList) {
					totalNum += model.getTotalNum();
					totalInvestment +=model.getTotalInvestment();
					invest +=model.getInvest();
					totalPrice+=model.getTotalPrice();
					cxBonus +=model.getCxBonus();
					
					
					FbNum += model.getFbNum();
					totalInvestmentFb += model.getTotalInvestmentFb();
					investFb += model.getInvestFb();
					totalPriceFb += model.getTotalPriceFb();
					cxBonusFb +=model.getCxBonusFb();
					
					
					BkNum +=model.getBkNum();
					totalInvestmentBk +=model.getTotalInvestmentBk();
					investBk += model.getInvestBk();
					totalPriceBk +=model.getTotalPriceBk();
					cxBonusBk += model.getCxBonusBk();
				
				}
				
			}	
			
		
			
			OrderTicketDetailSummaryModel monthTotal = new OrderTicketDetailSummaryModel();	
			
			monthTotal.setWeek("本月合计");
			monthTotal.setTotalNum(totalNum);
			monthTotal.setTotalInvestment(totalInvestment);
			monthTotal.setInvest(invest);
			monthTotal.setTotalPrice(totalPrice);
			monthTotal.setCxBonus(cxBonus);
			
			
			monthTotal.setFbNum(FbNum);
			monthTotal.setTotalInvestmentFb(totalInvestmentFb);
			monthTotal.setInvestFb(investFb);
			monthTotal.setTotalPriceFb(totalPriceFb);
			monthTotal.setCxBonusFb(cxBonusFb);
			
			monthTotal.setBkNum(BkNum);
			monthTotal.setInvestBk(investBk);
			monthTotal.setTotalInvestmentBk(totalInvestmentBk);
			monthTotal.setTotalPriceBk(totalPriceBk);
			monthTotal.setCxBonusBk(cxBonusBk);
			
			return monthTotal;
		
	} 
	

	
	
	
	
	
	//计算payoutrate
	public OrderTicketDetailSummaryModel calculatePayoutRate(OrderTicketDetailSummaryModel model){
		Double totalPrice = model.getTotalPrice();
		Double invest = model.getInvest();
		Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
		
		Double totalPriceFb = model.getTotalPriceFb();
		Double investFb = model.getInvestFb();
		Double payoutRateFb = investFb==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceFb-investFb)/investFb*100, 3);
		
		Double totalPriceBk = model.getTotalPriceBk();
		Double investBk = model.getInvestBk();
		Double payoutRateBk =investBk==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceBk-investBk)/investBk*100, 3);
		
		model.setPayoutRate(payoutRate);
		model.setPayoutRateFb(payoutRateFb);
		model.setPayoutRateBk(payoutRateBk);
		return model;
	}
	
	


	@Override
	public Response dailySummaryExcel(String year, String month,HttpServletResponse response) {
		Response res =new Response();
		List<OrderTicketDetailSummaryModel> list = new ArrayList<>();
		
		try {
			Map<String, Object> map = queryAll(year, month);
			
			OrderTicketDetailSummaryModel monthTotal = (OrderTicketDetailSummaryModel) map.get("monthTotal");
			list.add(monthTotal);
			list.add(null);
			list.add(null);
			list.add(null);
			list.add(null);
			list.add(null);
			map.remove("monthTotal");
			Object [] keys =   map.keySet().toArray();    
			Arrays.sort(keys);    
			
			for (Object key : keys) {
					Map<String,Object> model=(Map<String, Object>) map.get(key);
					    List <OrderTicketDetailSummaryModel> weekList = (List<OrderTicketDetailSummaryModel>) model.get("weekList");
					    OrderTicketDetailSummaryModel  Weektotal =  (OrderTicketDetailSummaryModel) model.get("Weektotal");
					    
					    if(key.equals("1") && weekList.size()<7){
					    	for (int i = 0; i < 7-weekList.size(); i++) {
								list.add(null);
							}
					    }
					    list.addAll(weekList);
						
					    if( !key.equals("1") && weekList.size()<7){
							for (int i = 0; i < 7-weekList.size(); i++) {
								list.add(null);
							}
						}
						list.add(Weektotal);
						list.add(null);
						list.add(null);
						list.add(null);
				
			}
			
			//String strTemplate = "/opt/FMS/fms/excel/daily_Summary-demo.xls";// 模板位置

			Resource resource = new ClassPathResource("/excel/daily_Summary-demo.xls");
			InputStream in = resource.getInputStream();
		
			String[] strKeyArray = new String[] {"week","totalNum", "totalInvestment", "invest", "cxBonus","totalPrice", "payoutRate", "fbNum", "totalInvestmentFb", "investFb","cxBonusFb", "totalPriceFb", "payoutRateFb", "bkNum", "totalInvestmentBk", "investBk", "cxBonusBk","totalPriceBk", "payoutRateBk"};
			
			HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, OrderTicketDetailSummaryModel.class);
			PoiUtil.returnExcel(response, workbook, "daily-summary");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			return res;
	}
	/*//每月合计的时间参数
		public Map<String ,Object> getTotalMonth(String year,String month) throws Exception{
			Map<String, Object> params = new HashMap<String, Object>(); 
		    String s1 = year+"-"+month+"-"+"1";
		    String s2 = year+"-"+month+"-"+getDays(year, month);
		    Date date1=DateUtil.parse(s1);
		    Date date2=DateUtil.parse(s2);
			String startDate = DateUtil.formatDate(date1);
			String endDate	= DateUtil.formatDate(date2);
			
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			return params;
		}
*/
		
		
		//每天的时间参数
		public Map<String, Object>  getOneDay(String someDay) throws Exception{
			 String startDate=null;
			 String endDate=null;
			Map<String, Object> params = new HashMap<String, Object>();
		
		   	startDate = someDay+" 14:00:00";
			
			calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.DATE, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
			
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			
			log.info("计算"+startDate+"到"+endDate);
			
			return params;
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
		
	
}
