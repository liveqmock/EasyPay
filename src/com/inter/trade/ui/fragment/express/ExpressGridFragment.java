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
import android.widget.Button;
import android.widget.GridView;

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
import com.inter.trade.ui.fragment.express.util.EpressGridAdapter;
import com.inter.trade.ui.fragment.express.util.ExpressData;
import com.inter.trade.ui.fragment.express.util.ExpressListParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;



public class ExpressGridFragment extends BaseFragment implements OnClickListener,OnItemClickListener{
	private Button find_more;
	public static  ArrayList<ExpressData> mList = new ArrayList<ExpressData>();
	private GridView express_gridview; 
	private ExpressTask mRecordTask;
	public static ExpressData mExpressData;
	
	
	private ArrayList<ExpressData> datas;
	public ExpressGridFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.express_title));
		mRecordTask = new ExpressTask();
		LoginUtil.detection(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.express_grid_layou, container, false);
		express_gridview = (GridView) view.findViewById(R.id.express_gridview);
		 find_more = (Button)view.findViewById(R.id.find_more);
		 find_more.setOnClickListener(this);
		setBackVisible();
		express_gridview.setOnItemClickListener(this);
		mRecordTask.execute("");
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(getString(R.string.express_title));
		setRightVisible(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mList.size()>0){
					Intent intent = new Intent();
					intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.EXPRESS_LIST_INDEX);
					intent.setClass(getActivity(), FunctionActivity.class);
					getActivity().startActivity(intent);
				}else{
					if(getActivity()==null){
						return;
					}
					PromptUtil.showToast(getActivity(), "暂无数据");
				}
			}
		}, "查看更多");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.EXPRESS_QUERY_INDEX);
		intent.setClass(getActivity(), FunctionActivity.class);
		mExpressData = datas.get(arg2);
		getActivity().startActivity(intent);
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
		case R.id.find_more:
			if(mList.size()>0){
				Intent intent = new Intent();
				intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.EXPRESS_LIST_INDEX);
				intent.setClass(getActivity(), FunctionActivity.class);
				getActivity().startActivity(intent);
			}else{
				if(getActivity()==null){
					return;
				}
				PromptUtil.showToast(getActivity(), "暂无数据");
			}
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

	public class ExpressTask extends AsyncTask<String, Integer, Boolean> {
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
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				List<ProtocolData> mDatas = createRequest(data);
				ExpressListParser versionParser = new ExpressListParser();
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
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, mActivity.getResources().getString(R.string.loading));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					datas=new ArrayList<ExpressData>();
					for(ExpressData e:mList){
						if("顺丰".equals(e.comname)||"申通".equals(e.comname)||"圆通".equals(e.comname)||"韵达".equals(e.comname)
								||"EMS".equals(e.comname)||"天天".equals(e.comname)||"速尔快递".equals(e.comname)||"汇通".equals(e.comname)
								||"德邦物流".equals(e.comname)||"中通".equals(e.comname)||"全峰".equals(e.comname)||"TNT快递".equals(e.comname)){
							datas.add(e);
						}
					}
					
					
					express_gridview.setAdapter(new EpressGridAdapter(getActivity(), datas));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}
	}

	private List<ProtocolData> createRequest(CommonData data) {
		return ProtocolUtil.getRequestDatas("ApiKuaiDiinfo", "readKuaiDicmpList",
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
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				//每次先清空，防止叠加
				mList.clear();
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
