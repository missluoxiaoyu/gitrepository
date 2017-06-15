package com.caiex.account.mapper;

import org.mybatis.spring.annotation.MapperScan;

import com.fms.entity.MatchInfo;
import com.fms.entity.OrderTicketMatches;


@MapperScan
public interface MatchInfoMapper {
	
	public MatchInfo queryBtachList(OrderTicketMatches matches);
}
