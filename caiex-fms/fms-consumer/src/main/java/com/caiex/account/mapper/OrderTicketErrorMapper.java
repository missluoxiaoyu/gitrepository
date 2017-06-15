package com.caiex.account.mapper;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

import com.caiex.account.entity.OrderTicketError;

@MapperScan
public interface OrderTicketErrorMapper {
	//存储error信息
	void saveTicketError(OrderTicketError ticketError);
	//分页查询error信息
	List<OrderTicketError> getTicketErrors(Map<String,Object> map);
	//查询单个票信息
	public OrderTicketError getTicketErrorByid(int id);
	//根据tkid和agentId 确定唯一票进行查询
	public OrderTicketError queryTicketErrorByTkIdAndAgentId(OrderTicketError ticketError);
	//删除error信息
	public void deleteTicketError(OrderTicketError ticketError);
	//更新state的状态
	public void updateTicketErrorState(OrderTicketError ticketError);
	//更新
	public void updateTicketError(OrderTicketError ticketError);
	//根据起止时间查询
	public List<OrderTicketError> getTicketErrorByTime(Map<String ,Object> paramsMap);
	//查询总的错误票的数量
	public Integer queryOrderTicketErrorAmount(Map<String ,Object> paramsMap);
	
	
}
