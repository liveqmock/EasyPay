package com.inter.trade.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.BankData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.MyBankListData;
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
import com.inter.trade.ui.creditcard.task.AddBankCardTask;
import com.inter.trade.ui.creditcard.task.AddBankCardTask2;
import com.inter.trade.ui.creditcard.task.BankTask;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.AgentApplyFragmentNew;
import com.inter.trade.ui.fragment.gamerecharge.dialog.FavoriteCharacterDialogFragment;
import com.inter.trade.ui.fragment.gamerecharge.dialog.IFavoriteCharacterDialogListener;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyDenominationParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 我的银行卡，储蓄卡添加
 * @author Lihaifeng
 *
 */
@SuppressLint("ValidFragment")
public class MyBankAddBankCardFragment extends BaseFragment implements OnClickListener, IFavoriteCharacterDialogListener{
	
	private Button btn_mybk_save;
	private Button btn_mybk_delete;
	private EditText agent_id_edit;
	private EditText agent_key_edit;
	
	private EditText mybk_bank_name_edit;
	private EditText mybk_card_id_edit;
	private EditText mybk_person_name_edit;
	private EditText mybk_phone_edit;
	
	private CheckBox cb_default;
	private CheckBox cb_default_receive;
	
	private Button btnChoose, btnSubmit, btnBank, btnMonth, btnYear;
	/**
	 * 是否选中月份
	 */
	private boolean isMonth = false;
	/**
	 * 是否选中年份
	 */
	private Boolean isYear = false;
	/**
	 * 是否选中证件类型
	 */
	private Boolean isId = true;
	/**
	 * 是否选中银行
	 */
	private boolean isBank = false;
	
	/**
	 * 银行列表数据
	 */
	private ArrayList<BankData> bankList;
	
	/**
	 * 选中的银行的position
	 * @param in
	 * @return
	 */
	private int bankIndex;
	
	private Bundle bundle = new Bundle();
	private BuyTask mBuyTask;
	
	private CommonData mData = new CommonData();
	
	public CommonData mfeeData=new CommonData();
	
	public MyBankListData mBankCardData;
	
	private ArrayList<MyBankListData> mList = new ArrayList<MyBankListData>();
	
	public static MyBankAddBankCardFragment create(Bundle b){
		return new MyBankAddBankCardFragment(b);
	}
	
	public MyBankAddBankCardFragment()
	{
	}
	
	public MyBankAddBankCardFragment(Bundle b)
	{
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//LoginUtil.detection(getActivity());
		
//		mBankCardData=(MyBankListData) bundle.getSerializable("data");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		
//		View view = inflater.inflate(R.layout.mybank_creditcard_edit_layout, container,false);
		View view = inflater.inflate(R.layout.mybank_card_add_layout, container,false);
		initView(view);
		
		setTitle("添加银行卡");
		setMyBackVisible();
		
		new BankTask(getActivity(), new ResponseStateListener() {
			
			@Override
			public void onSuccess(Object obj, Class cla) {
				bankList=(ArrayList<BankData>) obj;
			}
		}).execute("");
		
		/*need
		// 获取话费充值面额选择,改为获取代理商协议及代理商申请填写项
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
		*/
		
		return view;
	}
	
	private void initView(View view){
		
//		mybk_bank_name_edit = (EditText)view.findViewById(R.id.mybk_bank_name_edit);
		mybk_card_id_edit = (EditText)view.findViewById(R.id.mybk_card_id_edit);
		mybk_person_name_edit = (EditText)view.findViewById(R.id.mybk_person_name_edit);
		mybk_phone_edit = (EditText)view.findViewById(R.id.mybk_phone_edit);
		
//		cb_default = (CheckBox) view.findViewById(R.id.cb_default);
		cb_default_receive = (CheckBox) view.findViewById(R.id.cb_default_receive);
		
		btn_mybk_save = (Button)view.findViewById(R.id.btn_mybk_save);
//		btn_mybk_delete = (Button)view.findViewById(R.id.btn_mybk_delete);
		btn_mybk_save.setOnClickListener(this);
//		btn_mybk_delete.setOnClickListener(this);
		
		btnBank = (Button) view.findViewById(R.id.btn_bank);
//		btnMonth = (Button) view.findViewById(R.id.btn_month);
//		btnYear = (Button) view.findViewById(R.id.btn_year);

		btnBank.setOnClickListener(this);
//		btnMonth.setOnClickListener(this);
//		btnYear.setOnClickListener(this);
	}
	
