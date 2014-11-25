package com.inter.trade.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalBitmap;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gdseed.mobilereader.MobileReader;
import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.LoadUserInfo;
import com.inter.trade.LoginTask;
import com.inter.trade.NoticeNoTask;
import com.inter.trade.R;
import com.inter.trade.VersionTask;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.NoticeData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.agent.AgentApplyFragmentNew;
import com.inter.trade.ui.fragment.agent.AgentMainContentFragment;
import com.inter.trade.ui.fragment.agent.task.BindAgentTask;
import com.inter.trade.ui.fragment.agent.util.AgentBindTask;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.SafetyRigesterFragment;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserData;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.AppResponseParser;
import com.inter.trade.ui.func.ExtendData;
import com.inter.trade.ui.func.ExtendListParser;
import com.inter.trade.ui.func.FuncXmlUtil;
import com.inter.trade.ui.func.FunctionFragment;
import com.inter.trade.ui.func.TwoFunctionFragment;
import com.inter.trade.ui.func.data.ModleData;
import com.inter.trade.ui.func.task.ModleTask;
import com.inter.trade.ui.func.util.FunctionlUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginTimeoutUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.view.dialog.QuitAppDialog;
import com.inter.trade.view.dialog.QuitAppDialog.QuitAppListener;

public class MainActivity extends FragmentActivity implements OnClickListener,QuitAppListener{
	public static final String TAG = MainActivity.class.getSimpleName();
	public SlidingMenuView slidingMenuView;
	public boolean isShowLeft=false;
	ViewGroup tabcontent;
	FragmentManager mFragmentManager;
	FragmentTransaction mFragmentTransaction;
	
	private RelativeLayout left_main_layout;
	private RelativeLayout left_user_info;
	private RelativeLayout left_pwd_layout;
	private RelativeLayout left_open_layout;
	private RelativeLayout left_feedback_layout;
	private RelativeLayout left_update_layout;
	private RelativeLayout left_about_layout;
	private RelativeLayout left_help_layout;
	private RelativeLayout more_layout;
	private RelativeLayout left_bind_agent;
	private LinearLayout left_bind_agent_layout;
	
	private final static String INTENT_ACTION_CALL_STATE = "com.gdseed.mobilereader.CALL_STATE";
	private MobileReader mobileReader;
	
	
	private ArrayList<ExtendData> mAdsDatas = new ArrayList<ExtendData>();
	private DataListener mDataListener;
//	private IncomingCallServiceReceiver incomingCallServiceReceiver;
	/**
	 * 普通用户的Fragment
	 */
	private FunctionFragment fragment ;
	/**
	 * 代理商用户的Fragment
	 */
	private AgentMainContentFragment agentFragment ;
	/**
	 * 代理商ID
	 */
	private String agentid;
	/**
	 * 记录用户是否重新切换过登录账户，如果已切换switchLogin = true;则在onResume刷新主activity
	 */
	public static boolean switchLogin = false;
	
	private AdTask mAdTask;
	private boolean isExcude = false;
	private LoginStatus mLoginStatus = new LoginStatus();
	
	
	private boolean isLocal=false;//記錄是否採用本地菜單
	
	private Bundle bundle=null;//保存功能菜单的数据
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d("MainActivity", "onCreate");
		super.onCreate(savedInstanceState);
		setTheme(R.style.DefaultLightTheme);
		setContentView(R.layout.activity_main_new);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		fragment = new FunctionFragment();
//		agentFragment = new AgentMainContentFragment();
//		mDataListener = fragment;
		initFragment();
//		showDefaultTab();
//		initLeft();
		
		if(savedInstanceState!=null){
			System.out.println("MainActivity 获取了数据");
			bundle=savedInstanceState.getBundle("key");
			
			//内存不够，自动登录，以恢复登录状态
//			new LoginTask(this, new AsyncLoadWorkListener() {
//				@Override
//				public void onSuccess(Object protocolDataList, Bundle bundle1) {
//				}
//				@Override
//				public void onFailure(String error) {
//					
//				}
//			}).execute("");
			LoginUtil.detection(this);
		}
		
