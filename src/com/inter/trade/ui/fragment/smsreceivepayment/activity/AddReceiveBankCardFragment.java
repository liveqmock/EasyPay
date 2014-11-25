package com.inter.trade.ui.fragment.smsreceivepayment.activity;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
 * 默认收款账户  添加fragment
 * @author Lihaifeng
 *
 */
public class AddReceiveBankCardFragment extends BaseFragment implements OnClickListener{
	
	private AddCreditCardFragment mAddCreditCardFragment;
	private AddBankCardFragment mAddBankCardFragment;
	
	private int mCurrent=0;
	private LinearLayout credit_layout;
	private LinearLayout card_layout;
	private ImageView credit_select;
	private ImageView card_select;
	
	private Button agent_apply_btn;
	private Button agent_login_btn;
	private EditText agent_id_edit;
	private EditText agent_key_edit;
	
	private BuyTask mBuyTask;
	
	private CommonData mData = new CommonData();
	
	public CommonData mfeeData=new CommonData();
	
	private ArrayList<QMoneyDenominationData> mList = new ArrayList<QMoneyDenominationData>();
	
	public static AddReceiveBankCardFragment create(){
		return new AddReceiveBankCardFragment();
	}
	
	public AddReceiveBankCardFragment()
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
		
		View view = inflater.inflate(R.layout.mybank_add_layout, container,false);
		initView(view);
		
		setTitle("添加默认收款账户");
		setBackVisible();
		
		/*need
		// 获取话费充值面额选择,改为获取代理商协议及代理商申请填写项
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
		*/
		
		return view;
	}
	
	private void initView(View view){
		credit_layout = (LinearLayout)view.findViewById(R.id.credit_layout);
		card_layout = (LinearLayout)view.findViewById(R.id.card_layout);
		credit_select = (ImageView)view.findViewById(R.id.credit_select);
		card_select = (ImageView)view.findViewById(R.id.card_select);
		
		credit_layout.setOnClickListener(this);
		card_layout.setOnClickListener(this);
		
		credit_layout.setSelected(true);
		card_layout.setSelected(false);
		credit_select.setVisibility(View.VISIBLE);
		card_select.setVisibility(View.INVISIBLE);
		switchFragment(0);
//		switchFragment(mAddBankCardFragment, mAddCreditCardFragment);
	}
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.credit_layout://信用卡
			credit_layout.setSelected(true);
			card_layout.setSelected(false);
			credit_select.setVisibility(View.VISIBLE);
			card_select.setVisibility(View.INVISIBLE);
			switchFragment(0);
//			switchFragment(mAddBankCardFragment, mAddCreditCardFragment);
			break;
		case R.id.card_layout://储蓄卡
			credit_layout.setSelected(false);
			card_layout.setSelected(true);
			credit_select.setVisibility(View.INVISIBLE);
			card_select.setVisibility(View.VISIBLE);
			switchFragment(1);
//			switchFragment(mAddCreditCardFragment, mAddBankCardFragment);
			break;
		default:
			break;
		}
	}
	
	
	public void switchFragment(int index){  
	    
	    FragmentTransaction ft = getChildFragmentManager().beginTransaction(); 
	    if(mAddCreditCardFragment == null){
	    	mAddCreditCardFragment = new AddCreditCardFragment();
	    	ft=ft.add(R.id.mybank_container, mAddCreditCardFragment);
	    }
	    if(mAddBankCardFragment == null){
	    	mAddBankCardFragment = new AddBankCardFragment();
	    	ft=ft.add(R.id.mybank_container, mAddBankCardFragment);
	    }
	    if(index==0){
	    	ft.hide(mAddBankCardFragment).show(mAddCreditCardFragment).commit();
	    }else{
	    	ft.hide(mAddCreditCardFragment).show(mAddBankCardFragment).commit();
	    }
	    mCurrent=index;
	    //任何fragment transaction处理后，都通过commit()进行确认。 
	} 
	
	public void switchFragment(Fragment from, Fragment to) {
		if (from != to) {
//			mContent = to;
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			
			//to.getLoaderManager().hasRunningLoaders();
			// 先判断是否被add过
			if (!to.isAdded()) {
				
				// 隐藏当前的fragment，add下一个到Activity中
				transaction.hide(from).add(R.id.mybank_container, to).commit();
			} else {
				// 隐藏当前的fragment，显示下一个
				transaction.hide(from).show(to).commit();
			}
		}
		
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
//		setTitle("添加银行卡");
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
				if (message != null) {
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
