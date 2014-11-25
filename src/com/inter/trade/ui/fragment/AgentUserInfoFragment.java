package com.inter.trade.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.inter.trade.R;
import com.inter.trade.data.PicData;
import com.inter.trade.data.RigesterData;
import com.inter.trade.imageframe.ImageFetcher;
import com.inter.trade.ui.UserInfoActivity;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 代理商用户信息
 * @author zhichao.huang
 *
 */
public class AgentUserInfoFragment extends BaseFragment implements OnClickListener{
	public ImageView identity_1;
	public ImageView identity_2;
	
	private EditText name_edit;
	private EditText identity_edit;
	private EditText email_edit;
	
	public RigesterData mRigesterData = new RigesterData();
	private static final String TAG = "Fragment";
	public static int IDENTITY_FIRST=1;
	public static int IDENTITY_SECOND=2;
	public boolean isClickUpload=false;
	
	public AgentUserInfoFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setTitle(getTitle(R.string.agent_userinfo_title));
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateView");
		LoginUtil.detection(getActivity());
		
		View view = inflater.inflate(R.layout.agent_userinfo_set_layout, container, false);
		identity_1 = (ImageView)view.findViewById(R.id.identity_1);
		identity_2 =  (ImageView)view.findViewById(R.id.identity_2);
		identity_1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_pic));
		identity_2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_pic));
		initIdentity();
		name_edit =  (EditText)view.findViewById(R.id.name_edit);
		identity_edit =  (EditText)view.findViewById(R.id.identity_edit);
		email_edit =  (EditText)view.findViewById(R.id.email_edit);
		
		name_edit.setText(LoginUtil.mUserInfo.autruename);
		identity_edit.setText(LoginUtil.mUserInfo.autrueidcard);
		email_edit.setText(LoginUtil.mUserInfo.auemail);
		
		identity_1.setOnClickListener(this);
		identity_2.setOnClickListener(this);
		setRightVisible(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isClickUpload){
					return;
				}
				if(getActivity() instanceof UserInfoActivity){
					if(!((UserInfoActivity)getActivity()).isPicInput()){
						PromptUtil.showToast(getActivity(), "请添加身份证照片");
					}else{
						if(checkInput()){
							if(getActivity() instanceof UserInfoActivity){
								((UserInfoActivity)getActivity()).upload();
								isClickUpload=true;
							} 
						}
					}
				}
				
			}
		}, "提交审核");
		return view;
	}
	private void initIdentity(){
		if(getActivity() instanceof UserInfoActivity){
			ImageFetcher mFetcher = ((UserInfoActivity)getActivity()).getFetcher();
			ArrayList<PicData> datas = LoginUtil.mUserInfo.mPicData;
			if(datas != null && datas.size()>0){
				if(datas.get(0)!= null){
					mFetcher.loadImage(datas.get(0).picpath, identity_1);
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(datas.get(1)!= null){
					mFetcher.loadImage(datas.get(1).picpath, identity_2);
				}	
			}
			
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.identity_1:
			showPicture(IDENTITY_FIRST);
			break;
		case R.id.identity_2:
			showPicture(IDENTITY_SECOND);
			break;

		default:
			break;
		}
		
	}
	private boolean checkInput()
	{
		boolean flag = true;
		
		String true_name = name_edit.getText().toString();
		
		if(null == true_name){
			PromptUtil.showToast(getActivity(), "请输入姓名");
			return false;
		}
		if(!UserInfoCheck.checkName(true_name)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		mRigesterData.setName(true_name);
		
		String identity_card = identity_edit.getText().toString();
		if(null == identity_card || identity_card.length()  != 18){
			PromptUtil.showToast(getActivity(), "身份证输入有误");
			return false;
		}
		if(!UserInfoCheck.checkIdentity(identity_card)){
			PromptUtil.showToast(getActivity(), "身份证格式不正确");
			return false;
		}
		mRigesterData.setIdentity(identity_card);
		
		String email = email_edit.getText().toString();
		if(null == email){
			PromptUtil.showToast(getActivity(), "请输入邮箱");
			return false;
		}
		if(!UserInfoCheck.checkEmail(email)){
			PromptUtil.showToast(getActivity(), "邮箱格式不正确");
			return false;
		}
		mRigesterData.setEmail(email);
		
		return flag;
	}
	
	private void showPicture(int index){
		if(getActivity() instanceof UserInfoActivity){
			((UserInfoActivity)getActivity()).showPicture(index);
		} 
	}
	
	public void setBGFirst(Bitmap bitmap){
		identity_1.setBackgroundDrawable(null);
		identity_1.setImageBitmap(bitmap)
		;
	}
	public void setBGSec(Bitmap bitmap){
		identity_2.setBackgroundDrawable(null);
		identity_2.setImageBitmap(bitmap)
		;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d(TAG, "onAttach");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d(TAG, "onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
		setTitle(getTitle(R.string.agent_userinfo_title));
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");
	}
	
	
}
