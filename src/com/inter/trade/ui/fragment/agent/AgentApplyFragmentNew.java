package com.inter.trade.ui.fragment.agent;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SunType;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.imageframe.ImageCache;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.util.AgentApplyInfoData;
import com.inter.trade.ui.fragment.agent.util.AgentApplyInfoTask;
import com.inter.trade.ui.fragment.agent.util.AgentApplySumitTask;
import com.inter.trade.ui.fragment.agent.util.AgentAreaParser;
import com.inter.trade.ui.fragment.agent.util.AgentData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

@SuppressLint("ValidFragment")
public class AgentApplyFragmentNew extends BaseFragment implements OnClickListener, ResponseStateListener{
	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	private Uri photoUri;
	private String picPath;
	private Cursor mCursor;
	private int pos = 0;
	private String[] path = new String[4];
	private boolean isPicChange = false;
	private ImageView bigpicture;
	private Bitmap identityBitmap1;
	private Bitmap identityBitmap2;
	private Bitmap identityBitmap3;
	private Bitmap identityBitmap4;
	
	private Button agent_appling_agree_btn;
	private Button agent_appling_refuse_btn;
	private Button agent_applying_photo_btn;
	private Button agent_applying_submit_btn;
	private Button agent_applying_reset_btn;
	private ImageView agent_applying_photo_done_img;
	private ImageView agent_applying_photo_img;
	private ImageView agent_applying_license_photo_img, agent_applying_license_photo_done_img;
	private ImageView agent_applying_organization_photo_img, agent_applying_organization_photo_done_img;
	private ImageView agent_applying_tax_photo_img, agent_applying_tax_photo_done_img;
	private ImageView agent_applying_id_card_photo_img, agent_applying_id_card_photo_done_img;
	private EditText agent_applying_name_edit;
	private EditText agent_applying_phone_edit;
	private EditText agent_applying_code_edit;
	private EditText agent_applying_adress_edit;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
//	private LinearLayout agent_applying_layout;
	private RelativeLayout activity_title_layout;
	private ScrollView agent_applying_layout;
	private RelativeLayout agent_applying_agreement_layout;
	private LinearLayout agent_applying_info_more_layout, agent_applying_more_layout;
	private TextView agent_applying_adress_combine, agent_applying_more_tv;
	private LinearLayout agent_applying_license, agent_applying_organization, agent_applying_tax, agent_applying_id_card;
	private TextView agent_applying_license_tv, agent_applying_organization_tv, agent_applying_tax_tv, agent_applying_id_card_tv;
	private Spinner agent_applying_spinner_prov, agent_applying_spinner_city, agent_applying_spinner_town, agent_applying_spinner_agentType;
	private List<String> mList;
	private List<AgentApplyInfoData> mInfoList = new ArrayList<AgentApplyInfoData>();
	private List<String> spinnerProvList = new ArrayList<String>();
	private List<String> spinnerCityList = new ArrayList<String>();
	private List<String> spinnerTownList = new ArrayList<String>();
	private List<String> spinnerTypeList = new ArrayList<String>();
	private ArrayAdapter<String> adapterProv;  
	private ArrayAdapter<String> adapterCity;  
	private ArrayAdapter<String> adapterTown;  
	private ArrayAdapter<String> adapterType;  
	
	/*
	 * 第一次进入页面时获取的省市区数据，保存以备“重置”时恢复，不需再次请求网络。
	 */
	private List<String> spinnerProvListFirst = new ArrayList<String>();
	private List<String> spinnerCityListFirst = new ArrayList<String>();
	private List<String> spinnerTownListFirst = new ArrayList<String>();
	
	//是否第一次进入页面时获取省市区数据,true:是，第一次获取；false:否，已获取过
	private boolean isFirstIn=true;
	
	//是否重置
	private boolean isReset=false;
	
	//以哪种名义代理，默认0，以公司名义代理
	private int agentType=0;
	
	//true 省份变动，市和区都要重新请求数据，刷新
	private boolean mIsProvChanged=true;
	
	//true 市变动，区要重新请求数据，刷新
	private boolean mIsCityChanged=true;
	
	//true dialog 已打开; false,dialog 未打开
	private boolean mIsDialogOpen=false;
	
	//true dialog 要关闭; false,dialog 不需关闭
	private boolean mDialogClose=false;
	
