package com.caiex.fms.fb.service.impl;
/*
 * 程序的简单说明  
 * 用户管理页面：查询：
 * 1、根据查询条件查询，如：今天00点到明天00点开始、查询分三种：1是查询全部（取消+交易成功）；2是查询取消的；3是查询交易成功的
 * 2、根据查询出来的数据查询渠道
 * 3、只有 local_m=alive_m 才能取消  
 * 4、派奖前不可以（只有Alive）手动回收
 * 5、取消之后不能再回收
 * 
 * */
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.currentdb.api.AgentInfoProApi;
import com.caiex.dbservice.currentdb.api.OrderManageProService;
import com.caiex.dbservice.currentdb.entity.AgentInfo;
import com.caiex.dbservice.currentdb.entity.MatchInfo;
import com.caiex.dbservice.currentdb.entity.OrderCancel;
import com.caiex.dbservice.currentdb.entity.OrderTicket;
import com.caiex.dbservice.currentdb.entity.OrderTicketDetail;
import com.caiex.dbservice.currentdb.entity.OrderTicketInfo;
import com.caiex.dbservice.currentdb.entity.OrderTicketMatches;
import com.caiex.dbservice.historydb.api.OrderManageProHisService;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.dbservice.utils.DateUtil;
import com.caiex.fms.constants.AccountSystemConstants;
import com.caiex.fms.fb.service.AllOrderManageService;
import com.caiex.fms.redis.RedisUtil;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PageBean;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;
import com.caiex.oltp.api.dto.TicketDetailInfo;
import com.caiex.oltp.api.dto.TicketInfo;
import com.caiex.oltp.api.dto.TicketMatchInfo;
import com.caiex.reduce.Reduce;
@Service
public class OrderManageServiceImpl implements AllOrderManageService{
	private final static Logger log= Logger.getLogger(OrderManageServiceImpl.class);
	
	
	@Autowired
	private OrderManageProService orderManageProService;
	
	@Autowired
	private OrderManageProHisService orderManageProHisService;

	
	@Autowired
	private AgentInfoProApi agentInfoApi;
	
	@Autowired
	RedisUtil redisDao;
	
	Calendar   calendar   =   new   GregorianCalendar(); 
	
