package com.inter.trade.ui.fragment.waterelectricgas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.AgentMainContentActivity;
import com.inter.trade.ui.AgentQueryActivity;
import com.inter.trade.ui.AgentReplenishActivity;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.QMoneyPayRecordActivity;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.QMoneyPayActivity;
import com.inter.trade.ui.WaterElectricGasCitySelectActivity;
import com.inter.trade.ui.WaterElectricGasPayActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyNoParser;
import com.inter.trade.ui.fragment.waterelectricgas.WaterElectricGasMainFragment;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasData;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

@SuppressLint("ValidFragment")
public class WaterElectricGasCompanySelectFragment extends BaseFragment implements OnClickListener{
//	public static ArrayList<String> mItemsTag;
//	public static ArrayList<String> mItems;
//	
//	public static ArrayList<WaterElectricGasData> mDatas;
//	public static ArrayList<ArrayList<String>> mpayType;
//	public static ArrayList<ArrayList<ArrayList<String>>> mCompany;
//	public static ArrayList<String> mCompanyId;

//	private int mCityIndex=0;
//	private int mPayType=0;
//	private String[] mPayTypeList;
	
	private EditText company_edit;
	private EditText client_edit;
	private ImageView image_in;
	private Button ok_btn;
	private RelativeLayout company_select_layout;
	private boolean isSelected = false;
	
	private ArrayList<String> mCompanyList;
	public static ArrayList<String> mCompanyIdList;
	
	private String mBkntno;
	
	private BillTask mBillTask;
	private WaterElectricGasBillData mBillData = new WaterElectricGasBillData();
//	private DaikuanActivity mActivity;
	
	private Bundle bundle;
	private int mPayType=0;
	
	public static WaterElectricGasCompanySelectFragment create(double value,String couponId){
		return new WaterElectricGasCompanySelectFragment();
	}
	
	public WaterElectricGasCompanySelectFragment()
	{
	}
	
	public WaterElectricGasCompanySelectFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		if(getActivity() instanceof DaikuanActivity){
//			 mActivity = (DaikuanActivity)getActivity();
//		}
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.water_electric_gas_pay_select_company_layout, container,false);
		initView(view);
		
		mPayType = WaterElectricGasMainFragment.mPayType;
		String title="";
		switch(mPayType){
		case 1:
			title = "水费";
			break;
		case 2:
			title = "电费";
			break;
		case 3:
			title = "燃气费";
			break;
		default:
			title = "水费";
		}
		setTitle(title);
//		setTitle("水电煤缴费");
		setBackVisible();
//		setRightVisible(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showMore();
//			}
//		}, "历史");
		
