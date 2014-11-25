/*
 * @Title:  GetBillData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月23日 上午10:34:55
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;

/**
 * 生成订单的请求数据
 * @author  ChenGuangChi
 * @data:  2014年6月23日 上午10:34:55
 * @version:  V1.0
 */
public class GetBillData implements Serializable{
	private String gameId;
	private String gameName;
	private String area;
	private String server;
	private String quantity;
	private String price;
	private String userCount;
	private String paycardid;
	private String rechabkcardno;
	private String cost;
	
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUserCount() {
		return userCount;
	}
	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}
	public String getPaycardid() {
		return paycardid;
	}
	public void setPaycardid(String paycardid) {
		this.paycardid = paycardid;
	}
	public String getRechabkcardno() {
		return rechabkcardno;
	}
	public void setRechabkcardno(String rechabkcardno) {
		this.rechabkcardno = rechabkcardno;
	}
	
	
}	