	long time=0L;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
//		case R.id.btn_idtype:// 选择证件类型
//		isId = true;
//		choosetype = 4;
//		FavoriteCharacterDialogFragment.show(this, getActivity(), "选择证件类型",
//				new String[] { "身份证" });// ,"护照","军官证","港澳居民往来内地通行证","台湾居民来往大陆通行证","其他"});
//		break;
		case R.id.btn_bank:// 选择银行
			isBank = true;
			choosetype = 3;
			if(bankList!=null){
				FavoriteCharacterDialogFragment.show(this, getActivity(), "选择所属银行",
						CreditcardInfoUtil.getBankNameList(bankList));
			}
//			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择所属银行",
//					getResources().getStringArray(R.array.banks));// ,"护照","军官证","港澳居民往来内地通行证","台湾居民来往大陆通行证","其他"});
			break;
		case R.id.btn_month:// 月份有效期
			isMonth = true;
			choosetype = 1;
			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择月份",
					getResources().getStringArray(R.array.months));
			break;
		case R.id.btn_year:// 年份有效期
			isYear = true;
			choosetype = 2;
			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择年份",
					getResources().getStringArray(R.array.years));
			break;
		case R.id.btn_mybk_save://保存
			//1秒内，禁止双击两次提交
			long currentTime=System.currentTimeMillis();
			if(currentTime-time<1000){
				return;
			}
			time=currentTime;
			
			myBankSave();
			break;
