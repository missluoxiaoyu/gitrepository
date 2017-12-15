package com.caiex.fms.fb.service.impl;

import java.io.InputStream;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.basketball.api.BkBreakDownProService;
import com.caiex.dbservice.currentdb.api.BreakDownProService;
import com.caiex.dbservice.historydb.api.BkBreakDownProHisService;
import com.caiex.dbservice.historydb.api.BreakDownProHisService;
import com.caiex.dbservice.model.OrderTicketDetailFinancialModel;
import com.caiex.fms.fb.service.DailyBreakDownService;
import com.caiex.fms.utils.MergeUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;

@Service
public class DailyBreakDownServiceImpl implements DailyBreakDownService {
	private final static Logger log = Logger.getLogger(DailyBreakDownServiceImpl.class);
	
	@Autowired
	BreakDownProService breakDownProService;
	@Autowired
	BreakDownProHisService breakDownProHisService ;
	
	@Autowired
	BkBreakDownProService bkBreakDownProService;
	
	@Autowired
	BkBreakDownProHisService bkBreakDownProHisService;
	
	
	@Autowired
	MergeUtil mergeUtil;
	
	
	
	@Override
	public Map<String, Object> queryAll(String year, String month, String day) throws Exception {
		Map<String, Object> date = getStartAndEndDate(year, month, day);
	
		return queryAllModels(date);
	}

	
	public Map<String, Object>  queryAllModels(Map<String, Object> params) throws Exception{
		Map<String, Object> models = new HashMap<>();
		List<OrderTicketDetailFinancialModel> modelList =  new ArrayList<OrderTicketDetailFinancialModel>();
		List<OrderTicketDetailFinancialModel> basketBallModelList =  new ArrayList<OrderTicketDetailFinancialModel>();
		//足球
		String [] products = {"HAD","HHAD","HAFU","TTG","CRS","FCA"};
			for (String product : products) {
				OrderTicketDetailFinancialModel modelCur = breakDownProService.getModel(params, product);
				OrderTicketDetailFinancialModel modelHis = breakDownProHisService.getModel(params, product);
				
				OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel();
				model = (OrderTicketDetailFinancialModel) mergeUtil.merge(modelCur, modelHis, model);
				calculatePayoutRate(model);
				modelList.add(model);
			}
			OrderTicketDetailFinancialModel totalFBCur =  breakDownProService.getModel(params, null);
			
			OrderTicketDetailFinancialModel totalFBHis =  breakDownProHisService.getModel(params, null);
			
			OrderTicketDetailFinancialModel totalFB = new OrderTicketDetailFinancialModel();
			totalFB = (OrderTicketDetailFinancialModel) mergeUtil.merge(totalFBCur, totalFBHis, totalFB);
			calculatePayoutRate(totalFB);
		
		//篮球
		String [] basketballProducts = {"HDC","HILO","MNL","WNM","FCA"};	
			for (String product : basketballProducts) {
				
				OrderTicketDetailFinancialModel modelCur = bkBreakDownProService.getModel(params, product);
				
				OrderTicketDetailFinancialModel modelHis = bkBreakDownProHisService.getModel(params, product);
				
				OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel();
				model = (OrderTicketDetailFinancialModel) mergeUtil.merge(modelCur, modelHis, model);
				calculatePayoutRate(model);
				basketBallModelList.add(model);
			}
			
			OrderTicketDetailFinancialModel totalBKCur =  bkBreakDownProService.getModel(params, null);
			
			OrderTicketDetailFinancialModel totalBKHis =  bkBreakDownProHisService.getModel(params, null);
			
			OrderTicketDetailFinancialModel totalBK = new OrderTicketDetailFinancialModel();
			totalBK = (OrderTicketDetailFinancialModel) mergeUtil.merge(totalBKCur, totalBKHis, totalBK);
			calculatePayoutRate(totalBK);
			
			OrderTicketDetailFinancialModel total = new OrderTicketDetailFinancialModel();
			total=(OrderTicketDetailFinancialModel) mergeUtil.merge(totalFB, totalBK, total);
			calculatePayoutRate(total);
	
			
			models.put("basketBallModelList", basketBallModelList);
			models.put("FBtotal", totalFB);//合计
			models.put("BKtotal", totalBK);//合计
			models.put("modelList", modelList);
			models.put("total", total);
	
		return models;
	}
	

	
	

	
	
	
	
	
	
