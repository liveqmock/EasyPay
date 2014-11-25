package com.inter.trade.ui.fragment.qmoney.util;

import com.inter.trade.data.BaseData;

/**
 * Q币充值面额Data
 * @author Lihaifeng
 *
 */
public class QMoneyDenominationData extends BaseData{
	
	/**
	 * 流水id
	 */
	public String rechaMoneyid;
	
	/**
	 * 充值金额
	 */
	public String rechamoney;
	
	/**
	 * 支付金额rechapaymoney
	 */
	public String rechapaymoney;
	
	/**
	 * 到帐描述
	 */
	public String rechamemo;
	
	/**
	 * 默认选择金额 0 非 1默认
	 */
	public String rechaisdefault;
	
	/**
	 * 默认金额优惠百分比
	 */
	public String rechapersent;
}
