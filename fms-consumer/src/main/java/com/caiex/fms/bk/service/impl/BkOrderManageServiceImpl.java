package com.caiex.fms.bk.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.basketball.api.BkAgentProApi;
import com.caiex.dbservice.basketball.api.BkOrderManageProApi;
import com.caiex.dbservice.basketball.entity.BasketBallAgentInfo;
import com.caiex.dbservice.basketball.entity.BasketBallMatchInfo;
import com.caiex.dbservice.basketball.entity.BasketBallOrderCancel;
import com.caiex.dbservice.basketball.entity.BasketBallOrderTicketDetail;
import com.caiex.dbservice.basketball.entity.BasketBallOrderTicketInfo;
import com.caiex.dbservice.basketball.entity.BasketBallOrderTicketMatches;
import com.caiex.dbservice.currentdb.entity.OrderTicketInfo;
import com.caiex.dbservice.historydb.api.BkOrderManageProHisApi;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.fms.bk.service.BkOrderManageService;
import com.caiex.fms.constants.AccountSystemConstants;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PageBean;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;
@Service
public class BkOrderManageServiceImpl implements BkOrderManageService{
	private final static Logger log= Logger.getLogger(BkOrderManageServiceImpl.class);

	@Autowired
	private BkOrderManageProApi  bkOrderManageProApi;
	
	@Autowired
	private BkOrderManageProHisApi bkOrderManageProHisApi;
	
	@Autowired
	BkAgentProApi basketBallAgentApi;
	
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
				BasketBallAgentInfo agent = basketBallAgentApi.queryAgentInfoByOrderTicket(orderTicket.getAgent_id());
				orderTicket.setAgent_id(agent.getAgentNum());
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
			BasketBallAgentInfo agentinfo = basketBallAgentApi.queryAgentInfoByOrderTicket(model.getAgent_id());
			model.setBallType(2);//蓝球
			
			if(agentinfo == null){
				log.error("方案号为"+model.getTkId()+"的渠道编号为"+model.getAgent_id()+"没有查询到渠道信息");
			}else{
				model.setAgentName(agentinfo.getAgentName());
			}
			
			BasketBallOrderTicketInfo info = bkOrderManageProApi.getCxBonus(model.getTicketInfo_id());
			BasketBallOrderTicketInfo infoHis = bkOrderManageProHisApi.getCxBonus(model.getTicketInfo_id());
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
			
			model.setTrade_price(model.getTrade_price()+model.getCxBonus().doubleValue());
			
