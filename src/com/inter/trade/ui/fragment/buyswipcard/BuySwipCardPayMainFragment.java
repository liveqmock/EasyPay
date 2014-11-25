package com.inter.trade.ui.fragment.buyswipcard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts.People.Phones;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Log;
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
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.BuySwipCardAddressRecordActivity;
import com.inter.trade.ui.BuySwipCardRecordActivity;
import com.inter.trade.ui.BuySwipcardPayActivity;
import com.inter.trade.ui.CommonActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.TelphonePayRecordActivity;
import com.inter.trade.ui.TranserRecordActivity;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.TelephonePayActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.balance.BalanceParser;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardProductData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardProductParser;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask.FeeListener;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanfeeParser;
//import com.inter.trade.ui.fragment.telephone.TelephonePayConfirmFragment.BuyTask;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeDenominationData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeDenominationParser;
import com.inter.trade.ui.fragment.transfer.TransferRecordFragment;
import com.inter.trade.ui.fragment.transfer.TransferFragment.TransferType;
import com.inter.trade.ui.fragment.transfer.util.TransferData;
import com.inter.trade.ui.fragment.transfer.util.TransferNoParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.inter.trade.xml.MobileAttribution;
import com.inter.trade.xml.MobileQueryAttributionHandler;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 购买刷卡器主页面
 * @author zhichao.huang
 *
 */
public class BuySwipCardPayMainFragment extends BaseFragment implements OnClickListener,/**SwipListener*/ FeeListener{
	
	private Button cridet_back_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private TextView acount;
	
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
	 * 购买按钮
	 */
	private Button buy_swipcard_button;
	/**
	 * 产品名称
	 */
	private TextView product_name;
	
	/**
	 * 产品简介
	 */
	private TextView product_memo;
	
	/**
	 * 优惠价格
	 */
	private TextView product_zheprice;
	
	/**
	 * 原价
	 */
	private TextView product_price;
	
	/**
	 * 产品ID
	 */
	private String product_id ="1";
	
	
	
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
	
	private String mBankName;
	private String mBankNoString;//银行卡no
	private String mBankIdString;
	
	private String mMessage ="";
	private String mPhoneString =""; 
	private boolean isSelectedBank=false;
	private String mCardNo;
	
	private OrderData mOrderData;
	
	private CommonData mData = new CommonData();
	private String merReserved ="{}";
	private BuyTask mBuyTask;
	
	private CreditCardfeeTask mCardfeeTask;
	
	/**
	 * 交易流水号 订单号
	 */
	private String mBkntno;
	
	public CommonData mfeeData=new CommonData();
	
	/**
	 * 充值面额集合
	 */
	private ArrayList<BuySwipCardProductData> mList = new ArrayList<BuySwipCardProductData>();
	
	public BuySwipCardPayMainFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
//		initReader();
//		PayApp.mSwipListener = this;
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.buy_swipcard_layout, container,false);
		initView(view);
		
		setTitle("购买刷卡器");
		setBackVisible();
		setRightVisible(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				PromptUtil.showToast(getActivity(), "该功能正在开发...");
				Intent intent = new Intent();
				intent.setClass(getActivity(), BuySwipCardRecordActivity.class);
				getActivity().startActivityForResult(intent, 200);
			}
		}, "历史记录");
