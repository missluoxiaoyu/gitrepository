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

import com.caiex.dbservice.currentdb.api.AllupProService;
import com.caiex.dbservice.currentdb.entity.OrderTicketDetail;
import com.caiex.dbservice.historydb.api.AllupProHisService;
import com.caiex.dbservice.model.OrderTicketDetailModel;
import com.caiex.fms.fb.service.DailyAllupService;
import com.caiex.fms.utils.MergeUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;
import com.mysql.fabric.xmlrpc.base.Array;



@Service
public class DailyAllupServiceImpl implements DailyAllupService{
	private final static Logger log= Logger.getLogger(DailyAllupServiceImpl.class);

	@Autowired
	private AllupProService allupProService;
	
	@Autowired
	private AllupProHisService allupProHisService;

	@Autowired
	MergeUtil mergeUtil;
	
	Integer [] local_ms={2,3,4,5,6,7,8};
	String [] products = {"HAD","HHAD","HAFU","TTG","CRS","FCA"};
	
	@Override
	public Map<String, Object> queryAll(String year,String month,String day)throws Exception {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object>  params = new HashMap<>();
		Map<String, Object>  date = getStartAndEndDate(year, month, day);
		params.putAll(date);
	    
		List<Map<String, Object>> list =new ArrayList<>();
		Double allTotalInvestment=0.0;
		Double allTotalPrice = 0.0;
		Double allInvest=0.0;
		Double allPayout=0.0;
		Double allPayoutrate=0.0;
		Double allBonus = 0.0;
		List<OrderTicketDetailModel> modelList = new ArrayList<>();
		
		for (String product : products) {
			
			Double totalInvestment=0.0;
			Double totalPrice = 0.0;
			Double invest=0.0;
			Double payout=0.0;
			Double payoutrate=0.0;
			Double bonus = 0.0;
			
			for (Integer local_m : local_ms) {
				params.put("product", product);
				params.put("local_m", local_m);
				long start = System.currentTimeMillis();
				OrderTicketDetailModel detailCur = allupProService.getModels(params);
				log.info("查询"+product+"的"+local_m+"关");
				OrderTicketDetailModel detailHis = allupProHisService.getModels(params);
				long end = System.currentTimeMillis();
				log.info(product+"查询用时"+(end-start));
				OrderTicketDetailModel detail = new OrderTicketDetailModel();
				detail = (OrderTicketDetailModel) mergeUtil.merge(detailCur, detailHis, detail);
				detail.setLocal_m(local_m);
				calculatePayoutRate(detail);
				modelList.add(detail);
					
				totalInvestment +=detail.getTotalInvestment();
				totalPrice +=detail.getTotalPrice();
				invest +=detail.getInvest();
				
				bonus +=detail.getBonus();
				
				}
			payout = totalPrice-invest;
			payoutrate = invest == 0.0 ?0:(totalPrice-invest)/invest *100;
			OrderTicketDetailModel total = new OrderTicketDetailModel(0,"玩法合计", NumberUtil.getNumberAccordingToPercision(totalInvestment,3), NumberUtil.getNumberAccordingToPercision(invest,3),NumberUtil.getNumberAccordingToPercision(totalPrice,3),NumberUtil.getNumberAccordingToPercision(payoutrate,3), NumberUtil.getNumberAccordingToPercision(payout,3),null,null,bonus);
			modelList.add(total);
			map.put(product, modelList);
			
			modelList= new ArrayList<>();
	
			allTotalPrice +=totalPrice;
			allInvest +=invest;
			allBonus +=bonus;
		}
		
		allPayout = allTotalPrice-allInvest;
		allPayoutrate = allInvest==0.0?0:(allTotalPrice-allInvest)/allInvest *100;
		
		OrderTicketDetail totalc = allupProService.getTotalInvestment(date);
		OrderTicketDetail totalh = allupProHisService.getTotalInvestment(date);
		
		allTotalInvestment = Double.valueOf(totalc== null?0:totalc.getTotalInvestment());
		allTotalInvestment +=Double.valueOf(totalh == null?0:totalh.getTotalInvestment());
		
		OrderTicketDetailModel total = new OrderTicketDetailModel(0,"合计", NumberUtil.getNumberAccordingToPercision(allTotalInvestment, 3),NumberUtil.getNumberAccordingToPercision(allInvest, 3) ,NumberUtil.getNumberAccordingToPercision(allTotalPrice,3), allPayoutrate,allPayout,null,null,allBonus);
	
		mergeUtil.getNumberAccordingToPercision(total);
		map.put("total", total);
	
		return map;
	}

	

	//计算payoutrate
	public OrderTicketDetailModel calculatePayoutRate(OrderTicketDetailModel model){
		Double totalPrice = model.getTotalPrice();
		Double invest = model.getInvest();
		Double payout=NumberUtil.getNumberAccordingToPercision((totalPrice - invest),3);
		Double payoutRate = invest==0?0:NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
		model.setTotalPrice(NumberUtil.getNumberAccordingToPercision(totalPrice, 3));
		model.setInvest(NumberUtil.getNumberAccordingToPercision(invest,3));
		model.setPayout(payout);
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
				
				for (OrderTicketDetailModel orderTicketDetailModel : model) {
					orderTicketDetailModel.setProduct(null);
				}
				
				list.addAll(model);
			}

			Resource resource = new ClassPathResource("/excel/dailyALLUP-demo.xls");
			InputStream in = resource.getInputStream();
			
			String[] strKeyArray = new String[] {"product","totalInvestment","invest","bonus","totalPrice","payoutRate","payout"};
			
			HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, OrderTicketDetailModel.class);
			PoiUtil.returnExcel(response, workbook, "allup");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			return res;
			}

	
	// 获得查询的起止时间
			private Map<String, Object> getStartAndEndDate(String year, String month,String day) throws Exception {
				String startDate = null;// 开始时间
				String endDate = null;// 结束时间
				Map<String, Object> date = new HashMap<>();

				if (StringUtils.isEmpty(month)) {// 月份为空	
					
					startDate = year + "-01-01 14:00:00";
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
					calendar.add(Calendar.YEAR, 1);
					endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
				} else if (StringUtils.isEmpty(day)) {// day为空
											
					startDate = year + "-" + month + "-01 14:00:00";
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
					calendar.add(Calendar.MONTH, 1);
					endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
				} else {//均有值
					
					startDate = year + "-" + month + "-" + day + " 14:00:00";
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
					calendar.add(Calendar.DATE, 1);
					endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
				}
				date.put("startDate", startDate);
				date.put("endDate", endDate);
			
				return date;
			}	
		
			
			
		
			
			
		
	
}
