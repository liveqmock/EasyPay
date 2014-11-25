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
import android.text.InputType;
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
import com.inter.trade.R.string;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
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
import com.inter.trade.ui.WaterElectricGasCityRecordActivity;
import com.inter.trade.ui.WaterElectricGasCitySelectActivity;
import com.inter.trade.ui.WaterElectricGasCompanySelectActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyNoParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasData;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

@SuppressLint("ValidFragment")
public class WaterElectricGasMainFragment extends BaseFragment implements OnClickListener{
	public static ArrayList<String> mItemsTag;
	public static ArrayList<String> mItems;
	
	public static ArrayList<WaterElectricGasData> mDatas;
//	public static ArrayList<ArrayList<String>> mpayType;
//	public static ArrayList<ArrayList<ArrayList<String>>> mCompany;
//	public static ArrayList<String> mCompanyId;

	private int mCityIndex=0;
	public static int mPayType=0;
	private String mCityName;
	
	/**
	 * 水电煤图标0灰色不可选，1彩色可选，初始化0
	 */
	private int[] mPay ={0,0,0};
	
	private ArrayList<Integer> mPayTypeList;
	public static ArrayList<String> mCompanyList;
	public static ArrayList<String> mCompanyIdList;
	public static int mCompanyIndex=0;
	private boolean isSelected = false;
	
	private EditText city_edit;
	private ImageView image_in, notes_img;
	private ImageButton water_pay_imgbtn, electric_pay_imgbtn, gas_pay_imgbtn;
	private TextView notes_tv;
	private RelativeLayout city_select_layout;
	private LinearLayout notes_layout;
	
	private String mBkntno;
	
	private BuyTask mBuyTask;
//	private DaikuanActivity mActivity;
	
	private Bundle bundle;
	
	public static WaterElectricGasMainFragment create(double value,String couponId){
		return new WaterElectricGasMainFragment();
	}
	
	public WaterElectricGasMainFragment()
	{
	}
	
