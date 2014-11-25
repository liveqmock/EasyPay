package com.inter.trade.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.AuthorPwdModifyParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 修改密码
 * @author apple
 *
 */
public class SetNewPwdFragment extends BaseFragment implements OnClickListener{
	private Button modify_btn;
	private static final String MODIFY_INDEX="MODIFY_INDEX";
	private static String mType = "1";
	
	private TextView current_pwd_tv;
	private EditText current_pwd_edit;
	private TextView new_pwd_tv;
	private EditText new_pwd_edit;
	private String mCurrentPwd;
	private String mNewPwd;
	private String mConfirmPwd;
	
	private static String mPhone;
	private ModifyTask modifyTask;
	private LinearLayout current_layout;
	public static SetNewPwdFragment createFragment(String index,String type){
		SetNewPwdFragment f = new SetNewPwdFragment();
		mPhone = index;
		mType =type;
		
		return f;
	}
	
	public SetNewPwdFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				setTitle("设置新密码");
		
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.set_new_pwd_layout, container, false);
		initView(view);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("设置新密码");
	}

	private void initView(View v){
		current_pwd_tv = (TextView)v.findViewById(R.id.current_pwd_tv);
		current_pwd_edit = (EditText)v.findViewById(R.id.current_pwd_edit);
		new_pwd_tv = (TextView)v.findViewById(R.id.new_pwd_tv);
		new_pwd_edit = (EditText)v.findViewById(R.id.new_pwd_edit);
		modify_btn = (Button)v.findViewById(R.id.modify_btn);
		
		current_layout = (LinearLayout)v.findViewById(R.id.current_layout);
		
		modify_btn.setOnClickListener(this);
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

		default:
			break;
		}
		
	}
	
	private boolean checkInput(){
		boolean flag = true;
		//修改支付密码
			flag = checkLoginPwd();
		return flag;
	}
	
	
	/**
	 * 
	 * 检测登录密码输入
	 * @return
	 */
	private boolean checkLoginPwd(){
		String temp = current_pwd_edit.getText().toString();
		if(null == temp || "".equals(temp)){
			PromptUtil.showToast(getActivity(), "请输入密码");
			return false;
		}
		
		if(!UserInfoCheck.checkPassword(temp)){
			PromptUtil.showToast(getActivity(), "密码格式不正确");
			return false;
		}
		mConfirmPwd = temp;
		String new_pwd = new_pwd_edit.getText().toString();
		if(null == new_pwd || "".equals(new_pwd)){
			PromptUtil.showToast(getActivity(), "请再次输入新密码");
			return false;
		}
		if(!UserInfoCheck.checkPassword(new_pwd)){
			PromptUtil.showToast(getActivity(), "新密码格式不正确");
			return false;
		}
		mNewPwd = new_pwd;
		if(!temp.equals(new_pwd)){
			PromptUtil.showToast(getActivity(), "两次密码输入不一致，请重新输入");
			return false;
		}
		return true;
	}
	private void modify(){
		modifyTask = new ModifyTask();
		modifyTask.execute("");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(modifyTask != null){
			modifyTask.cancel(true);
		}
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
					 
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
						getActivity().finish();
					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					}
				}else {
					PromptUtil.showToast(getActivity(), getString(R.string.net_error));
				}
			} catch (Exception e) {
				// TODO: handle exception
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
		if(mCurrentPwd==null){
			mCurrentPwd = mNewPwd;
		}
		data.putValue("auoldpwd", "");
		data.putValue("aunewpwd", mNewPwd);
		data.putValue("aurenewpwd", mConfirmPwd);
		data.putValue("aumobile", mPhone);
		data.putValue("aumoditype", mType);
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfo", "forgetPwdModify", data);
		
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
