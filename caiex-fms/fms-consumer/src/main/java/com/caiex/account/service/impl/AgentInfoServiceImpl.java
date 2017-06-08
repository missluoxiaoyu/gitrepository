package com.caiex.account.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.constants.AccountSystemConstants;
import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.AgentInfoDetail;
import com.caiex.account.entity.OrderTicket;
import com.caiex.account.entity.OrderTicketInfo;
import com.caiex.account.entity.SysUser;
import com.caiex.account.mapper.AgentInfoMapper;
import com.caiex.account.mapper.OrderTicketInfoMapper;
import com.caiex.account.mapper.OrderTicketMapper;
import com.caiex.account.model.AgentInfoModel;
import com.caiex.account.redis.RedisUtil;
import com.caiex.account.service.AgentInfoDetailService;
import com.caiex.account.service.AgentInfoService;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;

@Service
public class AgentInfoServiceImpl implements AgentInfoService {
	private final static Logger log = Logger.getLogger(AgentInfoServiceImpl.class);

	@Autowired
	private AgentInfoMapper agentInfoMapper;
	@Autowired
	private OrderTicketMapper orderTicketMapper;
	@Autowired
	private OrderTicketInfoMapper orderTicketInfoMapper;

	@Autowired
	private AgentInfoDetailService agentInfoDetailService;

	@Autowired
	private RedisUtil redisDao;

	private static final String[] MONEY_KEYS = { "PRESTORE", "Investment","RECYCLECHANNELS" };

