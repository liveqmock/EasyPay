/*
 * @Title:  DefaultBankCardData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月8日 上午10:16:00
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.data;

import java.io.Serializable;

/**
 * 默认银行卡信息
 * @author  ChenGuangChi
 * @data:  2014年8月8日 上午10:16:00
 * @version:  V1.0
 */
public class DefaultBankCardData implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4448364162809594196L;
	/**
	 * 银行卡ID
	 */
	private String bkcardid;
	/**
	 * 银行卡号
	 */
	private String bkcardno;
	/**
	 * 所属银行id
	 */
	private String bkcardbankid;
	
	/**
	 * 所属银行代码
	 */
	private String bkcardbankcode;
	
	/**
	 * 所属银行名
	 */
	private String bkcardbank;
	/**
	 * 所属银行LOGO 本地
	 */
	private String Bkcardbanklogo;
	/**
	 * 所属银行LOGO 网络
	 */
	private String banklogo;
	/**
	 * 开户人
	 */
	private String bkcardbankman;
	/**
	 * 预留电话号码
	 */
	private String bkcardbankphone;
	/**
	 * 有效月
	 */
	private String bkcardyxmonth;
	/**
	 * 有效年
	 */
	private String bkcardyxyear;
	/**
	 * CVV校验
	 */
	private String bkcardcvv;
	/**
	 * 身份证
	 */
	private String bkcardidcard;
	/**
	 * 是否默认支付卡（旧接口，兼容并存）
	 */
	private String bkcardisdefault;
	/**
	 * 是否默认支付卡（新接口）
	 */
	private String bkcardfudefault;
	/**
	 * 是否默认收款卡（新接口）
	 */
	private String bkcardshoudefault;
	/**
	 * 银行卡类型
	 */
	private String bkcardcardtype;
	/**
	 * 通道名
	 */
	private String paychalname;
	
	/**
	 * 该字段只用于携程的CTT,如机票等
	 */
	public String ctripbankctt;
	
	
	public String getBkcardbankcode() {
		return bkcardbankcode;
	}
	public void setBkcardbankcode(String bkcardbankcode) {
		this.bkcardbankcode = bkcardbankcode;
	}
	public String getBkcardid() {
		return bkcardid;
	}
	public void setBkcardid(String bkcardid) {
		this.bkcardid = bkcardid;
	}
	public String getBkcardno() {
		return bkcardno;
	}
	public void setBkcardno(String bkcardno) {
		this.bkcardno = bkcardno;
	}
	public String getBkcardbankid() {
		return bkcardbankid;
	}
	public void setBkcardbankid(String bkcardbankid) {
		this.bkcardbankid = bkcardbankid;
	}
	public String getBkcardbank() {
		return bkcardbank;
	}
	public void setBkcardbank(String bkcardbank) {
		this.bkcardbank = bkcardbank;
	}
	
	/**
	 * 本地LOGO
	 * @return
	 */
	public String getBkcardbanklogo() {
		return Bkcardbanklogo;
	}
	public void setBkcardbanklogo(String bkcardbanklogo) {
		this.Bkcardbanklogo = bkcardbanklogo;
	}
	
	/**
	 * 网络返回LOGO
	 * @return
	 */
	public String getBanklogo() {
		return banklogo;
	}
	public void setBanklogo(String banklogo) {
		this.banklogo = banklogo;
	}
	
	public String getBkcardbankman() {
		return bkcardbankman;
	}
	public void setBkcardbankman(String bkcardbankman) {
		this.bkcardbankman = bkcardbankman;
	}
	public String getBkcardbankphone() {
		return bkcardbankphone;
	}
	public void setBkcardbankphone(String bkcardbankphone) {
		this.bkcardbankphone = bkcardbankphone;
	}
	public String getBkcardyxmonth() {
		return bkcardyxmonth;
	}
	public void setBkcardyxmonth(String bkcardyxmonth) {
		this.bkcardyxmonth = bkcardyxmonth;
	}
	public String getBkcardyxyear() {
		return bkcardyxyear;
	}
	public void setBkcardyxyear(String bkcardyxyear) {
		this.bkcardyxyear = bkcardyxyear;
	}
	public String getBkcardcvv() {
		return bkcardcvv;
	}
	public void setBkcardcvv(String bkcardcvv) {
		this.bkcardcvv = bkcardcvv;
	}
	public String getBkcardidcard() {
		return bkcardidcard;
	}
	public void setBkcardidcard(String bkcardidcard) {
		this.bkcardidcard = bkcardidcard;
	}
	public String getBkcardisdefault() {
		return bkcardisdefault;
	}
	public void setBkcardisdefault(String bkcardisdefault) {
		this.bkcardisdefault = bkcardisdefault;
	}
	public String getBkcardfudefault() {
		return bkcardfudefault;
	}
	public void setBkcardfudefault(String bkcardfudefault) {
		this.bkcardfudefault = bkcardfudefault;
	}
	public String getBkcardshoudefault() {
		return bkcardshoudefault;
	}
	public void setBkcardshoudefault(String bkcardshoudefault) {
		this.bkcardshoudefault = bkcardshoudefault;
	}
	public String getBkcardcardtype() {
		return bkcardcardtype;
	}
	public void setBkcardcardtype(String bkcardcardtype) {
		this.bkcardcardtype = bkcardcardtype;
	}
	public String getPaychalname() {
		return paychalname;
	}
	public void setPaychalname(String paychalname) {
		this.paychalname = paychalname;
	}
	public String getCtripbankctt() {
		return ctripbankctt;
	}
	public void setCtripbankctt(String ctripbankctt) {
		this.ctripbankctt = ctripbankctt;
	}
	
}
