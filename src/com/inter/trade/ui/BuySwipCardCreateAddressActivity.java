package com.inter.trade.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
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
 * 购买刷卡器-添加收货地址
 * @author zhichao.huang
 *
 */
public class BuySwipCardCreateAddressActivity extends Activity {

	public static BuySwipCardCreateAddressData buySwipCardCreateAddressData = new BuySwipCardCreateAddressData();
	
	/**
	 * 收款人EditText
	 */
	private EditText shouhuo_name_edit;

	/**
	 * 收款人电话EditText
	 */
	private EditText shouhuo_phone_edit;

	/**
	 * 收款人地址EditText
	 */
	private EditText shouhuo_address_edit;

	/**
	 * 提交按钮
	 */
	private Button submitButton;

	/**
	 * 标题名
	 */
	private TextView title_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.buy_swipcard_add_address_layout);

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
		title_name.setText("新增收货地址");

		shouhuo_name_edit = (EditText)findViewById(R.id.shouhuo_address_name_edit);
		shouhuo_phone_edit = (EditText)findViewById(R.id.shouhuo_address_phone_edit);
		shouhuo_address_edit = (EditText)findViewById(R.id.shouhuo_address_edit);

		submitButton = (Button)findViewById(R.id.cridet_back_btn);
		submitButton.setOnClickListener(onSubmitButtonListener);
	}

	/**
	 * 提交按钮监听
	 */
	private OnClickListener onSubmitButtonListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if(checkUserInfo()){
				//提交参数到服务器
				BuyTask buyTask = new BuyTask();
				buyTask.execute("");
			}

		}
	};

	/**
	 * 检查用户信息
	 * @return
	 */
	private boolean checkUserInfo() {

		String shouhuo_name = shouhuo_name_edit.getText().toString();
		if(shouhuo_name == null || shouhuo_name.equals("")) {
			PromptUtil.showToast(this, "收款人姓名不能为空");
			return false;
		}

		if(!UserInfoCheck.checkName(shouhuo_name)) {
			PromptUtil.showToast(this, "姓名格式不正确");
			return false;
		}
		
		buySwipCardCreateAddressData.sunMap.put(buySwipCardCreateAddressData.SHMAN, shouhuo_name);

		//
		String shouhuo_phone = shouhuo_phone_edit.getText().toString();
		if(shouhuo_phone == null || shouhuo_phone.equals("")) {
			PromptUtil.showToast(this, "手机号码不能为空");
			return false;
		}

		if(!UserInfoCheck.checkMobilePhone(shouhuo_phone)) {
			PromptUtil.showToast(this, "手机号码格式不正确");
			return false;
		}
		buySwipCardCreateAddressData.sunMap.put(buySwipCardCreateAddressData.SHPHONE, shouhuo_phone);
		
		//
		String shouhuo_address = shouhuo_address_edit.getText().toString();
		if(shouhuo_address == null || shouhuo_address.equals("")) {
			PromptUtil.showToast(this, "地址不能为空");
			return false;
		}
		if(shouhuo_address.length() <= 6) {
			PromptUtil.showToast(this, "地址长度太短");
			return false;
		}
//		String address = null;
//		try {
//			address =  new String(shouhuo_address.getBytes(), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		buySwipCardCreateAddressData.sunMap.put(buySwipCardCreateAddressData.SHADDRESS, shouhuo_address);

		return true;
	}




	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiBuyOderInfo", 
						"shaddressAdd", buySwipCardCreateAddressData);
				BuySwipCardCreateAddressParser authorRegParser = new BuySwipCardCreateAddressParser();
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
				PromptUtil.showToast(BuySwipCardCreateAddressActivity.this, BuySwipCardCreateAddressActivity.this.getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					
					if(LoginUtil.mLoginStatus.mResponseData.getRetcode().equals(ProtocolUtil.ERROR_DATABASE)) {
						PromptUtil.showToast(BuySwipCardCreateAddressActivity.this, "您输入的地址存在非法字符");
						return;
					}
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,BuySwipCardCreateAddressActivity.this)){
						return;
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){

						BuySwipCardAddressRecordFragment.isRefresh = true;
						PromptUtil.showToast(BuySwipCardCreateAddressActivity.this, "添加地址成功");
		                Intent intent = new Intent();
		                intent.putExtra("result", "success");
						setResult(RESULT_OK, intent);
						finish();

					}else {
						PromptUtil.showToast(BuySwipCardCreateAddressActivity.this, LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
					PromptUtil.showToast(BuySwipCardCreateAddressActivity.this,getString(R.string.req_error));
				}

			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(BuySwipCardCreateAddressActivity.this, BuySwipCardCreateAddressActivity.this.getResources().getString(R.string.loading));
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

				//				List<ProtocolData> bkntno = data.find("/bkntno");
				//				if(bkntno != null){
				//					mBkntno = bkntno.get(0).mValue;
				//				}
			}

		}

	}
}
