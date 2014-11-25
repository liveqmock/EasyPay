package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;

public class GameListData implements Serializable{
	
	/**游戏ID*/
	private String gameId;
	/***游戏名字*/
	private String gameName;
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
