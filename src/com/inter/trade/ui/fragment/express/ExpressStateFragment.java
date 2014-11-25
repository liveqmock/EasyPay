package com.inter.trade.ui.fragment.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.ui.fragment.express.util.ExpressData;
import com.inter.trade.ui.fragment.express.util.ExpressDetialParser;
import com.inter.trade.ui.fragment.express.util.ExpressEntity;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class ExpressStateFragment extends BaseFragment implements OnClickListener {
	private Button find_more;
	private TextView express_title_tv;
	private EditText order_no_edit;
	private String mOrderNo;
	private ExpressData mCurrentData;
	private ExpressEntity mEntity;
	
	private ArrayList<ExpressData> mList = new ArrayList<ExpressData>();
	private RecordTask mRecordTask;
	public ExpressStateFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("快递查询");
		mCurrentData = ExpressGridFragment.mExpressData;
		LoginUtil.detection(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.express_find_layout, container, false);
		 find_more = (Button)view.findViewById(R.id.order_query_btn);
		 express_title_tv = (TextView)view.findViewById(R.id.express_title_tv);
		 order_no_edit = (EditText)view.findViewById(R.id.order_no_edit);
		 find_more.setOnClickListener(this);
		setBackVisible();
		initData();
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("快递查询");
	}

	private void initData(){
		if(mCurrentData==null)
		{
			return;
		}
		express_title_tv.setText(mCurrentData.comname);
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
		case R.id.order_query_btn:
			if(checkInput()){
				mRecordTask = new RecordTask();
				mRecordTask.execute("")
				;
			}
//			showChuxuka();
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
//			PromptUtil.showDialog(getActivity(), "");
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				data.putValue("nu", mOrderNo);
				data.putValue("com", mCurrentData.com);
				List<ProtocolData> mDatas = createRequest(data);
				ExpressDetialParser versionParser = new ExpressDetialParser();
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
//					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
//						return;
//					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}
	}

	private List<ProtocolData> createRequest(CommonData data) {
		return ProtocolUtil.getRequestDatas("ApiKuaiDiinfo", "chaxunKuaiDiNo",
				data);
	}

	private void parserResponse(List<ProtocolData> mDatas) {
		mEntity = new ExpressEntity();
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData= response;
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
				List<ProtocolData> apitype = data.find("/apitype");
				if (apitype != null) {
				}
				List<ProtocolData> apiurl = data.find("/apiurl");
				if (apiurl != null) {
				}
				List<ProtocolData> nu = data.find("/nu");
				if (nu != null) {
				}
				List<ProtocolData> com = data.find("/com");
				if (com != null) {
				}
				List<ProtocolData> updatetime = data.find("/updatetime");
				if (updatetime != null) {
				}
				List<ProtocolData> status = data.find("/status");
				if (status != null) {
				}
				
				List<ProtocolData> data1 = data.find("/data");
				if (data1 != null) {
					List<ProtocolData> aupic = data1.get(0).find("/msgchild");
					for(ProtocolData child:aupic){
						ExpressData picData = new ExpressData();
						if (child.mChildren != null && child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							for(String key:keys){
								List<ProtocolData> rs = child.mChildren.get(key);
								for(ProtocolData item: rs){
									if(item.mKey.equals("comid")){
										picData.comid  = item.mValue;
										
									}else if(item.mKey.equals("com")){
										picData.com  = item.mValue;
										
									}else if(item.mKey.equals("comname")){
										
										picData.comname  = item.mValue;
										
									}else if(item.mKey.equals("apitype")){
										
										picData.apitype  = item.mValue;
										
									}else if(item.mKey.equals("comlogo")){
										
										picData.comlogo  = item.mValue;
										
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

}
