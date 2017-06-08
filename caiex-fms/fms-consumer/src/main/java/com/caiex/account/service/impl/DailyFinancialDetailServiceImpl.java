package com.caiex.account.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.entity.OrderTicketDetailHis;
import com.caiex.account.entity.OrderTicketInfo;
import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.caiex.account.mapper.OrderTicketDetailMapper;
import com.caiex.account.model.AgentInfoModel;
import com.caiex.account.model.OrderTicketDetailFinancialModel;
import com.caiex.account.service.DailyFinancialDetailService;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;


@Service
public class DailyFinancialDetailServiceImpl implements DailyFinancialDetailService {
	private final static Logger log = Logger.getLogger(DailyFinancialDetailServiceImpl.class);
	@Autowired
	private  OrderTicketDetailHisMapper orderTicketDetailHisMapper;
	@Autowired
	private  OrderTicketDetailMapper orderTicketDetailMapper;
	
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
			
			OrderTicketDetail ticketWinFsgl = orderTicketDetailMapper.queryWinInvest(params);
			Double winInvest = ticketWinFsgl == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketWinFsgl.getPrice_allup(), 3);
			Double average = winInvest == 0.0 ? 0 :NumberUtil.getNumberAccordingToPercision(totalPriceFsgl / winInvest, 3);
				
	//0关		
			OrderTicketDetail ticketTotalInvestLevel0 = orderTicketDetailMapper.queryTotalInvestmentLEVEL0(params);
			Double totalInvestmentLevel0 = ticketTotalInvestLevel0 == null ?0.0 :ticketTotalInvestLevel0.getInv_allup();
			
			OrderTicketDetail  ticketInvestLevel0 = orderTicketDetailMapper.queryinvestLEVEL0(params);
			Double investLevel0= ticketInvestLevel0 == null ? 0.0 :NumberUtil.getNumberAccordingToPercision(ticketInvestLevel0.getPrice_allup(), 3);
			
			Double totalPriceLevel0=ticketInvestLevel0== null ? 0.0 :ticketInvestLevel0.getTotal_price();
			Double payoutRateLevel0 = investLevel0.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPriceLevel0 - investLevel0) / investLevel0 * 100, 3);
			
			OrderTicketDetailFinancialModel orderTicketDetailHisModel = new OrderTicketDetailFinancialModel(null,null,product, totalInvestment, invest, totalPrice, payoutRate,  totalInvestmentAllup, investAllup, totalPriceAllup, payoutRateAllup,totalInvestmentFsgl, investFsgl, totalPriceFsgl, payoutRateFsgl, winInvest, average,totalInvestmentLevel0, investLevel0,totalPriceLevel0,payoutRateLevel0);
			return orderTicketDetailHisModel;
	 } 
	 
	 
	 private Map<String, Object> getStartAndEndDate(String year, String month,String day) throws Exception {
			String startDate = null;
			String endDate = null;
			Map<String, Object> params = new HashMap<>();
			if (StringUtils.isEmpty(month)) {
				startDate = year + "-01-01 00:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
				calendar.add(Calendar.YEAR, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
			} else if (StringUtils.isEmpty(day)) {							
				startDate = year + "-" + month + "-01 00:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.MONTH, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
			} else {
				startDate = year + "-" + month + "-" + day + " 00:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.DATE, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
			}
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			log.info("开始时间"+startDate);
			log.info("结束时间"+endDate);
			return params;
		}
	 
	 
	 
	 
	 
	 
	
	@Override
	public Response dailyFinancialDetailExcel(String year, String month, String day,HttpServletResponse response){
		Response res = new Response();
		try {
		
		Map<String, Object> map = queryAll(year, month, day);
		List<OrderTicketDetailFinancialModel> modelList = (List<OrderTicketDetailFinancialModel>) map.get("modelList");
		OrderTicketDetailFinancialModel FBtotal = (OrderTicketDetailFinancialModel) map.get("FBtotal");
		OrderTicketDetailFinancialModel total = (OrderTicketDetailFinancialModel) map.get("total");
		
		
		List<OrderTicketDetailFinancialModel> list = new ArrayList<>();
		String strTemplate = "/opt/FMS/consumer/template/daily_Breakdown-demo.xls";// 模板位置
		
		for (OrderTicketDetailFinancialModel orderTicketDetailHisModel : modelList) {
			orderTicketDetailHisModel.setColumn1(null);
			orderTicketDetailHisModel.setColumn2(null);
		}
		
		list.add(null);
		list.add(null);
		list.addAll(modelList);
		
		FBtotal.setColumn1(null);
		FBtotal.setColumn2(null);
		list.add(FBtotal);
		list.add(null);
		list.add(null);
		list.add(null);
		
		total.setColumn1(null);
		total.setColumn2(null);
		list.add(total);
		
		String[] strKeyArray = new String[] {"column1","column2","totalInvestment", "invest", "totalPrice", "payoutRate","totalInvestmentAllup", "investAllup", "totalPriceAllup", "payoutRateAllup","totalInvestmentFsgl", "investFsgl", "totalPriceFsgl","payoutRateFsgl", "winpriceFsgl","averageFsgl", "totalInvestmentLevel0", "investLevel0","totalPriceLevel0","payoutRateLevel0"};
		HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, OrderTicketDetailFinancialModel.class);
		PoiUtil.returnExcel(response, workbook, "breakdown");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
		
		
	}
	
}

