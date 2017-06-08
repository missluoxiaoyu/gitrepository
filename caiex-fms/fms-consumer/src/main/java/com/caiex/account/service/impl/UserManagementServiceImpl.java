package com.caiex.account.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.entity.AgentInfo;
import com.caiex.account.mapper.AgentInfoMapper;
import com.caiex.account.mapper.OrderTicketMapper;
import com.caiex.account.model.OrderTicketModel;
import com.caiex.account.service.UserManagementService;
import com.caiex.account.utils.NumberUtil;

@Service
public class UserManagementServiceImpl implements UserManagementService{

	@Autowired
	private AgentInfoMapper  agentInfoMapper;
	
	@Autowired
	private OrderTicketMapper orderTicketmapper;
	
	//将参数封装成对象
	/*public void  queryAll(OrderTicketModel orderTicket) throws Exception {
	    // TODO 用户管理报表 查询
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar   calendar   =   new   GregorianCalendar();  
		//根据渠道名称查询渠道编码
	
		//AgentInfo agentInfo=(AgentInfo) agentInfoDao.queryByAgentCode(agentInfoModel);
			
		String startDate=orderTicket.getStartDate();
		if(startDate!=null &&  !"".equals(startDate)){
			orderTicket.setStartDate(startDate+" 00-00-00");
		}
		String endDate=orderTicket.getEndDate();
		if(endDate!=null &&  !"".equals(endDate)){
			Date dates = sdf.parse(endDate);  
	        calendar.setTime(dates);  
	        calendar.add(calendar.DATE,+1);
	        String newEndDate = sdf.format(calendar.getTime()); 
			orderTicket.setEndDate(newEndDate+" 00-00-00");
		}
		//if(orderTicket.getOrder_id()!=null && !"".equals(orderTicket.getOrder_id())){
		//	orderTicket.setTid(orderTicket.getTid());
		//}
		
		
		
		List<OrderTicketModel> list=new ArrayList<OrderTicketModel>();
		List<OrderTicketModel> listSize=new ArrayList<OrderTicketModel>();
		
		if(orderTicket.getTradeType()==1){
			list=orderTicketmapper.queryTicketInfoSuccess(orderTicket);
			listSize=orderTicketmapper.queryTicketInfoSuccessExcel(orderTicket);
		}else if(orderTicket.getTradeType()==2){
			list=orderTicketmapper.queryTicketInfoCancel(orderTicket);
			listSize=orderTicketmapper.queryTicketInfoCancelExcel(orderTicket);
		}else{
			list=orderTicketmapper.queryTicketInfo(orderTicket);
			listSize=orderTicketmapper.queryTicketInfoExcel(orderTicket);
		}
		
		List<OrderTicketModel> lists=new ArrayList<OrderTicketModel>();
		Integer totalInvestment=0;
		Double trade_price=0.0;
		Double totaltrade=0.0;
		BigDecimal winMoney=new BigDecimal(0);
		Double totalprice=0.0;
		for (int i = 0; i < list.size(); i++) {
			OrderTicketModel ticket=list.get(i);
			//AgentInfo agent=(AgentInfo) agentInfoDao.queryAgentid(ticket);
			//ticket.setAgentName(agent.getAgentName());
			if(ticket.getTotalinvestment()==null){
				ticket.setTotalinvestment(0);
			}
			if(ticket.getTrade_price()==null){
				ticket.setTrade_price(0.0);
			}
			
			if(ticket.getTrade_type()==1){
			
			}else{
				ticket.setTrade_price(0.0);
			}
			
			if(ticket.getState()==1){
				totalprice=ticket.getTotal_price()+totalprice;
			}
			totaltrade=ticket.getTrade_price()+totaltrade;
			
			BigDecimal winMoneys=ticket.getWinMoney();
			if(winMoneys==null || "".equals(winMoneys)){
				winMoneys=new BigDecimal(0);
			}
			//winMoney=winMoneys.add(winMoney);
			if(ticket.getTrade_type()!=7){
			//有没有审批的
			OrderCancelModel ordercancel=new OrderCancelModel();
			ordercancel.setAgentid(ticket.getAgent_id());
			ordercancel.setTkid(ticket.getTkId());
	
			OrderCancel orderCan=(OrderCancel) orderCancelDao.queryByOrderTick(ordercancel);
			if(orderCan!=null){
				if(orderCan.getApplyState()==0){
					ticket.setTrade_type(4);
				}else if(orderCan.getApplyState()==1){
					ticket.setTrade_type(5);
				}else{
					ticket.setTrade_type(6);
				}
			}
		}else{
			if(ticket.getRecycleState()==0){
				ticket.setRecyclePrice(new BigDecimal(-1));
			}
			ticket.setState(4);
		}
			if(ticket.getState()==3 && ticket.getRecycleState()==0 ){
				ticket.setRecyclePrice(new BigDecimal(-1));
			}
		//只有 local_m=alive_m 才能取消
			OrderTicketDetailModel dmodel=new OrderTicketDetailModel();
			dmodel.setTid(ticket.getTid());
			List<OrderTicketDetail> listDe=orderTicketDetailDao.queryByList(dmodel);
			boolean falg=true;
			for (int j = 0; j < listDe.size(); j++) {
				OrderTicketDetail ortd=listDe.get(j);
				if(ortd.getLocal_m()!=ortd.getAlive_m()){
					falg=false;
				}
            }
			if(falg==false){
				if(ticket.getTrade_type()==4){
					ticket.setTrade_type(44);
				}
			}
			lists.add(ticket);
        }
		BigDecimal recycleMoney=new BigDecimal(0);
		//统计
		for (int i = 0; i < listSize.size(); i++) {
			
			if(listSize.get(i).getTrade_type()==1){
				trade_price=listSize.get(i).getTrade_price()+trade_price;
			}
			BigDecimal winMoneys=listSize.get(i).getWinMoney();
			if(winMoneys==null || "".equals(winMoneys)){
				winMoneys=new BigDecimal(0);
			}
			if(listSize.get(i).getTrade_type()!=7){
				winMoney=winMoneys.add(winMoney);
				totalInvestment=listSize.get(i).getTotalinvestment()+totalInvestment;
			}
			
			recycleMoney=recycleMoney.add(listSize.get(i).getRecyclePrice()==null || "".equals(listSize.get(i).getRecyclePrice())?new BigDecimal(0):listSize.get(i).getRecyclePrice());
		}
		ArrayList<Integer> newlist=this.listSize(listSize);	
		OrderTicket tick=new OrderTicket();
		tick.setUid(newlist.size()+"");
		tick.setTotalInvestment(totalInvestment);
		tick.setTrade_price(NumberUtil.getNumberAccordingToPercision(trade_price,2));
		tick.setWinMoney(winMoney);
		if(totaltrade==0.0){
			tick.setPl(0.0);
		}else{
			tick.setPl(NumberUtil.getNumberAccordingToPercision((winMoney.doubleValue()-totaltrade)/totaltrade*100, 3));
		}
		tick.setRecyclePrice(recycleMoney);
		
		dp.setObj(tick);
		dp.setRows(lists);
		dp.setTotal(listSize.size());
	    return dp;
    }

	public ArrayList<Integer> listSize(List<OrderTicket> lists) {
		
		List<Integer> list = new ArrayList<Integer>();//新建一个集合
		for (int i = 0; i < lists.size(); i++) {
			String terminalNo=lists.get(i).getUid();
			list.add(terminalNo==null || "".equals(terminalNo)?0:Integer.parseInt(terminalNo));
        }
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();//新建一个map集合，用来保存重复的次数
		
		for(Integer obj: list){
			
			if(map.containsKey(obj)){//判断是否已经有该数值，如有，则将次数加1
				map.put(obj, map.get(obj).intValue() + 1);
			}else{
				map.put(obj, 1);
			}
		}
		
		ArrayList<Integer> newList = new ArrayList<Integer>(map.values());//新建另外一个list
		return newList;
		
	}

	@Override
    public List<OrderTicketDetail> queryDetail(OrderTicketModel orderTicket) {
	    // TODO 无需测试  用户管理票明细
		OrderTicketDetailModel model=new OrderTicketDetailModel();
		model.setTid(orderTicket.getTid());
		List<OrderTicketDetail> listDetail=orderTicketDetailDao.queryByListUnCancel(model);
		List<OrderTicketDetail> list=new ArrayList<OrderTicketDetail>();

		for (int i = 0; i < listDetail.size(); i++) {
			OrderTicketDetail orderTicketDetail=listDetail.get(i);
			OrderTicketMatchesModel orderTicketMatches=new OrderTicketMatchesModel();
			orderTicketMatches.setSid(orderTicketDetail.getSid());
			List<OrderTicketMatches> listMatch=orderTicketMatchesDao.queryByListUnCancel(orderTicketMatches);
			StringBuffer betline = new StringBuffer();
			String detail="";
			String l_code="";
			for (Iterator<OrderTicketMatches> iterator2 = listMatch.iterator(); iterator2.hasNext();) {
				OrderTicketMatches matches = (OrderTicketMatches) iterator2.next();
				betline.append(matches.getL_prod());
				betline.append(" ");
				betline.append(matches.getL_code());
				betline.append(" * ");
				
				//把0801-1102-1102格式的数据去第一条比分，切成8-1的格式
				String s=matches.getStatistics();
				
				String a="";
				String b="";
				if(s!=null && !s.equals("")){
					
				
				String[] ss=s.split("-");
				String sss=ss[0];
				String[] ssss=sss.split("");
				
				if(ssss[1].equals("0")){
					if(ssss[2].equals("0")){
						a="0";
					}else{
						a=ssss[2];
					}
				}else{
					a=ssss[1]+ssss[2];
				}
				
				if(ssss[3].equals("0")){
					if(ssss[4].equals("0")){
						b="0";
					}else{
						b=ssss[4];
					}
				}else{
					b=ssss[3]+ssss[4];
				}
				}
				
				//String a=ssss[2]+ "-"+ ssss[4];
				//String matchcode=convertMatchCode(matches.getL_code());
				MatchInfoModel info=new MatchInfoModel();
				if("".equals(l_code)){
					l_code=matches.getLocal_m()+"";
				}else{
					l_code=l_code+","+matches.getLocal_m();
				}
				
				info.setMatch_code(matches.getL_code());
				info.setWeek_id(matches.getWeekno()+"");
				MatchInfo matchInfo=(MatchInfo) matchInfoDao.queryByCodeWeek(info);
				if(matchInfo!=null){
					betline.append("[").append(matchInfo.getHome_team()+" vs "+matchInfo.getAway_team())
					.append("]").append(" ").append(matches.getL_opt())
					.append("@").append(matches.getLocal_odds())
					.append(" ("+matches.getL_odds()+") ")
					.append(" ["+a+"-"+b+"] ")
					.append(" / ");
				}
			}
			if(betline.length()>0){
				detail= betline.substring(0, betline.length()-3);
			}
			orderTicketDetail.setL_code(l_code);
			orderTicketDetail.setNum(NumberUtil.getNumberAccordingToPercision(orderTicketDetail.getInv_allup()==null?0:orderTicketDetail.getInv_allup()/2,2));
			orderTicketDetail.setBetContent(detail);
			list.add(orderTicketDetail);
        }
	    return list;
    }

	private String convertMatchCode(String week) {

        String[] splitArr = week.split(" ");

        if ("MON".equals(splitArr[0].trim())) {
            if (splitArr[1].length() == 1) {
                return "100" + splitArr[1];
            } else if (splitArr[1].length() == 2) {
                return "10" + splitArr[1];
            } else {
                return "1" + splitArr[1];
            }
        } else if ("TUE".equals(splitArr[0].trim())) {
            if (splitArr[1].length() == 1) {
                return "200" + splitArr[1];
            } else if (splitArr[1].length() == 2) {
                return "20" + splitArr[1];
            } else {
                return "2" + splitArr[1];
            }
        } else if ("WED".equals(splitArr[0].trim())) {
            if (splitArr[1].length() == 1) {
                return "300" + splitArr[1];
            } else if (splitArr[1].length() == 2) {
                return "30" + splitArr[1];
            } else {
                return "3" + splitArr[1];
            }
        } else if ("THU".equals(splitArr[0].trim())) {
            if (splitArr[1].length() == 1) {
                return "400" + splitArr[1];
            } else if (splitArr[1].length() == 2) {
                return "40" + splitArr[1];
            } else {
                return "4" + splitArr[1];
            }
        } else if ("FRI".equals(splitArr[0].trim())) {
            if (splitArr[1].length() == 1) {
                return "500" + splitArr[1];
            } else if (splitArr[1].length() == 2) {
                return "50" + splitArr[1];
            } else {
                return "5" + splitArr[1];
            }
        } else if ("SAT".equals(splitArr[0].trim())) {
            if (splitArr[1].length() == 1) {
                return "600" + splitArr[1];
            } else if (splitArr[1].length() == 2) {
                return "60" + splitArr[1];
            } else {
                return "6" + splitArr[1];
            }
        } else if ("SUN".equals(splitArr[0].trim())) {
            if (splitArr[1].length() == 1) {
                return "700" + splitArr[1];
            } else if (splitArr[1].length() == 2) {
                return "70" + splitArr[1];
            } else {
                return "7" + splitArr[1];
            }
        } else {
            return null;
        }
    }*/
    
	@Override
    public List<AgentInfo> queryAllAgentInfo(){
    	return agentInfoMapper.queryAll();
    }
    
}
