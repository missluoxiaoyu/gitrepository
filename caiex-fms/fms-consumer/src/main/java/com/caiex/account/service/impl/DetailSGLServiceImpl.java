package com.caiex.account.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import com.caiex.account.entity.MatchInfoHis;
import com.caiex.account.entity.OrderTicketDetailHis;
import com.caiex.account.entity.OrderTicketInfo;
import com.caiex.account.entity.OrderTicketMatchesHis;
import com.caiex.account.mapper.MatchInfoHisMapper;
import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.caiex.account.mapper.OrderTicketDetailMapper;
import com.caiex.account.mapper.OrderTicketInfoMapper;
import com.caiex.account.mapper.OrderTicketMatchesHisMapper;
import com.caiex.account.model.OrderTicketDetailFinancialModel;
import com.caiex.account.model.OrderTicketDetailSGLModel;
import com.caiex.account.model.OrderTicketFsglModel;

import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;
import com.mysql.fabric.xmlrpc.base.Array;

@Service
public class DetailSGLServiceImpl  {
	private final static Logger log = Logger.getLogger(DetailSGLServiceImpl.class);

	/*@Autowired
	private MatchInfoHisMapper matchInfoHisMapper;

	@Autowired
	private OrderTicketDetailHisMapper orderTicketDetailHisMapper;

	@Autowired
	private OrderTicketMatchesHisMapper orderTicketMatchesHisMapper;

	@Autowired
	private OrderTicketInfoMapper orderTicketInfoMapper;

	@Override
	public Map<String, Object> queryAll(String year, String month, String day) throws Exception {
		Map<String, Object> date = getStartAndEndDate(year, month, day);
		Map<String, Object> result = new HashMap<>();
		
		List<MatchInfoHis> matchInfoList = new ArrayList<MatchInfoHis>();
		List<OrderTicketDetailHis> detailHisList = orderTicketDetailHisMapper.queryDetailHisByPayouttime(date);// 根据时间查询 detail表
		
		if (detailHisList.size() > 0) {
			List<OrderTicketMatchesHis> ticketMatchList = orderTicketMatchesHisMapper.queryBySid(detailHisList);// 根据detail 查询比赛的信息
			List<Integer> midList = new ArrayList<>();
			for (int i = 0; i < ticketMatchList.size(); i++) {
				OrderTicketMatchesHis orderTicketMatches = ticketMatchList.get(i);
				MatchInfoHis info = matchInfoHisMapper.queryBtachList(orderTicketMatches);// 根据orderTicketMatch 查询 matchhis										
				if (info != null) {
					if(! midList.contains(info.getMid())){
						midList.add(info.getMid());
						matchInfoList.add(info);
					}
				}
			}
			result= getModels( matchInfoList, ticketMatchList);
			return result; 
		}

		return result;
	}

	public Map<String, Object>  getModels(List<MatchInfoHis> matchInfoList,List<OrderTicketMatchesHis> ticketMatchList) {
		 String [] products = {"HAD","HHAD","HAFU","TTG","CRS"};
		 Map<String, Object> result = new HashMap<>();
		 List<OrderTicketDetailSGLModel> list = new ArrayList<>();
		for (int i = 0; i < matchInfoList.size(); i++) {
			MatchInfoHis infoModel = matchInfoList.get(i);
			Map<String, Object> models = new HashMap<>();
			for (int j = 0; j < products.length; j++) {
				
				OrderTicketFsglModel model =   getModelByProduct(products[j], infoModel, ticketMatchList);
				models.put(model.getProduct(), model);
			}
			OrderTicketDetailSGLModel orderTicketDetailSGLModel = getjson(models, infoModel);
			list.add(orderTicketDetailSGLModel);
		}
	
		result.put("modelList", list);
		result.put("total", getTotal(ticketMatchList));
		
		return result;
	}
	
	public OrderTicketDetailSGLModel getTotal(List<OrderTicketMatchesHis> ticketMatchList){
		 String [] products = {"HAD","HHAD","HAFU","TTG","CRS"};
		 Map<String, Object> models = new HashMap<>();
			MatchInfoHis infoModel = new MatchInfoHis();
		for (String product : products) {
			OrderTicketFsglModel model =   getModelByProduct(product, infoModel, ticketMatchList);
			models.put(model.getProduct(), model);
		}
		OrderTicketDetailSGLModel orderTicketDetailSGLModel = getjson(models, infoModel);
		return orderTicketDetailSGLModel;
	}
	
	
	
	public OrderTicketDetailSGLModel getjson(Map<String, Object> models ,MatchInfoHis infoModel){
		 OrderTicketFsglModel modelHAD = (OrderTicketFsglModel) models.get("HAD");
		 OrderTicketFsglModel modelHHAD = (OrderTicketFsglModel) models.get("HHAD");
		 OrderTicketFsglModel modelHAFU = (OrderTicketFsglModel) models.get("HAFU");
		 OrderTicketFsglModel modelTTG = (OrderTicketFsglModel) models.get("TTG");
		 OrderTicketFsglModel modelCRS = (OrderTicketFsglModel) models.get("CRS");
		
		     String match_code = infoModel.getMatch_code();
		     Timestamp match_date = infoModel.getMatch_date();
			 String match_league = infoModel.getMatch_league();
			 String home_team = infoModel .getHome_team();
			 String away_team = infoModel.getAway_team();
			 String home_score_half = infoModel.getHome_score_half();
			 String away_score_half = infoModel.getAway_score_half();
			 String home_score = infoModel.getHome_score();
			 String away_score = infoModel.getAway_score();
		 
		 	  double totalInvestmentHAD = modelHAD.getTotalInvestment();
			  double investHAD = modelHAD.getInvest();
			  double totalPriceHAD = modelHAD.getTotalPrice();
			  double payoutRateHAD = modelHAD.getPayoutRate();
			  double winpriceHAD = modelHAD .getWinprice();
			  double averageHAD = modelHAD.getAverage();
			
			  double totalInvestmentHHAD = modelHHAD.getTotalInvestment();
			  double investHHAD = modelHHAD.getInvest();
			  double totalPriceHHAD= modelHHAD.getTotalPrice();
			  double payoutRateHHAD = modelHHAD.getPayoutRate();
			  double winpriceHHAD = modelHHAD .getWinprice();
			  double averageHHAD = modelHHAD.getAverage();
			  
			  double totalInvestmentHAFU = modelHAFU.getTotalInvestment();
			  double investHAFU = modelHAFU.getInvest();
			  double totalPriceHAFU= modelHAFU.getTotalPrice();
			  double payoutRateHAFU = modelHAFU.getPayoutRate();
			  double winpriceHAFU = modelHAFU .getWinprice();
			  double averageHAFU = modelHAFU.getAverage();

			  double totalInvestmentTTG = modelTTG.getTotalInvestment();
			  double investTTG = modelTTG.getInvest();
			  double totalPriceTTG= modelTTG.getTotalPrice();
			  double payoutRateTTG = modelTTG.getPayoutRate();
			  double winpriceTTG = modelTTG .getWinprice();
			  double averageTTG = modelTTG.getAverage();

			  double totalInvestmentCRS = modelCRS.getTotalInvestment();
			  double investCRS = modelCRS.getInvest();
			  double totalPriceCRS= modelCRS.getTotalPrice();
			  double payoutRateCRS = modelCRS.getPayoutRate();
			  double winpriceCRS = modelCRS .getWinprice();
			  double averageCRS = modelCRS.getAverage();
	
		 OrderTicketDetailSGLModel orderTicketDetailSGLModel = new OrderTicketDetailSGLModel(match_code, match_date, match_league, home_team, away_team, home_score_half, away_score_half, home_score, away_score, totalInvestmentHAD, investHAD, totalPriceHAD, payoutRateHAD, winpriceHAD, averageHAD, totalInvestmentHHAD, investHHAD, totalPriceHHAD, payoutRateHHAD, winpriceHHAD, averageHHAD, totalInvestmentHAFU, investHAFU, totalPriceHAFU, payoutRateHAFU, winpriceHAFU, averageHAFU, totalInvestmentTTG, investTTG, totalPriceTTG, payoutRateTTG, winpriceTTG, averageTTG, totalInvestmentCRS, investCRS, totalPriceCRS, payoutRateCRS, winpriceCRS, averageCRS);
		 return orderTicketDetailSGLModel;
	}
	
	
	
	
	
	public OrderTicketFsglModel getModelByProduct(String product,MatchInfoHis infoModel ,List<OrderTicketMatchesHis> ticketMatchList ){
		infoModel.setProduct(product);
	
		List<OrderTicketMatchesHis> NewTicketMatchList = new ArrayList<OrderTicketMatchesHis>();
		for (int j = 0; j < ticketMatchList.size(); j++) {
			
			if(infoModel.getMatch_code() == null){
				if (infoModel.getProduct().equals(ticketMatchList.get(j).getL_prod()) ) {
					NewTicketMatchList.add(ticketMatchList.get(j));
				}
			}else {
				if (infoModel.getMatch_code().equals(ticketMatchList.get(j).getL_code()) && infoModel.getProduct().equals(ticketMatchList.get(j).getL_prod())) {
					NewTicketMatchList.add(ticketMatchList.get(j));
				}
			} 
		}
		
		infoModel.setSids(NewTicketMatchList);
		double totalInvestment = 0;
		Double invest =0.0;
		Double totalPrice =0.0;
		Double winprice = 0.0;
		Double average =0.0;
		Double payoutRate = 0.0;
		if (NewTicketMatchList.size() > 0) {
			OrderTicketDetailHis detailInvest = orderTicketDetailHisMapper.detailHisQueryInvest(infoModel);
			OrderTicketDetailHis detailTotalPrice = orderTicketDetailHisMapper.detailHisQueryTotalPrice(infoModel);
			OrderTicketDetailHis detailWinInvest = orderTicketDetailHisMapper.detailHisQueryWinInvest(infoModel);
			// 单关HAD的totalInvestment
			OrderTicketDetailHis detailTotalInvestment = orderTicketDetailHisMapper.detailHisQueryTotalInvestment(infoModel);
			 
			 totalInvestment = detailTotalInvestment == null ?0.0:detailTotalInvestment.getInv_allup();
			 invest = detailInvest == null ?0.0:NumberUtil.getNumberAccordingToPercision(detailInvest.getPrice_allup(),3);
			 totalPrice = detailTotalPrice == null ?0.0: NumberUtil.getNumberAccordingToPercision(detailTotalPrice.getTotal_price() ,3);
			 winprice = detailWinInvest == null ?0.0:NumberUtil.getNumberAccordingToPercision(detailWinInvest.getPrice_allup() ,3);
			 average = winprice == 0.0 ? 0 :NumberUtil.getNumberAccordingToPercision(totalPrice / winprice, 3);
			 payoutRate= invest.equals(0.0) ? 0.0 :NumberUtil.getNumberAccordingToPercision((totalPrice - invest) / invest * 100, 3);
			
		}	
		OrderTicketFsglModel  model= new OrderTicketFsglModel(product,totalInvestment, invest, totalPrice, payoutRate, winprice, average);
		return model;
	}
	
	
	

	@Override
	public Response detailSGLExcel(HttpServletResponse response, String year,String month, String day) {
		Response res = new Response();
		try {
		Map<String, Object> map = queryAll(year, month, day);
		List<OrderTicketDetailSGLModel> modelList = (List<OrderTicketDetailSGLModel>) map.get("modelList");
		OrderTicketDetailSGLModel total = (OrderTicketDetailSGLModel) map.get("total");
		
		List<OrderTicketDetailSGLModel> list = new ArrayList<>();
		String strTemplate = "/opt/FMS/consumer/template/dailySGL-demo.xls";// 模板位置
		
		list.add(null);
		total.setMatch_code(null);
		total.setMatch_date(null);
		total.setMatch_league(null);
		total.setHome_team(null);
		total.setHome_score("合计");
		list.add(total);
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(null);
		for (OrderTicketDetailSGLModel model : modelList) {
			model.setHome_team(model.getHome_team()+"vs"+model.getAway_team());
			model.setHome_score(model.getHome_score_half()+":"+model.getAway_score_half() +"/"+model.getHome_score()+":"+model.getAway_score());
		}
		
		list.addAll(modelList);
		
		String[] strKeyArray = new String[] {"match_code", "match_date","match_league", "home_team", "home_score",
											"totalInvestmentHAD","investHAD","totalPriceHAD","winpriceHAD","averageHAD","payoutRateHAD",
											"totalInvestmentHHAD","investHHAD","totalPriceHHAD","winpriceHHAD","averageHHAD","payoutRateHHAD",
											"totalInvestmentHAFU","investHAFU","totalPriceHAFU","winpriceHAFU","averageHAFU","payoutRateHAFU",
											"totalInvestmentTTG","investTTG","totalPriceTTG","winpriceTTG","averageTTG","payoutRateTTG",
											"totalInvestmentCRS","investCRS","totalPriceCRS","winpriceCRS","averageCRS","payoutRateCRS" };
		
		HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, OrderTicketDetailSGLModel.class);
		PoiUtil.returnExcel(response, workbook, "single");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
		
		
	}

	private Map<String, Object> getStartAndEndDate(String year, String month,
			String day) throws Exception {
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
			calendar.setTime(DateUtils.parseDate(startDate,	new String[] { "yyyy-MM-dd hh:mm:ss" }));
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
		return params;
	}*/
}