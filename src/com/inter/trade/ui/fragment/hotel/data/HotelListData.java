package com.inter.trade.ui.fragment.hotel.data;

import java.io.Serializable;

import com.inter.trade.data.BaseData;

/**
 * 酒店预订 酒店列表Data
 * @author haifengli
 *
 */
public class HotelListData extends BaseData implements Serializable{
	
	/**
	 * 酒店ID
	 */
	public String hotelId;
	
	/**
	 * 酒店代码
	 */
	public String hotelCode;
	
	/**
	 * 酒店图片url地址
	 */
	public String imageUrl;
	
	/**
	 * 酒店简介
	 */
	public String description;
	
/*****************以上成员没有方法*************************/	
	
	/**
	 * 酒店名称
	 */
	public String hotelName;
	
	/**
	 * 酒店价格 最低价
	 */
	public String minPrice;
	
	/**
	 * 酒店星级
	 */
	public String starRate;
	
	/**
	 * 酒店评分
	 */
	public String ctripRate;
	
	/**
	 * 酒店地址
	 */
	public String address;
	
	/**
	 * 酒店WIFI
	 */
	public String hotelWifi;
	
	/**
	 * 酒店停车位
	 */
	public String hotelPark;

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getHotelStar() {
		return starRate;
	}

	public void setHotelStar(String hotelStar) {
		this.starRate = hotelStar;
	}
	
	public String getHotelScore() {
		return ctripRate;
	}
	
	public void setHotelScore(String hotelScore) {
		this.ctripRate = hotelScore;
	}
	
	public String getHotelAddress() {
		return address;
	}
	
	public void setHotelAddress(String hotelAddress) {
		this.address = hotelAddress;
	}
	
	public String getHotelWifi() {
		return hotelWifi;
	}
	
	public void setHotelWifi(String hotelWifi) {
		this.hotelWifi = hotelWifi;
	}
	
	public String getHotelPark() {
		return hotelPark;
	}
	
	public void setHotelPark(String hotelPark) {
		this.hotelPark = hotelPark;
	}
	
}
