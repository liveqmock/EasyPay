/*
 * @Title:  PayWaysHandler.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月8日 下午1:44:22
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inter.trade.R;

/**
 * 处理支付方式公用页面
 * 
 * @author ChenGuangChi
 * @data: 2014年8月8日 下午1:44:22
 * @version: V1.0
 */
public class PayWaysHandlerTwo implements OnCheckedChangeListener {

	private CheckBox cb_creditcard, cb_depositcard;

	private EditText etCreditCard, etDepositCard;

	private LinearLayout llDeposit, llCredit;
	
	private ImageView ivIcon,ivCredit,ivDeposit;
	
	
	private Context context;
	
	

	public PayWaysHandlerTwo(Context context) {
		super();
		this.context=context;
	}

	public void initView(Activity activity, View view) {
		if (activity == null && view != null) {
			cb_creditcard = (CheckBox) view.findViewById(R.id.cb_creditcard);
			cb_depositcard = (CheckBox) view.findViewById(R.id.cb_deposit);


			etCreditCard = (EditText) view.findViewById(R.id.card_edit);
			etDepositCard = (EditText) view.findViewById(R.id.deposit_edit);

			llCredit = (LinearLayout) view.findViewById(R.id.ll_credit);
			llDeposit = (LinearLayout) view.findViewById(R.id.ll_deposit);
			
			ivIcon=(ImageView) view.findViewById(R.id.ib_bank_mark);
			ivCredit=(ImageView) view.findViewById(R.id.swip_card_credit);
			ivDeposit=(ImageView) view.findViewById(R.id.swip_card_deposit);
			
			cb_creditcard.setOnCheckedChangeListener(this);
			cb_depositcard.setOnCheckedChangeListener(this);
			
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_default:// 默认支付
			if(isChecked){
				cb_creditcard.setChecked(false);
				cb_depositcard.setChecked(false);
			}else{
			}
			break;
		case R.id.cb_creditcard:// 信用卡支付
			if (isChecked) {
				llCredit.setVisibility(View.VISIBLE);
				cb_depositcard.setChecked(false);
			} else {
				llCredit.setVisibility(View.GONE);
			}
			break;
		case R.id.cb_deposit:// 储蓄卡支付
			if (isChecked) {
				cb_creditcard.setChecked(false);
				llDeposit.setVisibility(View.VISIBLE);
			} else {
				llDeposit.setVisibility(View.GONE);
			}
			break;

		default:
			break;
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
	 * 获取是否选中了其中一种支付方式 
	 * @return
	 * @throw
	 * @return boolean
	 */
	public boolean isSelected(){
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
		case 2://信用卡支付
			cb_creditcard.setChecked(true);
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
		if(cb_creditcard.isChecked()){
			return 2;
		}
		if(cb_depositcard.isChecked()){
			return 3;
		}
		
		return 2;
	}
	
	
	/**
	 * 设置刷卡器标识的显示与否 
	 * @param flag
	 * @throw
	 * @return void
	 */
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
	
}
