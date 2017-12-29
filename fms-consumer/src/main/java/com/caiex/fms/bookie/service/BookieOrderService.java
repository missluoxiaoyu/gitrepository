package com.caiex.fms.bookie.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.caiex.dbservice.model.BookieOrderModel;
import com.caiex.oltp.api.Response;

public interface BookieOrderService {

	Map<String, Object> queryAll(BookieOrderModel orderTicket) throws Exception;

	Object queryAllAgentInfo();

	Response userManagementExcel(HttpServletResponse response,BookieOrderModel orderTicket) throws IOException;

	Map<String, Object> queryDetail(BookieOrderModel orderTicket);

}
