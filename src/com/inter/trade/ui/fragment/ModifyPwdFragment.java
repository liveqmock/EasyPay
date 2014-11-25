package com.inter.trade.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.trade.ui.fragment.ReadPwdProtectionTask;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.LockSetupActivity;
import com.inter.trade.ui.fragment.checking.SafetyAccountChangeActivity;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetyReplyActivity;
import com.inter.trade.ui.fragment.checking.util.AuthorPwdModifyParser;
import com.inter.protocol.body.ModifyAuthorInfoParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.UserInfoActivity;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 修改密码
 * @author apple
 *
 */
public class ModifyPwdFragment extends BaseFragment implements OnClickListener{
	private Button modify_btn;
	private Button gesture_modify_btn;
	private Button mibao_modify_btn;
	private static final String MODIFY_INDEX="MODIFY_INDEX";
	private int mType = 0;
	
	private TextView current_pwd_tv;
	private EditText current_pwd_edit;
	private TextView new_pwd_tv;
	private EditText new_pwd_edit;
	private TextView confirm_new_pwd_tv;
	private EditText confirm_new_pwd_edit;
	
	private String mCurrentPwd;
	private String mNewPwd;
	private String mConfirmPwd;
	
	private LinearLayout current_layout;
	private ReadPwdProtectionTask mReadPwdProtectionTask;
	public static ModifyPwdFragment createFragment(int index){
		ModifyPwdFragment f = new ModifyPwdFragment();
		Bundle args = new Bundle();
		args.putInt(MODIFY_INDEX, index);
		f.setArguments(args);
		return f;
	}
	
	public ModifyPwdFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int temp = getArguments()==null?null:Integer.parseInt(getArguments().get(MODIFY_INDEX).toString());
			if(temp == 2){
				setTitle("修改登录密码");
			}/**else{
				if(LoginUtil.mLoginStatus.ispaypwd.equals("0")){
					setTitle("设置支付密码");
					//已经设置当前密码
				}else{
					setTitle("修改支付密码");
				}
				
			}*/
			mType = temp;
		
	}
	
	/**
	 * 设置返回事件
	 */
	protected void setBackVisible() {
		if (getActivity() == null) {
			return;
		}
		back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager manager = getFragmentManager();
				int len = manager.getBackStackEntryCount();
				if (len > 0) {
					manager.popBackStack();
				} else {
					getActivity().finish();
				}
			}
		});

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
	}


	@Override
	public void onResume() {
		super.onResume();
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.modify_pwd_layout, container, false);
		initView(view);
