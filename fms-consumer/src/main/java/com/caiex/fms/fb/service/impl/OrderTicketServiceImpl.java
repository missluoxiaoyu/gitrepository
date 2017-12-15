package com.caiex.fms.fb.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.currentdb.api.AgentInfoProApi;
import com.caiex.dbservice.currentdb.api.OrderTicketApi;
import com.caiex.dbservice.currentdb.api.OrderTicketInfoApi;
import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.currentdb.entity.OrderTicketError;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.fms.constants.AccountSystemConstants;
import com.caiex.fms.fb.service.OrderTicketErrorService;
import com.caiex.fms.fb.service.OrderTicketService;
import com.caiex.fms.utils.Md5Util;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.fms.utils.TicketResult;
import com.caiex.fms.utils.VerificationUtil;
import com.caiex.fms.utils.biYingAgentUrlInfo;
import com.caiex.fms.utils.xiaomiAgentUrlInfo;
import com.caiex.oltp.api.Response;
import com.caiex.oltp.api.ticket.RedisOptApi;


@Service
public class OrderTicketServiceImpl implements OrderTicketService {
	
	private final static Logger log = Logger.getLogger(OrderTicketServiceImpl.class);
	private int tradeState=100;
	private String message="";
	
	@Autowired
	private AgentInfoProApi agentInfoApi;
	
