package com.inter.trade.ui.fragment.agent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.AgentApplyNewActivity;
import com.inter.trade.ui.AgentQueryActivity;
import com.inter.trade.ui.AgentReplenishActivity;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.task.ReadAgentInfoTask;
import com.inter.trade.ui.fragment.agent.util.AgentData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;

@SuppressLint("ValidFragment")
public class AgentMainContentFragment extends BaseFragment implements OnClickListener,ResponseStateListener{
	private RelativeLayout activity_title_layout;
	private LinearLayout agent_main_mytfb, agent_main_query, agent_main_replenish, agent_apply_btn_layout;
	private ImageView agent_main_mytfb_img, agent_main_query_img, agent_main_replenish_img;
	private TextView agent_main_mytfb_tv, agent_main_query_tv, agent_main_replenish_tv;
	private TextView agent_id_tv, todayfenrun, areafenrun, salepaycardnum45, salepaycardnum20, areapaycardnum, areaauthornum;
	
	private String mBkntno;
	
	private ReadAgentInfoTask mBuyTask;
//	private DaikuanActivity mActivity;
	
	private String agenttypeid;
	
	/*
	 * 是否正式代理商
	 */
	private boolean mIsRealAgent=true;
	
	/*
	 * 代理商代号，即激活码，正式为6位数字，虚拟为4位数字
	 */
	private String mAgentId="";
	
	private Bundle bundle;
	private AgentData agentData = new AgentData();
	
	public static AgentMainContentFragment create(String agentid){
		return new AgentMainContentFragment(agentid);
	}
	
	public AgentMainContentFragment()
	{
	}
	
	public AgentMainContentFragment(String agentid) {
		this.mAgentId = agentid;
	}
	
	public AgentMainContentFragment(Bundle b) {
		this.bundle = b;
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
		//LoginUtil.detection(getActivity());
		int layoutId;
		agenttypeid = LoginUtil.mLoginStatus.agenttypeid;
		Logger.d("AgentMainContentFragment", "agenttypeid=="+agenttypeid);
		if(agenttypeid == null){
			agenttypeid=PreferenceConfig.instance(getActivity()).getString(Constants.AGENT_TYPE_ID, "1");
			LoginUtil.mLoginStatus.agenttypeid = agenttypeid;
			Logger.d("PreferenceConfig", "agenttypeid=="+agenttypeid);
		}
		
		if(agenttypeid != null && "1".equals(agenttypeid)){//正式代理
			mIsRealAgent=true;
			layoutId = R.layout.agent_main_content_layout;
		}else if(agenttypeid != null && "2".equals(agenttypeid)){//虚拟代理
			mIsRealAgent=false;
			layoutId = R.layout.agent_main_content_temp_layout;
		}else{
			mIsRealAgent=true;
			agenttypeid="1";
			layoutId = R.layout.agent_main_content_layout;
		}
		View v = inflater.inflate(layoutId, container,false);
		
		/*
		 * 宿主Activity的标题栏隐藏
		 */
		setActivityTitleVisible(View.GONE);
		hideTitleLine();
		
		
		final RelativeLayout title_layout = (RelativeLayout) v.findViewById(R.id.agent_title_layout);
		
		ImageView agent_title_img = (ImageView)title_layout.findViewById(R.id.agent_title_img);
		agent_title_img.setVisibility(View.VISIBLE);
		
		//购买汇通卡
		setTitlebarRightButton(title_layout);
		
		initView(v);
		
		mBuyTask = new ReadAgentInfoTask(getActivity(),this);
		mBuyTask.execute("");
		return v;
	}
	
	/**
	 * 超时 再次登录成功后该方法得到调用
	 */
	@Override
	public void onTimeout() {
		mBuyTask = new ReadAgentInfoTask(getActivity(),this);
		mBuyTask.execute("");
	}
	
