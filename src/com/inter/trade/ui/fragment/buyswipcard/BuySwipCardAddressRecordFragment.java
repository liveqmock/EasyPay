package com.inter.trade.ui.fragment.buyswipcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.BuySwipCardAddressRecordAdapter;
import com.inter.trade.adapter.BuySwipCardRecordAdapter;
import com.inter.trade.adapter.CridetAdapter;
import com.inter.trade.adapter.MobileRecordAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BuySwipCardCreateAddressActivity;
import com.inter.trade.ui.BuySwipcardPayActivity;
import com.inter.trade.ui.DetialActivity;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.BankRecordListActivity.TYPECLASS;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardAddressRecordData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardAddressRecordParser;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardCreateAddressParser;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardDeleteAddressData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardDeleteAddressParser;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardRecordData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardRecordParser;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.ui.fragment.cridet.CridetRecordFragment.RecordTask;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeRecordData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeRecordParser;
import com.inter.trade.ui.fragment.transfer.RecordDetialFragment;
import com.inter.trade.ui.fragment.transfer.TransferFragment.TransferType;
import com.inter.trade.ui.fragment.transfer.util.TransferRecordParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 购买刷卡器历史地址记录Fragment
 * @author zhichao.huang
 *
 */
@SuppressLint("ValidFragment")
public class BuySwipCardAddressRecordFragment extends BaseFragment implements OnClickListener,OnItemClickListener, OnItemLongClickListener{
	private Button cridet_back_btn;
	private ArrayList<BuySwipCardAddressRecordData> mList = new ArrayList<BuySwipCardAddressRecordData>();
	private MyListView mListView; 
	private RecordTask mRecordTask;
	
	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	private boolean isMore=false;
	
	BuySwipCardAddressRecordAdapter mAdapter;
	public static final String TYPE_STRING="TYPE_STRING";
//	private String mType;
	
	private Bundle bundleData;
	
	/**
	 * 判断是否要刷新地址列表
	 */
	public static boolean isRefresh = false;
	
	private BuySwipCardDeleteAddressData buySwipCardDeleteAddressData;
	
	public static BuySwipCardAddressRecordFragment instance(String type){
		BuySwipCardAddressRecordFragment fragment = new BuySwipCardAddressRecordFragment();
		Bundle argsBundle = new Bundle();
		argsBundle.putString(TYPE_STRING, type);
		fragment.setArguments(argsBundle);
		return fragment;
		
	}
	
	
	public BuySwipCardAddressRecordFragment() {

	}
	
