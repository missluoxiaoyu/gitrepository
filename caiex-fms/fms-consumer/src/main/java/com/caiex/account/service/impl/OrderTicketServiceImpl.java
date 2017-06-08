package com.caiex.account.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.constants.AccountSystemConstants;
import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.OrderTicketError;
import com.caiex.account.mapper.AgentInfoMapper;
import com.caiex.account.mapper.OrderTicketInfoMapper;
import com.caiex.account.mapper.OrderTicketMapper;
import com.caiex.account.model.OrderTicketModel;
import com.caiex.account.service.OrderTicketErrorService;
import com.caiex.account.service.OrderTicketService;
import com.caiex.account.utils.biYingAgentUrlInfo;
import com.caiex.account.utils.Md5Util;
import com.caiex.account.utils.TicketResult;
import com.caiex.account.utils.VerificationUtil;
import com.caiex.account.utils.xiaomiAgentUrlInfo;
import com.caiex.oltp.api.Response;
import com.caiex.oltp.api.ticket.RedisOptApi;

@Service
public class OrderTicketServiceImpl implements OrderTicketService {
	
	private final static Logger log = Logger.getLogger(OrderTicketServiceImpl.class);
	private int tradeState=100;
	
	@Autowired
	private AgentInfoMapper agentInfoMapper;
	
	@Autowired
	private biYingAgentUrlInfo biying;
	@Autowired
	private xiaomiAgentUrlInfo xiaomi;
	@Autowired
	private OrderTicketMapper orderTicketMapper;
	@Autowired
	private OrderTicketInfoMapper orderTicketInfoMapper;
	@Autowired
	private VerificationUtil verificationUtil;
	@Autowired
	private OrderTicketErrorService errorService;
	@Autowired
	private RedisOptApi  redisOptApi;
	
	/**发送tkid请求*/
	public TicketResult sendTkidRequestTo(String url ,int agentid,String priKey,String tkId){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("agentid", agentid+"");
		paramMap.put("tkId", tkId);
		paramMap.put("sign", Md5Util.md5("agentid"+agentid+"tkId"+tkId+priKey));
		TicketResult ticketResult = verificationUtil.getTicketResult(paramMap,url);//发送请求
		log.info("加密后的私钥"+Md5Util.md5("agentid"+agentid+"tkId"+tkId+priKey));
		return ticketResult;
	}
	
	/** 根据票的tkId验证单个票 
	 */
	@Override
	public void verifyOne(String tid) {
		String tkId = orderTicketInfoMapper.getOrderTicketInfoBytid(tid).getTkId();
		int agentid = orderTicketInfoMapper.getOrderTicketInfoBytid(tid).getAgentId();
		switch (agentid) {
			case 111:{//小米
				findError(tkId, agentid, xiaomi.getOnlineGetOrderUrl());
				break;
			}
			case 117:{//必赢
				findError(tkId, agentid, biying.getOnlineGetOrderUrl());
				break;
			}
		}
	}
	

	
	public void findError(String tkId,int agentid,String orderUrl ){
		//获取私钥
		Response response = redisOptApi.getRedisValue(agentid+"PriKey");
		String  priKey = response.getData() == null ? "":response.getData().toString();
		
		TicketResult ticketResult = sendTkidRequestTo(orderUrl, agentid, priKey, tkId);//发送请求获取ticket
		OrderTicketModel orderTicketModel = orderTicketMapper.getOrderTicketByTkid(tkId);//通过tkid查询数据库的票
		if (ticketResult == null) {//第三方没有查询到此票
			tradeState = AccountSystemConstants.OPPOSITE_NO_TICKET;
			log.error("第三方没有此票tkId为"+tkId);
			OrderTicketError errorTicket = orderTicketToErrorTicket(orderTicketModel);
			errorTicket.setTradeState(tradeState);
			errorService.saveTicketError(errorTicket);//检查是不是首次进入数据库；是，存贮，不是，更新状态
		} else {//双方都存在
			boolean error = contrast(orderTicketModel, ticketResult);//进行比对
			OrderTicketError errorTicket = orderTicketToErrorTicket(orderTicketModel);
			if (error) {//如果有错误
				errorTicket.setTradeState(tradeState);
				errorService.saveTicketError(errorTicket);
			} else {//当再次查询时，票变成正常票，将error表之前存贮的删除掉
				log.info(tkId+"为正常票");
				errorService.deleteTicketError(errorTicket);
			}
		}
		
	}
	
	
	
	
	
