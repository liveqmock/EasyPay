package com.inter.trade.ui.fragment.buyswipcard;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.inter.trade.data.BankCardInfo;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.parser.MyBankParser;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.BuySwipCardAddBankCardActivity;
import com.inter.trade.ui.BuySwipcardPayActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardOrderData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardOrderNoParser;
import com.inter.trade.ui.fragment.buyswipcard.util.GetAgentTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

@SuppressLint("ValidFragment")
public class BuySwipCardPayConfirmFragment extends BaseFragment implements OnClickListener,SwipListener,ResponseStateListener,
	AsyncLoadWorkListener{
	private Button cridet_back_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private EditText agentid_edittext;
	private TextView acount;
	
	/**
	 * 购买按钮
	 */
	private Button buy_swipcard_button;
	private EditText receiver_name_edit, receiver_mobile_edit, receiver_address_edit;
	
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
	
	private ImageView reduce;
	private ImageView plus;
	
	/**
	 * 折扣价
	 */
	private TextView produrezhepriceText;
	
	/**
	 * 总数
	 */
	private TextView totalText;
	
	/**
	 * 代理商代号
	 */
	private String agentid;
	
	private GetAgentTask getAgentTask;
	
	public static BuySwipCardPayConfirmFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new BuySwipCardPayConfirmFragment();
	}
	
	public BuySwipCardPayConfirmFragment()
	{
	}
	
	public BuySwipCardPayConfirmFragment(Bundle b) {
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
//		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.buy_card_swip_layout, container,false);
		initView(view);
		
		setTitle("购买刷卡器");
		setBackVisible();
		
		getAgentTask=new GetAgentTask(getActivity(), this);
		getAgentTask.execute();
//		setRightVisible(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				//				PromptUtil.showToast(getActivity(), "该功能正在开发...");
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), TelphonePayRecordActivity.class);
//				getActivity().startActivityForResult(intent, 200);
//			}
//		}, "历史地址");
		
//		openReader();
		
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
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_prompt = (TextView)view.findViewById(R.id.swip_prompt);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
		acount = (TextView)view.findViewById(R.id.acount);
		open_phone_edit = (EditText)view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText)view.findViewById(R.id.open_name_edit);
		coupon_layout =  (LinearLayout)view.findViewById(R.id.coupon_layout);
		agentid_edittext = (EditText)view.findViewById(R.id.agentid_edittext);
		
//		buy_swipcard_button = (Button)view.findViewById(R.id.buy_swipcard_button);
		
		if(bundle == null) return;
		
		produrezhepriceText = ((TextView)view.findViewById(R.id.produrezheprice));
		produrezhepriceText.setText(bundle.getString("produrezheprice")+"元  x 1");
		((TextView)view.findViewById(R.id.freight)).setText(bundle.getString("shyunfei")+"元");
//		double total;
		if(bundle.getString("produrezheprice") != null && bundle.getString("shyunfei") != null) {
			total = Double.valueOf(bundle.getString("produrezheprice")) + Double.valueOf(bundle.getString("shyunfei"));
		}else{
			total = Double.valueOf(bundle.getString("produrezheprice"));
		}
		
		totalText = ((TextView)view.findViewById(R.id.total));
		totalText.setText(total+"元");
		
//		((EditText)view.findViewById(R.id.receiver_name_edit)).setText(bundle.getString("shman"));
//		((EditText)view.findViewById(R.id.receiver_mobile_edit)).setText(bundle.getString("shphone"));
//		((EditText)view.findViewById(R.id.receiver_address_edit)).setText(bundle.getString("shaddress"));
		
		reduce =(ImageView)view.findViewById(R.id.reduce);
		plus = (ImageView)view.findViewById(R.id.plus);
		
		
//		((TextView)view.findViewById(R.id.mobile_rechamoney)).setText(bundle.getString(MoblieRechangeData.MRD_RECHAMONEY)+"元");
//		((TextView)view.findViewById(R.id.mobile_rechapaymoney)).setText(bundle.getString(MoblieRechangeData.MRD_RECHAPAYMONEY)+"元");
		
		//自动填充收货人，手机号，收货地址
		receiver_name_edit = (EditText)view.findViewById(R.id.receiver_name_edit);
		receiver_name_edit.setText(bundle.getString("shman"));
		receiver_mobile_edit = (EditText)view.findViewById(R.id.receiver_mobile_edit);
		receiver_mobile_edit.setText(bundle.getString("shphone"));
		receiver_address_edit = (EditText)view.findViewById(R.id.receiver_address_edit);
		receiver_address_edit.setText(bundle.getString("shaddress"));
		
		
		coupon_layout.setVisibility(View.GONE);
		
		cridet_back_btn.setOnClickListener(this);
