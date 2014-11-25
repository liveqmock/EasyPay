package com.inter.trade.ui.fragment.wallet;


import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadAccglistdetailtParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.BankListAdapter;
import com.inter.trade.adapter.IncomeAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.MonthDetialData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.imageframe.AsyncTask;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.RecordDetialActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.ui.fragment.cridet.CridetRecordDetialFragment;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 收支详情
 * @author apple
 *
 */

public class DetialMonthFragment extends BaseFragment implements OnItemClickListener{
	private ArrayList<MonthDetialData> monthDatas = new ArrayList<MonthDetialData>();
	private int mTtotalCount =0;//总计N条信息
	private int mLoadCount = 0;//累计加载第N条记录
	
	private ListView mListView;
	private BankListAdapter mAdapter;
	
	private String acctypeid;
	private String accmonth;
	private String accincome;
	private String accpayout;
	private String acctypename;
	
	private static final String ACCTYPEID_STRING="ACCTYPEID_STRING";
	private static final String ACCMONTH_STRING="ACCMONTH_STRING";
	private static final String ACCINCOME_STRING="ACCINCOME_STRING";
	private static final String ACCPAYOUT_STRING="ACCPAYOUT_STRING";
	private static final String ACCTYPENAME_STRING="ACCTYPENAME_STRING";
	
	MonthTask monthTask;
	private TextView month;
	private TextView income;
	private TextView outcome;
	
	public DetialMonthFragment()
	{
		
	}
	
	public static DetialMonthFragment newInstance(String type,String month,String in,String out,String name){
		final DetialMonthFragment fragment = new DetialMonthFragment();
		final Bundle args = new Bundle();
        args.putString(ACCTYPEID_STRING, type);
        args.putString(ACCMONTH_STRING, month);
        args.putString(ACCINCOME_STRING, in);
        args.putString(ACCPAYOUT_STRING, out);
        args.putString(ACCTYPENAME_STRING, name);
        
        fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		acctypeid =getArguments()==null?null:getArguments().getString(ACCTYPEID_STRING);
		accmonth =getArguments()==null?"201310":getArguments().getString(ACCMONTH_STRING);
		accincome =getArguments()==null?"0":getArguments().getString(ACCINCOME_STRING);
		accpayout =getArguments()==null?"0":getArguments().getString(ACCPAYOUT_STRING);
		acctypename = getArguments().getString(ACCTYPENAME_STRING);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.month_detial_layout, container,false);
		mListView = (ListView)view.findViewById(R.id.bank_listview);
		mListView.setOnItemClickListener(this);
		
		month = (TextView)view.findViewById(R.id.month);
		income = (TextView)view.findViewById(R.id.income);
		outcome = (TextView)view.findViewById(R.id.outcome);
		
		month.setText(accmonth);
		income.setText("收入："+accincome);
		outcome.setText("支出："+accpayout);
		
		setBackVisible();
		setTitle(accmonth+"月收支详情");
		monthTask = new MonthTask();
		monthTask.execute("");
		
		return view;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(accmonth+"月收支详情");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		CridetRecordDetialFragment.mMaps.clear();
		MonthDetialData data = monthDatas.get(arg2);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("交易流水号", data.accglistno);
		map.put("类型", data.accgpaymode);
		if(data.accgpaymode.equals("购买抵用券")){
			map.put("付款银行", data.accgcardbank);
			map.put("付款卡号", data.accgcardno);
		}else{
			map.put("充值银行", data.accgcardbank);
			map.put("充值卡号", data.accgcardno);
		}
		
		map.put("金额", data.accglistmoney);
		map.put("交易状态", data.accgstate);
		
		CridetRecordDetialFragment.mMaps.add(map);
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), RecordDetialActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(monthTask != null){
			monthTask.cancel(true);
		}
	}

	private void showChuxuka(){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new BuySuccessFragment());
		transaction.commit();
	}
	
	class MonthTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			UPLOAD_URL = LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				
					List<ProtocolData> mDatas = getRequestDatas();
					ReadAccglistdetailtParser authorRegParser = new ReadAccglistdetailtParser();
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
//					mAdapter = new BankListAdapter(getActivity(), mBankDatas);
					
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					
					mListView.setAdapter(new IncomeAdapter(getActivity(), monthDatas));
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
					MonthDetialData picData = new MonthDetialData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("accglistno")){
									picData.accglistno  = item.mValue;
									
								}else if(item.mKey.equals("accgpaymode")){
									picData.accgpaymode  = item.mValue;
									
								}else if(item.mKey.equals("accglistmoney")){
									
									picData.accglistmoney  = item.mValue;
								}else if(item.mKey.equals("accglistdate")){
									
									picData.accglistdate  = item.mValue;
								}else if(item.mKey.equals("accglistid")){
									
									picData.accglistid  = item.mValue;
								}else if(item.mKey.equals("accgstate")){
									
									picData.accgstate  = item.mValue;
								}else if(item.mKey.equals("accgcardbank")){
									
									picData.accgcardbank  = item.mValue;
								}else if(item.mKey.equals("accgcardno")){
									
									picData.accgcardno  = item.mValue;
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
		data.putValue("accmonth", accmonth);
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAppAccountInfo", 
				"readAccglistdetail", data);
		
		return mDatas;
	}
}
