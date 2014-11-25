package com.inter.trade.ui.func;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.TaskData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.net.SunHttpApi;
import com.inter.trade.ui.ArriveView;
import com.inter.trade.ui.CommonActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.AgentApplyActivity;
import com.inter.trade.ui.AgentMainContentActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyDenominationData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyDenominationParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 新版主界面
 * @author Lihaifeng
 *
 */
public class MainFragment extends BaseFragment implements OnClickListener{
	
	private Button agent_apply_btn;
	private Button agent_login_btn;
	private EditText agent_id_edit;
	private EditText agent_key_edit;
	
	
	private BuyTask mBuyTask;
	
	private CommonData mData = new CommonData();
	
	public CommonData mfeeData=new CommonData();
	
	private ArrayList<QMoneyDenominationData> mList = new ArrayList<QMoneyDenominationData>();
	
	public MainFragment()
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
		
		View view = inflater.inflate(R.layout.default_new_home_pay, container,false);
		initView(view);
		
//		setTitle("通付宝");
//		setBackVisible();
		
		return view;
	}
	
	private void initView(View view){
	}
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.agent_apply_btn://代理商申请
			invokeAgentApply();
			break;
		case R.id.agent_login_btn://代理商登录
			invokeAgentLogin();
			break;
		default:
			break;
		}
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
		setTitle("通付宝");
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
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiQQRechangeInfo", 
						"readRechaMoneyinfo", mData);
				QMoneyDenominationParser rechangeDenominationParser = new QMoneyDenominationParser();
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
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					QMoneyDenominationData picData = new QMoneyDenominationData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("rechaMoneyid")){
									picData.rechaMoneyid  = item.mValue;
									
								}else if(item.mKey.equals("rechamoney")){
									picData.rechamoney  = item.mValue;
									
								}else if(item.mKey.equals("rechapaymoney")){
									
									picData.rechapaymoney  = item.mValue;
									
								}else if(item.mKey.equals("rechamemo")){
									
									picData.rechamemo  = item.mValue;
									
								}else if(item.mKey.equals("rechaisdefault")){
									
									picData.rechaisdefault  = item.mValue;
									
								}
							}
						}
					}
					
					mList.add(picData);
				}
			}
		}
	}
	
}
