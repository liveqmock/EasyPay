/*
 * @Title:  EmployerData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月19日 下午1:42:29
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay.bean;

import java.io.Serializable;

/**
 * 员工信息
 * @author  chenguangchi
 * @data:  2014年9月19日 下午1:42:29
 * @version:  V1.0
 */
public class EmployerData implements Serializable{
	
	/**
	 * 唯一标识符
	 */
	public String id;
	
	/**
	 * 员工姓名
	 */
	public String name;
	/**
	 * 返回内容为“未注册”或者“已注册”
	 */
	public String hasRegister;
	/**
	 * 员工绑定银行卡号
	 */
	public String bankAccount;
	
	
	/**
	 * 工资月份  “2014-09”
	 */
	public String month;
	/**
	 * 员工手机号
	 */
	public String phone;
	/**
	 * 员工薪水数字
	 */
	public String money;
	/**
	 * 是否可以编辑
	 */
	public String canEdit;
	
	/**
	 * bossauthorid
	 */
	public String bossauthorid;
	
	/**
	 * 0：财务不能修改 1：财务能修改
	 */
	public String cwcanEdit;
	
	/**
	 * 
	 */
	public String wagelistid;
}
