package com.caiex.account.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.entity.OrderTicketError;
import com.caiex.account.mapper.OrderTicketErrorMapper;
import com.caiex.account.service.OrderTicketErrorService;
@Service
public class OrderTicketErrorServiceImpl implements OrderTicketErrorService{
	
	@Autowired
	private OrderTicketErrorMapper orderTicketErrorMapper;
	
	//分页查询
	@Override
	public List<OrderTicketError> getTicketErrors(int page,int size,String startDate,String endDate){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("page", (page-1)*size);
		map.put("size", size);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return orderTicketErrorMapper.getTicketErrors(map);
	}
	
	//存贮
	@Override
	public void saveTicketError(OrderTicketError newTicket) {
		//如果这个错误票是第一次存进数据库
		if(orderTicketErrorMapper.queryTicketErrorByTkIdAndAgentId(newTicket) == null){
			orderTicketErrorMapper.saveTicketError(newTicket);
		}else{//不是第一次存贮，判断状态
			OrderTicketError oldTicket = orderTicketErrorMapper.queryTicketErrorByTkIdAndAgentId(newTicket);
			//替换
			if(oldTicket.getTradeState() != newTicket.getTradeState() ){
				orderTicketErrorMapper.updateTicketError(newTicket);
				//orderTicketErrorMapper.saveTicketError(newTicket);
			}
			System.out.println("状态没变，不保存");
		}
	}

	//当数据是正常的时候，将error表中此数据删除
	@Override
	public void deleteTicketError(OrderTicketError ticketError) {
		if(orderTicketErrorMapper.queryTicketErrorByTkIdAndAgentId(ticketError) != null ){
			orderTicketErrorMapper.deleteTicketError(ticketError);
		}	
	}

	//当页面处理后数据库将更新state的状态
	@Override
	public void changeTicketErrorState(OrderTicketError ticketError) {
		orderTicketErrorMapper.updateTicketErrorState(ticketError);	
	}

	//根据id查ticket
	@Override
	public OrderTicketError getTicketErrorByid(int id) {
		return orderTicketErrorMapper.getTicketErrorByid(id);
	}
	
	//根据起止时间查询error表
	@Override
	public List<OrderTicketError> getTicketErrorByTime(String startTime,String endTime){
		Map paramsMap = new HashMap<>();
		paramsMap.put("startTime", startTime);
		paramsMap.put("endTime", endTime);
		return orderTicketErrorMapper.getTicketErrorByTime(paramsMap);
	}
		
}
