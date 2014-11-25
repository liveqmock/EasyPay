package com.inter.trade.ui.fragment.transfer2;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.TaskData;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.BankRecordListActivity;
import com.inter.trade.ui.BankRecordListActivity.TYPECLASS;
import com.inter.trade.ui.CounponActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask.FeeListener;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanfeeParser;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 转账-收款人信息确认
 * @author zhichao.huang
 *
 */

public class TransferShoukuanConfirmFragment extends IBaseFragment implements OnClickListener,SwipListener, FeeListener{
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
	
	private Bundle bundleData = null;
	/**
	 * 请求银行记录的类型,默认超级转账
	 */
	private String paytype = "suptfmg";
	
	private SwipKeyTask mKeyTask;
	
	/**
	 * 收款人银行卡信息
	 */
	private BankRecordData bankRecordData = null;
	
	/**
	 * 计算手续费
	 */
	private CreditCardfeeTask mCardfeeTask;
	public CommonData mfeeData=new CommonData();
	private TextView transfer_cost;//手续费
	private TextView arrive_time;//到帐时间
	private TextView pay_total_money;//支付总金额
	/**
	 * 默认没有获取手续费
	 */
	private boolean isSetFee = false;
	
	public static TransferShoukuanConfirmFragment newInstance (Bundle data) {
		TransferShoukuanConfirmFragment fragment = new TransferShoukuanConfirmFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
		
		Bundle bundle = getArguments();
		if(bundle != null) {
			bundleData = bundle;
			paytype = bundleData.getString("paytype");
			Serializable serializable = bundleData.getSerializable("bankShoukuanRecordData");
			if(serializable != null) {
				bankRecordData = (BankRecordData)serializable;
			}
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.transfer2_shoukuan_confirm_layout, container,false);
		initView(view);
		
		return view;
	}

	@Override
	protected void onAsyncLoadData() {
		
	}

