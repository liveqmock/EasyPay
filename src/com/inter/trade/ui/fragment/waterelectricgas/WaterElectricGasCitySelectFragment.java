package com.inter.trade.ui.fragment.waterelectricgas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.WaterElectricGasCitySelectAdapter;
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
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.Trans2PinYin;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasData;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.ui.views.IndexableListView;
import com.inter.trade.ui.views.StringMatcher;

/**
 *  水电煤城市选择Fragment
 * @author Lihaifeng
 *
 */
@SuppressLint("ValidFragment")
public class WaterElectricGasCitySelectFragment extends BaseFragment implements OnClickListener,OnItemClickListener{
//	public static ArrayList<String> mItemsTag;
//	public static ArrayList<String> mItems;
	private IndexableListView mListView;
	private WaterElectricGasCitySelectAdapter mAdapter;
	private EditText city_edit;
	
	
	private BuyTask mBuyTask;
	private Bundle bundle;
	
	private CommonData mData = new CommonData();
	
	public CommonData mfeeData=new CommonData();
	
	private String mProvinceName;
	private ArrayList<WaterElectricGasData> mList = new ArrayList<WaterElectricGasData>();
	private ArrayList<String> mSortList = new ArrayList<String>();
	
	public WaterElectricGasCitySelectFragment()
	{
	}
	
	public WaterElectricGasCitySelectFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//LoginUtil.detection(getActivity());
		
//		 获取水电煤缴费信息，包括城市列表数据，缴费类型，缴费公司
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		
		View view = inflater.inflate(R.layout.water_electric_gas_pay_select_city_layout, container,false);
		initView(view);
		
		setTitle("城市选择");
		setBackVisible();
		
		
//		 获取水电煤缴费信息，包括城市列表数据，缴费类型，缴费公司
//		mBuyTask = new BuyTask();
//		mBuyTask.execute("");
		
		
		return view;
	}
	
	private void initView(View view){
		mListView = (IndexableListView) view.findViewById(R.id.listview);
		mListView.setFastScrollEnabled(true);
	    mListView.setOnItemClickListener(this);
		
		city_edit = (EditText)view.findViewById(R.id.city_edit);
		searchCity();
//		city_edit.setOnClickListener(this);
//		init();
//		initTest();
	}
	
	private void init(){
//		WaterElectricGasMainFragment.mDatas = new ArrayList<WaterElectricGasData>();
		mAdapter = new WaterElectricGasCitySelectAdapter(getActivity(), WaterElectricGasMainFragment.mDatas);
        
	    mListView.setAdapter(mAdapter);
//	    mListView.setFastScrollEnabled(true);
//	    mListView.setOnItemClickListener(this);
	    mAdapter.notifyDataSetChanged();
	    
	    
//	    String s1 ="上海 #@#patypeName:电费 @@companyid:124,companyname:黄浦区电厂 @@companyid:125,companyname:松州区电厂 #@#patypeName:煤气 @@companyid:121,companyname:宝山区电厂";
//	    String s2 ="北京 #@#patypeName:电费 @@companyid:124,companyname:黄浦区电厂 @@companyid:125,companyname:松州区电厂 #@#patypeName:水费 @@companyid:121,companyname:宝山区电厂";
//	    String s3 ="昆明 #@#patypeName:水费 @@companyid:124,companyname:黄浦区电厂 @@companyid:125,companyname:松州区电厂 #@#patypeName:煤气 @@companyid:121,companyname:宝山区电厂";
//	    getData(s1);
//	    getData(s2);
//	    getData(s3);
//	    sortData();
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
//		case R.id.city_edit://输入城市
//			getCity();
//			break;
		default:
			break;
		}
	}
	
	private void getCity () {
//		Intent intent = new Intent();
//		intent.setClass(getActivity(), AgentApplyActivity.class);
//		startActivityForResult(intent, 3);
	}
	
	public void searchCity(){
		city_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("Hello", "onTextChanged:" + s + "-" + start + "-" + count + "-" + before); 
				boolean isMatch = false;
				String cityInput;
				cityInput = toUTF_8(s.toString().trim());
				if(cityInput==null || "".equals(cityInput)){
//					PromptUtil.showToast(getActivity(), "请输入正确的城市");
					return;
				}
				
				String cityInputTag="";
				cityInputTag = Trans2PinYin.trans2PinYin(cityInput);
				cityInputTag = cityInputTag.toUpperCase();
		        int sta = 0;
		        int end = WaterElectricGasMainFragment.mDatas.size()-1;
		        int mid = 0;
		        while(sta <= end){
		        	mid = (int)(sta+end)/2;
		        	if(WaterElectricGasMainFragment.mDatas.get(mid).cityNameTag.compareTo(cityInputTag) > 0){
		        		end = mid-1;
		        	}else if(WaterElectricGasMainFragment.mDatas.get(mid).cityNameTag.compareTo(cityInputTag) < 0){
		        		sta = mid+1;
		        	}else{
		        		isMatch = true;
		        		break;
		        	}
		        }
		        mListView.setSelection(mid);
//        		mAdapter.notifyDataSetChanged();	
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
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
//		PromptUtil.showToast(getActivity(), "position="+arg2 + ", rowId="+arg3);
//		log("agent list: position="+arg2 + ", rowId="+arg3);
		
		if(arg3<0 || WaterElectricGasMainFragment.mDatas.size()==0){
			return;
		}
//		String city = WaterElectricGasMainFragment.mItems.get(arg2-1);
		String city = WaterElectricGasMainFragment.mDatas.get(arg2).cityName;
		if(city.charAt(0) > 'Z'){//listview里的城市拼音字母A~Z不响应，只接收城市的名称
			Intent intent = new Intent();
			intent.putExtra("cityName", city);
			intent.putExtra("cityIndex", arg2);
			getActivity().setResult(Activity.RESULT_OK, intent);
			getActivity().finish();
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
		setTitle("城市选择");
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
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiUtility", 
						"getProductList", mData);
				WaterElectricGasParser myParser = new WaterElectricGasParser();
				mRsp = HttpUtil.doRequest(myParser, mDatas);
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
					init();
					
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
//					WaterElectricGasData picData = new WaterElectricGasData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("provinceName")){
									mProvinceName  = item.mValue;
								}else if(item.mKey.equals("city")){
									getData(item.mValue);
									mProvinceName = "";
								}
							}
						}
					}
					
