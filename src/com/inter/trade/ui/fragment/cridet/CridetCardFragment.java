package com.inter.trade.ui.fragment.cridet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.CardData;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.BankRecordListActivity;
import com.inter.trade.ui.BankRecordListActivity.TYPECLASS;
import com.inter.trade.ui.CounponActivity;
import com.inter.trade.ui.CridetPayActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.fragment.BankListFragment;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.FragmentUtil;
import com.inter.trade.ui.fragment.coupon.CouponBuyFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 还信用卡
 * @author apple
 *
 */

public class CridetCardFragment extends BaseFragment implements OnClickListener,SwipListener
	,AsyncLoadWorkListener{
	private Button cridet_back_btn;
	public static  JournalData mJournalData = new JournalData();
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private TextView acount;
	private EditText money_back;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	private static double count =0;
	private static String mCouponId;
	private boolean isSelectedBank=false;
	
	private CounponActivity mActivity;
	private RelativeLayout record_select_layout;
	
	/**
	 * 默认支付类型
	 */
	public static final String PAYTYPE_DEFAULT = "paytype_default";
	/**
	 * 支付类型:付临门通道
	 */
	public static final String PAYTYPE_FULINMEN = "paytype_fulinmen";
	
	/**
	 * 当前支付类型
	 */
	private String currentPayType = PAYTYPE_DEFAULT;
	
	private Bundle bundleData = null;
	
	public static CouponBuyFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new CouponBuyFragment();
	}
	
	public CridetCardFragment()
	{
	}
	
	public static CridetCardFragment newInstance (Bundle data) {
		CridetCardFragment fragment = new CridetCardFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	private SwipKeyTask mKeyTask;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
//		initReader();
		
		Bundle bundle = getArguments();
		if(bundle != null) {
			bundleData = bundle;
			currentPayType = bundleData.getString("paytype");
		}
		
		initData();
		if(getActivity() instanceof CounponActivity){
			 mActivity = (CounponActivity)getActivity();
		}
		setBackVisible();
		setRightVisible(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentUtil.startFuncActivity(getActivity(), FragmentFactory.DAIKUAN_RECORD_INDEX);
			}
		}, "历史记录");
//		mKeyTask =  new SwipKeyTask(getActivity(), "fff16101002261", new KeyData());
//		mKeyTask.execute("")
//		;
	}
	
	private void initData(){
		mJournalData.sunMap.put("authorid", LoginUtil.mLoginStatus.authorid);
		mJournalData.sunMap.put(JournalData.paytype, JournalData.DEFAULT_PAY);
		mJournalData.sunMap.put(JournalData.current, JournalData.DEFAULT_CURRENT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.cridet_back_layout, container,false);
		initView(view);
		
		setTitle("还信用卡");
		
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
		money_back = (EditText)view.findViewById(R.id.money_back);
		record_select_layout = (RelativeLayout)view.findViewById(R.id.record_select_layout);
				
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		record_select_layout.setOnClickListener(this);
		
		
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
//			boolean s = mobileReader.open();
			boolean s = PayApp.openReaderNow();
			log("status : "+s);
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
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
			mKeyTask = new SwipKeyTask(getActivity(),PayApp.mKeyCode, PayApp.mKeyData,
					card_edit.getText().toString(), "creditcard",this,"s");
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
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				transaction.replace(R.id.func_container, new SwipCardFragment());
//				transaction.commit();
				Intent intent = new Intent();
				intent.setClass(getActivity(), CridetPayActivity.class);
				
				if(currentPayType.equals(PAYTYPE_DEFAULT)) {//默认支付类型
					intent.putExtra("paytype", currentPayType);
				} else if (currentPayType.equals(PAYTYPE_FULINMEN)){//付临门支付类型
					intent.putExtra("paytype", currentPayType);
				} else {
					intent.putExtra("paytype", PAYTYPE_DEFAULT);
				}
				
//				intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.CRIDET_SWAP_INDEX);
//				startActivity(intent);
				getActivity().startActivityForResult(intent, 0);
			}
			break;
		case R.id.bank_layout:
			showBankList();
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
		intent.putExtra(BankRecordListActivity.TYPE_KEY_STRING, TYPECLASS.creditcard);
		startActivityForResult(intent, 0);
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), BankListActivity.class);
		/**
		 * 作为收款方还是付款方来读取的
		 * 付款方：f（屏蔽付款方禁用的银行）；
		 * 收款方：s（屏蔽收款方禁用的银行）;
		 * 默认为空：读取所有银行
		 */
		intent.putExtra(BankListFragment.READMODE_TAG, "s");
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
		mJournalData.sunMap.put(JournalData.shoucardno, cardNumber);
		
		
		String openName = open_name_edit.getText().toString();
		if(null == openName || "".equals(openName)){
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		if(!UserInfoCheck.checkName(openName)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		mJournalData.sunMap.put(JournalData.shoucardman, openName);
		String openPhone = open_phone_edit.getText().toString();
		if(null == openPhone || "".equals(openPhone)){
			PromptUtil.showToast(getActivity(), "请输入电话号码");
			return false;
		}
		if(!UserInfoCheck.checkMobilePhone(openPhone)){
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		mJournalData.sunMap.put(JournalData.shoucardmobile, openPhone);
		
		String money = money_back.getText().toString();
		if(null == money || "".equals(money)){
			PromptUtil.showToast(getActivity(), "请输入还款金额");
			return false;
		}
		mJournalData.sunMap.put(JournalData.paymoney, money);
//		isSwipIn = true;
//		if(!isSwipIn){
//			PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			return false;
//		}
		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}
		mJournalData.sunMap.put(JournalData.paycardid, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
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
			 
			 open_name_edit.setText(mData.shoucardman);
			 bank_name.setText(mData.shoucardbank);
			 open_phone_edit.setText(mData.shoucardmobile);
			 
			 isSelectedBank=true;
			 mJournalData.sunMap.put(JournalData.shoucardbank, mData.shoucardbank);
		 }
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 bank_name.setText(bankname);
			 mJournalData.sunMap.put(JournalData.shoucardbank, bankname);
		 }
		
		 
		 //银联支付结果
		 
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		endCallStateService();
//		mobileReader.close();
//		closeReader();
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
//		closeReader();
//		endCallStateService();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		mobileReader.open(false);
//		openReaderNow();
		setTitle("还信用卡");
		initSwipPic(PayApp.isSwipIn);
		PayApp.mSwipListener = this;
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

	DefaultBankCardData creditCard;
	
	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		if(protocolDataList != null) {
			creditCard = (DefaultBankCardData)protocolDataList;
			if(creditCard!=null){
				if(creditCard.getBkcardbankman()!=null){
					open_name_edit.setText(creditCard.getBkcardbankman());
					open_name_edit.setEnabled(false);
				}else{
					open_name_edit.setText("");
					open_name_edit.setEnabled(true);
				}
				if(creditCard.getBkcardbankphone()!=null){
					open_phone_edit.setText(creditCard.getBkcardbankphone());
					open_phone_edit.setEnabled(false);
				}else{
					open_phone_edit.setText("");
					open_phone_edit.setEnabled(true);
				}
				
				if(creditCard.getBkcardbank()!=null){
					bank_name.setText(creditCard.getBkcardbank());
					mJournalData.sunMap.put(JournalData.shoucardbank, creditCard.getBkcardbank());
//					isCanSelectBank=false;
					isSelectedBank=true;
				}else{
					bank_name.setText("点击选择");
//					isCanSelectBank=true;
					isSelectedBank=false;
				}
				
				
			}
		}
	}

	@Override
	public void onFailure(String error) {
		
	}
	
}
