package com.inter.trade.data;

import java.util.ArrayList;

public class UserInfo implements SunType{
	public String autruename;
	public String autrueidcard;
	public String auemail;
	public String aumobile;
	public ArrayList<PicData> mPicData;
	
	//代理商用户信息
	public String agentcompany;//代理商公司名
	public String agentarea;//归属地
	public String agentaddress;//地址
	public String agentmanphone;//联系电话
	public String agentfax;//传真
	public String agenthttime;//合同时间
	public String agentbzmoney;//保证金金额
}
