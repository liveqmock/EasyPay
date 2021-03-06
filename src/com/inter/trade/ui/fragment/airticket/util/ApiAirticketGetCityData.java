package com.inter.trade.ui.fragment.airticket.util;

import com.inter.trade.data.BaseData;

/**
 * 飞机票，获取城市Data
 * @author zhichao.huang
 *
 */
public class ApiAirticketGetCityData extends BaseData{
	
	/**
	 * 城市Code
	 */
	public String cityCode;
	
	/**
	 * 城市ID
	 */
	public String cityId;
	
	/**
	 * 城市中文名称
	 */
	public String cityNameCh;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityNameCh() {
		return cityNameCh;
	}

	public void setCityNameCh(String cityNameCh) {
		this.cityNameCh = cityNameCh;
	}
	
	
	
	
}