	public BuySwipCardAddressRecordFragment(Bundle bundle) {
		
		if(bundle != null) {
			bundleData = bundle;
		}
		

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("请选择收货地址");
		isRefresh = false;
		mRecordTask = new RecordTask();
		mRecordTask.execute("");
//		mType = getArguments().getString(TYPE_STRING)==null?TransferType.USUAL_TRANSER:getArguments().getString(TYPE_STRING);
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(mList.size() == (arg2-1)){
			return;
		}
		BuySwipCardAddressRecordData data = mList.get(arg2-1);
		RecordDetialFragment.mMaps.clear();
//		HashMap<String, String> item = new HashMap<String, String>();
//		item.put("订单编号", data.orderno);
//		RecordDetialFragment.mMaps.add(item);
//		
//		HashMap<String, String> item1 = new HashMap<String, String>();
//		item1.put("产品名称",
//				data.orderprodurename
//				);
//		RecordDetialFragment.mMaps.add(item1);
//		
//		HashMap<String, String> shoukuanbank = new HashMap<String, String>();
//		shoukuanbank.put("购买数量",
//				data.ordernum+"个"
//				);
//		RecordDetialFragment.mMaps.add(shoukuanbank);
//		
//		
//		HashMap<String, String> item2 = new HashMap<String, String>();
//		item2.put("购买单价", data.orderprice+"元");
//		RecordDetialFragment.mMaps.add(item2);
//		
//		HashMap<String, String> fukuanbank = new HashMap<String, String>();
//		fukuanbank.put("订单金额",
//				data.ordermoney
//				);
//		RecordDetialFragment.mMaps.add(fukuanbank);
//		
////		HashMap<String, String> item3 = new HashMap<String, String>();
////		item3.put("付款借记卡", CommonActivity.mTransferData.getValue(DaikuanData.fucardno));
////		mActivity.mCommonData.add(item3);
//		
//		HashMap<String, String> item4 = new HashMap<String, String>();
//		item4.put("详细收货地址", data.ordershaddress);
//		RecordDetialFragment.mMaps.add(item4);
//		
//		HashMap<String, String> item5 = new HashMap<String, String>();
//		item4.put("收货人", data.ordershman);
//		RecordDetialFragment.mMaps.add(item5);
//		
//		HashMap<String, String> item6 = new HashMap<String, String>();
//		item4.put("收货电话", data.ordershphone);
//		RecordDetialFragment.mMaps.add(item6);
//		
//		HashMap<String, String> item7 = new HashMap<String, String>();
//		item4.put("支付状态", data.orderpaystatus);
//		RecordDetialFragment.mMaps.add(item7);
//		
//		HashMap<String, String> item8 = new HashMap<String, String>();
//		item4.put("订单状态", data.orderstate);
//		RecordDetialFragment.mMaps.add(item8);
//		
//		HashMap<String, String> item9 = new HashMap<String, String>();
//		item4.put("物流订单号", data.wlno);
//		RecordDetialFragment.mMaps.add(item9);
//		
//		HashMap<String, String> item10 = new HashMap<String, String>();
//		item4.put("物流公司id", data.kdcomanyid);
//		RecordDetialFragment.mMaps.add(item10);
		
//		HashMap<String, String> item5 = new HashMap<String, String>();
//		item5.put("手续费", data.feemoney);
//		RecordDetialFragment.mMaps.add(item5);
//		
//		HashMap<String, String> item6 = new HashMap<String, String>();
//		item6.put("刷卡金额", data.allmoney);
//		RecordDetialFragment.mMaps.add(item6);
		
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		//产品ID
		bundle.putString("product_id", bundleData != null ? bundleData.getString("product_id") : "");
		//折扣价
		bundle.putString("produrezheprice", bundleData != null ? bundleData.getString("produrezheprice") : "");
		//产品名称
		bundle.putString("product_name", bundleData != null ? bundleData.getString("product_name") : "");
		//收货地址ID
		bundle.putString("shaddressid", data.shaddressid);
		//收货地址
		bundle.putString("shaddress", data.shaddress);
		//收货人
		bundle.putString("shman", data.shman);
		//收货人电话
		bundle.putString("shphone", data.shphone);
		//运费
		bundle.putString("shyunfei", data.shyunfei);
		intent.putExtra("product_card", bundle);
		intent.setClass(getActivity(), BuySwipcardPayActivity.class);
		
		isRefresh = false;
		
//		Intent intent = new Intent();
//		intent.putExtra("type", "record");
//		intent.setClass(getActivity(), DetialActivity.class);
		startActivityForResult(intent, 300);
	}
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final BuySwipCardAddressRecordData data = mList.get(position-1);
		//长按删除地址
		new AlertDialog.Builder(getActivity()).setTitle("删除历史地址").setMessage("确定删除地址："+data.shaddress+"?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
//				((Activity)context).finish();
				
				buySwipCardDeleteAddressData = new BuySwipCardDeleteAddressData();
				buySwipCardDeleteAddressData.sunMap.put(BuySwipCardDeleteAddressData.SHADDRESSID, data.shaddressid);
				
				DeleteBuyAddressTask deleteBuyAddressTask = new DeleteBuyAddressTask();
				deleteBuyAddressTask.execute("");
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.buy_swipcard_address_list_layout, container, false);//R.layout.mm_list
		 mListView = (MyListView) view.findViewById(R.id.mm_listview);
		 mListView.setOnItemClickListener(this);
		 
		 mListView.setOnItemClickListener(this);
		 mListView.setonRefreshListener(onRefreshListener);
			
		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);
		
		mListView.setOnItemLongClickListener(this);
		
//		//模拟数据
//		BuySwipCardAddressRecordData recordData = new BuySwipCardAddressRecordData();
//		recordData.shman = "李小明";
//		recordData.shphone ="13888888888";
//		recordData.shaddress ="江西赣州";
//		mList.add(recordData);
//		
//		BuySwipCardAddressRecordData recordData2 = new BuySwipCardAddressRecordData();
//		recordData2.shman = "黄晓明";
//		recordData2.shphone ="15888888888";
//		recordData2.shaddress ="中国北京";
//		mList.add(recordData2);
//		
//		BuySwipCardAddressRecordData recordData3 = new BuySwipCardAddressRecordData();
//		recordData3.shman = "成龙";
//		recordData3.shphone ="18888888888";
//		recordData3.shaddress ="香港澳门";
//		mList.add(recordData3);
//		
//		mAdapter = new BuySwipCardAddressRecordAdapter(getActivity(), mList);
//		mListView.setAdapter(mAdapter);
		
		setBackVisible();
		setRightVisible(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				PromptUtil.showToast(getActivity(), "该功能正在开发...");
				Intent intent = new Intent();
				intent.setClass(getActivity(), BuySwipCardCreateAddressActivity.class);
				getActivity().startActivityForResult(intent, 200);
			}
		}, "新增地址");
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("请选择收货地址");
//		mList.clear();
//		mRecordTask = new RecordTask();
//		mRecordTask.execute("");
		if(isRefresh){
			onRefresh();
		}
	}
	
	public void onRefresh() {
		mList.clear();
		mRecordTask = new RecordTask();
		mRecordTask.execute("");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null) return;
		Log.i("demoing", "onActivityResult requestCode:"+requestCode+";resultCode:"+resultCode+";data:"+data.getStringExtra("result"));
		if(requestCode == 200){
			
			if(resultCode == getActivity().RESULT_OK){
				mList.clear();
				mRecordTask = new RecordTask();
				mRecordTask.execute("");
			}
			
		}
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
			mRecordTask = new RecordTask();
			mRecordTask.execute("");
		}else{
			mListView.setLastText();
//			mListView.setProgressGone();
//			mListView.setIsFetchMoreing(true);
		}
	}
	
	OnRefreshListener onRefreshListener = new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			isMore=false;
			mStartIndex=0;
			mRecordTask = new RecordTask();
			mRecordTask.execute("");
		}
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mRecordTask != null){
			mRecordTask.cancel(true);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			showChuxuka();
			break;

		default:
			break;
		}
	}

	private void showChuxuka() {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.func_container, new BuySuccessFragment());
		transaction.commit();
	}

	public class RecordTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;
		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
