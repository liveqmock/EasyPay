package com.inter.trade.ui.fragment.agent;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.AgentApplyActivity;
import com.inter.trade.ui.AgentMainContentActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.util.AgentCodeTempParser;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;

/**
 * 代理商主界面--未登录
 * @author Lihaifeng
 *
 */
public class AgentMainFragment2 extends BaseFragment implements OnClickListener, ResponseStateListener{
	private String mAgentId;
	private String mAgentCode;
	private String mResult;
	private String mMessage;
	
	private RelativeLayout activity_title_layout;
	
	private Button agent_apply_btn;
	private Button agent_login_btn;
	private EditText agent_id_edit;
	private EditText agent_key_edit;
	
	private BuyTask mBuyTask;
	
	private CommonData mData = new CommonData();
	
	public CommonData mfeeData=new CommonData();
	
	public AgentMainFragment2()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//LoginUtil.detection(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		
		View view = inflater.inflate(R.layout.agent_appling_or_login_layout, container,false);
		initView(view);
		
		setTitle("申请代理商");
		setBackVisible();
		hideTitleLine();
		
		/*need
		// 获取话费充值面额选择,改为获取代理商协议及代理商申请填写项
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
		*/
		
		return view;
	}
	
	private void initView(View view){
		agent_apply_btn = (Button)view.findViewById(R.id.agent_apply_btn);
		agent_login_btn = (Button)view.findViewById(R.id.agent_login_btn);
		agent_apply_btn.setOnClickListener(this);
		agent_login_btn.setOnClickListener(this);
		setActivityTitleVisible(View.VISIBLE);
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

	long time=0L;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//1秒内，禁止双击两次提交
		long currentTime=System.currentTimeMillis();
		if(currentTime-time<1000){
			return;
		}
		time=currentTime;
		
		switch (arg0.getId()) {
		case R.id.agent_apply_btn://代理商，一键体验
			yijiantiyan();
//			invokeAgentApply();
			break;
		case R.id.agent_login_btn://代理商，正式申请
			getFragmentManager().beginTransaction().replace(R.id.func_container, AgentApplyFragmentNew.create()).addToBackStack(null).commit();
//			invokeAgentLogin();
			break;
		default:
			break;
		}
	}
	
	/*
	 * 代理商申请 一键申请
	 */
	private void yijiantiyan () {
		String agentid=LoginUtil.mLoginStatus.agentid;
		Logger.d("yijiantiyan", "agentid=="+agentid);
		
		if(agentid == null){
			agentid=PreferenceConfig.instance(getActivity()).getString(Constants.AGENT_ID, "0");
			LoginUtil.mLoginStatus.agentid = agentid;
			Logger.d("PreferenceConfig", "agentid=="+agentid);
		}
		
		if(agentid==null || "".equals(agentid) || Integer.parseInt(agentid) <=0){
			if(mBuyTask != null){
				mBuyTask.cancel(true);
			}
			mBuyTask = new BuyTask(this);
			mBuyTask.execute("");
		}else{
			getFragmentManager().beginTransaction().replace(R.id.func_container, AgentMainContentFragment.create(mAgentCode+"")).addToBackStack(null).commit();
		}
	}
	
	/*
	 * 代理商申请 一键申请,成功回调
	 */
	@Override
	public void onSuccess(Object obj, Class cla) {
		String agentid=LoginUtil.mLoginStatus.agentid;
		String agenttypeid=LoginUtil.mLoginStatus.agenttypeid;
		if(mMessage==null || agentid==null || "".equals(agentid) || Integer.parseInt(agentid) <=0
				|| agenttypeid==null || "".equals(agenttypeid) || Integer.parseInt(agenttypeid) <=0)
			return;
		
		//保存数据  "代理商id" 到PreferenceConfig
		PreferenceConfig.instance(getActivity()).putString(Constants.AGENT_ID, LoginUtil.mLoginStatus.agentid+"");
		
		//保存数据  "代理商类型id" 到PreferenceConfig
		PreferenceConfig.instance(getActivity()).putString(Constants.AGENT_TYPE_ID, LoginUtil.mLoginStatus.agenttypeid+"");
		
		LayoutInflater factory = LayoutInflater.from(getActivity());
//		final View DialogView = factory.inflate(R.layout.agent_login_layout, null);
		AlertDialog dlg = new AlertDialog.Builder(
		getActivity())
		.setTitle("申请成功")
		.setMessage(mMessage+"")
		.setPositiveButton("体验",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					// TODO Auto-generated method
					getFragmentManager().beginTransaction().replace(R.id.func_container, AgentMainContentFragment.create(mAgentCode+"")).addToBackStack(null).commit();
					dialog.dismiss();
				}
			})
			.setCancelable(false).show();
	}
	
	/*
	 * 代理商申请页面
	 */
	private void invokeAgentApply () {
		Intent intent = new Intent();
		intent.setClass(getActivity(), AgentApplyActivity.class);
		startActivityForResult(intent, 3);
	}
	
	/*
	 * 代理商登录页面
	 */
	private void invokeAgentLogin () {
//		ProgressDialog p_dialog;
		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View DialogView = factory.inflate(R.layout.agent_login_layout, null);
		AlertDialog dlg = new AlertDialog.Builder(
		getActivity())
		.setTitle("代理商登录")
		.setView(DialogView)
		.setPositiveButton("确定",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					// TODO Auto-generated method
					doAgentLogin();
					Intent intent = new Intent();
					intent.setClass(getActivity(), AgentMainContentActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(QMoneyData.MRD_RECHAMOBILE,agent_id_edit.getText().toString());
					bundle.putString(QMoneyData.MRD_RECHAMONEY, agent_key_edit.getText().toString());
					intent.putExtra("AgentMain", bundle);
					startActivityForResult(intent, 4);
				}
			})
		.setNegativeButton("取消",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					// TODO Auto-generated method
					//p_dialog.dismiss();
					//getActivity().finish();
					dialog.dismiss();
				}
			}).create();
