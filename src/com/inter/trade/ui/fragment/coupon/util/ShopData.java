package com.inter.trade.ui.fragment.coupon.util;

import java.util.ArrayList;

import com.inter.trade.data.BaseData;

public class ShopData extends BaseData{
	
	public String shopname;
	public String isshop;
	public ArrayList<Coupon> mCoupons = new ArrayList<ShopData.Coupon>();
	
	public static class Coupon{
		public String couponid;
		public String couponmoney;
		public String couponlimitnum;
	}
}
