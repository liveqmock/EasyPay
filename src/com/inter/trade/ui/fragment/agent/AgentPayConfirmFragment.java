package com.inter.trade.ui.fragment.agent;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.AgentPayActivity;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.util.AgentPayParser;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;
//import com.inter.trade.ui.fragment.qmoney.util.AgentPayData;
//import com.inter.trade.ui.fragment.qmoney.util.QMoneyNoParser;

/**
 * 代理商补货支付，确认Fragment
 * @author Lihaifeng
 *
 */

@SuppressLint("ValidFragment")
public class AgentPayConfirmFragment extends BaseFragment implements OnClickListener,SwipListener,AsyncLoadWorkListener{
	private Button toPay_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private TextView acount;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	private static double count =0;
	private static String mCouponId;
	private boolean isSelectedBank=false;
	private LinearLayout coupon_layout;
	
	private BuyTask mBuyTask;
	private DaikuanActivity mActivity;
	
	private Bundle bundle;
	
	public static AgentPayConfirmFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new AgentPayConfirmFragment();
	}
	
	public AgentPayConfirmFragment()
	{
	}
	
	public AgentPayConfirmFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		initReader();
		PayApp.mSwipListener = this;
		if(getActivity() instanceof DaikuanActivity){
			 mActivity = (DaikuanActivity)getActivity();
		}
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.agent_swip_layout, container,false);
		initView(view);
		
		setTitle("补货账单");
		setBackVisible();
		
		return view;
	}
	private void initSwipPic(boolean flag){
		if(flag){
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	private void initView(View view){
		toPay_btn = (Button)view.findViewById(R.id.toPay_btn);
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_prompt = (TextView)view.findViewById(R.id.swip_prompt);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
//		acount = (TextView)view.findViewById(R.id.acount);
		open_phone_edit = (EditText)view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText)view.findViewById(R.id.open_name_edit);
//		coupon_layout =  (LinearLayout)view.findViewById(R.id.coupon_layout);
		
		((TextView)view.findViewById(R.id.product_tv)).setText(bundle.getString("produrename"));
		((TextView)view.findViewById(R.id.number_tv)).setText(bundle.getString("ordernum"));
		((TextView)view.findViewById(R.id.price_tv)).setText(bundle.getString("orderprice"));
		((TextView)view.findViewById(R.id.total_money_tv)).setText("￥"+bundle.getString("ordermoney"));
		
//		coupon_layout.setVisibility(View.GONE);
		
		toPay_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		
	}
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		card_edit.setText(data.pan);
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			boolean s = PayApp.openReaderNow();
//			startTimer();
			log("status : "+s);
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	

	@Override
	public void progress(int status, String message) {
		// TODO Auto-generated method stub
		switch (status) {
		case SWIPING_START:
			PromptUtil.showToast(getActivity(), message);
			break;
		case SWIPING_FAIL:
			PromptUtil.showToast(getActivity(), message);
					break;
		case SWIPING_SUCCESS:
			PromptUtil.showToast(getActivity(), message);
			mKeyTask = new SwipKeyTask(getActivity(),
									   PayApp.mKeyCode, 
									   PayApp.mKeyData,
									   card_edit.getText().toString(),
									   "agentbuy",
									   this);
			mKeyTask.execute("");
			
			break;
		default:
			break;
		}
		
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
		case R.id.toPay_btn:
			if(checkInput()){
				mBuyTask = new BuyTask();
				mBuyTask.execute("");
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;

		default:
			break;
		}
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), BankListActivity.class);
		startActivityForResult(intent, 1);
	}
	
	private boolean checkInput(){
//		if(!isSelectedBank){
//			PromptUtil.showToast(getActivity(), "请选择银行");
//			return false;
//		}
		
		String cardNumber = card_edit.getText().toString();
		if(null == cardNumber || "".equals(cardNumber)){
			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		AgentPayActivity.moblieRechangeData.sunMap.put("orderfucardno", cardNumber);
		
		AgentPayActivity.moblieRechangeData.sunMap.put("orderprodureid", bundle.getString("orderprodureid"));
		AgentPayActivity.moblieRechangeData.sunMap.put("ordernum", bundle.getString("ordernum"));
		AgentPayActivity.moblieRechangeData.sunMap.put("orderprice", bundle.getString("orderprice"));
		AgentPayActivity.moblieRechangeData.sunMap.put("ordermoney", bundle.getString("ordermoney"));
		AgentPayActivity.moblieRechangeData.sunMap.put("produrename", bundle.getString("produrename"));
		
//		AgentPayActivity.moblieRechangeData.sunMap.put("orderfucardbank", "");
//		AgentPayActivity.moblieRechangeData.sunMap.put("ordermemo", "");
		
		//支付类型id ，默认为1
//		AgentPayActivity.moblieRechangeData.sunMap.put(AgentPayData.MRD_RECHAPAYTYPEID, 1+"");
		
		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}
//		
//		if(!PayApp.isSwipIn){
//			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
//				PromptUtil.showToast(getActivity(), "请刷卡");
//			}else{
//				PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			}
//			return false;
//		}
		
//		AgentPayActivity.moblieRechangeData.sunMap.put(AgentPayData.MRD_PAYCARDID, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
//		AgentPayActivity.moblieRechangeData.sunMap.put(AgentPayData.merReserved, 
//				Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		payResult(data);
		 if (data == null) {
	            return;
	     }
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardbank, bankname);
		 }
		 bank_name.setText(bankname);
		 
		 //银联支付结果
		 
	}
	
	private void payResult(Intent data){
		/*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
		 if (data == null) {
	            return;
	     }
		String msg ="";
        String str = data.getExtras().getString("pay_result");
        if(null == str){
        		return;
        }
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PayApp.mSwipListener= null;
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
		setTitle("补货账单");
		initSwipPic(PayApp.isSwipIn);
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

	private void showChuxuka(){
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, TransferConfirmFragment.create(mBkntno));
//		transaction.commit();
//		Intent intent = new Intent();
//		intent.putExtra("TN", mBkntno);
//		intent.setClass(getActivity(), UnionpayActivity.class);
//		getActivity().startActivity(intent);
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", 
						"payagentOrderRq", AgentPayActivity.moblieRechangeData);
				AgentPayParser authorRegParser = new AgentPayParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
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
				PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						AgentPayActivity.mCommonData.clear();
						AgentPayActivity.mBankNo = mBkntno;
						
							HashMap<String, String> item5 = new HashMap<String, String>();
							item5.put("产品名称:", AgentPayActivity.moblieRechangeData.getValue("produrename"));
							AgentPayActivity.mCommonData.add(item5);
							
							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("数量(台)", AgentPayActivity.moblieRechangeData.getValue("ordernum"));
							AgentPayActivity.mCommonData.add(item6);
							
							HashMap<String, String> item7 = new HashMap<String, String>();
							item7.put("进货价(元):", NumberFormatUtil.format2(AgentPayActivity.moblieRechangeData.getValue("orderprice")));
							AgentPayActivity.mCommonData.add(item7);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("支付总额(元):", NumberFormatUtil.format2(AgentPayActivity.moblieRechangeData.getValue("ordermoney")));
							AgentPayActivity.mCommonData.add(item3);
							
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("交易请求号:", mBkntno);
							AgentPayActivity.mCommonData.add(item);
						showChuxuka();
					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
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
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno != null){
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		
	}

	@Override
	public void onFailure(String error) {
		
	}
	
}
