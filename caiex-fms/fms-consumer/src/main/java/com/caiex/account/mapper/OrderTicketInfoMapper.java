package com.caiex.account.mapper;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderTicket;
import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.entity.OrderTicketInfo;
@MapperScan
public interface OrderTicketInfoMapper {
	//根据tid查询
	public OrderTicketInfo getOrderTicketInfoBytid(String tid);
   //关联查询
	public List<OrderTicketInfo> queryBatch(List<OrderTicket> orderTickets);
}
