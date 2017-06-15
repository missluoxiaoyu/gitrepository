package com.caiex.account.mapper;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderTicketMatches;

@MapperScan
public interface OrderTicketMatchesMapper {

	List<OrderTicketMatches> queryByListUnCancel(OrderTicketMatches orderTicketMatches);

}
