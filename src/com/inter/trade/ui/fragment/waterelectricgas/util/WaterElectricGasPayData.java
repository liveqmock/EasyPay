package com.inter.trade.ui.fragment.waterelectricgas.util;

import com.inter.trade.data.BaseData;

/**
 * 水电煤缴费支付，确认data
 * @author Lihaifeng
 *
 */
public class WaterElectricGasPayData extends BaseData{
	
	/**
	 * 硬件刷卡器ID
	 */
	public static String MRD_PAYCARDID = "paycardid";
	
	/**
	 * 订单编号
	 */
	public static String MRD_RECHAPAYTYPEID = "orderid";
	
	/**
	 * 账单金额
	 */
	public static String MRD_RECHAMONEY = "rechamoney";
	
	/**
	 * 实际支付金额
	 */
	public static String MRD_RECHAPAYMONEY = "rechapaymoney";
	
	/**
	 * 银行卡号
	 */
	public static String MRD_RECHABKCARDNO = "rechabkcardno";
	
	/**
	 * 银行卡ID (快捷支付银行卡选择ID)
	 */
	public static String MRD_RECHABKCARDID = "rechabkcardid";
	
	/**
	 * 银行卡信息保留域    格式：base64{信息1：***|信息2：***|… }
	 */
	public static String MRD_MERRESERVED = "merReserved";
	
}	
