package com.inter.trade.ui.manager.core;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * IMainHandler
 * @author zhichao.huang
 *
 */
public class IMainHandler extends Handler {
	
	private static final String TAG = IMainHandler.class.getName();
	
	private IMainHandlerProcess process;
	
	/**
	 * 调度UI
	 */
	public static final int HANDLE_UI = 1;

	private IMainHandler() {
		super();
	}

	public IMainHandler(IMainHandlerProcess process) {
		this();
		this.process = process;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case HANDLE_UI:
			process.handlerUI(msg.arg1, msg.arg2, msg.getData());
			break;
		default:
			Log.e(TAG,"unkown message what " + msg.what);
			break;
		}
	}
	

}