//		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		
		agent_id_edit = (EditText)DialogView.findViewById(R.id.agent_id_edit);
		agent_key_edit = (EditText)DialogView.findViewById(R.id.agent_key_edit);
	}
	
	private void doAgentLogin()
	{
		//此处要写代码：检查代理商账号，密码，提交网络请求及解析响应体，登录状态修改等处理
		
		//最后登录状态成功
		LoginUtil.isAgentLogin = true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) { 
		 //case 3:
		 case 4:
			 if(resultCode == Constants.ACTIVITY_FINISH )
			 {
				 AgentToMainTFB();
			 }
			 break;
		}
	}
	
	private void AgentToMainTFB()
	{
		getActivity().finish();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("申请代理商");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		log("onStop endCallStateService");
	}

	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		log("onDetach endCallStateService");
	}

	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		private ResponseStateListener listener;
		ProtocolRsp mRsp;
		
		public BuyTask(ResponseStateListener listener) {
			super();
			this.listener = listener;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentApply", 
						"insertParttimeagent", mData);
				AgentCodeTempParser rechangeDenominationParser = new AgentCodeTempParser();
				mRsp = HttpUtil.doRequest(rechangeDenominationParser, mDatas);
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
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						mResult=LoginUtil.mLoginStatus.result;
						mMessage=LoginUtil.mLoginStatus.message;
						if(listener!=null){
							listener.onSuccess(null, null);
						}
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
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				
				List<ProtocolData> agentcode = data.find("/agentcode");
				if (agentcode != null) {
					mAgentCode = agentcode.get(0).getmValue();
				}
				
				List<ProtocolData> agentid = data.find("/agentid");
				if (agentid != null) {
					mAgentId = agentid.get(0).getmValue();
					LoginUtil.mLoginStatus.agentid = agentid.get(0).getmValue();
				}
				
				List<ProtocolData> agenttypeid = data.find("/agenttypeid");
				if (agenttypeid != null) {
					LoginUtil.mLoginStatus.agenttypeid = agenttypeid.get(0).getmValue();
				}
				
//				List<ProtocolData> aupic = data.find("/msgchild");
//				if(aupic!=null)
//				for(ProtocolData child:aupic){
//					QMoneyDenominationData picData = new QMoneyDenominationData();
//					if (child.mChildren != null && child.mChildren.size() > 0) {
//						Set<String> keys = child.mChildren.keySet();
//						for(String key:keys){
//							List<ProtocolData> rs = child.mChildren.get(key);
//							for(ProtocolData item: rs){
//								if(item.mKey.equals("rechaMoneyid")){
//									picData.rechaMoneyid  = item.mValue;
//									
//								}else if(item.mKey.equals("rechamoney")){
//									picData.rechamoney  = item.mValue;
//									
//								}else if(item.mKey.equals("rechapaymoney")){
//									
//									picData.rechapaymoney  = item.mValue;
//									
//								}else if(item.mKey.equals("rechamemo")){
//									
//									picData.rechamemo  = item.mValue;
//									
//								}else if(item.mKey.equals("rechaisdefault")){
//									
//									picData.rechaisdefault  = item.mValue;
//									
//								}
//							}
//						}
//					}
//					
//					mList.add(picData);
//				}
			}
		}
	}
	
}