	//true spinner自动选择; false,spinner监听用户选择
	private boolean mAutoUpdate=false;
	
	/*
	 * 超时重新连接，根据状态mDataState，请求相应数据：
	 * 0没任何数据，
	 * 1已有代理名义数据，
	 * 2已有省数据
	 * 3已有省、市数据
	 * 4已有省市区数据，即全部所需数据
	 */
	private int mDataState=0;
	
	//省0,市1,区2,获取数据task,
	private static final int maxIndex=3;
	private int taskIndex=0;
	
	private String mName="";
	private String mPhone="";
	private String mCode="";
	private String mAddress="";
	
	private String mProv="";
	private String mCity="";
	private String mTown="";
	
	private String mOldProv="";
	private String mOldCity="";
	private String mOldTown="";
	
	private BuyTask mBuyTask;
	private AgentApplyInfoTask mInfoTask;
	private AgentApplySumitTask mSumitTask;
//	private DaikuanActivity mActivity;
	
	private Bundle bundle;
	private AgentData agentData = new AgentData();
	
	
	public static AgentApplyFragmentNew create(double value,String couponId){
		return new AgentApplyFragmentNew();
	}
	
	public static AgentApplyFragmentNew create(){
		return new AgentApplyFragmentNew();
	}
	
	public AgentApplyFragmentNew()
	{
	}
	
	public AgentApplyFragmentNew(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.agent_apply_layout, container,false);
		initView(view);
		
		setTitle("正式申请");
		setAgentBackVisible();
//		handleTask();
		
		CommonData dataReq = new CommonData();
		String tagRep = "";
		mInfoTask = new AgentApplyInfoTask(getActivity(), this, mInfoList, spinnerTypeList, adapterType, "ApiAgentApply", "readAgentbasinfo", dataReq, tagRep);
		mInfoTask.execute("");
		
//		handleTask();
		
		return view;
	}
	
	private void initView(View view){
//		agent_appling_agree_btn = (Button)view.findViewById(R.id.agent_appling_agree_btn);
//		agent_appling_refuse_btn = (Button)view.findViewById(R.id.agent_appling_refuse_btn);
		
		agent_applying_layout =  (ScrollView)view.findViewById(R.id.agent_applying_layout);
		agent_applying_name_edit = (EditText)view.findViewById(R.id.agent_applying_name_edit);
		agent_applying_phone_edit = (EditText)view.findViewById(R.id.agent_applying_phone_edit);
		agent_applying_code_edit = (EditText)view.findViewById(R.id.agent_applying_code_edit);
		agent_applying_adress_edit = (EditText)view.findViewById(R.id.agent_applying_adress_edit);
		agent_applying_photo_btn = (Button)view.findViewById(R.id.agent_applying_photo_btn);
		agent_applying_submit_btn = (Button)view.findViewById(R.id.agent_applying_submit_btn);
		agent_applying_reset_btn = (Button)view.findViewById(R.id.agent_applying_reset_btn);
		agent_applying_photo_img = (ImageView)view.findViewById(R.id.agent_applying_photo_img);
		
		agent_applying_license_photo_img = (ImageView)view.findViewById(R.id.agent_applying_license_photo_img);
		agent_applying_organization_photo_img = (ImageView)view.findViewById(R.id.agent_applying_organization_photo_img);
		agent_applying_tax_photo_img = (ImageView)view.findViewById(R.id.agent_applying_tax_photo_img);
		agent_applying_id_card_photo_img = (ImageView)view.findViewById(R.id.agent_applying_id_card_photo_img);
		
		agent_applying_license_photo_done_img = (ImageView)view.findViewById(R.id.agent_applying_license_photo_done_img);
		agent_applying_license_photo_done_img.setVisibility(View.GONE);
		
//		agent_appling_refuse_btn.setOnClickListener(this);
//		agent_appling_agree_btn.setOnClickListener(this);
		agent_applying_photo_btn.setOnClickListener(this);
		agent_applying_submit_btn.setOnClickListener(this);
		agent_applying_reset_btn.setOnClickListener(this);
		
		agent_applying_license_photo_img.setOnClickListener(this);
		agent_applying_organization_photo_img.setOnClickListener(this);
		agent_applying_tax_photo_img.setOnClickListener(this);
		agent_applying_id_card_photo_img.setOnClickListener(this);
		
		
		agent_applying_info_more_layout = (LinearLayout)view.findViewById(R.id.agent_applying_info_more_layout);
		agent_applying_info_more_layout.setVisibility(View.GONE);
		agent_applying_more_layout = (LinearLayout)view.findViewById(R.id.agent_applying_more_layout);
		agent_applying_more_layout.setOnClickListener(this);
		agent_applying_adress_combine = (TextView)view.findViewById(R.id.agent_applying_adress_combine);
		agent_applying_more_tv = (TextView)view.findViewById(R.id.agent_applying_more_tv);
		agent_applying_more_tv.setText(">>");
		
		agent_applying_license = (LinearLayout)view.findViewById(R.id.agent_applying_license);
		agent_applying_license_tv = (TextView)view.findViewById(R.id.agent_applying_license_tv);
		agent_applying_organization = (LinearLayout)view.findViewById(R.id.agent_applying_organization);
		agent_applying_organization_tv = (TextView)view.findViewById(R.id.agent_applying_organization_tv);
		agent_applying_tax = (LinearLayout)view.findViewById(R.id.agent_applying_tax);
		agent_applying_tax_tv = (TextView)view.findViewById(R.id.agent_applying_tax_tv);
		agent_applying_id_card = (LinearLayout)view.findViewById(R.id.agent_applying_id_card);
		agent_applying_id_card_tv = (TextView)view.findViewById(R.id.agent_applying_id_card_tv);
		agent_applying_license.setOnClickListener(this);
		agent_applying_organization.setOnClickListener(this);
		agent_applying_tax.setOnClickListener(this);
		agent_applying_id_card.setOnClickListener(this);
		
		agent_applying_spinner_prov = (Spinner)view.findViewById(R.id.agent_applying_spinner_prov);
		agent_applying_spinner_city = (Spinner)view.findViewById(R.id.agent_applying_spinner_city);
		agent_applying_spinner_town = (Spinner)view.findViewById(R.id.agent_applying_spinner_town);
		agent_applying_spinner_agentType = (Spinner)view.findViewById(R.id.agent_applying_spinner_agentType);
		setActivityTitleVisible(View.VISIBLE);
		spinnerHandle();
	}

	long time=0L;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//1秒内，禁止双击两次提交
