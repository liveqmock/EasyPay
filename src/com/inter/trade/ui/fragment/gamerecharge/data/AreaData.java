/*
 * @Title:  AreaData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午1:16:47
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TODO<请描述这个类是干什么的>
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午1:16:47
 * @version:  V1.0
 */
public class AreaData implements Serializable{
	
	/****
	 * 区名
	 */
	private String name;
	
	/***
	 * 服务器列表
	 */
	private ArrayList<String> serverList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getServerList() {
		return serverList;
	}
	public void setServerList(ArrayList<String> serverList) {
		this.serverList = serverList;
	}
	
	
}
