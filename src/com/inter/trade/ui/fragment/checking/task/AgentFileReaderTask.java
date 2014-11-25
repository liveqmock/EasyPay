/*
 * @Title:  AgentFileReaderTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年10月21日 上午9:25:33
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.checking.task;

import java.io.File;

import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.FileUtils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * 读取代理商配置文件
 * 
 * @author chenguangchi
 * @data: 2014年10月21日 上午9:25:33
 * @version: V1.0
 */
public class AgentFileReaderTask extends AsyncTask<String, String, String> {
	
	/**
	 * TAG
	 */
	private static final String TAG = "cn.tfbpay";

	private ResponseStateListener listener;
	
	private String fileName="tfbpay_agentid.txt";
	
	private String dir=File.separator+"tfbpay"+File.separator;
	
	public AgentFileReaderTask(ResponseStateListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String file="";
		if(FileUtils.ExistSDCard()){//Sd卡存在
			file = Environment.getExternalStorageDirectory().getAbsolutePath()+dir+fileName;
		}else{//Sd卡不存在
			file = Environment.getRootDirectory().getAbsolutePath()+dir+fileName;
		}
		
		StringBuilder agentid = FileUtils.readFile(file, "UTF-8");
		Log.i(TAG, file);
		
		if(agentid!=null){
			return agentid.toString();
		}
		return "020001";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i(TAG, "读取到的代理商号:"+result);
		if(listener!=null){
			listener.onSuccess(result, String.class);
		}
	}
}
