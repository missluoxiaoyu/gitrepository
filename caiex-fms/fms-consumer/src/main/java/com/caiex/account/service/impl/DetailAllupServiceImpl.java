package com.caiex.account.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.model.OrderTicketDetailModel;
import com.caiex.account.model.OrderTicketDetailSummaryModel;
import com.caiex.account.service.AllDetailAllupService;
import com.caiex.account.utils.MergeUtil;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;
import com.fms.api.DetailAllupService;
import com.fms.api.HisDetailAllupService;
import com.fms.entity.OrderTicketDetail;



@Service
public class DetailAllupServiceImpl implements AllDetailAllupService{
	private final static Logger log= Logger.getLogger(DetailAllupServiceImpl.class);

	@Autowired
	private DetailAllupService detailAllupService;
	
	@Autowired
	private HisDetailAllupService hisDetailAllupService;

	@Autowired
	MergeUtil mergeUtil;
	
	String [] products = {"HAD","HHAD","HAFU","TTG","CRS","FCA"};
	
	@Override
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception {
		Map<String, Object> map = new HashMap<>();
		
		Map<String, Object> mapcurrent = detailAllupService.queryAll(year, month, day);
		Map<String, Object> maphis = hisDetailAllupService.queryAll(year, month, day);
		
		OrderTicketDetailModel totalCur=(OrderTicketDetailModel) mapcurrent.get("total");
		OrderTicketDetailModel totalHis=(OrderTicketDetailModel) maphis.get("total");
		OrderTicketDetailModel total=new OrderTicketDetailModel();
		total =(OrderTicketDetailModel) mergeUtil.merge(totalCur, totalHis, total);
		calculatePayoutRate(total);
		map.put("total", total);
		
		for (String product : products) {
			List<OrderTicketDetailModel> listcur = (List<OrderTicketDetailModel>) mapcurrent.get(product);
			List<OrderTicketDetailModel> listhis = (List<OrderTicketDetailModel>) maphis.get(product);
			map.put(product, getList(listcur, listhis));
		}
		return map;
	}

	
	public List<OrderTicketDetailModel> getList(List <OrderTicketDetailModel> listcur,List <OrderTicketDetailModel> listhis) throws Exception{
		List<OrderTicketDetailModel> list = new ArrayList<>();
		
		for (int i = 0; i < listcur.size(); i++) {
			OrderTicketDetailModel model = new OrderTicketDetailModel();
			if(listcur.get(i).getLocal_m().equals(listhis.get(i).getLocal_m())){
				model = (OrderTicketDetailModel) mergeUtil.merge(listcur.get(i), listhis.get(i), model);
				calculatePayoutRate(model);
				list.add(model);
			}
		}
	
		return list;
	}
	
	
	//计算payoutrate
	public OrderTicketDetailModel calculatePayoutRate(OrderTicketDetailModel model){
		Double totalPrice = model.getTotalPrice();
		Double invest = model.getInvest();
		Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
		model.setPayoutRate(payoutRate);
		return model;
	}
	
	
	

	
	@Override
	public Response dailyAllupExcel(String year,String month,String day,HttpServletResponse response)throws Exception{
		Response res =  new Response();
		try {
			List<OrderTicketDetailModel> list = new ArrayList<>();
			Map<String, Object> map = queryAll(year, month, day);
			OrderTicketDetailModel total=(OrderTicketDetailModel) map.get("total");
			list.add(null);
			list.add(total);
			for (String product : products) {
				List<OrderTicketDetailModel> model = (List<OrderTicketDetailModel>) map.get(product);
				list.addAll(model);
			}
			
			String strTemplate = "/opt/FMS/consumer/template/dailyALLUP-demo.xls";// 模板位置
			
			String[] strKeyArray = new String[] {"product","totalInvestment","invest","totalPrice","payoutRate","payout"};
			
			HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, OrderTicketDetailModel.class);
			PoiUtil.returnExcel(response, workbook, "allup");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			return res;
			}

	
}