//		long currentTime=System.currentTimeMillis();
//		if(currentTime-time<1000){
//			return;
//		}
//		time=currentTime;
		
		switch (arg0.getId()) {
//		case R.id.agent_appling_refuse_btn://拒绝代理商申请协议
//			exitAgentApplication();
//			break;
//		case R.id.agent_appling_agree_btn://同意代理商申请协议
//			writeAgentApplication();
//			break;
		case R.id.agent_applying_more_layout://资质证明材料，展开或收起
			showMoreInfo();
			break;
		case R.id.agent_applying_submit_btn://提交代理商申请
			//1秒内，禁止双击两次提交
			long currentTime=System.currentTimeMillis();
			if(currentTime-time<1000){
				return;
			}
			time=currentTime;
			
			sumitAgentApplication();
			break;
		case R.id.agent_applying_reset_btn://重置申请内容
			//1秒内，禁止双击两次提交
			long currentTime2=System.currentTimeMillis();
			if(currentTime2-time<1000){
				return;
			}
			time=currentTime2;
			
			resetAgentApplication();
			break;
		case R.id.agent_applying_license_photo_img://营业执照
			pos=0;
			getAgentIdentity();
			break;
		case R.id.agent_applying_organization_photo_img://组织机构代码证
			pos=1;
			getAgentIdentity();
			break;
		case R.id.agent_applying_tax_photo_img://税务登记证
			pos=2;
			getAgentIdentity();
			break;
		case R.id.agent_applying_id_card_photo_img://法人身份证
			pos=3;
			getAgentIdentity();
			break;
			
		default:
			break;
		}
	}
	
	private void showMoreInfo(){
		int vis = agent_applying_info_more_layout.getVisibility();
		if(vis == View.GONE){
			agent_applying_info_more_layout.setVisibility(View.VISIBLE);
			agent_applying_more_tv.setText("<<");
		}else if(vis == View.VISIBLE){
			agent_applying_info_more_layout.setVisibility(View.GONE);
			agent_applying_more_tv.setText(">>");
		}
		
	}
	
	protected void setActivityTitleVisible(int vis) {
		if (getActivity() == null) {
			return;
		}
		activity_title_layout = (RelativeLayout) getActivity().findViewById(R.id.title_layout);
		if(activity_title_layout == null){
			return;
		}
		activity_title_layout.setVisibility(vis);
		
	}
	
	/**
	 * 设置返回事件
	 */
	protected void setAgentBackVisible() {
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
				// TODO Auto-generated method stub
				 FragmentManager manager =
				 getActivity().getSupportFragmentManager();
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
	
	private void checkState(){
		if(spinnerCityList.size()<=0){
			PromptUtil.showToast(getActivity(), "spinnerCityList.size()="+spinnerCityList.size());
		}
		
	}
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		mDataState=1;
		handleTask();
	}
	
	
	/**
	 * 超时 再次登录成功后该方法得到调用
	 */
	@Override
	public void onTimeout() {
		/**
		 * 获取代理名义、省市区数据
		 */	
		if(mDataState==0){	
			CommonData dataReq = new CommonData();
			String tagRep = "";
			mInfoTask = new AgentApplyInfoTask(getActivity(), this, mInfoList, spinnerTypeList, adapterType, "ApiAgentApply", "readAgentbasinfo", dataReq, tagRep);
			mInfoTask.execute("");
		}
		/**
		 * 获取省市区数据
		 */
		else{
			handleTask();
		}
	}
	
	
	private void handleTask(){
		if(taskIndex<maxIndex){
			if(taskIndex == maxIndex-1){
				mDialogClose=true;
			}
			getDataTask(taskIndex);
		}else{
			taskIndex=0;
			mAutoUpdate=false;
			mIsDialogOpen=false;
			mDialogClose=false;
		}
	}
	
	private void getDataTask(int which){
		CommonData dataReq = new CommonData();
		switch(which){
		case 0:
			mBuyTask = new BuyTask(spinnerProvList, adapterProv, "ApiAgentApply", "readChinaProv", dataReq, "prov");
			mBuyTask.execute("");
			break;
		case 1:
			dataReq.putValue("prov",mProv);
			mBuyTask = new BuyTask(spinnerCityList, adapterCity, "ApiAgentApply", "readChinaCity", dataReq, "city");
			mBuyTask.execute("");
			break;
		case 2:
			dataReq.putValue("prov",mProv);
			dataReq.putValue("city",mCity);
			mBuyTask = new BuyTask(spinnerTownList, adapterTown, "ApiAgentApply", "readChinaTown", dataReq, "town");
			mBuyTask.execute("");
			break;
		}
	}
	
	private void spinnerHandle(){
//		spinnerProvList.add("广东省");
//		spinnerProvList.add("江西省");
//		spinnerProvList.add("湖南省");
//		spinnerProvList.add("海南省");
//		spinnerProvList.add("山东省");
//		spinnerProvList.add("湖北省");
//		spinnerProvList.add("云南省");
//		spinnerProvList.add("河北省");
//		
//		spinnerCityList.add("广州市");
//		spinnerCityList.add("深圳市");
//		spinnerCityList.add("珠海市");
//		
//		spinnerTownList.add("天河区");
//		spinnerTownList.add("越秀区");
//		spinnerTownList.add("白云区");
		
//		spinnerTypeList.add("以公司名义代理");
//		spinnerTypeList.add("以个体户名义代理");
//		spinnerTypeList.add("以个人名义代理");
		
		adapterProv = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerProvList);  
		adapterCity = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerCityList);  
		adapterTown = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerTownList);  
		adapterType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerTypeList);  
     
        //设置下拉列表的风格  
		adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		adapterTown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
          
        //将adapter 添加到spinner中  
        agent_applying_spinner_prov.setAdapter(adapterProv);  
        agent_applying_spinner_city.setAdapter(adapterCity);  
        agent_applying_spinner_town.setAdapter(adapterTown);  
        agent_applying_spinner_agentType.setAdapter(adapterType);  
          
        agent_applying_spinner_prov.setSelection(0, true);
        agent_applying_spinner_city.setSelection(0, true);
        agent_applying_spinner_town.setSelection(0, true);
        
