/*
 * @Title:  ModleParser.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月4日 下午5:17:37
 * @version:  V1.0
 */
package com.inter.trade.ui.func.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.data.Ad;
import com.inter.trade.ui.func.data.AdData;
import com.inter.trade.ui.func.data.ModleData;
import com.inter.trade.ui.func.parser.AdListParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 主页功能获取接口
 * 
 * @author ChenGuangChi
 * @data: 2014年7月4日 下午5:17:37
 * @version: V1.0
 */
public class AdListTask extends AsyncTask<String, Void, Void> {
	private Context context;
	private ResponseStateListener listener;
	ProtocolRsp mRsp;

	public AdListTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected Void doInBackground(String... params) {
		try {
			CommonData mData = new CommonData();
			mData.putValue("msgadtype", "1");

			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
					"ApiAppInfo", "readIndexAdList", mData);
			AdListParser myParser = new AdListParser();
			mRsp = HttpUtil.doRequest(myParser, mDatas);

		} catch (Exception e) {
			e.printStackTrace();
			mRsp = null;
		}

		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(mRsp == null) {
			PromptUtil.showToast(context, context.getString(R.string.net_error));
			return;
		}
		List<ProtocolData> mList = mRsp.mActions;
		AdData data = parserResponse(mList);
		

		if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
				(Activity) context)) {
		}
		if (listener != null) {
			listener.onSuccess(data, ModleData.class);
		}
	}

	private AdData parserResponse(List<ProtocolData> mDatas) {
		AdData adData=new AdData();
		ResponseData response = new ResponseData();
		//LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).getmValue();
				}

				List<ProtocolData> message = data.find("/message");
				if (result != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				List<ProtocolData> isnew = data.find("/adallcount");
				if (result != null) {
					adData.setAdallcount(isnew.get(0).getmValue());
				}

				List<ProtocolData> aupic = data.find("/msgchild");
				ArrayList<Ad> adList=new ArrayList<Ad>();
				if (aupic != null)
					for (ProtocolData child : aupic) {
						if (child.mChildren != null
								&& child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							Ad ad=new Ad();
							for (String key : keys) {
								List<ProtocolData> rs = child.mChildren
										.get(key);
								for (ProtocolData item : rs) {
									if (item.mKey.equals("adno")) {
										ad.setAdno(item.mValue);
									} else if (item.mKey.equals("adpicurl")) {
										ad.setAdpicurl(item.mValue);
									} else if (item.mKey.equals("adtitle")) {
										ad.setAdtitle(item.mValue);
									} else if (item.mKey.equals("adlinkurl")) {
										ad.setAdlinkurl(item.mValue);
									}
								}

							}
							adList.add(ad);
						}
						
					}
				adData.setAdList(adList);
			}
		}

		return adData;
	}
}
