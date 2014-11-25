package com.inter.trade.ui.fragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
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
import com.inter.protocol.body.SmsCodeParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SmsCodeData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

public class ForgetPasswordFragment extends BaseFragment implements OnClickListener{
	private Button get_sms_code;
	private Button submit;
	private Button re_sms_btn;
	private View line;
	private LinearLayout codeLayout;
	private EditText smscode;
	private EditText recieve_sms_phone;
	private TextView phone_text;
	private SmsCodeData codeData=null;
	private String recieve_phone=null;
	private String zhanghao = null;
	private LinearLayout phone_layout;
	
	private Handler mHandler;
	private static final int TIME_COUNT= 1;
	private int total_count = 60;
	private Timer mTimer;
	
	private static final int TIME_FINISH= 2;
	private Timer mFiveMiniTimer;
	private SmsTask mSmsTask;
	private boolean isSmsInvilidate=false;
	private int mRealTime = 60*5;
	private int five_total_count=mRealTime;
	
	
	public ForgetPasswordFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("找回密码");
		setBackVisible();
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				try {
					if(msg.what==TIME_COUNT){
						if(total_count<=60&&total_count>0){
							re_sms_btn.setText("("+total_count+"秒后)重新获取");
						}else{
							re_sms_btn.setEnabled(true);
							re_sms_btn.setTextColor(getActivity().getResources().getColor(R.color.black));
							mTimer.cancel();
							total_count=60;
							re_sms_btn.setText("重新获取验证码");
						}
						
					}else if(msg.what== TIME_FINISH){
//						PromptUtil.showToast(getActivity(), "验证码失效，请重新获取验证码");
						isSmsInvilidate=true;
						codeData.smscode=null;
						mFiveMiniTimer.cancel();
						five_total_count = mRealTime;
						
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			
		};
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("找回密码");
	}

	private  void initTimer() {
		// 定义计时器 ，延迟1秒执行，间隔为1秒
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				total_count--;
				final Message message = new Message();
				message.what = TIME_COUNT;
				mHandler.sendMessage(message);
			}
		}, 0, 1000);
	}
	private void initFiveTimer(){
		mFiveMiniTimer = new Timer();
		mFiveMiniTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				five_total_count--;
				if(five_total_count==0){
					final Message message = new Message();
					message.what = TIME_FINISH;
					mHandler.sendMessage(message);
				}
			
			}
		}, 0, 1000);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mTimer != null){
			mTimer.cancel();
		}
		
		if(mFiveMiniTimer !=null){
			mFiveMiniTimer.cancel();
		}
		if(mSmsTask!=null)
		{
			mSmsTask.cancel(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.forget_password_layout, container, false);
		submit = (Button)view.findViewById(R.id.submit);
		get_sms_code = (Button)view.findViewById(R.id.get_sms_code);
		line = view.findViewById(R.id.my_line);
		codeLayout = (LinearLayout)view.findViewById(R.id.code_layout);
		smscode = (EditText)view.findViewById(R.id.smscode);
		recieve_sms_phone= (EditText)view.findViewById(R.id.recieve_sms_phone);
		re_sms_btn = (Button)view.findViewById(R.id.re_sms_btn);
		phone_text = (TextView)view.findViewById(R.id.phone_text);
		phone_layout = (LinearLayout)view.findViewById(R.id.phone_layout);
		
		submit.setOnClickListener(this);
		re_sms_btn.setOnClickListener(this);
		get_sms_code.setOnClickListener(this);
		
	
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit:
			String temp = recieve_sms_phone.getText().toString();
			if(null == temp || temp.length()<11){
				PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
				return;
			}
			String code = smscode.getText().toString();
			if(null == code){
				PromptUtil.showToast(getActivity(), "请输入验证码");
				return;
			}
			if(isSmsInvilidate){
				PromptUtil.showToast(getActivity(), "验证码失效，请重新获取验证码");
				return;
			}
			if(codeData.smscode==null){
				PromptUtil.showToast(getActivity(), "请重新获取验证码");
				return ;
			}
			if(code.equals(codeData.smscode)){
				zhanghao = temp;
				submit();
			}else{
				PromptUtil.showToast(getActivity(), "验证码输入错误");
				return;
			}
			
			
			break;
		case R.id.get_sms_code:
			String phone = recieve_sms_phone.getText().toString();
			if(null == phone || phone.length()!=11){
				PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
				return;
			}
			if(!UserInfoCheck.checkMobilePhone(phone)){
				PromptUtil.showToast(getActivity(), "手机格式不正确");
				return;
			}
			recieve_phone = phone;
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
		
		mSmsTask = new SmsTask();
		mSmsTask.execute("")
		;
		
	}
	private void submit(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, SetNewPwdFragment.createFragment(recieve_phone, "2"));
		transaction.commit();
	}
	class SmsTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp= null;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			CommonData data = new CommonData();
			data.putValue(ProtocolUtil.smsmobile, recieve_phone);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfo",
					"getSmsCode", data);
			
			
			
			mRsp = HttpUtil.doRequest(new SmsCodeParser(), mDatas);
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
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					if(codeData.result.equals(ProtocolUtil.SUCCESS)){
						getCodeSuccess();
						
					}else {
						PromptUtil.showToast(getActivity(), codeData.message);
					}
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
		codeData = new SmsCodeData();
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
					codeData.result = result1.get(0).mValue;
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					codeData.message = message.get(0).mValue;
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				
				List<ProtocolData> smsmobile = data.find("/smsmobile");
				if(smsmobile != null){
					codeData.smsmobile = smsmobile.get(0).mValue;
				}
				
				
				List<ProtocolData> smscode = data.find("/smscode");
				if(smscode != null){
					codeData.smscode = smscode.get(0).mValue;
				}
				
			}
		}
	}
	private void getCodeSuccess(){
		initTimer();
		initFiveTimer();
		isSmsInvilidate=false;
		re_sms_btn.setEnabled(false);
		re_sms_btn.setTextColor(getActivity().getResources().getColor(R.color.gray));
		setTitle("提交");
		get_sms_code.setVisibility(View.GONE);
		recieve_sms_phone.setVisibility(View.GONE);
		phone_text.setVisibility(View.GONE);
		phone_text.setText(recieve_phone);
		submit.setVisibility(View.VISIBLE);
		line.setVisibility(View.GONE);
		codeLayout.setVisibility(View.VISIBLE);
		phone_layout.setVisibility(View.GONE);
	}
}
