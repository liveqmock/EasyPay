package com.inter.trade.ui.fragment.hotel.data;

import com.inter.trade.data.BaseData;

/**
 * 酒店预订 关键字Data
 * @author haifengli
 *
 */
public class HotelKeywordData extends BaseData{
	
	/**
	 * 行政区ID
	 */
	public String districtId;
	
	/**
	 * 行政区名
	 */
	public String districtName;
	
	/**
	 * 商业区ID
	 */
	public String zoneId;
	
	/**
	 * 商业区名
	 */
	public String zoneName;
	
	/**
	 * 品牌ID
	 */
	public String brandId;
	
	/**
	 * 品牌名
	 */
	public String brandName;
	
	/**
	 * 主题ID
	 */
	public String themeId;
	
	/**
	 * 主题名
	 */
	public String themeName;

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	
	public String getZoneName() {
		return zoneName;
	}
	
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	
	public String getBrandId() {
		return brandId;
	}
	
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getThemeId() {
		return themeId;
	}
	
	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}
	
	public String getThemeName() {
		return themeName;
	}
	
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	
	/*****************************************************/
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
