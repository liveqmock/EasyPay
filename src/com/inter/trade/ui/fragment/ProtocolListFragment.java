package com.inter.trade.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.XieyiParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.ProtocolListAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.XieyiData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class ProtocolListFragment extends BaseFragment{
	private ArrayList<XieyiData> mHelpDatas = new ArrayList<XieyiData>();
	private MyListView mListView;
	private ProtocolListAdapter mAdapter;
	
	private HelpTask mHelpTask;
	public ProtocolListFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.help_list, container,false);
		mListView = (MyListView)view.findViewById(R.id.mm_listview);
//		mListView.setonRefreshListener(onRefreshListener);
		mAdapter = new ProtocolListAdapter(getActivity(),mHelpDatas);
		setBackVisible();
		
		setTitle(getTitleString());
		
		mHelpTask = new HelpTask();
		mHelpTask.execute("");
		return view;
	}
	
	private String getTitleString(){
		String title = "";
		if(AboutFragment.mProtocolType.equals("1")){
			title = "通付宝服务协议";
		}else if(AboutFragment.mProtocolType.equals("2")){
			title = "通付宝钱包服务协议 ";
		}else if(AboutFragment.mProtocolType.equals("3")){
			title = "通付宝注册协议";
		}else if(AboutFragment.mProtocolType.equals("4")){
			title = "关于通付宝公司";
		}else if(AboutFragment.mProtocolType.equals("6")){
			title="通付宝默认支付协议";
		}else if(AboutFragment.mProtocolType.equals("7")){
			title="信用卡交易受理银行清单";
		}else{
			title = "转账协议";
		}
		return title;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(getTitleString());
	}


	OnRefreshListener onRefreshListener = new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			
			mHelpTask = new  HelpTask();
			mHelpTask.execute("");
		}
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mHelpTask!=null){
			mHelpTask.cancel(true);
		}
	}

	class HelpTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			UPLOAD_URL = LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
					CommonData data = new CommonData();
					data.putValue("appruleid", AboutFragment.mProtocolType);
					List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAppInfo", 
							"readAppruleList", data);
					XieyiParser authorRegParser = new XieyiParser();
					mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
//					ProtocolParser.instance().setParser(authorRegParser);
//					String content = ProtocolParser.instance().aToXml(mDatas);
//					
//					Logger.d("HttpApi", "request\n"+content);
//					mRsp = HttpUtil.getRequest(content, authorRegParser);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
				if(mRsp != null){
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
				}else {
					PromptUtil.showToast(getActivity(), getString(R.string.net_error));
				}
			} catch (Exception e) {
				// TODO: handle exception
				PromptUtil.showToast(getActivity(),getString(R.string.req_error));
			}
			mListView.onRefreshComplete();
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
		
	}
	
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		mHelpDatas.clear();
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					XieyiData picData = new XieyiData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("appruleid")){
									picData.appruleid  = item.mValue;
									
								}else if(item.mKey.equals("appruletitle")){
									picData.appruletitle  = item.mValue;
									
								}else if(item.mKey.equals("apprulecontent")){
									
									picData.apprulecontent  = item.mValue;
								}else if(item.mKey.equals("updatetime")){
									
									picData.updatetime  = item.mValue;
								}
							}
						}
					}
					
					mHelpDatas.add(picData);
				}
				
			}
		}
	}
	
	/**
	 * 请求修改身份信息
	 * @return
	 */
	private List<ProtocolData> getRequestDatas(){
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, "ApiAppHelpinfo");
		ProtocolData authorid= new ProtocolData("authorid", LoginUtil.mLoginStatus.authorid);
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, "readHelpList");
		
		chinalData.addChild(authorid);
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		headerData.addChild(chinalData);
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		ProtocolData msgstart = new ProtocolData("msgstart", "0");
		ProtocolData msgdisplay = new ProtocolData("msgdisplay", "6");
		
		
		bodyData.addChild(msgstart);
		bodyData.addChild(msgdisplay);
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
}
