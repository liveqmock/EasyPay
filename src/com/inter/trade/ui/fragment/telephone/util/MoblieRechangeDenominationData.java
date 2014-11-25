package com.inter.trade.ui.fragment.telephone.util;

import com.inter.trade.data.BaseData;

/**
 * 话费充值面额Data
 * @author zhichao.huang
 *
 */
public class MoblieRechangeDenominationData extends BaseData{
	
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
}
