package com.inter.trade.ui.fragment.transfer;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.TaskData;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.ArriveView;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.BankRecordListActivity;
import com.inter.trade.ui.CommonActivity;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.BankRecordListActivity.TYPECLASS;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.TranserRecordActivity;
import com.inter.trade.ui.fragment.AboutFragment;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.FragmentUtil;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask.FeeListener;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanfeeParser;
import com.inter.trade.ui.fragment.transfer.util.TransferData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

public class TransferFragment extends BaseFragment implements OnClickListener,FeeListener,SwipListener{
	private Button cridet_back_btn;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private EditText card_edit_again;
	private EditText  beizhu;
	private CheckBox protocol_ck;
	private RadioButton next_radio;
	private RadioButton realtime_radio;
	private TextView money_back;
	private TextView transfer_cost;
	private ImageView swip_card;
	private ImageView swip_card_again;
	
	private boolean hasSwipListener=false;
	
	private boolean isSelectedBank=false;
	private CreditCardfeeTask mCardfeeTask;
	public CommonData mfeeData=new CommonData();
	
	private LinearLayout cost_container;
	private LinearLayout cost_layout;
	private LinearLayout phone_layout;
	private View phone_line;
	private boolean isDestrory=false;
	private TextView transferProtocol;
	
	private ArrayList<ArriveView> mViews  = new ArrayList<ArriveView>();
	
	private boolean isSetFee = false;
	/***转账金额*/
	private String mLastMoney="";
	private boolean isNext = false;
	
	/**是否获取手续费成功*/
	public boolean isFeeSuccess=false;
	
	private CheckBox ckSms;
	private RelativeLayout record_select_layout;
	private String mType ="";
	public static final String TYPE_STRING="TRANSFER_TYPE_KEY";
	
	public static TransferFragment instance(String type){
		TransferFragment fragment = new TransferFragment();
		Bundle argBundle = new Bundle();
		argBundle.putString(TYPE_STRING, type);
		fragment.setArguments(argBundle);
		return fragment;
		
	}
	
	public TransferFragment(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		setTitle("转账汇款");
//		setBackVisible();
		
		mType = getArguments().getString(TYPE_STRING)==null?TransferType.USUAL_TRANSER:getArguments().getString(TYPE_STRING);
		if(mType.equals(TransferType.USUAL_TRANSER)){
			setTitle(getString(R.string.zhuangzhang_title));
		}else{
			setTitle(getString(R.string.suptfmg_title));
		}
		
//		setRightVisible(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), TranserRecordActivity.class);
//				intent.putExtra(TransferRecordFragment.TYPE_STRING, mType);
//				getActivity().startActivityForResult(intent, 200);
////				FragmentUtil.startFuncActivity(getActivity(), FragmentFactory.TRANSFER_RECORD_INDEX);
//			}
//		},"转账历史");
		initData();
	}
	
	
	private void initData(){
		CommonActivity.mTransferData.putValue("authorid", LoginUtil.mLoginStatus.authorid);
		CommonActivity.mTransferData.putValue(TransferData.sendsms, "0");
		CommonActivity.mTransferData.putValue(TransferData.current, "RMB");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setTitle("转账汇款");
		
		PayApp.mSwipListener = TransferFragment.this;
		initSwipPic(PayApp.isSwipIn);
	}
	