//		bank_layout.setOnClickListener(this);
		
		reduce.setOnClickListener(this);
		plus.setOnClickListener(this);
		
//		buy_swipcard_button.setOnClickListener(this);
		
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
									   "orderbuy",
										this);
			mKeyTask.execute("");
			
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if(checkInput()){
//				showChuxuka();
				
				if(BuySwipCardAddBankCardActivity.giveup) {
					mBuyTask = new BuyTask();
					mBuyTask.execute("");
					BuySwipCardAddBankCardActivity.giveup = false;
					return;
				}
				if(agentid!=null&&!TextUtils.isEmpty(agentid)) {
					new BankTask().execute("");
				}else{
					mBuyTask = new BuyTask();
					mBuyTask.execute("");
				}
				
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				transaction.replace(R.id.func_container, new ReturnConfirmFragment());
//				transaction.commit();
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;
			
		case R.id.reduce:
			reduce();
			break;
		case R.id.plus:
			plus();
			break;

		default:
			break;
		}
	}
	
	private int mCount = 1;
	
	/**
	 * 总金额
	 */
	private double total ;
	
	/**
	 * 减少
	 */
	private void reduce() {
		mCount--;
		if(mCount<1){
			mCount=1;
		}
		setData(mCount);
	}
	
	/**
	 * 增加
	 */
	private void plus() {
		mCount++;
		if(mCount>100){
			mCount=100;
		}
		setData(mCount);
	}
	
	private void setData(int mCount) {
		produrezhepriceText.setText(bundle.getString("produrezheprice")+"元  x " + mCount);

		if(bundle.getString("produrezheprice") != null && bundle.getString("shyunfei") != null) {
			total = Double.valueOf(bundle.getString("produrezheprice")) *  mCount + Double.valueOf(bundle.getString("shyunfei"));
		}
		totalText.setText(total + "元");
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
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.clear();
		
		String cardNumber = card_edit.getText().toString();
		if(null == cardNumber || "".equals(cardNumber)){
			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		//银行卡号
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERFUCARDNO, cardNumber);
		
		if(bundle == null) {
			PromptUtil.showToast(getActivity(), "操作异常");
			return false;
		}
		
		String shouhuo_name = receiver_name_edit.getText().toString();
		if(shouhuo_name == null || shouhuo_name.equals("")) {
			PromptUtil.showToast(getActivity(), "收款人姓名不能为空");
			return false;
		}

		if(!UserInfoCheck.checkName(shouhuo_name)) {
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		
		//收货人
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERSHMAN, shouhuo_name);

		//
		String shouhuo_phone = receiver_mobile_edit.getText().toString();
		if(shouhuo_phone == null || shouhuo_phone.equals("")) {
			PromptUtil.showToast(getActivity(), "手机号码不能为空");
			return false;
		}

		if(!UserInfoCheck.checkMobilePhone(shouhuo_phone)) {
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		
		//收货人联系电话
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERSHPHONE, shouhuo_phone);
		
		//
		String shouhuo_address = receiver_address_edit.getText().toString();
		if(shouhuo_address == null || shouhuo_address.equals("")) {
			PromptUtil.showToast(getActivity(), "地址不能为空");
			return false;
		}
		
		if(shouhuo_address.length() <= 6) {
			PromptUtil.showToast(getActivity(), "地址长度太短");
			return false;
		}
		
		//收货地址
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.OREDERSHADDRESS, shouhuo_address);
		
		if(agentid != null && !agentid.equals("")) {
			BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.AGENTNO, agentid);
		}
		
		
		//购买产品ID
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERPRODUREID, bundle.getString("product_id"));
		
		//购买数量
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERNUM, mCount+"");
		
		//单个价格
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERPRICE, bundle.getString("produrezheprice"));
		
		//订单总额
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERMONEY, total+"");
		
		//收货地址ID
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERSHADDRESSID, bundle.getString("shaddressid"));
		
		//订单备注
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERMEMO,"");
		
		//运费金额
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.YUNMONEY, bundle.getString("shyunfei"));
		
		//运费价格
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.YUNPRICE, 4+"");
		
		//产品价格
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.PROMONEY, Double.valueOf(bundle.getString("produrezheprice")) *  mCount +"");

		//产品名称
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.PRODURENAME, bundle.getString("product_name"));
		
