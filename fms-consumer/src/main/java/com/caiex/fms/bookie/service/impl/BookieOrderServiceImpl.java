package com.caiex.fms.bookie.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.caiex.dbservice.caiexbooker.api.fms.AgentBookieProService;
import com.caiex.dbservice.caiexbooker.api.fms.OrderBookieProService;
import com.caiex.dbservice.caiexbooker.entity.VirBetAgentInfo;
import com.caiex.dbservice.caiexbooker.entity.VirBetMatchInfo;
import com.caiex.dbservice.caiexbooker.entity.VirBetOrderTicketDetail;
import com.caiex.dbservice.caiexbooker.entity.VirBetOrderTicketMatches;
import com.caiex.dbservice.currentdb.entity.MatchInfo;
import com.caiex.dbservice.currentdb.entity.OrderTicketMatches;
import com.caiex.dbservice.historydb.api.bookiefms.OrderBookieProHisService;
import com.caiex.dbservice.model.BookieOrderModel;
import com.caiex.dbservice.model.OrderTicketModel;
import com.caiex.fms.bookie.service.BookieOrderService;
import com.caiex.fms.utils.NumberUtil;
import com.caiex.fms.utils.PageBean;
import com.caiex.fms.utils.PoiUtil;
import com.caiex.oltp.api.Response;
@Service
public class BookieOrderServiceImpl implements BookieOrderService{
	
	
	@Autowired
	private AgentBookieProService  agentBookieProService;
	
	@Autowired
	private OrderBookieProService orderBookieProService;
	
	@Autowired
	private OrderBookieProHisService orderBookieProHisService;
	
	
	
