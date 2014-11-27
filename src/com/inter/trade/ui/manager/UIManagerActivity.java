package com.inter.trade.ui.manager;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.inter.trade.R;
import com.inter.trade.data.ResultData;
import com.inter.trade.ui.PayApp.BuySuccessListener;
import com.inter.trade.ui.creditcard.CommonEasyCreditcardPayFragment;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.fragment.airticket.AirTicketAddContactFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketAddPassengerFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainCalendarPickerFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainClearingFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainCreditCardFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainOtherFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainQueryAirlineDetailFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainQueryFanchengFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainQueryFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketOrderFormFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketOrderFormWangfanFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketOrderHistoryListFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketOrderHistoryListItemFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketOrderPaySuccessFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketSelectContactFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketSelectPassengerFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketSwipPayFragment;
import com.inter.trade.ui.fragment.airticket.AirTicketMainPagerFragment;
import com.inter.trade.ui.fragment.airticket.address.AirTicketExpandableListViewCityFragment;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.buyhtbcard.HTBBuyMainFragment;
import com.inter.trade.ui.fragment.buyhtbcard.HTBBuySuccessFragment;
import com.inter.trade.ui.fragment.buyhtbcard.PayMainFragment;
import com.inter.trade.ui.fragment.hotel.HotelDescriptionFragment;
import com.inter.trade.ui.fragment.hotel.HotelDetailFragment;
import com.inter.trade.ui.fragment.hotel.HotelListFragment;
import com.inter.trade.ui.fragment.hotel.HotelMainPagerFragment;
import com.inter.trade.ui.fragment.hotel.HotelOrderFragment;
import com.inter.trade.ui.fragment.hotel.HotelPayConfirmFragment;
import com.inter.trade.ui.fragment.hotel.HotelPayRecordFragment;
import com.inter.trade.ui.fragment.hotel.HotelSelectCityFragment;
import com.inter.trade.ui.fragment.hotel.HotelSelectDateFragment;
import com.inter.trade.ui.fragment.hotel.HotelSelectKeywordFragment;
import com.inter.trade.ui.fragment.hotel.data.HotelGetCityData;
import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
import com.inter.trade.ui.fragment.transfer2.TransferMainPagerFragment;
import com.inter.trade.ui.fragment.transfer2.TransferShoukuanConfirmFragment;
import com.inter.trade.ui.fragment.transfer2.TransferSwipPayFragment;
import com.inter.trade.ui.manager.core.IMainHandler;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.IMainHandlerProcess;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * UI Activity 管理类
 * @author zhichao.huang
 *
 */
public class UIManagerActivity extends FragmentActivity implements IMainHandlerProcess{
	
	public static final String TAG = UIManagerActivity.class.getSimpleName();
	
	private IMainHandler mainHandler;
	
	/**
	 * 默认Fragment
	 */
	public Fragment fragment;
	
	/**
	 * 返回按钮
	 */
	public Button backButton;
	
	/**
	 * 标题栏 右侧按钮
	 */
	public Button title_right_btn, title_right_icon;
	
	/**
	 * 标题
	 */
	public TextView title_name;
	
	/**
	 * 标题栏底部分割线
	 */
	public ImageView tilte_line;
	
	/**
	 * 目标Fragment的targetID
	 */
	public int targetID;
	
	/**
	 * 记录是否刷新页面
	 */
	public boolean isRefresh = false;
	
	 //*************************飞机票   返回数据封装 start*************************
	 
	/**
	 * 飞机票日期（返回数据）
	 */
	public String airTicketDate = null;//单程-出发日期
	
	public String airTicketStartDate = null;//往返-出发日期
	public String airTicketFanDate = null;//往返-返程日期
	/**
	 * 单程或返程，0：单程；1：返程，默认0
	 */
	public int danOrFan;
	
	public ApiAirticketGetCityData dancheng_start_data = null;//单程-出发-模型数据
	public ApiAirticketGetCityData dancheng_end_data = null;//单程-到达-模型数据
	
//	public ApiAirticketGetCityData wangfan_start_data = null;//往返-出发-模型数据
//	public ApiAirticketGetCityData wangfan_end_data = null;//往返-到达-模型数据
	
	public ArrayList<PassengerData> selectedPassengerList = null;//乘机人列表
	public ArrayList<PassengerData> selectedLinkmanList = null;//联系人列表
	
