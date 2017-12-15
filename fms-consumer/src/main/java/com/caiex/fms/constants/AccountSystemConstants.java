package com.caiex.fms.constants;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



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
	  * 2对方还在交易中 我们有票
	  * 4对方还在交易中 我们无票
	  * 3对方方案未交易，我们有票
	  * */
	
	 
	 public final static int OPPOSITE_NO_TICKET=1;
	 
	 public final static int OPPOSITE_TRADING_OUR_HAVETICKET=2;
	 public final static int OPPOSITE_TRADING_OUR_NOTICKET=4;
	 
	 public final static int OPPOSITE_NO_TRADE_AND_OUR_HAVE_TICKET=3;
	 
	 
	 
	 /*渠道页面修改标志参数
	  * 1 修改预存款
	  * 2修改预存款报警
	  * 3修改渠道名称 url 密码
	  * */
	 
	 public final static int UPDATE_AGENT_PRESTORE = 1;
	 public final static int UPDATE_AGENT_PRESTORE_ALARM = 2;
	 public final static int UPDATE_AGENT_INF = 3;
	 
	
	 /*用户管理
	  * 1足球2 篮球
	  * 1：交易成功；2：交易失败
	  * 0 死球 1 即场
	  * 
	  * */
	 public final static int BALL_TYPE_FB = 1;
	 public final static int BALL_TYPE_BK = 2;
	 public final static int DEAD_BALL =0;
	 public final static int THE_FIELD =1;
	 public final static int TRADE_SUCCESS=1;
	 public final static int TRADE_FAIL=2;
	 
	 
	 
	   
	   
	   public static enum SuperAdmin {
	        NO(0, "否"), YES(1, "是");

	        public int key;

	        public String value;

	        private SuperAdmin(int key, String value) {

	            this.key = key;
	            this.value = value;
	        }

	        public static SuperAdmin get(int key) {

	            SuperAdmin[] values = SuperAdmin.values();
	            for (SuperAdmin object : values) {
	                if (object.key == key) {
	                    return object;
	                }
	            }
	            return null;
	        }
	    }
	   
	   
	   
	 
}
