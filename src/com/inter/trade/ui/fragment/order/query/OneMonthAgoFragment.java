package com.inter.trade.ui.fragment.order.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.order.DetialOrderActivity;
import com.inter.trade.ui.fragment.order.util.OrderBean;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.ui.fragment.order.util.OrderData.Goods;
import com.inter.trade.ui.fragment.order.util.OrderIndex;
import com.inter.trade.ui.fragment.order.util.OrderListParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 一个月以前订单
 * 
 * @author apple
 * 
 */
public class OneMonthAgoFragment extends BaseFragment implements OnClickListener,OnItemClickListener,OnRefreshListener{
	private Button order_query_btn;
	private EditText order_no_edit;
	public static int selectedIndex=0;
	private OrderQueryAdapter mAdapter;
	private OrderTask mTask;
	public static OrderBean mOrderBean = new OrderBean();;
	private MyListView mListView;
	private String mOrderNo=null;
	FragmentActivity mActivity;
	
	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	
	private boolean isMore=false;
	
	
	private static final String ONE_MONTH_AGO_KEY_STRING="ONE_MONTH_AGO_KEY_STRING";
	private String whereString;
	
	public static OneMonthAgoFragment create(String typeString){
		OneMonthAgoFragment fragment = new OneMonthAgoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ONE_MONTH_AGO_KEY_STRING, typeString);
		fragment.setArguments(bundle);
		return fragment;
	}
	public OneMonthAgoFragment() {

	}
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
//			PromptUtil.showLogin(getActivity());
			LoginUtil.mLoginStatus.mResponseData=null;
			LoginUtil.detection(getActivity());
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity =getActivity();
		mOrderBean.mOrderDatas = new ArrayList<OrderData>();
		LoginUtil.detection(getActivity());
		whereString = getArguments().getString(ONE_MONTH_AGO_KEY_STRING);
		
		mTask = new OrderTask();
		mTask.execute("");
//		mHandler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				mHandler.sendEmptyMessage(0);
//			}
//		}, 3000);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		try {
			if(mOrderBean.mOrderDatas.size()==0||mOrderBean.mOrderDatas.size()==(arg2-1)){
				return;
			}
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetialOrderActivity.class);
			intent.putExtra(DetialOrderActivity.ORDER_INDEX, arg2-1);
			intent.putExtra(OrderIndex.ORDER_KEY_STRING, OrderIndex.ORDER_ONE_MONTH_AGO);
			getActivity().startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mStartIndex=0;
		mTask = new OrderTask();
		mTask.execute("");
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
			mTask = new OrderTask();
			mTask.execute("");
		}else{
			mListView.setLastText();
//			PromptUtil.showToast(getActivity(), getString(R.string.last_message));
//			mListView.setProgressGone();
//			mListView.setIsFetchMoreing(false);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.order_pay_layout, container,
				false);
		order_query_btn = (Button) view.findViewById(R.id.order_query_btn);
		order_query_btn.setOnClickListener(this);
		order_no_edit = (EditText) view.findViewById(R.id.order_no_edit);
		mListView = (MyListView)view.findViewById(R.id.order_pay_list);
		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(this);
		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.order_query_btn:
			if (checkInput()) {
				mTask = new OrderTask();
				mTask.execute("");
			}
			break;
		default:
			break;
		}
	}

	private boolean checkInput() {
		String no = order_no_edit.getText().toString();
		if (null == no || "".equals(no)) {
			PromptUtil.showToast(getActivity(), "请输入订单号");
			return false;
		}
		
		mOrderNo = no;
		return true;
	}

	public class OrderTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		
		private String mResultString;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(!isMore){
				 PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
			}
			
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				data.putValue("msgstart", mStartIndex+"");
				data.putValue("msgdisplay", Constants.DISPLAY_COUNT+"");
				if(null != mOrderNo){
					data.putValue("orderno", mOrderNo);
				}else{
					data.putValue("orderno", "");
				}
				data.putValue("orderstate", whereString);
				data.putValue("querywhere", "#");
				List<ProtocolData> mDatas = createRequest(data);
				OrderListParser versionParser = new OrderListParser();
				mRsp = HttpUtil.doRequest(versionParser, mDatas);
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
			 mListView.onRefreshComplete();
			 if(mActivity==null){
				 return;
			 }
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, mActivity.getResources().getString(R.string.loading));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					if(!isMore){
						mOrderBean.mOrderDatas.clear();
					}
					parserResponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					if(mAdapter ==null){
						mAdapter =new OrderQueryAdapter(getActivity(), mOrderBean.mOrderDatas);
						mListView.setAdapter(mAdapter);
					}else{
						mAdapter.notifyDataSetChanged();
					}
					
					 if(isMore){
						 mListView.setSelection(mLoadedCount);
					 }
					 isMore=false;
					 mListView.setProgressGone();
					 mListView.setIsFetchMoreing(false);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}
	}

	private List<ProtocolData> createRequest(CommonData data) {
		return ProtocolUtil.getRequestDatas("ApiOrderInfo", "readOrderList",
				data);
	}

	private void parserResponse(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		mOrderBean.mResponseData = response;
		LoginUtil.mLoginStatus.mResponseData =response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);
