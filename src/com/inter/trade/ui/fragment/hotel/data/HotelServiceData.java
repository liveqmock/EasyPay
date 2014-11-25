package com.inter.trade.ui.fragment.hotel.data;

import java.io.Serializable;

import com.inter.trade.data.BaseData;

/**
 * 酒店设施 客房设施Data
 * @author haifengli
 *
 */
public class HotelServiceData extends BaseData implements Serializable{
	
	/**
	 * 酒店设施
	 */
	public String service;
	
	/**
	 * 客房设施
	 */
	public String amenity;
	
}
