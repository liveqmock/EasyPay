/**
 * 
 */
package com.inter.trade.data;

/**
 * @author LiGuohui
 * @since 2013-1-2 下午10:30:45
 * @version 1.0.0
 */
public class CouponData implements SunType{
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	private String title;
	private String datetime;
	private String content;
	private String image;
	
	
	
}
