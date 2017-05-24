package com.caiex.account.mapper;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderTicketMatches;

@MapperScan
public interface OrderTicketDetailMapper {
	OrderTicketMatches getmatches();
}
