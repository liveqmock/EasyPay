package com.inter.trade.ui.fragment.checking.pwdsafety;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.AuthorLoginParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.LoginActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.ForgetPasswordFragment;
import com.inter.trade.ui.fragment.RigesterFragment;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyGetData;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyGetParser;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserData;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserParser;
import com.inter.trade.ui.fragment.checking.util.RegistParser;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeDenominationData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeDenominationParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 回答密保问题
 * @author zhichao.huang
 *
 */
public class PwdSafetyReplyFragment extends BaseFragment implements OnClickListener{
	private Button loginButton;
	private Button submitButton;
	private LoginStatus mData = new LoginStatus();
	private ProtocolRsp mRsp;
	private EditText login_name_edit;
	private EditText mobile_number;
	private String name = "";
	private String pwd  = "";
	
	private CheckBox remenber_ck;
	private PwdSafetyTask pwdSafetyTask;
	
	//用于抖动窗口
	private LinearLayout usepwdLayout, usepwdConfirmLayout;
	
	/**
	 * 手机唯一标识
	 */
	private String mobileDeviceId;
	
	/**
	 * 用户注册的手机号码
	 */
	private String phonenumber;
	
	private ArrayList<PwdSafetyValidateUserData> mList = new ArrayList<PwdSafetyValidateUserData>();
	
	public PwdSafetyReplyFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		if(!PwdSafetyReplyActivity.mCleanFlag){
//			setTitle("修改登录密码");
//			phonenumber=PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, "");
//			pwdSafetyTask = new PwdSafetyTask();
//			pwdSafetyTask.execute("");
//			return;
//		}else{
//			setTitle("找回密码");
//		}
		setTitle("找回密码");
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
		String true_name = mobile_number.getText().toString();
		
		if(null == true_name || "".equals(true_name)){
			PromptUtil.showToast(getActivity(), "请输入手机号码");
			runAnimation(usepwdLayout);
			return false;
		}
		
		if(!UserInfoCheck.checkMobilePhone(true_name)){
			PromptUtil.showToast(getActivity(), "手机号码不正确");
			runAnimation(usepwdLayout);
			return false;
		}
		phonenumber = true_name;
		return true;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(pwdSafetyTask!=null){
			pwdSafetyTask.cancel(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if(!PwdSafetyReplyActivity.mCleanFlag){
//			return null;
//		}
		
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.pwdsafety_reply_layout, container, false);
		
		usepwdLayout = (LinearLayout)view.findViewById(R.id.user_pwd_layout);
		mobile_number = (EditText)view.findViewById(R.id.mobile_number_edit);
		submitButton = (Button)view.findViewById(R.id.mobile_submit);
		
		usepwdLayout.setOnClickListener(this);
		submitButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mobile_submit:
			if(checkInput()){
				pwdSafetyTask = new PwdSafetyTask();
				pwdSafetyTask.execute("");
			}
			break;
		default:
			break;
		}
	}
	
	private void pwdSafetyReplyCheckFragment(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new PwdSafetyReplyCheckFragment(mList, phonenumber));
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
	
	class PwdSafetyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData mData = new CommonData();
				mData.putValue("phonenumber", phonenumber);
				
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiSafeGuard", 
						"validateUser", mData);
				PwdSafetyValidateUserParser pwdSafetyValidateUserParser = new PwdSafetyValidateUserParser();
				mRsp = HttpUtil.doRequest(pwdSafetyValidateUserParser, mDatas);
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
				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
//					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.FAIL)) {
//						
//						return;
//					}
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
//						if(!PwdSafetyReplyActivity.mCleanFlag){
//							getActivity().finish();
//						}
						return;
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						query_layout.setVisibility(View.VISIBLE);
//						query_input.setText(mMessage);
						
				
						pwdSafetyReplyCheckFragment();
					}
				} catch (Exception e) {
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}
			
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
	}
	
	private void parserResponse(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData =response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).getmValue();
				}
				
				List<ProtocolData> message = data.find("/message");
				if (result != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				List<ProtocolData> authorid = data.find("/authorid");
				if(authorid != null){
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}
//				List<ProtocolData> msgallcount = data.find("/msgallcount");
//				if(msgallcount != null){
//					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
//				}
//				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
//				if(msgdiscount != null){
//					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
//				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					PwdSafetyValidateUserData picData = new PwdSafetyValidateUserData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("que")){
									picData.que  = item.mValue;
									
								}else if(item.mKey.equals("answer")){
									picData.answer  = item.mValue;
									
								}/**else if(item.mKey.equals("rechapaymoney")){
									
									picData.rechapaymoney  = item.mValue;
									
								}else if(item.mKey.equals("rechamemo")){
									
									picData.rechamemo  = item.mValue;
									
								}else if(item.mKey.equals("rechaisdefault")){
									
									picData.rechaisdefault  = item.mValue;
									
								}/**else if(item.mKey.equals("feemoney")){
									
									picData.feemoney  = item.mValue;
								}else if(item.mKey.equals("state")){
									
									picData.state  = item.mValue;
								}else if(item.mKey.equals("allmoney")){
									
									picData.allmoney  = item.mValue;
								}else if(item.mKey.equals("huancardbank")){
									
									picData.huancardbank  = item.mValue;
									
								}else if(item.mKey.equals("fucardbank")){
									
									picData.fucardbank  = item.mValue;
								}*/
							}
						}
					}
					
					mList.add(picData);
				}
			}
		}
	}

	
}
