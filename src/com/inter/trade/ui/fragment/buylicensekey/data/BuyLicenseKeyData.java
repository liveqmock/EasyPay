package com.inter.trade.ui.fragment.buylicensekey.data;

import java.io.Serializable;

import com.inter.trade.data.BaseData;

/**
 * 购买授权码 Data
 * @author haifengli
 *
 */
public class BuyLicenseKeyData extends BaseData implements Serializable{
	
	/**
	 * 授权码
	 */
	public String licenseKey;
	
	/**
	 * 设备IMEI
	 */
	public String paycardIMEI;
	
	/**
	 * 设备型号
	 */
	public String paycardmachineno;
	

	public String getLicenseKey() {
		return licenseKey;
	}

	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}
	
	public String getPaycardIMEI() {
		return paycardIMEI;
	}
	
	public void setPaycardIMEI(String paycardIMEI) {
		this.paycardIMEI = paycardIMEI;
	}
	
	public String getPaycardmachineno() {
		return paycardmachineno;
	}
	
	public void setPaycardmachineno(String paycardmachineno) {
		this.paycardmachineno = paycardmachineno;
	}

}
