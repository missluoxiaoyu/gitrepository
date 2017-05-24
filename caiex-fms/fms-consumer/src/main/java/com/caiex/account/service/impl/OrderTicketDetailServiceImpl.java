package com.caiex.account.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.entity.OrderTicketMatches;
import com.caiex.account.mapper.OrderTicketDetailMapper;
import com.caiex.account.service.OrderTicketDetailService;
@Service
public class OrderTicketDetailServiceImpl implements OrderTicketDetailService{

	@Autowired
	private OrderTicketDetailMapper mapper;

	@Override
	public OrderTicketMatches getmatches() {
		
		return null;
	}
	

}
