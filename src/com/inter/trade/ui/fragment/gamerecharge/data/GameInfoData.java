/*
 * @Title:  GameInfoData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月20日 上午9:55:00
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TODO<请描述这个类是干什么的>
 * @author  ChenGuangChi
 * @data:  2014年6月20日 上午9:55:00
 * @version:  V1.0
 */
public class GameInfoData implements Serializable{
	
	/**
	 * 游戏名
	 */
	private String gameName;
	/**
	 * 区列表
	 */
	private ArrayList<AreaData> areaList;
	
	public ArrayList<AreaData> getAreaList() {
		return areaList;
	}
	public void setAreaList(ArrayList<AreaData> areaList) {
		this.areaList = areaList;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	
	
}