	@Override
	public void onRefreshDatas() {
		if(paytype.equals("suptfmg")) {
			((UIManagerActivity)getActivity()).setTopTitle("超级转账");
		} else {
			((UIManagerActivity)getActivity()).setTopTitle("普通转账");
		}
		initSwipPic(PayApp.isSwipIn);
		PayApp.mSwipListener = this;
		if(getActivity() != null){
			((UIManagerActivity)getActivity()).tilte_line.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onTimeout() {
		
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
		
		transfer_cost = (TextView)view.findViewById(R.id.cost);
		arrive_time = (TextView)view.findViewById(R.id.arrive_time);
		pay_total_money = (TextView)view.findViewById(R.id.pay_total_money);
		
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		record_select_layout.setOnClickListener(this);
		
		if(paytype.equals("suptfmg")) {
			((UIManagerActivity)getActivity()).setTopTitle("超级转账");
			arrive_time.setText("T+0");
		} else {
			((UIManagerActivity)getActivity()).setTopTitle("普通转账");
			arrive_time.setText("T+1");
		}
		
		initDatas();
		
		money_back.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length() >= 1) {
					cleanText();//计算手续费前清除上一次手续费的记录
					mfeeData.putValue("money", s.toString());
					getCost();
				}else {
					cleanText();
					transfer_cost.setText("");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	private void initDatas() {
		if(bankRecordData != null ) {//自动填充信息
			
			card_edit.setText(bankRecordData.shoucardno);
			 
			 open_name_edit.setText(bankRecordData.shoucardman);
			 bank_name.setText(bankRecordData.shoucardbank);
//			 open_phone_edit.setText(bankRecordData.shoucardmobile);
			 
			 mfeeData.putValue("bankid", bankRecordData.bankid);
			 
			 isSelectedBank=true;
			 
			 //禁止输入框输入
			 card_edit.setEnabled(false);
			 open_name_edit.setEnabled(false);
			 bank_name.setEnabled(false);
			 bank_layout.setEnabled(false);
			
		}
	}
	
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
//		if(bankRecordData == null ) {
			card_edit.setText(data.pan);
//		}
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
//			boolean s = mobileReader.open();
			boolean s = PayApp.openReaderNow();
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
//			mKeyTask = new SwipKeyTask(getActivity(),PayApp.mKeyCode, PayApp.mKeyData);
//			mKeyTask.execute("");
			
			String pay="";
			if(TYPECLASS.tfmg.equals(paytype)){
				pay="tfmg";
			}else{
				pay="suptfmg";
			}
			
			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode, PayApp.mKeyData,
					card_edit.getText().toString(), pay, new AsyncLoadWorkListener() {

						@Override
						public void onSuccess(Object protocolDataList,
								Bundle bundle) {
							creditCard = (DefaultBankCardData) protocolDataList;
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
									bankname = creditCard.getBkcardbank();
//									isCanSelectBank=false;
									isSelectedBank=true;
								}else{
									bank_name.setText("点击选择");
//									isCanSelectBank=true;
									isSelectedBank=false;
								}
								if(creditCard.getBkcardbankid() != null) {
									bankid = creditCard.getBkcardbankid();
								}
								
							}
							
						}

						@Override
						public void onFailure(String error) {
							// TODO Auto-generated method stub
							
						}
					});
			mKeyTask.execute("");
			break;
		default:
			break;
		}
		
	}
	
	private DefaultBankCardData creditCard;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if(checkInput()){
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_TRANSFER_FUKUAN_PAY, 1, bundleData);
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
	
	/**
	 * 获取手续费
	 */
	private void getCost() {
		onCardfeeTaskDestroy ();
		isSetFee = false;
		mCardfeeTask = new CreditCardfeeTask(getActivity(), initTaskData());
		mCardfeeTask.setFeeListener(this);
		mCardfeeTask.execute("");
	}
	
	/**
	 * 
	 * 销毁CardfeeTask
	 */
	private void onCardfeeTaskDestroy () {
		if (mCardfeeTask != null) {
			mCardfeeTask.cancel(true);
		}
	}
	
	private TaskData initTaskData(){
		TaskData taskData = new TaskData();
		taskData.mCommonData = mfeeData;
		taskData.apiName="ApiPayinfo";
		if(paytype.equals("suptfmg")){
			taskData.funcName="getSupTransferPayfee";
		}else{
			taskData.funcName="getTransferPayfee";
		}
		
		taskData.mNetParser=new DaikuanfeeParser();
		return taskData;
	}
	
	
	//手续费回调
	@Override
	public void result(int state, String fee, ArrayList<ArriveData> datas) {
		// TODO Auto-generated method stub
		if(state == 0) {//成功获取手续费
			setFee(fee);
			isSetFee = true;
			
			if(datas != null && datas.size() > 0) {
				ArriveData arriveData = datas.get(0);
				arriveid = arriveData.arriveid;
			}
			
		} else {
//			isSetFee = false;
			cleanText();
			feeErrorInfo = fee;
			transfer_cost.setText("计算失败");
		}
	}
	
	/**
	 * 手续费
	 */
	private String feeStr;
	/**
	 * 转账金额
	 */
	private String zhuanzhangMoney;
	/**
	 * 总转款额
	 */
	private String totalMoney;
	/**
	 * 到帐时间ID
	 */
	private String arriveid = "";
	
	/**
	 * 银行ID
	 */
	private String bankid;
	/**
	 * 银行名称
	 */
	private String bankname;
	
	/**
	 * 手续费计算失败，返回的错误提示信息
	 */
	private String feeErrorInfo;
	
	/**
	 * 填充手续费，支付总额
	 * @param fee
	 */
	public void setFee(String fee){

		transfer_cost.setText(NumberFormatUtil.format2(fee)+"元");
		
		float total = Float.parseFloat(fee) + Float.parseFloat(money_back.getText().toString());
		pay_total_money.setText(NumberFormatUtil.format2(total+"")+"元");//总金额
		
		feeStr = fee;
		zhuanzhangMoney = money_back.getText().toString();
		totalMoney = total +"";
	}
	
	/**
	 * 清除手续费、支付金额TextView的值
	 */
	public void cleanText() {
		onCardfeeTaskDestroy ();
		isSetFee = false;
		if(feeErrorInfo != null) {
			feeErrorInfo = null;//当计算出手续费把当前受限信息设为null,以免在网络不好的情况下，重新计算手续费时提示该信息
		}
		transfer_cost.setText("正在计算...");
		pay_total_money.setText("");
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
//		String openPhone = open_phone_edit.getText().toString();
//		if(null == openPhone || "".equals(openPhone)){
//			PromptUtil.showToast(getActivity(), "请输入电话号码");
//			return false;
//		}
//		if(!UserInfoCheck.checkMobilePhone(openPhone)){
//			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
//			return false;
//		}
//		mJournalData.sunMap.put(JournalData.shoucardmobile, openPhone);
		
		String money = money_back.getText().toString();
		if(null == money || "".equals(money)){
			PromptUtil.showToast(getActivity(), "请输入转账金额");
			return false;
		}
		
		if(!isSetFee) {
			if(feeErrorInfo != null){
				PromptUtil.showToast(getActivity(), feeErrorInfo);
			} else {
				if(transfer_cost.getText().toString().equals("正在计算...")) {
					PromptUtil.showToast(getActivity(), "请稍后，正在计算手续费...");
				} else if(transfer_cost.getText().toString().equals("计算失败")) {
					PromptUtil.showToast(getActivity(), "手续费计算失败，请检查网络");
				} else {
					PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
				}
					
			}
			
			return false;
		}
		
		
		bundleData.putString("paymoney", zhuanzhangMoney);//转款金额
		bundleData.putString("payfee", feeStr);//手续费
		bundleData.putString("money", totalMoney);//总转款额
		bundleData.putString("current", "RMB");//币种
		bundleData.putString("arriveid", arriveid != null ? arriveid : "");//到帐时间ID
		
		//说明是发起新的转账
		if(bankRecordData == null) {
			bankRecordData = new BankRecordData();
			bankRecordData.bankid = bankid;//收款银行ID
			bankRecordData.shoucardno = cardNumber;//收款卡号
			bankRecordData.shoucardman = openName;//收款人姓名
			bankRecordData.shoucardbank = bankname;//收款银行
			bundleData.putSerializable("bankShoukuanRecordData", bankRecordData);
		}
		
//		mfeeData.putValue("money", money);
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
		 bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 bank_name.setText(bankname);
			 mJournalData.sunMap.put(JournalData.shoucardbank, bankname);
			 
			 mfeeData.putValue("bankid", bankid);
		 }
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		PayApp.mSwipListener= null;
		
		if(mKeyTask!=null){
			mKeyTask.cancel(true);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
}
