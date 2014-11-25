package com.inter.trade.ui.fragment.cridet.data;

import com.inter.trade.data.BaseData;
import com.inter.trade.data.SunType;
/**
 * 信用卡还款流水实体
 * @author apple
 *
 */
public class JournalData extends BaseData{
	//默认支付方式
	public static String DEFAULT_PAY="pay";//插卡器
	public static String DEFAULT_CURRENT="RMB";//插卡器
	//key
	public static String paytype="paytype";//插卡器
	public static String paymoney="paymoney";//插卡器
	public static String shoucardno="shoucardno";//插卡器
	public static String shoucardmobile="shoucardmobile";//插卡器
	public static String shoucardman="shoucardman";//插卡器
	public static String shoucardbank="shoucardbank";//插卡器
	public static String fucardno="fucardno";//插卡器
	public static String fucardbank="fucardbank";//插卡器
	public static String fucardmobile="fucardmobile";//插卡器
	public static String fucardman="fucardman";//插卡器
	public static String current="current";//插卡器
	public static String paycardid="paycardid";//插卡器
	
	public static String allmoney="allmoney";//刷卡金额
	public static String feemoney="feemoney";//手续费
	/**
	 * 付款银行卡ID
	 */
	public static String fucardbankid="fucardbankid";
}	
