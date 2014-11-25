package com.inter.trade.ui.fragment.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadHelpListParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.HelpListAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.HelpData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.DetialActivity;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class HelpListFragment extends BaseFragment implements OnItemClickListener{
	private ArrayList<HelpData> mHelpDatas = new ArrayList<HelpData>();
	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	private boolean isMore=false;
	
	
	
	private MyListView mListView;
	private HelpListAdapter mAdapter;
	private HelpTask mHelpTask;
	public HelpListFragment()
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
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.help_list, container,false);
		mListView = (MyListView)view.findViewById(R.id.mm_listview);
		mListView.setonRefreshListener(onRefreshListener);
		mListView.setOnItemClickListener(this);
		
		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);
		setBackVisible();
		setTitle("帮助中心");
		
		mHelpTask = new HelpTask();
		mHelpTask.execute("");
		
		return view;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("帮助中心");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(mHelpDatas.size() == (arg2-1)){
			return;
		}
		HelpData data = mHelpDatas.get(arg2-1);
		Intent intent = new Intent();
		intent.setClass(getActivity(), DetialActivity.class);
		intent.putExtra("title", data.helpname);
		intent.putExtra("content", data.helpcontent);
		intent.putExtra("date", data.helpdate);
		getActivity().startActivity(intent);
	}
	
	
	OnPullDownListener onPullDownListener = new OnPullDownListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onMore() {
			// TODO Auto-generated method stub
			isMore=true;
			loadMore();
			
		}
	};
	private void loadMore(){
		if(mLoadedCount<mTotalCount){
			mStartIndex = mLoadedCount;
			mHelpTask = new HelpTask();
			mHelpTask.execute("");
		}else{
			mListView.setLastText();
//			mListView.setProgressGone();
//			mListView.setIsFetchMoreing(false);
		}
	}
	
	OnRefreshListener onRefreshListener = new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			isMore=false;
			mStartIndex=0;
			mHelpTask = new HelpTask();
			mHelpTask.execute("");
		}
	};
	class HelpTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			UPLOAD_URL = LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				data.putValue("msgstart", mStartIndex+"");
				data.putValue("msgdisplay", "10");
				List<ProtocolData> mDatas = createRequest(data);
				ReadHelpListParser authorRegParser = new ReadHelpListParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
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
					if(!isMore){
						mHelpDatas.clear();
					}
					if(datas!= null){
						parserResoponse(datas);
					}
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					mAdapter = new HelpListAdapter(getActivity(), mHelpDatas);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					
					if(isMore){
						 mListView.setSelection(mLoadedCount);
					 }
					 isMore=false;
					 mListView.setProgressGone();
					 mListView.setIsFetchMoreing(false);
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
			 if(!isMore){
				 PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
			 }
		}
		
	}
	
	private List<ProtocolData> createRequest(CommonData data) {
		return ProtocolUtil.getRequestDatas("ApiAppHelpinfo", "readHelpList",
				data);
	}
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
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
				
				List<ProtocolData> msgallcount = data.find("/msgallcount");
				if(msgallcount != null){
					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
				}
				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
				if(msgdiscount != null){
					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic != null)
				for(ProtocolData child:aupic){
					HelpData picData = new HelpData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("helpid")){
									picData.helpid  = item.mValue;
									
								}else if(item.mKey.equals("helpcontent")){
									picData.helpcontent  = item.mValue;
									
								}else if(item.mKey.equals("helpdate")){
									
									picData.helpdate  = item.mValue;
								}else if(item.mKey.equals("helpname")){
									
									picData.helpname  = item.mValue;
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
