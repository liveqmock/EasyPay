/*
 * @Title:  SmscodeData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 下午2:44:26
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.data;

/**
 * 短信
 * @author  ChenGuangChi
 * @data:  2014年7月23日 下午2:44:26
 * @version:  V1.0
 */
public class SmsCode {
	
	/**
	 * 是否需要发短信
	 */
	private boolean need=false;
	/**
	 * 验证码
	 */
	private String code;
	/**
	 * 订单ID
	 */
	private String orderId;
	
	/**
	 * 订单总金额
	 */
	private String money;
	
	/**
	 * 提示信息
	 */
	private String message;
	
	/**订单编号*/
	private String bkordernumber;
	
	/**易宝订单号*/
	private String bkntno;
	
	/**认证码*/
	private String verifytoken;
	
	
	public String getBkordernumber() {
		return bkordernumber;
	}
	public void setBkordernumber(String bkordernumber) {
		this.bkordernumber = bkordernumber;
	}
	public String getBkntno() {
		return bkntno;
	}
	public void setBkntno(String bkntno) {
		this.bkntno = bkntno;
	}
	public String getVerifytoken() {
		return verifytoken;
	}
	public void setVerifytoken(String verifytoken) {
		this.verifytoken = verifytoken;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isNeed() {
		return need;
	}
	public void setNeed(boolean need) {
		this.need = need;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
}
