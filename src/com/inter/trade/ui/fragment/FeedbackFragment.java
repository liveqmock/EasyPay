package com.inter.trade.ui.fragment;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.AuthorfeedbckParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 信息反馈
 * @author apple
 *
 */
public class FeedbackFragment extends BaseFragment implements OnClickListener{
	private EditText content;
	private EditText contact;
	
	private String content_string;
	private String contact_string;
	
	private Button feedback;
	public FeedbackFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("意见反馈");
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.feedback_layout, container, false);
		content = (EditText)view.findViewById(R.id.content);
		contact = (EditText)view.findViewById(R.id.contact);
		feedback = (Button)view.findViewById(R.id.feedback);
		feedback.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("意见反馈");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.feedback:
			if(checkInput()){
				new FeedbackTask().execute("");
			}
			break;
		default:
			break;
		}
		
	}
	
	
	private boolean checkInput()
	{
		String tempString = content.getText().toString();
		
		if(null == tempString|| "".equals(tempString)){
			PromptUtil.showToast(getActivity(), "请输入反馈内容");
			return false;
		}else if(tempString.length()<10){
			PromptUtil.showToast(getActivity(), "请输入10个字符以上的反馈意见");
			return false;
		}else if(tempString.length()>200){
			PromptUtil.showToast(getActivity(), "反馈内容超过200字，请确定在200字以内");
			return false;
		}
		content_string = tempString;
		
		boolean flag = true;
		String conString = contact.getText().toString();
		
		if(conString==null || conString.length()==0){
			conString ="";
		}
//		else if(!UserInfoCheck.checkMobilePhone(conString)){
//			PromptUtil.showToast(getActivity(), "手机号格式不正确");
//			return false;
//		}
		contact_string=conString;
		
		return flag;
	}
	private ProtocolRsp mRsp;
	class FeedbackTask extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ProtocolData> mDatas = getRequestDatas();
			AuthorfeedbckParser authorRegParser = new AuthorfeedbckParser();
			ProtocolParser.instance().setParser(authorRegParser);
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
				if(mRsp==null){
					PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
				}else{
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
						getActivity().finish();
						
					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				PromptUtil.showToast(getActivity(),getString(R.string.req_error));
			}
			
				
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), "正在反馈...");
		}
		
	}
	
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		data.putValue("fdcontent", content_string);
		data.putValue("fdlinkmethod", contact_string);
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorfeedbck"
				, "authorFeedbck", data);
		
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
