package com.inter.trade.ui.fragment.balance;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
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
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 余额查询
 * @author apple
 *
 */

public class BanlanceMainFragment extends BaseFragment implements OnClickListener,SwipListener{
	
	private Button cridet_back_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private TextView acount;
	
	private LinearLayout query_layout;
	private EditText query_input;
	private Button send;
	
	
	
	private String mBankName;
	private String mBankNoString;//银行卡no
	private String mBankIdString;
	
	private String mMessage ="";
	private String mPhoneString =""; 
	private boolean isSelectedBank=false;
	private String mCardNo;
	
	private OrderData mOrderData;
	
	private CommonData mData = new CommonData();
	private String merReserved ="{}";
	private BuyTask mBuyTask;
	public BanlanceMainFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
//		initReader();
		PayApp.mSwipListener = this;
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.balance_new_layout, container,false);
		initView(view);
		
		setTitle("余额查询");
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
		
		query_layout = (LinearLayout)view.findViewById(R.id.query_layout);
		query_input = (EditText)view.findViewById(R.id.query_input);
		send = (Button)view.findViewById(R.id.send);
		
		
		send.setOnClickListener(this);
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		
	}
	private void initSwipPic(boolean flag){
		if(flag){
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		card_edit.setText(data.pan);
		merReserved = "{";
		merReserved += data.firmwareVersion;
		merReserved += "|";
		merReserved += data.encryptionMode;
		merReserved += "|";
		merReserved += data.trackInfo;
		merReserved += "|";
		merReserved += data.encryptionMode;
		merReserved += "|";
		merReserved += data.xxx;
		merReserved += "|";
		merReserved += data.last4Pan;
		merReserved += "|";
		merReserved += data.expiryDate;
		merReserved += "|";
		merReserved += data.userName;
		merReserved += "|";
		merReserved += data.ksn;
		merReserved += "|";
		merReserved += data.encrypedData;
		merReserved += "|";
		merReserved += data.pan;
		merReserved += "|";
		merReserved += data.decrypedData;
		merReserved += "}";
		Log.d("CardInfo", "All:"+merReserved);
		Log.d("CardInfo", "firmwareVersion:"+data.firmwareVersion);
		Log.d("CardInfo", "encryptionMode:"+data.encryptionMode);		
		Log.d("CardInfo", "trackInfo:"+data.trackInfo);
		Log.d("CardInfo", "encryptionMode:"+data.encryptionMode);
		Log.d("CardInfo", "first6Pan:"+data.first6Pan);
		Log.d("CardInfo", "xxx:"+data.xxx);
		Log.d("CardInfo", "last4Pan:"+data.last4Pan);
		Log.d("CardInfo", "expiryDate:"+data.expiryDate);
		Log.d("CardInfo", "userName:"+data.userName);
		Log.d("CardInfo", "ksn:"+data.ksn);
		Log.d("CardInfo", "encrypedData:"+data.encrypedData);
		Log.d("CardInfo", "pan:"+data.pan);
		Log.d("CardInfo", "decrypedData:"+data.decrypedData);		
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
				mBuyTask = new BuyTask();
				mBuyTask.execute("");
//				showDialog();
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;
		case R.id.send:
			sendSms();
			break;
		default:
			break;
		}
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.putExtra("type", "1");
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
			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
			return false;
		}
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		mCardNo = cardNumber;
		mData.putValue("", mCardNo);
		CridetCardFragment.mJournalData.putValue(JournalData.fucardno, cardNumber);
		
		
		
//		if(!isSwipIn){
//			if(null == mKeyCode || "".equals(mKeyCode)){
//				PromptUtil.showToast(getActivity(), "请刷卡");
//			}else{
//				PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			}
//			return false;
//		}
		CridetCardFragment.mJournalData.putValue(JournalData.paycardid, PayApp.mKeyCode);
		CridetCardFragment.mJournalData.putValue(JournalData.merReserved, PayApp.merReserved.toString());
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
		 String bankno = data.getStringExtra(BankListActivity.BANK_NO);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 bank_name.setText(bankname);
			 mBankName= bankname;
			 mBankIdString= bankid;
			 mBankNoString = bankno;
		 }
		
		 
		 //银联支付结果
		 
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		endCallStateService();
//		mobileReader.close();
		PayApp.mSwipListener= null;
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
//		mobileReader.open(false);
		setTitle("余额查询");
		initSwipPic(PayApp.isSwipIn);
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

	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				mData.putValue("bankid", mBankIdString);
				mData.putValue("bankcardno", mCardNo);
				mData.putValue("bankname", mBankName);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAppInfo", 
						"readQueryCardMoney", mData);
				BalanceParser authorRegParser = new BalanceParser();
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
				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						query_layout.setVisibility(View.VISIBLE);
						query_input.setText(mMessage);
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
	private void sendSms(){
		if(query_input.getText().toString()==null ||"".equals(query_input.getText().toString())){
			PromptUtil.showToast(getActivity(), "请输入查询短信");
			return;
		}
//		Uri uri = Uri.parse("smsto:"+mPhoneString);          
//		Intent it = new Intent(Intent.ACTION_SENDTO, uri);          
//		it.putExtra("sms_body", mMessage);          
//		getActivity().startActivity(it);
		//直接调用短信接口发短信
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(mPhoneString, null, mMessage, null, null);  
//		smsManager.sendTextMessage("18664676691", null, mMessage, null, null);  
		PromptUtil.showToast(getActivity(), "短信发送成功，请留意收件箱");
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
				
				List<ProtocolData> smsmsg = data.find("/smsmsg");
				if(smsmsg != null){
					mMessage = smsmsg.get(0).mValue;
				}
				
				List<ProtocolData> smsphone = data.find("/smsphone");
				if(smsphone != null){
					mPhoneString = smsphone.get(0).mValue;
				}
				
			}
		}
	}

	
}
