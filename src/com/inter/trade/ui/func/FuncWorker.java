package com.inter.trade.ui.func;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.inter.trade.data.SunType;
import com.inter.trade.ui.factory.AbstractFactory;
import com.inter.trade.ui.fragment.LoginFragment;
import com.inter.trade.ui.fragment.balance.BanlanceMainFragment;
import com.inter.trade.ui.fragment.buylicensekey.BuyLicenseKeyMainFragment;
import com.inter.trade.ui.fragment.buyswipcard.BuySwipCardPayMainFragment;
import com.inter.trade.ui.fragment.coupon.CouponFirstFragment;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.express.ExpressGridFragment;
import com.inter.trade.ui.fragment.gamerecharge.GameRechargeMainFragment;
import com.inter.trade.ui.fragment.order.OrderPayFragment;
import com.inter.trade.ui.fragment.order.query.OrderQureyFragment;
import com.inter.trade.ui.fragment.returndaikuan.ReturnDaikuanFragment;
import com.inter.trade.ui.fragment.shoppingmall.ShoppingMallMainFragment;
import com.inter.trade.ui.fragment.smsreceivepayment.SmsReceivePaymentMainFragment;
import com.inter.trade.ui.fragment.qmoney.QMoneyPayMainFragment;
import com.inter.trade.ui.fragment.telephone.TelephonePayMainFragment;
import com.inter.trade.ui.fragment.tfbcard.TFBcardMainFragment;
import com.inter.trade.ui.fragment.agent.AgentApplyFragmentNew;
import com.inter.trade.ui.fragment.agent.AgentMainFragment;
import com.inter.trade.ui.fragment.agent.AgentMainContentFragment;
import com.inter.trade.ui.fragment.agent.AgentMainFragment2;
import com.inter.trade.ui.fragment.transfer.TransfeiMainFragment;
import com.inter.trade.ui.fragment.transfer.TransferFragment;
import com.inter.trade.ui.fragment.wallet.WalletFragment;
import com.inter.trade.ui.fragment.waterelectricgas.WaterElectricGasMainFragment;
import com.inter.trade.util.LoginUtil;

public class FuncWorker extends AbstractFactory{
	public  Fragment createFragment(int index,SunType params){
		Fragment fragment=null;
		
		//如果是点击代理商图标，不需要常规登录，根据代理商专用登录状态跳到代理商页面
//		if(index == FuncMap.AGENT_INDEX_FUNC){
//			if(LoginUtil.isAgentLogin){
//				fragment = new AgentMainContentFragment();
//			}else{
//				fragment = new AgentMainFragment();
//			}
//			return fragment;
//		}
		
		if(!LoginUtil.isLogin){
			fragment = new LoginFragment();
			return fragment;
		}
		switch (index) {
		case FuncMap.CRIDET_INDEX_FUNC:
			fragment = new CridetCardFragment();
			break;
		case FuncMap.TRANSFER_INDEX_FUNC:
//			fragment = new TransferFragment();
			fragment = new TransfeiMainFragment();
			break;
//		case 2:
//			fragment = new BalanceFragment();
//			break;
		case FuncMap.WALLET_INDEX_FUNC:
			fragment = new WalletFragment();
			break;
		case FuncMap.RETURN_DAIKUAN_INDEX_FUNC:
			
			fragment = new ReturnDaikuanFragment();
			break;
		case FuncMap.COUPON_INDEX_FUNC:
			fragment = new CouponFirstFragment();
			break;
		case FuncMap.ORDER_INDEX_FUNC:
			fragment = new OrderPayFragment();
			break;
		case FuncMap.ORDER_QUERY_INDEX_FUNC:
			fragment = OrderQureyFragment.create("query");
			break;
		case FuncMap.EXPRESS_INDEX_FUNC:
			fragment = new ExpressGridFragment();
			break;
		case FuncMap.BALANCE_INDEX_FUNC:
			fragment = new BanlanceMainFragment();
			break;
		case FuncMap.TELEPHONE_INDEX_FUNC:
			fragment = new TelephonePayMainFragment();
			break;
		case FuncMap.QMONEY_INDEX_FUNC:
			fragment = new QMoneyPayMainFragment();
			break;
		case FuncMap.FIXPHONE_INDEX_FUNC:
			fragment = new TelephonePayMainFragment();
			break;
		case FuncMap.ELECTRICCHARGE_INDEX_FUNC:
			fragment = new TelephonePayMainFragment();
			break;
		case FuncMap.BUY_SWIPCARD_INDEX_FUNC:
			/**
			 * 购买刷卡器
			 */
			fragment = new BuySwipCardPayMainFragment();
			
			/**
			 * 购买授权码
			 */
//			fragment = new BuyLicenseKeyMainFragment();
			
			break;
		case FuncMap.AGENT_INDEX_FUNC:
			fragment = new AgentMainFragment2();//AgentApplyFragmentNew();//AgentMainContentFragment();
			break;
		case FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC:
			fragment = new WaterElectricGasMainFragment();
			break;
		case FuncMap.GAME_RECHARGE_INDEX_FUNC:
			fragment = new GameRechargeMainFragment();
			break;
		case FuncMap.TFBCARD_INDEX_FUNC:
			fragment=new TFBcardMainFragment();
			break;
		case FuncMap.SAVINGSCARD_INDEX_FUNC:
			/**
			 * 付临门
			 */
			Bundle data = new Bundle();
			data.putString("paytype", CridetCardFragment.PAYTYPE_FULINMEN);
			fragment= CridetCardFragment.newInstance(data);
			break;
		case FuncMap.SMSRECEIPT_INDEX_FUNC:
			/**
			 * 短信收款，测试
			 */
			fragment = new SmsReceivePaymentMainFragment();
			break;
		case  FuncMap.SUNINGMALL_INDEX_FUNC://手机购物
			fragment=new ShoppingMallMainFragment();
			break;
		case FuncMap.TELEPHONE_INDEX_FUNC_GUIDE://从引导页启动
			fragment = TelephonePayMainFragment.getInstance(true);
			break;
		default:
			break;
		}
		return fragment;
	}
}
