/*
 * @Title:  AdData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月28日 下午1:58:53
 * @version:  V1.0
 */
package com.inter.trade.ui.func.data;

import java.util.ArrayList;

/**
 * 首頁廣告信息
 * @author  ChenGuangChi
 * @data:  2014年7月28日 下午1:58:53
 * @version:  V1.0
 */
public class AdData {
		
	private String adallcount;//图片的个数
	
	private ArrayList<Ad> adList;//广告列表

	public String getAdallcount() {
		return adallcount;
	}

	public void setAdallcount(String adallcount) {
		this.adallcount = adallcount;
	}

	public ArrayList<Ad> getAdList() {
		return adList;
	}

	public void setAdList(ArrayList<Ad> adList) {
		this.adList = adList;
	}
}
