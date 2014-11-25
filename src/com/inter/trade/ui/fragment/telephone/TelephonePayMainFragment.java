package com.inter.trade.ui.fragment.telephone;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.IntentCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.net.SunHttpApi;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.TelephonePayActivity;
import com.inter.trade.ui.TelphonePayRecordActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask.FeeListener;
import com.inter.trade.ui.fragment.telephone.util.CanPayTask;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeDenominationData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeDenominationParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.inter.trade.xml.MobileAttribution;
import com.inter.trade.xml.MobileQueryAttributionHandler;

/**
 * 话费充值
 * @author zhichao.huang
 *
 */
public class TelephonePayMainFragment extends BaseFragment implements OnClickListener,/**SwipListener*/ FeeListener,ResponseStateListener{
	private Button cridet_back_btn;
	private LinearLayout bank_layout;
	
	private LinearLayout query_layout;
	private EditText query_input;
	private Button send;
	private Button addressbook;//通讯录button
	private EditText telephone_edit;//电话号码输入框
	private LinearLayout chongzhi10, chongzhi20, chongzhi30, chongzhi50, 
	chongzhi100, chongzhi200, chongzhi300, chongzhi400, chongzhi500;//充值10元Layout
	private TextView chongzhi10_button, chongzhi20_button, chongzhi30_button,
	chongzhi50_button, chongzhi100_button, chongzhi200_button, chongzhi300_button, chongzhi400_button, chongzhi500_button;//充值10元按钮
	
	/**
	 * 手机归属地
	 */
	private TextView mobileAttribution;
	private String mobileAttributionStr = null;
	
	//充值的金额
	private double money = 0.0;
	//实际支付的金额
	private double payMoney;
	private TextView payMoneyTextView;
	
	private CommonData mData = new CommonData();
	private BuyTask mBuyTask;
	
	
	/**
	 * 交易流水号 订单号
	 */
	private String mBkntno;
	
	public CommonData mfeeData=new CommonData();
	
	
	private Bundle bundle;
	/**
	 * 充值面额集合
	 */
	private ArrayList<MoblieRechangeDenominationData> mList = new ArrayList<MoblieRechangeDenominationData>();
	
	/***
	 * 默认选中的充值索引
	 */
	private int index=2;
	
	private LinearLayout[] lls=new LinearLayout[9];
	
	private TextView[] btns=new TextView[9];
	
	public static TelephonePayMainFragment getInstance(boolean flag){
		
		TelephonePayMainFragment f=new TelephonePayMainFragment();
		Bundle b=new Bundle();
		b.putBoolean("islauchinguide", flag);
		f.setArguments(b);
		return f;
		
	}
	
	
	public TelephonePayMainFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		LoginUtil.detection(getActivity());
//		initReader();
//		PayApp.mSwipListener = this;
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.telephone_new_layout, container,false);
		initView(view);
		bundle = getArguments();
		setTitle("话费充值");
		
		setRightVisible(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				PromptUtil.showToast(getActivity(), "该功能正在开发...");
				Intent intent = new Intent();
				intent.setClass(getActivity(), TelphonePayRecordActivity.class);
				getActivity().startActivityForResult(intent, 200);
			}
		}, "历史记录");
		
		setBackVisible();
