/**
 * 
 */
package com.inter.trade.data;

/**
 * @author LiGuohui
 * @since 2013-1-1 下午9:58:49
 * @version 1.0.0
 */
public class PublicGroup implements SunType{
	public String getCridet() {
		return cridet;
	}
	public void setCridet(String cridet) {
		this.cridet = cridet;
	}
	public Group<? extends SunType> getGroup() {
		return group;
	}
	public void setGroup(Group<? extends SunType> group) {
		this.group = group;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private String result;
	private String size;
	private String message;
	private String cridet;
	private Group<? extends SunType> group;
	
	
}
