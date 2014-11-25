package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;

public class CompanyListData implements Serializable{
	
	/**平台ID*/
	private String companyId;
	/***平台名字*/
	private String companyName;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
