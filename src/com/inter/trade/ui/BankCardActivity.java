package com.inter.trade.ui;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.BankCardInfo;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.parser.BankSubmitParser;
import com.inter.trade.parser.MyBankParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

public class BankCardActivity extends BaseActivity implements OnClickListener{
	
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private EditText phone_edit;
	
	private EditText open_card_edit_do;
	private EditText open_phone_edit_do;
	private EditText phone_edit_do;
	
	private TextView bank_prompt_tv;
	private Button btn_confirm_submit;
	
	private BankTask mBankTask;
	private BankCardInfo info;
	private ModifyTask modifyTask;
	
	private String mBankName=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bank_card_layout);
		if(!LoginUtil.isLogin){
			PromptUtil.showLogin(this);
			finish();
			return;
		}
		setupView();
		initEdit(true);
		showEditVisibility(View.VISIBLE);
		setTitle(getResources().getString(R.string.left_bank));
		setBackVisible();
		mBankTask = new BankTask();
		mBankTask.execute();
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bank_name:
			startActivityForResult(new Intent(this, BankListActivity.class), 0);
			break;
		case R.id.btn_confirm_submit:
			if(checkInput()){
				modifyTask = new ModifyTask();
				modifyTask.execute();
			}
			break;
		case R.id.title_right_btn:
			showEditVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 bank_name.setText(bankname);
			 mBankName = bankname;
		 }
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBankTask!=null){
			mBankTask.cancel(true);
		}
		
		if(modifyTask!=null){
			modifyTask.cancel(true);
		}
	}
	private void setupView(){
		bank_prompt_tv = (TextView)findViewById(R.id.bank_prompt_tv);
		btn_confirm_submit = (Button)findViewById(R.id.btn_confirm_submit);
		
		bank_name = (TextView)findViewById(R.id.bank_name);
		open_name_edit =(EditText)findViewById(R.id.open_card_edit);
		open_phone_edit = (EditText)findViewById(R.id.open_phone_edit);
		phone_edit = (EditText)findViewById(R.id.phone_edit);

		open_card_edit_do = (EditText)findViewById(R.id.open_card_edit_do);
		open_phone_edit_do = (EditText)findViewById(R.id.open_phone_edit_do);
		phone_edit_do = (EditText)findViewById(R.id.phone_edit_do);
		
		
		bank_name.setOnClickListener(this);
		btn_confirm_submit.setOnClickListener(this);
		
	}
	
	
	
	private boolean checkInput(){
		if(mBankName==null){
			PromptUtil.showToast(this, "请选择开户银行");
			return false;
		}
		
		info.aushoucardbank = mBankName;
		
		String bankcard = open_card_edit_do.getText().toString();
		if(null ==bankcard ||"".equals(bankcard)){
			PromptUtil.showToast(this, "请输入银行卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBandCid(bankcard)){
			PromptUtil.showToast(this, "银行卡号格式不对");
			return false;
		}
		info.aushoucardno = bankcard;
		
		String openname = open_phone_edit_do.getText().toString();
		if(null ==openname ||"".equals(openname)){
			PromptUtil.showToast(this, "请输入持卡人姓名");
			return false;
		}
		if(!UserInfoCheck.checkName(openname)){
			PromptUtil.showToast(this, "持卡人姓名格式不对");
			return false;
		}
		info.aushoucardman=openname;
		
		String phone = phone_edit_do.getText().toString();
		if(null ==phone ||"".equals(phone)){
			PromptUtil.showToast(this, "请输入银行预留手机号码");
			return false;
		}
		if(!UserInfoCheck.checkMobilePhone(phone)){
			PromptUtil.showToast(this, "银行预留手机号码格式不对");
			return false;
		}
		
		info.aushoucardphone= phone;
		return true;
	}
	private void initEdit(boolean flag){
		if(flag){
			open_name_edit.setVisibility(View.GONE);
			open_phone_edit.setVisibility(View.GONE);
			phone_edit.setVisibility(View.GONE);
			
			open_card_edit_do.setVisibility(View.VISIBLE);
			open_phone_edit_do.setVisibility(View.VISIBLE);
			phone_edit_do.setVisibility(View.VISIBLE);
			
			bank_name.setOnClickListener(this);
		}else{
			open_name_edit.setFocusable(false);
			open_phone_edit.setFocusable(false);
			phone_edit.setFocusable(false);
			open_name_edit.setVisibility(View.VISIBLE);
			open_phone_edit.setVisibility(View.VISIBLE);
			phone_edit.setVisibility(View.VISIBLE);
			
			open_card_edit_do.setVisibility(View.GONE);
			open_phone_edit_do.setVisibility(View.GONE);
			phone_edit_do.setVisibility(View.GONE);
			
			bank_name.setOnClickListener(null);
		}
		
//		if(!flag){
//			open_name_edit.setFilters(new InputFilter[]{filter});
//			open_phone_edit.setFilters(new InputFilter[]{filter});
//			phone_edit.setFilters(new InputFilter[]{filter});
//			bank_name.setOnClickListener(null);
//		}else{
//			open_name_edit.setFilters(null);
//			open_phone_edit.setFilters(null);
//			phone_edit.setFilters(null);
//			bank_name.setOnClickListener(this);
//		}
		
		
	}
	
	
	private void showEditVisibility(int visibility){
		btn_confirm_submit.setVisibility(visibility);
		bank_prompt_tv.setVisibility(visibility);
		if(visibility==View.VISIBLE){
			initEdit(true);
			bank_name.setOnClickListener(this);
		}else{
			initEdit(false);
			bank_name.setOnClickListener(null);
		}
		
	}
	
	private void dealResult(){
		if(info!=null){
			if("0".equals(info.aushoucardstate)){
	//			setRightVisible(this, "编辑");
			}
			bank_name.setText(info.aushoucardbank);
			mBankName = info.aushoucardbank;
			
			open_name_edit.setText(info.aushoucardno);
			open_phone_edit.setText(info.aushoucardman);
			phone_edit.setText(info.aushoucardphone);
			
			open_card_edit_do.setText(info.aushoucardno);
			open_phone_edit_do.setText(info.aushoucardman);
			phone_edit_do.setText(info.aushoucardphone);
		}
	}
	InputFilter filter = new InputFilter(){

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			// TODO Auto-generated method stub
			 return source.length() < 1 ? dest.subSequence(dstart, dend) : ""; 
		}
			
	};
	class ModifyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				data.putValue("aushoucardman", info.aushoucardman);
				data.putValue("aushoucardbank", info.aushoucardbank);
				data.putValue("aushoucardno", info.aushoucardno);
				data.putValue("aushoucardphone", info.aushoucardphone);
				
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
				PromptUtil.showToast(BankCardActivity.this,getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserModifyResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,BankCardActivity.this)){
						
						return;
					}
					
					showEditVisibility(View.VISIBLE);
					dealResult();
					finish();
					
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(BankCardActivity.this,getString(R.string.req_error));
				}
			
			}
				
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(BankCardActivity.this, BankCardActivity.this.getResources().getString(R.string.loading));
		}
		
	}
	
	/**
	 * 请求银行卡信息
	 * @author apple
	 *
	 */
	class BankTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuCardInfo", 
						"readAuBkCardInfo", data);
				MyBankParser authorRegParser = new MyBankParser();
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
				PromptUtil.showToast(BankCardActivity.this,getString(R.string.net_error));
				finish();
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,BankCardActivity.this)){
						finish();
						return;
					}
					
					dealResult();
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(BankCardActivity.this,getString(R.string.req_error));
				}
			
			}
				
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(BankCardActivity.this, BankCardActivity.this.getResources().getString(R.string.loading));
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
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				info = new BankCardInfo();
				
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardman = data.find("/aushoucardman");
				if(aushoucardman != null){
					info.aushoucardman = aushoucardman.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardphone = data.find("/aushoucardphone");
				if(aushoucardphone != null){
					info.aushoucardphone = aushoucardphone.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardno = data.find("/aushoucardno");
				if(aushoucardno != null){
					info.aushoucardno = aushoucardno.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardbank = data.find("/aushoucardbank");
				if(aushoucardbank != null){
					info.aushoucardbank = aushoucardbank.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardstate = data.find("/aushoucardstate");
				if(aushoucardstate != null){
					info.aushoucardstate = aushoucardstate.get(0).mValue;
				}
				
			}
		}
	}
}
