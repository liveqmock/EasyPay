package com.inter.trade.ui.fragment.airticket.util;

import java.io.Serializable;

import com.inter.trade.data.BaseData;

/**
 * 飞机票，航班查询/或某趟航班详情Data
 * @author zhichao.huang
 *
 */
public class ApiAirticketGetAirlineData extends BaseData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6669256789578988349L;
	
	/**
	 * 用于保存返程数据
	 */
	public ApiAirticketGetAirlineData airticketFanchengAirlineData = null;

	/**
	 * 起飞时间 时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
	 */
	public String takeOffTime;
	
	/**
	 * 到达时间
	 */
	public String arriveTime;
	
	/**
	 * 航班号
	 */
	public String flight;
	
	/**
	 * 航空公司代码
	 */
	public String airLineCode;
	
	/**
	 * 航空公司中文名称
	 */
	public String airLineName;
	
	/**
	 * 机型
	 */
	public String craftType;
	
	/**
	 * 机票实际价格
	 */
	public String price;
	
	/**
	 * 机票原始价格
	 */
	public String standardPrice;
	
	/**
	 * 燃油附加费
	 */
	public String oilFee;
	
	/**
	 * 成人税
	 */
	public String tax;
	
	/**
	 * 儿童标准价
	 */
	public String standardPriceForChild;
	
	/**
	 * 儿童燃油费用
	 */
	public String oilFeeForChild;
	
	/**
	 * 儿童税
	 */
	public String taxForChild;
	
	/**
	 * 婴儿标准价
	 */
	public String standardPriceForBaby;
	
	/**
	 * 婴儿燃油费用
	 */
	public String oilFeeForBaby;
	
	/**
	 * 婴儿税
	 */
	public String taxForBaby;
	
	/**
	 * 剩余票量
	 */
	public String quantity;
	
	/**
	 * 退改签政策说明
	 */
	public String rePolicy;//已删
	
	/**
	 * 更改政策说明
	 */
	public String rerNote;
	
	/**
	 * 改签政策说明
	 */
	public String endNote;
	
	/**
	 * 退票政策说明
	 */
	public String refNote;
	
	/**
	 * 出发机场
	 */
	public String dPortName;
	
	/**
	 * 到达机场
	 */
	public String aPortName;
	
	/**
	 * 出发机场三字码
	 */
	public String dPortCode;
	
	/**
	 * 到达机场三字码
	 */
	public String aPortCode;
	
	/**
	 * 舱位等级
	 */
	public String aClass;
	
	/**
	 * 出发城市代码(用于判断去程和回程的字段，如该字段和用户选择的出发城市是一致的说明是去程，反之是回程)
	 */
	public String dCityCode;
	
	/**
	 * 航班ID，用于生成订单
	 */
	public String id;
	
}
