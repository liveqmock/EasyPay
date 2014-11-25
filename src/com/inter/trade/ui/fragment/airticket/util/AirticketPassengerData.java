package com.inter.trade.ui.fragment.airticket.util;

import com.inter.trade.data.BaseData;

/**
 * 飞机票，乘客Data
 * @author zhichao.huang
 *
 */
public class AirticketPassengerData extends BaseData{
	
	/**
	 * 乘机人类型，ADU（成人），CHI（儿童）， BAB（婴儿）
	 */
	public String passengerId;
	
	/**
	 * 乘机人姓名
	 */
	public String name;
	
	/**
	 * 乘机人出生日期，形如：1984-01-01
	 */
	public String birthDay;
	
	/**
	 * 证件类型ID;
	 * 1身份证，2护照，4军人证，7回乡证，8台胞证，
	 * 10港澳通行证，11国际海员证，20外国人永久居留证，
	 * 25户口簿，27出生证明，99其它
	 */
	public String passportTypeId;
	
	/**
	 * 证件号码
	 */
	public String passportNo;

	/**
	 * 乘机人性别
	 */
	public String gender;
	
	/**
	 * 乘机人联系电话
	 */
	public String telephone;
	
}