//					mList.add(picData);
				}
			}
		}
		
		sortData();
	}
	
	private void getData(String cityInfo){
		WaterElectricGasData data = new WaterElectricGasData();
    	data.payTypeList = new ArrayList<Integer>();;
    	data.companyLList = new ArrayList<ArrayList<String>>();
    	data.companyIdLList = new ArrayList<ArrayList<String>>();
    	
		String CITY_END = "#@#";
		
		String PAYTYPE_START = "#@#patypeName:";
		String PAYTYPE_END = "@@";
		
		String COMPANYID_START = "companyid:";
		String COMPANYID_END = ",";
		
		String COMPANYNAME_START = "companyname:";
		String COMPANYNAME_END = "@@";
		
//		data.payTypeList[0]=0;
//    	data.payTypeList[1]=0;
//    	data.payTypeList[2]=0;
		
		int index = -1;
		int start = -1;
		int end = -1;
		int start_type = -1;
		int end_type = -1;
		int start_com = -1;
		int end_com = -1;
		index = cityInfo.indexOf(CITY_END);
		if(index != -1){
			data.cityName = cityInfo.substring(0, index);
			data.cityName=toUTF_8(data.cityName.trim());
			
			if(mProvinceName != null && !"".equals(mProvinceName) 
					&& !toUTF_8(mProvinceName.trim()).equals(data.cityName)){
				data.cityName=toUTF_8(mProvinceName.trim())+data.cityName;
			}
			
			start_type = cityInfo.indexOf(PAYTYPE_START);
			if(start_type != -1 && start_type != cityInfo.length()){
				end_type = cityInfo.indexOf(PAYTYPE_START, start_type+PAYTYPE_START.length());
				if(end_type == -1){
					end_type = cityInfo.length();
				}
			}
			while(start_type != -1 && end_type != -1){
				String  payTypeStr = cityInfo.substring(start_type+PAYTYPE_START.length(), end_type);
				end = payTypeStr.indexOf(PAYTYPE_END);
				if(end != -1){
					String  payType = payTypeStr.substring(0, end);
					if(payType != null && !("".equals(payType))){
						
						String type = toUTF_8(payType.trim());  
						if(toUTF_8("水费").equals(type)){
							data.payTypeList.add(1);
						}else if(toUTF_8("电费").equals(type)){
							data.payTypeList.add(2);
						}else if(toUTF_8("燃气费").equals(type)){
							data.payTypeList.add(3);
						}else{
							start_type = end_type;
							if(start_type != -1 && start_type != cityInfo.length()){
								end_type = cityInfo.indexOf(PAYTYPE_START, start_type+PAYTYPE_START.length());
								if(end_type == -1){
									end_type = cityInfo.length();
								}
								continue;
							}else{
								break;
							}
						}
					}
				}
				
				ArrayList<String> companeyIdList = new ArrayList<String>();
				ArrayList<String> companeyList = new ArrayList<String>();
				
				start_com = payTypeStr.indexOf(PAYTYPE_END);
				if(start_com != -1 && start_com != payTypeStr.length()){
					end_com = payTypeStr.indexOf(PAYTYPE_END, start_com+PAYTYPE_END.length());
					if(end_com == -1){
						end_com = payTypeStr.length();
					}
				}
				while(start_com != -1 && end_com != -1){
					String  companyStr = payTypeStr.substring(start_com+PAYTYPE_END.length(), end_com);
					start = companyStr.indexOf(COMPANYID_START);
					if(start != -1){
						end = companyStr.indexOf(COMPANYID_END, start+COMPANYID_START.length());
					}
					if(start != -1 && end != -1){
						String  companyId = companyStr.substring(start+COMPANYID_START.length(), end);
						companeyIdList.add(companyId);
					}
					
					start = companyStr.indexOf(COMPANYNAME_START, end+COMPANYID_END.length());
					if(start != -1){
						String  company = companyStr.substring(start+COMPANYNAME_START.length());
						companeyList.add(company);
					}
					
					start_com = end_com;
					if(start_com != -1 && start_com != payTypeStr.length()){
						end_com = payTypeStr.indexOf(PAYTYPE_END, start_com+PAYTYPE_END.length());
						if(end_com == -1){
							end_com = payTypeStr.length();
						}
					}else{
						break;
					}
				}
				data.companyIdLList.add(companeyIdList);
				data.companyLList.add(companeyList);
				
				start_type = end_type;
				if(start_type != -1 && start_type != cityInfo.length()){
					end_type = cityInfo.indexOf(PAYTYPE_START, start_type+PAYTYPE_START.length());
					if(end_type == -1){
						end_type = cityInfo.length();
					}
				}else{
					break;
				}
			}
		}
		mList.add(data);
//    	WaterElectricGasMainFragment.mDatas.add(data);
	}
	
	private void sortData(){
		/*
		 * 以下测试
		 */
//		int cityCounts = Trans2PinYin.pinYinString.length;
//        for(int n=0; n<cityCounts; n++){
//        	WaterElectricGasData temdata = new WaterElectricGasData();
//        	temdata.cityName = Trans2PinYin.pinYinString[n];
//        	mList.add(temdata);
//        }
		/*
		 * 以上测试
		 */
		
		WaterElectricGasMainFragment.mDatas = new ArrayList<WaterElectricGasData>();
//		WaterElectricGasMainFragment.mDatas = new ArrayList<WaterElectricGasData>();
//		WaterElectricGasMainFragment.mItemsTag = new ArrayList<String>();
//        WaterElectricGasMainFragment.mItems = new ArrayList<String>();
        
        ArrayList<String> itemsTag = new ArrayList<String>();
        String cityStr = "";
        String cityStrTag = "";
        int cityCount = mList.size();
        for(int n=0; n<cityCount; n++){
        	cityStr = mList.get(n).cityName;
        	cityStrTag = Trans2PinYin.trans2PinYin(cityStr);
        	itemsTag.add(cityStrTag.toUpperCase() + String.format("%05d", n));
        }
        
//        Collections.sort(WaterElectricGasMainFragment.mItems);
        Collections.sort(itemsTag);

        String tag = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char tagCh = '#';
        char ch = tagCh;
        int index = 0;
        cityCount = itemsTag.size();
        for(int n=0; n<cityCount; n++){
        	cityStrTag = itemsTag.get(n);
        	ch = cityStrTag.charAt(0);
        	if(ch > tagCh){
        		if(tag.indexOf(ch) != -1){
	        		tagCh = ch;
	        		
	        		WaterElectricGasData data = new WaterElectricGasData();
//	            	data.payTypeList = new String[3];
//	            	data.companyLList = new ArrayList<ArrayList<String>>();
//	            	data.companyIdLList = new ArrayList<ArrayList<String>>();
	            	
	        		data.cityName = ch+"";
	        		data.cityNameTag = ch+"";
	            	WaterElectricGasMainFragment.mDatas.add(data);
        		}
        	}
        	cityStr = cityStrTag.substring(cityStrTag.length()-5);
        	index = Integer.parseInt(cityStr);
        	mList.get(index).cityNameTag = cityStrTag.substring(0, cityStrTag.length()-5);
        	WaterElectricGasMainFragment.mDatas.add(mList.get(index));
        	
//        	mSortList.add(toUTF_8(mList.get(index).cityName));
        }
//        Collections.sort(mSortList);
	}
	
	private static String toUTF_8(String str) {  
	     try {  
	         return new String(str.getBytes(), "UTF-8");  
//	         return new String(str.getBytes(), "ISO-8859-1");  
	     } catch (UnsupportedEncodingException e) {  
	         e.printStackTrace();  
	         throw new RuntimeException(e);  
	     }  
	 } 
}
