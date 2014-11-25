/*
 * @Title:  CreditCard.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月22日 上午10:08:49
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.util;

/**
 * 有关信用卡支付的相关变量
 * @author  ChenGuangChi
 * @data:  2014年7月22日 上午10:08:49
 * @version:  V1.0
 */
public class CreditCard {
	
	/**
	 * 充值的业务的键值
	 */
	public static final String PAY_KEY="key";
	/**
	 * 银行卡卡号
	 */
	public static final String CARDNO="card_no";
	
	/**
	 * 要支付的钱
	 */
	public static final String PAYMONEY="pay_money";
	/**
	 * 刷卡器ID
	 */
	public static final String PAYCARDID="pay_card_id";
	
	
	/***
	 * 话费充值
	 */
	public static final int MOBILE=0;//
	/**
	 * 购买刷卡器
	 */
	public static final int BUYSWIPCRAD=1;//
	/**
	 * Q币充值
	 */
	public static final int QQ=2;//
	/**
	 * 转账汇款
	 */
	public static final int TRANSFER=3;//
	/**
	 * 游戏充值
	 */
	public static final int GAME=4;//
	/**
	 * 商户收款或抵用劵
	 */
	public static final int COUPON=5;//
	/**
	 * 还信用卡
	 */
	public static final int REPAY_CREDITCARD=6;
	/**
	 * 飞机票
	 */
	public static final int AIRTICKET=7;
	
	/**
	 * 发工资
	 */
	public static final int SALARY=8;
	
	/**
	 * 购买汇通卡
	 */
	public static final int HTB_CARD_BUY=9;
	
	/**
	 * 酒店预订
	 */
	public static final int HOTEL=10;
}
