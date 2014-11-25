/*
 * @Title:  PayInfo.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月10日 上午11:47:24
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay.bean;

/**
 * 工资支付的条件
 * @author  chenguangchi
 * @data:  2014年9月10日 上午11:47:24
 * @version:  V1.0
 */
public class PayInfo {
	/**
	 * 本次工资金额
	 */
	public String wageallmoney;
	/**
	 * 发放月份
	 */
	public String wagemonth;
	/**
	 * 本次工资金额+手续费
	 */
	public String wagepaymoney;
	/**
	 * 工资月份id
	 */
	public String wagelistid;
	
	/**
	 * 总工资（未含）-已支付工资（未手含续费）
	 */
	public String needpaymoney;
	/**
	 * 本次需支付手续费  支付的总手续费
	 */
	public String payfee;
	/**
	 * 手续费最低额
	 */
	public String minfee;
	/**
	 * 手续费最高额
	 */
	public String maxfee;
	/**
	 * 手续费%
	 */
	public String feeperent;
	@Override
	public String toString() {
		return "PayInfo [wageallmoney=" + wageallmoney + ", wagemonth="
				+ wagemonth + ", wagepaymoney=" + wagepaymoney
				+ ", wagelistid=" + wagelistid + ", needpaymoney="
				+ needpaymoney + ", payfee=" + payfee + ", minfee=" + minfee
				+ ", maxfee=" + maxfee + ", feeperent=" + feeperent + "]";
	}
	
}
