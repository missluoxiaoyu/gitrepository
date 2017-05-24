package com.caiex.account.service;

import com.caiex.oltp.api.dto.RefusedTicket;
import com.caiex.oltp.api.dto.TicketInfo;



public interface NotifyResultService {
	public  Boolean notifyMsg(TicketInfo ticket, RefusedTicket rejectTicket, String url);
}
