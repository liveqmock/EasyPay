package com.inter.trade.ui.fragment;

import android.support.v4.app.Fragment;

import com.inter.trade.data.SunType;
import com.inter.trade.ui.factory.AbstractFactory;
import com.inter.trade.ui.fragment.agent.BindAgentFragment;
import com.inter.trade.ui.fragment.coupon.BuyRecordFragment;
import com.inter.trade.ui.fragment.coupon.CouponFirstFragment;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.cridet.CridetRecordFragment;
import com.inter.trade.ui.fragment.cridet.SwipCardFragment;
import com.inter.trade.ui.fragment.express.ExpressDetialFragment;
import com.inter.trade.ui.fragment.express.ExpressListFragment;
import com.inter.trade.ui.fragment.express.ExpressQueryResultFragment;
import com.inter.trade.ui.fragment.express.WebviewFragment;
import com.inter.trade.ui.fragment.help.HelpListFragment;
import com.inter.trade.ui.fragment.order.OrderPayFragment;
import com.inter.trade.ui.fragment.order.query.OrderQureyFragment;
import com.inter.trade.ui.fragment.returndaikuan.DaikuanRecordFragment;
import com.inter.trade.ui.fragment.returndaikuan.ReturnConfirmFirstFragment;
import com.inter.trade.ui.fragment.returndaikuan.ReturnDaikuanFragment;
import com.inter.trade.ui.fragment.transfer.TransferFragment;
import com.inter.trade.ui.fragment.transfer.TransferRecordFragment;
import com.inter.trade.ui.fragment.wallet.CridetRechageFragment;
import com.inter.trade.ui.fragment.wallet.DepositRechageFragment;
import com.inter.trade.ui.fragment.wallet.RechargeManagerFragment;
import com.inter.trade.ui.fragment.wallet.WalletFragment;
import com.inter.trade.util.LoginUtil;

public class FragmentFactory extends AbstractFactory {
	public static final String INDEX_KEY = "INDEX_KEY";
	
	public static final int LOGIN_FRAGMENT_INDEX=-1;
	public static final int LEFT_FRAGMENT_INDEX_START=100;
	public static final int FRAGMENT_COMMON_START=1000;
	
	public static final int WALLET_INDEX=3;
	
	public static final int LEFT_USERINFO_INDEX=LEFT_FRAGMENT_INDEX_START+1;
	
	public static final int LEFT_PWD_INDEX=LEFT_FRAGMENT_INDEX_START+2;
	public static final int LEFT_OPEN_INDEX=LEFT_FRAGMENT_INDEX_START+3;
	public static final int LEFT_FEEDBACK_INDEX=LEFT_FRAGMENT_INDEX_START+4;
	public static final int LEFT_UPDATE_INDEX=LEFT_FRAGMENT_INDEX_START+5;
	public static final int LEFT_ABOUT_INDEX=LEFT_FRAGMENT_INDEX_START+6;
	public static final int LEFT_HELP_INDEX=LEFT_FRAGMENT_INDEX_START+7;
	public static final int LEFT_MORE_INDEX=LEFT_FRAGMENT_INDEX_START+8;
	/**
	 * 绑定代理
	 */
	public static final int LEFT_BIND_AGENT_INDEX=LEFT_FRAGMENT_INDEX_START+9;
	
	public static final int LEFT_DOWNLOAD=LEFT_FRAGMENT_INDEX_START+10;//下载通付宝
	public static final int LEFT_APP_RECOMMEND=LEFT_FRAGMENT_INDEX_START+11;//应用推荐
	
	
	public static final int BUY_RECORD_INDEX=FRAGMENT_COMMON_START+1;
	public static final int RechageFragment_INDEX=FRAGMENT_COMMON_START+2;
	public static final int CridetRechageFragment_INDEX = FRAGMENT_COMMON_START+3;
	public static final int DepositRechageFragment_INDEX = FRAGMENT_COMMON_START+4;
	public static final int Cridet_RECORD_INDEX=FRAGMENT_COMMON_START+5;
	public static final int ReturnConfirmFirstFragment_INDEX=FRAGMENT_COMMON_START+6;
	public static final int DAIKUAN_RECORD_INDEX=FRAGMENT_COMMON_START+7;
	public static final int TRANSFER_RECORD_INDEX=FRAGMENT_COMMON_START+8;
	public static final int ORDER_PAY_INDEX=FRAGMENT_COMMON_START+9;
	public static final int PAY_QUERY_METHOD_INDEX=FRAGMENT_COMMON_START+10;
	public static final int EXPRESS_QUERY_INDEX=FRAGMENT_COMMON_START+11;
	public static final int EXPRESS_WEBVIEW_INDEX=FRAGMENT_COMMON_START+12;
	public static final int EXPRESS_RESULT_INDEX=FRAGMENT_COMMON_START+13;
	public static final int EXPRESS_LIST_INDEX=FRAGMENT_COMMON_START+14;
	public static final int PROTOCOL_LIST_INDEX=FRAGMENT_COMMON_START+15;
	public static final int CRIDET_SWAP_INDEX=FRAGMENT_COMMON_START+16;
	
