package com.caiex.fms.bookie.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.caiexbooker.api.fms.AgentBookieProService;
import com.caiex.dbservice.caiexbooker.entity.VirBetAgentInfo;
import com.caiex.dbservice.caiexbooker.entity.VirBetAgentInfoDetail;
import com.caiex.dbservice.currentdb.entity.AgentInfoDetail;
import com.caiex.dbservice.historydb.api.bookiefms.AgentBookieProHisService;
import com.caiex.dbservice.model.AgentInfoModel;
import com.caiex.dbservice.model.BookieOrderModel;
import com.caiex.fms.bookie.service.BookieAgentService;
import com.caiex.fms.redis.RedisUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PoiUtil;
@Service
public class BookieAgentServiceImpl implements BookieAgentService{

	private static final String[] MONEY_KEYS = {"PRESTORE", "fbInvestment","fbPayout"};
	@Autowired
	private AgentBookieProService agentBookieProService;
	
	@Autowired
	private AgentBookieProHisService agentBookieProHisService;
	
	@Autowired
	private RedisUtil redisDao;
	

	@Override
	public Map queryChannel(String year, String month, String day) throws Exception {

		Map<String, Object> date = getStartAndEndDate(year, month, day);// 获取时间
		return getInfo(date);
	}


	

	
	public Map<String, Object> getInfo(Map<String, Object> date){
	
		List<VirBetAgentInfo> all = agentBookieProService.queryAllAgents();
		
		AgentInfoModel params = new AgentInfoModel();
		params.setStartDate((String)date.get("startDate"));
		params.setEndDate((String)date.get("endDate"));
		
		AgentInfoModel total = new AgentInfoModel();
		
		List<AgentInfoModel> models = new ArrayList<>();
	
		for (VirBetAgentInfo agentInfo : all) {
			params.setAgentId(agentInfo.getAgentNum());
			AgentInfoModel result = new AgentInfoModel();
			
			getFb(params, result);
		
			result.setAgentSell(agentInfo.getAgentSell());
			result.setAgentCode(agentInfo.getAgentCode());
			result.setPrestoreAlarm(NumberUtil.getNumberAccordingToPercision(agentInfo.getPrestoreAlarm(),3));
			result.setAgentId(agentInfo.getAgentNum());
			result.setUrl(agentInfo.getUrl());
			result.setPassword(agentInfo.getPassword());
			result.setAgentName(agentInfo.getAgentName());
			result.setPrestore(NumberUtil.getNumberAccordingToPercision(getPreStore(agentInfo),3));// 预存款
			result.setAgentRate(agentInfo.getAgentRate());
			
			models.add(result);
		
			total.setPrestore(NumberUtil.getNumberAccordingToPercision(add(total.getPrestore(), result.getPrestore()), 3));
			total.setPrestoreAlarm(add(total.getPrestoreAlarm(), result.getPrestoreAlarm()));
			total.setUserAmountFb(add(total.getUserAmountFb(), result.getUserAmountFb()));// 用户数
			total.setOrderAmountFb(add(total.getOrderAmountFb(), result.getOrderAmountFb()));// 订单数
			
			total.setTradePriceFb(add(total.getTradePriceFb(), result.getTradePriceFb()));// 交易额
			total.setWinMoneyFb(add(total.getWinMoneyFb(), result.getWinMoneyFb()));// 最大中奖额
		}
		
	
		
		total.setProfitFb(NumberUtil.getNumberAccordingToPercision(total.getWinMoneyFb()-total.getTradePriceFb(), 3));
		total.setProfitabilityFb(total.getTradePriceFb()==0.0?0.0:NumberUtil.getNumberAccordingToPercision(total.getProfitFb()/ (total.getTradePriceFb()*100), 3));
	
		Map resultMap = new HashMap<>();
		resultMap.put("agentInfoModels", models);
		resultMap.put("total", total);
		
		return resultMap;
		
	
	}

	
	public Double add(Double addend,Double augend){
		if(addend==null){
			addend=0.0;
		}
		if(augend==null){
			augend=0.0;
		}
		return addend+augend;
	}
	
	
	public Integer add(Integer addend,Integer augend){
		if(addend==null){
			addend=0;
		}
		if(augend==null){
			augend=0;
		}
		return addend+augend;
	}
	
	
	
	
	
	
	public AgentInfoModel getFb(AgentInfoModel params,AgentInfoModel result){
		
		List<BookieOrderModel> orders = agentBookieProService.queryTicketByTimeAndAgentId(params);// 通过时间和 agentid查询票
		List<BookieOrderModel> orderhisss =agentBookieProHisService.queryTicketByTimeAndAgentId(params);
		orders.addAll(orderhisss);
		
		if(orders ==null || orders.size()==0){
			return result;
		}else{
			
		Double investPrice = queryInvestPrice(orders);
		
		Double winMoney = queryWinMoney(orders);// 总的最大中奖额
		
		Integer orderAmount = orders.size();//订单数
		int userAmount = obtainAccountSize(orders);//用户数
		
	
		result.setUserAmountFb(userAmount);// 用户数
		result.setOrderAmountFb(orderAmount);// 订单数
		result.setTradePriceFb(NumberUtil.getNumberAccordingToPercision(investPrice, 3));// 交易额
		result.setWinMoneyFb(NumberUtil.getNumberAccordingToPercision(winMoney,3));// 最大中奖额
		result.setProfitFb(NumberUtil.getNumberAccordingToPercision(winMoney - result.getTradePriceFb(),3));// 盈余
		result.setProfitabilityFb(investPrice==0.0?0.0:NumberUtil.getNumberAccordingToPercision(result.getProfitFb() / (result.getTradePriceFb() * 100), 3));// 盈余率
		
		}
		return result;
		
	}
	
	
	

	
	