//		case R.id.btn_mybk_delete://解除绑定
//			myBankDelete();
//			break;
		default:
			break;
		}
	}
	
	private boolean checkInput() {
//		String bkname = mybk_bank_name_edit.getText().toString();
		String bkname = btnBank.getText().toString()+"";
//		String month = btnMonth.getText().toString()+"";
//		String year = btnYear.getText().toString()+"";
		
		String cardid = mybk_card_id_edit.getText().toString();
		String name = mybk_person_name_edit.getText().toString();
		String phone = mybk_phone_edit.getText().toString();
		
//		if (TextUtils.isEmpty(bkname)) {
//			PromptUtil.showToast(getActivity(), "请输入开户银行");
//			return false;
//		}
		if (!isBank || "".equals(bkname)) {
			PromptUtil.showToast(getActivity(), "请选择开户银行");
			return false;
//		} else if (!isMonth || month.equals(btnMonth.getHint()+"")) {
//			PromptUtil.showToast(getActivity(), "请选择月份!");
//			return false;
//		} else if (!isYear || year.equals(btnYear.getHint()+"")) {
//			PromptUtil.showToast(getActivity(), "请选择年份!");
//			return false;
		} 
		else if (TextUtils.isEmpty(cardid)) {
			PromptUtil.showToast(getActivity(), "请输入银行卡号");
			return false;
		} else if(!UserInfoCheck.checkBankCard(cardid)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		} else if (TextUtils.isEmpty(name)) {
			PromptUtil.showToast(getActivity(), "请输入开户人");
			return false;
		} else if(TextUtils.isEmpty(phone)
				|| phone.length() != 11 || !UserInfoCheck.checkMobilePhone(phone)){
				PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
				return false;
		}
		mBankCardData = new MyBankListData();
//		mBankCardData.bkcardbank = bkname;
		mBankCardData.bkcardbank = btnBank.getText().toString()+"";
		mBankCardData.bkcardno = cardid;
		mBankCardData.bkcardbankman = name;
		mBankCardData.bkcardbankphone = phone;
		mBankCardData.bkcardcardtype="bankcard";
		mBankCardData.bkcardisdefault="0";
		
		/**
		 * 默认收款卡
		 */
		if(cb_default_receive.isChecked()){
			mBankCardData.bkcardshoudefault="1";
		}else{
			mBankCardData.bkcardshoudefault="0";
		}
		
		if(bankList != null && bankIndex != -1){
			mBankCardData.bkcardbankid=bankList.get(bankIndex).bankid + "";
		}
		mBankCardData.bkcardyxmonth = "";
		mBankCardData.bkcardyxyear =  "";
		mBankCardData.bkcardcvv = "";
		mBankCardData.bkcardidcard = "";
		
		// TODO 验证各种信息
		return true;
	}
	
	/*
	 * 保存mBankCardData到服务器 task
	 */
	private void myBankSave () {
		if(checkInput()){
			//task
			new AddBankCardTask2(getActivity(), new ResponseStateListener(){
				
				@Override
				public void onSuccess(Object obj,Class cla){
					 FragmentManager manager = getActivity().getSupportFragmentManager();
					 int len = manager.getBackStackEntryCount();
					 if(len>0){
						 manager.popBackStack();
					 }else{
						 getActivity().finish();
					 }
				}
			}
			).execute(mBankCardData.bkcardbankid+""
			,mBankCardData.bkcardbank
			,mBankCardData.bkcardno
			,mBankCardData.bkcardbankman
			,mBankCardData.bkcardbankphone
			,mBankCardData.bkcardyxmonth
			,mBankCardData.bkcardyxyear
			,mBankCardData.bkcardcvv
			,mBankCardData.bkcardidcard
			,mBankCardData.bkcardcardtype
			,mBankCardData.bkcardisdefault
			,mBankCardData.bkcardisdefault,mBankCardData.bkcardshoudefault);
		}
	}
	
	//解除绑定mBankCardData, task
	private void myBankDelete()
	{
		if(checkInput()){
			//task
		}
	}
	
	private int choosetype = 1;// 1是月份 2是年份 3是银行 4 是证件类型
	@Override
	public void onListItemSelected(String value, int number) {
		// PromptUtil.showToast(this,
		// CreditcardInfoUtil.transferStringToId(value));
		switch (choosetype) {
		case 1:
			btnMonth.setText(value);
			break;
		case 2:
			btnYear.setText(value);
			break;
		case 3:
			btnBank.setText(value);
			bankIndex=number;
			break;
		case 4:
			btnChoose.setText(value);
			break;

		default:
			break;
		}
	}
	
	protected void setMyBackVisible() {
		if (getActivity() == null) {
			return;
		}
		back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				 FragmentManager manager = getActivity().getSupportFragmentManager();
				 int len = manager.getBackStackEntryCount();
				 if(len>0){
					 manager.popBackStack();
				 }else{
					 getActivity().finish();
				 }
			}
		});

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
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
		setTitle("添加银行卡");
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
					MyBankListData picData = new MyBankListData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
//								if(item.mKey.equals("rechaMoneyid")){
//									picData.rechaMoneyid  = item.mValue;
//									
//								}else if(item.mKey.equals("rechamoney")){
//									picData.rechamoney  = item.mValue;
//									
//								}else if(item.mKey.equals("rechapaymoney")){
//									
//									picData.rechapaymoney  = item.mValue;
//									
//								}else if(item.mKey.equals("rechamemo")){
//									
//									picData.rechamemo  = item.mValue;
//									
//								}else if(item.mKey.equals("rechaisdefault")){
//									
//									picData.rechaisdefault  = item.mValue;
//									
//								}
							}
						}
					}
					
					mList.add(picData);
				}
			}
		}
	}
	
}