	 @Override
	public Map<String, Object> queryAll(OrderTicketModel orderTicket)throws Exception {

		 Map<String, Object> map = new HashMap<>();
		
		if(orderTicket.getStartDate()!=null &&  !"".equals(orderTicket.getStartDate())){
			orderTicket.setStartDate(orderTicket.getStartDate());
			log.info("订单管理开始查询时间"+orderTicket.getStartDate());
		}
		if(orderTicket.getEndDate()!=null &&  !"".equals(orderTicket.getEndDate())){
			orderTicket.setEndDate(orderTicket.getEndDate());
			log.info("订单管理截止查询时间"+orderTicket.getEndDate());
		}
		if(orderTicket.getAgent_id()!=null && !"".equals(orderTicket.getAgent_id())){
			AgentInfo agent = agentInfoApi.queryAgentInfoByOrderTicket(orderTicket.getAgent_id());
			orderTicket.setAgent_id(agent.getAgentNum());
			log.info("订单管理查询渠道"+orderTicket.getAgent_id());
		}
		List<OrderTicketModel> tradelist = new ArrayList<>();
		List<OrderTicketModel> totalModelList = new ArrayList<>();
		
		if(orderTicket.getTrade_type()==null){
			tradelist = getTicketInfo(orderTicket);
			totalModelList = getTotalInfo(orderTicket);
		}else{
			if(orderTicket.getTrade_type()==AccountSystemConstants.TRADE_SUCCESS){
				tradelist = getTicketInfoSuccess(orderTicket);
				totalModelList = getTotalSuccess(orderTicket);
			}else{
				tradelist = getTicketInfoCancel(orderTicket);
				totalModelList = getCancelSize(orderTicket);
			}
		}
		
		List<OrderTicketModel> list = new ArrayList<>();
	
		list = getAllList(tradelist);
		
		
		map.put("modelList", list);
		map.put("total", getTotal(totalModelList));
	
		return map;
	}
	
	

	
	public List<OrderTicketModel> getAllList( List<OrderTicketModel> tradelist){
		List<OrderTicketModel> list = new ArrayList<>();
		
		for (OrderTicketModel model : tradelist) {
			log.info("方案号为"+model.getTkId()+"的渠道编号为"+model.getAgent_id());
			AgentInfo agentinfo = agentInfoApi.queryAgentInfoByOrderTicket(model.getAgent_id());
			model.setBallType(1);//足球
			
			if(agentinfo == null){
				log.error("方案号为"+model.getTkId()+"的渠道编号为"+model.getAgent_id()+"没有查询到渠道信息");
			}else{
				model.setAgentName(agentinfo.getAgentName());
			}
			
			OrderTicketInfo info = orderManageProService.getCxBonus(model.getTicketInfo_id());
			OrderTicketInfo infoHis = orderManageProHisService.getCxBonus(model.getTicketInfo_id());
			if(info!=null){
				model.setCxBonus(info.getCxBonus());
			}else if(infoHis!=null){
				model.setCxBonus(infoHis.getCxBonus());
			}else{
				model.setCxBonus(new BigDecimal("0"));
			}
			
			
			if(model.getTotalInvestment()==null){
				model.setTotalInvestment(0);
			}
			if(model.getTrade_price()==null){
				model.setTrade_price(0.0);
			}
			if(model.getTrade_type()!=1){
				model.setTrade_price(0.0);
			}
			if(model.getWinMoney()==null){
				model.setWinMoney(new BigDecimal("0"));
			}if(model.getAddAwardAmount()==null){
				model.setAddAwardAmount(0);
			}
			//将加奖额 加至交易额
			model.setTrade_price(NumberUtil.getNumberAccordingToPercision(model.getTrade_price()+model.getCxBonus().doubleValue(), 3));
		
			if(model.getTrade_type() != 7){
				//先去order_cancel里查看是否有取消的票
				OrderCancel orderCancel = orderManageProService.getOrderCancel(model);//看看是否有申请取消的票
				if(orderCancel!=null){
					if(orderCancel.getApplyState()==0){//待审批
						model.setTrade_type(4);
					}else if(orderCancel.getApplyState()==1){//通过
						model.setTrade_type(5);
					}else{//拒绝
						model.setTrade_type(6);
					}
				}
			}else{
				if(model.getRecycleState()==0){
					model.setRecyclePrice(new BigDecimal("-1"));
				}
				model.setState(4);
			}
			
			if(model.getState()==3 && model.getRecycleState() ==0){
				model.setRecyclePrice(new BigDecimal("-1"));
			}
			//查看
			OrderTicketDetail detail=new OrderTicketDetail();
			detail.setTid(model.getTicketInfo_id());
			List<OrderTicketDetail> listDetail=orderManageProService.querydetail(detail);
			boolean flag=true;
			for (int j = 0; j < listDetail.size(); j++) {
				OrderTicketDetail ortd=listDetail.get(j);
				if(ortd.getLocal_m()!=ortd.getAlive_m()){
					flag=false;
				}
            }
			if(flag==false && model.getTrade_type()==4){//该票已申请取消但是并不能取消
					model.setTrade_type(44);
			}
			list.add(model);
		}
		
		return list;
	}
	
	
	
	
	public OrderTicketModel  getTotal( List<OrderTicketModel> totalModelList){
		OrderTicketModel total= new OrderTicketModel();
		total.setSize(totalModelList.size());
		Integer alltotalInvestment =0;
		Double alltradePrice = 0.0;
		Double  allwinMoney = 0.0;
		Double allrecyclePrice = 0.0;
		Double  allrakeRate = 0.0;
		Double allBonus  = 0.0;
		
		for (OrderTicketModel model : totalModelList) {
			if(model.getTrade_type()==1){
				alltradePrice=model.getTrade_price()+alltradePrice;
			}
			if(model.getWinMoney()==null){
				model.setWinMoney(new BigDecimal(0));
			}
			if(model.getTrade_type()!=7){
				
				allwinMoney+=model.getWinMoney().doubleValue();
				
				if(model.getTotalInvestment() != null){
					alltotalInvestment += model.getTotalInvestment();
				}
				 
			}
			if(model.getRecyclePrice()==null){
				model.setRecyclePrice(new BigDecimal(0));
			}
			if(model.getRakeRate()==null){
				model.setRakeRate(0.0);
			}
			
			OrderTicketInfo info = orderManageProService.getCxBonus(model.getTicketInfo_id());
			OrderTicketInfo infoHis = orderManageProHisService.getCxBonus(model.getTicketInfo_id());
			if(info!=null){
				model.setCxBonus(info.getCxBonus());
			}else if(infoHis!=null){
				model.setCxBonus(infoHis.getCxBonus());
			}else{
				model.setCxBonus(new BigDecimal("0"));
			}
			
			allBonus +=model.getCxBonus().doubleValue();
			
			allrecyclePrice += model.getRecyclePrice().doubleValue();
			allrakeRate+=model.getRakeRate();
		}
		
		alltradePrice +=allBonus;//将加奖额 加至交易额
		
		ArrayList<Integer> newlist=this.listSize(totalModelList);
		total.setUid(newlist.size()+"");
		total.setTotalInvestment(alltotalInvestment);
		total.setTrade_price(NumberUtil.getNumberAccordingToPercision(alltradePrice,2));
		allrecyclePrice = NumberUtil.getNumberAccordingToPercision(allrecyclePrice.doubleValue(),2);
		total.setRecyclePrice(new BigDecimal(allrecyclePrice.toString()));
		allwinMoney = NumberUtil.getNumberAccordingToPercision(allwinMoney.doubleValue(), 2);
		total.setWinMoney(new BigDecimal(allwinMoney.toString()));
		
		total.setPayoutrate(alltradePrice==0.0?0:NumberUtil.getNumberAccordingToPercision((allwinMoney.doubleValue()-alltradePrice)/alltradePrice*100, 3));
		
		return total;
	}
	
	
	
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
	public Map<String, Object> queryDetail(OrderTicketModel orderTicket) {
		OrderTicketDetail model=new OrderTicketDetail();
		model.setTid(orderTicket.getTicketInfo_id());
		List<OrderTicketDetail> listDetail=getAllDetail(model);
		List<OrderTicketDetail> list = new ArrayList<>();
		String tkId = "";
		
		
		for (int i = 0; i < listDetail.size(); i++) {
			
			OrderTicketDetail orderTicketDetail=listDetail.get(i);
			OrderTicketMatches orderTicketMatches=new OrderTicketMatches();
			orderTicketMatches.setSid(orderTicketDetail.getSid());
			List<OrderTicketMatches> listMatch=getAllMatches(orderTicketMatches);
			StringBuffer betline = new StringBuffer();
	
			String detail="";
			String l_code="";
			String canceled = "";
			for (Iterator<OrderTicketMatches> iterator2 = listMatch.iterator(); iterator2.hasNext();) {
				OrderTicketMatches matches = (OrderTicketMatches) iterator2.next();
				betline.append(matches.getL_prod());
				betline.append(" ");
				betline.append(matches.getL_code());
				betline.append(" * ");


				// 把0801-1102-1102格式的数据去第一条比分，切成8-1的格式
				String statistics=matches.getStatistics();
				
				String score_a="";
				String score_b="";
				
				if(statistics!=null && !statistics.equals("")){
				
				String[] statistics_array=statistics.split("-");
				String statistic=statistics_array[0];
				
				String scoreHome = statistic.substring(0, 2);
				String scoreAway = statistic.substring(2, 4);
				
				if(scoreHome.contains("VV") || scoreAway.contains("VV")){
					score_a=scoreHome.substring(0,1);
					score_b=scoreAway.substring(0,1);
				}else{
					
					score_a = Integer.valueOf(scoreHome)+"";
					score_b = Integer.valueOf(scoreAway)+"";
				}
				
				}
				
				MatchInfo info=new MatchInfo();
				if("".equals(l_code)){
					l_code=matches.getLocal_m()+"";
				}else{
					l_code=l_code+","+matches.getLocal_m();
				}
				
				info.setMatch_code(matches.getL_code());
				info.setWeek_id(matches.getWeekno()+"");
				MatchInfo matchInfo=(MatchInfo) orderManageProService.queryByCodeWeek(info);
				if(matchInfo!=null){
					betline.append("[").append(matchInfo.getHome_team()+" vs "+matchInfo.getAway_team())
					.append("]").append(" ").append(matches.getL_opt())
					.append("@").append(matches.getLocal_odds())
					.append(" ("+matches.getL_odds()+") ")
					.append(" ["+score_a+"-"+score_b+"] ")
					.append(" / ");
					
					canceled += matchInfo.getHome_score() + "," ;
				}
			}
			
			if(betline.length()>0){
				detail= betline.substring(0, betline.length()-3);
			}
			if(orderTicketDetail.getStatus()==1){
				orderTicketDetail.setStateMessage("中奖");
			}else if(orderTicketDetail.getStatus()==2){
				orderTicketDetail.setStateMessage("未中奖");
			}else{
				orderTicketDetail.setStateMessage("Alive");
			}
			
			
			OrderTicketInfo orderinfo=orderManageProService.queryById(orderTicket.getTicketInfo_id());
			OrderTicketInfo orderHis = orderManageProHisService.queryById(orderTicket.getTicketInfo_id());
			
			if(orderinfo!=null){
				tkId = orderinfo.getTkId();
			}else if(orderHis !=null){
				tkId = orderHis.getTkId();
			}else{
				OrderTicketInfo info =orderManageProService.queryInfoCancelById(orderTicket.getTicketInfo_id());
				tkId = info.getTkId();
			}
			
			
			orderTicketDetail.setL_code(l_code);
			orderTicketDetail.setBetContent(detail);
			orderTicketDetail.setCanceled(canceled);
			orderTicketDetail.setNum(NumberUtil.getNumberAccordingToPercision(orderTicketDetail.getInv_allup()/2, 2));
	
			list.add(orderTicketDetail);
        }
		
		Map<String, Object> map = new HashMap<>();
		map.put("tkId", tkId);
		map.put("detailList", list);
		return map;
			
      }
	   
			
			
	

	
	 
