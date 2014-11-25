package com.inter.trade.ui.fragment.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadMyAccountParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.AcctypeData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.FragmentUtil;
import com.inter.trade.ui.fragment.wallet.DetialMonthActivity;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class PaySelectedFragment extends BaseFragment implements OnClickListener,OnItemClickListener{
	
	private Button btn_zhuanzhang;
	
	private String mAccallMoney;//总额
	private ArrayList<AcctypeData> mAcctypeDatas = new ArrayList<AcctypeData>();
	private ListView wallet_listview;
	private TextView total_count_tv;
	private String total="0";
	
	private LayoutInflater mInflater=null;
	private LinearLayout wallet_container;
	private WalletTask mTask;
	
	public PaySelectedFragment()
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
		View view = inflater.inflate(R.layout.pay_method_layout, container,false);
		btn_zhuanzhang =(Button)view.findViewById(R.id.btn_zhuanzhang);
		wallet_container = (LinearLayout)view.findViewById(R.id.wallet_container);
		total_count_tv = (TextView)view.findViewById(R.id.total_count_tv);
		
		btn_zhuanzhang.setOnClickListener(this);
		
		mInflater=inflater;
		setTitle("支付方式");
		setBackVisible();
		
//		mTask= 	new WalletTask();
//		mTask.execute("");
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("支付方式");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mTask!=null){
			mTask.cancel(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getActivity(), DetialMonthActivity.class);
		intent.putExtra(DetialMonthActivity.MONTH_INDEX, mAcctypeDatas.get(arg2).acctypeid);
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
		default:
			break;
		}
	}
	private void showRecharge(){
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, new RechargeManagerFragment());
//		transaction.commit();
		FragmentUtil.startFuncActivity(getActivity(), FragmentFactory.RechageFragment_INDEX);
	}
	
	private void showTransfer(){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new OrderSwipFragment());
		transaction.commit();
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
					ProtocolParser.instance().setParser(authorRegParser);
					String content = ProtocolParser.instance().aToXml(mDatas);
					
					Logger.d("HttpApi", "request\n"+content);
					mRsp = HttpUtil.getRequest(content, authorRegParser);
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
					
//					mAdapter = new BankListAdapter(getActivity(), mBankDatas);
//					mListView.setAdapter(mAdapter);
					wallet_container.removeAllViews();
					for(AcctypeData data : mAcctypeDatas){
						ItemView itemView = new ItemView(data);
						wallet_container.addView(itemView.mView);
					}
					total_count_tv.setText(total+" 元");
//					wallet_listview.setAdapter(new WalletListAdapter(getActivity(), mAcctypeDatas));
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
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, "ApiAppAccountInfo");
		ProtocolData authorid= new ProtocolData("authorid", LoginUtil.mLoginStatus.authorid);
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, "readMyAccount");
		
		chinalData.addChild(authorid);
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		headerData.addChild(chinalData);
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		
		bodyData.addChild(authorid);
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
	
	public  class ItemView {
		public View mView;
		public ItemView(AcctypeData data){
			mView = mInflater.inflate(R.layout.wallet_item, null);
			TextView name = (TextView)mView.findViewById(R.id.name);
			TextView count = (TextView)mView.findViewById(R.id.count);
			name.setText(data.acctypename);
			count.setText(data.accmoney+"元");
		}
	}
}
