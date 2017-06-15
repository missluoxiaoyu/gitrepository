package com.caiex.account.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.model.OrderTicketDetailFinancialModel;
import com.caiex.account.model.OrderTicketDetailSummaryModel;
import com.caiex.account.service.AllDailyFinancialDetailService;
import com.caiex.account.utils.MergeUtil;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;
import com.fms.api.DailyFinancialDetailService;
import com.fms.api.HisDailyFinancialDetailService;




@Service
public class DailyFinancialDetailServiceImpl implements AllDailyFinancialDetailService {
	private final static Logger log = Logger.getLogger(DailyFinancialDetailServiceImpl.class);
	
	@Autowired
	DailyFinancialDetailService dailyFinancialDetailService;
	@Autowired
	HisDailyFinancialDetailService hisDailyFinancialDetailService ;
	@Autowired
	MergeUtil mergeUtil;
	
	
	//合并
	@Override
	public Map<String, Object> queryAll(String year,String month,String day) throws Exception{
		Map<String, Object> all = new HashMap<String, Object>();
		
		
		Map<String, Object> model = dailyFinancialDetailService.queryAll(year, month, day);
		Map<String, Object> modelHis =  hisDailyFinancialDetailService.queryAll(year, month, day);
		//当前库
		OrderTicketDetailFinancialModel currentFBtotal = (OrderTicketDetailFinancialModel) model.get("FBtotal");
		List<OrderTicketDetailFinancialModel>  currentmodelList= (List<OrderTicketDetailFinancialModel>) model.get("modelList");
		OrderTicketDetailFinancialModel currenttotal = (OrderTicketDetailFinancialModel) model.get("total");
		//历史库
		OrderTicketDetailFinancialModel hisFBtotal = (OrderTicketDetailFinancialModel) modelHis.get("FBtotal");
		List<OrderTicketDetailFinancialModel>  hismodelList= (List<OrderTicketDetailFinancialModel>) modelHis.get("modelList");
		OrderTicketDetailFinancialModel histotal = (OrderTicketDetailFinancialModel) modelHis.get("total");
	
		OrderTicketDetailFinancialModel FBtotal = new OrderTicketDetailFinancialModel();
		FBtotal= (OrderTicketDetailFinancialModel) mergeUtil.merge(currentFBtotal, hisFBtotal, FBtotal);
		calculatePayoutRate(FBtotal);
		
		OrderTicketDetailFinancialModel total = new OrderTicketDetailFinancialModel();
		total= (OrderTicketDetailFinancialModel) mergeUtil.merge(currenttotal, histotal, total);
		calculatePayoutRate(total);
		
		all.put("total", total);
		all.put("FBtotal", FBtotal);
		all.put("modelList", getModelList(hismodelList, currentmodelList));
		return all;
	}
		
	
	
	public List<OrderTicketDetailFinancialModel> getModelList(List<OrderTicketDetailFinancialModel>  hismodelList,List<OrderTicketDetailFinancialModel>  currentmodelList) throws Exception{
		List<OrderTicketDetailFinancialModel> list = new ArrayList<>();
		
		for (int i = 0; i < currentmodelList.size(); i++) {
			OrderTicketDetailFinancialModel model = new OrderTicketDetailFinancialModel();
			if(currentmodelList.get(i).getProduct().equals(hismodelList.get(i).getProduct())){
				model = (OrderTicketDetailFinancialModel) mergeUtil.merge(currentmodelList.get(i), hismodelList.get(i), model);
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

