/**
 * 
 */
package com.inter.trade.net;

/**
 * @author LiGuohui
 * @since 2012-12-4 下午11:08:35
 * @version 1.0.0
 */
public class SunException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mExtra;

	public String getmExtra() {
		return mExtra;
	}
	
	 public SunException(String message) {
	        super(message);
	    }
	/**
	 * 
	 */
	public SunException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public SunException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 */
	public SunException(String detailMessage,String extra) {
		super(detailMessage);
		mExtra = extra;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public SunException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
	
}