	//计算payoutrate
		public OrderTicketDetailFinancialModel calculatePayoutRate(OrderTicketDetailFinancialModel model){
			Double totalPrice = model.getTotalPrice();
			Double invest = model.getInvest();
			Double bonusInvest = model.getBonusInvest();
		
			Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
			model.setTotalPrice(NumberUtil.getNumberAccordingToPercision(totalPrice, 3));
			model.setInvest(NumberUtil.getNumberAccordingToPercision(invest, 3));
			model.setPayoutRate(payoutRate);
			model.setBonusInvest(NumberUtil.getNumberAccordingToPercision(bonusInvest, 3));
			
			
			Double totalPriceAllup = model.getTotalPriceAllup();
			Double investAllup = model.getInvestAllup();
			Double bonusInvestAllup= model.getBonusInvestAllup();
			
			
			Double payoutRateAllup = investAllup==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceAllup - investAllup) / investAllup * 100, 3);
			model.setTotalPriceAllup(NumberUtil.getNumberAccordingToPercision(totalPriceAllup, 3));
			model.setInvestAllup(NumberUtil.getNumberAccordingToPercision(investAllup, 3));
			model.setPayoutRateAllup(payoutRateAllup);
			model.setBonusInvestAllup(NumberUtil.getNumberAccordingToPercision(bonusInvestAllup, 3));
			
			
			Double totalPriceFsgl = model.getTotalPriceFsgl();
			Double investFsgl = model.getInvestFsgl();
			Double bonusInvestFsgl= model.getBonusInvestFsgl();
			
			
			Double payoutRateFsgl = investFsgl==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceFsgl - investFsgl) / investFsgl * 100, 3);
			Double averageFsgl = model.getWinpriceFsgl()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceFsgl()/model.getWinpriceFsgl(), 3);
			model.setWinpriceFsgl(NumberUtil.getNumberAccordingToPercision(model.getWinpriceFsgl(),3));
			//中奖交易额
			model.setTotalPriceFsgl(NumberUtil.getNumberAccordingToPercision(totalPriceFsgl, 3));
			model.setInvestFsgl(NumberUtil.getNumberAccordingToPercision(investFsgl, 3));
			model.setAverageFsgl(averageFsgl);
			model.setPayoutRateFsgl(payoutRateFsgl);
			model.setBonusInvestFsgl(NumberUtil.getNumberAccordingToPercision(bonusInvestFsgl, 3));
			
			
			Double totalPriceLevel0 = model.getTotalPriceLevel0();
			Double investLevel0 = model.getInvestLevel0();
			Double bonusInvestLevel0 = model.getBonusInvestLevel0();
			
			Double payoutRateLevel0 = investLevel0==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceLevel0- investLevel0) / investLevel0 * 100, 3);
			model.setTotalPriceLevel0(NumberUtil.getNumberAccordingToPercision(totalPriceLevel0, 3));
			model.setInvestLevel0(NumberUtil.getNumberAccordingToPercision(investLevel0, 3));
			model.setPayoutRateLevel0(payoutRateLevel0);
			model.setBonusInvestLevel0(NumberUtil.getNumberAccordingToPercision(bonusInvestLevel0, 3));
			
			return model;
		}
		
	
	@Override
	public Response dailyFinancialDetailExcel(String year, String month, String day,HttpServletResponse response){
		Response res = new Response();
		try {
		
		Map<String, Object> map = queryAll(year, month, day);
		List<OrderTicketDetailFinancialModel> modelList = (List<OrderTicketDetailFinancialModel>) map.get("modelList");
		
		List<OrderTicketDetailFinancialModel> basketBallModelList = (List<OrderTicketDetailFinancialModel>) map.get("basketBallModelList");
		
		OrderTicketDetailFinancialModel BKtotal = (OrderTicketDetailFinancialModel) map.get("BKtotal");
		OrderTicketDetailFinancialModel FBtotal = (OrderTicketDetailFinancialModel) map.get("FBtotal");
		OrderTicketDetailFinancialModel total = (OrderTicketDetailFinancialModel) map.get("total");
		
		
		
		List<OrderTicketDetailFinancialModel> list = new ArrayList<>();
		
		Resource resource = new ClassPathResource("/excel/daily_Breakdown-demo.xls");
		InputStream in = resource.getInputStream();
		
		
		
		for (OrderTicketDetailFinancialModel orderTicketDetailHisModel : modelList) {
			orderTicketDetailHisModel.setDate(null);
			orderTicketDetailHisModel.setNum(null);
		}
		
		list.add(null);
		list.add(null);
		list.addAll(modelList);
		
		FBtotal.setDate(null);
		FBtotal.setNum(null);
		list.add(FBtotal);
		
		for (OrderTicketDetailFinancialModel orderTicketDetailHisModel : basketBallModelList) {
			orderTicketDetailHisModel.setDate(null);
			orderTicketDetailHisModel.setNum(null);
		}
		
		list.addAll(basketBallModelList);
		
		BKtotal.setDate(null);
		BKtotal.setNum(null);
		
		list.add(BKtotal);
		
		total.setDate(null);
		total.setNum(null);
		list.add(total);
		
		String[] strKeyArray = new String[] {"date","num","totalInvestment", "invest","bonusInvest", "totalPrice", "payoutRate","totalInvestmentAllup", "investAllup","bonusInvestAllup", "totalPriceAllup", "payoutRateAllup","totalInvestmentFsgl", "investFsgl","bonusInvestFsgl", "totalPriceFsgl","payoutRateFsgl", "winpriceFsgl","averageFsgl", "totalInvestmentLevel0", "investLevel0","bonusInvestLevel0","totalPriceLevel0","payoutRateLevel0"};
		HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, OrderTicketDetailFinancialModel.class);
		PoiUtil.returnExcel(response, workbook, "breakdown");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
		
	}
	
	 private Map<String, Object> getStartAndEndDate(String year, String month,String day) throws Exception {
			String startDate = null;
			String endDate = null;
			Map<String, Object> params = new HashMap<>();
			if (StringUtils.isEmpty(month)) {
				startDate = year + "-01-01 14:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
				calendar.add(Calendar.YEAR, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
			} else if (StringUtils.isEmpty(day)) {							
				startDate = year + "-" + month + "-01 14:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.MONTH, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
			} else {
				startDate = year + "-" + month + "-" + day + " 14:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.DATE, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
			}
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			log.info("开始时间"+startDate);
			log.info("结束时间"+endDate);
			return params;
		}
	 
	 
}