//		if(mType == 1){
//			initPay();
//		}
		return view;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mReadPwdProtectionTask != null){
			mReadPwdProtectionTask.cancel(true);
		}
	}
	
	private void initView(View v){
		current_pwd_tv = (TextView)v.findViewById(R.id.current_pwd_tv);
		current_pwd_edit = (EditText)v.findViewById(R.id.current_pwd_edit);
		new_pwd_tv = (TextView)v.findViewById(R.id.new_pwd_tv);
		new_pwd_edit = (EditText)v.findViewById(R.id.new_pwd_edit);
		confirm_new_pwd_tv = (TextView)v.findViewById(R.id.confirm_new_pwd_tv);
		confirm_new_pwd_edit = (EditText)v.findViewById(R.id.confirm_new_pwd_edit);
		modify_btn = (Button)v.findViewById(R.id.modify_btn);
		mibao_modify_btn = (Button)v.findViewById(R.id.mibao_modify_btn);
		gesture_modify_btn = (Button)v.findViewById(R.id.gesture_modify_btn);
		
		current_layout = (LinearLayout)v.findViewById(R.id.current_layout);
		
		modify_btn.setOnClickListener(this);
		gesture_modify_btn.setOnClickListener(this);
		mibao_modify_btn.setOnClickListener(this);
		
		TextView tv =(TextView) v.findViewById(R.id.pwd_prompt);
		tv.setText("账户:  "+PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, ""));
	}
	
	private void initPay(){
		//未设置当前密码
		if(LoginUtil.mLoginStatus.ispaypwd.equals("0")){
//			current_pwd_tv.setText("当前支付密码");
			
			current_layout.setVisibility(View.GONE);
			current_pwd_tv.setVisibility(View.GONE);
			current_pwd_edit.setVisibility(View.GONE);
			//已经设置当前密码
		}else{
			current_layout.setVisibility(View.VISIBLE);
			current_pwd_tv.setVisibility(View.VISIBLE);
			current_pwd_edit.setVisibility(View.VISIBLE);
		}
		
		new_pwd_tv.setText("新支付密码");
		confirm_new_pwd_tv.setText("确认支付密码");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit:
			submit();
			break;
		case R.id.modify_btn:
			if(checkInput()){
				modify();
			}
			
			break;

		case R.id.gesture_modify_btn:
			Intent lockIntent = new Intent();
			lockIntent.putExtra("isLoadMain", false);
			lockIntent.putExtra("isGestureModifyPwd", true);
			lockIntent.setClass(getActivity(), LockActivity.class);
//			手势密码输错5次，不能修改登录密码，LockActivity销毁，本Activity也跟着销毁，然后跳到登录页面
//			startActivityForResult(lockIntent, 3);
			getActivity().startActivity(lockIntent);
			break;
			
		case R.id.mibao_modify_btn:
			if(mReadPwdProtectionTask != null){
				mReadPwdProtectionTask.cancel(true);
			}
			mReadPwdProtectionTask = new ReadPwdProtectionTask(getActivity(), PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, ""));
			mReadPwdProtectionTask.execute("");
