package com.caiex.fms.job;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.caiex.dbservice.historydb.api.SummaryProHisService;
import com.caiex.dbservice.model.OrderTicketDetailFinancialModel;
import com.caiex.fms.fb.service.SummaryService;
import com.caiex.fms.fb.service.impl.AgentInfoServiceImpl;

@Component
public class BackGroundTask {
	private final static Logger log = Logger.getLogger(BackGroundTask.class);
	
	@Autowired
	private SummaryService summary;
	
	@Autowired
	private SummaryProHisService summaryProHisService;
	
	private String Football="1";
	private String Basketball ="2";
	private  static   int count = 0;
	
	//后台任务summary
	@Scheduled(fixedRate = 10*60*1000)
	public void summary() throws Exception{
		count++;
		
		  Calendar cal = Calendar.getInstance();
		   String year = String.valueOf(cal.get(Calendar.YEAR));//获取年份
		   String month=String.valueOf(cal.get(Calendar.MONTH)+1);//获取月份
		   String day=String.valueOf(cal.get(Calendar.DATE));//获取日
		   log.info("正在查询"+year+"年"+month+"月"+day+"日的summary数据");
		   
		if(count==1){//第一次加载
			long str = System.currentTimeMillis();
			summary.addAllInfo(year, month, day, Football);//足球
			summary.addAllInfo(year, month, day, Basketball);//篮球
			long end =System.currentTimeMillis();
			log.info("第一次加载历史数据耗时"+(end-str));
		}else{
			log.info("定时查询summary本年截止到今日，本月截止到今日，本周截止到今日的数据，时间间隔为10分钟");
			summary.addInfo(year, month, day, Football);//足球
			summary.addInfo(year, month, day, Basketball);//篮球
		}
	
		
		
	}
}


 /* public  String objectSerialiable(Object obj){
    String serStr = null;
    try {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);  
        objectOutputStream.writeObject(obj);    
        serStr = byteArrayOutputStream.toString("ISO-8859-1");  
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");  
          
        objectOutputStream.close();  
        byteArrayOutputStream.close();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    return serStr;
}

//字符串反序列化为对象
public   Object objectDeserialization(String serStr){
    Object newObj = null;
    try {
        String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");  
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));  
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);   
        newObj = objectInputStream.readObject();
        objectInputStream.close();  
        byteArrayInputStream.close();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return newObj;
}	


*/
