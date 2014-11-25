package com.inter.trade.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.LoadUserInfo;
import com.inter.trade.R;
import com.inter.trade.VersionTask;
import com.inter.trade.ui.AgentUserInfoActivity;
import com.inter.trade.ui.BankCardActivity;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.MyBankCardActivity;
import com.inter.trade.ui.fragment.checking.SafetyAccountChangeActivity;
import com.inter.trade.ui.fragment.checking.SafetyLoginActivity;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetySettingActivity;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;

/**
 * 更多
 * @author apple
 *
 */
public class MoreFragment extends BaseFragment implements OnClickListener{
	private RelativeLayout  login_layout;
	private RelativeLayout  pwd_layout;
	private RelativeLayout  userinfo_layout;
	private RelativeLayout  open_layout;
	private RelativeLayout  feedback_layout;
	private RelativeLayout  check_layout;
	private RelativeLayout  help_layout;
	private RelativeLayout  about_layout;
	private RelativeLayout bank_layout;
	private RelativeLayout mybank_layout;
	private RelativeLayout pwdsafety_layout;
	
	private TextView login_text;
	private TextView login_id;
	
	public MoreFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.more_layout, container, false);
		login_layout = (RelativeLayout)view.findViewById(R.id.login_layout);
		pwd_layout = (RelativeLayout)view.findViewById(R.id.pwd_layout);
		userinfo_layout = (RelativeLayout)view.findViewById(R.id.userinfo_layout);
		open_layout = (RelativeLayout)view.findViewById(R.id.open_layout);
		feedback_layout = (RelativeLayout)view.findViewById(R.id.feedback_layout);
		check_layout = (RelativeLayout)view.findViewById(R.id.check_layout);
		help_layout = (RelativeLayout)view.findViewById(R.id.help_layout);
		about_layout = (RelativeLayout)view.findViewById(R.id.about_layout);
		bank_layout = (RelativeLayout)view.findViewById(R.id.bank_layout);
		mybank_layout = (RelativeLayout)view.findViewById(R.id.mybank_layout);
		pwdsafety_layout = (RelativeLayout)view.findViewById(R.id.pwdsafety_layout);
		
		login_text = (TextView)view.findViewById(R.id.login_text);
		login_id  = (TextView)view.findViewById(R.id.login_id);
		if(LoginUtil.isLogin){
			login_text.setText("注销");
			login_id.setText(LoginUtil.mLoginStatus.login_name);
		}else{
			login_text.setText("登录");
		}
		
		about_layout.setOnClickListener(this);
		login_layout.setOnClickListener(this);
		pwd_layout.setOnClickListener(this);
		userinfo_layout.setOnClickListener(this);
		open_layout.setOnClickListener(this);
		feedback_layout.setOnClickListener(this);
		check_layout.setOnClickListener(this);
		help_layout.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		mybank_layout.setOnClickListener(this);
		pwdsafety_layout.setOnClickListener(this);
		return view;
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackVisible();
		setTitle("账户管理");
		if(LoginUtil.isLogin){
			login_text.setText("注销");
			login_id.setText(LoginUtil.mLoginStatus.login_name);
		}else{
			login_text.setText("登录");
			login_id.setText("");
		}
	}

	/**
	 * 设置返回事件
	 */
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
				 FragmentManager manager =
				 getActivity().getSupportFragmentManager();
				 int len = manager.getBackStackEntryCount();
				 if(len>0){
				 manager.popBackStack();
				 }else{
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(!LoginUtil.isLogin){
			showLogin();
			return;
		}
		
		switch (v.getId()) {
		case R.id.userinfo_layout:
			
//			if(MainActivity.currentState == 0){
				if(LoginUtil.isLogin){
					new LoadUserInfo(getActivity()).execute("");
				}
				else{
					showLogin();
//					showUserInfo();
				}
//			}else if(MainActivity.currentState == 1){
////				PromptUtil.showToast(getActivity(), "代理商账户管理");
//				
//				showAgentUserInfo();
//			}
			break;
		case R.id.pwd_layout:
			showPwd();
			break;
		case R.id.open_layout:
			showActive();
			break;
		case R.id.check_layout:
			new VersionTask(getActivity(),false,false).execute("");
			break;
		case R.id.feedback_layout:
			showfeedback();
			break;
		case R.id.about_layout:
			showAout();
			break;
		case R.id.help_layout:
			showHelp();
			break;
		case R.id.login_layout:
			if(!LoginUtil.isLogin){
//				showMore();
				showLogin();
			}else{
				showCancel();
			}
			
			break;
		case R.id.bank_layout:
			showBank();
			break;
		case R.id.mybank_layout:
			showMyBank();
			break;
		case R.id.pwdsafety_layout:
			showPwdSafety();
			break;
		default:
			break;
		}
		
	}
	private void showBank(){
		startActivity(new Intent(getActivity(), BankCardActivity.class));
	}
	private void showMyBank(){
		startActivity(new Intent(getActivity(), MyBankCardActivity.class));
	}
	private void showCancel(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("确认注销当前账户?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				LoginUtil.isLogin=false;
				LoginUtil.mLoginStatus.cancel();
//				cleanNativeUserInfo();
				login_text.setText("登录");
				login_id.setText("");
				dialog.dismiss();
				
				PreferenceConfig.instance(getActivity()).putString(Constants.REQ_TOKEN, "");//清空req_token
				
				Intent intent=new Intent(getActivity(),SafetyAccountChangeActivity.class);
				//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				getActivity().setResult(Constants.ACTIVITY_FINISH);
				getActivity().finish();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	/**
	 * 清除本地保存的用户信息
	 */
	private void cleanNativeUserInfo() {
//    	LoginUtil.mLoginStatus.cancel();
    	PreferenceConfig.instance(getActivity()).putString(Constants.USER_NAME, "");
		PreferenceConfig.instance(getActivity()).putString(Constants.USER_PASSWORD, "");
		PreferenceConfig.instance(getActivity()).putString(Constants.USER_AUTHORID, "");
		PreferenceConfig.instance(getActivity()).putString(Constants.USER_GESTURE_PWD, "");
    }
	
	private void showLogin(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), SafetyLoginActivity.class);
		intent.putExtra("isLoadMain", false);
		startActivity(intent);
	}
	
	private void showAgentUserInfo(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), AgentUserInfoActivity.class);
		startActivity(intent);
	}
	
	private void showMore(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LOGIN_FRAGMENT_INDEX);
		startActivity(intent);
	}
	private void showActive(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_OPEN_INDEX);
		startActivity(intent);
	}
	
	private void showHelp(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_HELP_INDEX);
		startActivity(intent);
	}
	private void showAout(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_ABOUT_INDEX);
		startActivity(intent);
	}
	private void showUserInfo(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LOGIN_FRAGMENT_INDEX);
		startActivity(intent);
	}
	
	private void showfeedback(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_FEEDBACK_INDEX);
		startActivity(intent);
	}
	private void showPwd(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_PWD_INDEX);
		startActivity(intent);
//		getFragmentManager().beginTransaction().replace( R.id.func_container,FragmentFactory.create().createFragment(FragmentFactory.LEFT_PWD_INDEX,null)).addToBackStack(null).commit();
		
	}
	
	private void showPwdSafety(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), PwdSafetySettingActivity.class);
		startActivity(intent);
	}
	
}