		//是否加载到菜单界面
		boolean loadmenu = getIntent().getBooleanExtra("loadmenu", false);
		
		if(loadmenu){
			replaceCommonTab(0);
		}else{
			if(checkCommonOrAgent ()) {
				if(bundle!=null){
					replaceCommonTab(0);
				}else{
					loadFunc();
				}
			}else{
				replaceCommonTab(1);
			}
		}
		
		
		
//		enterCommonOrAgentPage();
		
//		hideMenu(); 

		if(bundle == null){//内存不够，重启onCreate，不再进行以下操作
			new VersionTask(MainActivity.this, true,false).execute("");//版本更新
			checkPwdSafetyExist ();	//密保检测
		}
		
		checkBindAgentExist ();//代理商绑定检测
		checkShowNoticeExist ();//通知公告检测		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		System.out.println("MainActivity 保存了数据");
		outState.putBundle("key", bundle);
		
	}
	
	private void loadFunc() {
		if(!isLocal){//採用網絡菜單
			ModleTask task=new ModleTask(this, new ResponseStateListener() {

					@Override
					public void onSuccess(Object obj, Class cla) {
						ModleData data=(ModleData)obj;
						if ("1".equals(data.getIsnew())) {// 新功能更新
							FunctionlUtil.getFunctions(data);
							bundle=new Bundle();
							bundle.putSerializable("favour", FunctionlUtil.getFavour());
							bundle.putSerializable("bank", FunctionlUtil.getBank());
							bundle.putSerializable("convince", FunctionlUtil.getConvince());
							replaceCommonTab(0);
						}
					}
				},true);
			task.execute("0", "16.1");
		}else{//採用本地菜單
			
		}
	}
