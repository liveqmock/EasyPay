package com.inter.trade.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * 飞机票 创建订单的请求
 * @author zhichao.huang
 *
 */
public class AirTicketCreateOrderData extends BaseData{
	
	/**
	 * 飞机票信息 ticket
	 */
	public HashMap<String, String> ticketMap = new HashMap<String, String>();
	
	/**
	 * 飞机票列表信息 tickets
	 */
	public List<HashMap<String, String>> ticketListMap = new ArrayList<HashMap<String, String>>();
	
	/**
	 * 乘机人列表
	 */
	public List<HashMap<String, String>> passengerList = new ArrayList<HashMap<String, String>>();
	
	/**
	 * 联系人列表
	 */
	public List<HashMap<String, String>> contacterList = new ArrayList<HashMap<String, String>>();
	
	/**
	 * 支付信息
	 */
	public HashMap<String, String> payinfoMap = new HashMap<String, String>();
	

}