	@Override
	public Response userManagementExcel(HttpServletResponse response, OrderTicketModel orderTicket) throws Exception {
		Response res =new Response();
		
		List<OrderTicketModel> list = new ArrayList<>();
		
		try {
			
			if(orderTicket.getStartDate()!=null &&  !"".equals(orderTicket.getStartDate())){
				orderTicket.setStartDate(orderTicket.getStartDate());
	
			}
			if(orderTicket.getEndDate()!=null &&  !"".equals(orderTicket.getEndDate())){
				orderTicket.setEndDate(orderTicket.getEndDate());
			}
			if(orderTicket.getAgent_id()!=null && !"".equals(orderTicket.getAgent_id())){
				AgentInfo agent = agentInfoApi.queryAgentInfoByOrderTicket(orderTicket.getAgent_id());
				orderTicket.setAgent_id(agent.getAgentNum());
			}
		
			List<OrderTicketModel> totalModelList = new ArrayList<>();

			if(orderTicket.getTrade_type()==null){
				totalModelList = getTotalInfo(orderTicket);
			}else{
				if(orderTicket.getTrade_type()==AccountSystemConstants.TRADE_SUCCESS){
					totalModelList = getTotalSuccess(orderTicket);
				}else{
					totalModelList = getCancelSize(orderTicket);
				}
			}
			
			
			List<OrderTicketModel> modelList = getAllList(totalModelList);
			
			Map<String,Object> map = queryAll(orderTicket);
			OrderTicketModel total  =(OrderTicketModel) map.get("total");
			
			total.setRecycleMessage(total.getRecyclePrice()+"");
			total.setStateMessage(total.getPayoutrate()+"");
			
			list.add(total);
			for (OrderTicketModel model : modelList) {
				
				if (model.getBallType() == 1) {
					model.setBallTypeMessage("竞彩足球");
				} else {
					model.setBallTypeMessage("篮球");
				}
				
				if (model.getInplay() == 0) {
					model.setInplayMessage("死球");
				} else if (model.getInplay() == 1) {
					model.setInplayMessage("即场");
				} else {
					model.setInplayMessage("-");
				}
				
				if (model.getTrade_type() == 0) {
					model.setTradeMessage("未交易");
				} else if (model.getTrade_type() == 1) {
					model.setTradeMessage("交易成功");
				} else if (model.getTrade_type() == 2) {
					model.setTradeMessage("等待");
				} else if (model.getTrade_type() == 3) {
					model.setTradeMessage("交易失败");
				} else if (model.getTrade_type() == 4) {
					model.setTradeMessage("取消申请");
				} else if (model.getTrade_type() == 5) {
					model.setTradeMessage("通过");
				} else if (model.getTrade_type() == 6) {
					model.setTradeMessage("拒绝");
				} else if (model.getTrade_type() == 7) {
					model.setTradeMessage("取消");
				} else if (model.getTrade_type() == 44) {
					model.setTradeMessage("取消申请");
				} else {
					model.setTradeMessage("-");
				}
				
				if (model.getState() == 2) {
					model.setStateMessage("未中奖");
				} else if (model.getState() == 1) {
					model.setStateMessage("中奖");
				} else if (model.getState() == 3) {
					model.setStateMessage("Alive");
				} else {
					model.setStateMessage("-");
				}
				if (model.getWinMoney() == null) {
					model.setWinMoney(new BigDecimal(0));
				}
				
				BigDecimal big = new BigDecimal(-1);
				
				if (model.getRecyclePrice() == null) {
					model.setRecycleMessage("未回收");
				} else if (model.getRecyclePrice() == big || big.equals(model.getRecyclePrice())) {
					model.setRecycleMessage("未回收");
				} else {
					model.setRecycleMessage(model.getRecyclePrice() + "");
				}
				
				
			}
			calculate(modelList);
			list.add(null);
			list.addAll(modelList);
			//String strTemplate = "/opt/FMS/fms/excel/user-demo.xls";// 模板位置
			Resource resource = new ClassPathResource("/excel/user-demo.xls");
			InputStream in = resource.getInputStream();
			
		
			String[] strKeyArray = new String[] {"tkId","uid","agentName","ballTypeMessage","bettingproducts","inplayMessage","totalInvestment", "totalPrice" ,"addAwardAmount","trade_time","trade_price","tradeMessage","stateMessage","winMoney","rakeRate","recycleMessage"};
					
			HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, OrderTicketModel.class);
			PoiUtil.returnExcel(response, workbook, "order_manage");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} 
				
			catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			return res;

	}
	
	@Override
	public List<AgentInfo> queryAllAgentInfo() {
		return agentInfoApi.queryAllAgents();
	}
	
	
		//分页
		public List<OrderTicketModel> getTicketInfoSuccess(OrderTicketModel model){
			List<OrderTicketModel> list = getTotalSuccess(model);
			calculate(list);
			
			 Collections.sort(list,new Comparator<OrderTicketModel>(){  
		            public int compare(OrderTicketModel arg0, OrderTicketModel arg1) {  
		                return arg1.getOrder_id().compareTo(arg0.getOrder_id());  
		            }  
		        });  
			
			PageBean pagebean=new PageBean();//初始化PageBean对象  
			Integer pageCount =pagebean.getPage(list.size(), model.getSize());
			pagebean.setPageCount(pageCount);
			pagebean.setCurPage(model.getPage());
			
			int firstIndex=(model.getPage()-1)*model.getSize();  
			int toIndex=model.getPage()*model.getSize();  
			if(toIndex>list.size()){  
			    toIndex=list.size();  
			}  
			if(firstIndex>toIndex){  
			    firstIndex=0;  
			    pagebean.setCurPage(1);  
			}  
			 //截取数据集合，获得分页数据  
			 List courseList=list.subList(firstIndex, toIndex);
			return courseList;
		}
		
		//成功总的
		public List<OrderTicketModel> getTotalSuccess(OrderTicketModel model){
			List<OrderTicketModel> allList = new ArrayList<OrderTicketModel>();
			List<OrderTicketModel> curList = orderManageProService.queryTicketInfoSuccess(model);
			List<OrderTicketModel> hisList = orderManageProHisService.queryTicketInfoSuccess(model);
			allList.addAll(curList);
			allList.addAll(hisList);
			return allList;
		}
		
		
		//总的
		public List<OrderTicketModel> getTotalInfo(OrderTicketModel model){
			List<OrderTicketModel> allList = new ArrayList<OrderTicketModel>();
			List<OrderTicketModel> curList = orderManageProService.queryTicketInfo(model);
			List<OrderTicketModel> hisList = orderManageProHisService.queryTicketInfoSuccess(model);
			
			allList.addAll(curList);
			allList.addAll(hisList);
			return allList;
		}
		

		//分页
		public List<OrderTicketModel> getTicketInfo(OrderTicketModel model){
			List<OrderTicketModel> list = getTotalInfo(model);
			calculate(list);
			
			 Collections.sort(list,new Comparator<OrderTicketModel>(){  
		            public int compare(OrderTicketModel arg0, OrderTicketModel arg1) {  
		                return arg1.getOrder_id().compareTo(arg0.getOrder_id());  
		            }  
		        });  
			
			
			PageBean pagebean=new PageBean();//初始化PageBean对象  
			Integer pageCount =pagebean.getPage(list.size(), model.getSize());
			pagebean.setPageCount(pageCount);
			pagebean.setCurPage(model.getPage());
			
			int firstIndex=(model.getPage()-1)*model.getSize();  
			int toIndex=model.getPage()*model.getSize();  
			if(toIndex>list.size()){  
			    toIndex=list.size();  
			}  
			if(firstIndex>toIndex){  
			    firstIndex=0;  
			    pagebean.setCurPage(1);  
			}  
			 //截取数据集合，获得分页数据  
			 List<OrderTicketModel> courseList=list.subList(firstIndex, toIndex);
			
			return courseList;
	
	}
		
		
		public void calculate(List<OrderTicketModel> list){
			
			for (OrderTicketModel model : list) {
				if(model.getWinMoney() !=null){
					Double winMoney = NumberUtil.getNumberAccordingToPercision(model.getWinMoney().doubleValue(), 3);
					model.setWinMoney(new BigDecimal(winMoney.toString()));
					
				}
				if(model.getRecyclePrice() !=null){
					Double recyclePrice = NumberUtil.getNumberAccordingToPercision(model.getRecyclePrice().doubleValue(), 3);
					model.setRecyclePrice(new BigDecimal(recyclePrice.toString()));	
				}
				if(model.getTotalPrice() !=null){
					model.setTotalPrice(NumberUtil.getNumberAccordingToPercision(model.getTotalPrice(),3));
				}
				if(model.getTrade_price() !=null){
					model.setTrade_price(NumberUtil.getNumberAccordingToPercision(model.getTrade_price(), 3));
				}
			}
			
		}
		
		
		//分页
		public List<OrderTicketModel> getTicketInfoCancel(OrderTicketModel model){
			return orderManageProService.queryTicketCancelPage(model);
		}
		
		//总的
		public List<OrderTicketModel> getCancelSize(OrderTicketModel model){
			return orderManageProService.queryTicketCancel(model);
		}
		
		
		

		public List<OrderTicketDetail> getAllDetail(OrderTicketDetail detail){
			List<OrderTicketDetail> allList = new ArrayList<>();
			allList.addAll(orderManageProService.querydetail(detail));
			allList.addAll(orderManageProService.querydetailCancel(detail));
			allList.addAll(orderManageProHisService.querydetail(detail));
			return allList;
		}
		
		public List<OrderTicketMatches> getAllMatches(OrderTicketMatches orderTicketMatches){
			List<OrderTicketMatches> allList = new ArrayList<>();
			allList.addAll(orderManageProService.queryMatches(orderTicketMatches));
			allList.addAll(orderManageProService.queryMatchesCancel(orderTicketMatches));
			allList.addAll(orderManageProHisService.queryMatches(orderTicketMatches));
			return allList;
		}
	
}