	/** 封装的验证的工具方法 */
	public boolean contrast(OrderTicketModel orderTicketModel,TicketResult ticketResult) {
		int schemeState = ticketResult.getSchemeState();//第三方交易状态
		int state = orderTicketModel.getState();//我方交易状态
		boolean flag = false;
		// 对方正在交易中，我们数据库有票
		if (schemeState == AccountSystemConstants.TRADEING && orderTicketModel != null) {
			tradeState = AccountSystemConstants.OPPOSITE_TRADING_AND_OUR_HAVE_TICKET;
			log.error("agentid="+" 对方正在交易中，我们数据库有票 tkId 为"+ticketResult.getTkId());
			flag = true;
		}
		// 对方显示转让完成且中奖，我们显示未中奖或alive 可能派奖延迟导致
		if (schemeState == AccountSystemConstants.TRANSFER_COMPLETE_AWARD && state != AccountSystemConstants.STATE_AWARD) {
			tradeState = AccountSystemConstants.OPPOSITE_AWARD_AND_OUR_NOAWARD_OR_ALIVE;
			log.error("agentid="+" 对方显示转让完成且中奖，我们显示未中奖或alive 可能派奖延迟导致 tkId 为"+ticketResult.getTkId());
			flag = true;
		}
		// 对方显示中奖已回收，我们显示未回收
		if (schemeState == AccountSystemConstants.AWARD_RECYCLE && state == AccountSystemConstants.STATE_NO_RECYCLE) {
			tradeState = AccountSystemConstants.OPPOSITE_RECYCLE_AND_OUR_NO_RECYCLE;
			log.error("agentid="+"对方显示中奖已回收，我们显示未回收 tkId 为"+ticketResult.getTkId());
			flag = true;
		}
		// 对方方案未交易，我们有票
		if (schemeState == AccountSystemConstants.NO_TRADE && orderTicketModel != null) {
			tradeState = AccountSystemConstants.OPPOSITE_NO_TRADE_AND_OUR_HAVE_TICKET;
			log.error("agentid="+"对方方案未交易，我们有票 tkId 为"+ticketResult.getTkId());
			flag = true;
		}
		return flag;
	}
	
	/**发送time请求*/
	public List<TicketResult> sendTimeRequestTo(String url ,int agentid,String priKey,long startTime,long endTime,int page){
		
		Map<String, String> map = new HashMap<>();
		map.put("startTime", startTime+"");
		map.put("endTime",  endTime+"");
		map.put("agentid",agentid+"");
		map.put("page", page+"");
		map.put("sign",Md5Util.md5("agentid"+agentid+"endTime"+endTime+"page"+page+"startTime"+startTime+priKey));
		List<TicketResult> resultList = verificationUtil.getTicketListResult(map, url);//获取该时间段第三方的票
		return resultList;
	}
	
	/**
	 * 根据一段时间去查看对方的票
	 * */
	@Override
	public void verifyMore(Date startTime, Date endTime,int agentid,int page) {
		switch (agentid) {
		case 111:{
			findErrors(xiaomi.getOnlineGetOrderListUrl(), startTime, endTime, agentid, page);
			break;
			}
		case 117:{
			findErrors(biying.getOnlineGetOrderListUrl(), startTime, endTime, agentid, page);
			break;
			}
		}

	}
	
	
	public void findErrors(String orderListUrl,Date startTime, Date endTime,int agentid,int page){
		
		//Response response = redisOptApi.getRedisValue(agentid+"PriKey");
		//String  priKey = response.getData() == null ? "":response.getData().toString();
		String priKey="4e9d6df992a1be66801bbdcc5138c87f";
		List<TicketResult> resultList=sendTimeRequestTo(orderListUrl, agentid, priKey, startTime.getTime(), endTime.getTime(), page);
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("startTime", startTime);
		paramsMap.put("endTime", endTime);
		paramsMap.put("agentid", agentid);
		paramsMap.put("page", (page-1)*100);//默认每页数据一百条
		paramsMap.put("size", 100);
		List<OrderTicketModel> orderTickets = getOrderTicketListWithTkidByTime(paramsMap);
		if(resultList.size() == 0 ){//查出票的数量为空
			log.error("该时间段查出"+agentid+"票的数量为0，请检查");
		}if(orderTickets.size() == 0){
			log.error("该时间段数据库查出票的数量为0，请检查");
		}
		else{
			contrastAll(orderTickets, resultList,agentid);	
		}
	}
	
	


	/** 根据时间去查数据库order_ticket表的信息 */
	@Override
	public List<OrderTicketModel> getOrderTicketListWithTkidByTime(Map<String, Object> map) {
		return orderTicketMapper.getOrderTicketListWithTkidByTime(map);
	}

