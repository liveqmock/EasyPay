/*
 * @Title:  CreditCardData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月22日 下午5:30:59
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.data;

/**
 * 信用卡信息
 * @author  ChenGuangChi
 * @data:  2014年7月22日 下午5:30:59
 * @version:  V1.0
 */
public class CreditCardData {
	
	/**
	 * 支付金额
	 */
	private String paymoney;
	/**
	 * 银行卡号
	 */
	private String bankno;
	/**
	 * cvv2码
	 */
	private String cvv2;
	/**
	 * 预留的电话号码
	 */
	private String phone;
	/**
	 * 所属银行的代号
	 */
	private String bank;
	/**
	 * 证件的类型
	 */
	private String idtype;
	/**
	 * 证件号
	 */
	private String id;
	
	/**
	 * 有效期的月
	 */
	private String month;
	
	/**
	 * 有效期的年
	 */
	private String year;
	
	/**
	 * 持卡人姓名
	 */
	private String username;
	
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/***
	 * 银行所属ID
	 */
	private String bankId;
	
	/**
	 * CTT 该字段只用于携程,如机票等
	 */
	private String ctripbankctt;
	
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPaymoney() {
		return paymoney;
	}
	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getCvv2() {
		return cvv2;
	}
	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCtripbankctt() {
		return ctripbankctt;
	}
	public void setCtripbankctt(String ctripbankctt) {
		this.ctripbankctt = ctripbankctt;
	}
	
}
