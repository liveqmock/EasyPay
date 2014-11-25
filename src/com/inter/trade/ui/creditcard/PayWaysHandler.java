/*
 * @Title:  PayWaysHandler.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月8日 下午1:44:22
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.util.BankCardUtil;

/**
 * 处理支付方式公用页面
 * 
 * @author ChenGuangChi
 * @data: 2014年8月8日 下午1:44:22
 * @version: V1.0
 */
public class PayWaysHandler implements OnCheckedChangeListener {

	private CheckBox cb_default, cb_creditcard, cb_depositcard;

	private Button btnOne, btnTwo;

	private EditText etCreditCard, etDepositCard;

	private LinearLayout llDeposit, llCredit,llDefault,llDefaultPay;
	
	private TextView tvBankName,tvNo,tvBankType,tvUserName,tvBankTip;
	
	private ImageView ivIcon,ivCredit,ivDeposit;
	
	private OnClickListener listener;
	
	private TextWatcher textWatcher;
	
	private OnFocusChangeListener focusChangeListener;
	
	
	
	private Context context;
	
	

	public PayWaysHandler(Context context,OnClickListener listener,TextWatcher textWatcher,OnFocusChangeListener focusChangeListener) {
		super();
		this.listener = listener;
		this.textWatcher=textWatcher;
		this.context=context;
		this.focusChangeListener=focusChangeListener;
	}

