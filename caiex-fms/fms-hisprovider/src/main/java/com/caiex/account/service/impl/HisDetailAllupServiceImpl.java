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
import com.caiex.account.model.OrderTicketDetailModel;
import com.caiex.account.util.NumberUtil;
import com.fms.api.DetailAllupService;
import com.fms.api.HisDetailAllupService;
import com.fms.api.OrderTicketDetailService;
import com.fms.entity.OrderTicketDetail;


@Service("hisDetailAllupService")
public class HisDetailAllupServiceImpl implements HisDetailAllupService{
	private final static Logger log= Logger.getLogger(HisDetailAllupServiceImpl.class);
	
	@Autowired
    private OrderTicketDetailHisMapper orderTicketDetailMapper;
	Integer [] local_ms={2,3,4,5,6,7,8};
	String [] products = {"HAD","HHAD","HAFU","TTG","CRS","FCA"};
	
	
	@Override
	public Map<String, Object> queryAll(String year,String month,String day)throws Exception {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object>  params = new HashMap<>();
		Map<String, Object>  date = getStartAndEndDate(year, month, day);
		params.putAll(date);
	    
		Double allTotalInvestment=0.0;
		Double allTotalPrice = 0.0;
		Double allInvest=0.0;
		Double allPayout=0.0;
		Double allPayoutrate=0.0;
		List<OrderTicketDetailModel> modelList = new ArrayList<>();
		
		for (String product : products) {
			for (Integer local_m : local_ms) {
				params.put("product", product);
				params.put("local_m", local_m);
					OrderTicketDetail  detailTotalInvestment= orderTicketDetailMapper.totalInvestmentALLUP(params);
					OrderTicketDetail  detailTotalPrice= orderTicketDetailMapper.totalPriceALLUP(params);
					OrderTicketDetail  detailInvest= orderTicketDetailMapper.investALLUP(params);
					Double totalInvestment = detailTotalInvestment == null?0.0:detailTotalInvestment.getInv_allup();
					Double totalPrice = detailTotalPrice == null ?0.0:detailTotalPrice.getTotal_price();
					Double invest = detailInvest == null?0.0:detailInvest.getPrice_allup();
					Double payout= totalPrice-invest;
					//Double payoutRate = invest==0.0 ? 0 :NumberUtil.getNumberAccordingToPercision(payout/invest*100, 3);
					
					OrderTicketDetailModel detail = new OrderTicketDetailModel(local_m,product, totalInvestment, invest, totalPrice, null, payout);
					modelList.add(detail);
					
					allTotalInvestment +=totalInvestment;
					allTotalPrice +=totalPrice;
					allInvest +=invest;
				}
			
			map.put(product, modelList);
			modelList = new ArrayList<>();
		}
		
		allPayout = allTotalPrice-allInvest;
		//allPayoutrate = allInvest==0.0 ? 0 :NumberUtil.getNumberAccordingToPercision(allPayout/allInvest*100, 3);
		
		OrderTicketDetailModel total = new OrderTicketDetailModel(0,"合计", allTotalInvestment, allInvest, allTotalPrice, allPayoutrate, allPayout);
		map.put("total", total);
		
		return map;
	}

	
	
	// 获得查询的起止时间
		private Map<String, Object> getStartAndEndDate(String year, String month,String day) throws Exception {
			String startDate = null;// 开始时间
			String endDate = null;// 结束时间
			Map<String, Object> date = new HashMap<>();

			if (StringUtils.isEmpty(month)) {// 月份为空	
				
				startDate = year + "-01-01 12:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
				calendar.add(Calendar.YEAR, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 12:00:00";
			} else if (StringUtils.isEmpty(day)) {// day为空
										
				startDate = year + "-" + month + "-01 12:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.MONTH, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 12:00:00";
			} else {//均有值
				
				startDate = year + "-" + month + "-" + day + " 12:00:00";
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
				calendar.add(Calendar.DATE, 1);
				endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 12:00:00";
			}
			date.put("startDate", startDate);
			date.put("endDate", endDate);
		
			return date;
		}	
	
	
}