//        agent_applying_spinner_city.setOnClickListener(this);
        
        //添加事件Spinner事件监听    
        agent_applying_spinner_prov.setOnItemSelectedListener(new OnItemSelectedListener(){  
      
            @Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
//            	PromptUtil.showToast(getActivity(), spinnerProvList.get(arg2));
            	mProv= spinnerProvList.get(arg2);
            	if(!mProv.equals(mOldProv)){
            		mOldProv=mProv;
            		
            		if(!mAutoUpdate){//&& !isReset){
	            		taskIndex=1;
	            		handleTask();
            		}
            		
            		//重置时isReset==true，不做上面的handleTask();
//            		if(isReset){
//            			isReset=false;
//            		}
            	}
            }  
            
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {  
            }  
        } );
        
        agent_applying_spinner_city.setOnItemSelectedListener(new OnItemSelectedListener(){  
        	
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
        			long arg3) {  
//            	PromptUtil.showToast(getActivity(), spinnerProvList.get(arg2));
        		mCity= spinnerCityList.get(arg2);
        		if(!mCity.equals(mOldCity)){
        			mOldCity=mCity;
        			if(!mAutoUpdate){//  && !isReset){
	            		taskIndex=2;
	            		handleTask();
            		}
        			
//        			//重置时isReset==true，不做上面的handleTask();
//            		if(isReset){
//            			isReset=false;
//            		}
        		}
        	}  
        	
        	@Override
        	public void onNothingSelected(AdapterView<?> arg0) {  
        	}  
        } );
        
        agent_applying_spinner_town.setOnItemSelectedListener(new OnItemSelectedListener(){  
        	
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
        			long arg3) {  
//            	PromptUtil.showToast(getActivity(), spinnerProvList.get(arg2));
        		mTown= spinnerTownList.get(arg2);
        		agent_applying_adress_combine.setText(mProv + "-" + mCity + "-" + mTown);
        		
//        		if(!mTown.equals(mOldTown)){
//        			mOldTown=mTown;
//        			taskIndex=2;
//        			handleTask();
//        		}
        	}  
        	
        	@Override
        	public void onNothingSelected(AdapterView<?> arg0) {  
        	}  
        } );
        
        agent_applying_spinner_agentType.setOnItemSelectedListener(new OnItemSelectedListener(){  
        	
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
        			long arg3) {  
//        		PromptUtil.showToast(getActivity(), arg2+"");
        		
        		agentType=arg2;
        		
//        		if(!isReset){
        			resetInfo();
//        		}
        		
        		//重置时isReset==true，不做上面的resetInfo();
//        		if(isReset){
//        			isReset=false;
//        		}
        		
        	}  
        	
        	@Override
        	public void onNothingSelected(AdapterView<?> arg0) {  
        	}  
        } );
        
		
	}
	
	private void writeAgentApplication()
	{
		agent_applying_agreement_layout.setVisibility(View.GONE);
		agent_applying_layout.setVisibility(View.VISIBLE);
	}
	
	private void exitAgentApplication()
	{
//		getActivity().finish();
	}
	private void getAgentIdentity()
	{
		int max = mInfoList.get(agentType).infoDataList.size();
		if(pos<max){
			showPicture(pos);
		}
		
//		agent_applying_photo_done_img.setVisibility(View.VISIBLE);
	}
	
	private void sumitAgentApplication()
	{
		//do something
		if(checkInput()){
			CommonData dataReq = new CommonData();
			dataReq.putValue("custypeid", mInfoList.get(agentType).custypeid);
			dataReq.putValue("name", mName);
			dataReq.putValue("phone", mPhone);
			dataReq.putValue("agentcode", mCode);
			dataReq.putValue("address", mAddress);
			dataReq.putValue("prov", mProv);
			dataReq.putValue("city", mCity);
			dataReq.putValue("town", mTown);
//			dataReq.putValue("upfileinfo", "");
			String tagRep = "";
			mSumitTask = new AgentApplySumitTask(getActivity(), mInfoList.get(agentType), "ApiAgentApply", "insertapplyAgent", dataReq, tagRep);
			mSumitTask.execute("");
		}
	}
	
	private void resetAgentApplication()
	{
//		isReset=true;
		agent_applying_name_edit.setText("");
		agent_applying_phone_edit.setText("");
		agent_applying_code_edit.setText("");
		agent_applying_code_edit.setHint(agent_applying_code_edit.getHint()+"");
		agent_applying_adress_edit.setText("");
		agent_applying_adress_edit.setHint(agent_applying_adress_edit.getHint()+"");
		
//		if(!isFirstIn){
//			int max=0;
//			spinnerProvList.clear();
//			max=spinnerProvListFirst.size();
//			for(int n=0; n<max; n++){
//				spinnerProvList.add(spinnerProvListFirst.get(n));
//			}
//			mProv = spinnerProvList.get(0);
//			adapterProv.notifyDataSetChanged();
//			agent_applying_spinner_prov.setSelection(0, true);
//			
//			spinnerCityList.clear();
//			max=spinnerCityListFirst.size();
//			for(int n=0; n<max; n++){
//				spinnerCityList.add(spinnerCityListFirst.get(n));
//			}
//			mCity = spinnerCityList.get(0);
//			adapterCity.notifyDataSetChanged();
//			agent_applying_spinner_city.setSelection(0, true);
//			
//			spinnerTownList.clear();
//			max=spinnerTownListFirst.size();
//			for(int n=0; n<max; n++){
//				spinnerTownList.add(spinnerTownListFirst.get(n));
//			}
//			mTown = spinnerTownList.get(0);
//			adapterTown.notifyDataSetChanged();
//			agent_applying_spinner_town.setSelection(0, true);
//			
//			agent_applying_adress_combine.setText(mProv + "-" + mCity + "-" + mTown);
//		}
		
		agentType=0;
		adapterType.notifyDataSetChanged();
		agent_applying_spinner_agentType.setSelection(0);
		resetInfo();
		agent_applying_info_more_layout.setVisibility(View.GONE);
		agent_applying_more_tv.setText(">>");
	}
	
	private boolean checkInput(){
		String name = agent_applying_name_edit.getText().toString();
		String phone = agent_applying_phone_edit.getText().toString();
		String code = agent_applying_code_edit.getText().toString();
		String address = agent_applying_adress_edit.getText().toString();
		if(null== name || "".equals(name)){
			PromptUtil.showToast(getActivity(), "请输入正确的姓名");
			return false;
		}
		mName=name;
		
		if(null== phone || "".equals(phone) 
			|| phone.length() != 11 || !isMobileNum(phone)){
			PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
			return false;
		}
		mPhone=phone;
		
		if(null== code || "".equals(code) || code.length() != 6){
			//区号需校验： 如广州区号020，茂名区号0668，再在末尾自定义数字，补够6位数字，如020001,066801。
			PromptUtil.showToast(getActivity(), "请输入正确的区域代号");
			return false;
		}
		mCode=code;
		
//		if(null== address || "".equals(address)){
//			PromptUtil.showToast(getActivity(), "请输入正确的意向地区");
//			return false;
//		}
//		mAddress=address;
		
		/*
		 * 自定义填写合作意向区 优先处理，若输入不为空，则已输入为准，忽略选择三项
		 */
		if(null== address || "".equals(address)){	
			if("".equals(mProv+"") || "".equals(mCity+"") || "".equals(mTown+"")){
				PromptUtil.showToast(getActivity(), "请选择合作意向区");
				return false;
			}
			mAddress="";
		}else{
			mAddress=address;
		}
		
		/*
		 * 资质材料图片上传核对，现在暂时是非必填项，不做核对
		 */
		int max = mInfoList.get(agentType).infoDataList.size();
		for(int n=0; n<max; n++){
			String select;
			select = mInfoList.get(agentType).infoDataList.get(n).getValue("selectpic");
			if(select == null || "".equals(select) || "0".equals(select)){
    			PromptUtil.showToast(getActivity(), "请上传图片："+mInfoList.get(agentType).infoDataList.get(n).getValue("pictypename"));
    			return false;
			}
		}
		
		return true;
	}
	
	private boolean isMobileNum(String mobile) {
		return UserInfoCheck.checkMobilePhone(mobile);
//		 Pattern p = Pattern
//	                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	        Matcher m = p.matcher(mobile);
//	        return m.matches();
	}
	
	private void resetInfo(){
		agent_applying_license.setVisibility(View.GONE);
		agent_applying_organization.setVisibility(View.GONE);
		agent_applying_tax.setVisibility(View.GONE);
		agent_applying_id_card.setVisibility(View.GONE);
		int max = mInfoList.get(agentType).infoDataList.size();
		for(int n=0; n<max; n++){
			mInfoList.get(agentType).infoDataList.get(n).putValue("selectpic", "0");
			String name;
			name = mInfoList.get(agentType).infoDataList.get(n).getValue("pictypename");
			if(name != null && !"".equals(name)){
				switch(n){
	    			case 0:
	        			agent_applying_license.setVisibility(View.VISIBLE);
	        			agent_applying_license_tv.setText(name);
	        			agent_applying_license_photo_img.setImageBitmap(null);
	        			agent_applying_license_photo_img.setBackgroundResource(R.drawable.camera_icon);
		        		break;
	    			case 1:
	    				agent_applying_organization.setVisibility(View.VISIBLE);
	    				agent_applying_organization_tv.setText(name);
	    				agent_applying_organization_photo_img.setImageBitmap(null);
	    				agent_applying_organization_photo_img.setBackgroundResource(R.drawable.camera_icon);
	    				break;
	    			case 2:
	    				agent_applying_tax.setVisibility(View.VISIBLE);
	    				agent_applying_tax_tv.setText(name);
	    				agent_applying_tax_photo_img.setImageBitmap(null);
	    				agent_applying_tax_photo_img.setBackgroundResource(R.drawable.camera_icon);
	    				break;
	    			case 3:
	    				agent_applying_id_card.setVisibility(View.VISIBLE);
	    				agent_applying_id_card_tv.setText(name);
	    				agent_applying_id_card_photo_img.setImageBitmap(null);
	    				agent_applying_id_card_photo_img.setBackgroundResource(R.drawable.camera_icon);
	    				break;
				}
			}
			
		}
	}

	public void showPicture(int index) {
//		pos = index;
//		CharSequence[] items = { "相册", "相机", "查看大图" };
		CharSequence[] items = { "相册", "相机"};
		new AlertDialog.Builder(getActivity()).setTitle("选择图片来源")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							pickPhoto();
						} else if (which == 1) {
							takePhoto();
						} else {
//							showBigPicure();
						}
					}
				}).create().show();
	}
	
	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = getActivity().getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(getActivity(), "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == Activity.RESULT_OK) {

				doPhoto(requestCode, data);
//				agent_applying_photo_done_img.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "请重新选择图片", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				Toast.makeText(getActivity(), "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				Toast.makeText(getActivity(), "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		mCursor = getActivity().managedQuery(photoUri, pojo, null, null, null);
		if (mCursor != null) {
			int columnIndex = mCursor.getColumnIndexOrThrow(pojo[0]);
			mCursor.moveToFirst();
			picPath = mCursor.getString(columnIndex);
			// cursor.close();
		}
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
			try {
				File file = new File(picPath);
				FileInputStream inputStream = new FileInputStream(file);
				Logger.d("file", "length " + inputStream.available());
//				inputStream.close();
			

//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 4;
//			Bitmap bm = BitmapFactory.decodeFile(picPath, options);
			Bitmap bm = decodeSampledBitmapFromDescriptor(inputStream.getFD()	, 300, 300, null);
			if (pos == 0) {
				agent_applying_license_photo_img.setBackgroundDrawable(null);
				agent_applying_license_photo_img.setImageBitmap(bm);
				identityBitmap1 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[0] = picPath;
				mInfoList.get(agentType).infoDataList.get(pos).putValue("selectpic", "1");
//				agent_applying_license_photo_done_img.setVisibility(View.VISIBLE);
			} else if (pos == 1){
				agent_applying_organization_photo_img.setBackgroundDrawable(null);
				agent_applying_organization_photo_img.setImageBitmap(bm);
				identityBitmap2 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[1] = picPath;
				mInfoList.get(agentType).infoDataList.get(pos).putValue("selectpic", "1");
//				agent_applying_organization_photo_done_img.setVisibility(View.VISIBLE);
			} else if (pos == 2){
				agent_applying_tax_photo_img.setBackgroundDrawable(null);
				agent_applying_tax_photo_img.setImageBitmap(bm);
				identityBitmap3 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[2] = picPath;
				mInfoList.get(agentType).infoDataList.get(pos).putValue("selectpic", "1");
//				agent_applying_tax_photo_done_img.setVisibility(View.VISIBLE);
			} else if (pos == 3){
				agent_applying_id_card_photo_img.setBackgroundDrawable(null);
				agent_applying_id_card_photo_img.setImageBitmap(bm);
				identityBitmap4 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[3] = picPath;
				mInfoList.get(agentType).infoDataList.get(pos).putValue("selectpic", "1");
//				agent_applying_id_card_photo_done_img.setVisibility(View.VISIBLE);
			}
			isPicChange = true;
//			agent_applying_license_photo_done_img.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			Toast.makeText(getActivity(), "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}
	
	public static Bitmap decodeSampledBitmapFromDescriptor(
            FileDescriptor fileDescriptor, int reqWidth, int reqHeight, ImageCache cache) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqWidth);
//
//        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inScaled=false;
        //测试
//        options.inInputShareable = true;
//        options.inPurgeable = true;
//        
        // If we're running on Honeycomb or newer, try to use inBitmap

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
		if(mInfoTask != null){
			mInfoTask.cancel(true);
		}
		if(mSumitTask != null){
			mSumitTask.cancel(true);
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
		setTitle("正式申请");
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
		String mApiName;
		String mApiFunc;
		SunType mdataReq;
		String mtagRep;
		ArrayAdapter<String> madapter;
		
		BuyTask(List<String> list, ArrayAdapter<String> adapterProv, String ApiName, String ApiFunc, SunType dataReq, String tagRep){
			mList=list;
			madapter=adapterProv;
			mApiName=ApiName;
			mApiFunc=ApiFunc;
			mdataReq=dataReq;
			mtagRep=tagRep;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				
//				CommonData data = new CommonData();
//				data.putValue("address",agentData.address);
//				data.putValue("phone",agentData.phone);
//				data.putValue("name",agentData.name);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(mApiName, 
						mApiFunc, mdataReq);
				AgentAreaParser authorRegParser = new AgentAreaParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp =null;
				PromptUtil.dissmiss();
				mIsDialogOpen=false;
				mDialogClose=false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(mIsDialogOpen && mDialogClose){
				PromptUtil.dissmiss();
			}
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
				PromptUtil.dissmiss();
				mAutoUpdate=false;
				mIsDialogOpen=false;
				mDialogClose=false;
			}else{
				try {
					mList.clear();
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas, mtagRep);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						mAutoUpdate=false;
						mIsDialogOpen=false;
						mDialogClose=false;
						return;
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						mAutoUpdate=true;
						madapter.notifyDataSetChanged();
//						int max = mList.size();
						switch(taskIndex){
							case 0:
								mProv = mList.get(0);
								agent_applying_spinner_prov.setSelection(0);
//								if(isFirstIn){
//									spinnerProvListFirst.clear();
//									for(int n=0; n<max; n++){
//										spinnerProvListFirst.add(mList.get(n));
//									}
//								}
								break;
							case 1:
								mCity = mList.get(0);
								agent_applying_spinner_city.setSelection(0);
//								if(isFirstIn){
//									spinnerCityListFirst.clear();
//									for(int n=0; n<max; n++){
//										spinnerCityListFirst.add(mList.get(n));
//									}
//								}
								break;
							case 2:
								mTown = mList.get(0);
								agent_applying_spinner_town.setSelection(0);
								agent_applying_adress_combine.setText(mProv + "-" + mCity + "-" + mTown);
//								if(isFirstIn){
//									isFirstIn=false;
//									spinnerTownListFirst.clear();
//									for(int n=0; n<max; n++){
//										spinnerTownListFirst.add(mList.get(n));
//									}
//								}
								break;
						}
						taskIndex++;
						handleTask();
					}
				} catch (Exception e) {
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
					PromptUtil.dissmiss();
					mAutoUpdate=false;
					mIsDialogOpen=false;
					mDialogClose=false;
				}
			
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(!mIsDialogOpen){
				PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
				mIsDialogOpen=true;
			}
		}
	}
	private void parserResoponse(List<ProtocolData> params, String tagRep){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					mResult = result1.get(0).mValue;
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					mMessage = message.get(0).mValue;
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					String prov = "";
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals(tagRep)){
									prov  = item.mValue;
									
								}	
							}
						}
					}
					
					mList.add(prov);
				}
				
			}
		}
	}
}
