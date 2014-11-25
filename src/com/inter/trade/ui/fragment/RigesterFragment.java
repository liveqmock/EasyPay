package com.inter.trade.ui.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ResponseMoreStateListener;
import com.inter.trade.data.SmsCodeData;
import com.inter.trade.ui.fragment.checking.SafetyRigesterFragment;
import com.inter.trade.ui.fragment.checking.task.SmsTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.inter.trade.view.dialog.GoBackDialog;
import com.inter.trade.view.dialog.MobileDialog;
import com.inter.trade.view.dialog.SMSResendBottonView;
import com.inter.trade.view.dialog.MobileDialog.SmsCodeSubmitListener;
import com.inter.trade.view.dialog.SMSResendBottonView.DialogButtonPositive;

public class RigesterFragment extends BaseFragment implements OnClickListener,ResponseMoreStateListener,DialogButtonPositive
			,SmsCodeSubmitListener{
	private Button get_sms_code;
	private EditText recieve_sms_phone,et_sms;
	
	/**
	 * 获取到的短息
	 */
	private SmsCodeData codeData;
	
	private View layout_sms_get,layout_sms_received;
	
	private TextView tv_phone_received,tv_getsms;//注册的手机号
	
	private Button submit;
	
	/**
	 * 注册的手机号码
	 */
	private String recieve_phone=null;
	private String zhanghao = null;
	
	private Handler mHandler;
	private static final int TIME_COUNT= 1;
	private static final int TIME_FINISH= 2;
	private int total_count = 60;
	
	/**短信验证码的有效期*/
	private int SMS_TIME=60;
	
	private Timer mTimer;
	private Timer mFiveMiniTimer;
	private SmsTask mSmsTask;
	private boolean isSmsInvilidate=false;
	private int mRealTime = 60*5;
	private int five_total_count=mRealTime;
	
	/**是否获取了短信验证码*/
	private boolean isGetSMS=false;
	
	/**短信获取界面*/
	private ViewStub vsSMSGet;
	
	/**短信接受到的界面*/
	private ViewStub vsSMSReceived;
	
	/***是否第一次获取短信验证码*/
	private boolean isFirst=true;
	
	public RigesterFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				try {
					if(msg.what==TIME_COUNT){
						if(total_count<=SMS_TIME&&total_count>0){
							tv_getsms.setText("接收短信大约需要"+total_count+"秒");
							tv_getsms.setTextColor(getActivity().getResources().getColor(R.color.gray_bg));
							tv_getsms.setEnabled(false);
						}else{
							mTimer.cancel();
							total_count=SMS_TIME;
							tv_getsms.setText("收不到验证码?");
							tv_getsms.setTextColor(getActivity().getResources().getColor(R.color.game_blue));
							tv_getsms.setEnabled(true);
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
		super.onResume();
		setTitle("注册");
		setBackVisible();
	}

	protected void setBackVisible() {
		if (getActivity() == null) {
			return;
		}
		
		View view = getActivity().findViewById(R.id.iv_tilte_line);
		if(view!=null){
			view.setVisibility(View.VISIBLE);
		}
		
		
		back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isGetSMS){
					GoBackDialog dialog=new GoBackDialog();
					dialog.show(getActivity(), new GoBackDialog.SmsCodeSubmitListener() {
						
						@Override
						public void onPositive() {
							getActivity().finish();
						}
					});
				}else{
					getActivity().finish();
				}
				
			}
		});
		
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
		super.onPause();
	}

	
	@Override
	public void onStop() {
		super.onStop();
		isFirst=true;
		isGetSMS=false;
		total_count = 60;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rigester_layout, container, false);
		
		
		vsSMSGet=(ViewStub) view.findViewById(R.id.vs_sms_get);
		vsSMSReceived=(ViewStub) view.findViewById(R.id.vs_sms_received);
		
		
		layout_sms_get =vsSMSGet.inflate();
		
		
		get_sms_code = (Button)view.findViewById(R.id.get_sms_code);
		recieve_sms_phone= (EditText)view.findViewById(R.id.recieve_sms_phone);
		
		get_sms_code.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit://提交注册
			String temp = "";
			String code = et_sms.getText().toString();
			if(null == code||code.length()==0){
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
		case R.id.get_sms_code://提交获取验证码
			String phone = recieve_sms_phone.getText().toString();
			
			if(null == phone || phone.length()!=11){
				PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
				return;
			}
			
			if(!UserInfoCheck.checkMobilePhone(phone)){
				PromptUtil.showToast(getActivity(), "手机号码格式不正确");
				return ;
			}
			recieve_phone = phone;
			isGetSMS=true;
			MobileDialog dialog=MobileDialog.getInstance(recieve_phone);
			dialog.show(getActivity(), this);
			break;
		case R.id.tv_getsms://重新获取验证码
			SMSResendBottonView bottom=new SMSResendBottonView(getActivity(), this);
			bottom.show();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onDestroy() {
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
	
	private void getSmsCode(){
		
		mSmsTask = new SmsTask(getActivity(),this);
		mSmsTask.execute(recieve_phone);
	}
	
	private void submit(){
		mFiveMiniTimer.cancel();
		mTimer.cancel();
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, SafetyRigesterFragment.createFragment(recieve_sms_phone.getText().toString()));
		transaction.addToBackStack("");
		transaction.commit();
	}
	
	
	private void getCodeSuccess(){
		initTimer();
		initFiveTimer();
		setTitle("填写验证码");
		if(isFirst){
			isFirst=false;
			layout_sms_get.setVisibility(View.GONE);
			layout_sms_received =vsSMSReceived.inflate();
			et_sms=(EditText) layout_sms_received.findViewById(R.id.et_sms);
			tv_phone_received=(TextView) layout_sms_received.findViewById(R.id.tv_phone_received);
			tv_getsms=(TextView) layout_sms_received.findViewById(R.id.tv_getsms);
			submit=(Button) layout_sms_received.findViewById(R.id.submit);
			submit.setOnClickListener(this);
			tv_getsms.setOnClickListener(this);
			
			if(tv_phone_received!=null && tv_phone_received.length()>=11){//设置注册的手机号
				tv_phone_received.setText(recieve_phone.substring(0, 3)+" "+recieve_phone.substring(3, 7)+" "+recieve_phone.substring(7, 11));
			}
			et_sms.requestFocus();
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(et_sms, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		codeData=(SmsCodeData) obj;
		getCodeSuccess();
		isSmsInvilidate=false;
	}

	@Override
	public void onPositive() {//点击重新获取或者第一次获取
		getSmsCode();
	}

	/**
	 * 重载方法
	 * @param obj
	 * @param cla
	 */
	@Override
	public void onFailure(Object obj, Class cla) {
		//getSmsCode();
	}
}