//		
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMONEY, bundle.getString(MoblieRechangeData.MRD_RECHAMONEY));
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAPAYMONEY, bundle.getString(MoblieRechangeData.MRD_RECHAPAYMONEY));
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMOBILE, bundle.getString(MoblieRechangeData.MRD_RECHAMOBILE));
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMOBILEPROV, bundle.getString(MoblieRechangeData.MRD_RECHAMOBILEPROV));
		//支付类型id ，默认为1
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERPAYTYPEID, 1+"");
		//发卡银行
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERFUCARDBANK, "");
//		
//		Log.i("mobile", bundle.getString(MoblieRechangeData.MRD_RECHAMONEY)+ "-"+bundle.getString(MoblieRechangeData.MRD_RECHAPAYMONEY)+"-"+
//		bundle.getString(MoblieRechangeData.MRD_RECHAMOBILE)+"-"+ bundle.getString(MoblieRechangeData.MRD_RECHAMOBILEPROV));
		
		
//		String openName = open_name_edit.getText().toString();
//		if(null == openName || "".equals(openName)){
//			PromptUtil.showToast(getActivity(), "请输入开户名");
//			return false;
//		}
//		if(!UserInfoCheck.checkName(openName)){
//			PromptUtil.showToast(getActivity(), "姓名格式不正确");
//			return false;
//		}
//		
//		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardman, openName);
//		String openPhone = open_phone_edit.getText().toString();
//		if(null == openPhone || "".equals(openPhone)){
//			PromptUtil.showToast(getActivity(), "请输入电话号码");
//			return false;
//		}
//		if(!UserInfoCheck.checkMobilePhone(openPhone)){
//			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
//			return false;
//		}
//		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardmobile, openPhone);
		
//		isSwipIn = true;
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
		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.PAYCARDID, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
//		BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.merReserved, 
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
//			 DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardbank, bankname);
			 BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERFUCARDBANK, bankname);
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
//		endCallStateService();
//		mobileReader.close();
//		closeReader();
		PayApp.mSwipListener= null;
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
//		cancelTimer();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mobileReader.close();
//		endCallStateService();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		mobileReader.open();
//		openReaderNow();
		setTitle("购买刷卡器");
		initSwipPic(PayApp.isSwipIn);
		
//		startCallStateService();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		mobileReader.close();
//		endCallStateService();
		log("onStop endCallStateService");
	}

	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
//		mobileReader.close();
//		endCallStateService();
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
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiBuyOderInfo", 
						"payOrderRq", BuySwipcardPayActivity.buySwipCardOrderData);
				BuySwipCardOrderNoParser authorRegParser = new BuySwipCardOrderNoParser();
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
						BuySwipcardPayActivity.mCommonData.clear();
						BuySwipcardPayActivity.mBankNo = mBkntno;
						
						
							
//							HashMap<String, String> item1 = new HashMap<String, String>();
//							item1.put("还贷银行",
//									DaikuanActivity.mDaikuanData.getValue(DaikuanData.shoucardbank)
//									);
//							DaikuanActivity.mCommonData.add(item1);
							
							HashMap<String, String> item2 = new HashMap<String, String>();
							item2.put("产品名称", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.PRODURENAME));
							BuySwipcardPayActivity.mCommonData.add(item2);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("单个价格", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.ORDERPRICE)+"元");
							BuySwipcardPayActivity.mCommonData.add(item3);
							
							HashMap<String, String> item4 = new HashMap<String, String>();
							item4.put("购买数量", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.ORDERNUM)+"个");
							BuySwipcardPayActivity.mCommonData.add(item4);
							
							HashMap<String, String> item10 = new HashMap<String, String>();
							item10.put("运费金额", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.YUNMONEY)+"元");
							BuySwipcardPayActivity.mCommonData.add(item10);
							
							HashMap<String, String> item5 = new HashMap<String, String>();
							item5.put("订单总额", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.ORDERMONEY)+"元");
							BuySwipcardPayActivity.mCommonData.add(item5);
							
							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("收货人", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.ORDERSHMAN));
							BuySwipcardPayActivity.mCommonData.add(item6);
							
							HashMap<String, String> item7 = new HashMap<String, String>();
							item7.put("收货人电话", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.ORDERSHPHONE));
							BuySwipcardPayActivity.mCommonData.add(item7);
							
							HashMap<String, String> item8 = new HashMap<String, String>();
							item8.put("收货地址", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.OREDERSHADDRESS));
							BuySwipcardPayActivity.mCommonData.add(item8);
							
							HashMap<String, String> item9 = new HashMap<String, String>();
							item9.put("支付银行卡号", BuySwipcardPayActivity.buySwipCardOrderData.getValue(BuySwipCardOrderData.ORDERFUCARDNO));
							BuySwipcardPayActivity.mCommonData.add(item9);
							
