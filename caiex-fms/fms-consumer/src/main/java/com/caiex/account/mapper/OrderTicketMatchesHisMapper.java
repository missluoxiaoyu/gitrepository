package com.caiex.account.mapper;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderTicketDetailHis;
import com.caiex.account.entity.OrderTicketMatchesHis;

@MapperScan
public interface OrderTicketMatchesHisMapper {
	//single
	public List<OrderTicketMatchesHis> batchDetailQuery(List<OrderTicketDetailHis> list);
	
	public List<OrderTicketMatchesHis> queryBySid(List<OrderTicketDetailHis> detailHisList);
}