//		openReader();
		/**
		 * 获取商品属性
		 */
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
		return view;
	}
	
	private void initView(View view){
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_prompt = (TextView)view.findViewById(R.id.swip_prompt);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
		acount = (TextView)view.findViewById(R.id.acount);
		
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
		
		
		//-------------------------------------------------
		buy_swipcard_button = (Button)view.findViewById(R.id.buy_swipcard_button);
		buy_swipcard_button.setOnClickListener(this);
		
		product_name = (TextView)view.findViewById(R.id.product_name);
		product_memo = (TextView)view.findViewById(R.id.product_memo);
		product_zheprice = (TextView)view.findViewById(R.id.product_zheprice);
		product_price = (TextView)view.findViewById(R.id.product_price);
		
		//原价 加上删除线
		SpannableString sp = new SpannableString(product_price.getText().toString());  
		sp.setSpan(new StrikethroughSpan(), 0, product_price.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
		product_price.setText(sp); 
		
		//-------------------------------------------------
		
		
		
		
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
		
		telephone_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("Hello", "onTextChanged:" + s + "-" + start + "-" + count + "-" + before); 
				boolean isMobile;
				if(start == 10){//当用户输入号码11位时，触发该动作
					isMobile = isMobileNum(s.toString());
					if(isMobile){
						//查询
						mobileQueryAttribution(s.toString());
						defaultMobileMoney();
					}else{
						mobileAttribution.setVisibility(View.GONE);
						PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
					}
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
				chongzhi10.setSelected(false);
				chongzhi20.setSelected(true);
				chongzhi30.setSelected(false);
				chongzhi50.setSelected(false);
				chongzhi100.setSelected(false);
				chongzhi200.setSelected(false);
				chongzhi300.setSelected(false);
				chongzhi400.setSelected(false);
				chongzhi500.setSelected(false);
				String tempMoney = chongzhi20_button.getText().toString();
				if(tempMoney != null)
				  money = Double.valueOf(tempMoney.substring(0, tempMoney.indexOf("元")));
				if(chongzhi20_button.getHint().toString() != null)
				  payMoney = Double.valueOf(chongzhi20_button.getHint().toString());
				setFee(null);
			}
		});
	}
	
//	private void initSwipPic(boolean flag){
//		if(flag){
//			swip_prompt.setText(getString(R.string.has_checked_swip));
//			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
//		}else{
//			swip_prompt.setText(getString(R.string.cridet_insert));
//			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
//		}
//	}
	
//	@Override
//	public void recieveCard(CardData data) {
//		// TODO Auto-generated method stub
//		card_edit.setText(data.pan);
//		merReserved = "{";
//		merReserved += data.firmwareVersion;
//		merReserved += "|";
//		merReserved += data.encryptionMode;
//		merReserved += "|";
//		merReserved += data.trackInfo;
//		merReserved += "|";
//		merReserved += data.encryptionMode;
//		merReserved += "|";
//		merReserved += data.xxx;
//		merReserved += "|";
//		merReserved += data.last4Pan;
//		merReserved += "|";
//		merReserved += data.expiryDate;
//		merReserved += "|";
//		merReserved += data.userName;
//		merReserved += "|";
//		merReserved += data.ksn;
//		merReserved += "|";
//		merReserved += data.encrypedData;
//		merReserved += "|";
//		merReserved += data.pan;
//		merReserved += "|";
//		merReserved += data.decrypedData;
//		merReserved += "}";
//		Log.d("CardInfo", "All:"+merReserved);
//		Log.d("CardInfo", "firmwareVersion:"+data.firmwareVersion);
//		Log.d("CardInfo", "encryptionMode:"+data.encryptionMode);		
//		Log.d("CardInfo", "trackInfo:"+data.trackInfo);
//		Log.d("CardInfo", "encryptionMode:"+data.encryptionMode);
//		Log.d("CardInfo", "first6Pan:"+data.first6Pan);
//		Log.d("CardInfo", "xxx:"+data.xxx);
//		Log.d("CardInfo", "last4Pan:"+data.last4Pan);
//		Log.d("CardInfo", "expiryDate:"+data.expiryDate);
//		Log.d("CardInfo", "userName:"+data.userName);
//		Log.d("CardInfo", "ksn:"+data.ksn);
//		Log.d("CardInfo", "encrypedData:"+data.encrypedData);
//		Log.d("CardInfo", "pan:"+data.pan);
//		Log.d("CardInfo", "decrypedData:"+data.decrypedData);		
//	}

//	@Override
//	public void checkedCard(boolean flag) {
//		// TODO Auto-generated method stub
//		if(flag){
//			boolean s = PayApp.openReaderNow();
//			log("status : "+s);
//			swip_prompt.setText(getString(R.string.has_checked_swip));
//			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
//		}else{
//			swip_prompt.setText(getString(R.string.cridet_insert));
//			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
//		}
//	}
	
