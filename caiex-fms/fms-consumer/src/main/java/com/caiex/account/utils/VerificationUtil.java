package com.caiex.account.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Component
public class VerificationUtil {
	 private final static Logger log = Logger.getLogger(VerificationUtil.class);
	
	 //查询单个票 
	public  TicketResult getTicketResult(Map<String,String> map,String url){
		String result = ThreadPoolHttpClient.getInstance().get(url, map);
		JSONObject resultData = JSONObject.parseObject(result);
		log.info(result);
		TicketResult ticketResult = new TicketResult(resultData.getString("tkId"),resultData.getInteger("schemeState"), resultData.getDate("tradeTime"),resultData.getDouble("tradePrice"), resultData.getDouble("winMoney"));
		return ticketResult;
	}
	
	//查询多个票
	public List<TicketResult> getTicketListResult(Map<String,String> map,String url){
		String result = ThreadPoolHttpClient.getInstance().get(url, map);
		List<TicketResult> ticketList=new ArrayList<>();
		log.info(map.get("agentid")+"接收到的结果为"+result);
		if(result == null){
			log.error("请检查   该时间段无票");
			return ticketList;
			
		} else if(result.equals("授权验证失败")){
			log.error(result);
			return ticketList;
		
		}else {
			JSONArray array= null;
			if(result.indexOf("schemeList") >0){
				JSONObject re = JSONObject.parseObject(result);
				 array= JSONArray.parseArray(re.getString("schemeList"));
			}else{
				array=JSONObject.parseArray(result);
			}
			
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject=(JSONObject) array.get(i);
				if(jsonObject.getInteger("schemeState") == 6){//方案未交易
					TicketResult ticket=new TicketResult();
					ticket.setTkId(jsonObject.getString("tkId"));
					ticket.setSchemeState(jsonObject.getInteger("schemeState"));
					ticketList.add(ticket);
				}
				else{
				TicketResult ticket=new TicketResult();
				ticket.setTkId(jsonObject.getString("tkId"));
				ticket.setSchemeState(jsonObject.getInteger("schemeState"));
				ticket.setTradeTime(jsonObject.getDate("tradeTime"));
				ticket.setTradePrice(jsonObject.getDouble("tradePrice"));
				ticket.setWinMoney(jsonObject.getDouble("winMoney"));
				ticketList.add(ticket);
				}
			}
			return ticketList;	
		}

	}
	
}
