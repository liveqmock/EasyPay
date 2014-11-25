package com.inter.trade.ui.fragment.hotel.data;

import java.io.Serializable;

import com.inter.trade.data.BaseData;

/**
 * 酒店详情 房型Data
 * @author haifengli
 *
 */
public class HotelRoomData extends BaseData implements Serializable{
	
	/**
	 * 酒店代码
	 */
	public String hotelCode;
	
	/**
	 * 房型代码
	 */
	public String code;
	
	/**
	 * 房型名称
	 */
	public String name;
	
	/**
	 * 标准入住人数
	 */
	public String resident;
	
	/**
	 * 床的尺寸
	 */
	public String bedSize;
	
	/**
	 * 房间价格
	 */
	public String price;
	
	/**
	 * 房间价格代码
	 */
	public String priceCode;
	
	/**
	 * 房间图片地址url,可能有，可能无，可能多张图片
	 */
	public String[] imageUrls;
	
}
