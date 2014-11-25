/**
 * 
 */
package com.inter.trade.net;

/**
 * @author LiGuohui
 * @since 2012-12-4 下午11:15:01
 * @version 1.0.0
 */
public class ParserException extends SunException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param detailMessage
	 * @param extra
	 */
	public ParserException(String detailMessage, String extra) {
		super(detailMessage, extra);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	 public ParserException(String message) {
	        super(message);
	    }
	public ParserException() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
