package com.inter.trade.ui.fragment.smsreceivepayment;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.Select;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.MyBankListData;
import com.inter.trade.data.TaskData;
import com.inter.trade.log.Logger;
//import com.inter.trade.ui.BuySwipCardRecordActivity;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.creditcard.MyBankCardActivity;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.task.GetBankListTask;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.creditcard.util.MyBankListAdapter;
import com.inter.trade.ui.fragment.AboutFragment;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.MyBankCardMainFragment;
import com.inter.trade.ui.fragment.buylicensekey.BuyLicenseKeyDetailFragment;
import com.inter.trade.ui.fragment.buylicensekey.BuyLicenseKeySuccessFragment;
import com.inter.trade.ui.fragment.buylicensekey.data.BuyLicenseKeyData;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyParser;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask.FeeListener;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanfeeParser;
import com.inter.trade.ui.fragment.smsreceivepayment.SmsDialogFragment.MyAppListener;
import com.inter.trade.ui.fragment.smsreceivepayment.SmsReceiveDialogFragment.SmsReceiveDialogListener;
import com.inter.trade.ui.fragment.smsreceivepayment.activity.AddReceiveBankCardActivity;
import com.inter.trade.ui.fragment.smsreceivepayment.activity.ReceiveBankCardActivity;
import com.inter.trade.ui.fragment.smsreceivepayment.activity.SelectReceiveBankCardActivity;
import com.inter.trade.ui.fragment.smsreceivepayment.activity.SmsRecordActivity;
import com.inter.trade.ui.fragment.smsreceivepayment.parser.SmsReceivePaymentSubmitParser;
import com.inter.trade.ui.fragment.smsreceivepayment.task.GetSmsDefaultTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
//import com.inter.trade.ui.fragment.hotel.adapter.HotelListAdapter;
//import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
//import com.inter.trade.ui.fragment.hotel.data.BuyLicenseKeyData;
//import com.inter.trade.ui.fragment.hotel.util.HotelListParser;
//import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.BankCardUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.TaskUtil;
import com.inter.trade.util.UserInfoCheck;
import com.inter.trade.util.PromptUtil.NegativeListener;
import com.inter.trade.util.PromptUtil.PositiveListener;
//import com.inter.trade.view.dialog.MySweetDialog;
//import com.inter.trade.view.dialog.MySweetDialog.MyAppListener;

/**
 * 短信收款 主Fragment
 * @author haifengli
 * @param <MyDialogActivity>
 *
 */
public class SmsReceivePaymentMainFragment<MyDialogActivity> extends BaseFragment implements OnClickListener, ResponseStateListener, FeeListener{
	
	private static final String TAG = SmsReceivePaymentMainFragment.class.getName();
	/**
	 * 手机号码
	 */
	public static String mPhone="";
	
	/**
	 * 收款金额
	 */
	public static String mMoney="";
	
	/**
	 * 收款金额
	 */
	public static Double mDoubleMoney=0.00;
	/**
	 * 手续费率
	 */
	public static Double mFeeRate=0.58/100;
	
	/**
	 * 手续费
	 */
	public static String mFee="";
	
	/**
	 * 留言
	 */
	public static String mMessage="";
	
	/**
	 * 短信收款 发起请求总数据
	 */
	private CommonData requsetData;
	
	/**
	 * 是否设置默认收款账户
	 */
	public static boolean isSetReceiveBankCard=false;
	
	/**
	 * 是否保存过我的银行卡
	 */
	public static boolean isSetMyBankCard=false;
	
	/**
	 * 是否同意短信收款业务服务协议
	 */
	public static boolean mIsChecked=false;
	
	/**
	 * 读取默认卡信息
	 */
	public static DefaultBankCardData creditCard;
	
	/**
	 * 获取默认卡异步任务
	 */
	private GetSmsDefaultTask getDefaultTask;
	
	/**
	 * 是否从[购买授权码支付成功]页面，点击[我知道了]，返回[购买授权码]首页?
	 * 若true, 则不执行readLicenseKeyTask();
	 * 若false, 则执行readLicenseKeyTask();
	 * 初始化为false, 执行readLicenseKeyTask();
	 */
	public static boolean isComeBackFromPaySuccess=false;
	
	
	private Button btn_addressbook;
	private Button btn_sms;
	private ImageView bankCard_default;
	private EditText et_phone;
	private EditText et_money;
	private EditText et_message;
	private EditText et_bankcard_id;
	private TextView tv_fee;
	private CheckBox cb_default;
	private TextView tv_sms_pro;
	
