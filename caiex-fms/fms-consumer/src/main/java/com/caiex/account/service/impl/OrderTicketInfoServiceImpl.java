package com.caiex.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.entity.OrderTicketInfo;
import com.caiex.account.mapper.OrderTicketInfoMapper;
import com.caiex.account.mapper.OrderTicketMapper;
import com.caiex.account.service.OrderTicketInfoService;
@Service
public class OrderTicketInfoServiceImpl implements OrderTicketInfoService{
	@Autowired
	private OrderTicketInfoMapper orderTicketInfoMapper;
	
	
//	@Override
//	public long getIdByTkid(String tkId) {
//		
//		return orderTicketInfoMapper.getIdByTkid(tkId);
//	}

	

}
