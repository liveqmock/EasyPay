package com.inter.trade.ui.fragment.order.util;

import java.util.ArrayList;

import com.inter.trade.data.BaseData;

public class OrderData extends BaseData{
	public ArrayList<Goods> mGoods;
	
	public String orderstate;
	public String orderid;
	public String orderno;
	public String ordertime;
	public String ordermoney;
	public String orderpronum;
	public String orderpaytype;
	public String shman;
	public String shcmpyname;
	public String shaddress;
	public String fhstorage;
	public String fhwltype;
	public String ordermemo;
	public String allpromoney;
	public String fhwlmoney;
	
	public static class Goods{
		public String proname;
		public String proprice;
		public String prounit;
		public String pronum;
		public String promoney;
	}
}
