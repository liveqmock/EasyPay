/*
 * @Title:  SalaryData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月10日 上午10:06:56
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay.bean;

import java.util.ArrayList;

/**
 * 月份工资列表
 * @author  chenguangchi
 * @data:  2014年9月10日 上午10:06:56
 * @version:  V1.0
 */
public class SalaryData {
	/**
	 * 发放人数
	 */
	public String wagestanum;
	/**
	 * 发放金额
	 */
	public String wageallmoney;
	/**
	 * 发放月份
	 */
	public String wagemonth;
	/**
	 * 已支付金额
	 */
	public String wagepaymoney;
	/**
	 * 员工工资列表
	 */
	public ArrayList<Stuff> stuffList;
	
	/**
	 * 列表中的動畫的狀態,0為無，1為關閉，2為打開
	 */
	public Integer anmiState=0;
}
