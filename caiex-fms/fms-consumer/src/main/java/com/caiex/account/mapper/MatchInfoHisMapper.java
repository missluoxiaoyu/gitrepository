package com.caiex.account.mapper;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.MatchInfoHis;
import com.caiex.account.entity.OrderTicketMatchesHis;



@MapperScan
public interface MatchInfoHisMapper {
	
	public MatchInfoHis queryBtachList(OrderTicketMatchesHis matches);
}
