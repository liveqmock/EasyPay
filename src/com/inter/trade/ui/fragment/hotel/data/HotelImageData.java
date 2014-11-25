package com.inter.trade.ui.fragment.hotel.data;

import java.io.Serializable;

import com.inter.trade.data.BaseData;

/**
 * 酒店详情  照片Data
 * @author haifengli
 *
 */
public class HotelImageData extends BaseData implements Serializable{
	
	/**
	 * 照片地址URL
	 */
	public String url;
	
	/**
	 * 照片说明
	 */
	public String caption;
	
}