//	@Override
//	public void progress(int status, String message) {
//		// TODO Auto-generated method stub
//		switch (status) {
//		case SWIPING_START:
//			PromptUtil.showToast(getActivity(), message);
//			break;
//		case SWIPING_FAIL:
//			PromptUtil.showToast(getActivity(), message);
//					break;
//		case SWIPING_SUCCESS:
//			PromptUtil.showToast(getActivity(), message);
//			
//			break;
//		default:
//			break;
//		}
//		
//	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.buy_swipcard_button:
			if(checkInput()){
//				mBuyTask = new BuyTask();
//				mBuyTask.execute("");
//				showDialog();
				invokeTelephonePayConfirm ();
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
//		intent.setClass(getActivity(), BuySwipcardPayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("product_id", product_id);
		bundle.putString("product_name", product_name.getText().toString());
		bundle.putString("produrezheprice", product_zheprice.getText().toString());
		intent.putExtra("product_card", bundle);
		intent.setClass(getActivity(), BuySwipCardAddressRecordActivity.class);
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
//		 Pattern p = Pattern
//	                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	        Matcher m = p.matcher(mobile);
//	        return m.matches();
	}
	
	private boolean checkInput(){
		
//		String telnumber = telephone_edit.getText().toString();
//		if(null == telnumber || "".equals(telnumber) || telephone_edit.length() != 11
//				|| !isMobileNum(telnumber)){
//			PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
//			return false;
//		}
//		
//		if(money <= 0.0 || payMoney <= 0.0) {
//			PromptUtil.showToast(getActivity(), "请选择充值金额");
//			return false;
//		}
		
		if(product_id == null || "".equals(product_id)){
			PromptUtil.showToast(getActivity(), "产品ID为空");
			return false;
		}
		
		String zheprice = product_zheprice.getText().toString();
		if( zheprice == null || "".equals(zheprice)){
			PromptUtil.showToast(getActivity(), "产品优惠价为空");
			return false;
		}
		
		
//		if(!isSelectedBank){
//			PromptUtil.showToast(getActivity(), "请选择银行");
//			return false;
//		}
		
		
//		String cardNumber = card_edit.getText().toString();
//		if(null == cardNumber || "".equals(cardNumber)){
//			PromptUtil.showToast(getActivity(), "请输入卡号");
//			return false;
//		}
//		if(!UserInfoCheck.checkBandCid(cardNumber)){
//			PromptUtil.showToast(getActivity(), "卡号格式不正确");
//			return false;
//		}
//		mCardNo = cardNumber;
//		mData.putValue("", mCardNo);
//		CridetCardFragment.mJournalData.putValue(JournalData.fucardno, cardNumber);
		
		
		
//		if(!isSwipIn){
//			if(null == mKeyCode || "".equals(mKeyCode)){
//				PromptUtil.showToast(getActivity(), "请刷卡");
//			}else{
//				PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			}
//			return false;
//		}
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
		 
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 String bankno = data.getStringExtra(BankListActivity.BANK_NO);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 bank_name.setText(bankname);
			 mBankName= bankname;
			 mBankIdString= bankid;
			 mBankNoString = bankno;
		 }
		 //银联支付结果
		 
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
			 String tempNum = phoneNumber;
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
	
	/**
	 * 计算手续费
	 */
	private void getCost(){
		mCardfeeTask = new CreditCardfeeTask(getActivity(), initTaskData());
		mCardfeeTask.setFeeListener(this);
		mCardfeeTask.execute("");
		
	}
	
	private TaskData initTaskData(){
		TaskData taskData = new TaskData();
		mfeeData.sunMap.put("money", money+"");
		taskData.mCommonData = mfeeData;
		taskData.apiName="ApiPayinfo";
//		if(mType.equals(TransferType.USUAL_TRANSER)){
			taskData.funcName="getTransferPayfee";
//		}else{
//			taskData.funcName="getSupTransferPayfee";
//		}
		
		taskData.mNetParser=new DaikuanfeeParser();
		return taskData;
	}
	
    public void setFee(String fee){
//    	payMoney = Integer.parseInt(fee) + money;
//    	payMoney = money;
		payMoneyTextView.setText("实际支付金额: "+NumberFormatUtil.format2(String.valueOf(payMoney))+"元");
		payMoneyTextView.setTextColor(android.graphics.Color.RED);
//		isSetFee =true;
//			int cost = Integer.parseInt(fee);
//			int money = Integer.parseInt(paymoney);
//			DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.money, (cost+money)+"");
//			Log.d("DaikuanActivity", DaikuanActivity.mDaikuanData.getValue(DaikuanData.money));
//		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.payfee, fee);
	}
	
	@Override
	public void result(int state, String fee, ArrayList<ArriveData> datas) {
		PromptUtil.dissmiss();
		if(state ==0){
			setFee(fee);
//			isFeeSuccess =true;
//			CommonActivity.mTransferData.putValue(TransferData.payfee	, fee.replace("元", ""));
//			cost_container.removeAllViews();
//			mViews.clear();
//			for(ArriveData data : datas){
//				ArriveView view = new ArriveView(getActivity(), data,this);
//				cost_container.addView(view.mView);
//				mViews.add(view);
//			}
//			if(isNext){
//				isNext = false;
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), CommonActivity.class);
//				startActivity(intent);
//			}
		}else{
//			isFeeSuccess =false;
			PromptUtil.showToast(getActivity(), "获取手续费失败");
		}
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		endCallStateService();
//		mobileReader.close();
		PayApp.mSwipListener= null;
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
//		mobileReader.open(false);
		setTitle("购买刷卡器");
