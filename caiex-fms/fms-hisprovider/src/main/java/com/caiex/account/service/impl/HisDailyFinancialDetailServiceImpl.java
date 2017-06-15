package com.caiex.account.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.caiex.account.model.OrderTicketDetailFinancialModel;
import com.caiex.account.util.NumberUtil;
import com.fms.api.DailyFinancialDetailService;
import com.fms.api.HisDailyFinancialDetailService;
import com.fms.api.OrderTicketDetailService;
import com.fms.entity.OrderTicketDetail;

@Service("hisDailyFinancialDetailService")
public class HisDailyFinancialDetailServiceImpl implements HisDailyFinancialDetailService{
	private final static Logger log = Logger.getLogger(HisDailyFinancialDetailServiceImpl.class);
	
	
	@Autowired
	private  OrderTicketDetailHisMapper orderTicketDetailMapper;
	
	@Override
	public Map<String, Object> queryAll(String year, String month, String day) throws Exception {
		Map<String, Object> date = getStartAndEndDate(year, month, day);
		return queryAllModels(date);
	}

	
	public Map<String, Object>  queryAllModels(Map<String, Object> params){
		Map<String, Object> models = new HashMap<>();
		List<OrderTicketDetailFinancialModel> modelList =  new ArrayList<OrderTicketDetailFinancialModel>();
		
		String [] products = {"HAD","HHAD","HAFU","TTG","CRS","FCA"};
			for (String product : products) {
				OrderTicketDetailFinancialModel model =  getModel(params, product);
				modelList.add(model);
			}
			OrderTicketDetailFinancialModel totalFB =  getModel(params, null);
			models.put("FBtotal", totalFB);//合计
			models.put("modelList", modelList);
			models.put("total", totalFB);
		return models;
	}
	
	
	
	 public OrderTicketDetailFinancialModel getModel(Map<String, Object> params,String product){
		 params.put("product", product);
		//统计每日
		 
		    OrderTicketDetail ticketTotalInvest = orderTicketDetailMapper.queryTotalInvestment(params);
		    Double totalInvestment = ticketTotalInvest == null ? 0.0 :ticketTotalInvest.getInv_allup();
			
		    OrderTicketDetail  ticketInvest  = orderTicketDetailMapper.queryInvest(params);
			Double invest= ticketInvest == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvest.getPrice_allup(),3);
			
			OrderTicketDetail  ticketTotalPrice =  orderTicketDetailMapper.queryTotalprice(params);
			Double totalPrice = ticketTotalPrice == null ? 0.0 :ticketTotalPrice.getTotal_price();
			//Double payoutRate = invest.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
			
		//串关	
			OrderTicketDetail ticketTotalInvestAllup= orderTicketDetailMapper.queryTotalInvestmentALLUP(params);
			Double totalInvestmentAllup = ticketTotalInvestAllup == null ? 0.0 :ticketTotalInvestAllup.getInv_allup();  
			
			OrderTicketDetail  ticketInvestAllup  = orderTicketDetailMapper.queryinvestALLUP(params);
			Double investAllup= ticketInvestAllup == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvestAllup.getPrice_allup(),3);
			
			OrderTicketDetail  ticketTotalPriceAllup =  orderTicketDetailMapper.queryTotalPriceALLUP(params);
			Double totalPriceAllup=ticketTotalPriceAllup == null ? 0.0 :ticketTotalPriceAllup.getTotal_price();
			//Double payoutRateAllup = investAllup.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPriceAllup - investAllup) / investAllup * 100, 3);		
			
	   //单关		
			OrderTicketDetail ticketTotalInvestFsgl = orderTicketDetailMapper.queryTotalInvestmentFSGL(params);
			Double totalInvestmentFsgl= ticketTotalInvestFsgl == null ?0.0 :ticketTotalInvestFsgl.getInv_allup();
			
			OrderTicketDetail  ticketInvestFsgl  = orderTicketDetailMapper.queryinvestFSGL(params);
			Double investFsgl= ticketInvestFsgl == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvestFsgl.getPrice_allup(), 3);
			
			OrderTicketDetail  ticketTotalPriceFsgl =  orderTicketDetailMapper.queryTotalPriceFSGL(params);
			Double totalPriceFsgl=ticketTotalPriceFsgl == null ? 0.0 :ticketTotalPriceFsgl.getTotal_price();
			//Double payoutRateFsgl = investFsgl.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPriceFsgl - investFsgl) / investFsgl * 100, 3);
			
			OrderTicketDetail ticketWinFsgl = orderTicketDetailMapper.queryWinInvest(params);
			Double winInvest = ticketWinFsgl == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketWinFsgl.getPrice_allup(), 3);
			Double average = winInvest == 0.0 ? 0 :NumberUtil.getNumberAccordingToPercision(totalPriceFsgl / winInvest, 3);
				
	//0关		
			OrderTicketDetail ticketTotalInvestLevel0 = orderTicketDetailMapper.queryTotalInvestmentLEVEL0(params);
			Double totalInvestmentLevel0 = ticketTotalInvestLevel0 == null ?0.0 :ticketTotalInvestLevel0.getInv_allup();
			
			OrderTicketDetail  ticketInvestLevel0 = orderTicketDetailMapper.queryinvestLEVEL0(params);
			Double investLevel0= ticketInvestLevel0 == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvestLevel0.getPrice_allup(), 3);
			
			Double totalPriceLevel0=ticketInvestLevel0== null ? 0.0 :ticketInvestLevel0.getTotal_price();
			//Double payoutRateLevel0 = investLevel0.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPriceLevel0 - investLevel0) / investLevel0 * 100, 3);
			
			OrderTicketDetailFinancialModel orderTicketDetailHisModel = new OrderTicketDetailFinancialModel(null,null,product, totalInvestment, invest, totalPrice, null,  totalInvestmentAllup, investAllup, totalPriceAllup, null,totalInvestmentFsgl, investFsgl, totalPriceFsgl, null, winInvest, average,totalInvestmentLevel0, investLevel0,totalPriceLevel0,null);
			return orderTicketDetailHisModel;
	 } 
	 
	 
	 private Map<String, Object> getStartAndEndDate(String year, String month,String day) throws Exception {
			String startDate = null;
			String endDate = null;
			Map<String, Object> params = new HashMap<>();
			if (StringUtils.isEmpty(month)) {
				startDate = year + "-01-01 12:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
				calendar.add(Calendar.YEAR, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 12:00:00";
			} else if (StringUtils.isEmpty(day)) {							
				startDate = year + "-" + month + "-01 12:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.MONTH, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 12:00:00";
			} else {
				startDate = year + "-" + month + "-" + day + " 12:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.DATE, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 12:00:00";
			}
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			log.info("开始时间"+startDate);
			log.info("结束时间"+endDate);
			return params;
		}
	 
	 
	
}
