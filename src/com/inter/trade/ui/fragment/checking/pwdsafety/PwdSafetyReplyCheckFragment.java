package com.inter.trade.ui.fragment.checking.pwdsafety;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolRsp;
import com.inter.trade.R;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.RigesterFragment;
import com.inter.trade.ui.fragment.checking.SafetyModifyPwdFragment;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserData;
import com.inter.trade.util.Constants;
import com.inter.trade.util.PromptUtil;

/**
 * 核对密保答案
 * @author zhichao.huang
 *
 */
@SuppressLint("ValidFragment")
public class PwdSafetyReplyCheckFragment extends BaseFragment implements OnClickListener{
	private Button loginButton;
	private Button registerButton;
	private LoginStatus mData = new LoginStatus();
	private ProtocolRsp mRsp;
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private String name = "";
	private String pwd  = "";
	
	private CheckBox remenber_ck;
//	private LoginTask mLoginTask;
	
	//用于抖动窗口
	private LinearLayout usepwdLayout, usepwdConfirmLayout;
	
	/**
	 * 手机唯一标识
	 */
	private String mobileDeviceId;
	
	private ArrayList<PwdSafetyValidateUserData> psvuDatas;
	
	/**
	 * 当前账户
	 */
	private String phonenumber = "";
	
	/**
	 * 密保修改登录密码，是否清除本地账号信息,默认mIsClean = true，代表清除
	 */
	private boolean mIsClean = true;
	
	public PwdSafetyReplyCheckFragment(){
	}
	
	public PwdSafetyReplyCheckFragment(ArrayList<PwdSafetyValidateUserData> mList, String phone){
		psvuDatas = mList;
		phonenumber = phone;
		mIsClean = true;
	}
	
	public PwdSafetyReplyCheckFragment(ArrayList<PwdSafetyValidateUserData> mList, String phone, boolean isClean){
		psvuDatas = mList;
		phonenumber = phone;
		mIsClean = isClean;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("核对密保问题");
		setBackVisible();
	}
	
