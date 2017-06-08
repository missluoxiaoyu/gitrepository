package com.caiex.account.mapper;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderTicketDetail;


@MapperScan
public interface OrderTicketDetailMapper {
	   //单关的总投注额
		public OrderTicketDetail queryTotalInvestmentFSGL(Map<String, Object> params);
		//单关的总交易额
		public OrderTicketDetail queryinvestFSGL(Map<String, Object> params);
		//单关的中奖额
		public OrderTicketDetail queryTotalPriceFSGL(Map<String, Object> params);
		//单关中的invest
		public OrderTicketDetail queryWinInvest(Map<String, Object> params);
		
		//串关的总投注额
	    public OrderTicketDetail queryTotalInvestmentALLUP(Map<String, Object> params);
		//串关的总交易额
		public OrderTicketDetail queryinvestALLUP(Map<String, Object> params);
		//串关的中奖额
		public OrderTicketDetail queryTotalPriceALLUP(Map<String, Object> params);
		
		
		//0关的总交易额
		public OrderTicketDetail queryinvestLEVEL0(Map<String, Object> params);
		//0关的中奖额
	
		public OrderTicketDetail queryTotalInvestmentLEVEL0(Map<String, Object> params);
		
		//每日统计
		public OrderTicketDetail queryTotalInvestment(Map<String, Object> params);
		
		public OrderTicketDetail queryTotalprice(Map<String, Object> params);
		
		public OrderTicketDetail queryInvest(Map<String, Object> params);
		
		
}
