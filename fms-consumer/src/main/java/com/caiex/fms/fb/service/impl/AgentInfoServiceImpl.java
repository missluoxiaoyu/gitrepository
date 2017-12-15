package com.caiex.fms.fb.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import com.caiex.dbservice.basketball.api.BkAgentProApi;
import com.caiex.dbservice.basketball.entity.BasketBallAgentInfo;
import com.caiex.dbservice.basketball.entity.BasketBallOrderTicket;
import com.caiex.dbservice.basketball.entity.BasketBallOrderTicketInfo;
import com.caiex.dbservice.currentdb.api.AgentInfoProApi;
import com.caiex.dbservice.currentdb.api.OrderTicketApi;
import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.currentdb.entity.AgentInfoDetail;
import com.caiex.dbservice.currentdb.entity.OrderTicket;
import com.caiex.dbservice.currentdb.entity.OrderTicketInfo;
import com.caiex.dbservice.historydb.api.AgentInfoProHisApi;
import com.caiex.dbservice.historydb.api.BkAgentProHisApi;
import com.caiex.dbservice.historydb.api.OrderTicketHistoryApi;
import com.caiex.dbservice.model.AgentInfoModel;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.fms.fb.service.AgentInfoService;
import com.caiex.fms.redis.RedisUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;
import com.mysql.fabric.xmlrpc.base.Array;


@Service
public class AgentInfoServiceImpl implements AgentInfoService {
	private final static Logger log = Logger.getLogger(AgentInfoServiceImpl.class);
	private static final String[] MONEY_KEYS = { "PRESTORE", "Investment","RECYCLECHANNELS","bkInvestment" ,"bkRECYCLECHANNELS","fbCxBonus","bkCxBonus"};


	@Autowired
	private AgentInfoProApi agentInfoApi;
	
	@Autowired
	private AgentInfoProHisApi agentInfoHistoryApi;
	
	
	@Autowired
	private BkAgentProApi basketBallAgentApi;
	
	@Autowired
	private BkAgentProHisApi basketBallAgentHistoryApi;
	
	@Autowired
	private OrderTicketApi orderTicketApi;
	
	@Autowired
	private OrderTicketHistoryApi orderTicketHistoryApi;
	
	@Autowired
	private RedisUtil redisDao;
	
	