	 //*************************飞机票   返回数据封装 end***************************
	
	
	//*************************酒店预订   返回数据封装 start*************************
	/**
	 * 酒店预订 日期(入住)
	 */
	public String hotelDate = null;
	
	/**
	 * 酒店预订 日期(离店)
	 */
	public String hotelDateOut = null;
	
	/**
	 * 酒店预订 城市
	 */
	public String hotelCity = null;
	public HotelGetCityData hotelCityData = null;
	
	
	/**
	 * 酒店预订 价格
	 */
	public String hotelPrice = null;
	
	/**
	 * 酒店预订 价格ID
	 */
	public int priceId = 0;
	
	/**
	 * 酒店预订 星级 文字
	 */
	public String hotelStarLevel = null;
	
	/**
	 * 酒店预订 星级 ID
	 */
	public int starId = 0;
	
	/**
	 * 酒店预订 关键字
	 */
	public HotelKeywordData hotelKeyWord = null;
	
	/**
	 * 酒店预订 关键字类别
	 */
	public int hotelKeyType = 0;
	
	
	//*************************酒店预订   返回数据封装 end***************************
	 
	/**
	 * 银联交易流水号
	 */
	public static String mBankNo="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTheme(R.style.DefaultLightTheme);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.func_layout2);
		mBankNo = "";
		
		mainHandler = new IMainHandler (this);
		IMainHandlerManager.regist(mainHandler);
		
		initViews();
		
		if(savedInstanceState != null) {
			Log.i(TAG, "onSaveInstanceState get");
			targetID = savedInstanceState.getInt("targetID");
		} else {
			targetID = getIntent().getIntExtra("targetFragment", 0);
			loadFirstFragment(switchUI(targetID, null));
//			switchFragment(switchUI(targetID, null), 1);
		}
		
		
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState put");
		outState.putInt("targetID", targetID);
		
	}
	
	/**
	 * 初始化
	 */
	private void initViews() {
		
		backButton = (Button)findViewById(R.id.back_btn);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeFragmentToStack();
			}
		});
		
		title_name = (TextView)findViewById(R.id.title_name);
		title_right_btn = (Button)findViewById(R.id.title_right_btn);
		title_right_icon = (Button)findViewById(R.id.title_right_btn_two);
		tilte_line = (ImageView)findViewById(R.id.iv_tilte_line);
		
		getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				Log.i(TAG, "onBackStackChanged() - getBackStackEntryCount():"+getSupportFragmentManager().getBackStackEntryCount());
