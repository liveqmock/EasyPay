package com.inter.trade.ui.fragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ActivePayCardParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SmsCodeData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class ActiveKeyFragment extends BaseFragment implements OnClickListener,SwipListener{
	private Button submit;
	private EditText recieve_sms_phone;
	
	public ActiveKeyFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("开通刷卡器");
		setBackVisible();
	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.active_swip_layout, container, false);
		submit = (Button)view.findViewById(R.id.active_btn);
		recieve_sms_phone = (EditText)view.findViewById(R.id.recieve_sms_phone);
		if(PayApp.mKeyCode !=null){
			recieve_sms_phone.setText(PayApp.mKeyCode);
		}
		submit.setOnClickListener(this);
		PayApp.mSwipListener = this;
	
		return view;
	}
	
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void progress(int status, String message) {
		// TODO Auto-generated method stub
		if(status == PayApp.CMD_KSN){
			recieve_sms_phone.setText(message);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.active_btn:
			String temp = recieve_sms_phone.getText().toString();
			if(null == temp||temp.equals("")){
				PromptUtil.showToast(getActivity(), "请输入刷卡器设备号");
				return;
			}
			
			if(!PayApp.isSwipIn){
				PromptUtil.showToast(getActivity(), "请插入刷卡器");
				return;
			}
			
			if(!temp.equals(PayApp.mKeyCode)){
				PromptUtil.showToast(getActivity(), "手动输入设备号与刷卡器设备号不一致");
				return;
			}
			
			submit();
			
//			String temp = recieve_sms_phone.getText().toString();
//			if(null == temp || temp.length()<11){
//				PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
//				return;
//			}
//			String code = smscode.getText().toString();
//			if(null == code){
//				PromptUtil.showToast(getActivity(), "请输入验证码");
//				return;
//			}
//			
//			if(code.equals(codeData.smscode)){
//				zhanghao = temp;
//				submit();
//			}else{
//				PromptUtil.showToast(getActivity(), "验证码输入错误");
//				return;
//			}
			
			
			break;
		case R.id.get_sms_code:
			String phone = recieve_sms_phone.getText().toString();
			if(null == phone || phone.length()<11){
				PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
				return;
			}
			
			getSmsCode();
			break;
		case R.id.re_sms_btn:
			
			getSmsCode();
			break;

		default:
			break;
		}
		
	}
	
	private void getSmsCode(){
		
		new ActiveTask().execute("");
		
	}
	private void submit(){
		new ActiveTask().execute("");
	}
	class ActiveTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp= null;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			CommonData data = new CommonData();
			data.putValue("paycardkey", recieve_sms_phone.getText().toString());
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfo", "activePayCard", data);
			
			ActivePayCardParser parser = new ActivePayCardParser() ;
			
			mRsp = HttpUtil.doRequest(parser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					//错误处理
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
//					if(codeData.result.equals(ProtocolUtil.SUCCESS)){
//						PromptUtil.showToast(getActivity(), codeData.message);
//					}else {
//						PromptUtil.showToast(getActivity(), codeData.message);
//					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}
				
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), "请稍候...");
		}
		
	}
	
	private void parserResponse(List<ProtocolData> mDatas){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :mDatas){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
//				List<ProtocolData> req_seq = data.find("/req_seq");
//				if(req_seq!=null){
//					response.setReq_seq(req_seq.get(0).mValue);
//				}
//				
//				List<ProtocolData> ope_seq = data.find("/ope_seq");
//				if(ope_seq!=null){
//					response.setOpe_seq(ope_seq.get(0).mValue);
//				}
//			
//				
//				List<ProtocolData> rettype = data.find("/retinfo/rettype");
//				if(rettype!=null){
//					response.setRettype(rettype.get(0).mValue);
//				}
//				
//				List<ProtocolData> retcode = data.find("/retinfo/retcode");
//				if(retcode!=null){
//					response.setRetcode(retcode.get(0).mValue);
//				}
//			
//				
//				List<ProtocolData> retmsg = data.find("/retinfo/retmsg");
//				if(retmsg!=null){
//					response.setRetmsg(retmsg.get(0).mValue);
//				}
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				
//				List<ProtocolData> smsmobile = data.find("/smsmobile");
//				if(smsmobile != null){
//					codeData.smsmobile = smsmobile.get(0).mValue;
//				}
//				
//				
//				List<ProtocolData> smscode = data.find("/smscode");
//				if(smscode != null){
//					codeData.smscode = smscode.get(0).mValue;
//				}
				
			}
		}
	}
}
