package com.inter.trade.ui.manager.core;

import android.os.Bundle;
import android.os.Message;

/**
 * 单例 业务调度管理器
 * @author zhichao.huang
 *
 */
public class IMainHandlerManager {
	
	private static IMainHandlerManager instance;
	
	private IMainHandler handler;
	
	/**
	 * 主程序注册主消息调度器
	 * @param handler
	 */
	public static void regist(IMainHandler handler){
		instance = new IMainHandlerManager(handler);
	}
	
	private IMainHandlerManager(IMainHandler handler){
		this.handler = handler;
	}
	
	
	static IMainHandlerManager get(){
		if(instance == null){
			throw new IllegalArgumentException("Not init");
		}
		return instance;
	}
	
	/**
	 * UI调度
	 * @param uiarg UI标签，用于控制用哪个fragement 
	 * @see com.inter.trade.ui.manager.core.UIConstantDefault
	 * @param isAddToBackStack 用于标识fragement是否添加到堆栈，0：不添加到堆栈；1：添加到堆栈
	 * @param data Bundle数据
	 */
	public static void handlerUI(int uiarg, int isAddToBackStack, Bundle data) {
		Message msg = get().handler.obtainMessage(IMainHandler.HANDLE_UI, uiarg, isAddToBackStack);
		msg.setData(data);
		msg.sendToTarget();
	}
	
	public static void onDestroy(){
		if(instance != null){
			instance.handler.removeCallbacksAndMessages(null);
			instance.handler = null;
			instance = null;
		}
	}

}
