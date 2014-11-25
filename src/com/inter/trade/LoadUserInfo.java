package com.inter.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.AuthorInfoParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.PicData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.UserInfo;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.UserInfoActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 读取用户信息
 * 
 * @author apple
 * 
 */
public class LoadUserInfo extends AsyncTask<String, Integer, Boolean> {
	FragmentActivity mActivity;

	public LoadUserInfo(FragmentActivity activity) {
		mActivity = activity;
	}

	private ProtocolRsp mRsp;

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		List<ProtocolData> mDatas = getRequestDatas();
		AuthorInfoParser authorRegParser = new AuthorInfoParser();
		mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if (mRsp == null) {
			PromptUtil.showToast(mActivity, mActivity.getString(R.string.net_error));
		} else {
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResoponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,mActivity)){
					return;
				}
				String retCode = LoginUtil.mLoginStatus.mResponseData.getRettype();
				if (retCode.equals(
						ProtocolUtil.HEADER_SUCCESS)) {
					if (LoginUtil.mLoginStatus.result
							.equals(ProtocolUtil.SUCCESS)) {
						Intent intent = new Intent();
						intent.setClass(mActivity, UserInfoActivity.class);
						intent.putExtra(FragmentFactory.INDEX_KEY,
								FragmentFactory.LEFT_USERINFO_INDEX);
						mActivity.startActivity(intent);
//						PromptUtil.showToast(mActivity,
//								LoginUtil.mLoginStatus.mResponseData.getRetmsg());
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Logger.e(e);
				PromptUtil.showToast(mActivity,
						mActivity.getString(R.string.req_error));
			}

		}

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		PromptUtil.showDialog(mActivity, mActivity.getResources().getString(R.string.loading));
	}

	private List<ProtocolData> getRequestDatas() {
		CommonData data = new CommonData();
//		data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfo",
				"readAuthorInfo", data);
		return mDatas;
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		UserInfo userInfo = new UserInfo();
		LoginUtil.mLoginStatus.mResponseData = response;
		LoginUtil.mUserInfo = userInfo;
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> autruename = data.find("/autruename");
				if (autruename != null) {
					userInfo.autruename = autruename.get(0).mValue;
				}
				List<ProtocolData> autrueidcard = data.find("/autrueidcard");
				if (autrueidcard != null) {
					userInfo.autrueidcard = autrueidcard.get(0).mValue;
				}
				List<ProtocolData> auemail = data.find("/auemail");
				if (auemail != null) {
					userInfo.auemail = auemail.get(0).mValue;
				}

				List<ProtocolData> aumobile = data.find("/aumobile");
				if (aumobile != null) {
					userInfo.aumobile = aumobile.get(0).mValue;
				}

				userInfo.mPicData = new ArrayList<PicData>();
				List<ProtocolData> aupic = data.find("/msgchild");
				if (aupic == null) {
					return;
				}
				for (ProtocolData child : aupic) {
					PicData picData = new PicData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for (String key : keys) {
							List<ProtocolData> rs = child.mChildren.get(key);
							for (ProtocolData item : rs) {
								if (item.mKey.equals("picid")) {
									picData.picid = item.mValue;

								} else if (item.mKey.equals("pictype")) {
									picData.pictype = item.mValue;

								} else if (item.mKey.equals("picpath")) {

									picData.picpath = item.mValue;
								} else if (item.mKey.equals("uploadpictype")) {

									picData.uploadpictype = item.mValue;
								} else if (item.mKey.equals("uploadurl")) {

									picData.uploadurl = item.mValue;
								} else if (item.mKey.equals("uploadmethod")) {

									picData.uploadmethod = item.mValue;
								}
							}
						}
					}

					userInfo.mPicData.add(picData);
				}
				
				//代理商用户基本信息
				List<ProtocolData> agentcompany = data.find("/agentcompany");
				if (agentcompany != null) {
					userInfo.agentcompany = agentcompany.get(0).mValue;
				}
				List<ProtocolData> agentarea = data.find("/agentarea");
				if (agentarea != null) {
					userInfo.agentarea = agentarea.get(0).mValue;
				}
				List<ProtocolData> agentaddress = data.find("/agentaddress");
				if (agentaddress != null) {
					userInfo.agentaddress = agentaddress.get(0).mValue;
				}
				List<ProtocolData> agentmanphone = data.find("/agentmanphone");
				if (agentmanphone != null) {
					userInfo.agentmanphone = agentmanphone.get(0).mValue;
				}
				List<ProtocolData> agentfax = data.find("/agentfax");
				if (agentfax != null) {
					userInfo.agentfax = agentfax.get(0).mValue;
				}
				List<ProtocolData> agenthttime = data.find("/agenthttime");
				if (agenthttime != null) {
					userInfo.agenthttime = agenthttime.get(0).mValue;
				}
				List<ProtocolData> agentbzmoney = data.find("/agentbzmoney");
				if (agentbzmoney != null) {
					userInfo.agentbzmoney = agentbzmoney.get(0).mValue;
				}

			}
		}
	}
}
