/*
 * @Title:  LoginInfo.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年10月25日 上午11:52:13
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.checking.bean;

/**
 * 登陆的相关信息
 * @author  chenguangchi
 * @data:  2014年10月25日 上午11:52:13
 * @version:  V1.0
 */
public class LoginInfo {
	/**
	 * 用户的id
	 */
	public String authorid;
	/**
	 * 用户的id
	 */
	public String agentid;
	/**
	 * 用户是否绑定过代理商，relateAgent，“0”否，“1”是
	 */
	public String relateAgent;
	/**
	 * 代理商类型，agenttypeid，“0”普通用户，“1”正式代理商， “2”虚拟代理商
	 */
	public String agenttypeid;
	/**
	 * 是否设置支付密码
	 */
	public String ispaypwd;
	/**
	 * 是否设置手势密码
	 */
	public String gesturepasswd;
}