//				if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
//					finish();
//				}
			}
		});
	}
	
	/**
	 * 设置顶部标题
	 * @param title
	 */
	public void setTopTitle(String title) {
		if(title_name != null) {
			title_name.setText(title);
		}
	}
	
	/**
	 * 设置返回按钮监听
	 * 
	 * @param listener,如为null,返回按钮默认操作是removeFragmentToStack();
	 */
	public void setBackButtonOnClickListener (OnClickListener listener) {
		if(backButton != null && listener != null) {
			backButton.setOnClickListener(listener);
		}
		
		if(backButton != null && listener == null) {
			backButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removeFragmentToStack();
				}
			});
		}
	}
	
	/**
	 * 设置返回按钮隐藏、显示
	 * @param visible
	 */
	public void setBackButtonHideOrShow (int visible) {
		if(backButton != null) {
			backButton.setVisibility(visible);
		}
	}
	
	/**
	 * 设置顶部右侧按钮监听
	 * @param title
	 * @param isVisible
	 * @param listener
	 */
	public void setRightButtonOnClickListener (String title, int isVisible, OnClickListener listener) {
		setRightButtonIconOnClickListener(View.GONE, null);
		if(title_right_btn == null || listener == null) return;
		if(title != null && !title.equals("")) {
			title_right_btn.setText(title);
		}
		title_right_btn.setVisibility(isVisible);
		title_right_btn.setOnClickListener(listener);
	}
	
	/**
	 * 设置顶部右侧按钮图标监听
	 * @param isVisible
	 * @param listener
	 */
	public void setRightButtonIconOnClickListener (int isVisible, OnClickListener listener) {
		title_right_btn.setVisibility(View.GONE);
		title_right_icon.setVisibility(isVisible);
		title_right_icon.setOnClickListener(listener);
	}
	
	@Override
	public void handlerUI(int uiarg, int isAddToStack, Bundle data) {
		switchFragment(switchUI(uiarg, data), isAddToStack);
	}
	
	/**
	 * 切换Fragment
	 * @param uitag
	 */
	private Fragment switchUI(int uitag, Bundle data) {
		switch (uitag) {
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET:
			fragment = AirTicketMainPagerFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_OTHER:
			fragment = AirTicketMainOtherFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_SWIP_PAY:
			fragment = AirTicketSwipPayFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_CALENDAR_PICKER:
			fragment = AirTicketMainCalendarPickerFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_CITY_SELECTE:
//			fragment = AirTicketAddressCityMainFragment.newInstance(data);
			fragment = AirTicketExpandableListViewCityFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_QUERY:
			fragment = AirTicketMainQueryFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_FORM:
			fragment = AirTicketOrderFormFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_AIRLINE_DETAIL:
			fragment = AirTicketMainQueryAirlineDetailFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_ADD_PASSENGER://增加或者更新乘机人
			fragment=AirTicketAddPassengerFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_SELECT_PASSENGER://选择乘机人
			fragment=AirTicketSelectPassengerFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_ADD_CONTACT://增加联系人
			fragment=AirTicketAddContactFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_CLEARINGL:
			fragment = AirTicketMainClearingFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_CREDITCARD_PAY:
			fragment = AirTicketMainCreditCardFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_SELECT_CONTACT:
			fragment = AirTicketSelectContactFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS:
			fragment = AirTicketOrderPaySuccessFragment.newInstance(data);
			break;
			
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_HISTORY_LIST:
			fragment = AirTicketOrderHistoryListFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_HISTORY_LISTITEM:
			fragment = AirTicketOrderHistoryListItemFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_FORM_WANGFAN:
			fragment = AirTicketOrderFormWangfanFragment.newInstance(data);
			break;
		case UIConstantDefault.UI_CONSTANT_CREDITCARD_PAY://统一的信用卡支付页面
			Intent intent = new Intent();
			intent.putExtras(data);
			intent.putExtra(CreditCard.PAY_KEY, data.getInt("pay_key"));//支付业务类型
			intent.putExtra(CreditCard.CARDNO, data.getString("bankno"));//付款卡号
			intent.putExtra(CreditCard.PAYMONEY, data.getString("paymonery"));//支付金额
			fragment = CommonEasyCreditcardPayFragment.newInstance(intent);
			break;
		case UIConstantDefault.UI_CONSTANT_AIR_TICKET_QUERY_FANCHENG://机票查询(返程)
			fragment = AirTicketMainQueryFanchengFragment.newInstance(data);
			break;

		/**
		 * 	酒店预订 开始
		 */
		case UIConstantDefault.UI_CONSTANT_HOTEL:
			fragment = HotelMainPagerFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_DATE:
			fragment = HotelSelectDateFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_CITY:
			fragment = HotelSelectCityFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_KEYWORD:
			fragment = HotelSelectKeywordFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_LIST:
			fragment = HotelListFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_DETAIL:
			fragment = HotelDetailFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_DESCRIPTION:
			fragment = HotelDescriptionFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_ORDER:
			fragment = HotelOrderFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_PAY_CONFIRM:
			fragment = HotelPayConfirmFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_HOTEL_ORDER_HISTORY_LIST:
			fragment = HotelPayRecordFragment.instance("");
			break;	
		/**
		 * 	酒店预订 结束
		 */
			
		/**
		 * 	购买汇通卡 start
		 */
		case UIConstantDefault.UI_CONSTANT_BUY_HTBCARD_MAIN:
			fragment = new HTBBuyMainFragment(data);
			break;	
			
		case UIConstantDefault.UI_CONSTANT_ORDER_PAY://支付
			fragment = PayMainFragment.newInstance(data);
			break;	
			
		case UIConstantDefault.UI_CONSTANT_BUY_HTBCARD_ORDER_PAY_SUCCESS://支付成功
			fragment = HTBBuySuccessFragment.newInstance(data);
			break;	
		/**
		 * 	购买汇通卡 end
		 */	
			
		/**
		* 	转账 start
		*/
		case UIConstantDefault.UI_CONSTANT_TRANSFER_MAIN:
			fragment = TransferMainPagerFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_TRANSFER_SHOUKUAN_CONFIRM:
			fragment = TransferShoukuanConfirmFragment.newInstance(data);
			break;	
		case UIConstantDefault.UI_CONSTANT_TRANSFER_FUKUAN_PAY:
			fragment = TransferSwipPayFragment.newInstance(data);
			break;	
		/**
		 * 	转账 end
		 */	
			
		default:
			fragment = AirTicketMainPagerFragment.newInstance(data);
			break;
		}
		return fragment;
	}
	
	/**
     * 装载第一个fragment
     * @param targetFragment
     */
    private void loadFirstFragment(Fragment targetFragment){
    	FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
    	ft.add(R.id.func_container, targetFragment);
    	ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)/**.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)*/;
