package com.inter.trade.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.util.Log;

import com.inter.trade.util.Constants;

/**
 * 本类为应用程序日志打印类 控制整个应用程序的打印等级
 * 
 * @author LiGuohui
 * 
 */
public class Logger {

	/**
	 * 打印一个等级为DEBUG日志消息
	 * 
	 * @param tag
	 * @param msg
	 */
	static public void d(String tag, String msg) {
		try {
			if (Constants.LOG_LEVEL == android.util.Log.DEBUG) {
				android.util.Log.d(tag, msg);
//				Logdog.d(msg);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	/**
	 * 打印一个等级为INFO日志消息
	 * 
	 * @param tag
	 * @param msg
	 */
	static public void i(String tag, String msg) {
		if (Constants.LOG_LEVEL == android.util.Log.DEBUG) {
			android.util.Log.i(tag, msg);
			Logdog.i(msg);
		}
	}

	/**
	 * 打印一个等级为 ERROR日志消息
	 * 
	 * @param tag
	 * @param msg
	 */
	static public void e(String tag, String msg) {
		if (Constants.LOG_LEVEL == android.util.Log.DEBUG) {
			android.util.Log.e(tag, msg);
			Logdog.e(msg);
		}
	}

	/**
	 * 打印一个等级为DEBUG日志消息
	 * 
	 * tag = KVLog
	 * @param msg
	 */
	static public void d(String log) {
		if (Constants.LOG_LEVEL == android.util.Log.DEBUG) {
			Log.d("SFLog", log);
//			Logdog.d(log);
		}
	}
	
	/**
	 * 打印一个等级为 ERROR日志消息
	 * 
	 * tag = KVLog
	 * @param msg
	 */
	static public void e(String log) {
		if (Constants.LOG_LEVEL == android.util.Log.DEBUG) {
			Log.e("SFLog", log);
			Logdog.e(log);
		}
	}

	/**
	 * 打印一个等级为INFO日志消息
	 * 
	 * tag = KVLog
	 * @param msg
	 */
	static public void i(String log) {
		if (Constants.LOG_LEVEL == android.util.Log.DEBUG) {
			Log.i("SFLog", log);
			Logdog.i(log);
		}
	}

	static public void e(Throwable e) {
		if(Constants.LOG_LEVEL != android.util.Log.DEBUG){
			return;
		}
		Logger.e("Error info:" + e.toString());
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(printWriter);
		Throwable cause = e.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		Logger.e("Cause Result:" + result);
	}
}
