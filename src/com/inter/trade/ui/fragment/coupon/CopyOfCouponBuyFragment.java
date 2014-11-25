package com.inter.trade.ui.fragment.coupon;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.CounponActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.parser.CouponBuyParser;
import com.inter.trade.ui.fragment.coupon.util.CouponData;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

public class CopyOfCouponBuyFragment extends BaseFragment implements OnClickListener,SwipListener{
	private Button cridet_back_btn;
	private CouponData mCouponData;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private TextView acount;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	private static double count =0;
	private static String mCouponId;
	private boolean isSelectedBank=false;
	
	private BuyTask mBuyTask;
	private CounponActivity mActivity;
	
	public static CopyOfCouponBuyFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new CopyOfCouponBuyFragment();
	}
	
	public CopyOfCouponBuyFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
//		initReader();
		PayApp.mSwipListener = this;
		initData();
		if(getActivity() instanceof CounponActivity){
			 mActivity = (CounponActivity)getActivity();
		}
		SwipKeyTask.showDialog(getActivity(), DECODING);
		
	}
	
	private void initData(){
		mCouponData = new CouponData();
		mCouponData.sunMap.put("authorid", LoginUtil.mLoginStatus.authorid);
		mCouponData.sunMap.put(mCouponData.couponid, mCouponId);
		mCouponData.sunMap.put(mCouponData.couponmoney, count+"");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.coupon_cridet_layout, container,false);
		initView(view);
		
		setTitle("确认付款");
		setBackVisible();
//		openReader();
		return view;
	}
	
	private void initView(View view){
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_prompt = (TextView)view.findViewById(R.id.swip_prompt);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
		acount = (TextView)view.findViewById(R.id.acount);
		open_phone_edit = (EditText)view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText)view.findViewById(R.id.open_name_edit);
		
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		
		acount.setText(count+"元");
	}
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		card_edit.setText(data.pan);
		
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			boolean s = PayApp.openReaderNow();
			log("status : "+s);
			
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			SwipKeyTask.showDialog(getActivity(), ReaderError);
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	

	@Override
	public void progress(int status, String message) {
		// TODO Auto-generated method stub
		switch (status) {
		case SWIPING_START:
			PromptUtil.showToast(getActivity(), message);
			break;
		case SWIPING_FAIL:
			PromptUtil.showToast(getActivity(), message);
					break;
		case SWIPING_SUCCESS:
			PromptUtil.showToast(getActivity(), message);
			
			break;
		case CMD_KSN:
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if(checkInput()){
//				showChuxuka();
				mBuyTask = new BuyTask();
				mBuyTask.execute("");
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;

		default:
			break;
		}
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
			PromptUtil.showToast(getActivity(), "请刷卡");
			return false;
		}
		
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		mCouponData.sunMap.put(mCouponData.creditcardno, cardNumber);
		
		
		String openName = open_name_edit.getText().toString();
		if(null == openName || "".equals(openName)){
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		if(!UserInfoCheck.checkName(openName)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		
		mCouponData.sunMap.put(mCouponData.creditcardman, openName);
		String openPhone = open_phone_edit.getText().toString();
		if(null == openPhone || "".equals(openPhone)){
			PromptUtil.showToast(getActivity(), "请输入电话号码");
			return false;
		}
		if(!UserInfoCheck.checkMobilePhone(openPhone)){
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		mCouponData.sunMap.put(mCouponData.creditcardphone, openPhone);
		
//		isSwipIn = true;
//		if(!PayApp.isSwipIn){
//			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
//				PromptUtil.showToast(getActivity(), "请刷卡");
//			}else{
//				PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			}
//			return false;
//		}
		mCouponData.sunMap.put(mCouponData.paycardid, PayApp.mKeyCode);
		mCouponData.sunMap.put(CouponData.merReserved, Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 if (data == null) {
	            return;
	     }
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 mCouponData.sunMap.put(mCouponData.creditbank, bankname);
		 }
		 bank_name.setText(bankname);
		 
		 //银联支付结果
		 
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		endCallStateService();
//		mobileReader.close();
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mobileReader.close();
//		endCallStateService();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		mobileReader.open();
//		startCallStateService();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		mobileReader.close();
//		endCallStateService();
		log("onStop endCallStateService");
	}

	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
//		mobileReader.close();
//		endCallStateService();
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
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiCouponInfo", 
						"couponSale", mCouponData);
				CouponBuyParser authorRegParser = new CouponBuyParser();
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
				PromptUtil.showToast(getActivity(), mActivity.getResources().getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
				
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						if(getActivity() instanceof CounponActivity){
							((CounponActivity)getActivity()).mBankNo = mBkntno;
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("订单号", mBkntno);
							mActivity.mCommonData.add(item);
							
							HashMap<String, String> item1 = new HashMap<String, String>();
							item1.put("抵用券额度", mCouponData.sunMap.get(mCouponData.couponmoney)+"元");
							mActivity.mCommonData.add(item1);
							
							HashMap<String, String> item2 = new HashMap<String, String>();
							item2.put("付款信用卡", mCouponData.sunMap.get(mCouponData.creditcardno));
							mActivity.mCommonData.add(item2);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("刷卡金额", mCouponData.sunMap.get(mCouponData.couponmoney)+"元");
							mActivity.mCommonData.add(item3);
						}
						showChuxuka();
					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(getActivity(),getString(R.string.net_error));
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