	public WaterElectricGasMainFragment(Bundle b) {
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
		View view = inflater.inflate(R.layout.water_electric_gas_pay_layout, container,false);
		initView(view);
		
		setTitle("水电煤缴费");
		setBackVisible();
		setRightVisible(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMore();
			}
		}, "历史记录");
		
		return view;
	}
	
	public void showMore() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), WaterElectricGasCityRecordActivity.class);
		startActivityForResult(intent, 6);
	}
	
	
	private void initView(View view){
		city_select_layout = (RelativeLayout)view.findViewById(R.id.city_select_layout);
		city_edit = (EditText)view.findViewById(R.id.city_edit);
		image_in = (ImageView)view.findViewById(R.id.image_in);
		
		water_pay_imgbtn = (ImageButton)view.findViewById(R.id.water_pay_imgbtn);
		electric_pay_imgbtn = (ImageButton)view.findViewById(R.id.electric_pay_imgbtn);
		gas_pay_imgbtn = (ImageButton)view.findViewById(R.id.gas_pay_imgbtn);
		
		notes_img = (ImageView)view.findViewById(R.id.notes_img);
		notes_tv = (TextView)view.findViewById(R.id.notes_tv);
		notes_layout = (LinearLayout)view.findViewById(R.id.notes_layout);
		
		
		//禁止软键盘
		city_edit.setInputType(InputType.TYPE_NULL);
		
//		//开启软键盘
//		city_edit.setInputType(InputType.TYPE_CLASS_TEXT);
		
		image_in.setOnClickListener(this);
		city_edit.setOnClickListener(this);
		city_select_layout.setOnClickListener(this);
		water_pay_imgbtn.setOnClickListener(this);
		electric_pay_imgbtn.setOnClickListener(this);
		gas_pay_imgbtn.setOnClickListener(this);
		notes_img.setOnClickListener(this);
		notes_tv.setOnClickListener(this);
		notes_layout.setOnClickListener(this);
		
		water_pay_imgbtn.setEnabled(false);
		electric_pay_imgbtn.setEnabled(false);
		gas_pay_imgbtn.setEnabled(false);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.city_select_layout://水电煤缴费所在城市选择
		case R.id.city_edit://水电煤缴费所在城市选择
		case R.id.image_in://水电煤缴费所在城市选择
			citySelect();
			break;
		case R.id.water_pay_imgbtn://水费
			mPayType=1;
			companySelect();
//			city_edit.setText("水费");
			break;
		case R.id.electric_pay_imgbtn://电费
			mPayType=2;
			companySelect();
//			city_edit.setText("电费");
			break;
		case R.id.gas_pay_imgbtn://燃气费
			mPayType=3;
			companySelect();
//			city_edit.setText("燃气费");
			break;
		case R.id.notes_img://点击查看注意事项
		case R.id.notes_tv://点击查看注意事项
		case R.id.notes_layout://点击查看注意事项
			showNotes();
			break;
			
		
		default:
			break;
		}
	}
	
	private void showNotes()
	{
		String note = "1、不支持预付费区域（后付费）：水电煤缴费金额必须等于欠费金额，低于或大于欠费金额则有可能显示不成功；而支持预付费区域（预付费），缴费金额必须等于或大于欠费金额。\n" +
				"2、核对扣款账号与扣款记录，若因客户输入缴费号错误，公司不承担客户损失。\n" +
				"3、请用户在帐单的有效期内进行缴付，如因超过账单有效期而未能缴付成功的，我公司不承担相关责任，建议客户到相关营业厅进行处理。\n" +
				"4、公用事业单位的网上托管数据会迟于纸质账单发出，并且在发送途中也会出现数据包丢失，而导致要重新传输。因此当查询不到账单时，请不用着急，可隔几天重试，只要在最后缴费日前查询到即可。\n" +
				"5、缴费时请输入正确的账单编号，并确认缴费信息与所要缴付的账单信息是否一致。如因用户输错账单编号造成的各类损失，我公司不予承担。\n";
		
		new AlertDialog.Builder(getActivity()).setTitle("注意事项")
				.setMessage(note).create().show();
	}
	
	private void citySelect()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), WaterElectricGasCitySelectActivity.class);
		startActivityForResult(intent, 3);
	}
	
	private void companySelect()
	{
		String city = city_edit.getText().toString();
		if(!isSelected || city == null || "".equals(city)){
			PromptUtil.showToast(getActivity(), "请选择所在城市");
			return;
		}
		for(int n=0; n<mPayTypeList.size(); n++){
			if(mPayType == mPayTypeList.get(n)){
				mCompanyList = mDatas.get(mCityIndex).companyLList.get(n);
				mCompanyIdList = mDatas.get(mCityIndex).companyIdLList.get(n);
				break;
			}
		}
//		mCompanyList = mDatas.get(mCityIndex).companyLList.get(mPayType);
		Intent intent = new Intent();
		intent.setClass(getActivity(), WaterElectricGasCompanySelectActivity.class);
		startActivityForResult(intent, 4);
	}
	
	private boolean checkInput(){
		
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) { 
		 case 3:
			 if(resultCode == Activity.RESULT_OK)
			 {
				 String city = data.getStringExtra("cityName");
				 if(city != null && !"".equals(city)){
					 city_edit.setText(city);
					 isSelected = true;
				 }
				 
				 mCityIndex = data.getIntExtra("cityIndex", 0);
				 mPayTypeList = mDatas.get(mCityIndex).payTypeList;
				 mPay[0]=0;
				 mPay[1]=0;
				 mPay[2]=0;
				 for(int n=0; n<mPayTypeList.size(); n++){
					if(mPayTypeList.get(n) == 1){
						water_pay_imgbtn.setEnabled(true);
						mPay[0]=1;
					}
					else if(mPayTypeList.get(n) == 2){
						electric_pay_imgbtn.setEnabled(true);
						mPay[1]=1;
					}
					else if(mPayTypeList.get(n) == 3){
						gas_pay_imgbtn.setEnabled(true);
						mPay[2]=1;
					}
				 }
				 
				if(mPay[0]!=1){
					water_pay_imgbtn.setEnabled(false);
				}
				if(mPay[1]!=1){
					electric_pay_imgbtn.setEnabled(false);
				}
				if(mPay[2]!=1){
					gas_pay_imgbtn.setEnabled(false);
				}
				 
//				 mCompanyLList = mDatas.get(mCityIndex).companyLList;
				 
//				 PromptUtil.showToast(getActivity(), "city="+city );
//				 log("cityName="+city);
				
			 }
			 break;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBuyTask != null){
			mBuyTask.cancel(true);
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
		setTitle("水电煤缴费");
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
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiQQRechangeInfo", 
						"RechaMoneyRq", QMoneyPayActivity.moblieRechangeData);
				QMoneyNoParser authorRegParser = new QMoneyNoParser();
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
						QMoneyPayActivity.mCommonData.clear();
						QMoneyPayActivity.mBankNo = mBkntno;
							
							HashMap<String, String> item5 = new HashMap<String, String>();
							item5.put("充值号码:", QMoneyPayActivity.moblieRechangeData.getValue(QMoneyData.MRD_RECHAMOBILE));
							QMoneyPayActivity.mCommonData.add(item5);
							
							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("充值金额:", NumberFormatUtil.format2(QMoneyPayActivity.moblieRechangeData.getValue(QMoneyData.MRD_RECHAMONEY)));
							QMoneyPayActivity.mCommonData.add(item6);
							
							HashMap<String, String> item7 = new HashMap<String, String>();
							item7.put("实际支付金额:", NumberFormatUtil.format2(QMoneyPayActivity.moblieRechangeData.getValue(QMoneyData.MRD_RECHAPAYMONEY)));
							QMoneyPayActivity.mCommonData.add(item7);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("刷卡卡号:", QMoneyPayActivity.moblieRechangeData.getValue(QMoneyData.MRD_RECHABKCARDNO));
							QMoneyPayActivity.mCommonData.add(item3);
							
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("交易请求号:", mBkntno);
							QMoneyPayActivity.mCommonData.add(item);
						showChuxuka();
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
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno != null){
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}
}
