package com.caiex.account.entity;

/*
 * OrderCancelModel
 * */
public class OrderCancel {

	private Integer agentid;//   合作方编号
	private String tkid;//   合作方给出的票号
	private Integer applyState;//   审批状态：0 待审批；1 已通过；2 已拒绝；
	private Long oid;//   主键
	private java.sql.Timestamp applyTime;//   申请提交的时间
	private String canceldesc;
	private Integer cancelType;
	
	
	
	public Integer getAgentid() {
		return agentid;
	}
	public void setAgentid(Integer agentid) {
		this.agentid = agentid;
	}
	public String getTkid() {
		return tkid;
	}
	public void setTkid(String tkid) {
		this.tkid = tkid;
	}
	public Integer getApplyState() {
		return applyState;
	}
	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
	}
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public java.sql.Timestamp getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(java.sql.Timestamp applyTime) {
		this.applyTime = applyTime;
	}
	public String getCanceldesc() {
		return canceldesc;
	}
	public void setCanceldesc(String canceldesc) {
		this.canceldesc = canceldesc;
	}
	public Integer getCancelType() {
		return cancelType;
	}
	public void setCancelType(Integer cancelType) {
		this.cancelType = cancelType;
	}
	
	
}
