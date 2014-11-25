package com.inter.trade.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.AuthorRegParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.RigesterData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.mpay.utils.e;

public class RigesterInfoFragment extends BaseFragment implements OnClickListener{
	private Button rigster_btn;
	private Button submit;
	private View line;
	private LinearLayout codeLayout;
	private RigesterData mData;
	
	private EditText login_pwd_edit;
	private EditText true_name_edit;
	private EditText identity_edit;
	private EditText email_edit;
	private EditText login_again_pwd_edit;
	private ProtocolRsp mRsp;
	
	private CheckBox pwdBox;
	private CheckBox protocol_ck;
	
	private TextView register_protocol;
	
	private static String REGISTER_PHONE = "REGISTER_PHONE";
	
	public static RigesterInfoFragment createFragment(String phone){
		RigesterInfoFragment  fragment = new RigesterInfoFragment();
		final Bundle args = new Bundle();
        args.putString(REGISTER_PHONE, phone);
		fragment.setArguments(args);
		return fragment;
	}
	
	public RigesterInfoFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getActivity().getResources().getString(R.string.rigester_userinfo_title));
		setBackVisible();
		String phone = getArguments()==null?"":getArguments().get(REGISTER_PHONE).toString();
		mData = new RigesterData();
		mData.setPhone(phone);
//		mData.setPhone("18664676691");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.rigester_userinfo, container, false);
		rigster_btn= (Button)view.findViewById(R.id.rigster_btn);
		
		login_pwd_edit = (EditText)view.findViewById(R.id.login_pwd_edit);
		true_name_edit = (EditText)view.findViewById(R.id.true_name_edit);
		identity_edit = (EditText)view.findViewById(R.id.identity_edit);
		email_edit = (EditText)view.findViewById(R.id.email_edit);
		login_again_pwd_edit =  (EditText)view.findViewById(R.id.login_again_pwd_edit);
		
		pwdBox = (CheckBox)view.findViewById(R.id.pwd_checkbox);
		pwdBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					login_pwd_edit.setInputType(InputType.TYPE_CLASS_TEXT);
					login_again_pwd_edit.setInputType(InputType.TYPE_CLASS_TEXT);
//					login_pwd_edit.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					login_pwd_edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
					login_again_pwd_edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
				}
			}
		});
		
		protocol_ck = (CheckBox)view.findViewById(R.id.protocol_ck);
		register_protocol = (TextView)view.findViewById(R.id.register_protocol);
		
		rigster_btn.setOnClickListener(this);
		register_protocol.setOnClickListener(this);
		return view;
	}
	
	private boolean checkInput()
	{
		boolean flag = true;
		String login_pwd = login_pwd_edit.getText().toString();
		if(null == login_pwd||"".equals(login_pwd)){
			PromptUtil.showToast(getActivity(), "请输入密码");
			return false;
		}
		if(!UserInfoCheck.checkPassword(login_pwd)){
			PromptUtil.showToast(getActivity(), "密码格式不正确");
			return false;
		}
		
		String login_pwd_again = login_again_pwd_edit.getText().toString();
		if(null == login_pwd_again || login_pwd_again.length()<6){
			PromptUtil.showToast(getActivity(), "确认密码输入有误");
			return false;
		}
		
		if(!UserInfoCheck.checkPassword(login_pwd_again)){
			PromptUtil.showToast(getActivity(), "确认密码格式不正确");
			return false;
		}
		mData.setLogin_pwd(login_pwd_again);
		String true_name = true_name_edit.getText().toString();
		
		if(null == true_name||"".equals(true_name)){
			PromptUtil.showToast(getActivity(), "请输入姓名");
			return false;
		}
		
		if(!UserInfoCheck.checkName(true_name)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		mData.setName(true_name);
		
		String identity_card = identity_edit.getText().toString();
		if(null == identity_card||"".equals(identity_card)){
			
			PromptUtil.showToast(getActivity(), "请输入身份证");
			return false;
		}
		if(!UserInfoCheck.checkIdentity(identity_card)){
			PromptUtil.showToast(getActivity(), "身份证格式不正确");
			return false;
		}
		mData.setIdentity(identity_card);
		
		String email = email_edit.getText().toString();
		if(null == email||"".equals(email)){
			PromptUtil.showToast(getActivity(), "请输入邮箱");
			return false;
		}
		if(!UserInfoCheck.checkEmail(email)){
			PromptUtil.showToast(getActivity(), "邮箱格式不正确");
			return false;
		}
		mData.setEmail(email);
		
		if(!login_pwd.equals(login_pwd_again)){
			PromptUtil.showToast(getActivity(), "两次密码输入不一致");
			return false;
		}
		mData.setLogin_pwd(login_pwd_again);
		
		if(!protocol_ck.isChecked())
		{
			PromptUtil.showToast(getActivity(), "请确认已经阅读通付宝协议");
			return false;
		}
		return flag;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rigster_btn:
//			HttpUtil.getRequest(ProtocolUtil.getUserRigester(mData));
			if(checkInput()){
				new InfoTask().execute("");
			}
			break;
		case R.id.get_sms_code:
			break;
		case R.id.register_protocol:
			AboutFragment.mProtocolType="3";
			showProtocol();
			break;
		default:
			break;
		}
		
	}
	private void showProtocol(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.PROTOCOL_LIST_INDEX);
		getActivity().startActivity(intent);
	}
	
	class InfoTask extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ProtocolData> mDatas = getRequestDatas();
			AuthorRegParser authorRegParser = new AuthorRegParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
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
					parserResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						LoginUtil.showLogin(getActivity().getSupportFragmentManager());
						
					}
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}
			
			}
				
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), "正在注册...");
		}
		
	}
	
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		data.putValue("aumobile", mData.getPhone());
		data.putValue("aupassword", mData.getLogin_pwd());
		data.putValue("autruename", mData.getName());
		data.putValue("auidcard", mData.getIdentity());
		data.putValue("auemail", mData.getEmail());
		
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorReg", "authorReg", data);
		
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
//				List<ProtocolData> req_seq = data.find("/req_seq");
//				if(req_seq!=null){
//					response.setReq_seq(req_seq.get(0).mValue);
//				}
//				
//				
//				List<ProtocolData> ope_seq = data.find("/ope_seq");
//				if(ope_seq!=null){
//					response.setOpe_seq(ope_seq.get(0).mValue);
//				}
//			
//				
//				List<ProtocolData> rettype = data.find("/retinfo/rettype");
//				if(rettype!=null){
//					response.setRettype(rettype.get(0).mValue);
//				}
//				
//				List<ProtocolData> retcode = data.find("/retinfo/retcode");
//				if(retcode!=null){
//					response.setRetcode(retcode.get(0).mValue);
//				}
//			
//				
//				List<ProtocolData> retmsg = data.find("/retinfo/retmsg");
//				if(retmsg!=null){
//					response.setRetmsg(retmsg.get(0).mValue);
//				}
//				
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
