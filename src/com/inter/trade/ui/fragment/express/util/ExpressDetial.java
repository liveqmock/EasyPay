package com.inter.trade.ui.fragment.express.util;

import java.util.ArrayList;

import com.inter.trade.data.SunType;

public class ExpressDetial implements SunType {
	public ArrayList<InnerStatus> mInnerStatus;
	public String nu;
	public String com;
	public String updatetime;
	public String status;
	public String condition;
	public String data;
	public String state;
	public static class InnerStatus{
		public String time;
		public String context;
		public String ftime;
	}
}