	@SuppressWarnings("rawtypes")
	@Override
	public Map queryChannel(String year, String month, String day) throws Exception {
		Map resultMap = new HashMap<>();
		
		Map<String, Object> date = getStartAndEndDate(year, month, day);// 获取时间

		List<AgentInfoModel> agentInfoModels = getInfo(date);
		resultMap.put("agentInfoModels", agentInfoModels);
		return resultMap;
	}


	

	
	public List<AgentInfoModel> getInfo(Map<String, Object> date){
	
		List<AgentInfo> all = agentInfoApi.queryAllAgents();
		
		AgentInfoModel params = new AgentInfoModel();
		params.setStartDate((String)date.get("startDate"));
		params.setEndDate((String)date.get("endDate"));
		
		List<AgentInfoModel> models = new ArrayList<>();
	
		for (AgentInfo agentInfo : all) {
			params.setAgentId(agentInfo.getAgentNum());
			AgentInfoModel result = new AgentInfoModel();
			
			getFb(params, result);
			getbk(params, result);
			result.setAgentSell(agentInfo.getAgentSell());
			result.setAgentCode(agentInfo.getAgentCode());
			result.setPrestoreAlarm(NumberUtil.getNumberAccordingToPercision(agentInfo.getPrestoreAlarm(),3));
			result.setAgentId(agentInfo.getAgentNum());
			result.setUrl(agentInfo.getUrl());
			result.setPassword(agentInfo.getPassword());
			result.setAgentName(agentInfo.getAgentName());
			result.setPrestore(NumberUtil.getNumberAccordingToPercision(agentInfo.getPrestore(),3));// 预存款
			models.add(result);

		}
		
		return models;

	}

	
	public AgentInfoModel getFb(AgentInfoModel params,AgentInfoModel result){
		
		List<OrderTicket> orderTickets = agentInfoApi.queryTicketInfo_id(params);// 通过时间和 agentid查询票
		List<OrderTicket> orderTickethisss =agentInfoHistoryApi.queryTicketInfo_id(params);
		orderTickets.addAll(orderTickethisss);
		
		if(orderTickets ==null || orderTickets.size()==0){
			return result;
		}else{
			
		OrderTicket orderTradePrice = agentInfoApi.queryBatchPrice(params);// 查询交易成功的交易额
		OrderTicket orderTradePriceHis = agentInfoHistoryApi.queryBatchPrice(params);// 查询交易成功的交易额				
		
		Double tradePrice = orderTradePrice==null?0: orderTradePrice.getTrade_price();
		tradePrice +=orderTradePriceHis==null?0: orderTradePriceHis.getTrade_price();// 总的交易额
		
		Double totalPrice = queryTotalPrice(orderTickets);//总的回收额
		Double winMoney = queryWinMoney(orderTickets);// 总的最大中奖额
		
		Integer orderAmount = getOrderAmount(params);//订单数
		int userAmount = obtainAccountSize(orderTickets);//用户数
		
		
		result.setCxBonusFb(getFbCxBonus(orderTickets));
		result.setUserAmountFb(userAmount);// 用户数
		result.setOrderAmountFb(orderAmount);// 订单数
		result.setTradePriceFb(NumberUtil.getNumberAccordingToPercision(tradePrice, 3));// 交易额
		result.setWinMoneyFb(NumberUtil.getNumberAccordingToPercision(winMoney,3));// 最大中奖额
		result.setProfitFb(NumberUtil.getNumberAccordingToPercision(winMoney - tradePrice,3));// 盈余
		result.setProfitabilityFb(tradePrice==0.0?0.0:NumberUtil.getNumberAccordingToPercision(result.getProfitFb() / result.getTradePriceFb() * 100, 3));// 盈余率
		result.setRecyclePriceFb(NumberUtil.getNumberAccordingToPercision(totalPrice, 3));
		
		}
		return result;
		
	}
	
	
	public AgentInfoModel getbk(AgentInfoModel params,AgentInfoModel result){
		
		List<BasketBallOrderTicket> orderTickets = basketBallAgentApi.queryTicketInfo_id(params);// 通过时间和 agentid查询票
		orderTickets.addAll(basketBallAgentHistoryApi.queryTicketInfo_id(params));
		if(orderTickets ==null || orderTickets.size()==0){
			return result;
		}else{
			
		
		BasketBallOrderTicket orderTradePrice = basketBallAgentApi.queryBatchPrice(params);// 查询交易成功的交易额
		BasketBallOrderTicket orderTradePriceHis = basketBallAgentHistoryApi.queryBatchPrice(params);// 查询交易成功的交易额	
		
		Double tradePrice = 0.0;
		if(orderTradePrice !=null){
			tradePrice += orderTradePrice.getTrade_price();
		}
		if(orderTradePriceHis !=null){
			tradePrice +=orderTradePriceHis.getTrade_price();
		}
	
		 // 总的交易额
		Double totalPrice = queryBkTotalPrice(orderTickets);//回收额
		Double winMoney = queryBkWinMoney(orderTickets);// 总的最大中奖额
		
		Integer orderAmount = getBkOrderAmount(params);//订单数
		int userAmount = obtainBkAccountSize(orderTickets);//用户数
		
		result.setCxBonusBk(getBkCxBonus(orderTickets));
		result.setUserAmountBk(userAmount);// 用户数
		result.setOrderAmountBk(orderAmount);// 订单数
		result.setTradePriceBk(NumberUtil.getNumberAccordingToPercision(tradePrice, 3));// 交易额
		result.setWinMoneyBk(NumberUtil.getNumberAccordingToPercision(winMoney,3));// 最大中奖额
		result.setProfitBk(NumberUtil.getNumberAccordingToPercision(winMoney - tradePrice,3));// 盈余
		result.setProfitabilityBk( tradePrice ==0.0?0.0:NumberUtil.getNumberAccordingToPercision(result.getProfitBk() / result.getTradePriceBk() * 100, 3));// 盈余率
		result.setRecyclePriceBk(NumberUtil.getNumberAccordingToPercision(totalPrice, 3));
		}
		
		return result;
	}
	
	

	
	
	
	public List<Integer> getDistinctAgent(List<Integer> agentids){
		List<Integer> newList = new ArrayList<>();
		
		for (Integer agentid : agentids) {
			if(!newList.contains(agentid)){
				newList.add(agentid);
			}
		}
		return newList;
	}
	

	


	
	// 封装没交易的渠道信息
		public AgentInfoModel noTradeModel(List<AgentInfo> infos) {
			AgentInfoModel model = new AgentInfoModel();
			Double totalPrestore = 0.0;
			Double totalPrestoreAlarm =0.0;
			
			for (AgentInfo info : infos) {
				Double preStore = getPreStore(info);
				totalPrestore += preStore;
				totalPrestoreAlarm += info.getPrestoreAlarm();
			
			}
			model.setPrestore(NumberUtil.getNumberAccordingToPercision(totalPrestore,3));
			model.setPrestoreAlarm(NumberUtil.getNumberAccordingToPercision(totalPrestoreAlarm, 3));
			
			return model;
		}
	
	
	


	
	
	
	// 用户数
	public int obtainBkAccountSize(List<BasketBallOrderTicket> orderTickets) {
		List<BasketBallOrderTicketInfo> ticketInfos = basketBallAgentApi.queryBatch(orderTickets);
		List<BasketBallOrderTicketInfo> ticketInfoHis = basketBallAgentHistoryApi.queryBatch(orderTickets);
		ticketInfos.addAll(ticketInfoHis);
		Set<Object> set = new HashSet<Object>();
		for (BasketBallOrderTicketInfo orderTicketInfo : ticketInfos) {
			set.add(orderTicketInfo.getUid());
		}
		return set.size();
	}
	
	
	
