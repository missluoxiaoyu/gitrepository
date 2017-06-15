package com.caiex.account.mapper;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderCancel;

@MapperScan
public interface OrderCancelMapper {
	OrderCancel  queryByOrderTicket(OrderCancel orderCancel);
	
}
