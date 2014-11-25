package com.inter.trade.ui.fragment.wallet;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadAccglistParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.BankListAdapter;
import com.inter.trade.adapter.MonthListAdapter;
import com.inter.trade.data.AccMonthData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.imageframe.AsyncTask;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 收支明细
 * @author apple
 *
 */

public class DetialMonthListFragment extends BaseFragment implements OnItemClickListener{
	private ArrayList<AccMonthData> monthDatas = new ArrayList<AccMonthData>();
	private int mTtotalCount =0;//总计N条信息
	private int mLoadCount = 0;//累计加载第N条记录
	
	private ListView mListView;
	private BankListAdapter mAdapter;
	
	private String acctypeid;
	private String name ;
	private MonthTask monthTask;
	
	public DetialMonthListFragment()
	{
		
	}
	
	public static DetialMonthListFragment newInstance(String type,String name){
		final DetialMonthListFragment fragment = new DetialMonthListFragment();
		final Bundle args = new Bundle();
        args.putString(DetialMonthActivity.MONTH_INDEX, type);
        args.putString(DetialMonthActivity.MONTH_TYPE_STRING, name);
        fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		acctypeid =getArguments()==null?null:getArguments().getString(DetialMonthActivity.MONTH_INDEX);
		name = getArguments().getString(DetialMonthActivity.MONTH_TYPE_STRING);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.bank_list, container,false);
		mListView = (ListView)view.findViewById(R.id.bank_listview);
		mListView.setOnItemClickListener(this);
		setBackVisible();
		setTitle("收支明细");
		monthTask=new MonthTask();
		monthTask.execute("");
		return view;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
//		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, DetialMonthFragment.newInstance(
//				acctypeid,
//				monthDatas.get(arg2).accmonth,
//				monthDatas.get(arg2).accincome,
//				monthDatas.get(arg2).accpayout
//				)
//				);
//		transaction.commit();
		Intent intent = new Intent();
		intent.putExtra("acctypeid", acctypeid);
		intent.putExtra("acctypename", name);
		intent.putExtra("accmonth", monthDatas.get(arg2).accmonth);
		intent.putExtra("accincome", monthDatas.get(arg2).accincome);
		intent.putExtra("accpayout", monthDatas.get(arg2).accpayout);
		intent.setClass(getActivity(), DetialXiangqingActivity.class);
		getActivity().startActivity(intent);
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(monthTask!=null){
			monthTask.cancel(true);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("收支明细");
	}


	class MonthTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			UPLOAD_URL = LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				
					List<ProtocolData> mDatas = getRequestDatas();
					ReadAccglistParser authorRegParser = new ReadAccglistParser();
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
					parserResoponse(datas);
//					mAdapter = new BankListAdapter(getActivity(), mBankDatas);
//					mListView.setAdapter(mAdapter);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					
					mListView.setAdapter(new MonthListAdapter(getActivity(), monthDatas));
				}else {
					PromptUtil.showToast(getActivity(), getString(R.string.net_error));
				}
			} catch (Exception e) {
				// TODO: handle exception
				PromptUtil.showToast(getActivity(),getString(R.string.req_error));
			}
			
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
		monthDatas.clear();
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
				if(aupic == null){
					return;
				}
				for(ProtocolData child:aupic){
					AccMonthData picData = new AccMonthData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("accmonth")){
									picData.accmonth  = item.mValue;
									
								}else if(item.mKey.equals("accincome")){
									picData.accincome  = item.mValue;
									
								}else if(item.mKey.equals("accpayout")){
									
									picData.accpayout  = item.mValue;
								}
							}
						}
					}
					
					monthDatas.add(picData);
				}
				
			}
		}
	}
	
	/**
	 * 请求修改身份信息
	 * @return
	 */
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		data.putValue("acctypeid", acctypeid);
		List<ProtocolData> mDatas =ProtocolUtil.getRequestDatas("ApiAppAccountInfo"
				, "readAccglist", data);
		return mDatas;
	}
}
