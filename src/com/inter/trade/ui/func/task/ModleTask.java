/*
 * @Title:  ModleParser.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月4日 下午5:17:37
 * @version:  V1.0
 */
package com.inter.trade.ui.func.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.data.IconData;
import com.inter.trade.ui.func.data.ModleData;
import com.inter.trade.ui.func.parser.ModelParser;
import com.inter.trade.ui.func.util.ModelUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.SerializeUtils;

/**
 * 主页功能获取接口
 * 
 * @author ChenGuangChi
 * @data: 2014年7月4日 下午5:17:37
 * @version: V1.0
 */
public class ModleTask extends AsyncTask<String, Void, Void> {
	private Context context;
	private ResponseStateListener listener;
	ProtocolRsp mRsp;
	private ArrayList<ArrayList<FuncData>> funList=null;
	private String path;
	private boolean isshow;
	
	private String[] params;

	public ModleTask(Context context, ResponseStateListener listener,boolean isshow) {
		super();
		this.context = context;
		this.listener = listener;
		path = context.getFilesDir()+File.separator+Constants.OBJECTFILE;
		this.isshow=isshow;
		
	}

	@Override
	protected Void doInBackground(String... params) {
		this.params=params;
		try {
		
//		File file=new File(path);
//		System.out.println("文件的路径:" + path);
//		if(!file.exists()){
//			file.createNewFile();
//		}else{
//			//从本地获取序列化对象
//			funList =(ArrayList<ArrayList<FuncData>>) SerializeUtils.deserialization(path);
//		}
//		if(funList!=null){//如果本地有这个对象
//			return null;
//		}else{//如果没有就从网络获取
				CommonData mData = new CommonData();
				mData.putValue("paycardkey", params[0]);
				mData.putValue("appversion", params[1]);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiAppInfo", "readMenuModule", mData);
				ModelParser myParser = new ModelParser();
				mRsp = HttpUtil.doRequest(myParser, mDatas);
				
			  //}

			} catch (Exception e) {
				e.printStackTrace();
				mRsp = null;
			}
		
		
		
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(isshow){
			PromptUtil.showDialog(context, "正在初始化...");
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(isshow){
			PromptUtil.dissmiss();
		}
		if(mRsp == null) {
			//PromptUtil.showToast(context, context.getString(R.string.net_error));
			showAgainLoadDialog();
			return;
		}
			List<ProtocolData> mList = mRsp.mActions;
			ModleData data = parserResponse(mList);
			/*if ("1".equals(data.getIsnew())) {// 新功能更新

				funList = ModelUtil
						.getFunctions(data);// 进行逻辑排序等操作
				
				//SerializeUtils.serialization(path, funList);//将对象保存到本地
				
				if (listener != null) {
					listener.onSuccess(funList, ModleData.class);
				}
			}*/
			if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
					(Activity) context)) {
			}
			
			if (listener != null) {
				listener.onSuccess(data, ModleData.class);
			}
		
		
	}

	private ModleData parserResponse(List<ProtocolData> mDatas) {
		ModleData modle = new ModleData();
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).getmValue();
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				List<ProtocolData> version = data.find("/version");
				if (version != null) {
					modle.setVersion(version.get(0).getmValue());
				}
				List<ProtocolData> isnew = data.find("/isnew");
				if (isnew != null) {
					modle.setIsnew(isnew.get(0).getmValue());
				}

				List<ProtocolData> aupic = data.find("/msgchild");
				ArrayList<IconData> iconList = new ArrayList<IconData>();
				if (aupic != null)
					for (ProtocolData child : aupic) {
						if (child.mChildren != null
								&& child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							IconData icon = new IconData();
							for (String key : keys) {
								List<ProtocolData> rs = child.mChildren
										.get(key);
								for (ProtocolData item : rs) {
									if (item.mKey.equals("mnuname")) {
										icon.setMnuname(item.mValue);
									} else if (item.mKey.equals("mnupic")) {
										icon.setMnupic(item.mValue);
									} else if (item.mKey.equals("mnuorder")) {
										icon.setMnuorder(item.mValue);
									} else if (item.mKey.equals("mnuurl")) {
										icon.setMnuurl(item.mValue);
									} else if (item.mKey.equals("mnuversion")) {
										icon.setMnuversion(item.mValue);
									} else if (item.mKey.equals("mnuid")) {
										icon.setMnuid(item.mValue);
									} else if (item.mKey.equals("mnutypeid")) {
										icon.setMnutypeid(item.mValue);
									} else if (item.mKey.equals("mnutypename")) {
										icon.setMnutypename(item.mValue);
									} else if (item.mKey.equals("pointnum")) {
										icon.setPointnum(item.mValue);
									} else if (item.mKey.equals("mnuisconst")) {
										icon.setMnuisconst(item.mValue);
									} else if (item.mKey.equals("mnuno")) {
										icon.setMnuno(item.mValue);
									}
								}

							}
							if (icon.getMnuorder() != null
									&& !"".equals(icon.getMnuorder())) {
								iconList.add(icon);
							}
						}

					}
				modle.setIconList(iconList);
			}
		}

		return modle;
	}
	
	private void showAgainLoadDialog () {
		new AlertDialog.Builder(context).setTitle("网络异常").setMessage("网络可能出现异常是否重试?")
		.setPositiveButton("重试", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				new ModleTask(context, listener,true).execute(params);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setCancelable(false).show();
	}
}