//		openReader();
		/**
		 * 获取话费充值面额选择
		 */
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
		return view;
	}
	
	@Override
	protected void setBackVisible() {
		
		
		if (getActivity() == null) {
			return;
		}
		
		View view = getActivity().findViewById(R.id.iv_tilte_line);
		if(view!=null){
			view.setVisibility(View.VISIBLE);
		}
		
		
		back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(bundle!=null && bundle.getBoolean("islauchinguide")){
					Intent intent=new Intent();
					 intent.setClass(getActivity(), MainActivity.class);
					 intent.putExtra("loadmenu", true);
			            intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					getActivity().finish();
				}else{
					getActivity().finish();
				}
			}
		});

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.VISIBLE);
	}
	
	private void initView(View view){
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		addressbook = (Button)view.findViewById(R.id.addressbook);
		telephone_edit = (EditText)view.findViewById(R.id.telephone_edit);
		
		chongzhi10 = (LinearLayout)view.findViewById(R.id.chongzhi10);
		chongzhi10_button = (TextView)view.findViewById(R.id.chongzhi10_button);
		chongzhi20 = (LinearLayout)view.findViewById(R.id.chongzhi20);
		chongzhi20_button = (TextView)view.findViewById(R.id.chongzhi20_button);
		chongzhi30 = (LinearLayout)view.findViewById(R.id.chongzhi30);
		chongzhi30_button = (TextView)view.findViewById(R.id.chongzhi30_button);
		chongzhi50 = (LinearLayout)view.findViewById(R.id.chongzhi50);
		chongzhi50_button = (TextView)view.findViewById(R.id.chongzhi50_button);
		chongzhi100 = (LinearLayout)view.findViewById(R.id.chongzhi100);
		chongzhi100_button = (TextView)view.findViewById(R.id.chongzhi100_button);
		chongzhi200 = (LinearLayout)view.findViewById(R.id.chongzhi200);
		chongzhi200_button = (TextView)view.findViewById(R.id.chongzhi200_button);
		chongzhi300 = (LinearLayout)view.findViewById(R.id.chongzhi300);
		chongzhi300_button = (TextView)view.findViewById(R.id.chongzhi300_button);
		chongzhi400 = (LinearLayout)view.findViewById(R.id.chongzhi400);
		chongzhi400_button = (TextView)view.findViewById(R.id.chongzhi400_button);
		chongzhi500 = (LinearLayout)view.findViewById(R.id.chongzhi500);
		chongzhi500_button = (TextView)view.findViewById(R.id.chongzhi500_button);
		
		payMoneyTextView = (TextView)view.findViewById(R.id.payMoney);
		
		mobileAttribution= (TextView)view.findViewById(R.id.mobileAttribution);
		
		query_layout = (LinearLayout)view.findViewById(R.id.query_layout);
		query_input = (EditText)view.findViewById(R.id.query_input);
		send = (Button)view.findViewById(R.id.send);
		
		
		send.setOnClickListener(this);
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		addressbook.setOnClickListener(this);
		chongzhi10.setOnClickListener(this);
		chongzhi10_button.setOnClickListener(this);
		chongzhi20.setOnClickListener(this);
		chongzhi20_button.setOnClickListener(this);
		chongzhi30.setOnClickListener(this);
		chongzhi30_button.setOnClickListener(this);
		chongzhi50.setOnClickListener(this);
		chongzhi50_button.setOnClickListener(this);
		chongzhi100.setOnClickListener(this);
		chongzhi100_button.setOnClickListener(this);
		chongzhi200.setOnClickListener(this);
		chongzhi200_button.setOnClickListener(this);
		
		chongzhi300.setOnClickListener(this);
		chongzhi300_button.setOnClickListener(this);
		chongzhi400.setOnClickListener(this);
		chongzhi400_button.setOnClickListener(this);
		chongzhi500.setOnClickListener(this);
		chongzhi500_button.setOnClickListener(this);
		
		
		lls[0]=chongzhi10;
		lls[1]=chongzhi20;
		lls[2]=chongzhi30;
		lls[3]=chongzhi50;
		lls[4]=chongzhi100;
		lls[5]=chongzhi200;
		lls[6]=chongzhi300;
		lls[7]=chongzhi400;
		lls[8]=chongzhi500;
		
		btns[0]=chongzhi10_button;
		btns[1]=chongzhi20_button;
		btns[2]=chongzhi30_button;
		btns[3]=chongzhi50_button;
		btns[4]=chongzhi100_button;
		btns[5]=chongzhi200_button;
		btns[6]=chongzhi300_button;
		btns[7]=chongzhi400_button;
		btns[8]=chongzhi500_button;
		
		
		telephone_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("Hello", "onTextChanged:" + s + "-" + start + "-" + count + "-" + before); 
//				boolean isMobile;
//				if(start == 10){//当用户输入号码11位时，触发该动作
//					isMobile = isMobileNum(s.toString());
//					if(isMobile){
//						//查询
//						mobileQueryAttribution(s.toString());
//						defaultMobileMoney();
//					}else{
//						mobileAttribution.setVisibility(View.GONE);
//						PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
//					}
//				}
				
				boolean isMobile;
				
				if(s.toString().length() >= 11) {
//					if(start == 10){//当用户输入号码11位时，触发该动作
						isMobile = isMobileNum(s.toString());
						if(isMobile){
							//查询
							mobileQueryAttribution(s.toString());
							defaultMobileMoney();
						}else{
							mobileAttribution.setVisibility(View.GONE);
							PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
						}
//					}
				}else{
					mobileAttribution.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
	}
	
	/**
	 * 当选择号码后自动选择充值金额50
	 */
	private void defaultMobileMoney() {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(money != 0.0) return;
//				chongzhi10.setSelected(false);
//				chongzhi20.setSelected(true);
//				chongzhi30.setSelected(false);
//				chongzhi50.setSelected(false);
//				chongzhi100.setSelected(false);
//				chongzhi200.setSelected(false);
//				chongzhi300.setSelected(false);
//				chongzhi400.setSelected(false);
//				chongzhi500.setSelected(false);
//				String tempMoney = chongzhi20_button.getText().toString();
//				if(tempMoney != null)
//				  money = Double.valueOf(tempMoney.substring(0, tempMoney.indexOf("元")));
//				if(chongzhi20_button.getHint().toString() != null)
//				  payMoney = Double.valueOf(chongzhi20_button.getHint().toString());
//				setFee(null);
				
				for(int i=0;i<lls.length;i++){
					if(index==i){
						lls[i].setSelected(true);
					}else{
						lls[i].setSelected(false);
					}
				}
				String tempMoney = btns[index].getText().toString();
				if(tempMoney != null)
				  money = Double.valueOf(tempMoney.substring(0, tempMoney.indexOf("元")));
				if(btns[index].toString() != null)
				  payMoney = Double.valueOf(btns[index].getHint().toString());
				setFee(null);
			}
		});
	}
	

	private CanPayTask canPayTask;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			
			//invokeTelephonePayConfirm ();
			
			if(checkInput()){
				canPayTask=new CanPayTask(getActivity(), this);
				canPayTask.execute(money+"",telephone_edit.getText().toString());
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;
		case R.id.send:
//			sendSms();
			break;
		case R.id.addressbook://通讯录
		    addressBook();
			break;
		case R.id.chongzhi10_button:	
		case R.id.chongzhi10://充值10元
			chongzhi10.setSelected(true);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(false);
			String tempMoney = chongzhi10_button.getText().toString();
			if(tempMoney != null)
			  money = Double.valueOf(tempMoney.substring(0, tempMoney.indexOf("元")));
			if(chongzhi10_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi10_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi20_button:	
		case R.id.chongzhi20://充值20元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(true);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(false);
			String tempMoney20 = chongzhi20_button.getText().toString();
			if(tempMoney20 != null)
			  money = Double.valueOf(tempMoney20.substring(0, tempMoney20.indexOf("元")));
			if(chongzhi20_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi20_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi30_button:	
		case R.id.chongzhi30://充值30元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(true);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(false);
			String tempMoney30 = chongzhi30_button.getText().toString();
			if(tempMoney30 != null)
			  money = Double.valueOf(tempMoney30.substring(0, tempMoney30.indexOf("元")));
			if(chongzhi30_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi30_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi50_button:	
		case R.id.chongzhi50://充值50元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(true);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(false);
			String tempMoney50 = chongzhi50_button.getText().toString();
			if(tempMoney50 != null)
			  money = Double.valueOf(tempMoney50.substring(0, tempMoney50.indexOf("元")));
			if(chongzhi50_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi50_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi100_button:	
		case R.id.chongzhi100://充值100元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(true);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(false);
			String tempMoney100 = chongzhi100_button.getText().toString();
			if(tempMoney100 != null)
			  money = Double.valueOf(tempMoney100.substring(0, tempMoney100.indexOf("元")));
			if(chongzhi100_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi100_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi200_button:	
		case R.id.chongzhi200://充值200元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(true);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(false);
			String tempMoney200 = chongzhi200_button.getText().toString();
			if(tempMoney200 != null)
			  money = Double.valueOf(tempMoney200.substring(0, tempMoney200.indexOf("元")));
			if(chongzhi200_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi200_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi300_button:	
		case R.id.chongzhi300://充值200元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(true);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(false);
			String tempMoney300 = chongzhi300_button.getText().toString();
			if(tempMoney300 != null)
			  money = Double.valueOf(tempMoney300.substring(0, tempMoney300.indexOf("元")));
			if(chongzhi300_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi300_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi400_button:	
		case R.id.chongzhi400://充值200元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(true);
			chongzhi500.setSelected(false);
			String tempMoney400 = chongzhi400_button.getText().toString();
			if(tempMoney400 != null)
			  money = Double.valueOf(tempMoney400.substring(0, tempMoney400.indexOf("元")));
			if(chongzhi400_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi400_button.getHint().toString());
			setFee(null);
			break;
		case R.id.chongzhi500_button:	
		case R.id.chongzhi500://充值200元
			chongzhi10.setSelected(false);
			chongzhi20.setSelected(false);
			chongzhi30.setSelected(false);
			chongzhi50.setSelected(false);
			chongzhi100.setSelected(false);
			chongzhi200.setSelected(false);
			chongzhi300.setSelected(false);
			chongzhi400.setSelected(false);
			chongzhi500.setSelected(true);
			String tempMoney500 = chongzhi500_button.getText().toString();
			if(tempMoney500 != null)
			  money = Double.valueOf(tempMoney500.substring(0, tempMoney500.indexOf("元")));
			if(chongzhi500_button.getHint().toString() != null)
			  payMoney = Double.valueOf(chongzhi500_button.getHint().toString());
			setFee(null);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 调度手机通讯录
	 */
	private void addressBook() {
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, 2);
	}
	
	/**
	 * 调度到话费充值确认界面，也就是刷卡支付页面
	 */
	private void invokeTelephonePayConfirm () {
		Intent intent = new Intent();
		intent.setClass(getActivity(), TelephonePayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(MoblieRechangeData.MRD_RECHAMOBILE, telephone_edit.getText().toString());
		bundle.putString(MoblieRechangeData.MRD_RECHAMOBILEPROV, mobileAttributionStr);
		bundle.putString(MoblieRechangeData.MRD_RECHAMONEY, money+"");
		bundle.putString(MoblieRechangeData.MRD_RECHAPAYMONEY, payMoney+"");
		bundle.putBoolean(MoblieRechangeData.ISLAUCHGUIDE,  this.bundle==null?false:this.bundle.getBoolean("islauchinguide"));
		intent.putExtra("MoblieRechange", bundle);
		startActivityForResult(intent, 3);
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.putExtra("type", "1");
		intent.setClass(getActivity(), BankListActivity.class);
		startActivityForResult(intent, 1);
	}
	
	private boolean isMobileNum(String mobile) {
		return UserInfoCheck.checkMobilePhone(mobile);
	}
	
	private boolean checkInput(){
		
		String telnumber = telephone_edit.getText().toString();
		if(null == telnumber || "".equals(telnumber) || telephone_edit.length() != 11
				|| !isMobileNum(telnumber)){
			PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
			return false;
		}
		
		if(money <= 0.0 || payMoney <= 0.0) {
			PromptUtil.showToast(getActivity(), "请选择充值金额");
			return false;
		}
		if(mobileAttributionStr==null){
			PromptUtil.showToast(getActivity(), "请稍等,正在获取手机号码的归属地");
			mobileQueryAttribution(telnumber);
			return false;
		}
		
		CridetCardFragment.mJournalData.putValue(JournalData.paycardid, PayApp.mKeyCode);
		CridetCardFragment.mJournalData.putValue(JournalData.merReserved, PayApp.merReserved.toString());
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		 
		 switch (requestCode) { 
		 case 2: 
			 if (data == null) { 
				 return; 
			 } 
			 String phoneNumber = null; 
			 Uri contactData = data.getData(); 
			 if (contactData == null) { 
				 return ; 
			 } 
			 Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null); 
			 if (cursor.moveToFirst()) { 
				 //	                  String name = cursor.getString(cursor 
						 //	                          .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 
				 String hasPhone = cursor 
						 .getString(cursor 
								 .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
				 String id = cursor.getString(cursor 
						 .getColumnIndex(ContactsContract.Contacts._ID)); 
				 if (hasPhone.equalsIgnoreCase("1")) { 
					 hasPhone = "true"; 
				 } else { 
					 hasPhone = "false"; 
				 } 
				 if (Boolean.parseBoolean(hasPhone)) { 
					 Cursor phones = getActivity().getContentResolver().query( 
							 ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							 null, 
							 ContactsContract.CommonDataKinds.Phone.CONTACT_ID 
							 + " = " + id, null, null); 
					 while (phones.moveToNext()) { 
						 phoneNumber = phones 
								 .getString(phones 
										 .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
					 } 
					 phones.close(); 
				 } 
			 } 
			 
			 //模拟带连接符“-”的手机号码，如：138-8888-8888
//			 String tempNum = "0138-8888-88-88";
			 String tempNum = phoneNumber.replace(" ", "");
			 
			 String[] phoneNumbers = tempNum.split("-");
			 
			 String tempPhoneNumber = "";
			 for(int i = 0; i < phoneNumbers.length ; i ++){
				 tempPhoneNumber +=  phoneNumbers[i];
			 }
			 if(tempPhoneNumber != null && !tempPhoneNumber.equals("")){
				 if(tempPhoneNumber.length() != 11){
					 String num = tempPhoneNumber.substring(0, 1);
					 String tempNumber = "";
					 if( num.equals("0") ){
						 tempNumber =  tempPhoneNumber.substring(1);
						 if(isMobileNum(tempNumber)){
							 telephone_edit.setText(tempNumber);
							 mobileQueryAttribution(tempNumber);
						 }else{
							 PromptUtil.showToast(getActivity(), "号码格式不正确");
						 }
					 }
				 }else{
					 if(isMobileNum(tempPhoneNumber)){
						 telephone_edit.setText(tempPhoneNumber);
						 mobileQueryAttribution(tempPhoneNumber);
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
				 }
			 }else{
				 if(tempNum != null && !tempNum.equals("") && tempNum.length() == 11 && isMobileNum(tempNum)){
					 if(isMobileNum(tempNum)){
						 telephone_edit.setText(tempNum);
						 mobileQueryAttribution(tempNum);
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
					 
				 }else{
					 if(tempNum == null || tempNum.equals("")){
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
						 return;
					 }
					 if(tempNum.length() > 11){
						 String num = tempNum.substring(0, 1);
						 if( num.equals("0") ){
							 if(isMobileNum(tempNum.substring(1))){
								 telephone_edit.setText(tempNum.substring(1));
								 mobileQueryAttribution(tempNum.substring(1));
							 }else{
								 PromptUtil.showToast(getActivity(), "号码格式不正确");
							 }
						 }
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
				 }
				
			 }
//			 telephone_edit.setText(phoneNumber);
//			 mobileQueryAttribution(phoneNumber);
			 break; 
		 } 


		 
	}
	/**
	 * 手机归属地查询
	 * @param mobileNumber 手机号码
	 */
	public void mobileQueryAttribution (final String mobileNumber){
		mobileAttributionStr=null;//先将归属地置空
		new Thread(new Runnable() {
			MobileAttribution attribution;
			@Override
			public void run() {
				MobileQueryAttributionHandler handle = new MobileQueryAttributionHandler();
				InputStream is = SunHttpApi.requestHttp("http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=" + mobileNumber);
				if(is !=null) {
					attribution = handle.parser(is,"GBK");
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							if(attribution.getProvince() == null || attribution.getSupplier() == null) {
								mobileAttribution.setVisibility(View.GONE);
								return;
							}
							
							mobileAttribution.setVisibility(View.VISIBLE);
							mobileAttributionStr = attribution.getProvince()+attribution.getSupplier();
							mobileAttribution.setText("号码归属地:"+attribution.getProvince()+attribution.getSupplier());
							defaultMobileMoney();
						}
					});
				}else{
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mobileAttribution.setVisibility(View.GONE);
						}
					});
				}
				
			}
		}).start();
	}
	
	public static String inputStream2String(InputStream is){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i=-1;
			while((i=is.read())!=-1){
				baos.write(i);
			}
			return baos.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
    public void setFee(String fee){
		payMoneyTextView.setText("实际支付金额: "+NumberFormatUtil.format2(String.valueOf(payMoney))+"元");
		payMoneyTextView.setTextColor(android.graphics.Color.RED);
	}
	
	@Override
	public void result(int state, String fee, ArrayList<ArriveData> datas) {
		PromptUtil.dissmiss();
		if(state ==0){
			setFee(fee);
		}else{
//			isFeeSuccess =false;
			PromptUtil.showToast(getActivity(), "获取手续费失败");
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		PayApp.mSwipListener= null;
	}

	@Override
	public void onPause() {
		super.onPause();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		super.onResume();
		setTitle("话费充值");
	}

	@Override
	public void onStop() {
		super.onStop();
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
	
	

	/**
	 * 超时 再次登录成功后该方法得到调用
	 */
	@Override
	public void onTimeout() {
		/**
		 * 获取话费充值面额选择
		 */
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
	}
	
	/**
	 * 隐藏充值面额
	 */
	private void hideDenominationButton () {
		mList.clear();
		
		chongzhi10.setVisibility(View.GONE);
		chongzhi20.setVisibility(View.GONE);
		chongzhi30.setVisibility(View.GONE);
		chongzhi50.setVisibility(View.GONE);
		chongzhi100.setVisibility(View.GONE);
		chongzhi200.setVisibility(View.GONE);
		chongzhi300.setVisibility(View.GONE);
		chongzhi400.setVisibility(View.GONE);
		chongzhi500.setVisibility(View.GONE);
		
	}

	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiMobileRechargeInfoV2", 
						"ReadPerValue", mData);
				MoblieRechangeDenominationParser rechangeDenominationParser = new MoblieRechangeDenominationParser();
				mRsp = HttpUtil.doRequest(rechangeDenominationParser, mDatas);
			} catch (Exception e) {
				e.printStackTrace();
				mRsp =null;
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					hideDenominationButton ();
					parserResponse(mDatas);

					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						query_layout.setVisibility(View.VISIBLE);
//						query_input.setText(mMessage);
						
						for(int i = 0; i < mList.size(); i ++){
							if(i == 0){
								chongzhi10.setVisibility(View.VISIBLE);
								chongzhi10_button.setText(mList.get(i).rechamoney+"元");
								chongzhi10_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 1){
								chongzhi20.setVisibility(View.VISIBLE);
								chongzhi20_button.setText(mList.get(i).rechamoney+"元");
								chongzhi20_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 2){
								chongzhi30.setVisibility(View.VISIBLE);
								chongzhi30_button.setText(mList.get(i).rechamoney+"元");
								chongzhi30_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 3){
								chongzhi50.setVisibility(View.VISIBLE);
								chongzhi50_button.setText(mList.get(i).rechamoney+"元");
								chongzhi50_button.setHint(mList.get(i).rechapaymoney);
								
								chongzhi100.setVisibility(View.INVISIBLE);
								chongzhi200.setVisibility(View.INVISIBLE);
							}else if(i == 4){
								chongzhi100.setVisibility(View.VISIBLE);
								chongzhi100_button.setText(mList.get(i).rechamoney+"元");
								chongzhi100_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 5){
								chongzhi200.setVisibility(View.VISIBLE);
								chongzhi200_button.setText(mList.get(i).rechamoney+"元");
								chongzhi200_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 6){
								chongzhi300.setVisibility(View.VISIBLE);
								chongzhi300_button.setText(mList.get(i).rechamoney+"元");
								chongzhi300_button.setHint(mList.get(i).rechapaymoney);
								
								chongzhi400.setVisibility(View.INVISIBLE);
								chongzhi500.setVisibility(View.INVISIBLE);
							}else if(i == 7){
								chongzhi400.setVisibility(View.VISIBLE);
								chongzhi400_button.setText(mList.get(i).rechamoney+"元");
								chongzhi400_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 8){
								chongzhi500.setVisibility(View.VISIBLE);
								chongzhi500_button.setText(mList.get(i).rechamoney+"元");
								chongzhi500_button.setHint(mList.get(i).rechapaymoney);
							}
							
							if("1".equals( mList.get(i).rechaisdefault )){
								index=i;
							}
							
						}
						
//						if(PhoneInfoUtil.getNativePhoneNumber(getActivity()) != null) {
//							String telnumber = null;
//							telnumber = PhoneInfoUtil.getNativePhoneNumber(getActivity());
//							
//							if(telnumber != null && isMobileNum(telnumber)) {
//								telephone_edit.setText(telnumber);
//								//查询
//								mobileQueryAttribution(telnumber);
//								defaultMobileMoney();
//							}
//						}
						if(!TextUtils.isEmpty(PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, ""))) {
							String telnumber = null;
							telnumber = PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, "");
							
							if(telnumber != null && isMobileNum(telnumber)) {
								telephone_edit.setText(telnumber);
								//查询
								mobileQueryAttribution(telnumber);
								defaultMobileMoney();
							}
						}
						
						if( mList != null && mList.size() > 0) {
							cridet_back_btn.setVisibility(View.VISIBLE);//加载完面额显示充值按钮
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
				if (result != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					MoblieRechangeDenominationData picData = new MoblieRechangeDenominationData();
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

	@Override
	public void onSuccess(Object obj, Class cla) {//可以充值
		boolean isCan =(Boolean)obj;
		if(isCan){
			invokeTelephonePayConfirm ();
		}else{
			PromptUtil.showToast(getActivity(), "该面值暂不可以充值");
		}
	}
}