	private Double queryInvestPrice(List<BookieOrderModel> orders) {
		Double investPrice  = 0.0;
		for (BookieOrderModel order : orders) {
			investPrice +=order.getTotalInvestment() ==null ?0.0:order.getTotalInvestment().doubleValue();
		}
	
		return investPrice;
	}





		// 用户数
		public int obtainAccountSize(List<BookieOrderModel> orders) {
	
			Set<Object> set = new HashSet<Object>();
			for (BookieOrderModel orderTicketInfo : orders) {
				set.add(orderTicketInfo.getUid());
			}
			return set.size();
			
		}

		
		
	
	
		
	
	//// 查询最大中奖额
	private Double queryWinMoney(List<BookieOrderModel> orders) {
		
		Double winMoney  = 0.0;
		for (BookieOrderModel order : orders) {
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
		
		return date;
	}

	

	
	
	
	
	
	
	// 导出excel表
	//@SuppressWarnings("unchecked")
	@Override
	public void exportchannelStatisticsExcel(HttpServletRequest request,HttpServletResponse response, String year, String month, String day)
			throws Exception {
	
			
		Map<String, Object> map = queryChannel(year, month, day);
		AgentInfoModel model = (AgentInfoModel) map.get("total");
		List<AgentInfoModel> agentInfoModels = (List<AgentInfoModel>) map.get("agentInfoModels");
		List<AgentInfoModel> list = new ArrayList<AgentInfoModel>();
		String[] strKeyArray = null;
		model.setAgentName("合计 Total");
		list.add(model);
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
		
		Resource resource = new ClassPathResource("/excel/bookie-channel.xls");
		InputStream in = resource.getInputStream();
		
		
		strKeyArray = new String[] { "agentName", "agentCode","prestore","prestoreAlarm", 
				"agentRate","agentSellMessage","userAmountFb", "orderAmountFb",
				"tradePriceFb", "winMoneyFb", "profitFb",
				"profitabilityFb"  };

		HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, AgentInfoModel.class);
		PoiUtil.returnExcel(response, workbook, "AGENT");
		
		
	}





	@Override
	public List<VirBetAgentInfoDetail> queryPreStoreDetail(String agentCode) {
		
		List<VirBetAgentInfoDetail> list = agentBookieProService.queryPreStoreDetail(agentCode);
		
		List<VirBetAgentInfoDetail> detailList = new ArrayList<>();
			
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
		public double getPreStore(VirBetAgentInfo agentInfo) {
		
			final String[] keys = new String[MONEY_KEYS.length];
			for (int t = 0; t < MONEY_KEYS.length; t++) {
				keys[t] = agentInfo.getAgentNum()==null?"":agentInfo.getAgentNum() + MONEY_KEYS[t];
			}
			
			List<Object> keyss = redisDao.bookiemGet(keys);
			
			Double preStore = calculatePro((String)keyss.get(0),(String)keyss.get(1),(String)keyss.get(2));
			return preStore;
		}
		
	
	





private Double calculatePro(String preStore, String investment,String Payout) {
		Double preStoreValue = StringUtils.isEmpty(preStore) ? 0.0 : Double.valueOf(preStore);
		Double investmentValue = StringUtils.isEmpty(investment) ? 0.0 : Double.valueOf(investment);
		Double payoutValue = StringUtils.isEmpty(Payout) ? 0.0 : Double.valueOf(Payout);
		
		return preStoreValue + investmentValue - payoutValue;
		
	}
	
	
	
	
	
}
