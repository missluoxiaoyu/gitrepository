package com.caiex.fms.bk.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import com.caiex.dbservice.basketball.api.BkSingleProService;
import com.caiex.dbservice.basketball.api.BkMatchInfoApi;
import com.caiex.dbservice.basketball.api.BkMatchProductApi;
import com.caiex.dbservice.basketball.entity.BasketBallMatchInfo;
import com.caiex.dbservice.basketball.entity.BasketBallMatchProduct;
import com.caiex.dbservice.basketball.entity.BasketBallOrderTicketMatches;
import com.caiex.dbservice.historydb.api.BkSingleProHisService;
import com.caiex.dbservice.model.BasketBallSGLModel;
import com.caiex.dbservice.model.OrderTicketDetailModel;
import com.caiex.dbservice.model.OrderTicketDetailSGLModel;
import com.caiex.fms.bk.service.BkSingleService;
import com.caiex.fms.utils.MergeUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;

@Service
public class BkDailySingleServiceImpl implements BkSingleService{

	@Autowired
	private BkMatchInfoApi basketBallMatchInfoApi;
	
	@Autowired
	private BkMatchProductApi basketBallMatchProductApi;
	
	@Autowired
	private BkSingleProService  basketBallDetailSGLService;
	
	@Autowired
	private BkSingleProHisService basketBallDetailSGLHistoryService;
	
	@Autowired
	private MergeUtil mergeUtil;
	
	String [] products = {"HDC","HILO","MNL","WNM"};
	
