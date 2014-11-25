package com.inter.trade.ui.fragment.agent.util;

import com.inter.trade.data.BaseData;

/**
 * 代理商补货支付确认data
 * @author Lihaifeng
 *
 */
public class AgentPayData extends BaseData{
	
	/**
	 * 硬件刷卡器ID
	 */
	public static String MRD_PAYCARDID = "paycardid";
	
	/**
	 * 支付类型id
	 */
	public static String MRD_RECHAPAYTYPEID = "rechapaytypeid";
	
	/**
	 * 充值金额
	 */
	public static String MRD_RECHAMONEY = "rechamoney";
	
	/**
	 * 实际支付金额
	 */
	public static String MRD_RECHAPAYMONEY = "rechapaymoney";
	
	/**
	 * 充值QQ号码
	 */
	public static String MRD_RECHAMOBILE = "rechaqq";
	
	/**
	 * 银行卡号
	 */
	public static String MRD_RECHABKCARDNO = "orderfucardno";
	
	/**
	 * 银行卡ID (快捷支付银行卡选择ID)
	 */
	public static String MRD_RECHABKCARDID = "rechabkcardid";
	
	/**
	 * 银行卡信息保留域    格式：base64{信息1：***|信息2：***|… }
	 */
	public static String MRD_MERRESERVED = "merReserved";
	
}	
