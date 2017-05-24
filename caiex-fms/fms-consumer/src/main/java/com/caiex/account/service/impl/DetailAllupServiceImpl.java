package com.caiex.account.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.entity.OrderTicketDetailHis;
import com.caiex.account.mapper.OrderTicketDetailHisMapper;
import com.caiex.account.service.DetailAllupService;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;



@Service
public class DetailAllupServiceImpl implements DetailAllupService{
	private final static Logger log= Logger.getLogger(DetailAllupServiceImpl.class);
	
	@Autowired
    private OrderTicketDetailHisMapper dao;

	@Override
	public Map<String, Object> queryAll(String date)  {
		 Map<String, Object> map=new HashMap<String, Object>();
	       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	       Date dates = null;
		try {
			dates = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}  
         Calendar   calendar   =   new   GregorianCalendar();   
         calendar.setTime(dates);  
         calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动   
         //这个时间就是日期往后推一天的结果   
         String putDate = sdf.format(calendar.getTime()); //增加一天后的日期  
	       String startDate=date+" "+"12:00:00";
	       String endDate=putDate+" "+"12:00:00";
	       
	       List <OrderTicketDetailHis> listHAD =  replace(startDate, endDate, "HAD");
	       List <OrderTicketDetailHis> listHHAD =  replace(startDate, endDate, "HHAD");
	       List <OrderTicketDetailHis> listHAFU = replace(startDate, endDate, "HAFU");
	       List <OrderTicketDetailHis> listTTG =  replace(startDate, endDate, "TTG");
	       List <OrderTicketDetailHis> listCRS =  replace(startDate, endDate, "CRS");
	       List <OrderTicketDetailHis> listFCA =  replace(startDate, endDate, "FCA");
	       
	      
	       OrderTicketDetailHis orderTicketDetailHis=new OrderTicketDetailHis();
	       orderTicketDetailHis.setStartDate(startDate);
	       orderTicketDetailHis.setEndDate(endDate);
	       
	       orderTicketDetailHis.setProduct("HHAD");
	       
	       OrderTicketDetailHis order3=new OrderTicketDetailHis();
		      Double totalInvest=0.0;
		      Double totalLib=0.0;
		      OrderTicketDetailHis totalIn= (OrderTicketDetailHis) dao.detailHisQueryTotalInvest(orderTicketDetailHis);
		      OrderTicketDetailHis totalLi= (OrderTicketDetailHis) dao.detailHisQueryTotalLib(orderTicketDetailHis);
		      OrderTicketDetailHis totalInv= (OrderTicketDetailHis) dao.queryTotalInvests(orderTicketDetailHis);
		      Integer totalInvestment=0;
		      if(totalInv!=null){
		    	  totalInvestment=totalInv.getTotalInvestment()==null?0:totalInv.getTotalInvestment();
		      }
		      if(totalIn!=null){
		    	  totalInvest=totalIn.getPrice_allup();
		      } if(totalLi!=null){
		    	  totalLib=totalLi.getTotal_price();
		      }
		      Double totalPl=totalLib-totalInvest;
		      Double totalPlt=0.0;
		      if(totalInvest!=0.0){
		    	  totalPlt=totalPl/totalInvest*100;
		      }
		      order3.setPrice_allup(NumberUtil.getNumberAccordingToPercision(totalInvest,3));
		      order3.setTotal_price(NumberUtil.getNumberAccordingToPercision(totalLib,3));
		      order3.setPl(NumberUtil.getNumberAccordingToPercision(totalPl,3));
		      order3.setPlt(NumberUtil.getNumberAccordingToPercision(totalPlt,3));
		      order3.setTotalInvestment(totalInvestment);
		  
		      
		   map.put("model", order3);
	       map.put("listHAD", listHAD);
	       map.put("listHHAD", listHHAD);
	       map.put("listHAFU", listHAFU);
	       map.put("listTTG", listTTG);
	       map.put("listCRS", listCRS);
	       map.put("listFCA", listFCA);
	      
		return map;
	}

/**将查出来的内容进行计算*/
	public List replace(String startDate ,String endDate,String product){
		OrderTicketDetailHis orderticketDetail=new OrderTicketDetailHis();
		orderticketDetail.setStartDate(startDate);
		orderticketDetail.setEndDate(endDate);
		orderticketDetail.setProduct(product);
	     
		List<OrderTicketDetailHis>  listOld= dao.queryAllup(orderticketDetail);
	    List<OrderTicketDetailHis>  listNew=new ArrayList<OrderTicketDetailHis>();

	      for(int i=0;i<listOld.size();i++){
	    	  
	    	  OrderTicketDetailHis order=listOld.get(i);
	    	  if(order!=null){
	    		Double invest= order.getPrice_allup();//获取此票总投注额
	    		Double lib=order.getTotal_price();//获取最大中奖额
	    		if(invest == null){
	    			invest = 0.0;
	    		}if(lib == null){
	    			lib = 0.0;
	    		}
	    		  Double pl=lib-invest;
	    		  Double plt=0.0;
	    		  if(invest != 0.0 && !invest.equals("")){
	    			  plt=pl/invest*100;
	    		  }
	    		  order.setProduct(product);
	    		  order.setPrice_allup(NumberUtil.getNumberAccordingToPercision(invest,3));//保留三位有效数字
	    		  order.setTotal_price(NumberUtil.getNumberAccordingToPercision(lib,3));//保留三位有效数字
	    		  order.setPl(NumberUtil.getNumberAccordingToPercision(pl,3));//保留三位有效数字
	    		  order.setPlt(NumberUtil.getNumberAccordingToPercision(plt,3));//保留三位有效数字
	    		  order.setTotalInvestment(order.getTotalInvestment()==null?0:order.getTotalInvestment());
	    		  listNew.add(order);
	    	  }
	      }
	      return listNew;
	}
	
	
	@Override
	public void dailyAllupExcel(HttpServletRequest request,
			HttpServletResponse response, String date) {
		
		Map<String, Object> map=this.queryAll(date);
		
		OrderTicketDetailHis model= (OrderTicketDetailHis) map.get("model");
		List<OrderTicketDetailHis> listHAD=(List<OrderTicketDetailHis>) map.get("listHAD");
		List<OrderTicketDetailHis> listHHAD=(List<OrderTicketDetailHis>) map.get("listHHAD");
		List<OrderTicketDetailHis> listHAFU=(List<OrderTicketDetailHis>) map.get("listHAFU");
		List<OrderTicketDetailHis> listTTG=(List<OrderTicketDetailHis>) map.get("listTTG");
		List<OrderTicketDetailHis> listCRS=(List<OrderTicketDetailHis>) map.get("listCRS");
		List<OrderTicketDetailHis> listFCA=(List<OrderTicketDetailHis>) map.get("listFCA");
		List<OrderTicketDetailHis> list=new ArrayList<OrderTicketDetailHis>();
		model.setWeekDate(null);
		list.add(model);
		list.addAll(listHAD);
		list.addAll(listHHAD);
		list.addAll(listHAFU);
		list.addAll(listTTG);
		list.addAll(listCRS);
		list.addAll(listFCA);
	
	String[] strKeyArray=null;
	String strTemplate ="/opt/FMS/consumer/template/dailyALLUP-demo.xls";// 模板位置
	 strKeyArray = new String[]{"weekDate","totalInvestment","price_allup","total_price","plt", "pl"};	 
	
    HSSFWorkbook workbook=  PoiUtil.getTListToExcel(strTemplate, 2, 2, list, strKeyArray, OrderTicketDetailHis.class);

    PoiUtil.returnExcel(response,workbook,"dailyALLUP");
		
	}

}