	@Override
	public Map<String, Object> queryAll(String year, String month, String day)throws Exception {
		 Map<String, Object> result = new HashMap<>();
		 Map<String, Object> date =  getStartAndEndDate(year, month, day);
		 
		List<BasketBallOrderTicketMatches> current = (List<BasketBallOrderTicketMatches>) basketBallDetailSGLService.queryAll(date);
		List<BasketBallOrderTicketMatches> his = (List<BasketBallOrderTicketMatches>) basketBallDetailSGLHistoryService.queryAll(date);
		

		List<BasketBallMatchInfo> matchListCurrent = getMatchInfo(current);
		List<BasketBallMatchInfo> matchListHis = getMatchInfo(his);
		
		Map<String, Object> mapCurrent= getModels(matchListCurrent, current, true);
		Map<String, Object> mapHis= getModels(matchListHis, his, false);
		
		 BasketBallSGLModel totalCur = (BasketBallSGLModel) mapCurrent.get("total");
		BasketBallSGLModel totalHis =  (BasketBallSGLModel) mapHis.get("total");
	
		BasketBallSGLModel total = new BasketBallSGLModel();
		total = (BasketBallSGLModel) mergeUtil.merge(totalCur, totalHis, total);//合并total
		mergeUtil.getNumberAccordingToPercision(total);
		calculatePayoutRate(total);
	
		List<BasketBallSGLModel> listAll = new ArrayList<>();
		List<BasketBallSGLModel> listCur= (List<BasketBallSGLModel>) mapCurrent.get("modelList");
		List<BasketBallSGLModel> listHis= (List<BasketBallSGLModel>)mapHis.get("modelList");
		listAll.addAll(listCur);
		listAll.addAll(listHis);
		
		List<BasketBallSGLModel> noTradeMatches =getNoTradeMatcheInfo(date, listAll);
		listAll.addAll(noTradeMatches);
		
		 Collections.sort(listAll,new Comparator<BasketBallSGLModel>(){  
	            public int compare(BasketBallSGLModel arg0, BasketBallSGLModel arg1) {  
	                return arg0.match_code.compareTo(arg1.match_code);  
	            }  
	        });  
		
		
		result.put("total", total);
		result.put("modelList", listAll);
		return result;
		

	}

	
	@Override
	public Response detailSGLExcel(HttpServletResponse response, String year,String month, String day) throws Exception {
	 Response res = new Response();
		try {
		Map<String, Object> map = queryAll(year, month, day);
		List<BasketBallSGLModel> modelList = (List<BasketBallSGLModel>) map.get("modelList");
		BasketBallSGLModel total = (BasketBallSGLModel) map.get("total");
		
		List<BasketBallSGLModel> list = new ArrayList<>();
		//String strTemplate = "/opt/FMS/fms/excel/bkdailySGL-demo.xls";// 模板位置
		
		Resource resource = new ClassPathResource("/excel/bkdailySGL-demo.xls");
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
		for (BasketBallSGLModel model : modelList) {
			model.setHome_team(model.getAway_team()+"vs"+model.getHome_team());
			model.setHome_score(model.getAway_score()+":"+model.getHome_score());
		}
		
		list.addAll(modelList);
		
		String[] strKeyArray = new String[] {"match_code", "match_date","match_league", "home_team", "home_score",
											"totalInvestmentHDC","investHDC","bonusHDC","totalPriceHDC","winpriceHDC","averageHDC","payoutRateHDC",
											"totalInvestmentHILO","investHILO","bonusHILO","totalPriceHILO","winpriceHILO","averageHILO","payoutRateHILO",
											"totalInvestmentMNL","investMNL","bonusMNL","totalPriceMNL","winpriceMNL","averageMNL","payoutRateMNL",
											"totalInvestmentWNM","investWNM","bonusWNM","totalPriceWNM","winpriceWNM","averageWNM","payoutRateWNM"};
		
		HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, BasketBallSGLModel.class);
		PoiUtil.returnExcel(response, workbook, "single");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
	
	}
	
	
	 public BasketBallSGLModel calculatePayoutRate(BasketBallSGLModel model){
		
		Double payoutRateHDC = model.getInvestHDC()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceHDC() - model.getInvestHDC()) / model.getInvestHDC() * 100, 3);
		model.setPayoutRateHDC(payoutRateHDC);
		Double payoutRateHILO = model.getInvestHILO()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceHILO() - model.getInvestHILO()) / model.getInvestHILO() * 100, 3);
		model.setPayoutRateHILO(payoutRateHILO);
		Double payoutRateMNL = model.getInvestMNL()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceMNL() - model.getInvestMNL()) / model.getInvestMNL() * 100, 3);
		model.setPayoutRateMNL(payoutRateMNL);
		Double payoutRateWNM = model.getInvestWNM()==0?0:NumberUtil.getNumberAccordingToPercision((model.getTotalPriceWNM() - model.getInvestWNM()) / model.getInvestWNM() * 100, 3);
		model.setPayoutRateWNM(payoutRateWNM);
		
	
		Double averageHDC = model.getWinpriceHDC()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceHDC()/model.getWinpriceHDC(), 3);
		model.setAverageHDC(averageHDC);
		Double averageHILO = model.getWinpriceHILO()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceHILO()/model.getWinpriceHILO(), 3);
		model.setAverageHILO(averageHILO);
		Double averageMNL =model.getWinpriceMNL()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceMNL()/model.getWinpriceMNL(), 3);
		model.setAverageMNL(averageMNL);
		Double averageWNM  =model.getWinpriceWNM()==0?0:NumberUtil.getNumberAccordingToPercision(model.getTotalPriceWNM()/model.getWinpriceWNM(), 3);
		model.setAverageWNM(averageWNM);
		
		return model;
	}
	 
	
	
	
	
	
	
	
	
	 public List<BasketBallSGLModel>  getNoTradeMatcheInfo(Map<String, Object> date , List<BasketBallSGLModel> listAll){
		List<BasketBallSGLModel> noTradeMatches = new ArrayList<>();
		
		List<BasketBallMatchInfo> infoList = new ArrayList<>();
		List<BasketBallMatchProduct> matchProductList = basketBallMatchProductApi.queryByPayout(date);
		if(matchProductList.size()>0){
			 infoList = basketBallMatchInfoApi.queryBatchListByMid(matchProductList);
		}
		String allCode="";
		for (BasketBallSGLModel list : listAll) {
			allCode=allCode+","+list.getMatch_code();
		}
		
		for (BasketBallMatchInfo info : infoList) {
			String matchCode= info.getMatch_code()+"";
			if(!allCode.contains(matchCode)){
				BasketBallSGLModel model = new BasketBallSGLModel();
			
				model.setMatch_code(info.getMatch_code()+"");
				model.setMatch_date(info.getMatch_date());
				model.setMatch_league(info.getMatch_league());
				model.setHome_team(info.getHome_team());
				model.setAway_team(info.getAway_team());
				model.setHome_score(info.getHome_score());
				model.setAway_score(info.getAway_score());
				
				noTradeMatches.add(model);
			}
		}
		
		return noTradeMatches;
	}
	
	
	
	
	
	
	public Map<String, Object>  getModels(List<BasketBallMatchInfo> matchInfoList,List<BasketBallOrderTicketMatches> ticketMatchList,boolean flag) {
		
		 Map<String, Object> result = new HashMap<>();
		 List<BasketBallSGLModel> list = new ArrayList<>();
		for (int i = 0; i < matchInfoList.size(); i++) {
			BasketBallMatchInfo infoModel = matchInfoList.get(i);
			Map<String, Object> models = new HashMap<>();
			for (String product :products) {
			
				if(flag){
					OrderTicketDetailModel model =  basketBallDetailSGLService.getModelByProduct(product, infoModel, ticketMatchList);
					models.put(model.getProduct(), model);
				}else{//历史
					OrderTicketDetailModel model =  basketBallDetailSGLHistoryService.getModelByProduct(product, infoModel, ticketMatchList);
					models.put(model.getProduct(), model);
				}
			}
			
			
			BasketBallSGLModel sModel = getjson(models, infoModel);
			
			calculatePayoutRate(sModel);
			list.add(sModel);
		}
	
		
		result.put("modelList", list);
		result.put("total", getTotal(list));
		
		return result;
	}
	
	
	public BasketBallSGLModel getTotal(List<BasketBallSGLModel> lists){

		 Double totalInvestmentHDC = 0.0;
		  Double investHDC = 0.0;
		  Double totalPriceHDC = 0.0;
		  Double payoutRateHDC = 0.0;
		  Double winpriceHDC = 0.0;
		  Double averageHDC=0.0;
		  Double bonusHDC = 0.0;
		
		  Double totalInvestmentHILO = 0.0;
		  Double investHILO = 0.0;
		  Double totalPriceHILO= 0.0;
		  Double payoutRateHILO = 0.0;
		  Double winpriceHILO = 0.0;
		  Double averageHILO = 0.0;
		  Double bonusHILO = 0.0;
		 
		  Double totalInvestmentMNL = 0.0;
		  Double investMNL = 0.0;
		  Double totalPriceMNL= 0.0;
		  Double payoutRateMNL = 0.0;
		  Double winpriceMNL = 0.0;
		  Double averageMNL = 0.0;
		  Double bonusMNL = 0.0;
		  
		  Double totalInvestmentWNM = 0.0;
		  Double investWNM = 0.0;
		  Double totalPriceWNM= 0.0;
		  Double payoutRateWNM = 0.0;
		  Double winpriceWNM = 0.0;
		  Double averageWNM = 0.0;
		  Double bonusWNM = 0.0;
		
		for (BasketBallSGLModel list : lists) {
			
			totalInvestmentHDC +=list.getTotalInvestmentHDC ();
			investHDC += list.getInvestHDC ();
			  totalPriceHDC  += list.getTotalPriceHDC();
			  winpriceHDC  += list .getWinpriceHDC();
			 averageHDC  += list.getAverageHDC();
			 bonusHDC  +=list.getBonusHDC ();
			 
			 
			   totalInvestmentHILO += list.getTotalInvestmentHILO();
			  investHILO += list.getInvestHILO();
			   totalPriceHILO += list.getTotalPriceHILO();
			   winpriceHILO += list .getWinpriceHILO();
			  averageHILO += list.getAverageHILO();
			  bonusHILO  +=list.getBonusHILO();
			  
			  
			  totalInvestmentMNL += list.getTotalInvestmentMNL();
			   investMNL += list.getInvestMNL();
			  totalPriceMNL += list.getTotalPriceMNL();
			   winpriceMNL += list .getWinpriceMNL();
			   averageMNL += list.getAverageMNL();
			   bonusMNL  +=list.getBonusMNL();
			   
			   
			   totalInvestmentWNM += list.getTotalInvestmentWNM();
			  investWNM += list.getInvestWNM();
			   totalPriceWNM  += list.getTotalPriceWNM();
			   winpriceWNM += list .getWinpriceWNM();
			   averageWNM += list.getAverageWNM();
			   bonusWNM +=list.getBonusWNM();
			 
		}
		BasketBallSGLModel total = new BasketBallSGLModel(null, null, null, null, null, null, null, totalInvestmentHDC, investHDC+bonusHDC, bonusHDC, totalPriceHDC, payoutRateHDC, winpriceHDC, averageHDC, totalInvestmentHILO, investHILO+bonusHILO, bonusHILO, totalPriceHILO, payoutRateHILO, winpriceHILO, averageHILO, totalInvestmentMNL, investMNL+bonusMNL, bonusMNL, totalPriceMNL, payoutRateMNL, winpriceMNL, averageMNL, totalInvestmentWNM, investWNM+bonusWNM, bonusWNM, totalPriceWNM, payoutRateWNM, winpriceWNM, averageWNM);
				return total;
	}
	
	
	
	
	
	
	public BasketBallSGLModel getjson(Map<String, Object> models ,BasketBallMatchInfo infoModel){
		OrderTicketDetailModel modelHDC = (OrderTicketDetailModel) models.get("HDC");
		OrderTicketDetailModel modelHILO = (OrderTicketDetailModel) models.get("HILO");
		OrderTicketDetailModel modelMNL = (OrderTicketDetailModel) models.get("MNL");
		OrderTicketDetailModel modelWNM = (OrderTicketDetailModel) models.get("WNM");
		
		     Integer match_code = infoModel.getMatch_code();
		     Date match_date = infoModel.getMatch_date();
			 String match_league = infoModel.getMatch_league();
			 String home_team = infoModel .getHome_team();
			 String away_team = infoModel.getAway_team();
			 String home_score = infoModel.getHome_score();
			 String away_score = infoModel.getAway_score();
		 
		 	  Double totalInvestmentHDC =modelHDC.getTotalInvestment();
			  Double investHDC = modelHDC.getInvest();
			  Double totalPriceHDC = modelHDC.getTotalPrice();
			  Double payoutRateHDC = modelHDC.getPayoutRate();
			  Double winpriceHDC = modelHDC.getWinprice();
			  Double averageHDC = modelHDC.getAverage();
			  Double bonusHDC = NumberUtil.getNumberAccordingToPercision(modelHDC.getBonus(),3);
			  
			  
			  Double totalInvestmentHILO = modelHILO.getTotalInvestment();
			  Double investHILO =modelHILO.getInvest();
			  Double totalPriceHILO=modelHILO.getTotalPrice(); 
			  Double payoutRateHILO =modelHILO.getPayoutRate(); 
			  Double winpriceHILO =modelHILO.getWinprice(); 
			  Double averageHILO =modelHILO.getAverage();
			  Double bonusHILO = NumberUtil.getNumberAccordingToPercision(modelHILO.getBonus(),3);
			  
			  Double totalInvestmentMNL = modelMNL.getTotalInvestment();
			  Double investMNL =modelMNL.getInvest();
			  Double totalPriceMNL=modelMNL.getTotalPrice(); 
			  Double payoutRateMNL =modelMNL.getPayoutRate(); 
			  Double winpriceMNL =modelMNL.getWinprice(); 
			  Double averageMNL = modelMNL.getAverage();
			  Double bonusMNL = NumberUtil.getNumberAccordingToPercision(modelMNL.getBonus(),3);

			  Double totalInvestmentWNM =modelWNM.getTotalInvestment();
			  Double investWNM = modelWNM.getInvest();
			  Double totalPriceWNM=modelWNM.getTotalPrice();
			  Double payoutRateWNM = modelWNM.getPayoutRate();
			  Double winpriceWNM=modelWNM.getWinprice();
			  Double averageWNM= modelWNM.getAverage();
			  Double bonusWNM = NumberUtil.getNumberAccordingToPercision(modelWNM.getBonus(),3);

			  BasketBallSGLModel model = new BasketBallSGLModel(match_code+"", match_date, match_league, home_team, away_team, home_score, away_score, totalInvestmentHDC, investHDC+bonusHDC, bonusHDC, totalPriceHDC, payoutRateHDC, winpriceHDC, averageHDC, totalInvestmentHILO, investHILO+bonusHILO, bonusHILO, totalPriceHILO, payoutRateHILO, winpriceHILO, averageHILO, totalInvestmentMNL, investMNL+bonusMNL, bonusMNL, totalPriceMNL, payoutRateMNL, winpriceMNL, averageMNL, totalInvestmentWNM, investWNM+bonusWNM, bonusWNM, totalPriceWNM, payoutRateWNM, winpriceWNM, averageWNM);
			  return model;
	}
	
	
	
	
	public List<BasketBallMatchInfo>  getMatchInfo(List<BasketBallOrderTicketMatches> list){
		List<BasketBallMatchInfo> matchInfoList = new ArrayList<>();
		List<Integer> midList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			BasketBallOrderTicketMatches orderTicketMatches = list.get(i);
			
			BasketBallMatchInfo info = basketBallMatchInfoApi.queryBatchList(orderTicketMatches);// 根据orderTicketMatch 查询 matchhis										
			if (info != null) {
				if(! midList.contains(info.getMid())){
					midList.add(info.getMid());
					matchInfoList.add(info);
				}
			}
		}
	
		return matchInfoList;
	}
	
	
	@SuppressWarnings("unused")
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
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return params;
	}

}