	/** 验证两个集合中的票 */
	public void contrastAll(List<OrderTicketModel> orderTickets,List<TicketResult> resultList,int agentid) {
		
		for (int i = 0; i < orderTickets.size(); i++) {
			boolean flag = false;
			for (int j = 0; j < resultList.size(); j++) {

				if (orderTickets.get(i).getTkId().equals(resultList.get(j).getTkId())) {
					flag = true;
					boolean error = contrast(orderTickets.get(i),resultList.get(j));
					OrderTicketError errorTicket = orderTicketToErrorTicket(orderTickets.get(i));
					if (error) {
						errorTicket.setTradeState(tradeState);
						errorTicket.setQueryTime(new Date());
						errorService.saveTicketError(errorTicket);
					} else {
						log.info(orderTickets.get(i).getTkId()+"正常票");
						errorService.deleteTicketError(errorTicket);
					}
				}
				
			}
			if (!flag) {
				System.out.println("我们有票而第三方没有" + orderTickets.get(i).getTkId());
				tradeState = AccountSystemConstants.OPPOSITE_NO_TICKET;
				OrderTicketError errorTicket = orderTicketToErrorTicket(orderTickets.get(i));
				errorTicket.setTradeState(tradeState);
				errorTicket.setQueryTime(new Date());
				errorService.saveTicketError(errorTicket);
			}
		}
		
	}
	
	
	/** 将数据库出现问题的票放在order_ticket_error表中 */
	public OrderTicketError orderTicketToErrorTicket(OrderTicketModel ticket) {
		OrderTicketError errorTicket = new OrderTicketError();
		errorTicket.setAgentId(ticket.getAgentId());
		errorTicket.setTkId(ticket.getTkId());
		errorTicket.setTradePrice(ticket.getTradePrice());
		errorTicket.setRecyclePrice(ticket.getRecyclePrice());
		errorTicket.setTradeTime(ticket.getTradeTime());
		errorTicket.setWinMoney(ticket.getWinMoney());
		errorTicket.setQueryTime(new Date());
		// state=0代表未处理
		errorTicket.setState(0);
		errorTicket.setRequestTimestamp(ticket.getRequestTimestamp());//时间戳
		return errorTicket;
	}

	/** 将第三方有，我方没有的票封装成错误票 */
	public OrderTicketError ticketResultToErrorTicket(TicketResult ticketResult,int agentid) {
		OrderTicketError errorTicket = new OrderTicketError();
		errorTicket.setAgentId(agentid);
		errorTicket.setRecyclePrice(ticketResult.getWinMoney());
		errorTicket.setTkId(ticketResult.getTkId());
		errorTicket.setTradePrice(ticketResult.getTradePrice());
		errorTicket.setState(0);
		errorTicket.setQueryTime(new Date());
		errorTicket.setTradeTime(ticketResult.getTradeTime());
		errorTicket.setWinMoney(ticketResult.getWinMoney());
		return errorTicket;
	}

	
	

@Override	
public  Map<String,Object> queryAgentTicketByTime(Date startTime, Date endTime,int agentid,int page){
	    Map<String,Object> resultMap = new HashMap<>();
	    List<TicketResult> resultList = new ArrayList<TicketResult>();
	    switch (agentid) {
		case 111:{
			//Response response = redisOptApi.getRedisValue(agentid+"PriKey");
			//String  priKey = response.getData() == null ? "":response.getData().toString();
			String priKey="4e9d6df992a1be66801bbdcc5138c87f";
			resultList=sendTimeRequestTo(xiaomi.getOnlineGetOrderListUrl(), agentid, priKey, startTime.getTime(), endTime.getTime(), page);
			
			break;
			}
		case 117:{

			Response response = redisOptApi.getRedisValue(agentid+"PriKey");
			String  priKey = response.getData() == null ? "":response.getData().toString();
			resultList=sendTimeRequestTo(biying.getOnlineGetOrderListUrl(), agentid, priKey, startTime.getTime(), endTime.getTime(), page);
			break;
			}
			default :{
			resultList= null;
			}
	    }

		resultMap.put("resultList", resultList);
		
	return resultMap;
}	
	
	
	
	
	/** 单独查询第三方ticket*/
	@Override
	public Map<String, Object> 	queryAgentTicket(String tid){
		String tkId = orderTicketInfoMapper.getOrderTicketInfoBytid(tid).getTkId();
		int agentid = orderTicketInfoMapper.getOrderTicketInfoBytid(tid).getAgentId();
		Map<String,Object> resultMap = new HashMap<>();
		switch (agentid) {
			case 111:{//小米
				resultMap = queryTicket(agentid, xiaomi.getOnlineGetOrderUrl(), tkId);
				break;
			}
			case 117:{//必赢
				resultMap = queryTicket(agentid, biying.getOnlineGetOrderUrl(), tkId);
				break;
			}default : {
				
			}
		}
	
		return resultMap;
	}
	
	public Map<String, Object> queryTicket(int agentid,String orderUrl,String tkId){
		Map<String,Object> resultMap = new HashMap<>();
		//Response response = redisOptApi.getRedisValue(agentid+"PriKey");
		//String  priKey = response.getData() == null ? "":response.getData().toString();
		String priKey="4e9d6df992a1be66801bbdcc5138c87f";
		TicketResult ticketResult = sendTkidRequestTo(orderUrl, agentid, priKey, tkId);//发送请求获取ticket
		
		resultMap.put("ticketResult", ticketResult);

		return resultMap;
		
	}

	@Override
    public List<AgentInfo> queryAllAgentInfo(){
    	return agentInfoMapper.queryAll();
    }
    
	
	
}
