package com.inter.trade.ui.manager.core;

import android.os.Bundle;

/**
 * UI Handler 调度接口
 * @author zhichao.huang
 *
 */
public interface IMainHandlerProcess {
	
	/**
	 * 调度UI
	 * @param uiarg UI标签，用于控制用哪个fragement 
	 * @see com.inter.trade.ui.manager.core.UIConstantDefault
	 * @param isAddToBackStack 用于标识fragement是否添加到堆栈，0：不添加到堆栈；1：添加到堆栈
	 * @param data Bundle数据
	 */
	void handlerUI(int uiarg, int isAddToBackStack, Bundle data);

}
