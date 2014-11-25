package com.inter.trade.ui.func;

import java.io.Serializable;

import com.inter.trade.data.SunType;

public class FuncData implements SunType,Serializable{
	public int identify=0;
	public String name;
	public int imageId=-1;
	public String url;
	public int mnuid;
	public int orderId;//排序字段
	public String id;//标识符
}
