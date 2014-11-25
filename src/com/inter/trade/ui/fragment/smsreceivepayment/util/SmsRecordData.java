package com.inter.trade.ui.fragment.smsreceivepayment.util;

import com.inter.trade.data.BaseData;

/**
 * 短信收款历史记录Data
 * @author Lihaifeng
 *
 */
public class SmsRecordData extends BaseData{
	
	/**
	 * 手机号码
	 */
	public String phone;
	
	/**
	 *交易金额
	 */
	public String money;
	
	/**
	 * 订单时间
	 */
	public String date;
	
	/**
	 * 订单状态
	 */
	public String status;
	
}