	// 该时间段内订单数
	public int getBkOrderAmount(AgentInfoModel params) {
			int total = 0;
			total = basketBallAgentApi.getOrderAmount(params) +basketBallAgentHistoryApi.getOrderAmount(params);
			return total;
	}
	
	
	// 该时间段内订单数
	public int getOrderAmount(AgentInfoModel params) {
		int total = 0;
		total = agentInfoApi.getOrderAmount(params) +agentInfoHistoryApi.getOrderAmount(params);
		return total;
	}

	
	// 用户数
		public int obtainAccountSize(List<OrderTicket> orderTickets) {
		
			List<OrderTicketInfo> ticketInfos = agentInfoApi.queryBatch(orderTickets);
			List<OrderTicketInfo> ticketInfoHis = agentInfoHistoryApi.queryBatch(orderTickets);
			ticketInfos.addAll(ticketInfoHis);
			Set<Object> set = new HashSet<Object>();
			for (OrderTicketInfo orderTicketInfo : ticketInfos) {
				set.add(orderTicketInfo.getUid());
			}
			return set.size();
			
		}

		
		
		
		public BigDecimal getFbCxBonus(List<OrderTicket> orderTickets){
			List<OrderTicketInfo> ticketInfos = agentInfoApi.queryBatch(orderTickets);
			List<OrderTicketInfo> ticketInfoHis = agentInfoHistoryApi.queryBatch(orderTickets);
			ticketInfos.addAll(ticketInfoHis);
			Double Bonus = 0.0;
			for (OrderTicketInfo info : ticketInfos) {
				Bonus += info.getCxBonus()==null?0.0:info.getCxBonus().doubleValue();
			}
			Bonus = NumberUtil.getNumberAccordingToPercision(Bonus, 3);
			return new BigDecimal(Bonus.toString());
		}
		
		
		public BigDecimal getBkCxBonus(List<BasketBallOrderTicket> orderTickets){
			
			List<BasketBallOrderTicketInfo> ticketInfos = basketBallAgentApi.queryBatch(orderTickets);
			List<BasketBallOrderTicketInfo> ticketInfoHis = basketBallAgentHistoryApi.queryBatch(orderTickets);
			ticketInfos.addAll(ticketInfoHis);
			Double Bonus = 0.0;
			for (BasketBallOrderTicketInfo info : ticketInfos) {
				Bonus += info.getCxBonus()==null?0.0:info.getCxBonus().doubleValue();
			}
			Bonus = NumberUtil.getNumberAccordingToPercision(Bonus, 3);
			return new BigDecimal(Bonus.toString());
			
		}
		
		
		
		
		
		
		
	// 查询TotalPrice
	private Double queryTotalPrice(List<OrderTicket> orderTickets) {
	
		Double recyclePrice  = 0.0;
		for (OrderTicket order : orderTickets) {
			recyclePrice +=order.getRecyclePrice() ==null ?0.0:order.getRecyclePrice().doubleValue();
		}
	
		return recyclePrice;
	}

	
	
	
	// 查询TotalPrice
		private Double queryBkTotalPrice(List<BasketBallOrderTicket> orderTickets) {
			Double recyclePrice  = 0.0;
			for (BasketBallOrderTicket order : orderTickets) {
				recyclePrice +=order.getRecyclePrice() ==null ?0.0:order.getRecyclePrice().doubleValue();
			}
			
			return recyclePrice;
		}
		
		
		
