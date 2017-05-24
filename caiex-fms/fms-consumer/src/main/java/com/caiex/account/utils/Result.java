package com.caiex.account.utils;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


public class Result implements Serializable {

	protected int resultCode;
	protected String resultMsg;

	public int getResultCode() {
		return this.resultCode;
	}

	public void setResultCode(int value) {
		this.resultCode = value;
	}

	public String getResultMsg() {
		return this.resultMsg;
	}

	public void setResultMsg(String value) {
		this.resultMsg = value;
	}
}