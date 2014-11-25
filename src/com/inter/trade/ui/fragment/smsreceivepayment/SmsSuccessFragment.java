package com.inter.trade.ui.fragment.smsreceivepayment;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.BuySwipCardRecordActivity;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 短信收款发起成功 结果页面 Fragment
 * @author haifengli
 *
 */
public class SmsSuccessFragment extends BaseFragment {
	
	private static final String TAG = SmsSuccessFragment.class.getName();
	
	private Button btn_ok;
	private TextView tv_key;
	private String licenseKey;
	
	/**
	 * 短信收款 发起请求总数据
	 */
	private CommonData requsetData;
	private Bundle data = null;
	
	public SmsSuccessFragment()
	{
	}
	
	public static SmsSuccessFragment newInstance (Bundle data) {
		SmsSuccessFragment fragment = new SmsSuccessFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
			requsetData=(CommonData) data.getSerializable("requsetData");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.sms_success_layout, container,false);
		initView(view);
		
		setTitle("交易通知");
		setBackVisibleForFragment();
		
		return view;
	}
	
	private void initView (View view) {
		if(requsetData != null){
			TextView tv_content = (TextView)view.findViewById(R.id.tv_content);
			String s1 = "　　您已成功向手机号码";
			String s2 = requsetData.getValue("fumobile");
			String s3 = "发起金额为";
			String s4 = "￥"+requsetData.getValue("money");
			String s5 = "的短信收款,对方成功付款后1个工作日内，该笔款项将转入您以下指定的银行账户：";
			String content = s1+s2+s3+s4+s5;
			int len=0;
			/**
			 * 同一个TEXTVIEW设置不同颜色
			 */
			SpannableString ss = new SpannableString(content);
	        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.agent_apply_yellow)), len=s1.length()+s2.length()+s3.length(), len+s4.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	        tv_content.setText(ss);
	        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
	        
//			String content = "　　您已成功向手机号码"
//					+requsetData.getValue("fumobile")
//					+"发起金额为￥"
//					+requsetData.getValue("money")
//					+"的短信收款,对方成功付款后1个工作日内，该笔款项将转入您以下指定的银行账户：";
//			tv_content.setText(content);
			
			TextView tv_shou_bank = (TextView)view.findViewById(R.id.tv_shou_bank);
			String shou_bank = "开户行："+requsetData.getValue("shoucardbank");
			tv_shou_bank.setText(shou_bank);
			
			TextView tv_shou_card = (TextView)view.findViewById(R.id.tv_shou_card);
			String shou_card = "账号："+requsetData.getValue("shoucardno");
			tv_shou_card.setText(shou_card);
		}
		
		Button see_history = (Button)view.findViewById(R.id.see_history);
		see_history.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().setResult(Constants.ACTIVITY_FINISH);
				getActivity().finish();
			}
		});
		
		Button btnAgain=(Button) view.findViewById(R.id.btn_again);
		btnAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * 再来一次，返回短信收款首页
				 */
				SmsReceivePaymentMainFragment.isComeBackFromPaySuccess=true;
				Intent intent=new Intent(getActivity(),IndexActivity.class);
				intent.putExtra(FragmentFactory.INDEX_KEY, FuncMap.SMSRECEIPT_INDEX_FUNC);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	
}