	private  static FragmentFactory mFactory;
	public static FragmentFactory create(){
		if(mFactory==null){
			mFactory= new FragmentFactory();
		}
		return mFactory;
	}
	public static int current_index=0;//当前界面索引
	/**
	 * 根据索引创建fragment
	 * @param index
	 * @return
	 */
	public  Fragment createFragment(int index,SunType params){
		Fragment fragment = null;
		fragment = createUncheckFragment(index);
		if(fragment !=null){
			return fragment;
		}
		if(!LoginUtil.isLogin){
			fragment = new LoginFragment();
			current_index = index;
			return fragment;
		}
		//创建普通功能界面
		if(index>FRAGMENT_COMMON_START){
			fragment = createCommonFragment(index);
		}
		//创建左侧界面
		else if(index>LEFT_FRAGMENT_INDEX_START){
			fragment = createLeftFragment(index);
		}else{
			//创建功能菜单界面
			switch (index) {
			case -1:
				fragment = new LoginFragment();
				break;
			case 0:
				fragment = new CridetCardFragment();
				break;
			case 1:
				fragment = new TransferFragment();
				break;
//			case 2:
//				fragment = new BalanceFragment();
//				break;
			case 2:
				fragment = new WalletFragment();
				break;
			case 3:
				
				fragment = new ReturnDaikuanFragment();
				break;
			case 4:
				fragment = new CouponFirstFragment();
				break;
			case 5:
				fragment = new OrderPayFragment();
				break;
			default:
				break;
			}
		}
		
		if(fragment==null){
			fragment = new LoginFragment();
		}
		
		return fragment;
	}
	
	private static Fragment createLeftFragment(int index){
		Fragment fragment= null;
		switch (index) {
		case LEFT_USERINFO_INDEX:
			fragment = new UserInfoFragment();
			break;
		case LEFT_PWD_INDEX:
			fragment= new PwdManagerFragment();
					break;
		case LEFT_OPEN_INDEX:
			fragment = new ActiveKeyFragment();
			break;
		case LEFT_FEEDBACK_INDEX:
			fragment = new FeedbackFragment();
			break;
		case LEFT_UPDATE_INDEX:
			
			break;
		case LEFT_ABOUT_INDEX:
			fragment = new AboutFragment();
			break;
		case LEFT_HELP_INDEX:
			fragment = new HelpListFragment();
			break;
		case LEFT_MORE_INDEX:
			fragment = new MoreFragment();
			break;
		case LEFT_BIND_AGENT_INDEX:
			fragment = new BindAgentFragment();
			break;
		case LEFT_DOWNLOAD:
			fragment = new DownloadFragment();
			break;
		case LEFT_APP_RECOMMEND://应用推荐
			fragment = new AppRecommendFragment();
			break;
		default:
			break;
		}
		return fragment;
	}
	
	
	private static Fragment createCommonFragment(int index){
		Fragment fragment= null;
		switch (index) {
		case BUY_RECORD_INDEX:
			fragment = new BuyRecordFragment();
			break;
		case RechageFragment_INDEX:
			fragment = new RechargeManagerFragment();
			break;
		case CridetRechageFragment_INDEX:
			fragment = new CridetRechageFragment();
			break;
		case DepositRechageFragment_INDEX:
			fragment = new DepositRechageFragment();
			break;
		case Cridet_RECORD_INDEX:
			fragment = new DaikuanRecordFragment();
			break;
		case ReturnConfirmFirstFragment_INDEX:
			fragment = new ReturnConfirmFirstFragment();
			break;
		case TRANSFER_RECORD_INDEX:
			fragment = new TransferRecordFragment();
			break;
		case DAIKUAN_RECORD_INDEX:
			fragment = new CridetRecordFragment();
			break;
		case ORDER_PAY_INDEX:
			fragment = new OrderPayFragment();
			break;
		case PAY_QUERY_METHOD_INDEX:
			fragment = new OrderQureyFragment();
			break;
		case EXPRESS_QUERY_INDEX:
			fragment = new ExpressDetialFragment();
				break;
		case EXPRESS_WEBVIEW_INDEX:
			fragment = new WebviewFragment();
			break;
		case EXPRESS_RESULT_INDEX:
			fragment = new ExpressQueryResultFragment();
			break;
		case EXPRESS_LIST_INDEX:
			fragment = new ExpressListFragment();
			break;
		case PROTOCOL_LIST_INDEX:
			fragment = new ProtocolListFragment();
			break;
		case CRIDET_SWAP_INDEX:
			fragment = new SwipCardFragment();
			break;
		default:
			break;
		}
		return fragment;
	}
	
	/**
	 * 创建不需要登录即可查看的界面
	 * @param index
	 * @return
	 */
	private static Fragment createUncheckFragment(int index){
		Fragment fragment= null;
		switch (index) {
		case LEFT_MORE_INDEX:
			fragment = new MoreFragment();
			break;
		case PROTOCOL_LIST_INDEX:
			fragment = new ProtocolListFragment();
			break;
		}
		return fragment;
	}
}