	/**
	 * 设置标题栏右边按钮
	 * @param view
	 */
	private void setTitlebarRightButton(View view) {
		
		Button buyCardButton = (Button) view.findViewById(R.id.title_right_btn);
		buyCardButton.setVisibility(View.VISIBLE);
		buyCardButton.setBackgroundColor(Color.parseColor("#00000000"));
		buyCardButton.setText("订卡");
		
		
		buyCardButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), UIManagerActivity.class);
				intent.putExtra("targetFragment",
						UIConstantDefault.UI_CONSTANT_BUY_HTBCARD_MAIN
				);
				startActivity(intent);
			}
		});
	}
	
	protected void setActivityTitleVisible(int vis) {
		if (getActivity() == null) {
			return;
		}
		activity_title_layout = (RelativeLayout) getActivity().findViewById(R.id.title_layout);
		if(activity_title_layout == null){
			return;
		}
		activity_title_layout.setVisibility(vis);
		
	}
	
	protected void setMenuTitleVisible() {
		if (getActivity() == null) {
			return;
		}
		Button back = (Button) getActivity().findViewById(R.id.title_back_btn);
		Button menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		Button right = (Button) getActivity().findViewById(R.id.title_right_btn);
		TextView title = (TextView) getActivity().findViewById(R.id.title_name);
		ImageView agent_title_img = (ImageView) getActivity().findViewById(R.id.agent_title_img);
		
		if (menu == null || back == null || right == null || title == null || agent_title_img == null) {
			return;
		}
		agent_title_img.setVisibility(View.VISIBLE);
		menu.setVisibility(View.VISIBLE);
		menu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showMore();
//				getActivity().finish();
			}
		});

		back.setVisibility(View.GONE);
		right.setVisibility(View.GONE);
		title.setVisibility(View.GONE);
	}
	
	public void showMore() {
		CharSequence[] items = { "注销", "返回" };
		new AlertDialog.Builder(getActivity()).setTitle("更多")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							agentLogout();
							dialog.dismiss();
						} else if (which == 1) {
							dialog.dismiss();
						} else {
//							showBigPicure();
						}
					}
				}).create().show();
	}
	
	private void agentLogout(){
		new AlertDialog.Builder(
			getActivity())
			.setTitle("注销")
			.setMessage("是否注销？")
			.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						doAgentLogout();
						dialog.dismiss();
					}
				})
			.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				}).create().show();
	}
	
	private void agentBack(){
		
	}
	
	private void doAgentLogout()
	{
		//此处要写代码：登录状态修改等处理
		
		//注销
		LoginUtil.isAgentLogin = false;
		AgentToMainTFB();
	}
	
	private void initView(View view){
		/*
		 * 
		 */
//		agent_id_tv = (TextView)view.findViewById(R.id.agent_id_tv);
//		agent_id_tv.setText(mAgentId+",");
		todayfenrun = (TextView)view.findViewById(R.id.todayfenrun);
		areafenrun = (TextView)view.findViewById(R.id.areafenrun);
		
		if("1".equals(agenttypeid)){
			salepaycardnum45 = (TextView)view.findViewById(R.id.salepaycardnum45);
			salepaycardnum20 = (TextView)view.findViewById(R.id.salepaycardnum20);
		}else if("2".equals(agenttypeid)){
			agent_apply_btn_layout = (LinearLayout)view.findViewById(R.id.agent_apply_btn_layout);
			agent_apply_btn_layout.setOnClickListener(this);
		}
		
		areapaycardnum = (TextView)view.findViewById(R.id.areapaycardnum);
		areaauthornum = (TextView)view.findViewById(R.id.areaauthornum);
		
		
		agent_main_query = (LinearLayout)view.findViewById(R.id.agent_main_query);
		agent_main_query_img = (ImageView)view.findViewById(R.id.agent_main_query_img);
		agent_main_query_tv = (TextView)view.findViewById(R.id.agent_main_query_tv);
		
		agent_main_replenish = (LinearLayout)view.findViewById(R.id.agent_main_replenish);
		agent_main_replenish_img = (ImageView)view.findViewById(R.id.agent_main_replenish_img);
		agent_main_replenish_tv = (TextView)view.findViewById(R.id.agent_main_replenish_tv);
		
		agent_main_mytfb = (LinearLayout)view.findViewById(R.id.agent_main_mytfb);
		agent_main_mytfb_img = (ImageView)view.findViewById(R.id.agent_main_mytfb_img);
		agent_main_mytfb_tv = (TextView)view.findViewById(R.id.agent_main_mytfb_tv);
		
		agent_main_mytfb.setOnClickListener(this);
		agent_main_mytfb_img.setOnClickListener(this);
		agent_main_mytfb_tv.setOnClickListener(this);
		
		agent_main_query.setOnClickListener(this);
		agent_main_query_img.setOnClickListener(this);
		agent_main_query_tv.setOnClickListener(this);
		
		agent_main_replenish.setOnClickListener(this);
		agent_main_replenish_img.setOnClickListener(this);
		agent_main_replenish_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.agent_apply_btn_layout://虚拟代理商登录后的正式申请
			invokeAgentApply();
			break;
		case R.id.agent_main_query://代理商登录后的主页面之查询
		case R.id.agent_main_query_img:
		case R.id.agent_main_query_tv:
			invokeAgentQuery();
			break;
		case R.id.agent_main_replenish://代理商登录后的主页面之补货
		case R.id.agent_main_replenish_img:
		case R.id.agent_main_replenish_tv:
			invokeAgentReplenish();
			break;
		case R.id.agent_main_mytfb://代理商登录后的主页面之我的通付宝，点击回到APP主功能菜单
		case R.id.agent_main_mytfb_img:
		case R.id.agent_main_mytfb_tv:
			agentToCommonTFB();
			break;
		
		default:
			break;
		}
	}
	
	
	/**
	 * 代理商页面转到普通通付宝页面
	 */
	private void agentToCommonTFB()
	{
		/*
		 * 正式代理商
		 */
		if(getActivity() instanceof MainActivity) {
			((MainActivity)getActivity()).replaceCommonTab(2);
		}
		/*
		 * 虚拟代理商（体验）
		 */
		else if(getActivity() instanceof IndexActivity) {
			((IndexActivity)getActivity()).finish();
		}
	}
	
	/***
	 * 启动代理商申请页面 
	 */
	private void invokeAgentApply()
	{
		if(!mIsRealAgent){
			Intent intent = new Intent();
			intent.setClass(getActivity(), AgentApplyNewActivity.class);
			startActivityForResult(intent, 5);
		}
	}
	private void invokeAgentQuery()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), AgentQueryActivity.class);
		startActivityForResult(intent, 3);
	}
	private void invokeAgentReplenish()
	{
//		if(mIsRealAgent){
			Intent intent = new Intent();
			intent.setClass(getActivity(), AgentReplenishActivity.class);
			startActivityForResult(intent, 4);
//		}
//		else{
			/*
			 * 正式申请代理商
			 */
//			getFragmentManager().beginTransaction().replace(R.id.func_container, AgentApplyFragmentNew.create()).addToBackStack(null).commit();
//		}
	}
	private void AgentToMainTFB()
	{
		getActivity().setResult(Constants.ACTIVITY_FINISH);
		getActivity().finish();
	}
	
	
	private void showData(){
		todayfenrun.setText(agentData.todayfenrun);
		areafenrun.setText("本区历史总收益"+agentData.areafenrun+"元");
		
		if("1".equals(agenttypeid)){
			String []saleStr = agentData.salepaycardnum.split("\\|");
			if(saleStr.length>1){
				salepaycardnum45.setText(saleStr[0]);
				salepaycardnum20.setText("/"+saleStr[1]);
				salepaycardnum20.setVisibility(View.VISIBLE);
			}else{
				salepaycardnum45.setText(agentData.salepaycardnum);
	//			salepaycardnum20.setText("/20");
				salepaycardnum20.setVisibility(View.GONE);
			}
		}
//		else if("2".equals(agenttypeid)){
//			
//		}
		
		areapaycardnum.setText(agentData.areapaycardnum);
		areaauthornum.setText(agentData.areaauthornum);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) { 
		 case 3:
		 case 4:
			 if(resultCode == Constants.ACTIVITY_FINISH_TO_MENU )
			 {
				 AgentToMainTFB();
			 }
			 break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mBuyTask != null &&! mBuyTask.isCancelled()){
			mBuyTask.cancel(true);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		super.onResume();
		setActivityTitleVisible(View.GONE);
	}

	@Override
	public void onStop() {
		super.onStop();
		log("onStop endCallStateService");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		log("onDetach endCallStateService");
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		agentData=(AgentData) obj;
		if(agentData!=null){
			showData();
		}
	}

}
