package com.inter.trade.ui.fragment.returndaikuan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.TaskData;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.BankRecordListActivity;
import com.inter.trade.ui.CounponActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.DaikuanSwipActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.BankRecordListActivity.TYPECLASS;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.FragmentUtil;
import com.inter.trade.ui.fragment.coupon.CouponBuyFragment;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask.FeeListener;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanfeeParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 还贷款
 * @author apple
 *
 */

public class ReturnDaikuanFragment extends BaseFragment implements OnClickListener,FeeListener{
	private LinearLayout select_bank_layout;
	
	public ReturnDaikuanFragment(){
		
	}
	
	
	private Button cridet_back_btn;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private EditText card_edit_again;
	private EditText money_back;
	private TextView fee_money;
	private LinearLayout fee_layout;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	private static double count =0;
	private static String mCouponId;
	private boolean isSelectedBank=false;
	private boolean isSetFee=false;
	private TaskData mTaskData;
	private CreditCardfeeTask mCardfeeTask;
	private CommonData mfeeData=new CommonData();
	private String paymoney;
	
	private BuyTask mBuyTask;
	private CounponActivity mActivity;
	private String mLastMoney;
	private boolean isComputeFee=false;
	private boolean isNext =false;
	private RelativeLayout record_select_layout;
	