	@Autowired
	private biYingAgentUrlInfo biying;
	@Autowired
	private xiaomiAgentUrlInfo xiaomi;
	@Autowired
	private OrderTicketApi orderTicketApi;
	@Autowired
	private OrderTicketInfoApi orderTicketInfoApi;
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
		ticketResult.setAgentid(agentid);
		log.info("加密后的私钥"+Md5Util.md5("agentid"+agentid+"tkId"+tkId+priKey));
		return ticketResult;
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
		for (TicketResult ticketResult : resultList) {
			ticketResult.setAgentid(agentid);
			showStateMessage(ticketResult);
		}
		return resultList;
	}
	
	
	
	
	
	/** 封装的验证的工具方法 */
	public boolean contrast(OrderTicketModel orderTicketModel,TicketResult ticketResult) {
		int schemeState = ticketResult.getSchemeState();//第三方交易状态
		
		boolean flag = false;
		// 对方正在交易中，我们数据库有票
		if (schemeState == AccountSystemConstants.TRADEING && orderTicketModel != null) {
			tradeState = AccountSystemConstants.OPPOSITE_TRADING_OUR_HAVETICKET;
			message ="agentid="+"对方正在交易中，数据库有票 tkId 为"+ticketResult.getTkId()+"收票通知失败,时间戳为"+orderTicketModel.getRequestTimestamp();
			log.info("agentid="+"对方正在交易中，数据库有票 tkId 为"+ticketResult.getTkId()+"收票通知失败,时间戳为"+orderTicketModel.getRequestTimestamp());
			flag = true;
		}
		
		if (schemeState == AccountSystemConstants.TRADEING && orderTicketModel == null) {
			tradeState = AccountSystemConstants.OPPOSITE_TRADING_OUR_NOTICKET;
			message ="agentid="+" 对方正在交易中， tkId 为"+ticketResult.getTkId()+"拒票通知失败,交易时间为"+ticketResult.getTradeTime();
			log.info("agentid="+" 对方正在交易中， tkId 为"+ticketResult.getTkId()+"拒票通知失败,交易时间为"+ticketResult.getTradeTime());
			flag = true;
		}
		// 对方方案未交易，我们有票
		if (schemeState == AccountSystemConstants.NO_TRADE && orderTicketModel != null) {
			tradeState = AccountSystemConstants.OPPOSITE_NO_TRADE_AND_OUR_HAVE_TICKET;
			message= "agentid="+"对方方案未交易，我们有票 tkId 为"+ticketResult.getTkId();
			log.info("agentid="+"对方方案未交易，我们有票 tkId 为"+ticketResult.getTkId());
			flag = true;
		}
		
		return flag;
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
		Response response = redisOptApi.getRedisValue(agentid+"PriKey");
		String  priKey = response.getData() == null ? "":response.getData().toString();
		//String  priKey="";
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
	

	/** 验证两个集合中的票 */
	public void contrastAll(List<OrderTicketModel> orderTickets,List<TicketResult> resultList,int agentid) {
		
		for (int i = 0; i < resultList.size(); i++) {
			boolean flag = false;
			for (int j = 0; j < orderTickets.size(); j++) {

				if (orderTickets.get(j).getTkId().equals(resultList.get(i).getTkId())) {
					flag = true;
					boolean error = contrast(orderTickets.get(j),resultList.get(i));
					OrderTicketError errorTicket = orderTicketToErrorTicket(orderTickets.get(j));
					if (error) {
						errorTicket.setTradeState(tradeState);
						errorTicket.setMessage(message);//保存问题信息
						errorTicket.setQueryTime(new Date());
						errorService.saveTicketError(errorTicket);
					} else {
						log.info(orderTickets.get(j).getTkId()+"正常票");
						errorService.deleteTicketError(errorTicket);
					}
				}
			}
			/*if (!flag) {
				
				System.out.println("我们有票而第三方没有" + orderTickets.get(i).getTkId());
				tradeState = AccountSystemConstants.OPPOSITE_NO_TICKET;
				OrderTicketError errorTicket = orderTicketToErrorTicket(orderTickets.get(i));
				errorTicket.setTradeState(tradeState);
				errorTicket.setQueryTime(new Date());
				errorService.saveTicketError(errorTicket);
			}*/
		}
		
	}
	
	
	/** 将数据库出现问题的票放在order_ticket_error表中 */
	public OrderTicketError orderTicketToErrorTicket(OrderTicketModel ticket) {
		OrderTicketError errorTicket = new OrderTicketError();
		errorTicket.setAgentId(ticket.getAgent_id());
		errorTicket.setTkId(ticket.getTkId());
		errorTicket.setTradePrice(ticket.getTrade_price());
		errorTicket.setRecyclePrice(ticket.getRecyclePrice());
		errorTicket.setTradeTime(ticket.getTrade_time());
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
		errorTicket.setRecyclePrice(new BigDecimal(ticketResult.getWinMoney()));
		errorTicket.setTkId(ticketResult.getTkId());
		errorTicket.setTradePrice(ticketResult.getTradePrice());
		errorTicket.setState(0);
		errorTicket.setQueryTime(new Date());
		errorTicket.setTradeTime(ticketResult.getTradeTime());
		errorTicket.setWinMoney(new BigDecimal(ticketResult.getWinMoney()));
		return errorTicket;
	}

	
	
	/*直接根据参数查询第三方的票信息*/
	@Override	
	public  Map<String,Object> queryAgentTicketByTime(Date startTime, Date endTime,int agentid,int page){
	    Map<String,Object> resultMap = new HashMap<>();
	    switch (agentid) {
		case 111:{
			resultMap = queryMoreTicket(startTime, endTime, agentid, page, xiaomi.getOnlineGetOrderListUrl());
			break;
			}
		case 117:{
			resultMap = queryMoreTicket(startTime, endTime, agentid, page, biying.getOnlineGetOrderListUrl());
			break;
			}
			default :{
				resultMap.put("resultList", "该渠道未开发此接口");
			}
	    }
	     	
	return resultMap;
}	
	
public Map<String, Object> queryMoreTicket(Date startTime, Date endTime,int agentid,int page,String orderListUrl){
	Map<String,Object> resultMap = new HashMap<>();
	 List<TicketResult> resultList = new ArrayList<TicketResult>();
	Response response = redisOptApi.getRedisValue(agentid+"PriKey");
	String  priKey = response.getData() == null ? "":response.getData().toString();
	// String  priKey="";
	 
	resultList=sendTimeRequestTo(orderListUrl, agentid, priKey, startTime.getTime(), endTime.getTime(), page);
	resultMap.put("resultList", resultList);
	
	return resultMap;
	
}
	
	
	/** 单独查询第三方ticket*/
	@Override
	public Map<String, Object> 	queryAgentTicket(String tkId,int agentid){
		
		Map<String,Object> resultMap = new HashMap<>();
		switch (agentid) {
			case 111:{//小米
				resultMap = queryOneTicket(agentid, xiaomi.getOnlineGetOrderUrl(), tkId);
				break;
			}
			case 117:{//必赢
				resultMap = queryOneTicket(agentid, biying.getOnlineGetOrderUrl(), tkId);
				break;
			}default : {
				resultMap.put("ticketResult", "该渠道未开发此接口");
			}
		}
	
		return resultMap;
	}
	
	public Map<String, Object> queryOneTicket(int agentid,String orderUrl,String tkId){
		Map<String,Object> resultMap = new HashMap<>();
		Response response = redisOptApi.getRedisValue(agentid+"PriKey");
		String  priKey = response.getData() == null ? "":response.getData().toString();
		//String  priKey="";
		
		TicketResult ticketResult = sendTkidRequestTo(orderUrl, agentid, priKey, tkId);//发送请求获取ticket
		
		resultMap.put("ticketResult", showStateMessage(ticketResult));
		return resultMap;
		
	}

	public TicketResult showStateMessage(TicketResult ticketResult){
		if(ticketResult.getSchemeState()==AccountSystemConstants.TRADEING){
			ticketResult.setStateMessage("交易中");
		}else if(ticketResult.getSchemeState()==AccountSystemConstants.TRANSFER_COMPLETE){
			ticketResult.setStateMessage("交易完成");
		}else if(ticketResult.getSchemeState()==AccountSystemConstants.TRANSFER_COMPLETE_AWARD){
			ticketResult.setStateMessage("交易完成且中奖");
		}else if(ticketResult.getSchemeState()==AccountSystemConstants.TRANSFER_COMPLETE_NO_AWARD){
			ticketResult.setStateMessage("交易完成未中奖");
		}else if(ticketResult.getSchemeState()==AccountSystemConstants.AWARD_RECYCLE){
			ticketResult.setStateMessage("中奖回收");
		}else{
			ticketResult.setStateMessage("未交易");
		}
		return ticketResult;
	}
	
	
	
	
	@Override
    public List<AgentInfo> queryAllAgentInfo(){
		System.out.println("查询渠道");
    	return agentInfoApi.queryAllAgents();
    }
    
	/** 根据时间去查数据库order_ticket表的信息 */
	@Override
	public List<OrderTicketModel> getOrderTicketListWithTkidByTime(Map<String, Object> map) {
		return orderTicketApi.getOrderTicketListWithTkidByTime(map);
		//return null;
	}
	
	
	@Override
	public Response orderExcel(Date startTime,Date endTime,int agentid,int page,HttpServletResponse response){
		
		Response res = new Response();
		try {
			
		Map<String, Object> map = queryAgentTicketByTime(startTime, endTime, agentid, page);
		List<TicketResult> resultList=new ArrayList<>();
		
		if( map.get("resultList").getClass().equals(String.class)){
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
			return  res;
		}else{
			resultList= (List<TicketResult>) map.get("resultList");
		}
		
		List<TicketResult> list = new ArrayList<TicketResult>();
		String[] strKeyArray = null;
		
		for (TicketResult result : resultList) {
			showStateMessage(result);
		
		}
		list.addAll(resultList);
		String strTemplate = "/opt/FMS/fms/excel/third-demo.xls";// 模板位置
		
		//String strTemplate = "C:/fms/template/third-demo.xls";// 模板位置
		
		strKeyArray = new String[] { "agentid","tkId","stateMessage","tradeTime","tradePrice","winMoney"};
		HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, TicketResult.class);
		PoiUtil.returnExcel(response, workbook, "order");
		
		res.getResult().setResultCode(1);
		res.getResult().setResultMsg("success");
		} catch (Exception e) {
			res.getResult().setResultCode(0);
			res.getResult().setResultMsg("fail");
		}
		return res;
		
	}
	
	
	
}