//		initSwipPic(PayApp.isSwipIn);
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

	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
//				mData.putValue("bankid", mBankIdString);
//				mData.putValue("bankcardno", mCardNo);
//				mData.putValue("bankname", mBankName);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiBuyOderInfo", 
						"readOrderProinfo", mData);
				BuySwipCardProductParser buySwipCardProductParser = new BuySwipCardProductParser();
				mRsp = HttpUtil.doRequest(buySwipCardProductParser, mDatas);
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
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						
						
						product_id = mList.get(0).produreid;
						product_name.setText(mList.get(0).produrename);
						product_memo.setText(mList.get(0).produrememo);
						product_zheprice.setText(mList.get(0).produrezheprice);
						
						String tempPrice = "原价: ￥" + mList.get(0).produreprice;
						//原价 加上删除线
						SpannableString sp = new SpannableString(tempPrice);  
						sp.setSpan(new StrikethroughSpan(), 0, tempPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
						product_price.setText(sp); 
						
						//当没有更新出数据的时候，应该禁止掉购买按钮的监听
						
						
						
//						query_layout.setVisibility(View.VISIBLE);
//						query_input.setText(mMessage);
						
//						for(int i = 0; i < mList.size(); i ++){
//							if(i == 0){
//								chongzhi10.setVisibility(View.VISIBLE);
//								chongzhi10_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi10_button.setHint(mList.get(i).rechapaymoney);
//							}else if(i == 1){
//								chongzhi20.setVisibility(View.VISIBLE);
//								chongzhi20_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi20_button.setHint(mList.get(i).rechapaymoney);
//							}else if(i == 2){
//								chongzhi30.setVisibility(View.VISIBLE);
//								chongzhi30_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi30_button.setHint(mList.get(i).rechapaymoney);
//							}else if(i == 3){
//								chongzhi50.setVisibility(View.VISIBLE);
//								chongzhi50_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi50_button.setHint(mList.get(i).rechapaymoney);
//								
//								chongzhi100.setVisibility(View.INVISIBLE);
//								chongzhi200.setVisibility(View.INVISIBLE);
//							}else if(i == 4){
//								chongzhi100.setVisibility(View.VISIBLE);
//								chongzhi100_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi100_button.setHint(mList.get(i).rechapaymoney);
//							}else if(i == 5){
//								chongzhi200.setVisibility(View.VISIBLE);
//								chongzhi200_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi200_button.setHint(mList.get(i).rechapaymoney);
//							}else if(i == 6){
//								chongzhi300.setVisibility(View.VISIBLE);
//								chongzhi300_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi300_button.setHint(mList.get(i).rechapaymoney);
//								
//								chongzhi400.setVisibility(View.INVISIBLE);
//								chongzhi500.setVisibility(View.INVISIBLE);
//							}else if(i == 7){
//								chongzhi400.setVisibility(View.VISIBLE);
//								chongzhi400_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi400_button.setHint(mList.get(i).rechapaymoney);
//							}else if(i == 8){
//								chongzhi500.setVisibility(View.VISIBLE);
//								chongzhi500_button.setText(mList.get(i).rechamoney+"元");
//								chongzhi500_button.setHint(mList.get(i).rechapaymoney);
//							}
//							
//						}
						
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
	
	
	
	private void sendSms(){
		if(query_input.getText().toString()==null ||"".equals(query_input.getText().toString())){
			PromptUtil.showToast(getActivity(), "请输入查询短信");
			return;
		}
//		Uri uri = Uri.parse("smsto:"+mPhoneString);          
//		Intent it = new Intent(Intent.ACTION_SENDTO, uri);          
//		it.putExtra("sms_body", mMessage);          
//		getActivity().startActivity(it);
		//直接调用短信接口发短信
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(mPhoneString, null, mMessage, null, null);  
//		smsManager.sendTextMessage("18664676691", null, mMessage, null, null);  
		PromptUtil.showToast(getActivity(), "短信发送成功，请留意收件箱");
	}
//	private void parserResoponse(List<ProtocolData> params){
//		ResponseData response = new ResponseData();
//		LoginUtil.mLoginStatus.mResponseData = response;
//		for(ProtocolData data :params){
//			if(data.mKey.equals(ProtocolUtil.msgheader)){
//				 ProtocolUtil.parserResponse(response, data);
//			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
//				
//				List<ProtocolData> result1 = data.find("/result");
//				if(result1 != null){
//					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
//				}
//				
//				List<ProtocolData> message = data.find("/message");
//				if(message != null){
//					LoginUtil.mLoginStatus.message = message.get(0).mValue;
//				}
//				
//				List<ProtocolData> smsmsg = data.find("/smsmsg");
//				if(smsmsg != null){
//					mMessage = smsmsg.get(0).mValue;
//				}
//				
//				List<ProtocolData> smsphone = data.find("/smsphone");
//				if(smsphone != null){
//					mPhoneString = smsphone.get(0).mValue;
//				}
//				
//			}
//		}
//	}
	
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
//				List<ProtocolData> msgallcount = data.find("/msgallcount");
//				if(msgallcount != null){
//					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
//				}
//				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
//				if(msgdiscount != null){
//					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
//				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					BuySwipCardProductData picData = new BuySwipCardProductData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("produreid")){
									picData.produreid  = item.mValue;
									
								}else if(item.mKey.equals("produrename")){
									picData.produrename  = item.mValue;
									
								}else if(item.mKey.equals("produreprice")){
									
									picData.produreprice  = item.mValue;
									
								}else if(item.mKey.equals("produrezheprice")){
									
									picData.produrezheprice  = item.mValue;
									
								}else if(item.mKey.equals("produrememo")){
									
									picData.produrememo  = item.mValue;
									
								}else if(item.mKey.equals("produrelimitnum")){
									
									picData.produrelimitnum  = item.mValue;
								}/**else if(item.mKey.equals("state")){
									
									picData.state  = item.mValue;
								}else if(item.mKey.equals("allmoney")){
									
									picData.allmoney  = item.mValue;
								}else if(item.mKey.equals("huancardbank")){
									
									picData.huancardbank  = item.mValue;
									
								}else if(item.mKey.equals("fucardbank")){
									
									picData.fucardbank  = item.mValue;
								}*/
							}
						}
					}
					
					mList.add(picData);
				}
			}
		}
	}

	
}