//	
//	/**
//	 * 判断当前用户是进入普通页面还是代理商页面
//	 */
//	private void enterCommonOrAgentPage() {
//		
//		if(agentid == null || "".equals(agentid) || Integer.parseInt(agentid) <=0) {
//			//进入普通用户界面
//			showDefaultTab();
//		}else{
//			if(Integer.parseInt(agentid) > 0) {
//				//进入代理商页面
//				replaceFragmentTab(agentFragment);
//			}
//		}
//	}
	
	private void initLeft()
	{
		left_main_layout = (RelativeLayout)findViewById(R.id.left_main_layout);
		left_user_info = (RelativeLayout)findViewById(R.id.left_user_info);
		left_pwd_layout = (RelativeLayout)findViewById(R.id.left_pwd_layout);
		left_open_layout = (RelativeLayout)findViewById(R.id.left_open_layout);
		left_feedback_layout = (RelativeLayout)findViewById(R.id.left_feedback_layout);
		left_update_layout = (RelativeLayout)findViewById(R.id.left_update_layout);
		left_about_layout = (RelativeLayout)findViewById(R.id.left_about_layout);
		left_help_layout = (RelativeLayout)findViewById(R.id.left_help_layout);
		more_layout = (RelativeLayout)findViewById(R.id.more_layout);
		left_bind_agent = (RelativeLayout)findViewById(R.id.left_bind_agent);
		left_bind_agent_layout = (LinearLayout)findViewById(R.id.left_bind_agent_layout);
		findViewById(R.id.rl_download).setOnClickListener(this);
		
		left_main_layout.setOnClickListener(this);
		//left_user_info.setOnClickListener(this);
		//left_pwd_layout.setOnClickListener(this);
		//left_open_layout.setOnClickListener(this);
		left_feedback_layout.setOnClickListener(this);
		left_update_layout.setOnClickListener(this);
		left_about_layout.setOnClickListener(this);
		left_help_layout.setOnClickListener(this);
		more_layout.setOnClickListener(this);
		left_bind_agent.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//PromptUtil.showQuit(this);
			QuitAppDialog dialog=new QuitAppDialog();
			dialog.show(this, this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Logger.d("MainActivity", "onDestroy");
		super.onDestroy();
		if(mAdTask!=null){
			mAdTask.cancel(true)
			;
		}
		if(checkPwdSafetyTask!=null){
			checkPwdSafetyTask.cancel(true)
			;
		}
		//销毁超时实例
		LoginTimeoutUtil.get().onDestroy();
		//imageload onDestroy()
		FinalBitmap.create(this).onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		mAdTask = new AdTask();
//		mAdTask.execute("");
		
		
//		if(agentid == null || "".equals(agentid) || Integer.parseInt(agentid) <=0) {
////			加载本地功能菜单，已去除从网络获取菜单
//			mDataListener.notify(mAdsDatas);
//		}
		/*if(switchLogin) {
			if(checkCommonOrAgent ()) {
				replaceCommonTab(0,null);
			}else{
				replaceCommonTab(1,null);
			}
			switchLogin = false;
		}*/
		
	}
	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if(VersionUtil.isNeedUpdate(this)){
//			VersionUtil.showUpdate(this);
//			return;
//		}
		
		if(!LoginUtil.isLogin){
			showLogin();
			return;
		}
		
		switch (v.getId()) {
		case R.id.left_main_layout:
			hideMenu();
//			HttpUtil.getRequest(ProtocolUtil.getSmsCode());
//			getTest();
			break;
		case R.id.left_user_info:
			if(LoginUtil.isLogin){
				new LoadUserInfo(this).execute("");
			}
			else{
				showUserInfo();
			}
			break;
		case R.id.left_pwd_layout:
			showPwd();
			break;
		case R.id.left_open_layout:
			showActive();
			break;
		case R.id.left_update_layout:
			new VersionTask(this,false,false).execute("");
			break;
		case R.id.left_feedback_layout:
			showfeedback();
			break;
		case R.id.left_about_layout:
			showAout();
			break;
		case R.id.left_help_layout:
			showHelp();
			break;
		case R.id.more_layout:
			showMore();
			break;
		case R.id.left_bind_agent:
			showBindAgent();
			break;
		case R.id.rl_download://下载
			showDownload();
			break;

		default:
			break;
		}
		
	}
	private void showDownload() {
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_DOWNLOAD);
		startActivity(intent);
	}

	private void showLogin(){
		Intent intent = new Intent();
		//进入手势密码登录页面
        intent.setClass(this, LockActivity.class);
        intent.putExtra("isLoadMain", false);
		startActivity(intent);
	}
	/**
	 * 服务信息（绑定代理）
	 */
	private void showBindAgent(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_BIND_AGENT_INDEX);
		startActivity(intent);
	}
	private void showMore(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_MORE_INDEX);
		startActivityForResult(intent, 0);
	}
	private void showActive(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_OPEN_INDEX);
		startActivity(intent);
	}
	
	private void showHelp(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_HELP_INDEX);
		startActivity(intent);
	}
	private void showAout(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_ABOUT_INDEX);
		startActivity(intent);
	}
	private void showUserInfo(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LOGIN_FRAGMENT_INDEX);
		startActivity(intent);
	}
	
	private void showfeedback(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_FEEDBACK_INDEX);
		startActivity(intent);
	}
	private void showPwd(){
		Intent intent = new Intent();
		intent.setClass(this, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LEFT_PWD_INDEX);
		startActivityForResult(intent, 3);
	}

	private void initFragment() {
		mFragmentManager = getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
	}

	public void hideMenu() {
		slidingMenuView.snapToScreen(1);
		isShowLeft= false;
	}

	public void showLeftMenu() {
		slidingMenuView.snapToScreen(0);
		isShowLeft=true;
	}

	public void showRightMenu(View view) {
		slidingMenuView.snapToScreen(2);
	}
	public int getScreen(){
		return slidingMenuView.getCurrentScreen();
	}
	
