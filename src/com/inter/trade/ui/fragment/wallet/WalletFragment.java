package com.inter.trade.ui.fragment.wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadMyAccountParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.WalletListAdapter;
import com.inter.trade.data.AcctypeData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.FragmentUtil;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;

public class WalletFragment extends BaseFragment implements OnClickListener,OnItemClickListener{
	
	private Button btn_recharge;
	private Button btn_zhuanzhang;
	
	private String mAccallMoney;//总额
	private ArrayList<AcctypeData> mAcctypeDatas = new ArrayList<AcctypeData>();
	private ListView wallet_listview;
	private TextView total_count_tv;
	private String total="0";
	private LinearLayout total_layout;
	private WalletTask mTask;
	
	public WalletFragment()
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
		View view = inflater.inflate(R.layout.wallet_layout, container,false);
		btn_recharge =(Button)view.findViewById(R.id.btn_recharge);
		btn_zhuanzhang =(Button)view.findViewById(R.id.btn_zhuanzhang);
		wallet_listview = (ListView)view.findViewById(R.id.wallet_listview);
		total_count_tv = (TextView)view.findViewById(R.id.total_count_tv);
		total_layout = (LinearLayout)view.findViewById(R.id.total_layout);
		
		btn_zhuanzhang.setOnClickListener(this);
		btn_recharge.setOnClickListener(this);
		total_layout.setOnClickListener(this);
		
		wallet_listview.setOnItemClickListener(this);
		setTitle("我的钱包");
		setBackVisible();
		
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("我的钱包");
		mTask = new WalletTask();
		mTask.execute("");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getActivity(), DetialMonthActivity.class);
		intent.putExtra(DetialMonthActivity.MONTH_INDEX, mAcctypeDatas.get(arg2).acctypeid);
		intent.putExtra(DetialMonthActivity.MONTH_TYPE_STRING, mAcctypeDatas.get(arg2).acctypename);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_recharge:
			showRecharge();
			break;
		case R.id.btn_zhuanzhang:
			showTransfer();
			
			break;
		case R.id.total_layout:
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetialMonthActivity.class);
			intent.putExtra(DetialMonthActivity.MONTH_INDEX, "");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mTask != null){
			mTask.cancel(true);
		}
	}

	private void showRecharge(){
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, new RechargeManagerFragment());
//		transaction.commit();
		FragmentUtil.startFuncActivity(getActivity(), FragmentFactory.RechageFragment_INDEX);
	}
	
	private void showTransfer(){
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY, FuncMap.ORDER_INDEX_FUNC);
		intent.setClass(getActivity(), IndexActivity.class);
		startActivity(intent);
	}
	
	
	class WalletTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			UPLOAD_URL = LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				
					List<ProtocolData> mDatas = getRequestDatas();
					ReadMyAccountParser authorRegParser = new ReadMyAccountParser();
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
					
					
					wallet_listview.setAdapter(new WalletListAdapter(getActivity(), mAcctypeDatas));
					total_count_tv.setText(NumberFormatUtil.format2(total)+" 元");
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
		mAcctypeDatas.clear();
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
				
				List<ProtocolData> accallmoney = data.find("/accallmoney");
				if(accallmoney != null){
					total = accallmoney.get(0).mValue;
				}
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic == null){
					return;
				}
				for(ProtocolData child:aupic){
					AcctypeData picData = new AcctypeData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("acctypeid")){
									picData.acctypeid  = item.mValue;
									
								}else if(item.mKey.equals("acctypename")){
									picData.acctypename  = item.mValue;
									
								}else if(item.mKey.equals("accmoney")){
									
									picData.accmoney  = item.mValue;
								}
							}
						}
					}
					
					mAcctypeDatas.add(picData);
				}
				
			}
		}
	}
	
	/**
	 * 读取钱包信息
	 * @return
	 */
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAppAccountInfo", 
				"readMyAccount", data);
		
		return mDatas;
	}
}
