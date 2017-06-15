package com.caiex.account.mapper;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import com.fms.entity.OrderTicketDetail;
import com.fms.entity.OrderTicketMatches;

@MapperScan
public interface OrderTicketMatchesHisMapper {
	//single
	public List<OrderTicketMatches> batchDetailQuery(List<OrderTicketDetail> list);
	
	public List<OrderTicketMatches> queryBySid(List<OrderTicketDetail> detailHisList);
}
