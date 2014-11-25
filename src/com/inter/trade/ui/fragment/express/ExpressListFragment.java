package com.inter.trade.ui.fragment.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.ui.fragment.coupon.parser.RecordParser;
import com.inter.trade.ui.fragment.coupon.util.BuyAdapter;
import com.inter.trade.ui.fragment.coupon.util.HistoryData;
import com.inter.trade.ui.fragment.express.util.ExpressListAdapter;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class ExpressListFragment extends BaseFragment implements OnClickListener,OnItemClickListener {
	private ArrayList<HistoryData> mList = new ArrayList<HistoryData>();
	private ListView mListView; 
	public ExpressListFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("更多查询");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity())
		;
		View view = inflater.inflate(R.layout.mm_list_two, container, false);
		 mListView = (ListView) view.findViewById(R.id.mm_listview);
		 mListView.setAdapter(new ExpressListAdapter(getActivity(), ExpressGridFragment.mList));
		 mListView.setOnItemClickListener(this);
		setBackVisible();
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.EXPRESS_QUERY_INDEX);
		intent.setClass(getActivity(), FunctionActivity.class);
		ExpressGridFragment.mExpressData = ExpressGridFragment.mList.get(arg2-1);
		getActivity().startActivity(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("更多查询");
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
//			PromptUtil.showDialog(getActivity(), "");
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				List<ProtocolData> mDatas = createRequest(data);
				RecordParser versionParser = new RecordParser();
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
//			PromptUtil.dissmiss();
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, mActivity.getResources().getString(R.string.loading));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					mListView.setAdapter(new BuyAdapter(getActivity(), mList));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}
	}

	private List<ProtocolData> createRequest(CommonData data) {
		return ProtocolUtil.getRequestDatas("ApiCouponInfo", "couponSalelist",
				data);
	}

	private void parserResponse(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
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
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				for(ProtocolData child:aupic){
					HistoryData picData = new HistoryData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("couponno")){
									picData.couponno  = item.mValue;
									
								}else if(item.mKey.equals("couponmoney")){
									picData.couponmoney  = item.mValue;
									
								}else if(item.mKey.equals("coupondate")){
									
									picData.coupondate  = item.mValue;
									
								}else if(item.mKey.equals("paycardid")){
									
									picData.paycardid  = item.mValue;
									
								}else if(item.mKey.equals("couponid")){
									
									picData.couponid  = item.mValue;
									
								}else if(item.mKey.equals("couponmemo")){
									
									picData.couponmemo  = item.mValue;
								}
							}
						}
					}
					
					mList.add(picData);
				}
			}
		}
	}

}
