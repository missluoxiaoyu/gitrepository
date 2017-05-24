package com.caiex.account.constants;

public class AccountSystemConstants {
	/*第三方
	 * TRADEING 交易中
	 * TRANSFER_COMPLETE 交易完成
	 * TRANSFER_COMPLETE_AWARD 交易完成且中奖
	 * TRANSFER_COMPLETE_NO_AWARD 交易完成未中奖
	 * AWARD_RECYCLE 中奖回收
	 * NO_TRADE 未交易
	 * */
	 public final static int TRADEING=1;
	 public final static int TRANSFER_COMPLETE=2;
	 public final static int TRANSFER_COMPLETE_AWARD=3;
	 public final static int TRANSFER_COMPLETE_NO_AWARD=4;
	 public final static int AWARD_RECYCLE=5;
	 public final static int NO_TRADE=6;
	 /* 数据库
	 *  STATE_AWARD 中奖
	 *  STATE_NO_AWARD 未中奖
	 *  STATE_ALIVE 未公布赛果
	 * 	NO_RECYCLE 中奖未回收
	 * 	RECYCLE 中奖已回收
	  * */
	 public final static int STATE_AWARD=1;
	 public final static int STATE_NO_AWARD=2;
	 public final static int STATE_ALIVE=3;
	 public final static int STATE_NO_RECYCLE=0;
	 public final static int STATE_RECYCLE=1;
	 
	 /*
	  * 
	  * 
	  * 1数据库有此票第三方无此票
	  * 2对方还在交易中
	  * 3对方显示转让完成已中奖，我们显示未中奖或alive
	  * 
	  * 5对方显示中奖已回收，我们显示未回收
	  * 6对方方案未交易，我们有票
	  * */
	
	 
	 public final static int OPPOSITE_NO_TICKET=1;
	 
	 public final static int OPPOSITE_TRADING_AND_OUR_HAVE_TICKET=2;
	 
	 public final static int OPPOSITE_AWARD_AND_OUR_NOAWARD_OR_ALIVE=3;
	
	 public final static int OPPOSITE_RECYCLE_AND_OUR_NO_RECYCLE=5;
	 
	 public final static int OPPOSITE_NO_TRADE_AND_OUR_HAVE_TICKET=6;
	 
	 
	 
	 /*渠道页面修改标志参数
	  * 1 修改预存款
	  * 2修改预存款报警
	  * 3修改渠道名称 url 密码
	  * */
	 
	 public final static int UPDATE_AGENT_PRESTORE = 1;
	 public final static int UPDATE_AGENT_PRESTORE_ALARM = 2;
	 public final static int UPDATE_AGENT_INF = 3;
	 
	
	 
}