			if(model.getTrade_type() != 7){
				//先去order_cancel里查看是否有取消的票
				BasketBallOrderCancel orderCancel = bkOrderManageProApi.getOrderCancel(model);//看看是否有申请取消的票
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
			BasketBallOrderTicketDetail detail=new BasketBallOrderTicketDetail();
			detail.setTid(model.getTicketInfo_id());
			List<BasketBallOrderTicketDetail> listDetail=bkOrderManageProApi.querydetail(detail);
			boolean flag=true;
			for (int j = 0; j < listDetail.size(); j++) {
				BasketBallOrderTicketDetail ortd=listDetail.get(j);
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
				alltotalInvestment +=model.getTotalInvestment();
			}
			if(model.getRecyclePrice()==null){
				model.setRecyclePrice(new BigDecimal(0));
			}
			if(model.getRakeRate()==null){
				model.setRakeRate(0.0);
			}
			
			BasketBallOrderTicketInfo info = bkOrderManageProApi.getCxBonus(model.getTicketInfo_id());
			BasketBallOrderTicketInfo infoHis = bkOrderManageProHisApi.getCxBonus(model.getTicketInfo_id());
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
	
	
	
	
	//查看方案明细
	@Override
	public Map<String, Object> queryDetail(OrderTicketModel orderTicket) {
		BasketBallOrderTicketDetail model=new BasketBallOrderTicketDetail();
		model.setTid(orderTicket.getTicketInfo_id());
		//查询所有detail信息
		List<BasketBallOrderTicketDetail> listDetail=getAllDetail(model);
		List<BasketBallOrderTicketDetail> list = new ArrayList<>();
		
		String tkId = "";
		
		for (int i = 0; i < listDetail.size(); i++) {
			
			BasketBallOrderTicketDetail orderTicketDetail = listDetail.get(i);
			BasketBallOrderTicketMatches orderTicketMatches = new BasketBallOrderTicketMatches();
			orderTicketMatches.setSid(orderTicketDetail.getSid());
			List<BasketBallOrderTicketMatches> listMatch=getAllMatches(orderTicketMatches);
			StringBuffer betline = new StringBuffer();
	
			String detail="";
			String l_code="";
			String canceled = "";
			for (Iterator<BasketBallOrderTicketMatches> iterator2 = listMatch.iterator(); iterator2.hasNext();) {
				BasketBallOrderTicketMatches matches = (BasketBallOrderTicketMatches) iterator2.next();
				betline.append(matches.getL_prod());
				betline.append(" ");
				betline.append(matches.getL_code());
				betline.append(" * ");
				
				//切割比分 statistics是一串数字  
				String statistics=matches.getStatistics();
				
				String score_a="";
				String score_b="";
				if(statistics!=null && !statistics.equals("")){
				
				String[] statistics_array=statistics.split("-");
				String statistic=statistics_array[1];
				
				String scoreAway = statistic.substring(0, 3);
				String scoreHome = statistic.substring(3, 6);
				if(scoreAway.indexOf("V") != -1 || scoreHome.indexOf("V") != -1){
					score_a = scoreAway;
					score_b = scoreHome;
				}else{

					score_a = Integer.valueOf(scoreAway)+"";
					score_b = Integer.valueOf(scoreHome)+"";
				}
				
			}
				BasketBallMatchInfo info=new BasketBallMatchInfo();
				if("".equals(l_code)){
					l_code=matches.getLocal_m()+"";
				}else{
					l_code=l_code+","+matches.getLocal_m();
				}
				
				info.setMatch_code(Integer.valueOf(matches.getL_code()));
				info.setWeek_id(matches.getWeekno()+"");
				BasketBallMatchInfo matchInfo=(BasketBallMatchInfo) bkOrderManageProApi.queryByCodeWeek(info);
				if(matchInfo!=null){
					betline.append("[").append(matchInfo.getAway_team()+" vs "+matchInfo.getHome_team())
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
			
			BasketBallOrderTicketInfo orderinfo=bkOrderManageProApi.queryById(orderTicket.getTicketInfo_id());
			BasketBallOrderTicketInfo orderHis = bkOrderManageProHisApi.queryById(orderTicket.getTicketInfo_id());
			if(orderinfo!=null){
				tkId = orderinfo.getTkId();
			}else if(orderHis !=null){
				tkId =orderHis.getTkId();
			}else{
				BasketBallOrderTicketInfo info =bkOrderManageProApi.queryInfoCancelById(orderTicket.getTicketInfo_id());
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

	//导出excel
	@Override
	public Response userManagementExcel(HttpServletResponse response,OrderTicketModel orderTicket) throws Exception {
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
				BasketBallAgentInfo agent = basketBallAgentApi.queryAgentInfoByOrderTicket(orderTicket.getAgent_id());
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
			
			OrderTicketModel total  =getTotal(modelList);
			
			total.setRecycleMessage(total.getRecyclePrice()+"");
			total.setStateMessage(total.getPayoutrate()+"");
			Double trPrice = 0.0;
			
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
			
			total.setTrade_price(trPrice);
			Double pay = total.getWinMoney()==null?0:total.getWinMoney().doubleValue()-trPrice;
			total.setPayoutrate(trPrice==0.0?0:NumberUtil.getNumberAccordingToPercision(pay/trPrice*100, 3));
			
			list.add(total);
			list.add(null);
			list.addAll(modelList);

			Resource resource = new ClassPathResource("/excel/user-demo.xls");
			InputStream in = resource.getInputStream();
			
			String[] strKeyArray = new String[] {"tkId","uid","agentName","ballTypeMessage","bettingproducts","inplayMessage","totalInvestment", "totalPrice" ,"addAwardAmount","trade_time","trade_price","tradeMessage","stateMessage","winMoney","rakeRate","recycleMessage"};
					
			HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, OrderTicketModel.class);
			PoiUtil.returnExcel(response, workbook, "order_manage");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			} catch (Exception e) {
				res.getResult().setResultCode(0);
				res.getResult().setResultMsg("fail");
			}
			
		return res;
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
			List<OrderTicketModel> curList = bkOrderManageProApi.queryTicketInfoSuccess(model);
			List<OrderTicketModel> hisList = bkOrderManageProHisApi.queryTicketInfoSuccess(model);
			allList.addAll(curList);
			allList.addAll(hisList);
			return allList;
		}
			
			
	//总的
	public List<OrderTicketModel> getTotalInfo(OrderTicketModel model){
			List<OrderTicketModel> allList = new ArrayList<OrderTicketModel>();
		    List<OrderTicketModel> curList = bkOrderManageProApi.queryTicketInfo(model);
			List<OrderTicketModel> hisList = bkOrderManageProHisApi.queryTicketInfoSuccess(model);
			
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
				return bkOrderManageProApi.queryTicketCancelPage(model);
			
			}
			
			//总的
			public List<OrderTicketModel> getCancelSize(OrderTicketModel model){
				return  bkOrderManageProApi.queryTicketCancel(model);
				
			}
			

			public List<BasketBallOrderTicketDetail> getAllDetail(BasketBallOrderTicketDetail detail){
				List<BasketBallOrderTicketDetail> allList = new ArrayList<>();
				allList.addAll(bkOrderManageProApi.querydetail(detail));
				allList.addAll(bkOrderManageProApi.querydetailCancel(detail));
				allList.addAll(bkOrderManageProHisApi.querydetail(detail));
				return allList;
			}
			
			public List<BasketBallOrderTicketMatches> getAllMatches(BasketBallOrderTicketMatches orderTicketMatches){
				List<BasketBallOrderTicketMatches> allList = new ArrayList<>();
				allList.addAll(bkOrderManageProApi.queryMatches(orderTicketMatches));
				allList.addAll(bkOrderManageProApi.queryMatchesCancel(orderTicketMatches));
				allList.addAll(bkOrderManageProHisApi.queryMatches(orderTicketMatches));
				return allList;
			}
	
	
	@Override
	public List<BasketBallAgentInfo> queryAllAgentInfo() {
		return basketBallAgentApi.queryAllAgents();
	}

}
