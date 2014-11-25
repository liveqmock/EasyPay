package com.inter.trade.ui.fragment.airticket.util;

import com.inter.trade.data.BaseData;

/**
 * 飞机票，机票历史记录Data
 * @author zhichao.huang
 *
 */
public class ApiAirticketGetOrderHistoryData extends BaseData{
	
	/**
	 * 出发城市
	 */
	public String departCity;
	
	/**
	 * 到达城市
	 */
	public String arriveCity;
	
	/**
	 * 下订单时间
	 */
	public String createOrderTime;
	
	/**
	 * 起飞时间
	 */
	public String takeOffTime;
	
	/**
	 * 航班号
	 */
	public String flight;
	
	/**
	 * 机型
	 */
	public String craftType;
	
	/**
	 * 实际总价格，订单中所有人所有费用的总和
	 */
	public String totalPrice;
	
	/**
	 * 订单状态 “订单完成”或者“订单未支付”两种之一
	 */
	public String status;
	
	/**
	 * W-未处理，P-处理中，S-已成交，C-已取消，R-全部退票，T-部分退票，U-未提交
	 */
	public String orderProcess;

}