//    	ft.addToBackStack(null);
    	ft.commit();

    }
    
    /**
     * 切换Fragment
     * @param targetFragment 目标Fragment
     * @param isAddToBackStack 是否要添加到堆栈；=1 ：添加到堆栈；否则不添加到堆栈
     */
    private void switchFragment(Fragment targetFragment, int isAddToBackStack){
    	FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
    	ft.replace(R.id.func_container, targetFragment);
    	ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)/**.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)*/;
    	if(isAddToBackStack == 1) {
    		ft.addToBackStack(null);
    	}
    	ft.commit();
    }
    
    /**
   	 * 从Stack移除Fragment
   	 */
   	public void removeFragmentToStack(){
   		runOnUiThread(new Runnable() {
   			
   			@Override
   			public void run() {
   				getSupportFragmentManager().popBackStack();
//   				fragmentManager.executePendingTransactions();
   				if(getSupportFragmentManager().getBackStackEntryCount() == 0 /**|| getSupportFragmentManager().getBackStackEntryCount() == 1*/){
   					finish();
   				}else{
   				}
   			}
   		});
   		
   	}
   	
   	/**
     * 返回首页
     */
   	public void backHomeFragment(){
   		setBackButtonOnClickListener(null);
    	getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    	
    }
   	
   	
   	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		payResult(arg2);
		
		/**
		 * 酒店预订成功，点击完成，回到首页
		 */
		if(arg1==Constants.ACTIVITY_FINISH){
			finish();
		}
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
        final String str = data.getExtras().getString("pay_result");
        if(null == str){
        		return;
        }
        ResultData resultData = new ResultData();
//        resultData.putValue(ResultData.bkntno, mBankNo);
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
            resultData.putValue(ResultData.result, "success");
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            resultData.putValue(ResultData.result, "failure");
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            resultData.putValue(ResultData.result, "cancel");
        }
        
        if(fragment instanceof BuySuccessListener) {
        	((BuySuccessListener) fragment).requestBuySuccess(resultData, msg);
        }
        
        Log.i(TAG, "union pay state:"+str);
        
	}
   	
   	/** 
   	 * 用户在软键盘出现的时候，点击非EditText任一处则隐藏软键盘
   	 * 点击空白隐藏软键盘
   	 */  
   	@Override  
   	public boolean dispatchTouchEvent(MotionEvent ev)  
   	{  
   	    if (ev.getAction() == MotionEvent.ACTION_DOWN)  
   	    {  
   	  
   	        // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）  
   	        View v = getCurrentFocus();  
   	  
   	        if (isShouldHideInput(v, ev))  
   	        {  
   	            hideSoftInput(v.getWindowToken());  
   	        }  
   	    }  
   	    return super.dispatchTouchEvent(ev);  
   	}  
   	  
   	/** 
   	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏 
   	 *  
   	 * @param v 
   	 * @param event 
   	 * @return 
   	 */  
   	private boolean isShouldHideInput(View v, MotionEvent event)  
   	{  
   	    if (v != null && (v instanceof EditText))  
   	    {  
   	        int[] l = { 0, 0 };  
   	        v.getLocationInWindow(l);  
   	        int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left  
   	                + v.getWidth();  
   	        if (event.getX() > left && event.getX() < right  
   	                && event.getY() > top && event.getY() < bottom)  
   	        {  
   	            // 点击EditText的事件，忽略它。  
   	            return false;  
   	        } else  
   	        {  
   	            return true;  
   	        }  
   	    }  
   	    // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点  
   	    return false;  
   	}  
   	  
   	/** 
   	 * 多种隐藏软件盘方法的其中一种 
   	 *  
   	 * @param token 
   	 */  
   	private void hideSoftInput(IBinder token)  
   	{  
   	    if (token != null)  
   	    {  
   	        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
   	        im.hideSoftInputFromWindow(token,  
   	                InputMethodManager.HIDE_NOT_ALWAYS);  
   	    }  
   	}  
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		IMainHandlerManager.onDestroy();
		FinalBitmap.create(this).onDestroy();
	}

}
