package com.inter.trade.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.parser.BankSubmitParser;
import com.inter.trade.ui.fragment.buyswipcard.BuySwipCardAddressRecordFragment;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardCreateAddressData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardCreateAddressParser;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardOrderData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardOrderNoParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 购买刷卡器-新增银行卡绑定
 * @author zhichao.huang
 *
 */
public class BuySwipCardAddBankCardActivity extends Activity {

	public static BuySwipCardCreateAddressData buySwipCardCreateAddressData = new BuySwipCardCreateAddressData();
	
	/**
	 * EditText
	 */
	private EditText phone_edit, kaihu_name_edit, kaihu_bank_edit, bank_card_edit;
	
	private String mkaihu_name, mphone, mkaihu_bank, mbank_card;


	/**
	 * 提交按钮
	 */
	private Button submitButton, giveupButton;

	/**
	 * 标题名
	 */
	private TextView title_name;
	
	public static Boolean giveup = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.buy_swipcard_add_bank_card_layout);

		initViews();
	}

	private void initViews() {

		findViewById(R.id.title_back_btn).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		title_name = (TextView)findViewById(R.id.title_name);
		title_name.setText("购买刷卡器");

		kaihu_name_edit = (EditText)findViewById(R.id.kaihu_name_edit);
		phone_edit = (EditText)findViewById(R.id.phone_edit);
		kaihu_bank_edit = (EditText)findViewById(R.id.kaihu_bank_edit);
		bank_card_edit = (EditText)findViewById(R.id.bank_card_edit);
		
		

		submitButton = (Button)findViewById(R.id.cridet_back_btn);
		submitButton.setOnClickListener(onSubmitButtonListener);
		
		//放弃返利按钮
		giveupButton = (Button)findViewById(R.id.give_up_btn);
		giveupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				giveup = true;
				finish();
			}
		});
	}

	/**
	 * 提交按钮监听
	 */
	private OnClickListener onSubmitButtonListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if(checkUserInfo()){
				//提交参数到服务器
				ModifyTask modifyTask = new ModifyTask();
				modifyTask.execute("");
			}

		}
	};

	/**
	 * 检查用户信息
	 * @return
	 */
	private boolean checkUserInfo() {

		String kaihu_name = kaihu_name_edit.getText().toString();
		if(kaihu_name == null || kaihu_name.equals("")) {
			PromptUtil.showToast(this, "开户人姓名不能为空");
			return false;
		}

		if(!UserInfoCheck.checkName(kaihu_name)) {
			PromptUtil.showToast(this, "姓名格式不正确");
			return false;
		}
		
		mkaihu_name = kaihu_name;

		//
		String phone = phone_edit.getText().toString();
		if(phone == null || phone.equals("")) {
			PromptUtil.showToast(this, "手机号码不能为空");
			return false;
		}

		if(!UserInfoCheck.checkMobilePhone(phone)) {
			PromptUtil.showToast(this, "手机号码格式不正确");
			return false;
		}
		mphone = phone;
		
		//
		String kaihu_bank = kaihu_bank_edit.getText().toString();
		if(kaihu_bank == null || kaihu_bank.equals("")) {
			PromptUtil.showToast(this, "开户银行不能为空");
			return false;
		}
		mkaihu_bank = kaihu_bank;
		
		String bank_card = bank_card_edit.getText().toString();
		if(bank_card == null || bank_card.equals("")) {
			PromptUtil.showToast(this, "银行卡号不能为空");
			return false;
		}

		if(!UserInfoCheck.checkBankCard(bank_card)) {
			PromptUtil.showToast(this, "银行卡号格式不正确");
			return false;
		}
		mbank_card = bank_card;
		
//		String address = null;
//		try {
//			address =  new String(shouhuo_address.getBytes(), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

		return true;
	}


	/**
	 * 新增、修改银行卡信息
	 * @author zhichao.huang
	 *
	 */
	class ModifyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				data.putValue("aushoucardman", mkaihu_name);
				data.putValue("aushoucardbank", mkaihu_bank);
				data.putValue("aushoucardno", mbank_card);
				data.putValue("aushoucardphone", mphone);
				
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuCardInfo", 
						"modifyAuBkCardInfo", data);
				BankSubmitParser authorRegParser = new BankSubmitParser();
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
				PromptUtil.showToast(BuySwipCardAddBankCardActivity.this,getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserModifyResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,BuySwipCardAddBankCardActivity.this)){
						
						return;
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						String ordernum = BuySwipcardPayActivity.buySwipCardOrderData.sunMap.get(BuySwipCardOrderData.ORDERNUM);
						PromptUtil.showToast(BuySwipCardAddBankCardActivity.this,"绑定成功，已减"+Integer.parseInt(ordernum)*30+"元");
						finish();
					}
					
//					showEditVisibility(View.GONE);
//					dealResult();
					
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(BuySwipCardAddBankCardActivity.this,getString(R.string.req_error));
				}
			
			}
				
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(BuySwipCardAddBankCardActivity.this, BuySwipCardAddBankCardActivity.this.getResources().getString(R.string.loading));
		}
		
	}

	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserModifyResoponse(List<ProtocolData> params){
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
				
			}
		}
	}
	
}