//			Intent pwdIntent = new Intent();
//			pwdIntent.putExtra(Constants.CLEAN_FLAG, false);
//			pwdIntent.setClass(getActivity(), PwdSafetyReplyActivity.class);
//			getActivity().startActivity(pwdIntent);
			break;
			
		default:
			break;
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if(resultCode == Constants.ACTIVITY_FINISH){
			getActivity().setResult(Constants.ACTIVITY_FINISH);
			getActivity().finish();
		}
	}
	
	private boolean checkInput(){
		boolean flag = true;
		//修改支付密码
//		if(mType == 1){
//			flag = checkPayPwd();
//		}else{
			flag = checkLoginPwd();
//		}
		return flag;
	}
	private boolean checkPayPwd(){
		if(!LoginUtil.mLoginStatus.ispaypwd.equals("0")){
			String temp = current_pwd_edit.getText().toString();
			
			if(null == temp || "".equals(temp)){
				PromptUtil.showToast(getActivity(), "请输入当前支付密码");
				return false;
			}
			if(!UserInfoCheck.checkPassword(temp)){
				PromptUtil.showToast(getActivity(), "当前支付格式不正确");
				return false;
			}
			mCurrentPwd = temp;
		}
		
		String new_pwd = new_pwd_edit.getText().toString();
		if(null == new_pwd || "".equals(new_pwd)){
			PromptUtil.showToast(getActivity(), "请输入新支付密码");
			return false;
		}
		if(!UserInfoCheck.checkPassword(new_pwd)){
			PromptUtil.showToast(getActivity(), "新支付密码格式不正确");
			return false;
		}
		mNewPwd = new_pwd;
		String confirm_pwd = confirm_new_pwd_edit.getText().toString();
		if(null == confirm_pwd || "".equals(confirm_pwd)){
			PromptUtil.showToast(getActivity(), "请确认新支付密码");
			return false;
		}
		if(!UserInfoCheck.checkPassword(new_pwd)){
			PromptUtil.showToast(getActivity(), "确认新支付密码格式不正确");
			return false;
		}
		mConfirmPwd = confirm_pwd;
		if(!confirm_pwd.equals(new_pwd)){
			PromptUtil.showToast(getActivity(), "新支付密码不同，请重新输入");
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * 检测登录密码输入
	 * @return
	 */
	private boolean checkLoginPwd(){
		String temp = current_pwd_edit.getText().toString();
		if(null == temp || "".equals(temp)){
			PromptUtil.showToast(getActivity(), "请输入当前密码");
			return false;
		}
		if(!UserInfoCheck.checkPassword(temp)){
			PromptUtil.showToast(getActivity(), "当前密码格式不正确");
			return false;
		}
		mCurrentPwd = temp;
		
		String new_pwd = new_pwd_edit.getText().toString();
		if(null == new_pwd || "".equals(new_pwd)){
			PromptUtil.showToast(getActivity(), "请输入新密码");
			return false;
		}
		if(!UserInfoCheck.checkPassword(new_pwd)){
			PromptUtil.showToast(getActivity(), "新密码格式不正确");
			return false;
		}
		
		if(mCurrentPwd.equals(new_pwd)){
			PromptUtil.showToast(getActivity(), "新旧密码相同，请重新输入");
			return false;
		}
		
		mNewPwd = new_pwd;
		String confirm_pwd = confirm_new_pwd_edit.getText().toString();
		if(null == confirm_pwd || "".equals(confirm_pwd)){
			PromptUtil.showToast(getActivity(), "请确认新密码");
			return false;
		}
		if(!UserInfoCheck.checkPassword(confirm_pwd)){
			PromptUtil.showToast(getActivity(), "确认密码格式不正确");
			return false;
		}
		mConfirmPwd = confirm_pwd;
		if(!confirm_pwd.equals(new_pwd)){
			PromptUtil.showToast(getActivity(), "新密码不同，请重新输入");
			return false;
		}
		return true;
	}
	private void modify(){
		new ModifyTask().execute("");
	}
	private void submit(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new RigesterInfoFragment());
		transaction.commit();
	}
	
	class ModifyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp=null;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			List<ProtocolData> mDatas = getRequestDatas();
			AuthorPwdModifyParser authorRegParser = new AuthorPwdModifyParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
				if(mRsp != null){
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas);
					//正确
					String retCode = LoginUtil.mLoginStatus.mResponseData.getRettype();
					if (retCode.equals(
							ProtocolUtil.HEADER_SUCCESS)) {
						if (LoginUtil.mLoginStatus.result
								.equals(ProtocolUtil.SUCCESS)) {
							
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.mResponseData.getRetmsg());
							Intent intent = new Intent();
				    		intent.setClass(getActivity(), SafetyAccountChangeActivity.class);
				    		intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				    		startActivity(intent);
				    		getActivity().finish();
						}else{
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.message);
						}
					}else if(retCode.equals(ProtocolUtil.REAL_ERROR)){
						//操作超时
//						PromptUtil.showRealFail(getActivity());
					}else{
						PromptUtil.showToast(getActivity(),
								LoginUtil.mLoginStatus.mResponseData.getRetmsg());
					}
				}else {
					//异常
					PromptUtil.showToast(getActivity(), getString(R.string.net_error));
				}
			} catch (Exception e) {
				// TODO: handle exception
				Logger.e(e);
				PromptUtil.showToast(getActivity(),getString(R.string.req_error));
			}
			
		}


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
	}
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
//		data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
		if(mCurrentPwd==null){
			mCurrentPwd = mNewPwd;
		}
		data.putValue("auoldpwd", mCurrentPwd);
		data.putValue("aunewpwd", mConfirmPwd);
//		data.putValue("aurenewpwd", mConfirmPwd);
//		String type = "";
//		if(mType == 1){
//			type ="2";//支付密码
//		}else{
//			type ="1";//登录密码
//		}
		data.putValue("aumoditype", "2");
		data.putValue("reset", "0");//如果为0，将判断用户输入的旧密码是否正确，只有用户输入了正确的旧密码，才会设置新的密码;如果为1，将无视用户输入的旧密码正确与否，直接设置新的密码
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfoV2",
				"authorPwdModify", data);
		return mDatas;
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