	public void initView(Activity activity, View view) {
		if (activity == null && view != null) {
			cb_default = (CheckBox) view.findViewById(R.id.cb_default);
			cb_creditcard = (CheckBox) view.findViewById(R.id.cb_creditcard);
			cb_depositcard = (CheckBox) view.findViewById(R.id.cb_deposit);

			btnOne = (Button) view.findViewById(R.id.btn_choose_one);
			btnTwo = (Button) view.findViewById(R.id.btn_choose_two);

			etCreditCard = (EditText) view.findViewById(R.id.card_edit);
			etDepositCard = (EditText) view.findViewById(R.id.deposit_edit);

			llCredit = (LinearLayout) view.findViewById(R.id.ll_credit);
			llDeposit = (LinearLayout) view.findViewById(R.id.ll_deposit);
			llDefault=(LinearLayout) view.findViewById(R.id.ll_default);
			llDefaultPay=(LinearLayout) view.findViewById(R.id.ll_default_pay);
			
			tvBankName=(TextView) view.findViewById(R.id.tv_bankname);
			tvNo=(TextView) view.findViewById(R.id.tv_bank_no);
			tvBankType=(TextView) view.findViewById(R.id.tv_cardtype);
			tvUserName=(TextView) view.findViewById(R.id.tv_username);
			tvBankTip=(TextView) view.findViewById(R.id.tv_banktip);
			
			ivIcon=(ImageView) view.findViewById(R.id.ib_bank_mark);
			ivCredit=(ImageView) view.findViewById(R.id.swip_card_credit);
			ivDeposit=(ImageView) view.findViewById(R.id.swip_card_deposit);
			
			cb_default.setOnCheckedChangeListener(this);
			cb_creditcard.setOnCheckedChangeListener(this);
			cb_depositcard.setOnCheckedChangeListener(this);
			
			btnOne.setOnClickListener(listener);
			btnTwo.setOnClickListener(listener);
			
			if(textWatcher != null) {
				etCreditCard.addTextChangedListener(textWatcher);
			}
			if(focusChangeListener != null) {
				etCreditCard.setOnFocusChangeListener(focusChangeListener);
			}

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_default:// 默认支付
			if(isChecked){
				cb_creditcard.setChecked(false);
				cb_depositcard.setChecked(false);
				llDefaultPay.setVisibility(View.VISIBLE);
			}else{
				llDefaultPay.setVisibility(View.GONE);
			}
			break;
		case R.id.cb_creditcard:// 信用卡支付
			if (isChecked) {
				llCredit.setVisibility(View.VISIBLE);
				cb_default.setChecked(false);
				cb_depositcard.setChecked(false);
				btnOne.setEnabled(true);
			} else {
				llCredit.setVisibility(View.GONE);
				btnOne.setEnabled(false);
			}
			break;
		case R.id.cb_deposit:// 储蓄卡支付
			if (isChecked) {
				cb_default.setChecked(false);
				cb_creditcard.setChecked(false);
				llDeposit.setVisibility(View.VISIBLE);
				btnTwo.setEnabled(true);
			} else {
				llDeposit.setVisibility(View.GONE);
				btnTwo.setEnabled(false);
			}
			break;

		default:
			break;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public void setDefaultBank(DefaultBankCardData data){
		data.getBkcardbanklogo();
		tvBankName.setText(data.getBkcardbank());
		tvNo.setText(data.getBkcardno()==null?"":"尾号"+data.getBkcardno().substring(data.getBkcardno().length()-4, data.getBkcardno().length()));
		tvUserName.setText(data.getBkcardbankman());
		tvBankType.setText(BankCardUtil.getCardType(data.getBkcardcardtype()));
		System.out.println(data.getBkcardbanklogo());
//		ivIcon.setBackgroundDrawable(context.getResources().getDrawable(CreditcardInfoUtil.getDrawableOfSmallBank(data.getBkcardbanklogo())));
		
		/**
		 * 先从本地拿银行LOGO，如没有，则去网络拿
		 */
		int drawableId=CreditcardInfoUtil.getDrawableOfSmallBank(data.getBkcardbanklogo());
		if(drawableId != R.drawable.bank_samll){
			ivIcon.setBackgroundDrawable(context.getResources().getDrawable(drawableId));
		}else if(TextUtils.isEmpty(data.getBanklogo())){
			ivIcon.setBackgroundDrawable(context.getResources().getDrawable(drawableId));
		}else{	
			FinalBitmap.create(context).display(ivIcon, data.getBanklogo());
		}
	}
	
	/**
	 * 设置信用卡号 
	 * @param text
	 * @throw
	 * @return void
	 */
	public void setCredit(String text){
		etCreditCard.setText(text);
	}
	/**
	 * 获取信用卡号 
	 * @param text
	 * @throw
	 * @return void
	 */
	public String getCredit(){
		
		return etCreditCard.getText()+"";
	}
	
	/**
	 * 设置储蓄卡号
	 * @param text
	 * @throw
	 * @return void
	 */
	public void setDeposit(String text){
		etDepositCard.setText(text);
	}
	/**
	 * 获取储蓄卡号
	 * @param text
	 * @throw
	 * @return void
	 */
	public String getDeposit(){
		
		return etDepositCard.getText()+"";
	}
	
	/**
	 * 设置默认支付方式的布局的显示
	 * @param visibility
	 * @throw
	 * @return void
	 */
	public void setDefaultVisibility(int visibility){
		llDefault.setVisibility(visibility);
	}
	
	/**
	 * 获取是否选中了其中一种支付方式 
	 * @return
	 * @throw
	 * @return boolean
	 */
	public boolean isSelected(){
		if(cb_default.isChecked()){
			return true;
		}
		if(cb_creditcard.isChecked()){
			return true;
		}
		if(cb_depositcard.isChecked()){
			return true;
		}
		return false;
	}
	
	/**
	 * 设置显示
	 * @param i
	 */
	public void setDefaultPay(int i){
		switch (i) {
		case 1://默认支付
			cb_default.setChecked(true);
			break;
		case 2://信用卡支付
			cb_creditcard.setChecked(true);
			break;
		case 3://储蓄卡支付
			cb_depositcard.setChecked(true);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 
	 * 获取支付方式  1 默认支付  2 信用卡支付  3储蓄卡支付 
	 * @return
	 * @throw
	 * @return int
	 */
	public int getCheckpay(){
		if(cb_default.isChecked()){
			return 1;
		}
		if(cb_creditcard.isChecked()){
			return 2;
		}
		if(cb_depositcard.isChecked()){
			return 3;
		}
		
		return 3;
	}
	
	
	/**
	 * 设置刷卡器标识的显示与否 
	 * @param flag
	 * @throw
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	public void setCardImageVisibility(boolean flag){
		if (flag) {
			ivCredit.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.swip_enable));
			ivDeposit.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.swip_enable));
		} else {
			ivCredit.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.swip_card_bg));
			ivDeposit.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.swip_card_bg));
		}
	}
	
	/**
	 * 设置其他卡支付的文字提示 
	 * @throw
	 * @return void
	 */
	public void setBankTip(){
		tvBankTip.setText("其他卡支付");
	}
}
