package com.caiex.account.service.impl;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caiex.account.service.NotifyResultService;
import com.caiex.account.utils.Md5Util;
import com.caiex.account.utils.ThreadPoolHttpClient;
import com.caiex.oltp.api.Response;
import com.caiex.oltp.api.dto.RefusedTicket;
import com.caiex.oltp.api.dto.TicketInfo;
import com.caiex.oltp.api.ticket.RedisOptApi;
import com.esotericsoftware.minlog.Log;


@Service
public class NotifyResultServiceImpl implements NotifyResultService{
	
	public static String ACCEPT_DOWN = "1001";
	public static String REJECT_DOWN = "2001";
	public static String ALREADY_DEAL = "3001";
	public static String ACCEPT_STATUS = "1";
	public static String REJECT_STATUS = "0";
	
	@Autowired
	private RedisOptApi  redisOptApi;

	public  Boolean notifyMsg(TicketInfo ticket, RefusedTicket rejectTicket, String url){
		Map<String, String> map = new HashMap<String, String>();
		Boolean flag = true;
		//收票
		if(ticket != null){
			switch(ticket.getAgentId()){
				case 100:{
					//新浪
					map.put("tkId", ""+ ticket.getTid());
					map.put("tradePrice", ticket.getTrade_price() + "");
					map.put("status", "false");
					//flag = ThreadPoolHttpClient.getInstance().post(url, map) == null?false:true;
					String response = ThreadPoolHttpClient.getInstance().post(url, map);
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						if(StringUtils.isEmpty(((String)obj.get("data")))){
							flag = false;
						}else{
							flag = true;	
						}
					}
					break;
				}
				case 110:{
					//彩票宝
					Response resp = redisOptApi.getRedisValue(ticket.getAgentId()+"PriKey");
					String  priKey = resp.getData() == null ? "":resp.getData().toString();
					
					map.put("tkId", ""+ ticket.getTid());
					map.put("tradePrice", ticket.getTrade_price() + "");
					map.put("status", "false");
					map.put("token", Md5Util.md5(ticket.getTid()+"false"+ticket.getTrade_price()+priKey));
					
					String response = ThreadPoolHttpClient.getInstance().get(url, map);
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						if("9999".equals(((JSONObject)obj.get("result")).get("resultCode")+"")){
							flag = true;
						}else{
							flag = false;	
						}
					}
					break;
 				}
				/*case 111: case 113: case 114: case 115: {
					String priKey = redisOptUtils.getStrFromRedis(ticket.getAgentId()+"PriKey");
					map.put("agentid", ticket.getAgentId()+"");
					map.put("tkId", ""+ ticket.getTid());
					map.put("tradePrice", ticket.getTrade_price() + "");
					map.put("status", ACCEPT_STATUS);
					
					map.put("sign", Md5Util.md5("agentid"+ticket.getAgentId()+"status"+ACCEPT_STATUS+"tkId"+ticket.getTid()+"tradePrice"+ticket.getTrade_price()+priKey));
					String response = ThreadPoolHttpClient.getInstance().post(url, map);
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						if("success".equals(((JSONObject)obj.get("result")).get("resultCode"))){
							flag = true;
						}else{
							flag = false;	
						}
					}
					break;
				}*/
				default : {
					Response resp = redisOptApi.getRedisValue(ticket.getAgentId()+"PriKey");
					String  priKey = resp.getData() == null ? "":resp.getData().toString();
					
					
					map.put("reqTimestamp", ticket.getRequestTimestamp());
					map.put("agentid", ticket.getAgentId()+"");
					map.put("tkId", ""+ ticket.getTid());
					map.put("tradePrice", ticket.getTrade_price() + "");
					map.put("status", ACCEPT_STATUS);
					
					map.put("sign", Md5Util.md5("agentid"+ticket.getAgentId() + "reqTimestamp"+ ticket.getRequestTimestamp() + "status" + ACCEPT_STATUS + "tkId"+ticket.getTid() + "tradePrice" + ticket.getTrade_price() + priKey));
					String response = ThreadPoolHttpClient.getInstance().post(url, map);
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						String code = ((JSONObject)obj.get("result")).get("resultCode").toString();
						if(ACCEPT_DOWN.equals(code) || ALREADY_DEAL.equals(code) ){
							flag = true;
						}else{
							flag = false;	
						}
					}
				}
			}
			
		}else{
		//拒票
			switch(rejectTicket.getAgent_id()){
				case 100:{
					//新浪
					map.put("tkId", ""+ rejectTicket.getTid());
					map.put("tradePrice", rejectTicket.getTotal()+"");
					map.put("status", "true");
				//	flag = ThreadPoolHttpClient.getInstance().post(url, map) == null?false:true;
					String response = ThreadPoolHttpClient.getInstance().post(url, map);
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						if(StringUtils.isEmpty(((String)obj.get("data")))){
							flag = false;
						}else{
							flag = true;	
						}
					}
					break;
				}
				case 110:{
					//彩票宝
					Response priKey = redisOptApi.getRedisValue(rejectTicket.getAgent_id()+"PriKey");
					map.put("tkId", ""+ rejectTicket.getTid());
					map.put("tradePrice", rejectTicket.getTotal()+"");
					map.put("status", "true");
					map.put("token", Md5Util.md5(rejectTicket.getTid()+"true"+rejectTicket.getTotal()+priKey));
					
					String response = ThreadPoolHttpClient.getInstance().get(url, map);
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						String respCode = ((JSONObject)obj.get("result")).get("resultCode")+"";
						if("9999".equals(respCode) || "1002".equals(respCode)){
							flag = true;
						}else{
							flag = false;	
						}
					}
					break;
				}
			/*	case 111: case 113: case 114: case 115: {
					String priKey = redisOptUtils.getStrFromRedis(rejectTicket.getAgent_id()+"PriKey");
				//	map.put("reqTimestamp", rejectTicket.getRequestTimestamp());
					map.put("agentid", rejectTicket.getAgent_id()+"");
					map.put("tkId", ""+ rejectTicket.getTid());
					map.put("tradePrice", rejectTicket.getTotal() + "");
					map.put("status", REJECT_STATUS);
					
					map.put("sign", Md5Util.md5("agentid" + rejectTicket.getAgent_id() + "status" + REJECT_STATUS + "tkId" + rejectTicket.getTid() + "tradePrice" + rejectTicket.getTotal() + priKey));  //"reqTimestamp"+ rejectTicket.getRequestTimestamp() +
					String response = ThreadPoolHttpClient.getInstance().post(url, map);
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						if("success".equals(((JSONObject)obj.get("result")).get("resultCode"))){
							flag = true;
						}else{
							flag = false;	
						}
					}
					break;
				}*/
				
				default : {
					Response resp = redisOptApi.getRedisValue(rejectTicket.getAgent_id()+"PriKey");
					String  priKey = resp.getData() == null ? "":resp.getData().toString();
					Log.info(resp.getResult().getResultCode() +resp.getResult().getResultMsg());
					
					map.put("reqTimestamp", rejectTicket.getRequestTimestamp());
					map.put("agentid", rejectTicket.getAgent_id()+"");
					map.put("tkId", ""+ rejectTicket.getTid());
					map.put("tradePrice", rejectTicket.getTotal() + "");
					map.put("status", REJECT_STATUS);
					
					map.put("sign", Md5Util.md5("agentid" + rejectTicket.getAgent_id() + "reqTimestamp" + rejectTicket.getRequestTimestamp() + "status" + REJECT_STATUS + "tkId" + rejectTicket.getTid() + "tradePrice" + rejectTicket.getTotal() + priKey));
					String response = ThreadPoolHttpClient.getInstance().post(url, map);
					
					if(response == null){
						flag = false;
					}else{
						JSONObject obj = (JSONObject) JSON.parse(response);
						String code = ((JSONObject)obj.get("result")).get("resultCode").toString();
						String message = ((JSONObject)obj.get("result")).get("resultMsg").toString();
						if(REJECT_DOWN.equals(code) || ALREADY_DEAL.equals(code)){
							flag = true;
						}else{
							flag = false;
							Log.error(code+message);
						}
					}
				}  
			}
		}
		
		return flag;
	}
	

}