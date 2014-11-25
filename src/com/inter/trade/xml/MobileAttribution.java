package com.inter.trade.xml;

/**
 * 手机归属地模型
 * @see http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=18102786610
 * @author Administrator
 *
 */
public class MobileAttribution {
	
	/**
	 * 手机号码
	 */
	public String chgmobile;
	
	/**
	 * 城市
	 */
	public String city;
	
	/**
	 * 省份
	 */
	public String province;
	
	/**
	 * 返回码
	 */
	public String retcode;
	
	/**
	 * 返回信息
	 */
	public String retmsg;
	
	/**
	 * 运营商
	 */
	public String supplier;

	public String getChgmobile() {
		return chgmobile;
	}

	public void setChgmobile(String chgmobile) {
		this.chgmobile = chgmobile;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
}