	@Override
	public AgentInfo getUrlByAgentNum(AgentInfo agentInfo) {
		return agentInfoMapper.getUrlByAgentNum(agentInfo);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map queryChannel(String year, String month, String day) throws Exception {
		Map<String, Object> date = getStartAndEndDate(year, month, day);// 获取时间
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.putAll(date);// 封装参数

		List<Integer> agentids = orderTicketMapper.queryAgentid(date);// 查询该时间段 的agentid（去重）集合
		
		List<AgentInfoModel> agentInfoModels = new ArrayList<>();// 返回结果
		AgentInfoModel model = new AgentInfoModel();// 用于统计数据
		Double totalTradePrice = 0.0;
		Double totalTotalPrice = 0.0;
		Double totalPreStore = 0.0;
		Double totalPreStoreAlarm = 0.0;
		Integer totalOrderAmount = 0; 
		Integer totalUserAmount = 0;
		
		Map resultMap = new HashMap<>();
		List<AgentInfo> noticketAgent = find(agentids);// 该时间段没有交易的渠道
		List<AgentInfoModel> noTradeResults = showNoTradeAgent(noticketAgent);

		if (agentids.size() > 0) {// 对该时间段有交易的渠道进行统计操作
			for (int i = 0; i < agentids.size(); i++) {
				AgentInfo agentInfo = agentInfoMapper.queryAgentInfoByAgentid(agentids.get(i));// 查询渠道的基本信息

				Double preStore = getPreStore(agentInfo);// 从redis里面查询出预存额
				agentInfo.setPrestore(preStore);
				agentInfoMapper.updateAgentinfo(agentInfo);//更新到数据库
				paramsMap.put("agentId", agentids.get(i));// 将agentid当做参数
				log.info("agentid为"+agentids.get(i));
				List<OrderTicket> orderTickets = orderTicketMapper.queryTicketInfo_id(paramsMap);// 通过时间和 agentid查询票

				OrderTicket orderTradePrice = orderTicketMapper.queryBatchPrice(paramsMap);// 查询交易成功的交易额
													
				Double totalPrice = queryTotalPrice(orderTickets);// 总的最大中奖额
				Double tradePrice = orderTradePrice.getTradePrice();// 总的交易额
				Integer orderAmount = getOrderAmount(paramsMap);//订单数
				int userAmount = obtainAccountSize(orderTickets);//用户数
				
				AgentInfoModel result = new AgentInfoModel();
				result.setAgentSell(agentInfo.getAgentSell());
				result.setAgentCode(agentInfo.getAgentCode());
				result.setPrestoreAlarm(agentInfo.getPrestoreAlarm());
				result.setAgentId(agentInfo.getAgentNum());
				result.setUrl(agentInfo.getUrl());
				result.setPassword(agentInfo.getPassword());
				result.setAgentName(agentInfo.getAgentName());
				result.setUserAmount(userAmount);// 用户数
				result.setOrderAmount(orderAmount);// 订单数
				result.setPrestore(preStore);// 预存款
				result.setTradePrice(tradePrice);// 交易额
				result.setWinMoney(totalPrice);// 最大中奖额
				result.setProfit(totalPrice - tradePrice);// 盈余
				result.setProfitability(NumberUtil.getNumberAccordingToPercision(result.getProfit() / result.getTradePrice() * 100, 3));// 盈余率
				
				totalTradePrice += tradePrice;// 求总的tradePrice
				totalTotalPrice += totalPrice;// 求总的totalPrice
				totalPreStore += preStore;// 求总的
				totalPreStoreAlarm +=result.getPrestoreAlarm();
				totalOrderAmount +=orderAmount;//求总的 订单数
				totalUserAmount +=userAmount;//求总的用户数
				
				agentInfoModels.add(result);
			}
			agentInfoModels.addAll(noTradeResults);
			totalPreStore += noTradeModel(noticketAgent).getPrestore();//统计没有交易的渠道
			totalPreStoreAlarm += noTradeModel(noticketAgent).getPrestoreAlarm();
			
			model.setPrestore(totalPreStore);
			model.setTradePrice(totalTradePrice);
			model.setWinMoney(totalTotalPrice);
			model.setProfit(totalTotalPrice - totalTradePrice);
			model.setOrderAmount(totalOrderAmount);
			model.setUserAmount(totalUserAmount);
			model.setProfitability(NumberUtil.getNumberAccordingToPercision(model.getProfit() / model.getTradePrice() * 100, 3));
			model.setPrestoreAlarm(totalPreStoreAlarm);
			
		} else {// 该时间段内，均无票,便于页面展示
			List<AgentInfo> allAgents = queryAll();
			agentInfoModels = showNoTradeAgent(allAgents);
			model=noTradeModel(allAgents);
		}
		
		resultMap.put("model", model);
		resultMap.put("agentInfoModels", agentInfoModels);
		return resultMap;
	}

	
	// 从redis里面查询

	public double getPreStore(AgentInfo agentInfo) {

		final String[] keys = new String[MONEY_KEYS.length];
		for (int t = 0; t < MONEY_KEYS.length; t++) {
			keys[t] = agentInfo.getAgentNum() + MONEY_KEYS[t];
		}
		List<String> preStoreAndInvAndRecycle = redisDao.mGet(keys);
		Double preStore = calculatePro(preStoreAndInvAndRecycle.get(0),preStoreAndInvAndRecycle.get(1),preStoreAndInvAndRecycle.get(2));
		return preStore;
	}

	// 封装没有票的渠道信息
	public List<AgentInfoModel> showNoTradeAgent(List<AgentInfo> infos) {
		List<AgentInfoModel> NoTicketResult = new ArrayList<>();
		
		for (AgentInfo info : infos) {
			AgentInfoModel res = new AgentInfoModel();
			res.setAgentId(info.getAgentNum());
			res.setAgentName(info.getAgentName());
			res.setPassword(info.getPassword());
			res.setUrl(info.getUrl());
			res.setPrestoreAlarm(info.getPrestoreAlarm());
			res.setPrestore(getPreStore(info));
			res.setAgentCode(info.getAgentCode());
			res.setAgentSell(info.getAgentSell());
			NoTicketResult.add(res);
			info.setPrestore(res.getPrestore());
			agentInfoMapper.updateAgentinfo(info);
		}
		return NoTicketResult;
	}
	
	// 封装没交易的渠道信息
		public AgentInfoModel noTradeModel(List<AgentInfo> infos) {
			AgentInfoModel model = new AgentInfoModel();
			Double totalPrestore = 0.0;
			Double totalPrestoreAlarm =0.0;
			for (AgentInfo info : infos) {
				double preStore = getPreStore(info);
				totalPrestore += preStore;
				totalPrestoreAlarm += info.getPrestoreAlarm();
			}
			model.setPrestore(totalPrestore);
			model.setPrestoreAlarm(totalPrestoreAlarm);
			return model;
		}
	
	
	

	// 该时间段没有交易的渠道
	public List<AgentInfo> find(List<Integer> agentids) {
		List<AgentInfo> agentInfos = new ArrayList<>();
		List<Integer> allAgents = queryAllIds();
		allAgents.removeAll(agentids);// 求差集

		for (Integer agentid : allAgents) {
			AgentInfo agentInfo = agentInfoMapper.queryAgentInfoByAgentid(agentid);
			agentInfos.add(agentInfo);
		}
		return agentInfos;// 返回没有交易的渠道信息
	}

	// 查询所有agent信息
	public List<AgentInfo> queryAll() {
		return agentInfoMapper.queryAll();
	}

	// 查询所有agent的agentNum
	public List<Integer> queryAllIds() {
		List<Integer> allIds = new ArrayList<>();
		List<AgentInfo> all = agentInfoMapper.queryAll();
		for (AgentInfo agentInfo : all) {
			allIds.add(agentInfo.getAgentNum());
		}
		return allIds;
	}

	// 该时间段内订单数
	public int getOrderAmount(Map<String, Object> paramsMap) {

		return orderTicketMapper.getOrderAmount(paramsMap);
	}

	// 修改渠道信息
	@Override
	public Response updateAgent(AgentInfoModel result, HttpSession session) {
		SysUser user = (SysUser) session.getAttribute("user");
		Response res = new Response();
		System.out.println(result.getUpdateflag());
		try {
			AgentInfo oldAgent = agentInfoMapper.queryAgentInfoByAgentid(result.getAgentId());// 根据id查询到原本数据库里面的数据
			// oldAgent.setUpdator(user.getId());//更新更改人
			oldAgent.setUpdateTime(new Date());// 更新更改时间

			if (AccountSystemConstants.UPDATE_AGENT_PRESTORE == result.getUpdateflag()) {
				agentInfoDetailService.changePreStoreDetail(oldAgent, result.getPrestore());;// 更改明细
				
				redisDao.incrByFloat(oldAgent.getAgentNum() + "PRESTORE",result.getPrestore());// 修改redis里面的值
				Double preStore = getPreStore(oldAgent);
				oldAgent.setPrestore(preStore);// 修改预存款
				log.info(new Date() + "" + "更改了渠道" + oldAgent.getAgentNum()+ "预存额");

			} else if (AccountSystemConstants.UPDATE_AGENT_PRESTORE_ALARM == result.getUpdateflag()) {
				oldAgent.setPrestoreAlarm(result.getPrestoreAlarm());// 修改预存款报警
				log.info(new Date() + "" + "更改了渠道" + oldAgent.getAgentNum()+ "更改了预存额报警线");

			} else {
				oldAgent.setAgentName(result.getAgentName());// 修改渠道名称 密码 url
				oldAgent.setUrl(result.getUrl());
				oldAgent.setPassword(result.getPassword());
				log.info(new Date() + "" + "更改了渠道" + oldAgent.getAgentNum()+ "更改了渠道信息");
			}

			agentInfoMapper.updateAgentinfo(oldAgent);// 更新数据库
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("update success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("update fail");
		}
		return res;
	}

	// 将redis里面查出来数据处理
	private Double calculatePro(String preStore, String investment,
			String recycle) {
		return (StringUtils.isEmpty(preStore) ? 0.0 : Double.valueOf(preStore))
				- (StringUtils.isEmpty(investment) ? 0.0 : Double.valueOf(investment))
				+ (StringUtils.isEmpty(recycle) ? 0.0 : Double.valueOf(recycle));
	}

	// 用户数
	public int obtainAccountSize(List<OrderTicket> orderTickets) {
		List<OrderTicketInfo> ticketInfos = orderTicketInfoMapper.queryBatch(orderTickets);
		Set<Object> set = new HashSet<Object>();
		for (OrderTicketInfo orderTicketInfo : ticketInfos) {
			set.add(orderTicketInfo.getUid());
		}
		return set.size();
	}

	// 查询TotalPrice
	private Double queryTotalPrice(List<OrderTicket> orderTickets) {
		OrderTicket listDetail = orderTicketMapper.queryRecycleState(orderTickets);
		if (listDetail != null) {
			return listDetail.getRecyclePrice();
		}
		return 0.0;
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
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
		} else if (StringUtils.isEmpty(day)) {// day为空
									
			startDate = year + "-" + month + "-01 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.MONTH, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
		} else {//均有值
			
			startDate = year + "-" + month + "-" + day + " 00:00:00";
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.parseDate(startDate,new String[] { "yyyy-MM-dd hh:mm:ss" }));
			calendar.add(Calendar.DATE, 1);
			endDate = DateFormatUtils.format(calendar, "yyyy-MM-dd")+ " 00:00:00";
		}
		date.put("startDate", startDate);
		date.put("endDate", endDate);
		log.info("开始时间"+startDate);
		log.info("结束时间"+endDate);
		return date;
	}