//							HashMap<String, String> item4 = new HashMap<String, String>();
//							item4.put("还贷金额", NumberFormatUtil.format2(DaikuanActivity.mDaikuanData.getValue(DaikuanData.paymoney)));
//							DaikuanActivity.mCommonData.add(item4);
//							
//							HashMap<String, String> item5 = new HashMap<String, String>();
//							item5.put("充值号码:", BuySwipcardPayActivity.buySwipCardOrderData.getValue(MoblieRechangeData.MRD_RECHAMOBILE));
//							BuySwipcardPayActivity.mCommonData.add(item5);
//							
//							HashMap<String, String> item8 = new HashMap<String, String>();
//							item8.put("号码归属:", BuySwipcardPayActivity.buySwipCardOrderData.getValue(MoblieRechangeData.MRD_RECHAMOBILEPROV));
//							BuySwipcardPayActivity.mCommonData.add(item8);
//							
//							HashMap<String, String> item6 = new HashMap<String, String>();
//							item6.put("充值金额:", NumberFormatUtil.format2(TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHAMONEY)));
//							TelephonePayActivity.mCommonData.add(item6);
//							
//							HashMap<String, String> item7 = new HashMap<String, String>();
//							item7.put("实际支付金额:", NumberFormatUtil.format2(TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHAPAYMONEY)));
//							TelephonePayActivity.mCommonData.add(item7);
//							
//							HashMap<String, String> item3 = new HashMap<String, String>();
//							item3.put("刷卡卡号:", TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHABKCARDNO));
//							TelephonePayActivity.mCommonData.add(item3);
//							
//							HashMap<String, String> item = new HashMap<String, String>();
//							item.put("交易请求号:", mBkntno);
//							BuySwipcardPayActivity.mCommonData.add(item);
							
							
							
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
	
	
	
	
	/**
	 * 请求银行卡信息
	 *
	 */
	class BankTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuCardInfo", 
						"readAuBkCardInfo", data);
				MyBankParser authorRegParser = new MyBankParser();
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
				PromptUtil.showToast(getActivity(),getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserBankResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						if(info.aushoucardno !=null && !info.aushoucardno.equals("")) {
							//当前账户已绑定银行卡
							
							BuySwipcardPayActivity.buySwipCardOrderData.sunMap.put(BuySwipCardOrderData.ORDERMONEY, (total-30*mCount)+"");
							mBuyTask = new BuyTask();
							mBuyTask.execute("");
						}
						else{
							getActivity().startActivity(new Intent(getActivity(), BuySwipCardAddBankCardActivity.class));
						}
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
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
		
	}
	
	
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserBankResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				info = new BankCardInfo();
				
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardman = data.find("/aushoucardman");
				if(aushoucardman != null){
					info.aushoucardman = aushoucardman.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardphone = data.find("/aushoucardphone");
				if(aushoucardphone != null){
					info.aushoucardphone = aushoucardphone.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardno = data.find("/aushoucardno");
				if(aushoucardno != null){
					info.aushoucardno = aushoucardno.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardbank = data.find("/aushoucardbank");
				if(aushoucardbank != null){
					info.aushoucardbank = aushoucardbank.get(0).mValue;
				}
				
				List<ProtocolData> aushoucardstate = data.find("/aushoucardstate");
				if(aushoucardstate != null){
					info.aushoucardstate = aushoucardstate.get(0).mValue;
				}
				
			}
		}
	}
	
	private BankCardInfo info;

	@Override
	public void onSuccess(Object obj, Class cla) {
		agentid=(String) obj;
	}

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		
	}

	@Override
	public void onFailure(String error) {
		
	}
	
	
	
	
	
	
	
	
	
}