	/**
	 * 运行动画
	 * @param view
	 */
	private void runAnimation(View view) {
		Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake_x);
		view.startAnimation(shakeAnim);
	}
	
	private boolean checkInput()
	{
		
//		pwdSafetyData = new ArrayList<PwdSafetySettingData> ();
//		PwdSafetySettingData pssd = null;
//		
		String  pwdsafety_set_str1 = pwdsafety_set_edittext1.getText().toString();
		String  pwdsafety_set_hint_str1 = pwdsafety_set_edittext1.getHint().toString();
		String  pwdsafety_reply_str1 = pwdsafety_reply_edittext1.getText().toString();
//		
//		String  pwdsafety_set_str2 = pwdsafety_set_edittext2.getText().toString();
//		String  pwdsafety_reply_str2 = pwdsafety_reply_edittext2.getText().toString();
//		
//		String  pwdsafety_set_str3 = pwdsafety_set_edittext3.getText().toString();
//		String  pwdsafety_reply_str3 = pwdsafety_reply_edittext3.getText().toString();
		
		
		if(null == pwdsafety_set_str1 || "".equals(pwdsafety_set_str1)){
			PromptUtil.showToast(getActivity(), "请选择密保问题");
			return false;
		}
		
		if(null == pwdsafety_reply_str1 || "".equals(pwdsafety_reply_str1)){
			PromptUtil.showToast(getActivity(), "请回答密保问题");
			return false;
		}
		
		if(!pwdsafety_set_hint_str1.equals(pwdsafety_reply_str1)) {
			PromptUtil.showToast(getActivity(), "密保问题回答错误");
			return false;
		}
//		
//		pssd = new PwdSafetySettingData();
//		pssd.queid =pwdsafety_set_edittext1.getHint().toString();
//		pssd.answer=pwdsafety_reply_str1;
//		pwdSafetyData.add(pssd);
//		
//		if(isSelecteMore) {
//			
//			
//			if(pwdsafety_set_str2 != null && !"".equals(pwdsafety_set_str2)){
//				if(null == pwdsafety_reply_str2 || "".equals(pwdsafety_reply_str2)){
//					PromptUtil.showToast(getActivity(), "请回答密保问题");
//					return false;
//				}
//			}
//			
//			if(pwdsafety_set_str3 != null && !"".equals(pwdsafety_set_str3)){
//				if(null == pwdsafety_reply_str3 || "".equals(pwdsafety_reply_str3)){
//					PromptUtil.showToast(getActivity(), "请回答密保问题");
//					return false;
//				}
//			}
//			
//			
//			if(pwdsafety_set_str2 != null && !"".equals(pwdsafety_set_str2)
//					&& pwdsafety_reply_str2 != null && !"".equals(pwdsafety_reply_str2)) {
//				pssd = new PwdSafetySettingData();
//				pssd.queid =pwdsafety_set_edittext2.getHint().toString();
//				pssd.answer=pwdsafety_reply_str2;
//				pwdSafetyData.add(pssd);
//			}
//			if(pwdsafety_set_str3 != null && !"".equals(pwdsafety_set_str3)
//					&& pwdsafety_reply_str3 != null && !"".equals(pwdsafety_reply_str3)) {
//				pssd = new PwdSafetySettingData();
//				pssd.queid =pwdsafety_set_edittext3.getHint().toString();
//				pssd.answer=pwdsafety_reply_str3;
//				pwdSafetyData.add(pssd);
//			}
//		}
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		if(mLoginTask!=null){
//			mLoginTask.cancel(true);
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.pwdsafety_reply_check_layout, container, false);
		
		initViews(view);
		
//		GetPwdSafetyTask getPwdSafetyTask = new GetPwdSafetyTask();
//		getPwdSafetyTask.execute("");
		
		return view;
	}
	
	private ImageView pwdsafety_more_selecte;
	
	private EditText pwdsafety_set_edittext1, pwdsafety_reply_edittext1;
	private ImageView pwdsafety_set_chosen1;
	
	private RelativeLayout pwdsafety_set_layout1, pwdsafety_set_layout2, pwdsafety_reply_layout2;
	private EditText pwdsafety_set_edittext2, pwdsafety_reply_edittext2;
	private ImageView pwdsafety_set_chosen2;
	
	private RelativeLayout pwdsafety_set_layout3, pwdsafety_reply_layout3;
	private EditText pwdsafety_set_edittext3, pwdsafety_reply_edittext3;
	private ImageView pwdsafety_set_chosen3;
	
	private TextView username;
	
	/**
	 * 记录更多按钮，当前是处于选中状态还是未选中状态
	 */
	private boolean isSelecteMore = false;
	
	private void initViews(View view){
		
		view.findViewById(R.id.submit).setOnClickListener(this);
		pwdsafety_more_selecte = (ImageView)view.findViewById(R.id.pwdsafety_more_selecte);
		pwdsafety_more_selecte.setOnClickListener(this);
		
		pwdsafety_set_layout1 = (RelativeLayout)view.findViewById(R.id.pwdsafety_set_layout1);
		pwdsafety_set_edittext1 = (EditText)view.findViewById(R.id.pwdsafety_set_edittext1);//密保问题
		pwdsafety_set_chosen1 = (ImageView)view.findViewById(R.id.pwdsafety_set_chosen1);//选择问题
		pwdsafety_reply_edittext1 = (EditText)view.findViewById(R.id.pwdsafety_reply_edittext1);//回答问题
		
		username = (TextView)view.findViewById(R.id.username);
		username.setText(phonenumber);
//		pwdsafety_set_layout2 = (RelativeLayout)view.findViewById(R.id.pwdsafety_set_layout2);
//		pwdsafety_reply_layout2 = (RelativeLayout)view.findViewById(R.id.pwdsafety_reply_layout2);
//		
//		pwdsafety_set_edittext2 = (EditText)view.findViewById(R.id.pwdsafety_set_edittext2);
//		pwdsafety_set_chosen2 = (ImageView)view.findViewById(R.id.pwdsafety_set_chosen2);
//		pwdsafety_reply_edittext2 = (EditText)view.findViewById(R.id.pwdsafety_reply_edittext2);
//		
//		pwdsafety_set_layout3 = (RelativeLayout)view.findViewById(R.id.pwdsafety_set_layout3);
//		pwdsafety_reply_layout3 = (RelativeLayout)view.findViewById(R.id.pwdsafety_reply_layout3);
//		
//		pwdsafety_set_edittext3 = (EditText)view.findViewById(R.id.pwdsafety_set_edittext3);
//		pwdsafety_set_chosen3 = (ImageView)view.findViewById(R.id.pwdsafety_set_chosen3);
//		pwdsafety_reply_edittext3 = (EditText)view.findViewById(R.id.pwdsafety_reply_edittext3);
//		
//		pwdsafety_set_edittext1.setOnClickListener(this);
		pwdsafety_set_chosen1.setOnClickListener(this);
		pwdsafety_set_layout1.setOnClickListener(this);
		pwdsafety_set_edittext1.setOnClickListener(this);
//		pwdsafety_set_edittext2.setOnClickListener(this);
//		pwdsafety_set_chosen2.setOnClickListener(this);
//		pwdsafety_set_edittext3.setOnClickListener(this);
//		pwdsafety_set_chosen3.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
//		case R.id.pwdsafety_more_selecte://设置更多问题
//			
//			if(isSelecteMore) {
//				pwdsafety_set_layout2.setVisibility(View.GONE);
//				pwdsafety_reply_layout2.setVisibility(View.GONE);
//				
//				pwdsafety_set_layout3.setVisibility(View.GONE);
//				pwdsafety_reply_layout3.setVisibility(View.GONE);
//				
//				pwdsafety_more_selecte.setImageResource(R.drawable.pwdsafety_more_off);
//				isSelecteMore = false;
//				
//			}else {
//				
//				pwdsafety_set_layout2.setVisibility(View.VISIBLE);
//				pwdsafety_reply_layout2.setVisibility(View.VISIBLE);
//				
//				pwdsafety_set_layout3.setVisibility(View.VISIBLE);
//				pwdsafety_reply_layout3.setVisibility(View.VISIBLE);
//				
//				pwdsafety_more_selecte.setImageResource(R.drawable.pwdsafety_more_on);
//				isSelecteMore = true;
//			}
//			break;
		case R.id.pwdsafety_set_layout1:
		case R.id.pwdsafety_set_edittext1:
		case R.id.pwdsafety_set_chosen1://选择问题
			showPwdSafetyDialog ();
			break;
//		case R.id.pwdsafety_set_chosen2://选择问题
//			showPwdSafetyDialog (2);
//			break;
//		case R.id.pwdsafety_set_chosen3://选择问题
//			showPwdSafetyDialog (3);
//			break;
			
		case R.id.submit://提交
			if(checkInput()) {
				safetyModifyPwdFragment();
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 进入修改密码页面
	 */
	private void safetyModifyPwdFragment(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		if(mIsClean){
			transaction.replace(R.id.func_container, SafetyModifyPwdFragment.createFragment(phonenumber));
		}
		else{
			transaction.replace(R.id.func_container, SafetyModifyPwdFragment.createFragment(phonenumber, false));
		}
		transaction.commit();
	}
	
	/*
	 * ArrayList<PwdSafetyGetData> 把所有psgData.quecontent转为String数组
	 */
	private CharSequence[] mListToStringArray(ArrayList<PwdSafetyValidateUserData> mList) {
		ArrayList<String> psgDataStr = new ArrayList<String>();
		for(PwdSafetyValidateUserData psgData : mList) {
			psgDataStr.add(psgData.que);
		}
		return psgDataStr.toArray(new CharSequence[psgDataStr.size()]);
	}
	
	private void showPwdSafetyDialog () {
		if(psvuDatas == null || psvuDatas.size() == 0) return;
		
		final CharSequence[] items = mListToStringArray(psvuDatas);
		new AlertDialog.Builder(getActivity()).setTitle("选择密保问题")
		.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
					pwdsafety_set_edittext1.setText(items[which]);
					pwdsafety_set_edittext1.setHint(psvuDatas.get(which).answer);
				
				dialog.dismiss();
			}
		}).create().show();
	}
	
	private void registerFragment(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new RigesterFragment());
		transaction.commit();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Constants.ACTIVITY_FINISH){
			getActivity().setResult(Constants.ACTIVITY_FINISH);
			Log.i("Result", "SafetyLoginActivity fragement finish()");
			getActivity().finish();
		}
	}
	
	//----------------------------------设置密保问题------------------------------------------------