	// 添加渠道信息
	@Override
	public Response addAgent(AgentInfo agentInfo, HttpSession session) {
	//	SysUser user = (SysUser) session.getAttribute("user");
		Response rs = new Response();
		try {
			if( agentInfo.getPassword().isEmpty() || StringUtils.isEmpty(agentInfo.getPassword())){
				log.error("渠道密码必须填");
				rs.getResult().setResultCode(0);
				rs.getResult().setResultMsg("渠道密码必须填");
				return rs;	
			}
			if (agentInfo.getAgentCode() == null  || agentInfo.getAgentName() == null || agentInfo.getAgentNum() == null) {
				log.error("渠道编码，名称，编号必须填");
				log.error("编码"+agentInfo.getAgentCode()+"名称"+agentInfo.getAgentName()+"编号"+agentInfo.getAgentNum());
				rs.getResult().setResultCode(0);
				rs.getResult().setResultMsg("渠道编码，名称，编号必须填");
				return rs;
			}
			if (agentInfoMapper.queryAgentInfoByAgentid(agentInfo.getAgentNum()) != null) {
				log.error("该渠道号已存在");
				rs.getResult().setResultCode(0);
				rs.getResult().setResultMsg("该渠道号已存在");
				return rs;
			}
			if (agentInfoMapper.queryAgentInfoByAgentName(agentInfo.getAgentName()) != null) {
				log.error("渠道名称已存在");
				rs.getResult().setResultCode(0);
				rs.getResult().setResultMsg("渠道名称已存在");
				return rs;
			}
			if (agentInfo.getPrestore() <= agentInfo.getPrestoreAlarm()) {
				log.error("预存额必须大于预存额报警线");
				rs.getResult().setResultCode(0);
				rs.getResult().setResultMsg("预存额必须大于预存额报警线");
				return rs;
			}
			String[] keys = { agentInfo.getAgentNum() + "PRESTORE",
					          agentInfo.getAgentNum() + "Investment",
							  agentInfo.getAgentNum() + "RECYCLECHANNELS",
							  agentInfo.getAgentNum() + "aid",
							  agentInfo.getAgentNum() + "Pwd" };

			String[] values = { agentInfo.getPrestore() + "", "0", "0",agentInfo.getAgentNum().toString(), agentInfo.getPassword() };
			redisDao.mSet(keys, values);
			agentInfo.setCreateTime(new Date());
			agentInfo.setUpdateTime(new Date());
			agentInfo.setUpdator(9);
			agentInfo.setCreator(9);
			agentInfo.setAgentSell(agentInfo.getAgentNum());
			// agentInfo.setUpdator(user.getId());
			// agentInfo.setCreator(user.getId());

			agentInfoMapper.addAgent(agentInfo);
			rs.getResult().setResultCode(1);
			rs.getResult().setResultMsg("add success");

		} catch (Exception e) {
			rs.getResult().setResultCode(0);
			rs.getResult().setResultMsg("add fail");
		}
		return rs;
	}

