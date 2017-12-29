package com.caiex.fms.bookie.service.impl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.caiexbooker.api.fms.SingleBookieProService;
import com.caiex.dbservice.caiexbooker.entity.VirBetMatchInfo;
import com.caiex.dbservice.caiexbooker.entity.VirBetMatchProduct;
import com.caiex.dbservice.caiexbooker.entity.VirBetOrderTicketMatches;
import com.caiex.dbservice.currentdb.entity.MatchInfo;
import com.caiex.dbservice.currentdb.entity.MatchProduct;
import com.caiex.dbservice.currentdb.entity.OrderTicketMatches;
import com.caiex.dbservice.historydb.api.bookiefms.SingleBookieProHisService;
import com.caiex.dbservice.model.OrderTicketDetailModel;
import com.caiex.dbservice.model.OrderTicketDetailSGLModel;
import com.caiex.fms.bookie.service.BookieSGLService;
import com.caiex.fms.utils.MergeUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;
@Service
public class BookieSGLServiceImpl implements BookieSGLService{
	
	@Autowired
	private SingleBookieProService singleBookieProService;
	
	@Autowired
	private SingleBookieProHisService singleBookieProHisService;
	
	String [] products = {"HAD","HHAD","HAFU","TTG","CRS"};
	
	@Autowired
	MergeUtil mergeUtil;
	
	
	@Override
	public Map<String, Object> queryAll(String year, String month, String day) throws Exception {
		 Map<String, Object> result = new HashMap<>();
		
		 Map<String, Object> date =  getStartAndEndDate(year, month, day);
		 
		List<VirBetOrderTicketMatches> current = (List<VirBetOrderTicketMatches>) singleBookieProService.queryAll(date);
		List<VirBetOrderTicketMatches> his = (List<VirBetOrderTicketMatches>) singleBookieProHisService.queryAll(date);
	
		
		List<VirBetMatchInfo> matchListCurrent = getMatchInfo(current);
		List<VirBetMatchInfo> matchListHis = getMatchInfo(his);
		
		Map<String, Object> mapCurrent= getModels(matchListCurrent, current, true);
		Map<String, Object> mapHis= getModels(matchListHis, his, false);
		
		OrderTicketDetailSGLModel totalCur = (OrderTicketDetailSGLModel) mapCurrent.get("total");
		OrderTicketDetailSGLModel totalHis =  (OrderTicketDetailSGLModel) mapHis.get("total");
	
		OrderTicketDetailSGLModel total = new OrderTicketDetailSGLModel();
		total = (OrderTicketDetailSGLModel) mergeUtil.merge(totalCur, totalHis, total);//合并total
		mergeUtil.getNumberAccordingToPercision(total);
		calculatePayoutRate(total);
	
		List<OrderTicketDetailSGLModel> listAll = new ArrayList<>();
		List<OrderTicketDetailSGLModel> listCur= (List<OrderTicketDetailSGLModel>) mapCurrent.get("modelList");
		List<OrderTicketDetailSGLModel> listHis= (List<OrderTicketDetailSGLModel>)mapHis.get("modelList");
		listAll.addAll(listCur);
		listAll.addAll(listHis);
		
		List<OrderTicketDetailSGLModel> noTradeMatches =getNoTradeMatcheInfo(date, listAll);
		listAll.addAll(noTradeMatches);
		
		 Collections.sort(listAll,new Comparator<OrderTicketDetailSGLModel>(){  
	            public int compare(OrderTicketDetailSGLModel arg0, OrderTicketDetailSGLModel arg1) {  
	                return arg0.match_code.compareTo(arg1.match_code);  
	            }  
	        });  
		
		
		result.put("total", total);
		result.put("modelList", listAll);
		return result;
	}
	
	
	public List<OrderTicketDetailSGLModel>  getNoTradeMatcheInfo(Map<String, Object> date , List<OrderTicketDetailSGLModel> listAll){
		List<OrderTicketDetailSGLModel> noTradeMatches = new ArrayList<>();
		
		List<VirBetMatchInfo> infoList = new ArrayList<>();
		List<VirBetMatchProduct> matchProductList = singleBookieProService.queryByPayout(date);
		if(matchProductList.size()>0){
			 infoList = singleBookieProService.queryBatchListByMid(matchProductList);
		}
		String allCode="";
		for (OrderTicketDetailSGLModel list : listAll) {
			allCode=allCode+","+list.getMatch_code();
		}
		
		for (VirBetMatchInfo info : infoList) {
			String matchCode= info.getMatch_code();
			if(!allCode.contains(matchCode)){
				OrderTicketDetailSGLModel model = new OrderTicketDetailSGLModel();
			
				model.setMatch_code(info.getMatch_code());
				model.setMatch_date(info.getMatch_date());
				model.setMatch_league(info.getMatch_league());
				model.setHome_team(info.getHome_team());
				model.setAway_team(info.getAway_team());
				model.setHome_score(info.getHome_score());
				model.setHome_score_half(info.getHome_score_half());
				model.setAway_score(info.getAway_score());
				model.setAway_score_half(info.getAway_score_half());
				
				noTradeMatches.add(model);
			}
		}
		
		return noTradeMatches;
	}
	
	
	
	
	
	
	public OrderTicketDetailSGLModel calculatePayoutRate(OrderTicketDetailSGLModel model){
		
		Double payoutRateHAD = model.getTotalInvestmentHAD()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceHAD() - model.getTotalInvestmentHAD()) / model.getTotalInvestmentHAD() * 100, 3);
		model.setPayoutRateHAD(payoutRateHAD);
		Double payoutRateHHAD = model.getTotalInvestmentHHAD()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceHHAD() - model.getTotalInvestmentHHAD()) / model.getTotalInvestmentHHAD() * 100, 3);
		model.setPayoutRateHHAD(payoutRateHHAD);
		Double payoutRateHAFU = model.getTotalInvestmentHAFU()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceHAFU() - model.getTotalInvestmentHAFU()) / model.getTotalInvestmentHAFU() * 100, 3);
		model.setPayoutRateHAFU(payoutRateHAFU);
		Double payoutRateTTG = model.getTotalInvestmentTTG()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceTTG() - model.getTotalInvestmentTTG()) / model.getTotalInvestmentTTG() * 100, 3);
		model.setPayoutRateTTG(payoutRateTTG);
		Double payoutRateCRS = model.getTotalInvestmentCRS()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceCRS() - model.getTotalInvestmentCRS()) / model.getTotalInvestmentCRS()* 100, 3);
		model.setPayoutRateCRS(payoutRateCRS);
	
		Double averageHAD = model.getWinpriceHAD()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceHAD()/model.getWinpriceHAD(), 3);
		model.setAverageHAD(averageHAD);
		Double averageHHAD = model.getWinpriceHHAD()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceHHAD()/model.getWinpriceHHAD(), 3);
		model.setAverageHHAD(averageHHAD);
		Double averageHAFU =model.getWinpriceHAFU()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceHAFU()/model.getWinpriceHAFU(), 3);
		model.setAverageHAFU(averageHAFU);
		Double averageTTG  =model.getWinpriceTTG()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceTTG()/model.getWinpriceTTG(), 3);
		model.setAverageTTG(averageTTG);
		Double averageCRS  = model.getWinpriceCRS()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceCRS()/model.getWinpriceCRS(), 3);
		model.setAverageCRS(averageCRS);
		return model;
	}
	
	
	public List<VirBetMatchInfo>  getMatchInfo(List<VirBetOrderTicketMatches> list){
		List<VirBetMatchInfo> matchInfoList = new ArrayList<>();
		List<Integer> midList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			
			VirBetOrderTicketMatches orderTicketMatches = list.get(i);
			VirBetMatchInfo info = singleBookieProService.queryBatchList(orderTicketMatches);// 根据orderTicketMatch 查询 matchhis										
			
			if (info != null) {
				if(! midList.contains(info.getMid())){
					midList.add(info.getMid());
					matchInfoList.add(info);
				}
			}
		}
	
		return matchInfoList;
	}
	
	
	

	public Map<String, Object>  getModels(List<VirBetMatchInfo> matchInfoList,List<VirBetOrderTicketMatches> ticketMatchList,boolean flag) throws Exception {
		
		 Map<String, Object> result = new HashMap<>();
		 List<OrderTicketDetailSGLModel> list = new ArrayList<>();
		for (int i = 0; i < matchInfoList.size(); i++) {
			VirBetMatchInfo infoModel = matchInfoList.get(i);
			Map<String, Object> models = new HashMap<>();
			for (int j = 0; j < products.length; j++) {
			
				if(flag){
					OrderTicketDetailModel model =  singleBookieProService.getModelByProduct(products[j], infoModel, ticketMatchList);
					//log.info("dailySingle统计当前库"+products[j]);
				
					models.put(model.getProduct(), model);
				}else{//历史

					OrderTicketDetailModel model =  singleBookieProHisService.getModelByProduct(products[j], infoModel, ticketMatchList);
					//log.info("dailySingle统计历史库"+products[j]);
					
					models.put(model.getProduct(), model);
				}
			}
			
			OrderTicketDetailSGLModel sModel = getjson(models, infoModel);
			
			calculatePayoutRate(sModel);
			list.add(sModel);
		}
	
		OrderTicketDetailSGLModel total = new OrderTicketDetailSGLModel();
		
		result.put("modelList", list);
		result.put("total", mergeUtil.mergeList(list,total));
		
		return result;
	}
	
	


	
	
	
	

	
	
	
	public OrderTicketDetailSGLModel getjson(Map<String, Object> models ,VirBetMatchInfo infoModel){
		OrderTicketDetailModel modelHAD = (OrderTicketDetailModel) models.get("HAD");
		OrderTicketDetailModel modelHHAD = (OrderTicketDetailModel) models.get("HHAD");
		OrderTicketDetailModel modelHAFU = (OrderTicketDetailModel) models.get("HAFU");
		OrderTicketDetailModel modelTTG = (OrderTicketDetailModel) models.get("TTG");
		OrderTicketDetailModel modelCRS = (OrderTicketDetailModel) models.get("CRS");
		
		     String match_code = infoModel.getMatch_code();
		     Timestamp match_date = infoModel.getMatch_date();
			 String match_league = infoModel.getMatch_league();
			 String home_team = infoModel .getHome_team();
			 String away_team = infoModel.getAway_team();
			 String home_score_half = infoModel.getHome_score_half();
			 String away_score_half = infoModel.getAway_score_half();
			 String home_score = infoModel.getHome_score();
			 String away_score = infoModel.getAway_score();
		 
		 	  Double totalInvestmentHAD =NumberUtil.getNumberAccordingToPercision(modelHAD.getTotalInvestment(), 3);
			  Double totalPriceHAD = NumberUtil.getNumberAccordingToPercision(modelHAD.getTotalPrice(),3);
			  Double payoutRateHAD = NumberUtil.getNumberAccordingToPercision(modelHAD.getPayoutRate(),3);
			  Double winpriceHAD = NumberUtil.getNumberAccordingToPercision(modelHAD .getWinprice(),3);
			  Double averageHAD = NumberUtil.getNumberAccordingToPercision(modelHAD.getAverage(),3);
			
			  Double totalInvestmentHHAD = NumberUtil.getNumberAccordingToPercision(modelHHAD.getTotalInvestment(),3);
			  Double totalPriceHHAD= NumberUtil.getNumberAccordingToPercision(modelHHAD.getTotalPrice(),3);
			  Double payoutRateHHAD = NumberUtil.getNumberAccordingToPercision(modelHHAD.getPayoutRate(),3);
			  Double winpriceHHAD = NumberUtil.getNumberAccordingToPercision(modelHHAD .getWinprice(),3);
			  Double averageHHAD = NumberUtil.getNumberAccordingToPercision(modelHHAD.getAverage(),3);
			  
			  Double totalInvestmentHAFU = NumberUtil.getNumberAccordingToPercision(modelHAFU.getTotalInvestment(),3);
			  Double totalPriceHAFU= NumberUtil.getNumberAccordingToPercision(modelHAFU.getTotalPrice(),3);
			  Double payoutRateHAFU = NumberUtil.getNumberAccordingToPercision(modelHAFU.getPayoutRate(),3);
			  Double winpriceHAFU = NumberUtil.getNumberAccordingToPercision(modelHAFU .getWinprice(),3);
			  Double averageHAFU = NumberUtil.getNumberAccordingToPercision(modelHAFU.getAverage(),3);

			  Double totalInvestmentTTG =NumberUtil.getNumberAccordingToPercision(modelTTG.getTotalInvestment(),3);
			  Double totalPriceTTG= NumberUtil.getNumberAccordingToPercision(modelTTG.getTotalPrice(),3);
			  Double payoutRateTTG = NumberUtil.getNumberAccordingToPercision(modelTTG.getPayoutRate(),3);
			  Double winpriceTTG = NumberUtil.getNumberAccordingToPercision(modelTTG .getWinprice(),3);
			  Double averageTTG = NumberUtil.getNumberAccordingToPercision(modelTTG.getAverage(),3);

			  Double totalInvestmentCRS = NumberUtil.getNumberAccordingToPercision(modelCRS.getTotalInvestment(),3);
			  Double totalPriceCRS= NumberUtil.getNumberAccordingToPercision(modelCRS.getTotalPrice(),3);
			  Double payoutRateCRS = NumberUtil.getNumberAccordingToPercision(modelCRS.getPayoutRate(),3);
			  Double winpriceCRS = NumberUtil.getNumberAccordingToPercision(modelCRS .getWinprice(),3);
			  Double averageCRS = NumberUtil.getNumberAccordingToPercision(modelCRS.getAverage(),3);
	
		 OrderTicketDetailSGLModel orderTicketDetailSGLModel = new OrderTicketDetailSGLModel(match_code, match_date, match_league, home_team, away_team, home_score_half, away_score_half, home_score, away_score, totalInvestmentHAD, null, null, totalPriceHAD, payoutRateHAD, winpriceHAD, averageHAD, totalInvestmentHHAD,null, null, totalPriceHHAD, payoutRateHHAD, winpriceHHAD, averageHHAD, totalInvestmentHAFU, null, null, totalPriceHAFU, payoutRateHAFU, winpriceHAFU, averageHAFU, totalInvestmentTTG, null, null, totalPriceTTG, payoutRateTTG, winpriceTTG, averageTTG, totalInvestmentCRS, null, null, totalPriceCRS, payoutRateCRS, winpriceCRS, averageCRS);
				 return orderTicketDetailSGLModel;
	}
	
	
	
	
	
	@Override
	public Response detailSGLExcel(HttpServletResponse response, String year,String month, String day) {
		Response res = new Response();
		try {
		Map<String, Object> map = queryAll(year, month, day);
		List<OrderTicketDetailSGLModel> modelList = (List<OrderTicketDetailSGLModel>) map.get("modelList");
		OrderTicketDetailSGLModel total = (OrderTicketDetailSGLModel) map.get("total");
		
		List<OrderTicketDetailSGLModel> list = new ArrayList<>();

		Resource resource = new ClassPathResource("/excel/bookie-single.xls");
		InputStream in = resource.getInputStream();
		
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
											"totalInvestmentHAD","totalPriceHAD","winpriceHAD","averageHAD","payoutRateHAD",
											"totalInvestmentHHAD","totalPriceHHAD","winpriceHHAD","averageHHAD","payoutRateHHAD",
											"totalInvestmentHAFU","totalPriceHAFU","winpriceHAFU","averageHAFU","payoutRateHAFU",
											"totalInvestmentTTG","totalPriceTTG","winpriceTTG","averageTTG","payoutRateTTG",
											"totalInvestmentCRS","totalPriceCRS","winpriceCRS","averageCRS","payoutRateCRS" };
		
		HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, OrderTicketDetailSGLModel.class);
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
			startDate = year + "-01-01 14:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
			calendar.add(Calendar.YEAR, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
		} else if (StringUtils.isEmpty(day)) {
			startDate = year + "-" + month + "-01 14:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate,	new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.MONTH, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
		} else {
			startDate = year + "-" + month + "-" + day + " 14:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.DATE, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 14:00:00";
		}
		
//	log.info("dailySingle统计的起止时间为startDate"+startDate+"endDate"+endDate);
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return params;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