		// 查询最大中奖额
			private Double queryBkWinMoney(List<BasketBallOrderTicket> orderTickets) {
				Double winMoney  = 0.0;
				for (BasketBallOrderTicket order : orderTickets) {
					winMoney +=order.getWinMoney() ==null ?0.0:order.getWinMoney().doubleValue();
				}
			
				return winMoney;
			}
		
	
	//// 查询最大中奖额
	private Double queryWinMoney(List<OrderTicket> orderTickets) {
		
		Double winMoney  = 0.0;
		for (OrderTicket order : orderTickets) {
			winMoney +=order.getWinMoney() ==null ?0.0:order.getWinMoney().doubleValue();
		}
	
		return winMoney;
	}
	
	
	
	
	
	
	// 获得查询的起止时间
	private Map<String, Object> getStartAndEndDate(String year, String month,String day) throws Exception {
		String startDate = null;// 开始时间
		String endDate = null;// 结束时间
		Map<String, Object> date = new HashMap<>();

		if (StringUtils.isEmpty(month)) {// 月份为空	
			
			startDate = year + "-01-01 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(year, new String[] { "yyyy" }));
			calendar.add(Calendar.YEAR, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 24:00:00";
		} else if (StringUtils.isEmpty(day)) {// day为空
									
			startDate = year + "-" + month + "-01 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.MONTH, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 24:00:00";
		} else {//均有值
			startDate = year + "-" + month + "-" + day + " 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.DATE, 0);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 24:00:00";
		}
		date.put("startDate", startDate);
		date.put("endDate", endDate);
		log.info("开始时间"+startDate);
		log.info("结束时间"+endDate);
		return date;
	}

	

	
	
	
	
	
	
	// 导出excel表
	@SuppressWarnings("unchecked")
	@Override
	public Response exportchannelStatisticsExcel(HttpServletRequest request,HttpServletResponse response, String year, String month, String day)
			throws Exception {
		Response res = new Response();
		try {
			
		Map<String, Object> map = queryChannel(year, month, day);
		AgentInfoModel model = (AgentInfoModel) map.get("model");
		List<AgentInfoModel> agentInfoModels = (List<AgentInfoModel>) map.get("agentInfoModels");
		List<AgentInfoModel> list = new ArrayList<AgentInfoModel>();
		String[] strKeyArray = null;
		model.setAgentName("合计 Total");
		list.add(model);
		list.add(null);
		list.add(null);
		for (int i = 0; i < agentInfoModels.size(); i++) {
			AgentInfoModel agentInfoModel = agentInfoModels.get(i);
			
			if (agentInfoModel.getAgentSell() == agentInfoModel.getAgentId() ) {
				
				agentInfoModel.setAgentSellMessage("开售");
			} else {
				agentInfoModel.setAgentSellMessage("停售");
			}
			list.add(agentInfoModels.get(i));
		}
		
		Resource resource = new ClassPathResource("/excel/agent-demo.xls");
		InputStream in = resource.getInputStream();
		
		
		strKeyArray = new String[] { "agentName", "agentCode","prestore","prestoreAlarm", 
				"recyclePrice","agentSellMessage","userAmountFb", "orderAmountFb",
				"tradePriceFb","cxBonusFb", "winMoneyFb", "profitFb",
				"profitabilityFb",  "userAmountBk", "orderAmountBk",
				"tradePriceBk","cxBonusBk", "winMoneyBk", "profitBk",
				"profitabilityBk",  };

		HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, AgentInfoModel.class);
		PoiUtil.returnExcel(response, workbook, "AGENT");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
		
	}





	@Override
	public List<AgentInfoDetail> queryPreStoreDetail(String agentCode) {
		
		List<AgentInfoDetail> list = agentInfoApi.queryPreStoreDetail(agentCode);
		
		List<AgentInfoDetail> detailList = new ArrayList<>();
			
		Double flag = 0.0;
		for (int i = 0; i < list.size(); i++) {
					
			if(list.get(i).getOldPreStore()!=null && !list.get(i).getOldPreStore().equals(flag)){
				
				if(list.get(i).getNewPreStore()!=null && !list.get(i).getNewPreStore().equals(flag)){
					detailList.add(list.get(i));
				}
			}
					
		}
		
		return detailList;
		
	}



	// 从redis里面查询
		public double getPreStore(AgentInfo agentInfo) {
		
			final String[] keys = new String[MONEY_KEYS.length];
			for (int t = 0; t < MONEY_KEYS.length; t++) {
				keys[t] = agentInfo.getAgentNum()==null?"":agentInfo.getAgentNum() + MONEY_KEYS[t];
			}
			
			List<Object> keyss = redisDao.mGet(keys);
			log.info(agentInfo.getAgentNum()==null?"":agentInfo.getAgentNum()+"渠道管理redis里查询PRESTORE，Investment，RECYCLECHANNELS bkInvestment bkRECYCLECHANNELS 的值分别为"+keyss.get(0)+","+keyss.get(1)+","+keyss.get(2)+","+keyss.get(3)+","+keyss.get(4));
			Double preStore = calculatePro((String)keyss.get(0),(String)keyss.get(1),(String)keyss.get(2),(String)keyss.get(3),(String)keyss.get(4),(String)keyss.get(5),(String)keyss.get(6));
			return preStore;
		}
		
	
		
		private Double calculatePro(String preStore, String investment, String recycle,String bkInvestment,String bkRecycle,String fbCxBonus, String bkCxBonus) {
			Double preStoreValue = StringUtils.isEmpty(preStore) ? 0.0 : Double.valueOf(preStore);
			Double investmentValue = StringUtils.isEmpty(investment) ? 0.0 : Double.valueOf(investment);
			Double bkInvestmentValue = StringUtils.isEmpty(bkInvestment) ? 0.0 : Double.valueOf(bkInvestment);
			Double fbCxBonusValue = StringUtils.isEmpty(fbCxBonus) ? 0.0 : Double.valueOf(fbCxBonus);
			Double bkCxBonusValue = StringUtils.isEmpty(bkCxBonus) ? 0.0 : Double.valueOf(bkCxBonus);
			
			Double recycleValue = StringUtils.isEmpty(recycle) ? 0.0 : Double.valueOf(recycle);
			Double bkRecycleValue = StringUtils.isEmpty(bkRecycle) ? 0.0 : Double.valueOf(bkRecycle);
			return preStoreValue - (investmentValue+bkInvestmentValue) - (fbCxBonusValue+bkCxBonusValue) +(recycleValue+bkRecycleValue) ;
			
		}




	
		
		@Override
		public Map<String, Object>  queryRecycleDetail(String agentId,String startDate,String endDate) {
			List<OrderTicketModel> listh = orderTicketHistoryApi.queryRecycleDetail(agentId, startDate, endDate);
			List<OrderTicketModel> listc = orderTicketApi.queryRecycleDetail(agentId, startDate, endDate);
			List<OrderTicketModel> list = new ArrayList<>();
			list.addAll(listc);
			list.addAll(listh);
			
			Double totalMoney = 0.0;
			
			for (OrderTicketModel orderTicketModel : list) {
				AgentInfo info = agentInfoApi.queryAgentInfoByOrderTicket(orderTicketModel.getAgent_id());
				orderTicketModel.setAgentName(info.getAgentName());
				
				if(orderTicketModel.getRecyclePrice() == null){
					log.error("足球方案号为"+orderTicketModel.getTkId()+"回收价格为null");
				}else{
					totalMoney +=orderTicketModel.getRecyclePrice().doubleValue();
					log.info("查询到足球方案号为"+orderTicketModel.getTkId()+"回收价格"+orderTicketModel.getRecyclePrice());
				}
			}
			
			OrderTicketModel total =  new OrderTicketModel();
			total.setRecyclePrice(new BigDecimal(NumberUtil.getNumberAccordingToPercision(totalMoney, 3)+""));
			
			Map<String, Object> map = new HashMap<>();
			map.put("total",total);
			map.put("list", list);
			return map;
		}
		
	
	
		@Override
		public void recycleDetailExcel(String agentId,String startDate,String endDate,HttpServletResponse response) throws Exception{
			Map<String, Object> map = new HashMap<>();
			map = queryRecycleDetail(agentId, startDate, endDate);
			List<OrderTicketModel> recycleList = (List<OrderTicketModel>) map.get("list");
			OrderTicketModel total = (OrderTicketModel) map.get("total");
	
			total.setRecyclePrice(total.getRecyclePrice() ==null?new BigDecimal("0"):new BigDecimal(new String(NumberUtil.getNumberAccordingToPercision(total.getRecyclePrice().doubleValue(), 3)+"")));
			List<OrderTicketModel> list = new ArrayList<OrderTicketModel>();
			
			String[] strKeyArray = null;
		
			list.add(total);
			list.add(null);
			list.add(null);
			list.addAll(recycleList);
			
			Resource resource = new ClassPathResource("/excel/agent_recycle_detail.xls");
			InputStream in = resource.getInputStream();
			
			strKeyArray = new String[] {"agentName","tkId","recyclePrice","recycleTime"};

			HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray,OrderTicketModel.class);
			PoiUtil.returnExcel(response, workbook, "agent_recycle_detail");
			
		}
		
		
		
	

}