//				List<ProtocolData> req_seq = data.find("/req_seq");
//				if (req_seq != null) {
//					response.setReq_seq(req_seq.get(0).mValue);
//				}
//
//				List<ProtocolData> ope_seq = data.find("/ope_seq");
//				if (ope_seq != null) {
//					response.setOpe_seq(ope_seq.get(0).mValue);
//				}
//
//				List<ProtocolData> rettype = data.find("/retinfo/rettype");
//				if (rettype != null) {
//					response.setRettype(rettype.get(0).mValue);
//				}
//
//				List<ProtocolData> retcode = data.find("/retinfo/retcode");
//				if (retcode != null) {
//					response.setRetcode(retcode.get(0).mValue);
//				}
//
//				List<ProtocolData> retmsg = data.find("/retinfo/retmsg");
//				if (retmsg != null) {
//					response.setRetmsg(retmsg.get(0).mValue);
//				}

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					mOrderBean.result = result.get(0).mValue;
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					mOrderBean.message = message.get(0).mValue;
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				List<ProtocolData> msgallcount = data.find("/msgallcount");
				if (msgallcount != null) {
					mOrderBean.msgallcount = msgallcount.get(0).mValue;
				}
				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
				if (msgdiscount != null) {
					mOrderBean.msgdiscount = msgdiscount.get(0).mValue;
				}
				
				mLoadedCount=Integer.parseInt(mOrderBean.msgdiscount);
				mTotalCount=Integer.parseInt(mOrderBean.msgallcount);
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for (ProtocolData child : aupic) {
					OrderData picData = new OrderData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for (String key : keys) {
							List<ProtocolData> rs = child.mChildren.get(key);
							for (ProtocolData item : rs) {
								if (item.mKey.equals("orderstate")) {
									picData.orderstate = item.mValue;

								} else if (item.mKey.equals("orderid")) {
									picData.orderid = item.mValue;

								} else if (item.mKey.equals("orderno")) {

									picData.orderno = item.mValue;

								} else if (item.mKey.equals("ordertime")) {

									picData.ordertime = item.mValue;

								} else if (item.mKey.equals("ordermoney")) {

									picData.ordermoney = item.mValue;

								} else if (item.mKey.equals("orderpronum")) {

									picData.orderpronum = item.mValue;
								} else if (item.mKey.equals("shman")) {

									picData.shman = item.mValue;
								} else if (item.mKey.equals("shcmpyname")) {

									picData.shcmpyname = item.mValue;
								} else if (item.mKey.equals("shaddress")) {

									picData.shaddress = item.mValue;
								} else if (item.mKey.equals("fhwltype")) {

									picData.fhwltype = item.mValue;
								} else if (item.mKey.equals("ordermemo")) {

									picData.ordermemo = item.mValue;
								} else if (item.mKey.equals("allpromoney")) {

									picData.allpromoney = item.mValue;
								} else if (item.mKey.equals("fhwlmoney")) {

									picData.fhwlmoney = item.mValue;
								} else if (item.mKey.equals("msgallcount")) {

									picData.orderpaytype = item.mValue;
								}

								else if (item.mKey.equals("msproinfo")) {
									parserGood(item, picData);
								}
							}
						}
						mOrderBean.mOrderDatas.add(picData);
						// mList.add(picData);
					}
				}
			}
		}
	}

	private void parserGood(ProtocolData data, OrderData picData) {
//		List<ProtocolData> goodDatas = data.find("/msproinfo");
		List<ProtocolData> msgchild = data.find("/msgchild");
		picData.mGoods = new ArrayList<OrderData.Goods>();
		if(msgchild!=null)
		for (ProtocolData good : msgchild) {
			Goods g = new Goods();
			if (good.mChildren != null && good.mChildren.size() > 0) {
				Set<String> gkeys = good.mChildren.keySet();
				for (String gkey : gkeys) {
					List<ProtocolData> chilDatas = good.mChildren.get(gkey);
					for (ProtocolData content : chilDatas) {
						if (content.mKey.equals("proname")) {
							g.proname = content.mValue;

						} else if (content.mKey.equals("proprice")) {
							g.proprice = content.mValue;

						} else if (content.mKey.equals("prounit")) {

							g.prounit = content.mValue;

						} else if (content.mKey.equals("pronum")) {

							g.pronum = content.mValue;

						} else if (content.mKey.equals("promoney")) {

							g.promoney = content.mValue;

						}
					}
				}
			}
			picData.mGoods.add(g);
		}
	}
}