	 @Override
	public Map<String, Object> queryAll(BookieOrderModel orderTicket)throws Exception {

		 Map<String, Object> map = new HashMap<>();
		
		if(orderTicket.getStartDate()!=null &&  !"".equals(orderTicket.getStartDate())){
			orderTicket.setStartDate(orderTicket.getStartDate());
			
		}
		if(orderTicket.getEndDate()!=null &&  !"".equals(orderTicket.getEndDate())){
			orderTicket.setEndDate(orderTicket.getEndDate());
			
		}
		if(orderTicket.getAgentId()!=null && !"".equals(orderTicket.getAgentId())){
			VirBetAgentInfo agent = agentBookieProService.queryAgentInfoByOrderTicket(orderTicket.getAgentId());
			orderTicket.setAgentId(agent.getAgentNum());
			
		}
		List<BookieOrderModel> tradelist = new ArrayList<>();
		List<BookieOrderModel> totalModelList = new ArrayList<>();
		
		tradelist = getTicketInfo(orderTicket);
		totalModelList = getTotalInfo(orderTicket);
		
		List<BookieOrderModel> list = new ArrayList<>();
	
		list = getAllList(tradelist);
		
		
		map.put("modelList", list);
		map.put("total", getTotal(totalModelList));
	
		return map;
	}
	
	

	
	public List<BookieOrderModel> getAllList( List<BookieOrderModel> tradelist){
		List<BookieOrderModel> list = new ArrayList<>();
		
		for (BookieOrderModel model : tradelist) {
			VirBetAgentInfo agentinfo = agentBookieProService.queryAgentInfoByOrderTicket(model.getAgentId());
			model.setBallType(1);//足球
			
			if(agentinfo != null){
				model.setAgentName(agentinfo.getAgentName());
			}
			if(model.getTotalInvestment()==null){
				model.setTotalInvestment(new BigDecimal("0"));
			}
			if(model.getTotalPrice()==null){
				model.setTotalPrice(0.0);
			}
			
			
			list.add(model);
		}
		
		return list;
	}
	
	
	
	
	public Double add(Double addend,Double augend){
		if(addend==null){
			addend=0.0;
		}
		if(augend==null){
			augend=0.0;
		}
		return addend+augend;
	}
	
	
	public BigDecimal add(BigDecimal addend,BigDecimal augend){
		if(addend==null){
			addend=new BigDecimal("0");
		}
		if(augend==null){
			augend=new BigDecimal("0");
		}
		return new BigDecimal(addend.doubleValue()+augend.doubleValue());
	}
	
	
	public BookieOrderModel  getTotal( List<BookieOrderModel> totalModelList){
		BookieOrderModel total= new BookieOrderModel();
		total.setSize(totalModelList.size());
		BigDecimal alltotalInvestment =new BigDecimal("0");
		Double  alltotalPrice = 0.0;
		BigDecimal totalWinmoney =new BigDecimal("0");
		
		for (BookieOrderModel model : totalModelList) {
			
			alltotalInvestment =add(alltotalInvestment, model.getTotalInvestment());
			alltotalPrice = add(alltotalPrice, model.getTotalPrice());
			
			totalWinmoney=add(totalWinmoney, model.getWinMoney());
		}
		
	
		total.setTotalInvestment(alltotalInvestment);
		total.setTotalPrice(alltotalPrice);
		total.setWinMoney(totalWinmoney);
		total.setPayoutRate(alltotalInvestment.doubleValue()==0.0?0:NumberUtil.getNumberAccordingToPercision((alltotalPrice-alltotalInvestment.doubleValue())/alltotalInvestment.doubleValue()*100, 3));
		
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
	public Map<String, Object> queryDetail(BookieOrderModel orderTicket) {
		VirBetOrderTicketDetail model=new VirBetOrderTicketDetail();
		model.setTicketInfo_id(orderTicket.getTicketInfo_id());
		List<VirBetOrderTicketDetail> listDetail=getAllDetail(model);
		List<VirBetOrderTicketDetail> list = new ArrayList<>();
		
		
		for (int i = 0; i < listDetail.size(); i++) {
			
			VirBetOrderTicketDetail orderTicketDetail=listDetail.get(i);
			VirBetOrderTicketMatches param=new VirBetOrderTicketMatches();
			param.setSid(orderTicketDetail.getSid());
			
			List<VirBetOrderTicketMatches> listMatch=getAllMatches(param);
			StringBuffer betline = new StringBuffer();
	
			String detail="";
			String l_code="";
			String canceled = "";
			for (Iterator<VirBetOrderTicketMatches> iterator2 = listMatch.iterator(); iterator2.hasNext();) {
				VirBetOrderTicketMatches matches = (VirBetOrderTicketMatches) iterator2.next();
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
					
					score_a = Integer.valueOf(scoreHome).toString();
					score_b = Integer.valueOf(scoreAway).toString();
				}
				
				}
				
				VirBetMatchInfo info=new VirBetMatchInfo();
				if("".equals(l_code)){
					l_code=matches.getLocal_m()+"";
				}else{
					l_code=l_code+","+matches.getLocal_m();
				}
				
				info.setMatch_code(matches.getL_code());
				info.setWeek_id(matches.getWeekno()+"");
				VirBetMatchInfo matchInfo=(VirBetMatchInfo) orderBookieProService.queryByCodeWeek(info);
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
		
		
			
			orderTicketDetail.setL_code(l_code);
			orderTicketDetail.setInvestInfo(detail);
			orderTicketDetail.setCanceled(canceled);
			if(orderTicketDetail.getStatus()==1){
				orderTicketDetail.setPayouttime("中奖");
			}else if(orderTicketDetail.getStatus()==2){
				orderTicketDetail.setPayouttime("未中奖");
			}else{
				orderTicketDetail.setPayouttime("Alive");
			}
			
			
		list.add(orderTicketDetail);
       }
		
		Map<String, Object> map = new HashMap<>();
		map.put("detailList", list);
		return map;
			
     }
	   
			
			
	

	
	 