	public void setmSwipListener(TransferFragment tf){
		PayApp.mSwipListener = tf;
	}
	
	
	private void initView(View view){
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_card_again = (ImageView)view.findViewById(R.id.swip_card_again);
		
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
		open_phone_edit = (EditText)view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText)view.findViewById(R.id.open_name_edit);
		card_edit_again = (EditText)view.findViewById(R.id.card_edit_again);
		protocol_ck =(CheckBox)view.findViewById(R.id.protocol_ck);
		transferProtocol = (TextView)view.findViewById(R.id.transferProtocol);
		transferProtocol.setOnClickListener(this);
//		next_radio =(RadioButton)view.findViewById(R.id.next_radio);
//		realtime_radio = (RadioButton)view.findViewById(R.id.realtime_radio);
		transfer_cost = (TextView)view.findViewById(R.id.transfer_cost);
		transfer_cost.setOnClickListener(this);
		phone_layout = (LinearLayout)view.findViewById(R.id.phone_layout);
		phone_line = view.findViewById(R.id.phone_line);
		ckSms=(CheckBox)view.findViewById(R.id.ckSms);
		CommonActivity.mTransferData.putValue(TransferData.sendsms, "0");
		ckSms.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
//					phone_layout.setVisibility(View.VISIBLE);
//					phone_line.setVisibility(View.VISIBLE);
					CommonActivity.mTransferData.putValue(TransferData.sendsms, "1");
				}else {
//					phone_line.setVisibility(View.GONE);
//					phone_layout.setVisibility(View.GONE);
					CommonActivity.mTransferData.putValue(TransferData.sendsms, "0");
				}
			}
		});
		beizhu = (EditText)view.findViewById(R.id.beizhu);
		money_back = (EditText)view.findViewById(R.id.money_back);