	private ImageView img_bank_logo;
	private TextView bank_name    ;
	private TextView person_name  ;
	private TextView bankCard_id4 ;
	private TextView bankCard_type;
	
	private Bundle data = null;
	private AsyncLoadWork<String> asyncHotelServiceTask = null;
	private SmsReceiveDialogFragment smsDialog;
//	private SmsToastDialogFragment smsToastdf;
	private SmsDialogFragment smsToastdf;
	private Handler mHandler;
	public static final int CLOSE_DIALOG_DEFAULT = 0;
	public static final int CLOSE_DIALOG_TOAST = 1;
	
	/**
	 * 默认没有获取手续费
	 */
	private boolean isSetFee = false;
	/**
	 * 手续费计算失败，返回的错误提示信息
	 */
	private String feeErrorInfo;
	/**
	 * 计算手续费
	 */
	private CreditCardfeeTask mCardfeeTask;
	public CommonData mfeeData=new CommonData();
	
	public SmsReceivePaymentMainFragment()
	{
	}
	
	public static SmsReceivePaymentMainFragment newInstance (Bundle data) {
		SmsReceivePaymentMainFragment fragment = new SmsReceivePaymentMainFragment();
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
		}
		
		/**
		 * 需要考虑异常重建后恢复保存savedInstanceState的处理
		 */
		firstLoad ();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		mHandler = new Handler(this);
//		getActivity().setTheme(R.style.DefaultLightTheme);
		View view = inflater.inflate(R.layout.sms_receive_payment_main_layout, container,false);
		initView(view);
		
		setTitle("短信收款");
		setBackVisibleForFragment();
		