	public static CouponBuyFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new CouponBuyFragment();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
		initData();
		if(getActivity() instanceof CounponActivity){
			 mActivity = (CounponActivity)getActivity();
		}
		mTaskData = new TaskData();
	}
	
	private void initData(){
		DaikuanActivity.mDaikuanData.sunMap.put("authorid", LoginUtil.mLoginStatus.authorid);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.credit_layout, container,false);
		initView(view);
		
		setTitle("还贷款");
		setBackVisible();
		setRightVisible(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentUtil.startFuncActivity(getActivity(), FragmentFactory.Cridet_RECORD_INDEX);
			}
		}, "贷款历史");
		return view;
	}
	
	private void initView(View view){
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
		open_phone_edit = (EditText)view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText)view.findViewById(R.id.open_name_edit);
		card_edit_again = (EditText)view.findViewById(R.id.card_edit_again);
		money_back = (EditText)view.findViewById(R.id.money_back);
		fee_layout = (LinearLayout)view.findViewById(R.id.fee_layout);
		fee_money = (TextView)view.findViewById(R.id.fee_money);
		record_select_layout = (RelativeLayout)view.findViewById(R.id.record_select_layout);
		
		record_select_layout.setOnClickListener(this);
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		fee_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if(checkInput()){
//				showChuxuka();
//				mBuyTask = new BuyTask();
//				mBuyTask.execute("");
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				transaction.replace(R.id.func_container, new ReturnConfirmFirstFragment());
//				transaction.commit();
				Intent intent = new Intent();
				intent.setClass(getActivity(), DaikuanSwipActivity.class);
				getActivity().startActivityForResult(intent, 200);
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;
		case R.id.fee_layout:
			if(checkCompute()){
				getcost();
			}
			break;
		case R.id.record_select_layout:
			showBankRecord();
			break;
		default:
			break;
		}
	}
	private void showBankRecord(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), BankRecordListActivity.class);
		intent.putExtra(BankRecordListActivity.TYPE_KEY_STRING, TYPECLASS.repay);
		startActivityForResult(intent, 0);
	}
	
	private void getcost(){
		PromptUtil.showDialog(getActivity(), "正在计算手续费");
		PromptUtil.dialog.setCancelable(false);
		mCardfeeTask = new CreditCardfeeTask(getActivity(), initTaskData());
		mCardfeeTask.setFeeListener(this);
		mCardfeeTask.execute("");
	}
	
	
	@Override
	public void result(int state, String fee,ArrayList<ArriveData> datas) {
		// TODO Auto-generated method stub
		PromptUtil.dissmiss();
		if(state ==0){
			isSetFee=true;
			setFee(fee);
			if(isNext){
				isNext =false;
				Intent intent = new Intent();
				intent.setClass(getActivity(), DaikuanSwipActivity.class);
				getActivity().startActivityForResult(intent, 200);
			}
		}else{
			isSetFee=false;
//			PromptUtil.showToast(getActivity(), "获取手续费失败");
		}
		
	}


	private boolean checkCompute(){
		if(!isSelectedBank){
			PromptUtil.showToast(getActivity(), "请选择银行");
			return false;
		}
		String moneyString = money_back.getText().toString();
		if(null == moneyString || "".equals(moneyString)){
			PromptUtil.showToast(getActivity(), "请输入还款金额");
			return false;
		}
		paymoney = moneyString;
		mfeeData.sunMap.put("money", moneyString);
		return true;
	}
	private boolean checkNext(){
		if(!isSelectedBank){
			PromptUtil.showToast(getActivity(), "请选择银行");
			return false;
		}
		String moneyString = money_back.getText().toString();
		if(null == moneyString || "".equals(moneyString)){
			PromptUtil.showToast(getActivity(), "请输入还款金额");
			return false;
		}
		return true;
	}
	public void setFee(String fee){
		
		fee_money.setText(NumberFormatUtil.format2(fee)+"元");
		isSetFee=true;
			float cost = Float.parseFloat(fee);
			
			float money = Float.parseFloat(paymoney);
			DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.money, (cost+money)+"");
			Log.d("DaikuanActivity", DaikuanActivity.mDaikuanData.getValue(DaikuanData.money));
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.payfee, fee);
	}
	
	private TaskData initTaskData(){
		TaskData taskData = new TaskData();
		taskData.mCommonData = mfeeData;
		taskData.apiName="ApiPayinfo";
		taskData.funcName="getRepayMoneyPayfee";
		taskData.mNetParser=new DaikuanfeeParser();
		return taskData;
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), BankListActivity.class);
		startActivityForResult(intent, 1);
	}
	
	private boolean checkInput(){
		if(!isSelectedBank){
			PromptUtil.showToast(getActivity(), "请选择银行");
			return false;
		}
		
		
		String cardNumber = card_edit.getText().toString();
		if(null == cardNumber || "".equals(cardNumber)){
			PromptUtil.showToast(getActivity(), "请输入卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBandCid(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		String cardAgain = card_edit_again.getText().toString();
		if(null == cardAgain || "".equals(cardAgain)){
			PromptUtil.showToast(getActivity(), "请再次输入卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBandCid(cardAgain)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.shoucardno, cardNumber);
		
		
		String openName = open_name_edit.getText().toString();
		if(null == openName || "".equals(openName)){
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		
		if(!UserInfoCheck.checkName(openName)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.shoucardman, openName);
		String openPhone = open_phone_edit.getText().toString();
		if(null == openPhone || "".equals(openPhone)){
			PromptUtil.showToast(getActivity(), "请输入电话号码");
			return false;
		}
		
		if(!UserInfoCheck.checkMobilePhone(openPhone)){
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.shoucardmobile, openPhone);
		
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.paycardid, PayApp.mKeyCode);
		if(!cardNumber.equals(cardAgain)){
			PromptUtil.showToast(getActivity(), "两次卡号输入不一致，请重新输入");
			return false;
		}
		String moneyString = money_back.getText().toString();
		if(null == moneyString || "".equals(moneyString)){
			PromptUtil.showToast(getActivity(), "请输入还款金额");
			return false;
		}
		if(checkNext()){
			mLastMoney = mfeeData.sunMap.get("money");
			if(mLastMoney==null || !isSetFee ||!mLastMoney.equals(moneyString)){
//				PromptUtil.showToast(getActivity(), "请点击获取手续费");
				mfeeData.sunMap.put("money", moneyString);
				paymoney = moneyString;
				isNext=true;
				DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.paymoney, moneyString);
				getcost();
				
				return false;
			}
//			else if(){
//				mfeeData.sunMap.put("money", moneyString);
//				getcost();
//				return false;
//			}
		}
		paymoney = moneyString;
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.paymoney, moneyString);
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		payResult(data);
		 if (data == null) {
	            return;
	     }
		 
		 if(resultCode==2){
			 BankRecordData mData = (BankRecordData)data.
					 getSerializableExtra(BankRecordListActivity.BABK_ITEM_VALUE_STRING);
			 if(mData==null){
				 return;
			 }
			 card_edit.setText(mData.shoucardno);
			 card_edit_again.setText(mData.shoucardno);
			 open_name_edit.setText(mData.shoucardman);
			 bank_name.setText(mData.shoucardbank);
			 open_phone_edit.setText(mData.shoucardmobile);
			 
			 isSelectedBank=true;
			 DaikuanActivity.mDaikuanData.putValue(DaikuanData.shoucardbank, mData.shoucardbank);
			 mfeeData.putValue("bankid", mData.bankid);
		 }
		 
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 DaikuanActivity.mDaikuanData.putValue(DaikuanData.shoucardbank, bankname);
			 mfeeData.putValue("bankid", bankid);
			 bank_name.setText(bankname);
		 }
		
		 
		 //银联支付结果
		 
	}
	
	private void payResult(Intent data){
		/*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
		 if (data == null) {
	            return;
	     }
		String msg ="";
        String str = data.getExtras().getString("pay_result");
        if(null == str){
        		return;
        }
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
		if(mCardfeeTask != null){
			mCardfeeTask.cancel(true);
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
		setTitle("还贷款");
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
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, new BuySuccessFragment());
//		transaction.commit();
//		Intent intent = new Intent();
//		intent.putExtra("TN", mBkntno);
//		intent.setClass(getActivity(), UnionpayActivity.class);
//		getActivity().startActivity(intent);
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
//				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiCouponInfo", 
//						"couponSale", mCouponData);
//				CouponBuyParser authorRegParser = new CouponBuyParser();
//				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
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
				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					
				
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						if(getActivity() instanceof CounponActivity){
							((CounponActivity)getActivity()).mBankNo = mBkntno;
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("交易请求号", mBkntno);
							mActivity.mCommonData.add(item);
//							
//							HashMap<String, String> item1 = new HashMap<String, String>();
//							item1.put("抵用券额度", mCouponData.sunMap.get(mCouponData.couponmoney)+"元");
//							mActivity.mCommonData.add(item1);
//							
//							HashMap<String, String> item2 = new HashMap<String, String>();
//							item2.put("付款信用卡", mCouponData.sunMap.get(mCouponData.creditcardno));
//							mActivity.mCommonData.add(item2);
//							
//							HashMap<String, String> item3 = new HashMap<String, String>();
//							item3.put("刷卡金额", mCouponData.sunMap.get(mCouponData.couponmoney)+"元");
//							mActivity.mCommonData.add(item3);
						}
						showChuxuka();
					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
					// TODO: handle exception
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