//				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
//				if(TransferType.USUAL_TRANSER.equals(mType)){
//					data.putValue("paytype",TYPECLASS.tfmg);	
//				}else {
//					data.putValue("paytype",TYPECLASS.suptfmg);	
//				}
				
				data.putValue("msgstart",mStartIndex+"");
				data.putValue("msgdisplay", Constants.RECORD_DISPLAY_COUNT+"");
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiBuyOderInfo", "readShaddressinfo",
						data);
				BuySwipCardAddressRecordParser recordParser = new BuySwipCardAddressRecordParser();
				mRsp = HttpUtil.doRequest(recordParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Logger.e(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, mActivity.getResources().getString(R.string.net_error));
			} else {
				
				if(!isMore){
					mList.clear();
				}
				
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					//如果mList.size() == 0，说明没有历史收货地址，则弹出新增地址的页面，便于用户第一次录入地址
					if(mList.size() == 0) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), BuySwipCardCreateAddressActivity.class);
						getActivity().startActivityForResult(intent, 200);
					}
					
					mAdapter = new BuySwipCardAddressRecordAdapter(getActivity(), mList);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					
					if(isMore){
						 mListView.setSelection(mLoadedCount);
					 }
					 isMore=false;
					 mListView.setProgressGone();
					 mListView.setIsFetchMoreing(false);

			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			mListView.onRefreshComplete();
		}
	}

//	private List<ProtocolData> createRequest(CommonData data) {
//		if(TransferType.USUAL_TRANSER.equals(mType)){
//			return ProtocolUtil.getRequestDatas("ApiPayinfo", "readTransferMoneyglist",
//					data);
//		}else{
//			//超级转账历史纪录
//			return ProtocolUtil.getRequestDatas("ApiPayinfo", "readSupTransferMoneyglist",
//					data);
//		}
//		
//	}

	private void parserResponse(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = result.get(0).mValue;
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
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					BuySwipCardAddressRecordData recordData = new BuySwipCardAddressRecordData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("shaddressid")){
									recordData.shaddressid  = item.mValue;
									
								}else if(item.mKey.equals("shaddress")){
									recordData.shaddress  = item.mValue;
									
								}else if(item.mKey.equals("shman")){
									
									recordData.shman  = item.mValue;
									
								}else if(item.mKey.equals("shphone")){
									
									recordData.shphone  = item.mValue;
									
								}else if(item.mKey.equals("shyunfei")){
									
									recordData.shyunfei  = item.mValue;
									
								}else if(item.mKey.equals("shyunfeitype")){
									
									recordData.shyunfeitype  = item.mValue;
								}else if(item.mKey.equals("shdefault")){
									
									recordData.shdefault  = item.mValue;
								}/**
								else if(item.mKey.equals("ordershphone")){
									
									recordData.ordershphone  = item.mValue;
								}else if(item.mKey.equals("orderpaystatus")){
									
									recordData.orderpaystatus  = item.mValue;
								}else if(item.mKey.equals("orderstate")){
									
									recordData.orderstate  = item.mValue;
								}else if(item.mKey.equals("wlno")){
									
									recordData.wlno  = item.mValue;
								}else if(item.mKey.equals("kdcomanyid")){
									
									recordData.kdcomanyid  = item.mValue;
								}*/
							}
						}
					}
					
					mList.add(recordData);
				}
			}
		}
	}
	
	/**
	 * 删除地址
	 * @author zhichao.huang
	 *
	 */
	class DeleteBuyAddressTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiBuyOderInfo", 
						"shaddressDelete", buySwipCardDeleteAddressData);
				BuySwipCardDeleteAddressParser authorRegParser = new BuySwipCardDeleteAddressParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp =null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){

//						BuySwipCardAddressRecordFragment.isRefresh = true;
//						PromptUtil.showToast(getActivity(), "已成功删除地址");
//		                Intent intent = new Intent();
//		                intent.putExtra("result", "success");
//						setResult(RESULT_OK, intent);
//						finish();
						onRefresh();

					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}

			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
	}
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

				//				List<ProtocolData> bkntno = data.find("/bkntno");
				//				if(bkntno != null){
				//					mBkntno = bkntno.get(0).mValue;
				//				}
			}

		}

	}
	

}