		setRightVisible(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * 收款历史
				 */
//				BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
//						SmsReceivePaymentRecordFragment.instance(null), 1);
				
				Intent intent = new Intent();
				intent.setClass(getActivity(), SmsRecordActivity.class);
				startActivityForResult(intent, 1);
			}
		}, "收款历史");
		
		return view;
	}
	
	private void firstLoad () {
		/**
		 * 需要考虑异常重建后恢复保存savedInstanceState的处理
		 */
//		mPhone="";
		creditCard=null;
		mIsChecked=false;
		isSetReceiveBankCard=false;
		isSetMyBankCard=false;
		
		/**
		 * 获取是否设置有默认收款账户
		 */
//		Logger.d(TAG, "获取默认收款账号Task");
		getDefaultTask = new GetSmsDefaultTask(getActivity(), this);
		getDefaultTask.execute("", "1");
		
		
		
//		if(!isComeBackFromPaySuccess){
//			Logger.d(TAG, "!isComeBackFromPaySuccess");
//			getDefaultTask = new GetSmsDefaultTask(getActivity(), this);
//			getDefaultTask.execute("", "1");
//		}else{
//			Logger.d(TAG, "isComeBackFromPaySuccess");
//			isComeBackFromPaySuccess=false;
//		}
	}
	
	private void initView (View view) {
		btn_addressbook    = (Button)view.findViewById(R.id.btn_addressbook);
		btn_sms    = (Button)view.findViewById(R.id.btn_sms);
		bankCard_default    = (ImageView)view.findViewById(R.id.bankCard_default);
		et_phone    = (EditText)view.findViewById(R.id.et_phone);
		et_money    = (EditText)view.findViewById(R.id.et_money);
		tv_fee    = (TextView)view.findViewById(R.id.tv_fee);
		et_message    = (EditText)view.findViewById(R.id.et_message);
		cb_default    = (CheckBox)view.findViewById(R.id.cb_default);
		tv_sms_pro    = (TextView)view.findViewById(R.id.tv_sms_pro);
		
		bank_name    = (TextView)view.findViewById(R.id.bank_name);
		person_name    = (TextView)view.findViewById(R.id.person_name);
		bankCard_id4    = (TextView)view.findViewById(R.id.bankCard_id4);
		bankCard_type    = (TextView)view.findViewById(R.id.bankCard_type);
		img_bank_logo    = (ImageView)view.findViewById(R.id.img_bank_logo);
		
		btn_addressbook.setOnClickListener(this);
		btn_sms.setOnClickListener(this);
		bankCard_default.setOnClickListener(this);
		tv_sms_pro.setOnClickListener(this);
		
		setFeeOnMoneyEdit(et_money);
		setCheckBox(cb_default);
		
		/**
		 * 以下测试
		 */
//		SpannableString ss = new SpannableString( "红色打电话粗体删除线绿色下划线图片:.");
//        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new URLSpan("tel:4155551212"), 2, 5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new StyleSpan(Typeface.BOLD), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new StrikethroughSpan(), 7, 10,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new UnderlineSpan(), 10, 16,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        Drawable d = getResources().getDrawable(R.drawable.add_pic); 
//        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight()); 
//        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE); 
//        ss.setSpan(span, 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
//
////        TextView t4 = (TextView) findViewById(R.id.text4);
//        et_message.setText(ss);
//        
//        et_message.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		/**
		 * 本地通讯录读取手机号
		 */
		case R.id.btn_addressbook:
			addressBook();
			break;
			
		/**
		 * 点击卡图标，设置默认收款账号，直接跳至选择已有银行卡页面
		 */	
		case R.id.bankCard_default:
			selectReceiveBankCard();
			break;
			
		/**
		 * 短信收款协议
		 */
		case R.id.tv_sms_pro:
			showSmsProtocol();
			break;
			
		/**
		 * 发起收款
		 */
		case R.id.btn_sms:
			if(checkDoubleClick() && checkInput()){
				SmsReceivePaymentSubmitTask();
			}
			break;
		default:
			break;
		}
		
	}
	
	private long time=0L;
	/**
	 * 1秒内，禁止双击两次提交
	 */
	private boolean checkDoubleClick () {
		long currentTime = System.currentTimeMillis();
		if (currentTime - time < 1000 ) {
			return false;
		}
		time = System.currentTimeMillis();
		return true;
	}
	
	private void SmsReceivePaymentSubmitTask () {
		SmsReceivePaymentSubmitParser netParser = new SmsReceivePaymentSubmitParser();
		requsetData = new CommonData();
		requsetData.putValue("fumobile", mPhone);
		requsetData.putValue("money", mMoney);
		requsetData.putValue("payfee", mFee);
		requsetData.putValue("memo", mMessage);
		requsetData.putValue("shoucardno", creditCard.getBkcardno());
		requsetData.putValue("shoucardman", creditCard.getBkcardbankman());
		requsetData.putValue("shoucardmobile", creditCard.getBkcardbankphone());
		requsetData.putValue("shoucardbank", creditCard.getBkcardbank());
		
		asyncHotelServiceTask = new AsyncLoadWork<String>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {
			
			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				Bundle b = new Bundle();
				b.putSerializable("requsetData", requsetData);
				BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
						SmsSuccessFragment.newInstance(b), 1);
			}
			
			@Override
			public void onFailure(String error) {
//				showDialogMessage(LoginUtil.mLoginStatus.message+"",5000);
			}
			
		}, false, false);
		
		asyncHotelServiceTask.execute("ApiSMSReceiptInfo ", "addSMSReceipt");
		
	}
	
	private void showSmsProtocol(){
		/**
		 * 默认支付是6，此处默认收款 沿用2 《通付宝钱包服务协议》
		 */
		AboutFragment.mProtocolType="2";
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.PROTOCOL_LIST_INDEX);
		getActivity().startActivity(intent);
	}
	
	/**
	 * 调度手机通讯录
	 */
	private void addressBook() {
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, 2);
	}
	
	/**
	 * 检查默认收款账户状态，未设置默认收款账号，则弹框提示用户操作，新增或选择已有银行卡作为默认收款账户
	 */
	private void checkBankCard() {
		showDialogSetReceiveBankCard("","选择已有账户","新增收款账户",isSetMyBankCard,true);
	}
	
	/**
	 *  检查输入各项
	 */
	private boolean checkInput() {
		String phone = et_phone.getText()+"";
		phone=phone.trim();
		if(TextUtils.isEmpty(phone) || phone.length() != 11 || !UserInfoCheck.checkMobilePhone(phone)){
//			PromptUtil.showToast(getActivity(), "请输入有效的手机号码");
			
//			if(smsToastdf != null){
//				smsToastdf.dismiss();
//				smsToastdf = null;
//			}
//			smsToastdf = SmsToastDialogFragment.newInstance("请输入有效的手机号码");
//			smsToastdf.show(getActivity());
//			smsToastdf.setCancelable(true);
			
			
//			if(smsToastdf != null){
//				smsToastdf.dismiss();
//				smsToastdf = null;
//			}
			showDialogMessage("请输入有效的手机号码", 3000);
//			smsToastdf=SmsDialogFragment.newInstance(null, "请输入有效的手机号码", null, null);
//			smsToastdf.show(getFragmentManager(), "dialog");
//			timerForDialog(CLOSE_DIALOG_TOAST);
			
			return false;
		}
		
		String money = et_money.getText()+"";
		money=money.trim();
		if(TextUtils.isEmpty(money) ){
			showDialogMessage("收款金额填写有误，请确认。",3000);
			return false;
		}
//		if(!checkMoney(money)){
//			showDialogMessage("收款金额超限，请重新输入或隔日再尝试。目前，短信收款业务单笔交易不超过人民币5,000.00元，日累计不超过人民币20,000.00元。", 5000);
//			return false;
//		}
		
		/**
		 * 本地检查手续费
		 */
//		String fee = tv_fee.getText()+"";
//		fee=fee.trim();
//		if(TextUtils.isEmpty(fee) || !checkFee(fee)){
//			PromptUtil.showToast(getActivity(), "手续费异常");
//			return false;
//		}
		
		/**
		 * 网络检查手续费
		 */
		checkFeeByNet();
		
		String message = et_message.getText()+"";
		message=message.trim();
//		if(TextUtils.isEmpty(message)){
//			showDialogMessage("请填写留言", 3000);
//			return false;
//		}
		if(!checkMessage(message)){
			showDialogMessage("留言信息超长，请修改。", 3000);
//			PromptUtil.showToast(getActivity(), "留言信息超长，请修改。");
			return false;
		}
		
		if(!isSetReceiveBankCard){
			checkBankCard();
			return false;
		}
		
		if(!mIsChecked){
			showDialogMessage("请阅读并同意《通付宝短信收款服务协议》", 3000);
//			PromptUtil.showToast(getActivity(), "请阅读并同意《通付宝短信收款服务协议》");
			return false;
		}
		
		mPhone=phone;
		mMoney=NumberFormatUtil.format2(money);
//		mMoney=money;
//		mFee=fee;
		mMessage=message;
		return true;
	}
	
	/**
	 * 用DialogFragment 显示提示语，并定时关闭
	 */
	private void showDialogMessage(String message, long time){
		closeDialog();
		smsToastdf=SmsDialogFragment.newInstance(null, message, null, null);
		smsToastdf.show(getFragmentManager(), "dialog");
		timerForDialog(CLOSE_DIALOG_TOAST, time);
	}
	
	/**
	 * 新式对话框
	 * @param message
	 * @param positive
	 * @param negative
	 * @param positiveEnable
	 * @param negativeEnable
	 */
	private void showDefaultCardDialog(String message, String positive, String negative, boolean positiveEnable,boolean negativeEnable){
		message="　　为了您收款资金及时到账，请您首先设置收款账户信息。该账户将作为您的默认收款账户，如果需要，您可以通过“我的银行卡”菜单进行账户的新增、变更或删除。";
		
		//此处直接new一个Dialog对象出来，在实例化的时候传入主题
        Dialog dialog = new Dialog(getActivity(), R.style.MyDialogStyleBottom);
        //设置它的ContentView
        dialog.setContentView(R.layout.sms_default_card_dialog);
        dialog.findViewById(R.id.select_card).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectReceiveBankCard();
			}
		});
        dialog.findViewById(R.id.add_card).setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		addReceiveBankCard();
        	}
        });
        
        dialog.findViewById(R.id.select_card).setEnabled(positiveEnable);
        dialog.findViewById(R.id.add_card).setEnabled(negativeEnable);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
	}
	
	/**
	 * 传统对话框
	 * @param message
	 * @param positive
	 * @param negative
	 * @param positiveEnable
	 * @param negativeEnable
	 */
	private void showDialogSetReceiveBankCard2(String message, String positive, String negative, boolean positiveEnable,boolean negativeEnable){
		message="　　为了您收款资金及时到账，请您首先设置收款账户信息。该账户将作为您的默认收款账户，如果需要，您可以通过“我的银行卡”菜单进行账户的新增、变更或删除。";
		PromptUtil.showSweetDialog( null, message, positive, negative, positiveEnable, negativeEnable,
				new PositiveListener() {
			
			@Override
			public void onPositive() {//选择已有账户
				selectReceiveBankCard();
			}
		}, new NegativeListener() {
			
			@Override
			public void onNegative() {//新增收款账户
				addReceiveBankCard();
			}
		}, getActivity(),true);
	}
	
	/**
	 * 正式用的
	 * @param message
	 * @param positive
	 * @param negative
	 * @param positiveEnable
	 * @param negativeEnable
	 */
	private void showDialogSetReceiveBankCard(String message, String positive, String negative, boolean negativeEnable, boolean positiveEnable){
		if(smsDialog != null){
			smsDialog.dismiss();
			smsDialog = null;
		}
		smsDialog = SmsReceiveDialogFragment.newInstance(negativeEnable, positiveEnable);
		smsDialog.show(getFragmentManager(), "dialog");
		smsDialog.setListener(new SmsReceiveDialogListener() {
			
			@Override
			public void onNegative(SmsReceiveDialogFragment dialog) {
				// TODO Auto-generated method stub
				selectReceiveBankCard();
			}
			
			@Override
			public void onPositive(SmsReceiveDialogFragment dialog) {
				// TODO Auto-generated method stub
				addReceiveBankCard();
			}
			
		});
		
//		SmsDialogFragment smsdf = SmsDialogFragment.newInstance(null, message, negative, positive);                                                                                                                                                                    
//		smsdf.setListener(new MyAppListener() {
//			@Override
//			public void onNegative() {
//				// TODO Auto-generated method stub
//				selectReceiveBankCard(0);
//			}
//			
//			@Override
//			public void onPositive() {
//				// TODO Auto-generated method stub
//				addReceiveBankCard(1);
//			}
//		});
//		smsdf.show(getFragmentManager(), "dialog");
		
//		MySweetDialog dialog=new MySweetDialog();
//		dialog.show(getActivity(), new MyAppListener() {
//			
//			@Override
//			public void onNegative() {
//				// TODO Auto-generated method stub
//				selectReceiveBankCard(0);
//			}
//			
//			@Override
//			public void onPositive() {
//				// TODO Auto-generated method stub
//				addReceiveBankCard(1);
//			}
//		});
		
//		timerForDialog();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what == CLOSE_DIALOG_TOAST){
			closeDialog();
		}
		return false;
	}
	
	public boolean closeDialog() {
		// TODO Auto-generated method stub
		if(smsToastdf != null){
			smsToastdf.dismiss();
			smsToastdf = null;
		}
		return false;
	}
	
	public boolean timerForDialog(final int whichDialog, long time) {
		// TODO Auto-generated method stub
//		mHandler = new Handler(this);
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mHandler.obtainMessage(whichDialog);
				mHandler.sendMessage(msg);
			}
		}, time);
		return false;
	}

	/**
	 *  选择已有账户作为默认收款账户
	 */
	private void selectReceiveBankCard() {
//		ReceiveBankCardActivity.startMyActivity(getActivity(), pageId+"", "");
		Intent intent = new Intent(getActivity(), SelectReceiveBankCardActivity.class);
		startActivityForResult(intent, 3);
	}
	
	/**
	 *  新增账户作为默认收款账户
	 */
	private void addReceiveBankCard() {
//		ReceiveBankCardActivity.startMyActivity(getActivity(), pageId+"", "");
		Intent intent = new Intent(getActivity(), AddReceiveBankCardActivity.class);
//		intent.putExtra("param1", pageId+"");
		startActivityForResult(intent, 4);
	}
	
	/**
	 *  检查收款金额
	 */
	private boolean checkMoney(String money) {
		if(TextUtils.isEmpty(money)){
			return false;
		}
		Double dMoney=0.00;
		try{
			dMoney=Double.parseDouble(money);
		}catch(NumberFormatException e){
			throw new NumberFormatException("收款金额转换异常");
		}
		if(dMoney>=0.01 && dMoney<=5000.00 ){
			mMoney=money;
			mDoubleMoney=dMoney;
			return true;
		}
		return false;
	}
	
	/**
	 *  本地检查手续费
	 */
	private boolean checkFee(String fee) {
		Double dFee=0.00;
		try{
			dFee=Double.parseDouble(fee);
		}catch(NumberFormatException e){
			throw new NumberFormatException("手续费转换异常");
		}
		if(dFee>0.00){
			mFee=fee;
			return true;
		}
		return false;
	}
	
	/**
	 *  后台检查手续费
	 */
	private boolean checkFeeByNet() {
		if(!isSetFee) {
			if(feeErrorInfo != null){
//				PromptUtil.showToast(getActivity(), feeErrorInfo);
				showDialogMessage(feeErrorInfo,5000);
			} else {
				if(tv_fee.getText().toString().equals("正在计算...")) {
//					PromptUtil.showToast(getActivity(), "请稍后，正在计算手续费...");
					showDialogMessage("请稍后，正在计算手续费...",3000);
				} else if(tv_fee.getText().toString().equals("计算失败")) {
//					PromptUtil.showToast(getActivity(), "手续费计算失败，请检查网络");
					showDialogMessage("手续费计算失败，请检查网络",3000);
				} else {
//					PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
					showDialogMessage(getActivity().getString(R.string.net_error),3000);
				}
					
			}
			return false;
		}
		return true;
	}
	
	/**
	 *  检查留言
	 */
	private boolean checkMessage(String message) {
		if(message.length()<=20){
			return true;
		}
		return false;
	}
	
	/**
	 *  计算手续费
	 */
	private void setFeeOnMoneyEdit(EditText et_money) {
		if(et_money==null){
			return;
		}
		et_money.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String money = s.toString();
				if(TextUtils.isEmpty(money)){
					mFee = "";
					tv_fee.setText(mFee);
					return;
				}
				money=money.trim();
				/**
				 * 屏蔽处理首位输入小数点"."
				 */
                int d = money.indexOf(".");
                
                /**
				 * 后台计算手续费
				 */
                if(d!=0){
					mMoney=money;
					cleanText();//计算手续费前清除上一次手续费的记录
					mfeeData.putValue("money", money);
					getCost();
                }
                
				/**
				 * 本地计算手续费
				 */
//				if(d!=0 && checkMoney(money)){
//					double dmoney = mDoubleMoney*mFeeRate;
//					String fee = String.format("%.2f", dmoney);
//					if(!TextUtils.isEmpty(fee) && checkFee(fee)){
//						tv_fee.setText(mFee);
//					}
//				}else{
//					mFee = "";
//					tv_fee.setText(mFee);
//				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * 限制输入到小数点后2位
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String temp = s.toString();
                int d = temp.indexOf(".");
                if (d < 0) return;
                if (temp.length() - d - 1 > 2){
                    s.delete(d + 3, d + 4);
                }else if (d==0) {
                    s.delete(d, d+1);
                }
			}
		});
		
	}
	
	/**
	 * 获取手续费
	 */
	private void getCost() {
		onCardfeeTaskDestroy ();
//		isSetFee = false;
		mCardfeeTask = new CreditCardfeeTask(getActivity(), initTaskData());
		mCardfeeTask.setFeeListener(this);
		mCardfeeTask.execute("");
	}
	
	/**
	 * 
	 * 销毁CardfeeTask
	 */
	private void onCardfeeTaskDestroy () {
		if (mCardfeeTask != null) {
			mCardfeeTask.cancel(true);
		}
	}
	
	private TaskData initTaskData(){
		TaskData taskData = new TaskData();
		taskData.mCommonData = mfeeData;
		taskData.apiName="ApiSMSReceiptInfo ";
		taskData.funcName="SMSReceiptfee";
//		taskData.apiName="ApiPayinfo";
//		taskData.funcName="getSupTransferPayfee";
		
		taskData.mNetParser=new DaikuanfeeParser();
		return taskData;
	}
	
	/**
	 * 填充手续费，支付总额
	 * @param fee
	 */
	public void setFee(String fee){

		tv_fee.setText(NumberFormatUtil.format2(fee));
		
//		float total = Float.parseFloat(fee) + Float.parseFloat(et_money.getText().toString());
//		pay_total_money.setText(NumberFormatUtil.format2(total+"")+"元");//总金额
		
		mFee = fee;
	}
	
	/**
	 * 清除手续费、支付金额TextView的值
	 */
	public void cleanText() {
		onCardfeeTaskDestroy ();
		isSetFee = false;
		if(feeErrorInfo != null) {
			feeErrorInfo = null;//当计算出手续费把当前受限信息设为null,以免在网络不好的情况下，重新计算手续费时提示该信息
		}
		tv_fee.setText("正在计算...");
//		pay_total_money.setText("");
	}
	
	//手续费回调
	@Override
	public void result(int state, String fee, ArrayList<ArriveData> datas) {
		// TODO Auto-generated method stub
		if(state == 0) {//成功获取手续费
			setFee(fee);
			isSetFee = true;
			
			if(datas != null && datas.size() > 0) {
				ArriveData arriveData = datas.get(0);
//				arriveid = arriveData.arriveid;
			}
			
		} else {
//			isSetFee = false;
			cleanText();
			feeErrorInfo = fee;
			tv_fee.setText("计算失败");
		}
	}
	
	/**
	 *  同意短信收款协议，打勾
	 */
	private void setCheckBox(CheckBox cb) {
		if(cb == null){
			return;
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
					mIsChecked=isChecked;
			}
		});
		cb.setChecked(true);
	}
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		if(obj!=null){
			Logger.d(TAG, "获取默认收款账号success");
			creditCard = (DefaultBankCardData) obj;
			if(creditCard != null && creditCard.getBkcardno()!=null){
				isSetReceiveBankCard=true;
				setDefaultBank(creditCard);
				Logger.d(TAG, "isSetReceiveBankCard");
			}
		}
		
		/**
		 * 获取是否保存过银行卡
		 */
		new GetBankListTask(getActivity(), new ResponseStateListener() {
			
			@Override
			public void onSuccess(Object obj, Class cla) {
				// TODO Auto-generated method stub
				if(obj!=null){
					ArrayList<DefaultBankCardData> mDatas=(ArrayList<DefaultBankCardData>) obj;
					if(mDatas != null && mDatas.size()>0){
						isSetMyBankCard=true;
					}
				}
				
				/**
				 * 未设置默认收款账号，则弹框提示用户操作
				 */
				if(!isSetReceiveBankCard){
					Logger.d(TAG, "!isSetReceiveBankCard");
					checkBankCard();
				}
			}
		}).execute("");
			
	}
	
	/**
	 * 显示默认卡信息
	 * @param data
	 */
	public void setDefaultBank(DefaultBankCardData data){
		if(data==null){
			return;
		}
		bank_name.setText(data.getBkcardbank());
		bankCard_id4.setText(data.getBkcardno()==null?"":"尾号"+data.getBkcardno().substring(data.getBkcardno().length()-4, data.getBkcardno().length()));
		person_name.setText(data.getBkcardbankman());
		bankCard_type.setText(BankCardUtil.getCardType(data.getBkcardcardtype()));
//		img_bank_logo.setBackgroundDrawable(getActivity().getResources().getDrawable(CreditcardInfoUtil.getDrawableOfSmallBank(data.getBkcardbanklogo())));
		
		/**
		 * 先从本地拿银行LOGO，如没有，则去网络拿
		 */
		int drawableId=CreditcardInfoUtil.getDrawableOfSmallBank(data.getBkcardbanklogo());
		if(drawableId != R.drawable.bank_samll){
			img_bank_logo.setBackgroundDrawable(getActivity().getResources().getDrawable(drawableId));
		}else if(TextUtils.isEmpty(data.getBanklogo())){
			img_bank_logo.setBackgroundDrawable(getActivity().getResources().getDrawable(drawableId));	
		}else{
			FinalBitmap.create(getActivity()).display(img_bank_logo, data.getBanklogo());
		}
	}
	
	/**
	 * 检查，获取从通讯录点选的电话号码
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void checkPhoneFromAddressBook(int requestCode, int resultCode, Intent data) {
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
						 if(UserInfoCheck.checkMobilePhone(tempNumber)){
							 et_phone.setText(tempNumber);
//							 mobileQueryAttribution(tempNumber);
						 }else{
							 PromptUtil.showToast(getActivity(), "号码格式不正确");
						 }
					 }
				 }else{
					 if(UserInfoCheck.checkMobilePhone(tempPhoneNumber)){
						 et_phone.setText(tempPhoneNumber);
//						 mobileQueryAttribution(tempPhoneNumber);
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
				 }
			 }else{
				 if(tempNum != null && !tempNum.equals("") && tempNum.length() == 11 && UserInfoCheck.checkMobilePhone(tempNum)){
					 if(UserInfoCheck.checkMobilePhone(tempNum)){
						 et_phone.setText(tempNum);
//						 mobileQueryAttribution(tempNum);
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
					 
				 }else{
					 if(tempNum == null || tempNum.equals("")){
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
						 return;
					 }
					 if(tempNum.length() > 11){
						 removeHeadNumberForPhone(tempNum);
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
				 }
				
			 }
//			 et_phone.setText(phoneNumber);
//			 mobileQueryAttribution(phoneNumber);
			 break; 
		 
		}
	}
	
	/**
	 * 去除手机号前面的优惠号码如12593，+86, 等
	 * @param tempNum
	 */
	public void removeHeadNumberForPhone(String tempNum){
		int len = tempNum.length();
		 if(len>11){
			 tempNum=tempNum.substring(len-11);
//			 PromptUtil.showToast(getActivity(), "手机号>11："+tempNum);
			 if(UserInfoCheck.checkMobilePhone(tempNum)){
				 et_phone.setText(tempNum);
			 }else{
				 PromptUtil.showToast(getActivity(), "号码格式不正确");
			 }
		 }
	}
	
	/**
	 * 设置标题栏右上角，收款历史按钮显示、隐藏
	 * @param vis
	 */
	public void setRightButtonVisibility(int vis){
		if(getActivity()==null){
			return;
		}
		Button right = (Button) getActivity().findViewById(R.id.title_right_btn);
		if(right==null){
			return;
		}
		right.setVisibility(vis);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		PromptUtil.showToast(getActivity(), "sms:onActivityResult--> requestCode:"+requestCode+"\nresultCode:"+resultCode+"\nIntent:"+data);
//		Logger.d("sms:onActivityResult-->\n", "requestCode:"+requestCode+"\nresultCode:"+resultCode+"\nIntent:"+data);
		if (data == null) {
			return;
		}
		
		if(requestCode==1){//读收款历史,返回手机号码
			String phone = (String) data.getStringExtra("phone");
			if(!TextUtils.isEmpty(phone) && UserInfoCheck.checkMobilePhone(phone)){
				 et_phone.setText(phone);
			}
		}
		else if(requestCode==2){//读通讯录
			checkPhoneFromAddressBook(requestCode, resultCode, data); 
		}else if(requestCode==3){//选择默认收款账号
			/**
			 * 以下注释部分在 选中某银行卡页面已做设置默认收款卡接口调用，故重新获取默认收款账号GetSmsDefaultTask，刷新界面即可
			 */
//			Object object = data.getSerializableExtra("bankcard");
//			if(object!=null && object.getClass()!=null){
//				/**
//				 * 直接选择已有卡号作默认收款账号，并返回
//				 */
//				if(object.getClass().equals(DefaultBankCardData.class)){
//					creditCard = (DefaultBankCardData) object;
//					if(creditCard != null && creditCard.getBkcardno() != null){
//						isSetReceiveBankCard=true;
//						setDefaultBank(creditCard);
//					}
//					return;
//				}
//			}
			
			/**
			 * 从选择默认收款账号页面，点击右上角添加按钮，保存后，返回。处理同requestCode==4【新增默认收款账号】
			 */
//			Logger.d(TAG, "获取默认收款账号Task");
			getDefaultTask = new GetSmsDefaultTask(getActivity(), this);
			getDefaultTask.execute("", "1");
			
//			creditCard = (DefaultBankCardData) data.getSerializableExtra("bankcard");
//			if(creditCard != null && creditCard.getBkcardno() != null){
//				isSetReceiveBankCard=true;
//				setDefaultBank(creditCard);
//			}
		}else if(requestCode==4){//新增默认收款账号
			/**
			 * 数据没统一前，只好这样处理MyBankListData-->DefaultBankCardData
			 */
//			MyBankListData mybanklistdata = (MyBankListData) data.getSerializableExtra("bankcard");
//			if(mybanklistdata != null && mybanklistdata.bkcardno != null){
//				creditCard = new DefaultBankCardData();
//				creditCard.setBkcardbank(mybanklistdata.bkcardbank);
//				creditCard.setBkcardbankman(mybanklistdata.bkcardbankman);
//				creditCard.setBkcardno(mybanklistdata.bkcardno);
//				creditCard.setBkcardcardtype(mybanklistdata.bkcardcardtype);
//				creditCard.setBkcardid(mybanklistdata.bkcardid);
//				creditCard.setBkcardbankphone(mybanklistdata.bkcardbankphone);
//				
//				isSetReceiveBankCard=true;
//				setDefaultBank(creditCard);
//			}
			
			/**
			 * 获取是否设置有默认收款账户
			 */
//			Logger.d(TAG, "获取默认收款账号Task");
			getDefaultTask = new GetSmsDefaultTask(getActivity(), this);
			getDefaultTask.execute("", "1");
		}
		
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		setRightButtonVisibility(View.VISIBLE);
		if(creditCard != null && creditCard.getBkcardno()!=null){
//			isSetReceiveBankCard=true;
			setDefaultBank(creditCard);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		setRightButtonVisibility(View.GONE);
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
