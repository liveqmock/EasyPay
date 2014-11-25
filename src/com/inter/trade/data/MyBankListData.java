package com.inter.trade.data;

import java.io.Serializable;

/**
 * 我的银行卡Data
 * @author Lihaifeng
 *
 *
 */
public class MyBankListData extends BaseData implements Serializable{
	
	/**
	 * 充值金额
	 */
	public String bkcardid;
	
	/**
	 * 支付金额
	 */
	public String bkcardno;
	
	/**
	 * 充值号码
	 */
	public String bkcardbankid;
	
	/**
	 * 银行卡号
	 */
	public String bkcardbank;
	
	/**
	 * 所属银行LOGO 本地
	 */
	public String bkcardbanklogo;
	
	/**
	 * 所属银行LOGO 网络
	 */
	public String banklogo;
	
	/**
	 * 订单状态
	 */
	public String bkcardbankman;
	
	/**
	 * 订单状态
	 */
	public String bkcardbankphone;
	
	/**
	 * 订单状态
	 */
	public String bkcardyxmonth;
	
	/**
	 * 订单状态
	 */
	public String bkcardyxyear;
	
	/**
	 * 订单状态
	 */
	public String bkcardcvv;
	
	/**
	 * 订单状态
	 */
	public String bkcardidcard;
	
	/**
	 * 默认支付卡（旧接口，兼容并存）
	 */
	public String bkcardisdefault;
	
	/**
	 * 默认支付卡（新接口）
	 */
	public String bkcardfudefault;
	
	/**
	 * 默认收款卡（新接口）
	 */
	public String bkcardshoudefault;
	
	/**
	 * 订单状态
	 */
	public String bkcardcardtype;
	
}
