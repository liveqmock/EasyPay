package com.inter.trade.ui.fragment.hotel.data;

import java.io.Serializable;

import com.inter.trade.data.BaseData;

/**
 * 酒店详情 产生订单Data
 * @author haifengli
 *
 */
public class HotelOrderData extends BaseData implements Serializable{
	
	/**
	 * 酒店代码
	 */
	public String hotelCode;
	
	/**
	 * 房型代码
	 */
	public String roomCode;
	
	/**
	 * 房间价格代码
	 */
	public String priceCode;
	
	/**
	 * 入住日期
	 */
	public String startDate;
	
	/**
	 * 离开日期
	 */
	public String endDate;
	
	/**
	 * 房间数
	 */
	public String roomCount;
	
	/**
	 * 订房人手机号
	 */
	public String phone;
	
	/**
	 * 住房人姓名（每个房间只需登记一个人）
	 */
	public String[] names;
	
	
	
	/**
	 * 房间价格
	 */
	public String price;
	
	/**
	 * 入住天数
	 */
	public String dayCount;
	
	/**
	 * 支付总额
	 */
	public String payMoney;
	
//	/**
//	 * 发票信息
//	 */
//	public String[] fapiao;
	
	
}