//	public void showDefaultTab() {
//		Logger.d("FunctionFragment","new FunctionFragment()");
//		mFragmentTransaction.add(R.id.sliding_body, fragment);
////		mFragmentTransaction.add(R.id.sliding_body, new IndexFragment());
//		mFragmentTransaction.commit();
//		
//		showCommonOrAgentLeftMenu();
//	}
//	public void replaceDefaultTab() {
//		Logger.d("FunctionFragment","new FunctionFragment()");
//		mFragmentTransaction.replace(R.id.sliding_body, new FunctionFragment());
////		mFragmentTransaction.add(R.id.sliding_body, new IndexFragment());
//		mFragmentTransaction.commit();
//	}
	
	/**
	 * 
	 * @param state 0:普通； 1：代理商 2:普通(定位在最爱)
	 */
	public static int currentState = 0;
	public void replaceCommonTab(int state) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if(state == 0) {
			if(bundle!=null){
				transaction.replace(R.id.sliding_body, TwoFunctionFragment.createFragment(bundle));
			}else{
				loadFunc();
			}
			
		}else if(state == 1){
//			transaction.replace(R.id.sliding_body, new AgentMainContentFragment(String.format("%06d", Integer.parseInt(agentid))));//020001
			transaction.replace(R.id.sliding_body, new AgentMainContentFragment());
		}else if(state==2){
			if(bundle!=null){
				bundle.putInt("selected", 0);
				transaction.replace(R.id.sliding_body, TwoFunctionFragment.createFragment(bundle));
			}else{
				loadFunc();
			}
		}else{
			if(bundle!=null){
				transaction.replace(R.id.sliding_body, TwoFunctionFragment.createFragment(bundle));
			}else{
				loadFunc();
			}
		}
		transaction.commit();
		currentState = state;
		showCommonOrAgentLeftMenu();
	}
	
	/*
	 * 代理商申请页面
	 */
	public void replaceAgentApply() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.sliding_body, new AgentApplyFragmentNew());//测试
		transaction.commit();
		showCommonOrAgentLeftMenu();
	}
	
	/**
	 * 切换Fragment
	 * @param fragment
	 */
	void replaceFragmentTab(Fragment fragment) {
		mFragmentTransaction.replace(R.id.sliding_body, fragment);
		mFragmentTransaction.commit();
		
		showCommonOrAgentLeftMenu();
	}
	
	/**
	 * 检测当前是普通用户还是代理商
	 * @return true :普通用户；false：代理商
	 */
	boolean checkCommonOrAgent (){
		agentid = LoginUtil.mLoginStatus.agentid;
		
		Logger.d("checkCommonOrAgent", "agentid=="+agentid);
		
		if(agentid == null){
			agentid=PreferenceConfig.instance(MainActivity.this).getString(Constants.AGENT_ID, "0");
			LoginUtil.mLoginStatus.agentid = agentid;
			Logger.d("PreferenceConfig", "agentid=="+agentid);
		}
		
		if(agentid == null || "".equals(agentid) || Integer.parseInt(agentid) <=0) {
			return true;
		}else{
			if(Integer.parseInt(agentid) > 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 根据登录状态是否是代理商/普通用户 来展示左侧边栏的菜单
	 */
	void showCommonOrAgentLeftMenu() {
		
		if(currentState == 0) {
			//普通用户界面
			if(left_bind_agent != null) {
				left_bind_agent_layout.setVisibility(View.VISIBLE);
			}
		}else if(currentState == 1){
			//代理商页面
			if(left_bind_agent != null) {
				left_bind_agent_layout.setVisibility(View.GONE);
			}
		}
	}
	
	class AdTask extends AsyncTask<String, Integer, Boolean> {
		private ProtocolRsp mRsp;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			// UPLOAD_URL =
			// LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				CommonData data = new CommonData();
				String keyCode ="0";
				if(PayApp.mKeyCode != null){
					keyCode = PayApp.mKeyCode;
				}
				data.putValue("paycardkey", keyCode);
				data.putValue("appversion", 
						PreferenceConfig.instance(MainActivity.this).getString(Constants.FUNC_ITEM_KEY, ""));
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiAppInfo", "readMenuModule", data);
				Logger.d("parser", "read from response");
				ExtendListParser authorRegParser = new ExtendListParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			 PromptUtil.dissmiss();
			try {
				isExcude = true;
				mAdsDatas.clear();
				if (mRsp != null) {
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas,false);

				} 

				//错误处理
				if(!ErrorUtil.create().errorDeal(mLoginStatus,MainActivity.this)){
					Log.i(TAG, "errorDeal");
//					initLocalData();
					if(mDataListener != null){
						Log.i(TAG, "errorDeal mDataListener.notify(mAdsDatas)");
						mDataListener.notify(mAdsDatas);
					}
					return;
				}
				//如果返回数据为0，那么从本地读取
				if(mAdsDatas.size()==0){
					ProtocolRsp rsp= FuncXmlUtil.readXML(FuncXmlUtil.getFuncXml());
					List<ProtocolData> datas=null;
					if(rsp !=null){
						datas = rsp.mActions;
					}
					if(datas!= null){
						parserResoponse(datas,true);
					}
				}
				
				Logger.d("login","功能菜单："+mLoginStatus.mResponseData.getReq_token());
				if(mDataListener != null){
					mDataListener.notify(mAdsDatas);
				}
////				Logger.d("FunctionFragment","new FunctionFragment()");
//				mFragmentTransaction.add(R.id.sliding_body, FunctionFragment.createFragment(mAdsDatas));
////				mFragmentTransaction.add(R.id.sliding_body, new IndexFragment());
//				mFragmentTransaction.commit();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				// PromptUtil.showToast(getActivity(),getString(R.string.net_error));
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(!isExcude)
			 PromptUtil.showDialog(MainActivity.this, "正在初始化...");
		}

	}
	
	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params,boolean islocal) {
		ResponseData response = new ResponseData();
//		LoginUtil.mLoginStatus.mResponseData = response;
		mLoginStatus.mResponseData = response;
		mAdsDatas.clear();
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserNotoken(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					mLoginStatus.result = result1.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					mLoginStatus.message = message.get(0).mValue;
				}
				List<ProtocolData> isupdate = data.find("/isnew");
				String isnew = "";
				if (isupdate != null) {
					isnew = isupdate.get(0).mValue;
				}
				/**
				 * 如果有更新将数据写入本地
				 */
				if(!islocal){
					if(isnew.equals("1")){
						
						ProtocolParser parser = ProtocolParser.instance();
						parser.setParser(new AppResponseParser());
						FuncXmlUtil.writeToXml(FuncXmlUtil.getFuncXml(), parser.aToXml(params));
					}
				}
				
				
				List<ProtocolData> vsn = data.find("/version");
				String version = "";
				if (vsn != null) {
					version = vsn.get(0).mValue;
					PreferenceConfig.instance(this).putString(Constants.FUNC_ITEM_KEY,
							version);
				}
				

				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic ==null){
					return;
				}
				for (ProtocolData child : aupic) {
					ExtendData picData = new ExtendData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for (String key : keys) {
							List<ProtocolData> rs = child.mChildren.get(key);
							for (ProtocolData item : rs) {
								if (item.mKey.equals("mnuname")) {
									picData.mnuname = item.mValue;

								} else if (item.mKey.equals("mnupic")) {
									picData.mnupic = item.mValue;

								} else if (item.mKey.equals("mnuorder")) {

									picData.mnuorder = item.mValue;
								} else if (item.mKey.equals("mnuurl")) {

									picData.mnuurl = item.mValue;
								}else if (item.mKey.equals("mnuid")) {

									picData.mnuid = item.mValue;
								}
							}
						}
					}

					mAdsDatas.add(picData);
				}

			}
		}
	}
	
	/**
	 * 检查当前用户是否绑定过代理商，即激活码（服务代号）
	 */
	private void checkBindAgentExist () {
		Logger.d("checkBindAgentExist", "function call start");
		String isRelateAgent = PreferenceConfig.instance(MainActivity.this).getString(Constants.IS_BIND_AGENT, "0");
		if(!("1".equals(isRelateAgent))){
			/**
			 * 3.1.0版本以后，如未绑定代理商号，默认绑定总部“020001”，不再弹窗（仍需考虑网络问题，确保绑定成功）
			 */
			new AgentBindTask(this, "020001", null).execute("");
			
			/**
			 * 3.0.0版本及以前，如未绑定代理商号，需要弹窗绑定
			 */
//			PromptUtil.showBindAgentDialog(MainActivity.this);
			
			
			//检查本地是否有代理商代号的配置文件，有就绑定，没有默认绑定020001
//			String agentID=PreferenceConfig.instance(this).getString(Constants.AGENT_NO_TO_BIND, "020001");
//			new BindAgentTask(this,null).execute(agentID);
		}
		Logger.d("checkBindAgentExist", "function call end");
		
		/*
		if(!("1".equals(isRelateAgent))){
			PromptUtil.showBindAgentDialog(MainActivity.this);
//			new AgentBindTask(this, "010888", null).execute("");
//			new AgentBindTask(this, "010333", null).execute("");
//			new AgentBindTask(this, "010666", null).execute("");
		}
		*/
	}
	
	/**
	 * 检查是否弹出通知公告
	 */
	private void checkShowNoticeExist () {
		Logger.d("checkShowNoticeExist", "function call start");
		String isnotice = PreferenceConfig.instance(MainActivity.this).getString(Constants.IS_SHOWED_NOTICE, "0");
		if(!("1".equals(isnotice))){
			List<NoticeData> mNoticeList = new ArrayList<NoticeData>();
			new NoticeNoTask(MainActivity.this, mNoticeList, "ApiAppInfo", "readNewNotice", new AsyncLoadWorkListener() {
					@Override
					public void onSuccess(Object protocolDataList, Bundle bundle1) {
						try{
							List<NoticeData> noticeList = (List<NoticeData>)protocolDataList;
							if(noticeList.size()>0){
								NoticeData noticedata = noticeList.get(0);
								if(noticedata != null && noticedata.getNoticecontent() != null && !("".equals(noticedata.getNoticecontent()))){
									PreferenceConfig.instance(MainActivity.this).putString(Constants.IS_SHOWED_NOTICE, "1");
									PromptUtil.showNoticeDialog(noticedata.getNoticetitle()+"", noticedata.getNoticecontent()+"", null, null, MainActivity.this);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					@Override
					public void onFailure(String error) {
						
					}
			}).execute("");
		}
		Logger.d("checkShowNoticeExist", "function call end");
	}
	
	/**
	 * 检查当前用户是否设置过密保，
	 */
	private CheckPwdSafetyTask checkPwdSafetyTask;
	private void checkPwdSafetyExist () {
		boolean is_set_pwdsafety = PreferenceConfig.instance(MainActivity.this).getBoolean(Constants.IS_SET_PWDSAFETY, false);
		if(!is_set_pwdsafety) {
			checkPwdSafetyTask = new CheckPwdSafetyTask();
			checkPwdSafetyTask.execute("");
		}
	}
	
	/**
	 * 检查是否设置过密保
	 * @author zhichao.huang
	 *
	 */
	class CheckPwdSafetyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData mData = new CommonData();
				String user_name = PreferenceConfig.instance(MainActivity.this).getString(Constants.USER_NAME, "");
				if(user_name == null || "".equals(user_name)) return null;
				mData.putValue("phonenumber", user_name);
				
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
			
			//PromptUtil.dissmiss();
			if(mRsp==null){
//				PromptUtil.showToast(MainActivity.this, getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
//					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,MainActivity.this)){
//						return;
//					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						//用户已设过密保，则把状态保存到本地，下次就不用去网络请求。
						PreferenceConfig.instance(MainActivity.this).putBoolean(Constants.IS_SET_PWDSAFETY, true);
					}else if (LoginUtil.mLoginStatus.result.equals(ProtocolUtil.FAIL)){
						boolean isset = PreferenceConfig.instance(MainActivity.this).getBoolean(Constants.IS_SET_PWDSAFETY, false);
						if(!isset) {
							PromptUtil.showPwdSafetyDialog(MainActivity.this);
						}
					}
				} catch (Exception e) {
//					PromptUtil.showToast(MainActivity.this,getString(R.string.req_error));
				}
			
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
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
									
								}
							}
						}
					}
					
				}
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if(resultCode == Constants.ACTIVITY_FINISH){
			setResult(Constants.ACTIVITY_FINISH);
			finish();
		}
	}
	
	public static interface DataListener{
		public void notify(ArrayList<ExtendData> extendDatas);
	}

	@Override
	public void onPositive() {//退出应用
		//退出应用可下次进入再弹出通知公告
		PreferenceConfig.instance(this).putString(Constants.IS_SHOWED_NOTICE, "0");
		finish();
		System.exit(0);		
	}
}
