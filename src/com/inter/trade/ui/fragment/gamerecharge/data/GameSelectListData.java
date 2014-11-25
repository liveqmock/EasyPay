package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;

public class GameSelectListData implements Serializable{
	/**游戏ID*/
	private String gameId;
	/***游戏名字*/
	private String gameName;
	/**价格*/
	private String price;
	/**成本*/
	private String cost;
	
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}
