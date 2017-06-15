package com.caiex.account.service.impl;


import java.util.ArrayList;
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
import com.caiex.account.service.AllDailyFinancialStatementsService;
import com.caiex.account.utils.MergeUtil;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;
import com.esotericsoftware.minlog.Log;
import com.fms.api.DailyFinancialStatementsService;
import com.fms.api.HisDailyFinancialStatementsService;

@Service
public class DailyFinancialStatementsServiceImpl implements AllDailyFinancialStatementsService{

	@Autowired
	DailyFinancialStatementsService dailyFinancialStatementsService;
	
	@Autowired
	HisDailyFinancialStatementsService hisDailyFinancialStatementsService;
	
	@Autowired
	MergeUtil mergeUtil;
	
	@Override
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, Object> model = dailyFinancialStatementsService.queryAll(year, month, day);
		Map<String, Object> modelHis = hisDailyFinancialStatementsService.queryAll(year, month, day);
		
		OrderTicketDetailFinancialModel modelTodayCur = (OrderTicketDetailFinancialModel) model.get("modelToday");
		OrderTicketDetailFinancialModel modelWeekCur = (OrderTicketDetailFinancialModel) model.get("modelWeek");
		OrderTicketDetailFinancialModel modelMonthCur =(OrderTicketDetailFinancialModel) model.get("modelMonth");
		OrderTicketDetailFinancialModel modelYearCur = (OrderTicketDetailFinancialModel) model.get("modelYear");
		OrderTicketDetailFinancialModel totalCur = (OrderTicketDetailFinancialModel) model.get("total");
		
		OrderTicketDetailFinancialModel modelTodayHis = (OrderTicketDetailFinancialModel) model.get("modelToday");
		OrderTicketDetailFinancialModel modelWeekHis = (OrderTicketDetailFinancialModel) model.get("modelWeek");
		OrderTicketDetailFinancialModel modelMonthHis =(OrderTicketDetailFinancialModel) model.get("modelMonth");
		OrderTicketDetailFinancialModel modelYearHis = (OrderTicketDetailFinancialModel) model.get("modelYear");
		OrderTicketDetailFinancialModel totalHis = (OrderTicketDetailFinancialModel) model.get("total");
		
		OrderTicketDetailFinancialModel modelToday = new OrderTicketDetailFinancialModel();
		modelToday =(OrderTicketDetailFinancialModel) mergeUtil.merge(modelTodayCur, modelTodayHis, modelToday);
		calculatePayoutRate(modelToday);

		OrderTicketDetailFinancialModel modelWeek = new OrderTicketDetailFinancialModel();
		modelWeek =(OrderTicketDetailFinancialModel) mergeUtil.merge(modelWeekCur, modelWeekHis, modelWeek);
		calculatePayoutRate(modelWeek);
		
		OrderTicketDetailFinancialModel modelMonth = new OrderTicketDetailFinancialModel();
		modelMonth =(OrderTicketDetailFinancialModel) mergeUtil.merge(modelMonthCur, modelMonthHis, modelMonth);
		calculatePayoutRate(modelMonth);
		
		OrderTicketDetailFinancialModel modelYear = new OrderTicketDetailFinancialModel();
		modelYear =(OrderTicketDetailFinancialModel) mergeUtil.merge(modelYearCur, modelYearHis, modelYear);
		calculatePayoutRate(modelYear);
		
		OrderTicketDetailFinancialModel total = new OrderTicketDetailFinancialModel();
		total =(OrderTicketDetailFinancialModel) mergeUtil.merge(totalCur, totalHis, total);
		calculatePayoutRate(total);
		
		List<OrderTicketDetailFinancialModel> modelList = (List<OrderTicketDetailFinancialModel>) model.get("modelList");
		List<OrderTicketDetailFinancialModel> modelListHis = (List<OrderTicketDetailFinancialModel>) modelHis.get("modelList");
		
		List<OrderTicketDetailFinancialModel> list = getModelList(modelList, modelListHis);
		map.put("modelToday", modelToday);
		map.put("modelWeek", modelWeek);
		map.put("modelMonth", modelMonth);
		map.put("modelYear", modelYear);
		map.put("modelList", list);
		map.put("total", total);
		
		return map;
	}


	public List<OrderTicketDetailFinancialModel> getModelList(List<OrderTicketDetailFinancialModel>  modelList,List<OrderTicketDetailFinancialModel>  modelListHis) throws Exception{
		List<OrderTicketDetailFinancialModel> list = new ArrayList<>();
		
		for (int i = 0; i < modelList.size(); i++) {
			OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel();
			if(modelList.get(i).getColumn1().equals(modelListHis.get(i).getColumn1())){
				model = (OrderTicketDetailFinancialModel) mergeUtil.merge(modelList.get(i), modelListHis.get(i), model);
				calculatePayoutRate(model);
				list.add(model);
			}
		}
		return list;
	}
	

	//计算payoutrate
			public OrderTicketDetailFinancialModel calculatePayoutRate(OrderTicketDetailFinancialModel model){
				Double totalPrice = model.getTotalPrice();
				Double invest = model.getInvest();
				Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
				model.setPayoutRate(payoutRate);
				
				Double totalPriceAllup = model.getTotalPriceAllup();
				Double investAllup = model.getInvestAllup();
				Double payoutRateAllup = investAllup==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceAllup - investAllup) / investAllup * 100, 3);
				model.setPayoutRateAllup(payoutRateAllup);
				
				Double totalPriceFsgl = model.getTotalInvestmentFsgl();
				Double investFsgl = model.getInvestFsgl();
				Double payoutRateFsgl = investFsgl==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceFsgl - investFsgl) / investFsgl * 100, 3);
				model.setPayoutRateFsgl(payoutRateFsgl);
				
				Double totalPriceLevel0 = model.getTotalInvestmentLevel0();
				Double investLevel0 = model.getInvestLevel0();
				Double payoutRateLevel0 = investLevel0==0?0:NumberUtil.getNumberAccordingToPercision((totalPriceLevel0- investLevel0) / investLevel0 * 100, 3);
				model.setPayoutRateFsgl(payoutRateLevel0);
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
