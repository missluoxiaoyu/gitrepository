/*package com.caiex.account.service.impl;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiex.account.constants.AccountSystemConstants;
import com.caiex.account.entity.AgentInfo;
import com.caiex.account.entity.MatchInfo;
import com.caiex.account.entity.OrderCancel;
import com.caiex.account.entity.OrderTicketDetail;
import com.caiex.account.entity.OrderTicketMatches;
import com.caiex.account.mapper.AgentInfoMapper;
import com.caiex.account.mapper.MatchInfoMapper;
import com.caiex.account.mapper.OrderCancelMapper;
import com.caiex.account.mapper.OrderTicketDetailMapper;
import com.caiex.account.mapper.OrderTicketMapper;
import com.caiex.account.mapper.OrderTicketMatchesMapper;
import com.caiex.account.model.OrderTicketDetailSummaryModel;
import com.caiex.account.model.OrderTicketDetailUMModel;
import com.caiex.account.model.OrderTicketModel;
import com.caiex.account.service.UserManagementService;
import com.caiex.account.utils.NumberUtil;
import com.caiex.account.utils.PoiUtil;
import com.caiex.account.utils.Response;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserManagementServiceImpl implements UserManagementService{

	@Autowired
	private AgentInfoMapper  agentInfoMapper;
	@Autowired
	private OrderTicketMapper orderTicketmapper;
	@Autowired
	private OrderTicketDetailMapper orderTicketDetailMapper;
	@Autowired
	private OrderTicketMatchesMapper orderTicketMatchesMapper;
	@Autowired
	private OrderCancelMapper orderCancelMapper;
	@Autowired
	private MatchInfoMapper matchInfoMapper;
	
	//根据页面上参数查询数据分页返回
	@Override
	public Map<String, Object>  queryAll(OrderTicketModel orderTicket) throws Exception{

		Map<String, Object> model = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar   calendar   =   new  GregorianCalendar();  
		
		String startDate=orderTicket.getStartDate();//开始时间
		if(startDate!=null &&  !"".equals(startDate)){
			orderTicket.setStartDate(startDate+" 00-00-00");
		}
		String endDate=orderTicket.getEndDate();//结束时间
		if(endDate!=null &&  !"".equals(endDate)){
			Date dates = sdf.parse(endDate);  
	        calendar.setTime(dates);  
	        calendar.add(calendar.DATE,+1);
	        String newEndDate = sdf.format(calendar.getTime()); 
			orderTicket.setEndDate(newEndDate+" 00-00-00");
		}
		
		PageHelper.startPage(orderTicket.getPage(), orderTicket.getSize());//分页
		
		List<OrderTicketModel> tradelist = new ArrayList<>();
		//List<OrderTicketModel> listSize=new ArrayList<OrderTicketModel>();
		
		if(orderTicket.getTradeType() == null){//查询order_ticket和历史表 还有取消表
			tradelist=  orderTicketmapper.queryTicketInfo(orderTicket);
			//listSize=orderTicketmapper.queryTicketInfoExcel(orderTicket);
		}else{
			if(orderTicket.getTradeType()==1){//order_ticket和历史表
				tradelist=  orderTicketmapper.queryTicketInfoSuccess(orderTicket);
				//listSize=orderTicketmapper.queryTicketInfoSuccessExcel(orderTicket);
			}else{//取消表
				tradelist=  orderTicketmapper.queryTicketInfoCancel(orderTicket);
				//listSize=orderTicketmapper.queryTicketInfoCancelExcel(orderTicket);
			}
		}
		
		new PageInfo<>(tradelist);
		
		List<OrderTicketModel> NewList=new ArrayList<OrderTicketModel>();
		Integer totalInvestment= 0;
		Double trade_price= 0.0;
		Double totaltradePrice= 0.0;
		Double winMoney= 0.0;
		Double totalprice= 0.0;
		
		for (int i = 0; i < tradelist.size(); i++) {
			OrderTicketModel ticket=tradelist.get(i);
			AgentInfo agent=agentInfoMapper.queryAgentInfoByAgentid(ticket.getAgentId());
			ticket.setAgentName(agent.getAgentName());
			
			if(ticket.getTotalInvestment()==null){
				ticket.setTotalInvestment(0);
			}
			if(ticket.getTradePrice()==null){
				ticket.setTradePrice(0.0);
			}
			
			if(ticket.getTradeType()!=1){
				ticket.setTradePrice(0.0);
			}
			
			if(ticket.getState()==1){//1中奖2未中奖3alive
				totalprice=ticket.getTotalPrice()+totalprice;
			}
			
			totaltradePrice=ticket.getTradePrice()+totaltradePrice;
			Double winMoneys=ticket.getWinMoney();
			
			if(winMoneys==null || "".equals(winMoneys)){
				winMoneys=0.0;
			}
			
			if(ticket.getTradeType()!=7){//看看有没有审批的票
			//有没有审批的
			OrderCancel ordercancel=new OrderCancel();
			ordercancel.setAgentid(ticket.getAgentId());
			ordercancel.setTkid(ticket.getTkId());
	
			OrderCancel orderCan=orderCancelMapper.queryByOrderTicket(ordercancel);
			
			if(orderCan!=null){
				if(orderCan.getApplyState()==0){//待审批
					ticket.setTradeType(4);
				}else if(orderCan.getApplyState()==1){//已通过
					ticket.setTradeType(5);
				}else{//已拒绝
					ticket.setTradeType(6);
				}
			}
		}else{//回收
			if(ticket.getRecycleState()==0){  //0未回收1 已回收
				ticket.setRecyclePrice(-1.0);
			}
			ticket.setState(4);//已回收
		}
			
		if(ticket.getState()==3 && ticket.getRecycleState()==0 ){//alive 未回收
				ticket.setRecyclePrice(-1.0);
			}
		
			//只有 local_m=alive_m 才能取消
			OrderTicketDetail detailParam =new OrderTicketDetail();
			
			detailParam.setTid(ticket.getTicketInfoId());//通过order_ticket_info 关联
			
			List<OrderTicketDetail> listDetail=orderTicketDetailMapper.queryByList(detailParam);
			boolean flag=true;
			for (int j = 0; j < listDetail.size(); j++) {
				OrderTicketDetail detail=listDetail.get(j);
				if(detail.getLocal_m()!=detail.getAlive_m()){//不相等
					flag=false;
				}
            }
			
			if(flag==false){//只有 local_m=alive_m 才能取消
				if(ticket.getTradeType()==4){ //判断是不是待审批的状态
					ticket.setTradeType(44);//已经为待审批的状态
				}
			}
			NewList.add(ticket);
       }
		Double recycleMoney=0.0;
		
		//统计
		for (int i = 0; i < tradelist.size(); i++) {
			
			if(tradelist.get(i).getTradeType()==1){
				trade_price=tradelist.get(i).getTradePrice()+trade_price;
			}
			Double winMoneys=tradelist.get(i).getWinMoney();
			if(winMoneys==null || "".equals(winMoneys)){
				winMoneys=0.0;
			}
			
			if(tradelist.get(i).getTradeType()!=7){
				
				totalInvestment=tradelist.get(i).getTotalInvestment()+totalInvestment;
			}
			
			recycleMoney= tradelist.get(i).getRecyclePrice() ==null || "".equals(tradelist.get(i).getRecyclePrice())?0.0:tradelist.get(i).getRecyclePrice();
		}
		ArrayList<Integer> newlist=this.listSize(tradelist);	
		OrderTicketModel tick=new OrderTicketModel();
		
		tick.setUid(newlist.size()+"");
		
		tick.setTotalInvestment(totalInvestment);
		tick.setTradePrice(NumberUtil.getNumberAccordingToPercision(trade_price,2));
		tick.setWinMoney(winMoney);
		
		if(totaltradePrice ==0.0){
			tick.setPayoutrate(0.0);
		}else{
			tick.setPayoutrate(NumberUtil.getNumberAccordingToPercision((winMoney-totaltradePrice)/totaltradePrice*100, 3));
		}
		tick.setRecyclePrice(0.0);
		
		
		model.put("List", NewList);
		model.put("total", tick);
	    return model;
    }

	
	//用户uid的数量
	public ArrayList<Integer> listSize(List<OrderTicketModel> lists) {
		
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
	public List<OrderTicketDetailUMModel> queryDetail(OrderTicketModel orderTicket) {
	  //用户管理票明细
		OrderTicketDetail model=new OrderTicketDetail();
		model.setTid(orderTicket.getTicketInfoId());
		List<OrderTicketDetail> listDetail=orderTicketDetailMapper.queryByListUnCancel(model);
		List<OrderTicketDetailUMModel> list=new ArrayList<OrderTicketDetailUMModel>();
		
		for (int i = 0; i < listDetail.size(); i++) {
			OrderTicketDetail orderTicketDetail=listDetail.get(i);
			OrderTicketMatches orderTicketMatches=new OrderTicketMatches();
			orderTicketMatches.setSid(orderTicketDetail.getSid());
			List<OrderTicketMatches> listMatch=orderTicketMatchesMapper.queryByListUnCancel(orderTicketMatches);
		
			StringBuffer betline = new StringBuffer();
			String detail="";
			
			for (OrderTicketMatches match : listMatch) {
				betline.append(match.getL_prod());
				betline.append(" ");
				betline.append(match.getL_code());
				betline.append(" * ");
				
				String s=match.getStatistics();
				System.out.println(s);
				String a="";
				String b="";
				if(s!=null && !s.equals("")){
					
				
				String[] ss=s.split("-");
				String sss=ss[0];
				String[] ssss=sss.split("");
				
				if(ssss[0].equals("0")){
					if(ssss[1].equals("0")){
						a="0";
					}else{
						a=ssss[1];
					}
				}else{
					a=ssss[0]+ssss[1];
				}
				
				if(ssss[2].equals("0")){
					if(ssss[3].equals("0")){
						b="0";
					}else{
						b=ssss[3];
					}
				}else{
					b=ssss[2]+ssss[3];
					}
				}
				
				MatchInfo info=new MatchInfo();
				info.setMatch_code(match.getL_code());
				info.setWeek_id(match.getWeekno()+"");
					
				MatchInfo matchInfo=matchInfoMapper.queryByCodeWeek(info);
				if(matchInfo!=null){
					betline.append("[").append(matchInfo.getHome_team()+" vs "+matchInfo.getAway_team())
					.append("]").append(" ").append(match.getL_opt())
					.append("@").append(match.getLocal_odds())
					.append(" ("+match.getL_odds()+") ")
					.append(" ["+a+"-"+b+"] ")
					.append(" / ");
				}
			}
			
			
			if(betline.length()>0){
				detail= betline.substring(0, betline.length()-3);
			}
			OrderTicketDetailUMModel detailModel = new OrderTicketDetailUMModel();
			detailModel.setStatus(orderTicketDetail.getStatus());
			detailModel.setMultiple((NumberUtil.getNumberAccordingToPercision(orderTicketDetail.getInv_allup()==null?0:orderTicketDetail.getInv_allup()/2,2)));
			detailModel.setInvestInfo(detail);
			detailModel.setLocal_m(orderTicketDetail.getLocal_m());
			detailModel.setTotal_price(orderTicketDetail.getTotal_price());
			detailModel.setPrice_allup(orderTicketDetail.getPrice_allup());
			list.add(detailModel);
        }
	    return list;
    }

	@Override
    public 	Response userManagementExcel( HttpServletResponse response, OrderTicketModel orderTicket) throws Exception {
		Response res =new Response();
		List<OrderTicketModel> list = new ArrayList<>();
		
		try {
			Map<String, Object> map = queryAll(orderTicket);
			
			OrderTicketModel total = (OrderTicketModel) map.get("total");
			List<OrderTicketModel> modelList =  (List<OrderTicketModel>) map.get("List");
			
			
			list.add(total);
			for (OrderTicketModel model : modelList) {
				if(model.getState()==AccountSystemConstants.STATE_AWARD){//中奖
					model.setStateMessage("中奖");
				}else if(model.getState()==AccountSystemConstants.STATE_NO_AWARD){
					model.setStateMessage("未中奖");
				}else{
					model.setStateMessage("Alive");
				}
				
				if(model.getRecycleState()==AccountSystemConstants.STATE_NO_RECYCLE){
					model.setRecycleMessage("未回收");
				}else{
					model.setRecycleMessage(model.getRecyclePrice()+"");
				}
				
				if(model.getBallType() ==AccountSystemConstants.BALL_TYPE_FB){
					model.setBallTypeMessage("竞彩足球");
				}else{
					model.setBallTypeMessage("竞彩蓝球");
				}
				
				if(model.getInplay()==AccountSystemConstants.DEAD_BALL){
					model.setInplayMessage("死球");
				}else{
					model.setInplayMessage("即场");
				}
				
				if(model.getTradeType()==AccountSystemConstants.TRADE_SUCCESS){
					model.setTradeMessage("交易成功");
				}else{
					model.setTradeMessage("交易失败");
				}
			}
			list.add(null);
			list.addAll(modelList);
			String strTemplate = "C:/fms/template/user-demo.xls";// 模板位置
		
			String[] strKeyArray = new String[] {"tkId","uid","agentName","ballTypeMessage","bettingproducts","inplayMessage","totalInvestment", "winMoney" ,"addAwardAmount","tradeTime","tradePrice","tradeMessage","stateMessage","totalPrice","recycleMessage"};
					
			HSSFWorkbook workbook = PoiUtil.getTListToExcel(strTemplate, 1, 1,list, strKeyArray, OrderTicketModel.class);
			PoiUtil.returnExcel(response, workbook, "user_manage");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			return res;
	
		
    }
	@Override
    public List<AgentInfo> queryAllAgentInfo(){
    	return agentInfoMapper.queryAll();
    }
    
}
*/