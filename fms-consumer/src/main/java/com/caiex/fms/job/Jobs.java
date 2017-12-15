/*package com.caiex.fms.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.caiex.dbservice.currentdb.api.AgentInfoApi;
import com.caiex.fms.service.OrderTicketService;
import com.caiex.fms.utils.biYingAgentUrlInfo;
import com.caiex.fms.utils.xiaomiAgentUrlInfo;



@Component
public class Jobs {
	
	private final static Logger log = Logger.getLogger(Jobs.class);
	public final static long ONE_HOUR = 60 * 60 * 1000;
	public final static long ONE_DAY = 24 * 60 * 60 * 1000;
	public final static long FIVE_MINUTE = 5 * 60 * 1000;
	public final static long CESHI = 3 * 1000;
	public static int count=0;
	@Autowired
	private OrderTicketService orderTicketService;
	
	@Autowired
	private  AgentInfoApi agentInfoApi;
	
	@Autowired
	private biYingAgentUrlInfo biying;
	
	@Autowired
	private xiaomiAgentUrlInfo xiaomi;
	

	//定时任务 
	@Scheduled(fixedRate = ONE_HOUR)
	public void fixedRateJob() throws Exception {
		long current=System.currentTimeMillis();//当前时间毫秒数  
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数  
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数  
        Date end = new Date(twelve-ONE_DAY);
		Date str = new Date(zero-ONE_DAY);
	
		count++;
		log.info("当前页数为"+count);
		
		int size = getTicketAmount(str, end, xiaomi.getAgentid());
		if(count > Math.ceil(size/100)){//如果页数增加到结尾处，从1重新开始查询
			count=1;
		}
		
		//Thread.sleep(FIVE_MINUTE);
		//orderTicketService.verifyMore(str,end,biying.getAgentid(),count );//每一小时执行查询昨天0点到12点的票
		orderTicketService.verifyMore(str,end,xiaomi.getAgentid(),count );
		}
	
	
	//获取该时间段的票数
		public Integer getTicketAmount(Date str,Date end,Integer agentid){
			Map<String, Object> dateParams = new HashMap<String, Object>();
			dateParams.put("startDate", str);
			dateParams.put("endDate", end);
			dateParams.put("agentId", agentid);
			int size = agentInfoApi.getOrderAmount(dateParams);
			log.info(agentid+"共有"+size+"张票");
			return size;
		}
	
	
}
*/