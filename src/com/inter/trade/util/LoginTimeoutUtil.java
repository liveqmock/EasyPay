package com.inter.trade.util;

/**
 * 处理登录超时
 * 
 * @author zhichao.huang
 *
 */
public class LoginTimeoutUtil {
	
	private static LoginTimeoutUtil loginTimeout = null;
	
	/**
	 * 超时状态
	 * 
	 * true: 超时;
	 * false: 不超时,默认
	 */
	private boolean isTimeout = false;
	
	private LoginTimeoutUtil () {
		
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static LoginTimeoutUtil get () {
		if(LoginTimeoutUtil.loginTimeout == null) {
			loginTimeout = new LoginTimeoutUtil();
		}
		return loginTimeout;
	}
	
	/**
	 * 清除超时状态
	 * 
	 */
	public void cleanTimeoutState () {
		isTimeout = false;
	}
	
	/**
	 * 启动超时状态
	 * 
	 */
	public void startTimeoutState () {
		isTimeout = true;
	}
	
	/**
	 * 判断超时状态
	 * 
	 * @return
	 */
	public boolean isTimeout () {
		return isTimeout;
	}
	
	/**
	 * 销毁实例
	 * 
	 */
	public void onDestroy () {
		if(loginTimeout != null) {
			loginTimeout.isTimeout = false;
			loginTimeout = null;
		}
	}

}