	@Override
	public Response userManagementExcel(HttpServletResponse response, BookieOrderModel orderTicket) throws IOException {
		Response res =new Response();
		
		List<BookieOrderModel> list = new ArrayList<>();
			
			if(orderTicket.getStartDate()!=null &&  !"".equals(orderTicket.getStartDate())){
				orderTicket.setStartDate(orderTicket.getStartDate());
	
			}
			if(orderTicket.getEndDate()!=null &&  !"".equals(orderTicket.getEndDate())){
				orderTicket.setEndDate(orderTicket.getEndDate());
			}
			if(orderTicket.getAgentId()!=null && !"".equals(orderTicket.getAgentId())){
				VirBetAgentInfo agent = (VirBetAgentInfo) agentBookieProService.queryAgentInfoByOrderTicket(orderTicket.getAgentId());
				orderTicket.setAgentId(agent.getAgentNum());
			}
		
			List<BookieOrderModel> totalModelList = new ArrayList<>();
			totalModelList = getTotalInfo(orderTicket);
			List<BookieOrderModel> modelList = getAllList(totalModelList);
			BookieOrderModel total  =getTotal(modelList);
			
		
			Double totalInvest  = 0.0;
			
			for (BookieOrderModel model : modelList) {
				
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
		
				totalInvest +=model.getTotalInvestment().doubleValue();
				
			}
			
			//calculate(modelList);
			total.setTotalInvestment(new BigDecimal(totalInvest));
			Double pay = total.getWinMoney()==null?0:total.getWinMoney().doubleValue()-totalInvest;
			
			total.setPayoutRate(totalInvest==0.0?0:NumberUtil.getNumberAccordingToPercision(pay/totalInvest*100, 3));
			
			total.setStateMessage(total.getPayoutRate().toString());
			list.add(total);
			list.add(null);
			list.addAll(modelList);
		
			Resource resource = new ClassPathResource("/excel/bookie-order.xls");
			InputStream in = resource.getInputStream();
			
		
			String[] strKeyArray = new String[] {"tkId","uid","agentName","ballTypeMessage","bettingproducts","inplayMessage", "totalPrice" ,"addAwardAmount","investTime","totalInvestment","stateMessage","winMoney"};
					
			HSSFWorkbook workbook = PoiUtil.getListToExcelIn(in, 1, 1,list, strKeyArray, BookieOrderModel.class);
			PoiUtil.returnExcel(response, workbook, "order_manage");
			
			res.getResult().setResultCode(1);
			res.getResult().setResultMsg("success");
			
			
			return res;

	}
	
	//@Override
	public List<VirBetAgentInfo> queryAllAgentInfo() {
		return agentBookieProService.queryAllAgents();
	}
	
	


		
		//总的
		public List<BookieOrderModel> getTotalInfo(BookieOrderModel model){
			List<BookieOrderModel> allList = new ArrayList<BookieOrderModel>();
			List<BookieOrderModel> curList = orderBookieProService.queryTicketInfo(model);
			List<BookieOrderModel> hisList = orderBookieProHisService.queryTicketInfo(model);
			
			allList.addAll(curList);
			allList.addAll(hisList);
			return allList;
		}
		

		//分页
		public List<BookieOrderModel> getTicketInfo(BookieOrderModel model){
			List<BookieOrderModel> list = getTotalInfo(model);

			
			 Collections.sort(list,new Comparator<BookieOrderModel>(){  
		            public int compare(BookieOrderModel arg0, BookieOrderModel arg1) {  
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
			 List<BookieOrderModel> courseList=list.subList(firstIndex, toIndex);
			
			return courseList;
	
	}
		
		
	
	
		
	
		

		public List<VirBetOrderTicketDetail> getAllDetail(VirBetOrderTicketDetail detail){
			List<VirBetOrderTicketDetail> allList = new ArrayList<>();
			allList.addAll(orderBookieProService.querydetail(detail));
			allList.addAll(orderBookieProHisService.querydetail(detail));
			return allList;
		}
		
		public List<VirBetOrderTicketMatches> getAllMatches(VirBetOrderTicketMatches matches){
			List<VirBetOrderTicketMatches> allList = new ArrayList<>();
			allList.addAll(orderBookieProService.queryMatches(matches));
			allList.addAll(orderBookieProHisService.queryMatches(matches));
			return allList;
		}




}
