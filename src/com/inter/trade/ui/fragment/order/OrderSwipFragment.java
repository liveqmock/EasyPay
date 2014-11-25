package com.inter.trade.ui.fragment.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.CridetActivity;
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
 * 刷卡
 * @author apple
 *
 */

public class OrderSwipFragment extends BaseFragment implements OnClickListener,SwipListener{
	
	private Button cridet_back_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private TextView acount;
	
	private TextView order_no_text;
	private TextView order_time_text;
	private TextView order_pay_text;
	
	
	private String mBkntno;
	private String mBankName;
	private String mMessage ="";
	private String mResult =""; 
	private static String feemoney;
	private static String allmoney;
	private static String paymoney;
	private boolean isSelectedBank=false;
	private String mCardNo;
	
	private OrderData mOrderData;
	
	private CridetActivity mActivity;
	
	private String merReserved ="{}";
	public OrderSwipFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			LoginUtil.detection(getActivity());
//			initReader();
			PayApp.mSwipListener = this;
			if(getActivity() instanceof CridetActivity){
				 mActivity = (CridetActivity)getActivity();
			}
			mOrderData = OrderPayFragment.mOrderBean.mOrderDatas.get(OrderPayFragment.selectedIndex);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			PromptUtil.showRealFail(getActivity());
		}
		
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.order_swip_layout, container,false);
		initView(view);
		
		setTitle("刷卡");
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
		
		order_no_text = (TextView)view.findViewById(R.id.order_no_text);
		order_time_text = (TextView)view.findViewById(R.id.order_time_text);
		order_pay_text = (TextView)view.findViewById(R.id.order_pay_text);
		
		order_no_text.setText(mOrderData.orderno);
		order_time_text.setText(mOrderData.ordertime);
		order_pay_text.setText(mOrderData.ordermoney);
		
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		
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
//			startTimer();
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
			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode, PayApp.mKeyData);
			mKeyTask.execute("");
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
				showChuxuka();
				
//				mBuyTask = new BuyTask();
//				mBuyTask.execute("");
//				showDialog();
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
			PromptUtil.showToast(getActivity(), "请刷卡获取卡号");
			return false;
		}
		if(!UserInfoCheck.checkBandCid(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		mCardNo = cardNumber;
		CridetCardFragment.mJournalData.putValue(JournalData.fucardno, cardNumber);
		
		
		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}
		
		if(!PayApp.isSwipIn){
			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
				PromptUtil.showToast(getActivity(), "请刷卡");
			}else{
				PromptUtil.showToast(getActivity(), "请插入刷卡器");
			}
			return false;
		}
		CridetCardFragment.mJournalData.putValue(JournalData.paycardid, PayApp.mKeyCode);
		CridetCardFragment.mJournalData.putValue(JournalData.merReserved, PayApp.merReserved.toString());
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
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 bank_name.setText(bankname);
			 mBankName= bankname;
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
//		endCallStateService();
////		mobileReader.close();
//		closeReader();
//		cancelTimer();
		PayApp.mSwipListener= null;
		if(mKeyTask!=null){
			mKeyTask.cancel(true);
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
//		openReaderNow();
		setTitle("刷卡");
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
	private void initSwipPic(boolean flag){
		if(flag){
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	private void showChuxuka(){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, CridetConfirmFragment.create(
//				feemoney, allmoney, mBkntno));
		transaction.replace(R.id.func_container,OrderConfirmFragment.create(mOrderData,
				mBkntno, mCardNo,mBankName));

		transaction.commit();
//		Intent intent = new Intent();
//		intent.putExtra("TN", mBkntno);
//		intent.setClass(getActivity(), UnionpayActivity.class);
//		getActivity().startActivity(intent);
//		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	
}
