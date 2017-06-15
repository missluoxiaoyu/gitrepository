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

import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.caiex.account.mapper.OrderTicketDetailMapper;
import com.caiex.account.model.OrderTicketDetailSummaryModel;
import com.caiex.account.model.SummaryTitle;
import com.caiex.account.service.AllDailyCollectStatementsService;
import com.caiex.account.utils.MergeUtil;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;
import com.fms.api.DailyCollectStatementsService;
import com.fms.api.HisDailyCollectStatementsService;
import com.mysql.fabric.xmlrpc.base.Array;
@Service
public class DailyCollectStatementsServiceImpl implements AllDailyCollectStatementsService{

	@Autowired
	private DailyCollectStatementsService dailyCollectStatementsService;
	
	@Autowired
	private HisDailyCollectStatementsService hisDailyCollectStatementsService;
	
	@Autowired
	MergeUtil mergeUtil;
	
	@Override
	public Map<String, Object> queryAll(String year, String month)throws Exception {
		Map<String, Object> all = new HashMap<>();
		
		Map<String, Object> model = dailyCollectStatementsService.queryAll(year, month);
		Map<String, Object> modelHis =  hisDailyCollectStatementsService.queryAll(year, month);
		//合并monthTotal
		OrderTicketDetailSummaryModel monthTotalCurrent = (OrderTicketDetailSummaryModel) model.get("monthTotal");
		OrderTicketDetailSummaryModel monthTotalHis = (OrderTicketDetailSummaryModel) modelHis.get("monthTotal");
		OrderTicketDetailSummaryModel monthTotal = new OrderTicketDetailSummaryModel();
		
		monthTotal = (OrderTicketDetailSummaryModel) mergeUtil.merge(monthTotalCurrent, monthTotalHis, monthTotal);
		calculatePayoutRate(monthTotal);//计算payoutrate
		all.put("monthTotal", monthTotal);
		
		model.remove("monthTotal");
		modelHis.remove("monthTotal");
		//合并每周每日
		for (String key : model.keySet()) {
			Map<String, Object> map = new HashMap<>();
			Map<String,Object> mapCurrent=(Map<String, Object>) model.get(key);
			Map<String,Object> mapHis=(Map<String, Object>) modelHis.get(key);
			
			OrderTicketDetailSummaryModel  weekCurrent =  (OrderTicketDetailSummaryModel) mapCurrent.get("Weektotal");
			OrderTicketDetailSummaryModel  weekHis =  (OrderTicketDetailSummaryModel) mapCurrent.get("Weektotal");
			OrderTicketDetailSummaryModel weekTotal = new OrderTicketDetailSummaryModel();
			weekTotal = (OrderTicketDetailSummaryModel) mergeUtil.merge(weekCurrent, weekHis, weekTotal);
			calculatePayoutRate(weekTotal);//计算payoutrate
			map.put("WeekTotal", weekTotal);
			SummaryTitle  title =  (SummaryTitle) mapCurrent.get("title");
			map.put("title", title);
			
			List <OrderTicketDetailSummaryModel> weekListC = (List<OrderTicketDetailSummaryModel>) mapCurrent.get("weekList");
			List <OrderTicketDetailSummaryModel> weekListH = (List<OrderTicketDetailSummaryModel>) mapHis.get("weekList");
			map.put("weekList", getWeekList(weekListC, weekListH));
			
			all.put(key, map);
		}
		
		return all;
	}
	 

	public List<OrderTicketDetailSummaryModel> getWeekList(List <OrderTicketDetailSummaryModel> weekListC,List <OrderTicketDetailSummaryModel> weekListH) throws Exception{
		List<OrderTicketDetailSummaryModel> list = new ArrayList<>();
		
		for (int i = 0; i < weekListC.size(); i++) {
			OrderTicketDetailSummaryModel model = new OrderTicketDetailSummaryModel();
			if(weekListC.get(i).getWeek().equals(weekListH.get(i).getWeek())){
				model = (OrderTicketDetailSummaryModel) mergeUtil.merge(weekListC.get(i), weekListH.get(i), model);
				calculatePayoutRate(model);
				list.add(model);
			}
		}
	
		return list;
	}
	
	
	//计算payoutrate
	public OrderTicketDetailSummaryModel calculatePayoutRate(OrderTicketDetailSummaryModel model){
		Double totalPrice = model.getTotalPrice();
		Double invest = model.getInvest();
		Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
		model.setPayoutRate(payoutRate);
		model.setPayoutRateFb(payoutRate);
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
			
			String strTemplate = "/opt/FMS/consumer/template/daily_Summary-demo.xls";// 模板位置
		
			String[] strKeyArray = new String[] {"week","totalNum", "totalInvestment", "invest", "totalPrice", "payoutRate", "fbNum", "totalInvestmentFb", "investFb", "totalPriceFb", "payoutRateFb", "bkNum", "totalInvestmentBk", "investBk", "totalPriceBk", "payoutRateBk"};
			
			HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, OrderTicketDetailSummaryModel.class);
			PoiUtil.returnExcel(response, workbook, "daily-summary");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			return res;
	}


	
}