//	class PwdSafetySettingTask extends AsyncTask<String, Integer, Boolean>{
//
//		@Override
//		protected Boolean doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			List<ProtocolData> mDatas = getRequestDatas();
//			PwdSafetySettingParser authorRegParser = new PwdSafetySettingParser();
//			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			// TODO Auto-generated method stub
//			
//			super.onPostExecute(result);
//			try {
//				PromptUtil.dissmiss();
//				if(mRsp == null){
//					PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
//				}else{
//					try {
//						List<ProtocolData> mDatas = mRsp.mActions;
//						parserResoponse(mDatas);
//						
//						if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//							PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
//							LoginUtil.isLogin=true;
////							LoginUtil.mLoginStatus.login_name="";
//							Logger.d("login","登录接口"+LoginUtil.mLoginStatus.mResponseData.getReq_token());
//							ProtocolUtil.printString("LoginFragment",
//									 LoginUtil.mLoginStatus.mResponseData.getReq_token());
//							
//							getActivity().finish();
//							
//						}else {
//							PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
//						}
//					} catch (Exception e) {
//						// TODO: handle exception
//						e.printStackTrace();
//						PromptUtil.showToast(getActivity(), getString(R.string.req_error));
//					}
//					
//				}
//			} catch (Exception e) {
//			}
//		
//			
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			PromptUtil.showDialog(getActivity(), "正在提交...");
//		}
//		
//	}
//    
//    private List<ProtocolData> getRequestDatas(){
//		CommonData data = new CommonData();
//		List<ProtocolData> mDatas = ProtocolUtil.getPwdSafetyRequestDatas("ApiSafeGuard", "setAnswer", data, pwdSafetyData);
//		return mDatas;
//	}
//    
//    
//    /**
//	 * 解析响应体
//	 * @param params
//	 */
//	private void parserResoponse(List<ProtocolData> params){
//		ResponseData response = new ResponseData();
//		LoginUtil.mLoginStatus.mResponseData = response;
//		for(ProtocolData data :params){
//			if(data.mKey.equals(ProtocolUtil.msgheader)){
//				ProtocolUtil.parserResponse(response, data);
//				
//			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
//				List<ProtocolData> result1 = data.find("/result");
//				if(result1 != null){
//					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
//				}
//				
//				
//				List<ProtocolData> message = data.find("/message");
//				if(message != null){
//					LoginUtil.mLoginStatus.message = message.get(0).mValue;
//				}
//				
//				List<ProtocolData> authorid = data.find("/authorid");
//				if(authorid != null){
//					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
//				}
//				
//				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
//				if(ispaypwd != null){
//					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
//				}
//				
//				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
//				if(gesturepasswd != null){
//					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
//				}
//			}
//		}
//	}
	
	
	//----------------------------------------------获取密保问题----------------------------------------------------