//		money_back.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if(!hasFocus){
//					isNext = false;
////					PromptUtil.showToast(getActivity(), "转账金额为"+money_back.getText().toString());
//					getCost();
//				}
//			}
//		});
		
		cost_container = (LinearLayout)view.findViewById(R.id.cost_container);
		cost_layout = (LinearLayout)view.findViewById(R.id.cost_layout);
		record_select_layout = (RelativeLayout)view.findViewById(R.id.record_select_layout);
		
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		record_select_layout.setOnClickListener(this);
//		transfer_cost.setOnClickListener(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		Log.i("TransferFragment", "onCreateView:"+mType);
		View view = inflater.inflate(R.layout.zhuanzhang_layout, container,false);
		initView(view);
		return view;
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if(checkInput()){
				Intent intent = new Intent();
				intent.putExtra(TransferRecordFragment.TYPE_STRING, mType);
				intent.setClass(getActivity(), CommonActivity.class);
//				startActivity(intent);
				getActivity().startActivityForResult(intent, 0);
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;
		case R.id.transfer_cost:
			if(checkCompute(true)){
				mCardfeeTask = new CreditCardfeeTask(getActivity(), initTaskData());
				mCardfeeTask.setFeeListener(this);
				mCardfeeTask.execute("");
			}
			break;
		case R.id.transferProtocol:
			AboutFragment.mProtocolType="5";
			showProtocol();
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
		if(mType.equals(TransferType.USUAL_TRANSER)){
			intent.putExtra(BankRecordListActivity.TYPE_KEY_STRING, TYPECLASS.tfmg);
		}else{
			intent.putExtra(BankRecordListActivity.TYPE_KEY_STRING, TYPECLASS.suptfmg);
		}
		
		startActivityForResult(intent, 0);
	}
	private void showProtocol(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.PROTOCOL_LIST_INDEX);
		getActivity().startActivity(intent);
	}
	
	private void getCost(){
		try {
			if(isDestrory){
				return;
			}
			if(checkCompute(false)){
				if(!getActivity().isRestricted()){
					PromptUtil.showDialog(getActivity(), "正在计算手续费");
					PromptUtil.dialog.setCancelable(false);
					mCardfeeTask = new CreditCardfeeTask(getActivity(), initTaskData());
					mCardfeeTask.setFeeListener(this);
					mCardfeeTask.execute("");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	@Override
	public void result(int state, String fee,ArrayList<ArriveData> datas) {
		// TODO Auto-generated method stub
		PromptUtil.dissmiss();
		if(state ==0){
			setFee(fee);
			isFeeSuccess =true;
			CommonActivity.mTransferData.putValue(TransferData.payfee	, fee.replace("元", ""));
			cost_container.removeAllViews();
			mViews.clear();
			for(ArriveData data : datas){
				ArriveView view = new ArriveView(getActivity(), data,this);
				cost_container.addView(view.mView);
				mViews.add(view);
			}
			if(isNext){
				isNext = false;
				Intent intent = new Intent();
				intent.putExtra(TransferRecordFragment.TYPE_STRING, mType);
				intent.setClass(getActivity(), CommonActivity.class);
				startActivity(intent);
			}
		}else{
			isFeeSuccess =false;
//			PromptUtil.showToast(getActivity(), "获取手续费失败");
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isDestrory =true;
		money_back.setOnFocusChangeListener(null);
		if(mCardfeeTask!= null){
			mCardfeeTask.cancel(true)
			;
		}
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PayApp.mSwipListener= null;
	}
	public void initArrive(){
		if(mViews.size()>1){
			for(ArriveView view : mViews){
				if(!view.isSelected){
					view.setChecked(false);
				}
			}
		}
		getCost();
	}
	public void setFee(String fee){
		
		transfer_cost.setText(NumberFormatUtil.format2(fee)+"元");
		isSetFee =true;
//			int cost = Integer.parseInt(fee);
//			int money = Integer.parseInt(paymoney);
//			DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.money, (cost+money)+"");
//			Log.d("DaikuanActivity", DaikuanActivity.mDaikuanData.getValue(DaikuanData.money));
//		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.payfee, fee);
	}
	
	private TaskData initTaskData(){
		TaskData taskData = new TaskData();
		taskData.mCommonData = mfeeData;
		taskData.apiName="ApiPayinfo";
		if(mType.equals(TransferType.USUAL_TRANSER)){
			taskData.funcName="getTransferPayfee";
		}else{
			taskData.funcName="getSupTransferPayfee";
		}
		
		taskData.mNetParser=new DaikuanfeeParser();
		return taskData;
	}
	private boolean checkCompute(boolean isPrompt){
		if(!isSelectedBank){
			if(isPrompt)
			PromptUtil.showToast(getActivity(), "请选择银行");
			return false;
		}
		String moneyString = money_back.getText().toString();
		if(null == moneyString || "".equals(moneyString)){
			if(isPrompt)
			PromptUtil.showToast(getActivity(), "请输入还款金额");
			return false;
		}
		mfeeData.sunMap.put("money", moneyString);
		return true;
	}
	
	
	private boolean checkNext(){
		if(!isSelectedBank){
//			PromptUtil.showToast(getActivity(), "请选择银行");
			return false;
		}
		String moneyString = money_back.getText().toString();
		if(null == moneyString || "".equals(moneyString)){
//			PromptUtil.showToast(getActivity(), "请输入还款金额");
			return false;
		}
		return true;
	}
//	TextWatcher mTextWatcher = new TextWatcher() {
//        private CharSequence temp;
//        @Override
//        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
//                int arg3) {
//            temp = s;
//        }
//       
//        @Override
//        public void onTextChanged(CharSequence s, int arg1, int arg2,
//                int arg3) {
//        }
//       
//        @Override
//        public void afterTextChanged(Editable s) {
//        		PromptUtil.showToast(getActivity(), "转账金额为"+s.toString());
//        }
//        
//    };
	
	public void initSwipPic(boolean flag){
		if(flag){
//			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_enable));
			swip_card_again.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_enable));
		}else{
//			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_card_bg));
			swip_card_again.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		card_edit.setText(data.pan);
		card_edit_again.setText(data.pan);
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			boolean s = PayApp.openReaderNow();
//			startTimer();
			log("status : "+s);
//			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_enable));
			swip_card_again.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_enable));
		}else{
//			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_card_bg));
			swip_card_again.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.swip_card_bg));
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
		String cardAgain = card_edit_again.getText().toString();
		if(null == cardAgain || "".equals(cardAgain)){
			PromptUtil.showToast(getActivity(), "请再次输入卡号");
			return false;
		}
		if(!cardNumber.equals(cardAgain)){
			PromptUtil.showToast(getActivity(), "两次卡号输入不一致，请重新输入");
			return false;
		}
		
		if(!UserInfoCheck.checkBankCard(cardAgain)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		CommonActivity.mTransferData.putValue(TransferData.shoucardno, cardNumber);
		
		
		String openName = open_name_edit.getText().toString();
		if(null == openName || "".equals(openName)){
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		if(!UserInfoCheck.checkName(openName)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		CommonActivity.mTransferData.putValue(TransferData.shoucardman, openName);
		if(ckSms.isChecked()){
			String openPhone = open_phone_edit.getText().toString();
			if(null == openPhone || "".equals(openPhone)){
				PromptUtil.showToast(getActivity(), "请输入电话号码");
				return false;
			}
			 
			
			if(!UserInfoCheck.checkMobilePhone(openPhone)){
				PromptUtil.showToast(getActivity(), "手机号码格式不正确");
				return false;
			}
			CommonActivity.mTransferData.putValue(TransferData.shoucardmobile, openPhone);
		}else{
			CommonActivity.mTransferData.putValue(TransferData.shoucardmobile, "");
		}
		
		
		
		CommonActivity.mTransferData.putValue(TransferData.paycardid, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
		
//		if(next_radio.isChecked()){
//			CommonActivity.mTransferData.putValue(TransferData.sendsms, "1");
//		}else{
//			CommonActivity.mTransferData.putValue(TransferData.sendsms, "0");
//		}
		
		String bak = beizhu.getText().toString();
		if(null == bak||"".equals(bak)){
			CommonActivity.mTransferData.putValue(TransferData.shoucardmemo, "");
		}else {
			CommonActivity.mTransferData.putValue(TransferData.shoucardmemo, bak);
		}
		
		String money = money_back.getText().toString();
		if(null == money || "".equals(money)){
			PromptUtil.showToast(getActivity(), "请输入转账金额");
			return false;
		}
		CommonActivity.mTransferData.putValue(TransferData.paymoney, money);
		
		
//		CommonActivity.mTransferData.putValue(TransferData.arriveid, "1");
		if(!protocol_ck.isChecked())
		{
			PromptUtil.showToast(getActivity(), "请确认已经阅读通付宝协议");
			return false;
		}
//		if(!isSetFee){
			if(checkNext()){
				mLastMoney = mfeeData.sunMap.get("money");
				
				if(mLastMoney == null ||!mLastMoney.equals(money) || !isFeeSuccess){
					isNext = true;
					getCost();
					return false;
				}
			}
			
//		}
			float totalMoney = Float.parseFloat(CommonActivity.mTransferData.getValue(TransferData.paymoney))
					+Float.parseFloat(CommonActivity.mTransferData.getValue(TransferData.payfee));
			CommonActivity.mTransferData.putValue(TransferData.money, totalMoney+"");
			Logger.d("Transfer",CommonActivity.mTransferData.sunMap.keySet().toString());
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 if (data == null) {
	            return;
	     }
		 //选择历史纪录
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
			 CommonActivity.mTransferData.putValue(TransferData.shoucardbank, mData.shoucardbank);
			 //计算手续费
			 mfeeData.putValue("bankid", mData.bankid);
			 getCost();
		 }
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 //选择银行
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 bank_name.setText(bankname);
			 CommonActivity.mTransferData.putValue(TransferData.shoucardbank, bankname);
			 //计算手续费
			 mfeeData.putValue("bankid", bankid);
			 getCost();
		 }
		 
		 
		 //银联支付结果
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), BankListActivity.class);
		startActivityForResult(intent, 1);
	}
	
	public static class TransferType{
		public static final String USUAL_TRANSER="USUAL_TRANSER";//转账汇款
		public static final String SUPER_TRANSER="SUPER_TRANSER";//超级转账
	}
}