	// 渠道全部开售或者停售 需要一个判断
	public Response changeAllAgentSellStatus(AgentInfoModel model,HttpServletRequest request) {
		Response res = new Response();
		try {
			
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<AgentInfo> allAgents = queryAll();
		
		 for(AgentInfo agentInfo : allAgents){
			if(model.getAgentSell() == 0){
				agentInfo.setAgentSell(-1);
			//	log.info(user.getId() + "all agent stop");//全部停售
			}else{
				agentInfo.setAgentSell(agentInfo.getAgentNum());
			//	log.info(user.getId() + "all agent start");//全部开售
			}	
			agentInfo.setUpdateTime(new Date());
			//agentInfo.setUpdator(user.getId());
			agentInfoMapper.updateAgentinfo(agentInfo);
		 }

		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
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
		String strTemplate = "/opt/FMS/consumer/template/agent-demo.xls";// 模板位置

		strKeyArray = new String[] { "agentName", "agentId", "userAmount",
				"orderAmount", "tradePrice", "winMoney", "profit",
				"profitability", "prestore", "prestoreAlarm", "agentSellMessage" };

		HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, AgentInfoModel.class);
		PoiUtil.returnExcel(response, workbook, "AGENT");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
		
	}

	// 渠道开停售
	@Override
	public Response changeAgentSellStatus(AgentInfoModel agent, HttpServletRequest request) {
		   Response res = new Response();
		try {
			HttpSession session = request.getSession();
			SysUser user = (SysUser) session.getAttribute("user");
			AgentInfo agentInfo = agentInfoMapper.queryAgentInfoByAgentid(agent	.getAgentId());
			// agentInfo.setUpdator(user.getId());
			agentInfo.setUpdateTime(new Date());
			if (agent.getAgentSell() == 0) {
				agentInfo.setAgentSell(-1);
				redisDao.set(agentInfo.getAgentNum() + "aid", "-1");
				// log.info(user.getId()+agentInfo.getAgentCode()+" agent stop");
			} else {
				agentInfo.setAgentSell(agentInfo.getAgentNum());
				redisDao.set(agentInfo.getAgentNum() + "aid", agentInfo.getAgentNum().toString());
				// log.info(user.getId()+agentInfo.getAgentCode()+" agent start");
			}
			agentInfoMapper.updateAgentinfo(agentInfo);// 更新数据库
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");

		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
	}

	// 查询preStore明细
	@Override
	public List<AgentInfoDetail> queryPreStoreDetail(int  agentId) {
		
		AgentInfo agentInfo = agentInfoMapper.queryAgentInfoByAgentid(agentId);
		List<AgentInfoDetail> agentInfoDetails = agentInfoDetailService.queryPreStoreDetail(agentInfo.getAgentCode());
		return agentInfoDetails;
	}

}
