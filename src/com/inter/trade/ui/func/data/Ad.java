/*
 * @Title:  Ad.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月28日 下午2:04:43
 * @version:  V1.0
 */
package com.inter.trade.ui.func.data;

/**
 * 每一个广告的详细信息
 * @author  ChenGuangChi
 * @data:  2014年7月28日 下午2:04:43
 * @version:  V1.0
 */
public class Ad {
	
	/**
	 * 排序
	 */
	private String adno;
	/**
	 * 图片的URL
	 */
	private String adpicurl;
	/**
	 * 广告的标题
	 */
	private String adtitle;
	/**
	 * 广告的链接
	 */
	private String adlinkurl;
	
	public String getAdno() {
		return adno;
	}
	public void setAdno(String adno) {
		this.adno = adno;
	}
	public String getAdpicurl() {
		return adpicurl;
	}
	public void setAdpicurl(String adpicurl) {
		this.adpicurl = adpicurl;
	}
	public String getAdtitle() {
		return adtitle;
	}
	public void setAdtitle(String adtitle) {
		this.adtitle = adtitle;
	}
	public String getAdlinkurl() {
		return adlinkurl;
	}
	public void setAdlinkurl(String adlinkurl) {
		this.adlinkurl = adlinkurl;
	}
	
}
