package com.inter.trade.data;

import com.inter.protocol.body.NetParser;

//通用请求数据
public class TaskData extends BaseData{
	public NetParser mNetParser;
	public String apiName;
	public String funcName;
	public CommonData mCommonData;
}