//	class GetPwdSafetyTask extends AsyncTask<String, Integer, Boolean>{
//		ProtocolRsp mRsp;
//		@Override
//		protected Boolean doInBackground(String... params) {
//			try {
//				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiSafeGuard", 
//						"getQueList", new CommonData());
//				PwdSafetyGetParser pwdSafetyGetParser = new PwdSafetyGetParser();
//				mRsp = HttpUtil.doRequest(pwdSafetyGetParser, mDatas);
//			} catch (Exception e) {
//				e.printStackTrace();
//				mRsp =null;
//			}
//			
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			super.onPostExecute(result);
//			
//			PromptUtil.dissmiss();
//			if(mRsp==null){
//				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
//			}else{
//				try {
//					List<ProtocolData> mDatas = mRsp.mActions;
//					parserResponse(mDatas);
//					
//					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
//						return;
//					}
//					
//					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						
//					}
//				} catch (Exception e) {
//					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
//				}
//			
//			}
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
//		}
//	}
//	
//	private void parserResponse(List<ProtocolData> mDatas) {
//		ResponseData response = new ResponseData();
//		LoginUtil.mLoginStatus.mResponseData =response;
//		for (ProtocolData data : mDatas) {
//			if (data.mKey.equals(ProtocolUtil.msgheader)) {
//				ProtocolUtil.parserResponse(response, data);
//
//			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
//
//				List<ProtocolData> result = data.find("/result");
//				if (result != null) {
//					LoginUtil.mLoginStatus.result = result.get(0).getmValue();
//				}
//				
//				List<ProtocolData> message = data.find("/message");
//				if (result != null) {
//					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
//				}
//				
//				List<ProtocolData> aupic = data.find("/msgchild");
//				if(aupic!=null)
//				for(ProtocolData child:aupic){
//					PwdSafetyGetData picData = new PwdSafetyGetData();
//					if (child.mChildren != null && child.mChildren.size() > 0) {
//						Set<String> keys = child.mChildren.keySet();
//						for(String key:keys){
//							List<ProtocolData> rs = child.mChildren.get(key);
//							for(ProtocolData item: rs){
//								if(item.mKey.equals("queid")){
//									picData.queid  = item.mValue;
//									
//								}else if(item.mKey.equals("quecontent")){
//									picData.quecontent  = item.mValue;
//									
//								}
//							}
//						}
//					}
//					
//					mList.add(picData);
//				}
//			}
//		}
//	}
	
}
