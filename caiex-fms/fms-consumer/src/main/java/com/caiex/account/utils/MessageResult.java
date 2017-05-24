package com.caiex.account.utils;

public class MessageResult {
	private  int status;
	private  String message;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public MessageResult(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public MessageResult() {
		super();
	}
	
	public static MessageResult ok(){
		return new MessageResult(200, "success");
	}
	
	public static MessageResult ok(int status,String message){
		return new MessageResult(status, message);
	}
	
	public static MessageResult fail(int status,String message){
		return new MessageResult(status, message);
	}
	
	public static MessageResult fail(){
		return new MessageResult(500, "failure");
	}
	
	public static MessageResult aok(){
		return new MessageResult(1001, "success");
	}
	
	
	public static MessageResult afail(){
		return new MessageResult(1002, "failure");
	}
	
	
	public static MessageResult rok(){
		return new MessageResult(2001, "拒票处理成功");
	}
	
	public static MessageResult rfail(){
		return new MessageResult(2002, "拒票处理失败");
	}
	
	
	
}