		return view;
	}
	
	private void initView(View view){
		company_select_layout = (RelativeLayout)view.findViewById(R.id.company_select_layout);
		company_edit = (EditText)view.findViewById(R.id.company_edit);
		client_edit = (EditText)view.findViewById(R.id.client_edit);
		image_in = (ImageView)view.findViewById(R.id.image_in);
		ok_btn = (Button)view.findViewById(R.id.ok_btn);
		
		image_in.setOnClickListener(this);
		company_edit.setOnClickListener(this);
		company_select_layout.setOnClickListener(this);
		ok_btn.setOnClickListener(this);
		
		mCompanyList = WaterElectricGasMainFragment.mCompanyList;
		mCompanyIdList = WaterElectricGasMainFragment.mCompanyIdList;
		
		if(mCompanyList.size() > 0){
			WaterElectricGasMainFragment.mCompanyIndex = 0;
			company_edit.setText(mCompanyList.get(0));
			isSelected = true;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.image_in://水电煤缴费公司选择
		case R.id.company_edit://水电煤缴费公司选择
		case R.id.company_select_layout://水电煤缴费公司选择
			companyShow();
			break;
		case R.id.ok_btn://确定
			checkToPay();
			break;
		
		default:
			break;
		}
	}
	
	private void checkToPay()
	{
		String company = company_edit.getText().toString();
		String client = client_edit.getText().toString();
		
		if(!isSelected || company == null || "".equals(company)){
			PromptUtil.showToast(getActivity(), "请选择缴费公司");
			return;
			
		}
		if(client == null || "".equals(client)){
			PromptUtil.showToast(getActivity(), "请输入客户编号");
			return;
			
		}
		
		//获取账单信息
		mBillTask = new BillTask();
		mBillTask.execute("");
		
		/*
		Intent intent = new Intent();
		intent.setClass(getActivity(), WaterElectricGasPayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("company", mCompanyList.get(WaterElectricGasMainFragment.mCompanyIndex));
		bundle.putString("companyId", mCompanyIdList.get(WaterElectricGasMainFragment.mCompanyIndex));
//		bundle.putString("company", company_edit.getText().toString());
		bundle.putString("client", client_edit.getText().toString());
//		bundle.putString("factBills", );
//		bundle.putString("totalBill", );
//		bundle.putString("username", );
		intent.putExtra("payInfo", bundle);
		startActivityForResult(intent, 3);
		*/
	}
	
	private void toPay(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), WaterElectricGasPayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("company", mCompanyList.get(WaterElectricGasMainFragment.mCompanyIndex));
		bundle.putString("companyId", mCompanyIdList.get(WaterElectricGasMainFragment.mCompanyIndex));
		bundle.putString("client", client_edit.getText().toString());
		bundle.putString("factBills", mBillData.factBills);
		bundle.putString("totalBill", mBillData.totalBill);
		bundle.putString("username", mBillData.username);
		bundle.putString("orderid", mBillData.orderid);
		intent.putExtra("payInfo", bundle);
		startActivityForResult(intent, 3);
	}
	
	private void companyShow()
	{
//		mCompanyList = WaterElectricGasMainFragment.mCompanyList;
//		mCompanyIdList = WaterElectricGasMainFragment.mCompanyIdList;
		final CharSequence[] items = mCompanyList.toArray(new CharSequence[mCompanyList.size()]);
		//		CharSequence[] items = { "公司1", "公司2" };
		new AlertDialog.Builder(getActivity()).setTitle("选择缴费公司")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						WaterElectricGasMainFragment.mCompanyIndex = which;
						company_edit.setText(items[which]);
						isSelected = true;
						dialog.dismiss();
					}
				}).create().show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) { 
		 case 3:
			 if(resultCode == Activity.RESULT_OK)
			 {
			 }
			 break;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBillTask != null){
			mBillTask.cancel(true);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setTitle("水电煤缴费");
		String title="";
		switch(mPayType){
		case 1:
			title = "水费";
			break;
		case 2:
			title = "电费";
			break;
		case 3:
			title = "燃气费";
			break;
		default:
			title = "水费";
		}
		setTitle(title);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		log("onStop endCallStateService");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		log("onDetach endCallStateService");
	}

	private void showChuxuka(){
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BillTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("account", client_edit.getText().toString());
				data.putValue("proId", mCompanyIdList.get(WaterElectricGasMainFragment.mCompanyIndex));
				
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiUtility", 
						"createOrder", data);
				WaterElectricGasBillParser authorRegParser = new WaterElectricGasBillParser();
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
					parserResoponseBill(mDatas);
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					toPay();
//					late_fees_tv.setText(mBillData.factBills);
//					pay_money_tv.setText(mBillData.totalBill);
//					client_user_tv.setText(mBillData.username);
					
//					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//					}else {
//						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
//					}
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
	private void parserResoponseBill(List<ProtocolData> params){
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
				
				List<ProtocolData> orderid = data.find("/orderid");
				if(orderid != null){
					mBillData.orderid = orderid.get(0).mValue;
				}
				List<ProtocolData> username = data.find("/username");
				if(username != null){
					mBillData.username = username.get(0).mValue;
				}
				List<ProtocolData> factBills = data.find("/factBills");
				if(factBills != null){
					mBillData.factBills = factBills.get(0).mValue;
				}
				List<ProtocolData> totalBill = data.find("/totalBill");
				if(totalBill != null){
					mBillData.totalBill = totalBill.get(0).mValue;
				}
			}
		}
	}
}
