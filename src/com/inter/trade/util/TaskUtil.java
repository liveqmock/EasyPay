package com.inter.trade.util;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;

/**
 * 异步任务的工具类 
 * @author  chenguangchi
 * @data:  2014年8月22日 下午2:00:32 
 * @version:  V1.0
 */
public class TaskUtil {
	
	
	/***
	 * 判断当前的状态是否正在执行
	 * @return
	 * @throw
	 * @return boolean
	 */
	public static boolean isTaskExcuting(AsyncTask task){
		if(task!=null && (Status.PENDING.equals(task.getStatus())||Status.RUNNING.equals(task.getStatus()))){
			return true;
		}
		return false;
	}
}
